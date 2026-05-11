// game/assets/core/entity/LevelMap.ts

import { Cell, createCell, createEmptyCell } from './Cell';
import { createTile } from './Tile';
import { createObject } from './GameObj';
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

export function createMapFromArray(width: number, height: number, tiles: number[][], objects: number[][]): LevelMap {
  const map = createEmptyMap(width, height);
  // 简化版本：根据数值映射类型
  // 后续会扩展完整的映射
  for (let y = 0; y < height; y++) {
    for (let x = 0; x < width; x++) {
      if (tiles[y] && tiles[y][x] !== undefined && tiles[y][x] !== 0) {
        map.cells[y][x].tile = createTile('ground');
      }
    }
  }
  return map;
}

export function getCell(map: LevelMap, x: number, y: number): Cell | null {
  if (x < 0 || x >= map.width || y < 0 || y >= map.height) {
    return null;
  }
  return map.cells[y][x];
}

export function setTile(map: LevelMap, x: number, y: number, tile: LevelMap['cells'][0][0]['tile']): void {
  if (x >= 0 && x < map.width && y >= 0 && y < map.height) {
    map.cells[y][x].tile = tile;
  }
}

export function setObject(map: LevelMap, x: number, y: number, obj: LevelMap['cells'][0][0]['object']): void {
  if (x >= 0 && x < map.width && y >= 0 && y < map.height) {
    map.cells[y][x].object = obj;
  }
}
