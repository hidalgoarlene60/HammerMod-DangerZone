package dangerzone.entities;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;
import dangerzone.items.Items;


public class Frog extends EntityLiving {
	private int singing = 0;
	private int jumpcount = 0;
	
	Texture texture2 = null;
	Texture texture3 = null;
	Texture texture4 = null;
	Texture texture5 = null;
	Texture texture6 = null;
	
	Texture texture7 = null;
	Texture texture8 = null;
	Texture texture9 = null;
	Texture texture10 = null;
	Texture texture11 = null;
	Texture texture12 = null;
	
	Texture texture13 = null;
	Texture texture14 = null;
	Texture texture15 = null;
	Texture texture16 = null;
	Texture texture17 = null;
	Texture texture18 = null;
	
	Texture texture19 = null;
	Texture texture20 = null;
	Texture texture21 = null;
	Texture texture22 = null;
	Texture texture23 = null;
	Texture texture24 = null;
	
	public	Frog(World w){
		super(w);
		maxrenderdist = 32; //in blocks
		this.height = 0.25f;
		this.width = 0.25f;
		uniquename = "DangerZone:Frog";
		moveSpeed = 0.16f;
		setMaxHealth(2.0f);
		setHealth(2.0f);
		setDefense(0.5f);
		setAttackDamage(2.0f);
		movefrequency = 40;
		setExperience(1);
		canSwim = true;
		takesFallDamage = false;
		attackRange = 1.0f;
		daytimespawn = true;
		nighttimespawn = false;
		daytimedespawn = true;
		nighttimedespawn = true;
		searchDistance = 8;
		enable_droppedfood = true;
		foodsearchDistance = 10;
		enable_hostile = true;
		canridemaglevcart = true;
	}
	
	public void init(){
		super.init();
		setSinging(false);
		if(getBID() == 0){
			setBID(DangerZone.rand.nextInt(2) + 1);
			if(DangerZone.rand.nextInt(3) == 1){
				setBID(DangerZone.rand.nextInt(2) + 3);
				if(DangerZone.rand.nextInt(3) == 1){
					setBID(DangerZone.rand.nextInt(2) + 5);
				}
			}
			
			//maybe switch to strength/weakness or regen/poison
			if(DangerZone.rand.nextInt(10) > 5){				
				if(DangerZone.rand.nextInt(2) == 1){
					setBID(getBID()+6);
				}else{
					setBID(getBID()+12);
					if(DangerZone.rand.nextInt(2)==1){
						setBID(getBID()+6);
					}
				}
			}
		}
	}
	
	public boolean isFoodItem(int foodid){
		if(foodid == Items.deadbug.itemID)return true;
		return false;
	}
	
	public void onKill(Entity e){
		heal(1);
	}
	
