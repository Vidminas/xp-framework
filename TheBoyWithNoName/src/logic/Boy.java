package logic;

import gui.GameFrame;
import intermediary.Settings;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


// The boy is the main player-controlled character of the game
public class Boy {
    /* **************** */
    /* Image properties */
    /* **************** */
    
    // Current frame in the animation
    private BufferedImage currentFrame;

    // All the bufferedImages used in the character's animation 
    private BufferedImage idle_R;
    private BufferedImage idle_L;
    private BufferedImage[] run_R;
    private BufferedImage[] run_L;
    
    // Determines the currentFrame to be used in a run animation
    private int currentFrameNumber = 0;
    
    // The direction that the character is currently facing
    // Used to tell which way facing animations to use
    private int facingDirection;
    
    /* **************************** */
    /* Position and size properties */
    /* **************************** */
    
    // The initial position of the character
    public static final int BOY_START_X = 2 * Settings.TILE_SIZE;
    public static final int BOY_START_Y = 6 * Settings.TILE_SIZE;
    // The current position of the character
    private int currentX;
    private int currentY;
    
    // The boundingBox (sometimes called hit box) is a rectangle around the character
    // It defines the space occupied by the character at the specific moment
    // Used for detecting collisions
    private Rectangle boundingBox;
    
    // Dimensions of the main character (used to set the boundingBox)
    private final int BOY_HEIGHT = 64;
    private final int BOY_WIDTH = 32;
    
    /* ******************* */
    /* Movement properties */
    /* ******************* */
    
    // DISPLACEMENT is the distance covered by a single step of the character
    private static final int DISPLACEMENT = 4;
    
    // jump_count works with JUMP_COUNTER_THRESH: in particular this 
    // Variable is incremented every time the main thread calls the checkState()
    // Function and goes on until jump_count has not reached JUMP_COUNTER_THRESH
    // Making the currentY of the character smaller and smaller so that he keeps
    // Ascending. When the jump_count reaches JUMP_COUNTER_THRESH the currentY is 
    // Incremented for the character is in the descending phase of the jump. It goes on
    // Incrementing until jump_count reaches JUMP_COUNTER_THRESH*2, then the jumping 
    // Boolean is set to false and the count is reinitialised
    private int jump_count;

    // jumping is 'true' when the character is actually jumping
    // Is 'false' when the character is not up in the air
    private boolean jumping;
    
    // JUMP_COUNTER_THRESH is the upper bound to the counter jump_count:
    // - from 0 to JUMP_COUNTER_THRESH the character is going up
    // - from JUMP_COUNTER_THRESH to JUMP_COUNTER_THRESH*2 the character is going down
    private static final int JUMP_COUNTER_THRESH = 20;
    
    // MOVE_COUNTER_THRESH is explained in the setFrameNumber function's comment
    private static final int MOVE_COUNTER_THRESH = 5;
    
    // moveCounter is explained in the setFrameNumber function's comment
    private int moveCounter = 0;
    
    // True when the character is falling
    private boolean falling;
    
    // Idle is 'true' if the character is not moving, false otherwise
    private boolean idle;
    
    /* **************** */
    /* Other properties */
    /* **************** */
    
    // Restoring is true when the character has just died and remains
    // True until his body flashes 3 times
    private boolean restoring = false;
    private int restoring_count = 0;
    private final static int RESTORING_THRESH = 84;
    private final static int RESTORING_MODULE = 12;

    public final int MAX_LIFE = 3;
    // Life initially equals 3, every time the character dies it decreases
    private int life = MAX_LIFE;
    
    /* ************** */
    /* Initialisation */
    /* ************** */
    public void resetPosition() {
        currentX = BOY_START_X;
        currentY = BOY_START_Y;
        
        boundingBox = new Rectangle(BOY_START_X, currentY, BOY_WIDTH, BOY_HEIGHT);
        
        // Initially, the character is standing still with his head turned right
        facingDirection = KeyEvent.VK_RIGHT;
        currentFrame = idle_R;
        falling = false;
        jump_count = 0;
        
        idle = true;
    }
    
	public Boy() {
	    // Initialise the buffers that will store the run sprites
        run_L = new BufferedImage[Settings.BOY_RUN_FRAMES];
        run_R = new BufferedImage[Settings.BOY_RUN_FRAMES];
        
	    // Load all the sprites needed to animate the character
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResource(Settings.playerSpritesheet));
          
            idle_R = spritesheet.getSubimage(0,
                                             0,
                                             Settings.BOY_SPRITE_WIDTH,
                                             Settings.BOY_SPRITE_HEIGHT);
            
            idle_L = spritesheet.getSubimage(0,
                                             Settings.BOY_SPRITE_HEIGHT,
                                             Settings.BOY_SPRITE_WIDTH,
                                             Settings.BOY_SPRITE_HEIGHT);
            
