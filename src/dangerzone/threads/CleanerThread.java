package dangerzone.threads;
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
public class CleanerThread implements Runnable {
	public static volatile boolean cleanmenowplease = false;

	public void run() {
		
		int pass = 0;
		
		//Let things settle down a little first...
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		

		while(DangerZone.gameover == 0){
			
			pass++;
			
			if(cleanmenowplease){ //looks weird, but closes a small timing hole...
				System.runFinalization();
				System.gc();
				cleanmenowplease = false;
			}else{
				if(DangerZone.keepitsqueakyclean || !DangerZone.start_client){
					System.runFinalization();
					System.gc();
				}
			}
			
			//System.out.printf("pass = %d\n", pass);

			for(int i = 0;i<64 && DangerZone.gameover == 0 && !cleanmenowplease ;i++){ //should loop through server cache about every 20 seconds. client much more often.
				
				if(DangerZone.start_server){
					//Cache cleanup!
					DangerZone.server_world.serverchunkcache.cleanCacheRow((pass%3)==0?true:false); //flush every once in a while...
				}else{
					//Client-only Cache cleanup!
					DangerZone.world.chunkcache.cleanCacheRow(DangerZone.player.dimension, (int)DangerZone.player.posx, (int)DangerZone.player.posy, (int)DangerZone.player.posz);
				}
				
				try {
					Thread.sleep(25+DangerZone.rand.nextInt(20));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	
}