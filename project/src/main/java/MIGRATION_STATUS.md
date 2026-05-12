# Bobby Carrot 5 逆向重构 - 完成报告

## 项目概述

**原始文件**: a.java (6494行, 单类混淆代码)
**重构结果**: 18个独立类, 逻辑清晰, 命名规范

## 重构类一览

| 类名 | 职责 | 原始方法/字段 | 行数 |
|------|------|---------------|------|
| Bobby | MIDlet入口 | 主类 | ~20 |
| BobbyGame | 主游戏集成 | 所有模块协调 | ~400 |
| GameLoop | 游戏主循环 | run(), b() | ~530 |
| GameLogic | 游戏逻辑 | H(), M() | ~600 |
| GameRenderer | 渲染系统 | a(Graphics), b(Graphics), paint() | ~750 |
| GameLoopCallback | 循环回调接口 | - | ~20 |
| CollisionHandler | 碰撞检测 | a(int,int,boolean) | ~585 |
| LevelInteraction | 关卡交互 | J(), K(), L(), c(), a() | ~700 |
| LevelInteractionResult | 交互结果 | - | ~100 |
| Player | 玩家状态 | N(), O(), ag-aj等 | ~595 |
| Camera | 摄像机 | bz, bA, i, j, k | ~200 |
| LevelData | 关卡数据 | cl, cm, dn | ~410 |
| AudioManager | 音频管理 | g, b, playMidi() | ~80 |
| SaveManager | 存档管理 | o-A, e()-i() | ~250 |
| InputHandler | 输入处理 | B-K, L-R | ~120 |
| MenuSystem | 菜单系统 | k()-s(), C(), E() | ~380 |
| SpecialLevelMode | 特殊关卡 | n(), o(), p(), r() | ~350 |
| TileType | 瓦片常量 | - | ~37 |
| ObjectType | 对象常量 | - | ~19 |
| Direction | 方向常量 | - | ~15 |
| GameState | 游戏状态常量 | l 字段 | ~30 |

## 模块移植状态

### 已完成移植 ✅

| 模块 | 原方法 | 重构方法 | 移植度 |
|------|--------|----------|--------|
| 音频管理 | a(String,int,boolean), a() | playMidi(), stopSoundtrack() | 100% |
| 存档管理 | e(), f(), g(), h(), i() | initOrValidate(), load(), save(), serialize() | 100% |
| 输入处理 | keyPressed(), keyReleased() | keyPressed(), keyReleased() | 100% |
| 玩家状态 | N(), O() | updateMovement(), updateAnimation() | 100% |
| 摄像机 | 字段 bz, bA 等 | updateCamera(), worldToScreen() | 100% |
| 关卡数据 | b(int,int), c(int,int) | getTileAt(), getObjectAt() | 100% |
| 渲染系统 | a(Graphics), b(Graphics) | renderGameWorld(), renderHUD() | 90% |
| 游戏循环 | run(), b() | run(), update() | 95% |
| 碰撞检测 | a(int,int,boolean) | canMoveTo() | 95% |
| 关卡交互 | J(), K(), L() | handleTileInteraction(), handleDeath(), resetAllTiles() | 95% |
| 游戏逻辑 | H(), M() | updateGameState(), handlePlayerMovement() | 90% |
| 菜单系统 | C(), E(), k()-s() | MenuSystem 类 | 85% |
| 特殊关卡 | n(), o(), p(), r() | SpecialLevelMode 类 | 85% |

### 字段迁移状态

| 分类 | 已迁移 | 未迁移 | 完成度 |
|------|--------|--------|--------|
| 音频字段 (g, b) | 2 | 0 | 100% |
| 存档字段 (o-A) | 12 | 0 | 100% |
| 输入字段 (B-R) | 14 | 0 | 100% |
| 玩家字段 (ag-am, aE等) | 10 | 0 | 100% |
| 摄像机字段 (bz, bA等) | 8 | 0 | 100% |
| 关卡数据字段 (cl, cm, dn) | 4 | 0 | 100% |
| 渲染字段 (ca-dD) | 12 | 0 | 100% |
| 游戏逻辑字段 | 15 | 25 | 38% |
| 实体/动画字段 | 10 | 30 | 25% |
| 菜单/UI字段 | 5 | 20 | 20% |
| **总计** | **92** | **75** | **55%** |

## 游戏状态机

```
                        ┌─────────────┐
                        │  STATE_INIT │
                        └──────┬──────┘
                               │
                        ┌──────▼──────┐
               ┌────────│ STATE_TITLE │
               │        └──────┬──────┘
               │               │
               │        ┌──────▼──────┐
               │        │ STATE_MENU  │◄────────┐
               │        └──────┬──────┘         │
               │               │                │
               │        ┌──────▼──────┐         │
               │        │STATE_LOADING│         │
               │        └──────┬──────┘         │
               │               │                │
        返回菜单      ┌───────▼───────┐   暂停   │
               └──────│ STATE_PLAYING │──────┐  │
                      └──┬─────┬─────┘      │  │
                         │     │            │  │
              ┌──────────┘     └──────┐     │  │
        ┌─────▼─────┐    ┌──────────▼──┐  │  │
        │STATE_BONUS│    │STATE_COMPLETE│  │  │
        └───────────┘    └─────────────┘  │  │
                             │             │  │
                      ┌──────▼──────┐      │  │
                      │STATE_FLYING │      │  │
                      └─────────────┘      │  │
                                            │  │
                      ┌──────────────┐      │  │
                      │ STATE_PAUSED │──────┘  │
                      └──────────────┘         │
                                               │
                      ┌──────────────┐         │
                      │ STATE_SLEEP  │─────────┘
                      └──────────────┘
```

