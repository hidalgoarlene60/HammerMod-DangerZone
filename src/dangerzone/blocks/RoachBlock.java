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
import dangerzone.World;
import dangerzone.entities.Entity;


public class RoachBlock extends Block {
	Texture ttop = null;
	Texture tbottom = null;
	Texture tside = null;
	String topname;
	String bottomname;
	String sidename;
	StitchedTexture sttop = new StitchedTexture();
	StitchedTexture stside = new StitchedTexture();
	StitchedTexture stbottom = new StitchedTexture();
	
	
	public RoachBlock(String n, String top, String bottom, String side){
		super(n, "");
		topname = top;
		bottomname = bottom;
		sidename = side;
		randomtick = true;	
		isDirt = true;
		maxdamage = 10;
		burntime = 40;
		breaksound = "DangerZone:dirt_hit";
		placesound = "DangerZone:dirt_place";
		hitsound =   "DangerZone:dirt_hit";
		showTop = true;
	}
	
	public String getStepSound(){
		int i = DangerZone.rand.nextInt(3);
		if(i == 0)return "DangerZone:dirt1";
		if(i == 1)return "DangerZone:dirt2";
		return "DangerZone:dirt3";
	}
	
	public void tickMe(World w, int d, int x, int y, int z){
		int i;
		if(Blocks.isSolid(w.getblock(d, x, y+1, z))){
			w.setblock(d, x, y, z, Blocks.dirt.blockID); //Can't grow with anything on top of me!
			//System.out.printf("Grass died\n");
			return;
		}
		//Spawn!
		if(w.rand.nextInt(10) == 1){
			int howmany = w.rand.nextInt(6)+1;
			for(i=0;i<howmany;i++){
				Entity eb = w.createEntityByName("DangerZone:Cockroach", d, ((double)x)+0.5f, ((double)y)+1.05f, ((double)z)+0.5f);
				if(eb != null){
					eb.init();
					w.spawnEntityInWorld(eb);
				}
			}
		}
		
	}
	
	public Texture getTexture(int side){
		if(ttop == null){
			ttop = initBlockTexture(topname);
		}
		if(tbottom == null){
			tbottom = initBlockTexture(bottomname);
		}
		if(tside == null){
			tside = initBlockTexture(sidename);
		}
		
		if(side == 0)return ttop;
		if(side == 5)return tbottom;
		return tside;
	}
	
	public StitchedTexture getStitchedTexture(int side){	
		if(side == 0)return sttop;
		if(side == 5)return stbottom;
		return stside;
	}
	
	public String getStitchedTextureName(int side){
		if(side == 0)return topname;
		if(side == 5)return bottomname;
		return sidename;
	}
}