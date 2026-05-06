/**
 * LevelInteraction - Handles player interactions with level tiles and objects.
 *
 * This class encapsulates the core game logic for tile/object interactions
 * extracted from decompiled a.java (Bobby Carrot 5).
 *
 * Original method mappings:
 * - J() -> handleTileInteraction() - Main tile/object interaction handler
 * - K() -> handleDeath() - Death state handling
 * - L() -> resetAllTiles() - Reset all tiles to base state
 * - c(int) -> rotateConveyers() - Rotate conveyor/arrow directions
 * - a(byte) -> swapDiagonalTiles() - Swap diagonal tile pairs
 * - a(byte, byte) -> replaceTileGlobal() - Replace all instances of one tile with another
 *
 * Field mappings (from a.java):
 * - cl[][] -> tileMap (terrain/surface tiles)
 * - cm[][] -> objectMap (collectible items/objects)
 * - ai -> playerTileX (current tile X)
 * - aj -> playerTileY (current tile Y)
 * - aw/ax -> savedPortalX/savedPortalY (portal entry position)
 * - ay/az -> savedArrowX/savedArrowY (arrow entry position)
 * - aL/aM -> savedConveyorX/savedConveyorY (conveyor entry position)
 * - aA/aB -> savedSpringX/savedSpringY (spring entry position)
 * - aC/aD -> savedSpringActiveX/savedSpringActiveY (spring active position)
 * - aI/aJ -> savedSeedPileX/savedSeedPileY (seed pile position)
 * - ct -> seedCount (seeds remaining)
 * - cu -> seedPiles (seed piles placed)
 * - bO -> carrotsCollected
 * - cJ -> specialLevel (e.g., boss level indicator)
 * - dj -> flightState (0=none, 1=active)
 * - bd -> isDying flag
 * - ba -> isBalloon flag
 * - aZ -> isFlying flag
 */
public class LevelInteraction {

    // ============================================================
    // Tile Constants (from TileType.java and analysis)
    // ============================================================

    // Terrain tiles
    public static final byte TILE_DEATH_TRIGGER = -81;      // 175 - triggers death
    public static final byte TILE_ICE = -108;               // 148 - slippery surface
    public static final byte TILE_DEATH_TRAP = -106;        // 150 - instant death zone
    public static final byte TILE_GRASS_1 = -57;            // 199 - mowable grass variant 1
    public static final byte TILE_GRASS_2 = -56;            // 200 - mowable grass variant 2

    // Conveyor belts (push player in direction)
    public static final byte TILE_CONVEYOR_RIGHT = -66;     // 190
    public static final byte TILE_CONVEYOR_DOWN = -67;      // 189
    public static final byte TILE_CONVEYOR_DIAG_1 = -68;    // 188
    public static final byte TILE_CONVEYOR_DIAG_2 = -69;    // 187
    public static final byte TILE_CONVEYOR_DIAG_3 = -70;    // 186
    public static final byte TILE_CONVEYOR_DIAG_4 = -71;    // 185

    // Directional arrows (force movement)
    public static final byte TILE_ARROW_RIGHT = -72;       // 184
    public static final byte TILE_ARROW_LEFT = -73;        // 183
    public static final byte TILE_ARROW_DOWN = -74;        // 182
    public static final byte TILE_ARROW_UP = -75;         // 181

    // Portal tiles (teleport between pairs)
    public static final byte TILE_PORTAL_1 = -90;         // 166
    public static final byte TILE_PORTAL_2 = -94;          // 162

    // Door tiles
    public static final byte TILE_DOOR_1 = -92;            // 164
    public static final byte TILE_DOOR_2 = -93;            // 163

    // Switch tiles (toggle state)
    public static final byte TILE_SWITCH_1_OFF = -89;     // 167
    public static final byte TILE_SWITCH_1_ON = -88;      // 168
    public static final byte TILE_SWITCH_2_OFF = -87;     // 169
    public static final byte TILE_SWITCH_2_ON = -86;      // 170
    public static final byte TILE_SWITCH_3_OFF = -85;     // 171
    public static final byte TILE_SWITCH_3_ON = -84;      // 172
    public static final byte TILE_SWITCH_4_OFF = -83;     // 173
    public static final byte TILE_SWITCH_4_ON = -82;      // 174

    // Special tiles
    public static final byte TILE_LEVEL_END = -92;        // 164 - exit condition
    public static final byte TILE_MOWER_PATH = -96;       // 160 - mower mode activation
    public static final byte TILE_MOWER_START = -97;      // 159 - mower mode start
    public static final byte TILE_PORTAL_ENTRY = -80;     // 176 - portal entry marker
    public static final byte TILE_ARROW_TRIGGER = -95;    // 161 - arrow trigger

