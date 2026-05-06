/**
 * 瓦片类型常量
 * 负值表示特殊地形
 */
public final class TileType {
    // 地形
    public static final byte GRASS_1 = -57;
    public static final byte GRASS_2 = -56;
    public static final byte ICE = -108;
    public static final byte DEATH_TRAP = -106;
    public static final byte DEATH_TRIGGER = -81;

    // 传送带
    public static final byte CONVEYOR_RIGHT = -66;
    public static final byte CONVEYOR_DOWN = -67;
    public static final byte CONVEYOR_DIAG_1 = -68;
    public static final byte CONVEYOR_DIAG_2 = -69;
    public static final byte CONVEYOR_DIAG_3 = -70;
    public static final byte CONVEYOR_DIAG_4 = -71;

    // 箭头(强制移动)
    public static final byte ARROW_RIGHT = -72;
    public static final byte ARROW_LEFT = -73;
    public static final byte ARROW_DOWN = -74;
    public static final byte ARROW_UP = -75;

    // 传送门
    public static final byte PORTAL_1 = -90;
    public static final byte PORTAL_2 = -94;

    // 门
    public static final byte DOOR_1 = -92;
    public static final byte DOOR_2 = -93;

    private TileType() {}
}
