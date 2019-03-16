package src;

import java.awt.Graphics2D;

public abstract class Putstuff
{
	public static void putProj(Proj p, Graphics2D g2d)
	{
		int x = (int)p.cord1.intoJcoord()._x;
		int y = (int)p.cord1.intoJcoord()._y;
		g2d.drawOval(x - (int)p._rad , y - (int)p._rad, (int)p._rad*2, (int)p._rad*2);  
	}
	
	public static void putWall(Wall w, Graphics2D g2d)
	{
		int x = (int)w.cord1.intoJcoord()._x;
		int y = (int)w.cord1.intoJcoord()._y;
		g2d.fillRect(x, y, (int)w.getLength(),  (int)w.getHeight());  
	}
	
	public static void putRoundwall(RoundWall w, Graphics2D g2d)
	{
		int x = (int)w.cord1.intoJcoord()._x;
		int y = (int)w.cord1.intoJcoord()._y;
		g2d.fillOval(x - (int)w._rad , y + (int)w._rad, (int)w._rad*2, (int)w._rad*2);  
	}
}
