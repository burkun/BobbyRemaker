/**
 * GameLoop - 游戏主循环类
 *
 * 从 a.java 逆向分析提取的游戏主循环逻辑。
 * 包含帧率控制、状态管理、暂停/恢复机制。
 *
 * 原始文件: a.java
 * 分析方法:
 *   - run() (行460-482): 主循环
 *   - b() (行1473-1642): 游戏逻辑更新
 *   - hideNotify() (行435-448): 暂停
 *   - showNotify() (行450-458): 恢复
 *   - d() (行483-509): 初始化
 */
public class GameLoop implements Runnable {

    // ==================== 游戏状态常量 ====================
    /** 状态: 初始化 */
    public static final int STATE_INIT = 0;
    /** 状态: 游戏进行中 */
    public static final int STATE_PLAYING = 1;
    /** 状态: 菜单 */
    public static final int STATE_MENU = 2;
    /** 状态: 退出 */
    public static final int STATE_EXIT = 3;
    /** 状态: 加载中 */
    public static final int STATE_LOADING = 4;
    /** 状态: 过场动画 */
    public static final int STATE_CUTSCENE = 5;
    /** 状态: 设置 */
    public static final int STATE_SETTINGS = 7;
    /** 状态: 关卡选择 */
    public static final int STATE_LEVEL_SELECT = 8;
    /** 状态: 帮助 */
    public static final int STATE_HELP = 9;
    /** 状态: 结算动画 */
    public static final int STATE_RESULT_ANIM = 10;
    /** 状态: 暂停菜单 */
    public static final int STATE_PAUSE = 12;
    /** 状态: 游戏结束 */
    public static final int STATE_GAME_OVER = 13;
    /** 状态: 完成 */
    public static final int STATE_COMPLETE = 14;
    /** 状态: 保存 */
    public static final int STATE_SAVE = 15;
    /** 状态: 错误 */
    public static final int STATE_ERROR = 16;

    // ==================== 帧率控制 ====================
    /** 目标帧间隔 (毫秒) - 约16fps */
    private static final long TARGET_FRAME_INTERVAL = 62L;
    /** 最小帧间隔 (毫秒) */
    private static final long MIN_FRAME_INTERVAL = 10L;

    // ==================== 游戏标志 ====================
    /** 游戏运行标志 - 对应 this.d */
    private volatile boolean gameRunning;
    /** 游戏终止标志 - 对应 this.e */
    private volatile boolean gameTerminated;
    /** 游戏暂停标志 - 对应 this.dL */
    private volatile boolean isPaused;
    /** 游戏初始化标志 - 对应 this.d (首次初始化) */
    private volatile boolean initialized;

    // ==================== 游戏状态 ====================
    /** 当前游戏状态 - 对应 this.l */
    private int currentState;
    /** 上一帧是否有更新 */
    private boolean frameUpdated;

    // ==================== 游戏回调接口 ====================
    private final GameLoopCallback callback;

    /**
     * 游戏循环回调接口
     */
    public interface GameLoopCallback {
        /** 初始化游戏 */
        void onInitialize();
        /** 更新游戏逻辑 */
        boolean onUpdate();
        /** 渲染画面 */
        void onRender();
        /** 处理暂停 */
        void onPause();
        /** 处理恢复 */
        void onResume();
        /** 游戏结束清理 */
        void onCleanup();
        /** 游戏退出 */
        void onExit();
    }

    /**
     * 构造函数
     * @param callback 游戏回调接口
     */
    public GameLoop(GameLoopCallback callback) {
        this.callback = callback;
        this.gameRunning = false;
        this.gameTerminated = false;
        this.isPaused = true;
        this.initialized = false;
        this.currentState = STATE_INIT;
        this.frameUpdated = true;
    }

