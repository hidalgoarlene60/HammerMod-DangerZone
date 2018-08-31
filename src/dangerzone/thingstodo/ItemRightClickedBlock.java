package dangerzone.thingstodo;

import java.util.Properties;

import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.ToDoItem;
import dangerzone.Utils;
import dangerzone.items.Item;


public class ItemRightClickedBlock extends ToDoItem {
	
	Object obj; //because IDs are not set at initialization! Check later!
	int count = 0;
	int needed = 0;
	
	//passthru...
	public ItemRightClickedBlock(String uniname, String mytitle, String somehelp, int type, Object inobj, int howmany) {
		super(uniname, mytitle, somehelp, type);
		obj = inobj;
		count = 0;
		needed = howmany;
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void itemRightClickedBlock(Player pl, InventoryContainer ic, int dimension, int focusx, int focusy, int focusz){
		if(ic == null || pl == null || truefalse == true)return;
		
		if(ic.iid != 0){
			if(obj instanceof Item){
				Item it = (Item)obj;
				if(ic.iid == it.itemID){
					count++;				
					if(count >= needed){
						truefalse = true;
						notify(pl);
					}
				}
			}
		}

	}
	
	public String gethelptext(){		
		String retstring = helptext + String.format("\nCurrent count %d of %d.", count, needed);
		return retstring;
	}
		
	public void writeSelf(Properties prop, String tag){	
		super.writeSelf(prop, tag);
		prop.setProperty(String.format("%s%s", tag, "CurCount"), String.format("%d", count));
	}
	
	public void readSelf(Properties prop, String tag){	
		super.readSelf(prop, tag);
		count = Utils.getPropertyInt(prop, String.format("%s%s", tag, "CurCount"), 0, Integer.MAX_VALUE, 0); //default is 0
	}


}
