package protein;

/*
 * CS2104 Project
 * 
 * Compiler: Java Sun
 * IDE: Eclipse
 * 
 * Authors: Justin Park, Shuaicheng Zhang, Rishi Pulluri, David Tchaou 
 * This class is used to determine the bounds of where the probe 
 * sphere can be placed.
 */
public class Bounds 
{
	public double x1;
	public double x2;
	public double y1;
	public double y2;
	public double z1;
	public double z2;
	
	/*
	 * Initializes the bounds for the probe, so that
	 * the probe cannot travel beyond these set vounds
	 * @param xA the top x bound
	 * @param xB the bottom x bound
	 * @param yA the top y bound
	 * @param yB the bottom x bound
	 * @param zA the top z bound
	 * @param zB the bottom x bound
	 */
	public Bounds(double xA, double xB, double yA, double yB, double zA, double zB)
	{
		x1 = xA;
		x2 = xB;
		y1 = yA;
		y2 = yB;
		z1 = zA;
		z2 = zB;
	}
	
	/*
	 * @return the low X bound for the
	 * probe sphere
	 */
	public double getLowX()
	{
		return x1;
	}
	
    /*
     * @return the High X bound for the
     * probe sphere
     */
	public double getHiX()
	{
		return x2;
	}
	
    /*
     * @return the low Y bound for the
     * probe sphere
     */
	public double getLowY()
	{
		return y1;
	}
	
    /*
     * @return the High Y bound for the
     * probe sphere
     */
	public double getHiY()
	{
		return y2;
	}
	
   /*
     * @return the low Z bound for the
     * probe sphere
     */
	public double getLowZ()
	{
		return z1;
	}
	
	/*
     * @return the High Z bound for the
     * probe sphere
     */
	public double getHiZ()
	{
		return z2;
	}
	
	
	/*
	 * formats all of the 6 x, y and z coordinates into 
	 * string and builds them into a single string.
	 * @return the formatted string.
	 */
	@Override
	public String toString()
	{
		return String.format("x:(%.3f, %.3f)  y:(%.3f, %.3f)  z:(%.3f, %.3f)", x1, x2, y1, y2, z1, z2);
	}

}
