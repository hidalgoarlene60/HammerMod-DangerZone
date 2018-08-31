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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DamageTypes;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.entities.Entities.ElistInfo;
import dangerzone.items.Items;
import dangerzone.Player;


public class Flag extends EntityLiving {
	
	public List<Entity> hostiles = null;
	
	//public int startat = 50;
	
	public	Flag(World w){
		super(w);
		maxrenderdist = 256; //in blocks
		this.height = 10f;
		this.width = 1f;
		uniquename = "DangerZone:Flag";
		setMaxHealth(5000.0f);
		setHealth(5000.0f);
		setDefense(2f);
		takesFallDamage = false;
		setExperience(1);
		daytimespawn = false;
		nighttimespawn = false;
		daytimedespawn = false;
		nighttimedespawn = false;
		temperament = Temperament.HOSTILE; //attack me please!!!!
		hostiles = new ArrayList<Entity>();
		ignoreCollisions = true;
		tower_defense_enable = false; //don't add me!
	}
	
	public void doAttackFrom(/*entity that hit me*/Entity e, /*DamageTypes*/int dt, float pain){	
		if(dt == DamageTypes.LIGHTNING)return;
		if(getListNext() >= 0){ //player can damage after we are done!
			if(e instanceof Player)return;
		}
		doAttackFromCustom(e, dt, pain, false); //false = no knockback
	}
	public void addKnockback(float dir, float xz, float y){
	}
	

	public void setCounter(int count){
		setVarInt(14, count);
	}
	public int getCounter(){
		return getVarInt(14);
	}
	public void setListNext(int next){
		setVarInt(15, next);
	}
	public int getListNext(){
		return getVarInt(15);
	}
	
	public void setBaby(boolean tf){
		//no!
	}
	
	
	//no default actions here!
	public void doEntityAction(float deltaT){	
		motionx = motiony = motionz = 0;
		rotation_pitch_motion = 0;
		rotation_roll_motion = 0;
		rotation_pitch = 0;
		rotation_roll = 0;
		
		float h = getHealth();
		if(h < 1)h = 1;
		h = (getMaxHealth()/h);
		rotation_yaw_motion = h - 1;
		rotation_yaw_motion *= 5;
		if(rotation_yaw_motion > 100)rotation_yaw_motion = 100;
		
		//we don't get poison or confusion or whatever...
		removeAllEffects();
		
		//add some effects!
		if(h > 32)h = 32;
		if(h > 4){
			Utils.spawnParticles(world, "DangerZone:ParticleSparkle", (int)(h/2), dimension, posx, posy+world.rand.nextFloat()*getHeight(), posz, true);
		}
		if(h > 2){
			Utils.spawnParticles(world, "DangerZone:ParticleSmoke", (int)(h*2), dimension, posx, posy+world.rand.nextFloat()*getHeight(), posz, true);
		}
		
		if(getListNext() < 0){
			temperament = Temperament.PASSIVE; 
			return;
		}
		
		if(hostiles.isEmpty()){
			Iterator<ElistInfo> ii = Entities.entities.iterator();
			ElistInfo st;
			Entity e;
			List<Entity> temphostiles = new ArrayList<Entity>();
			while(ii.hasNext()){
				st = ii.next();
				e = Entities.spawnEntityByName(st.uniquename, null);
				if(e != null){
					if(e instanceof EntityLiving && e.temperament == Temperament.HOSTILE){
						EntityLiving el = (EntityLiving)e;
						if(el.tower_defense_enable){
							temphostiles.add(e);
							//System.out.printf("td added %s\n", e.uniquename);
						}
					}					
				}
			}
			while(!temphostiles.isEmpty()){
				e = findlowesthostile(temphostiles);
				hostiles.add(e);
			}
			
			//while(startat > 0){
			//	find_next_critter();
			//	startat--;
			//}
		}
		
		setCounter(getCounter()+1);

		//spawn more!!!
		if(getCounter() > 300){
			setCounter(0);
			Entity e = null;
			EntityLiving el = find_next_critter();
			if(el != null){
				int howmany = 12;
				float hardness = el.getAttackDamage()*el.getDefense();
				if(hardness > 5)howmany = 10;
				if(hardness > 10)howmany = 8;
				if(hardness > 30)howmany = 6;
				if(hardness > 100)howmany = 5;
				if(hardness > 500)howmany = 4;
				
				for(int i=0;i<howmany;i++){
					float dir = world.rand.nextFloat()*360;
					double px = posx + Math.cos(dir)*(20+el.getWidth());
					double pz = posz + Math.sin(dir)*(20+el.getWidth());
					int j;
					for(j=255;j>0;j--){
						if(world.getblock(dimension, (int)px, j, (int)pz)!=0)break;						
					}
					double py = j + el.getHeight()*2;
					e = world.createEntityByName(el.uniquename, dimension, px, py, pz);
					if(e != null){
						e.init();
						//e.do_death_drops = false;
						world.spawnEntityInWorld(e);						
					}
				}
				e = world.createEntityByName("DangerZone:Lightning", dimension, posx, posy, posz);
				if(e != null){
					e.init();
					world.spawnEntityInWorld(e);					
				}
			}else{
				//we seem to be done!
				do_finished();
				setListNext(-1); //flag we are done!!!
				setHealth(getMaxHealth());
			}			
		}

	}
	
