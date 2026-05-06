/**
 * Player class - Encapsulates player state and movement logic.
 * Extracted from decompiled a.java (Bobby Carrot 5).
 *
 * Original field mappings:
 * - ag -> playerPixelX
 * - ah -> playerPixelY
 * - ai -> playerTileX
 * - aj -> playerTileY
 * - an -> direction
 * - ap -> moveProgress
 * - am -> animFrame
 * - aE -> pushForce
 * - ao -> fallDirection
 * - dM -> isDragging
 * - aZ -> isFlying
 * - bd -> isDying
 * - bc -> isMoving
 * - be -> isOnIce
 * - bb -> isMower
 * - ba -> isBalloon
 * - bf -> moveRequested
 * - aW -> animUpdateReady
 * - aV -> deathAnimForward
 * - aT -> mowerTimer
 * - aS -> bounceState
 * - aN -> bounceOffset
 * - aR -> specialState
 * - au -> animTick
 */
public class Player {

    // Direction constants
    public static final int DIR_LEFT = 0;
    public static final int DIR_RIGHT = 1;
    public static final int DIR_UP = 2;
    public static final int DIR_DOWN = 3;
    public static final int DIR_DEAD = 4;
    public static final int DIR_GLIDING = 5;
    public static final int DIR_FALLING = 6;

    // Tile size constant (32 pixels per tile)
    public static final int TILE_SIZE = 32;

    // Pixel coordinates (actual screen position in pixels)
    private int pixelX;
    private int pixelY;

    // Tile coordinates (grid position)
    private int tileX;
    private int tileY;

    // Direction facing (0-6, see DIR_* constants)
    private int direction;

    // Movement progress (0-32, counts down during movement)
    private int moveProgress;

    // Animation frame index
    private int animFrame;

    // Animation tick counter
    private int animTick;

    // Push force (ice/conveyor push momentum)
    private int pushForce;

    // Fall direction (stored direction when falling)
    private int fallDirection;

    // State flags
    private boolean isDragging;
    private boolean isFlying;
    private boolean isDying;
    private boolean isMoving;
    private boolean isOnIce;
    private boolean isMower;
    private boolean isBalloon;
    private boolean moveRequested;
    private boolean animUpdateReady;
    private boolean deathAnimForward;

    // Additional state
    private int mowerTimer;
    private int bounceState;
    private int bounceOffset;
    private int specialState;  // -1 = none

    /**
     * Default constructor - initializes player to default state.
     */
    public Player() {
        reset();
    }

    /**
     * Reset player to initial state.
     */
    public void reset() {
        this.pixelX = 0;
        this.pixelY = 0;
        this.tileX = -1;
        this.tileY = 0;
        this.direction = DIR_RIGHT;
        this.moveProgress = 1;
        this.animFrame = 0;
        this.animTick = 0;
        this.pushForce = 0;
        this.fallDirection = 0;

        this.isDragging = false;
        this.isFlying = false;
        this.isDying = true;  // Start in intro/dying state for title
        this.isMoving = false;
        this.isOnIce = false;
        this.isMower = false;
        this.isBalloon = false;
        this.moveRequested = false;
        this.animUpdateReady = false;
        this.deathAnimForward = false;

        this.mowerTimer = 0;
        this.bounceState = 0;
        this.bounceOffset = 0;
        this.specialState = -1;
    }

