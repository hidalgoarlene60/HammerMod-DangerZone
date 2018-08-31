package dangerzone;

import dangerzone.biomes.Biome;
import dangerzone.biomes.Trees;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.items.Items;

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
public class DZWorldDecorator extends WorldDecorator {
	
	//stuff you can find in a chest...
	public static StuffList[] things = new StuffList[] { 
			//Block or Item, min, max, %chance
			new StuffList(Items.coinsilver, 5, 25, 75),
			new StuffList(Items.coingold, 3, 15, 65),			
			new StuffList(Items.coinplatinum, 1, 5, 55),
			new StuffList(Items.diamond, 1, 5, 5),
			new StuffList(Items.emerald, 1, 5, 5),
			new StuffList(Items.bloodstone, 1, 5, 5),
			new StuffList(Items.sunstone, 1, 5, 5),
			new StuffList(Items.scubatanks, 1, 2, 55),
			new StuffList(Items.scubamask, 1, 2, 55),
		
			new StuffList(Items.woodenaxe, 1, 1, 25),
			new StuffList(Items.woodenshovel, 1, 1, 25),
			new StuffList(Items.woodenhoe, 1, 1, 25),
			new StuffList(Items.woodenpickaxe, 1, 1, 25),
			new StuffList(Items.woodensword, 1, 1, 25),
			new StuffList(Items.silveraxe, 1, 1, 20),
			new StuffList(Items.silvershovel, 1, 1, 20),
			new StuffList(Items.silverhoe, 1, 1, 20),
			new StuffList(Items.silverpickaxe, 1, 1, 20),
			new StuffList(Items.silversword, 1, 1, 20),
			new StuffList(Items.tinsword, 1, 1, 20),
			new StuffList(Items.tinaxe, 1, 1, 20),
			new StuffList(Items.tinshovel, 1, 1, 20),
			new StuffList(Items.tinhoe, 1, 1, 20),
			new StuffList(Items.tinpickaxe, 1, 1, 20),
			new StuffList(Items.coppersword, 1, 1, 20),
			new StuffList(Items.copperaxe, 1, 1, 20),
			new StuffList(Items.coppershovel, 1, 1, 20),
			new StuffList(Items.copperhoe, 1, 1, 20),
			new StuffList(Items.copperpickaxe, 1, 1, 20),
			
			new StuffList(Items.platinumaxe, 1, 1, 15),
			new StuffList(Items.platinumshovel, 1, 1, 15),
			new StuffList(Items.platinumhoe, 1, 1, 15),
			new StuffList(Items.platinumpickaxe, 1, 1, 15),
			new StuffList(Items.platinumsword, 1, 1, 15),
			new StuffList(Items.furball, 1, 16, 65),
			new StuffList(Items.furball, 1, 16, 65),
			new StuffList(Items.furball, 1, 16, 65),
			new StuffList(Items.furball, 1, 16, 65),
			new StuffList(Items.instability, 1, 16, 25),
			new StuffList(Items.instabilitylarge, 1, 16, 20),
			new StuffList(Items.instabilityhuge, 1, 16, 15),
			new StuffList(Items.corn, 1, 16, 45),
			new StuffList(Items.corn, 1, 16, 45),
			new StuffList(Items.radish, 1, 16, 45),
			new StuffList(Items.radish, 1, 16, 45),					
			new StuffList(Items.moosemeat, 1, 16, 45),
			new StuffList(Items.moosemeat_cooked, 1, 16, 35),
			new StuffList(Items.moosebone, 1, 16, 45),
			new StuffList(Items.goosemeat, 1, 16, 45),
			new StuffList(Items.goosemeat_cooked, 1, 16, 35),
			new StuffList(Items.feather, 1, 16, 45),
			new StuffList(Items.ostrichmeat, 1, 16, 35),
			new StuffList(Items.ostrichmeat_cooked, 1, 16, 45),
			new StuffList(Items.silverhelmet, 1, 1, 20),
			new StuffList(Items.silverchestplate, 1, 1, 20),
			new StuffList(Items.silverleggings, 1, 1, 20),
			new StuffList(Items.silverboots, 1, 1, 20),
			new StuffList(Items.platinumhelmet, 1, 1, 15),
			new StuffList(Items.platinumchestplate, 1, 1, 15),
			new StuffList(Items.platinumleggings, 1, 1, 15),
			new StuffList(Items.platinumboots, 1, 1, 15),
			new StuffList(Items.copperhelmet, 1, 1, 20),
			new StuffList(Items.copperchestplate, 1, 1, 20),
			new StuffList(Items.copperleggings, 1, 1, 20),
			new StuffList(Items.copperboots, 1, 1, 20),
			new StuffList(Items.tinhelmet, 1, 1, 20),
			new StuffList(Items.tinchestplate, 1, 1, 20),
			new StuffList(Items.tinleggings, 1, 1, 20),
			new StuffList(Items.tinboots, 1, 1, 20),
			new StuffList(Items.firestick, 1, 1, 45),
			new StuffList(Items.deadbug, 1, 16, 45),
			new StuffList(Items.peachseed, 1, 16, 45),
			new StuffList(Items.appleseed, 1, 16, 45),
			new StuffList(Items.cherryseed, 1, 16, 45),
			new StuffList(Items.dark, 1, 16, 25),
			new StuffList(Items.light, 1, 16, 25),
			new StuffList(Items.bottle, 1, 16, 45),
			new StuffList(Items.experiencebottle, 1, 16, 45),
			new StuffList(Items.bread, 1, 16, 45),
			new StuffList(Items.rice, 1, 16, 45),
			new StuffList(Items.scubatanks, 1, 1, 55),
			new StuffList(Items.scubamask, 1, 1, 55),
			
			new StuffList(Items.eggbutterfly, 1, 6, 15),
			new StuffList(Items.eggghost, 1, 6, 15),
			new StuffList(Items.eggghostskelly, 1, 6, 15),
			new StuffList(Items.eggrat, 1, 6, 15),
			new StuffList(Items.eggcockroach, 1, 6, 15),
			new StuffList(Items.eggmoose, 1, 6, 15),
			new StuffList(Items.egggoose, 1, 6, 15),
			new StuffList(Items.egggosling, 1, 6, 15),
			new StuffList(Items.eggostrich, 1, 6, 15),
			new StuffList(Items.eggsparklemuffin, 1, 6, 15),
			new StuffList(Items.eggskeletorus, 1, 6, 15),
			new StuffList(Items.egganteater, 1, 6, 15),
			new StuffList(Items.eggvampire, 1, 6, 15),
			new StuffList(Items.eggthecount, 1, 6, 15),
			new StuffList(Items.eggwerewolf, 1, 6, 15),
			new StuffList(Items.eggvixen, 1, 6, 15),
			
			new StuffList(Items.frog_speed1, 1, 6, 15),
			new StuffList(Items.frog_slowness1, 1, 6, 15),
			new StuffList(Items.frog_speed2, 1, 6, 15),
			new StuffList(Items.frog_slowness2, 1, 6, 15),
			new StuffList(Items.frog_speed3, 1, 6, 15),
			new StuffList(Items.frog_slowness3, 1, 6, 15),
			new StuffList(Items.frog_strength1, 1, 6, 15),
			new StuffList(Items.frog_weakness1, 1, 6, 15),
			new StuffList(Items.frog_strength2, 1, 6, 15),
			new StuffList(Items.frog_weakness2, 1, 6, 15),
			new StuffList(Items.frog_strength3, 1, 6, 15),
			new StuffList(Items.frog_weakness3, 1, 6, 15),		
			new StuffList(Items.frog_regen1, 1, 6, 15),
			new StuffList(Items.frog_poison1, 1, 6, 15),
			new StuffList(Items.frog_regen2, 1, 6, 15),
			new StuffList(Items.frog_poison2, 1, 6, 15),
			new StuffList(Items.frog_regen3, 1, 6, 15),
			new StuffList(Items.frog_poison3, 1, 6, 15),
			new StuffList(Items.frog_confusion1, 1, 6, 15),
			new StuffList(Items.frog_confusion2, 1, 6, 15),
			new StuffList(Items.frog_confusion3, 1, 6, 15),
			new StuffList(Items.frog_morph1, 1, 6, 15),
			new StuffList(Items.frog_morph2, 1, 6, 15),
			new StuffList(Items.frog_morph3, 1, 6, 15),
			
			new StuffList(Blocks.stand, 5, 10, 25),
			new StuffList(Blocks.stone, 1, 5, 10),
			new StuffList(Blocks.greystone, 1, 5, 10),
			new StuffList(Blocks.lightstick, 1, 16, 60),
			new StuffList(Blocks.darkstick, 1, 16, 10),
			new StuffList(Blocks.blocklight, 1, 1, 5),
			new StuffList(Blocks.blockdark, 1, 1, 5),
			new StuffList(Blocks.blocksilver, 1, 1, 5),
			new StuffList(Blocks.blockcopper, 1, 1, 15),
			new StuffList(Blocks.blocktin, 1, 1, 15),
			new StuffList(Blocks.blockplatinum, 1, 1, 5),
			new StuffList(Blocks.oresilver, 5, 10, 15),
			new StuffList(Blocks.oreplatinum, 5, 10, 10),
			new StuffList(Blocks.log, 5, 10, 25),
			new StuffList(Blocks.willowlog, 5, 10, 25),
			new StuffList(Blocks.redwoodlog, 5, 10, 25),
			new StuffList(Blocks.stopblock, 1, 5, 10)
			};
	
