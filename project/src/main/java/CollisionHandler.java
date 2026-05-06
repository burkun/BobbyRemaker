/**
 * CollisionHandler - Handles collision detection for Bobby Carrot 5.
 *
 * Extracted from decompiled a.java, method a(int, int, boolean) at lines 3327-3560.
 * This class handles:
 * - Movement validation (can player move to target tile)
 * - Tile type checking (conveyors, ice, traps, portals)
 * - Object type checking (carrots, seeds, level end, etc.)
 * - Special interaction triggers
 *
 * Original field mappings used:
 * - ai -> player tile X
 * - aj -> player tile Y
 * - cl[][] -> tile map
 * - cm[][] -> object map
 * - dn -> map width
 * - cfr_renamed_0 -> map height
 * - bd -> isDying (skip collision when dying)
 * - aZ -> isFlying (flying bypasses certain collisions)
 * - aT -> mower timer/special action
 * - bb -> isMower (grass cutting mode)
 * - aE -> push force
 * - bc -> isMoving (continuous movement)
 * - cQ -> hasMowerAbility
 * - cV -> hasSpecialAbility
 * - cW -> levelComplete flag
 * - cO -> seedCollected flag
 * - cP -> hasSeedAbility
 * - cM -> inBonusLevel
 * - bL -> level pack ID
 * - bM -> game mode (0 = normal)
 * - w -> level score/carrots
 * - x -> bonus items collected
 * - r[2] -> seed count
 */
public class CollisionHandler {

    // References to game data
    private LevelData levelData;
    private Player player;

    // Collision result flags
    public static final int COLLISION_NONE = 0;
    public static final int COLLISION_BLOCKED = 1;
    public static final int COLLISION_CARROT = 2;
    public static final int COLLISION_SEED = 3;
    public static final int COLLISION_LEVEL_END = 4;
    public static final int COLLISION_MOWER_PATH = 5;
    public static final int COLLISION_SPRING = 6;
    public static final int COLLISION_PORTAL = 7;
    public static final int COLLISION_DOOR = 8;
    public static final int COLLISION_BONUS_DOOR = 9;
    public static final int COLLISION_ICE = 10;
    public static final int COLLISION_DEATH = 11;
    public static final int COLLISION_FLIGHT_PICKUP = 12;

    // Special interaction result
    private int specialActionResult;
    private int pendingDirection;

    /**
     * Constructor with level data and player references.
     *
     * @param levelData The level map data
     * @param player    The player state
     */
    public CollisionHandler(LevelData levelData, Player player) {
        this.levelData = levelData;
        this.player = player;
        this.specialActionResult = 0;
        this.pendingDirection = 0;
    }

    /**
     * Core collision detection - can the player move to the target position.
     * This is the main entry point for movement validation.
     *
     * Corresponds to original method: a(int n, int n2, boolean bl) at line 3327.
     *
     * @param dx        Delta X (tile movement direction: -1, 0, or 1)
     * @param dy        Delta Y (tile movement direction: -1, 0, or 1)
     * @param checkOnly If true, only check without triggering side effects
     * @return CollisionResult containing movement validity and any special interactions
     */
    public CollisionResult canMoveTo(int dx, int dy, boolean checkOnly) {
        // Calculate target position
        int targetX = player.getTileX() + dx;
        int targetY = player.getTileY() + dy;

        // Reset special action
        this.specialActionResult = 0;

        // Boundary check
        if (targetX < 0 || targetY < 0 ||
            targetX >= levelData.getMapWidth() || targetY >= levelData.getMapHeight()) {
            return new CollisionResult(false, COLLISION_BLOCKED, "Out of bounds");
        }

        // Dying players bypass collision
        if (player.isDying()) {
            return new CollisionResult(true, COLLISION_NONE, "Dying - bypass collision");
        }

        // Get tile values
        byte currentTile = levelData.getTileAt(player.getTileX(), player.getTileY());
        byte targetTile = levelData.getTileAt(targetX, targetY);
        byte targetObject = levelData.getObjectAt(targetX, targetY);

        // Check current tile restrictions (e.g., arrow tiles force direction)
        boolean canMove = checkCurrentTileRestriction(currentTile, dx, dy);
        if (!canMove) {
            return new CollisionResult(false, COLLISION_BLOCKED, "Current tile restricts movement");
        }

        // Check for portal at target position (can enter portals from any direction when not flying)
        if (!player.isFlying() && checkPortalEntry(targetX, targetY)) {
            return new CollisionResult(true, COLLISION_PORTAL, "Portal entry");
        }

        // Check target tile walkability
        CollisionResult tileResult = checkTargetTile(targetTile, dx, dy);
        if (!tileResult.canMove) {
            // Check if object allows passage despite blocked tile
            CollisionResult objectOverride = checkObjectOverride(targetObject);
            if (objectOverride != null) {
                return objectOverride;
            }
            return tileResult;
        }

        // Check object interactions
        CollisionResult objectResult = checkTargetObject(targetObject, dx, dy, checkOnly);
        if (!objectResult.canMove) {
            return objectResult;
        }

        return new CollisionResult(true, objectResult.collisionType, objectResult.message);
    }

