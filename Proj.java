package src;

public class Proj extends Item
{
    
    // X and Y from Item represent top left of cube containing the ball
    public Vect _vel = new Vect(0,0);
    public double _rad;
    public double _mass;
    
    //constructor using x,y coordinates and a radius.
    public Proj(double x, double y, double rad)
    {
        super(x,y);
        _rad = rad;
        _mass = Math.PI * Math.pow(_rad,2);
    }
    
    //constructor using x,y coordinates, velocity x and y coordinates, and a radius.
    public Proj(double x, double y, double velX, double velY, double rad)
    {
    	super(x,y);
        _vel = new Vect(velX, velY);
        _rad = rad;
        _mass = Math.PI * Math.pow(_rad,2);
    }
    
    //copy constructor.
	public Proj(Proj p)
    {
		super(p.cord1._x,p.cord1._y);
        _vel = new Vect(p._vel.getX(), p._vel.getY());
        _rad = p._rad;
        _mass = p._mass;
    }

    //TODO- make these function unnecessary by using the x and y values in item as the center.
    
    //checks if another item is colliding with this projectile.
    public boolean isCol(Item other)
    {
        if (other instanceof Proj)
        {
            // If distance between this and the other item is less then the sum of their radius, they are colliding.
            return (Physics.projDist(this,((Proj)other)) <= _rad + ((Proj)other)._rad); 
        }
        else if (other instanceof Wall)
        {
            // Checks if the distance between the center of the projectile and any of the corners of  the wall is smaller then its radius.
            return 	
            		((Math.abs(this.cord1._y - ((Wall)other).cord2._y) <= _rad
            		&&
            		this.cord1._x <= ((Wall)other).cord2._x + _rad
            		&&
            		this.cord1._x >= ((Wall)other).cord1._x - _rad)
            		||
            		(Math.abs(other.cord1._y - this.cord1._y) <= _rad
            		&&
            		this.cord1._x <= ((Wall)other).cord2._x + _rad 
            		&&
            		this.cord1._x >= ((Wall)other).cord1._x - _rad )
            		||
            		(Math.abs(this.cord1._x - ((Wall)other).cord2._x) <= _rad
            		&&
            		this.cord1._y <= ((Wall)other).cord2._y + _rad
            		&&
            		this.cord1._y >= ((Wall)other).cord1._y - _rad)
					||
            		(Math.abs(other.cord1._x - this.cord1._x) <= _rad
            		&&
            		this.cord1._y <= ((Wall)other).cord2._y + _rad
            		&&
            		this.cord1._y >= ((Wall)other).cord1._y - _rad));
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
}
