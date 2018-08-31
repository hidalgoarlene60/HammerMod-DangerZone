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
import dangerzone.Effects;
import dangerzone.GameModes;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.WorldRendererUtils;
import dangerzone.items.Items;


public class GhostSkelly extends EntityLiving {
	
	float myspeed = 0.125f;
	
	public	GhostSkelly(World w){
		super(w);
		maxrenderdist = 80; //in blocks
		this.height = 1.75f;
		this.width = 1.25f;
		uniquename = "DangerZone:GhostSkelly";
		setMaxHealth(5.0f);
		setHealth(5.0f);
		setDefense(1.0f);
		setAttackDamage(0.55f);
		takesFallDamage = false;
		ignoreCollisions = true;
		setExperience(30);
		temperament = Temperament.HOSTILE;
		daytimespawn = false;
		daytimedespawn = true;
		nighttimespawn = true;
		nighttimedespawn = false;
		canFly = true;
		setFlying(true);
		tower_defense_enable = false;
	}
	
	//we are a ghost!
	public void doEntityCollisions(float deltaT){		
	}

	//no default actions here - we do our flying in the update routine.
	public void doEntityAction(float deltaT){	
		//make some noise!
		if(!this.isHurt() && !deadflag && !getStaying()){
			if(this.world.rand.nextInt(100) == 1){
				this.world.playSound(getLivingSound(), dimension, posx, posy+getHeight()/2, posz, getLivingSoundVolume(), getLivingSoundPitch());					
			}				
		}
	}
	

	public void update( float deltaT){
		
		if(this.world.isServer){
			int bid;
					
			if(this.world.isDaytime()){
				if(this.world.rand.nextInt(100) == 1){					
					double dist = 0;
					Player p = DangerZone.server.findNearestPlayer(this);
					if(p == null){
						this.deadflag = true;
					}else{
						dist = Utils.getDistanceBetweenEntities(this, p);
						if(dist > maxrenderdist/2){
							this.deadflag = true;
						}
					}
				}
			}

			
			if(target == null)target = new TargetHelper(posx, posy, posz);
			
			bid = this.world.getblock(this.dimension, (int)this.target.targetx, (int)this.target.targety, (int)this.target.targetz);
	    	if(bid != 0 || this.world.rand.nextInt(40) == 1 || this.target.getDistanceToTarget(posx, posy, posz) < 2.0f)
	    	{
	    		Player p = DangerZone.server.findNearestPlayer(this);
	    		int i, j;
	    		if(p != null && this.getDistanceFromEntity(p) < 12 && p.getGameMode() == GameModes.SURVIVAL){
	    			this.target.setTarget(p.posx, p.posy, p.posz);
	    			if(this.getDistanceFromEntity(p) < 2){
	    				if(world.rand.nextInt(10) == 1){
	    					p.doAttackFrom(this, DamageTypes.HOSTILE, this.getAttackDamage());
	    				}	    				
	    			}
	    		}else{
	    			for(i=0;i<3;i++){
    					bid = world.getblock(dimension, (int)this.posx, (int)this.posy+i, (int)this.posz);
    					if(bid == 0)break;
    				}
    				//stay down near the ground
    				for(j=-1;j>=-3;j--){
    					bid = world.getblock(dimension, (int)this.posx, (int)this.posy+j, (int)this.posz);
    					if(bid != 0)break;
    				}
    	    		this.target.setTarget((int)this.posx + world.rand.nextInt(10) - world.rand.nextInt(10), (int)this.posy + i + j + world.rand.nextInt(4) + 1, (int)this.posz + world.rand.nextInt(10) - world.rand.nextInt(10));  
	    		}
	    	}
	    	
	    	//Now rotate into direction we should be heading, and update motion!
	    	float dy = (float) (target.targety - this.posy);
	    	motiony += dy/30f;
	    	
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
	    	
		}else{
			if(WorldRendererUtils.getTotalLightAt(world, dimension, (int)posx, (int)posy, (int)posz) > 0.55f){
				if(world.rand.nextInt(70) == 1){
					DangerZone.server_connection.sendKillMe(entityID);
				}
			}
		}
		super.update( deltaT);
	}
	
	public void doDeathDrops(){
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophyghostskelly.itemID, 1f, dimension, posx, posy, posz);
	}
	
	public String getLivingSound(){
		if(world.rand.nextInt(2)!= 0)return null; //not so often!
		return "DangerZone:chain_rattles";
	}
	
	public String getHurtSound(){
		return "DangerZone:rathit";
	}
	
	public String getDeathSound(){
		return "DangerZone:chain_rattles";
	}
	
	//only at night where it's dark
	public boolean getCanSpawnHereNow(World w, int dimension, int x, int y, int z){
		if(w.isDaytime())return false;
		if(getLightAtLocation(w, dimension, x, y, z) > 0.35f)return false;
		return true;
	}

	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "GhostSkellytexture.png");
		}
		return texture;
	}
	
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof Ghost)return false;
		if(e instanceof GhostSkelly)return false;
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		if(e instanceof Player && CanProbablySeeEntity(e) )return true;
		return false;
	}

}

