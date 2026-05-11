// game/assets/core/rules/bobbyRules.ts
//
// Note: 'tile.direction' is a placeholder string that should be resolved at runtime
// to the tile's actual direction property. The game engine should replace this
// placeholder with the concrete direction value when processing effects.

import { RuleConfig } from './RuleConfig';

export const bobbyRules: RuleConfig = {
  tileRules: {
    'ground': {
      canWalk: true
    },
    'ice': {
      canWalk: true,
      onEnter: [{ type: 'slide' }]
    },
    'conveyer': {
      canWalk: true,
      onEnter: [{ type: 'push', params: { direction: 'tile.direction' } }]
    },
    'arrow': {
      canWalk: true,
      onEnter: [{ type: 'forceMove', params: { direction: 'tile.direction' } }]
    },
    'death': {
      canWalk: true,  // Player can enter the tile, but will die upon entering
      onEnter: [{ type: 'die' }]
    },
    'grass': {
      canWalk: (player) => player.state === 'flying'
    },
    'portal': {
      canWalk: true,
      onEnter: [{ type: 'teleport' }]
    },
    'water': {
      canWalk: false
    },
    'wall': {
      canWalk: false
    }
  },
  objectRules: {
    'carrot': {
      onCollide: [{ type: 'collect', params: { item: 'carrot' } }]
    },
    'seed': {
      onCollide: [{ type: 'pickup', params: { item: 'seed' } }]
    },
    'spring': {
      onCollide: [{ type: 'bounce', params: { height: 2 } }]
    },
    'door': {
      onCollide: [{ type: 'levelEnd' }]
    },
    'flight': {
      onCollide: [{ type: 'collect', params: { item: 'flight' } }]
    },
    'bonus': {
      onCollide: [{ type: 'levelEnd', params: { bonus: true } }]
    }
  }
};
