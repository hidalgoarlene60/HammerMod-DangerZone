package com.jtrent238.hammermod;

import dangerzone.Crafting;
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;

public class Recipes {

	
	/**
	 * Register Recipes with Game Registry.
	 */
	public static void registerRecpies(){
		addShaplessRecpies();
		addShapedRecpies();
		addsmeltigrecipies();
		addOreRecipes();
	}

	/**
	 * Add Shaped Recipes.
	 */
	private static void addShapedRecpies(){
		Crafting.registerCraftingRecipe(Blocks.plywood, Blocks.plywood, Blocks.plywood, Blocks.plywood, Items.stick, Blocks.plywood, null, Items.stick, null, ItemLoader.ItemWoodHammer, 1, true);
		Crafting.registerCraftingRecipe(Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, Items.stick, Blocks.stone, null, Items.stick, null, ItemLoader.ItemStoneHammer, 1, true);
		Crafting.registerCraftingRecipe(Items.diamond, Items.diamond, Items.diamond, Items.diamond, Items.stick, Items.diamond, null, Items.stick, null, ItemLoader.ItemDiamondHammer, 1, true);
		Crafting.registerCraftingRecipe(Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.dirt, null, Items.stick, null, ItemLoader.ItemDirtHammer, 1, true);
		Crafting.registerCraftingRecipe(Blocks.stone4, Blocks.stone4, Blocks.stone4, Blocks.stone4, Items.stick, Blocks.stone4, null, Items.stick, null, ItemLoader.ItemGlassHammer, 1, true);
		Crafting.registerCraftingRecipe(Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Items.stick, Blocks.sand, null, Items.stick, null, ItemLoader.ItemSandHammer, 1, true);
		Crafting.registerCraftingRecipe(Items.emerald, Items.emerald, Items.emerald, Items.emerald, Items.stick, Items.emerald, null, Items.stick, null, ItemLoader.ItemEmeraldHammer, 1, true);
		Crafting.registerCraftingRecipe(Blocks.grass, Blocks.grass, Blocks.grass, Blocks.grass, Items.stick, Blocks.grass, null, Items.stick, null, ItemLoader.ItemGrassHammer, 1, true);
		Crafting.registerCraftingRecipe(Blocks.grassblock, Blocks.grassblock, Blocks.grassblock, Blocks.grassblock, Items.stick, Blocks.grassblock, null, Items.stick, null, ItemLoader.ItemGrassHammer, 1, true);
		Crafting.registerCraftingRecipe(Items.charcoalstick, Items.charcoalstick, Items.charcoalstick, Items.charcoalstick, Items.stick, Items.charcoalstick, null, Items.stick, null, ItemLoader.ItemCharcoalHammer, 1, true);
		Crafting.registerCraftingRecipe(Items.moosebone, Items.moosebone, Items.moosebone, Items.moosebone, Items.stick, Items.moosebone, null, Items.stick, null, ItemLoader.ItemBoneHammer, 1, true);
		Crafting.registerCraftingRecipe(Items.apple, Items.apple, Items.apple, Items.apple, Items.stick, Items.apple, null, Items.stick, null, ItemLoader.ItemAppleHammer, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.stick, Items.stick, null, Items.stick, null, ItemLoader.ItemCopperHammer, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.stick, Items.lumpsilver, null, Items.stick, null, ItemLoader.ItemSilverHammer, 1, true);
		//Crafting.registerCraftingRecipe(Items.apple, Items.apple, Items.apple, Items.apple, Items.stick, Items.apple, null, Items.stick, null, ItemLoader.ItemRubyHammer, 1, true);
		Crafting.registerCraftingRecipe(Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, Items.stick, Items.lumptin, null, Items.stick, null, ItemLoader.ItemTinHammer, 1, true);
		//Crafting.registerCraftingRecipe(Items.apple, Items.apple, Items.apple, Items.apple, Items.stick, Items.apple, null, Items.stick, null, ItemLoader.ItemAmethystHammer, 1, true);
		//Crafting.registerCraftingRecipe(Items.apple, Items.apple, Items.apple, Items.apple, Items.stick, Items.apple, null, Items.stick, null, ItemLoader.ItemUraniumHammer, 1, true);
		//Crafting.registerCraftingRecipe(Items.apple, Items.apple, Items.apple, Items.apple, Items.stick, Items.apple, null, Items.stick, null, ItemLoader.ItemTitaniumHammer, 1, true);
		Crafting.registerCraftingRecipe(Items.bloodstone, Items.bloodstone, Items.bloodstone, Items.bloodstone, Items.stick, Items.bloodstone, null, Items.stick, null, ItemLoader.ItemBloodStoneHammer, 1, true);
		
		/*
		if(HammerMod.RAINBOWCRAFT == true){
			//int numHammers = 98;
		int numHammers = HammerMod.numHammers;
			for (int i = 0; i < numHammers ; i++) {
				
				GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.ItemRainbowHammer), "XXX", "XSX", "BSB", 'X', LootRegistry.hammers.get(i) , 'S' , Items.stick);
				
			}
			
		}*/
	}
	
	/**
	 * Add Shapeless Recipes.
	 */
	private static void addShaplessRecpies(){
		
		}
	/**
	 * Add Smelting Recipes
	 */
	private static void addsmeltigrecipies(){
		//GameRegistry.addSmelting(ItemLoader.ItemSandHammer, new ItemStack (ItemLoader.ItemGlassHammer, 1), 2F );
			   }
	
	public static void addOreRecipes()
    {

		//For my YouTuber mod
		//GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(ItemLoader.ItemxJSQHammer, true, new Object[]{"FFF", "FSF", "BSB", Character.valueOf('F'), "ingot_xJSQ", 'S', Items.stick}));
		//GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(ItemLoader.ItemDanTDMHammer, true, new Object[]{"FFF", "FSF", "BSB", Character.valueOf('F'), "ingot_DanTDM", 'S', Items.stick}));
		//GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(ItemLoader.ItemSkyTheKidRSHammer, true, new Object[]{"FFF", "FSF", "BSB", Character.valueOf('F'), "ingot_skythekidRS", 'S', Items.stick}));
		//GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(ItemLoader.Item_MrGregor_Hammer, true, new Object[]{"FFF", "FSF", "BSB", Character.valueOf('F'), "ingot__MrGregor_", 'S', Items.stick}));

		
    }
	
	
	}

	
	
	