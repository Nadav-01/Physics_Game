package src;

import java.awt.Dimension;

public class Physics
{
    public static Vect grav = new Vect(0.09, (float)(3*Math.PI/2));
    public static double airFric = 0.8;
    
    public Physics()
    {
        grav = new Vect(900, (float)(3*Math.PI/2));
    }
    
    // Upplys force on a projectile.
    public static void upplyF(Proj p, Vect f)
    {
    	long newT = System.currentTimeMillis();	//applying as much force as needed depending on how much time has passed since last frame.
    	long deltaT =  newT - attempt.oldT;
    	
        f.sizeMult(deltaT/(p._mass*1000));	// by formula v = v0 + at -> v = v0 + ft/m. divide by 1000 because messurment is in milliseconds.
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
        fric.setDir(fric.getDir() + (float)(Math.PI));	//friction is in opposite direction to velocity.
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
    
    public static double kineticE(Proj p)	//returns kinetic energy- 0.5 * m * v^2
    {
        return p._mass * Math.pow(p._vel.getSize(),2)/2;
    }
    
    public static double potenE(Proj p, Dimension winSize)	//returns potential (height) energy- m*g*h
    {
        return p._mass * grav.getSize()*(winSize.getHeight()-p._y);
    }
    
    public static double Energy(Proj p, Dimension winSize)	//returns total energy- E_k + U_g
    {
        return kineticE(p) + potenE(p, winSize);
    }
    
    public static Vect momentum(Proj p)	//returns momentum- m*v
    {
        Vect mom = new Vect(p._vel);
        mom.sizeMult(p._mass);
        return mom;
    }
    /*
     * only send 2 projectiles that are colliding now.
     * used formula from wikipedia.
     */
    public static void collision(Proj a, Proj b)
    {
    	
    	double angle = Math.atan(((a.getCentY() - b.getCentY())/(a.getCentX() - b.getCentX())));	//gets the angle between a and b.
		if (a.getCentX() < b.getCentX())	//if a is more left then b, add PI to the angle, so the math will work.
			angle += Math.PI;
    	
		Vect col1 = new Vect(Math.cos(angle), Math.sin(angle));
		Vect col2 = new Vect(-Math.sin(angle),Math.cos(angle));
		Matrix trans = new Matrix(col1,col2);
		
		col1 = new Vect(Math.cos(-angle), Math.sin(-angle));
		col2 = new Vect(-Math.sin(-angle),Math.cos(-angle));
		Matrix reverse = new Matrix(col1,col2);
		
    	Vect v1 = Vec_Math.transform(trans, new Vect(a._vel));
    	Vect v2 = Vec_Math.transform(trans, new Vect(b._vel));
    	
    	Vect x1 = Vec_Math.transform(trans, new Vect(a.getCentX(), a.getCentY()));
    	Vect x2 = Vec_Math.transform(trans, new Vect(b.getCentX(), b.getCentY()));
    	
    	
    	double m1 = a._mass;
    	double m2 = b._mass;
    	
    	
    	Vect v1_2 = Vec_Math.vectAdd(v1,Vec_Math.retSizeMult(v2,-1));
    	Vect x1_2 = Vec_Math.vectAdd(x1,Vec_Math.retSizeMult(x2,-1));
    	double dotProd1_2 = Vec_Math.dot_prod(v1_2,x1_2);
    	double x1_2Size = Vec_Math.dot_prod(x1_2,x1_2);
    	
    	Vect v2_1 = Vec_Math.vectAdd(v2,Vec_Math.retSizeMult(v1,-1));
    	Vect x2_1 = Vec_Math.vectAdd(x2,Vec_Math.retSizeMult(x1,-1));
    	double dotProd2_1 = Vec_Math.dot_prod(v2_1,x2_1);
    	double x2_1Size = Vec_Math.dot_prod(x2_1,x2_1);
    	
    	
    	Vect finalVect1 = new Vect (Vec_Math.retSizeMult(x1_2,(-2*m2*dotProd1_2)/((m1+m2)*x1_2Size )));
    	Vect finalVect2 = new Vect( Vec_Math.retSizeMult(x2_1,(-2*m1*dotProd2_1)/((m2+m1)*x2_1Size)));
    	
    	Vect u1 = Vec_Math.vectAdd(v1, finalVect1);
    	Vect u2 = Vec_Math.vectAdd(v2, finalVect2);
    	a._vel = Vec_Math.transform(reverse, new Vect(u1));
    	b._vel = Vec_Math.transform(reverse,new Vect(u2));
      

    	
    	/*
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
        b._vel = new Vect(velBx, velBy);*/
       
    }
    
    
    // Collides a projectile with a wall, by flipping the correct axis in its velocity.
    public static void collision(Proj a, Wall b)
    {

        if (!areColliding(a,b))
        {
            System.out.println("Error: cannot collide non colliding items");
            //return;
        }
        
        int corner = cornerCollision(a,b);
        if (corner == 0)
        {
	        a._vel.sizeMult(0.99);
	        if (a.getCentX() <= b._x)
	            Vec_Math.flipLeft(a._vel);
	        else if (a.getCentX() >= b._w)
	            Vec_Math.flipRight(a._vel);
	        else if (a.getCentY() <= b._y)
	            Vec_Math.flipUp(a._vel);
	        else
	            Vec_Math.flipDown(a._vel);
        }
        else
        {
        	//TODO - create class CircleWall and replace collision here in collision from there.
        	double x = 0, y = 0;
        	switch (corner) {
        	case 1:
        		x = b._x;
        		y = b._y;
        		break;
        	case 2:
        		x = b._w;
        		y = b._y;
        		break;
        	case 3:
        		x = b._w;
        		y = b._z;
        		break;
        	case 4:
        		x = b._x;
        		y = b._z;
        	}
        	
        	Proj temp = new Proj (x,y,3);
        	temp._mass = 10000000;
        	
        	collision(a,temp); 
        	
        }
    }
    
    public static int cornerCollision(Proj p, Wall other)
    {
    	
    		boolean fromBelow =	Math.abs(p.getCentY() - ((Wall)other)._z) <= p._rad;

    		boolean fromAbove =	Math.abs(other._y - p.getCentY()) <= p._rad;

        	boolean fromLeft = 	Math.abs(p.getCentX() - ((Wall)other)._w) <= p._rad;

        	boolean fromRight = Math.abs(other._x - p.getCentX()) <= p._rad;

    	
    	if (fromAbove&&fromLeft)
    		return 1;
    	if (fromAbove&&fromRight)
    		return 2;
    	if (fromBelow&&fromRight)
    		return 3;
    	if (fromBelow&&fromLeft)
    		return 4;
    	
    	return 0;
    }
    
    //checks if two items are colliding using the function built into them.
    public static boolean areColliding(Item a, Item b)
    {
        return a.isCol(b);
    }
    
    //returns the distance between the centers of 2 projectiles.
    public static double projDist(Proj a, Proj b)
    {
        return Math.sqrt(Math.pow(a.getCentX() - b.getCentX(),2) + Math.pow(a.getCentY() - b.getCentY(),2) );
    }

    //checks if there is overlap between two projectiles.
	public static boolean isOverlap(Proj a, Proj b) 
	{
		double dist = projDist(a,b);
		if (a._rad + b._rad > dist*0.95)	//checks if the distance between the projectiles is smaller then the sum of their radius'.
	    	return true;
    	return false;
	}

	//fixes overlap between two overlapping projectiles.
	public static void fixOverlap(Proj a, Proj b)
	{
		double dist = projDist(a,b);		
		double angle = Math.atan(((a.getCentY() - b.getCentY())/(a.getCentX() - b.getCentX())));	//gets the angle between a and b.
		if (a.getCentX() < b.getCentX())	//if a is more left then b, add PI to the angle, so the math will work.
			angle += Math.PI;
		
		double d = a._rad + b._rad - dist;
		d *= 1.05;
		
		a._x += d * Math.cos(angle) * b._mass/(a._mass + b._mass);	//changes the location of both projectiles,
		a._y += d * Math.sin(angle) * b._mass/(a._mass + b._mass);	//inversly proportional to their mass,
		b._x -= d * Math.cos(angle) * a._mass/(a._mass + b._mass);	//so they wont overlap anymore.
		b._y -= d * Math.sin(angle) * a._mass/(a._mass + b._mass);
	}
}