	public static StuffList[] martian_things = new StuffList[] { 
		//Block or Item, min, max, %chance
		new StuffList(Items.coinsilver, 5, 64, 75),
		new StuffList(Items.coingold, 3, 32, 65),			
		new StuffList(Items.coinplatinum, 1, 16, 55),
		new StuffList(Items.diamond, 1, 16, 5),
		new StuffList(Items.emerald, 1, 32, 5),
		new StuffList(Items.bloodstone, 1, 5, 5),
		new StuffList(Items.sunstone, 1, 5, 5),
		
		new StuffList(Items.frog_speed3, 1, 6, 15),
		new StuffList(Items.frog_slowness3, 1, 6, 15),
		new StuffList(Items.frog_strength3, 1, 6, 15),
		new StuffList(Items.frog_weakness3, 1, 6, 15),		
		new StuffList(Items.frog_regen3, 1, 6, 15),
		new StuffList(Items.frog_poison3, 1, 6, 15),
		new StuffList(Items.frog_confusion3, 1, 6, 15),
		new StuffList(Items.frog_morph3, 1, 6, 15),
		
		new StuffList(Items.instabilitylarge, 1, 16, 15),
		new StuffList(Items.instabilityhuge, 1, 8, 15),
		
		new StuffList(Items.scrollaccuracyIV, 1, 8, 15),
		new StuffList(Items.scrolldamageIV, 1, 8, 15),
		new StuffList(Items.scrolldurabilityIV, 1, 8, 15),
		new StuffList(Items.scrollreachIV, 1, 8, 15),
		new StuffList(Items.scrollspamIV, 1, 8, 15),
		
		new StuffList(Items.scrollaccuracyV, 1, 4, 15),
		new StuffList(Items.scrolldamageV, 1, 4, 15),
		new StuffList(Items.scrolldurabilityV, 1, 4, 15),
		new StuffList(Items.scrollreachV, 1, 4, 15),
		new StuffList(Items.scrollspamV, 1, 4, 15),
		
		new StuffList(Items.scrollharm, 1, 16, 15),
		new StuffList(Items.scrollheal, 1, 16, 15),
		
		new StuffList(Items.drone, 1, 4, 15),
		new StuffList(Items.drone_motor, 1, 8, 15),
		new StuffList(Items.drone_body, 1, 8, 15),
		new StuffList(Items.drone_camera, 1, 8, 15),
		
	};
	
