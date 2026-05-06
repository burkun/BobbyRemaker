import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * GameRenderer - Handles all drawing and rendering operations for Bobby Carrot 5.
 *
 * This class encapsulates the rendering logic extracted from the obfuscated a.java class.
 * It manages image resources and provides methods for drawing tiles, sprites, HUD elements,
 * menus, and transition effects.
 */
public class GameRenderer {

    // ========================================
    // Image Resources
    // ========================================

    /** Font image for text rendering (8x12 character cells) */
    private Image imgFont;           // ca

    /** Numbers image for HUD display */
    private Image imgNumbers;        // cb

    /** Arrow icons for navigation indicators */
    private Image imgArrows;         // cc

    /** Main spritesheet for HUD elements, icons, and UI components */
    private Image imgSpritesheet;    // cd

    /** Miscellaneous graphics (flags, icons, etc.) */
    private Image imgMisc;           // ce

    /** Tile set image for game terrain (32x32 tiles) */
    private Image imgTiles;          // cf

    /** Lawn mower animation sprites */
    private Image imgMower;          // cg

    /** Butterfly sprite image */
    private Image imgButterfly;      // ch

    /** Alarm/warning overlay image */
    private Image imgAlarm;          // ci

    /** Array of sprite images for player and NPCs (b0-b9.png) */
    private Image[] spriteImages;    // cj[10]

    /** Additional tile/animation set */
    private Image imgTilesAlt;       // ck

    /** Logo image for title screen */
    private Image imgLogo;           // dC

    /** Title background image */
    private Image imgTitle;          // dD

    /** Background image for parallax scrolling */
    private Image imgBackground;     // dA

    // ========================================
    // Screen Dimensions
    // ========================================

    /** Screen width in pixels */
    private int screenWidth;         // i

    /** Screen height in pixels */
    private int screenHeight;        // j

    /** Play area height (screen height minus HUD area) */
    private int playAreaHeight;      // k

    // ========================================
    // Rendering State
    // ========================================

    /** Camera X offset for scrolling */
    private int cameraX;             // bz

    /** Camera Y offset for scrolling */
    private int cameraY;             // bA

    /** Transition effect type (0=none, 1=open, 2=close) */
    private byte transitionEffect;   // bV

    /** Transition progress counter */
    private int transitionProgress;  // bT

    // ========================================
    // Offscreen Buffer
    // ========================================

    /** Offscreen image for double buffering */
    private Image offscreenImage;    // dA equivalent for buffer

    /** Offscreen graphics context */
    private Graphics offscreenGraphics; // dB

    // ========================================
    // Constructors
    // ========================================

    /**
     * Creates a new GameRenderer with specified screen dimensions.
     *
     * @param width Screen width in pixels
     * @param height Screen height in pixels
     */
    public GameRenderer(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.playAreaHeight = height;
        this.spriteImages = new Image[10];
    }

    // ========================================
    // Image Resource Management
    // ========================================

