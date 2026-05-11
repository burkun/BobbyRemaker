// game/assets/scripts/managers/LevelManager.ts

import { _decorator, Component, JsonAsset, resources } from 'cc';
import {
  LevelConfig, LevelMap, Player, createPlayer,
  createEmptyMap, createLevelConfig, Direction
} from '../../core';

const { ccclass, property } = _decorator;

@ccclass('LevelManager')
export class LevelManager extends Component {

  @property({ type: JsonAsset })
  levelAsset: JsonAsset | null = null;

  private _currentLevel: LevelConfig | null = null;
  private _player: Player | null = null;
  private _carrotsCollected: number = 0;

  public get currentLevel(): LevelConfig | null {
    return this._currentLevel;
  }

  public get player(): Player | null {
    return this._player;
  }

  public get carrotsCollected(): number {
    return this._carrotsCollected;
  }

  public get targetCarrots(): number {
    return this._currentLevel?.target.carrots || 0;
  }

  public async loadLevel(levelId: number): Promise<LevelConfig | null> {
    return new Promise((resolve) => {
      if (this.levelAsset) {
        const data = this.levelAsset.json as any;
        this.parseLevelData(data);
        resolve(this._currentLevel);
      } else {
        resources.load(`levels/level-${levelId}`, JsonAsset, (err, asset) => {
          if (err) {
            console.error('Failed to load level:', err);
            resolve(null);
            return;
          }
          const data = (asset as JsonAsset).json as any;
          this.parseLevelData(data);
          resolve(this._currentLevel);
        });
      }
    });
  }

  private parseLevelData(data: any): void {
    const map = this.parseMap(data);

    this._currentLevel = createLevelConfig(
      data.id || 1,
      data.packId || 1,
      data.name || 'Unnamed',
      map,
      data.target?.carrots || 0
    );

    if (data.startPosition) {
      this._player = createPlayer(data.startPosition.x, data.startPosition.y, 'down' as Direction);
    } else {
      const startPos = this.findStartPosition(map);
      this._player = createPlayer(startPos.x, startPos.y, 'down' as Direction);
    }

    this._carrotsCollected = 0;
  }

  private parseMap(data: any): LevelMap {
    const width = data.width || 10;
    const height = data.height || 8;
    const map = createEmptyMap(width, height);

    if (!data.cells) return map;

    for (let y = 0; y < height && y < data.cells.length; y++) {
      for (let x = 0; x < width && x < data.cells[y].length; x++) {
        const cellData = data.cells[y][x];

        if (cellData.tile) {
          map.cells[y][x].tile = {
            type: cellData.tile.type,
            direction: cellData.tile.direction,
            state: cellData.tile.state
          };
        }

        if (cellData.object) {
          map.cells[y][x].object = {
            type: cellData.object.type,
            state: cellData.object.state,
            visible: cellData.object.visible !== false
          };
        }
      }
    }

    return map;
  }

  private findStartPosition(map: LevelMap): { x: number; y: number } {
    for (let y = 0; y < map.height; y++) {
      for (let x = 0; x < map.width; x++) {
        if (map.cells[y][x].object?.type === 'start') {
          return { x, y };
        }
      }
    }
    return { x: 1, y: 1 };
  }

  public collectCarrot(): void {
    this._carrotsCollected++;
  }

  public resetLevel(): void {
    this._carrotsCollected = 0;
    if (this._currentLevel) {
      const startPos = this.findStartPosition(this._currentLevel.map);
      this._player = createPlayer(startPos.x, startPos.y, 'down' as Direction);
    }
  }
}
