package intermediary;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import logic.Boy;
import logic.KeyboardController;
import logic.World;
import gui.GamePanel;

// The GameManager is the main thread of the game
// It redraws game window components when necessary
// and manages pressed keys, associating them to actual actions 
public class GameManager extends Thread {
    // Main sleep time of the GameManager thread - in this case
    // The gameManager does all it has to do and then waits for 16ms
    // Before starting once again
    private static final int MAIN_SLEEP_TIME = 16;
    
    private Boy boy;
    private World world;
    private GamePanel gamePanel;
    
    private boolean gameIsRunning;
    private int currentLevel = 1;
    private int lastLevel = 2;
  
    private boolean isLastLevel() {
		  return (currentLevel >= lastLevel);
	  }
  
	public GameManager(GamePanel gamePanel) {
	    this.world = new World();
	    this.boy = new Boy();
	    this.gamePanel = gamePanel;
        this.gamePanel.addBoy(boy);
	    
        try {
            this.world.loadLevel(currentLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		// While the game is playing, gameIsRunning is set to true
		// Can be used to implement pausing in the game
		this.gameIsRunning = true;
	}
	
	@Override
	public void run() {
		while (gameIsRunning) {
		    if (boy.outOfBounds()) {
                try {
                    currentLevel++;
                    world.loadLevel(currentLevel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                boy.resetPosition();
            }

            boy.handleFalling();
            boy.handleJumping();
            manageKeys();
            boy.checkRestoringCount();
			
			gamePanel.repaintGame();
			
			try {
				Thread.sleep(MAIN_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// The function manages the keys currently pressed associating concrete
	// Actions to them
	private void manageKeys() {
		// Get the currently pressed keys from the KeyboardController
		HashSet<Integer> currentKeys=KeyboardController.getActiveKeys();
		
		// If ESC is pressed - close the game
		if (currentKeys.contains(KeyEvent.VK_ESCAPE)) {
		    System.exit(0);
		}
		
		// If right arrow is pressed - move the boy right
		if (currentKeys.contains(KeyEvent.VK_RIGHT)){
			boy.moveRight(isLastLevel());
		}
		
		// If left arrow is pressed - move the boy left
		if (currentKeys.contains(KeyEvent.VK_LEFT)){
			boy.moveLeft(isLastLevel());
		}
		
		// If spacebar is pressed - make the boy jump
        if (currentKeys.contains(KeyEvent.VK_SPACE)) {
            boy.startJumping();
        }
		
		// If the player is not pressing any keys, make the boy stand still
		else if (currentKeys.isEmpty()
		    && !boy.getJumping()
		    && !boy.getFalling()) {
			boy.stop();
		}
	}

	public Boy getBoy(){
		return boy;
	}
}
