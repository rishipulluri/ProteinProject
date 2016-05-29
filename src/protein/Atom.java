package protein;
/*
 * CS2104 Project
 * 
 * Compiler: Java Sun
 * IDE: Eclipse
 * 
 * Authors: Justin Park, Shuaicheng Zhang, Rishi Pulluri, David Tchaou 
 * This is the class representation of an atom 
 * which will be used to get the bounds the probe can
 * be placed in.
 */
public class Atom implements Comparable<Atom>
{
	private Point center;
	private double radius;
	
	/*
	 * intializes two values for atom's center and 
	 * radius
	 * @param c the atoms center
	 * @param r the atoms radius
	 */
	public Atom(Point c, double r)
	{
		center = c;
		radius = r;
	}
	
	/*
	 * @return The center point of the atom
	 * in terms of a x, y, z coordinate.
	 */
	public Point getCenter()
	{
		return center;
	}
	
	/*
	 * @return the radius of an atom in terms 
	 * of a double. Its accurate to six decimal 
	 * places
	 */
	public double getRadius()
	{
		return radius;
	}
	
	/*
	 * Calculates the distance between two points
	 * @param other The other point
	 * @return The distance between the two points
	 */
	public double calcDist(Atom other)
	{
		double a = Math.pow(center.getX() - other.getCenter().getX(), 2);
		double b = Math.pow(center.getY() - other.getCenter().getY(), 2);
		double c = Math.pow(center.getZ() - other.getCenter().getZ(), 2);
		return Math.sqrt(a+b+c) - (this.getRadius()+other.getRadius());
	}
	

	/*
	 * The equals method for the atom class. Two
	 * atoms are equal if both the center and the radius
	 * of the atoms are both the same
	 * @param obj the atom being compared to
	 * @return True if both atoms are equal
	 */
	@Override
	public boolean equals(Object obj)
	{
		if ((obj == null) || obj.getClass() != Atom.class)
        {
            return false;
        }
		Atom other = (Atom)obj;
		return this.getCenter().equals(other.getCenter()) && (this.getRadius() == other.getRadius());
	}
	
	
	/*
	 * @param other The other atom being compared to.
	 * 
	 * This method compares the center's x coordinate to the other atoms
	 * x-coordinate and if its equal, the method compares the y-coordinate
	 * and if this is equal, it compares the z-coordinate, then it returns 
	 * result which is any integer value of how far away each atoms x, y or
	 * z coordinates are
	 */
	@Override
	public int compareTo(Atom other)
	{
		int result = Double.compare(this.getCenter().getX(), other.getCenter().getX());
		if (result == 0)
		{
			result = Double.compare(this.getCenter().getY(), other.getCenter().getY());
		}
		if (result == 0)
		{
			result = Double.compare(this.getCenter().getZ(), other.getCenter().getZ());
		}
		return result;
	}
	
	
	/*
	 * @return the appended toString containing the radius and
	 * center's coordinates.
	 * toString method, the center and the radius of the atom
	 * is appended together.
	 */
	public String toString()
	{
		return "(" + center.toString() + ", " + radius + ")";
	}
}
