# P0: 核心原型实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 实现可玩的 Bobby Carrot 核心原型，包含角色移动、碰撞检测、胡萝卜收集、过关判定。

**Architecture:** 分离核心逻辑层（无引擎依赖）和渲染层（Cocos 组件）。核心层定义数据结构和规则，渲染层负责 Cocos 集成。

**Tech Stack:** Cocos Creator 3.8.8 LTS, TypeScript 5.x

---

## 文件结构

```
game/
├── assets/
│   ├── core/                          # 无引擎依赖的核心逻辑
│   │   ├── types/
│   │   │   ├── Direction.ts           # 方向类型
│   │   │   ├── TileType.ts            # 地形类型
│   │   │   ├── ObjectType.ts          # 道具类型
│   │   │   ├── PlayerState.ts         # 玩家状态
│   │   │   └── index.ts               # 导出
│   │   ├── entity/
│   │   │   ├── Cell.ts                # 格子定义
│   │   │   ├── Tile.ts                # 地形定义
│   │   │   ├── GameObj.ts             # 道具定义（重命名避免与内置object冲突）
│   │   │   ├── LevelMap.ts            # 地图定义
│   │   │   ├── Player.ts              # 玩家定义
│   │   │   ├── LevelConfig.ts         # 关卡配置
│   │   │   └── index.ts               # 导出
│   │   ├── rules/
│   │   │   ├── RuleConfig.ts          # 规则配置接口
│   │   │   ├── bobbyRules.ts          # Bobby Carrot 规则
│   │   │   └── index.ts               # 导出
│   │   └── index.ts                   # 核心层导出
│   │
│   ├── scripts/
│   │   ├── components/
│   │   │   ├── GridRenderer.ts        # 格子地图渲染
│   │   │   ├── PlayerSprite.ts        # 玩家精灵动画
│   │   │   ├── TouchController.ts     # 触摸控制
│   │   │   └── DPadController.ts      # 虚拟方向键
│   │   ├── systems/
│   │   │   ├── MovementSystem.ts      # 移动系统
│   │   │   ├── CollisionSystem.ts     # 碰撞检测
│   │   │   └── CollectionSystem.ts     # 收集系统
│   │   ├── managers/
│   │   │   ├── GameManager.ts         # 游戏管理器
│   │   │   └── LevelManager.ts        # 关卡管理器
│   │   └── constants/
│   │       └── GameConstants.ts       # 游戏常量
│   │
│   ├── resources/
│   │   └── levels/
│   │       └── level-1-1.json         # 测试关卡
│   │
│   ├── scenes/
│   │   └── Game.scene                 # 游戏场景
│   │
│   └── prefabs/
│       ├── Cell.prefab                # 格子预制体
│       └── Player.prefab              # 玩家预制体
```

---

## Task 1: 核心类型定义

**Files:**
- Create: `game/assets/core/types/Direction.ts`
- Create: `game/assets/core/types/TileType.ts`
- Create: `game/assets/core/types/ObjectType.ts`
- Create: `game/assets/core/types/PlayerState.ts`
- Create: `game/assets/core/types/index.ts`

- [ ] **Step 1: 创建 Direction 类型**

```typescript
// game/assets/core/types/Direction.ts

export type Direction = 'up' | 'down' | 'left' | 'right';

export const DIRECTIONS: Direction[] = ['up', 'down', 'left', 'right'];

export function getOppositeDirection(dir: Direction): Direction {
  switch (dir) {
    case 'up': return 'down';
    case 'down': return 'up';
    case 'left': return 'right';
    case 'right': return 'left';
  }
}

export function getDirectionDelta(dir: Direction): { dx: number; dy: number } {
  switch (dir) {
    case 'up': return { dx: 0, dy: -1 };
    case 'down': return { dx: 0, dy: 1 };
    case 'left': return { dx: -1, dy: 0 };
    case 'right': return { dx: 1, dy: 0 };
  }
}
```

- [ ] **Step 2: 创建 TileType 类型**

```typescript
// game/assets/core/types/TileType.ts

export type TileType =
  | 'ground'      // 普通地面
  | 'ice'         // 冰面，滑行
  | 'conveyer'    // 传送带
  | 'arrow'       // 箭头，强制方向
  | 'death'       // 死亡陷阱
  | 'grass'       // 草地，飞行才能进入
  | 'portal'      // 传送门
  | 'water'       // 水，不可通过
  | 'wall';       // 墙，不可通过

export const TILE_TYPES: TileType[] = [
  'ground', 'ice', 'conveyer', 'arrow', 'death', 'grass', 'portal', 'water', 'wall'
];

export const WALKABLE_TILES: TileType[] = [
  'ground', 'ice', 'conveyer', 'arrow', 'grass', 'portal'
];
```

- [ ] **Step 3: 创建 ObjectType 类型**

```typescript
// game/assets/core/types/ObjectType.ts

export type ObjectType =
  | 'carrot'      // 胡萝卜，收集目标
  | 'seed'        // 种子，可捡起/放置
  | 'spring'     // 弹簧
  | 'door'        // 关卡出口
  | 'flight'      // 飞行道具
  | 'bonus'       // 奖励门
  | 'start'       // 起点
  | 'rock'        // 石头，可推动
  | 'mower';      // 割草机

export const OBJECT_TYPES: ObjectType[] = [
  'carrot', 'seed', 'spring', 'door', 'flight', 'bonus', 'start', 'rock', 'mower'
];

export const COLLECTIBLE_OBJECTS: ObjectType[] = ['carrot', 'seed', 'flight'];
```