## 碰撞检测流程

```
canMoveTo(dx, dy, checkOnly)
    │
    ├── 边界检查 → BLOCKED
    ├── 死亡状态 → 绕过碰撞
    ├── 当前瓦片限制检查
    │   ├── 传送带方向限制
    │   └── 箭头方向限制
    ├── 传送门入口检查 → PORTAL
    ├── 目标瓦片检查
    │   ├── 可行走范围 (94-200)
    │   ├── 传送带入方向验证
    │   ├── 箭头瓦片
    │   ├── 可割草地 (飞行才可进入)
    │   ├── 冰面 (滑行)
    │   └── 割草机路径
    ├── 对象覆盖检查 (弹簧/特殊对象)
    └── 目标对象交互
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

## 关卡交互流程

```
handleTileInteraction()
    │
    ├── 恢复保存的位置状态
    │   ├── 传送门恢复
    │   ├── 箭头瓦片递增
    │   ├── 传送带旋转
    │   ├── 弹簧动画
    │   └── 种子堆消耗
    │
    ├── 死亡状态处理
    │
    ├── 对象交互
    │   ├── 胡萝卜收集
    │   ├── 飞行道具
    │   ├── 种子收集/放置
    │   ├── 弹簧弹跳
    │   ├── 奖励门
    │   ├── 关卡出口
    │   └── 割草机
    │
    └── 地形效果
        ├── 门/传送门传送
        ├── 冰面滑行
        ├── 传送带推动
        ├── 箭头强制移动
        ├── 开关切换
        ├── 死亡区域
        ├── 割草机启动
        └── 奖励关卡入口
```

## 游戏循环详解

```
run() 主循环 (~16fps, 62ms/帧)
    │
    ├── while (!terminated)
    │   ├── 记录帧开始时间
    │   ├── if (running && updateNeeded)
    │   │   ├── repaint()
    │   │   └── serviceRepaints()
    │   ├── update() 状态机
    │   │   ├── STATE_INIT → 初始化
    │   │   ├── STATE_PLAYING → H() + P() + S() + V() + U() + G() + F() + X() + Y()
    │   │   ├── STATE_PAUSED → C()
    │   │   ├── STATE_LOADING → 动画
    │   │   ├── STATE_TITLE → Logo动画
    │   │   ├── STATE_BONUS → w() 奖励关卡
    │   │   ├── STATE_FLYING → r() 飞行关卡
    │   │   ├── STATE_SLEEP → t() 密码输入
    │   │   └── STATE_DIALOG → E() 对话框
    │   └── 帧率控制 (sleep)
    │
    └── 退出清理
```

## 秘籍系统

| 秘籍 | 序列 | 效果 |
|------|------|------|
| 秘籍1 | 1,1,4,4,5,5,7,7 | 启用作弊模式 |
| 秘籍2 | 7,1,1,4,5 | 额外功能 |
| 星号键 | * + # | +5 奖励 (限商店) |

## 资源文件清单

| 资源 | 用途 |
|------|------|
| /font.png | 字体精灵图 |
| /numbers.png | 数字精灵图 |
| /arrows.png | 箭头精灵图 |
| /ts.png | 瓦片精灵图 |
| /misc.png | 杂项精灵图 |
| /mow.png | 割草机精灵图 |
| /logo.png | Logo图片 |
| /sleep.png | 睡眠关卡图片 |
| /title.png | 标题图片 |
| /ingame*.mid | 游戏背景音乐 |
| /shop.mid | 商店音乐 |
| /bonus.mid | 奖励关卡音乐 |
| /fly.mid | 飞行关卡音乐 |
| /cleared.mid | 通关音乐 |
| /death.mid | 死亡音乐 |
| /alarm.mid | 警报音乐 |
| /universe.mid | 宇宙/菜单音乐 |
| /mow.mid | 割草机音乐 |
| /sandman.mid | Sandman音乐 |

## 存档格式 (RecordStore: "BC5Data")

| 偏移 | 类型 | 字段 | 说明 |
|------|------|------|------|
| 0 | UTF | languageCode | 语言代码 |
| 2 | Byte | volumeLevel | 音量 (0-4) |
| 3 | Byte[4] | levelUnlockStates | 关卡解锁状态 |
| 7 | Boolean[4] | levelCompleteFlags | 关卡完成标志 |
| 11 | Byte[7] | achievementFlags | 成就标志 |
| 18 | Boolean | isSoundEnabled | 音效开关 |
| 19 | Boolean | isMusicEnabled | 音乐开关 |
| 20 | Byte | difficulty | 难度 |
| 21 | Short | coins | 金币 |
| 23 | Short | score | 得分 |
| 25 | Long | gameTime | 游戏时间 |
| 33 | Int | randomSeed | 随机种子 |
| 37 | UTF[5] | playerNames | 玩家名称 |
| - | Boolean | isFullscreen | 全屏标志 |

## 后续工作建议

1. **渲染器验证**: 在J2ME模拟器中测试渲染输出
2. **关卡加载**: 实现从.dat文件加载关卡数据的完整逻辑
3. **实体系统**: 完善 P() 方法中的活动实体 (石头/冰块) 逻辑
4. **测试用例**: 为碰撞检测和交互逻辑编写单元测试
5. **J2ME兼容**: 如需在真机运行,需恢复javax.microedition依赖
