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


import java.util.Iterator;

import dangerzone.entities.EntityFurnace;



public class FurnaceInventoryPacket extends CustomPacket {
	
	public FurnaceInventoryPacket(){
		super();
		uniquename = "DangerZone:FurnaceInventoryPacket";
		this.packetID = CustomPackets.getIDByName(uniquename); //invalid before registration, but works fine thereafter!
	}
	
	//Tell server what is now in the inventory slot
	public void inventoryUpdateToServer(int eid, int index, InventoryContainer ic){
		NetworkOutputBuffer os = DangerZone.server_connection.getOutputStream();
		if(os == null)return;
		int subtype = 0; //update
		os.writeInt(this.packetID);
		os.writeInt(eid);
		os.writeInt(subtype);
		os.writeInt(index);
		if(ic != null){
			os.writeInt(ic.count);
			os.writeInt(ic.bid);
			os.writeInt(ic.iid);
			os.writeInt(ic.currentuses);
			os.writeString(ic.icmeta);
			if(ic.count == 1 && ic.bid == 0 && ic.iid != 0){
				int atcount = 0;
				if(ic.attributes != null)atcount = ic.attributes.size();
				os.writeInt(atcount);
				if(atcount > 0){
					ItemAttribute ia = null;
					for(int k=0;k<atcount;k++){
						ia = ic.attributes.get(k);
						os.writeInt(ia.type);
						os.writeInt(ia.value);
					}
				}
			}
		}else{
			int i = 0;
			os.writeInt(i);
			os.writeInt(i);
			os.writeInt(i);
			os.writeInt(i);	
			os.writeString(null);
		}

		DangerZone.server_connection.releaseOutputStream();
	}
	
	public void requestContents(int eid){
		NetworkOutputBuffer os = DangerZone.server_connection.getOutputStream();
		if(os == null)return;
		int subtype = 1; //fill me up please!
		os.writeInt(this.packetID);
		os.writeInt(eid);
		os.writeInt(subtype);
		DangerZone.server_connection.releaseOutputStream();
	}
	

	public void messageFromServer(NetworkInputBuffer is){
		int subtype = 0;
		int count = 0;
		int eid = 0;
		int bid = 0;
		int iid = 0;
		int currentuses = 0;
		int index = 0;
		String inmeta = null;
		
		eid = is.readInt();
		subtype = is.readInt();
		if(subtype == 0){ //inventory slot update from client!
			index = is.readInt();
			count = is.readInt();
			bid = is.readInt();
			iid = is.readInt();
			currentuses = is.readInt();
			inmeta = is.readString();
			InventoryContainer ic = new InventoryContainer();
			ic.count = count;
			ic.bid = bid;
			ic.iid = iid;
			ic.currentuses = currentuses;
			ic.icmeta = inmeta;
			if(ic.count == 1 && ic.bid == 0 && ic.iid != 0){
				int atcount = is.readInt();
				if(atcount > 0){
					int i, attype, atval;
					for(i=0;i<atcount;i++){
						attype = is.readInt();
						atval = is.readInt();
						ic.addAttribute(attype, atval);
					}
				}
			}
			//got packet info. Now find the entity
			EntityFurnace ce = (EntityFurnace)DangerZone.entityManager.findEntityByID(eid);
			if(ce != null){
				if(index >= 0 && index < 50){
					if(count == 0){
						ce.setInventory(index, null);
					}else{
						ce.setInventory(index, ic);
					}
					//entity is updated
					//we are done
				}
			}				
		}
	}
	
	
	public void inventoryUpdateToClient(Player p, int eid, int index, InventoryContainer ic){
		NetworkOutputBuffer os = p.server_thread.getOutputStream();
		if(os == null)return;
		int subtype = 0;
		os.writeInt(this.packetID);
		os.writeInt(eid);
		os.writeInt(subtype);
		os.writeInt(index);
		if(ic != null){
			os.writeInt(ic.count);
			os.writeInt(ic.bid);
			os.writeInt(ic.iid);
			os.writeInt(ic.currentuses);
			os.writeString(ic.icmeta);
			if(ic.count == 1 && ic.bid == 0 && ic.iid != 0){
				int atcount = 0;
				if(ic.attributes != null)atcount = ic.attributes.size();
				os.writeInt(atcount);
				if(atcount > 0){
					ItemAttribute ia = null;
					for(int k=0;k<atcount;k++){
						ia = ic.attributes.get(k);
						os.writeInt(ia.type);
						os.writeInt(ia.value);
					}
				}
			}
		}else{
			int i = 0;
			os.writeInt(i);
			os.writeInt(i);
			os.writeInt(i);
			os.writeInt(i);	
			os.writeString(null);
		}		
		p.server_thread.releaseOutputStream();
	}
	
