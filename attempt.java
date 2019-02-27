package src;

import java.awt.event.KeyEvent;		//for input
import java.awt.event.KeyListener;	
import java.awt.Graphics;			//for graphics
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;			//to render the frame
import javax.swing.JPanel;
import java.awt.Color;
import java.util.Timer;				//to keep fps stable
import java.util.TimerTask;

@SuppressWarnings("serial")
public class attempt extends JPanel {
    
    // Define code of arrow keys
	/*	TODO- make boolean array of keys so more then one can be pressed at once.
		like so: static boolean[] keys = new boolean[]
		that is so i can access the key using the keycodes themselves.
		like:	keys[keyEvent.VK_UP] = true		//means up is pressed right now
	*/
    static int up = KeyEvent.VK_UP; 
    static int right = KeyEvent.VK_RIGHT;
    static int down = KeyEvent.VK_DOWN;
    static int left = KeyEvent.VK_LEFT;
    static int reset = KeyEvent.VK_R;
    
    
    static final int PLAYER_SIZE = 60;
    static Proj[] pro; // Projectile array
    static Wall[] walls = {	new Wall(-220,480,780,750),		// floor
    						new Wall(-220,-220,20,750 ),	// leftwall	
    						new Wall(580,-120,780,750), 	// rightwall
    						new Wall(-220,-220,780,20),		// ceiling
    						new Wall(200,200,250,250)
    };   // Wall array
    																							
    static int proSize;
    static int wallSize = walls.length;
    
    static long oldT;
    
    
    
    public static void inintilizeProj()	//initilize projectile array.
    {
    	pro = new Proj[] { 	new Proj(300,300,PLAYER_SIZE),
    						new Proj(50,50,PLAYER_SIZE/2)}; 
    	proSize = pro.length;
    }
    
    @Override   // Overriding paint of Jpanel
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
                
