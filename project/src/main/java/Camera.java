/**
 * Camera.java
 *
 * Manages camera position, viewport, and coordinate transformations for the game.
 * Extracted from the original a.java deobfuscation analysis.
 *
 * Original field mappings:
 * - bz -> cameraX (camera X offset in pixels)
 * - bA -> cameraY (camera Y offset in pixels)
 * - bB -> maxCameraX (maximum camera X position)
 * - bC -> maxCameraY (maximum camera Y position)
 * - i -> screenWidth
 * - j -> screenHeight
 * - k -> playAreaHeight (screenHeight - 2 - 12 - 6, accounting for HUD)
 * - bV -> transitionEffect (transition type: -4 to 2)
 * - bT -> transitionProgress (animation progress for transitions)
 */
public class Camera {

    // Screen dimensions
    private int screenWidth;
    private int screenHeight;
    private int playAreaHeight;

    // Camera position (viewport offset in world coordinates)
    private int cameraX;
    private int cameraY;

    // Camera bounds (maximum scrollable area)
    private int maxCameraX;
    private int maxCameraY;

    // Transition effects
    public static final int TRANSITION_NONE = 0;
    public static final int TRANSITION_FADE_OUT_RIGHT = 1;  // Wipe from left to right
    public static final int TRANSITION_FADE_OUT_LEFT = 2;   // Wipe from right to left
    public static final int TRANSITION_INIT = -1;           // Initial transition state
    public static final int TRANSITION_LOGO_TO_TITLE = -2; // Logo fading to title
    public static final int TRANSITION_TITLE_WAIT = -3;     // Title screen waiting
    public static final int TRANSITION_COMPLETE = -4;       // Transition complete

    private int transitionEffect;
    private int transitionProgress;

    /**
     * Default constructor.
     */
    public Camera() {
        this.transitionEffect = TRANSITION_NONE;
        this.transitionProgress = 0;
    }

