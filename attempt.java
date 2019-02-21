package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("serial")
public class attempt extends JPanel {
    
    // Define code of arrow keys
    static int up = KeyEvent.VK_UP; 
    static int right = KeyEvent.VK_RIGHT;
    static int down = KeyEvent.VK_DOWN;
    static int left = KeyEvent.VK_LEFT;
    static int reset = KeyEvent.VK_R;
    
    
    static final int PLAYER_SIZE = 60;
    static Proj[] pro; // Projectile array
    static Wall[] walls = {new Wall(-20,480,680,520), new Wall(-20,-20,20,550), new Wall(630,-20,670,550), new Wall(-20,-20,680,20)};   // Wall array
    static int proSize;
    static int wallSize = walls.length;
    
    //static Proj pro[0] = new Proj(300,300,PLAYER_SIZE/2);
    //static Proj c = new Proj(300,500,PLAYER_SIZE/2);
    
    
    public static void inintilizeProj()
    {
    	pro = new Proj[] { new Proj(300,300,PLAYER_SIZE*1.5) , new Proj(50,50,PLAYER_SIZE/3) }; 
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
        g2d.setColor(Color.red);
        g2d.fillOval((int)pro[1]._x, (int)pro[1]._y, (int)pro[1]._rad*2, (int)pro[1]._rad*2);   // Paint negetive player
        //g2d.drawRect(pro[1]._x, pro[1]._y, PLAYER_SIZE, PLAYER_SIZE);
        
        g2d.setColor(Color.black);
        
        for (int i = 0; i < wallSize; i++) // Paints walls
            g2d.fillRect((int)walls[i]._x, (int)walls[i]._y, (int)walls[i].getLength(), (int)walls[i].getHeight());
        
        g2d.drawOval((int)pro[0]._x, (int)pro[0]._y, (int)pro[0]._rad*2, (int)pro[0]._rad*2);   // Paint player
        //g2d.drawRect(pro[0]._x, pro[0]._y, PLAYER_SIZE, PLAYER_SIZE);
        
        g2d.drawString("speed = " + pro[0]._vel.getSize(), 200, 100);   // Debug info
        g2d.drawString("dir = " + pro[0]._vel.getDir(), 200, 150);
        g2d.drawLine(350, 150, 350 + (int)(10 * Math.cos(pro[0]._vel.getDir())), 150 - (int)(10 * Math.sin(pro[0]._vel.getDir())));
        g2d.fillOval(347 + (int)(10 * Math.cos(pro[0]._vel.getDir())), 147 - (int)(10 * Math.sin(pro[0]._vel.getDir())), 5, 5);
        g2d.drawString("x = " + pro[0]._x + "\t y = " + pro[0]._y, 200, 200);
        g2d.drawString("Energy = " + Physics.Energy(pro[0],this.getSize()) , 200, 250);
    }
    
    public attempt() {  // Implementing keylistener
        KeyListener listener = new MyKeyListener();
        addKeyListener(listener);
        setFocusable(true);
    }
    
    

    public class MyKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int action = e.getExtendedKeyCode();
            if (action == up)
            {
                System.out.println("up");
                Physics.upplyF(pro[0], new Vect(500,(float)(Math.PI/2)));
                Physics.upplyF(pro[1], new Vect(400,(float)(3*Math.PI/2)));
            }
            if (action == down)
            {
                System.out.println("down");
                Physics.upplyF(pro[0], new Vect(400,(float)(3*Math.PI/2)));
                Physics.upplyF(pro[1], new Vect(500,(float)(Math.PI/2)));
            }
            if (action == right)
            {
                System.out.println("right");
                Physics.upplyF(pro[0], new Vect(400,(float)(0)));
                Physics.upplyF(pro[1], new Vect(400,(float)(Math.PI)));
            }
            if (action == left)
            {
                System.out.println("left");
                Physics.upplyF(pro[0], new Vect(400,(float)(Math.PI)));
                Physics.upplyF(pro[1], new Vect(400,(float)(0)));
            }
            if (action == reset)
            {
            	System.out.println("reset");
            	inintilizeProj();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
    
    public static class gameloop extends TimerTask
    {
        public attempt at;
        public gameloop(attempt att)
        {
            at = att;
        }
        public void run()
        {
            // Upply gravity and friction to all projectiles.
            
            Physics.upplyG(pro, 2);
            Physics.upplyFric(pro, 2);
            

            for (int i = 0; i < proSize; i++) // Check all combination of items that can collide with each other
            {
                for (int j = 0; j < wallSize; j++)
                {
                    //if (Physics.areColliding((Item)pred[i],(Item)walls[j]))
                    if (Physics.areColliding((Item)pro[i],(Item)walls[j]))
                    {
                        System.out.println("bounce");
                        Physics.collision(pro[i],walls[j]);
                    }
                }
            }
            //if(Physics.areColliding(pred[0],pred[1]))
            if(Physics.areColliding(pro[0],pro[1]))
            {
                Physics.collision(pro[0],pro[1]);
                if(Physics.isOverlapse(pro[0],pro[1]))
                {
                    Physics.fixOverlapse(pro[0],pro[1]);
                }
            }
            
            
            for (int i = 0; i < proSize; i++) // Upply speed to projectiles.
            {
                double curX = pro[i]._vel.getX();
                double currX = pro[i]._x;
                pro[i]._x += pro[i]._vel.getX();
                pro[i]._y -= pro[i]._vel.getY();    //coordinate system flipped because window starts in upper left.
                
                if (pro[i]._x < -1000 || pro[i]._x > 2000 || pro[i]._y < -1000 || pro[i]._y > 2000)
                {
                    pro[i]._x = 50 + i*60;
                    pro[i]._y = 50 + i*60;
                    if (pro[i]._vel.getSize() > 500)
                        pro[i]._vel.setSize(50);
                }
            } 
            
            
            
            at.repaint();
        }
    }
    
    public static void main(String[] args)
    {
    	inintilizeProj();
        Physics phy = new Physics();
        JFrame frame = new JFrame("game");
        
        attempt attempt = new attempt();
        frame.add(attempt);
        frame.setSize(680, 550);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //int flipXcnt = 1;
        //int flipYcnt = 1;
        TimerTask gameloop = new gameloop(attempt);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(gameloop, 0, 10);
    }
}