
package protein;

import java.util.ArrayList;

/*
 * CS2104 Project
 * 
 * Compiler: Java Sun
 * IDE: Eclipse
 * 
 * Authors: Justin Park, Shuaicheng Zhang, Rishi Pulluri, David Tchaou 
 * This is a cluster that holds an arraylist of
 * atoms. The cluster will be used in the protein
 * Cavity-Finder to store the adjacent void spheres
 * together.
 */
public class Cluster 
{
	private ArrayList<ArrayList<Atom>> cluster;
	/*
	 * initializes the new arralylist of atoms
	 */
	public Cluster()
	{
		cluster = new ArrayList<ArrayList<Atom>>();
	}
	
	/*
	 * 
	 * @param i index to return the 
	 * cluster of all atoms near the atom 
	 * we selected
	 * @return the ArrayList of
	 * all void spheres adjacent 
	 * to the indexed void sphere
	 */
	public ArrayList<Atom> getVoidSpheres(int i)
	{
		return cluster.get(i);
	}
	
	
  /*
    * @return all of the void spheres and 
    * their adjacent void spheres. 
    */
	public ArrayList<ArrayList<Atom>> getCluster()
	{
		return cluster;
	}
	
	/*
	 * 
	 * @param a adding an arraylist of
	 * adjacent points to the selected atom
	 * which is going to be a.
	 */
	public void addCluster(ArrayList<Atom> a)
	{
		cluster.add(a);
	}
	
	/*
	 * the total number of void spheres 
	 * inside each of the lists in
	 * the cluster
	 * @return The total number of 
	 * void spheres in cluster.
	 */
	public int getNumVoidSpheres()
	{
		int count = 0;
		for (int x = 0; x < cluster.size(); x++)
		{
			count += cluster.get(x).size();
		}
		return count;
	}
	
	
	/*
	 * 
	 * @return the number of clusters currently in cluster
	 */
	public int getNumClusters()
	{
		return cluster.size();
	}
	
	
	/*
	 * 
	 * @return an arraylist that contains all of
	 * the adjacent void spheres of all of the atoms.
	 */
	public ArrayList<Atom> getList()
	{
		ArrayList<Atom> temp = new ArrayList<Atom>();
		for (int x = 0; x < getNumClusters(); x++)
		{
			temp.addAll(getVoidSpheres(x));
		}
		return temp;
	}
	
	/*
	 * @param the cluster being compared to
	 * @return true if the cluster being compared to
	 * and this cluster have the same list of atoms and 
	 * adjacent atoms.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if ((obj == null) || obj.getClass() != Cluster.class)
        {
            return false;
        }
		Cluster other = (Cluster)obj;
		return this.getCluster().equals(other.getCluster());
	}
}
