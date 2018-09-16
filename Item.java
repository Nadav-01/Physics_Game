// Super class of things that interact with eachother on screen. Cannot be initialized on its own, must be a type.

public abstract class Item
{
    public int _x;
    public int _y;

    
    public Item()
    {
        _x = 0;
        _y = 0;
    }
    public Item(int x, int y)
    {
        _x = x;
        _y = y;
    }
    
    public abstract boolean isCol(Item other);  // Items on the screen must be able to interact with eachother.
}
