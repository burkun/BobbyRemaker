# Bobby Carrot 5 逆向重构总结

## 项目完成状态

**原始代码**: a.java (6,494行混淆单类)  
**重构代码**: 18个独立类, 约6,000行代码  
**完成度**: 约90%

## 重构成果

### 核心模块 (已完成)

| 模块 | 说明 | 状态 |
|------|------|------|
| **BobbyGame** | 主游戏集成类,协调所有模块 | ✅ |
| **GameLoop** | 游戏主循环,帧率控制(~16fps) | ✅ |
| **GameLogic** | 游戏逻辑,H(),M()方法 | ✅ |
| **GameRenderer** | 渲染系统,paint(),a(Graphics),b(Graphics) | ✅ |
| **CollisionHandler** | 碰撞检测,a(int,int,boolean)核心算法 | ✅ |
| **LevelInteraction** | 关卡交互,J(),K(),L()方法 | ✅ |
| **Player** | 玩家状态,N(),O()动画更新 | ✅ |
| **Camera** | 摄像机控制,视野滚动 | ✅ |
| **LevelData** | 关卡数据管理,cl[][],cm[][] | ✅ |
| **AudioManager** | 音频管理,MIDI播放 | ✅ |
| **SaveManager** | 存档管理,RMS操作 | ✅ |
| **InputHandler** | 输入处理,秘籍检测 | ✅ |
| **MenuSystem** | 菜单系统 | ✅ |
| **SpecialLevelMode** | 特殊关卡(奖励/飞行) | ✅ |

### 常量类 (已完成)

| 类 | 说明 |
|----|------|
| TileType | 瓦片类型常量 |
| ObjectType | 对象类型常量 |
| Direction | 方向常量 |
| GameState | 游戏状态常量 |

## 方法映射表

### 游戏循环

| 原方法 | 新方法 | 功能 |
|--------|--------|------|
| run() | run() | 主游戏循环 |
| b() | update() | 状态机更新 |
| hideNotify() | onPause() | 暂停处理 |
| showNotify() | onResume() | 恢复处理 |
| d() | initialize() | 游戏初始化 |

### 游戏逻辑

| 原方法 | 新方法 | 功能 |
|--------|--------|------|
| H() | updateGameState() | 游戏状态更新 |
| M() | handlePlayerMovement() | 玩家移动处理 |
| N() | updateMovement() | 移动位置更新 |
| O() | updateAnimation() | 动画帧更新 |
| P() | updateActiveEntities() | 活动实体更新 |

### 碰撞检测

| 原方法 | 新方法 | 功能 |
|--------|--------|------|
| a(int,int,boolean) | canMoveTo() | 核心碰撞检测 |
| b(int,int) | getTileAt() | 获取瓦片类型 |
| c(int,int) | getObjectAt() | 获取对象类型 |
| a(int,int) | checkEntityAtPixel() | 像素级实体检测 |

### 关卡交互

| 原方法 | 新方法 | 功能 |
|--------|--------|------|
| J() | handleTileInteraction() | 瓦片交互处理 |
| K() | handleDeath() | 死亡处理 |
| L() | resetAllTiles() | 重置所有瓦片 |
| c(int) | rotateConveyers() | 旋转传送带 |
| a(byte) | swapDiagonalTiles() | 交换对角瓦片 |

### 渲染系统

| 原方法 | 新方法 | 功能 |
|--------|--------|------|
| paint(Graphics) | render() | 主渲染方法 |
| a(Graphics) | renderGameWorld() | 渲染游戏世界 |
| b(Graphics) | renderHUD() | 渲染UI元素 |
| c(Graphics) | renderTransition() | 渲染过渡效果 |

### 音频管理

| 原方法 | 新方法 | 功能 |
|--------|--------|------|
| a(String,int,boolean) | playMidi() | 播放MIDI |
| a() | stopSoundtrack() | 停止音乐 |
| I() | getLevelMusic() | 获取关卡音乐 |

### 存档管理

| 原方法 | 新方法 | 功能 |
|--------|--------|------|
| e() | initOrValidate() | 初始化存档 |
| f() | deleteAll() | 删除存档 |
| g() | load() | 加载存档 |
| h() | save() | 保存存档 |
| i() | serialize() | 序列化存档 |

### 菜单系统

| 原方法 | 新方法 | 功能 |
|--------|--------|------|
| C() | updatePauseMenu() | 暂停菜单更新 |
| E() | updateConfirmDialog() | 确认对话框 |
| k() | handleMainMenu() | 主菜单处理 |
| l() | handleSubMenu() | 子菜单处理 |

### 特殊关卡

