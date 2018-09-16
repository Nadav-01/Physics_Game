import java.awt.Dimension;

public class Physics
{
    public static Vect grav = new Vect(0.09, (float)(3*Math.PI/2));
    public static double airFric = 0.005;
    
    public Physics()
    {
        grav = new Vect(0.09, (float)(3*Math.PI/2));
    }
    
    // Upplys force on a projectile.
    public static void upplyF(Proj p, Vect f)
    {
        p._vel = Vec_Math.vectAdd(p._vel,f);
    }
    
    // Upplys Gravity on an array of projectiles.
    public static void upplyG(Proj p[], int n)
    {
        for (int i = 0; i < n; i++)
            upplyF(p[i],grav);
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
        return Math.pow(p._vel.getSize(),2)/2;
    }
    
    
    public static double potenE(Proj p, Dimension winSize)
    {
        return grav.getSize()*(winSize.getHeight()-p._y);
    }
    
    public static double Energy(Proj p, Dimension winSize)
    {
        return kineticE(p) + potenE(p, winSize);
    }
    
    /*
     * only send 2 projectiles that are colliding now.
     */
    public static void collision(Proj a, Proj b)
    {
        if (!areColliding(a,b))
        {
            System.out.println("Error: cannot collide non colliding projectiles");
            //return;
        }
        Vect temp = new Vect ((double)(a._x - b._x), (double)(a._y -b._y));
        temp.setDir(temp.getDir()+ (float)Math.PI/2);
        float Dir = temp.getDir();
        
        a._vel.setDir(2*Dir-a._vel.getDir());
        b._vel.setDir(2*Dir-b._vel.getDir());
        a._vel.sizeMult(0.98);
        b._vel.sizeMult(0.98);
    }
    
    
    // Collides a projectile with a wall, by flipping the correct axis in its velocity.
    public static void collision(Proj a, Wall b)
    {
        if (!areColliding(a,b))
        {
            System.out.println("Error: cannot collide non colliding items");
            return;
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
}
