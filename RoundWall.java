package src;

//A wall that is in the shape of a ball.
//
public class RoundWall extends Item
{

	public double _rad;
	
	//constructor using x,y coordinates and a radius.
    public RoundWall(double x, double y, double rad)
    {
        super(x,y);
        _rad = rad;
    }
    
    public RoundWall(Coord c, double rad)
    {
        super(c);
        _rad = rad;
    }
    
    public boolean isCol(Item other)
    {
        if (other instanceof Proj)
        {
            // If distance between this and the other item is less then the sum of their radius, they are colliding.
            return (Physics.projDist(((Proj)other),this) <= _rad + ((Proj)other)._rad); 
        }
        else if (other instanceof Wall)
        {
            // Checks if the distance between the center of the projectile and any of the corners of  the wall is smaller then its radius.
        	Coord UL = other.cord1;
        	Coord UR = new Coord(((Wall)other).cord2._x, other.cord1._y);
        	Coord LL = new Coord(other.cord1._x,((Wall)other).cord2._y);
        	Coord LR = ((Wall)other).cord2;
        	
            return 	
            		(
            		(Math.abs(this.cord1._y - ((Wall)other).cord2._y) <= _rad	//the projectile comes from below
            		&&
            		this.cord1._x <= ((Wall)other).cord2._x
            		&&
            		this.cord1._x >= ((Wall)other).cord1._x)
            		||
            		(Math.abs(this.cord1._y - other.cord1._y) <= _rad	//the projectile comes from above
            		&&
            		this.cord1._x <= ((Wall)other).cord2._x
            		&&
            		this.cord1._x >= ((Wall)other).cord1._x)
            		||
            		(Math.abs(this.cord1._x - ((Wall)other).cord2._x) <= _rad	//the projectile comes from the right
            		&&
            		this.cord1._y <= ((Wall)other).cord1._y
            		&&
            		this.cord1._y >= ((Wall)other).cord2._y)
					||
            		(Math.abs(this.cord1._x - other.cord1._x) <= _rad	//the projectile comes from the left
            		&&
            		this.cord1._y <= ((Wall)other).cord1._y
            		&&
            		this.cord1._y >= ((Wall)other).cord2._y)
            		)
            		||
            		Physics.projDist(this, UL) <= _rad
            		||
            		Physics.projDist(this, UR) <= _rad
            		||
            		Physics.projDist(this, LL) <= _rad
            		||
            		Physics.projDist(this, LR) <= _rad
            		|| this.isWithin((Wall)other);
        }
        else if (other instanceof RoundWall)
        {
        	return (Physics.projDist(this,((RoundWall)other)) <= _rad + ((RoundWall)other)._rad); 
        }
        
        else
        {
            System.out.println("Error: got non Wall or Projectile item int isCol");
            return false;
        }
        
    }
    
    public boolean isWithin(Wall other) 
	{
		boolean ret;
		ret =
				this.cord1._x >= other.cord1._x
				&&
				this.cord1._x <= other.cord2._x
				&&
				this.cord1._y <= other.cord1._y
				&&
				this.cord1._y >= other.cord2._y;

		
		return ret;
	}
	
}
