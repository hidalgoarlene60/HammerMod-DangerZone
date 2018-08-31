package dangerzone.entities;



import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;

import dangerzone.entities.ThrownBlockItem;
import dangerzone.items.Items;

public class EntityMagic extends ThrownBlockItem {
	
	public float spin = 0;
	public double oposx, oposy, oposz;
	
	public Texture texture_black;
	public Texture texture_white;
	public Texture texture_orange;
	public Texture texture_yellow;
	public Texture texture_blue;
	public Texture texture_purple;
	public Texture texture_green;
	public Texture texture_red;

	
	public int mycolor = 0;
	List<Entity> enchanted_list = null;
	public int spawnmore = 4;
	
	public EntityMagic(World w) {
		super(w);
		setAttackDamage(100.0f);
		uniquename = "DangerZone:EntityMagic";
		maxrenderdist = 200; //in blocks
		oposx = 0;
		oposy = 0;
		oposz = 0;
		deathtimer = 15; // 1.5 seconds
		if(w != null)mycolor = w.rand.nextInt(8);
		enchanted_list = new ArrayList<Entity>();
	}
	
	public float getWidth(){
		return getScale();
	}
	
	public float getHeight(){
		return getScale();
	}
	
	public void doEntityAction( float deltaT){

		if(getBID() != 0){ //master
			if(oposx == 0 && oposy == 0 && oposz == 0){
				oposx = posx;
				oposy = posy;
				oposz = posz;				
				world.playSound("DangerZone:magic1", dimension, posx, posy, posz, 0.5f, 1);
				rotation_yaw = (float)(Math.atan2(motionx, motionz) * 180d / Math.PI);
				rotation_pitch = 0;
				rotation_roll = 0;
			}
			deathtimer--;
			if(deathtimer <= 0){
				this.deadflag = true;
				return;
			}

			doFindThings(deltaT, getIID()>0?true:false);
			
			if(spawnmore > 0){
				spawnmore--;
				EntityMagic e = (EntityMagic)this.world.createEntityByName("DangerZone:EntityMagic", dimension, oposx, oposy, oposz);
				if(e != null){
					e.init();
					e.setBID(0);
					e.setIID(getIID());				//so it can know if it is normal scroll or not...
					e.setVarInt(3, getVarInt(3));	//harm or heal for now...
					e.setVarInt(2, getVarInt(2));
					e.thrower = this;					
					e.motionx = motionx + (world.rand.nextFloat()-world.rand.nextFloat())*0.15f;
					e.motiony = motiony + (world.rand.nextFloat()-world.rand.nextFloat())*0.15f;
					e.motionz = motionz + (world.rand.nextFloat()-world.rand.nextFloat())*0.15f;				
					this.world.spawnEntityInWorld(e);
				}
			}

		}else{

			doFindThings(deltaT, getIID()>0?true:false);

			deathtimer--;
			if(deathtimer <= 0){
				this.deadflag = true;
				//System.out.printf("max size = %f\n",  getWidth());
			}
		}

	}
	
