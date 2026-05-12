/**
 * SpecialLevelMode - 特殊关卡模式类
 *
 * 处理奖励关卡和飞行关卡的特殊逻辑
 * 从 a.java 的 n(), o(), p(), r() 方法提取
 */
public class SpecialLevelMode {

    // ==================== 关卡类型常量 ====================
    public static final int TYPE_NONE = 0;
    public static final int TYPE_BONUS = 1;      // 奖励关卡
    public static final int TYPE_FLYING = 2;     // 飞行关卡
    public static final int TYPE_SLEEP = 3;      // 睡眠/密码关卡

    // ==================== 状态常量 ====================
    public static final int STATE_INACTIVE = 0;
    public static final int STATE_INTRO = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_OUTRO = 3;
    public static final int STATE_COMPLETE = 4;

    // ==================== 奖励关卡参数 ====================
    private static final int BONUS_SCROLL_SPEED = 2;
    private static final int BONUS_CARROT_SPAWN_MIN = 32;
    private static final int BONUS_ITEM_COUNT = 5;

    // ==================== 飞行关卡参数 ====================
    private static final int FLIGHT_SCROLL_SPEED = 2;
    private static final int FLIGHT_INTRO_DURATION = 192;
    private static final int FLIGHT_TEXT_FADE = 256;

    // ==================== 实例字段 ====================
    private int currentType;
    private int currentState;
    private int stateTimer;

    // 奖励关卡状态
    private byte[] bonusItemTypes;
    private int[] bonusItemX;
    private int[] bonusItemY;
    private int bonusScrollOffset;
    private int bonusSpawnCounter;
    private int bonusCollected;

    // 飞行关卡状态
    private String flightText;
    private int flightTextIndex;
    private int flightIntroTimer;
    private int flightFadeValue;
    private boolean flightComplete;

    // 睡眠关卡状态
    private String sleepPassword;
    private char[] passwordChars;
    private int passwordScore;

    // 引用
    private LevelData levelData;
    private Player player;
    private GameRenderer renderer;

    // ==================== 构造函数 ====================
    public SpecialLevelMode() {
        reset();
    }

    public SpecialLevelMode(LevelData levelData, Player player, GameRenderer renderer) {
        this();
        this.levelData = levelData;
        this.player = player;
        this.renderer = renderer;
    }

    public void reset() {
        currentType = TYPE_NONE;
        currentState = STATE_INACTIVE;
        stateTimer = 0;

        bonusItemTypes = new byte[BONUS_ITEM_COUNT];
        bonusItemX = new int[BONUS_ITEM_COUNT];
        bonusItemY = new int[BONUS_ITEM_COUNT];
        bonusScrollOffset = 0;
        bonusSpawnCounter = 0;
        bonusCollected = 0;

        flightText = null;
        flightTextIndex = 0;
        flightIntroTimer = 0;
        flightFadeValue = FLIGHT_TEXT_FADE;
        flightComplete = false;

        sleepPassword = null;
        passwordChars = null;
        passwordScore = 0;
    }

    // ==================== 奖励关卡 (Bonus Level) ====================

    /**
     * 初始化奖励关卡
     * 对应原 a.java 的 n() 方法
     */
    public void initBonusLevel() {
        currentType = TYPE_BONUS;
        currentState = STATE_PLAYING;
        stateTimer = 0;

        // 清空关卡数据
        if (levelData != null) {
            int width = levelData.getMapWidth();
            int height = levelData.getMapHeight();
            levelData.setTileMap(new byte[height][width]);
            levelData.setObjectMap(new byte[height][width]);
        }

        bonusScrollOffset = 0;
        bonusCollected = 0;

        // 初始化奖励物品
        for (int i = 0; i < BONUS_ITEM_COUNT; i++) {
            bonusItemTypes[i] = (byte) (Math.random() * 8);
            bonusItemX[i] = (int) (32 + Math.random() * 200);  // 随机X位置
            bonusItemY[i] = -12;  // 屏幕外
        }

        // 玩家位置
        if (player != null) {
            player.setPixelX(100);
            player.setPixelY(100);
            player.setDirection(Player.DIR_RIGHT);
        }
    }

    /**
     * 更新奖励关卡
     * 对应原 a.java 的 o() 方法
     */
    public void updateBonusLevel(int screenWidth, int screenHeight) {
        // 滚动背景
        bonusScrollOffset += BONUS_SCROLL_SPEED;
        if (levelData != null) {
            int mapWidth = levelData.getMapWidth() * 32;
            if (bonusScrollOffset > mapWidth) {
                bonusScrollOffset -= (bonusScrollOffset / 32) * 32;
                // 重新生成行
                regenerateBonusRow(bonusScrollOffset);
            }
        }

        // 更新奖励物品
        for (int i = 0; i < BONUS_ITEM_COUNT; i++) {
            bonusItemX[i] -= BONUS_SCROLL_SPEED;

            // 检查是否出界或收集
            if (bonusItemTypes[i] >= 8 || bonusItemX[i] <= -12) {
                // 重新生成
                bonusItemTypes[i] = (byte) (Math.random() * 8);
                bonusItemX[i] = (int) (32 + Math.random() * screenWidth);
                bonusItemY[i] = (int) (Math.random() * screenHeight);
            }
        }

        bonusSpawnCounter++;
        if (bonusSpawnCounter >= 4) {
            bonusSpawnCounter = 0;
        }
    }

    private void regenerateBonusRow(int offset) {
        if (levelData == null) return;
        // 在滚动位置重新生成地形行
    }

    // ==================== 飞行关卡 (Flying Level) ====================

