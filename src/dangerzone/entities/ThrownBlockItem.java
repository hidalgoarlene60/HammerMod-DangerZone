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

import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.Utils;
import dangerzone.World;


public class ThrownBlockItem extends Entity {
	
	public int deathtimer = 10 * 30; // = 30 seconds on the server
	public Entity thrower = null; //only valid on server-side!
	public Entity thrower2 = null; //only valid on server-side!
	public float spz = 0;
	public float spzoff = 0;
	
	public ThrownBlockItem(World w){
		super(w);
		maxrenderdist = 50; //in blocks
		this.height = 0.25f;
		this.width = 0.25f;
		uniquename = "DangerZone:ThrownBlockItem";
		movement_friction = false; //we are traveling in AIR!
		if(w != null){
			rotation_pitch = w.rand.nextInt(360);
			rotation_yaw = w.rand.nextInt(360);
			rotation_roll = w.rand.nextInt(360);
		}
		setAttackDamage(0.1f);
		spzoff = DangerZone.rand.nextFloat() * 12f;
		spzoff += 9;
		spzoff = -spzoff; //counter-clockwise!
	}
	
	public void writeSelf(Properties prop, String tag){
		super.writeSelf(prop, tag);
		prop.setProperty(String.format("%s%s", tag, "DEATHTIMER"), String.format("%d", deathtimer));
	}
	
	public void readSelf(Properties prop, String tag){
		super.readSelf(prop, tag);
		deathtimer = Utils.getPropertyInt(prop, String.format("%s%s", tag, "DEATHTIMER"), 0, 32768, 600);
	}
	
	public void setDirectionAndVelocity(float x, float y, float z, float velocity, float variability){
		motionx = x*(velocity + ((world.rand.nextFloat()-world.rand.nextFloat())*variability*velocity));
		motiony = y*(velocity + ((world.rand.nextFloat()-world.rand.nextFloat())*variability*velocity));
		motionz = z*(velocity + ((world.rand.nextFloat()-world.rand.nextFloat())*variability*velocity));
	}

	public void doEntityAction( float deltaT){

		motiony -= 0.16f * deltaT; //gravity-ish

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

		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		nearby_list = DangerZone.server.entityManager.findEntitiesInRange(16.0f+dist, dimension, posx, posy, posz);

		lx = ly = lz = 0;
		fx = posx;
		fy = posy;
		fz = posz;

		if(thrower != null){
			ridden = thrower.getRiddenEntity(); //don't hit our mount if we are riding something
			if(ridden == null){
				ridden = thrower.getRiderEntity(); //don't hit our rider
			}
		}

		/*
		 * increment along in the direction we are heading to see if we hit something.
		 * We do this, because we can move more (a lot!) than one block distance in a clock tick!
		 */
		while(dist > 0 && blockdist < dist){
			fx = posx + motionx*deltaT*blockdist/dist;
			fy = posy + motiony*deltaT*blockdist/dist;
			fz = posz + motionz*deltaT*blockdist/dist;
			x = (int)fx;
			y = (int)fy;
			z = (int)fz;

			li = nearby_list.listIterator();
			while(li.hasNext()){
				enthit = (Entity)li.next();
				if(enthit != this && !enthit.canthitme){
					if(enthit != thrower && enthit != ridden && enthit != thrower2){ //don't hit self or things that shouldn't be hit!
						if(fy > enthit.posy && fy < enthit.posy+enthit.getHeight()){
							edist = (float) enthit.getHorizontalDistanceFromEntity(fx, fz); //Center of tip to center of entity
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

			doSpecialEffects(fx, fy, fz);

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

	public void update( float deltaT){
		
		this.rotation_pitch_motion = 0;
		this.rotation_yaw_motion = 0;
		this.rotation_roll_motion = 0;
				
		if(!world.isServer){
			rotateFlatToPlayer();
		}

		super.update( deltaT );
	}

	//for any special effects while thrown...
	public void doSpecialEffects(double x, double y, double z){

	}
	
	
	public void rotateFlatToPlayer(){

		//always stay flat to player so we look like we have actual substance...	
		
		float tdir = (float) Math.atan2(DangerZone.player.posx - posx, DangerZone.player.posz - posz);
		float dx, dz;
		dx = (float) (DangerZone.player.posx - posx);
		dz = (float) (DangerZone.player.posz - posz);
		dx = (float) Math.sqrt(dx*dx + dz*dz);
		dz = (float)((DangerZone.player.posy + DangerZone.player.eyeheight) - posy);
		dz = (float) Math.atan2(dx, dz);
		
		rotation_pitch = (float) Math.toDegrees(dz+Math.PI/2);
		rotation_yaw = (float) Math.toDegrees(tdir);
		rotation_roll = 0;
		
		//the math to do arbitrary rotation around 3 axis is sooooo ugly...
		//easier to cheat and let opengl do it.
		//we use getSpinz() below...
		//the model will push the matrix and do the z rotation
		   
	}
	
	public float getSpinz(){
		spz += spzoff;
		if(spz > 360f)spz -= 360f;
		if(spz < -360f)spz += 360f;
		return spz;
	}
	
	
	//override for when we hit something
	public void doHitSomething(boolean hb, double x, double y, double z, boolean he, Entity ent){
		//change back into an item/block
		EntityBlockItem ee = (EntityBlockItem)world.createEntityByName(DangerZone.blockitemname, dimension, posx+motionx, posy+motiony, posz+motionz);
		if(ee != null){
			ee.fill(this.getBID(), this.getIID(), 1);					
			world.spawnEntityInWorld(ee);
		}
		if(he && ent != null)ent.doAttackFrom(thrower, DamageTypes.PROJECTILE, getAttackDamage());
		world.playSound("DangerZone:little_splat", dimension, posx+motionx, posy+motiony, posz+motionz, 0.5f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
	}
	
}
