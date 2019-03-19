package logic;

import java.awt.Image;
import java.awt.Rectangle;
import intermediary.Settings;

// Blocks are all those tiles that you can walk on and collide against
// They do not entail any kind of interaction
public class Block {
    private int row;
    private int col;
    private Tile tile;
    private Rectangle boundingBox;

    public Block(int row, int col, Tile tile) {
        this.row = row * Settings.TILE_SIZE;
        this.col = col * Settings.TILE_SIZE;
        this.tile = tile;
        this.boundingBox = new Rectangle(col * Settings.TILE_SIZE,
                                         row * Settings.TILE_SIZE,
                                         Settings.TILE_SIZE,
                                         Settings.TILE_SIZE);
    }
    
    public boolean empty() {
        return tile == null;
    }
    
    public String getName() {
        return tile.getName();
    }
    
    public Image getImage() {
        return tile.getImage();
    }
    
    public boolean intersects(Rectangle boundingBox) {
        return this.boundingBox.intersects(boundingBox);
    }
    
    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}