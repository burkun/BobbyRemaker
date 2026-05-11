# Bobby Carrot 5 Java到Web迁移设计文档

## 1. 概述

### 1.1 目标

将Java逆向重构代码迁移到Web原型，最终适配微信小游戏。

### 1.2 约束

- 技术栈：TypeScript + Canvas API
- 目标平台：Web浏览器（开发调试）→ 微信小游戏（生产部署）
- 第一里程碑：Level 1-1 可完整游玩通关

### 1.3 当前状态

| 组件 | Java重构 | Web原型 |
|------|----------|---------|
| 碰撞检测 | ✅ 完整 | ⚠️ 基础 |
| 关卡交互 | ✅ 完整 | ❌ 缺失 |
| 渲染系统 | ✅ 完整 | ⚠️ 基础 |
| 游戏状态机 | ✅ 完整 | ❌ 缺失 |
| 实体系统 | ✅ 完整 | ❌ 缺失 |
| 特殊关卡 | ✅ 完整 | ❌ 缺失 |

## 2. 架构设计

### 2.1 模块结构

```
BobbyGame (主控制器)
├── GameLoop (游戏循环)
│   - 帧率控制 (~16fps, 62ms/帧)
│   - 状态机分发
│   - 暂停/恢复机制
│
├── InputHandler (输入处理)
│   - 方向键映射
│   - 秘籍检测
│
├── Player (玩家状态)
│   - 位置、方向、动画
│   - 移动、死亡、飞行状态
│
├── Camera (摄像机)
│   - 视口滚动
│   - 世界坐标转换
│
├── LevelData (关卡数据)
│   - tileMap[][] / objectMap[][]
│   - 地图加载与解析
│
├── CollisionHandler (碰撞检测)
│   - canMoveTo() 核心算法
│   - 传送带/冰面/传送门逻辑
│
├── LevelInteraction (关卡交互)
│   - 瓦片交互 (开关/弹簧/割草机)
│   - 对象收集 (胡萝卜/种子)
│
├── GameLogic (游戏逻辑)
│   - updateGameState() 状态更新
│   - handlePlayerMovement() 移动处理
│
├── GameRenderer (渲染系统)
│   - 渲染瓦片/对象/玩家
│   - HUD渲染
│   - 过渡效果
│
├── AudioManager (音频管理)
│   - MIDI播放
│
├── SaveManager (存档管理)
│   - LocalStorage操作
│
├── MenuSystem (菜单系统)
│   - 暂停菜单/确认对话框
│
└── SpecialLevelMode (特殊关卡)
    - 奖励关卡/飞行关卡
```

### 2.2 游戏状态机

```
STATE_INIT (0)
    │
    ▼
STATE_TITLE (7) ─────────────────────┐
    │                                 │
    ▼                                 │
STATE_MENU (3)                        │
    │                                 │
    ▼                                 │
STATE_LOADING (6)                     │
    │                                 │
    ▼                                 │
STATE_PLAYING (1) ◄───────────────────┤
    │                                 │
    ├──▶ STATE_PAUSED (2) ────────────┘
    │
    ├──▶ STATE_LEVEL_COMPLETE (4)
    │        │
    │        ▼
    │    STATE_FLYING (10) / STATE_BONUS (9)
    │
    └──▶ STATE_GAME_OVER (5)
             │
             ▼
         STATE_SLEEP (11) / STATE_DIALOG (8)
```

### 2.3 碰撞检测流程

```
canMoveTo(dx, dy, checkOnly)
    │
    ├── 1. 边界检查 → BLOCKED
    │
    ├── 2. 死亡状态 → 绕过碰撞
    │
    ├── 3. 当前瓦片限制检查
    │   ├── 传送带方向限制
    │   └── 箭头方向限制
    │
    ├── 4. 传送门入口检查 → PORTAL
    │
    ├── 5. 目标瓦片检查
    │   ├── 可行走范围 (94-200)
    │   ├── 传送带入方向验证
    │   ├── 箭头瓦片
    │   ├── 可割草地 (飞行才可进入)
    │   ├── 冰面 (滑行)
    │   └── 割草机路径
    │
    ├── 6. 对象覆盖检查 (弹簧/特殊对象)
    │
    └── 7. 目标对象交互
        ├── 胡萝卜 → CARROT
        ├── 种子 → SEED
        ├── 关卡出口 → LEVEL_END
        ├── 飞行道具 → FLIGHT_PICKUP
        ├── 弹簧 → SPRING
        ├── 奖励门 → BONUS_DOOR
        ├── 割草机过渡 → MOWER_PATH
        ├── 死亡触发 → DEATH
        └── 关卡包门 → LEVEL_END
```