	private Trees tr = null;
	
	//This is the best and easiest place to add decorations to the given chunk.
	//It is ONLY run on the server when creating new chunks.
	//NOT dimension or biome specific. YOU need to check.
	//just make a class that inherits from WorldDecorator, and override this function!
	//Oh, and then register your class with WorldDecorators...
	//That's it! You don't even need to call super() because this one registers itself.
	public void decorate(World world, int dimension, Biome b, int chunkx, int chunkz){
		if(!world.isServer)return;
		if(b == null)return;
		
		if(tr == null)tr = new Trees();
		
		//First things first... we need to finish the large terrain structures!
		//Let's add some caves!!!
		if(b.should_add_caves){
			double startx, starty, startz;
			starty = 10f + world.rand.nextFloat()*60;
			startx = world.rand.nextFloat()*16;
			startz = world.rand.nextFloat()*16;			
			startx += (chunkx << 4);
			startz += (chunkz << 4);
			add_cave(world, dimension, chunkx, chunkz, 0, 4, 100, startx, starty, startz);
		}
		
		//And now for some dungeons!
		if(b.should_add_dungeons)add_dungeon(world, dimension, chunkx, chunkz);
		
		//default decorator! Woohoo!
		//see if we can put down a few cockroach nests...
		//ALL dimensions, hence no checks for dimension name
		if(b.should_add_roaches && world.rand.nextInt(40) == 1){
			int howmany = 1 + world.rand.nextInt(3);
			for(int m = 0;m<howmany;m++){
				int ix = (chunkx<<4)+world.rand.nextInt(16);
				int iz = (chunkz<<4)+world.rand.nextInt(16);
				int iy;
				for(iy=150;iy>60;iy--){
					if(world.getblock(dimension, ix, iy, iz) == Blocks.grassblock.blockID){
						if(world.getblock(dimension, ix, iy+1, iz) == 0){
							world.setblock(dimension, ix, iy, iz, Blocks.roachnest.blockID);
							break;
						}
					}
				}				
			}
		}
		
		//and a few butterfly plants!
		if(b.should_add_butterflies && world.rand.nextInt(50) == 1){
			int howmany = 1 + world.rand.nextInt(3);
			for(int m = 0;m<howmany;m++){
				int ix = (chunkx<<4)+world.rand.nextInt(16);
				int iz = (chunkz<<4)+world.rand.nextInt(16);
				int iy;
				for(iy=150;iy>70;iy--){
					if(world.getblock(dimension, ix, iy, iz) == Blocks.grassblock.blockID){
						if(world.getblock(dimension, ix, iy+1, iz) == 0){
							world.setblock(dimension, ix, iy+1, iz, Blocks.butterfly_plant.blockID);
							break;
						}
					}
				}				
			}
		}
		
		//Red and yellow flowers go everywhere...
		if(b.should_add_flowers && world.rand.nextInt(12) == 1){
			int howmany = 1 + world.rand.nextInt(5);
			for(int m = 0;m<howmany;m++){
				int ix = (chunkx<<4)+world.rand.nextInt(16);
				int iz = (chunkz<<4)+world.rand.nextInt(16);
				int iy;
				for(iy=165;iy>60;iy--){
					if(world.getblock(dimension, ix, iy, iz) == Blocks.grassblock.blockID){
						if(world.getblock(dimension, ix, iy+1, iz) == 0){
							world.setblock(dimension, ix, iy+1, iz, Blocks.flower_red.blockID);
							break;
						}
					}
				}				
			}
		}
		
		if(b.should_add_flowers && world.rand.nextInt(12) == 1){
			int howmany = 1 + world.rand.nextInt(6);
			for(int m = 0;m<howmany;m++){
				int ix = (chunkx<<4)+world.rand.nextInt(16);
				int iz = (chunkz<<4)+world.rand.nextInt(16);
				int iy;
				for(iy=180;iy>60;iy--){
					if(world.getblock(dimension, ix, iy, iz) == Blocks.grassblock.blockID){
						if(world.getblock(dimension, ix, iy+1, iz) == 0){
							world.setblock(dimension, ix, iy+1, iz, Blocks.flower_yellow.blockID);
							break;
						}
					}
				}				
			}
		}
		
		
		if(b.should_add_waterplants && world.rand.nextInt(4) == 1){
			int howmany = 5 + world.rand.nextInt(12);
			int which = world.rand.nextInt(16);
			int planttype = Blocks.reefgrass.blockID;
			
			switch(which){
			case 0: planttype = Blocks.redcoral.blockID; break;
			case 1: planttype = Blocks.reefgrass.blockID; break;
			case 2: planttype = Blocks.redreefgrass.blockID; break;
			case 3: planttype = Blocks.yellowcoral.blockID; break;
			case 4: planttype = Blocks.bluecoral.blockID; break;
			case 5: planttype = Blocks.firecoral.blockID; break;
			case 6: planttype = Blocks.redfancoral.blockID; break;
			case 7: planttype = Blocks.blackfancoral.blockID; break;
			case 8: planttype = Blocks.seaweed.blockID; break;
			case 9: planttype = Blocks.yellowseaweed.blockID; break;
			case 10: planttype = Blocks.brownseaweed.blockID; break;
			case 11: planttype = Blocks.kelp.blockID; break;
			default: planttype = Blocks.reefgrass.blockID; break;
			}
			
			if(planttype == Blocks.reefgrass.blockID)howmany += 16;
			if(planttype == Blocks.redreefgrass.blockID)howmany += 10;
			
			for(int m = 0;m<howmany;m++){
				int ix = (chunkx<<4)+world.rand.nextInt(16);
				int iz = (chunkz<<4)+world.rand.nextInt(16);
				int iy;
				int bid = 0;
				for(iy=80;iy>10;iy--){
					bid = world.getblock(dimension, ix, iy, iz);
					if(bid == 0)continue;
					if(bid == Blocks.waterstatic.blockID)continue;
					if(bid == Blocks.stone.blockID || bid == Blocks.sand.blockID){
						if(world.getblock(dimension, ix, iy+1, iz) == Blocks.waterstatic.blockID){
							if(world.getblock(dimension, ix, iy+2, iz) == Blocks.waterstatic.blockID){
								if(world.rand.nextInt(200) == 5){
									world.setblocknonotify(dimension, ix, iy+1, iz, Blocks.fishnursery.blockID);
								}else{
									world.setblocknonotify(dimension, ix, iy+1, iz, planttype);
								}
							}
						}
					}
					break;
				}				
			}
		}else{
			if(b.should_add_waterplants && world.rand.nextInt(10) == 1){
				int howmany = 5 + world.rand.nextInt(6);
				int which = world.rand.nextInt(6);
				int planttype = Blocks.tallseaweed.blockID;
				
				switch(which){
				case 0: planttype = Blocks.tallseaweed.blockID; break;
				case 1: planttype = Blocks.browntallseaweed.blockID; break;
				case 2: planttype = Blocks.yellowtallseaweed.blockID; break;
				case 3: planttype = Blocks.tallkelp.blockID; break;
				default: planttype = Blocks.tallseaweed.blockID; break;
				}
				for(int m = 0;m<howmany;m++){
					int ix = (chunkx<<4)+world.rand.nextInt(16);
					int iz = (chunkz<<4)+world.rand.nextInt(16);
					int iy;
					int bid = 0;
					for(iy=80;iy>10;iy--){
						bid = world.getblock(dimension, ix, iy, iz);
						if(bid == 0)continue;
						if(bid == Blocks.waterstatic.blockID)continue;
						if(bid == Blocks.stone.blockID || bid == Blocks.sand.blockID){
							if(world.getblock(dimension, ix, iy+1, iz) == Blocks.waterstatic.blockID){
								if(world.getblock(dimension, ix, iy+2, iz) == Blocks.waterstatic.blockID){
									while(world.getblock(dimension, ix, iy+2, iz) == Blocks.waterstatic.blockID){
										world.setblocknonotify(dimension, ix, iy+1, iz, planttype);
										iy++;
										if(world.rand.nextInt(50) == 1)break; //add some randomness
									}
								}
							}
						}
						break;
					}				
				}
			}
		}
		
		
		
		
		if(Dimensions.getName(dimension).equals("DangerZone: Overworld Dimension")){
			int howmany = 0;
			int what = world.rand.nextInt(6);
			howmany = world.rand.nextInt(4);

			if(what != 0)howmany *= 2;
			
			if(b.uniquename.equals("DangerZone:Overworld Tall Forest")){
				what = 0;	
			}else if(b.uniquename.equals("DangerZone:Overworld Tall Willow Forest")){
				what = 1;
			}else if(b.uniquename.equals("DangerZone:Overworld Scraggly Forest")){
				what = 2;
			}else{
				//Make them at least a little scarce, otherwise they are everywhere!
				if(world.rand.nextInt(5) != 0)howmany = 0;
			}
			int cx = (chunkx<<4);
			int cz = (chunkz<<4);

			for(int i = 0; i < howmany; i++) {
				int posX = cx + world.rand.nextInt(16);
				int posZ = cz + world.rand.nextInt(16);    
				for(int posY = 128; posY > 40; posY--) //Start high and search down until we find a grass/dirt block.
				{
					//Must be in air (empty block), and on a grass block
					if(world.getblock(dimension, posX, posY, posZ) == 0 && world.getblock(dimension, posX, posY-1, posZ) == Blocks.grassblock.blockID)
					{					
						if(what == 0){
							tr.TallWoodTree(world, dimension, posX, posY, posZ);
						}else if(what == 1){
							tr.TallWillowTree(world, dimension, posX, posY, posZ);
						}else{
							tr.ScragglyRedwoodTree(world, dimension, posX, posY, posZ);
						}
						break;
					}	     	
				}
			}
		}
		
		//plant some corn in the overworld
		if(Dimensions.getName(dimension).equals("DangerZone: Overworld Dimension") || Dimensions.getName(dimension).equals("DangerZone: Rugged Hills Dimension")){
			if(world.rand.nextInt(64) == 1){
				int howmany = 1 + world.rand.nextInt(5);
				for(int m = 0;m<howmany;m++){
					int ix = (chunkx<<4)+world.rand.nextInt(16);
					int iz = (chunkz<<4)+world.rand.nextInt(16);
					int iy;
					for(iy=100;iy>50;iy--){
						if(world.getblock(dimension, ix, iy, iz) == Blocks.grassblock.blockID){
							if(world.getblock(dimension, ix, iy+1, iz) == 0){
																
								int howhigh = 2 + world.rand.nextInt(4);
								for(int i=0;i<howhigh;i++){
									if(world.getblock(dimension, ix, iy+i+2, iz) != 0){
										howhigh = i;
										break;
									}
									world.setblock(dimension, ix, iy+i+1, iz, world.rand.nextBoolean()?Blocks.corn_plant2.blockID:Blocks.corn_plant3.blockID);
								}							
								world.setblock(dimension, ix, iy, iz, Blocks.dirt.blockID);
								world.setblock(dimension, ix, iy+howhigh+1, iz, Blocks.corn_plant.blockID);
								break;
							}
						}
					}				
				}
			}
		}
		
		if(Dimensions.getName(dimension).equals("DangerZone: Big Round Tree Dimension")){
			
			if(b.uniquename.equals("DangerZone:Big Trees")){
				if(!tr.addGenericTrees(world, dimension, chunkx<<4, chunkz<<4)){
					tr.addTrees(world, dimension, chunkx<<4, chunkz<<4);
				}
			}
			
			if(b.uniquename.equals("DangerZone:Big Trees Flower")){
				if(!tr.addBigRoundLightTree(world, dimension, chunkx<<4, chunkz<<4)){
					tr.addFlowerTrees(world, dimension, chunkx<<4, chunkz<<4);
				}
			}
			
			if(b.uniquename.equals("DangerZone:Big Trees Flower Two")){
				if(!tr.addBigRoundLightTree(world, dimension, chunkx<<4, chunkz<<4)){
					tr.addFlowerTwoTrees(world, dimension, chunkx<<4, chunkz<<4);
					addMartianHabitat(world, dimension, chunkx<<4, chunkz<<4);
				}
			}
			
			if(world.rand.nextInt(7) == 1){
				int howmany = 2 + world.rand.nextInt(5);
				for(int m = 0;m<howmany;m++){
					int ix = (chunkx<<4)+world.rand.nextInt(16);
					int iz = (chunkz<<4)+world.rand.nextInt(16);
					int iy;
					for(iy=100;iy>50;iy--){
						if(world.getblock(dimension, ix, iy, iz) == Blocks.grassblock.blockID){
							if(world.getblock(dimension, ix, iy+1, iz) == 0){
								world.setblock(dimension, ix, iy+1, iz, Blocks.flower_pink.blockID);
								break;
							}
						}
					}				
				}
			}
			if(world.rand.nextInt(64) == 1){
				int howmany = 1 + world.rand.nextInt(5);
				for(int m = 0;m<howmany;m++){
					int ix = (chunkx<<4)+world.rand.nextInt(16);
					int iz = (chunkz<<4)+world.rand.nextInt(16);
					int iy;
					for(iy=100;iy>50;iy--){
						if(world.getblock(dimension, ix, iy, iz) == Blocks.grassblock.blockID){
							if(world.getblock(dimension, ix, iy+1, iz) == 0){
								world.setblock(dimension, ix, iy, iz, Blocks.dirt.blockID);
								if(world.rand.nextInt(3) == 0){
									world.setblock(dimension, ix, iy+1, iz, Blocks.radish_plant.blockID);
								}else{
									world.setblock(dimension, ix, iy+1, iz, Blocks.radish_plant3.blockID);	
								}
								break;
							}
						}
					}				
				}
			}
		}
		
		if(Dimensions.getName(dimension).equals("DangerZone: Pathway Dimension")){
			
			if(b.uniquename.equals("DangerZone:Pathway")){
				tr.addGenericTrees(world, dimension, chunkx<<4, chunkz<<4);
				
				//Make some fruit trees!
				if(world.rand.nextInt(3) == 0){

					int howmany = 0;
					int what = world.rand.nextInt(3);
					howmany = world.rand.nextInt(6);

					for(int i = 0; i < howmany; i++) {
						int posX = (chunkx<<4) + world.rand.nextInt(16);
						int posZ = (chunkz<<4) + world.rand.nextInt(16);    
						for(int posY = 90; posY > 50; posY--) //Start high and search down until we find a grass/dirt block.
						{
							//Must be in air (empty block), and on a grass block
							if(world.getblock(dimension, posX, posY, posZ) == 0 && world.getblock(dimension, posX, posY-1, posZ) == Blocks.grassblock.blockID)
							{					
								if(what == 0){
									tr.makeFruitTree(world, dimension, posX, posY-1, posZ, Blocks.cherryleaves.blockID);
								}else if(what == 1){
									tr.makeFruitTree(world, dimension, posX, posY-1, posZ, Blocks.appleleaves.blockID);
								}
								break;
							}	     	
						}
					}
				}
				
			}
			
			if(b.uniquename.equals("DangerZone:Pathway Forest")){			
				tr.addForestTrees(world, dimension, chunkx<<4, chunkz<<4);	
			}
			
			if(b.uniquename.equals("DangerZone:Pathway Umbrella Forest")){		
				tr.addUmbrellaTrees(world, dimension, chunkx<<4, chunkz<<4);
			}
			
			if(b.uniquename.equals("DangerZone:Pathway Bulb Forest")){		
				tr.addBulbTrees(world, dimension, chunkx<<4, chunkz<<4);
			}
			
			if(b.uniquename.equals("DangerZone:Pathway Loop Forest")){		
				tr.addlooplowspiralTree(world, dimension, chunkx<<4, chunkz<<4);
			}
			
			if(b.uniquename.equals("DangerZone:Pathway Loop Forest II")){		
				tr.addloopTree(world, dimension, chunkx<<4, chunkz<<4);
			}
			
			
			
			if(world.rand.nextInt(8) == 1){
				int howmany = 2 + world.rand.nextInt(6);
				for(int m = 0;m<howmany;m++){
					int ix = (chunkx<<4)+world.rand.nextInt(16);
					int iz = (chunkz<<4)+world.rand.nextInt(16);
					int iy;
					for(iy=100;iy>50;iy--){
						if(world.getblock(dimension, ix, iy, iz) == Blocks.grassblock.blockID){
							if(world.getblock(dimension, ix, iy+1, iz) == 0){
								world.setblock(dimension, ix, iy+1, iz, Blocks.flower_purple.blockID);
								break;
							}
						}
					}				
				}
			}
			if(world.rand.nextInt(64) == 1){
				int howmany = 1 + world.rand.nextInt(5);
				for(int m = 0;m<howmany;m++){
					int ix = (chunkx<<4)+world.rand.nextInt(16);
					int iz = (chunkz<<4)+world.rand.nextInt(16);
					int iy;
					for(iy=100;iy>50;iy--){
						if(world.getblock(dimension, ix, iy, iz) == Blocks.grassblock.blockID){
							if(world.getblock(dimension, ix, iy+1, iz) == 0){
								world.setblock(dimension, ix, iy, iz, Blocks.dirt.blockID);
								
								if(world.rand.nextInt(3) != 0){
									world.setblock(dimension, ix, iy+1, iz, Blocks.rice_plant.blockID);
								}else{
									world.setblock(dimension, ix, iy+1, iz, Blocks.rice_plant3.blockID);	
								}
								break;
							}
						}
					}				
				}
			}
			
		}	
		
		if(Dimensions.getName(dimension).equals("DangerZone: Rugged Hills Dimension")){
			if(world.rand.nextInt(18) == 1){
				int howmany = 2 + world.rand.nextInt(6);
				for(int m = 0;m<howmany;m++){
					int ix = (chunkx<<4)+world.rand.nextInt(16);
					int iz = (chunkz<<4)+world.rand.nextInt(16);
					int iy;
					for(iy=100;iy>50;iy--){
						if(world.getblock(dimension, ix, iy, iz) == Blocks.grassblock.blockID){
							if(world.getblock(dimension, ix, iy+1, iz) == 0){
								world.setblock(dimension, ix, iy+1, iz, Blocks.flower_purple.blockID);
								break;
							}
						}
					}				
				}
			}
			if(world.rand.nextInt(100) == 1){
				int howmany = 1 + world.rand.nextInt(5);
				for(int m = 0;m<howmany;m++){
					int ix = (chunkx<<4)+world.rand.nextInt(16);
					int iz = (chunkz<<4)+world.rand.nextInt(16);
					int iy;
					for(iy=100;iy>50;iy--){
						if(world.getblock(dimension, ix, iy, iz) == Blocks.grassblock.blockID){
							if(world.getblock(dimension, ix, iy+1, iz) == 0){
								world.setblock(dimension, ix, iy, iz, Blocks.dirt.blockID);
								if(world.rand.nextInt(3) != 0){
									world.setblock(dimension, ix, iy+1, iz, Blocks.rice_plant.blockID);
								}else{
									world.setblock(dimension, ix, iy+1, iz, Blocks.rice_plant3.blockID);	
								}
								break;
							}
						}
					}				
				}
			}

			int howoften = 30;
			int howmany = 1;
			
			if(b.uniquename.equals("DangerZone:Rugged Hills")){

				//Make some fruit trees!
				if(world.rand.nextInt(howoften) == 0){

					int what = world.rand.nextInt(3);

					for(int i = 0; i < howmany; i++) {
						int posX = (chunkx<<4) + world.rand.nextInt(16);
						int posZ = (chunkz<<4) + world.rand.nextInt(16);    
						for(int posY = 90; posY > 50; posY--) //Start high and search down until we find a grass/dirt block.
						{
							//Must be in air (empty block), and on a grass block
							if(world.getblock(dimension, posX, posY, posZ) == 0 && world.getblock(dimension, posX, posY-1, posZ) == Blocks.grassblock.blockID)
							{					
								if(what == 0){
									tr.makeFruitTree(world, dimension, posX, posY-1, posZ, Blocks.peachleaves.blockID);
								}else if(what == 1){
									tr.makeFruitTree(world, dimension, posX, posY-1, posZ, Blocks.appleleaves.blockID);
								}
								break;
							}	     	
						}
					}
				}
			}
			
			if(b.uniquename.equals("DangerZone:Rugged Plains")){
				howoften = 8;
				howmany = 1+world.rand.nextInt(3);

				//Make some fruit trees!
				if(world.rand.nextInt(howoften) == 0){

					int what = world.rand.nextInt(3);

					for(int i = 0; i < howmany; i++) {
						int posX = (chunkx<<4) + world.rand.nextInt(16);
						int posZ = (chunkz<<4) + world.rand.nextInt(16);    
						for(int posY = 90; posY > 50; posY--) //Start high and search down until we find a grass/dirt block.
						{
							//Must be in air (empty block), and on a grass block
							if(world.getblock(dimension, posX, posY, posZ) == 0 && world.getblock(dimension, posX, posY-1, posZ) == Blocks.grassblock.blockID)
							{					
								if(what == 0){
									tr.makeFruitTree(world, dimension, posX, posY-1, posZ, Blocks.peachleaves.blockID);
								}else if(what == 1){
									tr.makeFruitTree(world, dimension, posX, posY-1, posZ, Blocks.appleleaves.blockID);
								}
								break;
							}	     	
						}
					}
				}
			}
			
			if(b.uniquename.equals("DangerZone:Rugged Plains II")){
				howoften = 3;
				howmany = 1;
				
				if(world.rand.nextInt(howoften) == 0){

					int what = world.rand.nextInt(3);

					for(int i = 0; i < howmany; i++) {
						int posX = (chunkx<<4) + world.rand.nextInt(16);
						int posZ = (chunkz<<4) + world.rand.nextInt(16);    
						for(int posY = 90; posY > 50; posY--) //Start high and search down until we find a grass/dirt block.
						{
							//Must be in air (empty block), and on a grass block
							if(world.getblock(dimension, posX, posY, posZ) == 0 && world.getblock(dimension, posX, posY-1, posZ) == Blocks.grassblock.blockID)
							{					
								if(what == 0){
									tr.bowlTree(world, dimension, posX, posY-1, posZ);
								}else if(what == 1){
									tr.doublebowlTree(world, dimension, posX, posY-1, posZ);
								}
								break;
							}	     	
						}
					}
				}
			}
			
			if(b.uniquename.equals("DangerZone:Rugged Plains Desert")){	
					tr.addScrubTrees(world, dimension, (chunkx<<4), (chunkz<<4));
			}
			
		}
		
		if(Dimensions.getName(dimension).equals("DangerZone: Sky Islands Dimension")){
			int howmany = 0;
			int howoften = 8;

			howmany = world.rand.nextInt(3);

			//Make them at least a little scarce, otherwise they are everywhere!
			if(world.rand.nextInt(howoften) == 0){
				for(int i = 0; i < howmany; i++) {
					int posX = (chunkx<<4) + world.rand.nextInt(16);
					int posZ = (chunkz<<4) + world.rand.nextInt(16);    
					for(int posY = 220; posY > 100; posY--) //Start high and search down until we find a grass/dirt block.
					{
						//Must be in air (empty block), and on a grass block
						if(world.getblock(dimension, posX, posY, posZ) == 0 && world.getblock(dimension, posX, posY-1, posZ) == Blocks.grassblock.blockID)
						{					
							tr.vaseTree(world, dimension, posX, posY-1, posZ);
							break;
						}	     	
					}
				}
			}

		}
	}
	
