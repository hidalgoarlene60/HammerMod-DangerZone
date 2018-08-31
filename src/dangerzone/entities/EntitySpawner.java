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

import dangerzone.DangerZone;
import dangerzone.ModelBase;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.BlockSpawner;
import dangerzone.blocks.Blocks;

public class EntitySpawner extends Entity {
	
	Entity trophyentity = null;
	ModelBase trophymodel = null;
	float trophyscale = 1.0f;
	int timer = 100;
	int blkmeta = 0; //used for light check!

	public EntitySpawner(World w) {
		super(w);
		uniquename = "DangerZone:EntitySpawner";
		ignoreCollisions = true;
		canthitme = true;
		width = 0.25f;
		height = 0.25f;
		maxrenderdist = 64; //not too far!
	}
	
	public void init(){
		super.init();
		timer = getVarInt(2); //reset timer!
		if(timer > 1)timer = world.rand.nextInt(timer);
	}
	
	
	public void update(float deltaT){
		int bid = 0;
		if(trophyentity == null){
			trophyentity = world.createEntityByName(getVarString(0), dimension, posx, posy, posz);
			if(trophyentity != null){
				trophymodel = trophyentity.model;
				trophyscale = getVarFloat(0);				
			}
		}

		if(world.isServer){
			bid = world.getblock(dimension, (int)posx, (int)posy, (int)posz);
			if(bid == 0){
				this.deadflag = true; //block broken!
			}else{
				if(Blocks.getBlock(bid) instanceof BlockSpawner){
					blkmeta = world.getblockmeta(dimension, (int)posx, (int)posy, (int)posz);
				}else{
					this.deadflag = true; //block broken!
				}
			}
			if(!deadflag && DangerZone.server.isPlayerCloseInDimension(this, 16.0f)){
				timer--;
				if(timer <= 0){
					timer = getVarInt(2); //reset timer!
					if(timer > 1)timer += world.rand.nextInt(timer);

					float lv = 0;
					if(blkmeta == 0)lv = getLightAtLocation(world, dimension, (int)posx, (int)posy, (int)posz); //range is like -1 to +2;
					//System.out.printf("light = %f,  min %f, max %f\n", lv, getVarFloat(1), getVarFloat(2));
					if(blkmeta != 0 || (lv > getVarFloat(1) && lv < getVarFloat(2))){ //check to make sure light range is ok						
						if(trophyentity != null){ 
							for(int i = 0; i < getVarInt(3); i++){ //loop for the number we should spawn
								//try to find a reasonable place to spawn this thing!
								float range = trophyentity.getWidth()*2;
								range+=2; //at least a few..
								int ht = (int)trophyentity.getHeight();
								//if(ht < 1)ht = 1; //at least 1
								double px, py, pz;
								boolean fits = false;
								px = posx;
								py = posy;
								pz = posz;
								//try a few randomish places
								for(int tries=0;tries<10 && !fits;tries++){
									px = posx + range*(world.rand.nextFloat()-world.rand.nextFloat());
									pz = posz + range*(world.rand.nextFloat()-world.rand.nextFloat());
									py = posy;							
									for(int k = ht; k>=-ht && !fits;k--){	//search down!							
										if(trophyentity.canFly || isSolidHere(world, dimension, px, py+k, pz, trophyentity.getWidth())){
											if(!isSolidHere(world, dimension, px, py+k+1, pz, trophyentity.getWidth())){
												//found an empty spot over a solid. See if we fit here...
												fits = true; //almost!
												if(ht > 0){ //check entire height
													for(int j=1;j<=ht && fits;j++){
														if(isSolidHere(world, dimension, px, py+k+j+1, pz, trophyentity.getWidth()))fits = false;
													}
												}
												if(fits){
													py = py+k+1;
												}
											}
										}
									}							
								}							
								if(fits){
									Entity e = world.createEntityByName(getVarString(0), dimension, px, py, pz);
									if(e != null){
										e.init();
										e.doFromSpawner();
										world.spawnEntityInWorld(e);
									}
								}
							}
						}
					}
				}
				
				
				if(timer < getVarInt(2)){
					int nparticle = (int)(6f - 6f*((float)timer/(float)getVarInt(2)));
					if(nparticle > 0){
						Utils.spawnParticlesFromServer(world, "DangerZone:ParticleFire", nparticle, dimension, posx, posy+0.25f, posz);
						Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSparkle", nparticle, dimension, posx, posy+0.25f, posz);
					}
				}

			}

		}

		motionx = motiony = motionz = 0;
		rotation_yaw_motion = 10;
		super.update(deltaT);
	}
	
	public Texture getTexture(){
		if(trophyentity != null){
			return trophyentity.getTexture();
		}
		return null;
	}
	
	//Check all around the entity WIDTH to see if it is solid.
	//Used for Y collisions
	private boolean isSolidHere(World w, int d, double x, double y, double z, float width){
		int intwidth = (int)((width/2.0f)+0.5f);
		int i, j;
		int itemp;
		double dx, dz;
		for(i=-intwidth;i<=intwidth;i++){
			for(j=-intwidth;j<=intwidth;j++){
				if(Blocks.isSolid(w.getblock(d, (int)x+i, (int)y, (int)z+j))){
					itemp = (int)(x)+i;
					dx = x - ((double)itemp + 0.5f);
					dx = Math.abs(dx);
					if(dx < (0.49f + (width/2.0f))){
						itemp = (int)(z)+j;
						dz = z - ((double)itemp + 0.5f);
						dz = Math.abs(dz);
						if(dz < (0.49f + (width/2.0f))){
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
