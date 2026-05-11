// game/assets/core/entity/Player.ts

import { Direction, PlayerState } from '../types';

export interface Player {
  x: number;              // 网格坐标
  y: number;
  direction: Direction;    // 朝向
  state: PlayerState;      // 状态
  inventory: PlayerInventory;
}

export interface PlayerInventory {
  seeds: number;
}

export function createPlayer(x: number, y: number, direction: Direction = 'down'): Player {
  return {
    x,
    y,
    direction,
    state: 'idle',
    inventory: {
      seeds: 0
    }
  };
}

export function movePlayer(player: Player, dx: number, dy: number): void {
  player.x += dx;
  player.y += dy;
}

export function setPlayerDirection(player: Player, direction: Direction): void {
  player.direction = direction;
}

export function setPlayerState(player: Player, state: PlayerState): void {
  player.state = state;
}
