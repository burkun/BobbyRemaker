// game/assets/core/entity/GameObj.ts

import { ObjectType } from '../types';

export interface GameObj {
  type: ObjectType;
  state?: number;         // 状态（种子堆叠数等）
  visible?: boolean;      // 是否可见
}

export function createObject(type: ObjectType, state?: number, visible: boolean = true): GameObj {
  return { type, state, visible };
}

export function isCollectible(obj: GameObj | null): boolean {
  if (obj === null) return false;
  return obj.type === 'carrot' || obj.type === 'seed' || obj.type === 'flight';
}
