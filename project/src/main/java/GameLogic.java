/**
 * GameLogic.java
 *
 * Core game logic class extracted from decompiled a.java (Bobby Carrot 5).
 * Handles game state updates, player movement, state transitions, and level progress.
 *
 * Original method mappings:
 * - H() -> updateGameState()
 * - M() -> handlePlayerMovement()
 * - J() -> handleTileInteraction()
 * - K() -> handlePlayerDeath()
 * - N() -> updateMovementPosition()
 * - O() -> updateAnimation()
 */
public class GameLogic {

    // ============================================================
    // Game State Constants (bM/bL values)
    // ============================================================
    public static final int MODE_MAIN_MENU = 0;
    public static final int MODE_SHOP = 1;
    public static final int MODE_LEVEL_SELECT = 2;
    public static final int MODE_OPTIONS = 3;
    public static final int MODE_CREDITS = 4;
    public static final int MODE_BONUS = 5;

    // Screen state constants (l field)
    public static final int SCREEN_NONE = 0;
    public static final int SCREEN_GAME = 1;
    public static final int SCREEN_PAUSED = 2;
    public static final int SCREEN_EXTRA_LEVELPACK = 4;
    public static final int SCREEN_MAIN_MENU = 5;
    public static final int SCREEN_SPLASH = 6;
    public static final int SCREEN_LEVEL_COMPLETE = 7;
    public static final int SCREEN_OPTIONS = 8;
    public static final int SCREEN_SUB_MENU = 9;
    public static final int SCREEN_LOADING = 10;
    public static final int SCREEN_FADE_BLACK = 11;
    public static final int SCREEN_TITLE = 12;
    public static final int SCREEN_MESSAGE_DIALOG = 13;
    public static final int SCREEN_HINT_DIALOG = 14;
    public static final int SCREEN_INGAME_MENU = 15;
    public static final int SCREEN_CONFIRM_DIALOG = 16;

    // Direction constants
    public static final int DIR_LEFT = 0;
    public static final int DIR_RIGHT = 1;
    public static final int DIR_UP = 2;
    public static final int DIR_DOWN = 3;
    public static final int DIR_DEAD = 4;
    public static final int DIR_GLIDING = 5;
    public static final int DIR_FALLING = 6;

    // Tile size constant
    public static final int TILE_SIZE = 32;

    // Time limit constants
    private static final long TIME_LIMIT_MS = 60000L; // 60 seconds

    // ============================================================
    // Game State Fields
    // ============================================================

    private Player player;
    private Camera camera;
    private LevelData level;

    // Game mode and screen state
    private int gameMode;      // bM - current game mode
    private int screenState;   // l - current screen state

    // Timing
    private long timeElapsed;
    private long timeRemaining;
    private boolean timerActive;
    private boolean isTimeAttack;

    // Score and progress
    private int score;
    private int carrotsCollected;
    private int totalCarrots;
    private int currentLevel;
    private int currentWorld;

    // Level state flags
    private boolean levelComplete;
    private boolean levelFailed;
    private boolean isPaused;

    // Player movement input
    private boolean inputLeft;
    private boolean inputRight;
    private boolean inputUp;
    private boolean inputDown;

    // Special game states
    private boolean isDying;
    private boolean isFlying;
    private boolean isMower;
    private boolean isDragging;

    // Animation state
    private int animFrame;
    private int animTick;
    private boolean animUpdateReady;

    // Dialog state
    private String dialogTitle;
    private String dialogMessage;
    private int dialogType;

    // Level pack info
    private boolean isBonusLevel;
    private boolean isExtraLevelPack;

    // Cheat mode
    private boolean cheatModeEnabled;

    // ============================================================
    // Constructors
    // ============================================================

    /**
     * Default constructor.
     */
    public GameLogic() {
        reset();
    }

    /**
     * Constructor with dependencies.
     *
     * @param player Player instance
     * @param camera Camera instance
     * @param level  LevelData instance
     */
    public GameLogic(Player player, Camera camera, LevelData level) {
        this.player = player;
        this.camera = camera;
        this.level = level;
        reset();
    }

    // ============================================================
    // Main Update Methods
    // ============================================================

