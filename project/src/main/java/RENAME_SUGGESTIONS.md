# a.java 反混淆命名建议

## 汇总分析 (4个Agent并行分析结果)

---

## 一、音频系统

| 原名 | 建议名 | 类型 | 功能 |
|------|--------|------|------|
| `g` | `midiPlayer` | Player | MIDI播放器 |
| `b` | `currentSoundtrack` | String | 当前播放的音频文件路径 |

### 方法

| 原签名 | 建议签名 |
|--------|----------|
| `a(String, int, boolean)` | `playMidi(String resourcePath, int volumeLevel, boolean loop)` |
| `a()` | `stopSoundtrack()` |

---

## 二、存档系统

| 原名 | 建议名 | 功能 |
|------|--------|------|
| `m` | `languageCode` | 语言代码 (默认"EN") |
| `o[4]` | `levelUnlockStates` | 关卡解锁状态 |
| `p[4]` | `levelCompleteFlags` | 关卡完成标志 |
| `r[7]` | `achievementFlags` | 成就标志 |
| `s` | `isSoundEnabled` | 音效开关 |
| `t` | `isMusicEnabled` | 音乐开关 |
| `u` | `isFullscreen` | 全屏模式 |
| `v` | `difficulty` | 难度等级 |
| `w` | `coins` | 金币数量 |
| `x` | `score` | 得分 |
| `y` | `gameTime` | 游戏时间戳 |
| `z` | `randomSeed` | 随机种子 |
| `A[5]` | `playerNames` | 玩家名称数组 |

### 方法

| 原名 | 建议名 | 功能 |
|------|--------|------|
| `e()` | `initOrValidateSaveData()` | 初始化/验证存档 |
| `f()` | `deleteAllSaveData()` | 删除存档 |
| `g()` | `loadSaveData()` | 加载存档 |
| `h()` | `saveSaveData()` | 保存存档 |
| `i()` | `serializeSaveData()` | 序列化存档为byte[] |

---

## 三、输入系统

| 原名 | 建议名 | 功能 |
|------|--------|------|
| `B` | `keyUpPressed` | 上键按下 |
| `C` | `keyDownPressed` | 下键按下 |
| `D` | `keyLeftPressed` | 左键按下 |
| `E` | `keyRightPressed` | 右键按下 |
| `F` | `keyFirePressed` | 确认键按下 |
| `G` | `customKey1Pressed` | 自定义键1状态 |
| `H` | `customKey2Pressed` | 自定义键2状态 |
| `I` | `anyKeyPressed` | 任意键按下 |
| `J` | `customKeyCode1` | 自定义键1代码 |
| `K` | `customKeyCode2` | 自定义键2代码 |
| `L[8]` | `cheatCodeSequence1` | 秘籍序列1 "11445577" |
| `M` | `cheatCodeIndex1` | 秘籍序列1匹配位置 |
| `N` | `cheatCode1Unlocked` | 秘籍1已解锁 |
| `O` | `starKeyPending` | 星号键等待状态 |
| `P[5]` | `cheatCodeSequence2` | 秘籍序列2 "71145" |
| `Q` | `cheatCodeIndex2` | 秘籍序列2匹配位置 |
| `R` | `cheatCode2Unlocked` | 秘籍2已解锁 |

### 秘籍
- `*` 然后 `#` → 获得5个奖励

---

## 四、游戏状态机

字段 `l` → `gameState`

| Case | 建议常量名 | 描述 |
|------|-----------|------|
| 1 | `STATE_GAME_PLAY` | 游戏进行中 |
| 2 | `STATE_GAME_PAUSED` | 暂停界面 |
| 4 | `STATE_EXTRA_LEVELPACK` | 额外关卡包选择 |
| 5 | `STATE_MAIN_MENU` | 主菜单 |
| 6 | `STATE_SPLASH_SCREEN` | 启动画面 |
| 7 | `STATE_LEVEL_COMPLETE` | 关卡完成 |
| 8 | `STATE_OPTIONS_MENU` | 选项菜单 |
| 9 | `STATE_SUB_MENU` | 子菜单 |
| 10 | `STATE_LOADING_SCREEN` | 加载画面 |
| 11 | `STATE_FADE_BLACK` | 黑屏过渡 |
| 12 | `STATE_TITLE_SCREEN` | 标题画面 |
| 13 | `STATE_MESSAGE_DIALOG` | 消息对话框 |
| 14 | `STATE_HINT_DIALOG` | 提示对话框 |
| 15 | `STATE_INGAME_MENU` | 游戏内菜单 |
| 16 | `STATE_CONFIRM_DIALOG` | 确认对话框 |

---

## 五、关卡与地图

| 原名 | 建议名 | 功能 |
|------|--------|------|
| `cl[][]` | `tileMap` | 瓦片层(地形) |
| `cm[][]` | `objectMap` | 物品层(可收集物) |
| `dn` | `mapWidth` | 地图宽度 |
| `cfr_renamed_0` | `mapHeight` | 地图高度 |
| `bM` | `levelPackId` | 关卡包ID |
| `bL` | `levelId` | 关卡ID |
| `bN` | `totalLevels` | 总关卡数 |
| `bO` | `carrotsCollected` | 收集的胡萝卜 |

