/**
 * a - Bobby Carrot 5 游戏主画布 (重构版)
 *
 * 原始代码为6494行混淆单类，现拆分为18个独立模块。
 * 此文件作为J2ME Canvas入口，委托给BobbyGame处理。
 *
 * @see BobbyGame - 主游戏集成类
 * @see GameLoop - 游戏循环
 * @see GameLogic - 游戏逻辑
 * @see GameRenderer - 渲染系统
 * @see CollisionHandler - 碰撞检测
 * @see LevelInteraction - 关卡交互
 */
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public final class a extends Canvas {

    // 主游戏实例
    private BobbyGame game;
    private boolean initialized = false;

    /**
     * 构造函数 - 初始化游戏
     * @param bobby MIDlet主类
     */
    public a(Bobby bobby) {
        // 初始化游戏实例
        game = new BobbyGame(getWidth(), getHeight());
        initialized = false;
        setFullScreenMode(true);
    }

    /**
     * 绘制方法 - 委托给GameRenderer
     */
    public void paint(Graphics g) {
        if (!initialized) {
            initialized = true;
            game.start();
        }
        game.getRenderer().render(g, game);
    }

    /**
     * 按键按下 - 委托给InputHandler
     */
    public void keyPressed(int keyCode) {
        game.keyPressed(keyCode);
        repaint();
    }

    /**
     * 按键释放 - 委托给InputHandler
     */
    public void keyReleased(int keyCode) {
        game.keyReleased(keyCode);
    }

    /**
     * 隐藏通知 - 暂停游戏
     */
    public void hideNotify() {
        game.pause();
    }

    /**
     * 显示通知 - 恢复游戏
     */
    public void showNotify() {
        game.resume();
    }

    /**
     * 获取游戏实例
     */
    public BobbyGame getGame() {
        return game;
    }
}
