package src;


import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Graphics;			//for graphics
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import javax.swing.JFrame;			//to render the frame
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Font;
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
		ERASE (11),
		SHIFT (12),
		PAUSE (13),
		CRAZY (14),
		FREEZE (15),
		REWIND (16),
		FORWARD (17)
		;
		public int code;
		keyCode(int code)
		{
			this.code = code;
		} 
	}
	static boolean[] key = new boolean[keyCode.values().length];
	static boolean[] keyReleased = new boolean[keyCode.values().length];
	static Timer timer = new Timer(true);
	static JTextField ipText = new JTextField(0);
	
	static String ip;
	
    enum mode {BALL, WALL, RWALL,VBALL, ERASE, PAUSE, CRAZY};
    static mode lastMode;
    static attempt attempt = new attempt();
    
    static final int PLAYER_SIZE = 60;
    static final int FPS = 1;
    static LinkedList<Proj> pro = new LinkedList<Proj>();
    static LinkedList<Item> walls = new LinkedList<Item>();
    static LinkedList<Proj> proPred= new LinkedList<Proj>();
    static LinkedList<gamestate> states = new LinkedList<gamestate>();
    static int curState = 0;
    static long lastSave = 0;
    							
    static int totalBounce = 0;
    static double totalDist = 0;
    static int proSize;
    static int wallSize;
    static mode CurMode = mode.BALL;
    static boolean FirstFreezeCheck = false;
    static boolean isFrozen = false;
    
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
    public void paintComponent(Graphics g) {
        //super.paint(g);
    	if (CurMode != mode.CRAZY)
    		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
                
        
        putItems(g2d);
        if(CurMode == mode.PAUSE)	//pause menu
        {
        	
        	
        	ipText.grabFocus();
        	Color tempCol = new Color(25,30,30,200);
        	g2d.setColor(tempCol);
        	g2d.fillRect(0, 0, attempt.getWidth(), attempt.getHeight());
 
        	g2d.setColor(Color.WHITE);
        	Font font = new Font ("Arial", 10, 50);
        	g2d.setFont(font);
        	String str = "Paused, move to another mode to unpause";
        	g2d.drawString(str, attempt.getWidth()/2- str.length()*12, attempt.getHeight()/2);
        	
        	font = new Font ("Arial", 10, 15);
        	g2d.setFont(font);
        	
        	g2d.drawString("if you want to connect to another computer, put the ip here: " , attempt.getWidth()/2- str.length()*12, attempt.getHeight()/2 + 50);
        	
        	ipText.setBounds(attempt.getWidth()/2 - str.length()*12 + 380, attempt.getHeight()/2 + 38, 200, 15);       
        	g2d.setColor(new Color(255,255,255,100));
        	g2d.fillRect(attempt.getWidth()/2 - str.length()*12 + 380, attempt.getHeight()/2 + 38, 200, 15);
        	

        	
        }
        else
        {
        	
        	ipText.setBounds(0,0,0,0);

        }
        	
    }
    
    public void putItems(Graphics2D g2d)
    {
    	
        g2d.setColor(Color.black);
        if (!pro.isEmpty())
        {
	        for (int i = 0; i < proSize; i++)	//paints projectiles
	        {
	        	if (i == 0)
	        		g2d.setColor(Color.blue);
	        	if (i == 1)
	        		g2d.setColor(Color.red);
	        	Putstuff.putProj(pro.get(i),g2d);   // Paint projectiles
	        	g2d.setColor(Color.black);
	        }
        }
        g2d.setColor(Color.red);
        //g2d.fillOval((int)(pro[1].cord1._x - pro[1]._rad), (int)(pro[1].cord1._y - pro[1]._rad), (int)pro[1]._rad*2, (int)pro[1]._rad*2);   // Paint negetive proj

        g2d.setColor(Color.black);
        for (int i = 0; i < wallSize; i++) // Paints walls
        {
        	if (!walls.isEmpty())
        	{
	        	if (walls.get(i) instanceof Wall)
	        		Putstuff.putWall((Wall)walls.get(i),g2d); 
	        	if (walls.get(i) instanceof RoundWall)
	        		Putstuff.putRoundwall((RoundWall)walls.get(i),g2d); 
        	}
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
				case ERASE:
					double minX = Math.min(startLocation._x, curMouseLoc._x);
            		double maxX = Math.max(startLocation._x, curMouseLoc._x);
            		double minY = Math.min(startLocation._y, curMouseLoc._y);
            		double maxY = Math.max(startLocation._y, curMouseLoc._y);
            		Coord c1 = new Coord(minX, maxY);
            		Coord c2 = new Coord(maxX, minY);
            		Wall temp = new Wall(c1,c2);
            		Putstuff.putErase(temp,g2d);
					break;
					
				default:
				{
					break;
				}
        	}
        }
        
        Coord loc = curMouseLoc;
        g2d.drawString(" mouse location: x = " + loc._x + " y = " + loc._y , 900, 150);
        if (!pro.isEmpty())
        {
        	g2d.setColor(Color.blue);
        	g2d.drawString("First proj: ", 200, 150);   // Debug info
        	g2d.setColor(Color.black);
        	g2d.drawString("dir = " + pro.get(0)._vel.getDir(), 200, 175);
        	g2d.drawLine(350, 175, 350 + (int)(10 * Math.cos(pro.get(0)._vel.getDir())), 175 - (int)(10 * Math.sin(pro.get(0)._vel.getDir())));
	        g2d.fillOval(347 + (int)(10 * Math.cos(pro.get(0)._vel.getDir())), 172 - (int)(10 * Math.sin(pro.get(0)._vel.getDir())), 5, 5);
	        g2d.drawString("speed = " + pro.get(0)._vel.getSize(), 200, 200);
	        g2d.drawString("rad = " + pro.get(0)._rad, 200, 225);
	        g2d.drawString("x = " + pro.get(0).cord1._x + "\t y = " + pro.get(0).cord1._y, 200, 250);
	        
	        if (pro.size() > 1)
	        {
	        	g2d.setColor(Color.red);
		        g2d.drawString("Second proj: ", 500, 150);   // Debug info
	        	g2d.setColor(Color.black);
	        	g2d.drawString("dir = " + pro.get(1)._vel.getDir(), 500, 175);
	        	g2d.drawLine(650, 175, 650 + (int)(10 * Math.cos(pro.get(1)._vel.getDir())), 175 - (int)(10 * Math.sin(pro.get(1)._vel.getDir())));
		        g2d.fillOval(647 + (int)(10 * Math.cos(pro.get(1)._vel.getDir())), 172 - (int)(10 * Math.sin(pro.get(1)._vel.getDir())), 5, 5);
		        g2d.drawString("speed = " + pro.get(1)._vel.getSize(), 500, 200);
		        g2d.drawString("rad = " + pro.get(1)._rad, 500, 225);
		        g2d.drawString("x = " + pro.get(1).cord1._x + "\t y = " + pro.get(1).cord1._y, 500, 250);
	        }
	        double energy = 0;
	        for (int i = 0; i < proSize; i++)
		        energy += Physics.Energy(pro.get(i));
	        g2d.drawString("Energy = " + energy , 200, 300);
        }
        g2d.drawString("num of Proj: " + proSize , 200, 400);
        g2d.drawString("total bounce's: " + totalBounce , 200, 450);
        g2d.drawString("total distance: " + totalDist , 200, 500);
        g2d.setColor(Color.white);
        g2d.drawString("Press B to add more balls, V to launch balls, W to add more walls, M to add more round walls, G to toggle gravity, and H + arrowkey to control gravity direction, E to erase" , 200, attempt.getHeight() - 50);
        g2d.drawString("(release H while the arrow keys are still held)" , 810, attempt.getHeight() - 30);
        
        g2d.drawString("FPS = " + 100/FPS , 1000, 50);
        if (ip != null)
        	g2d.drawString("ip = " + ip, 700,50);
        
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
		case ERASE:
			g2d.drawString("Current Mode: Erase" , 100, 50);
			break;
		case PAUSE:
			g2d.drawString("paused" , 100, 50);
			break;
		case CRAZY:
			g2d.drawString("CRAZY" , 100, 50);
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
        ipText.addKeyListener(Klistener);
        setFocusable(true);
    }
    
    

    public static class SaveState extends TimerTask
    {
	    

		@Override
		public void run() {
			if (!isFrozen)
			{
				states.add(new gamestate(pro,walls));
				curState = states.size()-1;
			}
		}
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
            
            if (key[keyCode.PAUSE.code])
            {
            	lastMode = CurMode;
            	System.out.println("pause");
            	CurMode = mode.PAUSE;
            }
            
            if (keyReleased[keyCode.GRAV.code])
            {
            	System.out.println("Gravity");
            	if (Physics.grav.getSize() != 0)
            		Physics.grav.setSize(0);
            	
            	else if (Physics.grav.getSize() == 0)
            		Physics.grav = new Vect(900, (float)(3*Math.PI/2));
            }
            
            if (isFrozen && keyReleased[keyCode.REWIND.code])
            {
            	if (curState > 1)
            		curState--;
            	
            	if (curState >= 0  && !states.isEmpty())
            	{
            		pro.clear();
            		walls.clear();
	            	for (Proj p : states.get(curState).pro)
	            	{
	            		pro.add(p);
	            	}
	            	for (Item w : states.get(curState).walls)
	            	{
	            		walls.add((Wall)w);
	            	}
            	}
            	proSize = pro.size();
            	wallSize = walls.size();
            }
            if (isFrozen && keyReleased[keyCode.FORWARD.code])
            {
            	if (curState < states.size())
            		curState++;
            	
            	if (curState >= 0 && curState < states.size() &&  !states.isEmpty())
            	{
            		pro.clear();
            		walls.clear();
	            	for (Proj p : states.get(curState).pro)
	            		pro.add(p);
	            	
	            	for (Item w : states.get(curState).walls)
	            		walls.add(w);
            	}
            	proSize = pro.size();
            	wallSize = walls.size();
            }
            
            if (key[keyCode.ERASE.code])
            {
            	System.out.println("Erase");
            	CurMode = mode.ERASE;
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
            if (key[keyCode.CRAZY.code])
            {
            	CurMode = mode.CRAZY;
            }
            if (key[keyCode.FREEZE.code] && !isFrozen)
            {
            	isFrozen = true;
            }
            
            if (keyReleased[keyCode.FREEZE.code] && !FirstFreezeCheck)
            {
            	FirstFreezeCheck = true;
            }
            else if (keyReleased[keyCode.FREEZE.code] && FirstFreezeCheck)
            {
            	isFrozen = false;;
            	FirstFreezeCheck = false;
            	
            	while (states.size() > curState+1)
            		states.remove(curState+1);
            }
            
            if (mousePressed && startLocation == null)
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
		            	if (rad != 0)
		            	{
		            		pro.add(new Proj(cent, rad));
		            		proSize++;
		            	}
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
					case ERASE:
	            	{
	            		int sumProErased = 0;
	            		int sumWallErased = 0;

	            		double minX = Math.min(startLocation._x, endLocation._x);
	            		double maxX = Math.max(startLocation._x, endLocation._x);
	            		double minY = Math.min(startLocation._y, endLocation._y);
	            		double maxY = Math.max(startLocation._y, endLocation._y);
	            		Coord c1 = new Coord(minX, maxY);
	            		Coord c2 = new Coord(maxX, minY);
	            		
	            		Wall temp = new Wall(c1,c2);
	            		
	            		for (int i = 0; i < proSize; i++)
	            		{
	            			if (pro.get(i).isCol2(temp))
	            			{
	            				pro.remove(i);
	            				proSize--;
	            				sumProErased++;
	            				i=-1;	//the for loop will automatilcally do i++;
	            			}
	            		}
	            		for (int i = 4; i < wallSize; i++)
	            		{
	            			if (walls.get(i).isCol(temp))
	            			{
	            				walls.remove(i);
	            				wallSize--;
	            				sumWallErased++;
	            				i=3;	//the for loop will automatilcally do i++;
	            			}
	            		}
	            		System.out.println("Erased " + sumProErased + " projectiles and " + sumWallErased + " walls");
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
        	if (CurMode == mode.PAUSE ||  isFrozen)
        		oldT = System.currentTimeMillis();	//if paused, make it so time doesnt pass
        	
        	long deltaT = newT - oldT;				//gets the difference of the times between last frame and now.
        	
        	if (CurMode != mode.PAUSE)
        		ipText.setText("");
        	
            // Upply gravity and friction to all projectiles.
        	if (!pro.isEmpty())
        	{
        		Physics.applyG(pro, proSize);
            	Physics.applyFric(pro, proSize);
        	}
            
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
                if (wallSize > 4)
                {
	        		if (pro.get(i).cord1._x < ((Wall)walls.get(1)).cord2._x)
	        			pro.get(i).cord1._x = ((Wall)walls.get(1)).cord2._x + pro.get(i)._rad - 0.5;
	        		
	        		if (pro.get(i).cord1._x > walls.get(2).cord1._x)
	        			pro.get(i).cord1._x = walls.get(2).cord1._x - pro.get(i)._rad + 0.5;
	        		
	        		if (pro.get(i).cord1._y < walls.get(0).cord1._y)
	        			pro.get(i).cord1._y = walls.get(0).cord1._y + pro.get(i)._rad - 0.5;
	        		
	        		if (pro.get(i).cord1._y > ((Wall)walls.get(3)).cord2._y)
	        			pro.get(i).cord1._y = ((Wall)walls.get(3)).cord2._y - pro.get(i)._rad + 0.5;
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
        TimerTask SaveState = new SaveState();
        
        Timer timer = new Timer(true);
        attempt.add(ipText);
        timer.scheduleAtFixedRate(gameloop, 0, FPS);	//setting fps
        timer.scheduleAtFixedRate(SaveState,0,200);
    }
}