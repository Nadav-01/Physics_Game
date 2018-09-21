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
        /*
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
        */

        Vect temp = new Vect ((double)(a._x - b._x), (double)(a._y -b._y));
        Vect dir1 = new Vect((double)1,temp.getDir());
        Vect dir2 = new Vect((double)1,(float)(temp.getDir()+Math.PI/2));
        
        double aD1 = Vec_Math.dot_prod(dir1,a._vel);
        double aD2 = Vec_Math.dot_prod(dir2,a._vel);
        double bD1 = Vec_Math.dot_prod(dir1,b._vel);
        double bD2 = Vec_Math.dot_prod(dir2,b._vel);
        /*
        double aNewD1 = (aD1 * (a._mass - b._mass) + 2 * a._mass * bD1) / (a._mass + b._mass);
        double bNewD1 = (aD1 - bD1 + aNewD1);
        
        double aNewD2 = (aD2 * (a._mass - b._mass) + 2 * a._mass * bD2) / (a._mass + b._mass);
        double bNewD2 = (aD2 - bD2 + aNewD2);
        
        Vect aNewVel = new Vect(Vec_Math.retSizeMult(dir1,aNewD1));
        aNewVel = Vec_Math.vectAdd(aNewVel, new Vect(Vec_Math.retSizeMult(dir2,aNewD2)));
        
        Vect bNewVel = new Vect(Vec_Math.retSizeMult(dir1,bNewD1));
        bNewVel = Vec_Math.vectAdd(bNewVel, new Vect(Vec_Math.retSizeMult(dir2,bNewD2)));
        */
        double velAx = (aD1 * (a._mass - b._mass) + 2 * b._mass * bD1 ) / (a._mass + b._mass) * Math.cos(dir1.getDir()) + aD2 * Math.cos(dir2.getDir());
        double velAy = (aD1 * (a._mass - b._mass) + 2 * b._mass * bD1 ) / (a._mass + b._mass) * Math.sin(dir1.getDir()) + aD2 * Math.sin(dir2.getDir());
        double velBx = (bD1 * (b._mass - a._mass) + 2 * a._mass * aD1 ) / (a._mass + b._mass) * Math.cos(dir1.getDir()) + bD2 * Math.cos(dir2.getDir());
        double velBy = (bD1 * (b._mass - a._mass) + 2 * a._mass * aD1 ) / (a._mass + b._mass) * Math.sin(dir1.getDir()) + bD2 * Math.sin(dir2.getDir());
        
        a._vel = new Vect(velAx, velAy);
        b._vel = new Vect(velBx, velBy);
        
        /*
        if (a._mass > b._mass )
        {
            Proj bigger = new Proj(a);
            Proj smaller = new Proj(b);
            // Getting the speed for each new direction.
            double bD1 = Vec_Math.dot_prod(dir1,a._vel);    // Bigger
            double bD2 = Vec_Math.dot_prod(dir2,a._vel);
            double sD1 = Vec_Math.dot_prod(dir1,b._vel);    // Smaller
            double sD2 = Vec_Math.dot_prod(dir2,b._vel);
            
            double bNewD1 = (2 * bigger._mass * sD1) / (bigger._mass + smaller._mass);
            double sNewD1 = (bD1 - sD1 + bNewD1);
            
            double bNewD2 = (2 * bigger._mass * sD2) / (bigger._mass + smaller._mass);
            double sNewD2 = (bD2 - sD2 + bNewD2);
            
            Vect aNewVel = new Vect(Vec_Math.retSizeMult(dir1,bNewD1));
            aNewVel = Vec_Math.vectAdd(aNewVel, new Vect(Vec_Math.retSizeMult(dir2,bNewD2)));
            
            Vect bNewVel = new Vect(Vec_Math.retSizeMult(dir1,sNewD1));
            bNewVel = Vec_Math.vectAdd(bNewVel, new Vect(Vec_Math.retSizeMult(dir2,sNewD2)));
            
            a._vel = new Vect(aNewVel);
            b._vel = new Vect(bNewVel);
        }
        else
        {
            Proj bigger = new Proj(b);
            Proj smaller = new Proj(a);
            
            // Getting the speed for each new direction.
            double bD1 = Vec_Math.dot_prod(dir1,b._vel);    // Bigger
            double bD2 = Vec_Math.dot_prod(dir2,b._vel);
            double sD1 = Vec_Math.dot_prod(dir1,a._vel);    // Smaller
            double sD2 = Vec_Math.dot_prod(dir2,a._vel);
            
            double bNewD1 = (2 * bigger._mass * sD1) / (bigger._mass + smaller._mass);  // Using math formula developed by 2 equations of conservation of kinetic energy and momentum.
            double sNewD1 = (bD1 - sD1 + bNewD1);
            
            double bNewD2 = (2 * bigger._mass * sD2) / (bigger._mass + smaller._mass);
            double sNewD2 = (bD2 - sD2 + bNewD2);
            
            Vect bNewVel = new Vect(Vec_Math.retSizeMult(dir1,bNewD1));
            bNewVel = Vec_Math.vectAdd(bNewVel, new Vect(Vec_Math.retSizeMult(dir2,bNewD2)));
            
            Vect aNewVel = new Vect(Vec_Math.retSizeMult(dir1,sNewD1));
            aNewVel = Vec_Math.vectAdd(aNewVel, new Vect(Vec_Math.retSizeMult(dir2,sNewD2)));
            
            a._vel = new Vect(aNewVel);
            b._vel = new Vect(bNewVel);
        } */
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
}
