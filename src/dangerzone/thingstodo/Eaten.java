package dangerzone.thingstodo;

import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.ToDoItem;
import dangerzone.items.Item;


public class Eaten extends ToDoItem {
	
	Object obj; //because IDs are not set at initialization! Check later!
	
	//passthru...
	public Eaten(String uniname, String mytitle, String somehelp, int type, Object inobj) {
		super(uniname, mytitle, somehelp, type);
		obj = inobj;
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void onEaten(Player pl, InventoryContainer ic){
		if(ic == null || pl == null || truefalse == true)return;
		
		if(ic.iid != 0){
			if(obj instanceof Item){
				Item it = (Item)obj;
				if(ic.iid == it.itemID){
					truefalse = true;
					notify(pl);
				}
			}
		}
	}

}
