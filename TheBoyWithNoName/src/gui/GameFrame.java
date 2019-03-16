package gui;

import java.awt.Toolkit;
import javax.swing.JFrame;

// The main game window frame
public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1280;
    public static final int HEIGHT = 640;

	public GameFrame(GamePanel gamePanel) {
		// Set frame to appear at the centre of the screen
		this.setLocation((int)((Toolkit.getDefaultToolkit().getScreenSize().getWidth()-WIDTH)/2),
				((int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-HEIGHT)/2));	
		
		// Set the operation that will happen when closing the frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		this.setSize(WIDTH, HEIGHT);
		this.setTitle("The Boy With No Name");
		this.setVisible(true);
		this.setResizable(false);
		
		this.add(gamePanel);
		gamePanel.grabFocus();
		gamePanel.requestFocusInWindow();
	}
}