	/*
	 * Generates a cave. Der....
	 */
	public void add_cave(World world, int dimension, int chunkx, int chunkz, int recur, float mxw, float mxlen, double startx, double starty, double startz){
		//double startx, starty, startz;
		double dxz, dy;
		double ddxz, ddy;
		double width;
		double length;
		double currentwidth;
		double currad;
		double curx, cury, curz;
		double tx, tz;
		double ty;
		double dirang;
		double tt;
		double dirax, diraz;
		int da;
		int bid;
		
		if(!DangerZone.generatecaves)return;
		if(recur > 2)return;
		if(mxw < 0)return;
		if(mxlen < 10)return;
		
		//caves are somewhat rare, but when they do occur, they tend to be clumpy and awesome...
		if(recur == 0){
			if(world.rand.nextInt(32+((chunkx)&0x3f)*8 + ((chunkz)&0x3f)*8) != 0)return; //clumpy, but not too many or it overloads the graphics memory...
		}
		
		dxz = world.rand.nextFloat()*360f;
		dy = (world.rand.nextFloat()-world.rand.nextFloat())*25f;
		ddxz = world.rand.nextFloat()-world.rand.nextFloat();
		ddy = world.rand.nextFloat()-world.rand.nextFloat();
		ddxz *= 2;
		width = 2 + world.rand.nextFloat()*mxw;
		length = mxlen + world.rand.nextFloat()*mxlen;
		curx = startx;
		cury = starty;
		curz = startz;
		for(int i = 0;i<length;i++){
			if(cury > 35){
				if(dy>0)dy=-dy;
				if(ddy>0)ddy=-ddy;
			}
			if(cury < 0){
				if(dy<0)dy=-dy;
				if(ddy<0)ddy=-ddy;
			}
			
			if(world.rand.nextInt(25) == 1){
				add_cave(world, dimension, chunkx, chunkz, recur+1, (float)(mxw-1), (float)(length/2), curx, cury, curz);
			}
			currentwidth = (float) Math.sin((float)i/length*Math.PI)*width;
			if(currentwidth > 1){
				da = 10;
				if(currentwidth < 4)da = 15;
				if(currentwidth > 6)da = 5;
				dirang = (float) Math.toRadians(dxz-90);
				dirax = (float) Math.cos(dirang);
				diraz = (float) Math.sin(dirang);
				for(int j=0;j<360;j+=da){
					currad = 0;				
					ty = (float) Math.sin(Math.toRadians(j));
					tt = (float) Math.cos(Math.toRadians(j));
					tx = (float) (dirax*tt);
					tz = (float) (diraz*tt);				
					while(currad < currentwidth){
						currad += 0.20f;
						if((int)(cury + ty*currad) > 0){
							bid = world.getblock(dimension, (int)(curx + tx*currad), (int)(cury + ty*currad), (int)(curz + tz*currad));
							if(Blocks.isSolid(bid))world.setblocknonotify(dimension, (int)(curx + tx*currad), (int)(cury + ty*currad), (int)(curz + tz*currad), 0);
						}
					}				
				}
			}
			curx += Math.cos(Math.toRadians(dxz));
			curz += Math.sin(Math.toRadians(dxz));
			dxz += ddxz;
			cury += Math.sin(Math.toRadians(dy));
			dy += ddy;
			//maybe do some direction changes...
			if(world.rand.nextInt(20) == 1){
				dy = (world.rand.nextFloat()-world.rand.nextFloat())*25f;
				ddy = world.rand.nextFloat()-world.rand.nextFloat();
			}
			if(world.rand.nextInt(50) == 1){
				dxz += world.rand.nextFloat()*45f;
				ddxz = world.rand.nextFloat()-world.rand.nextFloat();
				ddxz *= 2;
			}
		}
		
		//Give up some time. We just hammered things...
		Thread.yield();
	}
	
