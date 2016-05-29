package protein;

/*
 * CS2104 Project
 * 
 * Compiler: Java Sun
 * IDE: Eclipse
 * Authors: Justin Park, Shuaicheng Zhang, Rishi Pulluri, David Tchaou 
 * a coordinate system to represent all of our void spheres.
 * In our vectorized model points will represent a single dot in
 * an atom or outside it.
 */
public class Point 
{
	private double x;
	private double y;
	private double z;
	
	/*
	 * Constructor for Point class
	 * @param a X coordinate
	 * @param b Y coordinate
	 * @param c Z coordinate
	 */
	public Point(double a, double b, double c)
	{
		x = a;
		y = b;
		z = c;
	}
	
	/*
	 * Gets the x coordinate
	 * @return the x coordinate
	 */
	public double getX()
	{
		return x;
	}
	
	/*
	 * Gets the y coordinate
	 * @return the y coordinate
	 */
	public double getY()
	{
		return y;
	}
	
	/*
	 * Gets the z coordinate
	 * @return the z coordinate
	 */
	public double getZ()
	{
		return z;
	}
	
	/*
     * sets the x coordinate
     * @param numX sets the x coordinate
     */
	public void setX(double numX)
	{
		x = numX;
	}
	/*
     * sets the y coordinate
     * @param numY sets the y coordinate
     */
	public void setY(double numY)
	{
		y = numY;
	}
	
	
	/*
     * sets the z coordinate
     * @param numZ sets the z coordinate
     */
	public void setZ(double numZ)
	{
		z = numZ;
	}
	
	/*
	 * two points are equal if they have same x, y and z coordinates
	 * @param obj the point being compared to
	 * @return true if both the points are equal
	 */
	@Override
	public boolean equals(Object obj)
	{
		if ((obj == null) || obj.getClass() != Point.class)
        {
            return false;
        }
		Point other = (Point)obj;
		return (this.getX() == other.getX()) && (this.getY() == other.getY()) && (this.getZ() == other.getZ());
	}
	/*
	 * The string representation of a Point
	 * @return The string representation of the Point
	 */
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
