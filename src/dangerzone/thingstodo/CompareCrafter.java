package dangerzone.thingstodo;

import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.ToDoItem;
import dangerzone.blocks.Block;
import dangerzone.items.Item;


public class CompareCrafter extends ToDoItem {
	
	Object obj; //because IDs are not set at initialization! Check later!
	
	//passthru...
	public CompareCrafter(String uniname, String mytitle, String somehelp, int type, Object inobj) {
		super(uniname, mytitle, somehelp, type);
		obj = inobj;
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void onCrafted(Player pl, InventoryContainer ic){
		if(ic == null || pl == null || truefalse == true)return;
		
		if(ic.iid != 0){
			if(obj instanceof Item){
				Item it = (Item)obj;
				if(ic.iid == it.itemID)truefalse = true;
			}
		}
		if(ic.bid != 0){
			if(obj instanceof Block){
				Block bl = (Block)obj;
				if(ic.bid == bl.blockID)truefalse = true;
			}
		}
		if(truefalse)notify(pl);
	}

}
