package dangerzone.entities;
/*
 * This code is copyright Richard H. Clark, TheyCallMeDanger, OreSpawn, 2015-2020.
 * You may use this code for reference for modding the DangerZone game program,
 * and are perfectly welcome to cut'n'paste portions for your mod as well.
 * DO NOT USE THIS CODE FOR ANY PURPOSE OTHER THAN MODDING FOR THE DANGERZONE GAME.
 * DO NOT REDISTRIBUTE THIS CODE. 
 * 
 * This copyright remains in effect until January 1st, 2021. 
 * At that time, this code becomes public domain.
 * 
 * WARNING: There are bugs. Big bugs. Little bugs. Every size in-between bugs.
 * This code is NOT suitable for use in anything other than this particular game. 
 * NO GUARANTEES of any sort are given, either express or implied, and Richard H. Clark, 
 * TheyCallMeDanger, OreSpawn are not responsible for any damages, direct, indirect, or otherwise. 
 * You should have made backups. It's your own fault for not making them.
 * 
 * NO ATTEMPT AT SECURITY IS MADE. This code is USE AT YOUR OWN RISK.
 * Regardless of what you may think, the reality is, that the moment you 
 * connected your computer to the Internet, Uncle Sam, among many others, hacked it.
 * DO NOT KEEP VALUABLE INFORMATION ON INTERNET-CONNECTED COMPUTERS.
 * Or your phone...
 * 
 */
public class TargetHelper {
	
	public double targetx;
	public double targety;
	public double targetz;
	public Entity te = null;

	public TargetHelper(){
		targetx = 0;
		targety = 0;
		targetz = 0;
		te = null;
	}
	
	public TargetHelper(double x, double y, double z){
		targetx = x;
		targety = y;
		targetz = z;
		te = null;
	}
	
	public TargetHelper(Entity e, double x, double y, double z){
		te = e;
		targetx = x;
		targety = y;
		targetz = z;
	}
	
	public double getDistanceToTarget(double x, double y, double z){
		double dx = targetx - x;
		double dy = targety - y;
		double dz = targetz - z;
		return  Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
	}
	
	public void setTarget(double x, double y, double z){
		targetx = x;
		targety = y;
		targetz = z;
	}
	
	public void setTarget(Entity e, double x, double y, double z){
		te = e;
		targetx = x;
		targety = y;
		targetz = z;
	}
	


}
