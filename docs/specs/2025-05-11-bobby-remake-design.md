# Bobby Carrot 5 复刻游戏设计文档

**版本**: 1.0
**日期**: 2025-05-11
**状态**: 待用户确认

---

## 1. 项目概述

### 1.1 目标

使用 Cocos Creator 复刻 Bobby Carrot 5 核心玩法，发布到 iOS、Android、微信小游戏、Web 四个平台。

### 1.2 范围

| 内容 | 包含 | 不包含 |
|------|------|--------|
| 关卡数量 | 主线约 30 关 | 完整 100+ 关 |
| 游戏系统 | 核心玩法、存档、菜单 | 商店、成就、多语言 |
| 美术资源 | 复用原版（后续替换） | 全新绘制 |
| 音频 | MIDI 替换为 MP3 | 原版 MIDI |

### 1.3 目标平台

| 平台 | 优先级 | 发布方式 |
|------|--------|---------|
| 微信小游戏 | P0 | Cocos 原生支持 |
| Web | P0 | Cocos 构建 |
| Android | P1 | Cocos 原生打包 |
| iOS | P1 | Cocos 原生打包 |

---

## 2. 技术选型

### 2.1 游戏引擎

| 技术 | 版本 | 选择理由 |
|------|------|---------|
| **Cocos Creator** | 3.8.8 LTS | 微信小游戏官方推荐，4 端统一，TypeScript 原生支持 |

**版本选择依据**：
- 3.8.8 是最新 LTS 版本（2024 年 8 月发布）
- 完整支持微信小游戏、Web、iOS、Android
- 支持 OpenHarmony 4.0（未来扩展）
- 官方长期维护，文档完善

### 2.2 关卡编辑器

| 技术 | 版本 | 用途 |
|------|------|------|
| React | 18.x | UI 框架 |
| Vite | 5.x | 构建工具 |
| Konva.js | 9.x | 格子画布绘制 |
| Tailwind CSS | 3.x | 样式 |
| Zustand | 4.x | 状态管理 |

### 2.3 开发语言

- **TypeScript 5.x**：用户熟悉，类型安全

### 2.4 版本控制

- Git + GitHub

---

## 3. 项目结构

```
BobbyRemaker/
├── game/                         # Cocos Creator 游戏项目
│   ├── assets/
│   │   ├── core/                 # 共享逻辑（无引擎依赖）
│   │   │   ├── entity/           # 实体定义
│   │   │   ├── types/            # 类型定义
│   │   │   ├── rules/            # 规则配置
│   │   │   └── utils/            # 工具函数
│   │   ├── scripts/              # Cocos 游戏脚本
│   │   │   ├── components/       # 渲染组件（cc.Component）
│   │   │   ├── systems/          # 游戏系统
│   │   │   └── managers/         # 管理器
│   │   ├── scenes/               # 场景文件
│   │   ├── resources/            # 资源文件
│   │   │   ├── sprites/          # 精灵图
│   │   │   ├── audio/            # 音频
│   │   │   └── levels/           # 关卡 JSON
│   │   └── prefabs/              # 预制体
│   ├── settings/                 # 项目设置
│   └── package.json
│
├── editor/                       # Web 关卡编辑器
│   ├── src/
│   │   ├── App.tsx
│   │   ├── canvas/               # 画布组件
│   │   ├── panels/               # 属性面板、图层面板
│   │   ├── toolbar/              # 工具栏
│   │   └── export/               # 导出 JSON
│   ├── package.json
│   └── vite.config.ts
│
├── tools/
│   └── level-extractor/          # 关卡逆向提取工具
│
└── docs/
    └── specs/                    # 设计文档
```

---

## 4. 数据结构设计

### 4.1 格子地图

```typescript
// 单个格子
interface Cell {
  tile: Tile | null;     // 地形层
  object: Object | null; // 道具层
}

// 地形
interface Tile {
  type: TileType;
  direction?: Direction; // 传送带/箭头方向
  state?: number;        // 状态（开关等）
}

// 道具
interface Object {
  type: ObjectType;
  state?: number;        // 状态
  visible?: boolean;     // 是否可见
}

// 地图
interface LevelMap {
  width: number;
  height: number;
  cells: Cell[][];
}
```

