package javax.microedition.lcdui;
public class Graphics {
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    public static final int TOP = 16;
    public static final int BOTTOM = 32;
    public static final int HCENTER = 1;
    public static final int VCENTER = 2;
    public static final int BASELINE = 64;
    public void setColor(int RGB) {}
    public void fillRect(int x, int y, int w, int h) {}
    public void drawRect(int x, int y, int w, int h) {}
    public void drawImage(Image img, int x, int y, int anchor) {}
    public void drawRegion(Image src, int x, int y, int w, int h, int transform, int dx, int dy, int anchor) {}
    public void setClip(int x, int y, int w, int h) {}
    public void clipRect(int x, int y, int w, int h) {}
    public void drawLine(int x1, int y1, int x2, int y2) {}
    public void fillArc(int x, int y, int w, int h, int startAngle, int arcAngle) {}
    public void drawArc(int x, int y, int w, int h, int startAngle, int arcAngle) {}
    public void drawString(String str, int x, int y, int anchor) {}
    public void drawChar(char c, int x, int y, int anchor) {}
    public void drawSubstring(String str, int offset, int len, int x, int y, int anchor) {}
}
