package dangerzone.entities;



import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.Dimensions;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.BlockRail;
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;

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


public class EntityRaft extends Entity {

	public float ypush = 0;
	public int lastx, lasty, lastz;
	public float dist_in_block;
	public float usemotionx;
	public float usemotiony;
	public float usemotionz;
	public float max_speed = 1.75f;
	public boolean turnRight, turnLeft;
	public float spinz = 0;
	public boolean played = false;
	public int levstate = 0;
	public float target_yaw = 0;

	
	public EntityRaft(World w) {
		super(w);
		uniquename = "DangerZone:Raft";
		width = 2.5f;
		height = 0.25f;
		rotation_roll = 0;
		lastx = lasty = lastz = 0;
		dist_in_block = 0.5f; //where we spawn!
		usemotionx = usemotiony = usemotionz = 0;
		turnRight = turnLeft = false;
		has_inventory = true;
		levstate = 0; //0=none, 1=up, 2=down
	}
	
	/*
	 * This was FUN!!!!! :)
	 *
	 */
	public void update(float deltaT){	
		float new_rotation_yaw = 0;
		float new_rotation_pitch = 0;
		//Throttle velocity!
		float velocity = (float)Math.sqrt(usemotionx*usemotionx + usemotionz*usemotionz);
		//System.out.printf("velocity = %f\n", velocity);
		if(velocity > max_speed){
			usemotionx *= (max_speed/velocity);
			usemotionz *= (max_speed/velocity);
		}
		
		
		rotation_roll = 0;
		
		float distcheck = 0.25f;
		int railtype = isRailBlock(dimension, posx, posy, posz, distcheck);
		boolean onrail = railtype!=0?true:false;
		int railmeta = world.getblockmeta(dimension, (int)posx, (int)(posy+distcheck), (int)posz);
		int raily = (int)(posy+distcheck);
		if(!onrail){
			railtype = isRailBlock(dimension, posx, posy, posz, -distcheck);
			onrail = railtype!=0?true:false;
			railmeta = world.getblockmeta(dimension, (int)posx, (int)(posy-distcheck), (int)posz);
			raily = (int)(posy-distcheck);
		}
		
		if(!world.isServer){			
			if(onrail){
				//posy = raily;
				double tmp = posy - raily;			
				if(railtype == Blocks.waterstatic.blockID){				
					posy -= tmp/2;
				}else{
					if((railmeta & 0x10) == 0x10){
						//Show us in the right place! Client updates faster than server...
						double tmpxz = posx - (int)posx;	
						if((railmeta&0x02) != 0){
							tmp = tmpxz;
						}else if((railmeta&0x01) != 0){
							tmp = 1 - tmpxz;
						}else if((railmeta&0x08) != 0){
							tmp = posz - (int)posz;
						}else{
							tmp = posz - (int)posz;
							tmp = 1 - tmp;
						}
						posy = (int)raily;
						posy += tmp+0.01f;
					//}else{
					//	posy -= tmp/2;
					}
				}
			}
			super.update(deltaT);
			return;
		}
		
		if(posy < 0 || posy > 256){
			deadflag = true;
			super.update(deltaT);
			return;
		}

		/*
		 * levitating up and down...
		 */
		if(levstate != 0){
			
			new_rotation_pitch = 0;
			float tdiff = new_rotation_pitch - rotation_pitch;
			while(tdiff>180)tdiff -= 360;
			while(tdiff<-180)tdiff += 360;
			rotation_pitch_motion = (tdiff)/6;
			
			tdiff = target_yaw - rotation_yaw;
			while(tdiff>180)tdiff -= 360;
			while(tdiff<-180)tdiff += 360;
			rotation_yaw_motion = (tdiff)/6;
			
			if(onrail){				
				if(railtype == Blocks.raildown.blockID){
					if(levstate == 1){
						levstate = 0;
						dist_in_block = 0.7f;
						posy = raily;
						lastx = (int)posx;
						lasty = (int)posy;
						lastz = (int)posz;
					}else{
						posy -= 0.1f;
						super.update(deltaT);
						return;
					}
				}else if(railtype == Blocks.railup.blockID){
					if(levstate == 2){
						levstate = 0;
						dist_in_block = 0.7f;
						posy = raily;
						lastx = (int)posx;
						lasty = (int)posy;
						lastz = (int)posz;
					}else{
						posy += 0.1f;
						super.update(deltaT);
						return;
					}
				}else{
					levstate = 0;
					dist_in_block = 0.5f;
					posy = raily;
					lastx = (int)posx;
					lasty = (int)posy;
					lastz = (int)posz;
				}
				
			}else{			
				if(railtype == 0 || !Blocks.isSolid(railtype)){
					if(levstate == 1)posy += 0.1f;
					if(levstate == 2)posy -= 0.1f;
					super.update(deltaT);
					return;
				}
				levstate = 0;
				dist_in_block = 0;
				lastx = (int)posx;
				lasty = (int)posy;
				lastz = (int)posz;
			}
		}
		
		if(onrail && railtype == Blocks.waterstatic.blockID){
			usemotionx *= 0.95f;
			usemotionz *= 0.95f;
		}
		
		
		/*
		 * Velocity can be large, and so need to step slowly through the motion so as to detect turns and stay on the track!
		 */
		
		double usex = posx;
		double usez = posz;
		double usey = posy;
		float mx, mz, usev;

		int intpos;
		double dpos;
		float ddiff = 0;
		float decvel = velocity*deltaT;
		int curdir = 0;
		double ydiff  = 0;
		
		if(world.rand.nextInt(100) == 1){
			world.playSound("DangerZone:little_splash", dimension, usex, usey, usez, 0.25f, 1);
		}
		
		/*
		 * Handle no velocity at all.
		 */
		if(onrail && decvel == 0){
			new_rotation_yaw = alignToRail(railtype, railmeta); //if stationary!
			
			//if we are on a tilt, let's start rolling!
			ydiff = usey - raily;
			if(railtype == Blocks.waterstatic.blockID){
				usey = raily + ydiff/2;
			}else{
				if((railmeta & 0x10) == 0x10){
					//where are we in the block? that determines our height!
					double tmpxz = usex - (int)usex;
					if((railmeta&0x02) != 0){
						ydiff = tmpxz;
						new_rotation_pitch = -45f;
						if(usemotionx > 0){
							new_rotation_pitch = 45;
							//System.out.printf("2 and > 0\n");
							usemotionx -= 0.005f; //up
						}else{
							//System.out.printf("2 and < 0\n");
							usemotionx -= 0.005f; //down
						}
					}else if((railmeta&0x01) != 0){
						ydiff = 1 - tmpxz;
						new_rotation_pitch = -45f;
						if(usemotionx < 0){
							new_rotation_pitch = 45;
							//System.out.printf("1 and < 0\n");
							usemotionx += 0.005f; //up
						}else{
							//System.out.printf("1 and > 0\n");
							usemotionx += 0.005f; //down
						}
					}else if((railmeta&0x08) != 0){
						ydiff = usez - (int)usez;
						new_rotation_pitch = -45f;
						if(usemotionz > 0){
							new_rotation_pitch = 45;
							//System.out.printf("8 and > 0\n");						
							usemotionz -= 0.005f; //up
						}else{
							//System.out.printf("8 and < 0\n");						
							usemotionz -= 0.005f; //down
						}
					}else{
						ydiff = usez - (int)usez;
						ydiff = 1 - ydiff;
						new_rotation_pitch = 45f;
						if(usemotionz > 0){
							new_rotation_pitch = -45;
							//System.out.printf("4 and > 0\n");
							usemotionz += 0.005f; //down
						}else{
							//System.out.printf("4 and < 0\n");						
							usemotionz += 0.005f; //up
						}
					}
					usey = (int)raily;
					usey += ydiff+0.01f;
				}else{
					usey = raily + ydiff/2;
				}
			}
		}
		
		/*
		 * Step through our distance just a little at a time!
		 */
		while(decvel > 0){
			usev = decvel;
			if(usev > 0.10f)usev = 0.10f;
			decvel -= 0.10f;

			//Special partial collision that can move entities
			doEntityCollisions(deltaT, usex, usey, usez, usev);

			//Stay centered on track and keep moving.
			if(onrail){
				if(railtype != Blocks.waterstatic.blockID){
					//set direction and center self
					if(Math.abs(usemotionx) > Math.abs(usemotionz)){
						new_rotation_yaw = 90;
						curdir = 2;
						if(usemotionx > 0){
							new_rotation_yaw = 270;
							curdir = 1;
						}
						usemotionz = 0;
						intpos = (int)usez;
						dpos = usez - intpos;
						if(dpos < 0.5f){
							usez += (0.5f-dpos)/2;
						}else{
							usez -= (dpos-0.5f)/2;
						}
					}else{					
						new_rotation_yaw = 0;
						curdir = 8;
						if(usemotionz > 0){
							new_rotation_yaw = 180;
							curdir = 4;
						}
						usemotionx = 0;
						intpos = (int)usex;
						dpos = usex - intpos;
						if(dpos < 0.5f){
							usex += (0.5f-dpos)/2;
						}else{
							usex -= (dpos-0.5f)/2;
						}
					}
				}
				
				ydiff = usey - raily;
				usey = raily;
				//up/down
				if(railtype == Blocks.waterstatic.blockID){
					usey = raily;
				}else{
					if((railmeta & 0x10) == 0x10){

						//where are we in the block? that determines our height!
						double tmpxz = usex - (int)usex;

						if((railmeta&0x02) != 0){
							ydiff = tmpxz;
							new_rotation_pitch = -45f;
							if(usemotionx > 0){
								new_rotation_pitch = 45;
								//System.out.printf("2 and > 0\n");
								usemotionx -= 0.005f; //up
							}else{
								//System.out.printf("2 and < 0\n");
								usemotionx -= 0.005f; //down
							}
						}else if((railmeta&0x01) != 0){
							ydiff = 1 - tmpxz;
							new_rotation_pitch = -45f;
							if(usemotionx < 0){
								new_rotation_pitch = 45;
								//System.out.printf("1 and < 0\n");
								usemotionx += 0.005f; //up
							}else{
								//System.out.printf("1 and > 0\n");
								usemotionx += 0.005f; //down
							}
						}else if((railmeta&0x08) != 0){
							ydiff = usez - (int)usez;
							new_rotation_pitch = -45f;
							if(usemotionz > 0){
								new_rotation_pitch = 45;
								//System.out.printf("8 and > 0\n");						
								usemotionz -= 0.005f; //up
							}else{
								//System.out.printf("8 and < 0\n");						
								usemotionz -= 0.005f; //down
							}
						}else{
							ydiff = usez - (int)usez;
							ydiff = 1 - ydiff;
							new_rotation_pitch = 45f;
							if(usemotionz > 0){
								new_rotation_pitch = -45;
								//System.out.printf("4 and > 0\n");
								usemotionz += 0.005f; //down
							}else{
								//System.out.printf("4 and < 0\n");						
								usemotionz += 0.005f; //up
							}
						}
						usey = (int)raily;
						usey += ydiff+0.01f;

					}else{
						usey += ydiff/2;					
						//turning!
						if(dist_in_block > 0.4f && dist_in_block < 0.6f){
							//Let's try turning if we need to!
							if(turnLeft){
								int nextdir = leftTurn(curdir, railmeta);
								if(nextdir != 0){
									switch(nextdir){
									case 1:
										usemotionx = velocity;
										usemotionz = 0;
										break;
									case 2:
										usemotionx = -velocity;
										usemotionz = 0;
										break;
									case 4:
										usemotionz = velocity;
										usemotionx = 0;
										break;
									case 8:
										usemotionz = -velocity;
										usemotionx = 0;
										break;
									default:
										break;
									}
									turnLeft = false;
									dist_in_block = 0.7f;//don't care anymore... only try turning once.
									curdir = nextdir;
								}							
							}
							if(turnRight){
								int nextdir = rightTurn(curdir, railmeta);
								if(nextdir != 0){
									switch(nextdir){
									case 1:
										usemotionx = velocity;
										usemotionz = 0;
										break;
									case 2:
										usemotionx = -velocity;
										usemotionz = 0;
										break;
									case 4:
										usemotionz = velocity;
										usemotionx = 0;
										break;
									case 8:
										usemotionz = -velocity;
										usemotionx = 0;
										break;
									default:
										break;
									}
									turnRight = false;
									dist_in_block = 0.7f;//don't care anymore... only try turning once.
									curdir = nextdir;
								}							
							}
							if((railmeta & curdir) == 0){ //curdir is bit that we need set to continue in current direction.
								int nextdir = rightTurn(curdir, railmeta);
								if(nextdir == 0)nextdir = leftTurn(curdir, railmeta);
								if(nextdir != 0){
									switch(nextdir){
									case 1:
										usemotionx = velocity;
										usemotionz = 0;
										break;
									case 2:
										usemotionx = -velocity;
										usemotionz = 0;
										break;
									case 4:
										usemotionz = velocity;
										usemotionx = 0;
										break;
									case 8:
										usemotionz = -velocity;
										usemotionx = 0;
										break;
									default:
										break;
									}	
									curdir = nextdir;
								}
							}
							dist_in_block = 0.7f;//don't care anymore... only try turning once.
						}	
					}
				}
			}

			mx = usemotionx * usev/velocity;
			mz = usemotionz * usev/velocity;
			usex += mx;
			usez += mz;
			
			if(railtype == Blocks.waterstatic.blockID){
				//hit a block?
				if(shouldBounce(usex, usey, usez, velocity)){
					return; //we broke!
				}
			}else{
				//hit a block and reverse?
				if(((railmeta&0x10)==0) && shouldBounce(usex, usey, usez, velocity)){
					return; //we broke!
				}
			}

			if(lastx != (int)usex || lastz != (int)usez || lasty != (int)usey){
				lastx = (int)usex;
				lasty = (int)usey;
				lastz = (int)usez;
				dist_in_block = 0;
				played = false;
			}
			
			dist_in_block += usev;
			
			if(onrail){
				//do this last, so can push off a stop rail!
				if(railtype == Blocks.railspeed.blockID){
					usemotionx *= 1.01f;
					usemotionz *= 1.01f;
					Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSparkle", 5, dimension, usex, usey, usez);
					if(!played && velocity < 1.5f){
						world.playSound("DangerZone:swish1", dimension, usex, usey, usez, 2, 1);
						played = true;
					}
				}else if(railtype == Blocks.railslow.blockID){
					usemotionx *= 0.98f;
					usemotionz *= 0.98f;
					Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", 5, dimension, usex, usey, usez);
					if(!played && velocity < 1.5f){
						world.playSound("DangerZone:swish1", dimension, usex, usey, usez, 2, 1);
						played = true;
					}
				}else if(railtype == Blocks.railstop.blockID){
					usemotionx *= 0.50f;
					usemotionz *= 0.50f;
					if(velocity > 0.5f){
						Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", 50, dimension, usex, usey, usez);
					}
					if(!played && velocity > 0.5f){
						if(world.rand.nextBoolean()){
							world.playSound("DangerZone:stopscreech1", dimension, usex, usey, usez, 1, 1);
						}else{
							world.playSound("DangerZone:stopscreech2", dimension, usex, usey, usez, 1, 1);
						}
						played = true;
					}
				}else if(railtype == Blocks.railfixedslow.blockID){
					usemotionx *= (0.10f/velocity);
					usemotionz *= (0.10f/velocity);				
					if(velocity < 0.09f){
						Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSparkle", 5, dimension, usex, usey, usez);
						if(!played)world.playSound("DangerZone:swish1", dimension, usex, usey, usez, 2, 1);
						played = true;
					}
					if(velocity > max_speed/2){
						Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", 25, dimension, usex, usey, usez);
						if(!played){
							if(world.rand.nextBoolean()){
								world.playSound("DangerZone:stopscreech1", dimension, usex, usey, usez, 1, 1);
							}else{
								world.playSound("DangerZone:stopscreech2", dimension, usex, usey, usez, 1, 1);
							}
							played = true;
						}
					}else{
						if(velocity > 1.1f){
							Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", 5, dimension, usex, usey, usez);
						}
					}
				}else if(railtype == Blocks.railfixedmedium.blockID){
					usemotionx *= ((max_speed/2)/velocity);
					usemotionz *= ((max_speed/2)/velocity);				
					if(velocity < (max_speed/2)-0.1f){
						Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSparkle", 5, dimension, usex, usey, usez);
						if(!played)world.playSound("DangerZone:swish1", dimension, usex, usey, usez, 2, 1);
						played = true;
					}
					if(velocity > (max_speed/2)+0.1f){
						Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", 15, dimension, usex, usey, usez);
					}
				}else if(railtype == Blocks.railload.blockID){
					doLoadSomething(usex, usey, usez);
				}else if(railtype == Blocks.railunload.blockID){
					doUnloadSomething(usex, usey, usez);
				}else if(railtype == Blocks.raildplus.blockID){
					usey = dodimensionplus(usex, usey, usez);
					break;
				}else if(railtype == Blocks.raildminus.blockID){
					usey = dodimensionminus(usex, usey, usez);
					break;
				}else if(railtype == Blocks.railup.blockID){
					if(dist_in_block > 0.4f && dist_in_block < 0.7f){
						levstate = 1;
						break;
					}
				}else if(railtype == Blocks.raildown.blockID){
					if(dist_in_block > 0.4f && dist_in_block < 0.7f){
						levstate = 2;
						break;
					}
				}
			}
			
			velocity = (float)Math.sqrt(usemotionx*usemotionx + usemotionz*usemotionz);
			
			if(velocity > max_speed){
				usemotionx *= (max_speed/velocity);
				usemotionz *= (max_speed/velocity);
			}
			
			//we may have changed blocks, refresh regardless...
			if(decvel > 0){
				railtype = isRailBlock(dimension, usex, usey, usez, distcheck);
				onrail = railtype!=0?true:false;
				railmeta = world.getblockmeta(dimension, (int)usex, (int)(usey+distcheck), (int)usez);
				raily = (int)(usey+distcheck);
				if(!onrail){
					railtype = isRailBlock(dimension, usex, usey, usez, -distcheck);
					onrail = railtype!=0?true:false;
					railmeta = world.getblockmeta(dimension, (int)usex, (int)(usey-distcheck), (int)usez);
					raily = (int)(usey-distcheck);
				}
			}
		}	
		
		//client will smooth movement automatically.
		//we can't use motionx, motiony, motionz, because we emulate up to max_speed*10*10 cycles per second!
		//10 times a second, we increment by 10ths, up to max_speed distance.
		motionx = motiony = motionz = 0; 
		posx = usex;
		posy = usey;
		posz = usez;
		
		if(!onrail){
			int bid = world.getblock(dimension, (int)posx, (int)(posy), (int)posz);
			new_rotation_yaw = rotation_yaw + (world.rand.nextInt(5) - world.rand.nextInt(5));
			if(Blocks.isSolid(bid) || Blocks.isLiquid(bid)){
				usemotionx /= 2;
				usemotionz /= 2;
				usemotiony += 0.01f;
			}else{
				//fall?
				bid = world.getblock(dimension, (int)posx, (int)(posy-0.25f), (int)posz);
				if(Blocks.isSolid(bid) || Blocks.isLiquid(bid)){
					usemotionx /= 2;
					usemotionz /= 2;
					if(usemotiony < 0)usemotiony = 0;
					usemotiony += 0.01f;
				}else{
					usemotiony -= 0.05f;
				}
			}
			if(usemotiony < -1)usemotiony = -1;
			
			//float up and down just a little!
			ypush += 18f;
			ypush = ypush % 360;
			usemotiony += (float)0.01f*Math.cos(ypush);		
			usemotiony *= 0.90f;
			posy += usemotiony;

		}
			
		float rotspeed = 4;
		if(velocity > 0){
			rotspeed = max_speed/velocity;
			if(rotspeed > 4)rotspeed = 4;
			if(rotspeed < 1)rotspeed = 1;
		}
		Entity e = getRiderEntity();
		if(e != null){
			if(!onrail || railtype == Blocks.waterstatic.blockID){
				new_rotation_yaw = e.rotation_yaw+180;
			}
		}
		ddiff = new_rotation_yaw - rotation_yaw;
		while(ddiff>180)ddiff -= 360;
		while(ddiff<-180)ddiff += 360;
		rotation_yaw_motion = (ddiff)/rotspeed;
		target_yaw = new_rotation_yaw;
		
		ddiff = new_rotation_pitch - rotation_pitch;
		while(ddiff>180)ddiff -= 360;
		while(ddiff<-180)ddiff += 360;
		rotation_pitch_motion = (ddiff)/rotspeed;
		
		
		super.update(deltaT);
	}
	