    // Tile range constants
    public static final int TILE_CONVEYOR_MIN = 185;      // -71 & 0xFF
    public static final int TILE_CONVEYOR_MAX = 190;     // -66 & 0xFF
    public static final int TILE_ARROW_MIN = 177;         // -79 & 0xFF
    public static final int TILE_ARROW_MAX = 180;        // -76 & 0xFF
    public static final int TILE_PORTAL_MIN = 185;        // Same as conveyor range check
    public static final int TILE_PORTAL_MAX = 190;
    public static final int TILE_CONVEYOR_PUSH_MIN = 185;
    public static final int TILE_CONVEYOR_PUSH_MAX = 190;
    public static final int TILE_BONUS_MIN = 151;        // -105 & 0xFF
    public static final int TILE_BONUS_MAX = 157;        // -99 & 0xFF

    // ============================================================
    // Object Constants (from ObjectType.java and analysis)
    // ============================================================

    public static final byte OBJECT_INVALID = -1;         // Invalid/empty object slot

    // Collectibles
    public static final byte OBJECT_CARROT = -8;          // 248 - standard carrot
    public static final byte OBJECT_CARROT_SPECIAL = -54; // 202 - special carrot
    public static final byte OBJECT_BONUS_ITEM = -33;     // 223 - bonus collectible

    // Interactive objects
    public static final byte OBJECT_LEVEL_END = -10;     // 246 - level exit
    public static final byte OBJECT_FLIGHT_PICKUP = -11; // 245 - flight powerup
    public static final byte OBJECT_SEED = -12;          // 244 - single seed
    public static final byte OBJECT_SEED_PILE = -52;     // 204 - seed pile
    public static final byte OBJECT_SEED_COUNTER_1 = -54;// 202 - seed counter
    public static final byte OBJECT_SEED_COUNTER_2 = -55;// 201 - seed counter variant

    // Special objects
    public static final byte OBJECT_SPRING = -44;        // 212 - spring/jump pad
    public static final byte OBJECT_MOWER_PATH = -19;     // 237 - mower path marker
    public static final byte OBJECT_BONUS_DOOR = -51;    // 205 - bonus level door
    public static final byte OBJECT_MOWER_TRANSITION = -36; // 220 - mower mode transition

    // Object states (after collection)
    public static final byte OBJECT_SEED_PLACED = -17;   // 239 - placed seed
    public static final byte OBJECT_SEED_PILE_TAKEN = -52; // 204 - seed pile taken

    // ============================================================
    // State Constants
    // ============================================================

    // Player state constants
    public static final int STATE_NORMAL = 0;
    public static final int STATE_DYING = 5;
    public static final int STATE_LEVEL_COMPLETE = 6;

    // Flight states
    public static final int FLIGHT_NONE = 0;
    public static final int FLIGHT_ACTIVE = 1;
    public static final int FLIGHT_GLIDER = 2;

    // Spring animation
    public static final int SPRING_ANIM_DURATION = 6;

    // ============================================================
    // Instance Fields
    // ============================================================

    // References to game data
    private LevelData levelData;
    private Player player;

    // Saved positions for tile interactions
    private int savedPortalX = -1;
    private int savedPortalY = -1;
    private int savedArrowX = -1;
    private int savedArrowY = -1;
    private int savedConveyorX = -1;
    private int savedConveyorY = -1;
    private int savedSpringX = -1;
    private int savedSpringY = -1;
    private int savedSpringActiveX = -1;
    private int savedSpringActiveY = -1;
    private int savedSeedPileX = -1;
    private int savedSeedPileY = -1;

    // State tracking
    private boolean isDying = false;
    private boolean isBalloon = false;
    private boolean isFlying = false;
    private boolean isOnIce = false;
    private int flightState = FLIGHT_NONE;
    private int springAnimTimer = 0;
    private int seedCount = 0;
    private int seedPilesPlaced = 0;
    private int carrotsCollected = 0;

    // Special level tracking
    private int specialLevelId = -1;
    private boolean hasFlightPowerup = false;

    // Level completion callback
    private LevelCompleteListener levelCompleteListener;

    // ============================================================
    // Constructors
    // ============================================================

    /**
     * Default constructor.
     */
    public LevelInteraction() {
        resetSavedPositions();
    }

    /**
     * Constructor with level data reference.
     *
     * @param levelData The level data to interact with
     */
    public LevelInteraction(LevelData levelData) {
        this();
        this.levelData = levelData;
    }

    /**
     * Constructor with level data and player reference.
     *
     * @param levelData The level data to interact with
     * @param player    The player entity
     */
    public LevelInteraction(LevelData levelData, Player player) {
        this(levelData);
        this.player = player;
    }

    // ============================================================
    // Core Interaction Methods
    // ============================================================

