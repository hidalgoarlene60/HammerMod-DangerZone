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


import dangerzone.DangerZone;
import dangerzone.GameModes;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;


public class EntityExp extends Entity {
	
	public int deathtimer = 10 * 60; // = one minute on the server
	public int pickup_delay = 0; //so you can toss things straight down
	public int points = 0;
	int nexttext = 0;
	int nexttextcount = 0;
	int countdir = 1;
	boolean tried = false;
	
	public EntityExp(World w){
		super(w);
		maxrenderdist = 32; //in blocks
		this.height = 0.25f;
		this.width = 0.25f;
		uniquename = "DangerZone:Experience";
		pickup_delay = 10; // = 1 second on server
		points = 0;
		if(w != null){
			nexttext = w.rand.nextInt(8);
			nexttextcount = w.rand.nextInt(10);
		}
		canthitme = true; //Ignore me!
		setFlying(true);
	}
	
	public void writeSelf(Properties prop, String tag){
		super.writeSelf(prop, tag);
		prop.setProperty(String.format("%s%s", tag, "DEATHTIMER"), String.format("%d", deathtimer));
		prop.setProperty(String.format("%s%s", tag, "POINTS"), String.format("%d", points));
	}
	
	public void readSelf(Properties prop, String tag){
		super.readSelf(prop, tag);
		deathtimer = Utils.getPropertyInt(prop, String.format("%s%s", tag, "DEATHTIMER"), 0, 32768, 600);
		points = Utils.getPropertyInt(prop, String.format("%s%s", tag, "POINTS"), 0, 10000, 1);
	}

	public void update( float deltaT){
		float rate = (float)((float)DangerZone.entityupdaterate/DangerZone.serverentityupdaterate);
				
		if(this.world.isServer){
			int bbid;
			boolean inliquid = false;
			
			bbid = this.world.getblock(dimension, (int)this.posx, (int)(this.posy-(this.getHeight()+0.25f)), (int)this.posz);
			if(Blocks.isSolid(bbid)){				
				motiony += 0.007f * deltaT;
			}else{
				if(Blocks.isLiquid(bbid)){
					Blocks.entityInLiquid(bbid, this);
					inliquid = true;
				}		
				motiony -= 0.005f * deltaT;
			}
			setInLiquid(inliquid);
			
			motiony *= (1.0f-(0.05f*deltaT));
			motionx *= (1.0f-(0.05f*deltaT));
			motionz *= (1.0f-(0.05f*deltaT));

			if(getOnFire() != 0){
				deathtimer -= 5;
			}
			
			deathtimer--;
			if(deathtimer <= 0){
				this.deadflag = true;
				return;
			}
			
			if(pickup_delay > 0)pickup_delay--;
			
			this.rotation_yaw_motion = 0;
			this.rotation_pitch_motion = 0;
			this.rotation_roll_motion = 0;
			this.rotation_pitch = 0;
			this.rotation_yaw = 0;
			this.rotation_roll = 0;
			
			if(!this.deadflag && pickup_delay <= 0){
				Player p = DangerZone.server.findNearestPlayer(this);
				if(p != null){
					if(this.getDistanceFromEntity(p) < 7 && !p.deadflag && p.getGameMode() != GameModes.LIMBO){
						if(this.getDistanceFromEntityCenter(p) < p.getWidth()){
							p.setExperience(p.getExperience()+this.getExperience());
							this.deadflag = true;
							if(getBID() == 0)world.playSound("DangerZone:pop", dimension, posx, posy, posz, 0.25f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.3f);
						}

					   	float tdir = (float) Math.atan2(p.posx - posx, p.posz - posz);
					   	double dist;
					   	double factor = 1.0f;
				    	dist = this.getDistanceFromEntity(p);
				    	factor = 7.0f/dist;
				    	if(factor > 1)factor = 1; 
				    	motionx += 0.04f*factor*Math.sin(tdir) * deltaT;
				    	motionz += 0.04f*factor*Math.cos(tdir) * deltaT;
				    	if(posy>p.posy+p.getHeight()/2)motiony -= 0.02f * deltaT;
				    	if(posy<p.posy+p.getHeight()/2)motiony += 0.06f * deltaT;
					}
				}
			}
			
			//if getting to be a lot of entities, try combining once...
			if(!tried && !deadflag && DangerZone.server.entityManager.active_entities > DangerZone.max_entities/4){
				List<Entity> nearby_list = null;
				ListIterator<Entity> li;
				Entity e = null;
				//						
				nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange(8.0f, dimension, posx, posy, posz);
				if(nearby_list != null){
					if(!nearby_list.isEmpty()){
						li = nearby_list.listIterator();
						while(li.hasNext()){
							e = (Entity)li.next();
							if(e != this && e instanceof EntityExp && !e.deadflag){	
								EntityExp ex = (EntityExp)e;
								ex.setExperience(ex.getExperience()+this.getExperience());
								ex.deathtimer = 10 * 120; //give it double!
								this.deadflag = true;
								break;																		
							}
						}
					}
				}
				tried = true;				
			}
			
			if(getInLiquid()){
				doInLiquid(1.0f);
				motionx *= 0.90f;
				motiony *= 0.90f;
				motionz *= 0.90f;
				doSolidsPushback(0, deltaT);
			}
			
		}else{
			motiony *= (1.0f-(0.05f*deltaT*rate));
			motionx *= (1.0f-(0.05f*deltaT*rate));
			motionz *= (1.0f-(0.05f*deltaT*rate));
		}
		super.update( deltaT );
	}
	
