/*
 * Wall is a static, immovable rectangle Item that affects other items.
 */

public class Wall extends Item
{
    // X and Y of Item are the top left point. W and Z here are the bottom right point.
    public double _w;
    public double _z;
    /**
     * Constructor for objects of class Wall
     */
    public Wall()
    {
        super();
        _w = 0;
        _z = 0;
    }

    public Wall(double x, double y, double w, double z)
    {
        super(x,y);
        _w = w;
        _z = z;
    }
    
    public double getHeight()
    {
        return _z - super._y;
    }
    
    public void setHeight(double h)
    {
        _z = super._y + h;
    }
    
    public double getLength()
    {
        return _w - super._x;
    }
    
    public void setLength(double l)
    {
        _w = super._x + l;
    }
    
    public boolean isCol(Item other)
    {
        if (other instanceof Proj)
        {
            return (Math.abs(other._y - _z) <= ((Proj)other)._rad) || (Math.abs(other._y - _y) <= ((Proj)other)._rad) || (Math.abs(other._x - _w) <= ((Proj)other)._rad) || (Math.abs(other._x - _x) <= ((Proj)other)._rad);
        }
        
        else if (other instanceof Wall)
        {
            if (_x <= ((Wall)other)._x)
            {
                if (_y <= ((Wall)other)._y)
                {
                    return (((Wall)other)._x - _x <= this.getLength() && ((Wall)other)._y - _y <= this.getHeight());
                }
                else
                {
                    return (((Wall)other)._x - _x <= this.getLength() &&  _y - ((Wall)other)._y <= ((Wall)other).getHeight());
                }
            }
            else
            {
                if (_y <= ((Wall)other)._y)
                {
                    return ( _x - ((Wall)other)._x <= ((Wall)other).getLength() && ((Wall)other)._y - _y <= this.getHeight());
                }
                else
                {
                    return (_x - ((Wall)other)._x <= ((Wall)other).getLength() &&  _y - ((Wall)other)._y <= ((Wall)other).getHeight());
                }
            }
        }
        else
        {
            System.out.println("Error: got non Wall or Projectile item int isCol");
            return false;
        }
    }
}
