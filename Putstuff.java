package src;

import java.awt.Graphics2D;

public abstract class Putstuff
{
	public static void putProj(Proj p, Graphics2D g2d)
	{
		int x = (int)p.cord1.intoJcoord()._x;
		int y = (int)p.cord1.intoJcoord()._y;
		g2d.drawOval(x - (int)p._rad , y - (int)p._rad, (int)p._rad*2, (int)p._rad*2);  
		if (attempt.debug)
		{
			int X = (int) (p.cord1._x + 0.1*p._vel.getX());
			int Y = (int) (p.cord1._y + 0.1*p._vel.getY());
			Coord to = new Coord(X,Y);
			
			g2d.drawLine(x, y, (int)to.intoJcoord()._x, (int)to.intoJcoord()._y);
		}
		if (attempt.art)
		{
			int X = (int) (x + p._vel.getX());
			int Y = (int) (y + p._vel.getY());
			
			g2d.drawLine(x, y, X, Y);
		}
		
	}
	
	public static void putWall(Wall w, Graphics2D g2d)
	{
		int x = (int)w.cord1.intoJcoord()._x;
		int y = (int)w.cord1.intoJcoord()._y;
		g2d.fillRect(x, y, (int)w.getLength(),  (int)w.getHeight());  
	}
	
	public static void putErase(Wall w, Graphics2D g2d)
	{
		int x = (int)w.cord1.intoJcoord()._x;
		int y = (int)w.cord1.intoJcoord()._y;
		g2d.drawRect(x, y, (int)w.getLength(),  (int)w.getHeight());  
	}
	
	public static void putRoundwall(RoundWall w, Graphics2D g2d)
	{
		int x = (int)w.cord1.intoJcoord()._x;
		int y = (int)w.cord1.intoJcoord()._y;
		g2d.fillOval(x - (int)w._rad , y - (int)w._rad, (int)w._rad*2, (int)w._rad*2);  
	}
}
