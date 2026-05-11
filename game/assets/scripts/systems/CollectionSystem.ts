// game/assets/scripts/systems/CollectionSystem.ts

import { LevelMap, Player, isCollectible } from '../../core';

export interface CollectionResult {
  collected: boolean;
  itemType: string | null;
  cell: { x: number; y: number } | null;
}

export class CollectionSystem {

  public tryCollect(map: LevelMap, player: Player): CollectionResult {
    const cell = map.cells[player.y]?.[player.x];

    if (!cell || !cell.object) {
      return { collected: false, itemType: null, cell: null };
    }

    const obj = cell.object;

    if (!isCollectible(obj)) {
      return { collected: false, itemType: null, cell: null };
    }

    obj.visible = false;

    return {
      collected: true,
      itemType: obj.type,
      cell: { x: player.x, y: player.y }
    };
  }

  public applyEffect(player: Player, effectType: string, params?: any): void {
    switch (effectType) {
      case 'collect':
        if (params?.item === 'seed') player.inventory.seeds++;
        break;
      case 'pickup':
        if (params?.item === 'seed') player.inventory.seeds++;
        break;
    }
  }
}
