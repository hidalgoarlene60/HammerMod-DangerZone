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

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import dangerzone.PIDController;
import dangerzone.PlayerKeyEvent;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


public class Drone extends EntityLiving {
	
	public Entity thrower = null;
	public DroneClaw clawentity = null;
	
	PIDController pidy = null;
	float tiltfb;
	float tiltlr;
	boolean lefthit;
	boolean righthit;
	boolean forwardhit;
	boolean backwardhit;
	boolean uphit;
	boolean downhit;
	int clawpos = 0;
	boolean down_isdown;
	boolean up_isdown;
	boolean updown_sound;
	
	public	Drone(World w){
		super(w);
		maxrenderdist = 400; //in blocks
		this.height = 0.75f;
		this.width = 1.25f;
		uniquename = "DangerZone:Drone";
		setMaxHealth(20.0f);
		setHealth(20.0f);
		setDefense(1.0f);
		takesFallDamage = false;
		setExperience(10);
		daytimedespawn = false;
		nighttimedespawn = false;
		canFly = true;
		setFlying(true);
		tiltfb = tiltlr = 0;
		lefthit = righthit = false;
		forwardhit = backwardhit = false;
		uphit = downhit = false;
		always_draw = true;
		temperament = Temperament.HOSTILE;
		clawpos = 0;
		down_isdown = up_isdown = updown_sound = false;
		tower_defense_enable = false;
	}
	
	public void init(){
		super.init();
		eyeheight = 0.0625f;
	}
	
	public void setDirectionAndVelocity(float x, float y, float z, float velocity, float variability){
		motionx = x*(velocity + ((world.rand.nextFloat()-world.rand.nextFloat())*variability*velocity));
		motiony = y*(velocity + ((world.rand.nextFloat()-world.rand.nextFloat())*variability*velocity));
		motionz = z*(velocity + ((world.rand.nextFloat()-world.rand.nextFloat())*variability*velocity));
	}
	
	public float getTiltLR(){
		return getVarFloat(10);
	}
	
	public float getTiltFB(){
		return getVarFloat(9);
	}
	