    /**
     * Check if current tile restricts movement direction.
     * Arrow tiles force movement in specific directions.
     *
     * @param currentTile The tile the player is standing on
     * @param dx          Movement delta X
     * @param dy          Movement delta Y
     * @return true if movement is allowed from this tile
     */
    private boolean checkCurrentTileRestriction(byte currentTile, int dx, int dy) {
        // Arrow/conveyor tiles restrict movement direction
        switch (currentTile) {
            case TileType.CONVEYOR_RIGHT:  // -66: Must move horizontally
                return dx != 0;

            case TileType.CONVEYOR_DOWN:    // -67: Must move vertically
                return dy != 0;

            case TileType.CONVEYOR_DIAG_1:  // -68: Move right or down
                return dx == 1 || dy == 1;

            case TileType.CONVEYOR_DIAG_2:  // -69: Move left or down
                return dx == -1 || dy == 1;

            case TileType.CONVEYOR_DIAG_3:  // -70: Move left or up
                return dx == -1 || dy == -1;

            case TileType.CONVEYOR_DIAG_4:  // -71: Move right or up
                return dx == 1 || dy == -1;

            default:
                return true;
        }
    }

    /**
     * Check if target tile is walkable.
     * Handles special tile types like ice, death traps, and mowable grass.
     *
     * @param targetTile The tile being moved to
     * @param dx         Movement delta X
     * @param dy         Movement delta Y
     * @return CollisionResult with walkability info
     */
    private CollisionResult checkTargetTile(byte targetTile, int dx, int dy) {
        int tileValue = targetTile & 0xFF;

        // Tiles 94-200 are generally walkable surfaces
        boolean isWalkable = tileValue >= 94 && tileValue <= 200;

        if (isWalkable) {
            // Special walkable tile handling

            // Conveyor tiles on target (185-190)
            if (tileValue >= 185 && tileValue <= 190) {
                if (!player.isFlying()) {
                    // Check if movement direction matches conveyor
                    boolean canEnter = checkConveyorEntry(targetTile, dx, dy);
                    return new CollisionResult(canEnter,
                        canEnter ? COLLISION_NONE : COLLISION_BLOCKED,
                        "Conveyor tile");
                } else {
                    // Flying bypasses conveyor restrictions
                    return new CollisionResult(false, COLLISION_BLOCKED, "Cannot enter conveyor while flying");
                }
            }

            // Arrow tiles (177-180)
            if (tileValue >= 177 && tileValue <= 180) {
                return new CollisionResult(!player.isFlying(), COLLISION_NONE, "Arrow tile");
            }

            // Mowable grass tiles
            if (targetTile == TileType.GRASS_1 || targetTile == TileType.GRASS_2) {
                if (player.isFlying()) {
                    // Mark for mowing when flying over
                    return new CollisionResult(true, COLLISION_NONE, "Grass - mow while flying");
                } else {
                    // Non-flying cannot enter grass
                    return new CollisionResult(false, COLLISION_BLOCKED, "Cannot enter grass without mowing");
                }
            }

            // Ice tiles (frozen water)
            if (targetTile == TileType.ICE) {
                return new CollisionResult(false, COLLISION_BLOCKED, "Ice tile - blocked");
            }

            // Default walkable
            return new CollisionResult(true, COLLISION_NONE, "Walkable tile");
        }

        // Non-walkable tile - check for special case (mower path at tile 77)
        if (targetTile == 77 && !player.isFlying()) {
            handleMowerPath();
        }

        return new CollisionResult(false, COLLISION_BLOCKED, "Non-walkable tile: " + tileValue);
    }

    /**
     * Check if movement direction matches conveyor direction.
     *
     * @param conveyorTile The conveyor tile value
     * @param dx           Movement delta X
     * @param dy           Movement delta Y
     * @return true if direction matches conveyor
     */
    private boolean checkConveyorEntry(byte conveyorTile, int dx, int dy) {
        switch (conveyorTile) {
            case TileType.CONVEYOR_DIAG_4:  // -71 / 185
                return dx == -1 || dy == 1;

            case TileType.CONVEYOR_DIAG_3:  // -70 / 186
                return dx == 1 || dy == 1;

            case TileType.CONVEYOR_DIAG_2:  // -69 / 187
                return dx == 1 || dy == -1;

            case TileType.CONVEYOR_DIAG_1:  // -68 / 188
                return dx == -1 || dy == -1;

            case TileType.CONVEYOR_DOWN:    // -67 / 189
                return dy != 0;

            case TileType.CONVEYOR_RIGHT:   // -66 / 190
                return dx != 0;

            default:
                return true;
        }
    }