    /**
     * Handle tile and object interactions at the player's current position.
     * This is the main interaction method called when player enters a tile.
     *
     * Original: J() method in a.java (lines 2656-2871)
     *
     * @return InteractionResult indicating what happened
     */
    public InteractionResult handleTileInteraction() {
        if (levelData == null) {
            return InteractionResult.ERROR;
        }

        int playerX = player != null ? player.getTileX() : 0;
        int playerY = player != null ? player.getTileY() : 0;

        byte tile = levelData.getTileAt(playerX, playerY);
        byte object = levelData.getObjectAt(playerX, playerY);

        // Handle saved position states first (portals, arrows, conveyors, springs, seeds)
        handleSavedPositionStates();

        // Handle dying state
        if (isDying) {
            if (object == OBJECT_FLIGHT_PICKUP) {
                // Can pick up flight while dying (special case)
                return handleFlightPickup(playerX, playerY);
            }
            return InteractionResult.DYING;
        }

        // Handle object interactions (items on the tile)
        InteractionResult objectResult = handleObjectInteraction(playerX, playerY, object);
        if (objectResult != InteractionResult.NONE) {
            return objectResult;
        }

        // Handle tile interactions (terrain effects)
        return handleTileTerrainEffect(playerX, playerY, tile);
    }

    /**
     * Handle saved position states from previous tile interactions.
     * Manages portals, arrows, conveyors, springs, and seed piles.
     */
    private void handleSavedPositionStates() {
        if (levelData == null) return;

        // Portal state - restore portal tile and clear saved position
        if (savedPortalX != -1) {
            levelData.setTileAt(savedPortalX, savedPortalY, TILE_PORTAL_ENTRY);
            // Trigger visual update (f() method in original)
            savedPortalX = -1;
            savedPortalY = -1;
        }

        // Arrow state - restore arrow tile
        if (savedArrowX != -1) {
            byte currentTile = levelData.getTileAt(savedArrowX, savedArrowY);
            levelData.setTileAt(savedArrowX, savedArrowY, incrementTileValue(currentTile));
            savedArrowX = -1;
            savedArrowY = -1;
        }

        // Conveyor state - restore conveyor tile
        if (savedConveyorX != -1) {
            byte currentTile = levelData.getTileAt(savedConveyorX, savedConveyorY);
            levelData.setTileAt(savedConveyorX, savedConveyorY, rotateConveyorOnce(currentTile));
            savedConveyorX = -1;
            savedConveyorY = -1;
        }

        // Spring state - handle spring animation and clearing
        if (savedSpringX != -1) {
            if (savedSpringActiveX != -1) {
                levelData.setObjectAt(savedSpringActiveX, savedSpringActiveY, OBJECT_INVALID);
            }
            levelData.setObjectAt(savedSpringX, savedSpringY, OBJECT_SPRING);
            springAnimTimer = SPRING_ANIM_DURATION;
            savedSpringX = -1;
            savedSpringY = -1;
        }

        // Seed pile state - consume a seed
        if (savedSeedPileX != -1) {
            seedCount--;
            levelData.setObjectAt(savedSeedPileX, savedSeedPileY, OBJECT_SEED_PILE_TAKEN);
            savedSeedPileX = -1;
            savedSeedPileY = -1;
        }
    }

    /**
     * Handle object (item) interactions.
     *
     * @param playerX Player tile X
     * @param playerY Player tile Y
     * @param object  Object at the position
     * @return InteractionResult indicating what happened
     */
    private InteractionResult handleObjectInteraction(int playerX, int playerY, byte object) {
        // Skip if flying (can't collect while flying)
        if (isFlying) {
            return InteractionResult.NONE;
        }

        switch (object) {
            case OBJECT_BONUS_DOOR:
                // Bonus level door - trigger special level transition
                return handleBonusDoor(playerX, playerY);

            case OBJECT_CARROT:
                // Standard carrot - increment counter
                return handleCarrotCollect(playerX, playerY, false);

            case OBJECT_FLIGHT_PICKUP:
                // Flight pickup - enable flying
                return handleFlightPickup(playerX, playerY);

            case OBJECT_SEED:
                // Single seed - add to count
                return handleSeedCollect(playerX, playerY);

            case OBJECT_SEED_PILE:
                // Seed pile - mark for pickup
                savedSeedPileX = playerX;
                savedSeedPileY = playerY;
                return InteractionResult.SEED_PILE;

            case OBJECT_SEED_COUNTER_1:
                // Seed counter - place seed if available
                return handleSeedPlacement(playerX, playerY);

            case OBJECT_BONUS_ITEM:
                // Bonus item - increment bonus counter
                return handleBonusCollect(playerX, playerY);

            case OBJECT_SEED_COUNTER_2:
                // Special seed counter - decrease seeds
                seedCount--;
                levelData.setObjectAt(playerX, playerY, OBJECT_SEED_COUNTER_1);
                return InteractionResult.SEED_USED;

            case OBJECT_LEVEL_END:
                // Level end marker (water/similar)
                isBalloon = true;
                return InteractionResult.LEVEL_PROGRESS;

            case OBJECT_MOWER_TRANSITION:
                // Mower transition - handle mowing
                return InteractionResult.MOWER_TRANSITION;

            case OBJECT_MOWER_PATH:
                // Mower path - indicates mowing area
                isBalloon = true;
                return InteractionResult.MOWER_PATH;

            default:
                return InteractionResult.NONE;
        }
    }

