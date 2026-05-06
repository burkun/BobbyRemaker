import javax.microedition.rms.RecordStore;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Random;

/**
 * 存档管理器 - 游戏进度保存与加载
 */
public class SaveManager {
    private static final String STORE_NAME = "BC5Data";

    // 存档数据字段
    private String languageCode = "EN";
    private byte lastKeyPressed = 3;
    private byte[] levelUnlockStates = new byte[4];
    private boolean[] levelCompleteFlags = new boolean[4];
    private byte[] achievementFlags = new byte[7];
    private boolean soundEnabled = false;
    private boolean musicEnabled = false;
    private byte difficulty = 0;
    private short coins = 0;
    private short score = 0;
    private long gameTime = 0L;
    private int randomSeed = 0;
    private String[] playerNames = new String[5];
    private boolean fullscreen = false;

    private boolean firstLaunch = false;
    private Random random;

    public SaveManager(Random random) {
        this.random = random;
        for (int i = 0; i < playerNames.length; i++) {
            playerNames[i] = "";
        }
    }

    /**
     * 初始化或验证存档
     * 首次运行或存档损坏时创建默认存档
     */
    public void initOrValidateSaveData() {
        firstLaunch = false;
        try {
            RecordStore recordStore = null;
            int recordCount = -1;
            try {
                recordStore = RecordStore.openRecordStore(STORE_NAME, true);
                recordCount = recordStore.getNumRecords();
            } catch (Exception e) {
            }

            if (recordCount != 1) {
                firstLaunch = true;
                if (recordCount != 0) {
                    if (recordStore != null) {
                        recordStore.closeRecordStore();
                    }
                    RecordStore.deleteRecordStore(STORE_NAME);
                    recordStore = RecordStore.openRecordStore(STORE_NAME, true);
                }

                // 初始化默认值
                for (int i = 0; i < 4; i++) {
                    levelUnlockStates[i] = 0;
                    levelCompleteFlags[i] = false;
                }
                for (int i = 0; i < achievementFlags.length; i++) {
                    achievementFlags[i] = 0;
                }
                soundEnabled = false;
                musicEnabled = false;
                difficulty = 0;
                coins = 0;
                score = 0;
                gameTime = 0L;
                randomSeed = random.nextInt();
                for (int i = 0; i < playerNames.length; i++) {
                    playerNames[i] = "";
                }
                fullscreen = false;

                byte[] data = serializeSaveData();
                recordStore.addRecord(data, 0, data.length);
            }
            recordStore.closeRecordStore();
        } catch (Exception e) {
            // 错误处理由调用方负责
        }
    }

    /**
     * 删除所有存档
     */
    public void deleteAllSaveData() {
        try {
            RecordStore.deleteRecordStore(STORE_NAME);
        } catch (Exception e) {
        }
    }

    /**
     * 加载存档数据
     */
    public void loadSaveData() {
        ByteArrayInputStream bais = null;
        DataInputStream dis = null;
        try {
            bais = new ByteArrayInputStream(readRecord(STORE_NAME, 1));
            dis = new DataInputStream(bais);

            languageCode = dis.readUTF();
            lastKeyPressed = dis.readByte();
            for (int i = 0; i < 4; i++) {
                levelUnlockStates[i] = dis.readByte();
                levelCompleteFlags[i] = dis.readBoolean();
            }
            for (int i = 0; i < achievementFlags.length; i++) {
                achievementFlags[i] = dis.readByte();
            }
            soundEnabled = dis.readBoolean();
            musicEnabled = dis.readBoolean();
            difficulty = dis.readByte();
            coins = dis.readShort();
            score = dis.readShort();
            gameTime = dis.readLong();
            randomSeed = dis.readInt();
            for (int i = 0; i < playerNames.length; i++) {
                playerNames[i] = dis.readUTF();
            }
            fullscreen = dis.readBoolean();
        } catch (Exception e) {
        } finally {
            if (dis != null) {
                try { dis.close(); } catch (Exception e) {}
            }
            if (bais != null) {
                try { bais.close(); } catch (Exception e) {}
            }
        }
    }

    /**
     * 保存存档数据
     */
    public void saveSaveData() {
        writeRecord(STORE_NAME, 1, serializeSaveData());
    }

    /**
     * 序列化存档数据为byte[]
     */
    public byte[] serializeSaveData() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] result = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);

            dos.writeUTF(languageCode);
            dos.writeByte(lastKeyPressed);
            for (int i = 0; i < 4; i++) {
                dos.writeByte(levelUnlockStates[i]);
                dos.writeBoolean(levelCompleteFlags[i]);
            }
            for (int i = 0; i < achievementFlags.length; i++) {
                dos.writeByte(achievementFlags[i]);
            }
            dos.writeBoolean(soundEnabled);
            dos.writeBoolean(musicEnabled);
            dos.writeByte(difficulty);
            dos.writeShort(coins);
            dos.writeShort(score);
            dos.writeLong(gameTime);
            dos.writeInt(randomSeed);
            for (int i = 0; i < playerNames.length; i++) {
                dos.writeUTF(playerNames[i]);
            }
            dos.writeBoolean(fullscreen);

            result = baos.toByteArray();
        } catch (Exception e) {
        } finally {
            if (dos != null) {
                try { dos.close(); } catch (Exception e) {}
            }
            if (baos != null) {
                try { baos.close(); } catch (Exception e) {}
            }
        }
        return result;
    }

    /**
     * 读取记录
     */
    private byte[] readRecord(String storeName, int recordId) {
        byte[] result = null;
        try {
            RecordStore recordStore = RecordStore.openRecordStore(storeName, false);
            result = recordStore.getRecord(recordId);
            recordStore.closeRecordStore();
        } catch (Exception e) {
            try {
                RecordStore.deleteRecordStore(storeName);
            } catch (Exception e2) {
            }
        }
        return result;
    }

    /**
     * 写入记录
     */
    private void writeRecord(String storeName, int recordId, byte[] data) {
        try {
            RecordStore recordStore = RecordStore.openRecordStore(storeName, false);
            recordStore.setRecord(recordId, data, 0, data.length);
            recordStore.closeRecordStore();
        } catch (Exception e) {
            try {
                RecordStore.deleteRecordStore(storeName);
            } catch (Exception e2) {
            }
        }
    }

    // Getter/Setter
    public boolean isFirstLaunch() { return firstLaunch; }
    public String getLanguageCode() { return languageCode; }
    public void setLanguageCode(String code) { this.languageCode = code; }
    public byte[] getLevelUnlockStates() { return levelUnlockStates; }
    public boolean[] getLevelCompleteFlags() { return levelCompleteFlags; }
    public byte[] getAchievementFlags() { return achievementFlags; }
    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean enabled) { this.soundEnabled = enabled; }
    public boolean isMusicEnabled() { return musicEnabled; }
    public void setMusicEnabled(boolean enabled) { this.musicEnabled = enabled; }
    public short getCoins() { return coins; }
    public void setCoins(short coins) { this.coins = coins; }
    public short getScore() { return score; }
    public void setScore(short score) { this.score = score; }
    public long getGameTime() { return gameTime; }
    public String[] getPlayerNames() { return playerNames; }
}