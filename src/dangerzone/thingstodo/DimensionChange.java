package dangerzone.thingstodo;


import java.util.Properties;

import dangerzone.Player;
import dangerzone.ToDoItem;
import dangerzone.Utils;


public class DimensionChange extends ToDoItem {
	
	int count = 0;
	
	//passthru...
	public DimensionChange(String uniname, String mytitle, String somehelp, int type) {
		super(uniname, mytitle, somehelp, type);
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void onDimension(Player pl,int oldd, int newd){
		if(pl == null || truefalse == true)return;
		count++;
		if(count >= 10){
			truefalse = true;
			notify(pl);
		}

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
