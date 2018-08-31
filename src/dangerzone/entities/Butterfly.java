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

import dangerzone.DangerZone;
import dangerzone.Effects;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


public class Butterfly extends EntityLiving {
	
	public float myspeed = 0;
	public Texture texture2 = null;
	public Texture texture3 = null;
	public Texture texture4 = null;
	public boolean despawn_fast = false;
	
	public	Butterfly(World w){
		super(w);
		maxrenderdist = 128; //in blocks
		this.height = 0.25f;
		this.width = 0.75f;
		uniquename = "DangerZone:Butterfly";
		setMaxHealth(2.0f);
		setHealth(2.0f);
		setDefense(0.5f);
		if(w != null)myspeed = 0.05f + world.rand.nextFloat()*0.10f;
		takesFallDamage = false;
		setBID(-1); //force a change for 0
		setBID(DangerZone.rand.nextInt(4));
		setExperience(1);
		daytimespawn = true;
		nighttimespawn = false;
		daytimedespawn = true;
		nighttimedespawn = true;
		canFly = true;
		setFlying(true);
	}
	
	
	//no default actions here - we do our flying in the update routine.
	public void doEntityAction(float deltaT){		
	}
	

	public void update( float deltaT){
		
		if(this.world.isServer){
			int bid;
			int keep_trying = 25;
			
			if(despawn_fast || !this.world.isDaytime()){
				if(this.world.rand.nextInt(100) == 1 && getCanDespawn()){
					this.deadflag = true;
				}
			}
			
			if(target == null)target = new TargetHelper(posx, posy, posz);
			
			bid = this.world.getblock(this.dimension, (int)this.target.targetx, (int)this.target.targety, (int)this.target.targetz);
	    	if(bid != 0 || this.world.rand.nextInt(50) == 1 || this.target.getDistanceToTarget(posx, posy, posz) < 2.0f)
	    	{
	    		bid = 1;
	    		while(bid != 0 && keep_trying != 0){	    			
	    			this.target.setTarget(posx + (this.world.rand.nextInt(10) - this.world.rand.nextInt(10)), posy + (this.world.rand.nextInt(7) - 2), posz + (this.world.rand.nextInt(10) - this.world.rand.nextInt(10)));
	    			bid = this.world.getblock(this.dimension, (int)this.target.targetx, (int)this.target.targety, (int)this.target.targetz);
	    			keep_trying--;
	    			myspeed = 0.05f + world.rand.nextFloat()*0.10f;
	    		}    	   	
	    	}
	    	
	    	bid = this.world.getblock(this.dimension, (int)this.target.targetx, (int)this.target.targety, (int)this.target.targetz);
	    	if(bid != 0){
	    		this.target.setTarget(posx + (this.world.rand.nextInt(5) - this.world.rand.nextInt(5)), posy + (this.world.rand.nextInt(5)+3), posz + (this.world.rand.nextInt(5) - this.world.rand.nextInt(5)));
	    	}
	    	
	    	//Now rotate into direction we should be heading, and update motion!
	    	float dy = (float) (target.targety - this.posy);
	    	motiony += 0.120f * deltaT; //Counter a little gravity!
	    	dy = dy/20f;
			if(dy > 0.20f)dy = 0.20f;
			if(dy < -0.20f)dy = -0.20f;
			motiony += dy;
			if(motiony > 1)motiony = 1;
			if(motiony < -1)motiony = -1;
	    	
	    	float cdir = (float) Math.toRadians(rotation_yaw);
	    	float tdir = (float) Math.atan2(target.targetx - posx, target.targetz - posz);
	    	float ddiff = tdir - cdir;
	    	while(ddiff>Math.PI)ddiff -= Math.PI*2;
	    	while(ddiff<-Math.PI)ddiff += Math.PI*2;
	    	rotation_yaw_motion += (ddiff*180f/Math.PI)/10f;
	    	
	    	float speedadjust = 1;
	    	float effectspeed;
	    	effectspeed = this.getTotalEffect(Effects.SPEED);
    		if(effectspeed > 1){
    			speedadjust *= effectspeed;
    		}
    		effectspeed = this.getTotalEffect(Effects.SLOWNESS);
    		if(effectspeed > 1){
    			speedadjust /= effectspeed;
    		}
	    	motionx += myspeed*speedadjust*Math.sin(tdir) * deltaT;
	    	motionz += myspeed*speedadjust*Math.cos(tdir) * deltaT;
	    	
		}
		super.update( deltaT);
	}
	
	public void doDeathDrops(){
		if(world.rand.nextInt(20)==1)Utils.doDropRand(world, 0, Items.trophybutterfly.itemID, 1f, dimension, posx, posy, posz);
	}

	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Butterfly.png");
			texture2 = TextureMapper.getTexture("res/skins/"+ "Butterfly2.png");
			texture3 = TextureMapper.getTexture("res/skins/"+ "Butterfly3.png");
			texture4 = TextureMapper.getTexture("res/skins/"+ "Butterfly4.png");
		}
		if(getBID() == 1)return texture2;
		if(getBID() == 2)return texture3;
		if(getBID() == 3)return texture4;
		return texture;
	}

}