	public boolean shouldBounce(double usex, double usey, double usez, float velocity){
		float mx = usemotionx * 0.45f/velocity;
		float mz = usemotionz * 0.45f/velocity;
		int bid = world.getblock(dimension, (int)(usex+mx), (int)(usey+0.25f), (int)(usez+mz));
		if(Blocks.isSolid(bid)){
			if(world.isServer){
				world.playSound("DangerZone:woodbreak", dimension, posx, posy, posz, 1, 1);
				Utils.doDropRand(world, 0, Items.raft.itemID, 1, dimension, posx, posy, posz);
				doDeathDrops();
				Entity e = getRiderEntity();
				if(e != null){
					unMount(e);
				}
				this.deadflag = true;
			}
			return true;
		}
		return false;
	}
	
	public void doEntityAction(float deltaT){
		Entity rider = getRiderEntity();
		if(rider == null)return;
		if(!(rider instanceof Player)){
			super.doEntityAction(deltaT);
			return;
		}

		//We have a rider! It's a player!
		float cdir = (float) Math.toRadians(rotation_yaw);
		
		float distcheck = 0.25f;
		int railtype = isRailBlock(dimension, posx, posy, posz, distcheck);
		boolean onrail = railtype!=0?true:false;
		if(!onrail){
			railtype = isRailBlock(dimension, posx, posy, posz, -distcheck);
			onrail = railtype!=0?true:false;
		}
		
		if(!onrail || railtype == Blocks.waterstatic.blockID){
			//now let's go...
			
	    	float tdir = (float) Math.toRadians(rider.rotation_yaw);	    	
	    	float speedfactor = 0.05f;
	    	if(!onrail)speedfactor = 0.01f;
	    	
	    	//now let's go...
	    	if(rider.getForward()){
	    		usemotionx += max_speed*speedfactor*Math.sin(tdir);
	    		usemotionz += max_speed*speedfactor*Math.cos(tdir);
	    	}
	    	if(rider.getBackward()){
	    		usemotionx -= max_speed*speedfactor*Math.sin(tdir);
	    		usemotionz -= max_speed*speedfactor*Math.cos(tdir);
	    	}
	    	if(rider.getLeft()){
	    		usemotionx += max_speed*speedfactor*Math.sin(tdir+Math.PI/2);
	    		usemotionz += max_speed*speedfactor*Math.cos(tdir+Math.PI/2);
	    	}
	    	if(rider.getRight()){
	    		usemotionx -= max_speed*speedfactor*Math.sin(tdir+Math.PI/2);
	    		usemotionz -= max_speed*speedfactor*Math.cos(tdir+Math.PI/2);
	    	}
	    	
		}else{
			//now let's go...
			if(rider.getForward()){
				usemotionx -= 0.1f*Math.sin(cdir);
				usemotionz -= 0.1f*Math.cos(cdir);
			}
			if(rider.getBackward()){
				usemotionx += 0.1f*Math.sin(cdir);
				usemotionz += 0.1f*Math.cos(cdir);
			}
			if(rider.getLeft()){
				turnLeft = true;
			}else{
				turnLeft = false;
			}
			if(rider.getRight()){
				turnRight = true;
			}else{
				turnRight = false;
			}
		}

		super.doEntityAction(deltaT);
	}
	