    /**
     * Handle terrain tile effects.
     *
     * @param playerX Player tile X
     * @param playerY Player tile Y
     * @param tile    Tile at the position
     * @return InteractionResult indicating what happened
     */
    private InteractionResult handleTileTerrainEffect(int playerX, int playerY, byte tile) {
        int tileValue = tile & 0xFF;

        // Level exit condition
        if (tile == TILE_LEVEL_END) {
            handleLevelComplete();
            return InteractionResult.LEVEL_COMPLETE;
        }

        // Door tiles - portal to next area
        if (tile == TILE_DOOR_1) {
            handlePortalTeleport(0);
            return InteractionResult.PORTAL_USED;
        }
        if (tile == TILE_DOOR_2) {
            handlePortalTeleport(1);
            return InteractionResult.PORTAL_USED;
        }

        // Ice tile - slippery surface
        if (tile == TILE_ICE) {
            isOnIce = true;
            return InteractionResult.ICE_SLIDE;
        }

        // Conveyor belts - push player in direction
        if (tileValue >= TILE_CONVEYOR_MIN && tileValue <= TILE_CONVEYOR_MAX) {
            int conveyorIndex = tileValue - TILE_CONVEYOR_MIN;
            handleConveyorPush(conveyorIndex);
            return InteractionResult.CONVEYOR_PUSH;
        }

        // Directional arrows - save position for later
        if (tileValue >= TILE_ARROW_MIN && tileValue <= TILE_ARROW_MAX) {
            if (!isFlying) {
                savedArrowX = playerX;
                savedArrowY = playerY;
            }
            return InteractionResult.ARROW_TRIGGER;
        }

        // Conveyor push range (alternative check)
        if (tileValue >= TILE_CONVEYOR_PUSH_MIN && tileValue <= TILE_CONVEYOR_PUSH_MAX) {
            if (!isFlying) {
                savedConveyorX = playerX;
                savedConveyorY = playerY;
            }
            return InteractionResult.CONVEYOR_PUSH;
        }

        // Portal tiles - save for teleport
        if (tile == TILE_PORTAL_1) {
            handlePortalTeleport(0);
            return InteractionResult.PORTAL_USED;
        }
        if (tile == TILE_PORTAL_2) {
            handlePortalTeleport(1);
            return InteractionResult.PORTAL_USED;
        }

        // Switch tiles - toggle state
        InteractionResult switchResult = handleSwitchTile(tile);
        if (switchResult != InteractionResult.NONE) {
            return switchResult;
        }

        // Death trigger
        if (tile == TILE_DEATH_TRIGGER) {
            if (!isFlying && seedCount == 0) {
                handleDeath();
                return InteractionResult.DEATH;
            }
            handleLevelComplete();
            return InteractionResult.LEVEL_COMPLETE;
        }

        // Death trap - instant death
        if (tile == TILE_DEATH_TRAP) {
            if (!isFlying) {
                handleDeath();
                return InteractionResult.DEATH;
            }
        }

        // Portal entry marker
        if (tile == TILE_PORTAL_ENTRY) {
            savedPortalX = playerX;
            savedPortalY = playerY;
            return InteractionResult.PORTAL_ENTRY;
        }

        // Mower path activation
        if (tile == TILE_MOWER_START) {
            if (!isFlying) {
                // Trigger mower mode
                return InteractionResult.MOWER_START;
            }
        }

        // Mower path
        if (tile == TILE_MOWER_PATH) {
            if (isFlying) {
                flightState = FLIGHT_GLIDER;
            }
            return InteractionResult.MOWER_PATH;
        }

        // Bonus tiles (151-157)
        if (tileValue >= TILE_BONUS_MIN && tileValue <= TILE_BONUS_MAX) {
            int bonusIndex = tileValue - TILE_BONUS_MIN;
            handleBonusTile(bonusIndex);
            return InteractionResult.BONUS;
        }

        return InteractionResult.NONE;
    }

