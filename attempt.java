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
	static boolean[] key = new boolean[Math.max(KeyEvent.VK_UP, Math.max(KeyEvent.VK_RIGHT, Math.max(KeyEvent.VK_DOWN, Math.max(KeyEvent.VK_LEFT, KeyEvent.VK_R)))) + 1];
	
    //static int up = KeyEvent.VK_UP; 
    //static int right = KeyEvent.VK_RIGHT;
    //static int down = KeyEvent.VK_DOWN;
    //static int left = KeyEvent.VK_LEFT;
    //static int reset = KeyEvent.VK_R;
    static attempt attempt = new attempt();
    
    static final int PLAYER_SIZE = 60;
    static Proj[] pro; // Projectile array
    static Item[] walls = {	new Wall(-100,100,attempt.getWidth()+100,-100),		// floor
    						new Wall(-100,attempt.getHeight()+100,100,-100 ),	// leftwall	
    						new Wall(attempt.getWidth()-100,attempt.getHeight()+100,attempt.getWidth()+100,-100), 	// rightwall
    						new Wall(-100,attempt.getHeight()+100,attempt.getWidth()+100,attempt.getHeight()-100),		// ceiling
    						//new Wall(200,200,250,250),
    						//new RoundWall(400,400,60)
    };   // Wall array
    																							
    static int proSize;
    static int wallSize = walls.length;
    
    static long oldT;
    
    public static void initilizeWall()
    {
    	walls = new Item[] {	new Wall(-100,100,attempt.getWidth()+100,-100),		// floor
				new Wall(-100,attempt.getHeight()+100,100,-100 ),	// leftwall	
				new Wall(attempt.getWidth()-100,attempt.getHeight()+100,attempt.getWidth()+100,-100), 	// rightwall
				new Wall(-100,attempt.getHeight()+100,attempt.getWidth()+100,attempt.getHeight()-100),		// ceiling
				new Wall(600,600,850,450),
				new RoundWall(400,400,60),
				new RoundWall(800,400,2)
    		};   // Wall array
    	wallSize = walls.length;
    }
    
    public static void inintilizeProj()	//initilize projectile array.
    {
    	pro = new Proj[] { 	new Proj(300,300,PLAYER_SIZE),
    						new Proj(250,250,PLAYER_SIZE/2),
    						new Proj(250,250,PLAYER_SIZE/2),
    						new Proj(250,250,PLAYER_SIZE/2),
    						new Proj(250,250,PLAYER_SIZE/3),
    						new Proj(250,250,PLAYER_SIZE/4),
    						new Proj(250,250,PLAYER_SIZE/1.5)
    						}; 
    	pro[0]._mass = pro[1]._mass;
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
        	Putstuff.putProj(pro[i],g2d);   // Paint projectiles
        }
        
        g2d.setColor(Color.red);
        //g2d.fillOval((int)(pro[1].cord1._x - pro[1]._rad), (int)(pro[1].cord1._y - pro[1]._rad), (int)pro[1]._rad*2, (int)pro[1]._rad*2);   // Paint negetive proj

        g2d.setColor(Color.black);
        for (int i = 0; i < wallSize; i++) // Paints walls
        {
        	if (walls[i] instanceof Wall)
        		Putstuff.putWall((Wall)walls[i],g2d); 
        	if (walls[i] instanceof RoundWall)
        		Putstuff.putRoundwall((RoundWall)walls[i],g2d); 
        }
        
        g2d.drawString("speed = " + pro[0]._vel.getSize(), 200, 200);   // Debug info
        g2d.drawString("dir = " + pro[0]._vel.getDir(), 200, 150);
        g2d.drawLine(350, 150, 350 + (int)(10 * Math.cos(pro[0]._vel.getDir())), 150 - (int)(10 * Math.sin(pro[0]._vel.getDir())));
        g2d.fillOval(347 + (int)(10 * Math.cos(pro[0]._vel.getDir())), 147 - (int)(10 * Math.sin(pro[0]._vel.getDir())), 5, 5);
        g2d.drawString("x = " + pro[0].cord1._x + "\t y = " + pro[0].cord1._y, 200, 250);
        g2d.drawString("Energy = " + Physics.Energy(pro[0],this.getSize()) , 200, 300);
        
        
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
        	//action = InputManager.action;
        	if (key[KeyEvent.VK_UP])
            {
                System.out.println("up");
                Physics.upplyF(pro[0], new Vect(POWER,(float)(Math.PI/2)));
                Physics.upplyF(pro[1], new Vect(POWER,(float)(3*Math.PI/2)));
            }
            if (key[KeyEvent.VK_DOWN])
            {
                System.out.println("down");
                Physics.upplyF(pro[0], new Vect(POWER,(float)(3*Math.PI/2)));
                Physics.upplyF(pro[1], new Vect(POWER,(float)(Math.PI/2)));
            }
            if (key[KeyEvent.VK_RIGHT])
            {
                System.out.println("right");
                Physics.upplyF(pro[0], new Vect(POWER,(float)(0)));
                Physics.upplyF(pro[1], new Vect(POWER,(float)(Math.PI)));
            }
            if (key[KeyEvent.VK_LEFT])
            {
                System.out.println("left");
                Physics.upplyF(pro[0], new Vect(POWER,(float)(Math.PI)));
                Physics.upplyF(pro[1], new Vect(POWER,(float)(0)));
            }
            if (key[KeyEvent.VK_R])
            {
            	System.out.println("reset");
            	inintilizeProj();
            }
        }
        
        public void run()
        {
        	initilizeWall();
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
                        Physics.collision(pro[i],(Item)walls[j]);
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
                       /* if(Physics.isOverlap(pro[i],pro[j]))
                        {
                            Physics.fixOverlap(pro[i],pro[j]);
                        }*/
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
            	
            	

        		pro[i].cord1._x += deltaT*pro[i]._vel.getX()/1000;	//divide by 1000 because messured by milliseconds.
        		pro[i].cord1._y += deltaT*pro[i]._vel.getY()/1000;

                
                if (pro[i].cord1._x < -1000 || pro[i].cord1._x > 2000 || pro[i].cord1._y < -1000 || pro[i].cord1._y > 2000)
                {
                	if ( 50 + i*pro[i]._rad*2 < 680)
                		pro[i].cord1._x = 50 + i*pro[i]._rad*2;
                	
                	if (50 + i*pro[i]._rad*2 < 550)
                		pro[i].cord1._y = 50 + i*pro[i]._rad*2;
                	
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
    	for (int i = 0 ; i < key.length ; i++) {
    		key[i] = false;}
    	inintilizeProj();
    	oldT = System.currentTimeMillis();
        Physics phy = new Physics();
        JFrame frame = new JFrame("game");
        

        frame.add(attempt);
        frame.setSize(1800, 1000);	//setting window size
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //int flipXcnt = 1;
        //int flipYcnt = 1;
        TimerTask gameloop = new gameloop(attempt);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(gameloop, 0, 1);	//setting fps
    }
}