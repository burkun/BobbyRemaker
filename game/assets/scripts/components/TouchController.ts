// game/assets/scripts/components/TouchController.ts

import { _decorator, Component, Node, EventTouch, Vec2 } from 'cc';
import { Direction } from '../../core';

const { ccclass, property } = _decorator;

export interface TouchControllerDelegate {
  onDirectionInput(direction: Direction): void;
}

@ccclass('TouchController')
export class TouchController extends Component {

  @property(Node)
  touchArea: Node | null = null;

  private _delegate: TouchControllerDelegate | null = null;
  private _touchStartPos: Vec2 | null = null;
  private _isEnabled: boolean = true;

  private readonly MIN_SWIPE_DISTANCE = 30;

  public set delegate(delegate: TouchControllerDelegate | null) {
    this._delegate = delegate;
  }

  public set enabled(value: boolean) {
    this._isEnabled = value;
  }

  protected onLoad(): void {
    if (this.touchArea) {
      this.touchArea.on(Node.EventType.TOUCH_START, this.onTouchStart, this);
      this.touchArea.on(Node.EventType.TOUCH_END, this.onTouchEnd, this);
      this.touchArea.on(Node.EventType.TOUCH_CANCEL, this.onTouchEnd, this);
    }
  }

  protected onDestroy(): void {
    if (this.touchArea) {
      this.touchArea.off(Node.EventType.TOUCH_START, this.onTouchStart, this);
      this.touchArea.off(Node.EventType.TOUCH_END, this.onTouchEnd, this);
      this.touchArea.off(Node.EventType.TOUCH_CANCEL, this.onTouchEnd, this);
    }
  }

  private onTouchStart(event: EventTouch): void {
    if (!this._isEnabled) return;

    const pos = event.getUILocation();
    this._touchStartPos = new Vec2(pos.x, pos.y);
  }

  private onTouchEnd(event: EventTouch): void {
    if (!this._isEnabled || !this._touchStartPos) return;

    const pos = event.getUILocation();
    const endPos = new Vec2(pos.x, pos.y);

    const direction = this.detectSwipeDirection(this._touchStartPos, endPos);

    if (direction && this._delegate) {
      this._delegate.onDirectionInput(direction);
    }

    this._touchStartPos = null;
  }

  private detectSwipeDirection(start: Vec2, end: Vec2): Direction | null {
    const dx = end.x - start.x;
    const dy = end.y - start.y;
    const distance = Math.sqrt(dx * dx + dy * dy);

    if (distance < this.MIN_SWIPE_DISTANCE) {
      return null;
    }

    const angle = Math.atan2(dy, dx) * 180 / Math.PI;

    if (angle >= -45 && angle < 45) return 'right';
    if (angle >= 45 && angle < 135) return 'up';
    if (angle >= 135 || angle < -135) return 'left';
    if (angle >= -135 && angle < -45) return 'down';

    return null;
  }
}
