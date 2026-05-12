import { Direction, getDirectionDelta, TileType, ObjectType } from './types';
import { LevelMap, Player, createPlayer, createEmptyMap, createMapFromArray, Tile, GameObj, createTile, createObject } from './entity';
import { bobbyRules, canWalkOnTile, getTileEnterEffects, getObjectCollideEffects } from './rules';
import { CollisionSystem } from './systems/CollisionSystem';
import { CollectionSystem } from './systems/CollectionSystem';
import { MovementSystem } from './systems/MovementSystem';
import { loadGameAssets, GameAssets, AudioManager } from './assets';

// 游戏常量 - 必须与原始J2ME游戏一致
const TILE_SIZE = 32; // 原始tile尺寸
const PLAYER_WIDTH = 32;  // 每帧宽度（精灵图每帧32宽）
const PLAYER_HEIGHT = 48; // 完整精灵高度（包含脚部）
const MOVE_DURATION = 200;
const SCALE = 2; // 放大倍数

// 关卡数据接口
interface LevelData {
  id: number;
  packId: number;
  name: string;
  width: number;
  height: number;
  cells: Array<Array<{
    tile: { type: TileType } | null;
    object: { type: ObjectType } | null;
  }>>;
  startPosition: { x: number; y: number };
  target: { carrots: number };
}

export class Game {
  private canvas: HTMLCanvasElement;
  private ctx: CanvasRenderingContext2D;
  private assets: GameAssets | null = null;
  private audioManager: AudioManager;

  private levelMap: LevelMap | null = null;
  private player: Player | null = null;
  private playerPixelX: number = 0;
  private playerPixelY: number = 0;

  private collisionSystem: CollisionSystem;
  private collectionSystem: CollectionSystem;
  private movementSystem: MovementSystem;

  private carrotsCollected: number = 0;
  private targetCarrots: number = 3;
  private isLevelComplete: boolean = false;
  private isMoving: boolean = false;
  private playerDirection: Direction = 'down';
  private animFrame: number = 0;
  private animCounter: number = 0;

  private gameState: 'loading' | 'title' | 'playing' | 'complete' = 'loading';
  private currentLevel: LevelData | null = null;

  constructor() {
    this.canvas = document.getElementById('gameCanvas') as HTMLCanvasElement;
    this.ctx = this.canvas.getContext('2d')!;
    this.audioManager = new AudioManager();

    this.collisionSystem = new CollisionSystem(bobbyRules);
    this.collectionSystem = new CollectionSystem();
    this.movementSystem = new MovementSystem();

    this.init();
  }

  private async init(): Promise<void> {
    await this.loadAssets();
    this.setupEventListeners();
    this.gameLoop();
  }

  private async loadAssets(): Promise<void> {
    const loadingScreen = document.getElementById('loading-screen')!;
    const loadingProgress = document.getElementById('loading-progress')!;

    try {
      loadingProgress.style.width = '30%';
      await new Promise(r => setTimeout(r, 200));

      this.assets = await loadGameAssets();
      loadingProgress.style.width = '100%';
      await new Promise(r => setTimeout(r, 300));

      loadingScreen.classList.add('hidden');
      this.showTitleScreen();
    } catch (error) {
      console.error('Failed to load assets:', error);
      document.getElementById('loading-text')!.textContent = '加载失败，请刷新重试';
    }
  }

  private showTitleScreen(): void {
    this.gameState = 'title';
    const titleScreen = document.getElementById('title-screen')!;
    titleScreen.classList.remove('hidden');

    if (this.assets) {
      const logoImg = document.getElementById('logo-img') as HTMLImageElement;
      logoImg.src = this.assets.logo.src;
      logoImg.style.display = 'block';
    }
  }

  private setupEventListeners(): void {
    document.getElementById('start-button')!.addEventListener('click', () => {
      this.startGame();
    });

    document.addEventListener('keydown', (e) => {
      if (this.gameState !== 'playing') return;

      const keyMap: Record<string, Direction> = {
        'ArrowUp': 'up', 'w': 'up', 'W': 'up',
        'ArrowDown': 'down', 's': 'down', 'S': 'down',
        'ArrowLeft': 'left', 'a': 'left', 'A': 'left',
        'ArrowRight': 'right', 'd': 'right', 'D': 'right'
      };
      const dir = keyMap[e.key];
      if (dir) {
        e.preventDefault();
        this.handleDirection(dir);
      }
    });

    let touchStartX = 0;
    let touchStartY = 0;
    const minSwipe = 30;

    this.canvas.addEventListener('touchstart', (e) => {
      e.preventDefault();
      touchStartX = e.touches[0].clientX;
      touchStartY = e.touches[0].clientY;
      this.showVirtualPad();
    }, { passive: false });

    this.canvas.addEventListener('touchend', (e) => {
      e.preventDefault();
      const dx = e.changedTouches[0].clientX - touchStartX;
      const dy = e.changedTouches[0].clientY - touchStartY;

      if (Math.abs(dx) > minSwipe || Math.abs(dy) > minSwipe) {
        if (Math.abs(dx) > Math.abs(dy)) {
          this.handleDirection(dx > 0 ? 'right' : 'left');
        } else {
          this.handleDirection(dy > 0 ? 'down' : 'up');
        }
      }
    }, { passive: false });

    this.setupVirtualPad();
  }