	public void add_dungeon(World world, int dimension, int chunkx, int chunkz){
		if(world.rand.nextInt(64) != 0)return;
		int xoff, yoff, zoff;
		int x = chunkx << 4;
		int z = chunkz << 4;
		int i, j, k;
		int bid, type;
		int width = 11;
		xoff = world.rand.nextInt(16);
		zoff = world.rand.nextInt(16);
		yoff = world.rand.nextInt(40) + 5;
		
		//a few basic checks first!
		if(!Blocks.isSolid(world.getblock(dimension, x+xoff, yoff-1, z+zoff)))return;
		if(!Blocks.isSolid(world.getblock(dimension, x+xoff+width, yoff-1, z+zoff)))return;
		if(!Blocks.isSolid(world.getblock(dimension, x+xoff, yoff-1, z+zoff+width)))return;
		if(!Blocks.isSolid(world.getblock(dimension, x+xoff+width, yoff-1, z+zoff+width)))return;
		
		//Let's just go ahead and assume it's a good spot. They are fairly rare...
		for(i=0;i<width;i++){
			for(j=0;j<5;j++){
				for(k=0;k<width;k++){
					bid = 0;
					if(i==0 || i == 10)bid = Blocks.greystone.blockID;
					if(j==0 || j == 4)bid = Blocks.greystone.blockID;
					if(k==0 || k == 10)bid = Blocks.greystone.blockID;
					world.setblocknonotify(dimension, x+xoff+i, yoff+j, z+zoff+k, bid);
				}
			}
		}
		
		//Add a spawner
		type = world.rand.nextInt(4); //0=rat, 1=werewolf, 2=vampire, 3=snarler
		bid = Blocks.ratspawner.blockID;
		if(type == 1)bid = Blocks.werewolfspawner.blockID;
		if(type == 2)bid = Blocks.vampirespawner.blockID;
		if(type == 3)bid = Blocks.snarlerspawner.blockID;
		world.setblock(dimension, x+xoff+5, yoff+1, z+zoff+5, bid);
		
		//Now add a chest and put some things in it
		Utils.add_chest(world, dimension, x+xoff+5, yoff+1, z+zoff+1, things);
		
	}
	
	
	public void addMartianHabitat(World w, int d, int cx, int cz){
		if(w.rand.nextInt(8) != 0)return; //make them rare!
		if((cx&0x3f)!= 0)return;
		if((cz&0x3f)!= 0)return;
		int iy, bid;
		for(iy=100;iy>50;iy--){
			bid = w.getblock(d, cx, iy, cz);
			if(bid == Blocks.stone.blockID){
				w.setblock(d, cx, iy+1, cz, Blocks.lightstick.blockID);
				buildHabitat(w, d, cx, iy, cz);				
				break;
			}else{
				if(bid != 0)break;
			}
		}
		
	}
	
