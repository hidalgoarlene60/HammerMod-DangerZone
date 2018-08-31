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

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;


import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.PlayerKeyEvent;
import dangerzone.TextureMapper;
import dangerzone.World;
import dangerzone.items.Item;


public class DroneClaw extends EntityLiving {
	
	public Entity thrower = null;
	public Entity caught = null;
	public float open = 0;
	
	public boolean open_down;
	public boolean close_down;
	public boolean oc_sound;
	
	public	DroneClaw(World w){
		super(w);
		maxrenderdist = 400; //in blocks
		this.height = 0.75f;
		this.width = 0.75f;
		uniquename = "DangerZone:DroneClaw";
		setMaxHealth(60.0f);
		setHealth(60.0f);
		setDefense(1.0f);
		takesFallDamage = false;
		setExperience(10);
		daytimedespawn = false;
		nighttimedespawn = false;
		canFly = true;
		setFlying(true);
		always_draw = true;
		temperament = Temperament.HOSTILE;
		open_down = close_down = oc_sound = false;
		ignoreCollisions = true;
		tower_defense_enable = false;
	}
	
	public float getOpen(){
		return getVarFloat(10);
	}

	
	public void doEntityAction(float deltaT){	
		
		if(thrower == null || thrower.deadflag){
			//System.out.printf("claw has no thrower\n");
			deadflag = true;
			super.doEntityAction(deltaT);
			return;
		}
		
		float speed = (float) Math.sqrt((motionx*motionx)+(motionz*motionz));
		//System.out.printf("claw here!\n");
		if(speed > 0.001f){
			float cdir = (float) Math.toRadians(rotation_yaw);
			float tdir = (float) Math.atan2(motionx, motionz);
			float ddiff = tdir - cdir;
			while(ddiff>Math.PI)ddiff -= Math.PI*2;
			while(ddiff<-Math.PI)ddiff += Math.PI*2;
			rotation_yaw += (ddiff*180f/Math.PI)/5f;
			float pitch = speed * 90;
			if(pitch > 90)pitch = 90;
			rotation_pitch = pitch;
			rotation_yaw_motion = 0;
		}else{
			rotation_yaw_motion = 1;
			rotation_pitch = 0;
		}
		   	
     	motiony = 0.20f * deltaT; //counter gravity so we are stable
 
     	PlayerKeyEvent plk = null;
     	
     	while(true){
     		plk = getNextKeyEvent();
     		if(plk == null)break;    		
     		//System.out.printf("Key %d is %s\n", plk.key, plk.isDown?"down":"up");
     		
     		if(plk.key == Keyboard.KEY_ESCAPE){
     			deadflag = true;	
     			return;
     		}
     		
     		if(plk.key == Keyboard.KEY_LEFT){
     			if(plk.isDown){
     				close_down = true;
     			}else{
     				close_down = false;
     			}
     		}
     		if(plk.key == Keyboard.KEY_RIGHT){
     			if(plk.isDown){
     				open_down = true;
     			}else{
     				open_down = false;
     			}
     		}
 
 
     	}
     	
		if(close_down){
     		if(!oc_sound){
     			play_oc_sound();
     			oc_sound = true;
     		}
 			open += 3;
 			if(open > 30)open = 30;
 			setVarFloat(10, open);
 			//yes, its backwards, positive is claw closed! 	
 
 			catch_something();

 		}
 		if(open_down){
     		if(!oc_sound){
     			play_oc_sound();
     			oc_sound = true;
     		}
 			open -= 3;
 			if(open < -50)open = -50;
 			setVarFloat(10, open);
 			if(open < 0 && caught != null){
 				release();
 			}
 		}
     	
     	if(!close_down && !open_down){
     		oc_sound = false;
     	}
     	
     	update_caught();
  
	}
	
	public void play_oc_sound(){
		world.playSound("DangerZone:motor_openclose", dimension, posx, posy, posz, 1, 1);
	}
	
	
	public void doDeathDrops(){
		//nothing!
	}
	
	public void release(){
		if(caught != null){
			if(caught instanceof EntityBlockItem){
				InventoryContainer ic = caught.getInventory(0);
				if(ic.count == 1){
					Item it = ic.getItem();
					if(it != null){
						rotation_pitch += 90; //pointing down! Yes... plus is down... it's backwards...
						rotation_pitch_head += 90; //pointing down!
						if(it.onRightClick(this, null, ic)){ //activate it! 
							caught.deadflag = true;
						}
						rotation_pitch -= 90; //pointing back up!
						rotation_pitch_head -= 90; //pointing back up!
					}
				}
			}
		}
		caught = null; //release!
	}

	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "DroneClawtexture.png");
		}
		return texture;
	}
	
	public void update_caught(){
		if(caught == null)return;
		
		if(caught.deadflag){
			caught = null;
			return;
		}
		

		caught.posy = posy+0.25f*Math.cos(Math.toRadians(rotation_pitch));
		caught.posx = posx;
		caught.posz = posz;
		caught.posx += 1.5f*Math.sin(Math.toRadians(rotation_pitch))*Math.sin(Math.toRadians(rotation_yaw));
		caught.posz += 1.5f*Math.sin(Math.toRadians(rotation_pitch))*Math.cos(Math.toRadians(rotation_yaw));
		caught.motionx = motionx;
		caught.motiony = motiony;
		caught.motionz = motionz;

		
		if(caught instanceof Player){ //picked up a player! Send new position and such back to them.
			Player pl = (Player)caught;
			pl.server_thread.sendPositionAndVelocityUpdateToPlayer(pl);			
		}
		
		if(caught instanceof EntityBlockItem){
			((EntityBlockItem)caught).deathtimer = 10 * 120; //reset!
		}
		if(caught instanceof EntityLiving){
			((EntityLiving)caught).fallcount = 0; //reset!
		}
		
		if(caught.getOnFire() != 0){
			caught.setOnFire(0);
		}
	}
	
	public void catch_something(){
		if(caught != null)return;
		
		if(open > 0 && open < 30){
			List<Entity> nearby_list = null;
			//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
			nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange(this.getWidth()*3, dimension, posx, posy, posz);
			if(nearby_list != null){
				if(!nearby_list.isEmpty()){
					Entity e = null;
					ListIterator<Entity> li;
					//Sort them out based on size and distance
					Collections.sort(nearby_list, this.TargetSorter);
					li = nearby_list.listIterator();
					while(li.hasNext()){
						e = (Entity)li.next();
						if(e != this){
							if(e instanceof EntityBlockItem){ 
								if(e.posy > posy-1 && e.posy < posy+getHeight()+1){
									caught = e;
									break;
								}
							}
							if(e instanceof EntityLiving){ 
								if(e.posy+e.getHeight() > posy-1 && e.posy < posy+getHeight()+1){
									if(e.getHeight() * e.getWidth() < 5.25f){ //Moose or smaller!
										caught = e;
										break;
									}
								}
							}
						}
					}								
				}			
			}
		}
	}

}