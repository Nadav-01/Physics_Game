package broke;

//Implementation of a 2d vector in space. Cannot be put on the canvas by itself, for use only in velocity and force calculations.
public class Vect
{
 private double _size;
 private float _dir;
 
 /**
  * Constructor for vector using size and direction in radians.
  * to use this constructor you must use a float as d.
  */
 public Vect(double s, float d)
 {
     if (s < 0)  // Size must not be negetive.
     {
         System.out.println("Error: size smaller then 0 given to vector constructor.");
         s = 0;
     }
     else if (s > 0) // Makes sure the angle is in [0,2pi] to ease calculations.
     {
         while (d > 2*Math.PI)
         {
             d -= 2*Math.PI;
         }
         while (d < 0)
         {
             d += 2*Math.PI;
         }
     }
     else 
         d = 0;
         
     _size = s;
     _dir = d;
 }
 
 /**
  * Constructor for vector using 2 numbers to initilize it.
  * Created to ease vector addition.
  */
 public Vect(double x, double y)
 {
     _size = Math.sqrt( Math.pow(x,2) + Math.pow(y,2) );
     float d = 0;
     if      (x > 0 && y == 0)   // Calculates d based on the quarter of the coordinates the vector is at.
         ;
     else if (x > 0 && y > 0)
         d = (float)Math.atan( y / x );
     else if (x == 0 && y > 0)
         d = (float)(Math.PI/2 );
     else if (x < 0 && y > 0)
         d = (float)(Math.PI/2 + Math.atan( Math.abs(x) / y ));
     else if (x < 0 && y == 0)
         d = (float)(Math.PI);
     else if (x < 0 && y < 0)
         d = (float)(Math.PI + Math.atan( Math.abs(y) / Math.abs(x) ));
     else if (x == 0 && y < 0)
         d = (float)(3*Math.PI/2);   
     else if (x > 0 && y < 0)
         d = (float)(3*Math.PI/2 + Math.atan( x / Math.abs(y) ));
         
     _dir = d;
 }
 
 // Copy constructor.
 public Vect(Vect A)
 {
     _size = A.getSize();
     _dir = A.getDir();
 }
 
 public double getSize()
 {
     return _size;
 }
 
 public void setSize( double s)
 {
     if (s >= 0)
     {
         _size = s;
         return;
     }
     System.out.println("Error: size smaller then 0 given to vector constructor.");
 }
 
 public float getDir()
 {
     return _dir;
 }
 
 public void setDir( float d)
 {
     
     while (d > 2*Math.PI)
     {
         d -= 2*Math.PI;
     }
     while (d < 0)
     {
         d += 2*Math.PI;
     }
         
     _dir = d;
 }
 
 public double getX()
 {
     return Math.cos(_dir)*_size;
 }
 
 public double getY()
 {
     return Math.sin(_dir)*_size;
 }
 
 public void sizeMult(double scal)
 {
     if (scal > 0)
     {
         _size *= scal;
     }
     else if (scal < 0)  // If the scaler is negetive, flips the vector.
     {
         _size *= Math.abs(scal);
         if (_dir >= Math.PI)
             _dir -= Math.PI;
         else
             _dir += Math.PI; 
     }
     else
     {
         _size = 0;
         _dir = 0;
     }
 }
}