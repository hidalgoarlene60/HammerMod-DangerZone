package dangerzone.blocks;

import java.util.List;
import java.util.ListIterator;

import dangerzone.DangerZone;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityFire;


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

public class BlockLava extends BlockLiquidActive {
	
	//static_partner is set in post_load_processing!
	
	public BlockLava(String n, String txt) {
		super(n, txt);
		brightness = 0.55f;
	}
	
	public void tickMe(World w, int d, int x, int y, int z){	
		if(w.isServer){
			List<Entity> nearby_list = null;
			ListIterator<Entity> li;
			Entity e = null;
			int bid, bt;
			int i, j, k;
			//Make some fire!						
			nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange(8.0f, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);

			for(i=-3;i<=3;i++){
				for(j=-2;j<=2;j++){
					for(k=-3;k<=3;k++){
						if(w.rand.nextInt(2+Math.abs(i)+Math.abs(k)+Math.abs(j)) < 3){							
							bid = w.getblock(d, x+i, y+j, z+k);
							if(bid != Blocks.lava.blockID && bid != Blocks.lavastatic.blockID){
								bt = Blocks.getBurnTime(bid);
								if(bt > 0){
									boolean hasfire = false;
									//Make sure there is NOT a fire entity already there!							
									if(nearby_list != null){
										if(!nearby_list.isEmpty()){
											li = nearby_list.listIterator();
											while(li.hasNext()){
												e = (Entity)li.next();
												if(e instanceof EntityFire){
													//System.out.printf("%d,  %d, %d and %f, %f, %f\n", x+i, y+j, z+k, e.posx, e.posy, e.posz);													
													if(e.posx > (double)(x+i) && e.posx < (double)x+i+1){
														//System.out.printf("xpass\n");
														if(e.posz > (double)(z+k) && e.posz < (double)z+k+1){
															//System.out.printf("zpass\n");
															if(e.posy > (double)(y+j)-0.5f && e.posy < (double)(y+j)+0.5f){
																//System.out.printf("hasfire\n");
																hasfire = true;
																break;
															}
														}
													}
												}
											}
										}
									}
									if(!hasfire){
										Entity eb = w.createEntityByName("DangerZone:Fire", d, (double)x+i+0.5f, (double)(y+j)-0.0625f, (double)z+k+0.5f);
										if(eb != null){
											eb.init();
											eb.setBID(0); //fresh fire...
											w.spawnEntityInWorld(eb);
										}
									}
								}
							}
						}
					}
				}
			}
			
			if(w.rand.nextInt(20) == 1){
				Utils.spawnParticlesFromServer(w, "DangerZone:ParticleFire", w.rand.nextInt(3)+2, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
				Utils.spawnParticlesFromServer(w, "DangerZone:ParticleSmoke", w.rand.nextInt(3)+2, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
			}
		}
		super.tickMe(w, d, x, y, z);
	}
	
	public void entityInLiquid(Entity e){
		e.doSetOnFire(100);	
	}
	

}
