package src;

import java.util.LinkedList;


public class gamestate 
{
    public LinkedList<Proj> pro = new LinkedList<Proj>();
    public LinkedList<Item> walls = new LinkedList<Item>();

    public gamestate (LinkedList<Proj> p, LinkedList<Item> w)
    {
    	for (Proj pr : p)
    		pro.add(new Proj(pr));
    	
    	for (Item wa : w)
    		walls.add(wa);
    }
}
