/**
 * LevelData - Manages level map data for Bobby Carrot 5.
 *
 * This class encapsulates the tile and object maps for game levels,
 * providing clean access methods for level data queries.
 *
 * Based on reverse engineering of a.java:
 * - cl[][] -> tileMap (terrain/surface tiles)
 * - cm[][] -> objectMap (collectible items/objects)
 * - dn    -> mapWidth
 * - cfr_renamed_0 -> mapHeight
 * - bM    -> levelPackId
 * - bL    -> levelId
 * - bN    -> totalLevels
 * - bO    -> carrotsCollected
 *
 * Original methods:
 * - e(int, int) -> loadLevel()
 * - b(int, int) -> getTileAt()
 * - c(int, int) -> getObjectAt()
 */
public class LevelData {

    // Tile constants (based on analysis)
    public static final byte TILE_INVALID = -1;

    // Conveyor belt tiles (directional)
    public static final byte TILE_CONVEYOR_RIGHT = -66;      // 190
    public static final byte TILE_CONVEYOR_UP = -67;          // 189
    public static final byte TILE_CONVEYOR_DOWN_RIGHT = -68; // 188
    public static final byte TILE_CONVEYOR_DOWN_LEFT = -69;   // 187
    public static final byte TILE_CONVEYOR_UP_LEFT = -70;    // 186
    public static final byte TILE_CONVEYOR_UP_RIGHT = -71;   // 185

    // Interactive tiles
    public static final byte TILE_GRASS_MOWABLE_1 = -57;      // 199
    public static final byte TILE_GRASS_MOWABLE_2 = -56;      // 200
    public static final byte TILE_DEATH_TRAP = -106;         // 150
    public static final byte TILE_ICE = -108;                // 148
    public static final byte TILE_PORTAL_A = -90;            // 166
    public static final byte TILE_PORTAL_B = -94;            // 162
    public static final byte TILE_SPAWN_POINT = -107;        // 149

    // Object constants
    public static final byte OBJECT_INVALID = -1;
    public static final byte OBJECT_CARROT = -54;            // 202
    public static final byte OBJECT_CARROT_SPECIAL = -53;    // 203
    public static final byte OBJECT_BONUS_ITEM = -33;        // 223

    // Map data arrays
    private byte[][] tileMap;      // cl[][] - terrain/surface tiles
    private byte[][] objectMap;    // cm[][] - objects/collectibles

    // Map dimensions
    private int mapWidth;          // dn
    private int mapHeight;         // cfr_renamed_0

    // Level identification
    private int levelPackId;       // bM - which level pack (0 = main game)
    private int levelId;           // bL - level number within pack
    private int totalLevels;       // bN - total levels in current pack

    // Level statistics
    private int carrotsCollected;  // bO - carrots collected in current level

    // Resource path (for loading)
    private String levelResourcePath;

    /**
     * Default constructor - creates empty level data
     */
    public LevelData() {
        this.tileMap = null;
        this.objectMap = null;
        this.mapWidth = 0;
        this.mapHeight = 0;
        this.levelPackId = 0;
        this.levelId = 0;
        this.totalLevels = 0;
        this.carrotsCollected = 0;
        this.levelResourcePath = null;
    }

