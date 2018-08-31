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


//this USES the GuiInterface class, but only parts of it...
//just process(), actually...
public class OverlayGUI {
		
	public static GuiInterface guiarray[];
	public static final int guiMAX = 16; //these are runtime overlays. No need for more than one or two, really.

		
	public OverlayGUI(){
		int i;
		guiarray = new GuiInterface[guiMAX];
		for(i=0;i<guiMAX;i++){
			guiarray[i] = null;
		}
	}
	
	public static int registerOverlayGUI(GuiInterface b){
		int i;
		for(i=1;i<guiMAX;i++){
			if(guiarray[i] == null){
				guiarray[i] = b;
				return i;
			}
		}
		return 0;
	}
	
}