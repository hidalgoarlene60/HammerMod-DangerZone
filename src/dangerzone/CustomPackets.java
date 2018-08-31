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




public class CustomPackets {
	
	//packetIDs are offset by 1024!
	
	public static CustomPacket CustomPacketsArray[];
	public static final int CustomPacketsMAX = 256;

		
	public CustomPackets(){
		int i;
		CustomPacketsArray = new CustomPacket[CustomPacketsMAX];
		for(i=0;i<CustomPacketsMAX;i++){
			CustomPacketsArray[i] = null;
		}
	}
	
	public static int registerCustomPacket(CustomPacket b){
		int i;
		for(i=1;i<CustomPacketsMAX;i++){
			if(CustomPacketsArray[i] == null){
				CustomPacketsArray[i] = b;
				b.packetID = i+1024;
				//System.out.printf("Custom packet %s at %d\n", b.uniquename, i+1024);
				return i+1024;
			}
		}
		return 0;
	}
	
	public static int getIDByName(String s){
		int i;
		for(i=1;i<CustomPacketsMAX;i++){
			if(CustomPacketsArray[i] != null){
				if(s.equals(CustomPacketsArray[i].uniquename)){
					return i+1024;
				}
			}
		}
		return 0;
	}
	
	public static void reRegisterCustomPacketAt(String s, int loc){
		int i;
		int ploc = loc - 1024;
		
		if(ploc <= 0 || ploc >= CustomPacketsMAX)return;
		if(s == null || s.equals(""))return;
		
		if(CustomPacketsArray[ploc] != null){
			if(s.equals(CustomPacketsArray[ploc].uniquename)){
				return; //already where its supposed to be! :)
			}
			//maybe it is somewhere else?
			for(i=1;i<CustomPacketsMAX;i++){
				if(CustomPacketsArray[i] != null){
					if(s.equals(CustomPacketsArray[i].uniquename)){
						//Take it, and move the intruder where I was
						CustomPacket me = CustomPacketsArray[i];
						CustomPacket intruder = CustomPacketsArray[ploc];
						CustomPacketsArray[ploc] = me;
						CustomPacketsArray[ploc].packetID = ploc + 1024;
						CustomPacketsArray[i] = intruder;
						CustomPacketsArray[i].packetID = i + 1024;
						return;
					}
				}
			}
		}else{
			//maybe it is somewhere else?
			for(i=1;i<CustomPacketsMAX;i++){
				if(CustomPacketsArray[i] != null){
					if(s.equals(CustomPacketsArray[i].uniquename)){
						//Move it!
						CustomPacketsArray[ploc] = CustomPacketsArray[i];
						CustomPacketsArray[ploc].packetID = ploc + 1024;
						CustomPacketsArray[i] = null;
						return;
					}
				}
			}
		}
	}
	
	public static void messageFromServer(int type, NetworkInputBuffer in){
		if(type <= 1024 || type >= 1024+CustomPacketsMAX)return;
		int ty = type-1024;
		if(CustomPacketsArray[ty] != null){
			CustomPacketsArray[ty].messageFromServer(in);
		}
	}
	
	public static void messageFromClient(int type, Player p, NetworkInputBuffer in){
		if(type <= 1024 || type >= 1024+CustomPacketsMAX)return;
		int ty = type-1024;
		if(CustomPacketsArray[ty] != null){
			CustomPacketsArray[ty].messageFromClient(p, in);
		}
	}
	
	

}