    /**
     * Main game state update method.
     * Corresponds to H() in original a.java.
     *
     * This method handles:
     * - Timer updates and timeout checks
     * - Player movement processing
     * - Tile interactions
     * - State transitions
     * - Special object updates
     *
     * @return true if update was successful, false if blocked by dialog
     */
    public boolean updateGameState() {
        // Check time attack timer
        if (isTimeAttack && timerActive && !isDying && !isFlying) {
            timeRemaining = TIME_LIMIT_MS - timeElapsed;
            if (timeRemaining <= 0) {
                // Time ran out - trigger death
                timeRemaining = 0;
                handlePlayerDeath();
            }
        }

        // Handle death animation countdown
        if (player.getSpecialState() > 0 && !isDying) {
            int deathTimer = player.getSpecialState();
            deathTimer--;
            player.setSpecialState(deathTimer);

            if (deathTimer == 0) {
                player.setSpecialState((byte) -1);
                // Reset some state
                resetAnimationState();
            }
        }

        // Handle cheat mode dialog
        if (gameMode == MODE_MAIN_MENU && !cheatModeEnabled && hasCheatInput()) {
            showCheatDialog();
            return false;
        }

        // Handle reset RMS dialog
        if (gameMode == MODE_MAIN_MENU && isResetRequested()) {
            showResetDialog();
            return false;
        }

        // Store previous camera position
        int prevCameraX = camera.getCameraX();
        int prevCameraY = camera.getCameraY();

        // Process player movement if not in special state
        if (player.getDirection() <= DIR_DOWN && player.getMoveProgress() == 0) {
            player.setPushForce(0);

            // Handle flying state
            if (player.isMoveRequested()) {
                player.setMoveRequested(false);
                // Handle flying movement logic
                handleFlyingMovement();
            }

            // Handle mower timer
            if (player.getMowerTimer() > 0) {
                player.setMowerTimer((byte) (player.getMowerTimer() - 1));
                if (player.getMowerTimer() <= 0) {
                    player.setMowerTimer(0);
                    // Place mower result
                    handleMowerComplete();
                }
            }
        }

        // Process movement if active or dying
        if (player.getMoveProgress() > 0) {
            player.setMoveProgress((byte) (player.getMoveProgress() - 1));
            if (player.getMoveProgress() <= 0) {
                player.setMoveProgress(0);
                handleMovementComplete();
            }
        }

        // Process player movement input
        if (!handlePlayerMovement()) {
            return false;
        }

        // Update animation
        if (updateAnimation()) {
            return false;
        }

        // Handle special game state transitions
        if (screenState == SCREEN_MAIN_MENU && (inputLeft || inputRight || inputUp || inputDown)) {
            // Handle menu navigation
            inputLeft = false;
            inputRight = false;
            inputUp = false;
            inputDown = false;
            // Transition handled elsewhere
            return true;
        }

        // Handle transition effects
        if (player.getMoveProgress() != 0) {
            if (player.isMoveRequested() && player.getMoveProgress() <= 16) {
                player.setMoveRequested(false);
                handleTileInteraction();
            }
            updateBounceState();
            player.setMoveProgress(0);
            updateMovementPosition();

            if (player.getMoveProgress() == 0) {
                handleMovementStateChange();
            }
        } else if (!isDying && !isFlying && !isMower && player.getMowerTimer() == 0
                && player.getDirection() < DIR_DEAD) {
            // Idle timeout - start death animation
            player.setDirection(DIR_DEAD);
            player.setAnimFrame(0);
            player.setDeathAnimForward(true);
        }

        // Update special effects
        updateSpecialEffects();

        // Update camera if player moved
        if (camera.getCameraX() != prevCameraX || camera.getCameraY() != prevCameraY) {
            camera.updateCamera(player.getPixelX(), player.getPixelY());
        }

        return true;
    }