	public void buildHabitat(World w, int d, int cx, int cy, int cz){
		int iy, iz;
		int level = 40 - w.rand.nextInt(20);
		iz = 0;
		for(iy=0;cy-iy>level;iy++){			
			for(int j=0;j<3;j++){
				for(int i=0;i<2;i++){
					w.setblock(d, cx+i, (cy-iy)+j, cz+iz, 0);
				}
			}
			iz++;
		}
		
		buildBigRoom(w, d, cx, level, cz+iz+6, false, false, false, true);
	}
	
	public void buildBigRoom(World w, int d, int cx, int cy, int cz, boolean fxp, boolean fxm, boolean fzp, boolean fzm){
		int width = 6;
		//System.out.printf("built martian big room at %d,  %d, %d\n", cx, cy, cz);
		
		if(w.getblock(d, cx, cy, cz) == Blocks.chest.blockID)return;
		
		for(int i=-width;i<=width;i++){
			for(int k=-width;k<=width;k++){
				for(int j=0;j<3;j++){
					w.setblock(d, cx+i, cy+j, cz+k, 0);
				}
			}
		}
			
		if(!Blocks.isSolid(w.getblock(d, cx, cy-1, cz)))return;
		//Now add a chest and put some things in it
		Utils.add_chest(w, d, cx, cy, cz, martian_things);
		
		for(int i=-2;i<=2;i++){
			for(int k=-2;k<=2;k++){
				if(k==-2||k==2||i==-2||i==2){
					if(!Blocks.isSolid(w.getblock(d, cx+i, cy-1, cz+k)))continue;
					switch(w.rand.nextInt(8)){
					case 0:
						w.setblock(d, cx+i, cy, cz+k, Blocks.workbench.blockID);
						break;
					case 1:
						w.setblock(d, cx+i, cy, cz+k, Blocks.desk.blockID);
						break;
					case 2:
						w.setblock(d, cx+i, cy, cz+k, Blocks.furnace.blockID);
						break;
					default:
						Entity e = w.createEntityByName("DangerZone:Martian", d, cx+i, cy, cz+k);
						if(e != null){
							e.init();
							w.spawnEntityInWorld(e);
						}						
						break;
					}
				}
			}
		}
		
		int dy;
		
		if(!fxp && w.rand.nextBoolean()){
			dy = w.rand.nextBoolean()?0:-1;
			if(w.getblock(d, cx+16, cy+(dy*16), cz)!= 0){
				buildConnection(w, d, cx+width, cy, cz, 1, dy, 0);
			}
		}
		
		if(!fxm && w.rand.nextBoolean()){
			dy = w.rand.nextBoolean()?0:-1;
			if(w.getblock(d, cx-16, cy+(dy*16), cz)!= 0){
				buildConnection(w, d, cx-width, cy, cz, -1, dy, 0);
			}
		}
		
		if(!fzp && w.rand.nextBoolean()){
			dy = w.rand.nextBoolean()?0:-1;
			if(w.getblock(d, cx, cy+(dy*16), cz+16)!= 0){
				buildConnection(w, d, cx, cy, cz+width, 0, dy, 1);
			}
		}
		
		if(!fzm && w.rand.nextBoolean()){
			dy = w.rand.nextBoolean()?0:-1;
			if(w.getblock(d, cx, cy+(dy*16), cz-16)!= 0){
				buildConnection(w, d, cx, cy, cz-width, 0, dy, -1);
			}
		}
		
	}
	
	public void buildConnection(World w, int d, int cx, int cy, int cz, int dx, int dy, int dz){
		int ix, iy, iz;
		int ddx, ddz;
		ix = cx;
		iz = cz;
		iy = cy;
		ddx = ddz = 0;
		if(dx == 0)ddx = 1;
		if(dz == 0)ddz = 1;

		for(int i=0;i<=16;i++){
			ix = cx + (dx*i);			
			iz = cz + (dz*i);
			iy = cy + (dy*i);
			if(iy <= 1)return; //stop at stop blocks!!!
			for(int j=0;j<3;j++){
				for(int k=0;k<2;k++){
					w.setblock(d, ix+(ddx*k), iy+j, iz+(ddz*k), 0);
				}
			}			
		}
		
		boolean fxp, fxm, fzp, fzm;
		fxp = fxm = fzp = fzm = false;
		if(dx > 0)fxm = true;
		if(dx < 0)fxp = true;
		if(dz > 0)fzm = true;
		if(dz < 0)fzp = true;
		
		buildBigRoom(w, d, ix+(dx*6), iy, iz+(dz*6), fxp, fxm, fzp, fzm);
	}

}
