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

//This class is for adding your own KeyHandler class, where you can add your own active GUI keys to
//the base game donormalinput() loop. For example, if the user presses "F" you could set their health to maxHealth(),
//or "H" could toggle a heads-up display on and off.
//Make your own hotkeys!
//Might work. I didn't actually try it... :)
//

public class KeyHandlers {
		
	public static KeyHandler keyhandlearray[];
	public static final int keyhandleMAX = 32; //hot keys

		
	public KeyHandlers(){
		int i;
		keyhandlearray = new KeyHandler[keyhandleMAX];
		for(i=0;i<keyhandleMAX;i++){
			keyhandlearray[i] = null;
		}
	}
	
	public static int registerKeyHandler(KeyHandler b){
		int i;
		for(i=1;i<keyhandleMAX;i++){
			if(keyhandlearray[i] == null){
				keyhandlearray[i] = b;
				return i;
			}
		}
		return 0;
	}
	
	public static void handleEvent(){
		int i;
		for(i=1;i<keyhandleMAX;i++){
			if(keyhandlearray[i] == null)return;
			if(keyhandlearray[i].handleEvent())return;
		}
		return;
	}
	
	public static void handleDown(){
		int i;
		for(i=1;i<keyhandleMAX;i++){
			if(keyhandlearray[i] == null)return;
			if(keyhandlearray[i].handleDown())return;
		}
		return;
	}
		
	
}