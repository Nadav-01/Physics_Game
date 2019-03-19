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
        public void keyTyped(KeyEvent e) {
        }

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
        	}
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
        	switch (e.getExtendedKeyCode()) {
        	case (KeyEvent.VK_UP):
        		attempt.key[attempt.keyCode.UP.code] = false;
        		break;
        	case (KeyEvent.VK_DOWN):
        		attempt.key[attempt.keyCode.DOWN.code] = false;
        		break;
        	case (KeyEvent.VK_LEFT):
        		attempt.key[attempt.keyCode.LEFT.code] = false;
        		break;
        	case (KeyEvent.VK_RIGHT):
        		attempt.key[attempt.keyCode.RIGHT.code] = false;
        		break;
        	case (KeyEvent.VK_R):
        		attempt.key[attempt.keyCode.RESET.code] = false;
        		break;
        	case (KeyEvent.VK_B):
        		attempt.key[attempt.keyCode.BALL.code] = false;
        		break;
        	case (KeyEvent.VK_V):
        		attempt.key[attempt.keyCode.VBALL.code] = false;
        		break;
        	case (KeyEvent.VK_W):
        		attempt.key[attempt.keyCode.WALL.code] = false;
        		break;
        	case (KeyEvent.VK_M):
        		attempt.key[attempt.keyCode.RWALL.code] = false;
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