    /**
     * Constructor with dimensions - creates empty maps
     * @param width Map width in tiles
     * @param height Map height in tiles
     */
    public LevelData(int width, int height) {
        this.mapWidth = width;
        this.mapHeight = height;
        this.tileMap = new byte[height][width];
        this.objectMap = new byte[height][width];

        // Initialize object map with invalid values
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.objectMap[y][x] = OBJECT_INVALID;
            }
        }

        this.levelPackId = 0;
        this.levelId = 0;
        this.totalLevels = 0;
        this.carrotsCollected = 0;
    }

    /**
     * Get the tile value at the specified coordinates.
     * Returns TILE_INVALID if coordinates are out of bounds.
     *
     * @param x X coordinate (column)
     * @param y Y coordinate (row)
     * @return Tile byte value, or TILE_INVALID if out of bounds
     */
    public byte getTileAt(int x, int y) {
        if (x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
            return TILE_INVALID;
        }
        return tileMap[y][x];
    }

    /**
     * Get the object value at the specified coordinates.
     * Returns OBJECT_INVALID if coordinates are out of bounds.
     *
     * @param x X coordinate (column)
     * @param y Y coordinate (row)
     * @return Object byte value, or OBJECT_INVALID if out of bounds
     */
    public byte getObjectAt(int x, int y) {
        if (x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
            return OBJECT_INVALID;
        }
        return objectMap[y][x];
    }

    /**
     * Set the tile value at the specified coordinates.
     * Does nothing if coordinates are out of bounds.
     *
     * @param x X coordinate (column)
     * @param y Y coordinate (row)
     * @param tile Tile value to set
     */
    public void setTileAt(int x, int y, byte tile) {
        if (x >= 0 && y >= 0 && x < mapWidth && y < mapHeight) {
            tileMap[y][x] = tile;
        }
    }

    /**
     * Set the object value at the specified coordinates.
     * Does nothing if coordinates are out of bounds.
     *
     * @param x X coordinate (column)
     * @param y Y coordinate (row)
     * @param object Object value to set
     */
    public void setObjectAt(int x, int y, byte object) {
        if (x >= 0 && y >= 0 && x < mapWidth && y < mapHeight) {
            objectMap[y][x] = object;
        }
    }

    /**
     * Check if a tile is walkable (not solid).
     * Based on analysis, tiles with values 94-200 are generally walkable.
     *
     * @param x X coordinate (column)
     * @param y Y coordinate (row)
     * @return true if the tile is walkable
     */
    public boolean isTileWalkable(int x, int y) {
        byte tile = getTileAt(x, y);
        if (tile == TILE_INVALID) {
            return false;
        }

        int tileValue = tile & 0xFF;
        // Tiles 94-200 are generally walkable surfaces
        return tileValue >= 94 && tileValue <= 200;
    }

    /**
     * Check if there's a collectible object at the position.
     *
     * @param x X coordinate (column)
     * @param y Y coordinate (row)
     * @return true if there's a collectible at this position
     */
    public boolean hasCollectible(int x, int y) {
        byte object = getObjectAt(x, y);
        return object != OBJECT_INVALID && object != -1;
    }

    /**
     * Collect (remove) the object at the specified position.
     *
     * @param x X coordinate (column)
     * @param y Y coordinate (row)
     * @return The object that was collected, or OBJECT_INVALID if none
     */
    public byte collectObjectAt(int x, int y) {
        byte object = getObjectAt(x, y);
        if (object != OBJECT_INVALID) {
            setObjectAt(x, y, OBJECT_INVALID);
        }
        return object;
    }

    /**
     * Load a level from resource data.
     * This method simulates the original e(int, int) loading process.
     *
     * Note: In the original J2ME code, this reads from .dat files.
     * This is a simplified version for documentation purposes.
     *
     * @param packId Level pack ID (0 for main game)
     * @param level Level number within pack
     * @param tileData Raw tile data (row-major order)
     * @param width Map width
     * @param height Map height
     */
    public void loadLevel(int packId, int level, byte[] tileData, int width, int height) {
        this.levelPackId = packId;
        this.levelId = level;
        this.mapWidth = width;
        this.mapHeight = height;

        // Allocate maps
        this.tileMap = new byte[height][width];
        this.objectMap = new byte[height][width];

        // Copy tile data
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tileMap[y][x] = tileData[index++];
                objectMap[y][x] = OBJECT_INVALID;
            }
        }

        // Reset level state
        this.carrotsCollected = 0;
    }

    /**
     * Find the spawn point position in the level.
     * Searches for TILE_SPAWN_POINT (-107) in the tile map.
     *
     * @return int[2] with {x, y} of spawn point, or null if not found
     */
    public int[] findSpawnPoint() {
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (tileMap[y][x] == TILE_SPAWN_POINT) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    /**
     * Count total carrots in the level.
     *
     * @return Number of carrots in object map
     */
    public int countCarrots() {
        int count = 0;
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                byte obj = objectMap[y][x];
                if (obj == OBJECT_CARROT || obj == OBJECT_CARROT_SPECIAL) {
                    count++;
                }
            }
        }
        return count;
    }

    // Getters and setters

    public byte[][] getTileMap() {
        return tileMap;
    }

    public void setTileMap(byte[][] tileMap) {
        this.tileMap = tileMap;
        if (tileMap != null && tileMap.length > 0) {
            this.mapHeight = tileMap.length;
            this.mapWidth = tileMap[0].length;
        }
    }

    public byte[][] getObjectMap() {
        return objectMap;
    }

    public void setObjectMap(byte[][] objectMap) {
        this.objectMap = objectMap;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getLevelPackId() {
        return levelPackId;
    }

    public void setLevelPackId(int levelPackId) {
        this.levelPackId = levelPackId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getTotalLevels() {
        return totalLevels;
    }

    public void setTotalLevels(int totalLevels) {
        this.totalLevels = totalLevels;
    }

    public int getCarrotsCollected() {
        return carrotsCollected;
    }

    public void setCarrotsCollected(int carrotsCollected) {
        this.carrotsCollected = carrotsCollected;
    }

    public void incrementCarrots() {
        this.carrotsCollected++;
    }

    /**
     * Get a human-readable tile name for debugging.
     *
     * @param tile Tile byte value
     * @return String description of the tile
     */
    public static String getTileName(byte tile) {
        switch (tile) {
            case TILE_CONVEYOR_RIGHT: return "CONVEYOR_RIGHT";
            case TILE_CONVEYOR_UP: return "CONVEYOR_UP";
            case TILE_CONVEYOR_DOWN_RIGHT: return "CONVEYOR_DOWN_RIGHT";
            case TILE_CONVEYOR_DOWN_LEFT: return "CONVEYOR_DOWN_LEFT";
            case TILE_CONVEYOR_UP_LEFT: return "CONVEYOR_UP_LEFT";
            case TILE_CONVEYOR_UP_RIGHT: return "CONVEYOR_UP_RIGHT";
            case TILE_GRASS_MOWABLE_1: return "GRASS_MOWABLE_1";
            case TILE_GRASS_MOWABLE_2: return "GRASS_MOWABLE_2";
            case TILE_DEATH_TRAP: return "DEATH_TRAP";
            case TILE_ICE: return "ICE";
            case TILE_PORTAL_A: return "PORTAL_A";
            case TILE_PORTAL_B: return "PORTAL_B";
            case TILE_SPAWN_POINT: return "SPAWN_POINT";
            case TILE_INVALID: return "INVALID";
            default:
                int value = tile & 0xFF;
                if (value >= 94 && value <= 200) {
                    return "WALKABLE_" + value;
                }
                return "TILE_" + value;
        }
    }

    /**
     * Get a human-readable object name for debugging.
     *
     * @param object Object byte value
     * @return String description of the object
     */
    public static String getObjectName(byte object) {
        switch (object) {
            case OBJECT_CARROT: return "CARROT";
            case OBJECT_CARROT_SPECIAL: return "CARROT_SPECIAL";
            case OBJECT_BONUS_ITEM: return "BONUS_ITEM";
            case OBJECT_INVALID: return "INVALID";
            default:
                int value = object & 0xFF;
                return "OBJECT_" + value;
        }
    }

    @Override
    public String toString() {
        return "LevelData{" +
                "levelPackId=" + levelPackId +
                ", levelId=" + levelId +
                ", mapWidth=" + mapWidth +
                ", mapHeight=" + mapHeight +
                ", carrotsCollected=" + carrotsCollected +
                '}';
    }
}
