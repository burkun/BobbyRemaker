/**
 * MenuSystem - 菜单系统类
 *
 * 从 a.java 逆向提取的菜单逻辑
 * 包含主菜单、选项菜单、关卡选择等
 */
public class MenuSystem {

    // ==================== 菜单状态常量 ====================
    public static final int MENU_NONE = 0;
    public static final int MENU_MAIN = 1;
    public static final int MENU_OPTIONS = 2;
    public static final int MENU_LEVEL_SELECT = 3;
    public static final int MENU_PAUSE = 4;
    public static final int MENU_CONFIRM = 5;
    public static final int MENU_SHOP = 6;
    public static final int MENU_CREDITS = 7;
    public static final int MENU_HELP = 8;

    // ==================== 菜单项定义 ====================
    private static final String[] MAIN_MENU_ITEMS = {
        "START GAME", "OPTIONS", "SHOP", "HELP", "CREDITS", "EXIT"
    };

    private static final String[] OPTIONS_MENU_ITEMS = {
        "SOUND", "MUSIC", "FULLSCREEN", "DIFFICULTY", "LANGUAGE", "KEYS", "BACK"
    };

    private static final String[] PAUSE_MENU_ITEMS = {
        "CONTINUE", "OPTIONS", "RESTART", "MAIN MENU"
    };

    // ==================== 菜单状态 ====================
    private int currentMenu;
    private int selectedIndex;
    private int scrollOffset;
    private int maxVisibleItems;

    // ==================== 选项状态 ====================
    private boolean soundEnabled;
    private boolean musicEnabled;
    private boolean fullscreenEnabled;
    private int difficulty;        // 0-3
    private int languageIndex;      // 0-4
    private int customKeyCode1;
    private int customKeyCode2;

    // ==================== 关卡选择状态 ====================
    private int selectedWorld;
    private int selectedLevel;
    private int unlockedWorlds;
    private boolean[] worldUnlocked;
    private byte[] levelUnlocked;   // 每个世界的关卡解锁状态
    private boolean[] levelCompleted;

    // ==================== 确认对话框 ====================
    private String confirmTitle;
    private String confirmMessage;
    private String confirmYes;
    private String confirmNo;
    private int confirmAction;

    // ==================== 滚动状态 ====================
    private int scrollY;
    private int targetScrollY;
    private int itemHeight;
    private boolean scrolling;

    // ==================== 回调接口 ====================
    public interface MenuCallback {
        void onMenuItemSelected(int menuType, int itemIndex);
        void onMenuCancelled(int menuType);
        void onConfirmResult(int action, boolean confirmed);
    }

    private MenuCallback callback;

    // ==================== 构造函数 ====================
    public MenuSystem() {
        reset();
    }

    public void reset() {
        currentMenu = MENU_NONE;
        selectedIndex = 0;
        scrollOffset = 0;
        maxVisibleItems = 6;

        soundEnabled = true;
        musicEnabled = true;
        fullscreenEnabled = false;
        difficulty = 1;
        languageIndex = 0;
        customKeyCode1 = -6;
        customKeyCode2 = -7;

        selectedWorld = 0;
        selectedLevel = 0;

        confirmTitle = null;
        confirmMessage = null;
        confirmYes = "YES";
        confirmNo = "NO";
        confirmAction = 0;

        scrollY = 0;
        targetScrollY = 0;
        itemHeight = 20;
        scrolling = false;
    }

    // ==================== 菜单导航 ====================

    public void showMainMenu() {
        currentMenu = MENU_MAIN;
        selectedIndex = 0;
        scrollOffset = 0;
    }

    public void showOptionsMenu() {
        currentMenu = MENU_OPTIONS;
        selectedIndex = 0;
    }

    public void showPauseMenu() {
        currentMenu = MENU_PAUSE;
        selectedIndex = 0;
    }

    public void showLevelSelect(int world) {
        currentMenu = MENU_LEVEL_SELECT;
        selectedWorld = world;
        selectedIndex = 0;
    }