    /**
     * 主游戏循环 - 对应 a.java run() 方法
     *
     * 原始逻辑:
     * while (!this.e) {
     *     long l = System.currentTimeMillis();
     *     if (this.d && (bl |= this.b())) {
     *         bl = false;
     *         this.repaint();
     *         this.serviceRepaints();
     *     }
     *     bl |= this.b();
     *     long l2 = System.currentTimeMillis() - l + 10L;
     *     if (l2 >= 62L) continue;
     *     Thread.sleep(62L - l2);
     * }
     */
    @Override
    public void run() {
        boolean needRender = true;

        while (!gameTerminated) {
            long frameStartTime = System.currentTimeMillis();

            if (gameRunning && (needRender |= update())) {
                needRender = false;
                render();
            }

            // 确保至少调用一次更新
            needRender |= update();

            // 帧率控制
            long elapsed = System.currentTimeMillis() - frameStartTime + MIN_FRAME_INTERVAL;
            if (elapsed < TARGET_FRAME_INTERVAL) {
                try {
                    Thread.sleep(TARGET_FRAME_INTERVAL - elapsed);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        // 游戏退出清理
        cleanup();
    }

    /**
     * 游戏逻辑更新 - 对应 a.java b() 方法
     *
     * 原始逻辑是一个大的 switch 语句，根据 this.l 状态分发
     */
    public boolean update() {
        try {
            // 如果暂停中，只处理暂停状态
            if (isPaused) {
                return handlePausedState();
            }

            // 状态机分发
            switch (currentState) {
                case STATE_INIT:
                    initializeGame();
                    return true;

                case STATE_PLAYING:
                    return updatePlaying();

                case STATE_MENU:
                    return updateMenu();

                case STATE_EXIT:
                    return updateExit();

                case STATE_LOADING:
                    return updateLoading();

                case STATE_CUTSCENE:
                    return updateCutscene();

                case STATE_SETTINGS:
                    return updateSettings();

                case STATE_LEVEL_SELECT:
                    return updateLevelSelect();

                case STATE_HELP:
                    return updateHelp();

                case STATE_RESULT_ANIM:
                    return updateResultAnim();

                case STATE_PAUSE:
                    return updatePause();

                case STATE_GAME_OVER:
                    return updateGameOver();

                case STATE_COMPLETE:
                    return updateComplete();

                case STATE_SAVE:
                    return updateSave();

                case STATE_ERROR:
                    return updateError();

                default:
                    return false;
            }
        } catch (Exception e) {
            handleError(e);
            return false;
        }
    }

    /**
     * 游戏初始化 - 对应 a.java d() 方法
     *
     * 原始逻辑:
     * - 计算屏幕参数
     * - 加载资源文件
     * - 初始化游戏数据
     */
    private void initializeGame() {
        if (!initialized) {
            initialized = true;
            if (callback != null) {
                callback.onInitialize();
            }
        }
        setState(STATE_PLAYING);
    }

    // ==================== 状态处理方法 ====================

    /**
     * 处理暂停状态
     */
    private boolean handlePausedState() {
        if (isPaused) {
            // 暂停中的更新逻辑
            return true;
        }
        return false;
    }

    /**
     * 游戏进行中状态更新
     */
    private boolean updatePlaying() {
        // 对应原 case 1: 的逻辑
        if (callback != null) {
            return callback.onUpdate();
        }
        return true;
    }

    /**
     * 菜单状态更新
     */
    private boolean updateMenu() {
        // 对应原 case 2: return this.C();
        return true;
    }

    /**
     * 退出状态更新
     */
    private boolean updateExit() {
        // 对应原 case 3: this.h.destroyApp(true);
        if (callback != null) {
            callback.onExit();
        }
        return false;
    }

    /**
     * 加载状态更新
     */
    private boolean updateLoading() {
        // 对应原 case 4: return this.ai() || this.bV > 0;
        return true;
    }

    /**
     * 过场动画状态更新
     */
    private boolean updateCutscene() {
        // 对应原 case 5: return this.an();
        return true;
    }

    /**
     * 设置状态更新
     */
    private boolean updateSettings() {
        // 对应原 case 7: return this.aq() || this.bV > 0;
        return true;
    }

    /**
     * 关卡选择状态更新
     */
    private boolean updateLevelSelect() {
        // 对应原 case 8: return this.as();
        return true;
    }

    /**
     * 帮助状态更新
     */
    private boolean updateHelp() {
        // 对应原 case 9: return this.at();
        return true;
    }

    /**
     * 结算动画状态更新
     */
    private boolean updateResultAnim() {
        // 对应原 case 10: 复杂的动画计数逻辑
        return true;
    }

    /**
     * 暂停菜单状态更新
     */
    private boolean updatePause() {
        // 对应原 case 12: return this.B();
        return true;
    }

    /**
     * 游戏结束状态更新
     */
    private boolean updateGameOver() {
        // 对应原 case 13: return this.w();
        return true;
    }

    /**
     * 完成状态更新
     */
    private boolean updateComplete() {
        // 对应原 case 14: return this.r();
        return true;
    }

    /**
     * 保存状态更新
     */
    private boolean updateSave() {
        // 对应原 case 15: return this.t();
        return true;
    }

    /**
     * 错误状态更新
     */
    private boolean updateError() {
        // 对应原 case 16: return this.E();
        return true;
    }

    // ==================== 暂停/恢复控制 ====================

    /**
     * 暂停游戏 - 对应 a.java hideNotify() 方法
     *
     * 原始逻辑:
     * if (this.d) {
     *     this.d = false;
     *     if (this.c == 1 && this.l != 16) {
     *         this.a();
     *         this.c = 0;
     *     }
     * }
     * if (!this.dL) {
     *     this.I = false;
     *     this.dL = true;
     *     this.l();
     * }
     */
    public void onPause() {
        if (gameRunning) {
            gameRunning = false;
            // 如果在游戏中，保存当前状态
            if (currentState == STATE_PLAYING) {
                saveCurrentState();
            }
        }

        if (!isPaused) {
            isPaused = true;
            if (callback != null) {
                callback.onPause();
            }
        }
    }

    /**
     * 恢复游戏 - 对应 a.java showNotify() 方法
     *
     * 原始逻辑:
     * if (!this.d && this.dP != null) {
     *     for (int i = 0; i < this.dT; ++i) {
     *         if (this.dQ[i] != 11) continue;
     *         this.dP[i] = this.a[4] + this.a[this.c == 1 ? 2 : 3];
     *     }
     * }
     * this.d = true;
     */
    public void onResume() {
        if (!gameRunning) {
            // 恢复音频等资源
            restoreResources();
        }
        gameRunning = true;
        isPaused = false;

        if (callback != null) {
            callback.onResume();
        }
    }

    /**
     * 保存当前状态
     */
    private void saveCurrentState() {
        // 子类可重写此方法保存游戏状态
    }

    /**
     * 恢复资源
     */
    private void restoreResources() {
        // 子类可重写此方法恢复音频等资源
    }

    // ==================== 渲染控制 ====================

    /**
     * 渲染画面
     */
    private void render() {
        if (callback != null) {
            callback.onRender();
        }
    }

    // ==================== 状态管理 ====================

    /**
     * 设置游戏状态
     * @param state 新状态
     */
    public void setState(int state) {
        this.currentState = state;
    }

    /**
     * 获取当前状态
     * @return 当前状态
     */
    public int getState() {
        return currentState;
    }

    // ==================== 生命周期控制 ====================

    /**
     * 启动游戏循环
     */
    public void start() {
        gameRunning = true;
        isPaused = false;
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * 停止游戏循环
     */
    public void stop() {
        gameTerminated = true;
        gameRunning = false;
    }

    /**
     * 清理资源
     */
    private void cleanup() {
        if (callback != null) {
            callback.onCleanup();
        }
    }

    /**
     * 错误处理
     */
    private void handleError(Exception e) {
        e.printStackTrace();
        setState(STATE_ERROR);
    }

    // ==================== Getter/Setter ====================

    public boolean isRunning() {
        return gameRunning && !gameTerminated;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        if (paused) {
            onPause();
        } else {
            onResume();
        }
    }
}
