// game/assets/scripts/managers/GameManager.ts

import { _decorator, Component, Node } from 'cc';
import { LevelManager } from './LevelManager';
import { GridRenderer } from '../components/GridRenderer';
import { PlayerSprite } from '../components/PlayerSprite';
import { TouchController, TouchControllerDelegate } from '../components/TouchController';
import { CollisionSystem } from '../systems/CollisionSystem';
import { CollectionSystem } from '../systems/CollectionSystem';
import { MovementSystem } from '../systems/MovementSystem';
import { bobbyRules, Direction } from '../../core';

const { ccclass, property } = _decorator;

@ccclass('GameManager')
export class GameManager extends Component implements TouchControllerDelegate {

  @property(LevelManager)
  levelManager: LevelManager | null = null;

  @property(GridRenderer)
  gridRenderer: GridRenderer | null = null;

  @property(PlayerSprite)
  playerSprite: PlayerSprite | null = null;

  @property(TouchController)
  touchController: TouchController | null = null;

  private _collisionSystem: CollisionSystem | null = null;
  private _collectionSystem: CollectionSystem | null = null;
  private _movementSystem: MovementSystem | null = null;
  private _isLevelComplete: boolean = false;

  protected onLoad(): void {
    this._collisionSystem = new CollisionSystem(bobbyRules);
    this._collectionSystem = new CollectionSystem();
    this._movementSystem = new MovementSystem();

    if (this.touchController) {
      this.touchController.delegate = this;
    }
  }

  protected start(): void {
    this.startGame();
  }

  public async startGame(): Promise<void> {
    await this.loadLevel(1);
  }

  public async loadLevel(levelId: number): Promise<void> {
    if (!this.levelManager) return;

    this._isLevelComplete = false;

    const level = await this.levelManager.loadLevel(levelId);
    if (!level) {
      console.error('Failed to load level');
      return;
    }

    if (this.gridRenderer) {
      this.gridRenderer.loadLevel(level);
    }

    if (this.playerSprite && this.levelManager.player && this.gridRenderer) {
      this.playerSprite.init(this.levelManager.player, this.gridRenderer);
    }

    console.log(`Level ${levelId} loaded: ${level.name}`);
    console.log(`Target: Collect ${level.target.carrots} carrots`);
  }

  public onDirectionInput(direction: Direction): void {
    if (this._isLevelComplete) return;
    if (!this.levelManager?.player || !this.levelManager?.currentLevel) return;
    if (!this._collisionSystem || !this._collectionSystem || !this._movementSystem) return;

    this._movementSystem.processMove(
      this.levelManager.player,
      direction,
      () => {
        const result = this._collisionSystem!.checkMovement(
          this.levelManager!.currentLevel!.map,
          this.levelManager!.player!,
          direction
        );
        return { canMove: result.canMove, effects: result.effects };
      },
      async (dx, dy, effects) => {
        if (this.playerSprite && this.gridRenderer) {
          const newX = this.levelManager!.player!.x + dx;
          const newY = this.levelManager!.player!.y + dy;
          await this.playerSprite.moveTo(newX, newY, this.gridRenderer!);
        }

        const collectResult = this._collectionSystem!.tryCollect(
          this.levelManager!.currentLevel!.map,
          this.levelManager!.player!
        );

        if (collectResult.collected) {
          this.levelManager!.collectCarrot();
          if (this.playerSprite) {
            await this.playerSprite.playCollectAnimation();
          }
          this.updateHUD();
        }

        await this._movementSystem!.processEffects(
          this.levelManager!.player!,
          effects,
          {
            onDie: async () => {
              if (this.playerSprite) {
                await this.playerSprite.playDeathAnimation();
              }
              this.levelManager!.resetLevel();
              await this.loadLevel(this.levelManager!.currentLevel!.id);
            }
          }
        );

        this.checkLevelComplete();
      }
    );
  }

  private checkLevelComplete(): void {
    if (!this.levelManager || !this._collisionSystem) return;

    const complete = this._collisionSystem.checkLevelComplete(
      this.levelManager.currentLevel!.map,
      this.levelManager.player!,
      this.levelManager.carrotsCollected,
      this.levelManager.targetCarrots
    );

    if (complete) {
      this._isLevelComplete = true;
      console.log('Level Complete!');
    }
  }

  private updateHUD(): void {
    if (!this.levelManager) return;
    console.log(`Carrots: ${this.levelManager.carrotsCollected}/${this.levelManager.targetCarrots}`);
  }
}
