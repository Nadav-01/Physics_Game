package src;

// Super class of things that interact with each other on screen. Cannot be initialized on its own, must be a type.

public abstract class Item
{
    Coord cord1;

    
    public Item()
    {
        cord1 = new Coord(0,0);
    }
    public Item(double x, double y)
    {
        cord1 = new Coord(x,y);
    }
    
    public Item(Coord c)
    {
        cord1 = new Coord(c);
    }
    
    public abstract boolean isCol(Item other);  // Items on the screen must be able to interact with eachother.
}