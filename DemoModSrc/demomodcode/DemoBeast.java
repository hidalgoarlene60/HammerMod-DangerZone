package demomodcode;


import org.newdawn.slick.opengl.Texture;


import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;
import dangerzone.entities.Temperament;
import dangerzone.items.Items;


public class DemoBeast extends EntityLiving {
	
	public DemoBeast(World w) {
		super(w);
		width = 0.75f;
		height = 1.75f;	
		uniquename = "DemoMod:DemoBeast";		
		has_inventory = false;
		attackRange = 3.0f;
		setMaxHealth(40);
		setHealth(40);
		setExperience(50);
		setDefense(1.5f);
		setAttackDamage(5);
		setMaxAir(40);
		setAir(40);
		temperament = Temperament.HOSTILE;
		enable_hostile = true;
		daytimespawn = false;
		daytimedespawn = true;
		nighttimespawn = true;
		nighttimedespawn = false;
	}

	
	
	public String getLivingSound(){
		if(world.rand.nextInt(5) != 0)return null;
		return "DangerZone:werewolf_living";
	}
	
	
	public String getHurtSound(){
		return "DangerZone:anteater_hit";
	}
	
	public String getDeathSound(){
		return "DangerZone:werewolf_death";
	}
	
	public String getAttackSound(){
		return "DangerZone:werewolf_attack";
	}
	
	public void doDeathDrops(){		
		super.doDeathDrops();
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, DemoModMain.trophydemobeast.itemID, 1f, dimension, posx, posy, posz);
		int howmany = 5+world.rand.nextInt(5);
		int i;
		for(i=0;i<howmany;i++){
			Utils.doDropRand(world, 0, Items.furball.itemID, 3f, dimension, posx, posy, posz);
		}
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("demores/skins/"+ "DemoBeastTexture.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}
	
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof DemoBeast)return false;
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		if(e instanceof Player && CanProbablySeeEntity(e) )return true;
		return false;
	}

}