### 4.2 玩家状态

```typescript
interface Player {
  x: number;             // 网格坐标
  y: number;
  direction: Direction;  // 朝向: "up" | "down" | "left" | "right"
  state: PlayerState;    // "idle" | "moving" | "flying" | "dying"
  inventory: {
    seeds: number;
  };
}
```

### 4.3 关卡配置

```typescript
interface LevelConfig {
  id: number;
  packId: number;
  name: string;
  map: LevelMap;
  target: {
    carrots: number;     // 需收集的胡萝卜数
  };
  music?: string;
}
```

### 4.4 存档数据

```typescript
interface SaveData {
  version: number;

  // 进度
  currentLevel: number;
  unlockedPacks: number[];
  completedLevels: number[];
  stars: { [levelId: number]: number };

  // 资源
  coins: number;

  // 设置
  soundEnabled: boolean;
  musicEnabled: boolean;
  volume: number;

  // 统计
  totalPlayTime: number;
  totalSteps: number;
}
```

---

## 5. 规则系统设计

### 5.1 核心思想

规则是配置，不是硬编码。换皮时只需修改配置表。

### 5.2 规则配置结构

```typescript
interface RuleConfig {
  tileRules: {
    [tileType: string]: TileRule;
  };
  objectRules: {
    [objectType: string]: ObjectRule;
  };
}

interface TileRule {
  canWalk?: boolean | ((player: Player, tile: Tile) => boolean);
  onEnter?: Effect[];
  onLeave?: Effect[];
}

interface ObjectRule {
  onCollide?: Effect[];
}

interface Effect {
  type: EffectType;
  params?: any;
}
```

### 5.3 Bobby Carrot 规则配置示例

```typescript
const bobbyRules: RuleConfig = {
  tileRules: {
    "ground": { canWalk: true },
    "ice": { canWalk: true, onEnter: [{ type: "slide" }] },
    "conveyer": {
      canWalk: true,
      onEnter: [{ type: "push", params: { direction: "tile.direction" } }]
    },
    "arrow": {
      canWalk: true,
      onEnter: [{ type: "forceMove", params: { direction: "tile.direction" } }]
    },
    "death": { onEnter: [{ type: "die" }] },
    "grass": { canWalk: (p) => p.state === "flying" },
    "portal": { onEnter: [{ type: "teleport" }] }
  },
  objectRules: {
    "carrot": { onCollide: [{ type: "collect", params: { item: "carrot" } }] },
    "seed": { onCollide: [{ type: "pickup", params: { item: "seed" } }] },
    "spring": { onCollide: [{ type: "bounce", params: { height: 2 } }] },
    "door": { onCollide: [{ type: "levelEnd" }] }
  }
};
```

### 5.4 效果处理器

```typescript
class EffectHandler {
  handle(effect: Effect, player: Player, context: GameContext) {
    switch (effect.type) {
      case "slide": /* 滑行逻辑 */
      case "push": /* 推动逻辑 */
      case "collect": /* 收集逻辑 */
      case "bounce": /* 弹跳逻辑 */
      case "die": /* 死亡逻辑 */
      case "teleport": /* 传送逻辑 */
      // ...
    }
  }
}
```

---

## 6. 触屏控制设计

### 6.1 控制方案

| 模式 | 操作 | 说明 |
|------|------|------|
| **点击移动** | 点击相邻格子 | 默认模式，精确控制 |
| **长按连续** | 长按某方向 | 持续移动直到松手或遇障碍 |
| **虚拟方向键** | 点击方向按钮 | 设置中开启，传统玩家适用 |

### 6.2 设置选项

```typescript
interface ControlSettings {
  mode: "tap" | "dpad";
  showPathPreview: boolean;
  longPressMove: boolean;
  quickDoubleTap: boolean;
}
```

### 6.3 触摸检测逻辑