- [ ] **Step 4: 创建 PlayerState 类型**

```typescript
// game/assets/core/types/PlayerState.ts

export type PlayerState = 'idle' | 'moving' | 'flying' | 'dying';

export const PLAYER_STATES: PlayerState[] = ['idle', 'moving', 'flying', 'dying'];
```

- [ ] **Step 5: 创建类型导出文件**

```typescript
// game/assets/core/types/index.ts

export type { Direction } from './Direction';
export { DIRECTIONS, getOppositeDirection, getDirectionDelta } from './Direction';

export type { TileType } from './TileType';
export { TILE_TYPES, WALKABLE_TILES } from './TileType';

export type { ObjectType } from './ObjectType';
export { OBJECT_TYPES, COLLECTIBLE_OBJECTS } from './ObjectType';

export type { PlayerState } from './PlayerState';
export { PLAYER_STATES } from './PlayerState';
```

- [ ] **Step 6: 提交核心类型**

```bash
git add game/assets/core/types/
git commit -m "feat(core): add core type definitions

- Direction: up/down/left/right with delta and opposite helpers
- TileType: terrain types with walkable classification
- ObjectType: game objects with collectible classification
- PlayerState: player states (idle/moving/flying/dying)

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 2: 实体定义

**Files:**
- Create: `game/assets/core/entity/Tile.ts`
- Create: `game/assets/core/entity/GameObj.ts`
- Create: `game/assets/core/entity/Cell.ts`
- Create: `game/assets/core/entity/LevelMap.ts`
- Create: `game/assets/core/entity/Player.ts`
- Create: `game/assets/core/entity/LevelConfig.ts`
- Create: `game/assets/core/entity/index.ts`

- [ ] **Step 1: 创建 Tile 实体**

```typescript
// game/assets/core/entity/Tile.ts

import { TileType, Direction } from '../types';

export interface Tile {
  type: TileType;
  direction?: Direction;  // 传送带/箭头方向
  state?: number;         // 状态（开关、传送门ID等）
}

export function createTile(type: TileType, direction?: Direction, state?: number): Tile {
  return { type, direction, state };
}

export function isWalkable(tile: Tile | null): boolean {
  if (tile === null) return false;
  return tile.type !== 'water' && tile.type !== 'wall';
}
```

- [ ] **Step 2: 创建 GameObj 实体**

```typescript
// game/assets/core/entity/GameObj.ts

import { ObjectType } from '../types';

export interface GameObj {
  type: ObjectType;
  state?: number;         // 状态（种子堆叠数等）
  visible?: boolean;      // 是否可见
}

export function createObject(type: ObjectType, state?: number, visible: boolean = true): GameObj {
  return { type, state, visible };
}

export function isCollectible(obj: GameObj | null): boolean {
  if (obj === null) return false;
  return obj.type === 'carrot' || obj.type === 'seed' || obj.type === 'flight';
}
```

- [ ] **Step 3: 创建 Cell 实体**

```typescript
// game/assets/core/entity/Cell.ts

import { Tile } from './Tile';
import { GameObj } from './GameObj';

export interface Cell {
  tile: Tile | null;
  object: GameObj | null;
}

export function createCell(tile: Tile | null = null, object: GameObj | null = null): Cell {
  return { tile, object };
}

export function createEmptyCell(): Cell {
  return { tile: null, object: null };
}
```

- [ ] **Step 4: 创建 LevelMap 实体**

```typescript
// game/assets/core/entity/LevelMap.ts

import { Cell, createCell, createEmptyCell } from './Cell';
import { createTile } from './Tile';
import { createObject } from './GameObj';
import { TileType, ObjectType, Direction } from '../types';

export interface LevelMap {
  width: number;
  height: number;
  cells: Cell[][];
}

export function createEmptyMap(width: number, height: number): LevelMap {
  const cells: Cell[][] = [];
  for (let y = 0; y < height; y++) {
    cells[y] = [];
    for (let x = 0; x < width; x++) {
      cells[y][x] = createEmptyCell();
    }
  }
  return { width, height, cells };
}

export function createMapFromArray(width: number, height: number, tiles: number[][], objects: number[][]): LevelMap {
  const map = createEmptyMap(width, height);
  // 简化版本：根据数值映射类型
  // 后续会扩展完整的映射
  for (let y = 0; y < height; y++) {
    for (let x = 0; x < width; x++) {
      if (tiles[y] && tiles[y][x] !== undefined && tiles[y][x] !== 0) {
        map.cells[y][x].tile = createTile('ground');
      }
    }
  }
  return map;
}

export function getCell(map: LevelMap, x: number, y: number): Cell | null {
  if (x < 0 || x >= map.width || y < 0 || y >= map.height) {
    return null;
  }
  return map.cells[y][x];
}

export function setTile(map: LevelMap, x: number, y: number, tile: LevelMap['cells'][0][0]['tile']): void {
  if (x >= 0 && x < map.width && y >= 0 && y < map.height) {
    map.cells[y][x].tile = tile;
  }
}

