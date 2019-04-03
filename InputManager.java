package src;

import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputManager 
{
	//static int action;
	
	public class MyActionListener implements KeyListener, MouseListener, MouseMotionListener {
    	
        @Override
        public void keyTyped(KeyEvent e) 
        {}
        @Override
        public void keyPressed(KeyEvent e) {
        	switch (e.getExtendedKeyCode()) {
        	case (KeyEvent.VK_UP):
        		attempt.key[attempt.keyCode.UP.code] = true;
        		break;
        	case (KeyEvent.VK_DOWN):
        		attempt.key[attempt.keyCode.DOWN.code] = true;
        		break;
        	case (KeyEvent.VK_LEFT):
        		attempt.key[attempt.keyCode.LEFT.code] = true;
        		break;
        	case (KeyEvent.VK_RIGHT):
        		attempt.key[attempt.keyCode.RIGHT.code] = true;
        		break;
        	case (KeyEvent.VK_R):
        		attempt.key[attempt.keyCode.RESET.code] = true;
        		break;
        	case (KeyEvent.VK_B):
        		attempt.key[attempt.keyCode.BALL.code] = true;
        		break;
        	case (KeyEvent.VK_V):
        		attempt.key[attempt.keyCode.VBALL.code] = true;
        		break;
        	case (KeyEvent.VK_W):
        		attempt.key[attempt.keyCode.WALL.code] = true;
        		break;
        	case (KeyEvent.VK_M):
        		attempt.key[attempt.keyCode.RWALL.code] = true;
        		break;
        	case (KeyEvent.VK_G):
        		attempt.key[attempt.keyCode.GRAV.code] = true;
        		break;
        	case (KeyEvent.VK_H):
        		attempt.key[attempt.keyCode.SGRAV.code] = true;
        		break;
        	case (KeyEvent.VK_E):
        		attempt.key[attempt.keyCode.ERASE.code] = true;
        		break;
        	case (KeyEvent.VK_SHIFT):
        		attempt.key[attempt.keyCode.SHIFT.code] = true;
        		break;
        	case (KeyEvent.VK_P):
        		attempt.key[attempt.keyCode.PAUSE.code] = true;
        		break;
        	case (KeyEvent.VK_C):
        		attempt.key[attempt.keyCode.CRAZY.code] = true;
        		break;
        	case (KeyEvent.VK_F):
        		attempt.key[attempt.keyCode.FREEZE.code] = true;
        		break;
        	case (KeyEvent.VK_ENTER):
        		if (attempt.CurMode == attempt.mode.PAUSE)
        		{
	        		attempt.ip = attempt.ipText.getText();
	        		attempt.ipText.setText("");
        		}
        		break;
        	}
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
        	switch (e.getExtendedKeyCode()) {
        	case (KeyEvent.VK_UP):
        		attempt.key[attempt.keyCode.UP.code] = false;
        		attempt.keyReleased[attempt.keyCode.UP.code] = true;
        		break;
        	case (KeyEvent.VK_DOWN):
        		attempt.key[attempt.keyCode.DOWN.code] = false;
	        	attempt.keyReleased[attempt.keyCode.DOWN.code] = true;	
	        	break;
        	case (KeyEvent.VK_LEFT):
        		attempt.key[attempt.keyCode.LEFT.code] = false;
        		attempt.keyReleased[attempt.keyCode.LEFT.code] = true;
        		break;
        	case (KeyEvent.VK_RIGHT):
        		attempt.key[attempt.keyCode.RIGHT.code] = false;
        		attempt.keyReleased[attempt.keyCode.RIGHT.code] = true;
        		break;
        	case (KeyEvent.VK_R):
        		attempt.key[attempt.keyCode.RESET.code] = false;
        		attempt.keyReleased[attempt.keyCode.RESET.code] = true;
        		break;
        	case (KeyEvent.VK_B):
        		attempt.key[attempt.keyCode.BALL.code] = false;
        		attempt.keyReleased[attempt.keyCode.BALL.code] = true;
        		break;
        	case (KeyEvent.VK_V):
        		attempt.key[attempt.keyCode.VBALL.code] = false;
        		attempt.keyReleased[attempt.keyCode.VBALL.code] = true;
        		break;
        	case (KeyEvent.VK_W):
        		attempt.key[attempt.keyCode.WALL.code] = false;
        		attempt.keyReleased[attempt.keyCode.WALL.code] = true;
        		break;
        	case (KeyEvent.VK_M):
        		attempt.key[attempt.keyCode.RWALL.code] = false;
        		attempt.keyReleased[attempt.keyCode.RWALL.code] = true;
        		break;
        	case (KeyEvent.VK_G):
        		attempt.key[attempt.keyCode.GRAV.code] = false;
        		attempt.keyReleased[attempt.keyCode.GRAV.code] = true;
        		break;
        	case (KeyEvent.VK_H):
        		attempt.key[attempt.keyCode.SGRAV.code] = false;
        		attempt.keyReleased[attempt.keyCode.SGRAV.code] = true;
        		break;
        	case (KeyEvent.VK_E):
        		attempt.key[attempt.keyCode.ERASE.code] = false;
        		attempt.keyReleased[attempt.keyCode.ERASE.code] = true;
        		break;
        		
        	case (KeyEvent.VK_SHIFT):
        		
        		attempt.key[attempt.keyCode.SHIFT.code] = false;
        		attempt.keyReleased[attempt.keyCode.SHIFT.code] = true;
        		break;
        		
        	case (KeyEvent.VK_P):
        		attempt.key[attempt.keyCode.PAUSE.code] = false;
        		attempt.keyReleased[attempt.keyCode.PAUSE.code] = true;
        		break;
        		
        	case (KeyEvent.VK_C):
        		attempt.key[attempt.keyCode.CRAZY.code] = false;
        		attempt.keyReleased[attempt.keyCode.CRAZY.code] = true;
        		break;
        	case (KeyEvent.VK_F):
        		attempt.key[attempt.keyCode.FREEZE.code] = false;
        		attempt.keyReleased[attempt.keyCode.FREEZE.code] = true;
        		break;
        		
        	}
        }

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			attempt.mouseLocation = (new Coord(e.getPoint().getX(), e.getPoint().getY()).intoJcoord());
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			attempt.mouseInScreen = true;
			attempt.mouseLocation = (new Coord(e.getPoint().getX(), e.getPoint().getY()).intoJcoord());
		}

		@Override
		public void mouseExited(MouseEvent e) {
			attempt.mouseInScreen = false;
			attempt.mouseLocation = (new Coord(e.getPoint().getX(), e.getPoint().getY()).intoJcoord());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			attempt.mousePressed = true;
			attempt.mouseLocation = (new Coord(e.getPoint().getX(), e.getPoint().getY()).intoJcoord());
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			attempt.mousePressed = false;
			attempt.mouseLocation = (new Coord(e.getPoint().getX(), e.getPoint().getY()).intoJcoord());
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			attempt.curMouseLoc = (new Coord(e.getPoint().getX(), e.getPoint().getY()).intoJcoord());
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			attempt.curMouseLoc = (new Coord(e.getPoint().getX(), e.getPoint().getY()).intoJcoord());
		}
    }
	
}
