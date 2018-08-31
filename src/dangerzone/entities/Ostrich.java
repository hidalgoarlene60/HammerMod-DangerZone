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

import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


public class Ostrich extends EntityLiving {

	public RenderInfo renderdata = new RenderInfo();
	public int jumps = 0;
	private int toggledup = 0;
	
	public Ostrich(World w) {
		super(w);
		maxrenderdist = 128; //in blocks
		this.height = 2.75f;
		this.width = 0.85f;
		uniquename = "DangerZone:Ostrich";
		moveSpeed = 0.45f;
		setMaxHealth(35.0f);
		setHealth(35.0f);
		setDefense(1.0f);
		setAttackDamage(0f);
		movefrequency = 45;
		setExperience(34);
		swimoffset = -1.25f;
		setCanDespawn(false);
		takesFallDamage = false;
		setOnGround(true); //so the trophy doesn't flap constantly!
		takesFallDamage = false;
		enable_followfood = true;
		foodsearchDistance = 16;
		canSwim = true;
		enable_taming = true;
		maxdisttoowner = 20f;
		enable_droppedfood = true;
		enableBreeding(10);
		findbuddydistance = 16;
		findbuddyfrequency = 6;
	}
	

	 public boolean isBuddy(Entity e){
		 if(e instanceof Ostrich)return true;
		 return false;
	 }
	
	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){	
		//Mount/unmount...
		if(world.isServer){
			//empty hand - ride, or get off!
			if(ic == null && (getOwnerName() == null || getOwnerName() != null && p.myname.equals(getOwnerName()))){
				if(isMountedBy(p)){
					unMount(p);
				}else{
					Mount(p);
					setSitting(false);
					setStaying(false);
				}
				return false;
			}
		}
		return super.rightClickedByPlayer(p, ic);
	}
	
	public float getRiderYoffset(){
		if(isBaby())return 1.35f/4;
		return 1.35f; //height to his back
	}
	
	public float getRiderXZoffset(){
		if(isBaby())return -0.25f/4;
		return -0.25f; //sit back a little so the neck is not blocking completely...
	}
	
	public void doEntityAction(float deltaT){	
		
		enable_buddy = isBaby();
		
		Entity rider = getRiderEntity();
 
		if(rider == null){
			jumpstrength = 0.15f * height; //normal jump
			super.doEntityAction(deltaT);
			return;
		}else{
			if(!(rider instanceof Player)){
				jumpstrength = 0.15f * height; //normal jump
				super.doEntityAction(deltaT);
				return;
			}
		}
		
		//We have a rider! It's a player!
	   	float faster = deltaT*3;
		//First, let's turn with rider...
		float cdir = (float) Math.toRadians(rotation_yaw);
    	float tdir = (float) Math.toRadians(rider.rotation_yaw);
    	float ddiff = tdir - cdir;
    	while(ddiff>Math.PI)ddiff -= Math.PI*2;
    	while(ddiff<-Math.PI)ddiff += Math.PI*2;
    	rotation_yaw_motion += (ddiff*180f/Math.PI)/6f;
    	
		cdir = (float) Math.toRadians(rotation_yaw_head);
    	tdir = (float) Math.toRadians(rider.rotation_yaw_head);
    	ddiff = tdir - cdir;
    	while(ddiff>Math.PI)ddiff -= Math.PI*2;
    	while(ddiff<-Math.PI)ddiff += Math.PI*2;
    	rotation_yaw_head += (ddiff*180f/Math.PI)/5f;
    	
    	jumpstrength = 1.5f * height; //ten times normal! :)
    	
    	//now let's go...
    	if(rider.getForward()){
    		motionx += moveSpeed*Math.sin(tdir)*faster;
    		motionz += moveSpeed*Math.cos(tdir)*faster;
    	}
    	if(rider.getBackward()){
    		motionx -= moveSpeed*Math.sin(tdir);
    		motionz -= moveSpeed*Math.cos(tdir);
    	}
    	if(rider.getLeft()){
    		motionx += moveSpeed*Math.sin(tdir+Math.PI/2);
    		motionz += moveSpeed*Math.cos(tdir+Math.PI/2);
    	}
    	if(rider.getRight()){
    		motionx -= moveSpeed*Math.sin(tdir+Math.PI/2);
    		motionz -= moveSpeed*Math.cos(tdir+Math.PI/2);
    	}
    	
    	if(!rider.getUp())toggledup = 0;

    	if(getOnGround()){
    		jumps = 0;
    		toggledup = 0;
    		if(rider.getUp()){
    			toggledup = 1;
    			jump();
    		}
    	}else{
    		//give them a few extra mid-air jumps for fun... :)
    		if(rider.getUp() && jumps < 3 && toggledup == 0){
    			toggledup = 1;
    			jumps++;
    			this.motiony += (0.75f + jumpstrength);
    		}   		
    	}
	}
	
	public boolean isFoodItem(int foodid){
		if(foodid == Items.corn.itemID)return true;
		return false;
	}	
	
	public String getLivingSound(){
		return null;
	}
	
	public String getHurtSound(){
		return "DangerZone:cryo_hurt";
	}
	
	public String getDeathSound(){
		return "DangerZone:cryo_death";
	}
	
	public void doDeathDrops(){
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophyostrich.itemID, 1f, dimension, posx, posy, posz);
		Utils.doDropRand(world, 0, Items.ostrichmeat.itemID, 1f, dimension, posx, posy, posz);
		int i=1+world.rand.nextInt(4);
		for(int j=0;j<i;j++){
			Utils.doDropRand(world, 0, Items.feather.itemID, 1.5f, dimension, posx, posy, posz);
		}
		//easter egg!
		if(world.rand.nextInt(1000) == 42){
			for(int j=0;j<1000;j++){
				Utils.doDropRand(world, 0, Items.feather.itemID, 3.5f, dimension, posx, posy, posz);
			}
		}
	}
	
	public void update(float deltaT){
		if(this.world.isServer){
			if(world.rand.nextInt(15000) == 1){
				Utils.doDropRand(world, 0, Items.eggostrich.itemID, 0.1f, dimension, posx, posy, posz);
			}
		}
		super.update(deltaT);
	}
		
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Ostrichtexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}
