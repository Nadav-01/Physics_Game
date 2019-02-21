package broke;

// Super class of things that interact with eachother on screen. Cannot be initialized on its own, must be a type.

public abstract class Item
{
    public double _x;
    public double _y;

    
    public Item()
    {
        _x = 0;
        _y = 0;
    }
    public Item(double x, double y)
    {
        _x = x;
        _y = y;
    }
    
    public abstract boolean isCol(Item other);  // Items on the screen must be able to interact with eachother.
}