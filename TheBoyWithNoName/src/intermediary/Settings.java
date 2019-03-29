package intermediary;

public class Settings {
    /* Game Frame properties */
    public static final int    WINDOW_WIDTH  = 1280;
    public static final int    WINDOW_HEIGHT = 640;
    public static final String WINDOW_TITLE  = "The Boy With No Name";

    /* Panel properties */
    // Height of the terrain in pixels = the distance of the boy's feet
    // From the bottom border of the game window
    public static final int TERRAIN_HEIGHT     = 192;
    public static final int PLAY_PANEL_HEIGHT  = 600;
    public static final int STATS_PANEL_HEIGHT = 40;

    /* Game properties */
    public static final int TILE_SIZE   = 64;
    public static final int FIRST_LEVEL = 1;
    public static final int LAST_LEVEL  = 2;

    // Number of the run animation frames of the player in the spritesheet
    public static final int BOY_RUN_FRAMES = 6;

    // Dimensions of the boy sprite
    public static final int BOY_SPRITE_WIDTH  = 40;
    public static final int BOY_SPRITE_HEIGHT = 64;

    /* Asset paths */
    public static final String playerSpritesheet = "/images/player.png";
    public static final String tileset           = "/levels/tileset.txt";

    public static String tileImage(String imageFile) {
        if (imageFile.startsWith("../")) {
            return imageFile.substring(2);
        }
        
        return ("/images/" + imageFile);
    }

    public static String levelBackgroundImage(int level) {
        return ("/images/background" + String.valueOf(level) + ".png");
    }

    public static String levelMap(int level) {
        return ("/levels/level" + String.valueOf(level) + ".txt");
    }
}
