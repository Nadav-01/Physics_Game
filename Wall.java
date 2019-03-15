package src;

/*
 * Wall is a static, immovable rectangle Item that affects other items.
 */

public class Wall extends Item
{
    // X and Y of Item are the top left point. W and Z here are the bottom right point.
    public Coord cord2;
    /**
     * Constructor for objects of class Wall
     */
    public Wall()
    {
        super(0,0);
        cord2 = new Coord(0,0);
    }

    public Wall(double x, double y, double w, double z)
    {
        super(x,y);
        cord2 = new Coord(w,z);
    }
    
    public double getHeight()
    {
        return Math.abs(cord2._y - cord1._y);
    }
    
    public void setHeight(double h)
    {
        cord2._y = cord1._y + h;
    }
    
    public double getLength()
    {
        return Math.abs(cord2._x - cord1._x);
    }
    
    public void setLength(double l)
    {
        cord2._x = cord1._x + l;
    }
    
    //checks if the wall collides with another item.
    public boolean isCol(Item other)
    {
        if (other instanceof Proj)
        {
            return other.isCol(this);
        }
        
        else if (other instanceof Wall)
        {
            if (cord1._x <= ((Wall)other).cord1._x)
            {
                if (cord1._y <= ((Wall)other).cord1._y)
                {
                    return (((Wall)other).cord1._x - cord1._x <= this.getLength() && ((Wall)other).cord1._y - cord1._y <= this.getHeight());
                }
                else
                {
                    return (((Wall)other).cord1._x - cord1._x <= this.getLength() &&  cord1._y - ((Wall)other).cord1._y <= ((Wall)other).getHeight());
                }
            }
            else
            {
                if (cord1._y <= ((Wall)other).cord1._y)
                {
                    return ( cord1._x - ((Wall)other).cord1._x <= ((Wall)other).getLength() && ((Wall)other).cord1._y - cord1._y <= this.getHeight());
                }
                else
                {
                    return (cord1._x - ((Wall)other).cord1._x <= ((Wall)other).getLength() &&  cord1._y - ((Wall)other).cord1._y <= ((Wall)other).getHeight());
                }
            }
        }
        else if (other instanceof RoundWall)
        {
        	return other.isCol(this);
        }
        else
        {
            System.out.println("Error: got non Wall or Projectile item int isCol");
            return false;
        }
    }
}