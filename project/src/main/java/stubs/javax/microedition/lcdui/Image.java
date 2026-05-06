package javax.microedition.lcdui;
import java.io.InputStream;
public class Image {
    public static Image createImage(String name) { return new Image(); }
    public static Image createImage(InputStream stream) { return new Image(); }
    public static Image createImage(int w, int h) { return new Image(); }
    public static Image createImage(Image img) { return new Image(); }
    public int getWidth() { return 32; }
    public int getHeight() { return 32; }
    public Graphics getGraphics() { return new Graphics(); }
    public boolean isMutable() { return false; }
}
