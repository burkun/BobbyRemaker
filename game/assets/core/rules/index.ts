// game/assets/core/rules/index.ts

export type { RuleConfig, TileRule, ObjectRule, Effect, EffectType } from './RuleConfig';
export { canWalkOnTile, getTileEnterEffects, getObjectCollideEffects } from './RuleConfig';
export { bobbyRules } from './bobbyRules';