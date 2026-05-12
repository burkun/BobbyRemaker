import javax.microedition.lcdui.Graphics;

/**
 * BobbyGame - Bobby Carrot 5 主游戏集成类
 *
 * 整合所有重构模块的主游戏类，替代原始的 a.java
 * 包含完整的游戏循环、状态管理和模块协调
 */
public class BobbyGame implements GameLoop.GameLoopCallback {

    // ==================== 游戏状态常量 ====================
    public static final int STATE_INIT = 0;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_MENU = 3;
    public static final int STATE_LEVEL_COMPLETE = 4;
    public static final int STATE_GAME_OVER = 5;
    public static final int STATE_LOADING = 6;
    public static final int STATE_TITLE = 7;
    public static final int STATE_DIALOG = 8;
    public static final int STATE_BONUS = 9;
    public static final int STATE_FLYING = 10;
    public static final int STATE_SLEEP = 11;

    // ==================== 模块实例 ====================
    private AudioManager audioManager;
    private SaveManager saveManager;
    private InputHandler inputHandler;
    private Player player;
    private Camera camera;
    private LevelData levelData;
    private GameLoop gameLoop;
    private CollisionHandler collisionHandler;
    private LevelInteraction levelInteraction;
    private GameLogic gameLogic;
    private GameRenderer renderer;
    private MenuSystem menuSystem;
    private SpecialLevelMode specialLevelMode;
    private GameState gameState;

    // ==================== 游戏状态 ====================
    private int currentState;
    private int previousState;
    private int levelPackId;
    private int levelId;
    private int totalCarrots;
    private int carrotsCollected;
    private int score;
    private int bonusItems;
    private long gameTime;

    // ==================== 关卡状态 ====================
    private boolean levelComplete;
    private boolean levelFailed;
    private boolean isFirstLaunch;
    private boolean cheatsEnabled;
    private int transitionEffect;
    private int transitionProgress;

    // ==================== 屏幕参数 ====================
    private int screenWidth;
    private int screenHeight;
    private int playAreaHeight;

    // ==================== 构造函数 ====================
    public BobbyGame(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.playAreaHeight = height - 20;

        initializeModules();
        resetGameState();
    }

    private void initializeModules() {
        // 初始化所有模块
        audioManager = new AudioManager();
        saveManager = new SaveManager();
        inputHandler = new InputHandler();
        player = new Player();
        camera = new Camera(screenWidth, screenHeight, playAreaHeight);
        levelData = new LevelData();
        collisionHandler = new CollisionHandler(levelData, player);
        levelInteraction = new LevelInteraction(levelData, player);
        gameLogic = new GameLogic(player, camera, levelData);
        renderer = new GameRenderer(screenWidth, screenHeight);
        menuSystem = new MenuSystem();
        specialLevelMode = new SpecialLevelMode(levelData, player, renderer);
        gameState = new GameState();

        // 设置游戏循环
        gameLoop = new GameLoop(this);
    }

    private void resetGameState() {
        currentState = STATE_INIT;
        previousState = STATE_INIT;
        levelPackId = 0;
        levelId = 1;
        totalCarrots = 0;
        carrotsCollected = 0;
        score = 0;
        bonusItems = 0;
        gameTime = 0;

        levelComplete = false;
        levelFailed = false;
        isFirstLaunch = true;
        cheatsEnabled = false;
        transitionEffect = 0;
        transitionProgress = 0;
    }

    // ==================== 游戏生命周期 ====================

    public void start() {
        loadSaveData();
        gameLoop.start();
    }

    public void stop() {
        saveGame();
        gameLoop.stop();
    }

    public void pause() {
        if (currentState == STATE_PLAYING) {
            previousState = currentState;
            currentState = STATE_PAUSED;
            menuSystem.showPauseMenu();
        }
        audioManager.stopSoundtrack();
    }

    public void resume() {
        if (currentState == STATE_PAUSED) {
            currentState = previousState;
            menuSystem.closeMenu();
        }
    }

    // ==================== GameLoop.GameLoopCallback 实现 ====================

    @Override
    public void onInitialize() {
        loadResources();
        currentState = STATE_TITLE;
    }

    @Override
    public boolean onUpdate() {
        switch (currentState) {
            case STATE_INIT:
                return handleInitState();

            case STATE_PLAYING:
                return handlePlayingState();

            case STATE_PAUSED:
                return handlePausedState();

            case STATE_MENU:
                return handleMenuState();

            case STATE_LEVEL_COMPLETE:
                return handleLevelCompleteState();

            case STATE_GAME_OVER:
                return handleGameOverState();

            case STATE_LOADING:
                return handleLoadingState();

            case STATE_TITLE:
                return handleTitleState();

            case STATE_BONUS:
                return handleBonusState();

            case STATE_FLYING:
                return handleFlyingState();

            case STATE_SLEEP:
                return handleSleepState();

            default:
                return true;
        }
    }

