package protein;

/**
 * CS2104 Project
 * 
 * Compiler: Java Sun
 * IDE: Eclipse
 * 
 * Authors: Justin Park, Shuaicheng Zhang, Rishi Pulluri, David Tchaou 
 * The vector is used to determine how much distance between the
 * pair of atoms the probe sphere has moved.
 */
public class Vector 
{
	private Atom atom1;
	private Atom atom2;
	private double xPos;
	private double yPos;
	private double zPos;
	
	/**
	 * 
	 * @param p the two pairs of atom's distance
	 * which is being vectorized.
	 */
	public Vector(Pair p)
	{
		atom1 = p.getAtom1();
		atom2 = p.getAtom2();
		xPos = atom2.getCenter().getX() - atom1.getCenter().getX();
		yPos = atom2.getCenter().getY() - atom1.getCenter().getY();
		zPos = atom2.getCenter().getZ() - atom1.getCenter().getZ();
	}
	
	/**
	 * @return the xPos of the current vector between the atomPair
	 */
	public double getXPos()
	{
		return xPos;
	}
	
	/**
     * @return the xPos of the current vector between the atomPair
     */
	public double getYPos()
	{
		return yPos;
	}
	
	/**
     * @return the xPos of the current vector between the atomPair
     */
	public double getZPos()
	{
		return zPos;
	}
	
	/**
	 * uses the distance formula and current coordinates of the 
	 * vector to determine the total distance the vector has
	 * traveled between the two atom-pairs.
	 * @return the vectors total distance
	 */
	public double calcTotalDist()
	{
		return Math.sqrt(Math.pow(xPos, 2) + Math.pow(yPos, 2) + Math.pow(zPos, 2));
	}
}
