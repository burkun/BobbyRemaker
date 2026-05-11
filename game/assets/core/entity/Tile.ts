// game/assets/core/entity/Tile.ts

import { TileType, Direction } from '../types';

export interface Tile {
  type: TileType;
  direction?: Direction;  // 传送带/箭头方向
  state?: number;         // 状态（开关、传送门ID等）
}

export function createTile(type: TileType, direction?: Direction, state?: number): Tile {
  return { type, direction, state };
}

export function isWalkable(tile: Tile | null): boolean {
  if (tile === null) return false;
  return tile.type !== 'water' && tile.type !== 'wall';
}
