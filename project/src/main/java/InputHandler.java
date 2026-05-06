/**
 * 输入处理器 - 按键状态管理
 */
public class InputHandler {
    // 方向键状态
    private boolean keyUpPressed = false;
    private boolean keyDownPressed = false;
    private boolean keyLeftPressed = false;
    private boolean keyRightPressed = false;
    private boolean keyFirePressed = false;

    // 自定义软键
    private int customKeyCode1 = -6;
    private int customKeyCode2 = -7;
    private boolean customKey1Pressed = false;
    private boolean customKey2Pressed = false;

    // 任意键按下标志
    private boolean anyKeyPressed = false;

    /**
     * 处理按键按下事件
     */
    public void keyPressed(int keyCode) {
        int gameAction = getGameAction(keyCode);

        switch (gameAction) {
            case 1: // UP
                keyUpPressed = true;
                break;
            case 6: // DOWN
                keyDownPressed = true;
                break;
            case 2: // LEFT
                keyLeftPressed = true;
                break;
            case 5: // RIGHT
                keyRightPressed = true;
                break;
            case 8: // FIRE
                keyFirePressed = true;
                break;
        }

        if (keyCode == customKeyCode1) {
            customKey1Pressed = true;
        } else if (keyCode == customKeyCode2) {
            customKey2Pressed = true;
        }

        anyKeyPressed = true;
    }

    /**
     * 处理按键释放事件
     */
    public void keyReleased(int keyCode) {
        int gameAction = getGameAction(keyCode);

        switch (gameAction) {
            case 1:
                keyUpPressed = false;
                break;
            case 6:
                keyDownPressed = false;
                break;
            case 2:
                keyLeftPressed = false;
                break;
            case 5:
                keyRightPressed = false;
                break;
            case 8:
                keyFirePressed = false;
                break;
        }

        if (keyCode == customKeyCode1) {
            customKey1Pressed = false;
        } else if (keyCode == customKeyCode2) {
            customKey2Pressed = false;
        }
    }

    /**
     * 重置所有按键状态
     */
    public void resetKeys() {
        keyUpPressed = false;
        keyDownPressed = false;
        keyLeftPressed = false;
        keyRightPressed = false;
        keyFirePressed = false;
        customKey1Pressed = false;
        customKey2Pressed = false;
        anyKeyPressed = false;
    }

    /**
     * 获取GameAction (子类Canvas需要实现)
     */
    protected int getGameAction(int keyCode) {
        return 0;
    }

    // Getter
    public boolean isUpPressed() { return keyUpPressed; }
    public boolean isDownPressed() { return keyDownPressed; }
    public boolean isLeftPressed() { return keyLeftPressed; }
    public boolean isRightPressed() { return keyRightPressed; }
    public boolean isFirePressed() { return keyFirePressed; }
    public boolean isCustomKey1Pressed() { return customKey1Pressed; }
    public boolean isCustomKey2Pressed() { return customKey2Pressed; }
    public boolean isAnyKeyPressed() { return anyKeyPressed; }

    // Setter
    public void setCustomKeyCode1(int code) { this.customKeyCode1 = code; }
    public void setCustomKeyCode2(int code) { this.customKeyCode2 = code; }
}
