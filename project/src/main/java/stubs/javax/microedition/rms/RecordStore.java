package javax.microedition.rms;
public class RecordStore {
    public static RecordStore openRecordStore(String name, boolean createIfNecessary) { return new RecordStore(); }
    public static void deleteRecordStore(String name) {}
    public int getNumRecords() { return 0; }
    public int addRecord(byte[] data, int offset, int numBytes) { return 0; }
    public void setRecord(int recordId, byte[] data, int offset, int numBytes) {}
    public byte[] getRecord(int recordId) { return null; }
    public void closeRecordStore() {}
}
