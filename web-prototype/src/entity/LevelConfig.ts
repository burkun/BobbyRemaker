// game/assets/core/entity/LevelConfig.ts

import { LevelMap } from './LevelMap';

export interface LevelConfig {
  id: number;
  packId: number;
  name: string;
  map: LevelMap;
  target: LevelTarget;
  music?: string;
}

export interface LevelTarget {
  carrots: number;
}

export function createLevelConfig(
  id: number,
  packId: number,
  name: string,
  map: LevelMap,
  carrots: number
): LevelConfig {
  return {
    id,
    packId,
    name,
    map,
    target: { carrots }
  };
}
