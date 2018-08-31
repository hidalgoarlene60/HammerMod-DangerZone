package dangerzone.thingstodo;


import java.util.Properties;

import dangerzone.Player;
import dangerzone.ToDoItem;
import dangerzone.Utils;
import dangerzone.entities.Entity;


public class MobKiller extends ToDoItem {
	
	Class<? extends Entity> objclass = null;
	String what = null;
	int count = 0;
	int needed = 0;
	
	//passthru...
	public MobKiller(String uniname, String mytitle, String somehelp, int type, Class<? extends Entity> inobjclass, String inname, int howmany) {
		super(uniname, mytitle, somehelp, type);
		objclass = inobjclass;
		count = 0;
		needed = howmany;
		what = inname;
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void onKilled(Player pl, Entity ent){
		if(ent == null || pl == null || objclass == null || truefalse == true)return;

		Class<? extends Entity> entclass = ent.getClass();	
		//System.out.printf("Class %s vs %s\n",  entclass.getCanonicalName(), objclass.getCanonicalName());
		//System.out.printf("and Class %s vs %s\n",  entclass.getName(), objclass.getName());
		//if(entclass.isInstance(objclass)){ - does not work!!!
		if(entclass.getName().equals(objclass.getName())){
			//System.out.printf("It is!\n");
			count++;
			if(count >= needed){
				truefalse = true;
				notify(pl);
			}
		}
	}
	
	public String gethelptext(){		
		String retstring = String.format("Kill %d %s.\nCurrent count %d.", needed, what, count);
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