    @Override
    public void onRender() {
        // 渲染委托给 GameRenderer (由 a.java 调用 render(Graphics))
    }

    /**
     * 渲染游戏画面 - 供 a.java Canvas 调用
     * @param g J2ME Graphics 对象
     */
    public void render(Graphics g) {
        renderer.render(g, this);
    }

    @Override
    public void onPause() {
        pause();
    }

    @Override
    public void onResume() {
        resume();
    }

    @Override
    public void onCleanup() {
        audioManager.stopSoundtrack();
        saveGame();
    }

    @Override
    public void onExit() {
        stop();
    }

    // ==================== 状态处理 ====================

    private boolean handleInitState() {
        loadSaveData();
        currentState = STATE_TITLE;
        return true;
    }

    private boolean handlePlayingState() {
        // 处理输入
        processInput();

        // 更新游戏逻辑
        boolean updated = gameLogic.updateGameState();

        // 检查碰撞
        checkCollisions();

        // 更新摄像机
        camera.updateCamera(player.getPixelX(), player.getPixelY());

        // 检查关卡完成
        if (levelInteraction.isLevelComplete()) {
            currentState = STATE_LEVEL_COMPLETE;
        }

        // 检查死亡
        if (player.isDying() && player.getAnimFrame() >= 10) {
            currentState = STATE_GAME_OVER;
        }

        // 更新动画
        player.updateAnimation();

        return updated;
    }

    private boolean handlePausedState() {
        menuSystem.update();
        return true;
    }

    private boolean handleMenuState() {
        menuSystem.update();
        return true;
    }

    private boolean handleLevelCompleteState() {
        if (transitionProgress >= 100) {
            nextLevel();
        } else {
            transitionProgress += 5;
        }
        return true;
    }

    private boolean handleGameOverState() {
        if (transitionProgress >= 100) {
            restartLevel();
        } else {
            transitionProgress += 3;
        }
        return true;
    }

    private boolean handleLoadingState() {
        // 加载中状态
        return transitionProgress >= 100;
    }

    private boolean handleTitleState() {
        // 标题画面动画
        return true;
    }

    private boolean handleBonusState() {
        specialLevelMode.update(screenWidth, screenHeight);
        if (specialLevelMode.isComplete()) {
            currentState = STATE_PLAYING;
        }
        return true;
    }

    private boolean handleFlyingState() {
        if (specialLevelMode.updateFlyingLevel()) {
            currentState = STATE_PLAYING;
        }
        return true;
    }

    private boolean handleSleepState() {
        specialLevelMode.updateSleepLevel();
        return true;
    }

    // ==================== 输入处理 ====================

    private void processInput() {
        // 处理移动输入
        if (inputHandler.isUp() && !player.isMovingBetweenTiles()) {
            requestMove(Player.DIR_UP);
        } else if (inputHandler.isDown() && !player.isMovingBetweenTiles()) {
            requestMove(Player.DIR_DOWN);
        } else if (inputHandler.isLeft() && !player.isMovingBetweenTiles()) {
            requestMove(Player.DIR_LEFT);
        } else if (inputHandler.isRight() && !player.isMovingBetweenTiles()) {
            requestMove(Player.DIR_RIGHT);
        }

        // 处理确认/取消键
        if (inputHandler.isConfirmPressed()) {
            // 确认键
        }
        if (inputHandler.isCancelPressed()) {
            pause();
        }
    }

    private void requestMove(int direction) {
        player.setDirection(direction);
        player.setMoveRequested(true);
    }

    public void keyPressed(int keyCode) {
        inputHandler.keyPressed(keyCode);

        // 处理秘籍
        if (inputHandler.isCheat1Activated() && levelPackId == 0 && levelId == 1) {
            cheatsEnabled = true;
            score += 5;
        }
    }

    public void keyReleased(int keyCode) {
        inputHandler.keyReleased(keyCode);
    }

    // ==================== 碰撞检测 ====================

    private void checkCollisions() {
        if (player.isMovingBetweenTiles()) {
            // 检查新位置的碰撞
            CollisionHandler.CollisionResult result =
                collisionHandler.canMoveTo(
                    getDeltaX(player.getDirection()),
                    getDeltaY(player.getDirection()),
                    false
                );

            // 处理碰撞结果
            handleCollisionResult(result);
        }

        // 检查瓦片交互
        LevelInteraction.InteractionResult interaction =
            levelInteraction.handleTileInteraction();
        handleInteractionResult(interaction);
    }

