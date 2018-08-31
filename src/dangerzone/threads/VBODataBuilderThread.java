package dangerzone.threads;
import dangerzone.Chunk;
import dangerzone.DangerZone;
import dangerzone.Dimensions;
import dangerzone.Ores;
import dangerzone.StitchedTexture;
import dangerzone.VBOBuffer;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.blocks.BlockRotation;
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



/*
 * Just scan the blocks and build VBO buffers.
 * Let the WorldRenderer handle all the actual graphics.
 * 
 */

public class VBODataBuilderThread implements Runnable {

	public World world = null;
	public int blockrenderwidth = 16;
	private static StitchedTexture st_last = null;
	private static VBOBuffer v_last = null;
	public static float cbr, cbg, cbb;
	public static float cbr_top, cbr_bottom, cbr_left, cbr_right, cbr_front, cbr_back;
	public static float cbg_top, cbg_bottom, cbg_left, cbg_right, cbg_front, cbg_back;
	public static float cbb_top, cbb_bottom, cbb_left, cbb_right, cbb_front, cbb_back;
	public static float sunpostop, sunposleft, sunposright;
	public static float sunposfront, sunposback, sunposbottom;
	private float brightness_red, brightness_green, brightness_blue;
	private volatile boolean take_a_break = false;
	private boolean fade_light_level;
	private float day_light;

	
	public VBODataBuilderThread(World w){
		world = w;
	}
	
	public void run() {

		int torender = 0;
		int i,j;
		int ci,cj;
		double pi = 3.1415926545D;
		double rdd, rr, rhdir;
		float dist = 0;
		int should_check;
		float f5yaw = 0;
		int renderclosedelta = 16;
		long currtime, lasttime;
		boolean allhere = false;
		int sleeper = 0;
		
		brightness_red = brightness_green = brightness_blue = 0;
		lasttime = currtime = System.currentTimeMillis();
		
		while(DangerZone.gameover == 0){
			
			if(DangerZone.player.rotation_pitch_head < 45 || DangerZone.player.rotation_pitch_head > 315){ //looking down or up?
				torender = DangerZone.renderdistance;
			}else{
				torender = DangerZone.renderdistance - 2;
			}
			if(torender < 3)torender = 3;
			
			if(DangerZone.f5_front){			
				f5yaw = 180f;
			}else{
				f5yaw = 0;
			}
			
			double px, py, pz, tod;
			int pd = DangerZone.player.dimension;
			px = DangerZone.player.posx;
			py = DangerZone.player.posy;
			pz = DangerZone.player.posz;
			
			tod = world.getTimeOfDay();
			if(tod > world.getLengthOfDay()/2)tod -= world.getLengthOfDay()/2;
			tod /= world.getLengthOfDay();			

			sunpostop = (float) (0.85f + 0.15f * Math.sin(Math.toRadians(360f*tod)));
			sunposleft = (float) (0.85f + 0.15f * Math.sin(Math.toRadians((360f*tod)+90f)));
			sunposright = (float) (0.85f + 0.15f * Math.sin(Math.toRadians((360f*tod)-90f)));
			sunposbottom = 0.70f;
			sunposfront = 0.85f;
			sunposback = 0.80f;

			fade_light_level = Dimensions.DimensionArray[pd].fade_light_level;
			day_light = recalcDayLight();
			
			if(DangerZone.f12_on){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
				continue; //PAUSED		
			}
			
			//constantly render far away chunks (more than 1 away)
			//check in between each for elapsed time, and if expired, re-render the close ones!

			for(i=-torender;i<=torender;i++){
				for(j=-torender;j<=torender;j++){ 
					if(Math.sqrt((i*i)+(j*j))<=torender){
						if(Math.abs(i)>1 || Math.abs(j)>1){
							dist = (float) Math.sqrt(i*i + j*j);
							rr = (float) Math.atan2((j*16), (i*16));					
							rhdir = Math.toRadians(((getRotationYawHead()+f5yaw)-90)%360f); 
							rdd = Math.abs(rr - rhdir)%(pi*2.0D);
							if(rdd > pi)rdd = rdd-(pi*2.0D);
							rdd = Math.abs(rdd); //Total differential, minus sign.
							if(DangerZone.player.rotation_pitch_head < 45 || DangerZone.player.rotation_pitch_head > 315){ //looking down or up?
								if(dist > 8 && rdd > pi/3*DangerZone.fieldOfView/45.0f){
									//skipped++;
									continue; //Don't render what we can't see!
								}
								if(dist > 4 && rdd > pi/2*DangerZone.fieldOfView/45.0f){
									//skipped++;
									continue; //Don't render what we can't see!
								}
								if(dist > 1 && rdd > pi*3/4*DangerZone.fieldOfView/45.0f){
									//skipped++;
									if(!DangerZone.f5_front && !DangerZone.f5_back)continue; //Don't render what we can't see!
								}
							}	

							should_check = 1;							
							if(world.rand.nextInt((int)(dist/2)+2) != 0)should_check = 0;
							

							take_a_break = false;
							allhere = true;
							
							//make sure that surrounding chunks exist in cache, otherwise graphics thrashes a bit with the sides of
							//the chunk having to be drawn and then UN-drawn
							
							if(!world.chunkcache.chunkExists(pd, ((i-1)*16)+(int)px, 0, (j*16)+(int)pz)){
								world.chunkcache.getDecoratedChunk(pd, ((i-1)*16)+(int)px, 0, (j*16)+(int)pz);
								allhere = false;
							}
							if(!world.chunkcache.chunkExists(pd, ((i+1)*16)+(int)px, 0, (j*16)+(int)pz)){
								world.chunkcache.getDecoratedChunk(pd, ((i+1)*16)+(int)px, 0, (j*16)+(int)pz);
								allhere = false;
							}
							if(!world.chunkcache.chunkExists(pd, (i*16)+(int)px, 0, ((j-1)*16)+(int)pz)){
								world.chunkcache.getDecoratedChunk(pd, (i*16)+(int)px, 0, ((j-1)*16)+(int)pz);
								allhere = false;
							}
							if(!world.chunkcache.chunkExists(pd, (i*16)+(int)px, 0, ((j+1)*16)+(int)pz)){
								world.chunkcache.getDecoratedChunk(pd, (i*16)+(int)px, 0, ((j+1)*16)+(int)pz);
								allhere = false;
							}
							
							if(allhere){							
								if(renderChunk(world, i, j, torender, should_check, 1, pd, px, py, pz)){
									//it was a sizing pass. Do it for real this time.
									renderChunk(world, i, j, torender, should_check, 2, pd, px, py, pz);	
								}
							}else{
								take_a_break = true;
							}

							if(take_a_break){
								try {
									Thread.sleep(4);
									if(DangerZone.wr.fps < 40)Thread.sleep(8);
									if(DangerZone.wr.fps < 20)Thread.sleep(16);
								} catch (InterruptedException e) {
									//e.printStackTrace();
								}
							}
						}
						
						//Give the CPU a break if its struggling with graphics!
						sleeper = 24 - DangerZone.renderdistance;	
						sleeper /= 4;
						try {							
							if(DangerZone.wr.fps < 40)sleeper += 8;
							if(DangerZone.wr.fps < 20)sleeper += 16;
							if(sleeper > 0)Thread.sleep(sleeper);
						} catch (InterruptedException e) {
							//e.printStackTrace();
						}
						
					}
						
					//check the time. Do we need to re-render the close chunks yet?
					//we like to do them about every one or two frames...
					//we do this so that there is no delay in placing or breaking blocks!
					currtime = System.currentTimeMillis();
					if(currtime-lasttime > renderclosedelta){
						lasttime = currtime;
						for(ci=-1;ci<=1;ci++){
							for(cj=-1;cj<=1;cj++){ 

								should_check = 1;

								if(renderChunk(world, ci, cj, torender, should_check, 1, pd, px, py, pz)){
									//it was a sizing pass. Do it for real this time.
									renderChunk(world, ci, cj, torender, should_check, 2, pd, px, py, pz);	
								}
							}
						}
					}
				}
			}

			renderclosedelta = 32; //skip a couple frames...
			renderclosedelta += (24-DangerZone.renderdistance)*8;
			if(DangerZone.wr.fps < 40)renderclosedelta += 256; 	//skip more
			if(DangerZone.wr.fps < 20)renderclosedelta += 256;	//about twice a second
			
			//System.out.printf("renderclosedelta = %d\n", renderclosedelta);
		}
	}
	
