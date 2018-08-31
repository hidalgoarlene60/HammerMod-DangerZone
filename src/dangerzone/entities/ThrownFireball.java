package dangerzone.entities;

import java.util.List;
import java.util.ListIterator;

import org.newdawn.slick.opengl.Texture;


import dangerzone.BreakChecks;
import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;


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

public class ThrownFireball extends ThrownBlockItem {
	
	public float explosive_power = 1.0f; //tested up to 10,000.

	public ThrownFireball(World w) {
		super(w);
		uniquename = "DangerZone:ThrownFireball";
		setAttackDamage(10.0f);
		maxrenderdist = 200; //in blocks
	}
	
	public void doSpecialEffects(double x, double y, double z){
		int pfreq = 20;
		if(explosive_power >= 10)pfreq = 10;
		if(explosive_power >= 100)pfreq = 5;
		if(explosive_power >= 1000)pfreq = 2;
		if(world.rand.nextInt(pfreq) == 0)Utils.spawnParticlesFromServer(world, "DangerZone:ParticleFire", world.rand.nextInt(2)+1, dimension, x, y, z);
		if(world.rand.nextInt(pfreq) == 0)Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", world.rand.nextInt(2)+1, dimension, x, y, z);
	}
	
	public void doHitSomething(boolean hb, double x, double y, double z, boolean he, Entity ent){
		//go ahead and hit this one now... direct hit.
		
		if(explosive_power > 100)explosive_power = 100;
		
		if(he && ent != null){
			ent.doAttackFromCustom(thrower, DamageTypes.EXPLOSIVE, getAttackDamage()*explosive_power, false);
			ent.doSetOnFire(100);
			//add a little more knockback!
			float dir = (float) Math.atan2(ent.posz - z, ent.posx - x);
			float hdist = (float) Math.sqrt((ent.posx - x)*(ent.posx - x) + (ent.posz - z)*(ent.posz - z));
			float ydir = (float) Math.atan2((ent.posy+ent.getHeight()/2) - y, hdist);
			float knockback = explosive_power/2;
			knockback /= (ent.getWidth()*ent.getHeight());
			if(knockback > 10)knockback = 10;
			//System.out.printf("ydir is %f from %f and %f\n", ydir, (ent.posy+ent.getHeight()/2)-y, hdist );
			//System.out.printf("sin = %f\n", Math.sin(ydir));
			//System.out.printf("cos = %f\n", Math.cos(ydir));
			//System.out.printf("knockback 1 added %f\n", knockback);
			ent.motionx += Math.cos(dir)*knockback*Math.cos(ydir);
			ent.motionz += Math.sin(dir)*knockback*Math.cos(ydir);
			ent.motiony += knockback*Math.sin(ydir);
			if(ent instanceof Player){
				Player pl = (Player)ent;
				pl.server_thread.sendVelocityUpdateToPlayer(ent.motionx, ent.motiony, ent.motionz);
			}
		}

			int which = world.rand.nextInt(5);
			if(which == 0)world.playSound("DangerZone:small_explosion1", dimension, posx+motionx, posy+motiony, posz+motionz, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 1)world.playSound("DangerZone:small_explosion2", dimension, posx+motionx, posy+motiony, posz+motionz, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 2)world.playSound("DangerZone:small_explosion3", dimension, posx+motionx, posy+motiony, posz+motionz, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 3)world.playSound("DangerZone:small_explosion4", dimension, posx+motionx, posy+motiony, posz+motionz, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 4)world.playSound("DangerZone:small_explosion5", dimension, posx+motionx, posy+motiony, posz+motionz, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));

		//now break some blocks and see if we hit anything else nearby!
		float raydist = (float) Math.sqrt(explosive_power);
		float dr;
		float curdist = 0;
		int nray = (int) (explosive_power*30);
		double dx, dy, dz;
		double dir; 
		double xzscale; 
		int i;
		float raypower, powerleft, powerlost;
		int lx, ly, lz;
		int ix, iy, iz;
		int bid;
		double fx, fy, fz;
		int spk;
		spk = 2;
		if(explosive_power >= 10)spk = 10;