    /**
     * Handle player movement based on input.
     * Corresponds to M() in original a.java.
     *
     * @return true if movement was processed, false if blocked
     */
    public boolean handlePlayerMovement() {
        int playerTileX = player.getTileX();
        int playerTileY = player.getTileY();

        byte currentTile = level.getTileAt(playerTileX, playerTileY);
        byte currentMeta = level.getObjectAt(playerTileX, playerTileY);

        // Handle movement request flag
        if (player.isMoveRequested()) {
            player.setMoveRequested(false);
            switch (player.getDirection()) {
                case DIR_LEFT:
                    if (canMove(-1, 0, false)) {
                        player.setTileX(playerTileX - 1);
                        player.setMoveProgress(TILE_SIZE);
                        if (!isFlying) {
                            player.setMoving(true);
                        }
                        return true;
                    }
                    break;
                case DIR_RIGHT:
                    if (canMove(1, 0, false)) {
                        player.setTileX(playerTileX + 1);
                        player.setMoveProgress(TILE_SIZE);
                        if (!isFlying) {
                            player.setMoving(true);
                        }
                        return true;
                    }
                    break;
                case DIR_UP:
                    if (canMove(0, -1, false)) {
                        player.setTileY(playerTileY - 1);
                        player.setMoveProgress(TILE_SIZE);
                        if (!isFlying) {
                            player.setMoving(true);
                        }
                        return true;
                    }
                    break;
                case DIR_DOWN:
                    if (canMove(0, 1, false)) {
                        player.setTileY(playerTileY + 1);
                        player.setMoveProgress(TILE_SIZE);
                        if (!isFlying) {
                            player.setMoving(true);
                        }
                        return true;
                    }
                    break;
            }
        }

        // Handle dying movement (forced movement)
        if (isDying) {
            switch (player.getDirection()) {
                case DIR_LEFT:
                    player.setTileX(playerTileX - 1);
                    player.setMoveProgress(TILE_SIZE);
                    break;
                case DIR_RIGHT:
                    player.setTileX(playerTileX + 1);
                    player.setMoveProgress(TILE_SIZE);
                    break;
                case DIR_UP:
                    player.setTileY(playerTileY - 1);
                    player.setMoveProgress(TILE_SIZE);
                    break;
                case DIR_DOWN:
                    player.setTileY(playerTileY + 1);
                    player.setMoveProgress(TILE_SIZE);
                    break;
            }
            return true;
        }

        // Handle conveyor/arrow tiles
        if (currentMeta != -44) { // Not special blocked tile
            if (currentTile == -73 && canMove(-1, 0, true)) {
                player.setDirection(DIR_LEFT);
                player.setPushForce(3);
            } else if (currentTile == -72 && canMove(1, 0, true)) {
                player.setDirection(DIR_RIGHT);
                player.setPushForce(3);
            } else if (currentTile == -75 && canMove(0, -1, true)) {
                player.setDirection(DIR_UP);
                player.setPushForce(3);
            } else if (currentTile == -74 && canMove(0, 1, true)) {
                player.setDirection(DIR_DOWN);
                player.setPushForce(3);
            }
        }

        // Handle push force movement
        if (player.getPushForce() > 0) {
            boolean moveSuccess = true;
            boolean blocked = false;

            switch (player.getDirection()) {
                case DIR_LEFT:
                    if (canMove(-1, 0, false)) {
                        player.setTileX(playerTileX - 1);
                        if (isDragging || !inputLeft) {
                            moveSuccess = false;
                        }
                    } else {
                        blocked = true;
                    }
                    break;
                case DIR_RIGHT:
                    if (canMove(1, 0, false)) {
                        player.setTileX(playerTileX + 1);
                        if (isDragging || !inputRight) {
                            moveSuccess = false;
                        }
                    } else {
                        blocked = true;
                    }
                    break;
                case DIR_UP:
                    if (canMove(0, -1, false)) {
                        player.setTileY(playerTileY - 1);
                        if (isDragging || !inputUp) {
                            moveSuccess = false;
                        }
                    } else {
                        blocked = true;
                    }
                    break;
                case DIR_DOWN:
                    if (canMove(0, 1, false)) {
                        player.setTileY(playerTileY + 1);
                        if (isDragging || !inputDown) {
                            moveSuccess = false;
                        }
                    } else {
                        blocked = true;
                    }
                    break;
            }

            if (!blocked) {
                // Handle special tile interactions
                if (isFlying && level.getObjectAt(playerTileX, playerTileY) == -19) {
                    level.setObjectAt(playerTileX, playerTileY, (byte) -1);
                    // Trigger effect
                }
                player.setPushForce(moveSuccess || currentTile == -108 ? 3 : player.getPushForce() - 1);
                if (currentTile == -108) {
                    player.setOnIce(true);
                    player.setPushForce(3);
                }
                player.setMoveProgress(TILE_SIZE);
                return true;
            }

            player.setPushForce(0);
            player.setSpecialState(8);
            player.setAnimUpdateReady(false);
        }

        // Handle ice sliding
        if (player.getMoveProgress() == 0 && currentTile == -108) {
            boolean moveSuccess = false;
            switch (player.getDirection()) {
                case DIR_LEFT:
                    if (canMove(-1, 0, false)) {
                        player.setTileX(playerTileX - 1);
                        moveSuccess = true;
                    }
                    break;
                case DIR_RIGHT:
                    if (canMove(1, 0, false)) {
                        player.setTileX(playerTileX + 1);
                        moveSuccess = true;
                    }
                    break;
                case DIR_UP:
                    if (canMove(0, -1, false)) {
                        player.setTileY(playerTileY - 1);
                        moveSuccess = true;
                    }
                    break;
                case DIR_DOWN:
                    if (canMove(0, 1, false)) {
                        player.setTileY(playerTileY + 1);
                        moveSuccess = true;
                    }
                    break;
                default:
                    moveSuccess = true;
                    break;
            }

            if (!moveSuccess) {
                player.setMoveProgress(TILE_SIZE);
                player.setOnIce(true);
                player.setAnimFrame(1);
                if (!isFlying && !isDragging) {
                    player.setMoving(true);
                }
                return true;
            }
        }

        // Handle normal input movement
        if (!isDragging && player.getSpecialState() == 0) {
            if (inputLeft && canMove(-1, 0, false)) {
                player.setTileX(playerTileX - 1);
                player.setDirection(DIR_LEFT);
                player.setMoveProgress(TILE_SIZE);
                if (!isFlying && !isDragging) {
                    player.setMoving(true);
                }
                return true;
            } else if (inputRight && canMove(1, 0, false)) {
                player.setTileX(playerTileX + 1);
                player.setDirection(DIR_RIGHT);
                player.setMoveProgress(TILE_SIZE);
                if (!isFlying && !isDragging) {
                    player.setMoving(true);
                }
                return true;
            } else if (inputUp && canMove(0, -1, false)) {
                player.setTileY(playerTileY - 1);
                player.setDirection(DIR_UP);
                player.setMoveProgress(TILE_SIZE);
                if (!isFlying && !isDragging) {
                    player.setMoving(true);
                }
                return true;
            } else if (inputDown && canMove(0, 1, false)) {
                player.setTileY(playerTileY + 1);
                player.setDirection(DIR_DOWN);
                player.setMoveProgress(TILE_SIZE);
                if (!isFlying && !isDragging) {
                    player.setMoving(true);
                }
                return true;
            }
        }

        return false;
    }