        putItems(g2d);
                
    }
    
    public void putItems(Graphics2D g2d)
    {
        
        
        
        
        g2d.setColor(Color.black);
        for (int i = 0; i < proSize; i++)	//paints projectiles
        {
        	g2d.drawOval((int)pro[i]._x, (int)pro[i]._y, (int)pro[i]._rad*2, (int)pro[i]._rad*2);   // Paint projectiles
        }
        
        g2d.setColor(Color.red);
        g2d.fillOval((int)pro[1]._x, (int)pro[1]._y, (int)pro[1]._rad*2, (int)pro[1]._rad*2);   // Paint negetive proj

        g2d.setColor(Color.black);
        for (int i = 0; i < wallSize; i++) // Paints walls
            g2d.fillRect((int)walls[i]._x, (int)walls[i]._y, (int)walls[i].getLength(), (int)walls[i].getHeight());
        
        g2d.drawOval((int)pro[0]._x, (int)pro[0]._y, (int)pro[0]._rad*2, (int)pro[0]._rad*2);   // Paint player
        //g2d.drawRect((int)pro[0]._x, (int)pro[0]._y, (int)pro[0]._rad*2, (int)pro[0]._rad*2);
        
        g2d.drawString("speed = " + pro[0]._vel.getSize(), 200, 100);   // Debug info
        g2d.drawString("dir = " + pro[0]._vel.getDir(), 200, 150);
        g2d.drawLine(350, 150, 350 + (int)(10 * Math.cos(pro[0]._vel.getDir())), 150 - (int)(10 * Math.sin(pro[0]._vel.getDir())));
        g2d.fillOval(347 + (int)(10 * Math.cos(pro[0]._vel.getDir())), 147 - (int)(10 * Math.sin(pro[0]._vel.getDir())), 5, 5);
        g2d.drawString("x = " + pro[0]._x + "\t y = " + pro[0]._y, 200, 200);
        g2d.drawString("Energy = " + Physics.Energy(pro[0],this.getSize()) , 200, 250);
    }
    
    public attempt() {  // Implementing keylistener
    	InputManager manage = new InputManager();
        KeyListener listener = manage.new MyKeyListener();
        addKeyListener(listener);
        setFocusable(true);
    }
    
    

    
    
    public static class gameloop extends TimerTask
    {
    	
    	
        public attempt at;	
        
        static int action;	//the action that the player is taking this frame
        
        public gameloop(attempt att)
        {
            at = att;
        }
        
        public static void processInput()
        {
        	int POWER = 4000000;
        	action = InputManager.action;
        	if (action == up)
            {
                System.out.println("up");
                Physics.upplyF(pro[0], new Vect(POWER,(float)(Math.PI/2)));
                Physics.upplyF(pro[1], new Vect(POWER,(float)(3*Math.PI/2)));
            }
            if (action == down)
            {
                System.out.println("down");
                Physics.upplyF(pro[0], new Vect(POWER,(float)(3*Math.PI/2)));
                Physics.upplyF(pro[1], new Vect(POWER,(float)(Math.PI/2)));
            }
            if (action == right)
            {
                System.out.println("right");
                Physics.upplyF(pro[0], new Vect(POWER,(float)(0)));
                Physics.upplyF(pro[1], new Vect(POWER,(float)(Math.PI)));
            }
            if (action == left)
            {
                System.out.println("left");
                Physics.upplyF(pro[0], new Vect(POWER,(float)(Math.PI)));
                Physics.upplyF(pro[1], new Vect(POWER,(float)(0)));
            }
            if (action == reset)
            {
            	System.out.println("reset");
            	inintilizeProj();
            }
        }
        
        public void run()
        {
        	long newT = System.currentTimeMillis();	//gets new time from the system.
        	long deltaT = newT - oldT;				//gets the difference of the times between last frame and now.
        	
            // Upply gravity and friction to all projectiles.
            Physics.upplyG(pro, proSize);
            //Physics.upplyFric(pro, 2);
            
            
            for (int i = 0; i < proSize; i++) // Check all combination of items that can collide with each other
            {
                for (int j = 0; j < wallSize; j++)
                {
                    if (Physics.areColliding((Item)pro[i],(Item)walls[j]))
                    {
                        System.out.println("bounce wall");
                        Physics.collision(pro[i],walls[j]);
                    }
                }
            }
            
            for (int i = 0; i < proSize; i++) // Check all combination of items that can collide with each other
            {
                for (int j = i+1; j < proSize; j++)
                {
                    if (Physics.areColliding((Item)pro[i],(Item)pro[j]))
                    {
                        System.out.println("bounce");
                        if(Physics.isOverlap(pro[i],pro[j]))
                        {
                            Physics.fixOverlap(pro[i],pro[j]);
                        }
                       Physics.collision(pro[i],pro[j]);
                    }
                }
            }
            /*
            if(Physics.areColliding(pro[0],pro[1]))	//collision calculations and updates
            {
            	 if(Physics.isOverlap(pro[0],pro[1]))
                 {
                     Physics.fixOverlap(pro[0],pro[1]);
                 }
                Physics.collision(pro[0],pro[1]);
               
            }
            */
            
            for (int i = 0; i < proSize; i++) // apply speed to projectiles.
            {
            	
            	
            	 pro[i]._x += deltaT*pro[i]._vel.getX()/1000;	//divide by 1000 because messured by milliseconds.
                 pro[i]._y -= deltaT*pro[i]._vel.getY()/1000;    //coordinate system flipped because window starts in upper left.
                
                
                if (pro[i]._x < -1000 || pro[i]._x > 2000 || pro[i]._y < -1000 || pro[i]._y > 2000)
                {
                	if ( 50 + i*pro[i]._rad*2 < 680)
                		pro[i]._x = 50 + i*pro[i]._rad*2;
                	
                	if (50 + i*pro[i]._rad*2 < 550)
                		pro[i]._y = 50 + i*pro[i]._rad*2;
                	
                    if (pro[i]._vel.getSize() > 500)
                        pro[i]._vel.setSize(50);
                }
            } 
            
            
            
            at.repaint();		//repaint the screen
            processInput();
            oldT = newT;		//update oldT to newT to remember this frames time for the next frame.
        }
    }
    
    
    
    public static void main(String[] args)
    {
    	inintilizeProj();
    	oldT = System.currentTimeMillis();
        Physics phy = new Physics();
        JFrame frame = new JFrame("game");
        
        attempt attempt = new attempt();
        frame.add(attempt);
        frame.setSize(680, 550);	//setting window size
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //int flipXcnt = 1;
        //int flipYcnt = 1;
        TimerTask gameloop = new gameloop(attempt);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(gameloop, 0, 7);	//setting fps
    }
}