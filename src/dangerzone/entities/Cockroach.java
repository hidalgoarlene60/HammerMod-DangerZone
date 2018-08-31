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
import dangerzone.Dimensions;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


public class Cockroach extends EntityLiving {
	
	Texture albino_texture = null;
	
	public	Cockroach(World w){
		super(w);
		maxrenderdist = 32; //in blocks
		this.height = 0.25f;
		this.width = 0.25f;
		uniquename = "DangerZone:Cockroach";
		moveSpeed = 0.25f;
		setMaxHealth(1.0f);
		setHealth(1.0f);
		setDefense(0.5f);
		setAttackDamage(0.15f);
		movefrequency = 20;
		daytimespawn = true;
		setExperience(2);
		canSwim = false;
		daytimespawn = true;
		nighttimespawn = false;
		daytimedespawn = true;
		nighttimedespawn = true;
		setBID(-1); //to force change to 0 to be sent!
		setBID(DangerZone.rand.nextInt(3)); //only 1/3 are albino...
		enableHostility(8, 1.5f);
		temperament = Temperament.PASSIVE;
		canridemaglevcart = true;
	}
	
	
	public void doEntityAction(float deltaT){

		//something else we should be doing?
		super.doEntityAction(deltaT);	
		
		if(getCanDespawn()){			
			if(this.world.rand.nextInt(1000) == 1){
				this.deadflag = true; //have to do our own despawning because we don't move much!!!
			}			
		}
	}
	
	
	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){	
		if(ic != null){
			if(ic.count != 0)return false; //must have empty hand!
		}
		if(world.isServer){		
			if(getBID() != 0){ //forward through dimensions
				for(int i=0;i<Dimensions.dimensionsMAX;i++){
					int id = (this.dimension+i+1)%Dimensions.dimensionsMAX;
					if(Dimensions.DimensionArray[id] != null){
						if(!Dimensions.DimensionArray[id].special_hidden){
							Utils.doTeleport(p, id, p.posx, p.posy, p.posz);		
							world.playSound("DangerZone:big_splat", dimension, posx, posy, posz, 1.0f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.2f);
							world.playSound("DangerZone:big_splat", id, posx, posy, posz, 1.0f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.2f);
							Utils.spawnParticlesFromServer(p.world, "DangerZone:ParticleHurt", 10, dimension, posx, posy, posz);
							Utils.spawnParticlesFromServer(p.world, "DangerZone:ParticleHurt", 10, id, posx, posy, posz);
							break;
						}
					}
				}
			}else{
				for(int i=0;i<Dimensions.dimensionsMAX;i++){
					int id = (this.dimension-i-1)%Dimensions.dimensionsMAX;
					while(id<0)id += Dimensions.dimensionsMAX;
					if(Dimensions.DimensionArray[id] != null){
						if(!Dimensions.DimensionArray[id].special_hidden){
							Utils.doTeleport(p, id, p.posx, p.posy, p.posz);		
							world.playSound("DangerZone:big_splat", dimension, posx, posy, posz, 1.0f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.2f);
							world.playSound("DangerZone:big_splat", id, posx, posy, posz, 1.0f, 1.0f+(world.rand.nextFloat()-world.rand.nextFloat())*0.2f);
							Utils.spawnParticlesFromServer(p.world, "DangerZone:ParticleHurt", 10, dimension, posx, posy, posz);
							Utils.spawnParticlesFromServer(p.world, "DangerZone:ParticleHurt", 10, id, posx, posy, posz);
							break;
						}
					}
				}
			}
		}
		return false;
	}
	
	public String getDeathSound(){
		return "DangerZone:big_splat";
	}
	
	public void doDeathDrops(){
		if(world.rand.nextInt(20)==1)Utils.doDropRand(world, 0, Items.trophycockroach.itemID, 1f, dimension, posx, posy, posz);
		Utils.doDropRand(world, 0, Items.deadbug.itemID, 1f, dimension, posx, posy, posz);
	}

	//Override, because we attack hostiles that are NOT cockroach!
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof Cockroach)return false;
		if(e instanceof Player && CanProbablySeeEntity(e) )return true;
		if(e instanceof Anteater && CanProbablySeeEntity(e) )return true; //and also anteaters...
		return false;
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Cockroachtexture.png");	//this is not fast, so we keep our own pointer!
			albino_texture = TextureMapper.getTexture("res/skins/"+ "AlbinoCockroachtexture.png");
		}
		if(getBID() == 0)return albino_texture;
		return texture;
	}
	

}