	public void doFindThings(float deltaT, boolean enchant){
		//Check for hitting a block...
		float dist = (float)Math.sqrt(motionx*deltaT*motionx*deltaT + motiony*deltaT*motiony*deltaT + motionz*deltaT*motionz*deltaT);
		float blockdist = 0;
		float edist = 0;
		List<Entity> hit_list = null;
		List<Entity> nearby_list = null;
		ListIterator<Entity> li;
		ListIterator<Entity> hli;
		Entity enthit = null;
		Entity henthit = null;
		double fx, fy, fz;
		boolean doit = true;

		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange(16.0f+dist, dimension, posx, posy, posz);
		hit_list = new ArrayList<Entity>();

		fx = posx;
		fy = posy;
		fz = posz;


		/*
		 * increment along in the direction we are heading to see if we hit something.
		 * We do this, because we can move more (a lot!) than one block distance in a clock tick!
		 */
		while(dist > 0 && blockdist < dist){
			fx = posx + motionx*deltaT*blockdist/dist;
			fy = posy + motiony*deltaT*blockdist/dist;
			fz = posz + motionz*deltaT*blockdist/dist;
			
			if(getIID() < 0 && getVarInt(3) == 2){ //DESTROY SPELL!
				breakSomeBlocks((int)fx, (int)fy, (int)fz);
			}else{
				li = nearby_list.listIterator();
				while(li.hasNext()){
					enthit = (Entity)li.next();
					if(enthit instanceof EntityBlockItem || (getIID() < 0 && enthit instanceof EntityLiving && enthit != thrower)){
						if(fy > enthit.posy-(getWidth()/2)  && fy < enthit.posy+enthit.getHeight()+(getWidth()/2) ){
							edist = (float) enthit.getHorizontalDistanceFromEntity(fx, fz); //Center of tip to center of entity
							edist -= getWidth()/2; //width of me
							edist -= (enthit.getWidth()/2); //general hitbox of entity
							if(edist < 0){
								//Hit something!
								doit = true;
								hli = hit_list.listIterator();
								while(hli.hasNext()){
									henthit = (Entity)hli.next();
									if(henthit == enthit){
										doit = false;
										break;
									}
								}

								if(doit){
									if(enchant){
										if(enthit instanceof EntityBlockItem)doEnchantSomething(false , fx, fy, fz, true, enthit);
									}else{
										if(enthit instanceof EntityLiving)doHitSomething(false , fx, fy, fz, true, enthit);
									}
									hit_list.add(enthit);
								}
							}
						}
					}

				}
			}

			doSpecialEffects(fx, fy, fz);

			blockdist += 0.25f;
		}
	}
	
	//override for when we hit something
	public void doHitSomething(boolean hb, double x, double y, double z, boolean he, Entity ent){
		Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSparkle", 5, ent.dimension, ent.posx, ent.posy, ent.posz);			
		if(getVarInt(3)==1){
			Utils.spawnParticlesFromServer(world, "DangerZone:ParticleHappy", 10, ent.dimension, ent.posx, ent.posy, ent.posz);	
			if(getVarInt(2) <= 0){
				ent.heal(ent.getMaxHealth()/4); //very powerful healing spell!
			}else{
				ent.heal(getVarInt(2));
			}		
		}
		if(getVarInt(3)==0){
			Utils.spawnParticlesFromServer(world, "DangerZone:ParticleDeath", 10, ent.dimension, ent.posx, ent.posy, ent.posz);	
			float eh = ent.getHealth()/8;
			if(eh < 20)eh = 20;
			if(getVarInt(2) > 0){
				eh = getVarInt(2);
			}
			ent.doAttackFromCustom(thrower, DamageTypes.MAGIC, eh, false);
		}
	}
	
	public void doEnchantSomething(boolean hb, double x, double y, double z, boolean he, Entity ent){
		Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSparkle", 20, ent.dimension, ent.posx, ent.posy, ent.posz);	
		InventoryContainer ic = ent.getInventory(0); //fetch what it is carrying
		if(getBID() != 0 && ic.bid == 0 && ic.iid != 0){
			if(Items.getMaxStack(ic.iid) == 1){
				//only once!
				ListIterator<Entity> hli;
				Entity henthit = null;
				hli = enchanted_list.listIterator();
				while(hli.hasNext()){
					henthit = (Entity)hli.next();
					if(henthit == ent){
						return;
					}
				}
				ic.addAttribute(getIID(), getVarInt(2)); //increment attribute!				
				world.playSound("DangerZone:magic2", dimension, posx, posy, posz, 1, 1);				
			}
		}

	}
	
	public float getDensity(){
		float d = 1.5f/getScale();
		if(d > 1)d = 1;
		return d;
	}
	
	public float getSpinz(){
		spin += 5;
		return spin;
	}
	
