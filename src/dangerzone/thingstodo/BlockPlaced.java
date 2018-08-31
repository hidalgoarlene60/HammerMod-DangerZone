package dangerzone.thingstodo;


import java.util.Properties;

import dangerzone.Player;
import dangerzone.ToDoItem;
import dangerzone.Utils;
import dangerzone.blocks.Block;


public class BlockPlaced extends ToDoItem {
	
	Block thisblock = null;
	String what = null;
	int count = 0;
	int needed = 0;
	
	//passthru...
	public BlockPlaced(String uniname, String mytitle, String somehelp, int type, Block blocktype, int howmany) {
		super(uniname, mytitle, somehelp, type);
		thisblock = blocktype;
		count = 0;
		needed = howmany;
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void onPlaced(Player pl, int d, int x, int y, int z, int bid){
		if(pl == null || truefalse == true || thisblock == null)return;

		if(bid == thisblock.blockID){
			count++;
			if(count >= needed){
				truefalse = true;
				notify(pl);
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