    /**
     * Initialize player at a specific position.
     *
     * @param tileX      Starting tile X coordinate
     * @param tileY      Starting tile Y coordinate
     * @param direction  Initial facing direction
     */
    public void initPosition(int tileX, int tileY, int direction) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.pixelX = tileX * TILE_SIZE;
        this.pixelY = tileY * TILE_SIZE;
        this.direction = direction;
        this.moveProgress = 0;
        this.animFrame = 0;
    }

    /**
     * Update movement - called each frame to update pixel position.
     * This corresponds to method N() in original a.java.
     *
     * Movement speed is 2 pixels per frame normally,
     * or 4 pixels per frame when pushed or mowing.
     */
    public void updateMovement() {
        int dir = this.direction;
        int speed = (this.pushForce > 0 || this.isMower) ? 4 : 2;

        // Decrease movement progress
        this.moveProgress -= speed;

        // Handle falling direction override
        if (this.direction == DIR_FALLING) {
            dir = this.fallDirection;
        }

        // Update pixel position based on direction
        switch (dir) {
            case DIR_LEFT:
                this.pixelX -= speed;
                break;
            case DIR_RIGHT:
                this.pixelX += speed;
                break;
            case DIR_UP:
                this.pixelY -= speed;
                break;
            case DIR_DOWN:
                this.pixelY += speed;
                break;
        }
    }

    /**
     * Update animation frame.
     * This corresponds to method O() in original a.java.
     *
     * @return true if animation completed (e.g., death animation finished)
     */
    public boolean updateAnimation() {
        if (!this.animUpdateReady) {
            return false;
        }

        // Check if animation should update
        if (this.isFlying || this.direction > DIR_DOWN && !this.isDying
                && (this.pushForce > 0 || this.isMoving && !this.isFlying && this.mowerTimer <= 0)) {

            // Update animation tick
            this.animTick = (this.animTick + 1) % 12;

            if (this.isFlying) {
                // Gliding animation
                this.animFrame = (this.animFrame + 1) % 2;
            } else if (this.isDying) {
                // Death animation - no frame change
                this.animFrame = 0;
            } else if (this.mowerTimer > 0) {
                // Mower animation
                this.animFrame = (this.animFrame + 1) % 9;
            } else {
                // Normal walking animation based on direction
                switch (this.direction) {
                    case DIR_LEFT:
                    case DIR_RIGHT:
                    case DIR_UP:
                    case DIR_DOWN:
                        if (this.moveProgress != 0) {
                            if (!this.isOnIce) {
                                this.animFrame = (this.animFrame + 1) % 8;
                            } else {
                                this.animFrame = 1;  // Ice slide frame
                            }
                        }
                        break;

                    case DIR_DEAD:
                        updateDeathAnimation();
                        break;

                    case DIR_GLIDING:
                        if (this.animFrame < 7) {
                            this.animFrame++;
                        }
                        break;

                    case DIR_FALLING:
                        updateFallingAnimation();
                        break;
                }
            }

            // Update special state animation
            if (this.specialState != -1) {
                this.specialState = (this.specialState + 1) % 2;
            }

            this.animUpdateReady = true;
        } else {
            this.animUpdateReady = false;
        }

        // Keep animFrame at 1 when on ice
        if (this.direction <= DIR_DOWN && this.isOnIce) {
            this.animFrame = 1;
        }

        return false;
    }

    /**
     * Update death animation frames.
     */
    private void updateDeathAnimation() {
        if (this.deathAnimForward) {
            this.animFrame++;
            if (this.animFrame >= 3) {
                this.animFrame = 1;
                this.deathAnimForward = false;
            }
        } else {
            this.animFrame--;
            if (this.animFrame < 0) {
                this.animFrame = 1;
                this.deathAnimForward = true;
            }
        }
    }

    /**
     * Update falling animation frames.
     *
     * @return true if falling animation completed
     */
    private boolean updateFallingAnimation() {
        if (this.deathAnimForward) {
            this.animFrame++;
            if (this.animFrame >= 10) {
                return true;  // Animation complete
            }
        } else {
            this.animFrame--;
            if (this.animFrame < 0) {
                this.direction = DIR_DOWN;
                this.animFrame = 3;
            }
        }
        return false;
    }

    /**
     * Start movement in a direction.
     *
     * @param dir       Direction to move
     * @param tileDelta Change in tile coordinate
     */
    public void startMovement(int dir, int tileDelta) {
        this.moveProgress = TILE_SIZE;
        this.moveRequested = false;

        switch (dir) {
            case DIR_LEFT:
                this.tileX--;
                break;
            case DIR_RIGHT:
                this.tileX++;
                break;
            case DIR_UP:
                this.tileY--;
                break;
            case DIR_DOWN:
                this.tileY++;
                break;
        }
    }

    /**
     * Check if movement is in progress.
     *
     * @return true if player is currently moving between tiles
     */
    public boolean isMovingBetweenTiles() {
        return this.moveProgress > 0;
    }

    /**
     * Get movement completion percentage (0.0 to 1.0).
     *
     * @return percentage of movement completed
     */
    public float getMovementProgress() {
        return 1.0f - (float) this.moveProgress / TILE_SIZE;
    }

    /**
     * Sync tile coordinates from pixel coordinates.
     * Call this when pixel position changes externally.
     */
    public void syncTileFromPixel() {
        this.tileX = this.pixelX >> 5;  // Divide by 32
        this.tileY = this.pixelY >> 5;
    }

    /**
     * Sync pixel coordinates from tile coordinates.
     * Call this when tile position changes externally.
     */
    public void syncPixelFromTile() {
        this.pixelX = this.tileX * TILE_SIZE;
        this.pixelY = this.tileY * TILE_SIZE;
    }

    // ============================================================
    // Getters and Setters
    // ============================================================

    public int getPixelX() {
        return pixelX;
    }

    public void setPixelX(int pixelX) {
        this.pixelX = pixelX;
    }

    public int getPixelY() {
        return pixelY;
    }

    public void setPixelY(int pixelY) {
        this.pixelY = pixelY;
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getMoveProgress() {
        return moveProgress;
    }

    public void setMoveProgress(int moveProgress) {
        this.moveProgress = moveProgress;
    }

    public int getAnimFrame() {
        return animFrame;
    }

    public void setAnimFrame(int animFrame) {
        this.animFrame = animFrame;
    }

    public int getAnimTick() {
        return animTick;
    }

    public void setAnimTick(int animTick) {
        this.animTick = animTick;
    }

    public int getPushForce() {
        return pushForce;
    }

    public void setPushForce(int pushForce) {
        this.pushForce = pushForce;
    }

    public int getFallDirection() {
        return fallDirection;
    }

    public void setFallDirection(int fallDirection) {
        this.fallDirection = fallDirection;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
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

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isOnIce() {
        return isOnIce;
    }

    public void setOnIce(boolean onIce) {
        isOnIce = onIce;
    }

    public boolean isMower() {
        return isMower;
    }

    public void setMower(boolean mower) {
        isMower = mower;
    }

    public boolean isBalloon() {
        return isBalloon;
    }

    public void setBalloon(boolean balloon) {
        isBalloon = balloon;
    }

    public boolean isMoveRequested() {
        return moveRequested;
    }

    public void setMoveRequested(boolean moveRequested) {
        this.moveRequested = moveRequested;
    }

    public boolean isAnimUpdateReady() {
        return animUpdateReady;
    }

    public void setAnimUpdateReady(boolean animUpdateReady) {
        this.animUpdateReady = animUpdateReady;
    }

    public boolean isDeathAnimForward() {
        return deathAnimForward;
    }

    public void setDeathAnimForward(boolean deathAnimForward) {
        this.deathAnimForward = deathAnimForward;
    }

    public int getMowerTimer() {
        return mowerTimer;
    }

    public void setMowerTimer(int mowerTimer) {
        this.mowerTimer = mowerTimer;
    }

    public int getBounceState() {
        return bounceState;
    }

    public void setBounceState(int bounceState) {
        this.bounceState = bounceState;
    }

    public int getBounceOffset() {
        return bounceOffset;
    }

    public void setBounceOffset(int bounceOffset) {
        this.bounceOffset = bounceOffset;
    }

    public int getSpecialState() {
        return specialState;
    }

    public void setSpecialState(int specialState) {
        this.specialState = specialState;
    }

    // ============================================================
    // Utility Methods
    // ============================================================

    /**
     * Get direction name as string.
     *
     * @param dir Direction constant
     * @return Human-readable direction name
     */
    public static String getDirectionName(int dir) {
        switch (dir) {
            case DIR_LEFT:    return "LEFT";
            case DIR_RIGHT:   return "RIGHT";
            case DIR_UP:      return "UP";
            case DIR_DOWN:    return "DOWN";
            case DIR_DEAD:    return "DEAD";
            case DIR_GLIDING: return "GLIDING";
            case DIR_FALLING: return "FALLING";
            default:          return "UNKNOWN(" + dir + ")";
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "pixelX=" + pixelX +
                ", pixelY=" + pixelY +
                ", tileX=" + tileX +
                ", tileY=" + tileY +
                ", direction=" + getDirectionName(direction) +
                ", moveProgress=" + moveProgress +
                ", animFrame=" + animFrame +
                ", isDragging=" + isDragging +
                ", isFlying=" + isFlying +
                ", isDying=" + isDying +
                '}';
    }
}
