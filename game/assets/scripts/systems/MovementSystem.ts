// game/assets/scripts/systems/MovementSystem.ts

import { Player, Direction, getDirectionDelta } from '../../core';

export class MovementSystem {

  private _isProcessing: boolean = false;

  public get isProcessing(): boolean {
    return this._isProcessing;
  }

  public async processMove(
    player: Player,
    direction: Direction,
    collisionCheck: () => { canMove: boolean; effects: any[] },
    moveExecutor: (dx: number, dy: number, effects: any[]) => Promise<void>
  ): Promise<void> {
    if (this._isProcessing) return;

    this._isProcessing = true;
    player.direction = direction;

    const collision = collisionCheck();

    if (collision.canMove) {
      const delta = getDirectionDelta(direction);
      await moveExecutor(delta.dx, delta.dy, collision.effects);
    }

    this._isProcessing = false;
  }

  public async processEffects(
    player: Player,
    effects: Array<{ type: string; params?: any }>,
    callbacks: {
      onSlide?: () => Promise<void>;
      onDie?: () => Promise<void>;
      onBounce?: (height: number) => Promise<void>;
      onTeleport?: () => Promise<void>;
    }
  ): Promise<void> {
    for (const effect of effects) {
      switch (effect.type) {
        case 'slide':
          if (callbacks.onSlide) await callbacks.onSlide();
          break;
        case 'die':
          player.state = 'dying';
          if (callbacks.onDie) await callbacks.onDie();
          break;
        case 'bounce':
          if (callbacks.onBounce) await callbacks.onBounce(effect.params?.height || 2);
          break;
        case 'teleport':
          if (callbacks.onTeleport) await callbacks.onTeleport();
          break;
      }
    }
  }
}
