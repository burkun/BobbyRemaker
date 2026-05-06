# a.java 移植状态分析

## 当前状态
- **原始文件**: 6507行, 109个方法
- **已移植**: 11个新类

## 已移植模块

| 类 | 已移植方法 | 原方法名 |
|----|-----------|---------|
| AudioManager | playMidi, stopSoundtrack | a(String,int,boolean), a() |
| SaveManager | initOrValidate, load, save, serialize | e(), f(), g(), h(), i() |
| InputHandler | keyPressed, keyReleased | keyPressed(), keyReleased() |
| Player | updateMovement, updateAnimation | N(), O() |
| Camera | updateCamera, worldToScreen | 字段 bz, bA, i, j, k, bV, bT |
| LevelData | getTileAt, getObjectAt, loadLevel | b(int,int), c(int,int) |
| GameRenderer | renderGameWorld, renderHUD, paint | a(Graphics), b(Graphics), paint() |

## 未移植模块

### 1. 游戏主循环 (GameLoop) - 高优先级
| 原方法 | 行号 | 功能 |
|--------|------|------|
| `run()` | 460 | 主游戏循环 |
| `b()` | 1473 | 游戏逻辑更新 |
| `hideNotify()` | 435 | 暂停处理 |
| `showNotify()` | 450 | 恢复处理 |

### 2. 碰撞与移动 (CollisionHandler) - 高优先级
| 原方法 | 行号 | 功能 |
|--------|------|------|
| `a(int,int,boolean)` | 3327 | 核心碰撞检测 canMoveTo |
| `b(int,int)` | 3313 | 获取瓦片类型 |
| `c(int,int)` | 3320 | 获取物品类型 |
| `a(int,int)` | 3062 | 位置检测 |

### 3. 关卡交互 (LevelInteraction) - 高优先级
| 原方法 | 行号 | 功能 |
|--------|------|------|
| `J()` | 2656 | 瓦片交互处理 |
| `K()` | 2873 | 死亡处理 |
| `L()` | 2891 | 关卡完成检测 |
| `f(int,int)` | ~2300 | 加载关卡 |

### 4. 游戏状态更新 (GameLogic) - 中优先级
| 原方法 | 行号 | 功能 |
|--------|------|------|
| `H()` | 2352 | 游戏状态更新 |
| `M()` | 3074 | 玩家移动处理 |
| `d()` | 483 | 游戏初始化 |
| `j()` | 717 | 未知初始化 |

### 5. 特殊关卡模式 - 中优先级
| 原方法 | 行号 | 功能 |
|--------|------|------|
| `n()` | 1654 | 奖励关卡初始化 |
| `o()` | 1701 | 奖励关卡更新 |
| `p()` | 1729 | 飞行关卡开始 |
| `r()` | 1767 | 飞行关卡更新 |

### 6. 菜单系统 (MenuSystem) - 低优先级
| 原方法 | 行号 | 功能 |
|--------|------|------|
| `k()` | 1220 | 菜单相关 |
| `l()` | 1463 | 菜单相关 |
| `m()` | 1468 | 菜单相关 |
| `q()` | 1751 | 菜单相关 |
| `s()` | 1798 | 菜单相关 |

### 7. UI辅助方法 - 低优先级
| 原方法 | 行号 | 功能 |
|--------|------|------|
| `c(Graphics,int,int)` | 1643 | 绘图辅助 |
| `d(Graphics,int,int)` | 4932 | 绘图辅助 |
| `e(Graphics)` | 6355 | 选项菜单渲染 |
| `a(Graphics,String,byte,int)` | 5781 | 文字绘制 |

### 8. 数据处理方法
| 原方法 | 行号 | 功能 |
|--------|------|------|
| `a(byte[],int,int)` | 1939 | 字节数组处理 |
| `a(byte,byte,int,int)` | 1374 | 字节操作 |
| `a(byte,int,int)` | 1383 | 字节操作 |

### 9. 未分类方法 (大量单字母方法名)
行号范围: 2000-6500，约80+个方法
- `A()` - `at()` 等大量方法需要逐个分析

## 建议下一步

### 立即移植
1. **GameLoop** - 主循环是核心
2. **CollisionHandler** - 碰撞检测是关键逻辑
3. **LevelInteraction** - 关卡交互是玩法核心

### 后续移植
4. GameLogic - 状态更新
5. 特殊关卡模式
6. MenuSystem - 菜单系统
7. UI辅助方法

## 字段迁移状态

### 已迁移字段
- `g, b` → AudioManager
- `o, p, r, s, t, u, v, w, x, y, z, A` → SaveManager
- `ag, ah, ai, aj, an, ap, am` → Player
- `bz, bA, i, j, k, bV, bT` → Camera
- `cl, cm, dn, bM, bL, bN` → LevelData
- `ca-ci, cj, dC, dD` → GameRenderer

### 未迁移字段 (约150+个)
- 游戏状态标志 (B-I, N-R, by, bS, dx等)
- 精灵/动画字段 (cB-cK, da-dz等)
- 关卡临时变量
- 菜单状态字段
- 碰撞检测临时变量

## 移植优先级

```
高 ████████████ GameLoop + Collision + LevelInteraction
中 ████████      GameLogic + SpecialModes
低 ████          MenuSystem + UIHelpers + DataMethods
```
