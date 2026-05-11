// game/assets/core/entity/index.ts

export type { Tile } from './Tile';
export { createTile, isWalkable } from './Tile';

export type { GameObj } from './GameObj';
export { createObject, isCollectible } from './GameObj';

export type { Cell } from './Cell';
export { createCell, createEmptyCell } from './Cell';

export type { LevelMap } from './LevelMap';
export { createEmptyMap, createMapFromArray, getCell, setTile, setObject } from './LevelMap';

export type { Player, PlayerInventory } from './Player';
export { createPlayer, movePlayer, setPlayerDirection, setPlayerState } from './Player';

export type { LevelConfig, LevelTarget } from './LevelConfig';
export { createLevelConfig } from './LevelConfig';
