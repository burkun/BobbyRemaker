// game/assets/core/rules/RuleConfig.ts

import { Player } from '../entity';
import { Tile } from '../entity/Tile';
import { GameObj } from '../entity/GameObj';

export type EffectType =
  | 'slide'        // 滑行
  | 'push'        // 推动方向
  | 'forceMove'   // 强制移动
  | 'die'         // 死亡
  | 'teleport'    // 传送
  | 'collect'     // 收集
  | 'pickup'      // 捡起
  | 'bounce'      // 弹跳
  | 'levelEnd';   // 关卡结束

export interface Effect {
  type: EffectType;
  params?: Record<string, any>;
}

export interface TileRule {
  canWalk?: boolean | ((player: Player, tile: Tile) => boolean);
  onEnter?: Effect[];
  onLeave?: Effect[];
}

export interface ObjectRule {
  onCollide?: Effect[];
}

export interface RuleConfig {
  tileRules: Record<string, TileRule>;
  objectRules: Record<string, ObjectRule>;
}

export function canWalkOnTile(
  rule: RuleConfig,
  player: Player,
  tile: Tile | null
): boolean {
  if (tile === null) return false;

  const tileRule = rule.tileRules[tile.type];
  if (!tileRule || tileRule.canWalk === undefined) return false;

  if (typeof tileRule.canWalk === 'boolean') {
    return tileRule.canWalk;
  }
  return tileRule.canWalk(player, tile);
}

export function getTileEnterEffects(rule: RuleConfig, tile: Tile | null): Effect[] {
  if (tile === null) return [];
  const tileRule = rule.tileRules[tile.type];
  return tileRule?.onEnter || [];
}

export function getObjectCollideEffects(rule: RuleConfig, obj: GameObj | null): Effect[] {
  if (obj === null) return [];
  const objectRule = rule.objectRules[obj.type];
  return objectRule?.onCollide || [];
}
