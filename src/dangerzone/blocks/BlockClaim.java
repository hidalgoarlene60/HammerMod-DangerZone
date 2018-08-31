package dangerzone.blocks;

import java.util.ArrayList;
import java.util.List;

import dangerzone.Player;
import dangerzone.ServerHooker;
import dangerzone.World;


public class BlockClaim extends Block {

	public BlockClaim(String n, String txt) {
		super(n, txt);
		maxdamage = 1;
		burntime = 45;
	}
	
	public boolean doPlaceBlock(int focusbid, Player p, World w, int dimension, int x, int y, int z, int side){

		if(w.isServer){
			w.playSound(Blocks.getPlaceSound(this.blockID), dimension, x, y, z, 0.5f, 1.0f);
			if(ServerHooker.canClaimHere(p, dimension, x, y, z)){
				int chunkx = x>>4;
				int chunkz = z>>4;
				List <String> owners = w.serverchunkcache.getChunkOwners(dimension, x, y, z);
				if(owners != null)return true;
				owners = new ArrayList<String>();
				owners.add(p.myname);
				w.serverchunkcache.setChunkOwners(dimension, x, y, z, owners);
				int i, j, k;
				for(i=0;i<16;i++){
					for(k=0;k<16;k++){
						for(j=250;j>0;j--){
							if(w.getblock(dimension, (chunkx<<4)+i, j, (chunkz<<4)+k) != 0){
								w.setblock(dimension, (chunkx<<4)+i, j+1, (chunkz<<4)+k, Blocks.claim_marker.blockID);
								break;
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z){
		return 0;
	}

}
