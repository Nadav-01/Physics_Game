package src;

import java.awt.event.KeyEvent;		//for input
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.Graphics;			//for graphics
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import javax.swing.JFrame;			//to render the frame
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Timer;				//to keep fps stable
import java.util.TimerTask;

@SuppressWarnings("serial")
public class attempt extends JPanel {
    
	static boolean[] key = new boolean[9];
	
	enum keyCode {
		UP (0),
		DOWN (1),
		LEFT (2),
		RIGHT (3),
		RESET (4),
		BALL (5),
		VBALL (6),
		WALL (7),
		RWALL (8);
		
		public int code;
		keyCode(int code)
		{
			this.code = code;
		} 
	}
	
    enum mode {BALL, WALL, RWALL,VBALL};
    static attempt attempt = new attempt();
    
    static final int PLAYER_SIZE = 60;
    //static Proj[] pro; // Projectile array
    static LinkedList<Proj> pro = new LinkedList<Proj>();
    static LinkedList<Item> walls = new LinkedList<Item>();
    //static Item[] walls = {	new Wall(-100,100,attempt.getWidth()+100,-100),		// floor
    //						new Wall(-100,attempt.getHeight()+100,100,-100 ),	// leftwall	
    //						new Wall(attempt.getWidth()-100,attempt.getHeight()+100,attempt.getWidth()+100,-100), 	// rightwall
    //						new Wall(-100,attempt.getHeight()+100,attempt.getWidth()+100,attempt.getHeight()-100),		// ceiling
    						//new Wall(200,200,250,250),
    						//new RoundWall(400,400,60)
    //};   // Wall array
    																							
    static int proSize;
    static int wallSize;
    static mode CurMode;
    
    static Coord mouseLocation = new Coord (0,0);
    static boolean mouseInScreen;
    static boolean mousePressed;
    
    static Point windowLocation;
    
    static long oldT;
    static int oldHeight;
    static int oldWidth;
    
    public static void initilizeWall()
    {
    	/*walls = new Item[] {	new Wall(-100,100,attempt.getWidth()+100,-100),		// floor
				new Wall(-100,attempt.getHeight()+100,100,-100 ),	// leftwall	
				new Wall(attempt.getWidth()-100,attempt.getHeight()+100,attempt.getWidth()+100,-100), 	// rightwall
				new Wall(-100,attempt.getHeight()+100,attempt.getWidth()+100,attempt.getHeight()-100),		// ceiling
				new Wall(600,600,850,450),
				new RoundWall(400,400,60),
				new RoundWall(800,400,2)
    		};   // Wall array
    		
    		wallSize = walls.length;*/
    	
    	walls.clear();
    	walls.add(new Wall(-100,100,attempt.getWidth()+100,-100));	// floor
    	walls.add(new Wall(-100,attempt.getHeight()+100,100,-100 ));	//leftwall
    	walls.add(new Wall(attempt.getWidth()-100,attempt.getHeight()+100,attempt.getWidth()+100,-100));	// rightwall
    	walls.add(new Wall(-100,attempt.getHeight()+100,attempt.getWidth()+100,attempt.getHeight()-100));	//ceiling
    	
    	wallSize = walls.size();
    }
    
    public static void inintilizeProj()	//initilize projectile array.
    {
    	if (!pro.isEmpty())
    		pro.clear();
    	pro.add(new Proj(300,300,PLAYER_SIZE));
    	pro.add(new Proj(250,250,PLAYER_SIZE/2));
    	pro.add(new Proj(250,250,PLAYER_SIZE/2));
    	pro.add(new Proj(250,250,PLAYER_SIZE/2));
    	pro.add(new Proj(250,250,PLAYER_SIZE/3));
    	pro.add(new Proj(250,250,PLAYER_SIZE/4));
    	pro.add(new Proj(250,250,PLAYER_SIZE/1.5));
    	pro.add(new Proj(250,250,PLAYER_SIZE*2));
    	proSize = pro.size();
    	
    	/*pro = new Proj[] { 	new Proj(300,300,PLAYER_SIZE),
    						new Proj(250,250,PLAYER_SIZE/2),
    						new Proj(250,250,PLAYER_SIZE/2),
    						new Proj(250,250,PLAYER_SIZE/2),
    						new Proj(250,250,PLAYER_SIZE/3),
    						new Proj(250,250,PLAYER_SIZE/4),
    						new Proj(250,250,PLAYER_SIZE/1.5)
    						}; 
    	pro[0]._mass = pro[1]._mass;
    	proSize = pro.length;*/
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
        	Putstuff.putProj(pro.get(i),g2d);   // Paint projectiles
        }
        
        g2d.setColor(Color.red);
        //g2d.fillOval((int)(pro[1].cord1._x - pro[1]._rad), (int)(pro[1].cord1._y - pro[1]._rad), (int)pro[1]._rad*2, (int)pro[1]._rad*2);   // Paint negetive proj

        g2d.setColor(Color.black);
        for (int i = 0; i < wallSize; i++) // Paints walls
        {
        	if (walls.get(i) instanceof Wall)
        		Putstuff.putWall((Wall)walls.get(i),g2d); 
        	if (walls.get(i) instanceof RoundWall)
        		Putstuff.putRoundwall((RoundWall)walls.get(i),g2d); 
        }
        
        Coord loc = mouseLocation.intoJcoord();
        g2d.drawString(" mouse location: x = " + loc._x + " y = " + loc._y , 500, 400);
        
