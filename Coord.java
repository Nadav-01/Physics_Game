package src;

public class Coord
{
	public double _x;
	public double _y;
	
	public Coord(double x, double y)
	{
		_x = x;
		_y = y;
	}
	
	
	public Coord(Coord c)
	{
		_x = c._x;
		_y = c._y;
	}
	
	public Coord intoJcoord()
	{
		return new Coord(_x, attempt.attempt.getHeight() -_y);
	}
	
	
	
	
}
