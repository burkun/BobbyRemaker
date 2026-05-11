// game/assets/scripts/components/GridRenderer.ts

import { _decorator, Component, Node, Sprite, Color, UITransform, Vec3 } from 'cc';
import { TILE_SIZE, Z_INDEX } from '../constants/GameConstants';
import { LevelMap, Cell, LevelConfig } from '../../core';

const { ccclass, property } = _decorator;

@ccclass('GridRenderer')
export class GridRenderer extends Component {

  @property(Node)
  tileLayer: Node | null = null;

  @property(Node)
  objectLayer: Node | null = null;

  private _map: LevelMap | null = null;
  private _tileSprites: Map<string, Sprite> = new Map();
  private _objectSprites: Map<string, Sprite> = new Map();

  private readonly TILE_COLORS: Record<string, Color> = {
    'ground': new Color(200, 180, 140),
    'ice': new Color(180, 220, 255),
    'conveyer': new Color(255, 200, 100),
    'arrow': new Color(100, 255, 100),
    'death': new Color(255, 100, 100),
    'grass': new Color(100, 200, 100),
    'portal': new Color(200, 100, 255),
    'water': new Color(100, 150, 255),
    'wall': new Color(100, 100, 100)
  };

  private readonly OBJECT_COLORS: Record<string, Color> = {
    'carrot': new Color(255, 150, 0),
    'seed': new Color(150, 100, 50),
    'spring': new Color(255, 255, 100),
    'door': new Color(150, 100, 200),
    'flight': new Color(200, 255, 255),
    'bonus': new Color(255, 200, 255),
    'start': new Color(0, 255, 0),
    'rock': new Color(150, 150, 150),
    'mower': new Color(255, 100, 50)
  };

  public get map(): LevelMap | null {
    return this._map;
  }

  public loadLevel(config: LevelConfig): void {
    this.clearMap();
    this._map = config.map;
    this.renderMap();
  }

  private clearMap(): void {
    this._tileSprites.forEach(sprite => sprite.node.destroy());
    this._objectSprites.forEach(sprite => sprite.node.destroy());
    this._tileSprites.clear();
    this._objectSprites.clear();
  }

  private renderMap(): void {
    if (!this._map || !this.tileLayer || !this.objectLayer) return;

    const map = this._map;

    for (let y = 0; y < map.height; y++) {
      for (let x = 0; x < map.width; x++) {
        const cell = map.cells[y][x];
        this.renderCell(x, y, cell);
      }
    }
  }

  private renderCell(x: number, y: number, cell: Cell): void {
    const worldPos = this.gridToWorld(x, y);

    if (cell.tile) {
      this.createTileSprite(x, y, cell.tile.type, worldPos);
    }

    if (cell.object && cell.object.visible !== false) {
      this.createObjectSprite(x, y, cell.object.type, worldPos);
    }
  }

  private createTileSprite(x: number, y: number, type: string, worldPos: Vec3): void {
    if (!this.tileLayer) return;

    const node = new Node(`tile_${x}_${y}`);
    node.setParent(this.tileLayer);
    node.setPosition(worldPos);
    node.addComponent(UITransform).setContentSize(TILE_SIZE, TILE_SIZE);

    const sprite = node.addComponent(Sprite);
    sprite.color = this.TILE_COLORS[type] || new Color(200, 200, 200);

    this._tileSprites.set(`${x}_${y}`, sprite);
  }

  private createObjectSprite(x: number, y: number, type: string, worldPos: Vec3): void {
    if (!this.objectLayer) return;

    const node = new Node(`obj_${x}_${y}`);
    node.setParent(this.objectLayer);
    node.setPosition(worldPos);
    node.addComponent(UITransform).setContentSize(TILE_SIZE * 0.8, TILE_SIZE * 0.8);

    const sprite = node.addComponent(Sprite);
    sprite.color = this.OBJECT_COLORS[type] || new Color(255, 255, 255);

    this._objectSprites.set(`${x}_${y}`, sprite);
  }

  public gridToWorld(x: number, y: number): Vec3 {
    return new Vec3(
      x * TILE_SIZE + TILE_SIZE / 2,
      -y * TILE_SIZE - TILE_SIZE / 2,
      0
    );
  }

  public worldToGrid(worldX: number, worldY: number): { x: number; y: number } {
    return {
      x: Math.floor(worldX / TILE_SIZE),
      y: Math.floor(-worldY / TILE_SIZE)
    };
  }

  public getAdjacentCell(x: number, y: number, dx: number, dy: number): Cell | null {
    if (!this._map) return null;

    const newX = x + dx;
    const newY = y + dy;

    if (newX < 0 || newX >= this._map.width || newY < 0 || newY >= this._map.height) {
      return null;
    }

    return this._map.cells[newY][newX];
  }
}
