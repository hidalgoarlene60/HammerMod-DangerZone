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
public class PacketTypes {
	public static final int CONNECT = 0x56;
	public static final int DISCONNECT = 0x57;
	public static final int TIME = 0x80;
	public static final int TIMERESPONSE = 0x81;
	public static final int BLOCK = 0x85;
	public static final int CHUNK = 0x86;
	public static final int GETCHUNK = 0x87;
	public static final int WHATISTHISENTITY = 0x89;
	public static final int ENTITYREMOVE = 0x8a;
	public static final int ENTITYUPDATE = 0x90;
	public static final int ENTITYDEATH = 0x91;
	public static final int SPAWNENTITY = 0x92;
	public static final int INVENTORYUPDATE = 0x93;
	public static final int PLAYERACTION = 0x94;
	public static final int ENTITYHIT = 0x95;
	public static final int PLAYSOUND = 0x96;
	public static final int PLAYERVELOCITY = 0x97;
	public static final int LIGHTINGREQUEST = 0x98;
	public static final int CHATMESSAGE = 0x99;
	public static final int SPAWNPARTICLES = 0x9a;
	public static final int KILLME = 0x9b;
	public static final int COMMANDMESSAGE = 0x9c;
	public static final int PLAYERTELEPORT = 0x9f;
	public static final int PETNAME = 0xa0;
	public static final int VARINTUPDATE = 0xa1;
	public static final int MOUNTENTITY = 0xa2;
	public static final int VARFLOATUPDATE = 0xa3;
	public static final int COLORINGBLOCK = 0xa4;
	public static final int PLAYERPOSITIONVELOCITY = 0xa5;
	public static final int GAMEMODECHANGE = 0xa6;
	public static final int NEWEFFECT = 0xa7;
	public static final int VARSTRINGUPDATE = 0xa8;
	public static final int PLAYSONG = 0xa9;
	public static final int ENTITYINVENTORYUPDATE = 0xaa; //client to server
	public static final int KEYEVENTTOENTITY = 0xab;
	public static final int MOUSEEVENTTOENTITY = 0xac;
	public static final int DEATHGUI = 0xad;
	public static final int RESPAWN = 0xae;
	public static final int PLAYERDIEDCLIENT = 0xaf;
	public static final int BREAKBLOCK = 0xb0;
	public static final int CHUNKMETAUPDATE = 0xb1;
	public static final int HANDLEINVENTORY = 0xb2;
	public static final int PLAYERSTATS = 0xb3;
	public static final int GAMEDIFFICULTYCHANGE = 0xb4;
	public static final int IDLE = 0xb5; //keepalive for when f12_on is on!
		
}
