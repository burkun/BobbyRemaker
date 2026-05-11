// game/assets/core/entity/Cell.ts

import { Tile } from './Tile';
import { GameObj } from './GameObj';

export interface Cell {
  tile: Tile | null;
  object: GameObj | null;
}

export function createCell(tile: Tile | null = null, object: GameObj | null = null): Cell {
  return { tile, object };
}

export function createEmptyCell(): Cell {
  return { tile: null, object: null };
}