		/*
		 * Yes, I chose ray-tracing. It is a little slow, but it makes a very nice random-ish hole... and some beautiful effects!
		 */
		for(i=0;i<nray;i++){
			//curdist = 0;
			//calculate proper dx,dy,dz directions
			dir = Math.toRadians(world.rand.nextDouble()*360d); 
			xzscale = Math.toRadians(world.rand.nextDouble()*360d); 
			dx = Math.cos(dir);
			dz = Math.sin(dir);
			dy = Math.cos(xzscale)/2;
			xzscale = Math.abs(Math.sin(xzscale));
			dx *= xzscale;
			dz *= xzscale;

			raypower = explosive_power*20;
			powerlost = 0;
			lx = ly = lz = 0;
			
			/*
			 * increment out the ray
			 */
	
			dr = raydist + ((world.rand.nextFloat()-world.rand.nextFloat())*0.2f*raydist); //scraggly edge

			
			//start further out the longer we go, because there is nothing left in the middle anyway!
			curdist = i;
			curdist /= nray;
			curdist *= dr;
			while(curdist < dr){
				fx = posx + motionx + dx*curdist;
				fy = posy + motiony + dy*curdist;
				fz = posz + motionz + dz*curdist;
				ix = (int)fx;
				iy = (int)fy;
				iz = (int)fz;
				
				//wait until block actually changes
				if(ix != lx || iy != ly || iz != lz){
					lx = ix; ly = iy; lz = iz;
					bid = world.getblock(dimension, ix, iy, iz);
					if(bid != 0){ //Hit a block!!!	
						powerleft = raypower*(dr-curdist)/dr;
						powerleft -= powerlost;
						if(powerleft <= 0)break;
						if(powerleft > Blocks.getMinDamage(bid)){
							if(powerleft > Blocks.getMaxDamage(bid)){
								if(BreakChecks.canChangeBlock(thrower, dimension, ix, iy, iz, 0, 0)
										|| BreakChecks.canChangeBlock(thrower2, dimension, ix, iy, iz, 0, 0)){
									if(explosive_power > 500 && curdist/dr < 0.9f){ 
										world.setblockandmetanonotify(dimension, ix, iy, iz, 0, 0);	//too much CPU!!! back off!
									}else{
										world.setblockandmeta(dimension, ix, iy, iz, 0, 0);	
									}
									if(world.rand.nextInt(spk*2) == 0){
										int ibid = Blocks.getBlockDrop(bid, null, world, dimension, ix, iy, iz);
										int iiid = Blocks.getItemDrop(bid, null, world, dimension, ix, iy, iz);
										Utils.doDropRand(world, ibid, iiid, 1, dimension, ix, iy, iz);
									}
								}
								if(world.rand.nextInt(spk) == 0)Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", world.rand.nextInt(3)+3, dimension, fx, fy, fz);

							}							
						}
						powerlost += Blocks.getMaxDamage(bid);						
					}
					if(world.rand.nextInt(spk) == 0)Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSparkle", 1, dimension, fx, fy, fz);
				}
				curdist += 0.1701f;
			}
			
			//keep going a little and set fire to things!
			if(powerlost == 0){
				lx = ly = lz = 0;
				while(curdist < dr*4){
					fx = posx + motionx + dx*curdist;
					fy = posy + motiony + dy*curdist;
					fz = posz + motionz + dz*curdist;
					ix = (int)fx;
					iy = (int)fy;
					iz = (int)fz;

					if(ix != lx || iy != ly || iz != lz){
						lx = ix; ly = iy; lz = iz;
						bid = world.getblock(dimension, ix, iy, iz);
						if(bid != 0){ //Hit a block!!!	
							if(Blocks.getBurnTime(bid) > 0 && world.rand.nextInt(3) == 1){
								EntityFire eb = (EntityFire)world.createEntityByName("DangerZone:Fire", dimension, ((double)ix)+0.5f, ((double)iy)-0.0625f, ((double)iz)+0.5f);
								if(eb != null){
									eb.init();
									eb.started_by = thrower;
									world.spawnEntityInWorld(eb);
								}
							}
							break; //fire only happens (maybe) on very first block hit.
						}else{
							if(world.rand.nextInt((int)curdist+1) == 0)Utils.spawnParticlesFromServer(world, "DangerZone:ParticleFire", world.rand.nextInt(2)+1, dimension, fx, fy, fz);
							if(world.rand.nextInt((int)curdist+1) == 0)Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", world.rand.nextInt(2)+1, dimension, fx, fy, fz);
						}
					}
					curdist += 0.3301f;
				}
			}
		}
		
		dr = raydist*2;
		
		//Now let's do some damage to surrounding critters...
		List<Entity> nearby_list = null;
		ListIterator<Entity> li;
		Entity enthit = null;
		float hdist, ydir;
		float exp = explosive_power/10;
		
		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		nearby_list = DangerZone.server.entityManager.findEntitiesInRange(16.0f+dr, dimension, posx, posy, posz);
		li = nearby_list.listIterator();
		
		while(li.hasNext()){
			enthit = (Entity)li.next();
			if(enthit != this && !enthit.canthitme){ 
				fx = (posx + motionx) - enthit.posx;
				fy = (posy + motiony) - enthit.posy;
				fz = (posz + motionz) - enthit.posz;
				curdist = (float) Math.sqrt((fx*fx)+(fy*fy)+(fz*fz));
				curdist -= enthit.getWidth()/2;
				if(curdist < enthit.getWidth()/2)curdist = enthit.getWidth()/2;
				if(curdist < 1)curdist = 1;
				if(curdist < dr){
					//curdist *= curdist; //drop with dist squared!
					if(!(enthit instanceof EntityBlock)){
						enthit.doSetOnFire(50);
						enthit.doAttackFromCustom(thrower, DamageTypes.EXPLOSIVE, getAttackDamage()*exp/(curdist*curdist), false);
					}else{
						enthit.doSetOnFire(5);
					}					
					//add a little more knockback!
					float knockback = exp/2;
					knockback /= (enthit.getWidth()*enthit.getHeight());
					knockback /= curdist;
					if(knockback > 10)knockback = 10;
					dir = Math.atan2(enthit.posz - z, enthit.posx - x);
					hdist = (float) Math.sqrt((enthit.posx - x)*(enthit.posx - x) + (enthit.posz - z)*(enthit.posz - z));
					ydir = (float) Math.atan2((enthit.posy+enthit.getHeight()/2) - y, hdist);
					enthit.motionx += Math.cos(dir)*knockback*Math.cos(ydir);
					enthit.motionz += Math.sin(dir)*knockback*Math.cos(ydir);
					enthit.motiony += knockback*Math.sin(ydir);
					//System.out.printf("knockback 2 added %f\n", knockback);
					if(enthit instanceof Player){
						Player pl = (Player)enthit;
						pl.server_thread.sendVelocityUpdateToPlayer(enthit.motionx, enthit.motiony, enthit.motionz);
					}
				}

			}
		}
			
		
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/misc/fireball.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}