        g2d.drawString("speed = " + pro.get(0)._vel.getSize(), 200, 200);   // Debug info
        g2d.drawString("dir = " + pro.get(0)._vel.getDir(), 200, 150);
        g2d.drawLine(350, 150, 350 + (int)(10 * Math.cos(pro.get(0)._vel.getDir())), 150 - (int)(10 * Math.sin(pro.get(0)._vel.getDir())));
        g2d.fillOval(347 + (int)(10 * Math.cos(pro.get(0)._vel.getDir())), 147 - (int)(10 * Math.sin(pro.get(0)._vel.getDir())), 5, 5);
        g2d.drawString("x = " + pro.get(0).cord1._x + "\t y = " + pro.get(0).cord1._y, 200, 250);
        g2d.drawString("Energy = " + Physics.Energy(pro.get(0),this.getSize()) , 200, 300);
        g2d.drawString("num of Proj: " + proSize , 200, 400);
        
    }
    
    public attempt() {  // Implementing keylistener
    	InputManager manage = new InputManager();
        KeyListener Klistener = manage.new MyActionListener();
        MouseListener Mlistener = manage.new MyActionListener();
        addKeyListener(Klistener);
        addMouseListener(Mlistener);
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
        	if (key[keyCode.UP.code])
            {
                System.out.println("up");
                Physics.upplyF(pro.get(0), new Vect(POWER,(float)(Math.PI/2)));
                Physics.upplyF(pro.get(1), new Vect(POWER,(float)(3*Math.PI/2)));
            }
            if (key[keyCode.DOWN.code])
            {
                System.out.println("down");
                Physics.upplyF(pro.get(0), new Vect(POWER,(float)(3*Math.PI/2)));
                Physics.upplyF(pro.get(1), new Vect(POWER,(float)(Math.PI/2)));
            }
            if (key[keyCode.RIGHT.code])
            {
                System.out.println("right");
                Physics.upplyF(pro.get(0), new Vect(POWER,(float)(0)));
                Physics.upplyF(pro.get(1), new Vect(POWER,(float)(Math.PI)));
            }
            if (key[keyCode.LEFT.code])
            {
                System.out.println("left");
                Physics.upplyF(pro.get(0), new Vect(POWER,(float)(Math.PI)));
                Physics.upplyF(pro.get(1), new Vect(POWER,(float)(0)));
            }
            if (key[keyCode.RESET.code])
            {
            	System.out.println("reset");
            	inintilizeProj();
            }
            if (key[keyCode.BALL.code])
            {
            	System.out.println("Ball");
            	CurMode = mode.BALL;
            }
            if (key[keyCode.VBALL.code])
            {
            	System.out.println("Velocity Ball");
            	CurMode = mode.VBALL;
            }
            if (key[keyCode.WALL.code])
            {
            	System.out.println("Wall");
            	CurMode = mode.WALL;
            }
            
            if (key[keyCode.RWALL.code])
            {
            	System.out.println("Round Wall");
            	CurMode = mode.RWALL;
            }
        }
        
        public void run()
        {
        	windowLocation = attempt.getLocation();
        	if (oldHeight != attempt.getHeight() || oldWidth != attempt.getWidth())
        		initilizeWall();
        	oldHeight = attempt.getHeight();
        	oldWidth = attempt.getWidth();
        	
        	long newT = System.currentTimeMillis();	//gets new time from the system.
        	long deltaT = newT - oldT;				//gets the difference of the times between last frame and now.
        	
            // Upply gravity and friction to all projectiles.
            Physics.upplyG(pro, proSize);
            //Physics.upplyFric(pro, 2);
            
            
            for (int i = 0; i < proSize; i++) // Check all combination of items that can collide with each other
            {
                for (int j = 0; j < wallSize; j++)
                {
                    if (Physics.areColliding((Item)pro.get(i),(Item)walls.get(j)))
                    {
                        System.out.println("bounce wall");
                        Physics.collision(pro.get(i),(Item)walls.get(j));
                    }
                }
            }
            
            for (int i = 0; i < proSize; i++) // Check all combination of items that can collide with each other
            {
            	
                for (int j = i+1; j < proSize; j++)
                {
                	
                    if (Physics.areColliding((Item)pro.get(i),(Item)pro.get(j)))
                    {
                        System.out.println("bounce");
                       /* if(Physics.isOverlap(pro[i],pro[j]))
                        {
                            Physics.fixOverlap(pro[i],pro[j]);
                        }*/
                       Physics.collision(pro.get(i),pro.get(j));
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
            	
            	

        		pro.get(i).cord1._x += deltaT*pro.get(i)._vel.getX()/1000;	//divide by 1000 because messured by milliseconds.
        		pro.get(i).cord1._y += deltaT*pro.get(i)._vel.getY()/1000;

                
                if 		(pro.get(i).cord1._x < ((Wall)walls.get(1)).cord2._x ||
                		pro.get(i).cord1._x > walls.get(2).cord1._x ||
                		pro.get(i).cord1._y < walls.get(0).cord1._y ||
                		pro.get(i).cord1._y > ((Wall)walls.get(3)).cord2._y)
                {
                		pro.get(i).cord1._x = 450;
                		pro.get(i).cord1._y = 450;
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
        oldHeight = 1000;
        oldWidth = 1800;
        
        //int flipXcnt = 1;
        //int flipYcnt = 1;
        TimerTask gameloop = new gameloop(attempt);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(gameloop, 0, 1);	//setting fps
    }
}