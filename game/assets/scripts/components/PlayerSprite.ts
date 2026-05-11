// game/assets/scripts/components/PlayerSprite.ts

import { _decorator, Component, Node, Sprite, Color, UITransform, Vec3, tween } from 'cc';
import { TILE_SIZE, MOVE_SPEED, Z_INDEX } from '../constants/GameConstants';
import { Player, Direction, PlayerState } from '../../core';

const { ccclass, property } = _decorator;

@ccclass('PlayerSprite')
export class PlayerSprite extends Component {

  private _player: Player | null = null;
  private _sprite: Sprite | null = null;
  private _isMoving: boolean = false;
  private _targetPosition: Vec3 | null = null;

  private readonly PLAYER_COLOR = new Color(255, 200, 150);

  public get player(): Player | null {
    return this._player;
  }

  public init(player: Player, gridRenderer: { gridToWorld: (x: number, y: number) => Vec3 }): void {
    this._player = player;

    const transform = this.node.getComponent(UITransform) || this.node.addComponent(UITransform);
    transform.setContentSize(TILE_SIZE * 0.6, TILE_SIZE * 0.6);

    this._sprite = this.node.getComponent(Sprite) || this.node.addComponent(Sprite);
    this._sprite.color = this.PLAYER_COLOR;

    const worldPos = gridRenderer.gridToWorld(player.x, player.y);
    this.node.setPosition(worldPos);

    this.node.setSiblingIndex(Z_INDEX.PLAYER);
  }

  public moveTo(x: number, y: number, gridRenderer: { gridToWorld: (x: number, y: number) => Vec3 }): Promise<void> {
    if (this._isMoving) {
      return Promise.resolve();
    }

    this._isMoving = true;
    this._targetPosition = gridRenderer.gridToWorld(x, y);

    if (this._player) {
      this._player.x = x;
      this._player.y = y;
      this._player.state = 'moving';
    }

    return new Promise((resolve) => {
      tween(this.node)
        .to(1 / MOVE_SPEED, { position: this._targetPosition })
        .call(() => {
          this._isMoving = false;
          this._targetPosition = null;
          if (this._player) {
            this._player.state = 'idle';
          }
          resolve();
        })
        .start();
    });
  }

  public setDirection(direction: Direction): void {
    if (this._player) {
      this._player.direction = direction;
    }
  }

  public setState(state: PlayerState): void {
    if (this._player) {
      this._player.state = state;
    }
  }

  public playDeathAnimation(): Promise<void> {
    return new Promise((resolve) => {
      tween(this.node)
        .to(0.3, { scale: new Vec3(1.2, 0.8, 1) })
        .to(0.3, { scale: new Vec3(0.8, 1.2, 1) })
        .to(0.3, { scale: new Vec3(1.2, 0.8, 1) })
        .to(0.3, { scale: new Vec3(1, 1, 1) })
        .call(resolve)
        .start();
    });
  }

  public playCollectAnimation(): Promise<void> {
    const originalScale = this.node.scale.clone();
    return new Promise((resolve) => {
      tween(this.node)
        .to(0.1, { scale: new Vec3(1.3, 1.3, 1) })
        .to(0.1, { scale: originalScale })
        .call(resolve)
        .start();
    });
  }
}