    // ============================================================
    // State Transition Methods
    // ============================================================

    /**
     * Transition to a new screen state.
     *
     * @param newScreenState The target screen state
     */
    public void transitionToScreen(int newScreenState) {
        this.screenState = newScreenState;

        switch (newScreenState) {
            case SCREEN_GAME:
                startGame();
                break;
            case SCREEN_MAIN_MENU:
                showMainMenu();
                break;
            case SCREEN_LEVEL_COMPLETE:
                showLevelComplete();
                break;
            case SCREEN_PAUSED:
                pauseGame();
                break;
            default:
                break;
        }
    }

    /**
     * Transition between game modes.
     *
     * @param newGameMode The target game mode
     */
    public void transitionToMode(int newGameMode) {
        this.gameMode = newGameMode;

        switch (newGameMode) {
            case MODE_MAIN_MENU:
                screenState = SCREEN_MAIN_MENU;
                break;
            case MODE_SHOP:
                screenState = SCREEN_OPTIONS;
                break;
            case MODE_LEVEL_SELECT:
                screenState = SCREEN_SUB_MENU;
                break;
            case MODE_BONUS:
                isBonusLevel = true;
                screenState = SCREEN_GAME;
                break;
            default:
                break;
        }
    }

    /**
     * Start the game from menu.
     */
    public void startGame() {
        isPaused = false;
        levelComplete = false;
        levelFailed = false;
        resetPlayer();
        loadLevel(currentLevel);
    }

    /**
     * Pause the game.
     */
    public void pauseGame() {
        isPaused = true;
        timerActive = false;
    }

    /**
     * Resume the game from pause.
     */
    public void resumeGame() {
        isPaused = false;
        if (isTimeAttack) {
            timerActive = true;
        }
    }

    /**
     * Handle level completion.
     */
    public void completeLevel() {
        levelComplete = true;
        screenState = SCREEN_LEVEL_COMPLETE;
        timerActive = false;

        // Calculate score
        int levelScore = calculateLevelScore();
        score += levelScore;

        // Save progress
        saveProgress();
    }

