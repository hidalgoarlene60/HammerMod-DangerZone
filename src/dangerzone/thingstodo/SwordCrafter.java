package dangerzone.thingstodo;

import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.ToDoItem;
import dangerzone.items.ItemSword;

public class SwordCrafter extends ToDoItem {
	
	//passthru...
	public SwordCrafter(String uniname, String mytitle, String somehelp, int type) {
		super(uniname, mytitle, somehelp, type);
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void onCrafted(Player pl, InventoryContainer ic){
		if(ic == null || pl == null || truefalse == true)return;
		if(ic.getItem() instanceof ItemSword){
			truefalse = true;
			notify(pl);
		}
	}

}