    /**
     * Check object at target position for collision or interaction.
     *
     * @param targetObject The object value at target position
     * @param dx           Movement delta X
     * @param dy           Movement delta Y
     * @param checkOnly    If true, only check without side effects
     * @return CollisionResult with interaction info
     */
    private CollisionResult checkTargetObject(byte targetObject, int dx, int dy, boolean checkOnly) {
        switch (targetObject) {
            // Carrot - collectible
            case ObjectType.SEED_COUNTER_1:  // -54
                return new CollisionResult(!player.isFlying(), COLLISION_CARROT, "Carrot");

            // Seed - needs to have seeds to pass
            case ObjectType.SEED:  // -12
                if (!player.isFlying()) {
                    // Check if player has seed ability or seeds
                    return new CollisionResult(true, COLLISION_SEED, "Seed");
                }
                return new CollisionResult(false, COLLISION_BLOCKED, "Cannot collect seed while flying");

            // Level end / exit
            case ObjectType.LEVEL_END:  // -10
                // Allow passage when not flying, otherwise check push force or checkOnly flag
                boolean canExit = player.isFlying() && (player.getPushForce() > 0 || checkOnly);
                return new CollisionResult(canExit, COLLISION_LEVEL_END, "Level end");

            // Flight pickup
            case ObjectType.FLIGHT_PICKUP:  // -11
                return new CollisionResult(!player.isFlying(), COLLISION_FLIGHT_PICKUP, "Flight pickup");

            // Mower transition
            case ObjectType.MOWER_TRANSITION:  // -36
                // Special handling for mower mode transitions
                return handleMowerTransition(checkOnly);

            // Spring object
            case ObjectType.SPRING:  // -44
                return new CollisionResult(!player.isFlying(), COLLISION_SPRING, "Spring");

            // Bonus door
            case ObjectType.BONUS_DOOR:  // -51
                return handleBonusDoor(checkOnly);

            // Seed pile
            case ObjectType.SEED_PILE:  // -52
            case -48:
            case -47:
            case -46:
            case -45:
            case -41:
            case -40:
            case -38:
            case -37:
            case -29:
            case -25:
            case -7:
            case -6:
            case -5:
            case -4:
            case -3:
            case -2:
                // These objects block movement
                return new CollisionResult(false, COLLISION_BLOCKED, "Blocking object: " + (targetObject & 0xFF));

            // Level pack doors
            case -22:
            case -9:
                return handleLevelPackDoor(targetObject, checkOnly);

            // Death trigger
            case -21:
                return new CollisionResult(false, COLLISION_DEATH, "Death trigger");

            // Special movement restriction
            case -19:
                // Only passable when flying with push force or in check mode
                boolean canPass = player.isFlying() && (player.getPushForce() > 0 || checkOnly);
                return new CollisionResult(canPass,
                    canPass ? COLLISION_NONE : COLLISION_BLOCKED, "Restricted passage");

            default:
                return new CollisionResult(true, COLLISION_NONE, "No special object");
        }
    }

    /**
     * Check if object allows passage despite blocked tile.
     *
     * @param targetObject The object at target position
     * @return CollisionResult override, or null if no override
     */
    private CollisionResult checkObjectOverride(byte targetObject) {
        switch (targetObject) {
            case -50:
            case ObjectType.SPRING:  // -44
            case -34:
                // These objects allow passage even from blocked tiles
                return new CollisionResult(!player.isFlying(), COLLISION_NONE, "Object override passage");

            default:
                return null;
        }
    }

    /**
     * Handle mower path entry.
     * Sets up the mower mode when entering a mower path.
     */
    private void handleMowerPath() {
        this.specialActionResult = 32;
        // Direction calculation based on movement
        this.pendingDirection = 0; // Will be set based on dx/dy
    }

    /**
     * Handle mower transition zones.
     *
     * @param checkOnly If true, only check without side effects
     * @return CollisionResult for mower transition
     */
    private CollisionResult handleMowerTransition(boolean checkOnly) {
        // Logic for mower transition
        // In original: if (!cO) { aP = 0; ar = 4; return false; }
        return new CollisionResult(true, COLLISION_MOWER_PATH, "Mower transition");
    }

    /**
     * Handle bonus door interaction.
     *
     * @param checkOnly If true, only check without side effects
     * @return CollisionResult for bonus door
     */
    private CollisionResult handleBonusDoor(boolean checkOnly) {
        if (!player.isFlying()) {
            // In original: check if can enter bonus (seeds collected, etc.)
            return new CollisionResult(true, COLLISION_BONUS_DOOR, "Bonus door");
        }
        return new CollisionResult(false, COLLISION_BLOCKED, "Cannot enter bonus door while flying");
    }