    /**
     * Handle player death.
     * Corresponds to K() in original a.java.
     */
    public void handlePlayerDeath() {
        levelFailed = true;
        isDying = true;
        player.setDying(true);
        player.setMoveProgress(0);
        player.setDirection(DIR_DEAD);
        player.setAnimFrame(0);
        player.setMoving(false);
        player.setOnIce(false);

        if (isDragging) {
            isDragging = false;
            resetAnimationState();
        }

        timerActive = false;
        player.setAnimUpdateReady(false);

        // Play death sound
        playDeathSound();
    }

    /**
     * Show main menu.
     */
    public void showMainMenu() {
        screenState = SCREEN_MAIN_MENU;
        gameMode = MODE_MAIN_MENU;
        isPaused = false;
        levelComplete = false;
        levelFailed = false;
    }

    /**
     * Show level complete screen.
     */
    public void showLevelComplete() {
        screenState = SCREEN_LEVEL_COMPLETE;
        timerActive = false;
    }

    /**
     * Show options menu.
     */
    public void showOptionsMenu() {
        screenState = SCREEN_OPTIONS;
    }

    /**
     * Show in-game menu.
     */
    public void showInGameMenu() {
        screenState = SCREEN_INGAME_MENU;
        isPaused = true;
    }

    /**
     * Show dialog.
     *
     * @param type    Dialog type
     * @param title   Dialog title
     * @param message Dialog message
     */
    public void showDialog(int type, String title, String message, String extra) {
        this.dialogType = type;
        this.dialogTitle = title;
        this.dialogMessage = message;
        screenState = SCREEN_MESSAGE_DIALOG;
    }

    public void showDialog(int type, String title, String message) {
        showDialog(type, title, message, null);
    }

    // ============================================================
    // Score and Progress Methods
    // ============================================================

    /**
     * Update score.
     *
     * @param points Points to add
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Calculate level score based on performance.
     *
     * @return Calculated score
     */
    public int calculateLevelScore() {
        int baseScore = 1000;
        int carrotBonus = carrotsCollected * 100;
        int timeBonus = (int) (timeRemaining / 100); // Time bonus

        return baseScore + carrotBonus + timeBonus;
    }

    /**
     * Collect a carrot.
     */
    public void collectCarrot() {
        carrotsCollected++;
        addScore(100);

        if (carrotsCollected >= totalCarrots) {
            completeLevel();
        }
    }

    /**
     * Update time tracking.
     *
     * @param deltaTimeMs Time elapsed since last update in milliseconds
     */
    public void updateTime(long deltaTimeMs) {
        if (timerActive && isTimeAttack) {
            timeElapsed += deltaTimeMs;
            timeRemaining = Math.max(0, TIME_LIMIT_MS - timeElapsed);
        }
    }

    /**
     * Save game progress.
     */
    public void saveProgress() {
        // Implementation would save to RMS
    }

    /**
     * Load a level.
     *
     * @param levelNumber Level number to load
     */
    public void loadLevel(int levelNumber) {
        this.currentLevel = levelNumber;
        this.carrotsCollected = 0;
        this.timeElapsed = 0;
        this.timeRemaining = TIME_LIMIT_MS;
        this.levelComplete = false;
        this.levelFailed = false;

        if (level != null) {
            level.setLevelId(levelNumber);
            this.totalCarrots = level.countCarrots();
        }

        resetPlayer();
    }

    /**
     * Go to next level.
     */
    public void nextLevel() {
        currentLevel++;
        if (currentLevel > getMaxLevelsInWorld()) {
            currentWorld++;
            currentLevel = 1;
        }
        loadLevel(currentLevel);
    }

    /**
     * Restart current level.
     */
    public void restartLevel() {
        loadLevel(currentLevel);
    }

    // ============================================================
    // Movement and Collision Helpers
    // ============================================================

