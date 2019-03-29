package logic;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import intermediary.Settings;

// The world contains everything about the playable part of the game
// Including the tileset, background, and game map for the current level
// It can also load new levels from game files
public class World {
    public static Tileset       tileset;
    public static BufferedImage backgroundImage;
    public static Block[][]     map;
    public static int           rows;
    public static int           cols;

    public World() {
        tileset = new Tileset();
    }

    public void loadLevel(int level) throws Exception {
        // Load a new background image for the level
        try {
            backgroundImage = ImageIO.read(getClass().getResource(Settings.levelBackgroundImage(level)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Open the level map data files
        InputStream is = getClass().getResourceAsStream(Settings.levelMap(level));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line = null;
        try {
            // Read the map file line-by-line
            if ((line = reader.readLine()) != null) {
                // The first line of a level file contains 3 configuration
                // parameters
                // 1. Map width
                // 2. Map height
                // 3. Tileset file name
                String[] config = line.split(" ", 3);

                // Create a new 2D map array with width * height dimensions
                cols = Integer.parseInt(config[0]);
                rows = Integer.parseInt(config[1]);
                map = new Block[rows][cols];

                // Since we are not using multiple tilesets, we ignore the last
                // parameter
            } else {
                throw new Exception("Tried to load level " + level + "but its map file was empty!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // The second line contains colour configuration
        // We ignore this and any other lines until the map data completely
        // The start of map data is preceded by a line with a single "."
        while (!line.equals(".")) {
            line = reader.readLine();
        }

        // The map data consists of 3 lines, each for a different layer
        // The first layer is terrain - blocks that cannot be walked through
        // The second layer is objects - things that can be walked through and
        // interacted with
        // These objects are displayed behind the player
        // The third layer is for objects to be displayed in front of the
        // player, such as lights

        // Currently only processing the first layer, hence z < 1
        for (int z = 0; z < 1; z++) {
            line = reader.readLine();

            if (line == null) {
                throw new Exception("The map file for level " + level + " did not contain layer data!");
            } else {
                StringTokenizer tokens = new StringTokenizer(line);

                for (int y = 0; y < rows; y++) {
                    for (int x = 0; x < cols; x++) {
                        int tileID = Integer.parseInt(tokens.nextToken());

                        Tile currentTile;

                        try {
                            currentTile = tileset.getTile(tileID);
                        } catch (NullPointerException e) {
                            throw new Exception("The map file for level " + level + " references non-existing tile with ID " + tileID);
                        }

                        map[y][x] = new Block(y, x, currentTile);
                    }
                }
            }
        }
        reader.close();
        is.close();
    }
}