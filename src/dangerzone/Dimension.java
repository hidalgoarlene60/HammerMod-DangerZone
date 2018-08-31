package dangerzone;
import dangerzone.biomes.BiomeManager;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;

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
public class Dimension {
	public int dimensionID;
	public String uniquename;
	public BiomeManager bm;
	public boolean fade_light_level = true;
	public boolean cloud_enable = true;
	public boolean rain_enable = true;
	public boolean lightning_enable = true;
	public float sky_red = 0.445f;    //default to blue sky!
	public float sky_green = 0.682f;  //default to blue sky!
	public float sky_blue = 1.0f;     //default to blue sky!
	public boolean special_hidden = false;		//regular dimension.
	
	
	public Dimension(){
		bm = null;
		uniquename = "Dimension 0";
	}
	
	public Dimension(String n){
		this();
		uniquename = n;
	}
	
	public Dimension(String n, float r, float g, float b){
		this();
		uniquename = n;
		sky_red = r;
		sky_green = g;
		sky_blue = b;
	}
	
	public void registerBiomeManager(BiomeManager b){
		bm = b;
	}
	
	public BiomeManager getBiomeManager(){
		return bm;
	}
	
	//Teleport the given entity to the given coords at the given dimension (if safe).
	//if the given coords are not safe, find new safe ones.
	//caveat: chunks may not be loaded yet and we have to wait for them to get real block data!
	public void teleportToDimension(Entity e, World w, int d, int x, int y, int z){
		double newx, newy, newz;
		int i, j, k, bid;
		int iter = 2;
		int tries = 100;

		newx = x;
		newy = y;
		newz = z;
		
		/*
		 * Client side
		 */
		if(!w.isServer){
			if(e instanceof Player){		
				DangerZone.server_connection.sendPlayerTeleport(d);
				DangerZone.messagestring = Dimensions.DimensionArray[d].uniquename;
				DangerZone.messagetimer = 120;
			}
			return;
		}
		
		/*
		 * Server side only...
		 */
		while(tries > 0){
			tries--;
			//make sure entity fits in the space given
			int iw = (int) (e.getWidth()/2 + 0.5f);
			int ih = (int) (e.getHeight() + 1);
			boolean fits = true;
			for(i=-iw;i<=iw && fits;i++){
				for(k=-iw;k<=iw && fits;k++){
					for(j=0;j<=ih && fits;j++){
						bid = w.getblock(d, (int)newx+i, (int)newy+j, (int)newz+k);
						if(Blocks.isSolid(bid)){
							fits = false;
						}
					}
				}
			}
			if(fits){
				bid = w.getblock( d, (int)newx, (int)(newy-0.25f), (int)newz);
				if(Blocks.isSolid(bid)){
					//fits in the space and there is a solid block underneath
					break; //Looks good to me!
				}
			}
			
			//poke around for another spot!
			boolean findsolid = true;
			int spottries = 0;
			while(findsolid){
				iter++;
				spottries++;
				if(spottries > 2000){ //give up. Random place...
					newx = x + (w.rand.nextInt(iter)-w.rand.nextInt(iter));
					newz = z + (w.rand.nextInt(iter)-w.rand.nextInt(iter));
					newy = y;
					break;
				}
				if(iter > 64)iter = 2;
				newx = x + (w.rand.nextInt(iter)-w.rand.nextInt(iter));
				newz = z + (w.rand.nextInt(iter)-w.rand.nextInt(iter));
				for(i=240;i>1;i--){
					bid = w.getblock( d, (int)newx, i, (int)newz);
					if(Blocks.isSolid(bid)){
						newy = (float)i+1.001f;
						findsolid = false;
						break; //Try here!
					}
					if(Blocks.isLiquid(bid)){
						if(spottries > 1000){ //Fine, we'll take water...
							newy = (float)i+1.001f;
							findsolid = false;
							break; //Try here!
						}
						break; //try another spot
					}
				}
			}						
		}
		
		//Let's go!
		e.dimension = d; //set new dimension!
		e.posx = newx + 0.5f + (w.rand.nextFloat()-w.rand.nextFloat())*0.1f;
		e.posy = newy + 0.1f;
		e.posz = newz + 0.5f + (w.rand.nextFloat()-w.rand.nextFloat())*0.1f;
		
		//System.out.printf("tpd %s to %d, %f, %f, %f\n", e.uniquename, e.dimension, e.posx, e.posy, e.posz);
		
	}
	
	public boolean canSpawnHere(World w, int d, int x, int z, SpawnlistEntry st){
		//if(st.whatToSpawn.uniquename.contains("OreSpawn"))return false; //example: disable orespawn creatures in this dimension!
		return true;
	}
	
	public void tickChunk(Player p, World w, Chunk c){

	}
	

}