    /**
     * Check if movement is possible.
     *
     * @param deltaX     Tile X delta
     * @param deltaY     Tile Y delta
     * @param checkOnly  If true, only check without triggering effects
     * @return true if movement is possible
     */
    private boolean canMove(int deltaX, int deltaY, boolean checkOnly) {
        int newTileX = player.getTileX() + deltaX;
        int newTileY = player.getTileY() + deltaY;

        // Check bounds
        if (newTileX < 0 || newTileY < 0 ||
            newTileX >= level.getMapWidth() || newTileY >= level.getMapHeight()) {
            return false;
        }

        // Dying can always move
        if (isDying) {
            return true;
        }

        byte currentTile = level.getTileAt(player.getTileX(), player.getTileY());
        byte targetTile = level.getTileAt(newTileX, newTileY);
        byte targetMeta = level.getObjectAt(newTileX, newTileY);

        // Check directional tiles
        if (currentTile == -66) {  // One-way right
            return deltaX != 0;
        } else if (currentTile == -67) {  // One-way down
            return deltaY != 0;
        } else if (currentTile == -68) {  // One-way bottom-right
            return deltaX == 1 || deltaY == 1;
        } else if (currentTile == -69) {  // One-way bottom-left
            return deltaX == -1 || deltaY == 1;
        } else if (currentTile == -70) {  // One-way top-left
            return deltaX == -1 || deltaY == -1;
        } else if (currentTile == -71) {  // One-way top-right
            return deltaX == 1 || deltaY == -1;
        }

        // Check if tile is walkable
        int tileValue = targetTile & 0xFF;
        if (tileValue >= 94 && tileValue <= 200) {
            // Special tile handling
            if (tileValue >= 185 && tileValue <= 190) {
                // Directional tiles
                return !isFlying;
            } else if (tileValue >= 177 && tileValue <= 180) {
                return !isFlying;
            } else if (targetTile == -57 || targetTile == -56) {
                if (isFlying) {
                    player.setMower(true);
                } else {
                    return false;
                }
            } else if (targetTile == -61 || targetTile == -59) {
                return false;
            }
        } else if (targetTile == 77 && !isFlying) {
            // Gate handling
            return false;
        }

        // Check meta tile
        switch (targetMeta) {
            case -50:
            case -44:
            case -34:
                return !isFlying;
            case -54:
                return !isFlying;
            case -52:
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
                return false;
            case -51:
                // Bonus carrot
                if (!isFlying) {
                    // Handle bonus unlock
                    return false;
                }
                break;
        }

        return true;
    }

    /**
     * Handle tile interaction when player arrives at new tile.
     * Corresponds to J() in original a.java.
     */
    private void handleTileInteraction() {
        int tileX = player.getTileX();
        int tileY = player.getTileY();

        byte currentTile = level.getTileAt(tileX, tileY);
        byte currentMeta = level.getObjectAt(tileX, tileY);

        // Handle special tile interactions
        switch (currentMeta) {
            case -11:  // Spring bounce
                if (!isDying) {
                    player.setBounceState((byte) 2);
                }
                break;
            case -12:  // Trampoline
                if (!isFlying) {
                    player.setBounceState((byte) 1);
                    player.setBounceOffset(0);
                }
                break;
            case -44:  // Drag object
                // Handle drag interaction
                break;
            case -39:  // Spike trigger
                // Handle spike trigger
                break;
            case -51:  // Bonus unlock
                // Handle bonus unlock
                break;
            case -8:   // Coin
                score += 10;
                level.setObjectAt(tileX, tileY, (byte) -1);
                break;
            case -35:  // Powerup
            case -13:  // Powerup
                // Handle powerup collection
                level.setObjectAt(tileX, tileY, (byte) -1);
                break;
            case -49:  // Extra life
                // Add extra life
                level.setObjectAt(tileX, tileY, (byte) -1);
                break;
        }

        // Handle special tiles
        if (!isFlying) {
            switch (currentTile) {
                case -81:  // Death tile
                    handlePlayerDeath();
                    break;
                case -106:  // Exit
                    if (carrotsCollected == 0) {
                        handlePlayerDeath();
                    } else {
                        completeLevel();
                    }
                    break;
                case -97:  // Checkpoint
                    // Save checkpoint
                    break;
            }
        } else {
            // Flying-specific interactions
            if (currentTile == -96) {
                // Handle flying pickup
            }
        }

        // Handle collectible tiles
        int tileValue = currentTile & 0xFF;
        if (tileValue >= 151 && tileValue <= 157) {
            // Carrot tile
            collectCarrot();
        }
    }

    /**
     * Update movement position.
     * Corresponds to N() in original a.java.
     */
    private void updateMovementPosition() {
        int direction = player.getDirection();
        int speed = player.getPushForce() > 0 || isMower ? 4 : 2;

        player.setMoveProgress(player.getMoveProgress() - speed);

        // Handle falling direction
        if (player.getDirection() == DIR_FALLING) {
            direction = player.getFallDirection();
        }

        int pixelX = player.getPixelX();
        int pixelY = player.getPixelY();

        switch (direction) {
            case DIR_LEFT:
                player.setPixelX(pixelX - speed);
                break;
            case DIR_RIGHT:
                player.setPixelX(pixelX + speed);
                break;
            case DIR_UP:
                player.setPixelY(pixelY - speed);
                break;
            case DIR_DOWN:
                player.setPixelY(pixelY + speed);
                break;
        }
    }