export function setObject(map: LevelMap, x: number, y: number, obj: LevelMap['cells'][0][0]['object']): void {
  if (x >= 0 && x < map.width && y >= 0 && y < map.height) {
    map.cells[y][x].object = obj;
  }
}
```

- [ ] **Step 5: 创建 Player 实体**

```typescript
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
```

- [ ] **Step 6: 创建 LevelConfig 实体**

```typescript
// game/assets/core/entity/LevelConfig.ts

import { LevelMap } from './LevelMap';

export interface LevelConfig {
  id: number;
  packId: number;
  name: string;
  map: LevelMap;
  target: LevelTarget;
  music?: string;
}

export interface LevelTarget {
  carrots: number;
}

export function createLevelConfig(
  id: number,
  packId: number,
  name: string,
  map: LevelMap,
  carrots: number
): LevelConfig {
  return {
    id,
    packId,
    name,
    map,
    target: { carrots }
  };
}
```

- [ ] **Step 7: 创建实体导出文件**

```typescript
// game/assets/core/entity/index.ts

export type { Tile } from './Tile';
export { createTile, isWalkable } from './Tile';

export type { GameObj } from './GameObj';
export { createObject, isCollectible } from './GameObj';

export type { Cell } from './Cell';
export { createCell, createEmptyCell } from './Cell';

export type { LevelMap } from './LevelMap';
export { createEmptyMap, createMapFromArray, getCell, setTile, setObject } from './LevelMap';

export type { Player, PlayerInventory } from './Player';
export { createPlayer, movePlayer, setPlayerDirection, setPlayerState } from './Player';

export type { LevelConfig, LevelTarget } from './LevelConfig';
export { createLevelConfig } from './LevelConfig';
```

- [ ] **Step 8: 提交实体定义**

```bash
git add game/assets/core/entity/
git commit -m "feat(core): add entity definitions

- Tile: terrain with walkable check
- GameObj: game objects with collectible check
- Cell: combination of tile and object
- LevelMap: 2D grid with cell operations
- Player: position, direction, state, inventory
- LevelConfig: level configuration with target

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 3: 规则系统

**Files:**
- Create: `game/assets/core/rules/RuleConfig.ts`
- Create: `game/assets/core/rules/bobbyRules.ts`
- Create: `game/assets/core/rules/index.ts`

- [ ] **Step 1: 创建规则配置接口**

```typescript
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
```

- [ ] **Step 2: 创建 Bobby Carrot 规则配置**

```typescript
// game/assets/core/rules/bobbyRules.ts

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
      canWalk: true,
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
```

- [ ] **Step 3: 创建规则导出文件**

```typescript
// game/assets/core/rules/index.ts

export type { RuleConfig, TileRule, ObjectRule, Effect, EffectType } from './RuleConfig';
export { canWalkOnTile, getTileEnterEffects, getObjectCollideEffects } from './RuleConfig';
export { bobbyRules } from './bobbyRules';
```

- [ ] **Step 4: 创建核心层总导出文件**

```typescript
// game/assets/core/index.ts

export * from './types';
export * from './entity';
export * from './rules';
```

- [ ] **Step 5: 提交规则系统**

```bash
git add game/assets/core/rules/ game/assets/core/index.ts
git commit -m "feat(core): add rule system

- RuleConfig: tile and object rule interfaces
- Effect types: slide/push/die/collect/bounce/etc
- bobbyRules: Bobby Carrot specific rule configuration
- canWalkOnTile: check if player can walk
- getTileEnterEffects: get effects when entering tile
- getObjectCollideEffects: get effects on collision

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 4: Cocos 项目初始化

**Files:**
- Initialize Cocos Creator project (手动操作)
- Create: `game/assets/scripts/constants/GameConstants.ts`

**注意：Task 4-8 需要 Cocos Creator 环境。以下代码为完整实现，但需要先安装 Cocos Creator 3.8.8。**

- [ ] **Step 1: 安装 Cocos Creator 3.8.8**

手动操作：
1. 访问 https://www.cocos.com/creator
2. 下载 Cocos Creator 3.8.8 LTS
3. 安装并登录

- [ ] **Step 2: 创建 Cocos 项目**

手动操作：
1. 打开 Cocos Creator
2. 新建项目，选择 "Empty(2D)"
3. 项目路径: `/data/hft/VibeCC/BobbyRemaker/game`
4. 项目名称: `bobby-game`

- [ ] **Step 3: 创建游戏常量文件**

```typescript
// game/assets/scripts/constants/GameConstants.ts

// 格子尺寸（像素）
export const TILE_SIZE = 32;

// 动画帧率
export const ANIMATION_FPS = 8;

// 移动速度（格子/秒）
export const MOVE_SPEED = 4;

// 游戏帧率（FPS）
export const GAME_FPS = 16;

// 帧间隔（毫秒）
export const FRAME_INTERVAL = 1000 / GAME_FPS;

// Z索引
export const Z_INDEX = {
  GROUND: 0,
  OBJECT: 10,
  PLAYER: 20,
  UI: 100
};
```

- [ ] **Step 4: 提交项目初始化**

```bash
git add game/
git commit -m "feat(game): initialize Cocos Creator project

- Cocos Creator 3.8.8 LTS
- Empty 2D template
- GameConstants: tile size, animation, movement, z-index

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 5: 测试关卡数据

**Files:**
- Create: `game/assets/resources/levels/level-1-1.json`

