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
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;

public class EntitySign extends Entity {
	
	int checker = 0;

	public EntitySign(World w) {
		super(w);
		uniquename = "DangerZone:EntitySign";
		ignoreCollisions = true;
		width = 1.0f;
		height = 1.5f;
	}
	
	public void doAttackFrom(/*entity that hit me*/Entity e, /*DamageTypes*/int dt, float pain){
		if(!world.isServer)return;		
		Utils.doDropRand(world, 0, Items.sign.itemID, 0f, dimension, posx, posy, posz);
		this.deadflag = true;
	}
	
	public void update(float deltaT){	
		motionx = motiony = motionz = 0;
		if(!Blocks.isSolid(world.getblock(dimension, (int)posx, (int)(posy-0.5f), (int)posz)))doAttackFrom(this, 1, 1);
		if(Blocks.isSolid(world.getblock(dimension, (int)posx, (int)(posy+0.5f), (int)posz)))doAttackFrom(this, 1, 1);
		if(Blocks.isSolid(world.getblock(dimension, (int)posx, (int)(posy+1.5f), (int)posz)))doAttackFrom(this, 1, 1);
		if(getBID() == 0){
			rotation_yaw_motion = 1;
		}else{
			rotation_yaw_motion = 0;
		}
		super.update(deltaT);
	}
	
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "MooseSigntexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}
	
	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){
		if(world.isServer)return false;
		//must spawn a gui on the CLIENT
		DangerZone.signgui.init(this);
		DangerZone.setActiveGui(DangerZone.signgui);
		return false;
	}

}