    public void showConfirm(int action, String title, String message, String yes, String no) {
        currentMenu = MENU_CONFIRM;
        confirmAction = action;
        confirmTitle = title;
        confirmMessage = message;
        confirmYes = yes != null ? yes : "YES";
        confirmNo = no != null ? no : "NO";
        selectedIndex = 0;
    }

    public void closeMenu() {
        currentMenu = MENU_NONE;
    }

    // ==================== 输入处理 ====================

    public void moveUp() {
        int itemCount = getMenuItemCount();
        if (itemCount <= 0) return;

        selectedIndex--;
        if (selectedIndex < 0) {
            selectedIndex = itemCount - 1;
        }
        updateScroll();
    }

    public void moveDown() {
        int itemCount = getMenuItemCount();
        if (itemCount <= 0) return;

        selectedIndex++;
        if (selectedIndex >= itemCount) {
            selectedIndex = 0;
        }
        updateScroll();
    }

    public void select() {
        if (callback != null) {
            callback.onMenuItemSelected(currentMenu, selectedIndex);
        }
        handleMenuAction();
    }

    public void cancel() {
        if (callback != null) {
            callback.onMenuCancelled(currentMenu);
        }

        switch (currentMenu) {
            case MENU_OPTIONS:
            case MENU_LEVEL_SELECT:
            case MENU_PAUSE:
                showMainMenu();
                break;
            case MENU_CONFIRM:
                closeMenu();
                break;
            default:
                closeMenu();
                break;
        }
    }

    // ==================== 菜单动作处理 ====================

    private void handleMenuAction() {
        switch (currentMenu) {
            case MENU_MAIN:
                handleMainMenuAction(selectedIndex);
                break;
            case MENU_OPTIONS:
                handleOptionsMenuAction(selectedIndex);
                break;
            case MENU_PAUSE:
                handlePauseMenuAction(selectedIndex);
                break;
            case MENU_LEVEL_SELECT:
                handleLevelSelectAction(selectedIndex);
                break;
            case MENU_CONFIRM:
                handleConfirmAction(selectedIndex == 0);
                break;
        }
    }

    private void handleMainMenuAction(int index) {
        switch (index) {
            case 0: // START GAME
                showLevelSelect(0);
                break;
            case 1: // OPTIONS
                showOptionsMenu();
                break;
            case 2: // SHOP
                currentMenu = MENU_SHOP;
                break;
            case 3: // HELP
                currentMenu = MENU_HELP;
                break;
            case 4: // CREDITS
                currentMenu = MENU_CREDITS;
                break;
            case 5: // EXIT
                showConfirm(0, "EXIT", "ARE YOU SURE?", "YES", "NO");
                break;
        }
    }

    private void handleOptionsMenuAction(int index) {
        switch (index) {
            case 0: // SOUND
                soundEnabled = !soundEnabled;
                break;
            case 1: // MUSIC
                musicEnabled = !musicEnabled;
                break;
            case 2: // FULLSCREEN
                fullscreenEnabled = !fullscreenEnabled;
                break;
            case 3: // DIFFICULTY
                difficulty = (difficulty + 1) % 4;
                break;
            case 4: // LANGUAGE
                languageIndex = (languageIndex + 1) % 5;
                break;
            case 5: // KEYS - 自定义按键设置
                // 进入按键设置模式
                break;
            case 6: // BACK
                showMainMenu();
                break;
        }
    }

    private void handlePauseMenuAction(int index) {
        switch (index) {
            case 0: // CONTINUE
                closeMenu();
                break;
            case 1: // OPTIONS
                showOptionsMenu();
                break;
            case 2: // RESTART
                showConfirm(1, "RESTART", "RESTART LEVEL?", "YES", "NO");
                break;
            case 3: // MAIN MENU
                showConfirm(2, "QUIT", "RETURN TO MAIN MENU?", "YES", "NO");
                break;
        }
    }

    private void handleLevelSelectAction(int index) {
        selectedLevel = index;
        // 触发关卡加载
        if (callback != null) {
            callback.onMenuItemSelected(MENU_LEVEL_SELECT, index);
        }
    }