  private showVirtualPad(): void {
    const pad = document.getElementById('virtual-pad')!;
    pad.classList.add('visible');
  }

  private setupVirtualPad(): void {
    const padButtons = {
      'pad-up': 'up' as Direction,
      'pad-down': 'down' as Direction,
      'pad-left': 'left' as Direction,
      'pad-right': 'right' as Direction
    };

    Object.entries(padButtons).forEach(([id, dir]) => {
      const btn = document.getElementById(id)!;
      btn.addEventListener('touchstart', (e) => {
        e.preventDefault();
        this.handleDirection(dir);
      });
      btn.addEventListener('click', () => {
        this.handleDirection(dir);
      });
    });
  }

  private async startGame(): void {
    document.getElementById('title-screen')!.classList.add('hidden');
    document.getElementById('game-screen')!.classList.add('active');
    this.gameState = 'playing';

    try {
      this.currentLevel = await this.loadLevelData('/assets/levels/level-1-1.json');
      this.loadLevel(this.currentLevel);
      this.audioManager.playBGM('ingame1');
    } catch (error) {
      console.error('Failed to load level:', error);
    }
  }

  private async loadLevelData(levelPath: string): Promise<LevelData> {
    const response = await fetch(levelPath);
    if (!response.ok) {
      throw new Error(`Failed to load level: ${response.status}`);
    }
    return response.json();
  }

  private loadLevel(levelData: LevelData): void {
    const cells: Array<Array<{ tile: Tile | null; object: GameObj | null }>> = [];
    for (let y = 0; y < levelData.height; y++) {
      cells[y] = [];
      for (let x = 0; x < levelData.width; x++) {
        const cellData = levelData.cells[y]?.[x];
        cells[y][x] = {
          tile: cellData?.tile ? createTile(cellData.tile.type) : createTile('ground'),
          object: cellData?.object ? createObject(cellData.object.type) : null
        };
      }
    }
    this.levelMap = createMapFromArray(cells);

    this.player = createPlayer(
      levelData.startPosition.x,
      levelData.startPosition.y,
      'down' as Direction
    );
    this.playerPixelX = this.player.x * TILE_SIZE * SCALE;
    this.playerPixelY = this.player.y * TILE_SIZE * SCALE;
    this.playerDirection = 'down';

    this.carrotsCollected = 0;
    this.targetCarrots = levelData.target.carrots;
    this.isLevelComplete = false;

    this.canvas.width = levelData.width * TILE_SIZE * SCALE;
    this.canvas.height = levelData.height * TILE_SIZE * SCALE;

    this.updateHUD();
  }

  private handleDirection(direction: Direction): void {
    if (!this.levelMap || !this.player || this.isMoving || this.isLevelComplete) return;

    this.isMoving = true;
    this.playerDirection = direction;

    const result = this.collisionSystem.checkMovement(
      this.levelMap,
      this.player,
      direction
    );

    if (result.canMove) {
      this.animateMove(direction, result.effects);
    } else {
      this.isMoving = false;
    }
  }

  private async animateMove(direction: Direction, effects: any[]): Promise<void> {
    if (!this.player || !this.levelMap) return;

    const delta = getDirectionDelta(direction);
    const startX = this.playerPixelX;
    const startY = this.playerPixelY;
    const endX = (this.player.x + delta.dx) * TILE_SIZE * SCALE;
    const endY = (this.player.y + delta.dy) * TILE_SIZE * SCALE;

    this.player.x += delta.dx;
    this.player.y += delta.dy;

    const startTime = performance.now();
    const duration = MOVE_DURATION;

    const animate = (time: number) => {
      const elapsed = time - startTime;
      const t = Math.min(elapsed / duration, 1);
      const easeT = 1 - Math.pow(1 - t, 3);

      this.playerPixelX = startX + (endX - startX) * easeT;
      this.playerPixelY = startY + (endY - startY) * easeT;
      this.animFrame = Math.floor((elapsed / 50) % 4);

      if (t < 1) {
        requestAnimationFrame(animate);
      } else {
        this.playerPixelX = endX;
        this.playerPixelY = endY;
        this.afterMove(effects);
      }
    };

    requestAnimationFrame(animate);
  }

