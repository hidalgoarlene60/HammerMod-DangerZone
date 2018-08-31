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
import dangerzone.GameModes;
import dangerzone.InventoryContainer;
import dangerzone.ItemAttribute;
import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;


public class EntityBlockItem extends Entity {
	
	public int deathtimer = 10 * 120; // = two minutes on the server
	public int pickup_delay = 0; //so you can toss things straight down
	private float mx, mz;
	boolean tried = false;
	
	public EntityBlockItem(World w){
		super(w);
		maxrenderdist = 40; //in blocks
		this.height = 0.25f;
		this.width = 0.25f;
		uniquename = DangerZone.blockitemname;
		pickup_delay = 10; // = 1 second on server
		canthitme = true; //Ignore me!
		if(w != null){
			rotation_pitch = w.rand.nextInt(360);
			rotation_yaw = w.rand.nextInt(360);
			rotation_roll = w.rand.nextInt(360);
		}
		mx = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat()) * 0.006f;
		mz = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat()) * 0.006f;
		setFlying(true);
		has_inventory = true;
	}
	
	public void fill(InventoryContainer ic){
		setInventory(0, ic);
	}
	
	public void fill(Object inobj, int count){
		InventoryContainer ic = new InventoryContainer(inobj, count, 0);
		setInventory(0, ic);
	}
	
	public void fill(Object inobj, int count, int inuses){
		InventoryContainer ic = new InventoryContainer(inobj, count, inuses);
		setInventory(0, ic);
	}
	
	public void fill(Object inobj, int count, int inuses, List<ItemAttribute>inlist){
		InventoryContainer ic = new InventoryContainer(inobj, count, inuses, inlist);
		setInventory(0, ic);
	}
	
	public void fill(Object inobj, int count, int inuses, List<ItemAttribute>inlist, String inmeta){
		InventoryContainer ic = new InventoryContainer(inobj, count, inuses, inlist, inmeta);
		setInventory(0, ic);
	}
	
	public void fill(int inbid, int iniid, int count){
		InventoryContainer ic = new InventoryContainer(inbid, iniid, count, 0);
		setInventory(0, ic);
	}
	
	public void fill(int inbid, int iniid, int count, int inuses){
		InventoryContainer ic = new InventoryContainer(inbid, iniid, count, inuses);
		setInventory(0, ic);
	}
	
	public void fill(int inbid, int iniid, int count, int inuses, List<ItemAttribute>inlist){
		InventoryContainer ic = new InventoryContainer(inbid, iniid, count, inuses, inlist);
		setInventory(0, ic);
	}
	
	public void fill(int inbid, int iniid, int count, int inuses, List<ItemAttribute>inlist, String inmeta){
		InventoryContainer ic = new InventoryContainer(inbid, iniid, count, inuses, inlist, inmeta);
		setInventory(0, ic);
	}
	
	public int getItemID(){
		InventoryContainer ic = getInventory(0);
		if(ic == null)return 0;
		return ic.iid;
	}
	
	public int getBlockID(){
		InventoryContainer ic = getInventory(0);
		if(ic == null)return 0;
		return ic.bid;
	}
	
	
	public void writeSelf(Properties prop, String tag){
		super.writeSelf(prop, tag);
		prop.setProperty(String.format("%s%s", tag, "DEATHTIMER"), String.format("%d", deathtimer));
	}
	
	public void readSelf(Properties prop, String tag){
		super.readSelf(prop, tag);
		deathtimer = Utils.getPropertyInt(prop, String.format("%s%s", tag, "DEATHTIMER"), 0, 32768, 600);
	}
	
	public void update( float deltaT){
		
		this.rotation_pitch_motion = 3;
		this.rotation_yaw_motion = 9;
		this.rotation_roll_motion = 5;
				
		if(this.world.isServer){

			int bbid;
			float f;
			boolean inliquid = false;
			InventoryContainer ic = getInventory(0);
			if(ic == null){
				deadflag = true;
				return;
			}
			if(ic.count <= 0){
				deadflag = true;
				return;
			}
			if(deadflag)return;
					
			if(this.world.rand.nextInt(100) == 1){
				f = this.world.rand.nextFloat() * 0.06f;
				//System.out.printf("Random f = %f\n", f);
				motiony += f;
			}
			if(this.world.rand.nextInt(200) == 1){
				mx = (this.world.rand.nextFloat()-this.world.rand.nextFloat()) * 0.006f;
				//System.out.printf("Random f = %f\n", f);
				//motionx += f;
			}
			if(this.world.rand.nextInt(200) == 1){
				mz = (this.world.rand.nextFloat()-this.world.rand.nextFloat()) * 0.006f;
				//System.out.printf("Random f = %f\n", f);
				//motionz += f;
			}
			
			motionx += mx;
			motionz += mz;
			
			bbid = this.world.getblock(dimension, (int)this.posx, (int)(this.posy-(this.getHeight())), (int)this.posz);
			if(Blocks.isSolid(bbid)){					
					motiony += 0.12f * deltaT;				
			}else{
				if(Blocks.isLiquid(bbid)){
					Blocks.entityInLiquid(bbid, this);
					inliquid = true;
				}			
				motiony -= 0.03f * deltaT;
			}
			setInLiquid(inliquid);
			
			if(getOnFire() != 0){
				deathtimer -= 5;
			}

			deathtimer--;
			if(deathtimer <= 0){
				this.deadflag = true;
				return;
			}
			
			if(pickup_delay > 0)pickup_delay--;
			
			if(!this.deadflag && pickup_delay <= 0){
				Player p = DangerZone.server.findNearestPlayer(this);
				if(p != null && !p.deadflag && (p.getGameMode() != GameModes.LIMBO)){
					if(this.getDistanceFromEntity(p) < (p.getWidth()/2)+5){
						if(this.getDistanceFromEntity(p) < p.getWidth()*3/2){
							boolean doplaysound = false;
							ic = getInventory(0);
							if(ic == null){
								this.deadflag = true;	
								return;
							}
							while(ic.count > 0){
								if(!p.putMeInASlot(new InventoryContainer(ic.bid, ic.iid, 1, ic.currentuses, ic.count==1?ic.attributes:null, ic.icmeta, ic.moreInventory))){
									doplaysound = false;
									break;
								}else{
									doplaysound = true;
								}
								ic.count--;
							}
							if(doplaysound){
								if(ic.count == 0)this.deadflag = true;							
								world.playSound("DangerZone:pop", dimension, posx, posy, posz, 0.25f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.3f);
							}
						}
												
					   	float tdir = (float) Math.atan2(p.posx - posx, p.posz - posz);
				    	double dist;
				    	double factor = 1.0f;
				    	dist = this.getDistanceFromEntity(p);
				    	factor = ((p.getWidth()/2)+5.0f)/dist;
				    	if(factor > 2)factor = 2; 
				    	motionx += 0.12f*factor*Math.sin(tdir) * deltaT;
				    	motionz += 0.12f*factor*Math.cos(tdir) * deltaT;
				    	if(posy>p.posy)motiony -= 0.05f * deltaT;
				    	if(posy<p.posy)motiony += 0.20f * deltaT;
					}
				}
			}
			
			//if undamaged, and getting to be a lot of entities, try combining once...
			if(!deadflag && !tried && this.getItemDamage() == 0 && DangerZone.server.entityManager.active_entities > DangerZone.max_entities/4){
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
							if(e != this && e instanceof EntityBlockItem && !e.deadflag){
								ic = e.getInventory(0);
								InventoryContainer myic = getInventory(0);
								if(ic != null && myic != null){
									if(ic.iid == myic.iid && ic.bid == myic.bid){
										if(ic.currentuses == 0 && myic.currentuses == 0){
											if(ic.attributes == null && myic.attributes == null && ic.icmeta == null && myic.icmeta == null && ic.moreInventory == null && myic.moreInventory == null){
												EntityBlockItem eb = (EntityBlockItem)e;												
												myic.count += ic.count; //update count!
												eb.setInventory(0, myic); //give him mine
												eb.sethotbarindex(1); //blink!!!
												if(eb.deathtimer < deathtimer)eb.deathtimer = deathtimer; //take max or two!
												//eb.deathtimer = 10 * 120;
												this.deadflag = true;
												break;	
											}
										}
									}
								}
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
			}else{
				motionx *= 0.99f;
				motiony *= 0.99f;
				motionz *= 0.99f;
			}

		}else{
			//float rate = DangerZone.entityupdaterate;
			//rate /= DangerZone.serverentityupdaterate;
			//int bbid = this.world.getblock(dimension, (int)this.posx, (int)(this.posy-(this.getHeight())), (int)this.posz);
			//if(Blocks.isSolid(bbid)){				
			//	motiony += 0.25f * rate * deltaT;
			//}else{
			//	motiony -= 0.05f * rate * deltaT;
			//}
		}
		super.update( deltaT );
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
		 if(gethotbarindex() <= 0)return 0;
		 //System.out.printf("%d blinking %s\n", gethotbarindex(), uniquename);
		 if((lifetimeticker%30) < 15)return -0.1f;
		 return 0.5f;
	 }
	
}