    private void handleConfirmAction(boolean confirmed) {
        if (callback != null) {
            callback.onConfirmResult(confirmAction, confirmed);
        }

        if (confirmed) {
            switch (confirmAction) {
                case 0: // EXIT
                    System.exit(0);
                    break;
                case 1: // RESTART
                    closeMenu();
                    break;
                case 2: // MAIN MENU
                    showMainMenu();
                    break;
            }
        } else {
            closeMenu();
        }
    }

    // ==================== 滚动处理 ====================

    private void updateScroll() {
        if (selectedIndex < scrollOffset) {
            scrollOffset = selectedIndex;
        } else if (selectedIndex >= scrollOffset + maxVisibleItems) {
            scrollOffset = selectedIndex - maxVisibleItems + 1;
        }
    }

    public void update() {
        if (scrolling) {
            if (scrollY < targetScrollY) {
                scrollY += 2;
                if (scrollY >= targetScrollY) {
                    scrollY = targetScrollY;
                    scrolling = false;
                }
            } else if (scrollY > targetScrollY) {
                scrollY -= 2;
                if (scrollY <= targetScrollY) {
                    scrollY = targetScrollY;
                    scrolling = false;
                }
            }
        }
    }

    // ==================== 辅助方法 ====================

    private int getMenuItemCount() {
        switch (currentMenu) {
            case MENU_MAIN:
                return MAIN_MENU_ITEMS.length;
            case MENU_OPTIONS:
                return OPTIONS_MENU_ITEMS.length;
            case MENU_PAUSE:
                return PAUSE_MENU_ITEMS.length;
            case MENU_LEVEL_SELECT:
                return getLevelCount(selectedWorld);
            case MENU_CONFIRM:
                return 2;
            default:
                return 0;
        }
    }

    private int getLevelCount(int world) {
        // 每个世界约16个关卡
        return 16;
    }

    public String getMenuItemText(int menuType, int index) {
        switch (menuType) {
            case MENU_MAIN:
                return index < MAIN_MENU_ITEMS.length ? MAIN_MENU_ITEMS[index] : null;
            case MENU_OPTIONS:
                if (index < OPTIONS_MENU_ITEMS.length) {
                    String item = OPTIONS_MENU_ITEMS[index];
                    switch (index) {
                        case 0: return item + ": " + (soundEnabled ? "ON" : "OFF");
                        case 1: return item + ": " + (musicEnabled ? "ON" : "OFF");
                        case 2: return item + ": " + (fullscreenEnabled ? "ON" : "OFF");
                        case 3: return item + ": " + getDifficultyName();
                        case 4: return item + ": " + getLanguageName();
                        default: return item;
                    }
                }
                return null;
            case MENU_PAUSE:
                return index < PAUSE_MENU_ITEMS.length ? PAUSE_MENU_ITEMS[index] : null;
            case MENU_CONFIRM:
                return index == 0 ? confirmYes : confirmNo;
            default:
                return null;
        }
    }

    private String getDifficultyName() {
        switch (difficulty) {
            case 0: return "EASY";
            case 1: return "NORMAL";
            case 2: return "HARD";
            case 3: return "EXPERT";
            default: return "NORMAL";
        }
    }

    private String getLanguageName() {
        String[] languages = {"EN", "DE", "FR", "ES", "IT"};
        return languageIndex < languages.length ? languages[languageIndex] : "EN";
    }

    // ==================== Getter/Setter ====================

    public int getCurrentMenu() {
        return currentMenu;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void setMusicEnabled(boolean musicEnabled) {
        this.musicEnabled = musicEnabled;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getSelectedWorld() {
        return selectedWorld;
    }

    public int getSelectedLevel() {
        return selectedLevel;
    }

    public MenuCallback getCallback() {
        return callback;
    }

    public void setCallback(MenuCallback callback) {
        this.callback = callback;
    }

    public String getConfirmTitle() {
        return confirmTitle;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }
}
