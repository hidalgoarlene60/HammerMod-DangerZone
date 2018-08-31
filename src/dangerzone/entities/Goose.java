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


import dangerzone.Effects;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


public class Goose extends EntityLiving {
	
	public float myspeed;

	public Goose(World w) {
		super(w);
		maxrenderdist = 120; //in blocks
		this.height = 1.25f;
		this.width = 0.75f;
		uniquename = "DangerZone:Goose";
		moveSpeed = 0.10f;
		setMaxHealth(10.0f);
		setHealth(10.0f);
		setDefense(1.0f);
		setAttackDamage(0f);
		movefrequency = 55;
		setExperience(14);
		canSwim = true;
		swimoffset = -0.75f;
		setCanDespawn(false);
		takesFallDamage = false;
		setOnGround(true); //so the trophy doesn't flap!
		enableFollowHeldFood(12);
		enableBreeding(10);
		if(w != null)myspeed = 0.32f + w.rand.nextFloat()*0.10f;
	}
	
	public void init(){
		super.init();
	}
	
	public String getLivingSound(){
		return "DangerZone:goose_living";
	}
	
	public String getHurtSound(){
		return "DangerZone:goose_hit";
	}
	
	public String getDeathSound(){
		return "DangerZone:goose_death";
	}
	
	public boolean isFoodItem(int foodid){
		if(foodid == Items.corn.itemID)return true;
		return false;
	}
	
	public boolean isMate(Entity e){
		if(e instanceof Goose)return true;
		return false;
	}
	
	public void makeBaby(Entity p1, Entity p2){
		Entity newme = world.createEntityByName("DangerZone:Gosling", p1.dimension, (p1.posx+p2.posx)/2, (p1.posy+p2.posy)/2, (p1.posz+p2.posz)/2);
		if(newme != null){
			newme.init();	
			world.spawnEntityInWorld(newme);
			world.playSound("DangerZone:pop", dimension, (p1.posx+p2.posx)/2, (p1.posy+p2.posy)/2, (p1.posz+p2.posz)/2, 1.0f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.3f);
		}
	}
	
	public void doDeathDrops(){
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophygoose.itemID, 1f, dimension, posx, posy, posz);
		Utils.doDropRand(world, 0, Items.goosemeat.itemID, 1f, dimension, posx, posy, posz);
		int i=1+world.rand.nextInt(4);
		for(int j=0;j<i;j++){
			Utils.doDropRand(world, 0, Items.feather.itemID, 1.5f, dimension, posx, posy, posz);
		}
		//easter egg!
		if(world.rand.nextInt(1000) == 42){
			for(int j=0;j<1000;j++){
				Utils.doDropRand(world, 0, Items.feather.itemID, 3.5f, dimension, posx, posy, posz);
			}
		}
	}
	
	public void update(float deltaT){
		if(this.world.isServer){
			if(world.rand.nextInt(5000) == 1){
				Utils.doDropRand(world, 0, Items.egggosling.itemID, 0.1f, dimension, posx, posy, posz);
			}
		}
		super.update(deltaT);
	}
	
	public void doEntityAction(float deltaT){	
		
		if(!isSwarming()){
			super.doEntityAction(deltaT);
			return;
		}
		
		int bid;
		int keep_trying = 20;
		
		if(target == null)target = new TargetHelper(posx, posy, posz);
		canFly = true;
		setFlying(true);
		do_swarm_despawn();
				
		//make some noise!
		if(!this.isHurt() && !deadflag){
			if(this.world.rand.nextInt(50) == 1){
				this.world.playSound(getLivingSound(), dimension, posx, posy+getHeight()/2, posz, getLivingSoundVolume(), getLivingSoundPitch());					
			}				
		}
				
		//bounce off most objects, like trees
		bid = this.world.getblock(this.dimension, (int)this.target.targetx, (int)this.target.targety, (int)this.target.targetz);
		if(bid == 0){
			if(wouldBump(posx + motionx*deltaT, posy + motiony*deltaT + getHeight()/2, posz + motionz*deltaT, getWidth()))bid = 1;
		}
		
    	if(bid != 0 || this.world.rand.nextInt(50) == 1 || this.target.getDistanceToTarget(posx, posy, posz) < 2.0f)
    	{
    		int updown = 5;
    		//stay around 15 from ground...
			for(int k=1;k<15;k++){
				bid = world.getblock(dimension, (int)this.posx, (int)this.posy-k, (int)this.posz);
				if(bid != 0){
					updown = -1; //go up!
					break;
				}
			}			
			//find a new empty space we can go to
    		bid = 1;
    		while(bid != 0 && keep_trying != 0){
    			if(isSwarming()){
    				//fly in a +x and +z direction for a passing swarm...
    				this.target.setTarget(posx + this.world.rand.nextInt(20), posy + (this.world.rand.nextInt(7) - updown), posz + this.world.rand.nextInt(20));
    			}else{
    				this.target.setTarget(posx + this.world.rand.nextInt(15) - this.world.rand.nextInt(15), posy + (this.world.rand.nextInt(7) - updown), posz + this.world.rand.nextInt(15) - this.world.rand.nextInt(15));
    			}
    			bid = this.world.getblock(this.dimension, (int)this.target.targetx, (int)this.target.targety, (int)this.target.targetz);
    			if(bid == 0){
    				//try not to go through walls.
    				if(!CanProbablySee(dimension, target.targetx, target.targety, target.targetz, (int)Math.sqrt((posx-target.targetx)*(posx-target.targetx) + (posz-target.targetz)*(posz-target.targetz) + (posy-target.targety)*(posy-target.targety))))bid = 1;
    			}
    			keep_trying--;
    			myspeed = 0.30f + world.rand.nextFloat()*0.10f;
    		}
    		//System.out.printf("kt = %d\n", keep_trying);
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
		
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Goosetexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}
