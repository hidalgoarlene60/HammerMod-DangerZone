package dangerzone;

import dangerzone.blocks.Blocks;

import dangerzone.items.Item;
import dangerzone.items.Items;

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

//inherit from this class... "extends BaseMod"
public class BaseMod { 
		
	
	public BaseMod(){
		//initialize your mod if need be...
	}
	
	/*
	 * Replace with the name of your mod.
	 */
	public String getModName()
	{
		return "";
	}
	
	
	/*
	 * RETURN THE VERSION OF DANGERZONE THIS MOD IS BUILT WITH!!!!
	 * THIS IS SO WE CAN CHECK COMPATIBILITY AND NOT FRUSTRATE AND CRASH THE PLAYER!!!
	 * SEE DANGERZONE.VERSIONSTRING
	 * 
	 * Return your own copy of DangerZone.versionstring
	 */
	public String versionBuiltWith()
	{
		return "";
	}
	
	/*
	 * It is IMPORTANT TO NOTE that itemIDs and blockIDs (and most others) are INDETERMINATE during loading.
	 * In other words, they can change!!!! Do not use itemIDs or blockIDs in the registerThings() routine!!!!
	 * (or the constructors for blocks and items!)
	 * 
	 * This routine is for REGISTRATION and comes before anything else.
	 * 99.9% of your mod loading should go here.
	 */
	public void registerThings(){		
		//We need some content in the world!		
	}
	
	
	/*
	 * This is the second stage of mod initialization.
	 * Everything from all active mods has been registered.
	 * ItemIDs, blockIDs and dimensionIDs are now stable. Maybe. Not necessarily for real server connections...
	 * Go ahead and finish whatever initialization, if any, you need to do here.
	 * 
	 * From world to world, your ItemIDs and blockIDs may change.
	 * But for any one world, your Item and Block IDs are fixed in stone.
	 * 
	 * 
	 */
	public void postLoadProcessing(){
		//String world_directory_where_i_should_save_files = DangerZone.getWorldDirectoryPath();
	}
	
	/*
	 * Some utility routines to save typing and make life easier
	 */
	
	public void craftChest(Object b){
		Crafting.registerCraftingRecipe(b, b, b, b, null, b, b, b, b, Blocks.chest, 1, true);
	}
	
	public void craftSword(Object b, Item i){
		//three possible positions
		Crafting.registerCraftingRecipe(b, null, null, b, null, null, Items.stick, null, null, i, 1, true);
		Crafting.registerCraftingRecipe(null, b, null, null, b, null, null, Items.stick, null, i, 1, true);
		Crafting.registerCraftingRecipe(null, null, b, null, null, b, null, null, Items.stick, i, 1, true);
	}
	
	public void craftAxe(Object b, Item i){
		//two possible positions
		Crafting.registerCraftingRecipe(b, b, null, b, Items.stick, null, null, Items.stick, null, i, 1, true);
		Crafting.registerCraftingRecipe(null, b, b, null, Items.stick, b, null, Items.stick, null, i, 1, true);
	}
	
	public void craftHoe(Object b, Item i){
		Crafting.registerCraftingRecipe(b, b, null, null, Items.stick, null, null, Items.stick, null, i, 1, true);
		Crafting.registerCraftingRecipe(null, b, b, null, Items.stick, null, null, Items.stick, null, i, 1, true);
	}
	
	public void craftPickaxe(Object b, Item i){
		Crafting.registerCraftingRecipe(b, b, b, null, Items.stick, null, null, Items.stick, null, i, 1, true);
	}
	
	public void craftShovel(Object b, Item i){
		Crafting.registerCraftingRecipe(b, null, null, Items.stick, null, null, Items.stick, null, null, i, 1, true);
		Crafting.registerCraftingRecipe(null, b, null, null, Items.stick, null, null, Items.stick, null, i, 1, true);
		Crafting.registerCraftingRecipe(null, null, b, null, null, Items.stick, null, null, Items.stick, i, 1, true);
	}

}
