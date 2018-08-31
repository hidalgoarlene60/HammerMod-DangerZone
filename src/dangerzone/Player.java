package dangerzone;
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


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Cockroach;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;
import dangerzone.entities.EntityFurnace;
import dangerzone.entities.EntityLiving;
import dangerzone.entities.EntityMagic;
import dangerzone.items.Item;
import dangerzone.items.ItemArmor;
import dangerzone.items.ItemAxe;
import dangerzone.items.ItemFood;
import dangerzone.items.ItemPickAxe;
import dangerzone.items.ItemShovel;
import dangerzone.items.ItemSword;
import dangerzone.items.Items;
import dangerzone.threads.LightingThread;
import dangerzone.threads.ServerConnection;
import dangerzone.threads.ServerThread;



public class Player extends EntityLiving {
	public Socket toServer;
	public Socket toClient;
	public ServerThread server_thread;			//Handy reference for packets that only go to specific player (on server)
	public ServerConnection server_connection;  //Same as DangerZone.server_connection (on client)
	public byte[] tdata = null;
	public boolean donewtexture = false;
	public String myname = null;
	public int starthealth = 50;
	public int swimdelay = 0;
	public int lightupdatecounter = 0;
	public int inventoryticker = 0;
	public int player_privs = 0;
	public Entity morph = null;	
	public Entity morphto = null;	
	public boolean do_morph;
	public float morphspeed = -0.01f;
	public float morphscale = 1.0f;
	public int clienthotbarindex = 0;
	public int home_dimension;
	public double home_x;
	public double home_y;
	public double home_z;
	private List<Integer>spread_list = null; //GUI
	private boolean wasOnGround = true;
	public AltHandleInventory alt_inv_handler = null;
	public int lasttrd;
	public double lasttrx, lasttry, lasttrz;
	
	//STATISTICS! Yeah... WE ARE WATCHING YOU! :)
	public int kills = 0;
	public int deaths = 0;
	public double damage_taken = 0.0d;
	public double damage_dealt = 0.0d;
	public int blocks_broken = 0;
	public int blocks_placed = 0;
	public int crafted = 0;
	public int bought = 0;
	public int sold = 0;
	public int broken = 0; //tools!
	public int traveled = 0;
	public int morphs = 0;
	public int teleports = 0;
	public int eaten = 0;
	public int roachstomps = 0;
	public int hard_landings = 0;
	public int flights = 0;
	public int blocks_colored = 0;
	public int spells = 0;
	public int lastlevel = 0;

	
	public Player(World w){
		super(w);
		width = 0.65f;
		height = 1.75f;	
		eyeheight = 1.65f;
		uniquename = "DangerZone:Player";		
		has_inventory = true;
		setMaxHealth(starthealth);
		setHealth(starthealth);
		setDefense(1);
		setMaxHunger(50);
		setHunger(50);
		setAttackDamage(1);
		setMaxAir(50);
		setAir(50);
		setCanDespawn(false);
		do_morph = false;
	};
	
	public void init(){
		super.init();
		eyeheight = 0.943f*getHeight();
		setSitting(false);
		morphto = morph = null;
		do_morph = false;
		lasttrd = -1;
		lasttrx = lasttry = lasttrz = 0;
	}
	
	public void doDeathDrops(){
		if(!DangerZone.keep_inventory_on_death){
			super.doDeathDrops();
		}
	}
		
	public void doEntityAction(float deltaT){	
		if(dimension != lasttrd || (int)posx != lasttrx || (int)posy != lasttry || (int)posz != lasttrz ){
			if(lasttrd != -1 && lasttrd != dimension){
				teleports++;
				server_thread.sendStatsToPlayer();
				ToDoList.onDimension(this, lasttrd, dimension);
			}
			lasttrd = dimension;
			lasttrx = (int)posx;
			lasttry = (int)posy;
			lasttrz = (int)posz;
			traveled++;
			if((traveled&0x0f) == 0)server_thread.sendStatsToPlayer(); //don't send for every block!
		}
		
		if(getExperience()/1000 != lastlevel){
			lastlevel = getExperience()/1000;
			setMaxMagic(lastlevel);
			ToDoList.onLeveled(this, getExperience());
		}
		if(getMaxMagic() > 0){
			float remagic = getMaxMagic() - getMagic();
			if(remagic > getMaxMagic()*0.005f)remagic = getMaxMagic()*0.005f;
			if(remagic > 0)setMagic(getMagic()+remagic);
		}
		//super.doEntityAction(deltaT); //NO! NO! NO! DO NOT CALL THIS!!!
	}
	
	public String getHurtSound(){
		if(morph != null)return morph.getHurtSound();
		int i = world.rand.nextInt(3);
		if(i == 0)return "DangerZone:ouch1";
		if(i == 1)return "DangerZone:ouch2";
		return "DangerZone:ouch3";		
	}
	
	public String getLivingSound(){
		if(morph != null)return morph.getLivingSound();
		return null;
	}
	
	public float getScale(){
		if(do_morph){
			return morphscale;
		}
		return super.getScale();
	}
	
	
	public float getRightArmAngle(){
		return -armangle;
	}
	