```typescript
class TouchController {
  private touchStart: { x: number; y: number } | null = null;
  private readonly MIN_SWIPE_DISTANCE = 30;

  onTouchStart(x: number, y: number) {
    this.touchStart = { x, y };
  }

  onTouchEnd(x: number, y: number): Direction | null {
    if (!this.touchStart) return null;

    const dx = x - this.touchStart.x;
    const dy = y - this.touchStart.y;
    const distance = Math.sqrt(dx * dx + dy * dy);

    if (distance < this.MIN_SWIPE_DISTANCE) {
      return null; // 点击，不触发移动
    }

    const angle = Math.atan2(dy, dx) * 180 / Math.PI;

    if (angle >= -45 && angle < 45) return "right";
    if (angle >= 45 && angle < 135) return "up";
    if (angle >= 135 || angle < -135) return "left";
    if (angle >= -135 && angle < -45) return "down";

    return null;
  }
}
```

---

## 7. 关卡编辑器设计

### 7.1 功能列表

| 功能 | 优先级 | 说明 |
|------|--------|------|
| 地图画布 | P0 | 可缩放/拖拽的格子网格 |
| 放置/删除 | P0 | 点击放置 tile/object |
| 导出 JSON | P0 | 导出关卡数据 |
| 属性面板 | P1 | 编辑方向、状态等属性 |
| 图层切换 | P1 | 地形层/道具层分开编辑 |
| 撤销/重做 | P2 | 操作历史 |
| 可达性验证 | P2 | 判断关卡是否有解 |
| AI 生成 | P3+ | 后续扩展 |

### 7.2 界面布局

```
┌─────────────────────────────────────────────────────┐
│  工具栏: [放置] [删除] [撤销] [重做] [清空] [导出]   │
├───────────────────┬─────────────────────────────────┤
│                   │                                 │
│   图层面板        │         地图画布                 │
│   ├ 地形层        │     (格子网格，可缩放)           │
│   ├ 道具层        │                                 │
│   └                 │                                 │
│   元素库          │                                 │
│   ├ 地形类型      │                                 │
│   │  [ground]     │                                 │
│   │  [ice]        │                                 │
│   │  [conveyer]   │                                 │
│   ├ 道具类型      │                                 │
│   │  [carrot]     │                                 │
│   │  [seed]       │                                 │
│                   │                                 │
├───────────────────┴─────────────────────────────────┤
│   属性面板: 选中格子后显示                           │
│   类型: conveyer    方向: [↑] [↓] [←] [→]           │
└─────────────────────────────────────────────────────┘
```

### 7.3 导出格式

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
      { "tile": { "type": "ice" }, "object": { "type": "carrot" } }
    ]
  ],
  "target": { "carrots": 3 }
}
```

### 7.4 可达性分析接口（预留）

```typescript
interface LevelAnalyzer {
  isSolvable(level: LevelConfig): boolean;
  getMinSteps(level: LevelConfig): number;
  findDeadlocks(level: LevelConfig): DeadlockState[];
  solve(level: LevelConfig): Move[] | null;
}

interface LevelGenerator {
  generate(params: GenerateParams): LevelConfig;
  validate(level: LevelConfig): boolean;
}
```

---

## 8. 游戏流程设计

### 8.1 状态机

```
启动 → 加载资源 → 主菜单
                  │
        ┌─────────┼─────────┐
        │         │         │
    关卡选择    设置      商店
        │
    加载关卡
        │
     游戏中 ←─── 暂停
        │
     过关 → 下一关
        │
    主菜单
```

### 8.2 存档时机

| 事件 | 操作 |
|------|------|
| 过关 | 更新进度 + 保存 |
| 收集道具 | 更新金币 + 保存 |
| 切换关卡 | 保存当前状态 |
| 设置变更 | 保存设置 |
| 游戏退出 | 保存所有数据 |

### 8.3 存档接口

```typescript
interface SaveManager {
  load(): Promise<SaveData | null>;
  save(data: SaveData): Promise<void>;
  clear(): Promise<void>;
}

// 各平台实现
class WebSaveManager implements SaveManager {
  // localStorage
}

class WechatSaveManager implements SaveManager {
  // wx.getStorageSync / wx.setStorageSync
}

