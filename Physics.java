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
        return p._mass * grav.getSize()*(winSize.getHeight()-p.cord1._y);
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
    	if(Physics.isOverlap(a,b))
        {
            Physics.fixOverlap(a,b);
        }
    	double angle = Math.atan(((a.cord1._y - b.cord1._y)/(a.cord1._x - b.cord1._x)));	//gets the angle between a and b.
		if (a.cord1._x < b.cord1._x)	//if a is more left then b, add PI to the angle, so the math will work.
			angle += Math.PI;
    	
		Vect col1 = new Vect(Math.cos(angle), Math.sin(angle));
		Vect col2 = new Vect(-Math.sin(angle),Math.cos(angle));
		Matrix trans = new Matrix(col1,col2);
		
		col1 = new Vect(Math.cos(-angle), Math.sin(-angle));
		col2 = new Vect(-Math.sin(-angle),Math.cos(-angle));
		Matrix reverse = new Matrix(col1,col2);
		
    	Vect v1 = Vec_Math.transform(trans, new Vect(a._vel));
    	Vect v2 = Vec_Math.transform(trans, new Vect(b._vel));
    	
    	Vect x1 = Vec_Math.transform(trans, new Vect(a.cord1._x, a.cord1._y));
    	Vect x2 = Vec_Math.transform(trans, new Vect(b.cord1._x, b.cord1._y));
    	
    	
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
        Vect temp = new Vect ((double)(b.cord1._x - a.cord1._x), (double)(b.cord1._y -a.cord1._y));
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
    
    public static void collision(Proj a, Item b)
    {
    	if (b instanceof Proj)
    		collision(a,(Proj)b);
    	if (b instanceof Wall)
    		collision(a,(Wall)b);
    	if (b instanceof RoundWall)
    		collision(a,(RoundWall)b);
    }
    
    // Collides a projectile with a wall, by flipping the correct axis in its velocity.\
    public static void collision(Proj a, Wall b)
    {

        if (!areColliding(a,b))
        {
            System.out.println("Error: cannot collide non colliding items");
            //return;
        }
        
        if(Physics.isOverlap(a,b))
        {
            Physics.fixOverlap(a,b);
        }
        
        int corner = cornerCollision(a,b);
        if (corner == 0)
        {
	        a._vel.sizeMult(0.99);
	        if (a.cord1._x < b.cord1._x)
	            Vec_Math.flipLeft(a._vel);
	        else if (a.cord1._x > b.cord2._x)
	            Vec_Math.flipRight(a._vel);
	        else if (a.cord1._y > b.cord1._y)
	            Vec_Math.flipUp(a._vel);
	        else
	            Vec_Math.flipDown(a._vel);
        }
        else
        {
        	double x = 0, y = 0;
        	switch (corner) {
        	case 1:
        		x = b.cord1._x;
        		y = b.cord1._y;
        		break;
        	case 2:
        		x = b.cord2._x;
        		y = b.cord1._y;
        		break;
        	case 3:
        		x = b.cord2._x;
        		y = b.cord2._y;
        		break;
        	case 4:
        		x = b.cord1._x;
        		y = b.cord2._y;
        	}
        	
        	double angle = Math.tan((a.cord1._y - y)/(a.cord1._x - x));
        	angle += Math.PI/2;
        	Vec_Math.flipAxis(a._vel, angle);
        }
        long newT = System.currentTimeMillis();
    	long deltaT =  newT - attempt.oldT;
        a.cord1._x += deltaT*a._vel.getX()/1000;	//divide by 1000 because messured by milliseconds.
		a.cord1._y += deltaT*a._vel.getY()/1000;
    }
    public static void collision(Proj a, RoundWall b)
    {
    	
    	if(Physics.isOverlap(a,b))
        {
            Physics.fixOverlap(a,b);
        }
    	
    	double angle = Math.atan(((a.cord1._y - b.cord1._y)/(a.cord1._x - b.cord1._x)));	//gets the angle between a and b.
		//if (a.cord1._x < b.cord1._x)	//if a is more left then b, add PI to the angle, so the math will work.
			//angle += Math.PI;
    	
    	angle += Math.PI/2;
    	Vec_Math.flipAxis(a._vel,angle);
    	
    	
    }
    
    
    public static int cornerCollision(Proj p, Wall other)
    {
    	
    		boolean fromBelow =	p.cord1._y < ((Wall)other).cord2._y;

    		boolean fromAbove =	p.cord1._y > other.cord1._y;

        	boolean fromRight = p.cord1._x  > ((Wall)other).cord2._x;

        	boolean fromLeft = p.cord1._x < other.cord1._x;

        	int ret;
        	
    	if (fromAbove&&fromLeft)
    		ret = 1;
    	else if (fromAbove&&fromRight)
    		ret = 2;
    	else if (fromBelow&&fromRight)
    		ret = 3;
    	else if (fromBelow&&fromLeft)
    		ret = 4;
    	else 
    		ret = 0;
    	System.out.println(ret);
    	return ret;
    	
    }
    
    //checks if two items are colliding using the function built into them.
    public static boolean areColliding(Item a, Item b)
    {
        return a.isCol(b);
    }
    
    //returns the distance between the centers of 2 projectiles.
    public static double projDist(Proj a, Proj b)
    {
        return Math.sqrt(Math.pow(a.cord1._x - b.cord1._x,2) + Math.pow(a.cord1._y - b.cord1._y,2) );
    }
    
    //returns the distance between the centers of a projectile and a round wall.
    public static double projDist(Proj a, RoundWall b)
    {
        return Math.sqrt(Math.pow(a.cord1._x - b.cord1._x,2) + Math.pow(a.cord1._y - b.cord1._y,2) );
    }

  //returns the distance between the centers of 2 round walls.
    public static double projDist(RoundWall a, RoundWall b)
    {
        return Math.sqrt(Math.pow(a.cord1._x - b.cord1._x,2) + Math.pow(a.cord1._y - b.cord1._y,2) );
    }
    
    
    public static double projDist(Proj a, Coord b)
    {
    	return Math.sqrt(Math.pow(a.cord1._x - b._x,2) + Math.pow(a.cord1._y - b._y,2) );
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
		double angle = Math.atan(((a.cord1._y - b.cord1._y)/(a.cord1._x - b.cord1._x)));	//gets the angle between a and b.
		if (a.cord1._x < b.cord1._x)	//if a is more left then b, add PI to the angle, so the math will work.
			angle += Math.PI;
		
		double d = a._rad + b._rad - dist;
		d *= 1.05;
		
		a.cord1._x += d * Math.cos(angle) * b._mass/(a._mass + b._mass);	//changes the location of both projectiles,
		a.cord1._y += d * Math.sin(angle) * b._mass/(a._mass + b._mass);	//inversly proportional to their mass,
		b.cord1._x -= d * Math.cos(angle) * a._mass/(a._mass + b._mass);	//so they wont overlap anymore.
		b.cord1._y -= d * Math.sin(angle) * a._mass/(a._mass + b._mass);
	}
	
	//checks if there's overlap between a projectile and a wall
	public static boolean isOverlap(Proj a, Wall b) 
	{
		Coord UL = b.cord1;
    	Coord UR = new Coord(((Wall)b).cord2._x, b.cord1._y);
    	Coord LL = new Coord(b.cord1._x,((Wall)b).cord2._y);
    	Coord LR = ((Wall)b).cord2;
    	
		return ((Math.abs(a.cord1._y - b.cord2._y) < a._rad
        		&&
        		a.cord1._x < b.cord2._x + a._rad
        		&&
        		a.cord1._x > b.cord1._x - a._rad)
        		||
        		(Math.abs(b.cord1._y - a.cord1._y) < a._rad
        		&&
        		a.cord1._x < b.cord2._x + a._rad 
        		&&
        		a.cord1._x > b.cord1._x - a._rad )
        		||
        		(Math.abs(a.cord1._x - b.cord2._x) < a._rad
        		&&
        		a.cord1._y < b.cord2._y - a._rad
        		&&
        		a.cord1._y > b.cord1._y + a._rad)
				||
        		(Math.abs(b.cord1._x - a.cord1._x) < a._rad
        		&&
        		a.cord1._y < b.cord2._y - a._rad
        		&&
        		a.cord1._y > b.cord1._y + a._rad))
				||
        		Physics.projDist(a, UL) <= a._rad
        		||
        		Physics.projDist(a, UR) <= a._rad
        		||
        		Physics.projDist(a, LL) <= a._rad
        		||
        		Physics.projDist(a, LR) <= a._rad
        		;
	}
	
	//fixes overlap between overlapping projectile and wall.
	public static void fixOverlap(Proj a, Wall b)
	{
		
		
		double dir = a._vel.getDir()+Math.PI;
		while (isOverlap(a,b))	//moves the projectile on the opposite direction to its speed, until it isnt overlapping with the wall anymore.
		{
			a.cord1._x += Math.cos(dir);
			a.cord1._y += Math.sin(dir);
		}
		//a.cord1._x += Math.cos(dir);
		//a.cord1._y += Math.sin(dir);
		/*
		int c = cornerCollision(a,b);
		if (c == 0)
		{
			boolean fromBelow =	a.cord1._y <= b.cord2._y;
	
			boolean fromAbove =	a.cord1._y >= b.cord1._y;
	
	    	boolean fromLeft = 	a.cord1._x <= b.cord1._x;
	
	    	boolean fromRight = a.cord1._x >= b.cord2._x;
	    	
	    	
	    	
			if (fromBelow)
				a.cord1._y = b.cord2._y - a._rad - 1;
			else if (fromAbove)
				a.cord1._y = b.cord1._y + a._rad + 1;
			else if (fromLeft)
				a.cord1._x = b.cord1._x - a._rad - 1;
			else if (fromRight)
				a.cord1._x = b.cord2._x + a._rad + 1;
		}
		else
		{
			double x = 0, y = 0;
        	switch (c) {
        	case 1:
        		x = b.cord1._x;
        		y = b.cord1._y;
        		break;
        	case 2:
        		x = b.cord2._x;
        		y = b.cord1._y;
        		break;
        	case 3:
        		x = b.cord2._x;
        		y = b.cord2._y;
        		break;
        	case 4:
        		x = b.cord1._x;
        		y = b.cord2._y;
        	}
        	Coord corner = new Coord(x,y);
			double dist = projDist(a,corner);		
			double angle = Math.atan(((a.cord1._y - corner._y)/(a.cord1._x - corner._x)));
			if (a.cord1._x < corner._x)
				angle += Math.PI;
			
			
			a.cord1._x +=  Math.cos(angle);
			a.cord1._y +=  Math.sin(angle);	
			*/
		//}
		
    	/*
		double dir = a._vel.getDir()-Math.PI;
		while (isOverlap(a,b))	//moves the projectile on the opposite direction to its speed, until it isnt overlapping with the wall anymore.
		{
			a.cord1._x += Math.cos(dir)/10;
			a.cord1._y -= Math.sin(dir)/10;
		}
    */
		
	}
	
	public static boolean isOverlap(Proj a, RoundWall b) 
	{
		double dist = projDist(a,b);
		if (a._rad + b._rad > dist)	//checks if the distance between the projectiles is smaller then the sum of their radius'.
	    	return true;
    	return false;
	}

	//fixes overlap between two overlapping projectiles.
	public static void fixOverlap(Proj a, RoundWall b)
	{
		double dir = a._vel.getDir()+Math.PI;
		while (isOverlap(a,b))	//moves the projectile on the opposite direction to its speed, until it isnt overlapping with the wall anymore.
		{
			a.cord1._x += Math.cos(dir);
			a.cord1._y += Math.sin(dir);
		}
		a.cord1._x += Math.cos(dir);
		a.cord1._y += Math.sin(dir);
	}
}