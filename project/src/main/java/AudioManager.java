import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;
import java.io.InputStream;

/**
 * 音频管理器 - MIDI播放控制
 */
public class AudioManager {
    private Player midiPlayer = null;
    private String currentSoundtrack = null;

    /**
     * 播放MIDI音频
     * @param resourcePath 资源路径 (如 "/title.mid")
     * @param volumeLevel 音量级别 (0-4)
     * @param loop 是否循环播放
     */
    public void playMidi(String resourcePath, int volumeLevel, boolean loop) {
        if (loop) {
            if (currentSoundtrack != null && currentSoundtrack.compareTo(resourcePath) == 0) {
                return;
            }
        } else {
            currentSoundtrack = null;
        }
        stopSoundtrack();
        try {
            InputStream inputStream = getClass().getResourceAsStream(resourcePath);
            midiPlayer = Manager.createPlayer(inputStream, "audio/midi");
            midiPlayer.setLoopCount(loop ? -1 : 1);
            midiPlayer.realize();
            try {
                VolumeControl volumeControl = (VolumeControl) midiPlayer.getControl("VolumeControl");
                if (volumeControl != null) {
                    volumeControl.setLevel(volumeLevel * 25);
                }
            } catch (Exception e) {
            }
            midiPlayer.prefetch();
            midiPlayer.start();
            currentSoundtrack = resourcePath;
        } catch (Exception e) {
        }
    }

    /**
     * 停止音频播放并释放资源
     */
    public void stopSoundtrack() {
        if (midiPlayer != null) {
            try {
                midiPlayer.stop();
                midiPlayer.deallocate();
                midiPlayer.close();
            } catch (Throwable t) {
            }
            midiPlayer = null;
        }
        currentSoundtrack = null;
    }

    /**
     * 获取当前播放的音频路径
     */
    public String getCurrentSoundtrack() {
        return currentSoundtrack;
    }

    /**
     * 检查是否正在播放
     */
    public boolean isPlaying() {
        return midiPlayer != null;
    }
}
