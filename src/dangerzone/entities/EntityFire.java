package dangerzone.entities;

import java.util.List;
import java.util.ListIterator;

import org.newdawn.slick.opengl.Texture;



import dangerzone.BreakChecks;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.threads.LightingThread;

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


public class EntityFire extends Entity {

	public Texture texturetb;
	private int seq = 0;
	private int sides = 0;
	private int mybt = 0;
	private short firesave[][][] = null;
	private int lightingticker = 300;
	private int adjusted_sides;
	public Entity started_by = null;
	public boolean diefast = false;
	
	public EntityFire(World w) {
		super(w);
		uniquename = "DangerZone:Fire";
		ignoreCollisions = true;
		width = 1.125f;
		height = 1.125f;
		firesave = new short [3][4][3];
		setBID(0); //anti-spread...
	}
	
	public void update(float deltaT){	
		int ts = 0;
		int bid;
		boolean islava = false;
		motionx = motiony = motionz = 0;
		rotation_pitch = rotation_yaw = rotation_roll = 0;
		//entity base is actually 0.0625 below block!
		bid = world.getblock(dimension, (int)posx+1, (int)posy+1, (int)posz);
		if(bid == 0 || bid == Blocks.lava.blockID)ts |= 0x01;
		if(bid == Blocks.lava.blockID)islava = true;
		bid = world.getblock(dimension, (int)posx-1, (int)posy+1, (int)posz);
		if(bid  == 0 || bid == Blocks.lava.blockID)ts |= 0x02;
		if(bid == Blocks.lava.blockID)islava = true;
		bid = world.getblock(dimension, (int)posx, (int)posy+1, (int)posz+1);
		if(bid  == 0 || bid == Blocks.lava.blockID)ts |= 0x04;
		if(bid == Blocks.lava.blockID)islava = true;
		bid = world.getblock(dimension, (int)posx, (int)posy+1, (int)posz-1);
		if(bid  == 0 || bid == Blocks.lava.blockID)ts |= 0x08;
		if(bid == Blocks.lava.blockID)islava = true;
		bid = world.getblock(dimension, (int)posx, (int)posy+2, (int)posz);
		if(bid  == 0 || bid == Blocks.lava.blockID)ts |= 0x010;
		if(bid == Blocks.lava.blockID)islava = true;
		bid = world.getblock(dimension, (int)posx, (int)posy, (int)posz);
		if(bid  == 0 || bid == Blocks.lava.blockID)ts |= 0x020;
		if(bid == Blocks.lava.blockID)islava = true;
		sides = ts;
		if(sides == 0 && !islava)this.deadflag = true; //oops!
		//update sides for rendering
		if(!world.isServer){
			lightingticker++;
			if(lightingticker > 300){
				lightingticker = world.rand.nextInt(150); //make it variable because we will surely have to retry!
				LightingThread.addRequest(dimension, (int)posx, (int)(posy+1), (int)posz, 0.65f); //make some light!
			}			
		}else{
			int i, j, k, bt, btuse;
			mybt++;
			if(!diefast && !BreakChecks.fireDamage(started_by!=null?started_by:this, dimension, (int)posx, (int)posy+1, (int)posz)){
				diefast = true;
			}
			bt = Blocks.getBurnTime(world.getblock(dimension, (int)posx, (int)posy+1, (int)posz));
			if(!DangerZone.start_client || diefast){ //if networked
				if(bt > 4)bt = (int)Math.sqrt(bt);
			}
			if(mybt > bt){
				if(bt > 0){
					if(!diefast){
						world.setblockWithPerm(started_by!=null?started_by:this, dimension, (int)posx, (int)posy+1, (int)posz, 0);
					}
				}
				this.deadflag = true;
			}
			//check to see if we should spread...
			for(i=-1;i<=1;i++){
				for(j=0;j<=3;j++){
					for(k=-1;k<=1;k++){
						if(i==0&&j==0&&k==0)continue;
						if(firesave[i+1][j][k+1]==0){
							bt = Blocks.getBurnTime(world.getblock(dimension, (int)posx+i, (int)(posy)+j, (int)posz+k));
							if(bt > 0){
								btuse = bt;
								if(bt > 10000)btuse = 10000; //because FireStone is MAXINT-2 and quickly overflow into negative...
								if(world.rand.nextInt(btuse+1+getBID()) == 0){
									//spread!
									List<Entity> nearby_list = null;
									ListIterator<Entity> li;
									Entity e = null;
									boolean hasfire = false;
									//Make sure there is NOT a fire entity already there!							
									nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange(4.0f, dimension, (int)posx+i, (int)posy+j, (int)posz+k);
									if(nearby_list != null){
										if(!nearby_list.isEmpty()){
											li = nearby_list.listIterator();
											while(li.hasNext()){
												e = (Entity)li.next();
												if(e instanceof EntityFire){
													//System.out.printf("%f,  %f, %f and %f, %f, %f\n", posx, posy, posz, e.posx, e.posy, e.posz);
													//System.out.printf("%d,  %d, %d\n",i, j, k);
													if(e.posx > posx+(double)i-0.5f && e.posx < posx+(double)i+0.5f){
														//System.out.printf("xpass\n");
														if(e.posz > posz+(double)k-0.5f && e.posz < posz+(double)k+0.5f){
															//System.out.printf("zpass\n");
															if(e.posy > posy+(double)j-1.5f && e.posy < posy+(double)j-0.5f){
																//System.out.printf("hasfire\n");
																hasfire = true;
																firesave[i+1][j][k+1]=1;
																break;
															}
														}
													}
												}else{
													if(e instanceof EntityLiving){
														e.doSetOnFire(120);
													}
												}
											}
										}
									}
									if(!hasfire){
										//light it up!
										int ix, iy, iz;
										//System.out.printf("spawn fire\n");
										ix = (int)posx+i;
										iy = (int)posy+j;
										iz = (int)posz+k;
										EntityFire eb = (EntityFire)world.createEntityByName("DangerZone:Fire", dimension, (double)ix+0.5f, (double)iy-0.0625f, (double)iz+0.5f);
										if(eb != null){
											eb.init();
											eb.started_by = started_by;
											eb.diefast = diefast;
											eb.setBID(getBID()+2); //slow to a stop...
											if(!DangerZone.start_client || diefast){ //if networked
												eb.setBID(getBID()+10); //slow to a stop... quickly...
											}
											world.spawnEntityInWorld(eb);
										}
										firesave[i+1][j][k+1]=1;
									}
								}
							//}else{ //someone could place burnables next to a long burning block...
							//	firesave[i+1][j][k+1]=1; //don't check next time!
							}
						}
					}
				}
			}
			
		}
		if(world.rand.nextInt(5) == 0){
			seq++;
			seq &= 0x07;
		}
		adjusted_sides = sides;

		if(DangerZone.start_client && DangerZone.player != null){
			//take a little extra strain off when they are not up close
			if(DangerZone.player.posy > posy+4)adjusted_sides &= 0x1f;
			if(DangerZone.player.posy < posy-3)adjusted_sides &= 0x2f;
			if(DangerZone.player.posx > posx+5)adjusted_sides &= 0x3d;
			if(DangerZone.player.posx < posx-5)adjusted_sides &= 0x3e;		
			if(DangerZone.player.posz > posz+5)adjusted_sides &= 0x37;
			if(DangerZone.player.posz < posz-5)adjusted_sides &= 0x3b;
		}
		super.update(deltaT);
	}
	
	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){
		if(world.isServer && world.rand.nextInt(2) == 0)this.deadflag = true;
		return false;
	}
	
	public boolean leftClickedByPlayer(Player p, InventoryContainer ic){
		if(world.isServer){
			if(world.rand.nextInt(2) == 0)this.deadflag = true;
		}
		return false;
	}
	
	public int gettextureSeq(){
		return seq;
	}
	
	//which sides should we render?
	public int getSides(){
		return adjusted_sides;
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/misc/"+ "fire_side.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}
	
	public Texture getTextureBottom(){
		if(texturetb == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texturetb = TextureMapper.getTexture("res/misc/"+ "fire_topbottom.png");	//this is not fast, so we keep our own pointer!
		}
		return texturetb;
	}
	

}
