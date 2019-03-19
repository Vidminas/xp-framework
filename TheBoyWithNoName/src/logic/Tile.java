package logic;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import intermediary.Settings;

// A tile is an element of the tileset
// Only one object per each kind of tile is created
// It contains the properties that all tiles of a particular kind share
// Such as the name, category type, description and image
public class Tile {
    private String name;
    private String type;
    private String info;
    private Image image;
    
    public Tile(String imageFile, String name, String type, String info) {
        this.name = name;
        this.type = type;
        this.info = info;
        
        try {
            this.image = ImageIO.read(getClass().getResource(Settings.tileImage(imageFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.image = image.getScaledInstance(Settings.TILE_SIZE,
                                             Settings.TILE_SIZE,
                                             BufferedImage.SCALE_SMOOTH);
    }
    
    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }
}
