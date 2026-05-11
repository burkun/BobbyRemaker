import { Direction, getDirectionDelta, TileType, ObjectType } from './types';
import { LevelMap, Player, createPlayer, createEmptyMap, createMapFromArray, LevelConfig, createLevelConfig, Tile, GameObj, createTile, createObject } from './entity';
import { bobbyRules, canWalkOnTile, getTileEnterEffects, getObjectCollideEffects } from './rules';
import { CollisionSystem } from './systems/CollisionSystem';
import { CollectionSystem } from './systems/CollectionSystem';
import { MovementSystem } from './systems/MovementSystem';

// 游戏常量
const TILE_SIZE = 32;
const PLAYER_SIZE = 28;
const MOVE_DURATION = 150; // ms

// 颜色配置
const COLORS = {
  ground: '#2d5a27',
  water: '#1a4a7a',
  wall: '#4a3a2a',
  carrot: '#ff6b35',
  player: '#f5f5dc',
  door: '#8b4513',
  start: '#ffd700',
  text: '#ffffff'
};

interface GameLevel {
  id: number;
  name: string;
  width: number;
  height: number;
  cells: Array<Array<{
    tile: { type: TileType } | null;
    object: { type: ObjectType } | null;
  }>>;
  startPosition: { x: number; y: number };
}

// 测试关卡数据
const TEST_LEVEL: GameLevel = {
  id: 1,
  name: "草地起点",
  width: 10,
  height: 8,
  cells: [
    [{ tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }],
    [{ tile: { type: 'ground' }, object: { type: 'start' } }, { tile: { type: 'ground' }, object: { type: 'carrot' } }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'water' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }],
    [{ tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'water' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'wall' }, object: null }, { tile: { type: 'wall' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }],
    [{ tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'wall' }, object: null }, { tile: { type: 'ground' }, object: { type: 'carrot' } }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }],
    [{ tile: { type: 'ground' }, object: null }, { tile: { type: 'water' }, object: null }, { tile: { type: 'water' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }],
    [{ tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'water' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }],
    [{ tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'water' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: { type: 'door' } }],
    [{ tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: { type: 'carrot' } }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }, { tile: { type: 'ground' }, object: null }]
  ],
  startPosition: { x: 0, y: 1 }
};

export class Game {
  private canvas: HTMLCanvasElement;
  private ctx: CanvasRenderingContext2D;
  private hud: HTMLElement;

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

  constructor() {
    this.canvas = document.getElementById('gameCanvas') as HTMLCanvasElement;
    this.ctx = this.canvas.getContext('2d')!;
    this.hud = document.getElementById('hud')!;

    this.collisionSystem = new CollisionSystem(bobbyRules);
    this.collectionSystem = new CollectionSystem();
    this.movementSystem = new MovementSystem();

    this.loadLevel(TEST_LEVEL);
    this.setupInput();
    this.gameLoop();
  }

  private loadLevel(levelData: GameLevel): void {
    // 创建地图
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

    // 创建玩家
    this.player = createPlayer(
      levelData.startPosition.x,
      levelData.startPosition.y,
      'down' as Direction
    );
    this.playerPixelX = this.player.x * TILE_SIZE + (TILE_SIZE - PLAYER_SIZE) / 2;
    this.playerPixelY = this.player.y * TILE_SIZE + (TILE_SIZE - PLAYER_SIZE) / 2;

    this.carrotsCollected = 0;
    this.isLevelComplete = false;
    this.updateHUD();
  }

