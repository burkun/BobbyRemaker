// game/assets/core/entity/LevelMap.ts

import { Cell, createCell, createEmptyCell } from './Cell';
import { createTile, Tile } from './Tile';
import { createObject, GameObj } from './GameObj';
import { TileType, ObjectType, Direction } from '../types';

export interface LevelMap {
  width: number;
  height: number;
  cells: Cell[][];
}

export function createEmptyMap(width: number, height: number): LevelMap {
  const cells: Cell[][] = [];
  for (let y = 0; y < height; y++) {
    cells[y] = [];
    for (let x = 0; x < width; x++) {
      cells[y][x] = createEmptyCell();
    }
  }
  return { width, height, cells };
}

export function createMapFromArray(cells: Cell[][]): LevelMap {
  const height = cells.length;
  const width = height > 0 ? cells[0].length : 0;
  return { width, height, cells };
}

export function getCell(map: LevelMap, x: number, y: number): Cell | null {
  if (x < 0 || x >= map.width || y < 0 || y >= map.height) {
    return null;
  }
  return map.cells[y][x];
}

export function setTile(map: LevelMap, x: number, y: number, tile: Tile | null): void {
  if (x >= 0 && x < map.width && y >= 0 && y < map.height) {
    map.cells[y][x].tile = tile;
  }
}

export function setObject(map: LevelMap, x: number, y: number, obj: GameObj | null): void {
  if (x >= 0 && x < map.width && y >= 0 && y < map.height) {
    map.cells[y][x].object = obj;
  }
}
