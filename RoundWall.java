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
            return 	
            		((Math.abs(this._y - ((Wall)other)._z) <= _rad
            		&&
            		this._x <= ((Wall)other)._w + _rad
            		&&
            		this._x >= ((Wall)other)._x - _rad)
            		||
            		(Math.abs(other._y - this._y) <= _rad
            		&&
            		this._x <= ((Wall)other)._w + _rad 
            		&&
            		this._x >= ((Wall)other)._x - _rad )
            		||
            		(Math.abs(this._x - ((Wall)other)._w) <= _rad
            		&&
            		this._y <= ((Wall)other)._z + _rad
            		&&
            		this._y >= ((Wall)other)._y - _rad)
					||
            		(Math.abs(other._x - this._x) <= _rad
            		&&
            		this._y <= ((Wall)other)._z + _rad
            		&&
            		this._y >= ((Wall)other)._y - _rad));
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
