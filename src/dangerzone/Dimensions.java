package dangerzone;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import dangerzone.biomes.BiomeManager;



public class Dimensions {
	
	public static Dimension overworlddimension = new Dimension("DangerZone: Overworld Dimension");
	public static Dimension bigroundtreedimension = new Dimension("DangerZone: Big Round Tree Dimension", 0.445f, 0.345f, 0.90f);
	public static Dimension pathwaydimension = new Dimension("DangerZone: Pathway Dimension", 0.345f, 0.445f, 0.95f);
	public static Dimension ruggedhillsdimension = new Dimension("DangerZone: Rugged Hills Dimension");
	public static Dimension windsweptdimension = new Dimension("DangerZone: Wind-Swept Dimension", 0.445f, 0.645f, 0.75f);
	public static Dimension skyislandsdimension = new Dimension("DangerZone: Sky Islands Dimension", 1.0f, 0.444f, 0.444f); //red sky!
	public static Dimension pleasantvilledimension = new Dimension("DangerZone: Pleasantville Dimension");
	//public static Dimension biometestdimension = new Dimension("DangerZone: Biome Test Dimension");
	
	
	public static Dimension DimensionArray[];
	public static final int dimensionsMAX = 256;
	public static Properties prop = null;
		
	public Dimensions(){
		int i;
		DimensionArray = new Dimension[dimensionsMAX];
		for(i=0;i<dimensionsMAX;i++){
			DimensionArray[i] = null;
		}
	}
	
	public static int registerDimension(Dimension b){
		int i = 0;
		
		for(i=1;i<dimensionsMAX;i++){
			if(DimensionArray[i] != null){
				if(DimensionArray[i].uniquename.equals(b.uniquename)){ //duplicate!!!
					return 0; //duplicate!!!
				}
			}
		}
		
		for(i=1;i<dimensionsMAX;i++){
			if(DimensionArray[i] == null)break;
		}
		if(i >= dimensionsMAX-1)return 0;
		
		if(prop != null)i = Utils.getPropertyInt(prop, b.uniquename, 1, dimensionsMAX-1, i);
		
		//existing or new dimension
		if(DimensionArray[i] == null){ //Slot is open, this is good. Give it the same ID it had last time!
			DimensionArray[i] = b;
			b.dimensionID = i;
			if(prop != null)prop.setProperty(b.uniquename, String.format("%d", i)); //next time we will find it!
			return i;
		}else{ 
			//Oops. Slot already taken.
			//Give this slot to the existing one, and bump the intruder to a new slot.
			//Should only happen when adding new mods and they get loaded before old ones.
			Dimension intruder = DimensionArray[i];
			int isave = i;
			DimensionArray[i] = b;
			b.dimensionID = i;
			for(i=1;i<dimensionsMAX;i++){
				if(DimensionArray[i] == null){
					DimensionArray[i] = intruder;
					intruder.dimensionID = i;
					if(prop != null)prop.setProperty(intruder.uniquename, String.format("%d", i)); //next time we will find it!
					return isave;
				}
			}			
		}
		return 0;
	}
	
	public static void reRegisterDimensionAt(String s, int loc){
		int i;
		
		if(loc <= 0 || loc >= dimensionsMAX)return;
		if(s == null || s.equals(""))return;
		
		if(DimensionArray[loc] != null){
			if(s.equals(DimensionArray[loc].uniquename)){
				return; //already where its supposed to be! :)
			}
			//maybe it is somewhere else?
			for(i=1;i<dimensionsMAX;i++){
				if(DimensionArray[i] != null){
					if(s.equals(DimensionArray[i].uniquename)){
						//Take it, and move the intruder where I was
						Dimension me = DimensionArray[i];
						Dimension intruder = DimensionArray[loc];
						DimensionArray[loc] = me;
						DimensionArray[loc].dimensionID = loc;
						DimensionArray[i] = intruder;
						DimensionArray[i].dimensionID = i;
						return;
					}
				}
			}
		}else{
			//maybe it is somewhere else?
			for(i=1;i<dimensionsMAX;i++){
				if(DimensionArray[i] != null){
					if(s.equals(DimensionArray[i].uniquename)){
						//Move it!
						DimensionArray[loc] = DimensionArray[i];
						DimensionArray[loc].dimensionID = loc;
						DimensionArray[i] = null;
						return;
					}
				}
			}
		}
	}
	
	public static void load(){
		InputStream input = null;
		prop = new Properties();
		String filepath = new String();		
		filepath = String.format("worlds/%s/dimensionIDs.dat", DangerZone.worldname);	
		 
		try {	 
			input = new FileInputStream(filepath);
	 
			// load a properties file
			prop.load(input);

		} catch (IOException ex) {
			//ex.printStackTrace();
			input = null;
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static void save(){

		OutputStream output = null;
		String filepath = new String();		
		filepath = String.format("worlds/%s/dimensionIDs.dat", DangerZone.worldname);	
		File f = new File(filepath);		
		f.getParentFile().mkdirs();	
		
		try {	 
			output = new FileOutputStream(filepath);	 

			// save properties
			prop.store(output, null);
	 
		} catch (IOException io) {
			io.printStackTrace();
		}
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static BiomeManager getBiomeManager(int dim){
		if(dim <= 0)return null;
		if(dim >= dimensionsMAX)return null;
		if(Dimensions.DimensionArray[dim]==null)return null;
		return Dimensions.DimensionArray[dim].getBiomeManager();
	}
	
	public static String getName(int dim){
		if(dim <= 0)return null;
		if(dim >= dimensionsMAX)return null;
		if(Dimensions.DimensionArray[dim]==null)return null;
		return Dimensions.DimensionArray[dim].uniquename;
	}
	
	public static void tickChunk(Player p, World w, int d, Chunk c){
		if(d <= 0)return;
		if(d >= dimensionsMAX)return;
		if(Dimensions.DimensionArray[d]==null)return;
		Dimensions.DimensionArray[d].tickChunk(p, w, c);
	}

}