    /**
     * 初始化飞行关卡
     * 对应原 a.java 的 p() 方法
     */
    public void initFlyingLevel(String text) {
        currentType = TYPE_FLYING;
        currentState = STATE_INTRO;
        stateTimer = 0;

        flightText = text != null ? text : "";
        flightTextIndex = 0;
        flightIntroTimer = FLIGHT_INTRO_DURATION;
        flightFadeValue = FLIGHT_TEXT_FADE;
        flightComplete = false;

        // 初始化玩家
        if (player != null) {
            player.setFlying(true);
            player.setDying(false);
            player.setDirection(Player.DIR_RIGHT);
            player.setMoveProgress(1);
            player.setAnimFrame(0);
        }
    }

    /**
     * 设置飞行关卡文本
     * 对应原 a.java 的 q() 方法
     */
    public void setFlightText(String text, int startIndex) {
        flightText = text;
        flightTextIndex = startIndex;
        flightIntroTimer = 0;
        flightFadeValue = FLIGHT_TEXT_FADE;
    }

    /**
     * 更新飞行关卡
     * 对应原 a.java 的 r() 方法
     * @return true 如果关卡完成
     */
    public boolean updateFlyingLevel() {
        if (currentState == STATE_COMPLETE) {
            return true;
        }

        // 处理淡入淡出
        if (currentState == STATE_INTRO) {
            flightIntroTimer--;
            if (flightIntroTimer < 0) {
                flightFadeValue--;
                if (flightFadeValue < 0) {
                    // 检查是否还有文本
                    if (flightText != null && flightTextIndex < flightText.length()) {
                        // 继续显示下一段文本
                        advanceFlightText();
                    } else {
                        // 结束飞行
                        if (player != null) {
                            player.setTileX(player.getPixelX());  // 同步位置
                        }
                        currentState = STATE_COMPLETE;
                        return true;
                    }
                }
            }
        } else if (currentState == STATE_PLAYING) {
            // 飞行中逻辑
            if (player != null && player.getPixelX() > player.getTileX() * 32) {
                player.setPixelX(player.getPixelX() - 2);
            } else {
                // 完成飞行
                currentState = STATE_COMPLETE;
                return true;
            }
        }

        return false;
    }

    private void advanceFlightText() {
        if (flightText == null) return;
        int nextIndex = flightText.indexOf('#', flightTextIndex);
        if (nextIndex == -1) {
            nextIndex = flightText.length();
        }
        flightTextIndex = nextIndex + 1;
        flightFadeValue = FLIGHT_TEXT_FADE;
    }

    /**
     * 完成飞行关卡
     */
    public void completeFlyingLevel() {
        if (player != null) {
            player.setFlying(false);
        }
        currentState = STATE_COMPLETE;
    }

    // ==================== 睡眠关卡 (Sleep/Password Level) ====================

    /**
     * 初始化睡眠关卡
     * 对应原 a.java 的 u() 方法
     */
    public void initSleepLevel(int score) {
        currentType = TYPE_SLEEP;
        currentState = STATE_PLAYING;
        stateTimer = 0;

        passwordScore = Math.min(score, 100);
        sleepPassword = generatePassword();
        passwordChars = sleepPassword.toCharArray();
    }

    /**
     * 生成密码
     * 对应原 a.java 的 v() 方法
     */
    private String generatePassword() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 16; i++) {
            int value = (int) (Math.random() * 32);
            char c = (char) (value < 10 ? value + 48 : value - 10 + 65);
            sb.append(c);
            if (i % 4 == 0 && i < 16) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * 更新睡眠关卡
     * @return true 如果完成
     */
    public boolean updateSleepLevel() {
        // 睡眠关卡主要是显示密码
        return false;
    }

    /**
     * 获取睡眠关卡密码
     */
    public String getSleepPassword() {
        return sleepPassword;
    }

    /**
     * 获取睡眠关卡分数
     */
    public int getPasswordScore() {
        return passwordScore;
    }

    // ==================== 通用更新 ====================

    /**
     * 通用更新方法
     */
    public void update(int screenWidth, int screenHeight) {
        switch (currentType) {
            case TYPE_BONUS:
                updateBonusLevel(screenWidth, screenHeight);
                break;
            case TYPE_FLYING:
                updateFlyingLevel();
                break;
            case TYPE_SLEEP:
                updateSleepLevel();
                break;
        }
    }

    // ==================== 状态检查 ====================

    public boolean isActive() {
        return currentType != TYPE_NONE && currentState != STATE_INACTIVE;
    }

    public boolean isComplete() {
        return currentState == STATE_COMPLETE;
    }

    public int getCurrentType() {
        return currentType;
    }

    public int getCurrentState() {
        return currentState;
    }

    public int getBonusCollected() {
        return bonusCollected;
    }

    public void incrementBonusCollected() {
        bonusCollected++;
    }

    public String getFlightText() {
        if (flightText == null || flightTextIndex >= flightText.length()) {
            return "";
        }
        int endIndex = flightText.indexOf('#', flightTextIndex);
        if (endIndex == -1) {
            endIndex = flightText.length();
        }
        return flightText.substring(flightTextIndex, endIndex);
    }

    public int getFlightFadeValue() {
        return flightFadeValue;
    }

    // ==================== 渲染数据获取 ====================

    public byte[] getBonusItemTypes() {
        return bonusItemTypes;
    }

    public int[] getBonusItemX() {
        return bonusItemX;
    }

    public int[] getBonusItemY() {
        return bonusItemY;
    }

    public int getBonusScrollOffset() {
        return bonusScrollOffset;
    }
}
