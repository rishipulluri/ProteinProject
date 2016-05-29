package protein;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
 * CS2104 Project
 * 
 * Compiler: Java Sun
 * IDE: Eclipse
 * Authors: Justin Park, Shuaicheng Zhang, Rishi Pulluri, David Tchaou 
 * 
 */
public class ProteinCavityFinder 
{
	private String inputFile;
	private String outputFile;
	private double probeRadius;
	private double resolution;
	private ArrayList<Atom> atomList;
	private Cluster cluster;
	private Cluster voidCluster;
	private ArrayList<Atom> voidSpheres;
	private ArrayList<Atom> cavityList;
	private Bounds bounds;
	
	/*
	 * Constructor for ProteinCavityFinder
	 * @param i: input file name, o: output file name, rad: probe radius, res: resolution
	 */
	public ProteinCavityFinder(String i, String o, Double rad, Double res)
	{
		inputFile = i;
		outputFile = o;
		probeRadius = rad;
		resolution = res;
		atomList = new ArrayList<Atom>();
		voidSpheres = new ArrayList<Atom>();
		cluster = new Cluster();
		voidCluster = new Cluster();
		cavityList = new ArrayList<Atom>();
	}
	
	/*
	 * Runs algorithm for finding cavities
	 */
	public void findCavities()
	{
		System.out.println("Starting ProteinCavityFinder");
		System.out.println("Probe Radius: " + probeRadius + "\tResolution: " + resolution);
		System.out.println("Getting data from: " + inputFile);
		atomList = readProteinFile(inputFile);
		System.out.println("Number of atoms: " + atomList.size());
		createBounds();
		System.out.println("Bounds are: " + bounds.toString());
		System.out.println("__________________________\n");
		System.out.println("Finding void spheres...");
		findVoidSpheres();
		System.out.println("Number of void spheres: " + cluster.getNumVoidSpheres());
		System.out.println("Determining cavity spheres...");
		cavityFinder();
		cavityRes();
		checkOverlapVoids();
		cavityList = cluster.getList();
		Collections.sort(cavityList);
		removeCavityDuplicates();
	    System.out.println("Number of cavity spheres: " + cavityList.size());
	    System.out.println("Writing results to: " + outputFile);
		printOutResults();
		System.out.println("Done");
	}
	
	/*
	 * STEP 1
	 * Read the protein file
	 * Store each atom into allAtoms ArrayList
	 */
	public ArrayList<Atom> readProteinFile(String fileName) 
	{
		ArrayList<Atom> allAtoms = new ArrayList<Atom>();
        Scanner file;
        
        try 
        {
            file = new Scanner(new File(fileName));
            while (file.hasNextLine())
            {
            	String[] line = file.nextLine().split("\\s+");
            	double xPos = Double.parseDouble(line[5]);
                double yPos = Double.parseDouble(line[6]);
                double zPos = Double.parseDouble(line[7]);
                double rad = Double.parseDouble(line[9]);
                
                Point center = new Point(xPos, yPos, zPos);
                double radius =  rad;
                Atom currAtom = new Atom(center, radius);
                allAtoms.add(currAtom);
            }
        } 
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        
        return allAtoms;
    }
	
	/*
	 * STEP 2
	 * Find all pairs of atoms whose distance can allow the probe to be placed.
	 * 
	 * If distance of center A to distance center B minus the radius of A and B
	 * is greater than probe radius, then check if a probe can be placed between
	 * the two atoms.
	 * If the probe can be placed then the sphere is added to cluster.
	 */
	public void findVoidSpheres()
	{
		for(int x = 0; x < atomList.size(); x++)
		{
			for(int y = x+1; y < atomList.size(); y++)
			{
				Atom a = atomList.get(x);
				Atom b = atomList.get(y);
				Pair atomPair = new Pair(a, b);
				if (a.calcDist(b) >= probeRadius * 2)
				{
					Atom tempProbe = moveProbeOnVector(atomPair, 0);
					double i = 1;
					boolean bool = false;
					ArrayList<Atom> tempList = new ArrayList<Atom>();
					
					// check if the probe can be placed along increments of the vector
					// add the probe to voidSpheres if the probe can be placed
					while (canPlaceProbe(tempProbe))
					{
						bool = true;
						if (!voidSpheres.contains(tempProbe))
						{
							voidSpheres.add(tempProbe);
							tempList.add(tempProbe);
						}
						tempProbe = moveProbeOnVector(atomPair, resolution*i);
						i++;
					}
					if (bool && !tempList.isEmpty())
					{
						cluster.addCluster(tempList);
					}
				}
			}
		}
	}
	