    /**
     * Handle switch tile toggles.
     *
     * @param tile Switch tile value
     * @return InteractionResult
     */
    private InteractionResult handleSwitchTile(byte tile) {
        switch (tile) {
            case TILE_SWITCH_1_OFF:
                toggleSwitchState(0, true);
                return InteractionResult.SWITCH_TOGGLED;
            case TILE_SWITCH_1_ON:
                toggleSwitchState(0, false);
                return InteractionResult.SWITCH_TOGGLED;
            case TILE_SWITCH_2_OFF:
                toggleSwitchState(1, true);
                return InteractionResult.SWITCH_TOGGLED;
            case TILE_SWITCH_2_ON:
                toggleSwitchState(1, false);
                return InteractionResult.SWITCH_TOGGLED;
            case TILE_SWITCH_3_OFF:
                toggleSwitchState(2, true);
                return InteractionResult.SWITCH_TOGGLED;
            case TILE_SWITCH_3_ON:
                toggleSwitchState(2, false);
                return InteractionResult.SWITCH_TOGGLED;
            case TILE_SWITCH_4_OFF:
                toggleSwitchState(3, true);
                return InteractionResult.SWITCH_TOGGLED;
            case TILE_SWITCH_4_ON:
                toggleSwitchState(3, false);
                return InteractionResult.SWITCH_TOGGLED;
            default:
                return InteractionResult.NONE;
        }
    }

    // ============================================================
    // Specific Interaction Handlers
    // ============================================================

    /**
     * Handle carrot collection.
     *
     * @param playerX    Player X position
     * @param playerY    Player Y position
     * @param isSpecial  Whether this is a special carrot
     * @return InteractionResult
     */
    private InteractionResult handleCarrotCollect(int playerX, int playerY, boolean isSpecial) {
        carrotsCollected++;
        levelData.setObjectAt(playerX, playerY, OBJECT_INVALID);
        levelData.incrementCarrots();
        return InteractionResult.CARROT_COLLECTED;
    }

    /**
     * Handle flight pickup.
     *
     * @param playerX Player X position
     * @param playerY Player Y position
     * @return InteractionResult
     */
    private InteractionResult handleFlightPickup(int playerX, int playerY) {
        hasFlightPowerup = true;
        flightState = FLIGHT_ACTIVE;
        levelData.setObjectAt(playerX, playerY, OBJECT_INVALID);
        return InteractionResult.FLIGHT_PICKUP;
    }

    /**
     * Handle seed collection.
     *
     * @param playerX Player X position
     * @param playerY Player Y position
     * @return InteractionResult
     */
    private InteractionResult handleSeedCollect(int playerX, int playerY) {
        seedCount++;
        levelData.setObjectAt(playerX, playerY, OBJECT_INVALID);
        return InteractionResult.SEED_COLLECTED;
    }

    /**
     * Handle seed placement on tile.
     *
     * @param playerX Player X position
     * @param playerY Player Y position
     * @return InteractionResult
     */
    private InteractionResult handleSeedPlacement(int playerX, int playerY) {
        if (seedCount > 0) {
            seedCount--;
            seedPilesPlaced++;
            // In original game, this would add to arrays for animation
            levelData.setObjectAt(playerX, playerY, OBJECT_SEED_PLACED);
            return InteractionResult.SEED_PLACED;
        } else {
            // No seeds - play error feedback
            return InteractionResult.NO_SEEDS;
        }
    }

    /**
     * Handle bonus door interaction.
     *
     * @param playerX Player X position
     * @param playerY Player Y position
     * @return InteractionResult
     */
    private InteractionResult handleBonusDoor(int playerX, int playerY) {
        // Special handling for bonus level entry
        levelData.setObjectAt(playerX, playerY, OBJECT_INVALID);
        return InteractionResult.BONUS_DOOR;
    }

    /**
     * Handle bonus item collection.
     *
     * @param playerX Player X position
     * @param playerY Player Y position
     * @return InteractionResult
     */
    private InteractionResult handleBonusCollect(int playerX, int playerY) {
        levelData.setObjectAt(playerX, playerY, OBJECT_INVALID);
        return InteractionResult.BONUS_ITEM;
    }

    /**
     * Handle conveyor belt push effect.
     *
     * @param conveyorIndex Conveyor direction index (0-5)
     */
    private void handleConveyorPush(int conveyorIndex) {
        // Direction mapping based on conveyor type
        // 0 = right, 1 = down, 2-5 = diagonals
        if (player != null) {
            player.setPushForce(conveyorIndex + 1);
        }
    }

    /**
     * Handle portal teleport.
     *
     * @param portalId Portal pair ID
     */
    private void handlePortalTeleport(int portalId) {
        // Find the matching portal destination
        // In original game, this uses cB/cC (portal 1) or cD/cE (portal 2)
        // This would teleport the player to the matching portal exit
        if (player != null) {
            // Set animation state for teleport
            player.setSpecialState(0);
        }
    }

    /**
     * Toggle switch state.
     *
     * @param switchId   Switch ID (0-3)
     * @param turnOn     Whether to turn the switch on
     */
    private void toggleSwitchState(int switchId, boolean turnOn) {
        if (levelData == null) return;

        byte[][] tileMap = levelData.getTileMap();
        if (tileMap == null) return;

        // Toggle all switches of this type in the level
        for (int y = 0; y < tileMap.length; y++) {
            for (int x = 0; x < tileMap[y].length; x++) {
                replaceSwitchTile(tileMap, x, y, switchId, turnOn);
            }
        }
    }

