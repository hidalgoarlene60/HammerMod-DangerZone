package dangerzone.gui;
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


import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import dangerzone.DangerZone;
import dangerzone.entities.Entity;


public class PlayerEntityGUI extends GuiInterface {

	int updatecounter = 0;
	Map<String, Integer> entitycounts = new HashMap<String, Integer>();
	int total = 0;
	int unique = 0;
	
	Map<String, Integer> s_entitycounts = new HashMap<String, Integer>();
	int s_total = 0;
	int s_unique = 0;
	
	public PlayerEntityGUI(){
		super();
	}
	
	/*
	 * Show active entities, client and server....
	 */
	public void process(){

		
		updatecounter++;
		if(updatecounter > DangerZone.wr.fps){
			updatecounter = 0;

			entitycounts.clear();
			total = 0;
			unique = 0;
			
			s_entitycounts.clear();
			s_total = 0;
			s_unique = 0;
			
			Integer Icurcount;
			Entity e;
			for(int i=0;i < DangerZone.max_entities; i++){
				e = DangerZone.entityManager.entities[i];
				if(e != null){
					Icurcount = entitycounts.get(e.uniquename);
					if(Icurcount == null){
						entitycounts.put(e.uniquename, 1);
						unique++;
						total++;
					}else{
						entitycounts.replace(e.uniquename, Icurcount.intValue()+1);
						total++;
					}
				}
			}
			
			if(DangerZone.server != null){
				for(int i=0;i < DangerZone.max_entities; i++){
					e = DangerZone.server.entityManager.entities[i];
					if(e != null){
						Icurcount = s_entitycounts.get(e.uniquename);
						if(Icurcount == null){
							s_entitycounts.put(e.uniquename, 1);
							s_unique++;
							s_total++;
						}else{
							s_entitycounts.replace(e.uniquename, Icurcount.intValue()+1);
							s_total++;
						}
					}
				}	
			}
		}
		
		int i = 80;
		for(Map.Entry<String, Integer> entry: entitycounts.entrySet()){
			textAt( 50, DangerZone.screen_height - i, String.format("CLIENT: %d  %s", entry.getValue().intValue(), entry.getKey()));
			i += 25;
			if(i > 900)break;
		}
		
		if(DangerZone.server != null){
			i = 80;
			for(Map.Entry<String, Integer> entry: s_entitycounts.entrySet()){
				textAt( 650, DangerZone.screen_height - i, String.format("SERVER: %d  %s", entry.getValue().intValue(), entry.getKey()));
				i += 25;
				if(i > 900)break;
			}
		}
		
		textAt( 50, DangerZone.screen_height - 40, String.format("TOTALS: %d, %d  :: %d, %d", unique, total, s_unique, s_total));
		
		//Check for exit via keypad
		while(K_next()){
			if (K_getEventKey() == Keyboard.KEY_ESCAPE && K_isKeyDown(Keyboard.KEY_ESCAPE)){
				entitycounts.clear();
				s_entitycounts.clear();
				total = 0;
				unique = 0;
				s_total = 0;
				s_unique = 0;
				ImAllDone();
				return;
			}
			if (K_getEventKey() == Keyboard.KEY_BACKSLASH && K_isKeyDown(Keyboard.KEY_BACKSLASH)){
				entitycounts.clear();
				s_entitycounts.clear();
				total = 0;
				unique = 0;
				s_total = 0;
				s_unique = 0;
				ImAllDone();
				return;
			}
		}
		
		//Check for mouse events!
		while(M_next()){

		}

	}
	

	

}
