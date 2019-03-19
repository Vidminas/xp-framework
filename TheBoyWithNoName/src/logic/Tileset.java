package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import intermediary.Settings;

// The tileset contains a HashMap of all possible tiles in the game
// It is used to draw the game tiles on the screen
public class Tileset {
    // The key of each element is an integer ID of the tile
    private HashMap<Integer, Tile> tiles;
    
    public Tileset() {
        tiles = new HashMap<>();
        tiles.put(0, null);
        loadTilesFromDisk();
    }
    
    public void addTile(int id, String imageFile, String name, String type, String info) {
        Tile tile = new Tile(imageFile, name, type, info);
        tiles.put(id, tile);
    }
    
    public Tile getTile(int id) {
        return tiles.get(id);
    }
    
    public void loadTilesFromDisk() {
        // Load the tileset file
        InputStream is = getClass().getResourceAsStream(Settings.tileset);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        
        String line = null;
        String[] tileProperties;
        try {
            // Read the tileset file line-by-line
            while ((line = reader.readLine()) != null) {
                // Ignore lines beginning with # - they are comments
                if (line.charAt(0) == '#')
                    continue;
                
                // Each tile has 5 comma-separated properties (some empty)
                // 1. The tile number (maps refer to this)
                // 2. Tile image file name
                // 3. Tile name
                // 4. Tile type
                // 5. Any extended info / comments
                tileProperties = line.split(", ", 5);
                addTile(Integer.parseInt(tileProperties[0]),
                                         tileProperties[1],
                                         tileProperties[2],
                                         tileProperties[3],
                                         tileProperties[4]);
            }
            
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
