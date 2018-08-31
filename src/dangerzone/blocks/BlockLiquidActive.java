package dangerzone.blocks;
import dangerzone.BreakChecks;
import dangerzone.Player;
import dangerzone.World;

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
public class BlockLiquidActive extends Block {

	public BlockLiquidActive(String n, String txt) {
		super(n, txt);
		alwaystick = true;
		isSolidForRendering = false;
		isTranslucent = true;
		isSolid = false;
		isLiquid = true;
		friction = 0.10f;
		maxstack = 16;
		showInInventory = false;
	}
	
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z){
		//Player can be NULL! Make sure you check!
		return 0;
	}
	
	public void tickMe(World w, int d, int x, int y, int z){
		
		int i, j, k;
		int bid;
		int nm;
		int meta;
		boolean change = true;
		int mymeta = w.getblockmeta(d, x, y, z);
		
		if((mymeta&0xf) == 0){
			for(i=-1;i<=1&&change;i++){
				for(j=-1;j<=0&&change;j++){
					for(k=-1;k<=1&&change;k++){
						if(i==0 && j==0 && k==0)continue; //not self!
						bid = w.getblock(d, x+i, y+j, z+k);
						meta = w.getblockmeta(d, x+i, y+j, z+k);
						if(bid == 0){
							change = false;
						}else if(bid == blockID){
							if(meta != 0)change = false;
						}else if(bid != Blocks.getStaticBlockid(blockID)){
							if(Blocks.isLeaves(bid) && !Blocks.isWaterPlant(bid))change = false;
						}
					}
				}
			}
		}else{
			change = false;
		}
		
		if(change){
			//System.out.printf("Changed!\n");
			if(BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, Blocks.getStaticBlockid(this.blockID), 0)){
				w.setblockandmeta(d, x, y, z, Blocks.getStaticBlockid(blockID), 0); //change to stable block!
			}
			return;
		}
		
		//if block on top is static, or active and full, fill up self!
		
		bid = w.getblock(d, x, y+1, z);
		meta = w.getblockmeta(d, x, y+1, z);
		if(bid == Blocks.getStaticBlockid(blockID) && bid != 0){
			if((mymeta&0xf) != 0){
				if(BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, blockID, mymeta&0xfff0)){
					w.setblockandmeta(d, x, y, z, blockID, mymeta&0xfff0); //fill up!
				}
			}
		}else if(bid == blockID && meta == 0){
			if((mymeta&0xf) != 0){
				if(BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, blockID, mymeta&0xfff0)){
					w.setblockandmeta(d, x, y, z, blockID, mymeta&0xfff0); //fill up!
				}
			}
		}

		meta = w.getblockmeta(d, x, y, z) & 0x0f;

		
		//META IS HEIGHT. 0 = FULL, 0x0f = lowest level. Yes, its' backwards. Deal with it.
		//0 means it stays full. >0 and it can empty.
		
		//should we just fall?
		bid = w.getblock(d, x, y-1, z);
		if(bid == 0 || (Blocks.isLeaves(bid) && !Blocks.isWaterPlant(bid))){
			if(BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y-1, z, blockID, meta)
				&& BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, 0, 0)){				
				Blocks.doBreakBlock(bid, w, d, x, y-1, z);
				if(meta == 0)meta = 1; //falling liquid cannot be a source!
				w.setblockandmeta(d, x, y-1, z, blockID, meta);			
				w.setblock(d, x, y, z, 0);
			}
			return;
		}
		
		//tick self down
		if(meta != 0){
			if(meta < 0x0f){
				meta++;	
				//System.out.printf("set self down to %d\n", meta);
				if(BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, blockID, meta)){
					w.setblockandmetanonotify(d, x, y, z, blockID, meta);
				}
			}
		}
		
		if(meta == 0xf){
			boolean byebye = true;
			for(j=0;j<=1;j++){
				for(i=-1;i<=1;i++){
					for(k=-1;k<=1;k++){
						bid = w.getblock(d, x+i, y+j, z+k);
						//System.out.printf("check bid,  x,y,z == %d, %d, %d, %d, %d\n", bid, i, j, k, y+j);
						if(bid == blockID){ //it's water!
							nm = w.getblockmeta(d, x+i, y+j, z+k) & 0x0f;
							if(nm < meta || (j==1&&k==0&&i==0)){
								//if there is one higher than me around, don't exit!
								//System.out.printf("found filler %d\n", nm);
								byebye = false;
							}
						}
					}
				}
			}
			if(byebye){
				if(BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y, z, 0, 0)){
					w.setblockandmeta(d, x, y, z, 0, 0);
				}
				//System.out.printf("byebye\n");
				return; //bye!
			}		
		}
		
		bid = w.getblock(d, x, y-1, z);
		if(bid == blockID){	
			int bm = w.getblockmeta(d, x, y-1, z) & 0x0f;
			if(bm > meta){
				if(BreakChecks.canChangeBlock(w, d, x, y, z, d, x, y-1, z, blockID, meta)){
					w.setblockandmeta(d, x, y-1, z, blockID, meta); //no loss, usually...
				}
			}
		}
		if(bid != blockID && meta < 14){
			for(j=-1;j<=0;j++){
				for(i=-1;i<=1;i++){
					for(k=-1;k<=1;k++){
						if(i==0 && k == 0)continue;
						bid = w.getblock(d, x+i, y+j, z+k);
						//System.out.printf("bid,  x,y,z == %d, %d, %d, %d\n", bid, i, j, k);
						if(bid == blockID){ //it's water!
							nm = w.getblockmeta(d, x+i, y+j, z+k) & 0x0f;
							//System.out.printf("found %d\n", nm);
							//if it is a lot lower than me, inc it!
							if(nm > meta+2){								
								nm--;
								nm--;
								//System.out.printf("meta set to %d\n", nm);
								if(BreakChecks.canChangeBlock(w, d, x, y, z, d, x+i, y+j, z+k, blockID, nm)){
									w.setblockandmeta(d, x+i, y+j, z+k, blockID, nm);	
								}
							}
						}
						if(bid == 0 || (Blocks.isLeaves(bid) && !Blocks.isWaterPlant(bid))){
							if(j==0){
								int mbid = w.getblock(d, x+i, y+j-1, z+k);
								if(mbid != 0 && !Blocks.isLeaves(mbid) && !Blocks.isLiquid(mbid)){
									if(BreakChecks.canChangeBlock(w, d, x, y, z, d, x+i, y+j, z+k, blockID, 0xf)){
										Blocks.doBreakBlock(bid, w, d, x+i, y+j, z+k);
										w.setblockandmeta(d, x+i, y+j, z+k, blockID, 0xf); //start small!
									}
									//System.out.printf("first set %d\n", meta);
								}
							}else{
								if(BreakChecks.canChangeBlock(w, d, x, y, z, d, x+i, y+j, z+k, blockID, 0xf)){
									Blocks.doBreakBlock(bid, w, d, x+i, y+j, z+k);
									w.setblockandmeta(d, x+i, y+j, z+k, blockID, 0xf); //start small!
								}
								//System.out.printf("second set %d\n", meta);
							}
						}
					}
				}
			}
		}
	}
	

}
