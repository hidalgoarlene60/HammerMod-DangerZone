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


import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.Explosion;
import dangerzone.World;
import dangerzone.entities.EntityLiving;
import dangerzone.blocks.Blocks;


public class EntityExplosiveBlock extends EntityLiving {
	
	int exploded_already = 0;

	public EntityExplosiveBlock(World w) {
		super(w);
		uniquename = "DangerZone:EntityExplosiveBlock";
		width = 0.90f;
		height = 0.90f;
		this.moveSpeed = 0;
		this.movefrequency = 5555; //dont go!
		setVarFloat(10, DangerZone.rand.nextInt(100) + 1);
		setHealth(1);
		setMaxHealth(1);
	}
	
	
	public void update(float deltaT){		
		if(world.isServer){
			if(Blocks.getMaxStack(getBID()) == 0){
				deadflag = true; //illegal block ID!
			}else{
				setVarFloat(11, getVarFloat(11)+1);
				if(getVarFloat(11) > getVarFloat(10)){
					exploded_already = 1;	
					Explosion.boom(this, world, dimension, posx, posy, posz, (int)getAttackDamage(), true);
					deadflag = true;
					//System.out.printf("Boom!\n");
				}
			}
		}
		super.update(deltaT);
	}
	
	public void onDeath(){
		if(exploded_already == 0){
			exploded_already = 1;		
			Explosion.boom(this, world, dimension, posx, posy, posz, (int)getAttackDamage(), true);
			//System.out.printf("Boom Death!\n");
		}
	}
	
	public float getWidth(){
		if(getVarFloat(11)==0)return 1f;
		return width + (getVarFloat(11)*width*1.5f)/getVarFloat(10);
	}
	
	public float getHeight(){
		if(getVarFloat(11)==0)return 1f;
		return height + (getVarFloat(11)*height*1.5f)/getVarFloat(10);
	}
	
	public float getScale(){
		if(getVarFloat(11)==0)return 1f;
		return 1f + (getVarFloat(11)*1.5f)/getVarFloat(10);
	}
	
	public boolean takesDamageFrom(/*DamageTypes*/int dt){
		if(dt == DamageTypes.EXPLOSIVE)return false;
		return true;
	}
	
	public Texture getTexture(){
		return null; //the block will supply its own texture!
	}
	
	public boolean isDying(){
		return false; //we are already dead, no animation, no delay.
	}
	
	public void doDeathAnimation(){
		deathfactor = 0; //none
		motiony = motionx = motionz = 0;
	}
	

}