    /**
     * Initialize camera with screen dimensions.
     *
     * @param width Screen width in pixels
     * @param height Screen height in pixels
     */
    public void initialize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.playAreaHeight = height - 2 - 12 - 6; // Reserve space for HUD
        this.cameraX = 0;
        this.cameraY = 0;
        this.maxCameraX = 0;
        this.maxCameraY = 0;
    }

    /**
     * Reset camera position to origin.
     */
    public void reset() {
        this.cameraX = 0;
        this.cameraY = 0;
    }

    /**
     * Set camera bounds based on world size.
     *
     * @param worldWidth Total world width in pixels
     * @param worldHeight Total world height in pixels
     */
    public void setBounds(int worldWidth, int worldHeight) {
        this.maxCameraX = worldWidth - screenWidth;
        this.maxCameraY = worldHeight - screenHeight;

        if (this.maxCameraX < 0) {
            this.maxCameraX = 0;
        }
        if (this.maxCameraY < 0) {
            this.maxCameraY = 0;
        }
    }

    /**
     * Update camera position to follow a target.
     * Clamps position to valid bounds.
     *
     * @param targetX Target X position in world coordinates
     * @param targetY Target Y position in world coordinates
     */
    public void updateCamera(int targetX, int targetY) {
        // Center camera on target
        int newCameraX = targetX - screenWidth / 2;
        int newCameraY = targetY - screenHeight / 2;

        // Clamp to bounds
        setPosition(newCameraX, newCameraY);
    }

    /**
     * Set camera position directly with bounds checking.
     *
     * @param x Camera X offset
     * @param y Camera Y offset
     */
    public void setPosition(int x, int y) {
        this.cameraX = x;
        this.cameraY = y;

        if (this.cameraX < 0) {
            this.cameraX = 0;
        } else if (this.cameraX > maxCameraX) {
            this.cameraX = maxCameraX;
        }

        if (this.cameraY < 0) {
            this.cameraY = 0;
        } else if (this.cameraY > maxCameraY) {
            this.cameraY = maxCameraY;
        }
    }

    /**
     * Move camera by delta values.
     *
     * @param deltaX X offset to add
     * @param deltaY Y offset to add
     */
    public void move(int deltaX, int deltaY) {
        setPosition(this.cameraX + deltaX, this.cameraY + deltaY);
    }

    /**
     * Convert world coordinates to screen coordinates.
     *
     * @param worldX X position in world space
     * @param worldY Y position in world space
     * @return int[2] containing screenX and screenY
     */
    public int[] worldToScreen(int worldX, int worldY) {
        return new int[] { worldX - cameraX, worldY - cameraY };
    }

    /**
     * Convert screen coordinates to world coordinates.
     *
     * @param screenX X position in screen space
     * @param screenY Y position in screen space
     * @return int[2] containing worldX and worldY
     */
    public int[] screenToWorld(int screenX, int screenY) {
        return new int[] { screenX + cameraX, screenY + cameraY };
    }

    /**
     * Convert world X to screen X.
     *
     * @param worldX X position in world space
     * @return X position in screen space
     */
    public int worldToScreenX(int worldX) {
        return worldX - cameraX;
    }

    /**
     * Convert world Y to screen Y.
     *
     * @param worldY Y position in world space
     * @return Y position in screen space
     */
    public int worldToScreenY(int worldY) {
        return worldY - cameraY;
    }

    /**
     * Convert screen X to world X.
     *
     * @param screenX X position in screen space
     * @return X position in world space
     */
    public int screenToWorldX(int screenX) {
        return screenX + cameraX;
    }

    /**
     * Convert screen Y to world Y.
     *
     * @param screenY Y position in screen space
     * @return Y position in world space
     */
    public int screenToWorldY(int screenY) {
        return screenY + cameraY;
    }

    /**
     * Check if a world position is visible on screen.
     *
     * @param worldX X position in world space
     * @param worldY Y position in world space
     * @param width Width of the object
     * @param height Height of the object
     * @return true if the object is at least partially visible
     */
    public boolean isVisible(int worldX, int worldY, int width, int height) {
        int screenX = worldToScreenX(worldX);
        int screenY = worldToScreenY(worldY);

        return screenX + width > 0 && screenX < screenWidth &&
               screenY + height > 0 && screenY < screenHeight;
    }

    /**
     * Start a transition effect.
     *
     * @param effectType Transition type constant
     */
    public void startTransition(int effectType) {
        this.transitionEffect = effectType;
        this.transitionProgress = 0;
    }

    /**
     * Update transition animation.
     * Increases transition progress by 2 each frame.
     */
    public void updateTransition() {
        this.transitionProgress += 2;
    }

    /**
     * Check if a transition is currently active.
     *
     * @return true if transition effect is active
     */
    public boolean isTransitionActive() {
        return transitionEffect > TRANSITION_NONE;
    }

    /**
     * Check if transition is in special state (logo, title screens).
     *
     * @return true if in special transition state
     */
    public boolean isSpecialTransition() {
        return transitionEffect < TRANSITION_NONE;
    }

    /**
     * Reset transition state.
     */
    public void clearTransition() {
        this.transitionEffect = TRANSITION_NONE;
        this.transitionProgress = 0;
    }

    /**
     * Get the tile column range visible on screen.
     * Useful for tile-based rendering optimization.
     *
     * @param tileSize Size of each tile in pixels
     * @return int[2] containing start and end tile columns (inclusive)
     */
    public int[] getVisibleTileRangeX(int tileSize) {
        int startTile = cameraX / tileSize;
        int endTile = (cameraX + screenWidth + tileSize - 1) / tileSize;
        return new int[] { startTile, endTile };
    }

    /**
     * Get the tile row range visible on screen.
     *
     * @param tileSize Size of each tile in pixels
     * @return int[2] containing start and end tile rows (inclusive)
     */
    public int[] getVisibleTileRangeY(int tileSize) {
        int startTile = cameraY / tileSize;
        int endTile = (cameraY + screenHeight + tileSize - 1) / tileSize;
        return new int[] { startTile, endTile };
    }

    // Getters and Setters

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getPlayAreaHeight() {
        return playAreaHeight;
    }

    public int getCameraX() {
        return cameraX;
    }

    public void setCameraX(int cameraX) {
        this.cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
    }

    public int getCameraY() {
        return cameraY;
    }

    public void setCameraY(int cameraY) {
        this.cameraY = Math.max(0, Math.min(cameraY, maxCameraY));
    }

    public int getMaxCameraX() {
        return maxCameraX;
    }

    public int getMaxCameraY() {
        return maxCameraY;
    }

    public int getTransitionEffect() {
        return transitionEffect;
    }

    public void setTransitionEffect(int transitionEffect) {
        this.transitionEffect = transitionEffect;
    }

    public int getTransitionProgress() {
        return transitionProgress;
    }

    public void setTransitionProgress(int transitionProgress) {
        this.transitionProgress = transitionProgress;
    }
}