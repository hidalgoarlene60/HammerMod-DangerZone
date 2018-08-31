package dangerzone.gui;
import dangerzone.CommandHandlers;
import dangerzone.DangerZone;

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
public class PlayerCommandGUI extends GuiInterface {

	String[] last20 = new String[20];
	String currstring = new String();
	int upindex = 0;
	
	public PlayerCommandGUI(){
		super();
		for(int i=0;i<20;i++){
			last20[i] = new String();
		}
	}
	
	/*
	 * Commands to server... hunh... forgot where they actually go TO...!
	 * Aha! CommandHandler.doCommand()
	 */
	public void process(){

		int i;
		for(i=0;i<20;i++){
			if(!last20[i].equals("")){
				last20[i] = last20[i].trim();
				textAt( 50, (i*30) + 90, last20[i]);
			}
		}
		
		drawRectangleWithTexture(DangerZone.textinputtexture, 115, 60, 600, 30);
		textAt( 50, 60, "CMD:   " + currstring);
		
		//Check for exit via keypad
		String s = getTextChar();
		if(escaped){
			ImAllDone();
			return;
		}
		if(arrow_up){
			String temp[] = last20[upindex].split(":");
			if(temp.length > 1){
				currstring = temp[1];
				currstring = currstring.trim();
			}
			upindex++;
			if(upindex >= 20)upindex = 0;
		}
		if(arrow_down){
			String temp[] = last20[upindex].split(":");
			if(temp.length > 1){
				currstring = temp[1];
				currstring = currstring.trim();
			}
			upindex--;
			if(upindex < 0)upindex = 19;
		}
		if(entered){
			if(currstring.length() > 0){
				//Send it!
				boolean handled_local = CommandHandlers.parseCommand(DangerZone.player, DangerZone.playername + ": " + currstring);		
				if(!handled_local)DangerZone.server_connection.sendCommandMessage(DangerZone.playername + ": " + currstring);
				//we don't record it here, because we will get it back when it is broadcast out from the server
				currstring = "";
				upindex = 0;
			}else{			
				ImAllDone();
				return;
			}
		}
		if(s != null){
			if(!s.equals("delete")){
				//add a new char
				currstring += s;
			}else{
				//delete the last char
				if(currstring.length() > 0){
					String newstring = new String();
					for(i=0;i<currstring.length()-1;i++){
						newstring += currstring.charAt(i);
					}
					currstring = newstring;
				}
			}
		}

	}
	
	public void receiveMessage(String s){
		if(s != null){
			if(!s.equals("")){
				String lines[] = s.split("\n");
				int ilen = lines.length;
				int li = 0;
				while(ilen > 0){
					for(int i=19;i>0;i--){
						last20[i] = last20[i-1];
					}
					last20[0] = lines[li];
					li++;
					ilen--;
				}
			}
		}
	}
	

}
