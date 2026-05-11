// game/assets/scripts/systems/CollisionSystem.ts

import { LevelMap, Cell, Player, RuleConfig, canWalkOnTile, getTileEnterEffects, getObjectCollideEffects } from '../../core';
import { Direction, getDirectionDelta } from '../../core/types';

export interface CollisionResult {
  canMove: boolean;
  targetCell: Cell | null;
  effects: Array<{ type: string; params?: any }>;
}

export class CollisionSystem {

  private _ruleConfig: RuleConfig;

  constructor(ruleConfig: RuleConfig) {
    this._ruleConfig = ruleConfig;
  }

  public checkMovement(
    map: LevelMap,
    player: Player,
    direction: Direction
  ): CollisionResult {
    const delta = getDirectionDelta(direction);
    const newX = player.x + delta.dx;
    const newY = player.y + delta.dy;

    if (newX < 0 || newX >= map.width || newY < 0 || newY >= map.height) {
      return { canMove: false, targetCell: null, effects: [] };
    }

    const targetCell = map.cells[newY][newX];
    const effects: Array<{ type: string; params?: any }> = [];

    if (!canWalkOnTile(this._ruleConfig, player, targetCell.tile)) {
      return { canMove: false, targetCell, effects: [] };
    }

    const tileEffects = getTileEnterEffects(this._ruleConfig, targetCell.tile);
    effects.push(...tileEffects);

    const objectEffects = getObjectCollideEffects(this._ruleConfig, targetCell.object);
    effects.push(...objectEffects);

    return { canMove: true, targetCell, effects };
  }

  public checkLevelComplete(
    map: LevelMap,
    player: Player,
    carrotsCollected: number,
    targetCarrots: number
  ): boolean {
    if (carrotsCollected < targetCarrots) return false;

    const cell = map.cells[player.y]?.[player.x];
    if (!cell || !cell.object) return false;

    return cell.object.type === 'door';
  }
}
