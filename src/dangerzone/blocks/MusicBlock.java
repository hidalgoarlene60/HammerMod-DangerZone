package dangerzone.blocks;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.StitchedTexture;
import dangerzone.Utils;
import dangerzone.World;


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
public class MusicBlock extends Block {
	
	Texture ttop = null;
	Texture tbottom = null;
	Texture tleft = null;
	String topname;
	String bottomname;
	String leftname;
	StitchedTexture sttop = new StitchedTexture();
	StitchedTexture stbottom = new StitchedTexture();
	StitchedTexture stleft = new StitchedTexture();

	public MusicBlock(String n, String txttop, String txt2, String txt3) {
		super(n, "");
		isStone = true;
		maxdamage = 20;
		mindamage = 10; 
		breaksound = "DangerZone:stonebreak"; 
		placesound = "DangerZone:stoneplace"; 
		hitsound =   "DangerZone:stonehit";
		topname = txttop;
		bottomname = txt3;
		leftname = txt2;
		alwaystick = true;
		showTop = true;
	}
	
	public void tickMe(World w, int d, int x, int y, int z){	
		//FastBlockTicker.addFastTick(d,  x,  y, z, 2); //ticks, in TENTHS of a second
		boolean spout = false;
		if(Blocks.isLiquid(w.getblock(d, x+1, y, z)))spout = true;
		if(Blocks.isLiquid(w.getblock(d, x-1, y, z)))spout = true;
		if(Blocks.isLiquid(w.getblock(d, x, y, z+1)))spout = true;
		if(Blocks.isLiquid(w.getblock(d, x, y, z-1)))spout = true;
		if(Blocks.isLiquid(w.getblock(d, x, y-1, z)))spout = true;
		
		if(spout){
			DangerZone.server.sendSongToAll(DangerZone.soundmangler.getRandomMusicFile());
			Utils.spawnParticlesFromServer(w, "DangerZone:ParticleMorph", 1 + w.rand.nextInt(10), d, (double)x+0.5f, (double)y+1.1f, (double)z+0.5f);
		}
	}
	
	//side 0 = top
	//side 1 = front
	//side 2 = back
	//side 3 = left
	//side 4 = right
	//side 5 = bottom
	public Texture getTexture(int side){

		if(ttop == null){
			ttop = initBlockTexture(topname);
		}
		if(tbottom == null){
			tbottom = initBlockTexture(bottomname);
		}
		if(tleft == null){
			tleft = initBlockTexture(leftname);
		}
		
		if(side == 0)return ttop;
		if(side == 5)return tbottom;
		if(side == 3)return tleft;
		if(side == 4)return tleft;
		if(side == 1)return tleft;
		if(side == 2)return tleft;
		return null;
	}
	
	public StitchedTexture getStitchedTexture(int side){	
		if(side == 0)return sttop;
		if(side == 5)return stbottom;
		if(side == 3)return stleft;
		if(side == 4)return stleft;
		if(side == 1)return stleft;
		return stleft;
	}
	
	public String getStitchedTextureName(int side){
		if(side == 0)return topname;
		if(side == 5)return bottomname;
		if(side == 3)return leftname;
		if(side == 4)return leftname;
		if(side == 1)return leftname;
		return leftname;
	}

}