- [ ] **Step 1: 创建测试关卡 JSON**

```json
{
  "id": 1,
  "packId": 1,
  "name": "Level 1-1",
  "width": 10,
  "height": 8,
  "cells": [
    [
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null }
    ],
    [
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": { "type": "start" } },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null }
    ],
    [
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": { "type": "carrot" } },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null }
    ],
    [
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null }
    ],
    [
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": { "type": "carrot" } },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null }
    ],
    [
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null }
    ],
    [
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": { "type": "carrot" } },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": { "type": "door" } }
    ],
    [
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null },
      { "tile": { "type": "ground" }, "object": null }
    ]
  ],
  "target": {
    "carrots": 3
  },
  "startPosition": {
    "x": 1,
    "y": 1
  }
}
```

- [ ] **Step 2: 提交关卡数据**

```bash
git add game/assets/resources/levels/
git commit -m "feat(game): add test level 1-1

- 10x8 grid with ground tiles
- 3 carrots to collect
- Start position at (1, 1)
- Door at (9, 6)

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 6: 格子地图渲染组件

**Files:**
- Create: `game/assets/scripts/components/GridRenderer.ts`

- [ ] **Step 1: 创建格子渲染组件**

```typescript
// game/assets/scripts/components/GridRenderer.ts

import { _decorator, Component, Node, Sprite, Color, UITransform, Vec3 } from 'cc';
import { TILE_SIZE, Z_INDEX } from '../constants/GameConstants';
import { LevelMap, Cell, LevelConfig } from '../../core';

const { ccclass, property } = _decorator;

@ccclass('GridRenderer')
export class GridRenderer extends Component {
  
  @property(Node)
  tileLayer: Node | null = null;
  
  @property(Node)
  objectLayer: Node | null = null;
  
  private _map: LevelMap | null = null;
  private _tileSprites: Map<string, Sprite> = new Map();
  private _objectSprites: Map<string, Sprite> = new Map();
  
  // 临时颜色映射（后续替换为实际精灵图）
  private readonly TILE_COLORS: Record<string, Color> = {
    'ground': new Color(200, 180, 140),
    'ice': new Color(180, 220, 255),
    'conveyer': new Color(255, 200, 100),
    'arrow': new Color(100, 255, 100),
    'death': new Color(255, 100, 100),
    'grass': new Color(100, 200, 100),
    'portal': new Color(200, 100, 255),
    'water': new Color(100, 150, 255),
    'wall': new Color(100, 100, 100)
  };
  
  private readonly OBJECT_COLORS: Record<string, Color> = {
    'carrot': new Color(255, 150, 0),
    'seed': new Color(150, 100, 50),
    'spring': new Color(255, 255, 100),
    'door': new Color(150, 100, 200),
    'flight': new Color(200, 255, 255),
    'bonus': new Color(255, 200, 255),
    'start': new Color(0, 255, 0),
    'rock': new Color(150, 150, 150),
    'mower': new Color(255, 100, 50)
  };
  
  public get map(): LevelMap | null {
    return this._map;
  }
  
  public loadLevel(config: LevelConfig): void {
    this.clearMap();
    this._map = config.map;
    this.renderMap();
  }
  
  private clearMap(): void {
    // 清理现有精灵
    this._tileSprites.forEach(sprite => sprite.node.destroy());
    this._objectSprites.forEach(sprite => sprite.node.destroy());
    this._tileSprites.clear();
    this._objectSprites.clear();
  }
  
  private renderMap(): void {
    if (!this._map || !this.tileLayer || !this.objectLayer) return;
    
    const map = this._map;
    
    for (let y = 0; y < map.height; y++) {
      for (let x = 0; x < map.width; x++) {
        const cell = map.cells[y][x];
        this.renderCell(x, y, cell);
      }
    }
  }
  
  private renderCell(x: number, y: number, cell: Cell): void {
    const worldPos = this.gridToWorld(x, y);
    
    // 渲染地形
    if (cell.tile) {
      this.createTileSprite(x, y, cell.tile.type, worldPos);
    }
    
    // 渲染道具
    if (cell.object && cell.object.visible !== false) {
      this.createObjectSprite(x, y, cell.object.type, worldPos);
    }
  }
  
  private createTileSprite(x: number, y: number, type: string, worldPos: Vec3): void {
    if (!this.tileLayer) return;
    
    const node = new Node(`tile_${x}_${y}`);
    node.setParent(this.tileLayer);
    node.setPosition(worldPos);
    node.addComponent(UITransform).setContentSize(TILE_SIZE, TILE_SIZE);
    
    const sprite = node.addComponent(Sprite);
    sprite.color = this.TILE_COLORS[type] || new Color(200, 200, 200);
    
    this._tileSprites.set(`${x}_${y}`, sprite);
  }
  
  private createObjectSprite(x: number, y: number, type: string, worldPos: Vec3): void {
    if (!this.objectLayer) return;
    
    const node = new Node(`obj_${x}_${y}`);
    node.setParent(this.objectLayer);
    node.setPosition(worldPos);
    node.addComponent(UITransform).setContentSize(TILE_SIZE * 0.8, TILE_SIZE * 0.8);
    
    const sprite = node.addComponent(Sprite);
    sprite.color = this.OBJECT_COLORS[type] || new Color(255, 255, 255);
    
    this._objectSprites.set(`${x}_${y}`, sprite);
  }
  
