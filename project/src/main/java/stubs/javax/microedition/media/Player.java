package javax.microedition.media;
import java.io.InputStream;
public interface Player {
    void realize();
    void prefetch();
    void start();
    void stop();
    void deallocate();
    void close();
    void setLoopCount(int count);
    Object getControl(String type);
}
