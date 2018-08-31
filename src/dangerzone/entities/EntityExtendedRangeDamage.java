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


import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import dangerzone.DangerZone;
import dangerzone.Explosion;
import dangerzone.Utils;
import dangerzone.World;



public class EntityExtendedRangeDamage extends Entity {

	public int deathtimer = 60; // a few seconds...
	public Entity thrower = null; //only valid on server-side!
	public double oposx = 0;
	public double oposy = 0;
	public double oposz = 0;

	public EntityExtendedRangeDamage(World w){
		super(w);
		maxrenderdist = 1; //in blocks
		this.height = 0f;
		this.width = 0f;
		uniquename = "DangerZone:ExtendedRangeDamage";
		movement_friction = false; //we are traveling in AIR!
		canthitme = true;
		setAttackDamage(0); //damage
		setBID(0); //damagetype
		setMaxHealth(0); //explosive power
		setIID(0); //range
	}

	public void writeSelf(Properties prop, String tag){
		super.writeSelf(prop, tag);
		prop.setProperty(String.format("%s%s", tag, "DEATHTIMER"), String.format("%d", deathtimer));
	}

	public void readSelf(Properties prop, String tag){
		super.readSelf(prop, tag);
		deathtimer = Utils.getPropertyInt(prop, String.format("%s%s", tag, "DEATHTIMER"), 0, 60, 60);
	}

	public void setDirectionAndVelocity(float x, float y, float z, float velocity, float variability){
		motionx = x*(velocity + ((world.rand.nextFloat()-world.rand.nextFloat())*variability*velocity));
		motiony = y*(velocity + ((world.rand.nextFloat()-world.rand.nextFloat())*variability*velocity));
		motionz = z*(velocity + ((world.rand.nextFloat()-world.rand.nextFloat())*variability*velocity));
	}

	public void setDamageType(int dt){
		setBID(dt); //damagetype
	}
	public void setExplosivePower(int exp){
		setMaxHealth(exp); //explosive power
	}
	public void setRange(int rng){
		setIID(rng); //range
	}
	
	public void update( float deltaT){

		if(this.world.isServer){

			//Check for hitting a block...
			float dist = (float)Math.sqrt(motionx*deltaT*motionx*deltaT + motiony*deltaT*motiony*deltaT + motionz*deltaT*motionz*deltaT);
			float blockdist = 0;
			float edist = 0;
			boolean hitblock = false;
			boolean hitentity = false;
			int x,y,z;
			int lx, ly, lz;
			int bid;
			List<Entity> nearby_list = null;
			ListIterator<Entity> li;
			Entity enthit = null;
			double fx, fy, fz;
			Entity ridden = null; //only valid on server-side!

			if(oposx == 0){
				oposx = posx;
				oposy = posy;
				oposz = posz;
			}

			//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
			nearby_list = DangerZone.server.entityManager.findEntitiesInRange(dist+16f, dimension, posx, posy, posz);

			lx = ly = lz = 0;

			if(thrower != null){
				ridden = thrower.getRiddenEntity(); //don't hit our mount if we are riding something
				if(ridden == null){
					ridden = thrower.getRiderEntity(); //don't hit our rider
				}
			}
			fx = posx;
			fy = posy;
			fz = posz;

			/*
			 * increment along in the direction we are heading to see if we hit something.
			 * We do this, because we can move more (a lot!) than one block distance in a clock tick!
			 */
			while(blockdist < dist){
				fx = posx + motionx*deltaT*blockdist/dist;
				fy = posy + motiony*deltaT*blockdist/dist;
				fz = posz + motionz*deltaT*blockdist/dist;
				x = (int)fx;
				y = (int)fy;
				z = (int)fz;

				edist = (float) Math.sqrt(((oposx-fx)*(oposx-fx))+((oposy-fy)*(oposy-fy))+((oposz-fz)*(oposz-fz)));
				if(edist > getIID()){
					//ran out of range.
					this.deadflag = true;
					break;
				}

				li = nearby_list.listIterator();
				while(li.hasNext()){
					enthit = (Entity)li.next();
					if(enthit != this && !enthit.canthitme && enthit != thrower && enthit != ridden){ //don't hit self or things that shouldn't be hit!
						if(fy > enthit.posy && fy < enthit.posy+enthit.getHeight()){
							edist = (float)enthit.getHorizontalDistanceFromEntity(fx, fz); //Center of tip to center of entity
							edist -= getWidth()/2; //width of me
							edist -= (enthit.getWidth()/2); //general hitbox of entity
							if(edist < 0){
								//Hit something!
								hitentity = true;
								break;
							}
						}
					}
				}
				if(hitentity)break;

				//wait until block actually changes
				if(x != lx || y != ly || z != lz){
					lx = x; ly = y; lz = z;
					bid = world.getblock(dimension, x, y, z);
					if(bid != 0){ //Hit a block!!!	
						hitblock = true;
						break;
					}
				}

				blockdist += 0.1f;
			}

			if(hitentity || hitblock){
				//set motion = distance we hit something and stopped.
				motionx = motionx*deltaT*blockdist/dist;
				motiony = motiony*deltaT*blockdist/dist;
				motionz = motionz*deltaT*blockdist/dist;
				doHitSomething(hitblock, fx, fy, fz, hitentity, hitentity?enthit:null);				
				this.deadflag = true;
				return;
			}

			deathtimer--;
			if(deathtimer <= 0){
				this.deadflag = true;
				return;
			}

		}
		super.update( deltaT );
	}


	//override for when we hit something
	public void doHitSomething(boolean hb, double x, double y, double z, boolean he, Entity ent){
		if(getAttackDamage() > 0 && ent != null){
			ent.doAttackFrom(thrower, getBID(), getAttackDamage());
		}		
		if(getMaxHealth() > 0){
			Explosion.boom(thrower, world, dimension, x, y, z, (int)getMaxHealth(), true);
		}
	}

}


