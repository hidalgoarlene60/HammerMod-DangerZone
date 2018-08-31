package dangerzone.thingstodo;

import java.util.Properties;

import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.ToDoItem;
import dangerzone.ToDoList;
import dangerzone.Utils;
import dangerzone.items.Item;


public class FenceBuilder extends ToDoItem {
	
	Object obj; //because IDs are not set at initialization! Check later!
	Boolean keytruefalse = false;
	
	//passthru...
	public FenceBuilder(String uniname, String mytitle, String somehelp, int type, Object inobj) {
		super(uniname, mytitle, somehelp, type);
		obj = inobj;
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void onCrafted(Player pl, InventoryContainer ic){
		if(ic == null || pl == null || truefalse == true)return;
		
		
		if(ic.iid != 0){
			if(obj instanceof Item){
				Item it = (Item)obj;
				if(ic.iid == it.itemID)keytruefalse = true; //key has been crafted
			}
		}
		
		if(keytruefalse == true){
			ToDoItem tdi = ToDoList.find("DangerZone:autofence"); //find the other part
			if(tdi != null){
				if(tdi.truefalse){
					truefalse = true;
					notify(pl);
				}
			}
		}
	}
	
	public void writeSelf(Properties prop, String tag){	
		super.writeSelf(prop, tag);
		prop.setProperty(String.format("%s%s", tag, "KeyTrueFalse"), String.format("%s", keytruefalse?"true":"false"));
	}
	
	public void readSelf(Properties prop, String tag){	
		super.readSelf(prop, tag);
		keytruefalse = Utils.getPropertyBoolean(prop, String.format("%s%s", tag, "KeyTrueFalse"), false); //default is false!
	}

}