  public gridToWorld(x: number, y: number): Vec3 {
    return new Vec3(
      x * TILE_SIZE + TILE_SIZE / 2,
      -y * TILE_SIZE - TILE_SIZE / 2,
      0
    );
  }
  
  public worldToGrid(worldX: number, worldY: number): { x: number; y: number } {
    return {
      x: Math.floor(worldX / TILE_SIZE),
      y: Math.floor(-worldY / TILE_SIZE)
    };
  }
  
  public getAdjacentCell(x: number, y: number, dx: number, dy: number): Cell | null {
    if (!this._map) return null;
    
    const newX = x + dx;
    const newY = y + dy;
    
    if (newX < 0 || newX >= this._map.width || newY < 0 || newY >= this._map.height) {
      return null;
    }
    
    return this._map.cells[newY][newX];
  }
}
```

- [ ] **Step 2: 提交格子渲染组件**

```bash
git add game/assets/scripts/components/GridRenderer.ts
git commit -m "feat(game): add GridRenderer component

- Render LevelMap to Cocos nodes
- Tile and object layers separation
- Grid to world coordinate conversion
- Temporary color mapping (sprites later)

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 7: 玩家精灵组件

**Files:**
- Create: `game/assets/scripts/components/PlayerSprite.ts`

- [ ] **Step 1: 创建玩家精灵组件**

```typescript
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
  
  // 临时颜色（后续替换为动画精灵）
  private readonly PLAYER_COLOR = new Color(255, 200, 150);
  
  public get player(): Player | null {
    return this._player;
  }
  
  public init(player: Player, gridRenderer: { gridToWorld: (x: number, y: number) => Vec3 }): void {
    this._player = player;
    
    // 创建精灵
    const transform = this.node.getComponent(UITransform) || this.node.addComponent(UITransform);
    transform.setContentSize(TILE_SIZE * 0.6, TILE_SIZE * 0.6);
    
    this._sprite = this.node.getComponent(Sprite) || this.node.addComponent(Sprite);
    this._sprite.color = this.PLAYER_COLOR;
    
    // 设置位置
    const worldPos = gridRenderer.gridToWorld(player.x, player.y);
    this.node.setPosition(worldPos);
    
    // 设置层级
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
    // 后续添加朝向动画
  }
  
  public setState(state: PlayerState): void {
    if (this._player) {
      this._player.state = state;
    }
    // 后续添加状态动画
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
```

- [ ] **Step 2: 提交玩家精灵组件**

```bash
git add game/assets/scripts/components/PlayerSprite.ts
git commit -m "feat(game): add PlayerSprite component

- Player visualization and movement
- Tween-based smooth movement
- Direction and state management
- Death and collect animations

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 8: 触摸控制组件

**Files:**
- Create: `game/assets/scripts/components/TouchController.ts`

- [ ] **Step 1: 创建触摸控制组件**

```typescript
// game/assets/scripts/components/TouchController.ts

import { _decorator, Component, Node, EventTouch, Vec2, Vec3, UITransform } from 'cc';
import { Direction, getDirectionDelta } from '../../core';

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
      return null; // 点击，不触发移动
    }
    
    const angle = Math.atan2(dy, dx) * 180 / Math.PI;
    
    if (angle >= -45 && angle < 45) return 'right';
    if (angle >= 45 && angle < 135) return 'up';
    if (angle >= 135 || angle < -135) return 'left';
    if (angle >= -135 && angle < -45) return 'down';
    
    return null;
  }
  
  public getAdjacentGridFromTouch(
    playerX: number, 
    playerY: number, 
    touchPos: Vec2,
    gridRenderer: { worldToGrid: (x: number, y: number) => { x: number; y: number } }
  ): { x: number; y: number; direction: Direction } | null {
    // 获取触摸位置的网格坐标
    const gridPos = gridRenderer.worldToGrid(touchPos.x, touchPos.y);
    
    // 检查是否为相邻格子
    const dx = gridPos.x - playerX;
    const dy = gridPos.y - playerY;
    
    // 必须是相邻格子（曼哈顿距离为1）
    if (Math.abs(dx) + Math.abs(dy) !== 1) {
      return null;
    }
    
    // 确定方向
    let direction: Direction;
    if (dx === 1) direction = 'right';
    else if (dx === -1) direction = 'left';
    else if (dy === -1) direction = 'up';
    else direction = 'down';
    
    return { x: gridPos.x, y: gridPos.y, direction };
  }
}
```

- [ ] **Step 2: 提交触摸控制组件**

```bash
git add game/assets/scripts/components/TouchController.ts
git commit -m "feat(game): add TouchController component

- Swipe direction detection
- Touch area management
- Adjacent grid click detection
- Delegate pattern for input handling

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 9: 碰撞系统

**Files:**
- Create: `game/assets/scripts/systems/CollisionSystem.ts`

- [ ] **Step 1: 创建碰撞系统**