	public void update(float deltaT){
		
		if(!world.isServer && this == DangerZone.player)stray_entity_ticker = 0; //Let's make sure we don't get removed just because we don't get updates for ourselves!!!
		
		if(morph != null){
			//we need this stuff for distance attacks and such... might as well do both client and server...
			morph.posx = posx;
			morph.posy = posy;
			morph.posz = posz;
			morph.rotation_yaw = rotation_yaw;
			morph.rotation_pitch = rotation_pitch;
			morph.rotation_roll = rotation_roll;
			morph.rotation_yaw_head = rotation_yaw_head;
			morph.rotation_pitch_head = rotation_pitch_head;
			morph.rotation_roll_head = rotation_roll_head;
			if(DangerZone.player == this){
				morph.display_rotation_yaw = -display_rotation_yaw+180f; //cuz player-view is kind of backwards!!!
			}else{
				morph.display_rotation_yaw = display_rotation_yaw;
			}
			morph.display_rotation_pitch = display_rotation_pitch;
			morph.display_rotation_roll = display_rotation_roll;
			
			morph.player_morph_update(deltaT, this);
		}
		
		//update what we are mounted on first!
		if(!world.isServer){
			//sync morph with the server side through player...
			if(morph != null){
				morph.setFlying(isFlying());
				morph.setBaby(isBaby());
				morph.setAttacking(getAttacking());
				morph.setOnFire(getOnFire());
				morph.setSitting(getSitting());
			}
			
		}else{
			if(getGameMode() != GameModes.GHOST && getGameMode() != GameModes.LIMBO && isSolidAtLevel(dimension, posx, posy-0.02f, posz)){
				if(isFlying()){
					setFlying(false);
					Utils.spawnParticlesFromServer(this.world, "DangerZone:ParticleBreak", 50, this.dimension, posx, posy, posz, world.getblock(dimension, (int)posx, (int)(posy-0.2f), (int)posz));
					this.world.playSound(getHurtSound(), dimension, posx, posy+getHeight()/2, posz, 0.25f, 1);
				}

			}

			if(morph != null){
				if(this.world.rand.nextInt(600) == 1){			
					this.world.playSound(getLivingSound(), dimension, posx, posy+getHeight()/2, posz, getLivingSoundVolume(), getLivingSoundPitch());	
				}
			}		
		}
		
		//morph check!
		//this gets done on BOTH client and server
		String morphname = getMorphName();
		if(!do_morph){
			if(morph == null){	
				if(morphname != null){
					Entity ent = world.createEntityByName(morphname, dimension, posx, posy, posz);
					if(ent != null){
						ent.init();
						//looks good so far!
						morphto = ent;
						morphspeed = -0.01f;
						do_morph = true;
						if(!world.isServer)world.playSound("DangerZone:morph1", dimension, posx, posy, posz, 0.25f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
					}
				}
			}else{
				if(!morph.uniquename.equals(morphname)){
					if(morphname == null){
						morphto = this;
						morphspeed = -0.01f;
						do_morph = true;
						if(!world.isServer)world.playSound("DangerZone:morph1", dimension, posx, posy, posz, 0.25f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
						setMorphName(null);
					}else{
						if(morphname.equals(this.uniquename)){
							morphto = this;
							morphspeed = -0.01f;
							do_morph = true;
							if(!world.isServer)world.playSound("DangerZone:morph1", dimension, posx, posy, posz, 0.25f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
							setMorphName(null);
						}else{
							Entity ent = world.createEntityByName(morphname, dimension, posx, posy, posz);
							if(ent != null){
								ent.init();
								//looks good so far!
								morphto = ent;
								morphspeed = -0.01f;
								do_morph = true;
								if(!world.isServer)world.playSound("DangerZone:morph1", dimension, posx, posy, posz, 0.25f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
							}
						}
					}				
				}
				//show what we are holding if model supports it!
				if(morph.has_inventory){
					morph.sethotbarindex(gethotbarindex());
					morph.setHotbar(gethotbarindex(), getHotbar(gethotbarindex()));
				}
			}
		}
		
		if(do_morph){
			morphscale += morphspeed;
			
			if(morphscale < 0.01f){
				morphscale = 0.01f;
				morphspeed = 0.01f;
				morph = morphto;
				model = morphto.model;
				if(morph == this){
					morph = null;
					model = DangerZoneBase.modelhumanoid;
				}
				if(!world.isServer)world.playSound("DangerZone:morph2", dimension, posx, posy, posz, 0.25f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			}			

			if(morphscale >= 1){
				morphscale = 1;
				morphspeed = -0.01f;
				do_morph = false;
				if(world.isServer){
					morphs++;
					server_thread.sendStatsToPlayer();
				}
			}
			
			if(!world.isServer){
				Utils.spawnParticles(world, "DangerZone:ParticleSparkle", 5, dimension, 
						posx + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
						posy+world.rand.nextFloat()*getHeight(), 
						posz + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
						true);
				Utils.spawnParticles(world, "DangerZone:ParticleSmoke", 5, dimension, 
						posx + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
						posy+world.rand.nextFloat()*getHeight(), 
						posz + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
						true);
				Utils.spawnParticles(world, "DangerZone:ParticleFire", 5, dimension, 
						posx + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
						posy+world.rand.nextFloat()*getHeight(), 
						posz + (world.rand.nextFloat()-world.rand.nextFloat())*getWidth()/2, 
						true);
			}
			
		}
		
		//people whining about player motion too squishy.... sigh... fine... unsquish it a bit...
		if(!getForward() && !getBackward() && !getLeft() && !getRight() && getGameMode() != GameModes.GHOST && getGameMode() != GameModes.LIMBO ){
			if(world.isServer){
				motionx *= (1.0f-(0.35f*deltaT));
				motionz *= (1.0f-(0.35f*deltaT));
			}else{
				float rate = DangerZone.entityupdaterate;
				rate /= DangerZone.serverentityupdaterate;
				motionx *= (1.0f-(0.35f*deltaT*rate));
				motionz *= (1.0f-(0.35f*deltaT*rate));	
			}
		}
			
		if(getSitting()){
			eyeheight = 0.586f*getHeight();
		}else{
			eyeheight = 0.943f*getHeight();
		}
	
		
		if(!world.isServer){
						
			InventoryContainer ic = null;
			
			/*
			 * If player is carrying something that makes light...
			 * Send request to Lighting thread.
			 */
			lightupdatecounter++;
			if(lightupdatecounter > 5){
				ic = getHotbar(gethotbarindex());
				if(ic != null){
					float lvl = 0;			
					if(ic.bid != 0){
						lvl = Blocks.getLightLevel(ic.bid, world, dimension, (int)posx, (int)(posy+1.25f), (int)posz);
					}
					if(ic.iid != 0){
						lvl = Items.getLightLevel(ic.iid);
					}
					if(lvl != 0){
						//LightingThread.updateLightMaps(world, lvl, dimension, (int)posx, (int)(posy+1.25f), (int)posz);
						LightingThread.addRequest(dimension, (int)posx, (int)(posy+1.25f), (int)posz, lvl);
					}
				}
				lightupdatecounter = 0;
			}		
		}else{
			
			if(morph == null){
				setMaxHealth(starthealth+(getExperience()/1000));
				setDefense(1.0f + ((float)getExperience()/10000f));
				setAttackDamage(1.0f + ((float)getExperience()/10000f));
			}
			if(morph != null){
				//System.out.printf("server morph !null\n");
				if(getLeft() || getRight()){
					//System.out.printf("server morph dist att\n");
					double px, py, pz;					
					px = (float)Math.sin(Math.toRadians(rotation_yaw_head))*(float)Math.cos(Math.toRadians(rotation_pitch_head));
					py = (float)Math.sin(Math.toRadians(rotation_pitch_head));
					pz = (float)Math.cos(Math.toRadians(rotation_yaw_head))*(float)Math.cos(Math.toRadians(rotation_pitch_head));
					//make a temporary entity to stuff the coordinates into....
					Entity tempent = world.createEntityByName("DangerZone:Cockroach", dimension, (px*32)+posx, (py*32)+posy, (pz*32)+posz);
					if(tempent != null){
						morph.doDistanceAttack(tempent);
					}
					//don't actually spawn it!
					//this will work for thrown objects pretty well. Not so much for anything that is actually spawned at their feet!
				}
			}
			if(getGameMode() == GameModes.SURVIVAL){
				//use up some food
				if(getHunger() > 0){
					float hunger = 0.0011f;
					int diffi = getGameDifficulty();
					if(diffi == -1)hunger = 0.0005f;
					if(diffi == -2)hunger = 0.0001f;
					if(diffi == 1)hunger = 0.003f;
					if(diffi == 2)hunger = 0.006f;
					setHunger(getHunger()-hunger); //slower than health heal!!!
				}
				//regenerate a bit if not hungry
				if(getHealth() >= 0 && getHealth() < getMaxHealth() && getHunger() > getMaxHunger()/2){
					float regen = 0.0006f;
					int diffi = getGameDifficulty();
					if(diffi == -1)regen = 0.001f;
					if(diffi == -2)regen = 0.003f;
					if(diffi == 1)regen = 0.0004f;
					if(diffi == 2)regen = 0.0002f;
					setHealth(getHealth()+(getMaxHealth()*regen));
				}
				//uh oh. starving to death!
				if(getHunger() <= 0){
					if(world.rand.nextInt(50) == 1)doAttackFrom(null, DamageTypes.HUNGER, 0.1f);
				}
			}
		}

		
		super.update(deltaT);
		
		if(world.isServer){
			if(getOnGround()){
				if(!wasOnGround){			
					int bid = world.getblock(dimension, (int)posx, (int)(posy-0.2f), (int)posz);
					//if just came down on the ground...
					Utils.spawnParticlesFromServer(this.world, "DangerZone:ParticleBreak", 50, this.dimension, posx, posy, posz, bid);
					Blocks.doblocktick(this.world, dimension, (int)posx, (int)(posy-0.2f), (int)posz, bid);
					//see if we stomped on a cockroach!
					Entity roach = steppedOnCockroach();
					if(roach != null){
						if(roach.getBID() != 0){ //forward through dimensions
							for(int i=0;i<Dimensions.dimensionsMAX;i++){
								int id = (this.dimension+i+1)%Dimensions.dimensionsMAX;
								if(Dimensions.DimensionArray[id] != null){
									if(!Dimensions.DimensionArray[id].special_hidden){
										world.playSound("DangerZone:big_splat", dimension, posx, posy, posz, 0.25f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.2f);
										Utils.spawnParticlesFromServer(this.world, "DangerZone:ParticleHurt", 10, this.dimension, posx, posy, posz);
										Utils.doTeleport(this, id, (int)this.posx, (int)this.posy, (int)this.posz);									
										world.playSound("DangerZone:big_splat", id, posx, posy, posz, 0.25f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.2f);
										Utils.spawnParticlesFromServer(this.world, "DangerZone:ParticleHurt", 10, id, posx, posy, posz);
										break;
									}
								}
							}
						}else{ //backwards through dimensions
							for(int i=0;i<Dimensions.dimensionsMAX;i++){
								int id = (this.dimension-i-1)%Dimensions.dimensionsMAX;
								while(id<0)id += Dimensions.dimensionsMAX;
								if(Dimensions.DimensionArray[id] != null){
									if(!Dimensions.DimensionArray[id].special_hidden){
										world.playSound("DangerZone:big_splat", dimension, posx, posy, posz, 0.25f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.2f);
										Utils.spawnParticlesFromServer(this.world, "DangerZone:ParticleHurt", 10, this.dimension, posx, posy, posz);
										Utils.doTeleport(this, id, (int)this.posx, (int)this.posy, (int)this.posz);									
										world.playSound("DangerZone:big_splat", id, posx, posy, posz, 0.25f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.2f);
										Utils.spawnParticlesFromServer(this.world, "DangerZone:ParticleHurt", 10, id, posx, posy, posz);
										break;
									}
								}
							}
						}
						roachstomps++;
						server_thread.sendStatsToPlayer();
					}else{
						world.playSound(Blocks.getStepSound(bid), this.dimension, posx, posy, posz, 0.25f, 1.0f + ((DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*0.2f));
					}
				}
				wasOnGround = true;
			}else{
				wasOnGround = false;
			}
		}
	}
	
	public void tryfly(){
		if(morph != null && morph.canFly && !isFlying()){
			setFlying(true);
			posy += 0.05f; //go!
			motiony += 0.25f;				
		}
	}
	
	public void jump(){	

		if(getRiddenEntity() == null)setSitting(false);
		
		if(getInLiquid()){
			if(Blocks.isLiquid(world.getblock(dimension, (int)posx, (int)(posy+1.5f),(int)posz))){
				swimdelay++;
				if(swimdelay > 40)swimdelay = 0;
				if(swimdelay > 15){
					motiony += 0.035f;
					if(isSolidAtLevel(dimension, posx, posy-0.35f, posz))motiony += 0.55f;
				}				
			}
		}
						
		if(!isSolidAtLevel(dimension, posx, posy-0.05f, posz)){
			return;
		}
		
		if(Math.abs(motiony)>0.08f){
			return;
		}
		
		float jumpfactor = 0.80f + (getHeight())/8.0f;
		float jumpadjust = getTotalEffect(Effects.STRENGTH);
		if(jumpadjust != 0)jumpfactor += jumpfactor*jumpadjust/4;
		jumpadjust = getTotalEffect(Effects.WEAKNESS);
		if(jumpadjust != 0)jumpfactor /= jumpadjust;
		
		if(isBaby()){
			if(world.getblock(dimension, (int)posx, (int)posy+1, (int)posz) != 0){
				motiony += 0.75f;	
			}else{
				motiony += jumpfactor*0.95f;	
			}
		}else{
			if(world.getblock(dimension, (int)posx, (int)posy+2, (int)posz) != 0 || world.getblock(dimension, (int)posx, (int)posy+3, (int)posz) != 0){
				motiony += 0.75f;	
			}else{
				motiony += jumpfactor*0.95f;	
			}
		}
		//System.out.printf("Jump! posy, moy == %f, %f\n", posy, motiony);
	}
	
	public void incCurrentUses(InventoryContainer ic){
		if(ic == null)return;
		if(Items.getMaxStack(ic.iid) == 1){
			int foo = ic.getAttribute(ItemAttribute.DURABILITY);
			int damageodds = 1+(foo*2);
			if(damageodds <= 1){
				ic.currentuses++;
			}else{
				if(world.rand.nextInt(damageodds) == 1)ic.currentuses++;
			}
			setHotbarChanged(gethotbarindex());
			if(ic.currentuses >= Items.getMaxUses(ic.iid)){
				if(world.rand.nextInt(2)==1){
					world.playSound("DangerZone:toolbreak1", dimension, posx, posy, posz, 0.45f, 1.0f);		
				}else{
					world.playSound("DangerZone:toolbreak2", dimension, posx, posy, posz, 0.45f, 1.0f);
				}
				setHotbar(gethotbarindex(), null);
				broken++;
				server_thread.sendStatsToPlayer();
			}
		}
	}
	
	public void leftclick(World world, int focus_x, int focus_y, int focus_z, int focus_side, int eid, float mp, int mt){
		Entity e = null;
		
		if(getGameMode() == GameModes.LIMBO)return;
		
		if(world.isServer){
			armdir = 1;
			setAttacking(true);
			//System.out.printf("server left eid = %d\n", eid);
			InventoryContainer ic = getHotbar(gethotbarindex());
			e = DangerZone.server.entityManager.findEntityByID(eid);
			boolean leftcontinue = true;
			if(ic != null){
				Item it = ic.getItem();
				if(it != null)leftcontinue = it.onLeftClick(this, e, ic);
				Block bl = ic.getBlock();
				if(bl != null)leftcontinue = bl.onLeftClick(this, e, ic);
			}
			if(leftcontinue){
				if(e != null){
					int dt = DamageTypes.GENERIC;
					float damage = getAttackDamage();				
					if(ic != null){
						if(ic.bid != 0){
							dt = DamageTypes.BLOCK;
						}else{
							if(ic.iid > 0 && ic.iid < Items.itemsMAX){
								damage += Items.getAttackStrength(ic.iid);
								Item it = ic.getItem();
								if(it != null){
									if(it instanceof ItemSword)dt = DamageTypes.SWORD;
									if(it instanceof ItemPickAxe)dt = DamageTypes.TOOL;
									if(it instanceof ItemAxe)dt = DamageTypes.TOOL;
									if(it instanceof ItemShovel)dt = DamageTypes.TOOL;
								}
							}
							float damageadjust = (ic.getAttribute(ItemAttribute.DAMAGE)*0.5f) + 1;
							damage *= damageadjust;
						}
					}
					if(e.leftClickedByPlayer(this, ic)){
						if(e instanceof EntityLiving && ic != null){
							EntityLiving el = (EntityLiving)e;
							int foo = ic.getAttribute(ItemAttribute.SPAM);
							el.damage_backoff -= foo;
						}
						e.doAttackFrom(this, dt, damage);	
					}
					if(getHunger() > 0 && getGameMode() == GameModes.SURVIVAL)setHunger(getHunger()-0.025f); //hitting things makes us hungry!
					if(this.getGameMode() == GameModes.SURVIVAL){
						incCurrentUses(ic);	
					}					
				}else{
					if(focus_x > 0 && focus_y > 0 && focus_z > 0){	
						if(getHunger() > 0 && getGameMode() == GameModes.SURVIVAL)setHunger(getHunger()-0.025f); //hitting things makes us hungry!
						int bid = world.getblock(dimension, focus_x, focus_y, focus_z);
						if(bid > 0){
							Blocks.leftClickOnBlock(bid, this, dimension, focus_x, focus_y, focus_z);
							if(this.getGameMode() == GameModes.SURVIVAL){
								incCurrentUses(ic);	
							}
						}
					}else{
						if(ic == null && mp > 0 && mt > 0){
							if(mt != 0){
								make_magic(mp, mt);
								ToDoList.onSpellCast(this, mt, mp);
							}
						}
					}
				}
			}
		}else{
			
			//e = findHitEntity(true);
			//System.out.printf("client left eid = %d\n", eid);
			e = DangerZone.entityManager.findEntityByID(eid);
			if(e == null){
				server_connection.playerActionToServer(0, 0, 0, focus_x, focus_y, focus_z, focus_side, mp, mt); //0 = left mouse click	
			}else{
				server_connection.playerActionToServer(0, 0, e.entityID, focus_x, focus_y, focus_z, focus_side, mp, mt); //0 = left mouse click	
			}
			InventoryContainer ic = getHotbar(gethotbarindex());
			boolean leftcontinue = true;
			if(ic != null){
				Item it = ic.getItem();
				if(it != null)leftcontinue = it.onLeftClick(this, e, ic);
				Block bl = ic.getBlock();
				if(bl != null)leftcontinue = bl.onLeftClick(this, e, ic);
			}
			if(leftcontinue){
				if(e == null){ //Server will handle entities that get hit. We still had to check and TELL IT!
					if(focus_x > 0 && focus_y > 0 && focus_z > 0){				
						int bid = world.getblock(dimension, focus_x, focus_y, focus_z);
						if(bid > 0){
							if(Blocks.leftClickOnBlock(bid, this, dimension, focus_x, focus_y, focus_z) || this.getGameMode() != GameModes.SURVIVAL){
								float dmg = getAttackDamage();
								if(ic != null && ic.iid != 0){
									if(Blocks.isWood(bid) || Blocks.isLeaves(bid)){
										dmg += Items.getWoodStrength(ic.iid);
									}
									if(Blocks.isStone(bid)){
										dmg += Items.getStoneStrength(ic.iid);
									}
									if(Blocks.isDirt(bid)){
										dmg += Items.getDirtStrength(ic.iid);
									}
									if(ic.getItem() instanceof ItemSword && !Blocks.isLeaves(bid)){
										dmg = 0;
									}
									if(ic.getItem() instanceof ItemSword && Blocks.isLeaves(bid)){
										dmg += Items.getAttackStrength(ic.iid);
									}
									ic.getItem().leftClickOnBlock(this, dimension, focus_x, focus_y, focus_z, focus_side);
								}

								if(dmg != 0){
									float damageadjust = getTotalEffect(Effects.STRENGTH);
									if(damageadjust != 0)dmg *= damageadjust;
									damageadjust = getTotalEffect(Effects.WEAKNESS);
									if(damageadjust != 0)dmg /= damageadjust;
									if(ic != null){
										damageadjust = (ic.getAttribute(ItemAttribute.DAMAGE)*0.5f) + 1;
										dmg *= damageadjust;
									}
								}

								String particlename = Blocks.getParticleName(bid);
								if(particlename == null || particlename.equals(""))particlename = "DangerZone:ParticleBreak";
								Utils.spawnParticles(this.world, particlename, 10, this.dimension, (double)focus_x+0.5f, (double)focus_y+0.5f, (double)focus_z+0.5f, bid, true);
								int md = Blocks.getMinDamage(bid);
								if(dmg >= md)WorldRenderer.focus_damage += dmg;
								//System.out.printf("min,  act, tot: %d, %d, %d\n", (int)md, (int)dmg, (int)WorldRenderer.focus_damage);
								if(dmg != 0 && (WorldRenderer.focus_damage >= WorldRenderer.focus_maxdamage || this.getGameMode() != GameModes.SURVIVAL)){
									world.playSound(Blocks.getBreakSound(bid), dimension, focus_x, focus_y, focus_z, 0.15f, 1.0f);	
									//do actual breaking on SERVER where there are permission checks!
									server_connection.sendBreakBlock(dimension, focus_x, focus_y, focus_z);								
								}else{
									world.playSound(Blocks.getHitSound(bid), dimension, focus_x, focus_y, focus_z, 0.15f, 1.0f);
								}
							}
						}
					}
				}else{
					//System.out.printf("spawn hurt at %f,  %f, %f\n", hit_x, hit_y, hit_z);
					Utils.spawnParticles(this.world, "DangerZone:ParticleHurt", 10, this.dimension, e.posx, e.posy, e.posz, true);
				}			
			}
		}
	}
	
	public void middleclick(World world, int focus_x, int focus_y, int focus_z, int eid){
		if(getGameMode() == GameModes.LIMBO)return;
		if(focus_x > 0 && focus_y > 0 && focus_z > 0){
			int bid = world.getblock(dimension, focus_x, focus_y, focus_z);			
			if(bid != 0){
				world.playSound(Blocks.getHitSound(bid), dimension, focus_x, focus_y, focus_z, 0.15f, 1.0f);
				DangerZone.messagetimer = 100;
				DangerZone.messagestring = Blocks.BlockArray[bid].uniquename;
				//System.out.printf("meta = %d\n", world.getblockmeta(dimension, focus_x, focus_y, focus_z));
			}
		}
	}
	
	
//Handle right-clicks by player
	public void rightclick(World world, int focusx, int focusy, int focusz, int side, int eid){
		//System.out.printf("x,  y, z, side, eid = %d, %d, %d, %d, %d\n", focusx, focusy, focusz, side, eid);
		Entity e = null;
		InventoryContainer ic = getHotbar(gethotbarindex());
		boolean delme = false;
		if(getGameMode() == GameModes.LIMBO)return;
		if(world.isServer){	
			boolean hold_still = false;
			if(ic != null){
				if(getRightButtonDownCount() != 0){
					Item it = ic.getItem();
					if(it != null){
						if(it.hold_straight){
							hold_still = true;
							armdir = 0;
							armangle = 0f;
							setAttacking(false);
						}
					}
				}
			}
			if(!hold_still){
				armdir = 1;
				setAttacking(true);
			}
			
			//System.out.printf("rt click server eid = %d\n", eid);
			e = DangerZone.server.entityManager.findEntityByID(eid);
			if(e != null){					
				if(e.rightClickedByPlayer(this, ic) && getGameMode() == GameModes.SURVIVAL){
					//decrement inventory?					
					if(ic != null){
						ic.count--;
						if(ic.count <= 0){
							ic = null;
						}
					}
					setHotbar(gethotbarindex(), ic);
				}else{						
					if(ic != null){
						Item it = ic.getItem();
						if(it != null)delme = it.onRightClick(this, e, ic);
						Block bl = ic.getBlock();
						if(bl != null)delme = bl.onRightClick(this, e, ic);
						if(getGameMode() == GameModes.SURVIVAL && delme){
							if(Items.getMaxStack(ic.iid) == 1){
								incCurrentUses(ic);	
							}else{
								ic.count--;
								if(ic.count <= 0){
									ic = null;
								}
								setHotbar(gethotbarindex(), ic);
							}
						}
					}
				}
			}else{
				int bid = 0;
				int iid = 0;
				if(ic != null && ic.count >= 1){
					bid = ic.bid;
					iid = ic.iid;
				}

				if(focusx > 0 && focusy >= 0 && focusz > 0){
					if(getHunger() > 0 && getGameMode() == GameModes.SURVIVAL)setHunger(getHunger()-0.025f); //hitting things makes us hungry!
					int fbid = world.getblock(dimension, focusx, focusy, focusz);
					boolean cont = Blocks.rightClickOnBlock(fbid, this, dimension, focusx, focusy, focusz);
					if(cont){						
						if(bid != 0){
							if(Blocks.doPlaceBlock(bid, fbid, this, world, dimension, focusx, focusy, focusz, side)){
								if(getGameMode() == GameModes.SURVIVAL){
									if(ic != null){
										//System.out.printf("doplaceblock = %d, %s\n", ic.count, w.isServer?"true":"false");
										ic.count--;
										setHotbarChanged(gethotbarindex());
										if(ic.count <= 0){
											setHotbar(gethotbarindex(), null);
										}
									}
								}
							}
						}
					}
					if(ic != null && iid != 0){						
						delme = Items.rightClickOnBlock(iid, this, dimension, focusx, focusy, focusz, side);	
						if(delme)ToDoList.itemRightClickedBlock(this, ic, dimension, focusx, focusy, focusz);
						world.playSound(Blocks.getHitSound(fbid), dimension, focusx, focusy, focusz, 0.15f, 1.0f);
						if(getGameMode() == GameModes.SURVIVAL && delme){
							if(Items.getMaxStack(ic.iid) == 1){
								incCurrentUses(ic);	
							}else{
								//System.out.printf("rt click client count= %d\n", ic.count);
								ic.count--;
								if(ic.count <= 0){
									setHotbar(gethotbarindex(), null);
								}
								setHotbarChanged(gethotbarindex());
							}
						}
					}							
				}else{
					if(ic != null){					
						if(ic.count >= 1){
							bid = ic.bid;
							iid = ic.iid;
						}
						Item it = ic.getItem();
						if(it != null)delme = it.onRightClick(this, null, ic);
						Block bl = ic.getBlock();
						if(bl != null)delme = bl.onRightClick(this, null, ic);
					}
					if(delme){
						if(ic != null && iid != 0){						
							if(getGameMode() == GameModes.SURVIVAL){
								if(Items.getMaxStack(ic.iid) == 1){
									incCurrentUses(ic);	
								}else{
									//System.out.printf("rt click client count= %d\n", ic.count);
									ic.count--;
									if(ic.count <= 0){
										setHotbar(gethotbarindex(), null);
									}
									setHotbarChanged(gethotbarindex());
								}
							}
						}
					}
				}
			}
		}else{
			e = DangerZone.entityManager.findEntityByID(eid);
			if(e != null){	
				server_connection.playerActionToServer(0, 1, e.entityID, focusx, focusy, focusz, side, 0, 0); //1 = right mouse click with entity
				e.rightClickedByPlayer(this, ic); //might need a GUI!
			}else{
				server_connection.playerActionToServer(0, 1, 0, focusx, focusy, focusz, side, 0, 0); //1 = right mouse click on block

				if(focusx > 0 && focusy >= 0 && focusz > 0){
					int fbid = world.getblock(dimension, focusx, focusy, focusz);
					Blocks.rightClickOnBlock(fbid, this, dimension, focusx, focusy, focusz);
					if(ic != null && ic.iid != 0)Items.rightClickOnBlock(ic.iid, this, dimension, focusx, focusy, focusz, side);	
				}else{
					if(ic!= null){
						Item it = ic.getItem();
						if(it != null)it.onRightClick(this, null, ic);
						Block bl = ic.getBlock();
						if(bl != null)bl.onRightClick(this, null, ic);
					}
				}
			}
		}
	}
	
	public void rightclickup(World world, int focusx, int focusy, int focusz, int side, int eid, int holdcount){
		if(getGameMode() == GameModes.LIMBO)return;
		InventoryContainer ic = getHotbar(gethotbarindex());
		if(ic != null){
			Item itm = ic.getItem();
			if(itm != null){				
				if(!world.isServer){
					server_connection.playerActionToServer(0, 3, holdcount, focusx, focusy, focusz, side, 0, 0); //1 = right mouse click UP
				}else{
					boolean doit = itm.rightclickup((Entity)this, ic, holdcount);
					if(doit && this.getGameMode() == GameModes.SURVIVAL){
						incCurrentUses(ic);	
					}
				}
			}
		}
	}
	
	
	/*
	 * Add a block or item into a slot
	 */
	public boolean putMeInASlot(InventoryContainer inic){
		int bid = inic.bid;
		int iid = inic.iid;
		
		if(getGameMode() == GameModes.LIMBO)return false;
		
		if(inic.count == 0 || (inic.iid == 0 && inic.bid == 0))return true; //pretend we did so it will go away. It should be null anyway!
		
		
		//Can we add to an existing one?
		for(int i=0;i<10;i++){
			InventoryContainer ic = getHotbar(i);
			if(ic != null){
				if(ic.bid == bid && ic.iid == iid){
					if(ic.bid != 0){
						if(ic.count < Blocks.getMaxStack(bid)){
							ic.count++;
							ToDoList.onPickedUp(this, ic);
							setHotbarChanged(i);
							return true;
						}
					}
					if(ic.iid != 0){
						if(ic.count < Items.getMaxStack(iid)){
							ic.count++;
							ToDoList.onPickedUp(this, ic);
							setHotbarChanged(i);
							return true;
						}
					}
				}
			}
		}
		
		for(int i=0;i<50;i++){
			InventoryContainer ic = getInventory(i);
			if(ic != null){
				if(ic.bid == bid && ic.iid == iid){
					if(ic.bid != 0){
						if(ic.count < Blocks.getMaxStack(bid)){
							ic.count++;
							ToDoList.onPickedUp(this, ic);
							setInventoryChanged(i);
							return true;
						}
					}
					if(ic.iid != 0){
						if(ic.count < Items.getMaxStack(iid)){
							ic.count++;
							ToDoList.onPickedUp(this, ic);
							setInventoryChanged(i);
							return true;
						}
					}
				}
			}
		}
				
		//find empty spot
		for(int i=0;i<10;i++){
			if(getHotbar(i) == null){
				setHotbar(i, inic);
				ToDoList.onPickedUp(this, inic);
				return true;
			}
		}
		for(int i=0;i<50;i++){
			if(getInventory(i) == null){
				ToDoList.onPickedUp(this, inic);
				setInventory(i, inic);
				return true;
			}
		}
		return false;
	}
	

	
	public void doEatHeldItem(){
		int hbi = gethotbarindex();
		InventoryContainer ic = getHotbar(hbi);
		if(ic != null && Items.isFood(ic.iid)){
			if(getHunger() < getMaxHunger()  || Items.eatAnyTime(ic.iid)){
				Item it = ic.getItem();
				if(it != null && it instanceof ItemFood){
					it.onFoodEaten(this); //server side callback!
					eaten++;
					ToDoList.onEaten(this, ic);
					server_thread.sendStatsToPlayer();
					if(getGameMode() == GameModes.SURVIVAL){
						ic.count--;
						if(ic.count <= 0)ic = null;
						setHotbar(hbi, ic);
					}
				}
			}
		}
	}

	
	private Entity steppedOnCockroach(){
		List<Entity> nearby_list = null;
		ListIterator<Entity> li;
		double dist;
		
		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		if(!this.world.isServer){
			return null;
		}else{
			nearby_list = DangerZone.server.entityManager.findEntitiesInRange(4.0f, dimension, posx, posy, posz);
		}
		

		if(nearby_list != null){
			if(!nearby_list.isEmpty()){			
				Entity e = null;
				li = nearby_list.listIterator();
				while(li.hasNext()){
					e = (Entity)li.next();
					if(e instanceof Cockroach){ 
						dist = getDistanceFromEntity(e); 						
						if(dist < 0.75f){
							Utils.spawnParticlesFromServer(this.world, "DangerZone:ParticleHurt", 10, this.dimension, e.posx, e.posy, e.posz);
							return e;
						}
					}
				}

			}
		}
				
		return null;
	}
	
	public Texture getTexture(){
		
		if(morph != null){
			return morph.getTexture();
		}
		
		if(donewtexture){ //Make a texture for another player
			File file = null;	
			
			try {
				file = File.createTempFile("TmpSkin", ".tmp");
				file.deleteOnExit();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				
			int width = 64;
			int height = 32;
			String format = "PNG"; 
			
			//ARGB - because we also now display outer headpiece!
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			   
			for(int x = 0; x < width; x++){
			    for(int y = 0; y < height; y++){
			        int i = (x + (width * y)) * 4;
			        int r = tdata[i] & 0xFF;
			        int g = tdata[i+1] & 0xFF;
			        int b = tdata[i+2] & 0xFF;
			        int a = tdata[i+3] & 0xFF;
			        image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b );
			    }
			}
			   
			try {
			    ImageIO.write(image, format, file);
			} catch (IOException e) { 
				e.printStackTrace(); 
			}
			image.flush();
			
			if(texture != null){
				texture.release();
				texture = null;
			}
			
			try {
				texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(file.getPath()));	
				tdata = texture.getTextureData();
			} catch (IOException e) {
				e.printStackTrace();
				texture = null;
			}			
			donewtexture = false;
		}
		
		if(texture == null){ //load my own texture
			try {
				texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("Player.png"));	
				tdata = texture.getTextureData();
			} catch (IOException e) {
				e.printStackTrace();
				texture = null;
			}
		}
		return texture;
	}
	
	public float getWidth(){
		if(morph != null){
			return morph.getWidth();
		}
		return super.getWidth();
	}
	
	
	public float getHeight(){
		if(morph != null){
			return morph.getHeight();
		}
		return super.getHeight();
	}
	
	public float getNameHeight(){
		if(morph != null)return morph.getNameHeight();
		return height;
	}
	
	public void onKill(Entity victim){
		if(victim == null)return;
		if(!(victim instanceof EntityLiving))return; //filter out at least some of the things we shouldn't morph to!
		//See if we can morph!
		float ef = getTotalEffect(Effects.MORPH);
		if(ef != 0){			
			//System.out.printf("morph to: %s, cs = %s\n", morphname, world.isServer?"true":"false");
			setMorphName(victim.uniquename);		
		}
	}
	
	public float getAttackDamage(){
		if(morph != null)return morph.getAttackDamage();
		return super.getAttackDamage();
	}
	
	public float getDefense(){
		float df = 0;
		if(morph != null)df = morph.getDefense();
		return df + super.getDefense();
	}
	
	 public float getAdjustedFallDamage(float ouch){
		 if(morph != null)return morph.getAdjustedFallDamage(ouch);
		 return ouch;
	 }
	 
	 public void setOnFire(int fire){
		 if(morph != null && morph.isImmuneToFire){
			 morph.setOnFire(0);
			 super.setOnFire(0);
			 return;
		 }
		 if(morph != null)morph.setOnFire(fire);
		 super.setOnFire(fire);
	 }
	 
	 public void setAttacking(boolean tf){
		 if(morph != null)morph.setAttacking(tf);
		 super.setAttacking(tf);
	 }
	 
	 public boolean takesDamageFrom(/*DamageTypes*/int dt){
		 if(morph != null){
			 if(!morph.takesDamageFrom(dt))return false;
		 }
		 return super.takesDamageFrom(dt);
	 }
	 
	 public void setFlying(boolean tf){
		 if(morph != null)morph.setFlying(tf);
		 super.setFlying(tf);
	 }

	 public void writeSelf(Properties prop, String tag){
		 super.writeSelf(prop, tag);
		 prop.setProperty(String.format("%s%s", tag, "HomeDimension"), String.format("%d", home_dimension));
		 prop.setProperty(String.format("%s%s", tag, "HomePosx"), String.format("%f", home_x));
		 prop.setProperty(String.format("%s%s", tag, "HomePosy"), String.format("%f", home_y));
		 prop.setProperty(String.format("%s%s", tag, "HomePosz"), String.format("%f", home_z));
		 
		 prop.setProperty(String.format("%s%s", tag, "Kills"), String.format("%d", kills));
		 prop.setProperty(String.format("%s%s", tag, "Deaths"), String.format("%d", deaths));
		 prop.setProperty(String.format("%s%s", tag, "Damage_taken"), String.format("%f", damage_taken));
		 prop.setProperty(String.format("%s%s", tag, "Damage_dealt"), String.format("%f", damage_dealt));
		 prop.setProperty(String.format("%s%s", tag, "Blocks_broken"), String.format("%d", blocks_broken));
		 prop.setProperty(String.format("%s%s", tag, "Blocks_placed"), String.format("%d", blocks_placed));
		 prop.setProperty(String.format("%s%s", tag, "Blocks_colored"), String.format("%d", blocks_colored));
		 prop.setProperty(String.format("%s%s", tag, "Crafted"), String.format("%d", crafted));
		 prop.setProperty(String.format("%s%s", tag, "Bought"), String.format("%d", bought));
		 prop.setProperty(String.format("%s%s", tag, "Sold"), String.format("%d", sold));
		 prop.setProperty(String.format("%s%s", tag, "Broken"), String.format("%d", broken));
		 prop.setProperty(String.format("%s%s", tag, "Traveled"), String.format("%d", traveled));
		 prop.setProperty(String.format("%s%s", tag, "Morphs"), String.format("%d", morphs));
		 prop.setProperty(String.format("%s%s", tag, "Teleports"), String.format("%d", teleports));
		 prop.setProperty(String.format("%s%s", tag, "Eaten"), String.format("%d", eaten));
		 prop.setProperty(String.format("%s%s", tag, "RoachStomps"), String.format("%d", roachstomps));
		 prop.setProperty(String.format("%s%s", tag, "HardLandings"), String.format("%d", hard_landings));
		 prop.setProperty(String.format("%s%s", tag, "Flights"), String.format("%d", flights));
		 prop.setProperty(String.format("%s%s", tag, "Spells"), String.format("%d", spells));
		 
		 ToDoList.writeSelf(prop, tag);

	 }

	 public void readSelf(Properties prop, String tag){
		 super.readSelf(prop, tag);
		 home_dimension = Utils.getPropertyInt(prop, String.format("%s%s", tag, "HomeDimension"), 0, 128, 0); //default is zero!
		 home_x = Utils.getPropertyDouble(prop, String.format("%s%s", tag, "HomePosx"), 0, Integer.MAX_VALUE, 10000);
		 home_y = Utils.getPropertyDouble(prop, String.format("%s%s", tag, "HomePosy"), 0, Integer.MAX_VALUE, 70);
		 home_z = Utils.getPropertyDouble(prop, String.format("%s%s", tag, "HomePosz"), 0, Integer.MAX_VALUE, 10000);
		 
		 kills = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Kills"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 deaths = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Deaths"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 damage_taken = Utils.getPropertyDouble(prop, String.format("%s%s", tag, "Damage_taken"), 0, Integer.MAX_VALUE, 0);
		 damage_dealt = Utils.getPropertyDouble(prop, String.format("%s%s", tag, "Damage_dealt"), 0, Integer.MAX_VALUE, 0);
		 blocks_broken = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Blocks_broken"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 blocks_placed = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Blocks_placed"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 blocks_colored = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Blocks_colored"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 crafted = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Crafted"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 bought = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Bought"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 sold = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Sold"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 broken = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Broken"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 traveled = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Traveled"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 morphs = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Morphs"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 teleports = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Teleports"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 eaten = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Eaten"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 roachstomps = Utils.getPropertyInt(prop, String.format("%s%s", tag, "RoachStomps"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 hard_landings = Utils.getPropertyInt(prop, String.format("%s%s", tag, "HardLandings"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 flights = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Flights"), 0, Integer.MAX_VALUE, 0); //default is zero!
		 spells = Utils.getPropertyInt(prop, String.format("%s%s", tag, "Spells"), 0, Integer.MAX_VALUE, 0); 
		 
		 ToDoList.readSelf(prop, tag); //Only works if list is built before player is loaded, which, apparently, it is... 
		 
	 }
	 
	 ////////////////////////////////////////////BIG GUI MESS! :)
	 
	 //called SERRVER SIDE ONLY
	 public void handleInventory(int command, int p1, int p2, int p3, int p4){
		 //inventory/gui handler can be overridden.
		 if(alt_inv_handler != null){
			 //PLEASE USE CASE VALUES OVER 256.
			 //DZ reserves numbers (commands) under 256 for future expansion.
			 alt_inv_handler.doHandleInventory(this, command, p1, p2, p3, p4);
		 }else{
			 defaultHandleInventory(command, p1, p2, p3, p4);
		 }
	 }
		 		 
	 public void defaultHandleInventory(int command, int p1, int p2, int p3, int p4){
		 if(getGameMode() == GameModes.LIMBO)return;
		 switch(command){
		 case 0:
			 if(ServerHooker.clickedHotBar(this, p1, p2, p3!=0?true:false)){
				 ClickedHotBar(p1, p2, p3!=0?true:false);
			 }
			 break;
		 case 1:
			 if(ServerHooker.clickedInventory(this, p1, p2, p3!=0?true:false)){
				 ClickedInventory(p1, p2, p3!=0?true:false);
			 }
			 break;
		 case 2:
			 if(ServerHooker.clickedEntityInventory(this, p1, p2, p3, p4!=0?true:false)){
				 ClickedEntityInventory(p1, p2, p3, p4!=0?true:false);
			 }
			 break;
		 case 3:
			 if(getGameMode() == GameModes.SURVIVAL)return;
			 ClickedCreativeInventory(p1, p2, p3!=0?true:false);
			 break;
		 case 4:
			 ClickedCrafted(p1, p2!=0?true:false);
			 break;
		 case 5:
			 ClickedCrafting(p1, p2, p3!=0?true:false);
			 break;
		 case 6:
			 SpreadListAdd(p1, p2, p3);
			 break;
		 case 7:
			 SpreadListClear();
			 break;
		 case 8:
			 ClearTable();
			 break;
		 case 9:
			 if(ServerHooker.clickedArmor(this, p1, p2, p3!=0?true:false)){
				 ClickedArmor(p1, p2, p3!=0?true:false);
			 }
			 break;
		 case 10:
			 DeleteMouseBite();
			 break;
		 case 11:
			 SpitMouseBite();
			 break;
		 case 12:
			 ClickedInventoryWithEntity(p1, p2, p3!=0?true:false, p4);
			 break;
		 case 13:
			 SpitOneHotbar();
			 break;
		 case 14:
			 SellIt(p1, p2, p3, p4);
			 break;
		 case 15:
			 BuyIt(p1, p2, p3, p4);
			 break;
		 case 16:
			 UnStayEntity(p1, p2, p3, p4);
			 break;
		 case 17:
			 MouseBiteToEntity(p1, p2, p3, p4);
			 break;
		 default:
			 break;
		 }
	 }
	 
	 public void MouseBiteToEntity(int eid, int p2, int p3, int p4){
		 Entity ent = DangerZone.server.entityManager.findEntityByID(eid);
		 if(ent == null)return;
		 InventoryContainer mousebite = getMouseBite();
		 InventoryContainer eh = ent.getHotbar(0);
		 setMouseBite(eh);
		 ent.setHotbar(0, mousebite);
	 }
	 
	 public void UnStayEntity(int eid, int p2, int p3, int p4){
		 Entity ent = DangerZone.server.entityManager.findEntityByID(eid);
		 if(ent == null)return;
		 ent.setStaying(false);
	 }
	 
	 public void SellIt(int eid, int p2, int p3, int p4){
		 Entity ent = DangerZone.server.entityManager.findEntityByID(eid);
		 if(ent == null)return;
		 InventoryContainer ic = ent.getHotbar(0);
		 if(ic == null)return;
		 int cost = ent.getVarInt(30);
		 if(cost <= 0)return;
		 if(getMouseBite() != null)return;
		 int i;
		 
		 ent.setHotbar(0,  null);
		 sold += ic.count;
		 server_thread.sendStatsToPlayer();
				 
		 //if we have room, keep it and double the price!
		 for(i=0;i<8;i++){
			 if(ent.getInventory(i) == null){
				 ent.setInventory(i, ic);
				 ent.setVarInt(i+16, cost*2);
				 break;
			 }
		 }
		 while(cost >= 1000){
			 ic = new InventoryContainer("DangerZone:Platinum Coin", 1);
			 if(!putMeInASlot(ic)){
				 setMouseBite(ic);
				 DropMouseBite();
				 setMouseBite(null);
			 }
			 cost -= 1000;
		 }
		 while(cost >= 100){
			 ic = new InventoryContainer("DangerZone:Gold Coin", 1);
			 if(!putMeInASlot(ic)){
				 setMouseBite(ic);
				 DropMouseBite();
				 setMouseBite(null);
			 }
			 cost -= 100;
		 }
		 while(cost >= 10){
			 ic = new InventoryContainer("DangerZone:Silver Coin", 1);
			 if(!putMeInASlot(ic)){
				 setMouseBite(ic);
				 DropMouseBite();
				 setMouseBite(null);
			 }
			 cost -= 10;
		 }
		 
	 }
	 
	 public void BuyIt(int which, int eid, int p3, int p4){
		 if(which < 0 || which > 7)return; //0-7
		 InventoryContainer mousebite = null;
		 InventoryContainer ic = null;
		 InventoryContainer spare_change[];
		 int i, totalcoins, cost;
		 Entity ent = DangerZone.server.entityManager.findEntityByID(eid);
		 if(ent == null)return;
		 spare_change = new InventoryContainer[2];
		 
		 totalcoins = 0;
		 mousebite = getMouseBite();
		 if(mousebite != null)return; //mouse must be empty!
		 		 
		 for(i=0;i<50;i++){
			 ic = getInventory(i);
			 if(ic != null){
				 if(ic.iid == Items.coinplatinum.itemID)totalcoins += 1000 * ic.count;
				 if(ic.iid == Items.coingold.itemID)totalcoins += 100 * ic.count;
				 if(ic.iid == Items.coinsilver.itemID)totalcoins += 10 * ic.count;
			 }
		 }

		for(i=0;i<10;i++){
				ic = getHotbar(i);
				if(ic != null){
					if(ic.iid == Items.coinplatinum.itemID)totalcoins += 1000 * ic.count;
					if(ic.iid == Items.coingold.itemID)totalcoins += 100 * ic.count;
					if(ic.iid == Items.coinsilver.itemID)totalcoins += 10 * ic.count;
				}
		}
		
		cost = ent.getVarInt(which+16);
		if(cost > totalcoins)return;
		
		//has money and empty mouse. do it!
		mousebite = ent.getInventory(which);
		if(mousebite == null)return; //no charge for nothing!
		
		setMouseBite(mousebite); //take it
		ent.setInventory(which,  null);
		
		bought += mousebite.count;
		server_thread.sendStatsToPlayer();
		
		//now subtract coins from inventory!
		//Yeah, what a pain... Player inventory could be full and he only has platinum... who knows...
		//it's ugly, but it works, i hope...
		
		int giveup = 30000; //stop infinite loop in case something goes wrong.
		while(giveup > 0 && cost > 0){
			giveup--;
			ic = findThis(Items.coinplatinum.itemID, spare_change);
			while(cost >= 1000 && ic != null){
				ic = findThis(Items.coinplatinum.itemID, spare_change);
				if(ic != null){
					if(ic.count > 0){
						ic.count--;
						cost -= 1000;
					}					
				}
			}
			ic = findThis(Items.coingold.itemID, spare_change);
			while(cost >= 100 && ic != null){
				ic = findThis(Items.coingold.itemID, spare_change);
				if(ic != null){
					if(ic.count > 0){
						ic.count--;
						cost -= 100;
					}					
				}
			}
			ic = findThis(Items.coinsilver.itemID, spare_change);
			while(cost >= 10 && ic != null){
				ic = findThis(Items.coinsilver.itemID, spare_change);
				if(ic != null){
					if(ic.count > 0){
						ic.count--;
						cost -= 10;
					}					
				}
			}
			if(cost > 0){
				//need to make some change!
				ic = findThis(Items.coingold.itemID, spare_change);
				if(ic != null && ic.count > 0){					
					ic.count--;
					findThis(Items.coingold.itemID, spare_change); //clear if 0
					spare_change[0] = new InventoryContainer("DangerZone:Silver Coin", 10);					
				}else{
					ic = findThis(Items.coinplatinum.itemID, spare_change);
					if(ic != null && ic.count > 0){					
						ic.count--;
						findThis(Items.coinplatinum.itemID, spare_change); //clear if 0
						spare_change[1] = new InventoryContainer("DangerZone:Gold Coin", 10);					
					}
				}
			}			
		}
		
		//null any leftovers with count 0. I think I do this elsewhere too, but what the heck...
		ic = findThis(Items.coinplatinum.itemID, spare_change);
		ic = findThis(Items.coingold.itemID, spare_change);
		ic = findThis(Items.coinsilver.itemID, spare_change);
		
		//try to put spare change into inventory somewhere
		if(spare_change[0] != null){
			if(!putMeInASlot(spare_change[0])){
				ic = getMouseBite();
				setMouseBite(spare_change[0]);
				DropMouseBite();
				setMouseBite(ic);
			}
		}
		if(spare_change[1] != null){
			if(!putMeInASlot(spare_change[1])){
				ic = getMouseBite();
				setMouseBite(spare_change[1]);
				DropMouseBite();
				setMouseBite(ic);
			}
		}
		
	 }
	 
	 public InventoryContainer findThis(int iid, InventoryContainer spare_change[]){
		 int i;
		 InventoryContainer ic;
		 
		 for(i=0;i<50;i++){
			 ic = getInventory(i);
			 if(ic != null){
				 if(ic.iid == iid){
					 setInventoryChanged(i);
					 if(ic.count <= 0){
						 setInventory(i, null);
						 continue;
					 }
					 return ic;
				 }
			 }
		 }

		for(i=0;i<10;i++){
				ic = getHotbar(i);
				if(ic != null){
					 if(ic.iid == iid){
						 setHotbarChanged(i);
						 if(ic.count <= 0){
							 setHotbar(i, null);
							 continue;
						 }
						 return ic;
					 }
				}
		}
		
		if(spare_change != null){
			if(spare_change[0] != null){
				if(spare_change[0].iid == iid){
					if(spare_change[0].count > 0){
						return spare_change[0];
					}else{
						spare_change[0] = null;
					}
				}
			}
			if(spare_change[1] != null){
				if(spare_change[1].iid == iid){
					if(spare_change[1].count > 0){
						return spare_change[1];
					}else{
						spare_change[1] = null;
					}
				}
			}
		}
		 
		 return null;
	 }
	 
	 public void ClickedHotBar(int which, int leftrightmid, boolean shifted){	
		 if(which < 0 || which > 9)return;
		 InventoryContainer mousebite = null;
		 InventoryContainer ic = null;
		 //System.out.printf("Clicked hotbar %d,  %d\n", which, leftrightmid);
		 if(leftrightmid == 0){ //left
			 if(shifted){ //shifted, send to general inventory
				 mousebite = getMouseBite();
				 if(mousebite != null)mousebiteToInventory();
				 mousebite = getMouseBite();
				 if(mousebite == null){
					 setMouseBite(getHotbar(which));
					 setHotbar(which, null);
					 mousebiteToInventory();				 
				 }
			 }else{
				 mousebite = getMouseBite();
				 if(mousebite == null){ //pick up
					 setMouseBite(getHotbar(which));
					 setHotbar(which, null);				 
				 }else{
					 ic = getHotbar(which);
					 if(ic != null){
						 //if same, add, else swap!
						 if(ic.iid != mousebite.iid || ic.bid != mousebite.bid){
							 setHotbar(which, mousebite);
							 setMouseBite(ic);
						 }else{
							 int mx = ic.getMaxStack() - ic.count;
							 if(mx > 0){ //room for more?
								 if(mx >= mousebite.count){
									 ic.count += mousebite.count;
									 setMouseBite(null);
									 setHotbarChanged(which);
								 }else{
									 mousebite.count -= mx;
									 ic.count += mx;
									 setMouseBiteChanged();
									 setHotbarChanged(which);
								 }
							 }
						 }					 
					 }else{ //put down
						 setHotbar(which, mousebite);
						 setMouseBite(null);
					 }
				 }
			 }			 
		 }else if(leftrightmid == 1){ //right
			 mousebite = getMouseBite();
			 if(mousebite == null){ //pick up half
				 ic = getHotbar(which);
				 if(ic != null){
					 int half = ic.count / 2;
					 if(half == 0){
						 setMouseBite(ic);
						 setHotbar(which, null);
					 }else{
						 mousebite = new InventoryContainer();
						 mousebite.bid = ic.bid;
						 mousebite.iid = ic.iid;
						 mousebite.count = half;
						 ic.count -= half;
						 setMouseBite(mousebite);
						 setHotbarChanged(which);
					 }
				 }
			 }else{ //drop half
				 
			 }			 
		 }else{ //mid
			 setMouseBite(null);
			 setHotbar(which, null);
		 }
	 }
	 public void ClickedInventory(int which, int leftrightmid, boolean shifted){
		 if(which < 0 || which > 49)return;
		 InventoryContainer mousebite = null;
		 InventoryContainer ic = null;
		 if(leftrightmid == 0){ //left
			 if(shifted){ //shifted, send to general inventory
				 mousebite = getMouseBite();
				 if(mousebite != null)mousebiteToInventory();
				 mousebite = getMouseBite();
				 if(mousebite == null){
					 setMouseBite(getInventory(which));
					 setInventory(which, null);
					 mousebiteToInventory();				 
				 }
			 }else{
				 mousebite = getMouseBite();
				 if(mousebite == null){ //pick up
					 setMouseBite(getInventory(which));
					 setInventory(which, null);				 
				 }else{
					 ic = getInventory(which);
					 if(ic != null){
						 //if same, add, else swap!
						 if(ic.iid != mousebite.iid || ic.bid != mousebite.bid){
							 setInventory(which, mousebite);
							 setMouseBite(ic);
						 }else{
							 int mx = ic.getMaxStack() - ic.count;
							 if(mx > 0){ //room for more?
								 if(mx >= mousebite.count){
									 ic.count += mousebite.count;
									 setMouseBite(null);
									 setInventoryChanged(which);
								 }else{
									 mousebite.count -= mx;
									 ic.count += mx;
									 setMouseBiteChanged();
									 setInventoryChanged(which);
								 }
							 }
						 }					 
					 }else{ //put down
						 setInventory(which, mousebite);
						 setMouseBite(null);
					 }
				 }
			 }			 
		 }else if(leftrightmid == 1){ //right
			 mousebite = getMouseBite();
			 if(mousebite == null){ //pick up half
				 ic = getInventory(which);
				 if(ic != null){
					 int half = ic.count / 2;
					 if(half == 0){
						 setMouseBite(ic);
						 setInventory(which, null);
					 }else{
						 mousebite = new InventoryContainer();
						 mousebite.bid = ic.bid;
						 mousebite.iid = ic.iid;
						 mousebite.count = half;
						 ic.count -= half;
						 setMouseBite(mousebite);
						 setInventoryChanged(which);
					 }
				 }
			 }else{ //drop half
				 
			 }			 
		 }else{ //mid
			 setMouseBite(null);
			 setInventory(which, null);
		 }
	 }
	 
	 public void ClickedInventoryWithEntity(int which, int leftrightmid, boolean shifted, int eid){
		 
		 if(which < 0 || which > 49)return;
		 Entity ent = DangerZone.server.entityManager.findEntityByID(eid);
		 if(ent == null)return;
		 
		 if(!BreakChecks.canTakeStuff(this, ent))return;
		 InventoryContainer mousebite = null;
		 InventoryContainer ic = null;
		 if(leftrightmid == 0){ //left
			 if(shifted){ //shifted, send to general inventory
				 mousebite = getMouseBite();
				 if(mousebite != null)mousebiteToInventory(ent);
				 mousebite = getMouseBite();
				 if(mousebite == null){
					 setMouseBite(getInventory(which));
					 setInventory(which, null);
					 mousebiteToInventory(ent);				 
				 }
			 }else{
				 mousebite = getMouseBite();
				 if(mousebite == null){ //pick up
					 setMouseBite(getInventory(which));
					 setInventory(which, null);				 
				 }else{
					 ic = getInventory(which);
					 if(ic != null){
						 //if same, add, else swap!
						 if(ic.iid != mousebite.iid || ic.bid != mousebite.bid){
							 setInventory(which, mousebite);
							 setMouseBite(ic);
						 }else{
							 int mx = ic.getMaxStack() - ic.count;
							 if(mx > 0){ //room for more?
								 if(mx >= mousebite.count){
									 ic.count += mousebite.count;
									 setMouseBite(null);
									 setInventoryChanged(which);
								 }else{
									 mousebite.count -= mx;
									 ic.count += mx;
									 setMouseBiteChanged();
									 setInventoryChanged(which);
								 }
							 }
						 }					 
					 }else{ //put down
						 setInventory(which, mousebite);
						 setMouseBite(null);
					 }
				 }
			 }			 
		 }else if(leftrightmid == 1){ //right
			 mousebite = getMouseBite();
			 if(mousebite == null){ //pick up half
				 ic = getInventory(which);
				 if(ic != null){
					 int half = ic.count / 2;
					 if(half == 0){
						 setMouseBite(ic);
						 setInventory(which, null);
					 }else{
						 mousebite = new InventoryContainer();
						 mousebite.bid = ic.bid;
						 mousebite.iid = ic.iid;
						 mousebite.count = half;
						 ic.count -= half;
						 setMouseBite(mousebite);
						 setInventoryChanged(which);
					 }
				 }
			 }else{ //drop half
				 
			 }			 
		 }else{ //mid
			 setMouseBite(null);
			 setInventory(which, null);
		 }
	 }
	 
	 
	 public void ClickedEntityInventory(int eid, int which, int leftrightmid, boolean shifted){	
		 if(which < 0 || which > 49)return;
		 Entity ent = DangerZone.server.entityManager.findEntityByID(eid);
		 if(ent == null)return;
		 
		 if(!BreakChecks.canTakeStuff(this, ent))return;
		 
		 InventoryContainer mousebite = null;
		 InventoryContainer ic = null;
		 if(leftrightmid == 0){ //left
			 if(shifted){ //shifted, send to general inventory
				 mousebite = getMouseBite();
				 if(mousebite != null)mousebiteToInventory();
				 mousebite = getMouseBite();
				 if(mousebite == null){
					 setMouseBite(ent.getInventory(which));
					 if(ent instanceof EntityFurnace)ToDoList.onCrafted(this, getMouseBite());
					 ent.setInventory(which, null);
					 mousebiteToInventory();				 
				 }
			 }else{
				 mousebite = getMouseBite();
				 if(mousebite == null){ //pick up
					 setMouseBite(ent.getInventory(which));
					 if(ent instanceof EntityFurnace)ToDoList.onCrafted(this, getMouseBite());
					 ent.setInventory(which, null);				 
				 }else{
					 ic = ent.getInventory(which);
					 if(ic != null){
						 //if same, add, else swap!
						 if(ic.iid != mousebite.iid || ic.bid != mousebite.bid){
							 ent.setInventory(which, mousebite);
							 setMouseBite(ic);
						 }else{
							 int mx = ic.getMaxStack() - ic.count;
							 if(mx > 0){ //room for more?
								 if(mx >= mousebite.count){
									 ic.count += mousebite.count;
									 setMouseBite(null);
									 ent.setInventoryChanged(which);
								 }else{
									 mousebite.count -= mx;
									 ic.count += mx;
									 setMouseBiteChanged();
									 ent.setInventoryChanged(which);
								 }
							 }
						 }					 
					 }else{ //put down
						 ent.setInventory(which, mousebite);
						 setMouseBite(null);
					 }
				 }
			 }			 
		 }else if(leftrightmid == 1){ //right
			 mousebite = getMouseBite();
			 if(mousebite == null){ //pick up half
				 ic = ent.getInventory(which);
				 if(ic != null){
					 if(ent instanceof EntityFurnace)ToDoList.onCrafted(this, ic);
					 int half = ic.count / 2;
					 if(half == 0){
						 setMouseBite(ic);
						 ent.setInventory(which, null);
					 }else{
						 mousebite = new InventoryContainer();
						 mousebite.bid = ic.bid;
						 mousebite.iid = ic.iid;
						 mousebite.count = half;
						 ic.count -= half;
						 setMouseBite(mousebite);
						 ent.setInventoryChanged(which);
					 }
				 }
			 }else{ //drop half

			 }			 
		 }else{ //mid
			 setMouseBite(null);
			 ent.setInventory(which, null);
		 }
	 }
	 
	 
	 public void ClickedCreativeInventory(int iid, int bid, boolean shifted){
		 InventoryContainer ic = null;
		 if(Items.isValid(iid)){
			 ic = new InventoryContainer(0, iid, Items.getMaxStack(iid), 0);
		 }else{
			 if(Blocks.isValid(bid)){
				 ic = new InventoryContainer(bid, 0, Blocks.getMaxStack(bid), 0);
			 }
		 }
		if(ic != null)ic.onCrafted(this);
		setMouseBite(ic);
		if(shifted)mousebiteToInventory();
	 }
	 
	 public void refreshCrafted(){
		 Recipe r = Crafting.find(getCrafting(0)==null?null:getCrafting(0).getUniqueName(), 
		 			getCrafting(1)==null?null:getCrafting(1).getUniqueName(),
		 			getCrafting(2)==null?null:getCrafting(2).getUniqueName(),
					getCrafting(3)==null?null:getCrafting(3).getUniqueName(), 
					getCrafting(4)==null?null:getCrafting(4).getUniqueName(), 
					getCrafting(5)==null?null:getCrafting(5).getUniqueName(),
					getCrafting(6)==null?null:getCrafting(6).getUniqueName(), 
					getCrafting(7)==null?null:getCrafting(7).getUniqueName(), 
					getCrafting(8)==null?null:getCrafting(8).getUniqueName());
		 
		 
		 if(r != null){
			 InventoryContainer ic = new InventoryContainer(r.outname, r.out_count);
			 setCrafted(ic);
		 }else{
			 setCrafted(null);
		 }
	 }
	 
	 public void ClickedCrafted(int leftrightmid, boolean shifted){
		 InventoryContainer mousebite = getMouseBite();
		 InventoryContainer ic = getCrafted();
		 if(ic != null){		 
			 if(leftrightmid == 0){
				 if(mousebite == null){
					 setMouseBite(ic);
					 ic.onCrafted(this);
					 crafted += ic.count;
					 ToDoList.onCrafted(this, ic);
					 server_thread.sendStatsToPlayer();
					 setCrafted(null);
					 for(int i=0;i<9;i++){
						 ic = getCrafting(i);
						 if(ic != null){						 
							 if(ic.iid == Items.bucketwater.itemID || ic.iid == Items.bucketlava.itemID || ic.iid == Items.bucketmilk.itemID){
								 ic = new InventoryContainer(0, Items.bucket.itemID, 1, 0);
								 setCrafting(i, ic);
							 }else{
								 ic.count--;
								 if(ic.count <= 0){
									 setCrafting(i, null);
								 }else{
									 setCraftingChanged(i);
								 }
							 }
						 }
					 }
				 }else{
					 if(mousebite.iid == ic.iid && mousebite.bid == ic.bid){
						 int df = 0;
						 if(ic.iid != 0)df = Items.getMaxStack(ic.iid);
						 if(ic.bid != 0)df = Blocks.getMaxStack(ic.bid);
						 if(mousebite.count + ic.count <= df){
							 mousebite.count += ic.count;
							 mousebite.onCrafted(this);
							 crafted += ic.count;
							 ToDoList.onCrafted(this, mousebite);
							 server_thread.sendStatsToPlayer();
							 setMouseBiteChanged();
							 setCrafted(null);
							 for(int i=0;i<9;i++){
								 ic = getCrafting(i);
								 if(ic != null){
									 if(ic.iid == Items.bucketwater.itemID || ic.iid == Items.bucketlava.itemID || ic.iid == Items.bucketmilk.itemID){
										 ic = new InventoryContainer(0, Items.bucket.itemID, 1, 0);
										 setCrafting(i, ic);
									 }else{
										 ic.count--;
										 if(ic.count <= 0){
											 setCrafting(i, null);
										 }else{
											 setCraftingChanged(i);
										 }
									 }
								 }
							 }
						 }					 
					 }
				 }
			 }
		 }
		 
		 refreshCrafted();
		
		 if(shifted)mousebiteToInventory();
	 }
	 public void ClickedCrafting(int which, int leftrightmid, boolean shifted){
		 if(which < 0 || which > 8)return;
		 InventoryContainer mousebite = getMouseBite();
		 InventoryContainer ic = getCrafting(which);
		 
		 
		 if(leftrightmid == 0){ //left
			 if(shifted){ //shifted, send to general inventory
				 if(mousebite != null)mousebiteToInventory();
				 mousebite = getMouseBite();
				 if(mousebite == null){
					 setMouseBite(getCrafting(which));
					 setCrafting(which, null);
					 mousebiteToInventory();				 
				 }
			 }else{
				 if(mousebite == null){ //pick up
					 setMouseBite(getCrafting(which));
					 setCrafting(which, null);				 
				 }else{
					 ic = getCrafting(which);
					 if(ic != null){
						 //if same, add, else swap!
						 if(ic.iid != mousebite.iid || ic.bid != mousebite.bid){
							 setCrafting(which, mousebite);
							 setMouseBite(ic);
						 }else{
							 int mx = ic.getMaxStack() - ic.count;
							 if(mx > 0){ //room for more?
								 if(mx >= mousebite.count){
									 ic.count += mousebite.count;
									 setMouseBite(null);
									 setCraftingChanged(which);
								 }else{
									 mousebite.count -= mx;
									 ic.count += mx;
									 setMouseBiteChanged();
									 setCraftingChanged(which);
								 }
							 }
						 }					 
					 }else{ //put down
						 setCrafting(which, mousebite);
						 setMouseBite(null);
					 }
				 }
			 }			 
		 }else if(leftrightmid == 1){ //right
			 if(mousebite == null){ //pick up half
				 ic = getCrafting(which);
				 if(ic != null){
					 int half = ic.count / 2;
					 if(half == 0){
						 setMouseBite(ic);
						 setCrafting(which, null);
					 }else{
						 mousebite = new InventoryContainer();
						 mousebite.bid = ic.bid;
						 mousebite.iid = ic.iid;
						 mousebite.count = half;
						 ic.count -= half;
						 setMouseBite(mousebite);
						 setCraftingChanged(which);
					 }
				 }
			 }else{ //drop half
				 
			 }			 
		 }else{ //mid
			 setMouseBite(null);
			 setCrafting(which, null);
		 }
		 		 	 
		 refreshCrafted();
		
		 if(shifted)mousebiteToInventory(); 
	 }
	 public void SpreadListAdd(int which, int shifted, int dir){

		 if(which < 0 || which > 8)return;
		 
		 if(shifted != 0){ 
			 ClickedCrafting(which, 0, true);
			 SpreadListClear();
			 return;
		 }
		 
		 InventoryContainer mousebite = getMouseBite();
		 InventoryContainer ic = null;
		 
		 if(dir == 1){ //down
			 if(mousebite == null){
				 setMouseBite(getCrafting(which));
				 setCrafting(which, null);
				 SpreadListClear();
				 return;
			 }
			 if(getCrafting(which) != null){
				 ClickedCrafting(which, 0, false);
				 SpreadListClear();
				 return;
			 }
			 spread_list = new ArrayList<Integer>();
			 spread_list.add(which);
			 setCrafting(which, mousebite);
			 setMouseBite(null);
			 return;
		 }
		 
		 if(isOnSpreadList(which))return;
		 if(spread_list == null)return;

		 
		 if(spread_list == null){
			 if(mousebite == null){
				 ic = getCrafting(which);
				 if(ic != null){
					 spread_list = new ArrayList<Integer>();
					 spread_list.add(which);
				 }
			 }else{
				 ic = getCrafting(which);
				 if(ic == null){
					 spread_list = new ArrayList<Integer>();
					 spread_list.add(which);
					 setCrafting(which, mousebite);
					 setMouseBite(null);
				 }
			 }
			 return;
		 }
		 
		 if(mousebite != null){
			 SpreadListClear();
			 return;
		 }
		 
		 ic = getCrafting(which);
		 if(ic != null)return;
		 
		 ic = getCrafting(spread_list.get(0));
		 if(ic.getMaxStack() <= 1)return;
		 
		 //got a new empty slot. get total count, then redistribute.
		 int i=spread_list.size();
		 int total = 0;
		 int bid, iid;
		 bid = iid = 0;
		 while(i>0){
			 ic = getCrafting(spread_list.get(i-1));
			 if(ic != null){
				 total += ic.count;
				 bid = ic.bid; //remember what this was!!!
				 iid = ic.iid;
			 }
			 setCrafting(spread_list.get(i-1), null); //clear!
			 i--;
		 }	
		 
		 //now add new one and then redist
		 spread_list.add(which);
		 int newsize = spread_list.size();
		 i = 0;
		 while(total > 0){
			 ic = getCrafting(spread_list.get(i));
			 if(ic == null){
				 ic = new InventoryContainer(bid, iid, 1, 0);
				 setCrafting(spread_list.get(i), ic);
			 }else{
				 ic.count++;
				 setCraftingChanged(spread_list.get(i));
			 }
			 
			 i++;
			 if(i >= newsize)i = 0;
			 total--;
		 }
		 
		 refreshCrafted();
		 
	 }
	 private boolean isOnSpreadList(int index){
		 if(spread_list == null)return false;
		 int i=spread_list.size();
		 while(i>0){
			 if(index == spread_list.get(i-1))return true;
			 i--;
		 }
		 return false;
	 }
	 public void SpreadListClear(){
		 spread_list = null;
		 refreshCrafted();
	 }
	 public void ClearTable(){
		 DropMouseBite();
		 setCrafted(null);
		 for(int i=0;i<9;i++){
			 setMouseBite(getCrafting(i));
			 DropMouseBite();
			 setCrafting(i, null);
		 }
		 spread_list = null;
	 }
	 public void ClickedArmor(int which, int leftrightmid, boolean shifted){	
		 if(which < 0 || which > 3)return;
		 InventoryContainer mousebite = null;
		 InventoryContainer ic = null;
		 if(leftrightmid == 0 || leftrightmid == 1){ //left
			 if(shifted){ //shifted, send to general inventory
				 mousebite = getMouseBite();
				 if(mousebite != null)mousebiteToInventory();
				 mousebite = getMouseBite();
				 if(mousebite == null){
					 setMouseBite(getArmor(which));
					 setArmor(which, null);
					 mousebiteToInventory();				 
				 }
			 }else{
				 mousebite = getMouseBite();
				 ic = getArmor(which);
				 if(mousebite == null){ //pick up!
					 setMouseBite(ic);
					 setArmor(which, null);
				 }else{
					 if(ic == null){ //put down?
						 Item it = mousebite.getItem();
						 if(it != null && it instanceof ItemArmor){
							 ItemArmor ia = (ItemArmor)it;
							 if(ia.armortype == which){
								 setArmor(which, mousebite);
								 ToDoList.onArmorPlaced(this, mousebite, which);
								 setMouseBite(null);
							 }
						 }
					 }else{ //swap
						 Item it = mousebite.getItem();
						 if(it != null && it instanceof ItemArmor){
							 ItemArmor ia = (ItemArmor)it;
							 if(ia.armortype == which){
								 setArmor(which, mousebite);
								 ToDoList.onArmorPlaced(this, mousebite, which);
								 setMouseBite(ic);
							 }
						 }
					 }					 
				 }				 
			 }
		 }else if(leftrightmid == 2){
			 setMouseBite(null);
			 setArmor(which, null);
		 }
	 }
	 public void DeleteMouseBite(){
		 setMouseBite(null);
	 }
	 public void SpitMouseBite(){
		 InventoryContainer mousebite = getMouseBite();
		 if(mousebite != null){
			 //Spit them out!
			 int i = mousebite.count;
			 while(i > 0){
				 EntityBlockItem e = (EntityBlockItem)world.createEntityByName(DangerZone.blockitemname, 
						 dimension, 
						 posx+(float)Math.sin(Math.toRadians(rotation_yaw_head))*2*(float)Math.cos(Math.toRadians(rotation_pitch_head)),
						 posy+(getHeight()*3/4) - (float)Math.sin(Math.toRadians(rotation_pitch_head))*2,
						 posz+(float)Math.cos(Math.toRadians(rotation_yaw_head))*2*(float)Math.cos(Math.toRadians(rotation_pitch_head)));
				 if(e != null){
					 if(mousebite.count == 1){
						 e.fill(mousebite); //preserves moreInventory field
					 }else{
						 e.fill(mousebite.bid, mousebite.iid, 1, mousebite.currentuses, mousebite.attributes, mousebite.icmeta);
					 }
					 e.rotation_pitch = DangerZone.server_world.rand.nextInt(360);
					 e.rotation_yaw = DangerZone.server_world.rand.nextInt(360);
					 e.rotation_roll = DangerZone.server_world.rand.nextInt(360);
					 e.motionx = (float)Math.sin(Math.toRadians(rotation_yaw_head))*(float)Math.cos(Math.toRadians(rotation_pitch_head))*2; 
					 e.motiony = -(float)Math.sin(Math.toRadians(rotation_pitch_head));
					 e.motionz = (float)Math.cos(Math.toRadians(rotation_yaw_head))*(float)Math.cos(Math.toRadians(rotation_pitch_head))*2;
					 world.spawnEntityInWorld(e);
				 }
				 i--;
			 }
			 setMouseBite(null);
		 }
	 }
	 
	 public void SpitOneHotbar(){
		 InventoryContainer ic = getHotbar(gethotbarindex());
		 if(getGameMode() == GameModes.LIMBO)return;
		 if(ic != null){
			 //Spit one out!
			 if(ic.count > 0){
				 EntityBlockItem e = (EntityBlockItem)world.createEntityByName(DangerZone.blockitemname, 
						 dimension, 
						 posx+(float)Math.sin(Math.toRadians(rotation_yaw_head))*2*(float)Math.cos(Math.toRadians(rotation_pitch_head)),
						 posy+(getHeight()*3/4) - (float)Math.sin(Math.toRadians(rotation_pitch_head))*2,
						 posz+(float)Math.cos(Math.toRadians(rotation_yaw_head))*2*(float)Math.cos(Math.toRadians(rotation_pitch_head)));
				 if(e != null){
					 if(ic.count == 1){
						 e.fill(ic); //preserves moreInventory field
					 }else{
						 e.fill(ic.bid, ic.iid, 1, ic.currentuses, ic.attributes, ic.icmeta);
					 }
					 e.rotation_pitch = DangerZone.server_world.rand.nextInt(360);
					 e.rotation_yaw = DangerZone.server_world.rand.nextInt(360);
					 e.rotation_roll = DangerZone.server_world.rand.nextInt(360);
					 e.motionx = (float)Math.sin(Math.toRadians(rotation_yaw_head))*(float)Math.cos(Math.toRadians(rotation_pitch_head))*2; 
					 e.motiony = -(float)Math.sin(Math.toRadians(rotation_pitch_head));
					 e.motionz = (float)Math.cos(Math.toRadians(rotation_yaw_head))*(float)Math.cos(Math.toRadians(rotation_pitch_head))*2;
					 world.spawnEntityInWorld(e);
				 }

			 }
			 //always decrement because moreInventory might not be null, and weirdness happens when multiple entities point to it.
			 //If they are creative mode, they can get more!
			 if(ic.count > 1){
				 ic.count--;
			 }else if(ic.count == 1){ //it was just tossed out
				 ic = null;
			 }
			 setHotbar(gethotbarindex(), ic);
		 }
	 }
	 
	 public void DropMouseBite(){
		 InventoryContainer mousebite = getMouseBite();
		 if(mousebite != null){
			 //Spit them out!
			 int i = mousebite.count;
			 while(i > 0){
				 EntityBlockItem e = (EntityBlockItem)world.createEntityByName(DangerZone.blockitemname, 
						 dimension, 
						 posx+(float)Math.sin(Math.toRadians(rotation_yaw_head))*2*(float)Math.cos(Math.toRadians(rotation_pitch_head)),
						 posy+(getHeight()*3/4) - (float)Math.sin(Math.toRadians(rotation_pitch_head))*2,
						 posz+(float)Math.cos(Math.toRadians(rotation_yaw_head))*2*(float)Math.cos(Math.toRadians(rotation_pitch_head)));
				 if(e != null){
					 if(mousebite.count == 1){
						 e.fill(mousebite); //preserves moreInventory field
					 }else{
						 e.fill(mousebite.bid, mousebite.iid, 1, mousebite.currentuses, mousebite.attributes, mousebite.icmeta);
					 }
					 e.rotation_pitch = DangerZone.server_world.rand.nextInt(360);
					 e.rotation_yaw = DangerZone.server_world.rand.nextInt(360);
					 e.rotation_roll = DangerZone.server_world.rand.nextInt(360);
					 e.motionx = 0; 
					 e.motiony = 0;
					 e.motionz = 0;
					 world.spawnEntityInWorld(e);
				 }
				 i--;
			 }
			 setMouseBite(null);
		 }
	 }

	 //shift-click should call this...
	 public void mousebiteToInventory(){

		 InventoryContainer mousebite = getMouseBite();

		 if(mousebite == null)return;
		 if((mousebite.iid == 0 && mousebite.bid == 0) || mousebite.count <= 0){
			 setMouseBite(null);
			 return;
		 }
		 int i, df;
		 InventoryContainer ic = null;

		 //first search is to see if we can add to a stack
		 for(i=0;i<50;i++){
			 ic = getInventory(i);
			 if(ic != null){
				 if(ic.bid == mousebite.bid && ic.iid == mousebite.iid){
					 df = 0;
					 if(ic.bid != 0){
						 df = Blocks.getMaxStack(ic.bid) - ic.count;
					 }else{
						 df = Items.getMaxStack(ic.iid) - ic.count;
					 }
					 if(df > 0){
						 if(df >= mousebite.count){
							 ic.count += mousebite.count;
							 setInventoryChanged(i);
							 mousebite = null;
							 break;
						 }
						 ic.count += df;
						 setInventoryChanged(i);
						 mousebite.count -= df;
					 }
					 if(mousebite.count <= 0){
						 mousebite = null;
						 break;
					 }
				 }
			 }			
		 }
		 if(mousebite == null){
			 setMouseBite(null);
			 return;
		 }
		 //check the hotbar too!
		 for(i=0;i<10;i++){
			 ic = getHotbar(i);
			 if(ic != null){
				 if(ic.bid == mousebite.bid && ic.iid == mousebite.iid){
					 df = 0;
					 if(ic.bid != 0){
						 df = Blocks.getMaxStack(ic.bid) - ic.count;
					 }else{
						 df = Items.getMaxStack(ic.iid) - ic.count;
					 }
					 if(df > 0){
						 if(df >= mousebite.count){
							 ic.count += mousebite.count;
							 setHotbarChanged(i);
							 mousebite = null;
							 break;
						 }
						 ic.count += df;
						 setHotbarChanged(i);
						 mousebite.count -= df;
					 }
					 if(mousebite.count <= 0){
						 mousebite = null;
						 break;
					 }
				 }
			 }			
		 }
		 if(mousebite == null){
			 setMouseBite(null);
			 return;
		 }

		 //still have leftover. Find an empty slot
		 for(i=0;i<50;i++){
			 ic = getInventory(i);
			 if(ic == null){
				 setInventory(i, mousebite);
				 mousebite = null;
				 break;
			 }			
		 }
		 if(mousebite == null){
			 setMouseBite(null);
			 return;
		 }
		 for(i=0;i<10;i++){
			 ic = getHotbar(i);
			 if(ic == null){
				 setHotbar(i, mousebite);
				 mousebite = null;
				 break;
			 }			
		 }
		 if(mousebite == null){
			 setMouseBite(null);
			 return;
		 }
		 setMouseBiteChanged();
	 }
	 
	 public void mousebiteToInventory(Entity ent){
		 //does NOT check hotbar! Used for chests and desks...
		 InventoryContainer mousebite = getMouseBite();

		 if(mousebite == null)return;
		 if((mousebite.iid == 0 && mousebite.bid == 0) || mousebite.count <= 0){
			 setMouseBite(null);
			 return;
		 }
		 int i, df;
		 InventoryContainer ic = null;

		 //first search is to see if we can add to a stack
		 for(i=0;i<50;i++){
			 ic = ent.getInventory(i);
			 if(ic != null){
				 if(ic.bid == mousebite.bid && ic.iid == mousebite.iid){
					 df = 0;
					 if(ic.bid != 0){
						 df = Blocks.getMaxStack(ic.bid) - ic.count;
					 }else{
						 df = Items.getMaxStack(ic.iid) - ic.count;
					 }
					 if(df > 0){
						 if(df >= mousebite.count){
							 ic.count += mousebite.count;
							 ent.setInventoryChanged(i);
							 mousebite = null;
							 break;
						 }
						 ic.count += df;
						 ent.setInventoryChanged(i);
						 mousebite.count -= df;
					 }
					 if(mousebite.count <= 0){
						 mousebite = null;
						 break;
					 }
				 }
			 }			
		 }
		 if(mousebite == null){
			 setMouseBite(null);
			 return;
		 }


		 //still have leftover. Find an empty slot
		 for(i=0;i<50;i++){
			 ic = ent.getInventory(i);
			 if(ic == null){
				 ent.setInventory(i, mousebite);
				 mousebite = null;
				 break;
			 }			
		 }
		 if(mousebite == null){
			 setMouseBite(null);
			 return;
		 }

		 setMouseBiteChanged();
	 }
	 
	 public void sethotbarindex(int f){
		 if(f >= 0 && f <= 9){
			 if(!world.isServer && this == DangerZone.player){
				 clienthotbarindex = f;
			 }else{
				 setVarInt(3, f);
			 }
		 }
	 }

	 public int gethotbarindex(){
		 if(world == null)return 0;
		 if(!world.isServer && this == DangerZone.player)return clienthotbarindex;
		 return getVarInt(3);
	 }
	 
	 public void make_magic(float power, int type){
		 int whichattr = 0;
		 float attrampf = power * getMaxMagic();
		 int attramp = 0;

		 if(attrampf > getMagic())attrampf = getMagic();
		 if(attrampf < 1)return; //oops!
		 attramp = (int)attrampf;
		 //System.out.printf("attramp = %d\n",  attramp);
		 if(attramp < 0)return;

		 spells++;
		 server_thread.sendStatsToPlayer();

		 setMagic(getMagic()-attrampf);

		 if(type == 1)whichattr = 1; //heal
		 if(type == 2)whichattr = 0; //harm
		 if(type == 3)whichattr = 2; //destroy

		 EntityMagic e = (EntityMagic)world.createEntityByName("DangerZone:EntityMagic", 
				 dimension, 
				 posx+(float)Math.sin(Math.toRadians(rotation_yaw_head))*(getWidth()+1)*(float)Math.cos(Math.toRadians(rotation_pitch_head)),
				 posy+(getHeight()*9/10) - (float)Math.sin(Math.toRadians(rotation_pitch_head))*(getWidth()+1),
				 posz+(float)Math.cos(Math.toRadians(rotation_yaw_head))*(getWidth()+1)*(float)Math.cos(Math.toRadians(rotation_pitch_head)));
		 if(e != null){
			 e.init();
			 e.setBID(1);//spawn the head of the magic stream!
			 e.setIID(-1); //tell it special! Cast spell!
			 e.setVarInt(2, attramp);
			 e.setVarInt(3, whichattr); //harm or heal! 0=harm, 1=heal
			 e.thrower = this;
			 e.setDirectionAndVelocity(
					 (float)Math.sin(Math.toRadians(rotation_yaw_head))*(float)Math.cos(Math.toRadians(rotation_pitch_head)), 
					 -(float)Math.sin(Math.toRadians(rotation_pitch_head)),
					 (float)Math.cos(Math.toRadians(rotation_yaw_head))*(float)Math.cos(Math.toRadians(rotation_pitch_head)),
					 5f, 0.05f);
			 world.spawnEntityInWorld(e);
			 Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSparkle", 10 + attramp, dimension, 
					 posx+(float)Math.sin(Math.toRadians(rotation_yaw_head))*(getWidth()+1)*(float)Math.cos(Math.toRadians(rotation_pitch_head)),
					 posy+(getHeight()*9/10), 
					 posz+(float)Math.cos(Math.toRadians(rotation_yaw_head))*(getWidth()+1)*(float)Math.cos(Math.toRadians(rotation_pitch_head)));	
		 }
	 }
}
