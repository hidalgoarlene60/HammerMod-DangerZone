package dangerzone.entities;

import org.newdawn.slick.opengl.Texture;

import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;
import dangerzone.items.Items;


public class DesertRainFrog extends EntityLiving {
	private int jumpcount = 0;
	
	
	public	DesertRainFrog(World w){
		super(w);
		maxrenderdist = 32; //in blocks
		this.height = 0.25f;
		this.width = 0.25f;
		uniquename = "DangerZone:Desert Rain Frog";
		moveSpeed = 0.16f;
		setMaxHealth(2.0f);
		setHealth(2.0f);
		setDefense(1.5f);
		setAttackDamage(2.0f);
		movefrequency = 40;
		setExperience(1);
		canSwim = true;
		takesFallDamage = false;
		attackRange = 1.0f;
		daytimespawn = true;
		nighttimespawn = false;
		daytimedespawn = true;
		nighttimedespawn = true;
		searchDistance = 8;
		enable_droppedfood = true;
		foodsearchDistance = 10;
		enable_hostile = true;
		canridemaglevcart = true;
	}
	
	public boolean isFoodItem(int foodid){
		if(foodid == Items.deadbug.itemID)return true;
		return false;
	}
	
	public void onKill(Entity e){
		heal(1);
	}
	
	//we attack all small things!
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof DesertRainFrog)return false;
		if(e instanceof Frog)return false;
		if(e.getWidth()*e.getHeight() > 0.53f)return false;
		if(!CanProbablySeeEntity(e))return false;
		return true;
	}
	
	public void update(float deltaT){

		if(world.isServer){
    		if(jumpcount > 0)jumpcount--;
    		if(jumpcount == 0){
    			if(world.rand.nextInt(75) == 1){
    				jumpAround();
    				jumpcount = 75;
    			}  			
    		}
    		if(this.world.rand.nextInt(10000) == 10 && getCanDespawn()){
				this.deadflag = true; //have to do our own despawning because we don't move much!!!
			}	
		}
		super.update(deltaT);
	}
	
	public String getLivingSound(){
		switch(world.rand.nextInt(10)){
		case 0: return "DangerZone:rainfrog1";
		case 1: return "DangerZone:rainfrog2";
		case 2: return "DangerZone:rainfrog3";
		case 3: return "DangerZone:rainfrog4";
		case 4: return "DangerZone:rainfrog5";
		case 5: return "DangerZone:rainfrog6";
		case 6: return "DangerZone:rainfrog7";
		case 7: return "DangerZone:rainfrog8";
		case 8: return "DangerZone:rainfrog9";
		case 9: return "DangerZone:rainfrog10";
		}
		return null;
	}
	
	public String getDeathSound(){
		return "DangerZone:rainfrog_death";
	}
	
	public String getHurtSound(){
		return "DangerZone:rainfrog_hit";
	}
	
	public void doDeathDrops(){
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophydesertrainfrog.itemID, 1f, dimension, posx, posy, posz);	
		if(world.rand.nextInt(5)==1)Utils.doDropRand(world, 0, Items.squeaktoy.itemID, 1f, dimension, posx, posy, posz);	
		Utils.doDropRand(world, 0, Items.deadbug.itemID, 1f, dimension, posx, posy, posz);
	}
	
    
    private void jumpAround() {
    	target = null;
    	motiony += 0.55f + Math.abs(world.rand.nextFloat()*0.55f);
    	posy += 0.20f;
    	float f =  0.58f + Math.abs(world.rand.nextFloat()*0.65f);
    	float cdir = (float) Math.toRadians(rotation_yaw);
    	motionx += f*Math.sin(cdir);
    	motionz += f*Math.cos(cdir);
    }

	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "DesertRainFrogtexture.png");	//this is not fast, so we keep our own pointer!
		}

		return texture;
	}
	

}
