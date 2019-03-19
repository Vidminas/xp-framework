package gui;

import java.awt.Toolkit;
import javax.swing.JFrame;

import intermediary.Settings;

// The main game window frame
public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public GameFrame(GamePanel gamePanel) {
		// Set frame to appear at the centre of the screen
		this.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()-WIDTH) / 2),
		                ((int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()-HEIGHT) / 2));	
		
		// Set the operation that will happen when closing the frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		this.setSize(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
		this.setTitle(Settings.WINDOW_TITLE);
		this.setVisible(true);
		this.setResizable(false);
		
		this.add(gamePanel);
		gamePanel.grabFocus();
		gamePanel.requestFocusInWindow();
	}
}