	public int getSubTexture(){
		int howfast = 16;
		if(this.getExperience() >= 10)howfast = 8;
		if(this.getExperience() >= 100)howfast = 4;
		if(this.getExperience() >= 1000)howfast = 1;
		
		nexttextcount++;
		if(nexttextcount >= howfast){
			nexttextcount = 0;
			nexttext += countdir;
			if(nexttext > 7){
				nexttext = 7;
				countdir = -1;
			}
			if(nexttext < 0){
				nexttext = 0;
				countdir = 1;
			}
		}
		return nexttext;
	}
	
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/misc/"+ "exp.png");
		}
		return texture;
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
		float tmx = motionx*deltaT;
		float tmz = motionz*deltaT;
		float rate = (float)((float)DangerZone.entityupdaterate/DangerZone.serverentityupdaterate);
		if(!world.isServer){
			tmx *= rate;
			tmz *= rate;
		}
		boolean hitsomething = false;
		float dist = (float) Math.sqrt(tmx*tmx + tmz*tmz);
		float curdist = 0.0f;
		int bid = 0;
		
		if(dist == 0)return false;
		/*
		 * Have to increment this out like a big fat ray for things that are moving fast!
		 */
		while(curdist < dist && !hitsomething){
			curdist += 0.1f;	
			if(curdist > dist)curdist = dist;
			float mx = (curdist/dist)*tmx;
			float mz = (curdist/dist)*tmz;
			double upx = posx + mx;
			double upz = posz + mz;
			//int bx, bz;

			for(i=-intwidth;i<=intwidth;i++){
				for(j=-intwidth;j<=intwidth;j++){
					bid = this.world.getblock(dimension, (int)upx+i, (int)(posy+kf), (int)upz+j);
					if(bid != 0 && Blocks.isSolid(bid, this.world, dimension, (int)upx+i, (int)(posy+kf), (int)upz+j)){				
						
						itemp = (int)(upx)+i;
						dx = ((double)itemp + 0.5f) - posx;
						itemp = (int)(upz)+j;
						dz = ((double)itemp + 0.5f) - posz;
						//if(this instanceof Player){
						//	System.out.printf("solid\n");
						//}

						if(Math.abs(dx)-(0.51f + (wdth/2)) < 0 && Math.abs(dz)-(0.51f + (wdth/2)) < 0){
							//already in a block. probably just jumped off. give them a nudge...
							if(dx > 0){
								posx -= 0.02f;
							}else{
								posx += 0.02f;
							}
							if(dz > 0){
								posz -= 0.02f;
							}else{
								posz += 0.02f;
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
									motionx = (float) (dx/deltaT);
									hitsomething = true;
									Blocks.bumpedBlock(bid, this, this.world, dimension, (int)upx+i, (int)(posy+kf), (int)upz+j);
								}
							}
						}else if(dx > 0 && mx > 0){
							//heading towards the solid, positive
							//but would I even hit it in the z direction?
							if(Math.abs(dz) < (0.51f + (wdth/2))){
								dx -= 0.51f;
								dx -= wdth/2;
								if(dx < mx){
									motionx = (float) (dx/deltaT);
									hitsomething = true;
									Blocks.bumpedBlock(bid, this, this.world, dimension, (int)upx+i, (int)(posy+kf), (int)upz+j);
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
									motionz = (float) (dz/deltaT);
									hitsomething = true;
									Blocks.bumpedBlock(bid, this, this.world, dimension, (int)upx+i, (int)(posy+kf), (int)upz+j);
								}
							}
						}else if(dz > 0 && mz > 0){
							//heading towards the solid, positive
							//but would I even hit it in the x direction?
							if(Math.abs(dx) < (0.51f + (wdth/2))){
								dz -= 0.51f;
								dz -= wdth/2;
								if(dz < mz){
									motionz = (float) (dz/deltaT);
									hitsomething = true;
									Blocks.bumpedBlock(bid, this, this.world, dimension, (int)upx+i, (int)(posy+kf), (int)upz+j);
								}
							}
						}
						
					}
				}
			}
		}
		return hitsomething;
	}
	
	 public float getBrightness(){
		 if(getExperience() < 10)return -0.25f;
		 if(getExperience() < 100)return 0f;
		 return 0.25f;
	 }
	
}