```typescript
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
    
    // 边界检查
    if (newX < 0 || newX >= map.width || newY < 0 || newY >= map.height) {
      return {
        canMove: false,
        targetCell: null,
        effects: []
      };
    }
    
    const targetCell = map.cells[newY][newX];
    const effects: Array<{ type: string; params?: any }> = [];
    
    // 地形可行走性检查
    if (!canWalkOnTile(this._ruleConfig, player, targetCell.tile)) {
      return {
        canMove: false,
        targetCell,
        effects: []
      };
    }
    
    // 收集地形效果
    const tileEffects = getTileEnterEffects(this._ruleConfig, targetCell.tile);
    effects.push(...tileEffects);
    
    // 收集道具效果
    const objectEffects = getObjectCollideEffects(this._ruleConfig, targetCell.object);
    effects.push(...objectEffects);
    
    return {
      canMove: true,
      targetCell,
      effects
    };
  }
  
  public checkLevelComplete(
    map: LevelMap,
    player: Player,
    carrotsCollected: number,
    targetCarrots: number
  ): boolean {
    // 检查是否收集了足够胡萝卜
    if (carrotsCollected < targetCarrots) {
      return false;
    }
    
    // 检查是否在门的位置
    const cell = map.cells[player.y]?.[player.x];
    if (!cell || !cell.object) {
      return false;
    }
    
    return cell.object.type === 'door';
  }
}
```

- [ ] **Step 2: 提交碰撞系统**

```bash
git add game/assets/scripts/systems/CollisionSystem.ts
git commit -m "feat(game): add CollisionSystem

- Movement collision check with boundary
- Tile walkability check using rule config
- Effect collection from tiles and objects
- Level completion check

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 10: 收集系统

**Files:**
- Create: `game/assets/scripts/systems/CollectionSystem.ts`

- [ ] **Step 1: 创建收集系统**

```typescript
// game/assets/scripts/systems/CollectionSystem.ts

import { LevelMap, Player, GameObj, isCollectible } from '../../core';

export interface CollectionResult {
  collected: boolean;
  itemType: string | null;
  cell: { x: number; y: number } | null;
}

export class CollectionSystem {
  
  public tryCollect(
    map: LevelMap,
    player: Player
  ): CollectionResult {
    const cell = map.cells[player.y]?.[player.x];
    
    if (!cell || !cell.object) {
      return {
        collected: false,
        itemType: null,
        cell: null
      };
    }
    
    const obj = cell.object;
    
    if (!isCollectible(obj)) {
      return {
        collected: false,
        itemType: null,
        cell: null
      };
    }
    
    // 标记为不可见
    obj.visible = false;
    
    return {
      collected: true,
      itemType: obj.type,
      cell: { x: player.x, y: player.y }
    };
  }
  
  public applyEffect(
    player: Player,
    effectType: string,
    params?: any
  ): void {
    switch (effectType) {
      case 'collect':
        if (params?.item === 'seed') {
          player.inventory.seeds++;
        }
        break;
      
      case 'pickup':
        if (params?.item === 'seed') {
          player.inventory.seeds++;
        }
        break;
    }
  }
}
```

- [ ] **Step 2: 提交收集系统**

```bash
git add game/assets/scripts/systems/CollectionSystem.ts
git commit -m "feat(game): add CollectionSystem

- Collect items from current cell
- Mark objects as invisible
- Apply effect to player inventory

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 11: 移动系统

**Files:**
- Create: `game/assets/scripts/systems/MovementSystem.ts`

- [ ] **Step 1: 创建移动系统**

```typescript
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
    if (this._isProcessing) {
      return;
    }
    
    this._isProcessing = true;
    
    // 更新玩家朝向
    player.direction = direction;
    
    // 检查碰撞
    const collision = collisionCheck();
    
    if (collision.canMove) {
      const delta = getDirectionDelta(direction);
      await moveExecutor(delta.dx, delta.dy, collision.effects);
    }
    
    this._isProcessing = false;
  }
  
  public processEffects(
    player: Player,
    effects: Array<{ type: string; params?: any }>,
    callbacks: {
      onSlide?: () => Promise<void>;
      onDie?: () => Promise<void>;
      onBounce?: (height: number) => Promise<void>;
      onTeleport?: () => Promise<void>;
    }
  ): Promise<void> {
    return new Promise(async (resolve) => {
      for (const effect of effects) {
        switch (effect.type) {
          case 'slide':
            if (callbacks.onSlide) {
              await callbacks.onSlide();
            }
            break;
          
          case 'die':
            player.state = 'dying';
            if (callbacks.onDie) {
              await callbacks.onDie();
            }
            break;
          
          case 'bounce':
            if (callbacks.onBounce) {
              await callbacks.onBounce(effect.params?.height || 2);
            }
            break;
          
          case 'teleport':
            if (callbacks.onTeleport) {
              await callbacks.onTeleport();
            }
            break;
        }
      }
      resolve();
    });
  }
}
```

- [ ] **Step 2: 提交移动系统**

```bash
git add game/assets/scripts/systems/MovementSystem.ts
git commit -m "feat(game): add MovementSystem

- Process player movement with collision
- Direction update before move
- Effect processing (slide/die/bounce/teleport)
- Async movement handling

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 12: 关卡管理器

**Files:**
- Create: `game/assets/scripts/managers/LevelManager.ts`

- [ ] **Step 1: 创建关卡管理器**

```typescript
// game/assets/scripts/managers/LevelManager.ts

import { _decorator, Component, JsonAsset, resources, Node } from 'cc';
import { 
  LevelConfig, LevelMap, Player, createPlayer, 
  createEmptyMap, createLevelConfig,
  Direction 
} from '../../core';

const { ccclass, property } = _decorator;

