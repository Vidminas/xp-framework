package intermediary;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import logic.Boy;
import logic.KeyboardController;
import logic.World;
import gui.GamePanel;

//the GameManager is the main thread of the game, it calls repaints 
//for the play panel and statsPanel when necessary and manages keys 
//pressed, associating them to actual actions 
public class GameManager extends Thread {
	public GameManager(GamePanel gamePanel){
	    this.world=new World();
        this.world.initializeStage(currentLevel);
        
		//initialize the protagonist of the game
		this.boy=new Boy();
		
		//stores the gamePanel and adds the boy to it
		this.gamePanel=gamePanel;
		this.gamePanel.addBoy(boy);
		
		//while you're playing the game, the gameIsRunning is set to true
		this.gameIsRunning=true;
	}
	
	@Override
	public void run() {
		while(gameIsRunning){
		    if(boy.outOfBounds() && (currentLevel < lastLevel)){
                world.initializeStage(++currentLevel);
                boy.reinitialize();
            }

            boy.checkFallingState();

            //updates the character movement if he's 'jumping'
            boy.checkJumpState();

            //manage the keys currently pressed
            manageKeys();

            boy.checkBlockCollisions();

            boy.checkRestoringCount();
			
			gamePanel.repaintGame();
			
			try {
				Thread.sleep(MAIN_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//the function manages the keys currently pressed associating concrete
	//actions to them
	private void manageKeys() {
		//get the currently pressed keys from the KeyboardController
		HashSet<Integer> currentKeys=KeyboardController.getActiveKeys();
		
		//Close the window with escape button
		if(currentKeys.contains(KeyEvent.VK_ESCAPE)){
			System.exit(0);
		}
		
		//manage the two possible run direction
		if(currentKeys.contains(KeyEvent.VK_RIGHT)){
			//move right
			boy.move(KeyEvent.VK_RIGHT, isLastLevel());
		} else if (currentKeys.contains(KeyEvent.VK_LEFT)){
			//move left
			boy.move(KeyEvent.VK_LEFT, isLastLevel());
		} else if(currentKeys.isEmpty() && !boy.getJumping() && !boy.getFalling()){
			//if the player is not pressing keys, the protagonist stands still
			boy.stop();
		}
		
		//if the character is not jumping, tell him to jump
        if(currentKeys.contains(KeyEvent.VK_SPACE)) {
            if(!boy.getJumping() && !boy.getFalling()){
                boy.jump();
            }
        }
	}

	public Boy getBoy(){
		return boy;
	}
	
	private boolean isLastLevel() {
		if(currentLevel >= lastLevel)
			return true;
		return false;
	}
	
	private int lastLevel = 2;
	
   //number of the current level the character finds himself in
    private int currentLevel=1;
	
	//variable set to 'true' if the game is running, 'false' otherwise
	private boolean gameIsRunning;
	
	//reference to the gamePanel
	private GamePanel gamePanel;
	
	//main sleep time of the GameManager thread - in this case
	//the gameManager does all it has to do and then waits for 16ms
	//before starting once again
	private static final int MAIN_SLEEP_TIME=16;
	
	//reference to the game main character
	private Boy boy;
	private World world;
}
