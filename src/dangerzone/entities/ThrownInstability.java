package dangerzone.entities;

import java.util.List;
import java.util.ListIterator;


import dangerzone.BreakChecks;
import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.Player;
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

public class ThrownInstability extends ThrownBlockItem {
	
	public float explosive_power = 1.0f; //tested up to 10,000.

	public ThrownInstability(World w) {
		super(w);
		uniquename = "DangerZone:ThrownInstability";
		setAttackDamage(10.0f);
	}
	
	public void doSpecialEffects(double x, double y, double z){
		int pfreq = 40;
		if(explosive_power >= 10)pfreq = 20;
		if(explosive_power >= 100)pfreq = 10;
		if(explosive_power >= 1000)pfreq = 5;
		if(world.rand.nextInt(pfreq) == 0)Utils.spawnParticlesFromServer(world, "DangerZone:ParticleFire", world.rand.nextInt(3)+2, dimension, x, y, z);
		if(world.rand.nextInt(pfreq) == 0)Utils.spawnParticlesFromServer(world, "DangerZone:ParticleSmoke", world.rand.nextInt(3)+2, dimension, x, y, z);
	}
	
	public void doHitSomething(boolean hb, double x, double y, double z, boolean he, Entity ent){
		//go ahead and hit this one now... direct hit.
		if(explosive_power < 0.01f)return;
		if(he && ent != null){
			ent.doAttackFromCustom(thrower, DamageTypes.EXPLOSIVE, getAttackDamage()*explosive_power, false);
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
		if(explosive_power < 50){
			int which = world.rand.nextInt(5);
			if(which == 0)world.playSound("DangerZone:small_explosion1", dimension, posx+motionx, posy+motiony, posz+motionz, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 1)world.playSound("DangerZone:small_explosion2", dimension, posx+motionx, posy+motiony, posz+motionz, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 2)world.playSound("DangerZone:small_explosion3", dimension, posx+motionx, posy+motiony, posz+motionz, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 3)world.playSound("DangerZone:small_explosion4", dimension, posx+motionx, posy+motiony, posz+motionz, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 4)world.playSound("DangerZone:small_explosion5", dimension, posx+motionx, posy+motiony, posz+motionz, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
		}else{
			int which = world.rand.nextInt(3);
			if(which == 0)world.playSound("DangerZone:large_explosion1", dimension, posx+motionx, posy+motiony, posz+motionz, 2.5f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 1)world.playSound("DangerZone:large_explosion2", dimension, posx+motionx, posy+motiony, posz+motionz, 2.5f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 2)world.playSound("DangerZone:large_explosion3", dimension, posx+motionx, posy+motiony, posz+motionz, 2.5f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
		}
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
		if(explosive_power >= 100)spk = 50; //don't overload with sparkles! causes serious lag!
		if(explosive_power >= 1000)spk = 250;
		if(explosive_power >= 5000)spk = 2000;
		
		if(explosive_power >= 5000){
			//need a smoother edge so as to not drive the renderer nutso...
			raydist *= 0.75f;
			nray *= 2;
		}

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

			raypower = explosive_power*40;
			powerlost = 0;
			lx = ly = lz = 0;
			
			/*
			 * increment out the ray
			 */
			if(explosive_power < 500){
				dr = raydist + ((world.rand.nextFloat()-world.rand.nextFloat())*0.2f*raydist); //scraggly edge
			}else{
				dr = raydist + ((world.rand.nextFloat()-world.rand.nextFloat())*0.1f*raydist); //scraggly edge
			}
			
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
				while(curdist < dr*2){
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
							if(Blocks.getBurnTime(bid) > 0 && world.rand.nextInt(5) == 1){
								EntityFire eb = (EntityFire)world.createEntityByName("DangerZone:Fire", dimension, ((double)ix)+0.5f, ((double)iy)-0.0625f, ((double)iz)+0.5f);
								if(eb != null){
									eb.init();
									eb.started_by = thrower;
									world.spawnEntityInWorld(eb);
								}
							}
							break; //fire only happens (maybe) on very first block hit.
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
						enthit.doAttackFromCustom(thrower, DamageTypes.EXPLOSIVE, getAttackDamage()*explosive_power/(curdist*curdist), false);
					}					
					//add a little more knockback!
					float knockback = explosive_power/4;
					knockback /= (enthit.getWidth()*enthit.getHeight());
					knockback /= curdist;
					//System.out.printf("knockback 2 added %f\n", knockback);
					if(knockback > 10)knockback = 10;
					dir = Math.atan2(enthit.posz - z, enthit.posx - x);
					hdist = (float) Math.sqrt((enthit.posx - x)*(enthit.posx - x) + (enthit.posz - z)*(enthit.posz - z));
					ydir = (float) Math.atan2((enthit.posy+enthit.getHeight()/2) - y, hdist);
					enthit.motionx += Math.cos(dir)*knockback*Math.cos(ydir);
					enthit.motionz += Math.sin(dir)*knockback*Math.cos(ydir);
					enthit.motiony += knockback*Math.sin(ydir);					
					if(enthit instanceof Player){
						Player pl = (Player)enthit;
						pl.server_thread.sendVelocityUpdateToPlayer(enthit.motionx, enthit.motiony, enthit.motionz);
					}
				}

			}
		}
		
	}

}
