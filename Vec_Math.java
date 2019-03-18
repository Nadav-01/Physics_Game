package src;

public abstract class Vec_Math
{
    //dot product between 2d vectors.
    public static double dot_prod(Vect A, Vect B)
    {
        return A.getX() * B.getX() + A.getY() * B.getY();
    }
    
    //scalar multiplication.
    public static void sizeMult (Vect A, double s)
    {
        A.sizeMult(s);
    }
    
    //returns a new vector instead of changing the given vector.
    public static Vect retSizeMult (Vect A, double s)
    {
        Vect temp = new Vect(A);
        temp.sizeMult(s);
        return temp;
    }
    
    //adds two 2d vectors.
    public static Vect vectAdd(Vect A, Vect B)
    {
        return new Vect (A.getX() + B.getX(), A.getY() + B.getY());
    }
    
    //flips the y axis of a vector.
    public static void flipYaxis(Vect A)
    {
        A.setDir((float)(Math.PI) - A.getDir());
    }
    
    //makes the vector point left.
    public static void flipLeft(Vect A)
    {
        if (A.getX() > 0)
            A.setDir((float)(Math.PI) - A.getDir());
    }
    
  //makes the vector point right.
    public static void flipRight(Vect A)
    {
        if (A.getX() < 0)
            A.setDir((float)(Math.PI) - A.getDir());
    }
    
    //flips the x axis of a vector.
    public static void flipXaxis(Vect A)
    {
        A.setDir(- A.getDir());
    }
    
    //makes the vector point up.
    public static void flipUp(Vect A)
    {
        if (A.getY() < 0)
            A.setDir(- A.getDir());
    }
    
    //makes the vector point down.
    public static void flipDown(Vect A)
    {
        if (A.getY() > 0)
            A.setDir(- A.getDir());
    }
    
    public static Vect transform(Matrix a, Vect b)
    {
    	 Vect ret = new Vect(dot_prod(a.getHvec1(),b),dot_prod(a.getHvec2(),b));
    	 return ret;	
    }

	public static void flipAxis(Vect v, double angle)
	{
		while (angle > Math.PI)
			angle -= Math.PI;
		while (angle < 0)
			angle += Math.PI;
		
		double newDir = angle - v.getDir();
		v.setDir((float)(angle +  newDir));
	}
    
}