package src;

import java.awt.event.KeyEvent;		//for input
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
    
	
	
	enum keyCode {
		UP (0),
		DOWN (1),
		LEFT (2),
		RIGHT (3),
		RESET (4),
		BALL (5),
		VBALL (6),
		WALL (7),
		RWALL (8),
		GRAV (9),
		SGRAV (10),
		SHIFT (11);
		public int code;
		keyCode(int code)
		{
			this.code = code;
		} 
	}
	static boolean[] key = new boolean[keyCode.values().length];
	static boolean[] keyReleased = new boolean[keyCode.values().length];
	
    enum mode {BALL, WALL, RWALL,VBALL};
    static attempt attempt = new attempt();
    
    static final int PLAYER_SIZE = 60;
    //static Proj[] pro; // Projectile array
    static LinkedList<Proj> pro = new LinkedList<Proj>();
    static LinkedList<Item> walls = new LinkedList<Item>();
    static LinkedList<Proj> proPred= new LinkedList<Proj>();
    							
    static int totalBounce = 0;
    static double totalDist = 0;
    static int proSize;
    static int wallSize;
    static mode CurMode = mode.BALL;
    
    static Coord mouseLocation = new Coord (0,0);
    static Coord curMouseLoc = new Coord(0,0);
    
    static boolean mouseInScreen;
    static boolean mousePressed;
    static Coord startLocation;
    static Coord endLocation;
    
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
    	//pro.add(new Proj(300,300,PLAYER_SIZE));
    	//pro.add(new Proj(500,300,PLAYER_SIZE));
    	//pro.add(new Proj(300,300,PLAYER_SIZE));
    	//pro.add(new Proj(250,250,PLAYER_SIZE/2));
    	//pro.add(new Proj(250,250,PLAYER_SIZE/2));
    	//pro.add(new Proj(250,250,PLAYER_SIZE/2));
    	//pro.add(new Proj(250,250,PLAYER_SIZE/3));
    	//pro.add(new Proj(250,250,PLAYER_SIZE/4));
    	//pro.add(new Proj(250,250,PLAYER_SIZE/1.5));
    	//pro.add(new Proj(250,250,PLAYER_SIZE*2));
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
        
        if (startLocation != null)	//if the cursor is clicked (a new item is added) draw a display
        {
        	switch(CurMode)
        	{
            	case BALL:
            	{
	        	double rad = Physics.CoordDist(startLocation, curMouseLoc)/2;
	        	Coord cent = Physics.findMiddle(startLocation, curMouseLoc);
	        	Proj temp = new Proj(cent, rad);
	        	Putstuff.putProj(temp,g2d);
            	}
				case RWALL:
				{
					double rad = Physics.CoordDist(startLocation, curMouseLoc)/2;
		        	Coord cent = Physics.findMiddle(startLocation, curMouseLoc);
		        	RoundWall temp = new RoundWall(cent, rad);
		        	Putstuff.putRoundwall(temp,g2d);
					break;
				}
				case VBALL:
				{
					double rad;
					if (pro.isEmpty())
						rad = PLAYER_SIZE/2;
					else
						rad = pro.get(pro.size()-1)._rad;
	            	Coord cent = startLocation;
	            	Proj temp = new Proj(cent, rad);
	            	Putstuff.putProj(temp,g2d);
	            	Coord strtLoc = startLocation.intoJcoord();
	            	Coord curMseLoc = curMouseLoc.intoJcoord();
	            	
	            	if (key[keyCode.SHIFT.code])
					{
						if (Math.abs(curMseLoc._x - strtLoc._x) >=  Math.abs(curMseLoc._y - strtLoc._y))
							curMseLoc._y = strtLoc._y;
						else if (Math.abs(curMseLoc._x - strtLoc._x) <  Math.abs(curMseLoc._y - strtLoc._y))
							curMseLoc._x = strtLoc._x;
					}
	            	
	            	g2d.drawLine((int)strtLoc._x,  (int)strtLoc._y, (int)curMseLoc._x, (int)curMseLoc._y);
	            	
					break;
				}
				case WALL:
				{
					double minX = Math.min(startLocation._x, curMouseLoc._x);
            		double maxX = Math.max(startLocation._x, curMouseLoc._x);
            		double minY = Math.min(startLocation._y, curMouseLoc._y);
            		double maxY = Math.max(startLocation._y, curMouseLoc._y);
            		Coord c1 = new Coord(minX, maxY);
            		Coord c2 = new Coord(maxX, minY);
            		Wall temp = new Wall(c1,c2);
            		Putstuff.putWall(temp,g2d);
					break;
				}
				default:
				{
					break;
				}
        	}
        }
        
        Coord loc = curMouseLoc;
        g2d.drawString(" mouse location: x = " + loc._x + " y = " + loc._y , 500, 400);
        if (!pro.isEmpty())
        {
	        g2d.drawString("speed = " + pro.get(0)._vel.getSize(), 200, 200);   // Debug info
	        g2d.drawString("dir = " + pro.get(0)._vel.getDir(), 200, 150);
	        g2d.drawLine(350, 150, 350 + (int)(10 * Math.cos(pro.get(0)._vel.getDir())), 150 - (int)(10 * Math.sin(pro.get(0)._vel.getDir())));
	        g2d.fillOval(347 + (int)(10 * Math.cos(pro.get(0)._vel.getDir())), 147 - (int)(10 * Math.sin(pro.get(0)._vel.getDir())), 5, 5);
	        g2d.drawString("x = " + pro.get(0).cord1._x + "\t y = " + pro.get(0).cord1._y, 200, 250);
	        double energy = 0;
	        for (int i = 0; i < proSize; i++)
		        energy += Physics.Energy(pro.get(i),this.getSize());
	        g2d.drawString("Energy = " + energy , 200, 300);
        }
        g2d.drawString("num of Proj: " + proSize , 200, 400);
        g2d.drawString("total bounce's: " + totalBounce , 200, 450);
        g2d.drawString("total distance: " + totalDist , 200, 500);
        g2d.setColor(Color.white);
        g2d.drawString("Press B to add more balls, V to launch balls, W to add more walls, M to add more round walls, G to toggle gravity, and H + arrowkey to control gravity direction" , 200, attempt.getHeight() - 50);
        g2d.drawString("(release H while the arrow keys are still held)" , 810, attempt.getHeight() - 30);
        switch (CurMode)
        {
		case BALL:
			g2d.drawString("Current Mode: Ball" , 100, 50);
			break;
		case RWALL:
			g2d.drawString("Current Mode: Round Wall" , 100, 50);
			break;
		case VBALL:
			g2d.drawString("Current Mode: Launch Ball" , 100, 50);
			break;
		case WALL:
			g2d.drawString("Current Mode: Wall" , 100, 50);
			break;
		default:
			break;
        
        }
        
    }
    
    public attempt() {  // Implementing keylistener
    	InputManager manage = new InputManager();
        KeyListener Klistener = manage.new MyActionListener();
        MouseListener Mlistener = manage.new MyActionListener();
        MouseMotionListener MMlistener = manage.new MyActionListener();
        addKeyListener(Klistener);
        addMouseListener(Mlistener);
        addMouseMotionListener(MMlistener);
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
                //Physics.upplyF(pro.get(0), new Vect(POWER,(float)(Math.PI/2)));
                //Physics.upplyF(pro.get(1), new Vect(POWER,(float)(3*Math.PI/2)));
            }
            if (key[keyCode.DOWN.code])
            {
                System.out.println("down");
                //Physics.upplyF(pro.get(0), new Vect(POWER,(float)(3*Math.PI/2)));
                //Physics.upplyF(pro.get(1), new Vect(POWER,(float)(Math.PI/2)));
            }
            if (key[keyCode.RIGHT.code])
            {
                System.out.println("right");
                //Physics.upplyF(pro.get(0), new Vect(POWER,(float)(0)));
                //Physics.upplyF(pro.get(1), new Vect(POWER,(float)(Math.PI)));
            }
            if (key[keyCode.LEFT.code])
            {
                System.out.println("left");
                //Physics.upplyF(pro.get(0), new Vect(POWER,(float)(Math.PI)));
                //Physics.upplyF(pro.get(1), new Vect(POWER,(float)(0)));
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
            
            if (keyReleased[keyCode.GRAV.code])
            {
            	System.out.println("Gravity");
            	if (Physics.grav.getSize() != 0)
            		Physics.grav.setSize(0);
            	
            	else if (Physics.grav.getSize() == 0)
            		Physics.grav = new Vect(900, (float)(3*Math.PI/2));
            }
            
            if (keyReleased[keyCode.SGRAV.code] && (key[keyCode.UP.code] || key[keyCode.DOWN.code] || key[keyCode.RIGHT.code] || key[keyCode.LEFT.code]))
            {
            	System.out.println("Gravity sideways");
            	float dir;
            	Vect tempDir = new Vect(0,0.0);
            	Vect up = new Vect(1, (float)(Math.PI/2));
            	Vect down = new Vect(1, (float)(3*Math.PI/2));
            	Vect left = new Vect(1, (float)(Math.PI));
            	Vect right = new Vect(1, (float)(0));
            	
            	if (key[keyCode.UP.code])
            		tempDir = Vec_Math.vectAdd(tempDir, up);
            	
            	if (key[keyCode.DOWN.code])
            		tempDir = Vec_Math.vectAdd(tempDir, down);
            	
            	if (key[keyCode.LEFT.code])
            		tempDir = Vec_Math.vectAdd(tempDir, left);
            	
            	if (key[keyCode.RIGHT.code])
            		tempDir = Vec_Math.vectAdd(tempDir, right);
            	
            	dir = tempDir.getDir();
            	Physics.grav.setDir(dir);
            }
            
            
            if (mousePressed)
            	startLocation = new Coord(mouseLocation);
            if (!mousePressed && startLocation != null)
            {
            	endLocation = new Coord(mouseLocation);
            	switch(CurMode)
            	{
	            	case BALL:
	            	{
		            	double rad = Physics.CoordDist(startLocation, endLocation)/2;
		            	Coord cent = Physics.findMiddle(startLocation, endLocation);
		            	pro.add(new Proj(cent, rad));
		            	proSize++;
		            	break;
	            	}
	            	case WALL:
	            	{
	            		double minX = Math.min(startLocation._x, endLocation._x);
	            		double maxX = Math.max(startLocation._x, endLocation._x);
	            		double minY = Math.min(startLocation._y, endLocation._y);
	            		double maxY = Math.max(startLocation._y, endLocation._y);
	            		Coord c1 = new Coord(minX, maxY);
	            		Coord c2 = new Coord(maxX, minY);
	            		
	            		walls.add(new Wall(c1,c2));
	            		wallSize++;
	            		break;
	            	}
	            	case RWALL:
	            	{
	            		double rad = Physics.CoordDist(startLocation, endLocation)/2;
		            	Coord cent = Physics.findMiddle(startLocation, endLocation);
	            		walls.add(new RoundWall(cent, rad));
	            		wallSize++;
	            		break;
	            	}
					case VBALL:
					{
						
						double rad;
						if (pro.isEmpty())
							rad = PLAYER_SIZE/2;
						else
							rad = pro.get(pro.size()-1)._rad;
						Coord cent = startLocation;
						double dir = Math.atan2(startLocation._y - endLocation._y, startLocation._x - endLocation._x);
						double size = Physics.CoordDist(startLocation, endLocation)*10;
						if (size > 3000)	//to prevent passing through walls.
							size = 3000;
						
						Vect vel = new Vect ((float)size, (float)dir);
						if (key[keyCode.SHIFT.code])
						{
							if (Math.abs(vel.getX()) >=  Math.abs(vel.getY()))
								vel.setY(0);
							else if (Math.abs(vel.getX()) <  Math.abs(vel.getY()))
								vel.setX(0);
						}
						pro.add(new Proj(cent, vel, rad));
		            	proSize++;
						break;
					}
					default:
					{
						System.out.println("Got default in mode switch case");
						break;
					}
            	}
            	startLocation = null;
            	endLocation = null;
            }
            for (int i = 0; i < keyReleased.length ; i++)
            	keyReleased[i] = false;
            	
            
            	
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
                    	totalBounce++;
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
                    	totalBounce++;
                        System.out.println("bounce");
                       Physics.collision(pro.get(i),pro.get(j));
                    }
                }
            }
            
            for (int i = 0; i < proSize; i++) // apply speed to projectiles.
            {
        		pro.get(i).cord1._x += deltaT*pro.get(i)._vel.getX()/1000;	//divide by 1000 because messured by milliseconds.
        		pro.get(i).cord1._y += deltaT*pro.get(i)._vel.getY()/1000;
        		totalDist += deltaT*pro.get(i)._vel.getSize()/1000;
                
                if 		(pro.get(i).cord1._x < ((Wall)walls.get(1)).cord2._x ||	//if out of bounds
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
    	
        //Physics phy = new Physics();
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
        timer.scheduleAtFixedRate(gameloop, 0, 5);	//setting fps
    }
}