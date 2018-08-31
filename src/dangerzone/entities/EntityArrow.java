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

import org.newdawn.slick.opengl.Texture;

import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.threads.LightingThread;


public class EntityArrow extends Entity {
	
	public int deathtimer = 10 * 60; // = 60 seconds on the server
	public Entity thrower = null; //only valid on server-side!
	public Entity thrower2 = null; //only valid on server-side!
	public boolean stuck = false;
	
	public EntityArrow(World w){
		super(w);
		maxrenderdist = 75; //in blocks
		this.height = 0.25f;
		this.width = 0.25f;
		uniquename = "DangerZone:EntityArrow";
		movement_friction = false; //we are traveling in AIR!
		setAttackDamage(1.0f); //overridden by the bow
	}
	
	public void writeSelf(Properties prop, String tag){
		super.writeSelf(prop, tag);
		prop.setProperty(String.format("%s%s", tag, "DEATHTIMER"), String.format("%d", deathtimer));
		prop.setProperty(String.format("%s%s", tag, "STUCK"), stuck?"true":"false");
	}
	
	public void readSelf(Properties prop, String tag){
		super.readSelf(prop, tag);
		deathtimer = Utils.getPropertyInt(prop, String.format("%s%s", tag, "DEATHTIMER"), 0, 32768, 600);
		stuck = Utils.getPropertyBoolean(prop, String.format("%s%s", tag, "STUCK"), true);
	}
	
	public void setDirectionAndVelocity(float x, float y, float z, float velocity, float variability){
		motionx = x*(velocity + ((world.rand.nextFloat()-world.rand.nextFloat())*variability*velocity));
		motiony = y*(velocity + ((world.rand.nextFloat()-world.rand.nextFloat())*variability*velocity));
		motionz = z*(velocity + ((world.rand.nextFloat()-world.rand.nextFloat())*variability*velocity));
		rotateToDirection();
	}

	public void doEntityAction( float deltaT){

		deathtimer--;
		if(deathtimer <= 0){
			this.deadflag = true;
			return;
		}
		if(stuck){
			motionx = motiony = motionz = 0;
			return;
		}

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
		
		motiony -= 0.16f * deltaT; //gravity-ish
				
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
		float omotionx, omotiony, omotionz;
		omotionx = motionx;
		omotiony = motiony;
		omotionz = motionz;
		while(dist > 0 && blockdist < dist){
			fx = posx + omotionx*deltaT*blockdist/dist;
			fy = posy + omotiony*deltaT*blockdist/dist;
			fz = posz + omotionz*deltaT*blockdist/dist;
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
					if(Blocks.isLiquid(bid)){
						motionx *= 0.90f;
						motionz *= 0.90f;
						motiony *= 0.90f;
					}else if(Blocks.isSquishy(bid) || Blocks.isSolid(bid)){
						hitblock = true;
						break;
					}
				}
			}

			doSpecialEffects(fx, fy, fz); //gets called 10x PER BLOCK TRAVELLED!

			blockdist += 0.1f;
		}
		


		if(hitentity || hitblock){
			//set motion = distance we hit something and stopped.
			posx = fx;
			posy = fy;
			posz = fz;
			doHitSomething(hitblock, fx, fy, fz, hitentity, hitentity?enthit:null);				
			if(hitentity)this.deadflag = true;
			//set final rotation direction
			rotateToDirection();
			setVarFloat(5, rotation_pitch);
			setVarFloat(6, rotation_yaw);
			motionx = motiony = motionz = 0;
			stuck = true;
			return;
		}
	}
	
	public void play_arrow_hit_sound(){
		int which = world.rand.nextInt(4);
		if(which == 0)world.playSound("DangerZone:arrow_hit1", dimension, posx, posy, posz, 0.5f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.4f));
		if(which == 1)world.playSound("DangerZone:arrow_hit2", dimension, posx, posy, posz, 0.5f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.4f));
		if(which == 2)world.playSound("DangerZone:arrow_hit3", dimension, posx, posy, posz, 0.5f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.4f));
		if(which == 3)world.playSound("DangerZone:arrow_hit4", dimension, posx, posy, posz, 0.5f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.4f));
	}
	
	public void update( float deltaT){		
		this.rotation_pitch_motion = 0;
		this.rotation_yaw_motion = 0;
		this.rotation_roll_motion = 0;		
		if(!world.isServer){
			rotateToDirection();
			if(getOnFire() > 0 && world.rand.nextInt(60) == 1)LightingThread.addRequest(dimension, (int)posx, (int)(posy), (int)posz, 0.35f); //make some light!
		}
		super.update( deltaT );
	}
	
	public void doAttackFrom(Entity e, /*DamageTypes*/int dt, float ouch){	
		//change back into an item/block
		EntityBlockItem ee = (EntityBlockItem)world.createEntityByName(DangerZone.blockitemname, dimension, posx, posy, posz);
		if(ee != null){
			ee.fill(this.getBID(), this.getIID(), 1);					
			world.spawnEntityInWorld(ee);
		}	
		play_arrow_hit_sound();
		this.deadflag = true;
	}

	//for any special effects while thrown...
	public void doSpecialEffects(double x, double y, double z){
		if(world.rand.nextInt(50) == 0)Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", 1, dimension, x, y, z);
		if(getOnFire() > 0){
			if(world.rand.nextInt(50) == 0)Utils.spawnParticlesFromServer(world, "DangerZone:ParticleFire", 1, dimension, x, y, z);
		}
	}
	
	
	public void rotateToDirection(){

		if(motionx == 0 && motiony == 0 && motionz == 0){
			rotation_pitch = getVarFloat(5);
			rotation_yaw = getVarFloat(6);
			return; //only way to know we stopped! Keep original direction...
		}
		
		float tdir = (float) Math.atan2(motionx, motionz);
		float dx, dz;
		dx = (float) Math.sqrt(motionx*motionx + motionz*motionz);
		dz = (float) Math.atan2(-motiony, dx);
		
		rotation_pitch = (float) Math.toDegrees(dz);
		rotation_yaw = (float) Math.toDegrees(tdir);
		rotation_roll = 0;
		   
	}
	
	//override for when we hit something
	public void doHitSomething(boolean hb, double x, double y, double z, boolean he, Entity ent){
		//change back into an item/block
		if(he && ent != null){
			ent.doAttackFrom(thrower, DamageTypes.PROJECTILE, getAttackDamage());
			int fire = getOnFire();
			if(fire > 0){
				ent.setOnFire(fire);
			}
		}
		play_arrow_hit_sound();
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Arrowtexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}
	
}