  private afterMove(effects: any[]): void {
    if (!this.levelMap || !this.player) return;

    const collectResult = this.collectionSystem.tryCollect(this.levelMap, this.player);
    if (collectResult.collected) {
      this.carrotsCollected++;
      this.updateHUD();
      this.audioManager.playSFX('collect');
    }

    for (const effect of effects) {
      if (effect.type === 'die') {
        this.player.state = 'dying';
        this.audioManager.playSFX('death');
        this.resetLevel();
        return;
      }
    }

    if (this.collisionSystem.checkLevelComplete(
      this.levelMap,
      this.player,
      this.carrotsCollected,
      this.targetCarrots
    )) {
      this.isLevelComplete = true;
      this.gameState = 'complete';
      this.audioManager.stopBGM();
      this.audioManager.playSFX('cleared');
      this.showCompleteScreen();
    }

    this.isMoving = false;
  }

  private resetLevel(): void {
    setTimeout(() => {
      if (this.currentLevel) {
        this.loadLevel(this.currentLevel);
      }
      this.isMoving = false;
    }, 500);
  }

  private showCompleteScreen(): void {
    const hud = document.getElementById('hud-carrot-count')!;
    hud.textContent = '关卡完成！';
  }

  private updateHUD(): void {
    document.getElementById('hud-carrot-count')!.textContent = `${this.carrotsCollected} / ${this.targetCarrots}`;
  }

  private gameLoop = (): void => {
    this.render();
    this.animCounter++;
    requestAnimationFrame(this.gameLoop);
  };

  private render(): void {
    if (!this.levelMap || !this.assets) return;

    const ctx = this.ctx;
    ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

    for (let y = 0; y < this.levelMap.height; y++) {
      for (let x = 0; x < this.levelMap.width; x++) {
        const cell = this.levelMap.cells[y][x];
        this.renderTile(ctx, x, y, cell.tile);
      }
    }

    for (let y = 0; y < this.levelMap.height; y++) {
      for (let x = 0; x < this.levelMap.width; x++) {
        const cell = this.levelMap.cells[y][x];
        this.renderObject(ctx, x, y, cell.object);
      }
    }

    this.renderPlayer(ctx);
  }

  private renderTile(ctx: CanvasRenderingContext2D, x: number, y: number, tile: Tile | null): void {
    const px = x * TILE_SIZE * SCALE;
    const py = y * TILE_SIZE * SCALE;
    const size = TILE_SIZE * SCALE;

    if (!tile) {
      ctx.fillStyle = '#2d5a27';
      ctx.fillRect(px, py, size, size);
      return;
    }

    if (this.assets) {
      const tileIndex = this.getTileIndex(tile.type);
      const tileX = (tileIndex % 16) * TILE_SIZE;
      const tileY = Math.floor(tileIndex / 16) * TILE_SIZE;

      ctx.drawImage(
        this.assets.tiles,
        tileX, tileY, TILE_SIZE, TILE_SIZE,
        px, py, size, size
      );
      return;
    }

    const colors: Record<TileType, string> = {
      'ground': '#2d5a27',
      'ice': '#a8d8ea',
      'conveyer': '#4a4a6a',
      'arrow': '#6a5a4a',
      'death': '#8b0000',
      'grass': '#1a4a1a',
      'portal': '#8a2be2',
      'water': '#1a4a7a',
      'wall': '#4a3a2a'
    };

    ctx.fillStyle = colors[tile.type] || '#2d5a27';
    ctx.fillRect(px, py, size, size);
  }

  private getTileIndex(type: TileType): number {
    // 精灵图索引映射 - 基于原始J2ME游戏的byte值（转无符号）
    // ts.png是512x512，每行16个32x32 tile，共256个tile
    // srcX = (tileValue % 16) * 32, srcY = (tileValue / 16) * 32
    const indices: Partial<Record<TileType, number>> = {
      'ground': 100,        // 基础可行走地面（94-200范围内）
      'wall': 0,            // 墙壁/障碍（不可行走）
      'water': 77,          // 水域（不可行走，特殊检查）
      'ice': 148,           // 冰面 TILE_ICE = -108 → 148
      'grass': 199,         // 可割草地 TILE_GRASS_1 = -57 → 199
      'death': 175,         // 死亡触发 TILE_DEATH_TRIGGER = -81 → 175
      'portal': 166,        // 传送门 TILE_PORTAL_1 = -90 → 166
      'arrow': 184,         // 箭头 TILE_ARROW_RIGHT = -72 → 184
      'conveyer': 190       // 传送带 TILE_CONVEYOR_RIGHT = -66 → 190
    };
    return indices[type] ?? 100;
  }

