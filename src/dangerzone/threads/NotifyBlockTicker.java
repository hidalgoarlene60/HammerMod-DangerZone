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


public class NotifyBlockTicker implements Runnable {
	
	public static List<ListCoords> blocklist = null;
	public static List<ListCoords> workinglist = null;
	public static List<ListCoords> notyetlist = null;
	private static Lock lock = new ReentrantLock();
	public static volatile int recursioncount = 0;

	public void run() {

		Thread thisthread = Thread.currentThread();
		thisthread.setPriority(Thread.MIN_PRIORITY);
		blocklist = new ArrayList<ListCoords>();
		workinglist = null;
		notyetlist = null;
		//int count = 0;
		
		//Let things settle down a little first...
		try {
			Thread.sleep(4000);
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
			
			//copy from the accumulation list to the working list
			lock.lock();
			notyetlist = new ArrayList<ListCoords>();
			workinglist = new ArrayList<ListCoords>();
			Iterator<ListCoords> ib = blocklist.iterator();
			ListCoords li = null;
			//count = 0;
			while(ib.hasNext()){
				//count++;
				li = ib.next();
				//keep the list pruned down!!! It gets SLOW when it gets long.
				bid = DangerZone.server_chunk_cache.getBlockTry(li.dim, li.xpos, li.ypos, li.zpos); //try, but not very hard!
				if(bid != 0){
					li.count--;
					if(li.count <= 0){
						workinglist.add(li);
					}else{
						notyetlist.add(li);
					}
				}
			}
			
			blocklist = notyetlist;
			notyetlist = null;
			
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
						Blocks.notifyNeighborChanged(bid, DangerZone.server_world, li.dim, li.xpos, li.ypos, li.zpos);
					}
				}				
			}
			workinglist = null;

			
		}
	}
	
	public static void addNotifySingle(int d, int x, int y, int z){
		addNotifySingle(d, x, y, z, 0);
	}

	public static void addNotifySingle(int d, int x, int y, int z, int c){
		if(blocklist == null)return; //not initialized yet!
		if(!DangerZone.start_server)return; //NOT for client-only
		if(x < 0 || y < 0 || z < 0)return;
		
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
	
	public static void addNotifyBlocksAround(int d, int x, int y, int z){
		addNotifyBlocksAround(d, x, y, z, 0);
	}
	
	//try to avoid painful searches through the list, and especially adding blocks one by one...
	public static void addNotifyBlocksAround(int d, int x, int y, int z, int c){
		if(blocklist == null)return; //not initialized yet!
		if(!DangerZone.start_server)return; //NOT for client-only
		if(x < 0 || y < 0 || z < 0)return;
		int i, j, k;
		int bid;
		
		lock.lock();
	
		if(blocklist.size() > 4000 && c <= 1){ //non-delayed notifies can be done immediately when the list gets too big.
			//something is overloading us. Either a new chunk or an explosion.
			//Too much overhead adding to a list. Just do it now. Hopefully the list will clear eventually...
			recursioncount++;
			if(recursioncount > 500){ //something REALLY big. bail...
				recursioncount--;
				lock.unlock();
				return;
			}
			lock.unlock();
			for(i=-1;i<=1;i++){
				for(j=-1;j<=1;j++){
					for(k=-1;k<=1;k++){
						if(i==0&&j==0&&k==0)continue;										
						bid = DangerZone.server_chunk_cache.getBlockTry(d, x+i, y+j, z+k);
						if(bid != 0){
							Blocks.notifyNeighborChanged(bid, DangerZone.server_world, d, x+i, y+j, z+k);
						}
					}
				}
			}
			lock.lock();
			recursioncount--;
			lock.unlock();
			return;
		}
		
		int blks[][][] = new int[3][3][3];
		ListCoords ln = null;
		Iterator<ListCoords> ib = blocklist.iterator();
		ListCoords li = null;
		while(ib.hasNext()){
			li = ib.next();
			//check list entry to see if it is in range
			if(li.dim == d && li.xpos >= x-1 && li.ypos >= y-1 && li.zpos >= z-1){
				if(li.xpos <= x+1 && li.ypos <= y+1 && li.zpos <= z+1){
					//see which block it is and mark it off
					i = li.xpos - x;
					j = li.ypos - y;
					k = li.zpos - z;
					if(i==0&&j==0&&k==0)continue;
					blks[i+1][j+1][k+1] = 1; //don't add this!
				}
			}
		}

		//now see which ones are left to add...
		for(i=-1;i<=1;i++){
			for(j=-1;j<=1;j++){
				for(k=-1;k<=1;k++){
					if(i==0&&j==0&&k==0)continue;	//not self				
					if(blks[i+1][j+1][k+1] == 0){
						bid = DangerZone.server_chunk_cache.getBlockTry(d, x+i, y+j, z+k);
						if(bid != 0){
							ln = new ListCoords(d, x+i, y+j, z+k, c + (blocklist.size()/250)); //add in more delay as the list grows!
							blocklist.add(ln);
						}
					}
				}
			}
		}
		
		lock.unlock();
		
	}
	
	
}