	/*
	 * STEP 3
	 * Moves each void sphere and checks if it
	 * reaches the bounding box or overlaps another atom.
	 * 
	 * If moving the void sphere results in reaching the bounding box
	 * without overlapping another atom, that void sphere will be removed. 
	 */
	public void cavityFinder()
	{
		Cluster tempClust = new Cluster();
		for (int x = 0; x < cluster.getNumClusters(); x++)
		{
			ArrayList<Atom> temp = cluster.getVoidSpheres(x);
			boolean cavity = true;
			for (int y = 0; (y < temp.size()) && cavity; y++)
			{
				if (isVoid(temp.get(y), resolution))
				{
					voidCluster.addCluster(cluster.getVoidSpheres(x));
					cavity = false;
				}
			}
			if (cavity)
			{
				tempClust.addCluster(cluster.getVoidSpheres(x));
			}
		}
		cluster = tempClust;
	}
	
	/*
	 * STEP 4
	 * Places probe +/- x, y, z if it doesn't overlap any atom
	 * and stores the location into cluster.
	 */
	public void cavityRes()
	{
		for (int x = 0; x < cluster.getNumClusters(); x++)
		{
			ArrayList<Atom> currList = cluster.getVoidSpheres(x);
			int size = currList.size();
			
			for (int y = 0; y < size; y++)
			{
				Atom currAtom = currList.get(y);
				Point cent = currAtom.getCenter();
				Point[] p = new Point[6];
				p[0] = new Point(cent.getX() + resolution, cent.getY(), cent.getZ());
				p[1] = new Point(cent.getX() - resolution, cent.getY(), cent.getZ());
				p[2] = new Point(cent.getX(), cent.getY() + resolution, cent.getZ());
				p[3] = new Point(cent.getX(), cent.getY() - resolution, cent.getZ());
				p[4] = new Point(cent.getX(), cent.getY(), cent.getZ() + resolution);
				p[5] = new Point(cent.getX(), cent.getY(), cent.getZ() - resolution);
				ArrayList<Atom> alist = new ArrayList<Atom>();
				
				for (int z = 0; z < p.length; z++)
				{
					alist.add(new Atom(p[z], probeRadius));
				}
				
				for (int i = 0; i < alist.size(); i++)
				{
					boolean overlap = false;
					for (int j = 0; j < atomList.size() && !overlap; j++)
					{
						if (alist.get(i).calcDist(atomList.get(j)) < 0)
						{
							overlap = true;
						}
					}
					if (!overlap)
					{
						currList.add(alist.get(i));
					}
				}
			}
		}
	}
	
	/*
	 * STEP 5
	 * Checks through the new potential cavity spheres found in step 4 to make
	 * sure they do not overlap any void spheres. If a potential cavity
	 * sphere overlaps a void sphere, then those potential cavity spheres
	 * are also void spheres and will be removed.
	 */
	public void checkOverlapVoids()
	{
		Cluster tempClust = new Cluster();
		int index = 0;
		ArrayList<Atom> temp = cluster.getList();
		ArrayList<Atom> check = voidCluster.getList();
		for (int x = 0; x < temp.size(); x++)
		{
			boolean b = true;
			Atom curr = temp.get(x);
			for (int y = 0; y < check.size() && b; y++)
			{
				if (curr.calcDist(check.get(y)) < 0)
				{
					for (int z = 0; z < cluster.getNumClusters() && b; z++)
					{
						if (cluster.getVoidSpheres(z).contains(curr))
						{
							index = z;
							b = false;
						}
					}
				}
			}
			if (!b)
			{
				tempClust.addCluster(cluster.getVoidSpheres(index));
			}
		}
		
		for (int j = 0; j < tempClust.getNumClusters(); j++)
		{
			cluster.getCluster().remove(tempClust.getVoidSpheres(j));
		}
	}
	
