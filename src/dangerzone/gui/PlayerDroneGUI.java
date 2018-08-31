package dangerzone.gui;
import java.util.List;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;


import dangerzone.DangerZone;
import dangerzone.Effects;
import dangerzone.GameModes;
import dangerzone.entities.Drone;
import dangerzone.entities.Entity;

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
public class PlayerDroneGUI extends GuiInterface {

	Drone droneentity = null;
	int connection_tries = 300;
	boolean droneview = false;
	boolean dronecontrol = true;
	float accel;
	int gofasttimer, flytimer, gofly, gofast;
	
	public PlayerDroneGUI(){
		super();
		grab_mouse = true;
		droneview = false;
		accel = 0;
		gofasttimer = flytimer = gofast = gofly = 0;
	}
	
	/*
	 * Fly a drone!
	 * 
	 */
	public void process(){
		
		if(droneview){
			textAt(50, 100, "DRONE VIEW");
		}else{
			textAt(50, 100, "PLAYER VIEW");
			if(dronecontrol){
				textAt(50, 60, "DRONE CONTROL");
			}else{
				textAt(50, 60, "PLAYER CONTROL");
			}
		}
		
		smallTextAt(50, 160, "Arrow up/down - Claw up/down");
		smallTextAt(50, 190, "Arrow left/right - Claw open/close");
		smallTextAt(50, 220, "T - toggle Player/Drone View");
		smallTextAt(50, 250, "R - toggle Player/Drone Control");
				
		
		if(droneentity == null){
			textAt(120, 120, "Connecting....");
			connection_tries--;
			if(connection_tries <= 0){
				ImAllDone();
				return;
			}
			
			List<Entity> nearby_list = null;
			//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
			nearby_list = DangerZone.entityManager.findEntitiesInRange(10, DangerZone.player.dimension, DangerZone.player.posx, DangerZone.player.posy, DangerZone.player.posz);
			if(nearby_list != null){
				if(!nearby_list.isEmpty()){
					Entity e = null;
					ListIterator<Entity> li;
					li = nearby_list.listIterator();
					while(li.hasNext()){
						e = (Entity)li.next();												
						if(e instanceof Drone){
							//DangerZone.wr.ViewFromEntity = e;
							droneentity = (Drone)e;
							break;
						}		
					}					
				}								
			}			
		}
		
		if(droneentity == null)return;
		if(droneentity.deadflag){
			ImAllDone();
			return;
		}
		
		
		if(droneview){
		
		//drone is controlled through the player, so get inputs and set them (without actually moving the player!)
		
			while (DangerZone.K_next()) {	

				//toggle view!
				if (DangerZone.K_getEventKey() == Keyboard.KEY_T && DangerZone.K_isKeyDown(Keyboard.KEY_T)){
					droneview = false;
					DangerZone.wr.ViewFromEntity = DangerZone.player;
				}

				//Look at self, of course...
				if (DangerZone.K_getEventKey() == Keyboard.KEY_F5 && DangerZone.K_isKeyDown(Keyboard.KEY_F5)){
					//show me!!!
					if(DangerZone.f5_front){
						DangerZone.f5_front = false;
						DangerZone.f5_back = true;
					}else{
						if(DangerZone.f5_back){
							DangerZone.f5_back = false;
						}else{
							DangerZone.f5_front = true;
						}
					}
				}
				
				//forward all key events to the drone. It can figure out what to do with them...
				DangerZone.player.server_connection.sendKeyEventToEntity(droneentity.entityID, DangerZone.K_getEventKey(), DangerZone.K_isKeyDown(DangerZone.K_getEventKey()));

			}
			
			if (DangerZone.K_isKeyDown(Keyboard.KEY_SPACE)){
				DangerZone.player.setUp(true);
			}else{
				DangerZone.player.setUp(false);
			}
			if (DangerZone.K_isKeyDown(Keyboard.KEY_LSHIFT)){
				DangerZone.player.setDown(true);
			}else{
				DangerZone.player.setDown(false);
			}
			if (DangerZone.K_isKeyDown(Keyboard.KEY_W)){
				DangerZone.player.setForward(true);
			}else{
				DangerZone.player.setForward(false);
			}
			if (DangerZone.K_isKeyDown(Keyboard.KEY_S)){
				DangerZone.player.setBackward(true);
			}else{
				DangerZone.player.setBackward(false);
			}
			if (DangerZone.K_isKeyDown(Keyboard.KEY_A)){
				DangerZone.player.setLeft(true);
			}else{
				DangerZone.player.setLeft(false);
			}
			if (DangerZone.K_isKeyDown(Keyboard.KEY_D)){
				DangerZone.player.setRight(true);
			}else{
				DangerZone.player.setRight(false);
			}

			//emergency exit!
			if (DangerZone.K_isKeyDown(Keyboard.KEY_ESCAPE)){
				ImAllDone();
				return;
			}

			//adjust where looking with mouse...
			//makes player look around... kinda funny! :)
			float dx, dy;		
			while(DangerZone.M_next()){
				//System.out.printf("Mouse!\n");
				dx = DangerZone.M_getDX();
				dx *= 0.1f + (0.01f * (float)DangerZone.mouseSensitivity);
				dy = DangerZone.M_getDY();
				dy *= 0.1f + (0.01f * (float)DangerZone.mouseSensitivity);

				DangerZone.player.rotation_yaw_head += dx; //normal
				if(DangerZone.player.rotation_yaw_head < 0)DangerZone.player.rotation_yaw_head += 360;
				DangerZone.player.rotation_yaw_head = DangerZone.player.rotation_yaw_head%360;		
				DangerZone.player.rotation_pitch_head -= dy;
				DangerZone.player.rotation_pitch_head = DangerZone.player.rotation_pitch_head%360;

				if(DangerZone.player.rotation_pitch_head > 180){
					if(DangerZone.player.rotation_pitch_head < 270)DangerZone.player.rotation_pitch_head = 270;
				}else{
					if(DangerZone.player.rotation_pitch_head > 90)DangerZone.player.rotation_pitch_head = 90;
				}
				
				//send button clicks to entity!
				if(DangerZone.M_getEventButtonState()){	
					//System.out.printf("DOWN: %d\n", Mouse.getEventButton());
					if(DangerZone.M_getEventButton() >= 0)DangerZone.player.server_connection.sendMouseEventToEntity(droneentity.entityID, DangerZone.M_getEventButton(), true);
				}else{
					//Mouse button released!
					if(DangerZone.M_getEventButton() >= 0)DangerZone.player.server_connection.sendMouseEventToEntity(droneentity.entityID, DangerZone.M_getEventButton(), false);
				}

			}
		}else{
			
			//minimal player functionality while flying drone.
			//not sure I like the way I did this though...
			//
			accel -= 0.05f;
			if(accel < 0.1f)accel = 0.1f;
			if(flytimer > 0)flytimer--;
			if(gofasttimer > 0)gofasttimer--;
			
			while (DangerZone.K_next()) {	
				if (DangerZone.K_getEventKey() == Keyboard.KEY_W && DangerZone.K_isKeyDown(Keyboard.KEY_W)){
					if(gofasttimer != 0)gofast = 1;
					gofasttimer = 20;
				}
				
				if (DangerZone.K_getEventKey() == Keyboard.KEY_SPACE && DangerZone.K_isKeyDown(Keyboard.KEY_SPACE)){
					if(flytimer != 0)gofly = 1;
					flytimer = 20;
				}
				
				//Look at self, of course...
				if (DangerZone.K_getEventKey() == Keyboard.KEY_F5 && DangerZone.K_isKeyDown(Keyboard.KEY_F5)){
					//show me!!!
					if(DangerZone.f5_front){
						DangerZone.f5_front = false;
						DangerZone.f5_back = true;
					}else{
						if(DangerZone.f5_back){
							DangerZone.f5_back = false;
						}else{
							DangerZone.f5_front = true;
						}
					}
				}
				
				//toggle view!
				if (DangerZone.K_getEventKey() == Keyboard.KEY_T && DangerZone.K_isKeyDown(Keyboard.KEY_T)){
					droneview = true;
					DangerZone.wr.ViewFromEntity = droneentity;
				}
				//toggle control!
				if (DangerZone.K_getEventKey() == Keyboard.KEY_R && DangerZone.K_isKeyDown(Keyboard.KEY_R)){
					dronecontrol = !dronecontrol;
				}
				
				//forward all up/down key events to the drone. It can figure out what to do with them...
				DangerZone.player.server_connection.sendKeyEventToEntity(droneentity.entityID, DangerZone.K_getEventKey(), DangerZone.K_isKeyDown(DangerZone.K_getEventKey()));
				
			}
			
			if(dronecontrol){
				if (DangerZone.K_isKeyDown(Keyboard.KEY_SPACE)){
					DangerZone.player.setUp(true);
				}else{
					DangerZone.player.setUp(false);
				}
				if (DangerZone.K_isKeyDown(Keyboard.KEY_LSHIFT)){
					DangerZone.player.setDown(true);
				}else{
					DangerZone.player.setDown(false);
				}
				if (DangerZone.K_isKeyDown(Keyboard.KEY_W)){
					DangerZone.player.setForward(true);
				}else{
					DangerZone.player.setForward(false);
				}
				if (DangerZone.K_isKeyDown(Keyboard.KEY_S)){
					DangerZone.player.setBackward(true);
				}else{
					DangerZone.player.setBackward(false);
				}
				if (DangerZone.K_isKeyDown(Keyboard.KEY_A)){
					DangerZone.player.setLeft(true);
				}else{
					DangerZone.player.setLeft(false);
				}
				if (DangerZone.K_isKeyDown(Keyboard.KEY_D)){
					DangerZone.player.setRight(true);
				}else{
					DangerZone.player.setRight(false);
				}
			}else{
				if (DangerZone.K_isKeyDown(Keyboard.KEY_SPACE)){
					if(DangerZone.player.getGameMode() == GameModes.GHOST || DangerZone.player.isFlying()){
						DangerZone.player.motiony += 0.055f*DangerZone.deltaT*accel;
						accel += 0.1f;
						if(accel > 1)accel = 1;
					}else{
						if(DangerZone.player.getOnGround() || DangerZone.player.getInLiquid()){
							DangerZone.player.jump();
						}
						if(gofly != 0)DangerZone.player.tryfly();
					}
				}else{
					gofly = 0;
				}

				if (DangerZone.K_isKeyDown(Keyboard.KEY_LSHIFT)){
					if(DangerZone.player.getGameMode() == GameModes.GHOST || DangerZone.player.isFlying()){
						DangerZone.player.motiony -= 0.055f*DangerZone.deltaT*accel;
						accel += 0.1f;
						if(accel > 1)accel = 1;
					}
				}

				if (DangerZone.K_isKeyDown(Keyboard.KEY_W)){
					float speed = 0.042f*accel;
					if(DangerZone.player.getGameMode() != GameModes.GHOST && 
							(DangerZone.world.getblock(DangerZone.player.dimension, (int)DangerZone.player.posx, (int)DangerZone.player.posy+2, (int)DangerZone.player.posz) != 0
							|| DangerZone.world.getblock(DangerZone.player.dimension, (int)DangerZone.player.posx, (int)DangerZone.player.posy+3, (int)DangerZone.player.posz) != 0 ))speed *= 0.70f;
					accel += 0.2f;
					if(accel > 1)accel = 1;
					if(DangerZone.player.getGameMode() == GameModes.GHOST || DangerZone.player.isFlying()){
						speed *= 4.0f;
					}else{
						if(gofast != 0)speed *= 2.5f;
					}
					float effectspeed;
					effectspeed = DangerZone.player.getTotalEffect(Effects.SPEED);
					if(effectspeed > 1){
						speed *= effectspeed;
					}
					effectspeed = DangerZone.player.getTotalEffect(Effects.SLOWNESS);
					if(effectspeed > 1){
						speed /= effectspeed;
					}
					DangerZone.player.motionx += speed*Math.cos(Math.toRadians(DangerZone.player.rotation_yaw_head-90))*DangerZone.deltaT;
					DangerZone.player.motionz += speed*Math.sin(Math.toRadians(DangerZone.player.rotation_yaw_head-90))*DangerZone.deltaT;
				}else{
					gofast = 0;
				}
				if (DangerZone.K_isKeyDown(Keyboard.KEY_S)){
					float speed = 0.030f*accel;
					accel += 0.1f;
					if(accel > 1)accel = 1;
					if(DangerZone.player.getGameMode() == GameModes.GHOST)speed *= 2.5f;
					float effectspeed;
					effectspeed = DangerZone.player.getTotalEffect(Effects.SPEED);
					if(effectspeed > 1){
						speed *= effectspeed;
					}
					effectspeed = DangerZone.player.getTotalEffect(Effects.SLOWNESS);
					if(effectspeed > 1){
						speed /= effectspeed;
					}
					DangerZone.player.motionx -= speed*Math.cos(Math.toRadians(DangerZone.player.rotation_yaw_head-90))*DangerZone.deltaT;
					DangerZone.player.motionz -= speed*Math.sin(Math.toRadians(DangerZone.player.rotation_yaw_head-90))*DangerZone.deltaT;
				}
				if (DangerZone.K_isKeyDown(Keyboard.KEY_A)){
					float speed = 0.030f*accel;
					accel += 0.1f;
					if(accel > 1)accel = 1;
					if(DangerZone.player.getGameMode() == GameModes.GHOST)speed *= 2.5f;
					float effectspeed;
					effectspeed = DangerZone.player.getTotalEffect(Effects.SPEED);
					if(effectspeed > 1){
						speed *= effectspeed;
					}
					effectspeed = DangerZone.player.getTotalEffect(Effects.SLOWNESS);
					if(effectspeed > 1){
						speed /= effectspeed;
					}
					DangerZone.player.motionx += speed*Math.cos(Math.toRadians(DangerZone.player.rotation_yaw_head-180))*DangerZone.deltaT;
					DangerZone.player.motionz += speed*Math.sin(Math.toRadians(DangerZone.player.rotation_yaw_head-180))*DangerZone.deltaT;
				}
				if (DangerZone.K_isKeyDown(Keyboard.KEY_D)){
					float speed = 0.030f*accel;
					accel += 0.1f;
					if(accel > 1)accel = 1;
					if(DangerZone.player.getGameMode() == GameModes.GHOST)speed *= 2.5f;
					float effectspeed;
					effectspeed = DangerZone.player.getTotalEffect(Effects.SPEED);
					if(effectspeed > 1){
						speed *= effectspeed;
					}
					effectspeed = DangerZone.player.getTotalEffect(Effects.SLOWNESS);
					if(effectspeed > 1){
						speed /= effectspeed;
					}
					DangerZone.player.motionx -= speed*Math.cos(Math.toRadians(DangerZone.player.rotation_yaw_head-180))*DangerZone.deltaT;
					DangerZone.player.motionz -= speed*Math.sin(Math.toRadians(DangerZone.player.rotation_yaw_head-180))*DangerZone.deltaT;
				}
			}
				
			//emergency exit!
			if (DangerZone.K_isKeyDown(Keyboard.KEY_ESCAPE)){
				ImAllDone();
				return;
			}
			
			//adjust where looking with mouse...
			//makes player look around... kinda funny! :)
			float dx, dy;		
			while(DangerZone.M_next()){
				//System.out.printf("Mouse!\n");
				dx = DangerZone.M_getDX();
				dx *= 0.1f + (0.01f * (float)DangerZone.mouseSensitivity);
				dy = DangerZone.M_getDY();
				dy *= 0.1f + (0.01f * (float)DangerZone.mouseSensitivity);

				DangerZone.player.rotation_yaw_head += dx; //normal
				if(DangerZone.player.rotation_yaw_head < 0)DangerZone.player.rotation_yaw_head += 360;
				DangerZone.player.rotation_yaw_head = DangerZone.player.rotation_yaw_head%360;		
				DangerZone.player.rotation_pitch_head -= dy;
				DangerZone.player.rotation_pitch_head = DangerZone.player.rotation_pitch_head%360;
		
				if(DangerZone.player.rotation_pitch_head > 180){
					if(DangerZone.player.rotation_pitch_head < 270)DangerZone.player.rotation_pitch_head = 270;
				}else{
					if(DangerZone.player.rotation_pitch_head > 90)DangerZone.player.rotation_pitch_head = 90;
				}
				
				if(dronecontrol){
					//send button clicks to entity!
					if(DangerZone.M_getEventButtonState()){	
						//System.out.printf("DOWN: %d\n", Mouse.getEventButton());
						if(DangerZone.M_getEventButton() >= 0)DangerZone.player.server_connection.sendMouseEventToEntity(droneentity.entityID, DangerZone.M_getEventButton(), true);
					}else{
						//Mouse button released!
						if(DangerZone.M_getEventButton() >= 0)DangerZone.player.server_connection.sendMouseEventToEntity(droneentity.entityID, DangerZone.M_getEventButton(), false);
					}	
				}

			}
		}

	}
	
	public void ImAllDone(){

		DangerZone.wr.ViewFromEntity = DangerZone.player;
		
		super.ImAllDone();
	}


}