    private int getDeltaX(int direction) {
        switch (direction) {
            case Player.DIR_LEFT: return -1;
            case Player.DIR_RIGHT: return 1;
            default: return 0;
        }
    }

    private int getDeltaY(int direction) {
        switch (direction) {
            case Player.DIR_UP: return -1;
            case Player.DIR_DOWN: return 1;
            default: return 0;
        }
    }

    private void handleCollisionResult(CollisionHandler.CollisionResult result) {
        switch (result.collisionType) {
            case CollisionHandler.COLLISION_CARROT:
                carrotsCollected++;
                score += 10;
                break;
            case CollisionHandler.COLLISION_LEVEL_END:
                levelComplete = true;
                break;
            case CollisionHandler.COLLISION_DEATH:
                triggerDeath();
                break;
        }
    }

    private void handleInteractionResult(LevelInteraction.InteractionResult result) {
        switch (result.getType()) {
            case LevelInteraction.InteractionResult.TYPE_CARROT:
                carrotsCollected++;
                score += 10;
                break;
            case LevelInteraction.InteractionResult.TYPE_LEVEL_COMPLETE:
                levelComplete = true;
                currentState = STATE_LEVEL_COMPLETE;
                break;
            case LevelInteraction.InteractionResult.TYPE_DEATH:
                triggerDeath();
                break;
        }
    }

    private void triggerDeath() {
        player.setDying(true);
        player.setDirection(Player.DIR_DEAD);
        audioManager.playMidi("/death.mid", 3, false);
    }

    // ==================== 关卡管理 ====================

    public void loadLevel(int packId, int level) {
        currentState = STATE_LOADING;
        levelPackId = packId;
        levelId = level;
        transitionProgress = 0;

        // 清理前一关
        player.reset();
        camera.reset();

        // 加载关卡数据
        // levelData.loadLevel(packId, level, data, width, height);

        // 找到出生点
        int[] spawn = levelData.findSpawnPoint();
        if (spawn != null) {
            player.initPosition(spawn[0], spawn[1], Player.DIR_RIGHT);
        }

        // 统计胡萝卜
        totalCarrots = levelData.countCarrots();
        carrotsCollected = 0;

        // 播放背景音乐
        audioManager.playMidi(getLevelMusic(), 3, true);

        currentState = STATE_PLAYING;
    }

    private String getLevelMusic() {
        // 根据关卡类型返回音乐文件
        if (levelPackId == 0) {
            return "/ingame" + levelId + ".mid";
        } else {
            return "/bonus.mid";
        }
    }

    public void nextLevel() {
        levelId++;
        if (levelId > levelData.getTotalLevels()) {
            levelPackId++;
            levelId = 1;
        }
        loadLevel(levelPackId, levelId);
    }

    public void restartLevel() {
        loadLevel(levelPackId, levelId);
    }

    public void startBonusLevel() {
        specialLevelMode.initBonusLevel();
        currentState = STATE_BONUS;
    }

    public void startFlyingLevel(String text) {
        specialLevelMode.initFlyingLevel(text);
        currentState = STATE_FLYING;
    }

    // ==================== 存档管理 ====================

    private void loadSaveData() {
        saveManager.load();

        // 应用存档数据到游戏状态
        cheatsEnabled = false;
        score = saveManager.getScore();
        bonusItems = saveManager.getBonusItems();
    }

    private void saveGame() {
        saveManager.setScore(score);
        saveManager.setBonusItems(bonusItems);
        saveManager.save();
    }

    private void loadResources() {
        // 加载字体、图片等资源
        renderer.loadResources();
    }

    // ==================== Getter 方法 ====================

    public int getCurrentState() {
        return currentState;
    }

    public Player getPlayer() {
        return player;
    }

    public Camera getCamera() {
        return camera;
    }

    public LevelData getLevelData() {
        return levelData;
    }

    public GameRenderer getRenderer() {
        return renderer;
    }

    public MenuSystem getMenuSystem() {
        return menuSystem;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public SaveManager getSaveManager() {
        return saveManager;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScore() {
        return score;
    }

    public int getCarrotsCollected() {
        return carrotsCollected;
    }

    public int getTotalCarrots() {
        return totalCarrots;
    }

    public int getLevelId() {
        return levelId;
    }

    public int getLevelPackId() {
        return levelPackId;
    }

    public SpecialLevelMode getSpecialLevelMode() {
        return specialLevelMode;
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public boolean isCheatsEnabled() {
        return cheatsEnabled;
    }
}