	public void inventoryUpdateToAllClients(int eid, int d, int index, InventoryContainer ic){
		DangerZone.server.player_list_lock.lock();
		Iterator<ListInt> ii = DangerZone.server.server_thread_list.iterator();
		ListInt st;
		while(ii.hasNext()){
			st = ii.next();
			if(DangerZone.server.players[st.index] != null && DangerZone.server.players[st.index].p.dimension == d){
				//send!
				inventoryUpdateToClient(DangerZone.server.players[st.index].p, eid, index, ic);
			}
		}		
		DangerZone.server.player_list_lock.unlock();
	}
	
	//RECEIVE side
	public void messageFromClient(Player plyr, NetworkInputBuffer is){
		int subtype = 0;
		int count = 0;
		int eid = 0;
		int bid = 0;
		int iid = 0;
		int currentuses = 0;
		int index = 0;
		String inmeta = null;
		
		eid = is.readInt();
		subtype = is.readInt();
		if(subtype == 0){ //inventory slot update from client!
			index = is.readInt();
			count = is.readInt();
			bid = is.readInt();
			iid = is.readInt();
			currentuses = is.readInt();
			inmeta = is.readString();
			InventoryContainer ic = new InventoryContainer();
			ic.count = count;
			ic.bid = bid;
			ic.iid = iid;
			ic.currentuses = currentuses;
			ic.icmeta = inmeta;
			if(ic.count == 1 && ic.bid == 0 && ic.iid != 0){
				int atcount = is.readInt();
				if(atcount > 0){
					int i, attype, atval;
					for(i=0;i<atcount;i++){
						attype = is.readInt();
						atval = is.readInt();
						ic.addAttribute(attype, atval);
					}
				}
			}
			//got packet info. Now find the entity
			EntityFurnace ce = (EntityFurnace)DangerZone.server.entityManager.findEntityByID(eid);
			if(ce != null){
				if(index >= 0 && index < 50){
					if(count == 0){
						ce.setInventory(index, null);
					}else{
						ce.setInventory(index, ic);
					}
					//entity is updated
					//now send out to all players including the one that sent this.
					//
					DangerZone.server.player_list_lock.lock();
					Iterator<ListInt> ii = DangerZone.server.server_thread_list.iterator();
					ListInt st;
					while(ii.hasNext()){
						st = ii.next();
						if(DangerZone.server.players[st.index] != null && DangerZone.server.players[st.index].p.dimension == plyr.dimension){
							//send!
							inventoryUpdateToClient(DangerZone.server.players[st.index].p, eid, index, ce.getInventory(index));
						}
					}		
					DangerZone.server.player_list_lock.unlock();
				}
			}				
		}else if(subtype == 1){
			//want to know its contents.
			//update it!
			EntityFurnace ce = (EntityFurnace)DangerZone.server.entityManager.findEntityByID(eid);
			if(ce != null){
				for(int i=0;i<50;i++){
					inventoryUpdateToClient(plyr, eid, i, ce.getInventory(i));
				}
			}				
		}
	}

}
