package src;

public class Proj extends Item
{
    
    // X and Y from Item represent top left of cube containing the ball
    public Vect _vel = new Vect(0,0);
    public double _rad;
    public double _mass;
    
    public Proj(double x, double y, double rad)
    {
        super(x,y);
        _rad = rad;
        _mass = Math.PI * Math.pow(_rad,2);
    }
    public Proj(double x, double y, double velX, double velY, double rad)
    {
        _x = x;
        _y = y;
        _vel = new Vect(velX, velY);
        _rad = rad;
        _mass = Math.PI * Math.pow(_rad,2);
    }
	public Proj(Proj p)
    {
        _x = p._x;
        _y = p._y;
        _vel = new Vect(p._vel.getX(), p._vel.getY());
        _rad = p._rad;
        _mass = p._mass;
    }

    
    public double getCentX()
    {
        return _x + _rad;
    }
    
    public double getCentY()
    {
        return _y + _rad;
    }
    
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
            return (Math.abs(this.getCentY() - ((Wall)other)._z) <= _rad) || (Math.abs(other._y - this.getCentY()) <= _rad) || (Math.abs(this.getCentX() - ((Wall)other)._w) <= _rad) || (Math.abs(other._x - this.getCentX()) <= _rad);
        }
        else
        {
            System.out.println("Error: got non Wall or Projectile item int isCol");
            return false;
        }
        
    }
}
