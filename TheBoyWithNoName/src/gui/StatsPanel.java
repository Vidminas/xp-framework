package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import intermediary.Settings;
import logic.Boy;

public class StatsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private static final int LEFT_MARGIN = 5;

    private static final int HEARTS_X_DISTANCE = 60;
    private static final int HEARTS_START_X    = 84;
    private static final int HEARTS_START_Y    = 4;
    private static final int HEARTS_SIZE       = 32;

    private BufferedImage livingHeart;
    private BufferedImage deadHeart;
    private BufferedImage background;

    private Boy boy;

    public StatsPanel() {
        this.setSize(Settings.WINDOW_WIDTH, Settings.STATS_PANEL_HEIGHT);
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        try {
            background  = ImageIO.read(getClass().getResource("/images/statsBar.png"));
            livingHeart = ImageIO.read(getClass().getResource("/images/livingHeart.png"));
            deadHeart   = ImageIO.read(getClass().getResource("/images/deadHeart.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBoy(Boy boy) {
        this.boy = boy;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(background, 0, 0, Settings.WINDOW_WIDTH - LEFT_MARGIN, Settings.STATS_PANEL_HEIGHT, null);

        if (boy != null) {
            for (int i = 0; i < boy.MAX_LIFE; ++i) {
                if (boy.getLife() > i) {
                    g2.drawImage(livingHeart, HEARTS_START_X + HEARTS_X_DISTANCE * i, HEARTS_START_Y, HEARTS_SIZE, HEARTS_SIZE, null);
                } else {
                    g2.drawImage(deadHeart, HEARTS_START_X + HEARTS_X_DISTANCE * i, HEARTS_START_Y, HEARTS_SIZE, HEARTS_SIZE, null);
                }
            }
        }
    }
}