	//we attack all small things!
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof Frog)return false;
		if(e instanceof DesertRainFrog)return false;
		if(e.getWidth()*e.getHeight() > 0.53f)return false;
		if(!CanProbablySeeEntity(e))return false;
		return true;
	}
	
	public void update(float deltaT){

		if(world.isServer){
			if(singing > 0){
				singing--;
				if(singing <= 0){
					setSinging(false);
				}
			}
    		if(jumpcount > 0)jumpcount--;
    		if(jumpcount == 0){
    			if(world.rand.nextInt(50) == 1){
    				jumpAround();
    				jumpcount = 50;
    			}  			
    		}
    		if(this.world.rand.nextInt(10000) == 10 && getCanDespawn()){
				this.deadflag = true; //have to do our own despawning because we don't move much!!!
			}	
		}
		super.update(deltaT);
	}
	
	public String getLivingSound(){
		if(world.rand.nextInt(2) == 1)return null;
		setSinging(true);
		singing = 20; //about 2 seconds
		if(world.rand.nextInt(2) == 1)return "DangerZone:frog2";
		return "DangerZone:frog1";
	}
	
	public float getLivingSoundPitch(){
		return 1.5f;
	}
	
	public String getDeathSound(){
		return "DangerZone:big_splat";
	}
	
	public String getHurtSound(){
		return "DangerZone:little_splat";
	}
	
	public void doDeathDrops(){
		int i = getBID();
		if(i == 1)Utils.doDropRand(world, 0, Items.frog_speed1.itemID, 1f, dimension, posx, posy, posz);		
		if(i == 2)Utils.doDropRand(world, 0, Items.frog_slowness1.itemID, 1f, dimension, posx, posy, posz);
		if(i == 3)Utils.doDropRand(world, 0, Items.frog_speed2.itemID, 1f, dimension, posx, posy, posz);		
		if(i == 4)Utils.doDropRand(world, 0, Items.frog_slowness2.itemID, 1f, dimension, posx, posy, posz);
		if(i == 5)Utils.doDropRand(world, 0, Items.frog_speed3.itemID, 1f, dimension, posx, posy, posz);		
		if(i == 6)Utils.doDropRand(world, 0, Items.frog_slowness3.itemID, 1f, dimension, posx, posy, posz);
		if(i == 7)Utils.doDropRand(world, 0, Items.frog_strength1.itemID, 1f, dimension, posx, posy, posz);		
		if(i == 8)Utils.doDropRand(world, 0, Items.frog_weakness1.itemID, 1f, dimension, posx, posy, posz);
		if(i == 9)Utils.doDropRand(world, 0, Items.frog_strength2.itemID, 1f, dimension, posx, posy, posz);		
		if(i == 10)Utils.doDropRand(world, 0, Items.frog_weakness2.itemID, 1f, dimension, posx, posy, posz);
		if(i == 11)Utils.doDropRand(world, 0, Items.frog_strength3.itemID, 1f, dimension, posx, posy, posz);		
		if(i == 12)Utils.doDropRand(world, 0, Items.frog_weakness3.itemID, 1f, dimension, posx, posy, posz);
		if(i == 13)Utils.doDropRand(world, 0, Items.frog_regen1.itemID, 1f, dimension, posx, posy, posz);		
		if(i == 14)Utils.doDropRand(world, 0, Items.frog_poison1.itemID, 1f, dimension, posx, posy, posz);
		if(i == 15)Utils.doDropRand(world, 0, Items.frog_regen2.itemID, 1f, dimension, posx, posy, posz);		
		if(i == 16)Utils.doDropRand(world, 0, Items.frog_poison2.itemID, 1f, dimension, posx, posy, posz);
		if(i == 17)Utils.doDropRand(world, 0, Items.frog_regen3.itemID, 1f, dimension, posx, posy, posz);		
		if(i == 18)Utils.doDropRand(world, 0, Items.frog_poison3.itemID, 1f, dimension, posx, posy, posz);
		if(i == 19)Utils.doDropRand(world, 0, Items.frog_confusion1.itemID, 1f, dimension, posx, posy, posz);
		if(i == 20)Utils.doDropRand(world, 0, Items.frog_confusion2.itemID, 1f, dimension, posx, posy, posz);
		if(i == 21)Utils.doDropRand(world, 0, Items.frog_confusion3.itemID, 1f, dimension, posx, posy, posz);
		if(i == 22)Utils.doDropRand(world, 0, Items.frog_morph1.itemID, 1f, dimension, posx, posy, posz);
		if(i == 23)Utils.doDropRand(world, 0, Items.frog_morph2.itemID, 1f, dimension, posx, posy, posz);
		if(i == 24)Utils.doDropRand(world, 0, Items.frog_morph3.itemID, 1f, dimension, posx, posy, posz);	
		
	}
	
    
    private void jumpAround() {
    	target = null;
    	motiony += 0.85f + Math.abs(world.rand.nextFloat()*0.85f);
    	posy += 0.33f;
    	float f =  0.88f + Math.abs(world.rand.nextFloat()*0.95f);
    	float cdir = (float) Math.toRadians(rotation_yaw);
    	motionx += f*Math.sin(cdir);
    	motionz += f*Math.cos(cdir);
    }

	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "FrogtextureSpeed1.png");	//this is not fast, so we keep our own pointer!
			texture2 = TextureMapper.getTexture("res/skins/"+ "FrogtextureSlowness1.png");	//this is not fast, so we keep our own pointer!
			texture3 = TextureMapper.getTexture("res/skins/"+ "FrogtextureSpeed2.png");	//this is not fast, so we keep our own pointer!
			texture4 = TextureMapper.getTexture("res/skins/"+ "FrogtextureSlowness2.png");	//this is not fast, so we keep our own pointer!
			texture5 = TextureMapper.getTexture("res/skins/"+ "FrogtextureSpeed3.png");	//this is not fast, so we keep our own pointer!
			texture6 = TextureMapper.getTexture("res/skins/"+ "FrogtextureSlowness3.png");	//this is not fast, so we keep our own pointer!
			
			texture7 = TextureMapper.getTexture("res/skins/"+ "FrogtextureStrength1.png");	
			texture8 = TextureMapper.getTexture("res/skins/"+ "FrogtextureWeakness1.png");	
			texture9 = TextureMapper.getTexture("res/skins/"+ "FrogtextureStrength2.png");	
			texture10 = TextureMapper.getTexture("res/skins/"+ "FrogtextureWeakness2.png");	
			texture11 = TextureMapper.getTexture("res/skins/"+ "FrogtextureStrength3.png");	
			texture12 = TextureMapper.getTexture("res/skins/"+ "FrogtextureWeakness3.png");	
			
			texture13 = TextureMapper.getTexture("res/skins/"+ "FrogtextureRegen1.png");	
			texture14 = TextureMapper.getTexture("res/skins/"+ "FrogtexturePoison1.png");	
			texture15 = TextureMapper.getTexture("res/skins/"+ "FrogtextureRegen2.png");	
			texture16 = TextureMapper.getTexture("res/skins/"+ "FrogtexturePoison2.png");	
			texture17 = TextureMapper.getTexture("res/skins/"+ "FrogtextureRegen3.png");	
			texture18 = TextureMapper.getTexture("res/skins/"+ "FrogtexturePoison3.png");	
			
			texture19 = TextureMapper.getTexture("res/skins/"+ "FrogtextureConfusion1.png");	
			texture20 = TextureMapper.getTexture("res/skins/"+ "FrogtextureConfusion2.png");	
			texture21 = TextureMapper.getTexture("res/skins/"+ "FrogtextureConfusion3.png");	
			texture22 = TextureMapper.getTexture("res/skins/"+ "FrogtextureMorph1.png");	
			texture23 = TextureMapper.getTexture("res/skins/"+ "FrogtextureMorph2.png");	
			texture24 = TextureMapper.getTexture("res/skins/"+ "FrogtextureMorph3.png");	
		}
		int i = getBID();
		
		if(i == 2)return texture2;
		if(i == 3)return texture3;
		if(i == 4)return texture4;
		if(i == 5)return texture5;
		if(i == 6)return texture6;
		
		if(i == 7)return texture7;
		if(i == 8)return texture8;
		if(i == 9)return texture9;
		if(i == 10)return texture10;
		if(i == 11)return texture11;
		if(i == 12)return texture12;
		
		if(i == 13)return texture13;
		if(i == 14)return texture14;
		if(i == 15)return texture15;
		if(i == 16)return texture16;
		if(i == 17)return texture17;
		if(i == 18)return texture18;
		
		if(i == 19)return texture19;
		if(i == 20)return texture20;
		if(i == 21)return texture21;
		if(i == 22)return texture22;
		if(i == 23)return texture23;
		if(i == 24)return texture24;
		
		return texture;
	}
	

}
