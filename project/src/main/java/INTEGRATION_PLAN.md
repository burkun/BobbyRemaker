# a.java 集成方案

## 目标
将混淆的字段和方法替换为新类实例，逐步重构。

## 集成步骤

### 第1步：添加新类实例字段

在a.java类顶部添加：

```java
// 新重构类实例
private AudioManager audioManager = new AudioManager();
private SaveManager saveManager;
private InputHandler inputHandler = new InputHandler();
private Player player = new Player();
private Camera camera = new Camera();
private LevelData levelData = new LevelData();
private GameLoop gameLoop;
private CollisionHandler collisionHandler;
private LevelInteraction levelInteraction;
private GameLogic gameLogic;
```

### 第2步：替换音频模块

**原始字段：**
```java
private Player g = null;     // midiPlayer
private String b = null;     // currentSoundtrack
```

**替换为：**
```java
// 删除 g, b 字段
// 使用 audioManager 替代
```

**原始方法：**
```java
public final void a(String string, int n, boolean bl) { // playMidi
    // ... 播放逻辑
}
public final void a() { // stopSoundtrack
    // ... 停止逻辑
}
```

**替换为：**
```java
public final void playMidi(String path, int volume, boolean loop) {
    audioManager.playMidi(path, volume, loop);
}
public final void stopSoundtrack() {
    audioManager.stopSoundtrack();
}
```

### 第3步：替换存档模块

**原始字段：**
```java
private byte[] o = new byte[4];      // levelUnlockStates
private boolean[] p = new boolean[4]; // levelCompleteFlags
private byte[] r = new byte[7];      // achievementFlags
private boolean s, t, u;             // soundEnabled, musicEnabled, fullscreen
private byte v;                      // difficulty
private short w, x;                  // coins, score
private long y;                      // gameTime
private int z;                       // randomSeed
private String[] A = new String[5];  // playerNames
```

**替换为：**
```java
// 删除以上字段
// 在构造函数中初始化 saveManager
saveManager = new SaveManager(n); // n是Random实例
```

**原始方法替换：**
```java
// e() → saveManager.initOrValidateSaveData()
// f() → saveManager.deleteAllSaveData()
// g() → saveManager.loadSaveData()
// h() → saveManager.saveSaveData()
```

### 第4步：替换输入处理

**原始字段：**
```java
private boolean B, C, D, E, F;  // keyUp/Down/Left/Right/Fire
private int J, K;               // customKeyCode1, 2
```

**原始方法：**
```java
public final void keyPressed(int keyCode) {
    // ... 输入处理逻辑
}
public final void keyReleased(int keyCode) {
    // ... 输入处理逻辑
}
```

**替换为：**
```java
public final void keyPressed(int keyCode) {
    inputHandler.keyPressed(keyCode);
    // 处理秘籍检测等其他逻辑...
}

public final void keyReleased(int keyCode) {
    inputHandler.keyReleased(keyCode);
}
```

### 第5步：替换玩家状态

**原始字段：**
```java
private int ag, ah;   // playerPixelX, playerPixelY
private int ai, aj;   // playerTileX, playerTileY
private int an;       // direction
private int ap;       // moveProgress
private int am;       // animFrame
private int aE;       // pushForce
```

**替换为：**
```java
// 删除以上字段
// 使用 player.getPixelX(), player.setPixelX() 等
```

**原始方法：**
```java
private final void N() { // updateMovement
    // ... 移动更新逻辑
}
private final boolean O() { // updateAnimation
    // ... 动画更新逻辑
}
```

**替换为：**
```java
private final void updateMovement() {
    player.updateMovement();
}
```

### 第6步：替换摄像机

**原始字段：**
```java
private int bz, bA;   // cameraX, cameraY
private int bB, bC;   // maxCameraX, maxCameraY
private int i, j, k;  // screenWidth, screenHeight, playAreaHeight
private byte bV;      // transitionEffect
private int bT;       // transitionProgress
```

**替换为：**
```java
// 删除以上字段
// 使用 camera.getCameraX(), camera.setCameraX() 等
```

### 第7步：替换关卡数据

**原始字段：**
```java
private byte[][] cl;  // tileMap
private byte[][] cm;  // objectMap
private int dn;       // mapWidth
```

**替换为：**
```java
// 删除以上字段
// 使用 levelData.getTileAt(), levelData.getObjectAt() 等
```

### 第8步：替换碰撞检测

**原始方法：**
```java
private final boolean a(int dx, int dy, boolean checkOnly) { // canMoveTo
    // ... 碰撞检测逻辑（约230行）
}
```

**替换为：**
```java
private final boolean canMoveTo(int dx, int dy, boolean checkOnly) {
    CollisionHandler.CollisionResult result = collisionHandler.canMoveTo(dx, dy, checkOnly);
    return result.isValid();
}
```

### 第9步：替换关卡交互

**原始方法：**
```java
private final void J() { // handleTileInteraction（约217行）
    // ... 瓦片交互
}
private final void K() { // handleDeath
    // ... 死亡处理
}
private final void L() { // checkLevelComplete
    // ... 关卡完成检测
}
```

**替换为：**
```java
private final void handleTileInteraction() {
    levelInteraction.handleTileInteraction();
}
private final void handleDeath() {
    levelInteraction.handleDeath();
}
private final void checkLevelComplete() {
    levelInteraction.checkLevelComplete();
}
```

## 集成优先级

```
高 ████████████ 音频 → 存档 → 输入 (简单替换)
中 ████████      玩家 → 摄像机 → 关卡数据 (字段迁移)
低 ████          碰撞 → 交互 → 渲染 (复杂逻辑)
```

## 注意事项

1. **逐步替换**：先替换简单模块，验证后再继续
2. **保留兼容**：新旧代码并存，逐步删除旧字段
3. **测试验证**：每步替换后编译测试
4. **getter/setter**：新类使用方法访问，原代码直接访问字段

## 下一步行动

1. 先替换 AudioManager（最简单，无依赖）
2. 再替换 InputHandler（简单，有依赖）
3. 然后替换 SaveManager（中等复杂）