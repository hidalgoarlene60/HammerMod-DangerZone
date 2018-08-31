package dangerzone;

public class PIDController {
	public double desiredpos;
	public float pfactor;
	public float ifactor;
	public float dfactor;
	public float scale;
	public float iaccum;
	public float ifade;
	public float maxspeed;
	public float minspeed;
	
	/*software PID controller... ish... seems to work...*/
	public PIDController(float sc, float p, float i, float ifd, float d, float mins, float maxs){
		scale = sc;
		pfactor = p;
		ifactor = i;
		ifade = ifd;
		dfactor = d;
		iaccum = 0;
		minspeed = mins;
		maxspeed = maxs;
	}
	
	public void setHoldpos(double pos){
		desiredpos = pos;
		iaccum = 0;
	}
	
	public float getAdjustment(double currentpos, float currentspeed){
		float posdif = (float)(desiredpos - currentpos);
		float adjust = scale * pfactor * posdif; //P
		float reldist = 1;
		
		iaccum += posdif;
		iaccum *= ifade; 
		adjust += (float) (scale * ifactor * iaccum); //I
		
		if(posdif != 0){ 
			reldist = Math.abs(currentspeed / posdif);
			if(reldist > 1)reldist = 1; //throttle throttle!
		}
		adjust -= scale * dfactor * currentspeed; //D
		
		//System.out.printf("p, i, d == %f, %f, %f\n", scale * pfactor * posdif, (float) (scale * ifactor * iaccum), -(scale * dfactor * currentspeed));
		
		if(currentspeed > maxspeed && adjust > 0)return 0;
		if(currentspeed < minspeed && adjust < 0)return 0;
		
		return adjust;
	}

}