    /**
     * Replace switch tile at position.
     */
    private void replaceSwitchTile(byte[][] tileMap, int x, int y, int switchId, boolean turnOn) {
        byte tile = tileMap[y][x];
        byte offTile = getSwitchOffTile(switchId);
        byte onTile = getSwitchOnTile(switchId);

        if (turnOn) {
            if (tile == offTile) {
                tileMap[y][x] = onTile;
            }
        } else {
            if (tile == onTile) {
                tileMap[y][x] = offTile;
            }
        }
    }

    /**
     * Get switch OFF tile value.
     */
    private byte getSwitchOffTile(int switchId) {
        switch (switchId) {
            case 0: return TILE_SWITCH_1_OFF;
            case 1: return TILE_SWITCH_2_OFF;
            case 2: return TILE_SWITCH_3_OFF;
            case 3: return TILE_SWITCH_4_OFF;
            default: return 0;
        }
    }

    /**
     * Get switch ON tile value.
     */
    private byte getSwitchOnTile(int switchId) {
        switch (switchId) {
            case 0: return TILE_SWITCH_1_ON;
            case 1: return TILE_SWITCH_2_ON;
            case 2: return TILE_SWITCH_3_ON;
            case 3: return TILE_SWITCH_4_ON;
            default: return 0;
        }
    }

    /**
     * Handle bonus tile interaction.
     *
     * @param bonusIndex Bonus tile index
     */
    private void handleBonusTile(int bonusIndex) {
        // Original: checks if player has enough carrots for bonus
        // int n = (by & 0xFF) - 151;
        // boolean bl = this.w >= this.bl[n];
        // Shows bonus selection dialog
    }

    // ============================================================
    // Death Handling
    // ============================================================

    /**
     * Handle player death.
     * Original: K() method in a.java (lines 2873-2889)
     *
     * This method:
     * - Stops movement
     * - Sets death state
     * - Resets certain flags
     * - Plays death sound
     */
    public void handleDeath() {
        // Stop movement
        if (player != null) {
            player.setMoving(false);
            player.setDying(true);
            player.setMoveProgress(0);
            player.setDirection(Player.DIR_DEAD);
            player.setAnimFrame(0);
        }

        // Reset state flags
        isDying = true;
        isBalloon = false;
        isOnIce = false;

        // Reset drag state if active
        if (player != null && player.isDragging()) {
            player.setDragging(false);
            // W() method would reset drag state
        }

        // Sound playback would be handled by audio system
        // Original: plays "/death.mid" or "/alarm.mid" based on level type
    }

    /**
     * Check if player should die based on current state.
     *
     * @return true if death should occur
     */
    public boolean shouldDie() {
        return isDying;
    }

    // ============================================================
    // Level Completion
    // ============================================================

    /**
     * Handle level completion.
     * Original: L() method in a.java (lines 2891-2898)
     *
     * This method resets all tiles to their base state.
     */
    public void handleLevelComplete() {
        if (levelData == null) return;

        byte[][] tileMap = levelData.getTileMap();
        if (tileMap == null) return;

        // Reset all tiles to base state
        for (int y = 0; y < tileMap.length; y++) {
            for (int x = 0; x < tileMap[y].length; x++) {
                tileMap[y][x] = getBaseTileState(tileMap[y][x]);
            }
        }

        // Trigger visual refresh
        // Original calls f(bz, bA) for repaint

        // Notify listener
        if (levelCompleteListener != null) {
            levelCompleteListener.onLevelComplete();
        }
    }

    /**
     * Check if level is complete.
     *
     * @return true if level completion conditions are met
     */
    public boolean checkLevelComplete() {
        // Level is complete when:
        // 1. All carrots collected
        // 2. Player reached exit tile
        // 3. Special conditions met (varies by level)

        if (levelData == null) return false;

        // Check carrot requirement
        int totalCarrots = levelData.countCarrots();
        if (carrotsCollected < totalCarrots) {
            return false;
        }

        // Check if at exit position
        if (player == null) return false;

        byte tile = levelData.getTileAt(player.getTileX(), player.getTileY());
        return tile == TILE_LEVEL_END || tile == TILE_DOOR_1 || tile == TILE_DOOR_2;
    }

    /**
     * Check if player can enter level exit.
     *
     * @return true if exit conditions are satisfied
     */
    public boolean canEnterExit() {
        // Check if all objectives completed
        if (levelData == null) return false;

        int totalCarrots = levelData.countCarrots();
        return carrotsCollected >= totalCarrots;
    }

    // ============================================================
    // Tile Utility Methods
    // ============================================================