	public Entity findlowesthostile(List<Entity>inlist){
		Entity thisone = null;
		float hardness = 100000000000f;
		int index = 0;
		Iterator<Entity> ii = inlist.iterator();
		int here = 0;
		while(ii.hasNext()){
			thisone = ii.next();
			if(thisone.getAttackDamage()*thisone.getDefense() < hardness){
				index = here;
				hardness = thisone.getAttackDamage()*thisone.getDefense();
			}
			here++;
		}
		thisone = inlist.get(index);
		inlist.remove(index);
		//System.out.printf("Entity %s, hardness = %f\n", thisone.uniquename, thisone.getAttackDamage()*thisone.getDefense());
		return thisone;
	}
	
	public EntityLiving find_next_critter(){
		int next = getListNext();
		if(next >= hostiles.size())return null;
		EntityLiving e = (EntityLiving)hostiles.get(next);		
		setListNext(next+1);
		return e;
	}
	
	public void do_finished(){
		int i, howmany;
		
		howmany = 80+world.rand.nextInt(30);
		for(i=0;i<howmany;i++){
			Utils.doDropRand(world, 0, Items.coinsilver.itemID, 10f, dimension, posx, posy, posz);
		}
		
		howmany = 80+world.rand.nextInt(30);
		for(i=0;i<howmany;i++){
			Utils.doDropRand(world, 0, Items.coingold.itemID, 10f, dimension, posx, posy, posz);
		}
		
		howmany = 80+world.rand.nextInt(30);
		for(i=0;i<howmany;i++){
			Utils.doDropRand(world, 0, Items.coinplatinum.itemID, 10f, dimension, posx, posy, posz);
		}
		
		Utils.doDropRand(world, 0, Items.flag.itemID, 1f, dimension, posx, posy, posz);
		
		Entity e = world.createEntityByName("DangerZone:Lightning", dimension, posx, posy, posz);
		if(e != null){
			e.init();
			world.spawnEntityInWorld(e);					
		}
	}
	

	public void update( float deltaT){
		
		if(world.isServer){
			//keep somewhat clear around the flag...
			int i, j, k;
			for(int n=0;n<10;n++){
				i = world.rand.nextInt(5) - world.rand.nextInt(5);
				k = world.rand.nextInt(5) - world.rand.nextInt(5);
				j = world.rand.nextInt(15);
				world.setblock(dimension, (int)(posx+i), (int)(posy+j), (int)(posz+k), 0);
			}
		}
		super.update( deltaT);
	}
	
	public void doDeathDrops(){
		Utils.doDropRand(world, 0, Items.flag.itemID, 1f, dimension, posx, posy, posz);
	}

	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "Flagtexture.png");
		}
		return texture;
	}

}
