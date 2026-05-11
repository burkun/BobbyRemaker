import { Direction, getDirectionDelta, TileType, ObjectType } from './types';
import { LevelMap, Player, createPlayer, createEmptyMap, createMapFromArray, Tile, GameObj, createTile, createObject } from './entity';
import { bobbyRules, canWalkOnTile, getTileEnterEffects, getObjectCollideEffects } from './rules';
import { CollisionSystem } from './systems/CollisionSystem';
import { CollectionSystem } from './systems/CollectionSystem';
import { MovementSystem } from './systems/MovementSystem';
import { loadGameAssets, GameAssets, AudioManager } from './assets';

// 游戏常量
const TILE_SIZE = 16; // J2ME原始尺寸
const PLAYER_WIDTH = 16;
const PLAYER_HEIGHT = 48;
const MOVE_DURATION = 200;
const SCALE = 2; // 放大倍数

// 关卡数据
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

// 测试关卡
const TEST_LEVEL: LevelData = {
  id: 1,
  packId: 1,
  name: "草地起点",
  width: 20,
  height: 12,
  cells: generateTestLevel(),
  startPosition: { x: 1, y: 1 },
  target: { carrots: 3 }
};

function generateTestLevel(): LevelData['cells'] {
  const cells: LevelData['cells'] = [];
  for (let y = 0; y < 12; y++) {
    cells[y] = [];
    for (let x = 0; x < 20; x++) {
      // 边界是墙
      if (x === 0 || x === 19 || y === 0 || y === 11) {
        cells[y][x] = { tile: { type: 'wall' }, object: null };
      } else {
        cells[y][x] = { tile: { type: 'ground' }, object: null };
      }
    }
  }
  // 起点标记
  cells[1][1] = { tile: { type: 'ground' }, object: { type: 'start' } };
  // 放置胡萝卜
  cells[1][3] = { tile: { type: 'ground' }, object: { type: 'carrot' } };
  cells[5][8] = { tile: { type: 'ground' }, object: { type: 'carrot' } };
  cells[8][15] = { tile: { type: 'ground' }, object: { type: 'carrot' } };
  // 放置门
  cells[10][18] = { tile: { type: 'ground' }, object: { type: 'door' } };
  // 添加一些障碍
  cells[3][5] = { tile: { type: 'water' }, object: null };
  cells[3][6] = { tile: { type: 'water' }, object: null };
  cells[4][5] = { tile: { type: 'water' }, object: null };
  cells[6][10] = { tile: { type: 'wall' }, object: null };
  cells[6][11] = { tile: { type: 'wall' }, object: null };
  cells[7][10] = { tile: { type: 'wall' }, object: null };

  return cells;
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
      // 模拟加载进度
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

    // 显示logo
    if (this.assets) {
      const logoImg = document.getElementById('logo-img') as HTMLImageElement;
      logoImg.src = this.assets.logo.src;
      logoImg.style.display = 'block';
    }
  }

  private setupEventListeners(): void {
    // 开始按钮
    document.getElementById('start-button')!.addEventListener('click', () => {
      this.startGame();
    });

    // 键盘输入
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

    // 触摸输入
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

    // 虚拟方向键
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

  private startGame(): void {
    document.getElementById('title-screen')!.classList.add('hidden');
    document.getElementById('game-screen')!.classList.add('active');
    this.gameState = 'playing';

    this.loadLevel(TEST_LEVEL);
    this.audioManager.playBGM('ingame1');
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

    // 调整画布大小
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
      this.loadLevel(TEST_LEVEL);
      this.isMoving = false;
    }, 500);
  }

  private showCompleteScreen(): void {
    const hud = document.getElementById('hud-carrot-count')!;
    hud.textContent = '🎉 关卡完成！';
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

    // 渲染地图
    for (let y = 0; y < this.levelMap.height; y++) {
      for (let x = 0; x < this.levelMap.width; x++) {
        const cell = this.levelMap.cells[y][x];
        this.renderTile(ctx, x, y, cell.tile);
      }
    }

    // 渲染对象
    for (let y = 0; y < this.levelMap.height; y++) {
      for (let x = 0; x < this.levelMap.width; x++) {
        const cell = this.levelMap.cells[y][x];
        this.renderObject(ctx, x, y, cell.object);
      }
    }

    // 渲染玩家
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

    // 尝试使用tile sheet
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

    // Fallback colors
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
    // ts.png tile索引映射
    const indices: Partial<Record<TileType, number>> = {
      'ground': 0,
      'water': 4,
      'wall': 8,
      'ice': 16,
      'grass': 32,
      'death': 48,
      'portal': 64,
      'arrow': 80,
      'conveyer': 96
    };
    return indices[type] ?? 0;
  }

  private renderObject(ctx: CanvasRenderingContext2D, x: number, y: number, obj: GameObj | null): void {
    if (!obj || obj.visible === false) return;

    const px = x * TILE_SIZE * SCALE + (TILE_SIZE * SCALE) / 2;
    const py = y * TILE_SIZE * SCALE + (TILE_SIZE * SCALE) / 2;

    // 尝试使用tile objects sheet
    if (this.assets) {
      const objIndex = this.getObjectIndex(obj.type);
      const objX = (objIndex % 8) * TILE_SIZE;
      const objY = Math.floor(objIndex / 8) * TILE_SIZE;

      ctx.drawImage(
        this.assets.tileObjects,
        objX, objY, TILE_SIZE, TILE_SIZE,
        x * TILE_SIZE * SCALE, y * TILE_SIZE * SCALE,
        TILE_SIZE * SCALE, TILE_SIZE * SCALE
      );
      return;
    }

    // Fallback
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

    switch (obj.type) {
      case 'carrot':
        ctx.beginPath();
        ctx.moveTo(px, py - 6);
        ctx.lineTo(px - 4, py + 4);
        ctx.lineTo(px + 4, py + 4);
        ctx.closePath();
        ctx.fill();
        break;
      case 'door':
        ctx.fillRect(px - 6, py - 8, 12, 16);
        break;
      default:
        ctx.beginPath();
        ctx.arc(px, py, 5, 0, Math.PI * 2);
        ctx.fill();
    }
  }

  private getObjectIndex(type: ObjectType): number {
    const indices: Partial<Record<ObjectType, number>> = {
      'start': 0,
      'carrot': 8,
      'door': 16,
      'seed': 24,
      'spring': 32,
      'flight': 40,
      'bonus': 48,
      'rock': 56,
      'mower': 64
    };
    return indices[type] ?? 0;
  }

  private renderPlayer(ctx: CanvasRenderingContext2D): void {
    if (!this.player || !this.assets) return;

    // 选择正确的精灵图
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

    const sprite = sprites[0];
    const frame = this.isMoving ? this.animFrame : 0;

    // 精灵图中每帧16像素宽，48像素高
    const srcX = frame * PLAYER_WIDTH;
    const srcY = 0;

    // 居中绘制玩家
    const drawX = this.playerPixelX - (PLAYER_WIDTH * SCALE - TILE_SIZE * SCALE) / 2;
    const drawY = this.playerPixelY - PLAYER_HEIGHT * SCALE + TILE_SIZE * SCALE;

    ctx.drawImage(
      sprite,
      srcX, srcY, PLAYER_WIDTH, PLAYER_HEIGHT,
      drawX, drawY, PLAYER_WIDTH * SCALE, PLAYER_HEIGHT * SCALE
    );
  }
}

// 启动游戏
new Game();