| 原方法 | 新方法 | 功能 |
|--------|--------|------|
| n() | initBonusLevel() | 初始化奖励关卡 |
| o() | updateBonusLevel() | 更新奖励关卡 |
| p() | initFlyingLevel() | 初始化飞行关卡 |
| r() | updateFlyingLevel() | 更新飞行关卡 |

## 字段映射表

### 存档字段

| 原字段 | 新字段 | 类型 | 说明 |
|--------|--------|------|------|
| m | languageCode | String | 语言代码 |
| o[4] | levelUnlockStates | byte[] | 关卡解锁状态 |
| p[4] | levelCompleteFlags | boolean[] | 关卡完成标志 |
| r[7] | achievementFlags | byte[] | 成就标志 |
| s | isSoundEnabled | boolean | 音效开关 |
| t | isMusicEnabled | boolean | 音乐开关 |
| v | difficulty | byte | 难度等级 |
| w | coins | short | 金币数量 |
| x | score | short | 得分 |
| y | gameTime | long | 游戏时间 |
| z | randomSeed | int | 随机种子 |
| A[5] | playerNames | String[] | 玩家名称 |

### 玩家字段

| 原字段 | 新字段 | 类型 | 说明 |
|--------|--------|------|------|
| ag | pixelX | int | 像素X坐标 |
| ah | pixelY | int | 像素Y坐标 |
| ai | tileX | int | 瓦片X坐标 |
| aj | tileY | int | 瓦片Y坐标 |
| an | direction | int | 朝向(0-6) |
| ap | moveProgress | int | 移动进度(0-32) |
| am | animFrame | int | 动画帧 |
| aE | pushForce | int | 推动力 |
| aZ | isFlying | boolean | 飞行状态 |
| bd | isDying | boolean | 死亡状态 |

### 摄像机字段

| 原字段 | 新字段 | 类型 | 说明 |
|--------|--------|------|------|
| bz | cameraX | int | 摄像机X偏移 |
| bA | cameraY | int | 摄像机Y偏移 |
| bB | maxCameraX | int | 最大X偏移 |
| bC | maxCameraY | int | 最大Y偏移 |
| i | screenWidth | int | 屏幕宽度 |
| j | screenHeight | int | 屏幕高度 |
| k | playAreaHeight | int | 游戏区域高度 |

### 关卡字段

| 原字段 | 新字段 | 类型 | 说明 |
|--------|--------|------|------|
| cl[][] | tileMap | byte[][] | 瓦片地图 |
| cm[][] | objectMap | byte[][] | 对象地图 |
| dn | mapWidth | int | 地图宽度 |
| bM | levelPackId | int | 关卡包ID |
| bL | levelId | int | 关卡ID |
| bN | totalLevels | int | 总关卡数 |
| bO | carrotsCollected | int | 收集的胡萝卜 |

## 游戏机制详解

### 碰撞检测规则

1. **边界检测**: 坐标必须在有效范围内
2. **当前瓦片限制**: 传送带/箭头强制方向
3. **传送门检查**: 飞行状态可进入传送门
4. **瓦片可行走性**: 值94-200为可行走
5. **特殊瓦片**:
   - 草地(-57,-56): 仅飞行可进入
   - 冰面(-108): 触发滑行
   - 死亡陷阱(-106,-81): 触发死亡
   - 传送门(-90,-94): 传送点
6. **对象交互**: 根据对象类型处理收集/触发

### 关卡交互规则

1. **恢复保存位置**: 处理上一帧的延迟状态更新
2. **对象收集**: 胡萝卜、种子、飞行道具
3. **地形效果**: 冰面滑行、传送带推动、开关切换
4. **特殊交互**: 弹簧弹跳、割草机、奖励门
5. **死亡判定**: 触发死亡动画

### 动画系统

- **行走**: 8帧循环
- **滑行**: 固定帧1
- **死亡**: 前后摆动 (0→3→0)
- **飞行**: 2帧循环
- **掉落**: 10帧动画
- **割草**: 9帧循环

## 编译与运行

### 依赖

```bash
# J2ME SDK (如需在真机运行)
# Java JDK 8+
```

### 编译

```bash
cd project/src/main/java
javac -d ../../../target *.java
```

### 打包JAR

```bash
jar cvf bobby-refactored.jar -C target .
```

### J2ME模拟器

推荐使用 MicroEmulator 或 KEmulator 运行。

## 结论

本次逆向重构成功将6,494行的混淆代码拆分为18个清晰的类,恢复了约90%的游戏逻辑。核心游戏循环、碰撞检测、关卡交互等关键模块已完成移植。代码具有良好的可读性和可维护性,可作为进一步研究或移植的基础。
