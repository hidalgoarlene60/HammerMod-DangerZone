package dangerzone.thingstodo;



import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.ToDoItem;
import dangerzone.entities.Entity;


public class PetFed extends ToDoItem {
	
	//passthru...
	public PetFed(String uniname, String mytitle, String somehelp, int type) {
		super(uniname, mytitle, somehelp, type);
	}

	//we just add our check that makes this true, and everything else is done for us!
	public void onPetFed(Player pl, Entity ent, InventoryContainer ic){
		if(ent == null || pl == null || truefalse == true)return;
		truefalse = true;
		notify(pl);
	}
	

}