	public float getScale(){
		if(oposx == 0){
			oposx = posx;
			oposy = posy;
			oposz = posz;
		}
		double d1, d2, d3;
		d1 = oposx - this.posx;
		d2 = oposy - this.posy;
		d3 = oposz - this.posz;
		float scale = (float) Math.sqrt((d1*d1)+(d2*d2)+(d3*d3));
		if(scale < 2)scale = 2;
		if(getIID() < 0)return scale/10;
		return scale/20;
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		
		if(getIID() < 0){ //special 
			if(getVarInt(3) == 0){
				mycolor = 7;
			}else if(getVarInt(3) == 1){
				mycolor = 6;
			}else{
				mycolor = 0;
			}
		}
		
		if(mycolor == 0){
			if(texture_black == null){
				texture_black = TextureMapper.getTexture("res/skins/magic_black.png");
			}
			return texture_black;
		}
		if(mycolor == 1){
			if(texture_white == null){
				texture_white = TextureMapper.getTexture("res/skins/magic_white.png");
			}
			return texture_white;
		}
		if(mycolor == 2){
			if(texture_orange == null){
				texture_orange = TextureMapper.getTexture("res/skins/magic_orange.png");
			}
			return texture_orange;
		}
		if(mycolor == 3){
			if(texture_yellow == null){
				texture_yellow = TextureMapper.getTexture("res/skins/magic_yellow.png");
			}
			return texture_yellow;
		}
		if(mycolor == 4){
			if(texture_blue == null){
				texture_blue = TextureMapper.getTexture("res/skins/magic_blue.png");
			}
			return texture_blue;
		}
		if(mycolor == 5){
			if(texture_purple == null){
				texture_purple = TextureMapper.getTexture("res/skins/magic_purple.png");
			}
			return texture_purple;
		}
		if(mycolor == 6){
			if(texture_green == null){
				texture_green = TextureMapper.getTexture("res/skins/magic_green.png");
			}
			return texture_green;
		}
		if(mycolor == 7){
			if(texture_red == null){
				texture_red = TextureMapper.getTexture("res/skins/magic_red.png");
			}
			return texture_red;
		}
		return null;
	}
	
	public void breakSomeBlocks(int fx, int fy, int fz){
		int currentwidth = (int)getWidth();
		int cury = fy;
		int curx = fx;
		int curz = fz;
		
		if(currentwidth < 1)currentwidth = 1;

		int bid = 0;
		int da = 10;
		float currad = 0;
		float ty, tt, tx, tz;
		if(currentwidth < 4)da = 15;
		if(currentwidth > 6)da = 5;

		float dirang = (float) Math.toRadians(this.rotation_yaw-90);
		float dirax = (float) Math.cos(dirang);
		float diraz = (float) Math.sin(dirang);
		boolean canbreak = true;
		int breakval = getVarInt(2)*5; //works out to 20K exp needed to break stone!
		//System.out.printf("breakval = %d\n", breakval);
		
		if(!DangerZone.playnicely && !DangerZone.freeze_world){
			for(int j=0;j<360 && (canbreak);j+=da){
				currad = 0;				
				ty = (float) Math.sin(Math.toRadians(j));
				tt = (float) Math.cos(Math.toRadians(j));
				tx = (float) (dirax*tt);
				tz = (float) (diraz*tt);				
				while(currad < currentwidth){
					currad += 0.20f;
					if((int)(cury + ty*currad) >= 0){
						bid = world.getblock(dimension, (int)(curx + tx*currad), (int)(cury + ty*currad), (int)(curz + tz*currad));
						if(bid != 0){
							if(bid == Blocks.stopblock.blockID){
								canbreak = false;
								break;
							}
							if(breakval >= Blocks.getMaxDamage(bid)){
								canbreak = world.setblocknonotifyWithPerm(this, dimension, (int)(curx + tx*currad), (int)(cury + ty*currad), (int)(curz + tz*currad), 0);
								if(!canbreak)break;
								if(world.rand.nextInt(10) == 1){
									//Utils.doDropRand(world, bid, 0, 1, dimension, (int)(curx + tx*currad), (int)(cury + ty*currad), (int)(curz + tz*currad));
									EntityBlockItem e = (EntityBlockItem)world.createEntityByName(DangerZone.blockitemname, dimension, (int)(curx + tx*currad), (int)(cury + ty*currad), (int)(curz + tz*currad));
									if(e != null){
										e.init();
										e.fill(bid, 0, 1);
										e.rotation_pitch = world.rand.nextInt(360);
										e.rotation_yaw = world.rand.nextInt(360);
										e.rotation_roll = world.rand.nextInt(360);
										e.motionx = (world.rand.nextFloat()-world.rand.nextFloat()); 
										e.motiony = world.rand.nextFloat()/4;
										e.motionz = (world.rand.nextFloat()-world.rand.nextFloat());
										e.deathtimer = 10 * 20; //20 seconds! Fast!
										world.spawnEntityInWorld(e);
									}			
								}
							}
						}
					}else{
						canbreak = false;
						break;
					}
				}				
			}
		}
	}

}