    /**
     * Get the base state of a tile.
     * Used when resetting level state.
     *
     * Original: b(byte) method
     */
    private byte getBaseTileState(byte tile) {
        int value = tile & 0xFF;
        // Arrow tiles cycle: 184 <-> 183, 182 <-> 181, etc.
        // This converts active arrows back to inactive
        if (value >= 184 && value <= 187) {
            return (byte) (tile + 1);
        }
        if (value >= 183 && value <= 186) {
            return (byte) (tile - 1);
        }
        // Add other tile state conversions as needed
        return tile;
    }

    /**
     * Increment tile value (for arrow toggling).
     *
     * Original: b(byte) method (used in arrow handling)
     */
    private byte incrementTileValue(byte tile) {
        int value = tile & 0xFF;
        // Toggle between arrow states
        if (value >= 183 && value <= 186) {
            return (byte) (tile + 1);
        }
        if (value >= 184 && value <= 187) {
            return (byte) (tile - 1);
        }
        return tile;
    }

    /**
     * Rotate conveyor tile once.
     *
     * Original: c(byte) method
     */
    private byte rotateConveyorOnce(byte tile) {
        int value = tile & 0xFF;
        // Cycle through conveyor directions
        if (value >= 185 && value <= 190) {
            return (byte) ((value - 185 + 1) % 6 + 185);
        }
        return tile;
    }

    /**
     * Rotate all conveyers in the level.
     *
     * Original: c(int) method in a.java (lines 2900-2932)
     *
     * @param direction Rotation direction (0 or 1)
     */
    public void rotateAllConveyers(int direction) {
        if (levelData == null) return;

        byte[][] tileMap = levelData.getTileMap();
        if (tileMap == null) return;

        // Define tile sets for each rotation group
        byte[] tiles;
        switch (direction) {
            case 0:
                // Group 1: -73, -72, -75, -74 (arrows) + -95, -94 (portals)
                tiles = new byte[]{-73, -72, -75, -74, -95, -94};
                break;
            default:
                // Group 2: 90, 89, 88, 87 (conveyers) + -91, -90 (portals)
                tiles = new byte[]{90, 89, 88, 87, -91, -90};
                break;
        }

        for (int y = 0; y < tileMap.length; y++) {
            for (int x = 0; x < tileMap[y].length; x++) {
                tileMap[y][x] = swapTileInGroup(tileMap[y][x], tiles);
            }
        }
    }

    /**
     * Swap tile within rotation group.
     */
    private byte swapTileInGroup(byte tile, byte[] group) {
        for (int i = 0; i < group.length; i++) {
            if (tile == group[i]) {
                return group[(i + 1) % group.length];
            }
        }
        return tile;
    }

    /**
     * Swap diagonal tile pairs.
     *
     * Original: a(byte) method in a.java (lines 2934-2980)
     *
     * @param tileType Diagonal tile type indicator
     */
    public void swapDiagonalTiles(byte tileType) {
        if (levelData == null) return;

        byte[][] tileMap = levelData.getTileMap();
        if (tileMap == null) return;

        byte pair1A, pair1B, pair2A, pair2B;

        switch (tileType) {
            case -65:
            case -64:
                // First diagonal pair
                pair1A = -65;
                pair1B = -64;
                pair2A = -61;
                pair2B = -60;
                break;
            case -63:
            case -62:
                // Second diagonal pair
                pair1A = -63;
                pair1B = -62;
                pair2A = -59;
                pair2B = -58;
                break;
            default:
                return;
        }

        for (int y = 0; y < tileMap.length; y++) {
            for (int x = 0; x < tileMap[y].length; x++) {
                byte tile = tileMap[y][x];
                if (tile == pair1A) {
                    tileMap[y][x] = pair1B;
                } else if (tile == pair1B) {
                    tileMap[y][x] = pair1A;
                } else if (tile == pair2A) {
                    tileMap[y][x] = pair2B;
                } else if (tile == pair2B) {
                    tileMap[y][x] = pair2A;
                }
            }
        }
    }

    /**
     * Replace all instances of one tile with another.
     *
     * Original: a(byte, byte) method in a.java (lines 2982-2990)
     *
     * @param fromTile Tile to replace
     * @param toTile   Replacement tile
     */
    public void replaceTileGlobal(byte fromTile, byte toTile) {
        if (levelData == null) return;

        byte[][] tileMap = levelData.getTileMap();
        if (tileMap == null) return;

        for (int y = 0; y < tileMap.length; y++) {
            for (int x = 0; x < tileMap[y].length; x++) {
                if (tileMap[y][x] == fromTile) {
                    tileMap[y][x] = toTile;
                }
            }
        }
    }

    // ============================================================
    // Helper Methods
    // ============================================================