	/*
	 * STEP 6
	 * FINAL STEP
	 * 
	 * Print a copy of the input file with cavity spheres attached to the end
	 */
	public void printOutResults()
    {
        try 
        {    
            File file = new File(outputFile);
            BufferedReader input = new BufferedReader(new FileReader(inputFile));
            String lineInput = null;
            PrintWriter output = new PrintWriter(file);
            
            while ((lineInput = input.readLine()) != null) 
            {             
                output.println(lineInput);
            }
            
            int counter10001 = 10001;
            int counter5001 = 5001;
            
            for (int i = 0; i < cavityList.size(); i++) 
            {               
                output.append("ATOM  " + counter10001 + "  MC  CAV");
                output.append("  " + counter5001 + "     ");
                
                output.printf("%7.3f" ,cavityList.get(i).getCenter().getX());
                output.append(" ");
                
                output.printf("%7.3f" ,cavityList.get(i).getCenter().getY());
                output.append(" ");
                
                output.printf("%7.3f" ,cavityList.get(i).getCenter().getZ());
                output.printf("  0.00  %.2f\n", probeRadius);
                counter10001++;
                counter5001++;
            }
            output.close();
            input.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
	
	/*
	 * Checks to see if a probe can be placed
	 * 
	 * @return true if the probe does not overlap any atom, false otherwise
	 */
	public boolean canPlaceProbe(Atom probe)
	{
		if (reachedBounds(probe))
		{
			return false;
		}
		for (int x = 0; x < atomList.size(); x++)
		{		
			if (probe.calcDist(atomList.get(x)) < 0)
			{
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Moves the probe along the vector between a pair
	 * 
	 * @return the probe at the new position
	 */
	public Atom moveProbeOnVector(Pair p, double step)
	{
		Atom startAtom = p.getAtom1();
		Vector vec = new Vector(p);
		double q = probeRadius + startAtom.getRadius() + step;
		double r = q/vec.calcTotalDist();
		double xPos = startAtom.getCenter().getX() + (r*vec.getXPos());
		double yPos = startAtom.getCenter().getY() + (r*vec.getYPos());
		double zPos = startAtom.getCenter().getZ() + (r*vec.getZPos());
		xPos = Math.round(xPos * 1000.0) / 1000.0;
		yPos = Math.round(yPos * 1000.0) / 1000.0;
		zPos = Math.round(zPos * 1000.0) / 1000.0;
	
		return new Atom(new Point(xPos, yPos, zPos), probeRadius);
	}
	
	/*
	 * Moves the current sphere in +/- x, y, z and checks
	 * whether they reach the bounding box.
	 * 
	 *  @return true if the the atom reaches the bounding box, false otherwise
	 */
	public boolean isVoid(Atom a, double step)
	{
		// initialize incremented points
		Point cent = a.getCenter();
		Point[] p = new Point[6];
		p[0] = new Point(cent.getX() + step, cent.getY(), cent.getZ());
		p[1] = new Point(cent.getX() - step, cent.getY(), cent.getZ());
		p[2] = new Point(cent.getX(), cent.getY() + step, cent.getZ());
		p[3] = new Point(cent.getX(), cent.getY() - step, cent.getZ());
		p[4] = new Point(cent.getX(), cent.getY(), cent.getZ() + step);
		p[5] = new Point(cent.getX(), cent.getY(), cent.getZ() - step);
		ArrayList<Atom> alist = new ArrayList<Atom>();
		
		for (int x = 0; x < p.length; x++)
		{
			alist.add(new Atom(p[x], probeRadius));
		}
		
		// Testing for overlap and reaching bounds
		for (int x = 0; x < alist.size(); x++)
		{
			boolean overlap = false;
			while (!overlap && !reachedBounds(alist.get(x)))
			{
				for (int y = 0; y < atomList.size() && !overlap; y++)
				{
					if (alist.get(x).calcDist(atomList.get(y)) < 0)
					{
						overlap = true;
					}
				}
				if (!overlap)
				{
					if (x == 0)
					{
						alist.get(x).getCenter().setX(alist.get(x).getCenter().getX() + resolution);
					}
					else if (x == 1)
					{
						alist.get(x).getCenter().setX(alist.get(x).getCenter().getX() - resolution);
					}
					else if (x == 2)
					{
						alist.get(x).getCenter().setY(alist.get(x).getCenter().getY() + resolution);
					}
					else if (x == 3)
					{
						alist.get(x).getCenter().setY(alist.get(x).getCenter().getY() - resolution);
					}
					else if (x == 4)
					{
						alist.get(x).getCenter().setZ(alist.get(x).getCenter().getZ() + resolution);
					}
					else
					{
						alist.get(x).getCenter().setZ(alist.get(x).getCenter().getZ() - resolution);
					}
				}
			}
			if (reachedBounds(alist.get(x)))
			{
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Creates the bounds using the maximum and minimum
	 * x, y, z coordinates and radii found from the input file
	 */
	public void createBounds()
	{
		double xLow, xHi, yLow, yHi, zLow, zHi;
		Atom temp = atomList.get(0);
		xLow = temp.getCenter().getX();
		xHi = temp.getCenter().getX();
		yLow = temp.getCenter().getY();
		yHi = temp.getCenter().getY();
		zLow = temp.getCenter().getZ();
		zHi = temp.getCenter().getZ();
		for (int x = 1; x < atomList.size(); x++)
		{
			Atom currAtom = atomList.get(x);
			double xTemp = currAtom.getCenter().getX();
			double yTemp = currAtom.getCenter().getY();
			double zTemp = currAtom.getCenter().getZ();
			if (xTemp < xLow)
			{
				xLow = xTemp - currAtom.getRadius();
			}
			else if (xTemp > xHi)
			{
				xHi = xTemp + currAtom.getRadius();
			}
			if (yTemp < yLow)
			{
				yLow = yTemp - currAtom.getRadius();
			}
			else if (yTemp > yHi)
			{
				yHi = yTemp + currAtom.getRadius();
			}
			if (zTemp < zLow)
			{
				zLow = zTemp - currAtom.getRadius();
			}
			else if (zTemp > zHi)
			{
				zHi = zTemp + currAtom.getRadius();
			}
		}
		bounds = new Bounds(xLow, xHi, yLow, yHi, zLow, zHi);
	}
	
	/*
	 * Checks to see if the probe has reached the bounds;
	 * 
	 * @return true if the probe reaches or is over the bounds, false otherwise
	 */
	public boolean reachedBounds(Atom probe)
	{
		double x = probe.getCenter().getX();
		double y = probe.getCenter().getY();
		double z = probe.getCenter().getZ();
		boolean xBool = x - probeRadius < bounds.getLowX() || x + probeRadius > bounds.getHiX();
		boolean yBool = y - probeRadius < bounds.getLowY() || y + probeRadius > bounds.getHiY();
		boolean zBool = z - probeRadius < bounds.getLowZ() || z + probeRadius > bounds.getHiZ();
		return xBool || yBool || zBool;
	}
	
	/*
	 * Removes any duplicate cavity spheres
	 */
	public void removeCavityDuplicates()
	{
		for (int x = 0; x < cavityList.size(); x++)
		{
	        for (int y = x + 1; y < cavityList.size(); y++)
	        {
	            if (cavityList.get(x).equals(cavityList.get(y)))
	            {
	                cavityList.remove(y);
	                y--;
	            }
	        }
	    }
	}
	
	/*
	 * The main method.
	 * 
	 * Checks to see that the appropriate arguments are given. If not,
	 * then an error message will be printed with an example of the correct
	 * arguments.
	 * 
	 * Calls the method findCavities()
	 */
	public static void main(String[] args) 
	{
        if (args.length == 0) 
        {
            System.out.println("Error!");
            System.out.println("No arguments found");
            System.out.println("Format: -i -o -probe -resolution");
            System.out.println("Example: example_input.pdb example_output.pdb 1.4 0.25");
            return;
        }
        
        if (args.length < 4 || args.length > 4)
        {
        	System.out.println("Error!");
        	System.out.println("Arguments found: " + args.length);
        	System.out.println("Arguments required: 4");
        	System.out.println("Format: -i -o -probe -resolution");
            System.out.println("Example: example_input.pdb example_output.pdb 1.4 0.25");
        	return;
        }

        String iFile = args[0];
        String oFile = args[1];
        double probeRad = Double.parseDouble(args[2]);
        double res = Double.parseDouble(args[3]);

        ProteinCavityFinder cavityFinder = new ProteinCavityFinder(iFile, oFile, probeRad, res);
        cavityFinder.findCavities();
    }
}