            for (int i = 0; i < Settings.BOY_RUN_FRAMES; i++) {
                run_R[i] = spritesheet.getSubimage((i+1) * Settings.BOY_SPRITE_WIDTH,
                                                   0,
                                                   Settings.BOY_SPRITE_WIDTH,
                                                   Settings.BOY_SPRITE_HEIGHT);
                
                run_L[i] = spritesheet.getSubimage((i+1) * Settings.BOY_SPRITE_WIDTH,
                                                   Settings.BOY_SPRITE_HEIGHT,
                                                   Settings.BOY_SPRITE_WIDTH,
                                                   Settings.BOY_SPRITE_HEIGHT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		resetPosition();
	}
	
	// Sets the current frame when the boy is moving.
	// We have BUFFER_RUN_SIZE frames for each run direction
    // moveCounter is incremented each time moveLeft or moveRight is called
    // So, according to moveCounter we can choose the current frame
    // The frame changes every MOVE_COUNTER_THRESH increments of the moveCounter variable.
    // In this case MOVE_COUNTER_THRESH is set to 5
    private void setFrameNumber() {
        currentFrameNumber = moveCounter / MOVE_COUNTER_THRESH;
        currentFrameNumber %= Settings.BOY_RUN_FRAMES;
        moveCounter %= MOVE_COUNTER_THRESH * Settings.BOY_RUN_FRAMES;
    }
	
	/* ****************** */
	/* Movement functions */
	/* ****************** */
	public void moveLeft(boolean isLastLevel) {
	    idle = false;
	    facingDirection = KeyEvent.VK_LEFT;
	    
	    // Attempt to move left by DISPLACEMENT amount
        currentX = checkMove(currentX, currentX - DISPLACEMENT, isLastLevel);
        boundingBox.setLocation(currentX, currentY);
        
        // Change the current frame in animation
        if (!jumping && !falling){
            currentFrame = run_L[currentFrameNumber];
            setFrameNumber();
            currentFrame = run_L[currentFrameNumber];
        } else {
            currentFrame = run_L[0];
        }
 
        moveCounter++;
	}
	
	public void moveRight(boolean isLastLevel) {
	    idle = false;
	    facingDirection = KeyEvent.VK_RIGHT;
	    
	      // Attempt to move right by DISPLACEMENT amount
        currentX = checkMove(currentX, currentX + DISPLACEMENT, isLastLevel);
        boundingBox.setLocation(currentX, currentY);
        
        // Change the current frame in animation
        if (!jumping && !falling){
            currentFrame = run_R[currentFrameNumber];
            setFrameNumber();
            currentFrame = run_R[currentFrameNumber];
        } else {
            currentFrame = run_R[0];
        }
        
        moveCounter++;
	}
	
	// Check whether the location the player wants to move into
	// Is not out of bounds and does not contain a block
	// If so, return the new position
	// Otherwise, return the old one
	private int checkMove(int oldX, int newX, boolean isLastLevel) {
        if (newX <= 0) {
            return 0;
        }
    
        if (newX >= (Settings.WINDOW_WIDTH - BOY_WIDTH) && isLastLevel) {
            return (Settings.WINDOW_WIDTH - BOY_WIDTH);
		}
        
        boundingBox.setLocation(newX, currentY);
        
        // Get the tile position (in the tiled map) 
        // Relative to the tile in front of the character
        int footCol;
        
        if (facingDirection == KeyEvent.VK_RIGHT) {
            int footX = (int) boundingBox.getMinX();
            footCol = (footX / Settings.TILE_SIZE) + 1;
        } else {
            int footX = (int) boundingBox.getMaxX();
            footCol = (footX / Settings.TILE_SIZE) - 1;
        }
        
        // The character is at the edge of the map and the tile in front of it
        // Would be out of bounds, so skip checking it
        if (footCol < 0 || footCol >= World.cols) {
            return newX;
        }
        
        int footY = (int) (boundingBox.getMaxY());
        int footRow = ((footY-1) / Settings.TILE_SIZE);
        
        Block tileInFrontOfFoot = World.map[footRow][footCol];
        
        if (!tileInFrontOfFoot.empty()
            && tileInFrontOfFoot.intersects(boundingBox)) {
            return oldX;
        }
        
        return newX;
    }
	
	// Called every time the player presses the jump key
    // Does nothing if the character is already jumping or falling
    public void startJumping() {
        if (!jumping && !falling) {
            jumping = true;

            // Reinitialise the jump_count, useful to determine for how 
            // Much time the character is going to stay in the air
            jump_count = 0;

            // Sets the current jumping frame based on the last direction
            if (facingDirection == KeyEvent.VK_RIGHT) {
                currentFrame = run_R[2];
            } else {
                currentFrame = run_L[2];
            }
        }
    }
    
    // Checks the jumping variables and animates jumps 
    // Check the comments above 'jumping' and 'jump_count' variables
    // For more details
    public void handleJumping() {
        if (jumping) {
            if (jump_count < JUMP_COUNTER_THRESH) {
                currentY -= DISPLACEMENT;
                boundingBox.setLocation(currentX, currentY);
            } 

            jump_count++;

            if (jump_count >= JUMP_COUNTER_THRESH){
                jumping = false;
                jump_count = 0;
                falling = true;
            }
            
            checkBlockCollisions();
        }
    }
    
    // Checks and handles possible collisions with static blocks (Block class)
    private void checkBlockCollisions() {
        // If the character is jumping, his head must not touch a block;
        // If it touches a block, stop the ascending phase of the jump (start falling)
        
        // Row position of the cell above the character's head (in the tiled map)
        int upRow = (int) ((boundingBox.getMinY() - 1) / Settings.TILE_SIZE);

        // Tile position relative to the upper-left corner of the character's bounding box
        int upLeftCornerCol = (int) (boundingBox.getMinX() / Settings.TILE_SIZE);
        Block leftCornerBlock = null;
        
        if (upRow >= 0 && upLeftCornerCol >= 0) {
            leftCornerBlock = World.map[upRow][upLeftCornerCol];
        }
        
        // Tile position relative to the upper-right corner of the character's bounding box
        int upRightCornerCol = (int) ((boundingBox.getMaxX()) / Settings.TILE_SIZE);
        Block rightCornerBlock = null;
        
        if (upRow >= 0 && upRightCornerCol < World.cols) {
            rightCornerBlock = World.map[upRow][upRightCornerCol];
        }

        if ((leftCornerBlock != null && !leftCornerBlock.empty() && leftCornerBlock.intersects(boundingBox))
            || (rightCornerBlock != null && !rightCornerBlock.empty() && rightCornerBlock.intersects(boundingBox))) {
            // If an upper corner is intersecting a block, stop the jumping phase
            // And start the falling phase, setting the jump_count to 0
            jumping = false;
            jump_count = 0;
            falling = true;
        }
    }
    
    // Sets an idle position as current frame
    public void stop() {
        // If the last direction was right, set the idle-right position
        // As the current frame
        if (facingDirection == KeyEvent.VK_RIGHT) {
            currentFrame = idle_R;
        // Otherwise set the idle-left position
        } else {
            currentFrame = idle_L;
        }
        
        idle = true;
    }

    public void handleFalling() {
        // Skip falling altogether if the character is jumping
        if (jumping) {
            return;
        }
        
        int currentRow = (int) (currentY / Settings.TILE_SIZE);
        
        // If the character falls to the bottom of the world map - instant death
        if (currentRow + 1 >= World.rows) {
            die();
            return;
        }

        // Since the character is wider than one tile but less wide than two
        // Check the two tiles below the character
        int lowLeftX = (int) boundingBox.getMinX() + 1;
        int lowRightX = (int) boundingBox.getMaxX() - 1;
        int lowLeftCol = lowLeftX / Settings.TILE_SIZE;
        int lowRightCol = lowRightX / Settings.TILE_SIZE;

        // Do not handle falling if the character is at the edge of the map
        // (The GameManager should transition to the next level instead)
        if (lowRightCol >= World.cols || lowLeftCol >= World.cols) {
            return;
        }
        
        // If both of the tiles below the character are thin air
        // Make the character fall down DISPLACEMENT units
        if ((World.map[currentRow + 1][lowLeftCol]).empty()
         && (World.map[currentRow + 1][lowRightCol]).empty()) {
            falling = true;
            currentY += DISPLACEMENT;
            boundingBox.setLocation(currentX, currentY);
        } else {
            falling = false;
        }
    }
    
    /* *************** */
    /* Other functions */
    /* *************** */
    public void checkRestoringCount() {
        if (restoring_count > 0) {
            restoring_count--;
            if (restoring_count % RESTORING_MODULE == 0) {
                restoring = !restoring;
            }
        } 
    }
    
    private void die() {
        resetPosition();
        restoring = true;
        restoring_count = RESTORING_THRESH;
        life--;
        
        //Game closes when you die
        if (life < 1) {
        	System.exit(0);
        }
    }
	
	/* ******* */
	/* Getters */
	/* ******* */
	public boolean getJumping() {
        return jumping;
    }

    public BufferedImage getCurrentFrame() {
        return currentFrame;
    }

    public int getCurrentX() {
        return currentX;
    }
    
    public int getCurrentY() {
        return currentY;
    }
    
    public Rectangle getBoundingBox() {
        return boundingBox;
    }
	
	public boolean getFalling() {
        return falling;
    }

    public boolean getRestoring() {
        return restoring;
    }

    public int getLife() {
        return life;
    }

    public boolean outOfBounds() {
        return (currentX >= Settings.WINDOW_WIDTH);
    }
}
