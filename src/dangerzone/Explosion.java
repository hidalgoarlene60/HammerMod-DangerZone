package dangerzone;

import java.util.List;
import java.util.ListIterator;

import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlock;
import dangerzone.entities.EntityFire;

public class Explosion {

	
	public static void boom(Entity thrower, World world, int dimension, double x, double y, double z, int pwr, boolean doentitydamage){
		if(!world.isServer)return;
		if(pwr <= 0 || pwr > 20000)return;
		
		if(pwr < 50){
			int which = world.rand.nextInt(5);
			if(which == 0)world.playSound("DangerZone:small_explosion1", dimension, x, y, z, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 1)world.playSound("DangerZone:small_explosion2", dimension, x, y, z, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 2)world.playSound("DangerZone:small_explosion3", dimension, x, y, z, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 3)world.playSound("DangerZone:small_explosion4", dimension, x, y, z, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 4)world.playSound("DangerZone:small_explosion5", dimension, x, y, z, 1.0f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
		}else{
			int which = world.rand.nextInt(3);
			if(which == 0)world.playSound("DangerZone:large_explosion1", dimension, x, y, z, 2.5f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 1)world.playSound("DangerZone:large_explosion2", dimension, x, y, z, 2.5f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
			if(which == 2)world.playSound("DangerZone:large_explosion3", dimension, x, y, z, 2.5f, 1.0f+((world.rand.nextFloat()-world.rand.nextFloat())*0.3f));
		}
		//now break some blocks and see if we hit anything else nearby!
		float raydist = (float) Math.sqrt(pwr);
		float dr;
		float curdist = 0;
		int nray = (int) (pwr*30);
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
		spk = 4;
		if(pwr >= 10)spk = 10;
		if(pwr >= 100)spk = 50; //don't overload with sparkles! causes serious lag!
		if(pwr >= 1000)spk = 250;
		if(pwr >= 5000)spk = 2000;
		
		if(pwr >= 5000){
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

			raypower = pwr*40;
			powerlost = 0;
			lx = ly = lz = 0;
			
			/*
			 * increment out the ray
			 */
			if(pwr < 500){
				dr = raydist + ((world.rand.nextFloat()-world.rand.nextFloat())*0.2f*raydist); //scraggly edge
			}else{
				dr = raydist + ((world.rand.nextFloat()-world.rand.nextFloat())*0.1f*raydist); //scraggly edge
			}
			
			//start further out the longer we go, because there is nothing left in the middle anyway!
			curdist = i;
			curdist /= nray;
			curdist *= dr;
			while(curdist < dr){
				fx = x + dx*curdist;
				fy = y + dy*curdist;
				fz = z + dz*curdist;
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
								if(BreakChecks.canChangeBlock(thrower, dimension, ix, iy, iz, 0, 0)){
									if(pwr > 500 && (curdist/dr < 0.9f || i < nray/2)){ 
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
					fx = x + dx*curdist;
					fy = y + dy*curdist;
					fz = z + dz*curdist;
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
		
		if(!doentitydamage)return;
		
		dr = raydist*2;
		
		//Now let's do some damage to surrounding critters...
		List<Entity> nearby_list = null;
		ListIterator<Entity> li;
		Entity enthit = null;
		float hdist, ydir;
		
		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		nearby_list = DangerZone.server.entityManager.findEntitiesInRange(16.0f+dr, dimension, x, y, z);
		li = nearby_list.listIterator();
		
		while(li.hasNext()){
			enthit = (Entity)li.next();
			if(!enthit.canthitme && !(enthit instanceof EntityBlock)){ 
				fx = x - enthit.posx;
				fy = y - enthit.posy;
				fz = z - enthit.posz;
				curdist = (float) Math.sqrt((fx*fx)+(fy*fy)+(fz*fz));
				curdist -= enthit.getWidth()/2;
				if(curdist < enthit.getWidth()/2)curdist = enthit.getWidth()/2;
				if(curdist < 1)curdist = 1;
				if(curdist < dr){
					//curdist *= curdist; //drop with dist squared!
					if(!(enthit instanceof EntityBlock)){
						enthit.doSetOnFire(50);
						enthit.doAttackFromCustom(null, DamageTypes.EXPLOSIVE, pwr/(curdist*curdist), false);
					}				
					//add a little more knockback!
					float knockback = pwr/3;
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
	

}