	public int isRailBlock(int d, double usex, double usey, double usez, double off){
		if(world == null)return 0;
		int bid = world.getblock(d, (int)usex, (int)(usey+off), (int)usez);
		if(bid == 0)return 0;
		Block b = Blocks.getBlock(bid);
		if(b instanceof BlockRail)return bid;
		if(bid == Blocks.waterstatic.blockID)return bid;
		return 0;
	}
	
	float alignToRail(int railtype, int railmeta){
		if(railtype == Blocks.waterstatic.blockID){	
			Entity rider = getRiderEntity();
			if(rider == null)return rotation_yaw;
			return rider.rotation_yaw;
		}else{
			if((railmeta&0x01) != 0)return 270;
			if((railmeta&0x02) != 0)return 90;
			if((railmeta&0x04) != 0)return 180;//
			if((railmeta&0x08) != 0)return 0;	//	
			return 0;
		}

	}
	
	int rightTurn(int indir, int inopts){
		if(indir == 1 && ((inopts & 4)!= 0))return 4;
		if(indir == 2 && ((inopts & 8)!= 0))return 8;
		if(indir == 4 && ((inopts & 2)!= 0))return 2;
		if(indir == 8 && ((inopts & 1)!= 0))return 1;
		return 0;
	}
	
	int leftTurn(int indir, int inopts){
		if(indir == 1 && ((inopts & 8)!= 0))return 8;
		if(indir == 2 && ((inopts & 4)!= 0))return 4;
		if(indir == 4 && ((inopts & 1)!= 0))return 1;
		if(indir == 8 && ((inopts & 2)!= 0))return 2;
		return 0;
	}
	
	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){	
		//Mount/unmount...
		if(world.isServer){
			//empty hand - ride, or get off!
			if(ic == null){
				if(getInventory(0) == null){
					if(isMountedBy(p)){
						unMount(p);
					}else{
						Mount(p);
					}
				}else{
					p.setHotbar(p.gethotbarindex(), getInventory(0));
					setInventory(0, null);
				}
			}else{
				if(getInventory(0) == null){
					setInventory(0, ic);
					p.setHotbar(p.gethotbarindex(), null);
				}
			}
			
		}
		return false;
	}
	
	public float getRiderYoffset(){
		int railtype = isRailBlock(dimension, posx, posy, posz, 0.25f);
		if(railtype != 0){
			if(railtype == Blocks.waterstatic.blockID)return 1.15f;
		}
		return 0.55f; //height to his back
	}
	
	public float getRiderXZoffset(){
		return 0; 
	}
	
	public boolean leftClickedByPlayer(Player p, InventoryContainer ic){
		if(world.isServer){
			Utils.doDropRand(world, 0, Items.raft.itemID, 1, dimension, posx, posy, posz);
			world.playSound("DangerZone:woodbreak", dimension, posx, posy, posz, 1, 1);
			doDeathDrops();
			if(isMountedBy(p)){
				unMount(p);
			}
			this.deadflag = true;
		}
		return false;
	}
	
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Rafttexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}
	
	public float getSpinz(){
		spinz += 1.5f;
		spinz = spinz % 360;
		return spinz;
	}
	
	
	public void doEntityCollisions(float deltaT){
		if(!world.isServer)return;
		
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
		nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange((wdth/2) + 8f, dimension, posx, posy, posz);

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
								dir = Math.atan2(e.posz-this.posz, e.posx-this.posx);
								
								if(isPushable(e)){
									dist = Math.sqrt(usemotionx*usemotionx + usemotionz*usemotionz);
									if(dist < 0.05f)dist = 0.05f;
									dist /= 2;
									e.motionx += Math.cos(dir)*dist*deltaT;
									e.motionz += Math.sin(dir)*dist*deltaT;
									if(e instanceof EntityRaft){
										EntityRaft emv = (EntityRaft)e;
										emv.usemotionx += Math.cos(dir)*dist*deltaT;
										emv.usemotionz += Math.sin(dir)*dist*deltaT;
										usemotionx *= 0.90f;
										usemotionz *= 0.90f;
									}
								}else{
									dist = Math.sqrt(e.motionx*e.motionx + e.motionz*e.motionz);
									if(dist < 0.05f)dist = 0.05f;
									dist = -dist/2;
									usemotionx += Math.cos(dir)*dist*deltaT; //push me!
									usemotionz += Math.sin(dir)*dist*deltaT; //push me!
								}
							}
						}
					}
				}								
			}			
		}				
	}
	
	public void doEntityCollisions(float deltaT, double usex, double usey, double usez, float usev){
		if(!world.isServer)return;
		
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
		nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange((wdth/2) + 8f, dimension, usex, usey, usez);

		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				double dir;
				double dist;
				Entity e = null;
				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (Entity)li.next();
					if(e != this && !e.ignoreCollisions){ //don't bump self!
						//System.out.printf("%f, %f found %s @ %f, %f == %f, %f\n", usex, usez, e.uniquename, e.posx, e.posz, usey, e.posy);
						if((usey+getHeight()) > e.posy && usey < (e.posy+e.getHeight())){
							dist = MYgetHorizontalDistanceFromEntity(e, usex, usez); //Center to center
							dist -= (wdth/2); //width of me
							dist -= (e.getWidth()/2); //width of it
							//System.out.printf("dist is %f\n", dist);
							if(dist < 0){
								//Bumped something!
								if(riddenid == e.entityID || riderid == e.entityID)continue; //oops nevermind...
								//do x-z bumping...
								dir = Math.atan2(e.posz-usez, e.posx-usex);
								
								if(isPushable(e)){
									//System.out.printf("pushable!\n");
									dist = usev;
									if(dist < 0.05f)dist = 0.05f;
									dist /= 2;
									e.motionx += Math.cos(dir)*dist*deltaT;
									e.motionz += Math.sin(dir)*dist*deltaT;
									e.posx += Math.cos(dir)*dist*deltaT;
									e.posz += Math.sin(dir)*dist*deltaT;
									if(e instanceof EntityRaft){
										//System.out.printf("maglev hit and moved!\n");
										EntityRaft emv = (EntityRaft)e;
										emv.usemotionx += Math.cos(dir)*dist*deltaT;
										emv.usemotionz += Math.sin(dir)*dist*deltaT;
										usemotionx *= 0.95f;
										usemotionz *= 0.95f;
									}
									if(e instanceof EntityMagLev){
										//System.out.printf("maglev hit and moved!\n");
										EntityMagLev emv = (EntityMagLev)e;
										emv.usemotionx += Math.cos(dir)*dist*deltaT;
										emv.usemotionz += Math.sin(dir)*dist*deltaT;
										usemotionx *= 0.95f;
										usemotionz *= 0.95f;
									}
								}else{
									//System.out.printf("not pushable!\n");
									dist = usev;
									if(dist < 0.05f)dist = 0.05f;
									dist = -dist/2;
									usemotionx += Math.cos(dir)*dist*deltaT;
									usemotionz += Math.sin(dir)*dist*deltaT;
								}
							}
						}
					}
				}								
			}			
		}				
	}
	
	public double MYgetHorizontalDistanceFromEntity(Entity p, double usex, double usez){
		double d1, d3;
		if(p.dimension != this.dimension)return 9999.0f;
		d1 = p.posx - usex;
		d3 = p.posz - usez;
		return Math.sqrt((d1*d1)+(d3*d3));
	}
	
	public boolean isPushable(Entity e){
		if(e instanceof EntityRaft)return true;
		if(e instanceof EntityMagLev)return true;
		if(e instanceof EntityBlockItem)return true;
		if(e instanceof EntityBlock)return true;
		if(e instanceof EntityExp)return true;
		if(e instanceof EntityArrow){
			e.deadflag = true;
			return true;
		}
		if(e instanceof EntityLiving){
			if(e.getHeight()*e.getWidth() < 1)return true;
		}
		return false;
	}
	
	public void writeSelf(Properties prop, String tag){
		super.writeSelf(prop, tag);
		prop.setProperty(String.format("%s%s", tag, "USEMOTIONX"), String.format("%f", usemotionx));
		prop.setProperty(String.format("%s%s", tag, "USEMOTIONY"), String.format("%f", usemotiony));
		prop.setProperty(String.format("%s%s", tag, "USEMOTIONZ"), String.format("%f", usemotionz));
		prop.setProperty(String.format("%s%s", tag, "DISTINBLOCK"), String.format("%f", dist_in_block));
		prop.setProperty(String.format("%s%s", tag, "LEVSTATE"), String.format("%d", levstate));
		
	}
	
	public void readSelf(Properties prop, String tag){
		super.readSelf(prop, tag);
		usemotionx = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "USEMOTIONX"), -5, 5, 0);
		usemotiony = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "USEMOTIONY"), -5, 5, 0);
		usemotionz = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "USEMOTIONZ"), -5, 5, 0);
		dist_in_block = Utils.getPropertyFloat(prop, String.format("%s%s", tag, "DISTINBLOCK"), 0, 1, 0.5f);
		levstate = Utils.getPropertyInt(prop, String.format("%s%s", tag, "LEVSTATE"), 0, 2, 0);
		
	}
	
	public void doLoadSomething(double usex, double usey, double usez){
		if(getInventory(0) != null)return; //already full!
		if(getRiderEntity() != null)return; //already full!
		
		List<Entity> nearby_list = null;
		Entity e = null;
		//Find an entity!
		nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange(1.75f, dimension, usex, usey, usez);
		if(nearby_list != null){
			if(!nearby_list.isEmpty()){				
				ListIterator<Entity> li;
				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (Entity)li.next();
					if(e != this && !(e instanceof EntityRaft) && e.has_inventory){ 	
						if((e instanceof EntityChest)
						|| (e instanceof EntityDesk)
						|| (e instanceof EntityBlockItem)){ //pick up stray items too!
							if(isNotEmpty(e)){
								break;
							}
						}
					}
					e = null;
				}								
			}			
		}
		if(e != null){
			InventoryContainer ic = null;
			for(int i=0;i<50;i++){
				ic = e.getInventory(i);
				if(ic != null){
					setInventory(0, ic);
					e.setInventory(i, null);
					break;
				}
			}
		}
	}
	
	public boolean isNotEmpty(Entity ent){
		for(int i=0;i<50;i++){
			if(ent.getInventory(i) != null)return true;
		}
		return false; //IS empty
	}
	
	public void doUnloadSomething(double usex, double usey, double usez){
		if(getInventory(0) == null)return; //nothing to unload!
		
		List<Entity> nearby_list = null;
		Entity e = null;
		//Find an entity!
		nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange(1.75f, dimension, usex, usey, usez);
		if(nearby_list != null){
			if(!nearby_list.isEmpty()){
				ListIterator<Entity> li;
				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (Entity)li.next();
					if(e != this && !(e instanceof EntityRaft) && e.has_inventory){ 
						if((e instanceof EntityChest)
						|| (e instanceof EntityDesk)){
							if(fillEntity(e, getInventory(0))){
								break;
							}
						}
					}
				}								
			}			
		}
		//if(getInventory(0) != null){
		//	Utils.doDropRand(world, getInventory(0), 1, dimension, usex, usey, usez);
		//	setInventory(0, null);
		//}
	}
	
	public boolean fillEntity(Entity ent, InventoryContainer ic){
		while(ic.count > 0){
			if(!putMeInASlot(ent, new InventoryContainer(ic.bid, ic.iid, 1, ic.currentuses, ic.count==1?ic.attributes:null, ic.icmeta))){
				break;
			}
			ic.count--;
		}
		if(ic.count <= 0){
			setInventory(0, null);
			return true; //DONE!!!
		}else{
			setInventoryChanged(0); //just set changed flag.
		}
		return false;
	}
	
	/*
	 * Add a SINGLE block or item into a slot
	 */
	public boolean putMeInASlot(Entity ent, InventoryContainer inic){
		int bid = inic.bid;
		int iid = inic.iid;
				
		if(inic.count == 0 || (inic.iid == 0 && inic.bid == 0))return true; //pretend we did so it will go away. It should be null anyway!
		
		//Can we add to an existing one?
		for(int i=0;i<50;i++){
			InventoryContainer ic = ent.getInventory(i);
			if(ic != null){
				if(ic.bid == bid && ic.iid == iid){
					if(ic.bid != 0){
						if(ic.count < Blocks.getMaxStack(bid)){
							ic.count++;
							ent.setInventoryChanged(i);
							return true;
						}
					}
					if(ic.iid != 0){
						if(ic.count < Items.getMaxStack(iid)){
							ic.count++;
							ent.setInventoryChanged(i);
							return true;
						}
					}
				}
			}
		}
		
		//ok then, go for new...		
		for(int i=0;i<50;i++){
			if(ent.getInventory(i) == null){
				ent.setInventory(i, inic);
				return true;
			}
		}
		return false;
	}
	
	public double dodimensionplus(double usex, double usey, double usez){
		int nextdim = 1;
		for(int i=0;i<Dimensions.dimensionsMAX;i++){
			int id = (this.dimension+i+1)%Dimensions.dimensionsMAX;
			if(Dimensions.DimensionArray[id] != null){
				if(!Dimensions.DimensionArray[id].special_hidden){
					nextdim = id;
					break;
				}
			}
		}
		double newy = findnewy(nextdim, usex, usey, usez);
		Entity rider = getRiderEntity();
		if(rider == null){
			dimension = nextdim;
			posy = newy;
			DangerZone.server.sendEntityUpdateToAll(this, true); //FORCE
		}else{
			if(!(rider instanceof Player)){
				dimension = nextdim;
				posy = newy;
				DangerZone.server.sendEntityUpdateToAll(this, true); //FORCE
				rider.dimension = nextdim;
				rider.posy = newy;
				DangerZone.server.sendEntityUpdateToAll(rider, true); //FORCE
			}else{
				Player pl = (Player)rider;
				Utils.doTeleport(pl, nextdim, usex, newy, usez);
			}			
		}
		return newy;
	}
	
	public double dodimensionminus(double usex, double usey, double usez){
		int nextdim = 1;
		for(int i=0;i<Dimensions.dimensionsMAX;i++){
			int id = (this.dimension-i-1)%Dimensions.dimensionsMAX;
			while(id<0)id += Dimensions.dimensionsMAX;
			if(Dimensions.DimensionArray[id] != null){
				if(!Dimensions.DimensionArray[id].special_hidden){
					nextdim = id;
					break;
				}
			}
		}
		double newy = findnewy(nextdim, usex, usey, usez);
		Entity rider = getRiderEntity();
		if(rider == null){
			dimension = nextdim;
			posy = newy;
			DangerZone.server.sendEntityUpdateToAll(this, true); //FORCE
		}else{
			if(!(rider instanceof Player)){
				dimension = nextdim;
				posy = newy;
				DangerZone.server.sendEntityUpdateToAll(this, true); //FORCE
				rider.dimension = nextdim;
				rider.posy = newy;
				DangerZone.server.sendEntityUpdateToAll(rider, true); //FORCE
			}else{
				Player pl = (Player)rider;
				Utils.doTeleport(pl, nextdim, usex, newy, usez);
			}			
		}
		return newy;
	}
	
	public double findnewy(int d, double usex, double usey, double usez){
		int bid = 0;
		for(int i=1;i<256;i++){
			bid = isRailBlock(d, usex, i, usez, 0);
			if(bid != 0)return i;
		}
		//no rail? go for first zero block over non-zero
		for(int i=255;i>0;i--){
			bid = world.getblock(d, (int)usex, i, (int)usez);
			if(bid != 0)return i+1;
		}
		return usey;
	}
	
	public float getDisplayOffset(){
		int railtype = isRailBlock(dimension, posx, posy, posz, 0.25f);
		if(railtype != 0){
			if(railtype == Blocks.waterstatic.blockID)return 10.0f;
			//return 6.0f;
		}
		return 0.0f;
	}
	
}