	private float getRotationYawHead(){
		return DangerZone.player.rotation_yaw_head; //put this in another routine and make it volatile so java will consistently get the latest value! maybe...
	}
	
	
	private boolean renderChunk(World world, int xrel, int zrel, int torender, int shouldcheck, int renderlevel, int pd, double px, double py, double pz){
		int i, j, k, bid, meta;
		int sides;
		int x, z;
		short leveldata[] = null;
		short levelmetadata[] = null;
		short prevleveldata[] = null;
		short nextleveldata[] = null;
		short cfrontleveldata[] = null;
		int cfrontleveldatavalid = 0;
		short cbackleveldata[] = null;
		int cbackleveldatavalid = 0;
		short cleftleveldata[] = null;
		int cleftleveldatavalid = 0;
		short crightleveldata[] = null;
		int crightleveldatavalid = 0;
		int starty = 0;
		int chunkX, chunkZ;
		int xpos, zpos;
		short lightmap[] = null;
		boolean focused = false;
		boolean drew_something = false;
		boolean leveldatainvalid = true;
		VBOBuffer v = null;
		int itemp;
		float dist;
		boolean bidSolid = false;
		int drawwidth = 8;
		boolean defective = false;
		
		if(DangerZone.f5_front || DangerZone.f5_back){
			drawwidth = 16;
		}
		
		cbr = cbg = cbb = 0;

		xpos = (xrel*16)+(int)px;
		zpos = (zrel*16)+(int)pz;
		chunkX = (xpos)>>4;
		chunkZ = (zpos)>>4;
		x = chunkX*16;
		z = chunkZ*16;
		dist = (float) Math.sqrt((xrel*xrel)+(zrel*zrel)); //chunk distance

		//This call is special. If the chunk is not in cache, it will just be ignored, but a request is sent to fetch it.
		Chunk c = world.chunkcache.getDecoratedChunk(pd, xpos, 0, zpos);
		if(c == null){
			//System.out.printf("Chunk get fail\n");
			return false;
		}
		
		WorldRenderer.VBOlistlock.lock();
		//reset existing vbos for this chunk, because we are about to rebuild them
		//this prevents a WHOLE BOATLOAD of re-extending new VBOs all the time...
		for(itemp=0;itemp<=20;itemp++){
			if(c.VBOids[itemp] <= 0)break;
			v = WorldRenderer.VBOmap.get(c.VBOids[itemp]);
			if(v != null){
				if(v.vbostate == 2){
					//System.out.printf("newvbodata not null\n");
					WorldRenderer.VBOlistlock.unlock();
					return false; //nevermind!
				}
			}else{
				//System.out.printf("missing vbo\n");
				defective = true;
			}
		}
		for(itemp=0;itemp<=20;itemp++){
			if(c.VBOids[itemp] <= 0)break;
			v = WorldRenderer.VBOmap.get(c.VBOids[itemp]);
			if(v != null){
				v.reset();
			}else{
				//System.out.printf("missing vbo 2\n");
				defective = true;
			}
		}
		
		if(defective){
			DangerZone.wr.deleteVBOlist(c.VBOids);
			c.VBOids = new long[20];
		}
		
		WorldRenderer.VBOlistlock.unlock();

		//if this chunk has been drawn at least once, we may be able to skip it.
		if(!defective && dist > 2){
			if(c.VBOids[0] > 0){
				if(renderlevel < 2){
					if(world.rand.nextInt((int)(dist/2) + 1) != 0)return false;
				}else{
					shouldcheck = 1; //real data pass
				}
			}else{
				shouldcheck = 1; ///sizing pass
				//System.out.printf("Virgin chunk %d, %d\n", c.chunkX, c.chunkZ);
			}
		}
		if(defective)shouldcheck = 1;
		
		st_last = null;
		v_last = null;
		
		//System.out.printf("redraw chunk %d, %d, %d\n", xpos>>4, zpos>>4, shouldcheck);
							
		starty = DangerZone.mindrawlevel;

		for(j=starty;j<256;j++){			
			recalcBrightness(pd, j);
			setVBOBrightness(0, c.b_red, c.b_green, c.b_blue);
			crightleveldatavalid = 0;
			cleftleveldatavalid = 0;
			cbackleveldatavalid = 0;
			cfrontleveldatavalid = 0;
			if(c.redraw == 0 && shouldcheck == 0 && c.drawn[j] == 0){
					leveldatainvalid = true;
					continue; //if not re-checking, and didn't draw anything at this level last time, ignore...
			}
						
			if(!leveldatainvalid){	
				if(j < 255){				
					if(j > 0){
						prevleveldata = leveldata;
						leveldata = nextleveldata;
						nextleveldata = c.blockdata[j+1];
					}else{
						prevleveldata = null;					
						leveldata = c.blockdata[j];
						nextleveldata = c.blockdata[j+1];
					}
				}else{
					prevleveldata = leveldata;
					leveldata = nextleveldata;
					nextleveldata = null;				
				}
			}else{
				prevleveldata = null;
				if(j>0)prevleveldata = c.blockdata[j-1];
				leveldata = c.blockdata[j];
				nextleveldata = null;
				if(j<255)nextleveldata = c.blockdata[j+1];
				leveldatainvalid = false;
			}			
			
			if(leveldata == null){
				c.drawn[j] = 0;
				continue; //no sense trying if no data!
			}
			
			
			levelmetadata = c.metadata[j];
			lightmap = null;
			if(c.lightmap != null){
				lightmap = c.lightmap[j];
			}

			drew_something = false;
			
			setVBOBrightness(0, c.b_red, c.b_green, c.b_blue);
			
			for(i=0;i<16;i++){
				for(k=0;k<16;k++){
					bid = leveldata[i*16+k];

					if(bid > 0 && bid < Blocks.blocksMAX && Blocks.BlockArray[bid] != null){
						meta = 0;
						if(levelmetadata != null)meta = levelmetadata[i*16+k];
						
						sides = 0;						  	
						//bidSolid = Blocks.isSolidForRender(bid);
						bidSolid = Blocks.BlockArray[bid].isSolidForRendering;

						if( DangerZone.all_sides || x+i > (int)px-drawwidth){ //Don't draw left if to the left...
							if(i > 0){
								if(shouldRenderSide(bid, bidSolid, leveldata[(i-1)*16+k]))sides |= 1;
							}else{
								if(cleftleveldatavalid == 0){
									cleftleveldata = world.chunkcache.getDecoratedChunkLevelData( pd, x+i-1,j,z+k);
									cleftleveldatavalid = 1;
								}
								if(cleftleveldata != null){
									if(shouldRenderSide(bid, bidSolid, cleftleveldata[((i-1)&0x0f)*16+k]))sides |= 1;
								}else{
									sides |= 1;
								}
							}
						}
						

						if( DangerZone.all_sides || x+i < (int)px+drawwidth){ //Don't draw right if to the right...
							if(i < 15){
								if(shouldRenderSide(bid, bidSolid, leveldata[(i+1)*16+k]))sides |= 2;
							}else{
								if(crightleveldatavalid == 0){
									crightleveldata = world.chunkcache.getDecoratedChunkLevelData(pd, x+i+1,j,z+k);
									crightleveldatavalid = 1;
								}
								if(crightleveldata != null){
									if(shouldRenderSide(bid, bidSolid, crightleveldata[((i+1)&0x0f)*16+k]))sides |= 2;
								}else{
									sides |= 2;
								}
							}
						}
											

						if( DangerZone.all_sides || z+k > (int)pz-drawwidth){ //Don't draw back if in front
							if(k > 0){
								if(shouldRenderSide(bid, bidSolid, leveldata[i*16+(k-1)]))sides |= 4;
							}else{
								if(cbackleveldatavalid == 0){
									cbackleveldata = world.chunkcache.getDecoratedChunkLevelData(pd, x+i,j,z+k-1);
									cbackleveldatavalid = 1;
								}
								if(cbackleveldata != null){
									if(shouldRenderSide(bid, bidSolid, cbackleveldata[i*16+((k-1)&0x0f)]))sides |= 4;
								}else{
									sides |= 4;
								}
							}
						}
						

						if( DangerZone.all_sides || z+k < (int)pz+drawwidth){ //Dont draw front if in back!
							if(k < 15){
								if(shouldRenderSide(bid, bidSolid, leveldata[i*16+(k+1)]))sides |= 8;
							}else{
								if(cfrontleveldatavalid == 0){
									cfrontleveldata = world.chunkcache.getDecoratedChunkLevelData(pd, x+i,j,z+k+1);
									cfrontleveldatavalid = 1;
								}
								if(cfrontleveldata != null){
									if(shouldRenderSide(bid, bidSolid, cfrontleveldata[i*16+((k+1)&0x0f)]))sides |= 8;
								}else{
									sides |= 8;
								}
							}
						}



						if( DangerZone.all_sides || j > (int)py-drawwidth){ //If above, check the bottom...
							if(prevleveldata == null){
								sides |= 0x10;
							}else{
								if(shouldRenderSide(bid, bidSolid, prevleveldata[i*16+k]))sides |= 0x10;
							}
						}							
						if(j < 255){
							if( DangerZone.all_sides || j < (int)py+drawwidth+2){ //If below, check the top...
								if(nextleveldata == null){
									sides |= 0x20;
								}else{
									if(shouldRenderSide(bid, bidSolid, nextleveldata[i*16+k]))sides |= 0x20;
								}
							}
						}else{
							sides |= 0x20;
						}
						
						
						//Must draw everything near focus if there is damage!
						if(WorldRenderer.focus_damage != 0){
							if(x+i > WorldRenderer.focus_x-2 && x+i < WorldRenderer.focus_x+2){
								if(z+k > WorldRenderer.focus_z-2 && z+k < WorldRenderer.focus_z+2){
									if(j > WorldRenderer.focus_y-2 && j < WorldRenderer.focus_y+2){
										if(!Blocks.isLiquid(bid)){
											sides = 0xff;
										}
									}
								}
							}
						}
						
						if(DangerZone.view_ores && dist < 4){
							//drawing dirt and sand blows it up! Too many...
							if(bid != Blocks.dirt.blockID && bid != Blocks.sand.blockID){
								if(Ores.isOre(bid))sides = 0xff;
							}
						}
						
						//double-check sides for liquids. We may have to display the top...
						//if(Blocks.isLiquid(bid)){		
						if(Blocks.BlockArray[bid].isLiquid){
							if(nextleveldata == null || !Blocks.isLiquid(nextleveldata[i*16+k])){							
								if((meta&0x0f) != 0){
									sides |= 0x20;
								}
							}
						}
						
						//if(Blocks.shouldAlwaysRender(bid))sides = 0xff;
						if(Blocks.BlockArray[bid].alwaysRender)sides = 0xff;

						if(sides != 0){ //Draw only the sides we need to!
							drew_something = true;
							focused = false;
							if(j == WorldRenderer.focus_y){
								if(x + i == WorldRenderer.focus_x){
									if(z + k == WorldRenderer.focus_z){
										focused = true;
									}
								}
							}

							if(lightmap != null){
								float tmp = lightmap[i*16+k];
								setVBOBrightness(tmp/1000f, c.b_red, c.b_green, c.b_blue);
							}

							if(Blocks.renderAllSides(bid))sides = 0xff;
							
							//normal block drawing							
							if(!focused || (focused && WorldRenderer.focus_damage == 0)){									
								if(Blocks.hasOwnRenderer(bid)){
									Blocks.renderMeToVBO(c.VBOids, DangerZone.wr, world, pd, x+i, j, z+k, bid, meta, sides, focused,
											i*blockrenderwidth, j*blockrenderwidth, k*blockrenderwidth);								
								}else{
									//Special processing for liquids
									if(Blocks.isLiquid(bid)){
										float brfl, brfr, brbl, brbr;
										int xbid;
										int xcount = 1;
										boolean checkit = false;
										
										brfl = brfr = brbl = brbr = (meta&0x0f);
										
										if((meta&0x0f)!=0)checkit = true;
										if((sides & 0x20) == 0x20){
											if(Blocks.alwaystick(bid)){ //this to filter OUT non-ticking water!
												checkit = true;
											}
										}
										
										if(checkit){
											//first, calculate where corners SHOULD be, IFF there are no liquids higher around us
											//take the average of us and the three adjoining
											//brfl
											xbid = world.chunkcache.getBlock(world, pd, x+i-1,j,z+k);
											if(Blocks.isLiquid(xbid)){
												brfl += world.chunkcache.getBlockmeta(world, pd, x+i-1,j,z+k)&0x0f;
												xcount++;
											}											
											xbid = world.chunkcache.getBlock(world, pd, x+i-1,j,z+k+1);
											if(Blocks.isLiquid(xbid)){
												brfl += world.chunkcache.getBlockmeta(world, pd, x+i-1,j,z+k+1)&0x0f;
												xcount++;
											}	
											xbid = world.chunkcache.getBlock(world, pd, x+i,j,z+k+1);
											if(Blocks.isLiquid(xbid)){
												brfl += world.chunkcache.getBlockmeta(world, pd, x+i,j,z+k+1)&0x0f;
												xcount++;
											}	
											brfl /= xcount;
											if(brfl > 0x0f)brfl = 0x0f;
											//brfr
											xcount = 1;
											xbid = world.chunkcache.getBlock(world, pd, x+i+1,j,z+k);
											if(Blocks.isLiquid(xbid)){
												brfr += world.chunkcache.getBlockmeta(world, pd, x+i+1,j,z+k)&0x0f;
												xcount++;
											}	
											xbid = world.chunkcache.getBlock(world, pd, x+i+1,j,z+k+1);
											if(Blocks.isLiquid(xbid)){
												brfr += world.chunkcache.getBlockmeta(world, pd, x+i+1,j,z+k+1)&0x0f;
												xcount++;
											}	
											xbid = world.chunkcache.getBlock(world, pd, x+i,j,z+k+1);
											if(Blocks.isLiquid(xbid)){
												brfr += world.chunkcache.getBlockmeta(world, pd, x+i,j,z+k+1)&0x0f;
												xcount++;
											}	
											brfr /= xcount;
											if(brfr > 0x0f)brfr = 0x0f;
											//brbl
											xcount = 1;
											xbid = world.chunkcache.getBlock(world, pd, x+i-1,j,z+k);
											if(Blocks.isLiquid(xbid)){
												brbl += world.chunkcache.getBlockmeta(world, pd, x+i-1,j,z+k)&0x0f;
												xcount++;
											}	
											xbid = world.chunkcache.getBlock(world, pd, x+i-1,j,z+k-1);
											if(Blocks.isLiquid(xbid)){
												brbl += world.chunkcache.getBlockmeta(world, pd, x+i-1,j,z+k-1)&0x0f;
												xcount++;
											}	
											xbid = world.chunkcache.getBlock(world, pd, x+i,j,z+k-1);
											if(Blocks.isLiquid(xbid)){
												brbl += world.chunkcache.getBlockmeta(world, pd, x+i,j,z+k-1)&0x0f;
												xcount++;
											}	
											brbl /= xcount;
											if(brbl > 0x0f)brbl = 0x0f;
											//brbr
											xcount = 1;
											xbid = world.chunkcache.getBlock(world, pd, x+i+1,j,z+k);
											if(Blocks.isLiquid(xbid)){
												brbr += world.chunkcache.getBlockmeta(world, pd, x+i+1,j,z+k)&0x0f;
												xcount++;
											}	
											xbid = world.chunkcache.getBlock(world, pd, x+i+1,j,z+k-1);
											if(Blocks.isLiquid(xbid)){
												brbr += world.chunkcache.getBlockmeta(world, pd, x+i+1,j,z+k-1)&0x0f;
												xcount++;
											}	
											xbid = world.chunkcache.getBlock(world, pd, x+i,j,z+k-1);
											if(Blocks.isLiquid(xbid)){
												brbr += world.chunkcache.getBlockmeta(world, pd, x+i,j,z+k-1)&0x0f;
												xcount++;
											}	
											brbr /= xcount;
											if(brbr > 0x0f)brbr = 0x0f;
											
											//Now go back and check the four sides to see if we need to pull up to cover...
											if(brfl!=0 || brbl!=0){
												//if we are next to a liquid
												if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i-1,j,z+k))){
													//and there is one on top of it
													if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i-1,j+1,z+k))){
														brfl = brbl = 0; //pull up to cover the side!
													}
												}
											}
											if(brfr!=0 || brbr!=0){
												//if we are next to a liquid
												if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i+1,j,z+k))){
													//and there is one on top of it
													if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i+1,j+1,z+k))){
														brfr = brbr = 0; //pull up to cover the side!
													}
												}
											}
											if(brbl!=0 || brbr!=0){
												//if we are next to a liquid
												if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i,j,z+k-1))){
													//and there is one on top of it
													if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i,j+1,z+k-1))){
														brbl = brbr = 0; //pull up to cover the side!
													}
												}
											}
											if(brfl!=0 || brfr!=0){
												//if we are next to a liquid
												if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i,j,z+k+1))){
													//and there is one on top of it
													if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i,j+1,z+k+1))){
														brfl = brfr = 0; //pull up to cover the side!
													}
												}
											}
											
											//and no. we're not done yet. more pain. check the four diagonals too...
											if(brfl!=0 ){
												//if we are next to a liquid
												if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i-1,j,z+k+1))){
													//and there is one on top of it
													if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i-1,j+1,z+k+1))){
														brfl = 0; //pull up to cover the side!
													}
												}
											}
											if(brfr!=0 ){
												//if we are next to a liquid
												if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i+1,j,z+k+1))){
													//and there is one on top of it
													if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i+1,j+1,z+k+1))){
														brfr = 0; //pull up to cover the side!
													}
												}
											}
											if(brbl!=0 ){
												//if we are next to a liquid
												if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i-1,j,z+k-1))){
													//and there is one on top of it
													if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i-1,j+1,z+k-1))){
														brbl = 0; //pull up to cover the side!
													}
												}
											}
											if(brbr!=0 ){
												//if we are next to a liquid
												if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i+1,j,z+k-1))){
													//and there is one on top of it
													if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i+1,j+1,z+k-1))){
														brbr = 0; //pull up to cover the side!
													}
												}
											}
											//and one more... if a liquid on top, draw all side high!
											if(Blocks.isLiquid(world.chunkcache.getBlock(world, pd, x+i,j+1,z+k))){
												brfl = brfr = brbl = brbr =  0; //pull up to cover the side!
											}
										}
					
										drawLiquidTexturedCubeToVBO(c.VBOids, sides, bid, meta, i*blockrenderwidth, j*blockrenderwidth, k*blockrenderwidth, brfl, brfr, brbl, brbr, Blocks.isTranslucentForRender(bid));										
									}else{
										drawTexturedCubeToVBO(c.VBOids, sides, bid, meta, i*blockrenderwidth, j*blockrenderwidth, k*blockrenderwidth);
									}
								}
							}
						}
					//}else{
					//	if(bid != 0)System.out.printf("bad block value %d\n", bid);
					}
				}
			}
			
			if(drew_something){
				c.drawn[j] = 1;
			}else{
				c.drawn[j] = 0;
			}
		}
		c.redraw = 0;

		boolean doitagain = false;

		WorldRenderer.VBOlistlock.lock();
		for(itemp=0;itemp<=20;itemp++){
			if(c.VBOids[itemp] <= 0)break;
			v = WorldRenderer.VBOmap.get(c.VBOids[itemp]);
			if(v != null){
				if(dist > 8){ //give a little help to the graphics by telling it distance!
					v.isfar = true;
				}else{
					v.isfar = false;
				}
				if(v.finishDraw()){
					doitagain = true;
				}
			}
		}
		WorldRenderer.VBOlistlock.unlock();
		if(!doitagain)take_a_break = true;
		return doitagain;
	}
	 
	private boolean shouldRenderSide(int bid, boolean thisIsSolid, int thatbid){
		if(thatbid == 0)return true;
		boolean thatIsSolid = Blocks.isSolidForRender(thatbid);
		if(thisIsSolid && thatIsSolid)return false;
		if(thisIsSolid && !thatIsSolid)return true;
		if(!thisIsSolid && !thatIsSolid && bid == thatbid)return false;
		if(!thisIsSolid && thatIsSolid)return false;
		if(Blocks.isLiquid(bid)&&(Blocks.isLiquid(thatbid)||Blocks.isWaterPlant(thatbid)))return false;
		return true;
	}
	
	public void drawTexturedCubeToVBO(long chunkvbos[], int sides, int bid, int meta, int xo, int yo, int zo) {
		
		boolean tr = Blocks.isTranslucentForRender(bid);
	
		//generic block, rotated
		if((meta & 0xfc00) != 0){
			drawRotatedTexturedCubeToVBO(chunkvbos, sides, bid, meta, xo, yo, zo, tr);
			return;
		}
		
		//generic block
		VBOBuffer v = null;
		StitchedTexture st = null;
		float brw = blockrenderwidth/2;
		if(Blocks.renderSmaller(bid))brw -= 0.01f;
			
		if((sides & 0x20) != 0){
			st = findVBOtextureforblockside(0, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				v.addVertexInfoToVBO(brw + xo, brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmin, cbr_top, cbg_top, cbb_top);		
				v.addVertexInfoToVBO(-brw + xo, brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, cbr_top, cbg_top, cbb_top);		
				v.addVertexInfoToVBO(-brw + xo, brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmax, cbr_top, cbg_top, cbb_top);
				v.addVertexInfoToVBO(brw + xo, brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, cbr_top, cbg_top, cbb_top);
			}
		}
		if((sides & 0x10) != 0){
			st = findVBOtextureforblockside(5, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				v.addVertexInfoToVBO(brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, cbr_bottom, cbg_bottom, cbb_bottom); 			
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmin, cbr_bottom, cbg_bottom, cbb_bottom); 			
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, cbr_bottom, cbg_bottom, cbb_bottom); 			
				v.addVertexInfoToVBO(brw + xo, -brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmax, cbr_bottom, cbg_bottom, cbb_bottom);
			}
		}
		if((sides & 0x08) != 0){
			st = findVBOtextureforblockside(1, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				v.addVertexInfoToVBO(brw + xo, brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, cbr_left, cbg_left, cbb_left); 	
				v.addVertexInfoToVBO(-brw + xo, brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmin, cbr_left, cbg_left, cbb_left); 		
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmax, cbr_left, cbg_left, cbb_left); 		
				v.addVertexInfoToVBO(brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, cbr_left, cbg_left, cbb_left);
			}
		}
		if((sides & 0x04) != 0){
			st = findVBOtextureforblockside(2, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				v.addVertexInfoToVBO(-brw + xo, brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmin, cbr_right, cbg_right, cbb_right); 
				v.addVertexInfoToVBO(brw + xo, brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, cbr_right, cbg_right, cbb_right); 
				v.addVertexInfoToVBO(brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, cbr_right, cbg_right, cbb_right); 
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmax, cbr_right, cbg_right, cbb_right); 
			}
		}
		if((sides & 0x01) != 0){
			st = findVBOtextureforblockside(3, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				v.addVertexInfoToVBO(-brw + xo, brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, cbr_front, cbg_front, cbb_front);
				v.addVertexInfoToVBO(-brw + xo, brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, cbr_front, cbg_front, cbb_front); 
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, cbr_front, cbg_front, cbb_front); 
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, cbr_front, cbg_front, cbb_front);
			}
		}		
		if((sides & 0x02) != 0){
			st = findVBOtextureforblockside(4, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				v.addVertexInfoToVBO(brw + xo, brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmin, cbr_back, cbg_back, cbb_back); 
				v.addVertexInfoToVBO(brw + xo, brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmin, cbr_back, cbg_back, cbb_back); 
				v.addVertexInfoToVBO(brw + xo, -brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmax, cbr_back, cbg_back, cbb_back); 
				v.addVertexInfoToVBO(brw + xo, -brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmax, cbr_back, cbg_back, cbb_back); 
			}
		}
		
	}
	
	public void drawLiquidTexturedCubeToVBO(long chunkvbos[], int sides, int bid, int meta, int xo, int yo, int zo, float inbrfl, float inbrfr, float inbrbl, float inbrbr, boolean tr) {
				
		VBOBuffer v = null;
		StitchedTexture st = null;
		int brw = blockrenderwidth/2;
		float brfl, brfr, brbl, brbr;
		
		brfl = brw - inbrfl;
		brfr = brw - inbrfr;
		brbl = brw - inbrbl;
		brbr = brw - inbrbr;			
			
		if((sides & 0x20) != 0){
			st = findVBOtextureforblockside(0, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				v.addVertexInfoToVBO(brw + xo, brbr + yo, -brw + zo, st.xoffsetmax, st.yoffsetmin, cbr, cbg, cbb);		//top
				v.addVertexInfoToVBO(-brw + xo, brbl + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, cbr, cbg, cbb);		
				v.addVertexInfoToVBO(-brw + xo, brfl + yo, brw + zo, st.xoffsetmin, st.yoffsetmax, cbr, cbg, cbb);
				v.addVertexInfoToVBO(brw + xo, brfr + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, cbr, cbg, cbb);
			}
		}
		if((sides & 0x10) != 0){
			st = findVBOtextureforblockside(5, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				v.addVertexInfoToVBO(brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, cbr, cbg, cbb); 		//bottom	
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmin, cbr, cbg, cbb); 			
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, cbr, cbg, cbb); 			
				v.addVertexInfoToVBO(brw + xo, -brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmax, cbr, cbg, cbb);
			}
		}
		if((sides & 0x08) != 0){
			st = findVBOtextureforblockside(1, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				v.addVertexInfoToVBO(brw + xo, brfr + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, cbr, cbg, cbb); 	//front
				v.addVertexInfoToVBO(-brw + xo, brfl + yo, brw + zo, st.xoffsetmin, st.yoffsetmin, cbr, cbg, cbb); 		
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmax, cbr, cbg, cbb); 		
				v.addVertexInfoToVBO(brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, cbr, cbg, cbb);
			}
		}
		if((sides & 0x04) != 0){
			st = findVBOtextureforblockside(2, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				v.addVertexInfoToVBO(-brw + xo, brbl + yo, -brw + zo, st.xoffsetmax, st.yoffsetmin, cbr, cbg, cbb); 	//back
				v.addVertexInfoToVBO(brw + xo, brbr + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, cbr, cbg, cbb); 
				v.addVertexInfoToVBO(brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, cbr, cbg, cbb); 
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmax, cbr, cbg, cbb); 
			}
		}
		if((sides & 0x01) != 0){
			st = findVBOtextureforblockside(3, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				v.addVertexInfoToVBO(-brw + xo, brfl + yo, brw + zo, st.xoffsetmax, st.yoffsetmin, cbr, cbg, cbb);	//left
				v.addVertexInfoToVBO(-brw + xo, brbl + yo, -brw + zo, st.xoffsetmin, st.yoffsetmin, cbr, cbg, cbb); 
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, -brw + zo, st.xoffsetmin, st.yoffsetmax, cbr, cbg, cbb); 
				v.addVertexInfoToVBO(-brw + xo, -brw + yo, brw + zo, st.xoffsetmax, st.yoffsetmax, cbr, cbg, cbb);
			}
		}		
		if((sides & 0x02) != 0){
			st = findVBOtextureforblockside(4, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				v.addVertexInfoToVBO(brw + xo, brbr + yo, -brw + zo, st.xoffsetmax, st.yoffsetmin, cbr, cbg, cbb); //right
				v.addVertexInfoToVBO(brw + xo, brfr + yo, brw + zo, st.xoffsetmin, st.yoffsetmin, cbr, cbg, cbb); 
				v.addVertexInfoToVBO(brw + xo, -brw + yo, brw + zo, st.xoffsetmin, st.yoffsetmax, cbr, cbg, cbb); 
				v.addVertexInfoToVBO(brw + xo, -brw + yo, -brw + zo, st.xoffsetmax, st.yoffsetmax, cbr, cbg, cbb); 
			}
		}
		
	}

	
	/*
	 * Perform block rotation the easier way. Just make sure to keep track of sides to draw!
	 */
	public void drawRotatedTexturedCubeToVBO(long chunkvbos[], int sides, int bid, int meta, int xo, int yo, int zo, boolean tr) {
		VBOBuffer v = null;
		StitchedTexture st = null;
		
		float cosrx = 1;
		float sinrx = 0;
		float cosry = 1;
		float sinry = 0;	
		float cosrz = 1;
		float sinrz = 0;
		
		//because sides move as we rotate!
		int top = 0x20;
		int bottom = 0x10;
		int front = 0x08;
		int back = 0x04;
		int left = 0x01;
		int right = 0x02;
		int tmp;
		
 		//oh good grief... rotate the brightness too... but only for Y! (grass looks like crap if we don't!)
		float ycbr_right = cbr_back;
		float ycbg_right = cbg_back;
		float ycbb_right = cbb_back;
		
		float ycbr_left = cbr_front;
		float ycbg_left = cbg_front;
		float ycbb_left = cbb_front;
		
		float ycbr_front = cbr_left;
		float ycbg_front = cbg_left;
		float ycbb_front = cbb_left;
		
		float ycbr_back = cbr_right;
		float ycbg_back = cbg_right;
		float ycbb_back = cbb_right;
		
		float tmpr, tmpg, tmpb;
		
		float brw = blockrenderwidth/2;
		if(Blocks.renderSmaller(bid))brw -= 0.01f;

		
		//if(bid == Blocks.workbench.blockID)System.out.printf("Rotation = 0x%x\n", meta);
		
		if((meta & BlockRotation.X_MASK) != 0){
			if((meta & BlockRotation.X_MASK) == BlockRotation.X_ROT_270){
				cosrx = 0; sinrx = -1;
				tmp = front;
				front = top;
				top = back;
				back = bottom;
				bottom = tmp;	
			}else if((meta & BlockRotation.X_MASK) == BlockRotation.X_ROT_180){
				cosrx = -1; sinrx = 0;
				tmp = top; top = bottom; bottom = tmp; //swap
				tmp = front; front = back; back = tmp; //swap
			}else{
				cosrx = 0; sinrx = 1;			
				tmp = front;
				front = bottom;
				bottom = back;
				back = top;
				top = tmp;
			}
		}

		if((meta & BlockRotation.Y_MASK) != 0){
			if((meta & BlockRotation.Y_MASK) == BlockRotation.Y_ROT_270){
				cosry = 0; sinry = 1;
				tmp = left;
				left = back;
				back = right;
				right = front;
				front = tmp;				
				tmpr = ycbr_left; tmpg = ycbg_left; tmpb = ycbb_left;
				ycbr_left = ycbr_back; ycbg_left = ycbg_back; ycbb_left = ycbb_back;
				ycbr_back = ycbr_right; ycbg_back = ycbg_right; ycbb_back = ycbb_right;
				ycbr_right = ycbr_front; ycbg_right = ycbg_front; ycbb_right = ycbb_front;
				ycbr_front = tmpr; ycbg_front = tmpg; ycbb_front = tmpb;				
			}else if((meta & BlockRotation.Y_MASK) == BlockRotation.Y_ROT_180){
				cosry = -1; sinry = 0;
				tmp = left; left = right; right = tmp; //swap
				tmp = front; front = back; back = tmp; //swap
				tmpr = ycbr_left; tmpg = ycbg_left; tmpb = ycbb_left;
				ycbr_left = ycbr_right; ycbg_left = ycbg_right; ycbb_left = ycbb_right;
				ycbr_right = tmpr; ycbg_right = tmpg; ycbb_right = tmpb;
				tmpr = ycbr_front; tmpg = ycbg_front; tmpb = ycbb_front;
				ycbr_front = ycbr_back; ycbg_front = ycbg_back; ycbb_front = ycbb_back;
				ycbr_back = tmpr; ycbg_back = tmpg; ycbb_back = tmpb;
			}else{
				cosry = 0; sinry = -1;
				tmp = left;
				left = front;
				front = right;
				right = back;
				back = tmp;
				tmpr = ycbr_left; tmpg = ycbg_left; tmpb = ycbb_left;
				ycbr_left = ycbr_front; ycbg_left = ycbg_front; ycbb_left = ycbb_front;
				ycbr_front = ycbr_right; ycbg_front = ycbg_right; ycbb_front = ycbb_right;
				ycbr_right = ycbr_back; ycbg_right = ycbg_back; ycbb_right = ycbb_back;
				ycbr_back = tmpr; ycbg_back = tmpg; ycbb_back = tmpb;	
			}
		}
		
		if((meta & BlockRotation.Z_MASK) != 0){
			if((meta & BlockRotation.Z_MASK) == BlockRotation.Z_ROT_270){
				cosrz = 0; sinrz = -1;
				tmp = left;
				left = top;
				top = right;
				right = bottom;
				bottom = tmp;
			}else if((meta & BlockRotation.Z_MASK) == BlockRotation.Z_ROT_180){
				cosrz = -1; sinrz = 0;
				tmp = left; left = right; right = tmp; //swap
				tmp = top; top = bottom; bottom = tmp; //swap
			}else{
				cosrz = 0; sinrz = 1;
				tmp = left;
				left = bottom;
				bottom = right;
				right = top;
				top = tmp;
			}
		}

		
		//top & bottom
		if((sides & top) != 0){
			st = findVBOtextureforblockside(0, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, brw, brw, -brw, st.xoffsetmax, st.yoffsetmin, cbr_top, cbg_top, cbb_top, xo, yo, zo);		
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, -brw, brw, -brw, st.xoffsetmin, st.yoffsetmin, cbr_top, cbg_top, cbb_top, xo, yo, zo);			
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, -brw, brw, brw, st.xoffsetmin, st.yoffsetmax, cbr_top, cbg_top, cbb_top, xo, yo, zo);	
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, brw, brw, brw, st.xoffsetmax, st.yoffsetmax, cbr_top, cbg_top, cbb_top, xo, yo, zo);	
			}
		}
		if((sides & bottom) != 0){
			st = findVBOtextureforblockside(5, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, brw, -brw, brw, st.xoffsetmax, st.yoffsetmin, cbr_bottom, cbg_bottom, cbb_bottom, xo, yo, zo);				
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, -brw, -brw, brw, st.xoffsetmin, st.yoffsetmin, cbr_bottom, cbg_bottom, cbb_bottom, xo, yo, zo);	 			
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, -brw, -brw, -brw, st.xoffsetmin, st.yoffsetmax, cbr_bottom, cbg_bottom, cbb_bottom, xo, yo, zo);	 			
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, brw, -brw, -brw, st.xoffsetmax, st.yoffsetmax, cbr_bottom, cbg_bottom, cbb_bottom, xo, yo, zo);	
			}
		}
		//front and back
		if((sides & front) != 0){
			st = findVBOtextureforblockside(1, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, brw, brw, brw, st.xoffsetmax, st.yoffsetmin, ycbr_front, ycbg_front, ycbb_front, xo, yo, zo);	
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, -brw, brw, brw, st.xoffsetmin, st.yoffsetmin, ycbr_front, ycbg_front, ycbb_front, xo, yo, zo);		
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, -brw, -brw, brw, st.xoffsetmin, st.yoffsetmax, ycbr_front, ycbg_front, ycbb_front, xo, yo, zo); 		
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, brw, -brw, brw, st.xoffsetmax, st.yoffsetmax, ycbr_front, ycbg_front, ycbb_front, xo, yo, zo);
			}
		}
		if((sides & back) != 0){
			st = findVBOtextureforblockside(2, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, -brw, brw, -brw, st.xoffsetmax, st.yoffsetmin, ycbr_back, ycbg_back, ycbb_back, xo, yo, zo); 
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, brw, brw, -brw, st.xoffsetmin, st.yoffsetmin, ycbr_back, ycbg_back, ycbb_back, xo, yo, zo); 
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, brw, -brw, -brw, st.xoffsetmin, st.yoffsetmax, ycbr_back, ycbg_back, ycbb_back, xo, yo, zo);
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, -brw, -brw, -brw, st.xoffsetmax, st.yoffsetmax, ycbr_back, ycbg_back, ycbb_back, xo, yo, zo); 
			}
		}
		//left and right
		if((sides & left) != 0){
			st = findVBOtextureforblockside(3, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, -brw, brw, brw, st.xoffsetmax, st.yoffsetmin, ycbr_left, ycbg_left, ycbb_left, xo, yo, zo);
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, -brw, brw, -brw, st.xoffsetmin, st.yoffsetmin, ycbr_left, ycbg_left, ycbb_left, xo, yo, zo); 
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, -brw, -brw, -brw, st.xoffsetmin, st.yoffsetmax, ycbr_left, ycbg_left, ycbb_left, xo, yo, zo); 
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, -brw, -brw, brw, st.xoffsetmax, st.yoffsetmax, ycbr_left, ycbg_left, ycbb_left, xo, yo, zo);
			}
		}		
		if((sides & right) != 0){
			st = findVBOtextureforblockside(4, bid);
			v = findOrMakeVBOForTexture(chunkvbos, st, tr);
			if(v != null){
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, brw, brw, -brw, st.xoffsetmax, st.yoffsetmin, ycbr_right, ycbg_right, ycbb_right, xo, yo, zo);
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, brw, brw, brw, st.xoffsetmin, st.yoffsetmin, ycbr_right, ycbg_right, ycbb_right, xo, yo, zo); 
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, brw, -brw, brw, st.xoffsetmin, st.yoffsetmax, ycbr_right, ycbg_right, ycbb_right, xo, yo, zo);
				rotateIntoVBOXYZ(v, cosrx, sinrx, cosry, sinry, cosrz, sinrz, brw, -brw, -brw, st.xoffsetmax, st.yoffsetmax, ycbr_right, ycbg_right, ycbb_right, xo, yo, zo); 
			}
		}
		
	}
	
	public static VBOBuffer findOrMakeVBOForTexture(long cvs[], StitchedTexture st, boolean translucent){
		
		//cheap and dirty quick-check!
		if(st == st_last && v_last != null && v_last.isTranslucent == translucent)return v_last;
		
		VBOBuffer v = null;
		int i;
		
		WorldRenderer.VBOlistlock.lock();
		for(i=0;i<20;i++){
			if(cvs[i] <= 0){ //not found. Make a new one.
				break;
			}
			//get it from the map
			v = WorldRenderer.VBOmap.get(cvs[i]);
			if(v != null){
				if(v.textureindex == st.texturesindex && translucent == v.isTranslucent){
					v_last = v;
					st_last = st;
					WorldRenderer.VBOlistlock.unlock();
					return v; //FOUND!
				}
			//}else{
			//	System.out.printf("existing vbo lookup fail\n");
			}
		}
		//if(i >= 20){
		//	WorldRenderer.VBOlistlock.unlock();
		//	System.out.printf("no chunkvbos space left for chunk... problem?\n");
		//	return null; //FULL?
		//}
		//Not found. Make a new VBO.
		v = new VBOBuffer();
		v.textureindex = st.texturesindex;
		v.VBOid = WorldRenderer.getNextVBOid();
		v.isTranslucent = translucent;
		WorldRenderer.VBOmap.put(v.VBOid, v); //add it to the map!
		WorldRenderer.vbocount++;
		cvs[i] = v.VBOid; //add it to the chunk
		v_last = v;
		st_last = st;
		WorldRenderer.VBOlistlock.unlock();
		return v;
	}
	
	public static StitchedTexture findVBOtextureforblockside(int side, int bid){		
		StitchedTexture st = Blocks.BlockArray[bid].getStitchedTexture(side);
		if(st.texturesindex < 0){
			//st = Blocks.BlockArray[1].getStitchedTexture(0); //default to stone!
			//THIS SHOULD ONLY HAPPEN FROM COLORING BLOCK!!!
			DangerZone.wr.oneblock = bid;
			while(DangerZone.wr.oneblock >= 0){
				Thread.yield();
			}
		}
		return st;
	}
	
	private void setVBOBrightness(float m, float redmul, float greenmul, float bluemul){
		float flr, flg, flb;
		flr = brightness_red + m;
		flr *= redmul;
		if(flr < 0)flr = 0;
		if(flr > 1)flr = 1;
		flg = brightness_green + m;
		flg *= greenmul;
		if(flg < 0)flg = 0;
		if(flg > 1)flg = 1;
		flb = brightness_blue + m;
		flb *= bluemul;
		if(flb < 0)flb = 0;
		if(flb > 1)flb = 1;

		cbr = flr;
		cbg = flg;
		cbb = flb;
		
		cbr_top = cbr * sunpostop;
		cbr_bottom = cbr * sunposbottom;
		cbr_left = cbr * sunposleft;
		cbr_right = cbr * sunposright;
		cbr_front = cbr * sunposfront;
		cbr_back = cbr * sunposback;
		
		cbg_top = cbg * sunpostop;
		cbg_bottom = cbg * sunposbottom;
		cbg_left = cbg * sunposleft;
		cbg_right = cbg * sunposright;
		cbg_front = cbg * sunposfront;
		cbg_back = cbg * sunposback;
		
		cbb_top = cbb * sunpostop;
		cbb_bottom = cbb * sunposbottom;
		cbb_left = cbb * sunposleft;
		cbb_right = cbb * sunposright;
		cbb_front = cbb * sunposfront;
		cbb_back = cbb * sunposback;
		
	}
	
	private void recalcBrightness(int d, int yp){			
		float f = day_light;
		if(yp < 50 && fade_light_level){
			if(yp < 0)yp = 0;
			float f2 = (float)yp/(50.0f + (50f-yp));
			f *= f2;
		}		
		if(f < 0)f = 0;
		if(f > 1)f = 1;
		brightness_red = brightness_green = brightness_blue = f;
	}
	
	private float recalcDayLight(){
		float tod = DangerZone.world.timetimer % DangerZone.world.lengthOfDay;
		float fsin = (float) Math.sin(Math.toRadians((tod/(float)DangerZone.world.lengthOfDay)*360f));
		fsin *= 1.75f;
		if(fsin > 1)fsin = 1;
		if(fsin < -1)fsin = -1;
		return 0.50f + 0.45f*fsin;
	}
    
    //rotate the point around its 0 along with its texture position
    //Z first
    private void rotateIntoVBOXYZ(VBOBuffer v, float cosrx, float sinrx, float cosry, float sinry, float cosrz, float sinrz, 
    		float x, float y, float z, float txx, float txy, float r, float g, float b, int xo, int yo, int zo){
    		rotateIntoVBOXY(v, cosrx, sinrx, cosry, sinry, x*cosrz - y*sinrz, y*cosrz + x*sinrz, z, txx, txy, r, g, b, xo, yo, zo);	
    }
    
    //now Y
    private void rotateIntoVBOXY(VBOBuffer v, float cosrx, float sinrx, float cosry, float sinry, 
    		float x, float y, float z, float txx, float txy, float r, float g, float b, int xo, int yo, int zo){
    		rotateIntoVBOXX(v, cosrx, sinrx, x*cosry - z*sinry, y, z*cosry + x*sinry, txx, txy, r, g, b, xo, yo, zo);	
    }
    
    //now X
    private void rotateIntoVBOXX(VBOBuffer v, float cosrx, float sinrx,
    		float x, float y, float z, float txx, float txy, float r, float g, float b, int xo, int yo, int zo){
    		stuffIntoVBO(v, x, y*cosrx - z*sinrx, z*cosrx + y*sinrx , txx, txy, r, g, b, xo, yo, zo);	
    }
    
    //lastly, offsets...
    private void stuffIntoVBO(VBOBuffer v, float x, float y, float z, float txx, float txy, float r, float g, float b, int xo, int yo, int zo){
    	v.addVertexInfoToVBO(x + xo , y + yo, z + zo, txx, txy, r, g, b);
    }
    
	
}