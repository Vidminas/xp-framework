package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

import intermediary.Settings;
import logic.Boy;
import logic.World;

// PlayPanel - the playable area of the game
public class PlayPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private Boy boy;

    public PlayPanel() {
        this.setSize(Settings.WINDOW_WIDTH, Settings.PLAY_PANEL_HEIGHT);
        // Set a random background colour to distinguish the play panel from the
        // rest
        // This will be invisible when the map background image is drawn over it
        this.setBackground(Color.DARK_GRAY);

        // Set no layouts
        this.setLayout(null);

        // Double buffering should improve animations
        // Read more about how double buffering works at
        // http://www.anandtech.com/show/2794/2
        this.setDoubleBuffered(true);
    }

    // function called by the GameManager to add the boy (protagonist) to the
    // play panel at runtime
    // the PlayPanel needs a reference to the boy since he's drawn a LOT of
    // times
    public void addBoy(Boy boy) {
        this.boy = boy;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Use anti-aliasing to draw smoother images and lines
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the game background
        g2.drawImage(World.backgroundImage, 0, -Settings.TILE_SIZE, Settings.WINDOW_WIDTH, Settings.PLAY_PANEL_HEIGHT, null);

        // Draw the game map
        for (int i = 0; i < World.rows; ++i) {
            for (int j = 0; j < World.cols; ++j) {
                if (!World.map[i][j].empty()) {
                    g2.drawImage(World.map[i][j].getImage(), j * Settings.TILE_SIZE, i * Settings.TILE_SIZE, null);
                }
            }
        }

        // Draw the protagonist of the game
        if (boy != null && !boy.getRestoring()) {
            g2.drawImage(boy.getCurrentFrame(), boy.getCurrentX(), boy.getCurrentY(), null);
            // g2.draw(boy.getBoundingBox());
        }
    }
}
