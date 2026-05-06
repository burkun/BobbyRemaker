package javax.microedition.lcdui;
public abstract class Canvas extends Displayable {
    public static final int UP = 1;
    public static final int DOWN = 6;
    public static final int LEFT = 2;
    public static final int RIGHT = 5;
    public static final int FIRE = 8;
    public Canvas() {}
    public final void setFullScreenMode(boolean mode) {}
    public int getWidth() { return 240; }
    public int getHeight() { return 320; }
    public final void repaint() {}
    public final void serviceRepaints() {}
    protected abstract void paint(Graphics g);
    protected void keyPressed(int keyCode) {}
    protected void keyReleased(int keyCode) {}
    protected void showNotify() {}
    protected void hideNotify() {}
    public int getGameAction(int keyCode) { return 0; }
    public int getKeyCode(int gameAction) { return 0; }
    public String getKeyName(int keyCode) { return ""; }
}
