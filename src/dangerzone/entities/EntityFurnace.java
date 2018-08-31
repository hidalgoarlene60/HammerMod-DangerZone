package dangerzone.entities;
/*
 * This code is copyright Richard H. Clark, TheyCallMeDanger, OreSpawn, 2015-2020.
 * You may use this code for reference for modding the DangerZone game program,
 * and are perfectly welcome to cut'n'paste portions for your mod as well.
 * DO NOT USE THIS CODE FOR ANY PURPOSE OTHER THAN MODDING FOR THE DANGERZONE GAME.
 * DO NOT REDISTRIBUTE THIS CODE. 
 * 
 * This copyright remains in effect until January 1st, 2021. 
 * At that time, this code becomes public domain.
 * 
 * WARNING: There are bugs. Big bugs. Little bugs. Every size in-between bugs.
 * This code is NOT suitable for use in anything other than this particular game. 
 * NO GUARANTEES of any sort are given, either express or implied, and Richard H. Clark, 
 * TheyCallMeDanger, OreSpawn are not responsible for any damages, direct, indirect, or otherwise. 
 * You should have made backups. It's your own fault for not making them.
 * 
 * NO ATTEMPT AT SECURITY IS MADE. This code is USE AT YOUR OWN RISK.
 * Regardless of what you may think, the reality is, that the moment you 
 * connected your computer to the Internet, Uncle Sam, among many others, hacked it.
 * DO NOT KEEP VALUABLE INFORMATION ON INTERNET-CONNECTED COMPUTERS.
 * Or your phone...
 * 
 */



import dangerzone.Cooking;
import dangerzone.CookingRecipe;
import dangerzone.DangerZone;
//import dangerzone.FurnaceInventoryPacket;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.items.Items;

//getBID() is furnace off ID
//getIID() is furnace on ID

public class EntityFurnace extends Entity {
	
	public int blocktries = 0;

	public EntityFurnace(World w) {
		super(w);
		uniquename = "DangerZone:EntityFurnace";
		ignoreCollisions = true;
		width = 0.01f;
		height = 0.01f;
		setVarFloat(4, 0);
		setVarFloat(6, 0);
		setVarFloat(8, 0);
		setVarFloat(10, 0);
		setVarFloat(5, 0);
		setVarFloat(7, 0);
		setVarFloat(9, 0);
		setVarFloat(11, 0);
		has_inventory = true;
		maxrenderdist = 64; //it is invisible, so just update when the player gets near
	}
	
