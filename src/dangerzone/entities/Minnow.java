package dangerzone.entities;


import org.newdawn.slick.opengl.Texture;

import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;

public class Minnow extends BetterFish {
	

	public Minnow(World w) {
		super(w);
		uniquename = "DangerZone:Minnow";		
		maxrenderdist = 80; //in blocks
		this.height = 0.25f;
		this.width = 0.25f;
		moveSpeed = 0.15f; //basic usual speed range
		setMaxHealth(1.0f);
		setHealth(1.0f);
		setDefense(1.0f);
		setAttackDamage(0f);
		movefrequency = 40; //direction change frequency
		setExperience(1);
		myspeed = moveSpeed;
		enable_buddy = true;
		findbuddyfrequency = 10;
		findbuddydistance = 10;
		movesearchdist = 10;
	}
	
	
	public boolean isBuddy(Entity e){
		if(e instanceof Minnow)return true;
		return false;
	}
	
	public void doDeathDrops(){		
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophyminnow.itemID, 1f, dimension, posx, posy, posz);				
		if(world.rand.nextInt(5)==1)Utils.doDropRand(world, 0, Items.fishmeat.itemID, 1.5f, dimension, posx, posy, posz);		
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/MinnowTexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}
