# Physics_Game
School project based on 2d physics. Written in Java. Pretty crappy, nothing to see here.


16.9  - Commented on all previous code. Started using github to sync files.

19.9  - Replaced collision mechanics to be more natural and correct physically.



https://gamedev.stackexchange.com/questions/56017/java-best-implementation-keylistener-for-games

try improving the input to reduce lag between holding down the movement keys and the movement.


https://gamedevelopment.tutsplus.com/tutorials/when-worlds-collide-simulating-circle-circle-collisions--gamedev-769
https://www.gamedev.net/forums/topic/551488-2d-vector-based-collision/

improve the 2d collision


      Vect v1 = new Vect(a._vel);
    	Vect v2 = new Vect(b._vel);
    	Vect x1 = new Vect(a._x, a._y);
    	Vect x2 = new Vect(b._x, b._y);
    	
    	double m1 = a._mass;
    	double m2 = b._mass;
    	
    	
    	Vect v1_2 = Vec_Math.vectAdd(v1,v2.sizeMultRet(-1));
    	Vect x1_2 = Vec_Math.vectAdd(x1,x2.sizeMultRet(-1));
    	double dotProd1_2 = Vec_Math.dot_prod(v1_2,x1_2);
    	double x1_2Size = Vec_Math.dot_prod(x1_2,x1_2);
    	
    	Vect v2_1 = Vec_Math.vectAdd(v2,v1.sizeMultRet(-1));
    	Vect x2_1 = Vec_Math.vectAdd(x2,x1.sizeMultRet(-1));
    	double dotProd2_1 = Vec_Math.dot_prod(v2_1,x2_1);
    	double x2_1Size = Vec_Math.dot_prod(x2_1,x2_1);
    	
    	
    	Vect finalVect1 = new Vect (x1_2.sizeMultRet((-2*m2*dotProd1_2)/((m1+m2)*x1_2Size*x1_2Size )));
    	Vect finalVect2 = new Vect( x2_1.sizeMultRet((-2*m1*dotProd2_1)/((m2+m1)*x2_1Size*x2_1Size)));
    	
    	Vect u1 = Vec_Math.vectAdd(v1, finalVect1);
    	Vect u2 = Vec_Math.vectAdd(v2, finalVect2);
    	
    	a._vel = new Vect(u1);
    	b._vel = new Vect(u2);
      
      public Vect sizeMultRet(double scal)
    {
    	double size = _size, dir = _dir;
        if (scal > 0)
        {
            size *= scal;
        }
        else if (scal < 0)  // If the scaler is negetive, flips the vector.
        {
            size *= Math.abs(scal);
            if (dir >= Math.PI)
                dir -= Math.PI;
            else
                dir += Math.PI; 
        }
        else
        {
            size = 0;
            dir = 0;
        }
        return new Vect(size,dir);
    }
      
