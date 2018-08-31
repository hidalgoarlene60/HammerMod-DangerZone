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


import java.util.Collections;
import java.util.List;
import java.util.ListIterator;




import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.Dimensions;
import dangerzone.Effects;
import dangerzone.GameModes;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.ServerHooker;
import dangerzone.ToDoList;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.items.Item;
import dangerzone.items.ItemArmor;
import dangerzone.items.ItemAxe;
import dangerzone.items.ItemFood;
import dangerzone.items.ItemPickAxe;
import dangerzone.items.ItemShovel;
import dangerzone.items.ItemSword;
import dangerzone.items.Items;
import dangerzone.threads.LightingThread;
import dangerzone.entities.Entity;


public class EntityLiving extends Entity {
	
	public float deathfactor = 0.0f;
	public TargetHelper target = null;
	public TargetHelper idletarget = null;
	public float moveSpeed = 0.25f;
	public float accellerator = 1.0f;
	public float petaccellerator = 1.0f;
	public boolean newtargetnow = false;
	public int movefrequency = 100; //Higher = less often! Lower = more often!
	public int damage_backoff = 0;
	public int fallcount = 0;
	public int lastbx, lastby, lastbz, lastbd;
	public int lastinx, lastiny, lastinz, lastind;
	public boolean canSwim = true;

	public float swimoffset = 0;
	public int closest = 99999;
	public int tx = 0, ty = 0, tz = 0;
	public int tickadjust = 0;
	
	public float jumpstrength = 0.90f;
	public boolean targetLiquidOnly = false;
	
	//There are a few convenience routines below to help set all these flags and values...
	
	//looking around. almost always enabled.
	public boolean enable_lookaround = true;
	public GenericTargetSorter LookTargetSorter = null;
	public float lookDistance = 16;
	
	//avoiding entities
	public boolean enable_avoid = false;
	public int avoiddistance = 10;
	
	//keeping close to another entity
	public boolean enable_buddy = false;
	public int findbuddydistance = 10;
	public int findbuddyfrequency = 20;
	
	//enable hostilities
	public boolean enable_hostile = false;
	public GenericTargetSorter TargetSorter = null;
	public float searchDistance = 16f;
	public float attackRange = 2.0f;
	public int searchcounter = 0;
	public Entity hurtMe = null;
	public Entity targetentity = null;
	public volatile float armangle = 0f;
	public int armdir = 0;
	
	//enable finding a food block
	public boolean enable_findfoodblock = false;
	public int foodblockfreq = 60;
	public int foodblockdistxz = 12;
	public int foodblockdisty = 3;
	public int foodblockdisteat = 12;
	public int foodblockheal = 5;
	public boolean fooddaytimeonly = true;
	
	//when to spawn and despawn
	public boolean daytimespawn = true;
	public boolean daytimedespawn = false;
	public boolean nighttimespawn = false;
	public boolean nighttimedespawn = true;
	
	//following held food
	public boolean enable_followfood = false;
	public int foodfollowDistance = 12;
	public GenericTargetSorter FoodSorter = null;
	
	//find dropped food
	public boolean enable_droppedfood = false;
	public int foodsearchDistance = 12;
	
	//taming
	public boolean enable_taming = false;
	public int ownercheckcounter = 0;
	public float maxdisttoowner = 16f;
	
	//babies growing up
	public int growupmax = 10000;
	public int growupcounter = 0;
	
	//making babies
	public boolean enable_breeding = false;
	public int breeding = 0;
	public int findmatedistance = 10;
	
	public boolean canridemaglevcart = false;
	public boolean tower_defense_enable = false;

	
	//DO NOT USE "world" IN THIS ROUTINE! IT MAY BE NULL!!! USE DangerZone.rand, or do something else!!!!
	public	EntityLiving(World w){
		super(w);	
		setMaxHealth(1.0f); //default
		setHealth(1.0f);
		setDefense(1.0f); //default - attack damage is DIVIDED by 
		deathfactor = 0;
		takesFallDamage = true;
		canSwim = true;
		setMaxAir(40);
		setAir(40);
		setCanDespawn(true);
		temperament = Temperament.PASSIVE;
		eyeheight = 1f;
		lastbx = lastby = lastbz = lastbd = 0;
		lastinx = lastiny = lastinz = lastind = 0;

	}
	
	//DO NOT USE "world" IN THIS ROUTINE! IT MAY BE NULL!!! USE DangerZone.rand, or do something else!!!!
	public void init(){
		super.init();
		eyeheight = getHeight()*9/10;
		LookTargetSorter = new GenericTargetSorter(this);
		TargetSorter = new GenericTargetSorter(this); //not used here, but still in some old entities...
		FoodSorter = new GenericTargetSorter(this);
		lookDistance = getWidth()*10; //default
		if(lookDistance > 64)lookDistance = 64;
		jumpstrength = 0.15f * getHeight();
		if(enable_taming && getOwnerName() != null){
			setCanDespawn(false);
			if(temperament == Temperament.HOSTILE)temperament = Temperament.PASSIVE_AGGRESSIVE;
		}
		if(isBaby())growupcounter = growupmax/2 + DangerZone.rand.nextInt(growupmax/2);
	}
	

	
	public void setSpawning(boolean spawn_daytime, boolean despawn_daytime, boolean spawn_nighttime, boolean despawn_nighttime){
		daytimespawn = spawn_daytime;
		daytimedespawn = despawn_daytime;
		nighttimespawn = spawn_nighttime;
		nighttimedespawn = despawn_nighttime;
	}
	
	public void enableLookAround(int distance){
		enable_lookaround = true;
		lookDistance = distance;
	}
	
	public void enableBreeding(int distance){
		enable_breeding = true;
		findmatedistance = distance;
	}
	
	public void enableAvoidEntity(int distance){
		enable_avoid = true;
		avoiddistance = distance;
	}
	
	public void enableFindBuddy(int distance, int frequency){
		enable_buddy = true;
		findbuddydistance = distance;
		findbuddyfrequency = frequency;
	}
	
	public void enableHostility(float search_distance, float attack_range){
		enable_hostile = true;
		searchDistance = search_distance;
		attackRange = attack_range;
		temperament = Temperament.HOSTILE;
	}
	
	public void enableFindFoodBlock(int frequency, int distance_xz, int distance_y, int distance_eat, int healamount, boolean daytimeonly){
		enable_findfoodblock = true;
		foodblockfreq = frequency;
		foodblockdistxz = distance_xz;
		foodblockdisty = distance_y;
		foodblockdisteat = (distance_eat*distance_eat)*3; //just distance without sqrt();
		foodblockheal = healamount;
		fooddaytimeonly = daytimeonly;
	}
	
	public void enableFollowHeldFood(int distance){
		enable_followfood = true;
		foodfollowDistance = distance;
	}
	
	public void enableDroppedFood(int distance){
		enable_droppedfood = true;
		foodsearchDistance = distance;
	}
	
	public void enableTaming(int max_distance_to_owner){
		enable_taming = true;
		maxdisttoowner = max_distance_to_owner;
	}
	
	public boolean getCanDespawn(){
		if(getOwnerName() != null)return false;
		if((getVarInt(5)&0x10) == 0)return false;
		return true;
	}
	
	//Check all around entity WIDTH to stop from running into things
	//make entity just a little fatter (0.01) so that isSolidAtLevel doesn't accidentally get triggered true
	public boolean doSolidsPushback(float kf, float deltaT){
		float wdth = getWidth();
		int intwidth = (int)((wdth/2.0f)+0.995f);
		int i, j;
		int itemp;
		double dx, dz;
		double dxsave;
		boolean lh, rh, fh, bh;
		float tmx = motionx*deltaT;
		float tmz = motionz*deltaT;
		float rate = (float)((float)DangerZone.entityupdaterate/DangerZone.serverentityupdaterate);
		if(!world.isServer){
			tmx *= rate;
			tmz *= rate;
		}
		boolean hitsomething = false;
		double dist =  Math.sqrt(tmx*tmx + tmz*tmz);
		double curdist = 0.0f;
		int bid = 0;
		
		if(dist == 0)return false;
		float nudge = 0.02f;
		float bounce = 2;
		lh = rh = fh = bh = false;
		
		if(this instanceof Player){
			if(world.getblock(dimension, (int)posx, (int)(posy+getHeight())+2, (int)posz) != 0 || world.getblock(dimension, (int)posx, (int)(posy+getHeight())+3, (int)posz) != 0){
				nudge = 0.02f;
				bounce = 20f;
				//System.out.printf("intwidth = %d, kf = %f\n", intwidth, kf);
			}
		}
		/*
		 * Have to increment this out like a big fat ray for things that are moving fast!
		 */
		while(curdist < dist && !hitsomething){
			curdist += 0.1f;	
			if(curdist > dist)curdist = dist;
			double mx = (curdist/dist)*tmx;
			double mz = (curdist/dist)*tmz;
			double upx = posx + mx;
			double upz = posz + mz;
			//int bx, bz;

			for(i=-intwidth;i<=intwidth;i++){
				for(j=-intwidth;j<=intwidth;j++){
					bid = world.getblock(dimension, (int)upx+i, (int)(posy+kf), (int)upz+j);
					if(bid != 0 && Blocks.isSolid(bid, world, dimension, (int)upx+i, (int)(posy+kf), (int)upz+j)){				
						
						itemp = (int)(upx)+i;
						dx = ((double)itemp + 0.5f) - posx;
						itemp = (int)(upz)+j;
						dz = ((double)itemp + 0.5f) - posz;
						

						if(Math.abs(dx)-(0.51f + (wdth/2)) < 0 && Math.abs(dz)-(0.51f + (wdth/2)) < 0){
							//already in a block. probably just jumped off. give them a nudge...
							if(dx > 0){
								posx -= nudge;
								//if(this instanceof Player)System.out.printf("nudge -x\n");
							}else{
								posx += nudge;
								//if(this instanceof Player)System.out.printf("nudge +x\n");
							}
							if(dz > 0){
								posz -= nudge;
								//if(this instanceof Player)System.out.printf("nudge -z\n");
							}else{
								posz += nudge;
								//if(this instanceof Player)System.out.printf("nudge +z\n");
							}
							itemp = (int)(posx)+i;
							dx = ((double)itemp + 0.5f) - posx;
							itemp = (int)(posz)+j;
							dz = ((double)itemp + 0.5f) - posz;						
						}
						dxsave = dx;
						if(dx < 0 && mx < 0){
							//heading towards the solid, negative
							//but would I even hit it in the z direction?
							if(Math.abs(dz) < (0.51f + (wdth/2))){
								dx += 0.51f;
								dx += wdth/2;
								if(dx > mx){
									//but will we actually hit this block, or is it an edge effect?
									if(!Blocks.isSolid(world.getblock(dimension, (int)upx+i+1, (int)(posy+kf), (int)upz+j), world, dimension, (int)upx+i+1, (int)(posy+kf), (int)upz+j)){
										//motionx = dx/deltaT;
										if(!lh)motionx = -motionx/bounce;
										//if(this instanceof Player)System.out.printf("new < mx = %f\n", motionx);
										hitsomething = true;
										Blocks.bumpedBlock(bid, this, world, dimension, (int)upx+i, (int)(posy+kf), (int)upz+j);
										lh = true;
									}
								}
							}
						}else if(dx > 0 && mx > 0){
							//heading towards the solid, positive
							//but would I even hit it in the z direction?
							if(Math.abs(dz) < (0.51f + (wdth/2))){
								dx -= 0.51f;
								dx -= wdth/2;
								if(dx < mx){
									if(!Blocks.isSolid(world.getblock(dimension, (int)upx+i-1, (int)(posy+kf), (int)upz+j), world, dimension, (int)upx+i-1, (int)(posy+kf), (int)upz+j)){
										//motionx = dx/deltaT;
										if(!rh)motionx = -motionx/bounce;
										//if(this instanceof Player)System.out.printf("new > mx = %f\n", motionx);
										hitsomething = true;
										Blocks.bumpedBlock(bid, this, world, dimension, (int)upx+i, (int)(posy+kf), (int)upz+j);
										rh = true;
									}
								}
							}
						}
						dx = dxsave;
						if(dz < 0 && mz < 0){
							//heading towards the solid, negative
							//but would I even hit it in the x direction?
							if(Math.abs(dx) < (0.51f + (wdth/2))){
								dz += 0.51f;
								dz += wdth/2;
								if(dz > mz){
									if(!Blocks.isSolid(world.getblock(dimension, (int)upx+i, (int)(posy+kf), (int)upz+j+1), world, dimension, (int)upx+i, (int)(posy+kf), (int)upz+j+1)){
										//motionz = dz/deltaT;
										if(!fh)motionz = -motionz/bounce;
										//if(this instanceof Player)System.out.printf("new < mz = %f\n", motionz);
										hitsomething = true;
										Blocks.bumpedBlock(bid, this, world, dimension, (int)upx+i, (int)(posy+kf), (int)upz+j);
										fh = true;
									}
								}
							}
						}else if(dz > 0 && mz > 0){
							//heading towards the solid, positive
							//but would I even hit it in the x direction?
							if(Math.abs(dx) < (0.51f + (wdth/2))){
								dz -= 0.51f;
								dz -= wdth/2;
								if(dz < mz){
									if(!Blocks.isSolid(world.getblock(dimension, (int)upx+i, (int)(posy+kf), (int)upz+j-1), world, dimension, (int)upx+i, (int)(posy+kf), (int)upz+j-1)){
										//motionz = dz/deltaT;
										if(!bh)motionz = -motionz/bounce;
										//if(this instanceof Player)System.out.printf("new > mz = %f\n", motionz);
										hitsomething = true;
										Blocks.bumpedBlock(bid, this, world, dimension, (int)upx+i, (int)(posy+kf), (int)upz+j);
										bh = true;
									}
								}
							}
						}
						
					}
				}
			}
		}
		return hitsomething;
	}
	
