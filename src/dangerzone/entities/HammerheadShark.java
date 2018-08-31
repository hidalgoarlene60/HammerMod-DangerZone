package dangerzone.entities;


import org.newdawn.slick.opengl.Texture;

import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;

public class HammerheadShark extends BetterFish {

	public HammerheadShark(World w) {
		super(w);
		uniquename = "DangerZone:Hammerhead Shark";		
		maxrenderdist = 200; //in blocks
		this.height = 1.15f;
		this.width = 1.15f;
		moveSpeed = 0.36f; //basic usual speed range
		setMaxHealth(250.0f);
		setHealth(250.0f);
		setDefense(3.5f);
		setAttackDamage(50f);
		movefrequency = 50; //direction change frequency
		setExperience(750);
		daytimespawn = true;
		nighttimespawn = false;
		daytimedespawn = false;
		nighttimedespawn = true;
		myspeed = moveSpeed;
		searchDistance = 50;
		enable_hostile = true;
		temperament = Temperament.HOSTILE;
		attackRange = 3.0f;
	}
	
	public String getLivingSound(){
		if(world.rand.nextBoolean())return null;
		return "DangerZone:big_splash";
	}
	
	public String getHurtSound(){
		return "DangerZone:big_splat";
	}
	
	public String getDeathSound(){
		return "DangerZone:big_splat";
	}
	
	public void doDeathDrops(){		
		if(world.rand.nextInt(5)==1)Utils.doDropRand(world, 0, Items.trophyhammerhead.itemID, 1f, dimension, posx, posy, posz);			
		for(int i=0;i<10;i++)Utils.doDropRand(world, 0, Items.fishmeat.itemID, 3.5f, dimension, posx, posy, posz);		
		if(world.rand.nextInt(5)==1)Utils.doDropRand(world, 0, Items.sharkfin.itemID, 3f, dimension, posx, posy, posz);		
		if(world.rand.nextInt(3)==1)Utils.doDropRand(world, 0, Items.sharktooth.itemID, 3f, dimension, posx, posy, posz);		
	}
	
	//Override
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof HammerheadShark)return false;
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
			texture = TextureMapper.getTexture("res/skins/Hammerheadtexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}