    /**
     * Loads an image from resource path, reusing existing image if available.
     *
     * @param existingImage Existing image to reuse, or null
     * @param resourcePath Path to image resource (e.g., "/b0.png")
     * @return Loaded or reused Image object
     */
    public Image loadImage(Image existingImage, String resourcePath) {
        if (existingImage != null) {
            return existingImage;
        }
        try {
            return Image.createImage(resourcePath);
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Loads all sprite images (b0.png through b9.png).
     */
    public void loadSpriteImages() {
        for (int i = 0; i < spriteImages.length; i++) {
            spriteImages[i] = loadImage(spriteImages[i], "/b" + i + ".png");
        }
        imgTilesAlt = loadImage(imgTilesAlt, "/ta.png");
        imgSpritesheet = loadImage(imgSpritesheet, "/hud.png");
        imgButterfly = loadImage(imgButterfly, "/bf.png");
        imgAlarm = loadImage(imgAlarm, "/alarm.png");
        System.gc();
    }

    /**
     * Releases sprite image resources.
     *
     * @param partial If true, keep image 9 for special use
     */
    public void releaseSpriteImages(boolean partial) {
        int limit = partial ? 9 : 10;
        for (int i = 0; i < limit; i++) {
            spriteImages[i] = null;
        }
        if (!partial) {
            imgTilesAlt = null;
        }
        imgButterfly = null;
        imgAlarm = null;
    }

    // ========================================
    // Core Drawing Methods
    // ========================================

    /**
     * Draws a region from an image to the screen.
     * This is the fundamental drawing method used throughout the game.
     *
     * @param g Graphics context
     * @param image Source image
     * @param srcX Source X coordinate in image
     * @param srcY Source Y coordinate in image
     * @param width Width of region to draw
     * @param height Height of region to draw
     * @param destX Destination X coordinate on screen
     * @param destY Destination Y coordinate on screen
     */
    public void drawImageRegion(Graphics g, Image image, int srcX, int srcY,
                                 int width, int height, int destX, int destY) {
        g.setClip(destX, destY, width, height);
        g.drawImage(image, destX - srcX, destY - srcY, Graphics.TOP | Graphics.LEFT);
    }

    /**
     * Draws an image region with horizontal wrapping for scrolling backgrounds.
     *
     * @param g Graphics context
     * @param image Source image
     * @param srcX Source X coordinate
     * @param srcY Source Y coordinate
     * @param width Width of region
     * @param height Height of region
     * @param destX Destination X coordinate
     * @param destY Destination Y coordinate
     */
    public void drawImageRegionWrapped(Graphics g, Image image, int srcX, int srcY,
                                        int width, int height, int destX, int destY) {
        drawImageRegion(g, image, srcX, srcY, width, height, destX, destY);
        // Draw wrapped portion if needed
        if (srcX + width < screenWidth) {
            drawImageRegion(g, image, srcX, srcY, width, height, 256 + destX, destY);
        }
    }

    /**
     * Draws the parallax scrolling background.
     *
     * @param g Graphics context
     * @param cameraX Camera X offset
     * @param cameraY Camera Y offset
     */
    public void drawTileLayer(Graphics g, int cameraX, int cameraY) {
        if (imgBackground == null) {
            return;
        }

        boolean wrapX = false;
        boolean wrapY = false;

        int offsetX = cameraX % 256; // Assuming 256 is the bg width
        int offsetY = cameraY % 256; // Assuming 256 is the bg height

        // Check if wrapping is needed
        if (offsetX + screenWidth > 256) {
            wrapX = true;
        }
        if (offsetY + screenHeight > 256) {
            wrapY = true;
        }

        // Draw main background
        g.drawImage(imgBackground, -offsetX, -offsetY, Graphics.TOP | Graphics.LEFT);

        // Draw wrapped portions
        if (wrapX) {
            g.drawImage(imgBackground, 256 - offsetX, -offsetY, Graphics.TOP | Graphics.LEFT);
        }
        if (wrapY) {
            g.drawImage(imgBackground, -offsetX, 256 - offsetY, Graphics.TOP | Graphics.LEFT);
        }
        if (wrapX && wrapY) {
            g.drawImage(imgBackground, 256 - offsetX, 256 - offsetY, Graphics.TOP | Graphics.LEFT);
        }
    }

    // ========================================
    // Sprite Drawing Methods
    // ========================================

    /**
     * Draws a sprite at the specified position.
     *
     * @param g Graphics context
     * @param largeSprite Whether to use large sprite set (32x32 vs 16x16)
     * @param spriteIndex Index into the tile/sile sprite sheet
     * @param x Screen X coordinate
     * @param y Screen Y coordinate
     */
    public void drawSprite(Graphics g, boolean largeSprite, byte spriteIndex, int x, int y) {
        int shift = largeSprite ? 4 : 2;
        int row = (spriteIndex & 0xFF) >> shift;
        int srcY = row << 5;
        int srcX = (spriteIndex & 0xFF) - (row << shift) << 5;

        Image spriteSheet = largeSprite ? imgTiles : imgTilesAlt;
        drawImageRegion(g, spriteSheet, srcX, srcY, 32, 32, x, y);
    }

    /**
     * Draws the player character sprite.
     *
     * @param g Graphics context
     * @param direction Player direction (0=left, 1=right, 2=up, 3=down, 4=dead, 5=gliding)
     * @param animFrame Current animation frame
     * @param x Screen X coordinate
     * @param y Screen Y coordinate
     * @param cameraX Camera X offset
     * @param cameraY Camera Y offset
     * @param isGliding Whether player is in gliding state
     * @param isRiding Whether player is riding lawn mower
     * @param pushForce Current push force for animation
     * @param verticalOffset Vertical offset for jump animation
     */
    public void drawPlayer(Graphics g, int direction, int animFrame, int x, int y,
                           int cameraX, int cameraY, boolean isGliding, boolean isRiding,
                           int pushForce, int verticalOffset) {
        int screenX = x - cameraX;
        int screenY = y - cameraY;

        if (isGliding) {
            drawGlidingPlayer(g, direction, animFrame, screenX, screenY, pushForce);
        } else if (isRiding) {
            drawRidingPlayer(g, direction, animFrame, screenX, screenY, verticalOffset);
        } else {
            drawNormalPlayer(g, direction, animFrame, screenX, screenY, pushForce, verticalOffset);
        }
    }

    /**
     * Draws player in gliding (flying) state.
     */
    private void drawGlidingPlayer(Graphics g, int direction, int animFrame,
                                    int x, int y, int pushForce) {
        int srcX, srcY;
        int width = 40;
        int offsetX = -4;
        int offsetY = 0;
        int shadowOffsetX = 32;

        switch (direction) {
            case 0: // Left
                srcY = 0;
                srcX = 40;
                shadowOffsetX = -32;
                break;
            case 1: // Right
                srcY = 40;
                srcX = 40;
                shadowOffsetX = -32;
                break;
            case 2: // Up
                srcY = 0;
                srcX = 32;
                break;
            default: // Down or other
                srcY = 32;
                srcX = 80;
                break;
        }

        int spriteHeight = 56;
        int spriteOffsetY = -32;

        // Draw shadow if moving
        if (pushForce > 0) {
            drawImageRegion(g, spriteImages[7], srcX, animFrame * spriteHeight,
                           width, spriteHeight, x + offsetX, y + spriteOffsetY);
        }

        drawImageRegion(g, spriteImages[7], srcX, animFrame * spriteHeight,
                       width, spriteHeight, x + offsetX - shadowOffsetX, y + spriteOffsetY);
    }

    /**
     * Draws player riding lawn mower.
     */
    private void drawRidingPlayer(Graphics g, int direction, int animFrame,
                                   int x, int y, int verticalOffset) {
        int srcX;
        switch (direction) {
            case 0: srcX = 0; break;
            case 1: srcX = 80; break;
            case 2: srcX = 160; break;
            default: srcX = 240; break;
        }

        drawImageRegion(g, spriteImages[9], srcX, 0, 80, 48,
                       x - 24, y - 24 - verticalOffset);
    }

    /**
     * Draws player in normal walking state.
     */
    private void drawNormalPlayer(Graphics g, int direction, int animFrame,
                                   int x, int y, int pushForce, int verticalOffset) {
        int width = 32;
        int height = 48;
        int offsetX = 0;
        int offsetY = -24;

        int srcX = animFrame * width;
        int screenX = x + offsetX;
        int screenY = y + offsetY - verticalOffset;

        int spriteIndex = direction;
        if (pushForce > 0) {
            // Use pushing animation
        }

        drawImageRegion(g, spriteImages[spriteIndex], srcX, 0, width, height,
                       screenX, screenY);
    }

    /**
     * Draws the lawn mower attachment for player.
     *
     * @param g Graphics context
     * @param animFrame Animation frame for mower
     * @param x Screen X coordinate
     * @param y Screen Y coordinate
     * @param direction Player direction
     * @param cameraX Camera X offset
     * @param cameraY Camera Y offset
     */
    public void drawMower(Graphics g, int animFrame, int x, int y, int direction,
                          int cameraX, int cameraY) {
        int screenX = x - cameraX;
        int screenY = y - 8 - cameraY;

        int srcX = animFrame / 3 << 5;
        int srcY = (direction == 2) ? 0 : 32; // Up vs other directions

        drawImageRegion(g, imgMower, srcX, srcY, 32, 32, screenX, screenY);
    }

    // ========================================
    // HUD Rendering Methods
    // ========================================

    /**
     * Renders the HUD (Heads-Up Display) showing time, score, and collectibles.
     *
     * @param g Graphics context
     * @param timeRemaining Remaining time in milliseconds
     * @param carrotsCollected Number of carrots collected
     * @param coins Number of coins
     * @param showTimeIcon Whether to show time display
     * @param isTimedLevel Whether this is a timed level
     */
    public void renderHUD(Graphics g, long timeRemaining, int carrotsCollected,
                          int coins, boolean showTimeIcon, boolean isTimedLevel) {
        int x = 2;
        int y = 2;

        if (showTimeIcon) {
            // Draw time display
            int minutes = (int) timeRemaining / 60000;
            long remainder = timeRemaining % 60000;

            drawNumber(g, x, y, minutes, 2);

            int seconds = (int) remainder / 1000;
            drawNumber(g, x + 32, y, seconds, 2);

            // Draw colon separator (blinking)
            if (seconds % 2 == 0) {
                g.setClip(x + 26, y, 5, 13);
                g.drawImage(imgNumbers, x + 26 - 120, y, Graphics.TOP | Graphics.LEFT);
            }

            // Draw carrot icon with count
            x = screenWidth - 17 - 2;
            int iconWidth = 17;
            g.setClip(x, y, iconWidth, 21);
            g.drawImage(imgSpritesheet, x - 153, y, Graphics.TOP | Graphics.LEFT);

            drawNumber(g, x - 28, y + (21 - 13 >> 1), carrotsCollected, 2);
        }
    }

    /**
     * Draws a multi-digit number at the specified position.
     *
     * @param g Graphics context
     * @param x X coordinate
     * @param y Y coordinate
     * @param value Numeric value to display
     * @param digits Number of digits to display
     */
    public void drawNumber(Graphics g, int x, int y, int value, int digits) {
        int base = 10;
        int multiplier = 1;

        x += 13 * (digits - 1);

        for (int i = 0; i < digits; i++) {
            int digit = value % base / multiplier;
            g.setClip(x, y, 12, 13);
            g.drawImage(imgNumbers, x - digit * 12, y, Graphics.TOP | Graphics.LEFT);
            x -= 13;
            multiplier *= 10;
            base *= 10;
        }
    }

    // ========================================
    // Text Rendering Methods
    // ========================================

    /**
     * Draws a single character at the specified position.
     *
     * @param g Graphics context
     * @param x X coordinate
     * @param y Y coordinate
     * @param ch Character to draw
     */
    public void drawChar(Graphics g, int x, int y, char ch) {
        int charIndex = -1;
        int accent = -1;

        if (g != null) {
            g.setClip(x, y, 8, 12);
        }

        // Map character to index in font image
        if (ch >= '0' && ch <= '9') {
            charIndex = ch - 48;
        } else if (ch >= 'A' && ch <= 'Z') {
            charIndex = 10 + ch - 65;
        } else {
            switch (ch) {
                case '.': charIndex = 36; break;
                case ',': charIndex = 37; break;
                case '-': charIndex = 38; break;
                case ':': charIndex = 39; break;
                case '!': charIndex = 40; break;
                case '?': charIndex = 41; break;
                case '*': charIndex = 42; break;
                case '\'': charIndex = 43; break;
                case '©': charIndex = 44; break; // Copyright
                case '@': charIndex = 45; break;
                // Accented characters...
                default: return;
            }
        }

        if (g != null && charIndex >= 0) {
            int row = charIndex / 18;
            int col = charIndex % 18;
            g.drawImage(imgFont, x - col * 8, y - row * 12, Graphics.TOP | Graphics.LEFT);

            // Draw accent if needed
            if (accent != -1) {
                int accentRow = accent / 2;
                int accentCol = accent % 2;
                g.setClip(x, y + (accent != 5 ? -3 : 12), 8, 3);
                g.drawImage(imgFont, x - (128 + accentCol * 8),
                           y + (accent != 5 ? -3 : 12) - (24 + accentRow * 3),
                           Graphics.TOP | Graphics.LEFT);
            }
        }
    }

    /**
     * Draws a string of text at the specified position.
     *
     * @param text Text to draw
     * @param g Graphics context
     * @param x X coordinate (center point if centered)
     * @param y Y coordinate
     * @param centered Whether to center the text
     * @return The starting X coordinate of the drawn text
     */
    public int drawString(String text, Graphics g, int x, int y, boolean centered) {
        int length = text.length();
        int width = (length - 1) * 9 + 8;

        if (centered) {
            x -= width >> 1;
        }

        int startX = x;
        for (int i = 0; i < length; i++) {
            drawChar(g, x, y, text.charAt(i));
            x += 9;
        }

        return startX;
    }

    /**
     * Draws a text box with background.
     *
     * @param g Graphics context
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Box width
     * @param height Box height
     * @param borderColor Border color (RGB)
     * @param fillColor Fill color (RGB)
     */
    public void drawTextBox(Graphics g, int x, int y, int width, int height,
                            int borderColor, int fillColor) {
        g.setClip(x, y, width, height);
        g.setColor(fillColor);
        g.fillRect(x, y + 1, width, height - 2);
        g.fillRect(x + 1, y, width - 2, height);
        g.setColor(borderColor);
        g.fillRect(x + 3, y + 2, width - 6, height - 4);
        g.drawLine(x + 2, y + 3, x + 2, y + height - 4);
        g.drawLine(x + width - 3, y + 3, x + width - 3, y + height - 4);
    }

    // ========================================
    // Transition Effects
    // ========================================

    /**
     * Renders transition effect (screen wipe).
     *
     * @param g Graphics context
     */
    public void renderTransitionEffect(Graphics g) {
        int progress = transitionProgress;
        int tileHeight = (screenHeight + 32 - 1) >> 5;
        int tileWidth = (screenWidth + 32 - 1) >> 5;

        g.setClip(0, 0, screenWidth, screenHeight);
        g.setColor(0);

        switch (transitionEffect) {
            case 1: // Closing transition (fill from edges)
                renderClosingTransition(g, tileWidth, tileHeight, progress);
                break;

            case 2: // Opening transition (clear from edges)
                renderOpeningTransition(g, tileWidth, tileHeight, progress);
                break;
        }
    }

    /**
     * Renders closing transition effect.
     */
    private void renderClosingTransition(Graphics g, int tileWidth, int tileHeight, int progress) {
        int y = (tileHeight - 1) << 5;
        int x = 0;

        // Horizontal bars
        for (int i = 0; i < tileWidth; i++) {
            g.fillRect(x, 0, 32 - progress, screenHeight);
            if (progress - 2 < 0) {
                progress = 0;
            } else {
                progress -= 2;
            }
            x += 32;
        }

        // Vertical bars
        progress = transitionProgress;
        for (int i = 0; i < tileHeight; i++) {
            g.fillRect(0, y, screenWidth, 32 - progress);
            if (progress - 2 < 0) {
                progress = 0;
            } else {
                progress -= 2;
            }
            y -= 32;
        }

        if (progress >= 32 && transitionProgress >= 32) {
            transitionEffect = 0;
        }
    }

    /**
     * Renders opening transition effect.
     */
    private void renderOpeningTransition(Graphics g, int tileWidth, int tileHeight, int progress) {
        int y = 0;
        int x = (tileWidth - 1) << 5;

        // Horizontal bars from right
        for (int i = 0; i < tileWidth; i++) {
            g.fillRect(x, 0, progress, screenHeight);
            if (progress - 2 < 0) {
                progress = 0;
            } else {
                progress -= 2;
            }
            x -= 32;
        }

        // Vertical bars from top
        progress = transitionProgress;
        for (int i = 0; i < tileHeight; i++) {
            g.fillRect(0, y, screenWidth, progress);
            if (progress - 2 < 0) {
                progress = 0;
            } else {
                progress -= 2;
            }
            y += 32;
        }

        if (progress >= 32 && transitionProgress >= 32) {
            transitionEffect = 0;
        }
    }

    /**
     * Advances the transition animation.
     */
    public void advanceTransition() {
        transitionProgress += 2;
    }

    /**
     * Starts a transition effect.
     *
     * @param opening Whether this is an opening (true) or closing (false) transition
     * @param delay Delay before transition completes
     */
    public void startTransition(boolean opening, int delay) {
        transitionProgress = 0;
        transitionEffect = (byte) (opening ? 1 : 2);
    }

    // ========================================
    // Menu Rendering Methods
    // ========================================

    /**
     * Renders the main menu.
     *
     * @param g Graphics context
     * @param menuText Text to display
     * @param leftOption Left button text (or null)
     * @param rightOption Right button text (or null)
     * @param showArrows Whether to show navigation arrows
     */
    public void renderMenu(Graphics g, String menuText, String leftOption,
                           String rightOption, boolean showArrows) {
        int yOffset = 0;

        // Draw menu background bar
        g.setClip(0, 0, screenWidth, screenHeight);
        g.setColor(22935);
        g.fillRect(0, playAreaHeight + 2 + yOffset, screenWidth, 18);

        // Draw left option
        if (leftOption != null) {
            drawString(leftOption, g, 3, playAreaHeight + 2 + 3 + yOffset, false);
        }

        // Draw right option
        if (rightOption != null) {
            int textWidth = (rightOption.length() - 1) * 9 + 8;
            drawString(rightOption, g, screenWidth - textWidth - 3,
                      playAreaHeight + 2 + 3 + yOffset, false);
        }
    }

    /**
     * Renders overlay text (e.g., score, level name).
     *
     * @param g Graphics context
     * @param text Text to display
     * @param positionCode Position code (0=center, 1=top, 2=bottom)
     * @param yPosition Specific Y position override
     */
    public void renderOverlay(Graphics g, String text, byte positionCode, int yPosition) {
        int boxWidth = text.length() * 9 + 16;
        int boxHeight = 26;
        int x = screenWidth - boxWidth >> 1;
        int y;

        if (positionCode != 3) {
            y = positionCode == 0 ? playAreaHeight - boxHeight >> 1
                                  : (positionCode == 1 ? 34 : playAreaHeight - boxHeight - 5);
        } else {
            y = yPosition;
        }

        drawTextBox(g, x, y, boxWidth, boxHeight, 22935, 10370);
        drawString(text, g, screenWidth >> 1, y + 6, true);
    }

    /**
     * Renders the options menu.
     *
     * @param g Graphics context
     * @param options Array of option strings
     * @param selectedIndex Currently selected option index
     * @param scrollOffset Scroll offset for long lists
     * @param visibleCount Number of visible options
     */
    public void renderOptionsMenu(Graphics g, String[] options, int selectedIndex,
                                   int scrollOffset, int visibleCount) {
        // Draw selection highlight
        int y = 0;
        int itemHeight = 25;

        for (int i = 0; i < visibleCount && scrollOffset + i < options.length; i++) {
            int actualIndex = scrollOffset + i;
            boolean selected = (actualIndex == selectedIndex);

            if (selected) {
                drawTextBox(g, screenWidth - 100 >> 1, y, 100, 22, 41658, 10370);
            }

            drawString(options[actualIndex], g, screenWidth >> 1, y + 5, true);
            y += itemHeight;
        }
    }

    // ========================================
    // Special Effect Rendering
    // ========================================

    /**
     * Draws falling debris/particle effects.
     *
     * @param g Graphics context
     * @param cameraX Camera X offset
     * @param cameraY Camera Y offset
     * @param debrisX Array of debris X positions
     * @param debrisY Array of debris Y positions
     * @param debrisType Array of debris type indices
     */
    public void drawDebris(Graphics g, int cameraX, int cameraY,
                           short[] debrisX, short[] debrisY, byte[] debrisType) {
        for (int i = 0; i < debrisType.length; i++) {
            byte type = debrisType[i];
            if (type < 0) continue;

            int row = type / 5;
            int srcX = row * 12;
            int srcY = (type - row * 5) * 12;

            int screenX = debrisX[i] - cameraX - 6;
            int screenY = debrisY[i] - cameraY - 6;

            drawImageRegion(g, imgTilesAlt, 64 + srcX, 448 + srcY, 12, 12,
                           screenX, screenY);
        }
    }

    /**
     * Draws a warning/alarm overlay.
     *
     * @param g Graphics context
     * @param alarmFrame Current animation frame for alarm
     */
    public void drawAlarmOverlay(Graphics g, int alarmFrame) {
        if (alarmFrame != -1) {
            int x = screenWidth - 48 >> 1;
            int y = screenHeight - 48 >> 1;
            drawImageRegion(g, imgAlarm, alarmFrame * 48, 0, 48, 48, x, y);
        }
    }

    // ========================================
    // Getters and Setters
    // ========================================

    public void setScreenDimensions(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public void setPlayAreaHeight(int height) {
        this.playAreaHeight = height;
    }

    public void setCameraPosition(int x, int y) {
        this.cameraX = x;
        this.cameraY = y;
    }

    public void setOffscreenBuffer(Image image, Graphics g) {
        this.offscreenImage = image;
        this.offscreenGraphics = g;
    }

    public int getTransitionEffect() {
        return transitionEffect;
    }

    public int getTransitionProgress() {
        return transitionProgress;
    }

    // ========================================
    // Image Resource Getters
    // ========================================

    public Image getImgFont() { return imgFont; }
    public void setImgFont(Image img) { this.imgFont = img; }

    public Image getImgNumbers() { return imgNumbers; }
    public void setImgNumbers(Image img) { this.imgNumbers = img; }

    public Image getImgArrows() { return imgArrows; }
    public void setImgArrows(Image img) { this.imgArrows = img; }

    public Image getImgSpritesheet() { return imgSpritesheet; }
    public void setImgSpritesheet(Image img) { this.imgSpritesheet = img; }

    public Image getImgMisc() { return imgMisc; }
    public void setImgMisc(Image img) { this.imgMisc = img; }

    public Image getImgTiles() { return imgTiles; }
    public void setImgTiles(Image img) { this.imgTiles = img; }

    public Image getImgMower() { return imgMower; }
    public void setImgMower(Image img) { this.imgMower = img; }

    public Image getImgButterfly() { return imgButterfly; }
    public void setImgButterfly(Image img) { this.imgButterfly = img; }

    public Image getImgAlarm() { return imgAlarm; }
    public void setImgAlarm(Image img) { this.imgAlarm = img; }

    public Image[] getSpriteImages() { return spriteImages; }
    public void setSpriteImages(Image[] images) { this.spriteImages = images; }

    public Image getImgTilesAlt() { return imgTilesAlt; }
    public void setImgTilesAlt(Image img) { this.imgTilesAlt = img; }

    public Image getImgLogo() { return imgLogo; }
    public void setImgLogo(Image img) { this.imgLogo = img; }

    public Image getImgTitle() { return imgTitle; }
    public void setImgTitle(Image img) { this.imgTitle = img; }

    public Image getImgBackground() { return imgBackground; }
    public void setImgBackground(Image img) { this.imgBackground = img; }
}
