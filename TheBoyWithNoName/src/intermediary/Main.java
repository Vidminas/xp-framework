package intermediary;

import gui.GameFrame;
import gui.GamePanel;

public class Main {
	public static void main(String[] args) {
		// Initialise all the game components and start playing
		GamePanel gamePanel = new GamePanel();
		GameFrame gameFrame = new GameFrame(gamePanel);
        GameManager gameManager = new GameManager(gamePanel);
		
		gameManager.start();
	}
}