    /**
     * Update animation state.
     * Corresponds to O() in original a.java.
     *
     * @return true if animation completed (e.g., death animation)
     */
    private boolean updateAnimation() {
        if (!player.isAnimUpdateReady()) {
            return false;
        }

        // Update animation tick
        animTick = (animTick + 1) % 12;

        if (isFlying) {
            // Gliding animation
            animFrame = (animFrame + 1) % 2;
        } else if (isDying) {
            // Death animation
            animFrame = 0;
        } else if (player.getMowerTimer() > 0) {
            // Mower animation
            animFrame = (animFrame + 1) % 9;
        } else {
            // Normal walking animation
            if (player.getMoveProgress() != 0) {
                if (!player.isOnIce()) {
                    animFrame = (animFrame + 1) % 8;
                } else {
                    animFrame = 1;  // Ice slide frame
                }
            }
        }

        player.setAnimFrame(animFrame);
        player.setAnimTick(animTick);

        return false;
    }

    // ============================================================
    // Helper Methods
    // ============================================================

    /**
     * Reset player to initial state.
     */
    private void resetPlayer() {
        if (player != null) {
            player.reset();
            isDying = false;
            isFlying = false;
            isMower = false;
            isDragging = false;
        }
    }

    /**
     * Reset animation state.
     */
    private void resetAnimationState() {
        animFrame = 0;
        animTick = 0;
        animUpdateReady = false;
    }

    /**
     * Handle flying movement logic.
     */
    private void handleFlyingMovement() {
        // Flying-specific movement handling
    }

    /**
     * Handle mower completion.
     */
    private void handleMowerComplete() {
        // Handle mower mode completion
    }

    /**
     * Handle movement complete.
     */
    private void handleMovementComplete() {
        // Sync tile from pixel position
        player.syncTileFromPixel();
        handleTileInteraction();
    }

    /**
     * Handle movement state change.
     */
    private void handleMovementStateChange() {
        player.setMoving(false);
        player.setOnIce(false);
    }

    /**
     * Update bounce state.
     */
    private void updateBounceState() {
        switch (player.getBounceState()) {
            case 1:
                player.setBounceOffset(player.getBounceOffset() + 4);
                break;
            case 2:
                player.setBounceOffset(player.getBounceOffset() - 4);
                break;
        }
    }

    /**
     * Update special effects.
     */
    private void updateSpecialEffects() {
        // Update particles, effects, etc.
    }

    /**
     * Handle death sound.
     */
    private void playDeathSound() {
        // Implementation would play death sound
    }

    /**
     * Check for cheat input sequence.
     */
    private boolean hasCheatInput() {
        return false; // Placeholder
    }

    /**
     * Check for reset request.
     */
    private boolean isResetRequested() {
        return false; // Placeholder
    }

    /**
     * Show cheat activation dialog.
     */
    private void showCheatDialog() {
        showDialog(8, "CHEAT MODE", "Do you want to enable cheats?", null);
    }

    /**
     * Show reset confirmation dialog.
     */
    private void showResetDialog() {
        showDialog(11, "RESET GAME", "Do you want to format RMS and reset?", null);
    }

    /**
     * Get max levels in current world.
     */
    private int getMaxLevelsInWorld() {
        return 10; // Placeholder
    }

    // ============================================================
    // Input Methods
    // ============================================================

    /**
     * Set movement input.
     *
     * @param left  Move left
     * @param right Move right
     * @param up    Move up
     * @param down  Move down
     */
    public void setInput(boolean left, boolean right, boolean up, boolean down) {
        this.inputLeft = left;
        this.inputRight = right;
        this.inputUp = up;
        this.inputDown = down;
    }

    /**
     * Clear all input.
     */
    public void clearInput() {
        this.inputLeft = false;
        this.inputRight = false;
        this.inputUp = false;
        this.inputDown = false;
    }

    // ============================================================
    // Getters and Setters
    // ============================================================

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public LevelData getLevel() {
        return level;
    }