  private getObjectIndex(type: ObjectType): number {
    // J2ME对象byte值转无符号索引 - 与ts.png共用同一精灵图
    // 索引直接对应ts.png位置：srcX = (index % 16) * 32, srcY = (index / 16) * 32
    const indices: Partial<Record<ObjectType, number>> = {
      'start': 149,      // TILE_SPAWN_POINT = -107 → 149
      'carrot': 248,     // OBJECT_CARROT = -8 → 248
      'door': 246,       // OBJECT_LEVEL_END = -10 → 246
      'seed': 244,       // OBJECT_SEED = -12 → 244
      'spring': 212,     // OBJECT_SPRING = -44 → 212
      'flight': 245,     // OBJECT_FLIGHT_PICKUP = -11 → 245
      'bonus': 205,      // OBJECT_BONUS_DOOR = -51 → 205
      'rock': 240,       // 估计值
      'mower': 237       // OBJECT_MOWER_PATH = -19 → 237
    };
    return indices[type] ?? 0;
  }

  private renderObject(ctx: CanvasRenderingContext2D, x: number, y: number, obj: GameObj | null): void {
    if (!obj || obj.visible === false) return;

    const px = x * TILE_SIZE * SCALE;
    const py = y * TILE_SIZE * SCALE;

    if (this.assets) {
      // Objects也用ts.png渲染！与tiles共用同一精灵图
      const objIndex = this.getObjectIndex(obj.type);
      const objX = (objIndex % 16) * TILE_SIZE;  // ts.png每行16个
      const objY = Math.floor(objIndex / 16) * TILE_SIZE;

      ctx.drawImage(
        this.assets.tiles,  // 用tiles精灵图，不是tileObjects
        objX, objY, TILE_SIZE, TILE_SIZE,
        px, py, TILE_SIZE * SCALE, TILE_SIZE * SCALE
      );
      return;
    }

    // 备用渲染（无素材时）
    const colors: Record<ObjectType, string> = {
      'carrot': '#ff6b35',
      'seed': '#daa520',
      'spring': '#228b22',
      'door': '#8b4513',
      'flight': '#00bfff',
      'bonus': '#ffd700',
      'start': '#ffd700',
      'rock': '#696969',
      'mower': '#dc143c'
    };

    const color = colors[obj.type] || '#fff';
    ctx.fillStyle = color;

    const centerX = px + (TILE_SIZE * SCALE) / 2;
    const centerY = py + (TILE_SIZE * SCALE) / 2;

    switch (obj.type) {
      case 'carrot':
        ctx.beginPath();
        ctx.moveTo(centerX, centerY - 6);
        ctx.lineTo(centerX - 4, centerY + 4);
        ctx.lineTo(centerX + 4, centerY + 4);
        ctx.closePath();
        ctx.fill();
        break;
      case 'door':
        ctx.fillRect(centerX - 6, centerY - 8, 12, 16);
        break;
      default:
        ctx.beginPath();
        ctx.arc(centerX, centerY, 5, 0, Math.PI * 2);
        ctx.fill();
    }
  }

  private renderPlayer(ctx: CanvasRenderingContext2D): void {
    if (!this.player || !this.assets) {
      console.log('renderPlayer: no player or assets');
      return;
    }

    let sprites: HTMLImageElement[];
    switch (this.playerDirection) {
      case 'left':
        sprites = this.assets.bobbyLeft;
        break;
      case 'right':
        sprites = this.assets.bobbyRight;
        break;
      case 'up':
        sprites = this.assets.bobbyUp;
        break;
      default:
        sprites = this.assets.bobbyDown;
    }

    if (!sprites || sprites.length === 0) {
      console.log('renderPlayer: no sprites for direction', this.playerDirection);
      return;
    }

    const sprite = sprites[0];
    if (!sprite || !sprite.complete) {
      console.log('renderPlayer: sprite not ready', sprite?.complete);
      return;
    }

    const frame = this.isMoving ? Math.min(this.animFrame, 7) : 0;

    const srcX = frame * PLAYER_WIDTH;
    const srcY = 0;

    const drawX = this.playerPixelX;
    const drawY = this.playerPixelY;

    console.log('renderPlayer: drawing at', drawX, drawY, 'frame', frame, 'srcX', srcX);

    ctx.drawImage(
      sprite,
      srcX, srcY, PLAYER_WIDTH, PLAYER_HEIGHT,
      drawX, drawY, PLAYER_WIDTH * SCALE, PLAYER_HEIGHT * SCALE
    );
  }
}

(window as any).__game = new Game();