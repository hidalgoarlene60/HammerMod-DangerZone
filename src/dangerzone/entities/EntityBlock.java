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


import org.newdawn.slick.opengl.Texture;


import dangerzone.World;
import dangerzone.entities.EntityLiving;
import dangerzone.blocks.Blocks;


public class EntityBlock extends EntityLiving {
	
	public int maxlifecounter = 0;
	
	public EntityBlock(World w) {
		super(w);
		uniquename = "DangerZone:EntityBlock";
		width = 0.75f;
		height = 0.75f;
		this.takesFallDamage = false;
		this.movefrequency = 75; //don't move around while falling!
		canSwim = false;
	}
	
	
	public void update(float deltaT){		
		if(world.isServer){
			
			maxlifecounter++;
			if(maxlifecounter > 100)deadflag = true; //ten seconds...
			
			firecounter = 0; //cheap trick! no fire damage...
			if(Blocks.getMaxStack(getBID()) == 0){
				deadflag = true; //illegal block ID!
			}else{				
				if(Blocks.isSolid(world.getblock(dimension, (int)posx, (int)(posy-0.1f), (int)posz))){
					movefrequency = 3; //enable moving around!
					if(!Blocks.isSolid(world.getblock(dimension, (int)posx, (int)posy, (int)posz))){
						world.setblockandmetaWithPerm(this, dimension, (int)posx, (int)posy, (int)posz, getBID(), getIID());
						if(!Blocks.isSolid(getBID())){
							world.playSound("DangerZone:big_splash", dimension, posx, posy, posz, 0.75f, 1.0f);
						}else{
							if(world.rand.nextInt(3) == 1)world.playSound("DangerZone:dirt_place", dimension, posx, posy, posz, 0.125f, 1.0f);
						}
						deadflag = true;
					}				
				}
			}
		}
		super.update(deltaT);
	}
	
	public Texture getTexture(){
		return null; //the block will supply its own texture!
	}
	
	public boolean isDying(){
		return false; //we are already dead.
	}
	
	public void doDeathAnimation(){
		deathfactor = 0; //none
		motiony = motionx = motionz = 0;
	}
	

}