class NativeSaveManager implements SaveManager {
  // Cocos 原生存储 API
}
```

---

## 9. 音频设计

### 9.1 音频格式

原版 MIDI 不支持，替换为 MP3。

| 平台 | 格式 |
|------|------|
| Web | MP3 |
| 微信小游戏 | MP3 |
| iOS / Android | MP3 / AAC |

### 9.2 音频类型

```typescript
interface AudioConfig {
  bgm: {
    menu: string;
    game: string;
    levelEnd: string;
    death: string;
  };
  sfx: {
    collect: string;
    step: string;
    slide: string;
    bounce: string;
    teleport: string;
    unlock: string;
  };
}
```

### 9.3 音频管理接口

```typescript
interface AudioManager {
  playBGM(name: string, loop?: boolean): void;
  stopBGM(): void;
  playSFX(name: string): void;
  setVolume(volume: number): void;
  mute(): void;
  unmute(): void;
}
```

---

## 10. 资源管理

### 10.1 精灵图结构

```typescript
interface SpriteSheet {
  frames: {
    [name: string]: {
      frame: { x: number; y: number; w: number; h: number };
      anchor?: { x: number; y: number };
    };
  };
  meta: {
    image: string;
    size: { w: number; h: number };
  };
}
```

### 10.2 换皮资源包结构

```
themes/candy/
├── tiles.png
├── objects.png
├── player.png
├── ui.png
├── audio/
│   ├── bgm_menu.mp3
│   ├── bgm_game.mp3
│   └── sfx/
│       ├── collect.mp3
│       └── ...
└── theme.json
```

---

## 11. 开发路线图

### 11.1 阶段划分

| 阶段 | 目标 | 产出 | 周期 |
|------|------|------|------|
| **P0: 核心原型** | 跑通基本玩法 | 角色移动、碰撞检测、收集道具 | 2 周 |
| **P1: 关卡系统** | 完整关卡流程 | 关卡加载、过关判定、关卡选择 | 2 周 |
| **P2: 道具交互** | 核心交互逻辑 | 传送带、冰面、弹簧、门等 | 3 周 |
| **P3: 编辑器** | 关卡编辑能力 | Web 编辑器 + 导出导入 | 2 周 |
| **P4: 完善** | 产品化 | 菜单、存档、音效、UI | 2 周 |
| **P5: 发布** | 多平台上线 | 微信小游戏、Web、iOS、Android | 1 周 |

### 11.2 P0 详细任务

1. Cocos Creator 3.8.8 项目初始化
2. 资源导入（从 JAR 提取的精灵图）
3. 格子地图渲染
4. 玩家角色显示 + 移动动画
5. 基础碰撞检测（可行走判定）
6. 点击移动控制
7. 胡萝卜收集 + 过关检测

---

## 12. 扩展性设计

### 12.1 换皮流程

1. 定义新的 `TileType` / `ObjectType`
2. 编写新的 `RuleConfig`
3. 准备新素材（精灵图、音频）
4. 不改核心逻辑代码

### 12.2 2.5D 扩展预留

- 数据结构预留 `zIndex` / `height` 字段
- 渲染层可替换为等角视角
- 逻辑层（碰撞、交互）无需修改

### 12.3 AI 生成预留

- `LevelAnalyzer` 接口定义
- `LevelGenerator` 接口定义
- 编辑器 UI 预留"验证"按钮位置

---

## 附录

### A. 地形类型清单

| 类型 | 常量 | 效果 |
|------|------|------|
| ground | "ground" | 普通地面 |
| ice | "ice" | 滑行 |
| conveyer | "conveyer" | 传送带推动 |
| arrow | "arrow" | 强制方向移动 |
| death | "death" | 死亡陷阱 |
| grass | "grass" | 飞行才能进入 |
| portal | "portal" | 传送门 |

### B. 道具类型清单

| 类型 | 常量 | 效果 |
|------|------|------|
| carrot | "carrot" | 收集目标 |
| seed | "seed" | 可捡起/放置 |
| spring | "spring" | 弹跳 |
| door | "door" | 关卡出口 |
| flight | "flight" | 飞行道具 |
| bonus | "bonus" | 奖励门 |

### C. 平台差异处理

```typescript
import { sys } from 'cc';

if (sys.platform === sys.Platform.WECHAT_GAME) {
  // 微信小游戏特有逻辑
} else if (sys.isNative) {
  // iOS / Android 原生
} else {
  // Web
}
```

---

**文档结束**
