package src;

public abstract class Vec_Math
{
    
    public static double dot_prod(Vect A, Vect B)
    {
        return A.getX() * B.getX() + A.getY() * B.getY();
    }
    
    public static void sizeMult (Vect A, double s)
    {
        A.sizeMult(s);
    }
    
    public static Vect retSizeMult (Vect A, double s)
    {
        Vect temp = new Vect(A);
        temp.sizeMult(s);
        return temp;
    }
    
    public static Vect vectAdd(Vect A, Vect B)
    {
        return new Vect (A.getX() + B.getX(), A.getY() + B.getY());
    }
    
    public static void flipYaxis(Vect A)
    {
        A.setDir((float)(Math.PI) - A.getDir());
    }
    
    public static Vect rotate180(Vect A)
    {
    	Vect ret = new Vect(A);
        ret.setDir((float)(ret.getDir()+Math.PI));
        return ret;
    }
    
    
    public static void flipLeft(Vect A)
    {
        if (A.getX() > 0)
            A.setDir((float)(Math.PI) - A.getDir());
    }
    
    public static void flipRight(Vect A)
    {
        if (A.getX() < 0)
            A.setDir((float)(Math.PI) - A.getDir());
    }
    
    public static void flipXaxis(Vect A)
    {
        A.setDir(- A.getDir());
    }
    
    public static void flipUp(Vect A)
    {
        if (A.getY() < 0)
            A.setDir(- A.getDir());
    }
    
    public static void flipDown(Vect A)
    {
        if (A.getY() > 0)
            A.setDir(- A.getDir());
    }
}