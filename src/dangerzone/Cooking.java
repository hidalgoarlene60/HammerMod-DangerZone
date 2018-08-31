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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dangerzone.blocks.Block;
import dangerzone.items.Item;



public class Cooking {
	
	public static  List<CookingRecipe> recipies;
	
	public Cooking(){
		recipies = new ArrayList<CookingRecipe>();
	}
	
	/*
	 * Yes, all recipe registration is done by UNIQUENAME, because itemIDs and blockIDs are indeterminate at registration time!
	 * In other words, itemIDs and blockIDs don't have a stable value until AFTER all registration is complete.
	 * 
	 * "ordered" specifies whether items can be in random places or not.
	 * 
	 */
	public static void registerCookingRecipe( Object i1, Object i2, int cooktime){
		
		CookingRecipe r = new CookingRecipe();
		if(cooktime <= 0 || cooktime > 64000)return;
		if(i1 == null)return;
		if(i2 == null)return;
		
		if(i1 instanceof Item)r.inname = ((Item) i1).uniquename;
		if(i1 instanceof Block)r.inname = ((Block) i1).uniquename;
		
		if(i2 instanceof Item)r.outname = ((Item) i2).uniquename;
		if(i2 instanceof Block)r.outname = ((Block) i2).uniquename;
		
		r.cooktime = cooktime;
		
		//add recipe to list!
		recipies.add(r);		
		
	}
	
	//should some day change to a hashmap...
	public static CookingRecipe find(String i1){
		if(i1 == null)return null;
		Iterator<CookingRecipe> ii = recipies.iterator();
		CookingRecipe r;
		while(ii.hasNext()){
			r = ii.next();
			if(i1.equals(r.inname))return r;			
		}		
		return null;
	}

}
