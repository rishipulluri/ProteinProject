package protein;

/*
 * CS2104 Project
 * IDE: Eclipse
 * Compiler: Java Sun
 * 
 * Authors: Justin Park, Shuaicheng Zhang, Rishi Pulluri, David Tchaou 
 * A pair of  two atoms and the distance between them
 */
public class Pair 
{
	private Atom atom1;
	private Atom atom2;
	
	/*
	 * @param one the first atom in the pair
	 * @param two the second atom in the pair
	 */
	public Pair(Atom one, Atom two)
	{
		atom1 = one;
		atom2 = two;
	}
	
	/*
	 * the first atom in the pair
	 * @return the first atom in the pair.
	 */
	public Atom getAtom1()
	{
		return atom1;
	}
	
	/*
     * the second atom in the pair
     * @return the second atom in the pair.
	 */
	public Atom getAtom2()
	{
		return atom2;
	}
	
	/*
	 * two pairs are both equal if the first atom in first
	 * pair is equal to the first atom in second pair and the second
	 * atom in first pair is equal to the second atom in the second pair.
	 * @param obj the pair of atoms being compared to
	 * @return true if the two pairs are equal to each other
	 */
	public boolean equals(Object obj)
	{
		if ((obj == null) || obj.getClass() != Pair.class)
        {
            return false;
        }
		Pair other = (Pair)obj;
		return this.getAtom1().equals(other.getAtom1()) && this.getAtom2().equals(other.getAtom2());
	}
}
