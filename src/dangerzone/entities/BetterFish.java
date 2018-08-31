package dangerzone.entities;


import java.util.List;
import java.util.ListIterator;

import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.Effects;
import dangerzone.Player;
import dangerzone.World;
import dangerzone.blocks.Blocks;

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

public class BetterFish extends EntityLiving {
	
	float myspeed = 0;
	public int updown = 0;
	public int movesearchdist = 15;
	
	public BetterFish(World w) {
		super(w);
		maxrenderdist = 160; //in blocks
		this.height = 0.65f;
		this.width = 0.45f;
		uniquename = "DangerZone:BetterFish";
		moveSpeed = 0.25f; //basic usual speed range
		setMaxHealth(3.0f);
		setHealth(3.0f);
		setDefense(1.0f);
		setAttackDamage(0f);
		movefrequency = 50; //direction change frequency
		setExperience(3);
		canSwim = true;
		takesFallDamage = true;
		canBreateUnderWater = true;
		targetLiquidOnly = true; //stay in the water!!!!
		daytimespawn = true;
		nighttimespawn = false;
		daytimedespawn = false;
		nighttimedespawn = true;
		myspeed = moveSpeed;
		
	}
	
	public String getLivingSound(){
		if(world.rand.nextBoolean())return null;
		return "DangerZone:little_splash";
	}
	
	public String getHurtSound(){
		return "DangerZone:little_splat";
	}
	
	public String getDeathSound(){
		return "DangerZone:little_splat";
	}
	