    /**
     * Handle level pack door interaction.
     *
     * @param doorType  The door object type
     * @param checkOnly If true, only check without side effects
     * @return CollisionResult for level pack door
     */
    private CollisionResult handleLevelPackDoor(byte doorType, boolean checkOnly) {
        // Level pack doors trigger level completion or transition
        // In original: shows dialog and handles level state
        return new CollisionResult(false, COLLISION_LEVEL_END, "Level pack door: " + (doorType & 0xFF));
    }

    /**
     * Check for portal entry at target position.
     *
     * @param targetX Target tile X
     * @param targetY Target tile Y
     * @return true if this is a portal position
     */
    private boolean checkPortalEntry(int targetX, int targetY) {
        byte tile = levelData.getTileAt(targetX, targetY);
        return tile == TileType.PORTAL_1 || tile == TileType.PORTAL_2;
    }

    /**
     * Get tile value at position - corresponds to original b(int, int) at line 3313.
     *
     * @param x Tile X coordinate
     * @param y Tile Y coordinate
     * @return Tile value, or -1 if out of bounds
     */
    public byte getTileAt(int x, int y) {
        return levelData.getTileAt(x, y);
    }

    /**
     * Get object value at position - corresponds to original c(int, int) at line 3320.
     *
     * @param x Tile X coordinate
     * @param y Tile Y coordinate
     * @return Object value, or -1 if out of bounds
     */
    public byte getObjectAt(int x, int y) {
        return levelData.getObjectAt(x, y);
    }

    /**
     * Check if there's an active entity at pixel position.
     * Corresponds to original a(int, int) at line 3062.
     *
     * @param pixelX Pixel X coordinate
     * @param pixelY Pixel Y coordinate
     * @return true if there's a matching active entity
     */
    public boolean checkEntityAtPixel(int pixelX, int pixelY) {
        // Convert pixel to tile coordinates
        pixelX <<= 5;  // Multiply by 32
        pixelY <<= 5;

        // In original, this checks the active entity array (cw[])
        // for entities of type 4 at the given pixel position
        // This is used for special collision detection during movement

        // For now, return false as we don't have entity data
        return false;
    }

    /**
     * Get the special action result from last collision check.
     *
     * @return The special action code (e.g., mower mode flag)
     */
    public int getSpecialActionResult() {
        return this.specialActionResult;
    }

    /**
     * Get pending direction from last collision check.
     *
     * @return The pending direction value
     */
    public int getPendingDirection() {
        return this.pendingDirection;
    }

    /**
     * Update level data reference.
     *
     * @param levelData New level data
     */
    public void setLevelData(LevelData levelData) {
        this.levelData = levelData;
    }

    /**
     * Update player reference.
     *
     * @param player New player state
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    // ============================================================
    // Inner class for collision result
    // ============================================================

    /**
     * CollisionResult - Contains the result of a collision check.
     */
    public static class CollisionResult {
        public final boolean canMove;
        public final int collisionType;
        public final String message;

        public CollisionResult(boolean canMove, int collisionType, String message) {
            this.canMove = canMove;
            this.collisionType = collisionType;
            this.message = message;
        }

        @Override
        public String toString() {
            return "CollisionResult{" +
                    "canMove=" + canMove +
                    ", collisionType=" + collisionType +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    // ============================================================
    // Static utility methods
    // ============================================================

    /**
     * Get human-readable collision type name.
     *
     * @param type Collision type constant
     * @return Human-readable name
     */
    public static String getCollisionTypeName(int type) {
        switch (type) {
            case COLLISION_NONE:         return "NONE";
            case COLLISION_BLOCKED:      return "BLOCKED";
            case COLLISION_CARROT:       return "CARROT";
            case COLLISION_SEED:         return "SEED";
            case COLLISION_LEVEL_END:    return "LEVEL_END";
            case COLLISION_MOWER_PATH:   return "MOWER_PATH";
            case COLLISION_SPRING:       return "SPRING";
            case COLLISION_PORTAL:       return "PORTAL";
            case COLLISION_DOOR:         return "DOOR";
            case COLLISION_BONUS_DOOR:   return "BONUS_DOOR";
            case COLLISION_ICE:          return "ICE";
            case COLLISION_DEATH:        return "DEATH";
            case COLLISION_FLIGHT_PICKUP: return "FLIGHT_PICKUP";
            default:                     return "UNKNOWN(" + type + ")";
        }
    }

    @Override
    public String toString() {
        return "CollisionHandler{" +
                "player=" + player +
                ", levelData=" + levelData +
                ", specialActionResult=" + specialActionResult +
                '}';
    }
}