### 瓦片类型常量

| 值 | 建议常量名 |
|----|-----------|
| -66 ~ -71 | `TILE_CONVEYOR_*` (传送带) |
| -57, -56 | `TILE_GRASS` (可割草地) |
| -106 | `TILE_DEATH_TRAP` (死亡陷阱) |
| -108 | `TILE_ICE` (冰面) |
| -90, -94 | `TILE_PORTAL` (传送门) |

---

## 六、玩家坐标与移动

| 原名 | 建议名 | 功能 |
|------|--------|------|
| `ag` | `playerPixelX` | 玩家像素X |
| `ah` | `playerPixelY` | 玩家像素Y |
| `ai` | `playerTileX` | 玩家瓦片X |
| `aj` | `playerTileY` | 玩家瓦片Y |
| `an` | `direction` | 朝向 (0左 1右 2上 3下 4死 5滑翔 6掉落) |
| `ap` | `moveProgress` | 移动进度(0-32像素) |
| `am` | `animFrame` | 动画帧 |
| `aE` | `pushForce` | 推动力 |
| `dM` | `isDragging` | 是否拖拽 |

### 方法

| 原名 | 建议名 | 功能 |
|------|--------|------|
| `N()` | `updateMovement()` | 更新移动 |
| `a(int,int,boolean)` | `canMoveTo()` | 碰撞检测 |
| `b(int,int)` | `getTileAt()` | 获取瓦片类型 |
| `c(int,int)` | `getObjectAt()` | 获取物品类型 |

---

## 七、摄像机与渲染

| 原名 | 建议名 | 功能 |
|------|--------|------|
| `bz` | `cameraX` | 摄像机X偏移 |
| `bA` | `cameraY` | 摄像机Y偏移 |
| `bV` | `transitionEffect` | 过渡效果类型 |
| `bT` | `transitionProgress` | 过渡进度 |
| `i` | `screenWidth` | 屏幕宽度 |
| `j` | `screenHeight` | 屏幕高度 |
| `k` | `playAreaHeight` | 游戏区域高度 |

### 图片资源

| 原名 | 建议名 |
|------|--------|
| `ca` | `imgFont` |
| `cb` | `imgNumbers` |
| `cc` | `imgArrows` |
| `cd` | `imgSpritesheet` |
| `ce` | `imgMisc` |
| `cf` | `imgTiles` |
| `cg` | `imgMower` |
| `cj[10]` | `spriteImages` |
| `dC` | `imgLogo` |
| `dD` | `imgTitle` |

### 方法

| 原名 | 建议名 |
|------|--------|
| `a(Graphics)` | `renderGameWorld()` |
| `b(Graphics)` | `renderHUD()` |
| `c(Graphics)` | `renderTransitionEffect()` |
| `d(Graphics,int,int)` | `drawTileLayer()` |
| `paint(Graphics)` | `paint()` (状态机入口) |

---

## 八、游戏逻辑

| 原名 | 建议名 | 功能 |
|------|--------|------|
| `b()` | `gameLoop()` | 主游戏循环 |
| `H()` | `updateGameState()` | 更新游戏状态 |
| `M()` | `handlePlayerMovement()` | 处理玩家移动 |
| `J()` | `handleTileInteraction()` | 处理瓦片交互 |
| `K()` | `handleDeath()` | 处理死亡 |
| `f(int,int)` | `loadLevel()` | 加载关卡 |
| `n()` | `initBonusStage()` | 初始化奖励关卡 |
| `o()` | `updateBonusStage()` | 更新奖励关卡 |
| `p()` | `startFlyingStage()` | 开始飞行关卡 |
| `O()` | `updateAnimation()` | 更新动画 |

---

## 九、核心字段

| 原名 | 建议名 | 功能 |
|------|--------|------|
| `c` | `gameState` | 游戏状态码 |
| `d` | `isGameRunning` | 游戏运行中 |
| `e` | `isPaused` | 暂停标志 |
| `f` | `isFirstLaunch` | 首次运行 |
| `h` | `bobbyMain` | Bobby主类引用 |
| `a(String)` | `loadTextResources()` | 加载文本资源 |

---

## 十、建议的类拆分方案

| 新类名 | 职责 | 包含字段/方法 |
|--------|------|---------------|
| `AudioManager` | 音频管理 | g, b, playMidi(), stopSoundtrack() |
| `SaveManager` | 存档管理 | o, p, r, s, t, u, v, w, x, y, z, A + 存档方法 |
| `InputHandler` | 输入处理 | B-K, L-R, keyPressed(), keyReleased() |
| `LevelData` | 关卡数据 | cl, cm, dn, mapHeight, loadLevel() |
| `Player` | 玩家状态 | ag-aj, an, ap, am, direction常量 |
| `Camera` | 摄像机 | bz, bA, screenWidth, screenHeight |
| `GameRenderer` | 渲染 | 所有Graphics方法 + 图片资源 |
| `GameLogic` | 游戏逻辑 | gameLoop(), updateGameState() 等 |