	public void doAttackFrom(/*entity that hit me*/Entity e, /*DamageTypes*/int dt, float pain){	
		if(dt == DamageTypes.WATER)return; //no damage from water, duh!
		super.doAttackFrom(e, dt, pain);
	}
		
	
	//no default actions here!
	public void doEntityAction(float deltaT){	
		int bid;
		int keep_trying = 25;
		int frq = movefrequency;
		if(isMad()){
			frq /= 4;
			if(frq < 2)frq = 2;
		}


		do_swarm_despawn();
		
		if(world.rand.nextInt(200) == 1)heal(0.1f);

		if(enable_hostile){
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
					target = new TargetHelper(targetentity, targetentity.posx, targetentity.posy, targetentity.posz);
					setAttacking(true);
					myspeed = moveSpeed + (moveSpeed * world.rand.nextFloat() * 0.75f);
					frq *= 2;
				}else{
					setAttacking(false);
				}
			}			
			/*
			 * See if we should attack!
			 */
			if(targetentity != null && this.world.rand.nextInt(5) == 1){ //about twice a second
				if(this.getDistanceFromEntity(targetentity) < attackRange+targetentity.getWidth()/2+this.getWidth()/2){
					float dmg = this.getAttackDamage();
					int dt = DamageTypes.HOSTILE;
					targetentity.doAttackFrom(this, dt, dmg);
					this.world.playSound(getAttackSound(), dimension, posx, posy+getHeight()/2, posz, getLivingSoundVolume(), getLivingSoundPitch());	
				}
			}

		}

		//make some noise!
		if(!this.isMad() && !deadflag){
			if(this.world.rand.nextInt(50) == 1){
				this.world.playSound(getLivingSound(), dimension, posx, posy+getHeight()/2, posz, getLivingSoundVolume(), getLivingSoundPitch());					
			}				
		}

		//bounce off most objects, like trees
		bid = 0;
		if(target != null){
			bid = this.world.getblock(this.dimension, (int)this.target.targetx, (int)this.target.targety, (int)this.target.targetz);
			if(bid == Blocks.waterstatic.blockID){
				if(wouldBump(posx + motionx*deltaT, posy + motiony*deltaT + getHeight()/2, posz + motionz*deltaT, getWidth()))bid = 0;
			}
		}
		
		//System.out.printf("bid, dist == %d, %f -- %f, %f, %f\n", bid, this.target.getDistanceToTarget(posx, posy, posz), target.targetx-posx, target.targety-posy, target.targetz-posz);

		if(world.rand.nextInt(100) == 1){
			updown = world.rand.nextInt(3);
			if(world.rand.nextBoolean())updown = -updown;
			updown -= 2;
			//System.out.printf("updown = %d\n",  updown);
		}
		
		if(target!= null && this.target.getDistanceToTarget(posx, posy, posz) < 3.5f+getWidth()){
			target = null;
		}

		if(bid != Blocks.waterstatic.blockID || this.world.rand.nextInt(frq) == 1)
		{	
			if(target == null)target = new TargetHelper(posx, posy, posz);
			bid = 0;
			while(bid != Blocks.waterstatic.blockID && keep_trying != 0){
				if(isSwarming()){
					//fly in a +x and +z direction for a passing swarm...
					this.target.setTarget(posx + this.world.rand.nextInt(movesearchdist), ((posy + (this.world.rand.nextInt(7) - 4))) + updown, posz + this.world.rand.nextInt(movesearchdist));
				}else{
					this.target.setTarget(posx + this.world.rand.nextInt(movesearchdist) - this.world.rand.nextInt(movesearchdist), 
							((posy + (this.world.rand.nextInt(7) - 4)) ) + updown, 
							posz + this.world.rand.nextInt(movesearchdist) - this.world.rand.nextInt(movesearchdist));
				}
				bid = this.world.getblock(this.dimension, (int)this.target.targetx, (int)this.target.targety, (int)this.target.targetz);
				if(bid == Blocks.waterstatic.blockID){
					//try not to go through walls.
					if(!CanProbablySee(dimension, target.targetx, target.targety, target.targetz, (int)Math.sqrt((posx-target.targetx)*(posx-target.targetx) + (posz-target.targetz)*(posz-target.targetz) + (posy-target.targety)*(posy-target.targety))))bid = 0;
				}
				keep_trying--;
				myspeed = moveSpeed * world.rand.nextFloat() * 1.5f;
				if(isMad())myspeed += moveSpeed;
			}
			//System.out.printf("kt = %d, %d\n", keep_trying, bid);
		}
		
		if(enable_buddy){
			if(world.rand.nextInt(findbuddyfrequency) == 1){
				List<Entity> nearby_list = null;
				int nbuddy = 0;
				double totalx, totalz, totaly;
				totalx = totalz = totaly = 0;
				//Get a list of entities within range
				nearby_list = DangerZone.server.entityManager.findEntitiesInRange(findbuddydistance, dimension, posx, posy, posz);
				if(nearby_list != null){
					if(!nearby_list.isEmpty()){
						Entity e = null;
						ListIterator<Entity> li;
						li = nearby_list.listIterator();
						while(li.hasNext()){
							e = (Entity)li.next();												
							if(e != this && isBuddy(e)){
								totalx += e.posx;
								totalz += e.posz;
								totaly += e.posy;
								nbuddy++;
							}		
						}
						if(nbuddy > 0){
							//head towards middle of group
							target = new TargetHelper(totalx/nbuddy, (totaly/nbuddy)-1, totalz/nbuddy);
						}
					}								
				}			
			}
		}

		bid = this.world.getblock(this.dimension, (int)posx, (int)posy, (int)posz);
		if(bid != Blocks.waterstatic.blockID){
			if(!Blocks.isSolid(bid)){
				motiony -= 0.20f*deltaT;
			}else{
				motiony += 0.12f*deltaT;
			}
			if(bid != Blocks.water.blockID){
				if(world.rand.nextInt(50) == 1)doAttackFrom(null, DamageTypes.POISON, 0.1f);
			}
		}

		//Now rotate into direction we should be heading, and update motion!
		if(target != null){
			float dy = (float) (target.targety - this.posy);
			motiony += 0.100f * deltaT; //Counter a little gravity!
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

		if(Math.sqrt((motionx*motionx)+(motionz*motionz)) > myspeed*1.1f){
			motionx *= 0.85f;
			motionz *= 0.85f;
			motiony *= 0.85f;
		}
	
		
		if(enable_lookaround){
			doLookAction();
		}

	}
	
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		//if(e instanceof Piranah)return false;
		int blk = world.getblock(e.dimension, (int)e.posx, (int)e.posy, (int)e.posz);
		if(blk != Blocks.waterstatic.blockID)return false;
		if((e instanceof Player) && CanProbablySeeEntity(e) )return true;
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		return false;
	}

}
