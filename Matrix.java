package src;

public class Matrix 
{
	public Vect _v1;	//vertical vectors
	public Vect _v2;
	
	public Matrix(double x1, double y1, double x2, double y2)
	{
		_v1 = new Vect(x1,y1);
		_v2 = new Vect(x2,y2);
	}
	
	public Matrix(Vect v1, Vect v2)
	{
		_v1 = new Vect(v1);
		_v2 = new Vect(v2);
	}
	
	public Matrix(Matrix other)
	{
		_v1 = new Vect(other._v1);
		_v2 = new Vect(other._v2);
	}
	
	public Vect getVvec1()
	{
		return new Vect(_v1);
	}
	
	public Vect getVvec2()
	{
		return new Vect(_v2);
	}
	
	public Vect getHvec1()
	{
		return new Vect (_v1.getX(),_v2.getX());
	}
	
	public Vect getHvec2()
	{
		return new Vect (_v1.getY(),_v2.getY());
	}
}