@ccclass('LevelManager')
export class LevelManager extends Component {
  
  @property({ type: JsonAsset })
  levelAsset: JsonAsset | null = null;
  
  private _currentLevel: LevelConfig | null = null;
  private _player: Player | null = null;
  private _carrotsCollected: number = 0;
  
  public get currentLevel(): LevelConfig | null {
    return this._currentLevel;
  }
  
  public get player(): Player | null {
    return this._player;
  }
  
  public get carrotsCollected(): number {
    return this._carrotsCollected;
  }
  
  public get targetCarrots(): number {
    return this._currentLevel?.target.carrots || 0;
  }
  
  public async loadLevel(levelId: number): Promise<LevelConfig | null> {
    return new Promise((resolve) => {
      if (this.levelAsset) {
        // 从编辑器资源加载
        const data = this.levelAsset.json as any;
        this.parseLevelData(data);
        resolve(this._currentLevel);
      } else {
        // 从 resources 目录加载
        resources.load(`levels/level-${levelId}`, JsonAsset, (err, asset) => {
          if (err) {
            console.error('Failed to load level:', err);
            resolve(null);
            return;
          }
          const data = (asset as JsonAsset).json as any;
          this.parseLevelData(data);
          resolve(this._currentLevel);
        });
      }
    });
  }
  
  private parseLevelData(data: any): void {
    const map = this.parseMap(data);
    
    this._currentLevel = createLevelConfig(
      data.id || 1,
      data.packId || 1,
      data.name || 'Unnamed',
      map,
      data.target?.carrots || 0
    );
    
    // 创建玩家
    if (data.startPosition) {
      this._player = createPlayer(
        data.startPosition.x,
        data.startPosition.y,
        'down' as Direction
      );
    } else {
      // 查找起点标记
      const startPos = this.findStartPosition(map);
      this._player = createPlayer(startPos.x, startPos.y, 'down' as Direction);
    }
    
    this._carrotsCollected = 0;
  }
  
  private parseMap(data: any): LevelMap {
    const width = data.width || 10;
    const height = data.height || 8;
    const map = createEmptyMap(width, height);
    
    if (!data.cells) return map;
    
    for (let y = 0; y < height && y < data.cells.length; y++) {
      for (let x = 0; x < width && x < data.cells[y].length; x++) {
        const cellData = data.cells[y][x];
        
        if (cellData.tile) {
          map.cells[y][x].tile = {
            type: cellData.tile.type,
            direction: cellData.tile.direction,
            state: cellData.tile.state
          };
        }
        
        if (cellData.object) {
          map.cells[y][x].object = {
            type: cellData.object.type,
            state: cellData.object.state,
            visible: cellData.object.visible !== false
          };
        }
      }
    }
    
    return map;
  }
  
  private findStartPosition(map: LevelMap): { x: number; y: number } {
    for (let y = 0; y < map.height; y++) {
      for (let x = 0; x < map.width; x++) {
        if (map.cells[y][x].object?.type === 'start') {
          return { x, y };
        }
      }
    }
    return { x: 1, y: 1 }; // 默认位置
  }
  
  public collectCarrot(): void {
    this._carrotsCollected++;
  }
  
  public resetLevel(): void {
    this._carrotsCollected = 0;
    if (this._currentLevel) {
      const startPos = this.findStartPosition(this._currentLevel.map);
      this._player = createPlayer(startPos.x, startPos.y, 'down' as Direction);
    }
  }
}
```

- [ ] **Step 2: 提交关卡管理器**

```bash
git add game/assets/scripts/managers/LevelManager.ts
git commit -m "feat(game): add LevelManager

- Load level from JSON asset
- Parse level data to LevelConfig
- Player initialization and reset
- Carrot collection tracking

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 13: 游戏管理器

**Files:**
- Create: `game/assets/scripts/managers/GameManager.ts`

- [ ] **Step 1: 创建游戏管理器**

```typescript
// game/assets/scripts/managers/GameManager.ts

import { _decorator, Component, Node, director } from 'cc';
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
  
  @property(Node)
  hud: Node | null = null;
  
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
    
    // 渲染地图
    if (this.gridRenderer) {
      this.gridRenderer.loadLevel(level);
    }
    
    // 初始化玩家
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
        // 执行移动
        if (this.playerSprite && this.gridRenderer) {
          const newX = this.levelManager!.player!.x + dx;
          const newY = this.levelManager!.player!.y + dy;
          await this.playerSprite.moveTo(newX, newY, this.gridRenderer!);
        }
        
        // 处理收集
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
        
        // 处理效果
        await this._movementSystem!.processEffects(
          this.levelManager!.player!,
          effects,
          {
            onDie: async () => {
              if (this.playerSprite) {
                await this.playerSprite.playDeathAnimation();
              }
              // 重置关卡
              this.levelManager!.resetLevel();
              await this.loadLevel(this.levelManager!.currentLevel!.id);
            }
          }
        );
        
        // 检查过关
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
      // 后续：显示过关UI，加载下一关
    }
  }
  
  private updateHUD(): void {
    if (!this.levelManager) return;
    console.log(`Carrots: ${this.levelManager.carrotsCollected}/${this.levelManager.targetCarrots}`);
  }
}
```

- [ ] **Step 2: 提交游戏管理器**