	public void update(float deltaT){
		rotation_pitch = getTiltFB()*25;
		super.update(deltaT);
	}
	
	
	public void doEntityAction(float deltaT){	
		
		if(thrower == null || thrower.deadflag && !deadflag){
			deadflag = true;
			Utils.doDropRand(world, 0, Items.drone.itemID, 1, dimension, posx, posy, posz);    	
			super.doEntityAction(deltaT);
			return;
		}
		
		if(pidy == null){
			pidy = new PIDController(0.05f, 3f/*p*/, 0.25f/*i*/, 0.90f/*ifade*/, 7.1f/*d*/, -1.5f, 1.5f); //not bad. good enough...
			pidy.setHoldpos(posy+motiony*deltaT);
		}
		
		if(clawentity != null){
			if(clawentity.deadflag){
				clawentity = null;
			}
		}		
		
		//We have a driver! It's a player!
	   	float faster = deltaT * 2;
		//First, let's turn with rider...
		float cdir = (float) Math.toRadians(rotation_yaw);
    	float tdir = (float) Math.toRadians(thrower.rotation_yaw);
    	float ddiff = tdir - cdir;
    	while(ddiff>Math.PI)ddiff -= Math.PI*2;
    	while(ddiff<-Math.PI)ddiff += Math.PI*2;
    	rotation_yaw_motion += (ddiff*180f/Math.PI)/6f;
    	
		cdir = (float) Math.toRadians(rotation_yaw_head);
    	tdir = (float) Math.toRadians(thrower.rotation_yaw_head);
    	ddiff = tdir - cdir;
    	while(ddiff>Math.PI)ddiff -= Math.PI*2;
    	while(ddiff<-Math.PI)ddiff += Math.PI*2;
    	rotation_yaw_head += (ddiff*180f/Math.PI)/5f;
    	
    	rotation_pitch_head = thrower.rotation_pitch_head;
    	
    	tiltlr *= 0.80f;
    	if(Math.abs(tiltlr) < 0.01f)tiltlr = 0;
    	tiltfb *= 0.80f;
    	if(Math.abs(tiltfb) < 0.01f)tiltfb = 0;
    	 	
    	//now let's go...
    	if(thrower.getForward()){
    		motionx += moveSpeed*Math.sin(tdir)*faster;
    		motionz += moveSpeed*Math.cos(tdir)*faster;
    		tiltfb += 0.25f;
    		if(tiltfb > 1)tiltfb = 1;
    		if(!forwardhit)world.playSound("DangerZone:drone", dimension, posx, posy, posz, 1, 1);
    		forwardhit = true;
    	}else{
    		forwardhit = false;
    	}
    	if(thrower.getBackward()){
    		motionx -= moveSpeed*Math.sin(tdir)*faster;
    		motionz -= moveSpeed*Math.cos(tdir)*faster;
       		tiltfb -= 0.25f;
    		if(tiltfb < -1)tiltfb = -1; 
    		if(!backwardhit)world.playSound("DangerZone:drone", dimension, posx, posy, posz, 1, 1);
    		backwardhit = true;
    	}else{
    		backwardhit = false;
    	}
    	if(thrower.getLeft()){
    		motionx += moveSpeed*Math.sin(tdir+Math.PI/2)*faster;
    		motionz += moveSpeed*Math.cos(tdir+Math.PI/2)*faster;
       		tiltlr -= 0.25f;
    		if(tiltlr < -1)tiltlr = -1;
       		if(!lefthit)world.playSound("DangerZone:drone", dimension, posx, posy, posz, 1, 1);
    		lefthit = true;
    	}else{
    		lefthit = false;
    	}
    	if(thrower.getRight()){
    		motionx -= moveSpeed*Math.sin(tdir+Math.PI/2)*faster;
    		motionz -= moveSpeed*Math.cos(tdir+Math.PI/2)*faster;
    		tiltlr += 0.25f;
    		if(tiltlr > 1)tiltlr = 1;
       		if(!righthit)world.playSound("DangerZone:drone", dimension, posx, posy, posz, 1, 1);
    		righthit = true;
    	}else{
    		righthit = false;
    	}
    	
    	setVarFloat(9, tiltfb);
   		setVarFloat(10, tiltlr);
  
       	
    	boolean hover = true;
    	if(thrower.getUp()){
    		motiony += 0.05f*faster;
    		pidy.setHoldpos(posy+motiony*deltaT);
    		//System.out.printf("UP: set to %f\n", posy+motiony*deltaT);
    		hover = false;
       		if(!uphit)world.playSound("DangerZone:drone", dimension, posx, posy, posz, 1, 1);
    		uphit = true;
    	}else{
    		uphit = false;
    	}
    	if(thrower.getDown()){
    		motiony -= 0.01f*faster;
    		pidy.setHoldpos(posy+motiony*deltaT);
    		//System.out.printf("DOWN: set to %f\n", posy+motiony*deltaT);
    		hover = false;
       		if(!downhit)world.playSound("DangerZone:drone", dimension, posx, posy, posz, 1, 1);
    		downhit = true;
    	}else{
    		downhit = false;
    	}
    	
    	if(hover){
    		float adjust = deltaT * pidy.getAdjustment(posy, motiony*deltaT);
    		//System.out.printf("Curr, des, my, adjust == %f, %f, %f, %f\n", posy, pidy.desiredpos, motiony, adjust);
    		motiony += adjust;
    	}
    	
     	motiony += 0.15f * deltaT; //counter some gravity so we are mostly stable
 
     	PlayerKeyEvent plk = null;
     	
     	while(true){
     		plk = getNextKeyEvent();
     		if(plk == null)break;    		
     		//System.out.printf("Key %d is %s\n", plk.key, plk.isDown?"down":"up");
     		
     		//forward everything to the claw entity as well.
     		if(clawentity != null){
     			clawentity.addKeyEvent(plk.key, plk.isDown);
     		}
     		
     		if(plk.key == Keyboard.KEY_ESCAPE && plk.isDown){
     			deadflag = true;
     			Utils.doDropRand(world, 0, Items.drone.itemID, 1, dimension, posx, posy, posz);   
     			return;
     		}
     		
     		if(plk.key == Keyboard.KEY_DOWN){
     			if(plk.isDown){
     				//System.out.printf("down down\n");
     				down_isdown = true;
     			}else{
     				down_isdown = false;
     			}    			
     		}
     		if(plk.key == Keyboard.KEY_UP){
     			if(plk.isDown){
     				//System.out.printf("up down\n");
     				up_isdown = true;
     			}else{
     				up_isdown = false;
     			}    			
     		}
     	}
     	
     	//we don't actually DO anything with mouse clicks... 
     	//this is just here to clear the queue and be an example of how to get them...
     	while(true){
     		plk = getNextMouseEvent();
     		if(plk == null)break;    		
     		//System.out.printf("Mouse %d is %s\n", plk.key, plk.isDown?"down":"up");
     	}
     	
     	if(down_isdown){
     		if(!updown_sound){
     			play_updown_sound();
     			updown_sound = true;
     		}
     		if(clawentity == null){
     			clawentity = (DroneClaw)world.createEntityByName("DangerZone:DroneClaw", dimension, posx, posy-1, posz);
     			if(clawentity != null){
     				//System.out.printf("claw spawned\n");
     				clawentity.init();
     				clawentity.motionx = motionx;
     				clawentity.motiony = motiony;
     				clawentity.motionz = motionz;
     				clawentity.thrower = this;
     				world.spawnEntityInWorld(clawentity);
     			}
     		}
     		clawpos++; 
     		if(clawpos > 300)clawpos = 300;
     	}
     	
     	if(up_isdown){
     		if(!updown_sound){
     			play_updown_sound();
     			updown_sound = true;
     		}
     		clawpos--;
     		if(clawpos < 0)clawpos = 0;
     	}
     	
     	if(!down_isdown && !up_isdown){
     		updown_sound = false;
     	}
     	
     	if(clawentity != null){
     		while(clawentity.isSolidAtLevel(clawentity.dimension, clawentity.posx, posy - 1 - (0.1f*clawpos) + motiony, clawentity.posz)){
     			clawpos -= 2;
     			if(clawpos <= 0)break;
     		}
     	}
 		
 		if(clawpos <= 0){
 			clawpos = 0;
 			if(clawentity != null){
 				//System.out.printf("claw killed\n");
 				clawentity.deadflag = true;
 				clawentity = null;
 			}
 		}
     	
     	if(clawentity != null){
     		clawentity.posy = posy - 1 - (0.1f*clawpos) + motiony;
     		clawentity.posx += (posx - clawentity.posx)/4;
     		clawentity.posz += (posz - clawentity.posz)/4;
     		clawentity.motionx += (motionx - clawentity.motionx)/4;
     		clawentity.motionz += (motionz - clawentity.motionz)/4;
     		clawentity.motiony += (motiony - clawentity.motiony)/4;
     		
     		float dist = (0.1f*clawpos);
     		while(dist > 0){
     			if(world.rand.nextInt(30) == 0){
     				Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", 1, dimension, posx, posy + motiony - dist, posz);
     			}
     			dist -= 0.1f;
     		}
     	}
     	
  
	}
	
	public void play_updown_sound(){
		world.playSound("DangerZone:motor_updown", dimension, posx, posy, posz, 1, 1);
	}
	
	
	public void doDeathDrops(){
		//nothing!
	}

	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Dronetexture.png");
		}
		return texture;
	}

}