  private setupInput(): void {
    // 键盘输入
    document.addEventListener('keydown', (e) => {
      const keyMap: Record<string, Direction> = {
        'ArrowUp': 'up', 'w': 'up', 'W': 'up',
        'ArrowDown': 'down', 's': 'down', 'S': 'down',
        'ArrowLeft': 'left', 'a': 'left', 'A': 'left',
        'ArrowRight': 'right', 'd': 'right', 'D': 'right'
      };
      const dir = keyMap[e.key];
      if (dir) {
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
  }

  private handleDirection(direction: Direction): void {
    if (!this.levelMap || !this.player || this.isMoving || this.isLevelComplete) return;

    this.isMoving = true;

    const result = this.collisionSystem.checkMovement(
      this.levelMap,
      this.player,
      direction
    );

    this.player.direction = direction;

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
    const endX = (this.player.x + delta.dx) * TILE_SIZE + (TILE_SIZE - PLAYER_SIZE) / 2;
    const endY = (this.player.y + delta.dy) * TILE_SIZE + (TILE_SIZE - PLAYER_SIZE) / 2;

    this.player.x += delta.dx;
    this.player.y += delta.dy;

    const startTime = performance.now();
    const duration = MOVE_DURATION;

    const animate = (time: number) => {
      const elapsed = time - startTime;
      const t = Math.min(elapsed / duration, 1);
      const easeT = 1 - Math.pow(1 - t, 3); // ease-out

      this.playerPixelX = startX + (endX - startX) * easeT;
      this.playerPixelY = startY + (endY - startY) * easeT;

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

    // 收集物品
    const collectResult = this.collectionSystem.tryCollect(this.levelMap, this.player);
    if (collectResult.collected) {
      this.carrotsCollected++;
      this.updateHUD();
    }

    // 处理效果
    for (const effect of effects) {
      if (effect.type === 'die') {
        this.player.state = 'dying';
        this.resetLevel();
        return;
      }
    }

    // 检查关卡完成
    if (this.collisionSystem.checkLevelComplete(
      this.levelMap,
      this.player,
      this.carrotsCollected,
      this.targetCarrots
    )) {
      this.isLevelComplete = true;
      this.hud.textContent = '🎉 关卡完成！';
    }

    this.isMoving = false;
  }

  private resetLevel(): void {
    setTimeout(() => {
      this.loadLevel(TEST_LEVEL);
    }, 500);
  }

  private updateHUD(): void {
    this.hud.textContent = `胡萝卜: ${this.carrotsCollected} / ${this.targetCarrots}`;
  }

  private gameLoop = (): void => {
    this.render();
    requestAnimationFrame(this.gameLoop);
  };

  private render(): void {
    const ctx = this.ctx;
    ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

    if (!this.levelMap) return;

    // 渲染地图
    for (let y = 0; y < this.levelMap.height; y++) {
      for (let x = 0; x < this.levelMap.width; x++) {
        const cell = this.levelMap.cells[y][x];
        this.renderTile(ctx, x, y, cell.tile);
        this.renderObject(ctx, x, y, cell.object);
      }
    }

    // 渲染玩家
    this.renderPlayer(ctx);
  }

  private renderTile(ctx: CanvasRenderingContext2D, x: number, y: number, tile: Tile | null): void {
    const px = x * TILE_SIZE;
    const py = y * TILE_SIZE;

    if (!tile) {
      ctx.fillStyle = COLORS.ground;
      ctx.fillRect(px, py, TILE_SIZE, TILE_SIZE);
      return;
    }

    switch (tile.type) {
      case 'ground':
        ctx.fillStyle = COLORS.ground;
        ctx.fillRect(px, py, TILE_SIZE, TILE_SIZE);
        // 添加草地纹理
        ctx.fillStyle = '#3d7a37';
        for (let i = 0; i < 3; i++) {
          const gx = px + 5 + Math.random() * 22;
          const gy = py + 5 + Math.random() * 22;
          ctx.fillRect(gx, gy, 2, 4);
        }
        break;

      case 'water':
        ctx.fillStyle = COLORS.water;
        ctx.fillRect(px, py, TILE_SIZE, TILE_SIZE);
        // 波浪效果
        ctx.strokeStyle = '#3a6aaa';
        ctx.lineWidth = 1;
        ctx.beginPath();
        ctx.moveTo(px + 4, py + 10);
        ctx.quadraticCurveTo(px + 16, py + 6, px + 28, py + 10);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(px + 4, py + 22);
        ctx.quadraticCurveTo(px + 16, py + 18, px + 28, py + 22);
        ctx.stroke();
        break;

      case 'wall':
        ctx.fillStyle = COLORS.wall;
        ctx.fillRect(px, py, TILE_SIZE, TILE_SIZE);
        // 砖块纹理
        ctx.strokeStyle = '#3a2a1a';
        ctx.lineWidth = 1;
        ctx.strokeRect(px + 2, py + 2, 12, 12);
        ctx.strokeRect(px + 18, py + 2, 12, 12);
        ctx.strokeRect(px + 2, py + 18, 12, 12);
        ctx.strokeRect(px + 18, py + 18, 12, 12);
        break;

      default:
        ctx.fillStyle = COLORS.ground;
        ctx.fillRect(px, py, TILE_SIZE, TILE_SIZE);
    }
  }

  private renderObject(ctx: CanvasRenderingContext2D, x: number, y: number, obj: GameObj | null): void {
    if (!obj || obj.visible === false) return;

    const px = x * TILE_SIZE + TILE_SIZE / 2;
    const py = y * TILE_SIZE + TILE_SIZE / 2;

    switch (obj.type) {
      case 'carrot':
        // 胡萝卜
        ctx.fillStyle = COLORS.carrot;
        ctx.beginPath();
        ctx.moveTo(px, py - 10);
        ctx.lineTo(px - 6, py + 8);
        ctx.lineTo(px + 6, py + 8);
        ctx.closePath();
        ctx.fill();
        // 叶子
        ctx.fillStyle = '#2d5a27';
        ctx.fillRect(px - 4, py - 14, 2, 6);
        ctx.fillRect(px, py - 16, 2, 8);
        ctx.fillRect(px + 2, py - 14, 2, 6);
        break;

      case 'door':
        // 门
        ctx.fillStyle = COLORS.door;
        ctx.fillRect(px - 10, py - 14, 20, 28);
        ctx.fillStyle = '#6b3510';
        ctx.fillRect(px - 8, py - 12, 16, 24);
        ctx.fillStyle = '#ffd700';
        ctx.beginPath();
        ctx.arc(px + 4, py, 2, 0, Math.PI * 2);
        ctx.fill();
        break;

      case 'start':
        // 起点
        ctx.fillStyle = COLORS.start;
        ctx.beginPath();
        ctx.arc(px, py, 8, 0, Math.PI * 2);
        ctx.fill();
        ctx.fillStyle = '#fff';
        ctx.font = 'bold 12px sans-serif';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText('S', px, py);
        break;
    }
  }

  private renderPlayer(ctx: CanvasRenderingContext2D): void {
    if (!this.player) return;

    const px = this.playerPixelX;
    const py = this.playerPixelY;

    // 身体
    ctx.fillStyle = COLORS.player;
    ctx.beginPath();
    ctx.ellipse(px + PLAYER_SIZE / 2, py + PLAYER_SIZE / 2, PLAYER_SIZE / 2, PLAYER_SIZE / 2.2, 0, 0, Math.PI * 2);
    ctx.fill();

    // 耳朵
    ctx.fillStyle = '#ffcccc';
    const earOffset = this.player.direction === 'up' ? -4 : 0;
    ctx.beginPath();
    ctx.ellipse(px + PLAYER_SIZE / 2 - 8, py + 4 + earOffset, 4, 8, -0.3, 0, Math.PI * 2);
    ctx.fill();
    ctx.beginPath();
    ctx.ellipse(px + PLAYER_SIZE / 2 + 8, py + 4 + earOffset, 4, 8, 0.3, 0, Math.PI * 2);
    ctx.fill();

    // 眼睛
    ctx.fillStyle = '#333';
    const eyeY = py + PLAYER_SIZE / 2 - 2;
    ctx.beginPath();
    ctx.arc(px + PLAYER_SIZE / 2 - 5, eyeY, 2, 0, Math.PI * 2);
    ctx.fill();
    ctx.beginPath();
    ctx.arc(px + PLAYER_SIZE / 2 + 5, eyeY, 2, 0, Math.PI * 2);
    ctx.fill();

    // 鼻子
    ctx.fillStyle = '#ff9999';
    ctx.beginPath();
    ctx.arc(px + PLAYER_SIZE / 2, eyeY + 5, 3, 0, Math.PI * 2);
    ctx.fill();
  }
}

// 启动游戏
new Game();