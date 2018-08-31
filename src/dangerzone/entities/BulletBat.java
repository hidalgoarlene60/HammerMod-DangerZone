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
import dangerzone.Effects;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


public class BulletBat extends EntityLiving {
	
	public float myspeed = 0;
	
	public	BulletBat(World w){
		super(w);
		maxrenderdist = 128; //in blocks
		this.height = 0.5f;
		this.width = 0.75f;
		uniquename = "DangerZone:Bullet Bat";
		setMaxHealth(5.0f);
		setHealth(5.0f);
		setDefense(1f);
		setAttackDamage(1f);
		if(w != null)myspeed = 0.35f + w.rand.nextFloat()*0.10f;
		takesFallDamage = false;
		setExperience(1);
		daytimespawn = true;
		nighttimespawn = true;
		daytimedespawn = false;
		nighttimedespawn = false;
		canFly = true;
		setFlying(true);
		temperament = Temperament.HOSTILE;
		searchDistance = 32;
		attackRange = 3.0f;
		setVarInt(12, 0); //time to live
	}
	
	public String getLivingSound(){
		if(world.rand.nextBoolean())return "DangerZone:chirp1";
		return "DangerZone:chirp2";
	}
	
	public String getAttackSound(){
		if(world.rand.nextBoolean())return "DangerZone:batattack1";
		return "DangerZone:batattack2";
	}
	
	public String getDeathSound(){
		return "DangerZone:little_splat";
	}
	
	
	//no default actions here!
	public void doEntityAction(float deltaT){	
		int bid;
		int keep_trying = 25;
		
		if(target == null)target = new TargetHelper(posx, posy, posz);
		
		do_swarm_despawn();
		
		/*
		 * See if there is something around to chase...
		 */
		searchcounter++;
		if(searchcounter >= 10){ //re-target about a second
			searchcounter = world.rand.nextInt(4);
			if(hurtMe != null && hurtMe.deadflag)hurtMe = null;
			if(this.world.rand.nextInt(30) == 1)hurtMe = null;
			targetentity = hurtMe;
			if(targetentity == null)targetentity = findSomethingToAttack();
			if(targetentity != null){
				//System.out.printf("attackable entity found!\n");
				target = new TargetHelper(targetentity, targetentity.posx, targetentity.posy+1, targetentity.posz);
				setAttacking(true);
			}else{
				setAttacking(false);
			}
		}			
		/*
		 * See if we should attack!
		 */
		if(targetentity != null && this.world.rand.nextInt(8) == 1){ //about once a second
			if(this.getDistanceFromEntity(targetentity) < attackRange+targetentity.getWidth()/2+this.getWidth()/2){
				float dmg = this.getAttackDamage();
				int dt = DamageTypes.HOSTILE;
				targetentity.doAttackFrom(this, dt, dmg);
				this.world.playSound(getAttackSound(), dimension, posx, posy+getHeight()/2, posz, getLivingSoundVolume(), getLivingSoundPitch());	
			}
		}
		
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
    		//stay around 10 from ground...
			for(int k=1;k<10;k++){
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
    				this.target.setTarget(posx + this.world.rand.nextInt(15), posy + (this.world.rand.nextInt(7) - updown), posz + this.world.rand.nextInt(15));
    			}else{
    				this.target.setTarget(posx + this.world.rand.nextInt(15) - this.world.rand.nextInt(15), posy + (this.world.rand.nextInt(7) - updown), posz + this.world.rand.nextInt(15) - this.world.rand.nextInt(15));
    			}
    			bid = this.world.getblock(this.dimension, (int)this.target.targetx, (int)this.target.targety, (int)this.target.targetz);
    			if(bid == 0){
    				//try not to go through walls.
    				if(!CanProbablySee(dimension, target.targetx, target.targety, target.targetz, (int)Math.sqrt((posx-target.targetx)*(posx-target.targetx) + (posz-target.targetz)*(posz-target.targetz) + (posy-target.targety)*(posy-target.targety))))bid = 1;
    			}
    			keep_trying--;
    			myspeed = 0.35f + world.rand.nextFloat()*0.10f;
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
	
	
	//Override
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof BulletBat)return false;
		if((e instanceof Player) && CanProbablySeeEntity(e) )return true;
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		return false;
	}
	
	
	public void doDeathDrops(){
		if(world.rand.nextInt(20)==1)Utils.doDropRand(world, 0, Items.trophybulletbat.itemID, 1f, dimension, posx, posy, posz);
		Utils.doDropRand(world, 0, Items.deadbug.itemID, 1f, dimension, posx, posy, posz);
	}

	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "BulletBattexture.png");
		}
		return texture;
	}

}
