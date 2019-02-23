package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManager 
{
	static int action;
	
	public class MyKeyListener implements KeyListener {
    	
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            action = e.getExtendedKeyCode();
        }

        @Override
        public void keyReleased(KeyEvent e) {
        	action = 0;
        }
    }
	
}
