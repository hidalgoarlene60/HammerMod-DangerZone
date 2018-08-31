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

import org.newdawn.slick.opengl.Texture;


import dangerzone.DangerZone;
import dangerzone.Dimensions;
import dangerzone.Player;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.particles.ParticleRain;


public class EntityCloud extends Entity {
	
	float py, px, pz;
	int parts[];
	
	public EntityCloud(World w){
		super(w);
		maxrenderdist = 512; //in blocks
		this.height = 1f;
		this.width = 1;
		uniquename = "DangerZone:Cloud";
		canthitme = true; //Ignore me!
		py = 0;
		px = ((DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*0.1f);
		pz = ((DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*0.1f);
		setBID(Blocks.cloud_light.blockID);
		parts = new int[32];
		always_draw = true;
		ignoreCollisions = true;
	}
	
	public void init(){
		//Have to generate a new cloud!
		//Yes, the shape is different every time...
		int ix, iz, ic;
		for(int k=0;k<10;k++){
			for(int i=0;i<32;i++){
				for(int j=0;j<32;j++){
					ix = i - 16;
					if(ix < 0)ix = -ix;
					iz = j - 16;
					if(iz < 0)iz = -iz;
					ic = ix*ix+iz*iz;
					if(ic <= 2){
						setpart(i,j,true);
					}else{
						if(DangerZone.rand.nextInt(ic) == 0){
							setpart(i, j, true);
						}
					}					
				}
			}
		}	
		posy = 190 + DangerZone.rand.nextInt(10) - DangerZone.rand.nextInt(10);
		posy += (DangerZone.rand.nextFloat() - DangerZone.rand.nextFloat());
	}
	
	private void setpart(int i, int j, boolean b){
		if(i < 0 || i > 31)return;
		if(j < 0 || j > 31)return;
		int val = 0x01 << i;
		if(b){
			parts[j] |= val;
		}else{
			parts[j] &= ~val;
		}
	}
	
	public boolean getpart(int i, int j){
		if(i < 0 || i > 31)return false;
		if(j < 0 || j > 31)return false;
		int val = 0x01 << i;
		if((parts[j]&val) == val)return true;		
		return false;
	}

	public void update( float deltaT){
		
		if(this.world.isServer){

			motiony = py;
			motionx = px;
			motionz = pz;

			this.rotation_yaw_motion = 0;
			this.rotation_pitch_motion = 0;
			this.rotation_roll_motion = 0;
			this.rotation_pitch = 0;
			this.rotation_yaw = 0;
			this.rotation_roll = 0;
			
			//Despawn!
			if(getCanDespawn()){
				Player p = DangerZone.server.findNearestPlayer(this);
				if(p == null || getHorizontalDistanceFromEntity(p) > DangerZone.entityupdatedist - 8){
					setBID(0);
				}
			}
			
			int mybid = getBID();
			if(mybid == 0 || deadflag || Dimensions.DimensionArray[dimension].cloud_enable == false){
				deadflag = true;
				return;
			}
			if(mybid == Blocks.cloud_thunder.blockID && Dimensions.DimensionArray[dimension].lightning_enable){
				if(world.rand.nextInt(100) == 0){
					for(int i=(int)posy;i>0;i--){
						if(world.getblock(dimension, (int)posx, i, (int)posz) != 0){
							Entity e = world.createEntityByName("DangerZone:Lightning", dimension, posx, i, posz);
							if(e != null){
								e.init();
								e.setAttackDamage(5.0f); //set boom size!
								world.spawnEntityInWorld(e);
							}
							break; //!!!
						}
					}
				}				
			}
			
			if(world.rand.nextInt(4000) == 1){
				px = ((DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*0.1f);
				pz = ((DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*0.1f);
				if(mybid == Blocks.cloud_thunder.blockID){
					setBID(Blocks.cloud_rain.blockID);
				}else if(mybid == Blocks.cloud_rain.blockID){
					setBID(Blocks.cloud_light.blockID);
				}else{				
					setBID(0);
					deadflag = true;
				}
			}else{
				if(world.rand.nextInt(20) == 1){ //check for nearby clouds and combine with them...
					List<Entity> nearby_list = null;
					//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
					nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange(32, dimension, posx, posy, posz);
					if(nearby_list != null){
						if(!nearby_list.isEmpty()){
							Entity e = null;
							ListIterator<Entity> li;
							li = nearby_list.listIterator();
							while(li.hasNext()){
								e = (Entity)li.next();
								if(e != this && e instanceof EntityCloud && world.rand.nextBoolean()){
									if(mybid == Blocks.cloud_thunder.blockID){
										if(e.getBID() != Blocks.cloud_thunder.blockID){
											e.setBID(0);
											e.deadflag = true;
										}
									}else if(mybid == Blocks.cloud_rain.blockID){
										if(e.getBID() == Blocks.cloud_thunder.blockID){
											setBID(0);
											deadflag = true;
										}else if(e.getBID() == Blocks.cloud_rain.blockID){
											if(Dimensions.DimensionArray[dimension].lightning_enable && world.rand.nextBoolean())setBID(Blocks.cloud_thunder.blockID);
											e.setBID(0);
											e.deadflag = true;
										}else{
											e.setBID(0);
											e.deadflag = true;
										}
									}else{
										if(e.getBID() == Blocks.cloud_thunder.blockID){
											setBID(0);
											deadflag = true;
										}else if(e.getBID() == Blocks.cloud_rain.blockID){
											setBID(0);
											deadflag = true;
										}else{
											if(Dimensions.DimensionArray[dimension].rain_enable && world.rand.nextBoolean())setBID(Blocks.cloud_rain.blockID);
											e.setBID(0);
											e.deadflag = true;
										}
									}
									break; //just one at a time
								}								
							}								
						}			
					}
				}
			}

		}else{
			//spawn rain on the client side, so there is no network traffic generated.
			//it would be a total flood of packets if we did it from the server side!
			int mybid = getBID();
			if(DangerZone.show_rain && ((mybid == Blocks.cloud_rain.blockID && world.rand.nextInt(8) == 1) || mybid == Blocks.cloud_thunder.blockID)){
				for(int i=0;i<4;i++){
					double px = posx + ((DangerZone.rand.nextFloat() - DangerZone.rand.nextFloat())*16);
					double pz = posz + ((DangerZone.rand.nextFloat() - DangerZone.rand.nextFloat())*16);
					double py = posy + DangerZone.rand.nextFloat() - 1.5f;

					ParticleRain pr = (ParticleRain) world.createParticleByName("DangerZone:ParticleRain", dimension, px, py, pz);
					if(pr != null){
						world.spawnParticleInWorld(pr);
					}
				}
			}

		}
		
		if(getBID() == Blocks.cloud_thunder.blockID){
			if(this.world.isServer){
				if(DangerZone.thundercount < 50)DangerZone.thundercount+=2;
			}else{
				if(DangerZone.thundercount < 300)DangerZone.thundercount+=2;
			}
		}
		super.update( deltaT );
	}
	
	public boolean getCanSpawnHereNow(World w, int dimension, int x, int y, int z){
		return true;
	}
		
	
	public Texture getTexture(){
		return null; //uses a block texture in the model from getBID();
	}
	

	
}