## 3. 分层迁移计划

### 3.1 阶段1：核心引擎层

| 模块 | Java类 | TypeScript目标 | 说明 |
|------|--------|---------------|------|
| 游戏循环 | GameLoop | `core/GameLoop.ts` | 帧率控制、状态机分发 |
| 主控制器 | BobbyGame | `core/Game.ts` | 整合所有模块 |
| 输入处理 | InputHandler | `core/InputHandler.ts` | 键盘/触摸、秘籍检测 |
| 状态常量 | GameState | `core/GameState.ts` | 12种游戏状态常量 |

**关键改动**：
- 重构现有`Game.ts`为Java的`BobbyGame`结构
- 实现`GameLoopCallback`接口模式

### 3.2 阶段2：碰撞与交互层

| 模块 | Java类 | TypeScript目标 | 说明 |
|------|--------|---------------|------|
| 碰撞检测 | CollisionHandler | `level/CollisionHandler.ts` | 核心算法迁移 |
| 关卡交互 | LevelInteraction | `level/LevelInteraction.ts` | 瓦片/对象交互 |
| 玩家状态 | Player | `entity/Player.ts` | 完整玩家逻辑 |
| 关卡数据 | LevelData | `level/LevelData.ts` | tileMap/objectMap |

### 3.3 阶段3：渲染层

| 模块 | Java类 | TypeScript目标 | 说明 |
|------|--------|---------------|------|
| 渲染系统 | GameRenderer | `renderer/GameRenderer.ts` | 世界渲染 |
| HUD渲染 | GameRenderer | `renderer/HUDRenderer.ts` | HUD元素 |
| 过渡效果 | GameRenderer | `renderer/TransitionRenderer.ts` | 过渡动画 |
| 摄像机 | Camera | `entity/Camera.ts` | 视口控制 |

**渲染顺序**：
1. 背景/瓦片层
2. 对象层
3. 玩家层
4. HUD层
5. 过渡效果层

### 3.4 阶段4：实体系统

| 模块 | Java类 | TypeScript目标 | 说明 |
|------|--------|---------------|------|
| 活动实体 | GameLogic.P() | `entity/EntityManager.ts` | 石头、冰块、蝴蝶 |
| 实体更新 | GameLogic | `entity/Entity.ts` | 实体状态更新 |

### 3.5 阶段5：特殊关卡

| 模块 | Java类 | TypeScript目标 | 说明 |
|------|--------|---------------|------|
| 奖励关卡 | SpecialLevelMode | `special/BonusLevel.ts` | 奖励关卡逻辑 |
| 飞行关卡 | SpecialLevelMode | `special/FlyingLevel.ts` | 飞行关卡逻辑 |
| 菜单系统 | MenuSystem | `menu/MenuSystem.ts` | 暂停/确认菜单 |

## 4. 文件结构

