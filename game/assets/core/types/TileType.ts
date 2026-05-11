// game/assets/core/types/TileType.ts

export type TileType =
  | 'ground'      // 普通地面
  | 'ice'         // 冰面，滑行
  | 'conveyer'    // 传送带
  | 'arrow'       // 箭头，强制方向
  | 'death'       // 死亡陷阱
  | 'grass'       // 草地，飞行才能进入
  | 'portal'      // 传送门
  | 'water'       // 水，不可通过
  | 'wall';       // 墙，不可通过

export const TILE_TYPES: TileType[] = [
  'ground', 'ice', 'conveyer', 'arrow', 'death', 'grass', 'portal', 'water', 'wall'
];

export const WALKABLE_TILES: TileType[] = [
  'ground', 'ice', 'conveyer', 'arrow', 'grass', 'portal'
];
