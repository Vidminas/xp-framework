package gui;

import java.awt.Color;

import javax.swing.JPanel;

import logic.Boy;
import logic.KeyboardController;

// The game panel, which is a container for all the other panels
// It has little behaviour of its own
// Only passing data between the game logic and the panels it contains
public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private KeyboardController keyboardController = new KeyboardController();
    private StatsPanel statsPanel = new StatsPanel();
    private PlayPanel playPanel = new PlayPanel();
	
	public GamePanel() {
		this.setRequestFocusEnabled(true);
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(null);
		this.setBackground(Color.BLACK);

		this.add(statsPanel);
		statsPanel.setLocation(0, 0);

		this.add(playPanel);
		playPanel.setLocation(0, StatsPanel.STATS_HEIGHT);
		
		this.addKeyListener(keyboardController);
	}
	
	public void addBoy(Boy boy) {
		playPanel.addBoy(boy);
		statsPanel.addBoy(boy);
	}
	
	public void repaintGame(){
		playPanel.repaint();
		statsPanel.repaint();
	}
}
