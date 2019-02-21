package src;

import java.awt.Dimension;

public class Physics
{
    public static Vect grav = new Vect(0.09, (float)(3*Math.PI/2));
    public static double airFric = 0.8;
    
    public Physics()
    {
        grav = new Vect(0.09, (float)(3*Math.PI/2));
    }
    
    // Upplys force on a projectile.
    public static void upplyF(Proj p, Vect f)
    {
        f.sizeMult(1/p._mass);
        p._vel = Vec_Math.vectAdd(p._vel, f);
    }
    
    // Upplys Gravity on an array of projectiles.
    public static void upplyG(Proj p[], int n)
    {
        Vect Wei = new Vect(grav);
        for (int i = 0; i < n; i++)
        {
            Wei = new Vect(grav);
            Wei.sizeMult(p[i]._mass);
            upplyF(p[i],Wei);
        }
    }
    
    // Upplys friction on a projectile.
    public static void upplyFric(Proj p)
    {
        Vect fric = new Vect(p._vel);
        fric.setDir(fric.getDir() + (float)(Math.PI));
        fric.setSize(fric.getSize() * Physics.airFric);
        fric = new Vect(fric.getX()/3,fric.getY());
        Physics.upplyF(p, fric);
    }
    
    // Upplys friction on an array of projectiles.
    public static void upplyFric(Proj p[], int n)
    {
        for (int i = 0; i < n; i++)
            upplyFric(p[i]);
    }
    
    public static double kineticE(Proj p)
    {
        return p._mass * Math.pow(p._vel.getSize(),2)/2;
    }
    
    public static double potenE(Proj p, Dimension winSize)
    {
        return p._mass * grav.getSize()*(winSize.getHeight()-p._y);
    }
    
    public static double Energy(Proj p, Dimension winSize)
    {
        return kineticE(p) + potenE(p, winSize);
    }
    
    public static Vect momentum(Proj p)
    {
        Vect mom = new Vect(p._vel);
        mom.sizeMult(p._mass);
        return mom;
    }
    /*
     * only send 2 projectiles that are colliding now.
     */
    public static void collision(Proj a, Proj b)
    {
    	
        Vect temp = new Vect ((double)(b.getCentX() - a.getCentX()), (double)(b.getCentY() -a.getCentY()));
        Vect dir1 = new Vect((double)1,temp.getDir());
        Vect dir2 = new Vect((double)1,(float)(temp.getDir()+Math.PI/2));
        
        double aD1 = Vec_Math.dot_prod(dir1,a._vel);
        double aD2 = Vec_Math.dot_prod(dir2,a._vel);
        double bD1 = Vec_Math.dot_prod(dir1,b._vel);
        double bD2 = Vec_Math.dot_prod(dir2,b._vel);

        double velAx = (aD1 * (a._mass - b._mass) + 2 * b._mass * bD1 ) / (a._mass + b._mass) * Math.cos(dir1.getDir()) + aD2 * Math.cos(dir2.getDir());
        double velAy = (aD1 * (a._mass - b._mass) + 2 * b._mass * bD1 ) / (a._mass + b._mass) * Math.sin(dir1.getDir()) + aD2 * Math.sin(dir2.getDir());
        double velBx = (bD1 * (b._mass - a._mass) + 2 * a._mass * aD1 ) / (a._mass + b._mass) * Math.cos(dir1.getDir()) + bD2 * Math.cos(dir2.getDir());
        double velBy = (bD1 * (b._mass - a._mass) + 2 * a._mass * aD1 ) / (a._mass + b._mass) * Math.sin(dir1.getDir()) + bD2 * Math.sin(dir2.getDir());
        
        a._vel = new Vect(velAx, velAy);
        b._vel = new Vect(velBx, velBy);
       
    }
    
    
    // Collides a projectile with a wall, by flipping the correct axis in its velocity.
    public static void collision(Proj a, Wall b)
    {

        if (!areColliding(a,b))
        {
            System.out.println("Error: cannot collide non colliding items");
            //return;
        }
        a._vel.sizeMult(0.98);
        if (a.getCentX() <= b._x)
            Vec_Math.flipLeft(a._vel);
        else if (a.getCentX() >= b._w)
            Vec_Math.flipRight(a._vel);
        else if (a.getCentY() <= b._y)
            Vec_Math.flipUp(a._vel);
        else
            Vec_Math.flipDown(a._vel);
    }
    
    public static boolean areColliding(Item a, Item b)
    {
        return a.isCol(b);
    }
    
    
    public static double projDist(Proj a, Proj b)
    {
        return Math.sqrt(Math.pow(Math.abs(a.getCentX() - b.getCentX()),2) + Math.pow(Math.abs(a.getCentY() - b.getCentY()),2) );
    }

	public static boolean isOverlapse(Proj a, Proj b) 
	{
		double dist = Math.sqrt(Math.pow(a.getCentX()-b.getCentX(),2) + Math.pow(a.getCentY()-b.getCentY(),2));
    	if (a._rad + b._rad > dist*0.95)
	    	return true;
    	return false;
	}

	public static void fixOverlapse(Proj a, Proj b)
	{
		double dist = Math.sqrt(Math.pow(a.getCentX() - b.getCentX(),2) + Math.pow(a.getCentY()-b.getCentY(),2));
		double angle = Math.atan(((a.getCentY() - b.getCentY())/(a.getCentX() - b.getCentX())));
		double d = a._rad + b._rad - dist;
		d *= 1.01;
		
		a._x += d * Math.cos(angle) * b._mass/(a._mass + b._mass);
		a._y += d * Math.sin(angle) * b._mass/(a._mass + b._mass);
		b._x -= d * Math.cos(angle) * a._mass/(a._mass + b._mass);
		b._y -= d * Math.sin(angle) * a._mass/(a._mass + b._mass);
	}
}