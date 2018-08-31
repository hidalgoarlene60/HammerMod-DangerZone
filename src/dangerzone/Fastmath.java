package dangerzone;

public class Fastmath {
	
	//because on-the-fly calculations of sin and cos are painful, and we don't need 10 digits of accuracy,
	//make a pre-calculated table that gives us roughly 3-4 digits! Much faster to just look it up!
	
	public static double sintable[] = null;
	
	public static void inittable(){
		double dval = 0;
		double dindex;
		int index;
		sintable = new double [10000];
		
		for(index = 0; index < 10000; index++){
			dindex = index;
			dval = (Math.PI*2d)*(dindex/10000d);
			sintable[index] = Math.sin(dval);
		}
		
	}
	
	public static double sin(double rads){
		double retval = 0;
		int index = 0;
		
		if(sintable == null)inittable();
		
		retval = rads % (Math.PI*2d);
		retval = (retval * 10000d) / (Math.PI*2d);
		index = (int)retval;
		
		if(index < 0)index = 0;
		if(index > 9999)index = 9999;
		retval = sintable[index];
		return retval;
	}
	
	public static double cos(double rads){
		double retval = 0;
		int index = 0;
		
		if(sintable == null)inittable();
		
		retval = (rads+(Math.PI/2d)) % (Math.PI*2d); //cos is just 90 degrees off from sin
		retval = (retval * 10000d) / (Math.PI*2d);
		index = (int)retval;
		
		if(index < 0)index = 0;
		if(index > 9999)index = 9999;
		retval = sintable[index];
		return retval;
	}

}