```
web-prototype/src/
├── core/
│   ├── Game.ts            # 主控制器
│   ├── GameLoop.ts        # 游戏循环
│   ├── GameState.ts       # 状态常量
│   └── InputHandler.ts    # 输入处理
│
├── entity/
│   ├── Player.ts          # 玩家
│   ├── EntityManager.ts   # 实体管理
│   └── Camera.ts          # 摄像机
│
├── level/
│   ├── LevelData.ts       # 关卡数据
│   ├── LevelInteraction.ts # 关卡交互
│   └── CollisionHandler.ts # 碰撞检测
│
├── renderer/
│   ├── GameRenderer.ts    # 游戏渲染
│   ├── HUDRenderer.ts     # HUD渲染
│   └── TransitionRenderer.ts # 过渡效果
│
├── systems/
│   ├── CollisionSystem.ts # 现有，保留
│   ├── MovementSystem.ts  # 现有，保留
│   └── CollectionSystem.ts # 现有，保留
│
├── audio/
│   └── AudioManager.ts    # 音频管理
│
├── save/
│   └── SaveManager.ts     # 存档管理
│
├── menu/
│   └── MenuSystem.ts      # 菜单系统
│
├── special/
│   ├── BonusLevel.ts      # 奖励关卡
│   └── FlyingLevel.ts     # 飞行关卡
│
├── types/
│   └── ...                # 现有类型定义
│
├── rules/
│   └── ...                # 现有规则系统
│
├── assets.ts              # 现有资源加载
└── main.ts                # 入口
```

## 5. 第一里程碑：Level 1-1 可玩

### 5.1 最小功能集

| 优先级 | 模块 | 必需功能 |
|--------|------|----------|
| P0 | GameLoop | 帧率控制、PLAYING状态 |
| P0 | Player | 移动、动画、死亡 |
| P0 | CollisionHandler | 基础碰撞（可行走判断） |
| P0 | LevelData | 关卡加载、tileMap/objectMap |
| P0 | GameRenderer | 瓦片、对象、玩家渲染 |
| P1 | LevelInteraction | 胡萝卜收集、关门判断 |
| P1 | Camera | 视口滚动 |
| P1 | AudioManager | 背景音乐、收集音效 |
| P2 | InputHandler | 方向键、暂停键 |
| P2 | MenuSystem | 暂停菜单（最小化） |

### 5.2 Level 1-1 不需要的功能

- 传送带/箭头强制移动（如果没有这些瓦片）
- 弹簧（如果没有）
- 奖励关卡
- 飞行模式
- 复杂实体（石头、冰块）

## 6. 技术适配要点

### 6.1 微信小游戏适配

- 使用Canvas API，避免DOM
- 音频使用Web Audio API（微信支持）
- 触摸输入统一处理
- 包大小限制：主包4MB

### 6.2 代码风格

- 保持Java的命名风格（如`canMoveTo`而不是`can_move_to`）
- 使用TypeScript接口定义契约
- 避免DOM API，使用Canvas直接绘制

## 7. 方法映射表

### 7.1 游戏循环

| Java方法 | TypeScript方法 | 功能 |
|---------|---------------|------|
| run() | run() | 主游戏循环 |
| update() | update() | 状态机更新 |
| onPause() | onPause() | 暂停处理 |
| onResume() | onResume() | 恢复处理 |

### 7.2 碰撞检测

| Java方法 | TypeScript方法 | 功能 |
|---------|---------------|------|
| canMoveTo() | canMoveTo() | 核心碰撞检测 |
| getTileAt() | getTileAt() | 获取瓦片类型 |
| getObjectAt() | getObjectAt() | 获取对象类型 |

### 7.3 关卡交互

| Java方法 | TypeScript方法 | 功能 |
|---------|---------------|------|
| handleTileInteraction() | handleTileInteraction() | 瓦片交互处理 |
| handleDeath() | handleDeath() | 死亡处理 |
| resetAllTiles() | resetAllTiles() | 重置所有瓦片 |

### 7.4 渲染系统

| Java方法 | TypeScript方法 | 功能 |
|---------|---------------|------|
| render() | render() | 主渲染方法 |
| renderGameWorld() | renderGameWorld() | 渲染游戏世界 |
| renderHUD() | renderHUD() | 渲染UI元素 |

## 8. 后续里程碑

### 8.1 里程碑2：完整游戏流程

- 所有UI和状态机
- 菜单系统完整实现
- 关卡选择界面

### 8.2 里程碑3：全部功能

- 所有特殊关卡
- 完整实体系统
- 微信小游戏适配

---

*文档版本: 1.0*
*创建日期: 2026-05-11*
*作者: Claude Code*
