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



public class Shredding {
	
	public static  List<Shred> recipies;
	
	public Shredding(){
		recipies = new ArrayList<Shred>();
	}
	
	/*
	 * Yes, all recipe registration is done by UNIQUENAME, because itemIDs and blockIDs are indeterminate at registration time!
	 * In other words, itemIDs and blockIDs don't have a stable value until AFTER all registration is complete.
	 * 
	 * "ordered" specifies whether items can be in random places or not.
	 * 
	 */
	public static void registerShredding( Object inthing, Object outthing, int count){
		
		Shred r = new Shred();
		if(count <= 0 || count > 64)return;
		if(outthing == null)return;
		
		if(inthing instanceof Item)r.in = ((Item) inthing).uniquename;
		if(inthing instanceof Block)r.in = ((Block) inthing).uniquename;
		
		if(outthing instanceof Item)r.out = ((Item) outthing).uniquename;
		if(outthing instanceof Block)r.out = ((Block) outthing).uniquename;
		
		r.out_count = count;
		
		//add recipe to list!
		recipies.add(r);		
		
	}
	
	public static boolean mystrcmp(String a, String b){
		if(a == null && b == null)return true;
		if(a == null && b != null)return false;
		if(a != null && b == null)return false;
		if(a.equals(b))return true;
		return false;
	}
	
	public static Shred find(String i1){		
		Iterator<Shred> ii = recipies.iterator();
		Shred r;
		while(ii.hasNext()){
			r = ii.next();
			if(!mystrcmp(i1, r.in))continue;
			//match!
			return r;
		}		
		return null;
	}
	

}