	public boolean wouldBump(double x, double y, double z, float w){
		int intwidth = (int)((w/2.0f)+0.995f);
		int i, j;
		int itemp;
		double dx, dz;
		for(i=-intwidth;i<=intwidth;i++){
			for(j=-intwidth;j<=intwidth;j++){
				if(Blocks.isSolid(world.getblock(dimension, (int)x+i, (int)y, (int)z+j), world, dimension, (int)x+i, (int)y, (int)z+j)){
					itemp = (int)(posx)+i;
					dx = x - ((double)itemp + 0.5f);
					itemp = (int)(posz)+j;
					dz = z - ((double)itemp + 0.5f);	
					if(Math.sqrt((dx*dx)+(dz*dz)) < (0.5f + (w/2.0f))){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//Check all around the entity WIDTH to see if it is solid.
	//Used for Y collisions
	public boolean isSolidAtLevel(int d, double x, double y, double z){
		float wdth = getWidth();
		int intwidth = (int)((wdth/2.0f)+0.995f);
		int i, j;
		int itemp;
		double dx, dz;
		
		for(i=-intwidth;i<=intwidth;i++){
			for(j=-intwidth;j<=intwidth;j++){
				if(Blocks.isSolid(world.getblock(d, (int)x+i, (int)y, (int)z+j), world, d, (int)x+i, (int)y, (int)z+j)){
					itemp = (int)(x)+i;
					dx = x - ((double)itemp + 0.5f);
					dx = Math.abs(dx);
					if(dx < (0.49f + (wdth/2.0f))){
						itemp = (int)(z)+j;
						dz = z - ((double)itemp + 0.5f);
						dz = Math.abs(dz);
						if(dz < (0.49f + (wdth/2.0f))){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean isLiquidAtLevel(int d, double x, double y, double z){
		float wdth = getWidth();
		int intwidth = (int)((wdth/2.0f)+0.995f);
		int i, j;
		int itemp;
		double dx, dz;
		
		for(i=-intwidth;i<=intwidth;i++){
			for(j=-intwidth;j<=intwidth;j++){
				if(Blocks.isLiquid(world.getblock(d, (int)x+i, (int)y, (int)z+j))){
					itemp = (int)(x)+i;
					dx = x - ((double)itemp + 0.5f);
					dx = Math.abs(dx);
					if(dx < (0.49f + (wdth/2.0f))){
						itemp = (int)(z)+j;
						dz = z - ((double)itemp + 0.5f);
						dz = Math.abs(dz);
						if(dz < (0.49f + (wdth/2.0f))){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	
	public boolean isDying(){
		if(deathfactor > 0 && deathfactor < 8.0f)return true;
		return false;
	}
	
	public void doDeathAnimation(){
		deathfactor = 0.9f; //start
		motiony = motionx = motionz = 0;
		if(!world.isServer)Utils.spawnDeathParticles(world, dimension, posx, posy, posz,getWidth(), getHeight());
	}
	
	public float getDeathFactor(){
		return deathfactor;
	}
	
	//Only called on server
	public void doEntityCollisions(float deltaT){
		if(ignoreCollisions)return;
		
		float wdth = getWidth();
		List<Entity> nearby_list = null;
		ListIterator<Entity> li;
		Entity ridden = getRiddenEntity();
		Entity rider = getRiderEntity();
		int riddenid = 0;
		int riderid = 0;
		if(ridden != null)riddenid = ridden.entityID;
		if(rider != null)riderid = rider.entityID;

		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		nearby_list = DangerZone.server.entityManager.findEntitiesInRange((wdth/2) + 8f, dimension, posx, posy, posz);

		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				double dir;
				double dist;
				Entity e = null;
				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (Entity)li.next();
					if(e != this && !e.ignoreCollisions){ //don't bump self!
						if(posy+getHeight() > e.posy && posy < e.posy+e.getHeight()){
							dist = e.getHorizontalDistanceFromEntity(this); //Center to center
							dist -= (wdth/2); //width of me
							dist -= (e.getWidth()/2); //width of it
							if(dist < 0){
								//Bumped something!
								if(riddenid == e.entityID || riderid == e.entityID)continue; //oops nevermind...
								//do x-z bumping...
								dir = Math.atan2(e.posz-posz, e.posx-posx);
								dist = -0.25f;
								motionx += Math.cos(dir)*dist*deltaT;
								motionz += Math.sin(dir)*dist*deltaT;
								justCollidedWith(e);
							}
						}
					}
				}								
			}			
		}				
	}
	
	public void setBaby(boolean tf){
		if(tf){
			if(growupmax < 300)growupmax = 300;
			if(world != null)growupcounter = growupmax/2 + world.rand.nextInt(growupmax/2);
			if(!isBaby())doGrowDown();
		}else{
			if(isBaby())doGrowUp();
		}
		super.setBaby(tf);
	}
	
	public void doGrowUp(){	
		super.setBaby(false);
		//System.out.printf("grow up sethealth to %f\n", getMaxHealth());
		setHealth(getMaxHealth());
	}
	
	public void doGrowDown(){
		super.setBaby(true);
		//System.out.printf("grow down sethealth to %f\n", getMaxHealth());
		setHealth(getMaxHealth());
	}
				

	/*
	 * Update entity motion
	 * 
	 * THIS ROUTINE IS NOT MEANT TO BE OVERRIDDEN. 
	 * IF YOU DO, CALL IT VIA super.update(deltaT) or your motion may not get updated.
	 * OVERRIDE THE doEntityAction() routine or one if it's subroutines instead.
	 * 
	 */
	public void update(float deltaT){
		int k, i, intheight;
		float startheight;
		int imax, bid, lbid;
		float gravity = 0.045f * deltaT;
		float rate = (float)((float)DangerZone.entityupdaterate/DangerZone.serverentityupdaterate); // ~1/6
		Entity rider = null;

		//so it applies to players too!
		if(world.isServer && isBaby()){
			growupcounter--;
			if(growupcounter <= 0){
				setBaby(false);
			}
		}

		//server side sets attacking or not.
		//both sides read it and set armdir. Only client actually displays...

		if(getAttacking()){
			if(armdir == 0){
				armdir = 1;
				armangle = 0;
			}
		}
		if(armdir != 0){
			if(world.isServer){
				if(armdir > 0){
					armangle += 50f*deltaT;
					if(armangle > 150f){
						armdir = -1;
					}
				}
				if(armdir < 0){
					armangle -= 50f*deltaT;
					if(armangle <= 0){
						armdir = 0;
						armangle = 0;					
						if(this instanceof Player)setAttacking(false);
					}
				}
			}else{
				if(armdir > 0){
					armangle += 20f*deltaT;
					if(armangle > 150f){
						armdir = -1;
					}
				}
				if(armdir < 0){
					armangle -= 20f*deltaT;
					if(armangle <= 0){
						armdir = 0;
						armangle = 0;					
					}
				}
			}
		}
		//everything can heal
		if(!(this instanceof Player))if(world.isServer && world.rand.nextInt(250) == 1)heal(1);



		if(world.isServer && getCanDespawn()){
			if((world.isDaytime() && daytimedespawn) || (!world.isDaytime() && nighttimedespawn)){
				//Despawning... 
				double dist = 0;
				Player p = DangerZone.server.findNearestPlayer(this);
				if(p == null){
					deadflag = true;
				}else{
					int ratemul = 1;
					if(getAttacking())ratemul = 5; //busy, stick around!
					dist = Utils.getDistanceBetweenEntities(this, p);
					if(dist > DangerZone.entityupdatedist){
						deadflag = true;
					}else if(dist > DangerZone.entityupdatedist/2){
						if(world.rand.nextInt(100*ratemul) == 0){
							deadflag = true;
						}
					}else if(dist > DangerZone.entityupdatedist/4){
						if(world.rand.nextInt(250*ratemul) == 0){
							deadflag = true;
						}
					}else if(dist > DangerZone.entityupdatedist/8){
						if(world.rand.nextInt(500*ratemul) == 0){
							deadflag = true;
						}
					}
				}
			}
		}

		if(damage_backoff > 0)damage_backoff--;
		if(hurtanimationtimer > 0)hurtanimationtimer--;
		if(madtimer > 0)madtimer--;
		if(deadflag)deathfactor += 0.11f; //Delta death animation expander
		if(isDying())deadflag = true;

		if(world.isServer){
			int maybenot = (int)getTotalEffect(Effects.CONFUSION);
			if(maybenot >= 2){
				if(world.rand.nextInt(maybenot) != 1){
					addKnockback(world.rand.nextFloat()*(float)Math.PI*2, world.rand.nextFloat()*maybenot/10, 0);
				}
			}
		}

		if(ignoreCollisions){
		
			//Ghost mode (noClip) just update motion as per caller...

			if(world.isServer){	
				if(getOnFire() > 0){
					setOnFire(0);
				}
				updateEffects();
				float healthadjust = getTotalEffect(Effects.REGENERATION);
				healthadjust -= getTotalEffect(Effects.POISON);
				if(healthadjust < 0){
					doAttackFrom(this, DamageTypes.POISON, Math.abs(healthadjust)); 
				}else{
					if(healthadjust > 0){
						heal(healthadjust);
					}
				}

			}

			super.update(deltaT);
			return;
		}

		if(world.isServer){		
			gravity = 0.20f * deltaT;	
			
			//Common logic

			float healthadjust = getTotalEffect(Effects.REGENERATION);
			healthadjust -= getTotalEffect(Effects.POISON);
			if(healthadjust < 0){
				doAttackFrom(this, DamageTypes.POISON, Math.abs(healthadjust)); 
			}else{
				if(healthadjust > 0){
					heal(healthadjust);
				}
			}			
			updateEffects();			
			if(isImmuneToFire && getOnFire()>0)setOnFire(0);

			if(getOnFire() > 0){
				setOnFire(getOnFire()-1);
				firecounter++;
				if(firecounter > 20){
					doAttackFrom(this, DamageTypes.FIRE, 1);
					firecounter = 0;
				}
			}

			bid = world.getblock(dimension, (int)posx, (int)(posy+eyeheight), (int)posz);
			if(!Blocks.isLiquid(bid) || canBreateUnderWater){
				setAir(getMaxAir());
			}else{
				float air = 0.06f;
				if(this instanceof Player){
					int diffi = getGameDifficulty();
					if(diffi == -1)air = 0.03f;
					if(diffi == -2)air = 0.01f;
					if(diffi == 1)air = 0.12f;
					if(diffi == 2)air = 0.33f;
				}
				setAir(getAir()-air*deltaT);
			}
			if(getAir() < 0){
				doAttackFrom(null, DamageTypes.WATER, 2.5f*deltaT);
			}

			if(has_inventory){
				//tick everything
				InventoryContainer ic = null;
				for(i=0;i<maxinv;i++){
					ic = entity_inventory[i];
					if(ic != null){
						ic.inventoryTick(this, i);
					}
				}
				//tick the held item/block
				ic = getHotbar(gethotbarindex());
				if(ic != null){
					ic.inUseTick(this, gethotbarindex());
				}
				//tick armor being worn
				for(i=60;i<64;i++){
					ic = entity_inventory[i];
					if(ic != null){
						ic.armorTick(this, i-60);
					}
				}	
			}
			
			//is in a liquid?
			intheight = (int)getHeight();
			intheight++;
			boolean tmpb = false;
			boolean tmps = false;
			int sbid = 0;
			lbid = 0;
			for(k=0;k<intheight;k++){
				bid = world.getblock(dimension, (int)posx, (int)posy+k, (int)posz);
				if(Blocks.isLiquid(bid)){
					tmpb = true;
					lbid = bid;
				}
				if(Blocks.isSquishy(bid)){
					tmps = true;
					sbid = bid;
				}
			}
			setInLiquid(tmpb);
			if(getInLiquid()){
				fallcount = 0;
				float ff = Blocks.getFriction(lbid);
				if(ff != 0){
					ff = 1.0f - ff;
					if(ff < 0)ff = 0;
					motionx *= ff;
					motionz *= ff;
					motiony *= ff;
				}
				Blocks.entityInLiquid(lbid, this);
			}
			if(tmps){
				fallcount = 0;
				float ff = Blocks.getFriction(sbid);
				if(ff != 0){
					ff = 1.0f - ff;
					if(ff < 0)ff = 0;
					motionx *= ff;
					motionz *= ff;
					motiony *= ff;
				}
			}
			
			bid = world.getblock(dimension, (int)posx, (int)(posy+0.1f), (int)posz);
			Blocks.doIsIn(bid, this, world, dimension, (int)posx, (int)(posy+0.1f), (int)posz);
			if(dimension != lastind || (int)posx != lastinx || (int)(posy+0.1f) != lastiny || (int)posz != lastinz ){
				lastind = dimension;
				lastinx = (int)posx;
				lastiny = (int)(posy+0.1f);
				lastinz = (int)posz;
				Blocks.doEntered(bid, this, world, dimension, (int)posx, (int)(posy+0.1f), (int)posz); //call only on CHANGE of block
			}

			//creature logic
			if(!(this instanceof Player)){
				//ENTITY ON SERVER

				/*
				 * Normal solids collision for player on client or entities on server
				 */			
				//x and z direction block collisions

				startheight = getHeight()/4;//if it is less then 1/4 our height, we can cross automatically
				while(startheight < getHeight()){
					doSolidsPushback(startheight, deltaT);
					startheight += 1.0f;
					if(startheight > getHeight()){
						doSolidsPushback(getHeight(), deltaT);
					}
				}

				//y direction (down)
				if(getInLiquid())gravity = gravity/5;
				motiony -= gravity;			
				if(isSolidAtLevel(dimension, posx, posy, posz)){
					//I am in a solid
					motiony += gravity*2;
				}

				float df = motiony*deltaT;
				int idf = (int)df;
				df -= idf;
				//System.out.printf("stay %f\n", df);
				if(motiony < 0){
					for(i=0;i>=idf;i--){
						if(isSolidAtLevel(dimension, posx, posy+df+i, posz)){ 
							imax = (int)(posy+df+i) + 1;
							motiony = 0;
							posy = imax+0.01f;
							//System.out.printf("stay %f\n", df);
							if(fallcount > 7){
								//System.out.printf("fallcount ouch = %d, eid %d\n", fallcount, entityID);
								doAttackFrom(this, DamageTypes.FALL, getAdjustedFallDamage(fallcount-7));
							}
							fallcount = 0;
							break;
						}
					}
				}			

				//y direction (up)	
				if(motiony >= 0){
					if(!isSolidAtLevel(dimension, posx, posy+getHeight(), posz)){
						df = motiony*deltaT;
						idf = (int)df;
						df -= idf;
						for(i=0;i<=idf;i++){
							if(isSolidAtLevel(dimension, posx, posy+df+getHeight()+i, posz)){ 
								imax = (int)(posy+df+getHeight()+i);
								motiony = 0;
								posy = (double)imax-getHeight()-0.01f;									
								fallcount = 0;
								break;
							}
						}
					}
				}

				if(!isSolidAtLevel(dimension, posx, posy-0.15f, posz) && getRiddenEntity() == null){
					setOnGround(false);					
					if(!takesFallDamage){
						fallcount = 0;
					}else{
						if(motiony < 0){
							fallcount++;
							//System.out.printf("fallcount == %d for %s\n", fallcount, uniquename);
						}
					}
					lastbx = lastby = lastbz = lastbd = 0;
				}else{
					setOnGround(true);
					fallcount = 0;							
					//ENTITY ON SERVER
					//Use the block directly underneath...
					bid = world.getblock(dimension, (int)posx, (int)(posy-0.25f), (int)posz);
					float ff = Blocks.getFriction(bid);
					if(ff != 0){
						ff = 1.0f - ff;
						if(ff < 0)ff = 0;
						motionx *= ff;
						motionz *= ff;
					}
					if(dimension != lastbd || (int)posx != lastbx || (int)(posy-0.25f) != lastby || (int)posz != lastbz ){
						lastbd = dimension;
						lastbx = (int)posx;
						lastby = (int)(posy-0.25f);
						lastbz = (int)posz;
						Blocks.doSteppedOn(bid, this, world, dimension, (int)posx, (int)(posy-0.25f), (int)posz);
					}
				}

				if(getInLiquid() && canSwim){
					if(Blocks.isLiquid(world.getblock(dimension, (int)posx, (int)(posy+(getHeight()*3/4) + swimoffset),(int)posz))){				
						if(world.rand.nextInt(9) != 0){
							motiony += 0.08f;
						}				
					}
				}

				if(posy < 0){
					float h = getHealth();
					if(h > 20){
						setHealth(getHealth()/4);
					}else{
						setHealth(getHealth()-1.0f);
					}
				}

				doInLiquid(1.0f);	

			}else{ //Player logic
				////////////IS PLAYER ON SERVER!!!

				if(getGameMode() == GameModes.SURVIVAL){
					if(posy < 0){
						float h = getHealth();
						if(h > 10){
							setHealth(getHealth()-1.0f);
						}else{
							setHealth(getHealth()-0.01f);
						}
					}
				}

				if(world.getblock(dimension, (int)posx, (int)posy, (int)posz) == Blocks.ladder.blockID){
					gravity = 0;
					fallcount = 0;
					setLadder(true);
				}else{
					setLadder(false);
				}

				//airborne or not?
				if(!isSolidAtLevel(dimension, posx, posy-0.10f, posz) && getRiddenEntity() == null){
					setOnGround(false);			
					if(motiony < 0){
						fallcount++;
					}
					if(getGameMode() != GameModes.SURVIVAL)fallcount = 0;	
					lastbx = lastby = lastbz = lastbd = 0;
				}else{
					setOnGround(true);
					fallcount = 0;
					bid = world.getblock(dimension, (int)posx, (int)(posy-0.25f), (int)posz);
					//stepped on something!
					if(getGameMode() != GameModes.GHOST && getGameMode() != GameModes.LIMBO){				
						if(dimension != lastbd || (int)posx != lastbx || (int)(posy-0.25f) != lastby || (int)posz != lastbz ){
							lastbd = dimension;
							lastbx = (int)posx;
							lastby = (int)(posy-0.25f);
							lastbz = (int)posz;
							Blocks.doSteppedOn(bid, this, world, dimension, (int)posx, (int)(posy-0.25f), (int)posz);
						}
					}				
				}
				float df = motiony*deltaT;
				int idf = (int)df;
				df -= idf;
				//System.out.printf("df %f\n", df);
				if(motiony < 0){
					for(i=0;i>=idf;i--){
						if(isSolidAtLevel(dimension, posx, posy+df+i, posz)){ 
							imax = (int)(posy+df+i) + 1;
							motiony = 0;
							posy = imax+0.001f;
							//if(fallcount > 0)System.out.printf("fallcount = %d, rate = %f\n", fallcount, rate);
							int fallmin = 8;
							int diffi = getGameDifficulty();
							if(diffi == -1)fallmin += 2;
							if(diffi == -2)fallmin += 6;
							if(diffi == 1)fallmin -= 1;
							if(diffi == 2)fallmin -= 2;
							if(fallcount > fallmin){
								//System.out.printf("fallcount = %d\n", fallcount);
								doAttackFrom(this, DamageTypes.FALL, getAdjustedFallDamage((fallcount-fallmin)));
								Player ppl = (Player)this;
								ppl.hard_landings++;
								ppl.server_thread.sendStatsToPlayer();								
							}
							fallcount = 0;
							break;
						}
					}
				}
			}
		}else{
			//client side!
			if(getOnFire() != 0){
				if(world.rand.nextInt(60) == 0){
					LightingThread.addRequest(dimension, (int)posx, (int)(posy+getHeight()/2), (int)posz, 0.55f); //make some light!
				}
			}
			//tick the held item/block
			if(has_inventory){
				InventoryContainer ic = getHotbar(gethotbarindex());
				if(ic != null){
					ic.inUseTick(this, gethotbarindex());
				}
			}
			if(this instanceof Player){	//IS PLAYER ON CLIENT			
				//ME!
				if(this == DangerZone.player){	//DO MOTION-BASED THINGS USING VARS SET ON SERVER!			
					/*
					 * Body should follow head...
					 */			
					float cdir = (float) Math.toRadians(rotation_yaw);
					float tdir = (float) Math.toRadians(rotation_yaw_head);
					float ddiff = tdir - cdir;
					while(ddiff>Math.PI)ddiff -= Math.PI*2;
					while(ddiff<-Math.PI)ddiff += Math.PI*2;	    	
					rotation_yaw += (ddiff*180f/Math.PI)/10f;	


					if(getGameMode() == GameModes.GHOST || getGameMode() == GameModes.LIMBO){						
						super.update(deltaT);
						return;
					}
					/*
					 * Normal solids collision for player on client or entities on server
					 */			
					//x and z direction block collisions
					startheight = getHeight()/4;//if it is less then 1/4 our height, we can cross automatically
					while(startheight < getHeight()){
						doSolidsPushback(startheight, deltaT);
						startheight += 1.0f;
						if(startheight > getHeight()){
							doSolidsPushback(getHeight(), deltaT);
						}
					}

					if(isFlying())gravity /= 10;

					//y direction (down)
					if(getInLiquid())gravity = gravity/5;

					if(isLadder()){
						gravity = 0;
						motiony *= 0.90f;
					}

					motiony -= gravity;		
					//System.out.printf("motiony = %f\n", (float)motiony);
					//if(Blocks.isSolid(world.getblock(dimension, (int)posx, (int)posy, (int)posz))){
					if(isSolidAtLevel(dimension, posx, posy, posz)){
						//I am in a solid
						motiony += gravity*2;
						//System.out.printf("g*2\n");
					}

					float df = motiony*deltaT*rate;
					int idf = (int)df;
					df -= idf;
					//System.out.printf("df %f\n", df);
					if(motiony < 0){
						for(i=0;i>=idf;i--){
							if(isSolidAtLevel(dimension, posx, posy+df+i, posz)){ 
								imax = (int)(posy+df+i) + 1;
								motiony = 0;
								posy = imax+0.001f;
								break;
							}
						}
					}

					//y direction (up)	
					if(motiony >= 0){
						if(!isSolidAtLevel(dimension, posx, posy+getHeight(), posz)){
							df = motiony*deltaT*rate;
							idf = (int)df;
							df -= idf;
							for(i=0;i<=idf;i++){
								if(isSolidAtLevel(dimension, posx, posy+df+getHeight()+i, posz)){ 
									//imax = (int)((float)posy+df+getHeight()+i);
									motiony = 0;
									//posy = (float)imax-getHeight()-0.01f;									
									break;
								}
							}
						}
					}

					//airborne or not?
					if(getOnGround()){						
						//update motion based on block friction!
						if(getGameMode() != GameModes.GHOST && getGameMode() != GameModes.LIMBO){				
							//ME!
							bid = world.getblock(dimension, (int)posx, (int)(posy-0.25f), (int)posz);
							float ff = Blocks.getFriction(bid)*rate;
							if(ff != 0){
								ff = 1.0f - ff;
								if(ff < 0)ff = 0;
								motionx *= ff;
								motionz *= ff;
							}
						}				
					}

					//is in a liquid?
					intheight = (int)getHeight();
					intheight++;
					boolean tmps = false;
					boolean tmpb = false;
					int sbid = 0;
					lbid = 0;
					for(k=0;k<intheight;k++){
						bid = world.getblock(dimension, (int)posx, (int)posy+k, (int)posz);
						if(Blocks.isSquishy(bid)){
							tmps = true;
							sbid = bid;
						}
						if(Blocks.isLiquid(bid)){
							tmpb = true;
							lbid = bid;
						}
					}

					if(tmpb){
						if(getGameMode() != GameModes.GHOST && getGameMode() != GameModes.LIMBO){	
							float ff = Blocks.getFriction(lbid)*rate;
							if(ff != 0){
								ff = 1.0f - ff;
								if(ff < 0)ff = 0;
								motionx *= ff;
								motionz *= ff;
								motiony *= ff;
							}

						}
					}
					if(tmps){
						if(getGameMode() != GameModes.GHOST && getGameMode() != GameModes.LIMBO){	
							float ff = Blocks.getFriction(sbid)*rate;
							if(ff != 0){
								ff = 1.0f - ff;
								if(ff < 0)ff = 0;
								motionx *= ff;
								motionz *= ff;
								motiony *= ff;
							}
						}
					}

					doInLiquid(0.20f);
				}

			}else{
				rider = getRiderEntity();
				if(rider != null && rider.entityID == DangerZone.player.entityID){
					//do just the basic collision! We are riding this thing...
					//System.out.printf("got here for %d\n", entityID);
					//x and z direction block collisions
					startheight = getHeight()/4;//if it is less then 1/4 our height, we can cross automatically
					while(startheight < getHeight()){
						doSolidsPushback(startheight, deltaT);
						startheight += 1.0f;
						if(startheight > getHeight()){
							doSolidsPushback(getHeight(), deltaT);
						}
					}

					float df = motiony*deltaT*rate;
					int idf = (int)df;
					df -= idf;

					if(motiony < 0){
						for(i=0;i>=idf;i--){
							if(isSolidAtLevel(dimension, posx, posy+df+i, posz)){ 
								imax = (int)(posy+df+i) + 1;
								motiony = 0;
								posy = imax+0.001f;
								break;
							}
						}
					}

					//y direction (up)	
					if(motiony >= 0){
						if(!isSolidAtLevel(dimension, posx, posy+getHeight(), posz)){
							df = motiony*deltaT*rate;
							idf = (int)df;
							df -= idf;
							for(i=0;i<=idf;i++){
								if(isSolidAtLevel(dimension, posx, posy+df+getHeight()+i, posz)){ 
									imax = (int)(posy+df+getHeight()+i);
									motiony = 0;
									posy = (double)imax-getHeight()-0.01f;									
									break;
								}
							}
						}
					}				
				}
			}
		}

		super.update(deltaT);

	}
	
	public int getHelmetID(){
		if(getArmor(0) != null){
			return getArmor(0).iid;
		}
		return 0;
	}
	
	public int getChestplateID(){
		if(getArmor(1) != null){
			return getArmor(1).iid;
		}
		return 0;
	}
	
	public int getLeggingsID(){
		if(getArmor(2) != null){
			return getArmor(2).iid;
		}
		return 0;
	}
	
	public int getBootsID(){
		if(getArmor(3) != null){
			return getArmor(3).iid;
		}
		return 0;
	}

	/*
	 * return total defense of player. Add it all up!
	 */
	public float getDefense(){
		float df = super.getDefense();
		if(has_inventory){
			Item it = Items.getItem(getHelmetID());
			ItemArmor ia = null;
			if(it instanceof ItemArmor){
				ia = (ItemArmor)it;
				df += ia.protection;
			}
			it = Items.getItem(getChestplateID());
			if(it instanceof ItemArmor){
				ia = (ItemArmor)it;
				df += ia.protection;
			}
			it = Items.getItem(getLeggingsID());
			if(it instanceof ItemArmor){
				ia = (ItemArmor)it;
				df += ia.protection;
			}
			it = Items.getItem(getBootsID());
			if(it instanceof ItemArmor){
				ia = (ItemArmor)it;
				df += ia.protection;
			}
		}

		if(enable_taming && getOwnerName() != null && df < 3)df = 3; //a little tougher when tamed
		return df;
	}
	
	public float getAttackDamage(){ 
		if(isBaby())return super.getAttackDamage()/16f;
		return super.getAttackDamage();
	}
	
	//only called from server side.
	//damage the armor and update inventory in client
	public void doArmorDamage(Entity e, /*DamageTypes*/int dt, float pain){
		super.doArmorDamage(e, dt, pain);
		if(pain <= 0)return;
		int which = world.rand.nextInt(4); //which piece takes the hit?
		if(getArmor(which) != null){
			int ad = (int)(pain+1);
			getArmor(which).currentuses += ad;			
			if(getArmor(which).currentuses >= Items.getMaxUses(getArmor(which).iid)){
				//TODO play sound!
				setArmor(which, null);
			}
			setArmorChanged(which); //force update
		}		
	}
	
	public void doHurtAnimation(){
		hurtanimationtimer = 35;
		madtimer = world.rand.nextInt(100)+100; //client side
	}
	
	public boolean isHurt(){
		if(hurtanimationtimer > 0)return true; //client
		return false;
	}
	
	public boolean isMad(){
		if(madtimer > 0)return true; //server only!
		return false;
	}
	
	public void jump(){
		Entity e = getRiddenEntity();
		if(e != null){
			e.jump();
			return;
		}
		if(getInLiquid() && canSwim){
			if(Blocks.isLiquid(world.getblock(dimension, (int)posx, (int)(posy+(getHeight()*3/4) + swimoffset),(int)posz))){				
				if(world.rand.nextInt(2) == 0){
					motiony += 0.155f;
					if(isSolidAtLevel(dimension, posx, posy-0.35f, posz))motiony += 0.55f;
				}				
			}
		}
		//Gotta be on solid ground to jump!
		if(!isSolidAtLevel(dimension, posx, posy-0.10f, posz))return;
		if(Math.abs(motiony)>0.15f)return;
		
		float jumpfactor = 1 + (getHeight())/30.0f;
		float jumpadjust = getTotalEffect(Effects.STRENGTH);
		if(jumpadjust != 0)jumpfactor += jumpfactor*jumpadjust/4;
		jumpadjust = getTotalEffect(Effects.WEAKNESS);
		if(jumpadjust != 0)jumpfactor /= jumpadjust;
		
		motiony += (0.75f*jumpfactor + jumpstrength*jumpfactor);
	}
	
	
	/*
	 * Entity is BEING attacked by 
	 */
	public void doAttackFromNoKnock(/*entity that hit me*/Entity e, /*DamageTypes*/int dt, float pain){	
		doAttackFromCustom(e, dt, pain, false);
	}
	public void doAttackFrom(/*entity that hit me*/Entity e, /*DamageTypes*/int dt, float pain){	
		doAttackFromCustom(e, dt, pain, true);
	}
	public void doAttackFromCustom(/*entity that hit me*/Entity e, /*DamageTypes*/int dt, float pain, boolean addknockback){	

		if(!world.isServer)return;
		if(!ServerHooker.doAttackFrom(this, e, dt, pain))return;
			
		if(e != null){
			int maybenot = (int)e.getTotalEffect(Effects.CONFUSION);
			if(maybenot >= 2){
				if(world.rand.nextInt(maybenot) != 1)return;
			}
		}
		
		if(DangerZone.petprotection){
			if(getOwnerName() != null){
				return;
			}
		}
			
		if(pain > 0){
			if(e != null && e != this && !DangerZone.playnicely){
				if(!enable_taming){
					hurtMe = e; //revenge target!
				}else{
					Entity eo = DangerZone.server.entityManager.findPlayerByName(getOwnerName());
					if(e != eo){
						hurtMe = e;
					}	
				}
			}
		}
		
		
		if(    dt != DamageTypes.FALL 
			&& dt != DamageTypes.HUNGER
			&& dt != DamageTypes.WATER
			&& dt != DamageTypes.DAYTIME
			&& dt != DamageTypes.EXPLOSIVE
			&& dt != DamageTypes.FIRE
			&& dt != DamageTypes.POISON
			&& dt != DamageTypes.LIGHTNING
			&& dt != DamageTypes.MAGIC
				){		
			if(damage_backoff > 0)return; //not yet!!!
			damage_backoff = 6; //6 ticks
		}
				
		if(getOwnerName() != null){
			setStaying(false);
			setSitting(false);
			if(pain > 1 && (e instanceof Player)){
				pain = 1;
				//System.out.printf("player pet pain = 1\n");
			}			
		}
		
		float dfn = getDefense();
		if(dfn <= 0.1f)dfn = 0.1f; //set a minimum defense
		if(dfn > 1000.0f)dfn = 1000.0f; //Set a maximum defense
		if(dt == DamageTypes.POISON)dfn = 1;
		if(dt == DamageTypes.MAGIC)dfn = 1;
		float totalpain = pain / dfn; //Divide intended damage by total defense
		
		if(this instanceof Player){
			int diffi = getGameDifficulty();
			if(diffi == 1)totalpain *= 1.5f;
			if(diffi == 2)totalpain *= 2.5f;
			if(diffi == -1)totalpain /= 2;
			if(diffi == -2)totalpain /= 4;
		}
		
		if(e != null){
			float painadjust = e.getTotalEffect(Effects.STRENGTH);
			if(painadjust > 0){	
				totalpain *= (painadjust+1);
			}
			painadjust = e.getTotalEffect(Effects.WEAKNESS);
			if(painadjust > 0){	
				totalpain /= (painadjust+1);
			}
			
		}
		//is it immune to this type of damage?
		if(!takesDamageFrom(dt))return;
		if(totalpain == 0)return;
		
		madtimer = world.rand.nextInt(25)+25;
		if(dt != DamageTypes.POISON)newtargetnow = true; //yikes! Run!
		
		//And now for a little default knockback
		//System.out.printf("dt = %d\n", dt);
		if(e != null && addknockback){
			if(		   dt != DamageTypes.FALL
					&& dt != DamageTypes.DAYTIME
					&& dt != DamageTypes.FIRE
					&& dt != DamageTypes.HUNGER
					&& dt != DamageTypes.WATER 
					&& dt != DamageTypes.POISON ){

				float dir = (float) Math.atan2(posz - e.posz, posx - e.posx);
				float myf = getHeight()*getWidth();
				if(myf < 3)myf = 3;
				float sf = e.getHeight()*e.getWidth()/myf;
				if(sf < 0.15f)sf = 0.15f;
				if(sf > 4.25f)sf = 4.25f;
				motionx += Math.cos(dir)*sf*0.85f;
				motionz += Math.sin(dir)*sf*0.85f;
				motiony += 0.25f*sf;
				if(this instanceof Player){
					//System.out.printf("player knockback!\n");
					Player pl = (Player)this;
					pl.server_thread.sendVelocityUpdateToPlayer(motionx, motiony, motionz);
				}
			}
		}
		
		if(this instanceof Player && totalpain > 0){
			if(getGameMode() != GameModes.SURVIVAL)return;
		}
		
		if(has_inventory){
			if(dt != DamageTypes.DAYTIME
					&& dt != DamageTypes.HUNGER
					&& dt != DamageTypes.MAGIC
					&& dt != DamageTypes.POISON){
				if(pain > 0)doArmorDamage(e, dt, pain);
			}
		}
		
		if(this instanceof Player){
			Player pl = (Player)this;
			pl.damage_taken += totalpain;
			pl.server_thread.sendStatsToPlayer();
		}
		if(e != null && e instanceof Player){
			Player pl = (Player)e;
			pl.damage_dealt += totalpain;
			pl.server_thread.sendStatsToPlayer();
		}
		
		totalpain = getHealth() - totalpain;
		if(totalpain > getMaxHealth())totalpain = getMaxHealth(); //in case its actually healing...
		if(totalpain < 0)totalpain -= 10; //make sure we both know it's dead and stays dead!
		
		if(totalpain > 0){	
			setHealth(totalpain);	
			if(getHurtSound() != null && dt != DamageTypes.HUNGER && dt != DamageTypes.POISON){
				world.playSound(getHurtSound(), dimension, posx, posy+getHeight()/2, posz, getLivingSoundVolume(), 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.2f);
			}
			DangerZone.server.sendEntityHitToAll(this);
		}else{
			//System.out.printf("Dead: %s\n", uniquename);
			if(getDeathSound() != null){
				world.playSound(getDeathSound(), dimension, posx, posy+getHeight()/2, posz, getLivingSoundVolume(), 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.1f);
			}
			if(e != null && e != this){
				e.onKill(this);
			}
			
			if(this instanceof Player){
				if(ServerHooker.player_died((Player)this)){
					if(do_death_drops)doDeathDrops();
					onDeath();
					removeAllEffects();
					deadflag = false;
					setHealth(0.01f);
					Player pl = (Player)this;
					pl.deaths++;
					pl.server_thread.sendStatsToPlayer();
					pl.setGameMode(GameModes.LIMBO);
					pl.server_thread.sendStartDeathGUI();
				}
				
			}else{	
				if(ServerHooker.critter_died(this)){
					if(!isBaby()){ //no reward for killing babies!
						if(do_death_drops)doDeathDrops();
					}else{
						if(do_death_drops)doBabyDeathDrops();
					}
					onDeath();
					removeAllEffects();
					deadflag = true;
					setHealth(totalpain);	
					DangerZone.server.sendEntityDeathToAll(this);
					if(e != null && e instanceof Player){
						Player pl = (Player)e;
						pl.kills++;
						pl.server_thread.sendStatsToPlayer();
						ToDoList.onKilled(pl, this);
					}
				}
			}
		}
	}
	
	public void doBabyDeathDrops(){
		//you get nothing here! (from critters. players still drop their stuff!)
		if(this instanceof Player){
			doDeathDrops();
		}
	}
	
	public void addKnockback(Entity hitter, float xz, float y){
		if(!world.isServer)return;
		if(hitter == null)return;
		float dir = (float) Math.atan2(posz - hitter.posz, posx - hitter.posx);
		motionx += Math.cos(dir)*xz;
		motionz += Math.sin(dir)*xz;
		motiony += y;
		if(this instanceof Player){
			Player pl = (Player)this;
			pl.server_thread.sendVelocityUpdateToPlayer(motionx, motiony, motionz);
		}
	}
	
	public void addKnockback(float dir, float xz, float y){
		if(!world.isServer)return;
		motionx += Math.cos(dir)*xz;
		motionz += Math.sin(dir)*xz;
		motiony += y;
		if(this instanceof Player){
			Player pl = (Player)this;
			pl.server_thread.sendVelocityUpdateToPlayer(motionx, motiony, motionz);
		}
	}
	
	
	public int getExperience(){
		if(isBaby()&&!(this instanceof Player))return 1;
		return super.getExperience();
	}
	
	public void onDeath(){	
		if(world.isServer){
			//make some Experience particles!
			int iexp = getExperience();
			float ht = getHeight()/4;
			float wd = getWidth()/2;
			
			setExperience(0);
			
			if(ht > 2)ht = 2;
			if(wd > 1.75f)wd = 1.75f;
			
			while(iexp >= 1000){
				EntityExp e = (EntityExp)world.createEntityByName("DangerZone:Experience", dimension, posx,posy,posz);
				if(e != null){
					e.setBID(0);
					e.setIID(0);
					e.setExperience(1000);
					e.rotation_pitch = 0;
					e.rotation_yaw = world.rand.nextInt(360);
					e.rotation_roll = 0;
					e.motionx = (world.rand.nextFloat()-world.rand.nextFloat())*wd; 
					e.motiony = world.rand.nextFloat()*ht;
					e.motionz = (world.rand.nextFloat()-world.rand.nextFloat())*wd;
					world.spawnEntityInWorld(e);
				}				
				iexp -= 1000;
			}
			while(iexp >= 100){
				EntityExp e = (EntityExp)world.createEntityByName("DangerZone:Experience", dimension, posx,posy,posz);
				if(e != null){
					e.setBID(0);
					e.setIID(0);
					e.setExperience(100);
					e.rotation_pitch = 0;
					e.rotation_yaw = world.rand.nextInt(360);
					e.rotation_roll = 0;
					e.motionx = (world.rand.nextFloat()-world.rand.nextFloat())*wd; 
					e.motiony = world.rand.nextFloat()*ht;
					e.motionz = (world.rand.nextFloat()-world.rand.nextFloat())*wd;
					world.spawnEntityInWorld(e);
				}				
				iexp -= 100;
			}
			while(iexp >= 10){
				EntityExp e = (EntityExp)world.createEntityByName("DangerZone:Experience", dimension, posx,posy,posz);
				if(e != null){
					e.setBID(0);
					e.setIID(0);
					e.setExperience(10);
					e.rotation_pitch = 0;
					e.rotation_yaw = world.rand.nextInt(360);
					e.rotation_roll = 0;
					e.motionx = (world.rand.nextFloat()-world.rand.nextFloat())*wd; 
					e.motiony = world.rand.nextFloat()*ht;
					e.motionz = (world.rand.nextFloat()-world.rand.nextFloat())*wd;
					world.spawnEntityInWorld(e);
				}				
				iexp -= 10;
			}
			while(iexp >= 1){
				EntityExp e = (EntityExp)world.createEntityByName("DangerZone:Experience", dimension, posx,posy,posz);
				if(e != null){
					e.setBID(0);
					e.setIID(0);
					e.setExperience(1);
					e.rotation_pitch = 0;
					e.rotation_yaw = world.rand.nextInt(360);
					e.rotation_roll = 0;
					e.motionx = (world.rand.nextFloat()-world.rand.nextFloat())*wd; 
					e.motiony = world.rand.nextFloat()*ht;
					e.motionz = (world.rand.nextFloat()-world.rand.nextFloat())*wd;
					world.spawnEntityInWorld(e);
				}				
				iexp -= 1;
			}
		}
		super.onDeath();
	}
	
	public void do_hostility(){
		/*
		 * See if there is something around to chase...
		 */
		searchcounter++;
		if(searchcounter >= 10){ //re-target about once a second
			searchcounter = world.rand.nextInt(4);
			if(hurtMe != null && hurtMe.deadflag)hurtMe = null;
			if(world.rand.nextInt(25) == 1)hurtMe = null;
			if(!isSuitableTarget(hurtMe))hurtMe = null;
			targetentity = hurtMe;					
			if(targetentity == null)targetentity = findSomethingToAttack();
			if(targetentity != null){
				//System.out.printf("attackable entity found!\n");
				target = new TargetHelper(targetentity, targetentity.posx, targetentity.posy, targetentity.posz);
				setAttacking(true);
			}else{
				setAttacking(false);
			}
		}
		
		/*
		 * See if we should attack!
		 */

		doTargetPrep(targetentity);
		
		if(targetentity != null && world.rand.nextInt(8) == 1 && amFacingTarget(targetentity)){ //about once a second
			float ar = attackRange;
			if(isBaby())ar /= 4;
			if(getDistanceFromEntity(targetentity) < ar+targetentity.getWidth()/2+getWidth()/2){
				doAttack(targetentity);
			}else{
				ar = searchDistance*1.5f;
				if(isBaby())ar /= 4;
				if(getDistanceFromEntity(targetentity) < ar)doDistanceAttack(targetentity);
			}
		}
	}
	
	public void do_hungry(){
		if(world.rand.nextInt(5) == 1){
			//search for something to eat
			if(targetentity == null){
				EntityBlockItem e = findSomethingToEat();
				setAttacking(false);
				if(e != null){				
					if(e.getDistanceFromEntity(this) < getWidth()/2+2){
						e.deadflag = true;
						doEatDroppedFood(e);
						target = null;						
					}else{
						target = new TargetHelper(e.posx, e.posy, e.posz);
						setAttacking(true);
					}
				}
			}else{
				if(target != null && target.getDistanceToTarget(posx, posy, posz) < getWidth()/2+2){
					EntityBlockItem e = findSomethingToEat();
					if(e != null && e.getDistanceFromEntity(this) < getWidth()/2+2){
						e.deadflag = true;
						doEatDroppedFood(e);
						target = null;
						targetentity = null;
						setAttacking(false);
					}
				}
			}
		}
	}
	
	public void stay_near_owner(){
		ownercheckcounter++;
		if(ownercheckcounter > 10){
			ownercheckcounter = world.rand.nextInt(5); // just to slow things down a little. no sense scanning constantly.
			petaccellerator = 1.0f;
			//if we are tamed, stay within range of owner
			if(getOwnerName() != null){
				Entity e = DangerZone.server.entityManager.findPlayerByName(getOwnerName());
				if(e != null){
					double dist = getDistanceFromEntity(e);
					if(dist > maxdisttoowner){
						
						findNewTargetNearOwner(e);
						
						if(dist > maxdisttoowner*1.25f){
							petaccellerator = 1.5f; //move faster when out of range!
						}
						if(dist > maxdisttoowner*1.5f){
							petaccellerator = 2.25f; //move faster when out of range!
						}
						if(dist > maxdisttoowner*2){
							//teleport to owner
							if(e.getOnGround()){
								Dimensions.DimensionArray[e.dimension].teleportToDimension(this, world, e.dimension, (int)target.targetx, (int)target.targety, (int)target.targetz);
								target = null;
							}else{
								if(canFly)setFlying(true);
								Dimensions.DimensionArray[e.dimension].teleportToDimension(this, world, e.dimension, (int)target.targetx, (int)target.targety, (int)target.targetz);
								target = null;
							}
						}
					}
				}
			}
		}
	}
	
	public void do_follow_food(){
		if(target == null && world.rand.nextInt(5) == 1){
			Entity e = followFood();
			if(e != null){ 
				target = new TargetHelper(e, e.posx, e.posy, e.posz);
			}			
		}	
	}
	
	public void do_find_foodblock(){
		if(target == null && foodblockdistxz > 0 && foodblockdisty > 0){
			if((!fooddaytimeonly || (fooddaytimeonly && world.isDaytime())) && world.rand.nextInt(foodblockfreq) == 1)
			{
				findBlockFood(foodblockdistxz, foodblockdisty, foodblockheal, foodblockdisteat); //max dist = 13, heal amount = 5, 4+4+4
			}
		}
	}
	
	public void do_breeding(){
		//CLOSE YOUR EYES PLEASE!
		if(breeding > 0)breeding--;
		//find a mate!
		if(breeding != 0 && target == null && world.rand.nextInt(5) == 1){
			EntityLiving e = findMate();
			if(e != null){ 					
				target = new TargetHelper(e, e.posx, e.posy, e.posz);
				//see if we are already close enough
				if(e.breeding != 0 && getDistanceFromEntity(e) < getWidth()*2 && world.rand.nextInt(2) == 1){
					breeding = 0;
					e.breeding = 0;
					int howmany = 1 + world.rand.nextInt(2);
					for(int j=0;j<howmany;j++){
						makeBaby(this, e);
					}
				}
			}
		}
		if(breeding != 0){
			if(world.rand.nextInt(5) == 0)Utils.spawnParticlesFromServer(world, "DangerZone:ParticleHappy", 2, dimension, posx, posy+getHeight(), posz);
		}
	}
	
	public void do_something_else(){
		//additional behavior goes in this override
	}
	
	public void do_rider_support(){
		//non-player rider support...
		Entity rider = getRiderEntity();
		if(rider != null && rider instanceof EntityLiving && !(rider instanceof Player)){
			//fetch the rider's target.
			EntityLiving erider = (EntityLiving)rider;
			target = erider.target;
			if(target != null){
				if(target.getDistanceToTarget(posx, posy, posz) < getWidth()/2){ //got there!
					erider.target = null;
					target = null;
				}
			}else{
				float cdir = (float) Math.toRadians(rotation_yaw);
				float tdir = (float) Math.toRadians(erider.rotation_yaw);
				float ddiff = tdir - cdir;
				while(ddiff>Math.PI)ddiff -= Math.PI*2;
				while(ddiff<-Math.PI)ddiff += Math.PI*2;
				rotation_yaw_motion += (ddiff*180f/Math.PI)/10;
			}
		}
	}
	
	public void do_normal_movement(float deltaT){
		int mf = movefrequency;
		if(isBaby() && mf > 10)mf /= 2;
		if(madtimer > 0)mf = mf/4;
		if(isSwarming())mf = 10;
		if(mf < 2)mf = 2;
		
		if(world.rand.nextInt(mf) == 0 || newtargetnow){		
			findNewTarget(rotation_yaw, 70f);
		}		
		if(target != null && target.getDistanceToTarget(posx, posy, posz) > getWidth()/2){
			moveTowardsTarget(deltaT);				
		}else{
			target = null;
		}
	}
	
	//This routine has been broken up into many little pieces to make it easy to override.
	//This and all its little functions are server-side only, which is more than likely
	//exactly what you want anyway.
	public void doEntityAction(float deltaT){	
				
		if(!getStaying()){
			
			do_rider_support();
			
			if(enable_hostile){				
				do_hostility();
			}
			
			if(enable_droppedfood){
				do_hungry();
			}

			if(enable_avoid){
				if(avoiddistance > 0 && !DangerZone.playnicely){
					doAvoidEntity();
				}
			}

			if(enable_buddy){
				if(findbuddydistance > 0 && findbuddyfrequency > 1 && getOwnerName() == null){
					doFindBuddy();
				}
			}
			
			if(enable_followfood){
				do_follow_food();
			}

			do_normal_movement(deltaT);
			
			if(enable_taming){
				stay_near_owner();
			}
			
			if(enable_findfoodblock){
				do_find_foodblock();
			}
			
			if(enable_breeding && !isBaby()){
				do_breeding();
			}
			
			if(canridemaglevcart){
				hopOnOffCart();
			}
			
			//make some noise!
			if(!isHurt() && !deadflag){
				if(world.rand.nextInt(100) == 1){
					world.playSound(getLivingSound(), dimension, posx, posy+getHeight()/2, posz, getLivingSoundVolume(), getLivingSoundPitch());					
				}				
			}
			
		}
		
		do_swarm_despawn();
		
		//use this for additional behavior
		do_something_else();

		if(enable_lookaround){
			doLookAction();
		}
		
		super.doEntityAction(deltaT);
	}
	
	public void do_swarm_despawn(){
		if(isSwarming()){
			//our time is limited... maybe...
			if(super.getCanDespawn()){
				setVarInt(13, getVarInt(13)+1);
				if(getVarInt(13) > 900){
					if(world.rand.nextInt(30) == 1)deadflag = true;
					if(getVarInt(13) > 1800)deadflag = true;
				}
			}
		}
	}
	
	public void hopOnOffCart(){
		float wdth = getWidth();
		List<Entity> nearby_list = null;
		ListIterator<Entity> li;
		Entity ridden = getRiddenEntity();
		
		if(ridden != null){
			//I am riding... hop off!
			if(world.rand.nextInt(100) == 0){
				ridden.unMount(this);
				return;
			}
		}
		
		if(world.rand.nextInt(10) != 0){
			return;
		}

		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange((wdth/2) + 2f, dimension, posx, posy, posz);
		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				Entity e = null;
				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (Entity)li.next();
					if((e instanceof EntityMagLev)||(e instanceof EntityRaft)){
						if(e.getInventory(0) == null){
							if(e.getRiderEntity() == null){
								e.Mount(this);
							}
						}						
					}
				}
			}
		}
		return;
	}
	
	public void makeBaby(Entity p1, Entity p2){
		Entity newme = world.createEntityByName(p1.uniquename, p1.dimension, (p1.posx+p2.posx)/2, (p1.posy+p2.posy)/2, (p1.posz+p2.posz)/2);
		if(newme != null){
			newme.init();	
			newme.setBaby(true);
			newme.setCanDespawn(false);
			newme.setOwnerName(p1.getOwnerName());
			world.spawnEntityInWorld(newme);
			world.playSound("DangerZone:pop", dimension, (p1.posx+p2.posx)/2, (p1.posy+p2.posy)/2, (p1.posz+p2.posz)/2, 1.0f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.3f);
		}
		p1.setCanDespawn(false);
		p2.setCanDespawn(false);

	}
	
	public EntityBlockItem findSomethingToEat(){	
		List<Entity> nearby_list = null;
		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange((getWidth()/2) + foodsearchDistance, dimension, posx, posy, posz);
		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				Entity e = null;
				ListIterator<Entity> li;
				//Sort them out based on size and distance
				Collections.sort(nearby_list, FoodSorter);
				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (Entity)li.next();
					if(e instanceof EntityBlockItem && isSuitableFood(e)){ 
						return (EntityBlockItem)e;
					}
				}								
			}			
		}
		return null;
	}

	//Look for food!
	public boolean isSuitableFood(Entity e){
		if(!(e instanceof EntityBlockItem))return false;
		EntityBlockItem eb = (EntityBlockItem)e;
		if(!isFoodForMe(eb.getBlockID(), eb.getItemID()))return false;
		if(enable_taming && getOwnerName() != null){
			Entity p = DangerZone.server.entityManager.findPlayerByName(getOwnerName());
			if(p != null){
				if(p.getDistanceFromEntity(eb) > maxdisttoowner){
					return false;
				}
			}
		}
		if(!CanProbablySeeEntity(eb))return false; //final check, cuz its the slowest
		return true;
	}
	
	public void doEatDroppedFood(EntityBlockItem eb){
		int bid = eb.getBlockID();
		int iid = eb.getItemID();
		if(iid != 0){
			Item it = Items.getItem(iid);
			if(it != null)it.onFoodEaten(this);
		}
		if(bid != 0){
			Block bl = Blocks.getBlock(bid);
			if(bl != null)bl.onFoodEaten(this);
		}
		playburp();
		heal(2);
	}
	
	public void doAvoidEntity(){
		if(world.rand.nextInt(8) == 1){			
			List<Entity> nearby_list = null;
			//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
			nearby_list = DangerZone.server.entityManager.findEntitiesInRange(avoiddistance, dimension, posx, posy, posz);
			if(nearby_list != null){
				if(!nearby_list.isEmpty()){
					Entity e = null;
					ListIterator<Entity> li;
					double dist;
					Collections.sort(nearby_list, LookTargetSorter);
					li = nearby_list.listIterator();
					while(li.hasNext()){
						e = (Entity)li.next();
						dist = getDistanceFromEntityCenter(e);					
						if(dist < avoiddistance){ 							
							if(e != this && shouldAvoidEntity(e)){
								float rundir = (float) Math.atan2(e.posx - posx, e.posz - posz);
								rundir += Math.PI;
								rundir = (float) Math.toDegrees(rundir);
								findNewTarget(rundir, 45f);
								break;
							}		
						}					
					}								
				}			
			}
		}
	}
	
	public boolean shouldAvoidEntity(Entity e){
		return false;
	}
	
	public void doFindBuddy(){
		if(world.rand.nextInt(findbuddyfrequency) == 1){			
			List<Entity> nearby_list = null;
			int nbuddy = 0;
			double totalx, totalz;
			totalx = totalz = 0;
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
							nbuddy++;
						}		
					}
					if(nbuddy > 0){
						//head towards middle of group
						totalx /= nbuddy;
						totalz /= nbuddy;
						float rundir = (float) Math.atan2(totalx - posx, totalz - posz);
						rundir = (float) Math.toDegrees(rundir);
						findNewTarget(rundir, 45f);
					}
				}								
			}			
		}
	}

	
	public boolean isBuddy(Entity e){
		return false;
	}
	
	public EntityLiving findMate(){				
		List<Entity> nearby_list = null;
		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		nearby_list = DangerZone.server.entityManager.findEntitiesInRange(findmatedistance, dimension, posx, posy, posz);
		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				EntityLiving e = null;
				ListIterator<Entity> li;
				Collections.sort(nearby_list, LookTargetSorter);
				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (EntityLiving)li.next();												
					if(e != this && isMate(e)){
						return e;
					}		
				}					
			}								
		}			

		return null;
	}

	
	public boolean isMate(Entity e){
		if(e == this)return false;
		if(e.isBaby())return false;
		if(e.uniquename.equals(uniquename))return true;
		return false;
	}
	
	public void doLookAction(){
		// look around!		
		if(world.rand.nextInt(120) == 1)idletarget = null;
		if(world.rand.nextInt(movefrequency/8 + 3) == 0 && idletarget == null){
			Entity lookentity = findEntityToLookAt();
			if(lookentity != null){
				idletarget = new TargetHelper(lookentity, lookentity.posx, lookentity.posy, lookentity.posz);
			}else{
				idletarget = null;
			}
		}
		
		TargetHelper look = target;
		if(look == null){
			look = idletarget;
			if(look != null && look.te != null){
				look.setTarget(look.te.posx, look.te.posy, look.te.posz);
			}
		}
		if(look != null){
			//look where we are going! (or at what we are fighting!!!)
			float cdir = (float) Math.toRadians(rotation_yaw_head);
			float tdir = (float) Math.atan2(look.targetx - posx, look.targetz - posz);
			float ddiff = tdir - cdir;
			float ht = 0, mht = eyeheight;;
			while(ddiff>Math.PI)ddiff -= Math.PI*2;
			while(ddiff<-Math.PI)ddiff += Math.PI*2;
			rotation_yaw_head += (ddiff*180f/Math.PI)/5f;
			
			if(look.te != null){
				ht = look.te.getHeight()*7/8;
			}else{
				ht = eyeheight;
				if(world.rand.nextBoolean()){
					ht += eyeheight/8f;
				}else{
					ht -= eyeheight/8f;
				}
			}
			cdir = (float) Math.toRadians(rotation_pitch_head);
			tdir = (float) Math.atan2((posy+mht) - (look.targety+ht), look.getDistanceToTarget(posx, look.targety, posz));
			ddiff = tdir - cdir;
			while(ddiff>Math.PI)ddiff -= Math.PI*2;
			while(ddiff<-Math.PI)ddiff += Math.PI*2;
			rotation_pitch_head += (ddiff*180f/Math.PI)/5f;

			//turn body towards look target...
			cdir = (float) Math.toRadians(rotation_yaw);
			tdir = (float) Math.atan2(look.targetx - posx, look.targetz - posz);
			ddiff = tdir - cdir;
			while(ddiff>Math.PI)ddiff -= Math.PI*2;
			while(ddiff<-Math.PI)ddiff += Math.PI*2;
			rotation_yaw_motion += (ddiff*180f/Math.PI)/50f; //slowly!
		}else{
			//look straight ahead
			float cdir = (float) Math.toRadians(rotation_yaw_head);
			float tdir = (float) Math.toRadians(rotation_yaw);
			float ddiff = tdir - cdir;
			while(ddiff>Math.PI)ddiff -= Math.PI*2;
			while(ddiff<-Math.PI)ddiff += Math.PI*2;
			rotation_yaw_head += (ddiff*180f/Math.PI)/5f;
			
			cdir = (float) Math.toRadians(rotation_pitch_head);
			tdir = (float) 0;
			ddiff = tdir - cdir;
			while(ddiff>Math.PI)ddiff -= Math.PI*2;
			while(ddiff<-Math.PI)ddiff += Math.PI*2;
			rotation_pitch_head += (ddiff*180f/Math.PI)/5f;
		}
	}
	
	public void lookAtEntity(Entity ent){
		float cdir = (float) Math.toRadians(rotation_yaw_head);
		float tdir = (float) Math.atan2(ent.posx - posx, ent.posz - posz);
		float ddiff = tdir - cdir;
		float ht = 0, mht = 0;;
		while(ddiff>Math.PI)ddiff -= Math.PI*2;
		while(ddiff<-Math.PI)ddiff += Math.PI*2;
		rotation_yaw_head += (ddiff*180f/Math.PI)/5f;
				
		ht = ent.getHeight()*7/8;
		mht = eyeheight;
		
		cdir = (float) Math.toRadians(rotation_pitch_head);
		tdir = (float) Math.atan2((posy+mht) - (ent.posy+ht), getHorizontalDistanceFromEntity(ent));
		ddiff = tdir - cdir;
		while(ddiff>Math.PI)ddiff -= Math.PI*2;
		while(ddiff<-Math.PI)ddiff += Math.PI*2;
		rotation_pitch_head += (ddiff*180f/Math.PI)/5f;

		//turn body towards look target...
		cdir = (float) Math.toRadians(rotation_yaw);
		tdir = (float) Math.atan2(ent.posx - posx, ent.posz - posz);
		ddiff = tdir - cdir;
		while(ddiff>Math.PI)ddiff -= Math.PI*2;
		while(ddiff<-Math.PI)ddiff += Math.PI*2;
		rotation_yaw_motion += (ddiff*180f/Math.PI)/50f; //slowly!
	}
	
	public Entity findEntityToLookAt(){	
		List<Entity> nearby_list = null;
		//Get a list of entities within reach
		nearby_list = DangerZone.server.entityManager.findEntitiesInRange((getWidth()/2) + lookDistance, dimension, posx, posy, posz);
		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				Entity e = null;
				ListIterator<Entity> li;
				//Sort them out based on size and distance
				Collections.sort(nearby_list, LookTargetSorter);
				li = nearby_list.listIterator();
				//preference to players!
				while(li.hasNext()){
					e = (Entity)li.next();
					if(e instanceof Player){
						if(isSuitableLookTarget(e)){ 
							//System.out.printf("look at player\n");
							return e;
						}
					}
				}
				while(li.hasNext()){
					e = (Entity)li.next();
					if(isSuitableLookTarget(e)){ 
						return e;
					}
				}	
			}			
		}
		return null;
	}

	public boolean isSuitableLookTarget(Entity e){
		if(e == this)return false;
		if(!CanProbablySeeEntity(e))return false; //final check, cuz its the slowest
		if(e.isInvisible())return false;
		return true;
	}
	
	public void moveTowardsTarget(float deltaT){
		int intheight, startheight, k;
    	float cdir = (float) Math.toRadians(rotation_yaw);
    	float tdir = (float) Math.atan2(target.targetx - posx, target.targetz - posz);
    	float ddiff = tdir - cdir;
    	double dist;
    	double speed;
    	while(ddiff>Math.PI)ddiff -= Math.PI*2;
    	while(ddiff<-Math.PI)ddiff += Math.PI*2;
    	rotation_yaw_motion += (ddiff*180f/Math.PI)/10;
    	dist = target.getDistanceToTarget(posx,  posy,  posz);
    	speed = Math.sqrt(motionx*motionx + motionz*motionz);
    	speed = dist/(speed*10);
    	if(speed > 1)speed = 1; 
    	
    	if(!getOnGround() && !getInLiquid())return;
    	

    	if(isBaby() || madtimer > 0){
    		accellerator = 1.5f;
    	}else{
    		accellerator = 1;
    	}
    	dist = getTotalEffect(Effects.SPEED);
    	if(dist > 1){
    		speed *= dist;
    	}
    	dist = getTotalEffect(Effects.SLOWNESS);
    	if(dist > 1){
    		speed /= dist;
    	}
    	motionx += moveSpeed*accellerator*petaccellerator*speed*Math.sin(tdir);
    	motionz += moveSpeed*accellerator*petaccellerator*speed*Math.cos(tdir);

    	
    	
    	//can we even go this way?
    	if(!isSolidAtLevel(dimension, posx+motionx*deltaT, posy-1, posz+motionz*deltaT)){
    		if(!isSolidAtLevel(dimension, posx+motionx*deltaT, (posy-getHeight()/4f)-2, posz+motionz*deltaT)){
    			//would probably fall!
    			//but is it a liquid and we can swim?
    			if(canSwim && isLiquidAtLevel(dimension, posx+motionx*deltaT, (posy-1), posz+motionz*deltaT)){
    				//it's ok, we can swim...
    			}else{
    				motionx = motionz = 0;
    				//System.out.printf("Newtarget from fall\n");
    				newtargetnow = true;
    				return;
    			}
    		}
    	}
    	
    	
    	
    	Entity e = getRiddenEntity();
    	float useheight = getHeight();
    	float usewidth = getWidth();
    	double usex = posx+motionx*deltaT;
    	double usey = posy;
    	double usez = posz+motionz*deltaT;
    	if(e != null){
    		useheight = e.getHeight();
    		usewidth = e.getWidth();
    		usex = e.posx+motionx*deltaT;
        	usey = e.posy;
        	usez = e.posz+motionz*deltaT;
    	}
    	
		//x and z direction block collisions
		intheight = (int)(useheight+0.995f);
		startheight = (int)useheight/4; //if it is less then 1/4 our height, we can cross automatically
		for(k=startheight;k<intheight;k++){		
			if(wouldBump(usex, usey+k, usez, usewidth)){
				if(k <= startheight+1){
					if(world.rand.nextInt(3) == 0){
						jump();
						break;
					}
				}else{
	    			//motionx = motionz = 0;
					motionx /= 2*deltaT;
					motionz /= 2*deltaT;
					newtargetnow = true;
					//System.out.printf("Newtarget from bump\n");
					break;
				}				
			}
		}   	
	}
	
	public void findNewTarget(float dir, float delta){
		double newdir, newdist, newy, newx, newz;
		int intdist;
		
		//forward
		int tries = 4; //max tries per cycle. better luck next time!
		while(tries > 0){
			tries--;
			newdir = world.rand.nextFloat()-world.rand.nextFloat();
			newdir *= delta;
			newdir += dir; //grab a new general direction towards where we are already pointing
			newdist = 2.0f + world.rand.nextFloat()*getWidth()*getHeight()*2; //get a new general distance
			intdist = (int) (newdist+0.995f);
			if(isSwarming()){ //forget newdir, go +x and +z
				newx =  (posx+newdist);
				newz =  (posz+(2.0f + world.rand.nextFloat()*getWidth()*getHeight()*2));
			}else{
				newx =  (posx+Math.sin(Math.toRadians(newdir))*newdist);
				newz =  (posz+Math.cos(Math.toRadians(newdir))*newdist);
			}
			newy = posy;
			for(int i=intdist;i>-intdist;i--){
				newy = posy+i;
				if(!isSolidAtLevel(dimension, newx, newy+1, newz)){
					if(!targetLiquidOnly && isSolidAtLevel(dimension, newx, newy, newz) && CanProbablySee(dimension, newx, newy+1, newz, (int)newdist)){
						if(!canSwim && Blocks.isLiquid(world.getblock(dimension, (int)newx, (int)newy+1, (int)newz))){
							break;
						}
						newy += 1;
						newy = (int)newy;
						target = new TargetHelper(null, newx, newy, newz);
						newtargetnow = false;
						//System.out.printf("New Fwd Land Target set %f,  %f, %f\n", newx, newy, newz);
						return;
					}
					if(canSwim && isLiquidAtLevel(dimension, newx, newy, newz) && CanProbablySee(dimension, newx, newy+1, newz, (int)newdist)){
						newy += 1;
						newy = (int)newy;
						target = new TargetHelper(null, newx, newy, newz);
						newtargetnow = false;
						//System.out.printf("New Fwd Water Target set %f,  %f, %f\n", newx, newy, newz);
						return;
					}
				}
			}
		}
		
		//can't go that way?
		//try backward
		tries = 1; //max tries per cycle. better luck next time!
		while(tries > 0){
			tries--;
			newdir = world.rand.nextFloat()-world.rand.nextFloat();
			newdir *= delta;
			newdir += dir+180f; //try backwards
			newdist = 2.0f + world.rand.nextFloat()*getWidth()*getHeight()*4; //get a new general distance
			intdist = (int) (newdist+0.995f);
			newx =  (posx+Math.sin(Math.toRadians(newdir))*newdist);
			newz =  (posz+Math.cos(Math.toRadians(newdir))*newdist);
			newy = posy;
			for(int i=intdist;i>-intdist;i--){
				newy = posy+i;
				if(!isSolidAtLevel(dimension, newx, newy+1, newz)){
					if(!targetLiquidOnly && isSolidAtLevel(dimension, newx, newy, newz) && CanProbablySee(dimension, newx, newy+1, newz, (int)newdist)){
						if(!canSwim && Blocks.isLiquid(world.getblock(dimension, (int)newx, (int)newy+1, (int)newz))){
							break;
						}
						newy += 1;
						newy = (int)newy;
						target = new TargetHelper(null, newx, newy, newz);
						newtargetnow = false;
						//System.out.printf("New back land Target set %f,  %f, %f\n", newx, newy, newz);
						return;
					}
				}
				if(canSwim && isLiquidAtLevel(dimension, newx, newy, newz) && CanProbablySee(dimension, newx, newy+1, newz, (int)newdist)){
					newy += 1;
					newy = (int)newy;
					target = new TargetHelper(null, newx, newy, newz);
					newtargetnow = false;
					//System.out.printf("New back water Target set %f,  %f, %f\n", newx, newy, newz);
					return;
				}
			}
		}
		//if we are here because we can't swim...
		//try again and ignore the water...
		if(!canSwim){
			tries = 4; //max tries per cycle. better luck next time!
			while(tries > 0){
				tries--;
				newdir = world.rand.nextFloat()-world.rand.nextFloat();
				newdir *= delta;
				newdir += dir; //grab a new general direction towards where we are already pointing
				newdist = 2.0f + world.rand.nextFloat()*getWidth()*getHeight()*2; //get a new general distance
				intdist = (int) (newdist+0.995f);
				newx =  (posx+Math.sin(Math.toRadians(newdir))*newdist);
				newz =  (posz+Math.cos(Math.toRadians(newdir))*newdist);
				newy = posy;
				for(int i=intdist;i>-intdist;i--){
					newy = posy+i;
					if(!isSolidAtLevel(dimension, newx, newy+1, newz)){
						if(isSolidAtLevel(dimension, newx, newy, newz) && CanProbablySee(dimension, newx, newy+1, newz, (int)newdist)){
							newy += 1;
							newy = (int)newy;
							target = new TargetHelper(null, newx, newy, newz);
							newtargetnow = false;
							//System.out.printf("New fwd can't swim Target set %f,  %f, %f\n", newx, newy, newz);
							return;
						}
					}
				}
			}
			
			//can't go that way?
			//try backward
			tries = 1; //max tries per cycle. better luck next time!
			while(tries > 0){
				tries--;
				newdir = world.rand.nextFloat()-world.rand.nextFloat();
				newdir *= delta;
				newdir += dir+180f; //try backwards
				newdist = 2.0f + world.rand.nextFloat()*getWidth()*getHeight()*4; //get a new general distance
				intdist = (int) (newdist+0.995f);
				newx =  (posx+Math.sin(Math.toRadians(newdir))*newdist);
				newz =  (posz+Math.cos(Math.toRadians(newdir))*newdist);
				newy = posy;
				for(int i=intdist;i>-intdist;i--){
					newy = posy+i;
					if(!isSolidAtLevel(dimension, newx, newy+1, newz)){
						if(isSolidAtLevel(dimension, newx, newy, newz) && CanProbablySee(dimension, newx, newy+1, newz, (int)newdist)){
							newy += 1;
							newy = (int)newy;
							target = new TargetHelper(null, newx, newy, newz);
							newtargetnow = false;
							//System.out.printf("New back can't swim Target set %f,  %f, %f\n", newx, newy, newz);
							return;
						}
					}
				}
			}
		}
	}
	

	 
	 public void findBlockFood(int maxdistxz, int maxdisty, int healamount, int eatitdistance){
			int i, j;
			//System.out.printf("Searching for food %d\n",getBaryonyxHealth()); 
			//Very efficient search from near to far.
			closest = 99999;
			tx = ty = tz = 0;
			for(i=1;i<maxdistxz;i++){
				j = i;
				if(j > maxdisty)j = maxdisty; //Limit y range
				if(scan_it((int)posx, (int)posy+1, (int)posz, i, j, i) == true)break;
				if(i>=6)i++; //skip to reduce long-range intensive processing...
			}

			if(closest < 99999){
				//System.out.printf("Food found at %d distance\n", closest);
				target = new TargetHelper(tx, ty-1, tz);
				if(closest < (eatitdistance)){
					//System.out.printf("Food eaten\n");
					heal(healamount);
					doEatFoodAction(dimension, tx,  ty,  tz);
					target = null;
				}
			}		
	 }
	 
	 public void doEatFoodAction(int d, int x, int y, int z){
			if(world.rand.nextInt(4) == 1)playburp();
			world.setblockandmetaWithPerm(this, d, x,  y,  z,  0, 0);
	 }
	 
	 public void playburp(){
		 int which = world.rand.nextInt(4);
		 if(which == 0)world.playSound("DangerZone:burp1", dimension, posx, posy, posz, 0.55f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.25f));
		 if(which == 1)world.playSound("DangerZone:burp2", dimension, posx, posy, posz, 0.55f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.25f));
		 if(which == 2)world.playSound("DangerZone:burp3", dimension, posx, posy, posz, 0.55f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.25f));
		 if(which == 3)world.playSound("DangerZone:burp", dimension, posx, posy, posz, 0.55f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.25f));
	 }

	 
	   
	 public boolean scan_it(int x, int y, int z, int dx, int dy, int dz){
		 int found = 0;
		 int i, j, bid, d;

		 //Fixed x, scan two sides of 3d rectangle
		 for(i=-dy;i<=dy;i++){
			 for(j=-dz;j<=dz;j++){
				 bid = world.getblock(dimension, x+dx, y+i, z+j);
				 if(isFoodBlock(bid)){
					 d = dx*dx + j*j + i*i;
					 if(d < closest){
						 closest = d;
						 tx = x+dx; ty = y+i; tz = z+j;
						 found++;
					 }
				 }
				 bid = world.getblock(dimension, x-dx, y+i, z+j);
				 if(isFoodBlock(bid)){
					 d = dx*dx + j*j + i*i;
					 if(d < closest){
						 closest = d;
						 tx = x-dx; ty = y+i; tz = z+j;
						 found++;
					 }
				 } 			
			 }
		 }
		 //Fixed y, scan two sides of 3d rectangle
		 for(i=-dx;i<=dx;i++){
			 for(j=-dz;j<=dz;j++){
				 bid = world.getblock(dimension, x+i, y+dy, z+j);
				 if(isFoodBlock(bid)){
					 d = dy*dy + j*j + i*i;
					 if(d < closest){
						 closest = d;
						 tx = x+i; ty = y+dy; tz = z+j;
						 found++;
					 }
				 }
				 bid = world.getblock(dimension, x+i, y-dy, z+j);
				 if(isFoodBlock(bid)){
					 d = dy*dy + j*j + i*i;
					 if(d < closest){
						 closest = d;
						 tx = x+i; ty = y-dy; tz = z+j;
						 found++;
					 }
				 } 			
			 }
		 }    	
		 //Fixed z, scan two sides of 3d rectangle
		 for(i=-dx;i<=dx;i++){
			 for(j=-dy;j<=dy;j++){
				 bid = world.getblock(dimension, x+i, y+j, z+dz);
				 if(isFoodBlock(bid)){
					 d = dz*dz + j*j + i*i;
					 if(d < closest){
						 closest = d;
						 tx = x+i; ty = y+j; tz = z+dz;
						 found++;
					 }
				 }
				 bid = world.getblock(dimension, x+i, y+j, z-dz);
				 if(isFoodBlock(bid)){
					 d = dz*dz + j*j + i*i;
					 if(d < closest){
						 closest = d;
						 tx = x+i; ty = y+j; tz = z-dz;
						 found++;
					 }
				 } 			
			 }
		 }    	

		 if(found != 0)return true;
		 return false;
	 }
	 
	 public float getAdjustedFallDamage(float ouch){
		 float damage = ouch;
		 if(getOwnerName() != null){
			 if(damage > 1)damage = 1;
		 }
		 if(!takesFallDamage)return 0;
		 return damage;
	 }
	 

	 public boolean getCanSpawnHereNow(World w, int dimension, int x, int y, int z){
		 if(daytimespawn){
			 if(!w.isDaytime())return false;
			 if(getLightAtLocation(w, dimension, x, y, z) < 0.55f)return false;
		 }
		 if(nighttimespawn){
			 //System.out.printf("%s trying to spawn, %s: %f\n", this.uniquename, w.isDaytime()?"true":"false", getLightAtLocation(w, dimension, x, y, z));
			 if(w.isDaytime())return false;
			 //System.out.printf("light = %f\n", getLightAtLocation(w, dimension, x, y, z));
			 if(getLightAtLocation(w, dimension, x, y, z) > 0.35f)return false;
		 }
		 return true;
	 }
	 
		public void doAttack(Entity victim){
			float dmg = getAttackDamage();
			int dt = DamageTypes.HOSTILE;
			if(has_inventory){					
				InventoryContainer ic = getHotbar(gethotbarindex());
				if(ic != null){
					if(ic.bid != 0){
						dt = DamageTypes.BLOCK;
					}else{
						if(ic.iid > 0 && ic.iid < Items.itemsMAX){
							dmg = Items.getAttackStrength(ic.iid);
							dt = DamageTypes.GENERIC;
							Item it = ic.getItem();
							if(it != null){
								if(it instanceof ItemSword)dt = DamageTypes.SWORD;
								if(it instanceof ItemPickAxe)dt = DamageTypes.TOOL;
								if(it instanceof ItemAxe)dt = DamageTypes.TOOL;
								if(it instanceof ItemShovel)dt = DamageTypes.TOOL;
							}
						}
					}
				}
			}
			//make some noise!
			if(getAttackSound() != null){
				world.playSound(getAttackSound(), dimension, posx, posy+getHeight()/2, posz, getLivingSoundVolume(), getLivingSoundPitch());
			}
			victim.doAttackFrom(this, dt, dmg);
		}
		
		public float getRightArmAngle(){
			return -armangle;
		}

		public Entity findSomethingToAttack(){	
			List<Entity> nearby_list = null;
			int maybenot = (int)getTotalEffect(Effects.CONFUSION);
			if(maybenot >= 2){
				if(world.rand.nextInt(maybenot) != 1)return null;
			}
			//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
			nearby_list = DangerZone.server.entityManager.findEntitiesInRange((getWidth()/2) + searchDistance, dimension, posx, posy, posz);
			if(nearby_list != null){
				if(!nearby_list.isEmpty()){
					//rewritten. 
					//much faster than the original sort crap.
					//When the system gets busy this is faster than sorting a huge list!
					Entity e = null;
					Entity eret = null;
					float best = -1;
					float weight = -1;
					float dist = 1;
					ListIterator<Entity> li = nearby_list.listIterator();
					while(li.hasNext()){
						e = (Entity)li.next();
						if(e != this && e.posy < 300){ 
							dist = (float) getDistanceFromEntity(e);
							if(dist < 1)dist = 1;
							weight = (e.getHeight() * e.getWidth()) / dist;
					        if(weight > best){					        	
					        	if(isSuitableTarget(e)){
					        		best = weight;
					        		eret = e;
					        	}
					        }
					       
						}
					}
					return eret;					
				}			
			}
			return null;
		}

		//default cockroaches and players only
		public boolean isSuitableTarget(Entity e){
			if(e == null)return false;
			if(isIgnorable(e))return false;
			if( e instanceof Cockroach){
				if(CanProbablySeeEntity(e))return true;
				return false;
			}
			if( e instanceof Player){
				if(CanProbablySeeEntity(e))return true;
				return false;
			}
			if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e))return true;
			return false;
		}
		

		//screen out the obvious non-attackable types
		public boolean isIgnorable(Entity e){
			if(DangerZone.playnicely)return true;
			if(e == null)return true;
			if(e == this)return true;
			if(!(e instanceof EntityLiving))return true;
			if(e.deadflag)return true;
			if(e.isInvisible())return true;
			if(e.getInLiquid() && (!canBreateUnderWater))return true;
			if(targetLiquidOnly && !e.getInLiquid())return true;
			if(e instanceof Player && e.getGameMode() != GameModes.SURVIVAL)return true;
			if(e == getRiderEntity())return true;
			if(getOwnerName() != null){
				if(e instanceof Player)return true;
				if(e instanceof Flag)return true; //pets don't attack flags!
				if(e.getOwnerName() != null)return true;
				Entity o = DangerZone.server.entityManager.findPlayerByName(getOwnerName());
				if(o != null){
					if(e.getDistanceFromEntity(o)-e.getWidth() > maxdisttoowner*1.25f){
						return true;
					}
				}
			}
			return false;
		}
		
		public Entity followFood(){	
			List<Entity> nearby_list = null;
			//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
			nearby_list = DangerZone.server.entityManager.findEntitiesInRange((getWidth()/2) + foodfollowDistance, dimension, posx, posy, posz);
			if(nearby_list != null){
				if(!nearby_list.isEmpty()){
					Entity e = null;
					ListIterator<Entity> li;
					//Sort them out based on size and distance
					Collections.sort(nearby_list, FoodSorter);
					li = nearby_list.listIterator();
					while(li.hasNext()){
						e = (Entity)li.next();
						if(e instanceof Player){
							Player p = (Player)e;
							InventoryContainer ic = p.getHotbar(p.gethotbarindex());
							if(ic != null && isFoodForMe(ic.bid, ic.iid))return e;
						}
					}								
				}			
			}
			return null;
		}
		
		public boolean rightClickedByPlayer(Player p, InventoryContainer ic){

			//Most stuff gets done on the server, except naming, which requires a GUI!
			if(world.isServer){
				if(enable_breeding){
					if(enable_taming){	
						//taming
						if(ic != null && isFoodForMe(ic.bid, ic.iid) && getOwnerName() == null){
							if(world.rand.nextInt(3) == 1){
								setOwnerName(p.myname);
								ToDoList.onTamed(p, this);
								Utils.spawnParticlesFromServer(world, "DangerZone:ParticleHappy", 20, dimension, posx, posy+getHeight(), posz);
								heal(10);
								setCanDespawn(false);
								if(temperament == Temperament.HOSTILE)temperament = Temperament.PASSIVE_AGGRESSIVE;
							}
							playburp();	
							return true; //decrement inventory!
						}
					}
					if(ic != null && isBreedingFoodForMe(ic.bid, ic.iid)){
						Utils.spawnParticlesFromServer(world, "DangerZone:ParticleHappy", 10, dimension, posx, posy+getHeight(), posz);
						heal(10);
						breeding = 200 + world.rand.nextInt(200);
						playburp();
						return true; //decrement inventory!
					}
				}
				if(enable_taming){	
					//taming
					if(ic != null && isFoodForMe(ic.bid, ic.iid) && getOwnerName() == null){
						if(world.rand.nextInt(3) == 1){
							setOwnerName(p.myname);
							ToDoList.onTamed(p, this);
							Utils.spawnParticlesFromServer(world, "DangerZone:ParticleHappy", 20, dimension, posx, posy+getHeight(), posz);
							heal(10);
							setCanDespawn(false);
							if(temperament == Temperament.HOSTILE)temperament = Temperament.PASSIVE_AGGRESSIVE;
						}
						playburp();	
						return true; //decrement inventory!
					}

					//feeding
					if(ic != null && isFoodForMe(ic.bid, ic.iid) && getOwnerName() != null && p.myname.equals(getOwnerName())){
						Utils.spawnParticlesFromServer(world, "DangerZone:ParticleHappy", 10, dimension, posx, posy+getHeight(), posz);
						playburp();
						Item it = ic.getItem();
						if(it != null && ((it instanceof ItemFood)||it.isfood)){
							heal(it.foodvalue*10);
						}else{
							heal(25);
						}
						int bid = ic.bid;
						int iid = ic.iid;
						if(iid != 0){
							it = Items.getItem(iid);
							if(it != null)it.onFoodEaten(this);
							ToDoList.onPetFed(p, this, ic);
						}
						if(bid != 0){
							Block bl = Blocks.getBlock(bid);
							if(bl != null)bl.onFoodEaten(this);
							ToDoList.onPetFed(p, this, ic);
						}
						return true; //decrement inventory!
					}

					//naming
					if(ic != null && ic.iid == Items.moosebone.itemID && getOwnerName() != null && p.myname.equals(getOwnerName())){
						Utils.spawnParticlesFromServer(world, "DangerZone:ParticleHappy", 20, dimension, posx, posy+getHeight(), posz);
						heal(1);
						world.playSound("DangerZone:pop", dimension, posx, posy, posz, 1.0f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.3f);
						return true; //decrement inventory!
					}

					if(ic != null && ic.iid == Items.vampireteeth.itemID && getOwnerName() != null && p.myname.equals(getOwnerName())){
						Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", 10, dimension, posx, posy+getHeight(), posz);
						setStaying(false);
						setOwnerName(null);
						setPetName(null);
						setCanDespawn(true);
						world.playSound("DangerZone:pop", dimension, posx, posy, posz, 1.0f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.3f);
						return true; //decrement inventory!
					}

					//sit/unsit
					if(ic != null && getOwnerName() != null && p.myname.equals(getOwnerName()) && getRiderEntity() == null){
						if(getStaying()){
							setStaying(false);
							setSitting(false);
							Utils.spawnParticlesFromServer(world, "DangerZone:ParticleHappy", 20, dimension, posx, posy+getHeight(), posz);
						}else{
							setStaying(true);
							setAttacking(false);
							setSitting(true);
							motionx = motionz = 0;
							target = null;
							Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", 10, dimension, posx, posy+getHeight(), posz);
						}
					}
				
				}
			}else{
				//naming
				if(enable_taming && ic != null && ic.iid == Items.moosebone.itemID && getOwnerName() != null && p.myname.equals(getOwnerName())){
					DangerZone.petnamegui.pet = this;
					DangerZone.setActiveGui(DangerZone.petnamegui);
					return false; //no decrement inventory!
				}
			}
			return false; //do not decrement inventory
		}
		
		public void findNewTargetNearOwner(Entity e){
			double newdir, newdist, newy, newx, newz;
			int intdist;

			target = new TargetHelper(e.posx, e.posy, e.posz);
			
			int tries = 8; //max tries per cycle. better luck next time!
			while(tries > 0){
				tries--;
				newdir = world.rand.nextFloat()-world.rand.nextFloat();
				newdir *= 180f;
				newdir += rotation_yaw; //grab a new general direction towards where we are already pointing
				newdist = 4.0f + world.rand.nextFloat()*getWidth()*getHeight()*2; //get a new general distance
				intdist = (int) (newdist+0.995f);
				newx =  (e.posx+Math.cos(newdir)*newdist);
				newz =  (e.posz+Math.sin(newdir)*newdist);
				newy = e.posy;
				for(int i=intdist;i>-intdist;i--){
					newy = posy+i;
					if(!isSolidAtLevel(dimension, newx, newy+1, newz)){
						if(isSolidAtLevel(dimension, newx, newy, newz) && CanProbablySee(dimension, newx, newy+1, newz, (int)newdist)){
							if(!canSwim && Blocks.isLiquid(world.getblock(dimension, (int)newx, (int)newy+1, (int)newz))){
								break;
							}
							newy += 1;
							newy = (int)newy;
							target.setTarget(newx, newy, newz);
							newtargetnow = false;
							//System.out.printf("New Target set %f,  %f, %f\n", newx, newy, newz);
							return;
						}
					}
				}
			}
		}

}