	public void update(float deltaT){
		int bid = world.getblock(dimension,  (int)posx, (int)posy, (int)posz);
		int meta = world.getblockmeta(dimension,  (int)posx, (int)posy, (int)posz);
		if(bid != getBID() && bid != getIID()){ //check to see if our block types are there
			if(world.isServer){
				blocktries++;
				if(blocktries > 20){
					dumpInventory();
					this.deadflag = true;
				}
			}
		}

		
		if(!this.deadflag && world.isServer){
			//inventory
			//2 is fuel
			//1 is uncooked
			//0 is cooked
			
			//varfloats
			//4 is fuel remaining
			//6 is fuel started with
			//8 is cook time remaining
			//10 is cook time started with
			
			InventoryContainer ic = null;
			
			for(int i = 0;i<2;i++){			
				float fuel = getVarFloat(4+i);
				if(fuel > 0)fuel -= 0.1f;
				if(fuel <= 0){
					fuel = 0;
					ic = getInventory(2+(i*3));
					if(ic != null){
						float newfuel = 0;
						if(ic.bid != 0){
							newfuel = Blocks.getBurnTime(ic.bid);
						}
						if(ic.iid != 0){
							newfuel = Items.getBurnTime(ic.iid);
						}
						if(newfuel > 0 && getInventory(1+(i*3)) != null){
							ic.count--;
							if(ic.count <= 0){
								ic = null;														
							}
							setInventory(2+(i*3), ic);
							
							fuel = newfuel;						
						}					
					}
					setVarFloat(6+i, fuel);

				}
				setVarFloat(4+i, fuel);
				if(fuel > 0){
					if(bid == getBID()){
						world.setblockandmeta(dimension, (int)posx, (int)posy, (int)posz, getIID(), meta);
					}
					ic = getInventory(1+(i*3));
					if(ic != null){
						InventoryContainer ic2 = getInventory(0+(i*3));
						boolean canadd = true;
						int iid = 0, bbd = 0;
						CookingRecipe r = null;
						if(ic.bid != 0)r = Cooking.find(Blocks.getUniqueName(ic.bid));
						if(ic.iid != 0)r = Cooking.find(Items.getUniqueName(ic.iid));
						if(r != null){
							iid = Items.findByName(r.outname);
							bbd = Blocks.findByName(r.outname);
						}else{
							canadd = false;
						}						
						
						if(ic2 != null){
							if(bbd != ic2.bid || iid != ic2.iid)canadd = false;
							if(ic2.count >= ic2.getMaxStack())canadd = false;
						}
						if(canadd){
							if(getVarFloat(10+i) != 0){
								float te = getVarFloat(8+i); //time elapsed
								float tr = getVarFloat(10+i) - te;
								te += 0.1f;
								if(tr > 3.999f && tr < 4.001f)world.playSound("DangerZone:furnace_ding", dimension, posx, posy, posz, 0.75f, 1.0f);
								if(te >= getVarFloat(10+i)){
									//done!
									if(ic2 == null){
										ic2 = new InventoryContainer();
										ic2.iid = iid;
										ic2.bid = bbd;
										ic2.count = 0;
										setInventory(0+(i*3), ic2);
									}
									ic2.count++;
									setInventoryChanged(0+(i*3));

									ic.count--;
									if(ic.count <= 0){
										ic = null;
										setInventory(1+(i*3), ic);
									}
									setInventoryChanged(1+(i*3));
																

									setVarFloat(8+i, 0);
									setVarFloat(10+i, 0);

									Utils.spawnExperience(1, this.world, dimension, posx, posy, posz);
									
									//player puts things in and out through a GUI, not here...

								}else{
									setVarFloat(8+i, te);
								}
							}else{
								//start the timer!							
								setVarFloat(8+i, 0);
								setVarFloat(10+i, (float)r.cooktime);
							}
						}
					}else{
						setVarFloat(8+i, 0);
						setVarFloat(10+i, 0);
					}
				}else{
					setVarFloat(4+i, 0);
					setVarFloat(6+i, 0);
					setVarFloat(8+i, 0);
					setVarFloat(10+i, 0);
					if(bid == getIID()){
						world.setblockandmeta(dimension, (int)posx, (int)posy, (int)posz, getBID(), meta);
					}
				}
			}
		}
		
		//kick entity out to other players!
		motionx = motiony = motionz = 0;
		super.update(deltaT);
	}
	
	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){
		int bid = world.getblock(dimension,  (int)posx, (int)posy, (int)posz);
		if(bid != getBID() && bid != getIID()){
			if(world.isServer){
				dumpInventory();
			}
			this.deadflag = true;
			return false;
		}
		Blocks.rightClickOnBlock(getBID(), p, dimension, (int)posx, (int)posy, (int)posz);
		return false;
	}
	
	private void dumpInventory(){
		InventoryContainer ic = null;
		for(int i=0;i<50;i++){
			ic = getInventory(i);
			if(ic != null){
				EntityBlockItem e = (EntityBlockItem)world.createEntityByName(DangerZone.blockitemname, dimension, posx, posy, posz);
				if(e != null){
					e.fill(ic);
					e.rotation_pitch = world.rand.nextInt(360);
					e.rotation_yaw = world.rand.nextInt(360);
					e.rotation_roll = world.rand.nextInt(360);
					e.motionx = (world.rand.nextFloat()-world.rand.nextFloat()/10f); 
					e.motiony = world.rand.nextFloat()/2;
					e.motionz = (world.rand.nextFloat()-world.rand.nextFloat()/10f); 
					world.spawnEntityInWorld(e);
				}	
			}
		}
	}
	

}