    /**
     * Reset all saved position states.
     */
    private void resetSavedPositions() {
        savedPortalX = -1;
        savedPortalY = -1;
        savedArrowX = -1;
        savedArrowY = -1;
        savedConveyorX = -1;
        savedConveyorY = -1;
        savedSpringX = -1;
        savedSpringY = -1;
        savedSpringActiveX = -1;
        savedSpringActiveY = -1;
        savedSeedPileX = -1;
        savedSeedPileY = -1;
    }

    /**
     * Reset the entire interaction state.
     */
    public void reset() {
        resetSavedPositions();
        isDying = false;
        isBalloon = false;
        isFlying = false;
        isOnIce = false;
        flightState = FLIGHT_NONE;
        springAnimTimer = 0;
        seedCount = 0;
        seedPilesPlaced = 0;
        carrotsCollected = 0;
        hasFlightPowerup = false;
    }

    /**
     * Get tile name for debugging.
     */
    public static String getTileName(byte tile) {
        return LevelData.getTileName(tile);
    }

    /**
     * Get object name for debugging.
     */
    public static String getObjectName(byte object) {
        return LevelData.getObjectName(object);
    }

    // ============================================================
    // Getters and Setters
    // ============================================================

    public LevelData getLevelData() {
        return levelData;
    }

    public void setLevelData(LevelData levelData) {
        this.levelData = levelData;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isDying() {
        return isDying;
    }

    public void setDying(boolean dying) {
        isDying = dying;
    }

    public boolean isBalloon() {
        return isBalloon;
    }

    public void setBalloon(boolean balloon) {
        isBalloon = balloon;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    public boolean isOnIce() {
        return isOnIce;
    }

    public void setOnIce(boolean onIce) {
        isOnIce = onIce;
    }

    public int getFlightState() {
        return flightState;
    }

    public void setFlightState(int flightState) {
        this.flightState = flightState;
    }

    public int getSeedCount() {
        return seedCount;
    }

    public void setSeedCount(int seedCount) {
        this.seedCount = seedCount;
    }

    public int getSeedPilesPlaced() {
        return seedPilesPlaced;
    }

    public void setSeedPilesPlaced(int seedPilesPlaced) {
        this.seedPilesPlaced = seedPilesPlaced;
    }

    public int getCarrotsCollected() {
        return carrotsCollected;
    }

    public void setCarrotsCollected(int carrotsCollected) {
        this.carrotsCollected = carrotsCollected;
    }

    public int getSpecialLevelId() {
        return specialLevelId;
    }

    public void setSpecialLevelId(int specialLevelId) {
        this.specialLevelId = specialLevelId;
    }

    public boolean hasFlightPowerup() {
        return hasFlightPowerup;
    }

    public void setHasFlightPowerup(boolean hasFlightPowerup) {
        this.hasFlightPowerup = hasFlightPowerup;
    }

    public LevelCompleteListener getLevelCompleteListener() {
        return levelCompleteListener;
    }

    public void setLevelCompleteListener(LevelCompleteListener listener) {
        this.levelCompleteListener = listener;
    }

    // ============================================================
    // Inner Classes
    // ============================================================

    /**
     * Enum for interaction results.
     */
    public enum InteractionResult {
        NONE,               // No interaction occurred
        CARROT_COLLECTED,   // Collected a carrot
        SEED_COLLECTED,     // Collected a seed
        SEED_PLACED,        // Placed a seed
        SEED_USED,          // Used a seed
        SEED_PILE,          // Standing on seed pile
        NO_SEEDS,           // Tried to place seed but none available
        FLIGHT_PICKUP,      // Picked up flight powerup
        BONUS_ITEM,         // Collected bonus item
        BONUS_DOOR,         // Entered bonus door
        LEVEL_PROGRESS,     // Made progress in level
        LEVEL_COMPLETE,     // Level completed
        DEATH,              // Player died
        DYING,              // Currently dying (animation)
        PORTAL_USED,        // Used a portal
        PORTAL_ENTRY,       // Entered portal area
        CONVEYOR_PUSH,      // Being pushed by conveyor
        ARROW_TRIGGER,      // Triggered arrow
        ICE_SLIDE,          // Sliding on ice
        SWITCH_TOGGLED,     // Toggled a switch
        MOWER_START,        // Started mower mode
        MOWER_PATH,         // On mower path
        MOWER_TRANSITION,   // Mower mode transition
        BONUS,              // Entered bonus area
        ERROR               // Error occurred
    }

    /**
     * Interface for level completion callback.
     */
    public interface LevelCompleteListener {
        void onLevelComplete();
    }

    @Override
    public String toString() {
        return "LevelInteraction{" +
                "isDying=" + isDying +
                ", isFlying=" + isFlying +
                ", isOnIce=" + isOnIce +
                ", seedCount=" + seedCount +
                ", carrotsCollected=" + carrotsCollected +
                ", flightState=" + flightState +
                '}';
    }
}