```bash
git add game/assets/scripts/managers/GameManager.ts
git commit -m "feat(game): add GameManager

- Orchestrates all game systems
- TouchController delegate implementation
- Level loading and player initialization
- Movement, collection, and effect processing
- Level completion check

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 14: 场景配置

**Files:**
- Create: `game/assets/scenes/Game.scene` (手动在编辑器中创建)
- Create: `game/assets/prefabs/Cell.prefab` (手动)
- Create: `game/assets/prefabs/Player.prefab` (手动)

**注意：以下步骤需要在 Cocos Creator 编辑器中手动完成。**

- [ ] **Step 1: 创建游戏场景**

手动操作：
1. 打开 Cocos Creator
2. 在 `assets/scenes/` 下创建新场景 `Game.scene`
3. 场景结构：
   ```
   Game (Scene)
   ├── Canvas
   │   ├── Camera
   │   ├── TouchArea (空节点，用于接收触摸)
   │   ├── TileLayer (空节点，存放地形)
   │   ├── ObjectLayer (空节点，存放道具)
   │   └── Player (空节点或预制体)
   └── GameManager (空节点)
   ```

- [ ] **Step 2: 配置节点组件**

手动操作：
1. 选中 `Canvas/TouchArea`
   - 添加 `UITransform` 组件
   - 设置 `Content Size` 为屏幕尺寸
   - 添加 `BlockInputEvents` 组件（可选）

2. 选中 `GameManager`
   - 添加 `GameManager` 脚本组件
   - 配置各属性引用：
     - `Level Manager`: LevelManager 组件
     - `Grid Renderer`: GridRenderer 组件
     - `Player Sprite`: PlayerSprite 组件
     - `Touch Controller`: TouchController 组件

3. 选中 `Canvas`
   - 添加 `GridRenderer` 组件
   - 设置 `Tile Layer` 和 `Object Layer` 引用

- [ ] **Step 3: 添加控制组件**

手动操作：
1. 选中 `Canvas/TouchArea`
   - 添加 `TouchController` 组件
   - 设置 `Touch Area` 为自身

2. 在 `Canvas/Player` 节点
   - 添加 `PlayerSprite` 组件

3. 在 `GameManager` 节点
   - 添加 `LevelManager` 组件
   - 设置 `Level Asset` 为 `level-1-1.json`

- [ ] **Step 4: 设置层级**

手动操作：
1. 确保 `TileLayer` 的 ZIndex 为 0
2. 确保 `ObjectLayer` 的 ZIndex 为 10
3. 确保 `Player` 的 ZIndex 为 20

- [ ] **Step 5: 保存场景**

手动操作：
1. Ctrl+S 保存场景
2. 确保所有引用正确

- [ ] **Step 6: 提交场景配置**

```bash
git add game/assets/scenes/ game/assets/prefabs/
git commit -m "feat(game): configure Game scene

- Canvas with touch area
- Tile and object layers
- Player node with components
- GameManager with all system references

Co-Authored-By: Claude Opus 4.7 <noreply@anthropic.com>"
```

---

## Task 15: 构建与测试

- [ ] **Step 1: Web 构建测试**

手动操作：
1. 打开 Cocos Creator
2. 菜单: `Project -> Build`
3. 选择 `Web Mobile`
4. 点击 `Build`
5. 构建完成后点击 `Run`

预期：
- 浏览器打开游戏页面
- 显示 10x8 格子地图（彩色方块）
- 显示玩家（肉色方块）
- 触摸/点击可移动玩家
- 收集胡萝卜时控制台输出进度
- 收集 3 个胡萝卜后到达门位置显示过关

- [ ] **Step 2: 微信小游戏构建测试**

手动操作：
1. 菜单: `Project -> Build`
2. 选择 `WeChat Mini Game`
3. 配置微信开发者工具路径
4. 点击 `Build`
5. 在微信开发者工具中预览

- [ ] **Step 3: 修复可能的问题**

常见问题排查：
1. 如果触摸无响应：
   - 检查 TouchArea 是否正确配置 UITransform
   - 检查 TouchController 的 touchArea 引用

2. 如果玩家不显示：
   - 检查 PlayerSprite 组件是否正确初始化
   - 检查 gridRenderer 引用是否正确

3. 如果碰撞检测异常：
   - 检查 LevelConfig 是否正确解析
   - 检查 bobbyRules 导入是否正确

---

## P0 完成标准

| 功能 | 验证方式 | 状态 |
|------|---------|------|
| 格子地图渲染 | 显示彩色方块网格 | [ ] |
| 玩家显示 | 显示肉色方块代表玩家 | [ ] |
| 触摸移动 | 滑动或点击相邻格子移动 | [ ] |
| 胡萝卜收集 | 控制台输出收集进度 | [ ] |
| 过关判定 | 收集足够胡萝卜并到达门显示过关 | [ ] |
| Web 构建 | 浏览器可运行 | [ ] |
| 微信小游戏构建 | 微信开发者工具可运行 | [ ] |

---

## 后续阶段

- **P1: 关卡系统** - 多关卡支持、关卡选择UI、关卡切换
- **P2: 道具交互** - 传送带、冰面滑行、弹簧弹跳、传送门
- **P3: 编辑器** - Web 关卡编辑器、导出导入 JSON
- **P4: 完善** - 主菜单、暂停菜单、存档、音效
- **P5: 发布** - iOS/Android 打包、测试、上线

---

**计划结束**
