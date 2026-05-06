/**
 * 游戏状态常量
 * 用于控制游戏主循环的状态机
 */
public final class GameState {
    public static final int GAME_PLAY = 1;
    public static final int GAME_PAUSED = 2;
    public static final int EXTRA_LEVELPACK = 4;
    public static final int MAIN_MENU = 5;
    public static final int SPLASH_SCREEN = 6;
    public static final int LEVEL_COMPLETE = 7;
    public static final int OPTIONS_MENU = 8;
    public static final int SUB_MENU = 9;
    public static final int LOADING_SCREEN = 10;
    public static final int FADE_BLACK = 11;
    public static final int TITLE_SCREEN = 12;
    public static final int MESSAGE_DIALOG = 13;
    public static final int HINT_DIALOG = 14;
    public static final int INGAME_MENU = 15;
    public static final int CONFIRM_DIALOG = 16;

    private GameState() {}
}