    public void setLevel(LevelData level) {
        this.level = level;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public int getScreenState() {
        return screenState;
    }

    public void setScreenState(int screenState) {
        this.screenState = screenState;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCarrotsCollected() {
        return carrotsCollected;
    }

    public void setCarrotsCollected(int carrotsCollected) {
        this.carrotsCollected = carrotsCollected;
    }

    public int getTotalCarrots() {
        return totalCarrots;
    }

    public void setTotalCarrots(int totalCarrots) {
        this.totalCarrots = totalCarrots;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getCurrentWorld() {
        return currentWorld;
    }

    public void setCurrentWorld(int currentWorld) {
        this.currentWorld = currentWorld;
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public void setLevelComplete(boolean levelComplete) {
        this.levelComplete = levelComplete;
    }

    public boolean isLevelFailed() {
        return levelFailed;
    }

    public void setLevelFailed(boolean levelFailed) {
        this.levelFailed = levelFailed;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public boolean isTimeAttack() {
        return isTimeAttack;
    }

    public void setTimeAttack(boolean timeAttack) {
        isTimeAttack = timeAttack;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    public boolean isDying() {
        return isDying;
    }

    public void setDying(boolean dying) {
        isDying = dying;
    }

    public boolean isMower() {
        return isMower;
    }

    public void setMower(boolean mower) {
        isMower = mower;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public boolean isBonusLevel() {
        return isBonusLevel;
    }

    public void setBonusLevel(boolean bonusLevel) {
        isBonusLevel = bonusLevel;
    }

    public boolean isTimerActive() {
        return timerActive;
    }

    public void setTimerActive(boolean timerActive) {
        this.timerActive = timerActive;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public String getDialogMessage() {
        return dialogMessage;
    }

    public int getDialogType() {
        return dialogType;
    }

    /**
     * Reset game logic to initial state.
     */
    public void reset() {
        this.gameMode = MODE_MAIN_MENU;
        this.screenState = SCREEN_MAIN_MENU;
        this.score = 0;
        this.carrotsCollected = 0;
        this.totalCarrots = 0;
        this.currentLevel = 1;
        this.currentWorld = 1;
        this.levelComplete = false;
        this.levelFailed = false;
        this.isPaused = false;
        this.timeElapsed = 0;
        this.timeRemaining = TIME_LIMIT_MS;
        this.timerActive = false;
        this.isTimeAttack = false;
        this.isDying = false;
        this.isFlying = false;
        this.isMower = false;
        this.isDragging = false;
        this.animFrame = 0;
        this.animTick = 0;
        this.animUpdateReady = false;
        this.isBonusLevel = false;
        this.isExtraLevelPack = false;
        this.cheatModeEnabled = false;
        clearInput();
    }

    @Override
    public String toString() {
        return "GameLogic{" +
                "screenState=" + getScreenName(screenState) +
                ", gameMode=" + gameMode +
                ", currentLevel=" + currentLevel +
                ", currentWorld=" + currentWorld +
                ", score=" + score +
                ", carrots=" + carrotsCollected + "/" + totalCarrots +
                ", isPaused=" + isPaused +
                ", levelComplete=" + levelComplete +
                '}';
    }

    /**
     * Get screen state name as string.
     */
    public static String getScreenName(int state) {
        switch (state) {
            case SCREEN_NONE: return "NONE";
            case SCREEN_GAME: return "GAME";
            case SCREEN_PAUSED: return "PAUSED";
            case SCREEN_EXTRA_LEVELPACK: return "EXTRA_LEVELPACK";
            case SCREEN_MAIN_MENU: return "MAIN_MENU";
            case SCREEN_SPLASH: return "SPLASH";
            case SCREEN_LEVEL_COMPLETE: return "LEVEL_COMPLETE";
            case SCREEN_OPTIONS: return "OPTIONS";
            case SCREEN_SUB_MENU: return "SUB_MENU";
            case SCREEN_LOADING: return "LOADING";
            case SCREEN_FADE_BLACK: return "FADE_BLACK";
            case SCREEN_TITLE: return "TITLE";
            case SCREEN_MESSAGE_DIALOG: return "MESSAGE_DIALOG";
            case SCREEN_HINT_DIALOG: return "HINT_DIALOG";
            case SCREEN_INGAME_MENU: return "INGAME_MENU";
            case SCREEN_CONFIRM_DIALOG: return "CONFIRM_DIALOG";
            default: return "UNKNOWN(" + state + ")";
        }
    }
}
