package com.jtrent238.hammermod;

import dangerzone.BaseMod;


public class HammerModMain extends BaseMod {
	
	public static String MODID = "hammermod";
	public static String MODNAME = "jtrent238's HammerMod for DangerZone";
	public static String MODAUTHOR = "jtrent238";
	public static String MODVERSION = "1.0.0.0";
	public static String DANGERZONEVERSION = "1.7";
	public static int numHammers;
	
	public HammerModMain (){
		super();
	}
	
	public String getModName()
	{
		return MODNAME;
	}
	
	//Copy-paste the same string from Dangerzone.versionstring at the top of the DangerZone.java file.
	//DO NOT just return DangerZone.versionstring. That defeats the purpose, which is a compatibility check so as to not frustrate the player!
	//Make sure you update this string with each new version you update to.
	public String versionBuiltWith()
	{
		return DANGERZONEVERSION;
	}
	
	//This is where all your registration happens.
	public void registerThings(){
		//System.out.printf("Demo mod printf! registerThings()\n");

		ItemLoader.LoadItems();
		Recipes.registerRecpies();
		

/*
		Crafting.registerCraftingRecipe(
				Blocks.plywood, Items.stick, null, 
				Blocks.plywood, Items.stick, null, 
				null, Items.stick, null, 
				ItemWoodHammer, 1, true);
*/
	}
	
	//You probably won't need this routine unless you care which OTHER mods are installed.
	//This is called AFTER all mods are loaded and registered.
	//It needs to exist, just leave it empty.
	public void postLoadProcessing(){
		//System.out.printf("Demo mod printf! postLoadProcessing()\n");
		//System.out.printf("Block 1 name is: %s\n", Blocks.BlockArray[1].uniquename);
		//System.out.printf("Renderdistance is: %d\n", DangerZone.renderdistance);
	}

}
