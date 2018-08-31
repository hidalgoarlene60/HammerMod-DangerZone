package dangerzone;

import dangerzone.blocks.Block;
import dangerzone.items.Item;

public class StuffList {
	
	public Object thing;
	public int min;
	public int max;
	public int chance;
	
	public StuffList(Object inobj, int inmin, int inmax, int inchance){
		thing = inobj;
		min = inmin;
		max = inmax;
		chance = inchance;		
	}
	
	public int getbid(){
		if(thing instanceof Block)return ((Block) thing).blockID;
		return 0;
	}
	
	public int getiid(){
		if(thing instanceof Item)return ((Item) thing).itemID;
		return 0;
	}

}
