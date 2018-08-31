package dangerzone.thingstodo;

import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.ToDoItem;


public class ArmoredUp extends ToDoItem {
	
	
	//passthru...
	public ArmoredUp(String uniname, String mytitle, String somehelp, int type) {
		super(uniname, mytitle, somehelp, type);
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void onArmorPlaced(Player pl, InventoryContainer ic, int slot){
		if(pl == null || truefalse == true)return;
		
		if(pl.getArmor(0)!= null){
			if(pl.getArmor(1)!= null){
				if(pl.getArmor(2)!= null){
					if(pl.getArmor(3)!= null){
						truefalse = true;
						notify(pl);
					}
				}
			}			
		}
	}

}
