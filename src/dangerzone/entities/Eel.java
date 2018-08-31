package dangerzone.entities;


import org.newdawn.slick.opengl.Texture;

import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;

public class Eel extends BetterFish {

	public Eel(World w) {
		super(w);
		uniquename = "DangerZone:Eel";		
		maxrenderdist = 100; //in blocks
		this.height = 0.45f;
		this.width = 0.45f;
		moveSpeed = 0.30f; //basic usual speed range
		setMaxHealth(15.0f);
		setHealth(15.0f);
		setDefense(1.5f);
		setAttackDamage(5f);
		movefrequency = 50; //direction change frequency
		setExperience(25);
		daytimespawn = false;
		nighttimespawn = true;
		daytimedespawn = true;
		nighttimedespawn = false;
		myspeed = moveSpeed;
		searchDistance = 40;
		enable_hostile = true;
		temperament = Temperament.HOSTILE;
	}
	
	public void doDeathDrops(){		
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophyeel.itemID, 1f, dimension, posx, posy, posz);			
		Utils.doDropRand(world, 0, Items.fishmeat.itemID, 1.5f, dimension, posx, posy, posz);		
	}
	
	//Override
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof Eel)return false;
		int blk = world.getblock(e.dimension, (int)e.posx, (int)e.posy, (int)e.posz);
		if(blk != Blocks.waterstatic.blockID)return false;
		if((e instanceof Player) && CanProbablySeeEntity(e) )return true;		
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		if(e instanceof Minnow && CanProbablySeeEntity(e))return true;
		return false;
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/Eeltexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}
