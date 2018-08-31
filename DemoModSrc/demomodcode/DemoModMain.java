package demomodcode;

import dangerzone.BaseMod;
import dangerzone.Crafting;
import dangerzone.CreatureTypes;
import dangerzone.Dimension;
import dangerzone.Dimensions;
import dangerzone.Ores;
import dangerzone.Spawnlist;
import dangerzone.WorldDecorators;
import dangerzone.biomes.BiomeManager;
import dangerzone.biomes.RuggedBiome;
import dangerzone.biomes.RuggedHillsBiome;
import dangerzone.blocks.Block;
import dangerzone.blocks.BlockFlower;
import dangerzone.blocks.BlockSpawner;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entities;
import dangerzone.items.Item;
import dangerzone.items.ItemSpawnEgg;
import dangerzone.items.ItemTrophy;
import dangerzone.items.Items;


public class DemoModMain extends BaseMod {
	
	//See DangerZoneBase, Items, and Blocks. They have examples of everything...
	//This is just here to help you get everything set up.
	//This is it. This is where you start your mod!
	
	public static Block mydemoblock = new DemoBlock("DemoMod:A Demo Block", "demores/blocks/blockdemo.png", 100);
	public static Item trophydemobeast = new ItemTrophy("DemoMod:Demo Beast Trophy", "DemoMod:DemoBeast", 0.45f, 16);
	public static Item eggdemobeast = new ItemSpawnEgg("DemoMod:Spawn Demo Beast", "demores/items/eggbeast.png", "DemoMod:DemoBeast");
	public static Block demobeastspawner = new BlockSpawner("DemoMod:Demo Beast Spawner", "DemoMod:DemoBeast", 0.4f, 800, 3, -0.55f, 0.55f);
	public static Item bigstick = new BigStick("DemoMod:Big Stick", "demores/items/bigstick.png", 250, 16);
	public static Item beanie = new Beanie("DemoMod:Beanie", "demores/items/beanie.png", 0.25f, 300, 0);
	public static Block prettyflower = new BlockFlower("DemoMod:Pretty Flower", "demores/blocks/pretty_flower.png");
	
	public static Dimension demomoddimension = new Dimension("DemoMod: Demo Dimension");
	public static BiomeManager demo_biomemanager;
	public static RuggedBiome demomountains;
	public static RuggedBiome demohills;
	public static RuggedBiome demoplains;

	
	public static DemoWorldDecorator myworld = null;
	
	//NAME MUST END IN "Main" so loader can find it...
	//NAME MUST END IN "Main" so loader can find it...
	//NAME MUST END IN "Main" so loader can find it...
	public DemoModMain (){
		super();
	}
	
	public String getModName()
	{
		return "Demo Mod Version 1.2";
	}
	
	//Copy-paste the same string from Dangerzone.versionstring at the top of the DangerZone.java file.
	//DO NOT just return DangerZone.versionstring. That defeats the purpose, which is a compatibility check so as to not frustrate the player!
	//Make sure you update this string with each new version you update to.
	public String versionBuiltWith()
	{
		return "1.7";
	}
	
	//This is where all your registration happens.
	public void registerThings(){
		//System.out.printf("Demo mod printf! registerThings()\n");
		Blocks.registerBlock(prettyflower);
		Blocks.registerBlock(demobeastspawner);
		Blocks.registerBlock(mydemoblock);
		Ores.registerOre(mydemoblock, Blocks.stone, Dimensions.overworlddimension, null, 20, 50, 10, 16, 2);
		
		Items.registerItem(trophydemobeast);
		Items.registerItem(eggdemobeast);
		Items.registerItem(bigstick);
		Items.registerItem(beanie);
		
		Entities.registerEntity(DemoBeast.class, "DemoMod:DemoBeast", new ModelDemoBeast());
		
		Spawnlist.registerSpawn(new DemoBeast(null), Dimensions.overworlddimension, null, 60, 110, 50, 5, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		
		Crafting.registerCraftingRecipe(
				Blocks.plywood, Items.stick, null, 
				Blocks.plywood, Items.stick, null, 
				null, Items.stick, null, 
				bigstick, 1, true);
		
		myworld = new DemoWorldDecorator();
		WorldDecorators.registerWorldDecorator(myworld);
		
		Dimensions.registerDimension(demomoddimension);
		demo_biomemanager = new BiomeManager();
		demomountains = new RuggedBiome("DemoMod:Mountains");
		demohills = new RuggedHillsBiome("DemoMod:Hills");
		demoplains = new DemoPlainsBiome("DemoMod:Plains");
		demoplains.mul_green = 1.1f;

		demomoddimension.registerBiomeManager(demo_biomemanager);
		demo_biomemanager.registerBiome(demomountains);
		demo_biomemanager.registerBiome(demohills);
		demo_biomemanager.registerBiome(demoplains);
		
		
		Spawnlist.registerSpawn(new DemoBeast(null), demomoddimension, demoplains, 60, 110, 50, 5, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		
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
