package dangerzone.blocks;
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
import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.StitchedTexture;


public class BlockLog extends Block {
	Texture ttop = null;
	Texture tside = null;
	String topname;
	String sidename;
	StitchedTexture sttop = new StitchedTexture();
	StitchedTexture stside = new StitchedTexture();
	
	
	public BlockLog(String n, String side, String top){
		super(n, ""); //I have more than one texture...
		topname = top;
		sidename = side;
		isWood = true;
		canLeavesGrow = true;
		breaksound = "DangerZone:woodbreak"; 
		placesound = "DangerZone:woodplace"; 
		hitsound =   "DangerZone:woodhit";
		burntime = 180;
		maxdamage = 30;
	}
	
	public String getStepSound(){
		int i = DangerZone.rand.nextInt(4);
		if(i == 0)return "DangerZone:wood1";
		if(i == 1)return "DangerZone:wood2";
		if(i == 2)return "DangerZone:wood3";
		return "DangerZone:wood4";
	}
	
	public Texture getTexture(int side){
		if(ttop == null){
			ttop = initBlockTexture(topname);
		}
		if(tside == null){
			tside = initBlockTexture(sidename);
		}		
		if(side == 0)return ttop;
		if(side == 5)return ttop;
		return tside;
	}
	
	public int getTextureID(int side){
		if(ttop == null){
			ttop = initBlockTexture(topname);
		}
		if(tside == null){
			tside = initBlockTexture(sidename);
		}		
		if(side == 0)return ttop.getTextureID();
		if(side == 5)return ttop.getTextureID();
		return tside.getTextureID();
	}
	
	public StitchedTexture getStitchedTexture(int side){	
		if(side == 0)return sttop;
		if(side == 5)return sttop;
		return stside;
	}
	
	public String getStitchedTextureName(int side){
		if(side == 0)return topname;
		if(side == 5)return topname;
		return sidename;
	}
}