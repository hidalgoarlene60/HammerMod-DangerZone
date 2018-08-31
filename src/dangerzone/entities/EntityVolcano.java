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
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.particles.Particle;


public class EntityVolcano extends Entity {
	
	int flowrate = 50;
	int flowcount = 0;
	int flowdir = 1;

	
	public EntityVolcano(World w){
		super(w);
		maxrenderdist = 12; //in blocks
		this.height = 1f;
		this.width = 1;
		uniquename = "DangerZone:Volcano";
		canthitme = true; //Ignore me!
		ignoreCollisions = true;
	}
	
	public void init(){

	}
	

	public void update( float deltaT){	
		float flowheight = 1.75f+world.rand.nextFloat()*0.5f;
		int useflow = flowrate - flowcount;
		if(useflow < 2)useflow = 2;
		
		flowcount += flowdir;
		if(flowcount > flowrate){
			flowdir = -1;
		}
		if(flowdir < 0 && flowcount <= 0){
			flowrate = world.rand.nextInt(100);
			flowdir = 1;
			flowcount = 1;
		}
		
		if(this.world.isServer){
			int bidtospout, meta;
			int i, k;
			boolean found = false;

			
			if(world.rand.nextInt(useflow) == 1){
				bidtospout = Blocks.lavafiller.blockID;
				meta = 1 + world.rand.nextInt(0x0f);
				EntityBlock eb = (EntityBlock) world.createEntityByName("DangerZone:EntityBlock", dimension, 
						posx+0.5f, 
						posy+1.25f,
						posz+0.5f);
				if(eb != null){
					eb.init();
					eb.setBID(bidtospout);
					eb.setIID(meta);
					if(world.rand.nextBoolean())eb.setOnFire(world.rand.nextInt(30));
					eb.motiony = flowheight+DangerZone.rand.nextFloat()*flowheight;
					eb.motionx = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*flowheight;
					eb.motionz = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*flowheight;
					world.spawnEntityInWorld(eb);
				}
			}
			if(world.rand.nextInt(useflow) == 1){
				bidtospout = Blocks.lava.blockID;
				meta = 1 + world.rand.nextInt(0x0f);
				EntityBlock eb = (EntityBlock) world.createEntityByName("DangerZone:EntityBlock", dimension, 
						posx+0.5f, 
						posy+1.25f,
						posz+0.5f);
				if(eb != null){
					eb.init();
					eb.setBID(bidtospout);
					eb.setIID(meta);
					if(world.rand.nextBoolean())eb.setOnFire(world.rand.nextInt(30));
					eb.motiony = flowheight+DangerZone.rand.nextFloat()*flowheight;
					eb.motionx = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*flowheight;
					eb.motionz = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*flowheight;
					world.spawnEntityInWorld(eb);
				}
			}
			if(world.rand.nextInt(useflow) == 1){
				bidtospout = Blocks.stone.blockID;
				meta = 0;
				EntityBlock eb = (EntityBlock) world.createEntityByName("DangerZone:EntityBlock", dimension, 
						posx+0.5f, 
						posy+1.25f,
						posz+0.5f);
				if(eb != null){
					eb.init();
					eb.setBID(bidtospout);
					eb.setIID(meta);
					if(world.rand.nextBoolean())eb.setOnFire(world.rand.nextInt(10));
					eb.motiony = flowheight+DangerZone.rand.nextFloat()*flowheight;
					eb.motionx = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*flowheight;
					eb.motionz = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*flowheight;
					world.spawnEntityInWorld(eb);
				}
			}
			if(world.rand.nextInt(useflow) == 1){
				bidtospout = Blocks.greystone.blockID;
				meta = 0;
				EntityBlock eb = (EntityBlock) world.createEntityByName("DangerZone:EntityBlock", dimension, 
						posx+0.5f, 
						posy+1.25f,
						posz+0.5f);
				if(eb != null){
					eb.init();
					eb.setBID(bidtospout);
					eb.setIID(meta);
					if(world.rand.nextBoolean())eb.setOnFire(world.rand.nextInt(10));
					eb.motiony = flowheight+DangerZone.rand.nextFloat()*flowheight;
					eb.motionx = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*flowheight;
					eb.motionz = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*flowheight;
					world.spawnEntityInWorld(eb);
				}
			}
			if(world.rand.nextInt(useflow*4) == 1){
				bidtospout = Blocks.orediamond.blockID;
				meta = 0;
				EntityBlock eb = (EntityBlock) world.createEntityByName("DangerZone:EntityBlock", dimension, 
						posx+0.5f, 
						posy+1.25f,
						posz+0.5f);
				if(eb != null){
					eb.init();
					eb.setBID(bidtospout);
					eb.setIID(meta);
					if(world.rand.nextBoolean())eb.setOnFire(world.rand.nextInt(10));
					eb.motiony = flowheight+DangerZone.rand.nextFloat()*flowheight/2;
					eb.motionx = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*flowheight/2;
					eb.motionz = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*flowheight/2;
					world.spawnEntityInWorld(eb);
				}
			}
			if(world.rand.nextInt(useflow*4) == 1){
				bidtospout = Blocks.blockinstability_large.blockID;
				meta = 0;
				EntityExplosiveBlock eb = (EntityExplosiveBlock) world.createEntityByName("DangerZone:EntityExplosiveBlock", dimension, 
						posx+0.5f, 
						posy+1.25f,
						posz+0.5f);
				if(eb != null){
					eb.init();
					eb.setBID(bidtospout);
					eb.setIID(meta);
					if(world.rand.nextBoolean())eb.setOnFire(world.rand.nextInt(10));
					eb.setAttackDamage(1+world.rand.nextInt(10));
					eb.motiony = flowheight+DangerZone.rand.nextFloat()*flowheight/2;
					eb.motionx = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*flowheight/2;
					eb.motionz = (DangerZone.rand.nextFloat()-DangerZone.rand.nextFloat())*flowheight/2;
					world.spawnEntityInWorld(eb);
				}
			}
			
			
			if(world.rand.nextInt(100) == 1){				
				EntityCloud eb = (EntityCloud) world.createEntityByName("DangerZone:Cloud", dimension, 
						posx+0.5f, 
						200f,
						posz+0.5f);
				if(eb != null){
					eb.init();
					eb.setBID(Blocks.cloud_thunder.blockID);
					world.spawnEntityInWorld(eb);
				}
			}
			
			for(i=-2;i<=2&&!found;i++){
				for(k=-2;k<=2&&!found;k++){
					if(world.getblock(dimension, (int)posx+i, (int)posy, (int)posz+k) != 0){
						found = true;
					}
				}
			}
			if(found){
				posy+=1;
				if(posy > 200)this.deadflag = true;
			}else{
				posy -= 0.1f;
			}

		}else{
			//yes, I know they are out of sync... cool anyway!
			if(world.rand.nextInt(useflow) == 1){
				int howmany = 3 + world.rand.nextInt(9);
				for(int i=0;i<howmany;i++){
					Particle eb = world.createParticleByName("DangerZone:ParticleSmoke", dimension, 
							posx + 0.5f + (world.rand.nextFloat()-world.rand.nextFloat()), 
							posy, 
							posz + 0.5f + (world.rand.nextFloat()-world.rand.nextFloat()));
					if(eb != null){
						eb.motiony = world.rand.nextFloat()*0.25f;
						eb.motionx = (world.rand.nextFloat()-world.rand.nextFloat())*0.02f;
						eb.motionz = (world.rand.nextFloat()-world.rand.nextFloat())*0.02f;
						world.spawnParticleInWorld(eb);
					}
				}
			}
			if(world.rand.nextInt(useflow*2) == 1){
				int howmany = 3 + world.rand.nextInt(9);
				for(int i=0;i<howmany;i++){
					Particle eb = world.createParticleByName("DangerZone:ParticleFire", dimension, 
							posx + 0.5f + (world.rand.nextFloat()-world.rand.nextFloat()), 
							posy, 
							posz + 0.5f + (world.rand.nextFloat()-world.rand.nextFloat()));
					if(eb != null){
						eb.motiony = world.rand.nextFloat()*0.25f;
						eb.motionx = (world.rand.nextFloat()-world.rand.nextFloat())*0.02f;
						eb.motionz = (world.rand.nextFloat()-world.rand.nextFloat())*0.02f;
						world.spawnParticleInWorld(eb);
					}
				}
			}
		}
		super.update( deltaT );
	}
	
	public boolean getCanSpawnHereNow(World w, int dimension, int x, int y, int z){
		return true;
	}
		
	
	public Texture getTexture(){
		return null; //We are invisible...
	}
	

	
}

