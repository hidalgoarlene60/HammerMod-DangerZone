package dangerzone.threads;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



import dangerzone.DangerZone;
import dangerzone.ListCoords;
import dangerzone.blocks.Blocks;


public class FastBlockTicker implements Runnable {
	
	public static List<ListCoords> blocklist = null;
	public static List<ListCoords> workinglist = null;
	public static List<ListCoords> notyetlist = null;
	private static Lock lock = new ReentrantLock();
	public static int cycle;

	public void run() {

		Thread thisthread = Thread.currentThread();
		thisthread.setPriority(Thread.MIN_PRIORITY);
		blocklist = new ArrayList<ListCoords>();
		workinglist = new ArrayList<ListCoords>();
		notyetlist = new ArrayList<ListCoords>();
		cycle = 0;
		//int count = 0;
		
		//Let things settle down a little first...
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while(DangerZone.gameover == 0){
			int bid;
			
			//Approximately 100 ms ticks
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(DangerZone.f12_on)continue; //PAUSED
			
			//count this cycle!
			cycle++;
			
			//copy from the accumulation list to the working list
			lock.lock();
			Iterator<ListCoords> ib = blocklist.iterator();
			ListCoords li = null;
			//count = 0;
			while(ib.hasNext()){
				//count++;
				li = ib.next();
				li.count--;
				if(li.count <= 0){
					workinglist.add(li);
				}else{
					notyetlist.add(li);
				}
			}
			blocklist.clear();
			//add notyet back to blocklist
			ib = notyetlist.iterator();
			while(ib.hasNext()){
				li = ib.next();
				blocklist.add(li);
			}
			notyetlist.clear();
			lock.unlock();
			//System.out.printf("tick count = %d\n", count);
						
			ib = workinglist.iterator();
			li = null;
			while(ib.hasNext()){
				li = ib.next();			
				bid = DangerZone.server_chunk_cache.getBlockTry(li.dim, li.xpos, li.ypos, li.zpos); //try, but not very hard!
				if(bid != 0){
					//Because these ticks happen so fast, they can really wreak havoc on the caches. Try to make sure they won't!
					if(DangerZone.server.distToNearestPlayerFromHere(li.dim, li.xpos, li.ypos, li.zpos) < DangerZone.entityupdatedist){
						Blocks.dofastblocktick(DangerZone.server_world, li.dim, li.xpos, li.ypos, li.zpos, bid);
					}
				}				
			}
			workinglist.clear();
			
		}
	}
	
	public static void addFastTick(int d, int x, int y, int z){
		addFastTick(d, x, y, z, 0);
	}

	public static void addFastTick(int d, int x, int y, int z, int c){
		if(blocklist == null)return; //not initialized yet!
		if(!DangerZone.start_server)return; //NOT for client-only
		
		lock.lock();
		ListCoords ln = new ListCoords(d, x, y, z, c);
		Iterator<ListCoords> ib = blocklist.iterator();
		ListCoords li = null;
		while(ib.hasNext()){
			li = ib.next();
			if(li.dim == d && li.xpos == x && li.ypos == y && li.zpos == z){
				lock.unlock();
				return; //already on the list			
			}
		}
		blocklist.add(ln);
		lock.unlock();
	}
	
	
}
