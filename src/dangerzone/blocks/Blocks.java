package dangerzone.blocks;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.WorldRenderer;

import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;
import dangerzone.gui.InventoryMenus;



public class Blocks {
	
	public static Block sapling_tallwood = new BlockSapling("DangerZone:Tall Wood Tree Sapling", "res/blocks/sapling.png");
	public static Block sapling_cherry = new BlockSapling("DangerZone:Cherry Tree Sapling", "res/blocks/sapling_cherry.png");
	public static Block sapling_peach = new BlockSapling("DangerZone:Peach Tree Sapling", "res/blocks/sapling_peach.png");
	public static Block sapling_apple = new BlockSapling("DangerZone:Apple Tree Sapling", "res/blocks/sapling_apple.png");
	public static Block sapling_scragglyredwood = new BlockSapling("DangerZone:Scraggly Redwood Tree Sapling", "res/blocks/sapling_scragglyredwood.png");
	public static Block sapling_scraggly = new BlockSapling("DangerZone:Scraggly Tree Sapling", "res/blocks/sapling_scraggly.png");
	public static Block sapling_bigroundredwood = new BlockSapling("DangerZone:Big Round Redwood Tree Sapling", "res/blocks/sapling_bigroundredwood.png");
	public static Block sapling_bigroundwillow = new BlockSapling("DangerZone:Big Round Willow Tree Sapling", "res/blocks/sapling_bigroundwillow.png");
	public static Block sapling_flower = new BlockSapling("DangerZone:Flower Tree Sapling", "res/blocks/sapling_flower.png");
	public static Block sapling_flowertwo = new BlockSapling("DangerZone:Another Flower Tree Sapling", "res/blocks/sapling_flowertwo.png");
	public static Block sapling_scrub = new BlockSapling("DangerZone:Scrub Tree Sapling", "res/blocks/sapling_scrub.png");
	public static Block sapling_flowernormal = new BlockSapling("DangerZone:Forest Tree Sapling", "res/blocks/sapling_flowernormal.png");
	public static Block sapling_umbrella = new BlockSapling("DangerZone:Umbrella Tree Sapling", "res/blocks/sapling_umbrella.png");
	public static Block sapling_bulb = new BlockSapling("DangerZone:Bulb Tree Sapling", "res/blocks/sapling_bulb.png");
	public static Block sapling_looplowspiral = new BlockSapling("DangerZone:Loopy Spiral Tree Sapling", "res/blocks/sapling_looplowspiral.png");
	public static Block sapling_loop = new BlockSapling("DangerZone:Loop Tree Sapling", "res/blocks/sapling_loop.png");
	public static Block sapling_generic = new BlockSapling("DangerZone:Generic Tree Sapling", "res/blocks/sapling_generic.png");
	public static Block sapling_tallwillow = new BlockSapling("DangerZone:Tall Willow Tree Sapling", "res/blocks/sapling_tallwillow.png");
	public static Block sapling_vase = new BlockSapling("DangerZone:Vase Tree Sapling", "res/blocks/sapling_vase.png");
	public static Block sapling_spiral = new BlockSapling("DangerZone:Spiral Tree Sapling", "res/blocks/sapling_spiral.png");
	public static Block sapling_loopspiral = new BlockSapling("DangerZone:Another Loopy Spiral Tree Sapling", "res/blocks/sapling_loopspiral.png");
	public static Block sapling_bowl = new BlockSapling("DangerZone:Bowl Tree Sapling", "res/blocks/sapling_bowl.png");
	public static Block sapling_doublebowl = new BlockSapling("DangerZone:Double Bowl Tree Sapling", "res/blocks/sapling_doublebowl.png");
	
	public static Block stone = new BlockStone("DangerZone:Stone", "res/blocks/stone.png", 100);
	public static Block stone2 = new BlockStone("DangerZone:Decorative Stone", "res/blocks/stone2.png", 95);
	public static Block stone3 = new BlockStone("DangerZoneI:Decorative Stone", "res/blocks/stone3.png", 90);
	public static Block stone4 = new BlockStone("DangerZoneII:Decorative Stone", "res/blocks/stone4.png", 85);
	public static Block greystone = new BlockStone("DangerZone:Grey Stone", "res/blocks/greystone.png", 120);
	public static Block greystone2 = new BlockStone("DangerZone:Decorative Grey Stone", "res/blocks/greystone2.png", 115);
	public static Block greystone3 = new BlockStone("DangerZoneI:Decorative Grey Stone", "res/blocks/greystone3.png", 110);
	public static Block greystone4 = new BlockStone("DangerZoneII:Decorative Grey Stone", "res/blocks/greystone4.png", 105);
	public static Block whitestone = new BlockStone("DangerZone:White Stone", "res/blocks/whitestone.png", 140);
	public static Block dirt = new BlockDirt("DangerZone:Dirt", "res/blocks/dirt.png");
	public static Block stopblock = new StopBlock("DangerZone:Stop Block", "res/blocks/stopblock.png");
	public static Block hardrock = new StopBlock("DangerZone:Hard Rock", "res/blocks/hardrock.png");
	public static Block firestone = new FireStone("DangerZone:Fire Stone", "res/blocks/firestone.png", 85);
	public static Block redwoodlog = new BlockLog("DangerZone:Redwood Log", "res/blocks/redwood.png", "res/blocks/redwood_top.png");
	public static Block willowlog = new BlockLog("DangerZone:Willow Log", "res/blocks/willowwood.png", "res/blocks/willowwood_top.png");
	public static Block log = new BlockLog("DangerZone:Log", "res/blocks/wood.png", "res/blocks/wood_top.png");
	public static Block lightplywood = new BlockWood("DangerZone:Light Plywood", "res/blocks/lightplywood.png");
	public static Block plywood = new BlockWood("DangerZone:Plywood", "res/blocks/plywood.png");
	public static Block darkplywood = new BlockWood("DangerZone:Dark Plywood", "res/blocks/darkplywood.png");
	public static Block leaves = new BlockLeaves("DangerZone:Leaves", "res/blocks/leaves.png");
	public static Block redwoodleaves = new BlockLeaves("DangerZone:Redwood Leaves", "res/blocks/redwoodleaves.png");
	public static Block redleaves = new BlockLeaves("DangerZone:Red Leaves", "res/blocks/redleaves.png");
	public static Block orangeleaves = new BlockLeaves("DangerZone:Orange Leaves", "res/blocks/orangeleaves.png");
	public static Block yellowleaves = new BlockLeaves("DangerZone:Yellow Leaves", "res/blocks/yellowleaves.png");
	public static Block greenleaves = new BlockLeaves("DangerZone:Green Leaves", "res/blocks/greenleaves.png");
	public static Block willowleaves = new BlockLeaves("DangerZone:Willow Leaves", "res/blocks/willowleaves.png");
	public static Block pineleaves = new BlockLeaves("DangerZone:Pine Leaves", "res/blocks/pineleaves.png");
	public static Block grassblock = new GrassBlock("DangerZone:Grass Block", "res/blocks/grasstop.png", "res/blocks/grassbottom.png", "res/blocks/grassside.png");
	public static Block grass = new Grass("DangerZone:Grass", "res/blocks/grass.png");
	public static Block orecopper = new BlockOre("DangerZone:Copper Ore", "res/blocks/orecopper.png", 50, 1);
	public static Block oretin = new BlockOre("DangerZone:Tin Ore", "res/blocks/oretin.png", 100, 5);
	public static Block oresilver = new BlockOre("DangerZone:Silver Ore", "res/blocks/oresilver.png", 150, 12);
	public static Block oreplatinum = new BlockOre("DangerZone:Platinum Ore", "res/blocks/oreplatinum.png", 200, 16);
	public static Block orediamond = new BlockOreDiamond("DangerZone:Diamond Ore", "res/blocks/orediamond.png", 225, 16);
	public static Block oreemerald = new BlockOreEmerald("DangerZone:Emerald Ore", "res/blocks/oreemerald.png", 150, 12);
	public static Block orebloodstone = new BlockOreBloodstone("DangerZone:Bloodstone Ore", "res/blocks/orebloodstone.png", 125, 12);
	public static Block oresunstone = new BlockOreSunstone("DangerZone:Sunstone Ore", "res/blocks/oresunstone.png", 125, 12);
	public static Block workbench = new BlockWorkBench("DangerZone:Workbench");
	public static Block furnace = new BlockFurnace("DangerZone:Furnace");
	public static Block furnaceOn = new BlockFurnaceOn("DangerZone:FurnaceOn");
	public static Block chest = new BlockChest("DangerZone:Chest");
	public static Block claim_block = new BlockClaim("DangerZone:Chunk Claimer", "res/blocks/claim_block.png");
	public static Block claim_marker = new BlockClaimMarker("DangerZone:Chunk Marker", "res/blocks/claim_marker.png");
	public static Block lightstick = new BetterTorch("DangerZone:Lightstick", "res/blocks/torch.png", "res/blocks/BetterTorchTexture.png");
	public static Block darkstick = new DarkStick("DangerZone:Darkstick", "res/blocks/darkstick.png", "res/blocks/BetterDarkStickTexture.png");
	public static Block oredark = new BlockDark("DangerZone:Dark Ore", "res/blocks/oredark.png", 200);
	public static Block orelight = new BlockLight("DangerZone:Light Ore", "res/blocks/orelight.png", 50);
	public static Block water = new BlockWater("DangerZone:Water", "res/blocks/water.png");
	public static Block waterstatic = new BlockWaterStatic("DangerZone:Water Source", "res/blocks/water.png");
	public static Block corn_plant = new BlockCorn("DangerZone:Baby Corn Plant", "res/blocks/corn_0.png");
	public static Block corn_plant1 = new BlockCorn("DangerZone:Corn Plant", "res/blocks/corn_1.png");
	public static Block corn_plant2 = new BlockCorn("DangerZone:Flowering Corn Plant", "res/blocks/corn_2.png");
	public static Block corn_plant3 = new BlockCorn("DangerZone:Ripe Corn Plant", "res/blocks/corn_3.png");
	public static Block roachnest = new RoachBlock("DangerZone:Cockroach Nest", "res/blocks/roachnest.png", "res/blocks/grassbottom.png", "res/blocks/grassside.png");
	public static Block glass = new BlockGlass("DangerZone:Glass", "res/blocks/glass.png", 10);
	public static Block flower_red = new BlockFlower("DangerZone:Red Flower", "res/blocks/flower_red.png");
	public static Block flower_pink = new BlockFlower("DangerZone:Blue Flower", "res/blocks/flower_pink.png");
	public static Block flower_purple = new BlockFlower("DangerZone:Purple Flower", "res/blocks/flower_purple.png");
	public static Block flower_yellow = new BlockFlower("DangerZone:Yellow Flower", "res/blocks/flower_yellow.png");
	public static Block butterfly_plant = new ButterflyPlant("DangerZone:Butterfly Plant", "res/blocks/butterfly_0.png");
	public static Block butterfly_plant1 = new ButterflyPlant("DangerZone:Butterfly Plant 1", "res/blocks/butterfly_1.png");
	public static Block butterfly_plant2 = new ButterflyPlant("DangerZone:Butterfly Plant 2", "res/blocks/butterfly_2.png");
	public static Block butterfly_plant3 = new ButterflyPlant("DangerZone:Butterfly Plant 3", "res/blocks/butterfly_3.png");
	public static Block fishnursery = new FishNursery("DangerZone:Fish Nursery", "res/blocks/fishnursery.png");
	public static Block sand = new BlockSand("DangerZone:Sand", "res/blocks/sand.png");
	public static Block radish_plant = new RadishPlant("DangerZone:Radish Plant", "res/blocks/radish_0.png");
	public static Block radish_plant1 = new RadishPlant("DangerZone:Radish Plant 1", "res/blocks/radish_1.png");
	public static Block radish_plant2 = new RadishPlant("DangerZone:Radish Plant 2", "res/blocks/radish_2.png");
	public static Block radish_plant3 = new RadishPlant("DangerZone:Ripe Radish Plant", "res/blocks/radish_3.png");
	public static Block rice_plant = new RicePlant("DangerZone:Rice Plant", "res/blocks/rice_0.png");
	public static Block rice_plant1 = new RicePlant("DangerZone:Rice Plant 1", "res/blocks/rice_1.png");
	public static Block rice_plant2 = new RicePlant("DangerZone:Rice Plant 2", "res/blocks/rice_2.png");
	public static Block rice_plant3 = new RicePlant("DangerZone:Ripe Rice Plant", "res/blocks/rice_3.png");
	public static Block blockcopper = new BlockMetal("DangerZone:Copper Block", "res/blocks/blockcopper.png", 100);
	public static Block blocktin = new BlockMetal("DangerZone:Tin Block", "res/blocks/blocktin.png", 125);
	public static Block blocksilver = new BlockMetal("DangerZone:Silver Block", "res/blocks/blocksilver.png", 150);
	public static Block blockplatinum = new BlockMetal("DangerZone:Platinum Block", "res/blocks/blockplatinum.png", 250);
	public static Block blockdiamond = new BlockMetal("DangerZone:Diamond Block", "res/blocks/blockdiamond.png", 250);
	public static Block blockemerald = new BlockMetal("DangerZone:Emerald Block", "res/blocks/blockemerald.png", 200);
	public static Block blockbloodstone = new BlockMetal("DangerZone:Bloodstone Block", "res/blocks/blockbloodstone.png", 175);
	public static Block blocksunstone = new BlockMetal("DangerZone:Sunstone Block", "res/blocks/blocksunstone.png", 175);
	public static Block blockdark = new BlockBlockDark("DangerZone:Dark Block", "res/blocks/blockdark.png", 400);
	public static Block blocklight = new BlockBlockLight("DangerZone:Light Block", "res/blocks/blocklight.png", 350);
	public static Block peachleaves = new BlockPeachLeaves("DangerZone: Peach Leaves", "res/blocks/peachleaves.png");
	public static Block cherryleaves = new BlockCherryLeaves("DangerZone:Cherry Leaves", "res/blocks/cherryleaves.png");
	public static Block appleleaves = new BlockAppleLeaves("DangerZone:Apple Leaves", "res/blocks/appleleaves.png");
	public static Block blueglass = new BlockColoredGlass("DangerZone:Blue Glass", "res/blocks/glassblue.png", 10);
	public static Block greenglass = new BlockColoredGlass("DangerZone:Green Glass", "res/blocks/glassgreen.png", 10);
	public static Block redglass = new BlockColoredGlass("DangerZone:Red Glass", "res/blocks/glassred.png", 10);
	public static Block violetglass = new BlockColoredGlass("DangerZone:Violet Glass", "res/blocks/glassviolet.png", 10);
	public static Block yellowglass = new BlockColoredGlass("DangerZone:Yellow Glass", "res/blocks/glassyellow.png", 10);
	public static Block stickyblock = new StickyBlock("DangerZone:Sticky Block", "res/blocks/stickyblock.png");
	public static Block coloringblock = new ColoringBlock("DangerZone:Coloring Block", "res/blocks/coloringblock.png");
	public static Block doortop = new Door("DangerZone:Door Top", "res/blocks/door_top.png");
	public static Block doorbottom = new Door("DangerZone:Door Bottom", "res/blocks/door_bottom.png");
	public static Block blockinstability = new BlockInstability("DangerZone:Boom Block", "res/blocks/instability.png", 1);
	public static Block blockinstability_large = new BlockInstability("DangerZone:Large Boom Block", "res/blocks/instability_large.png", 10);
	public static Block blockinstability_huge = new BlockInstability("DangerZone:Huge Boom Block", "res/blocks/instability_huge.png", 100);
	public static Block waterpump = new BlockWaterPump("DangerZone:Water Pump");
	public static Block waterspout = new BlockWaterSpout("DangerZone:Water Spout", "res/blocks/waterspouttop.png","res/blocks/waterspoutside.png", "res/blocks/waterpump.png");
	public static Block waterlight = new BlockWaterLight("DangerZone:Water Light", "res/blocks/waterlight.png","res/blocks/waterlightside.png", "res/blocks/waterpump.png", 0.65f);
	public static Block waterdark = new BlockWaterLight("DangerZone:Water Dark", "res/blocks/waterdark.png","res/blocks/waterdarkside.png", "res/blocks/waterpump.png", -0.65f);
	public static Block musicbox = new MusicBlock("DangerZone:Music Box", "res/blocks/musicbox.png","res/blocks/waterspoutside.png", "res/blocks/waterpump.png");
	public static Block puddlemaker = new PuddleMaker("DangerZone:Puddle Maker", "res/blocks/puddlemaker.png","res/blocks/waterspoutside.png", "res/blocks/waterpump.png");
	public static Block watercannon = new WaterCannon("DangerZone:Water Cannon", "res/blocks/watercannon.png","res/blocks/waterspoutside.png", "res/blocks/waterpump.png");
	public static Block waterstone = new WaterStone("DangerZone:Squishy Stone", "res/blocks/stone.png");
	public static Block unwaterpump = new WaterNot("DangerZone:Un-Water Pump");
	public static Block waterswitch = new WaterAnd("DangerZone:Water Switch");
	public static Block milk = new BlockMilk("DangerZone:Milk", "res/blocks/milk.png");
	public static Block milkstatic = new BlockMilkStatic("DangerZone:Milk Source", "res/blocks/milk.png");
	public static Block lava = new BlockLava("DangerZone:Lava", "res/blocks/lava.png");
	public static Block lavastatic = new BlockLavaStatic("DangerZone:Lava Source", "res/blocks/lava.png");
	public static Block lavafiller = new BlockLavaFiller("DangerZone:Lava Filler", "res/blocks/lava.png");
	public static Block autofence = new BlockBarrier("DangerZone:Auto-Fence", "res/blocks/barrier.png", "res/blocks/BetterFenceTexture.png");
	public static Block post = new Post("DangerZone:Fence Post", "res/blocks/post.png", "res/blocks/BetterFenceTexture.png");
	public static Block ladder = new Ladder("DangerZone:Ladder", "res/blocks/ladder.png", "res/blocks/LadderTexture.png");
	public static Block shredder = new BlockShredder("DangerZone:Shredder");
	public static Block desk = new BlockDesk("DangerZone:Desk");
	public static BlockStand stand = new BlockStand("DangerZone:Stand", "res/blocks/stand.png", "res/blocks/Standtexture.png");
	public static Block rail = new BlockRail("DangerZone:MagLev Rail", "res/blocks/rail.png", "res/blocks/Railtexture.png");
	public static Block railspeed = new BlockRail("DangerZone:Acceleration Rail", "res/blocks/speedrail.png", "res/blocks/SpeedRailtexture.png");
	public static Block railslow = new BlockRail("DangerZone:Deceleration Rail", "res/blocks/slowrail.png", "res/blocks/SlowRailtexture.png");
	public static Block railstop = new BlockRail("DangerZone:Stop Rail", "res/blocks/stoprail.png", "res/blocks/StopRailtexture.png");
	public static Block railup = new BlockUpRail("DangerZone:Levitate Up Rail", "res/blocks/uprail.png", "res/blocks/UpRailtexture.png");
	public static Block raildown = new BlockDownRail("DangerZone:Levitate Down Rail", "res/blocks/downrail.png", "res/blocks/DownRailtexture.png");
	
	public static Block railfixedslow = new BlockRail("DangerZone:Slow Speed Rail", "res/blocks/fixedslowrail.png", "res/blocks/FixedSlowRailtexture.png");
	public static Block railfixedmedium = new BlockRail("DangerZone:Medium Speed Rail", "res/blocks/fixedmediumrail.png", "res/blocks/FixedMediumRailtexture.png");
	public static Block railload = new BlockRail("DangerZone:Load Rail", "res/blocks/loadrail.png", "res/blocks/LoadRailtexture.png");
	public static Block railunload = new BlockRail("DangerZone:Unload Rail", "res/blocks/unloadrail.png", "res/blocks/UnLoadRailtexture.png");
	
	public static Block raildplus = new BlockUpRail("DangerZone:Dimension+ Rail", "res/blocks/raildplus.png", "res/blocks/raildplustexture.png");
	public static Block raildminus = new BlockUpRail("DangerZone:Dimension- Rail", "res/blocks/raildminus.png", "res/blocks/raildminustexture.png");
	
	
	public static Block cloud_light = new BlockCloud("DangerZone:Cloud", "res/blocks/cloud_light.png");
	public static Block cloud_rain = new BlockCloud("DangerZone:Rain Cloud", "res/blocks/cloud_rain.png");
	public static Block cloud_thunder = new BlockCloud("DangerZone:Thunder Cloud", "res/blocks/cloud_thunder.png");
	
	//name of spawner, what to spawn, scale, tenths of seconds, how many to spawn, min light level (-1), max light level (2)
	public static Block butterflyspawner = new BlockSpawner("DangerZone:Butterfly Spawner", "DangerZone:Butterfly", 0.80f, 200, 3, 0.55f, 1.75f);
	public static Block ghostspawner = new BlockSpawner("DangerZone:Ghost Spawner", "DangerZone:Ghost", 0.65f, 300, 2, -0.55f, 0.55f);
	public static Block ghostskellyspawner = new BlockSpawner("DangerZone:Ghost Skelly Spawner", "DangerZone:GhostSkelly", 0.5f, 400, 2, -0.55f, 0.55f);
	public static Block ratspawner = new BlockSpawner("DangerZone:Rat Spawner", "DangerZone:Rat", 0.65f, 300, 3, -0.25f, 1.0f);
	public static Block cockroachspawner = new BlockSpawner("DangerZone:Cockroach Spawner", "DangerZone:Cockroach", 1.25f, 300, 3, -0.55f, 1.55f);
	public static Block moosespawner = new BlockSpawner("DangerZone:Moose Spawner", "DangerZone:Moose", 0.25f, 600, 1, 0.55f, 1.55f);
	public static Block sparklemuffinspawner = new BlockSpawner("DangerZone:Sparklemuffin Spawner", "DangerZone:Sparklemuffin", 0.4f, 1200, 1, -0.55f, 0.55f);
	public static Block skeletorusspawner = new BlockSpawner("DangerZone:Skeletorus Spawner", "DangerZone:Skeletorus", 0.4f, 1200, 1, -0.55f, 0.55f);
	public static Block goosespawner = new BlockSpawner("DangerZone:Goose Spawner", "DangerZone:Goose", 0.40f, 600, 2, 0.55f, 1.55f);
	public static Block goslingspawner = new BlockSpawner("DangerZone:Gosling Spawner", "DangerZone:Gosling", 0.90f, 600, 2, 0.55f, 1.55f);
	public static Block ostrichspawner = new BlockSpawner("DangerZone:Ostrich Spawner", "DangerZone:Ostrich", 0.35f, 700, 1, 0.55f, 1.55f);
	public static Block anteaterspawner = new BlockSpawner("DangerZone:Anteater Spawner", "DangerZone:Anteater", 0.25f, 600, 1, 0.55f, 1.55f);
	public static Block werewolfspawner = new BlockSpawner("DangerZone:Werewolf Spawner", "DangerZone:Werewolf", 0.48f, 800, 2, -0.55f, 0.55f);
	public static Block vampirespawner = new BlockSpawner("DangerZone:Vampire Spawner", "DangerZone:Vampire", 0.48f, 800, 2, -0.55f, 0.55f);
	public static Block vixenspawner = new BlockSpawner("DangerZone:Vixen Spawner", "DangerZone:Vixen", 0.48f, 800, 2, -0.55f, 0.55f);
	public static Block thecountspawner = new BlockSpawner("DangerZone:The Count Spawner", "DangerZone:The Count", 0.48f, 1200, 1, -0.55f, 0.55f);
	public static Block vampiremoosespawner = new BlockSpawner("DangerZone:Vampire Moose Spawner", "DangerZone:Vampire Moose", 0.25f, 1500, 1, -0.55f, 0.55f);
	public static Block fishspawner = new BlockSpawner("DangerZone:Fish Spawner", "DangerZone:Fish", 0.45f, 300, 3, 0.55f, 1.55f);
	public static Block anotherfishspawner = new BlockSpawner("DangerZone:Another Fish Spawner", "DangerZone:Another Fish", 0.45f, 300, 3, 0.55f, 1.55f);
	public static Block pufferfishspawner = new BlockSpawner("DangerZone:Puffer Fish Spawner", "DangerZone:Puffer Fish", 0.45f, 300, 3, 0.55f, 1.55f);
	public static Block butterflyfishspawner = new BlockSpawner("DangerZone:Butterfly Fish Spawner", "DangerZone:Butterfly Fish", 0.45f, 300, 3, 0.55f, 1.55f);
	public static Block stickfishspawner = new BlockSpawner("DangerZone:Stick Fish Spawner", "DangerZone:Stick Fish", 0.45f, 300, 3, 0.55f, 1.55f);
	public static Block minnowspawner = new BlockSpawner("DangerZone:Minnow Spawner", "DangerZone:Minnow", 0.45f, 200, 7, 0.55f, 1.55f);
	public static Block piranahspawner = new BlockSpawner("DangerZone:Piranah Spawner", "DangerZone:Piranah", 0.40f, 200, 7, 0.55f, 1.55f);
	public static Block snarlerspawner = new BlockSpawner("DangerZone:Snarler Spawner", "DangerZone:Snarler", 0.80f, 200, 3, -0.55f, 0.75f);
	public static Block bulletbatspawner = new BlockSpawner("DangerZone:Bullet Bat Spawner", "DangerZone:Bullet Bat", 0.75f, 200, 5, -0.55f, 1.75f);
	public static Block martianspawner = new BlockSpawner("DangerZone:Martian Spawner", "DangerZone:Martian", 0.45f, 250, 8, -0.55f, 1.75f);
	public static Block desertrainfrogspawner = new BlockSpawner("DangerZone:Desert Rain Frog Spawner", "DangerZone:Desert Rain Frog", 0.45f, 300, 3, 0.55f, 1.55f);
	public static Block eelspawner = new BlockSpawner("DangerZone:Eel Spawner", "DangerZone:Eel", 0.40f, 300, 3, -0.55f, 1.55f);
	public static Block mermaidspawner = new BlockSpawner("DangerZone:Mermaid Spawner", "DangerZone:Mermaid", 0.48f, 800, 2, 0.25f, 1.55f);
	
	public static Block block_lightgrey = new BlockOre("DangerZone:Light Grey Block", "res/blocks/block_lightgrey.png", 5, 1);
	public static Block block_lightgreen = new BlockOre("DangerZone:Light Green Block", "res/blocks/block_lightgreen.png", 5, 1);
	public static Block block_lightred = new BlockOre("DangerZone:Light Red Block", "res/blocks/block_lightred.png", 5, 1);
	public static Block block_lightblue = new BlockOre("DangerZone:Light Blue Block", "res/blocks/block_lightblue.png", 5, 1);
	public static Block block_lightyellow = new BlockOre("DangerZone:Light Yellow Block", "res/blocks/block_lightyellow.png", 5, 1);
	public static Block block_lightorange = new BlockOre("DangerZone:Light Orange Block", "res/blocks/block_lightorange.png", 5, 1);
	public static Block block_lightpurple = new BlockOre("DangerZone:Light Purple Block", "res/blocks/block_lightpurple.png", 5, 1);
	public static Block block_lightviolet = new BlockOre("DangerZone:Light Violet Block", "res/blocks/block_lightviolet.png", 5, 1);
	public static Block block_lightbrown = new BlockOre("DangerZone:Light Brown Block", "res/blocks/block_lightbrown.png", 5, 1);
	
	public static Block block_grey = new BlockOre("DangerZone:Grey Block", "res/blocks/block_grey.png", 5, 1);
	public static Block block_green = new BlockOre("DangerZone:Green Block", "res/blocks/block_green.png", 5, 1);
	public static Block block_red = new BlockOre("DangerZone:Red Block", "res/blocks/block_red.png", 5, 1);
	public static Block block_blue = new BlockOre("DangerZone:Blue Block", "res/blocks/block_blue.png", 5, 1);
	public static Block block_yellow = new BlockOre("DangerZone:Yellow Block", "res/blocks/block_yellow.png", 5, 1);
	public static Block block_orange = new BlockOre("DangerZone:Orange Block", "res/blocks/block_orange.png", 5, 1);
	public static Block block_purple = new BlockOre("DangerZone:Purple Block", "res/blocks/block_purple.png", 5, 1);
	public static Block block_violet = new BlockOre("DangerZone:Violet Block", "res/blocks/block_violet.png", 5, 1);
	public static Block block_brown = new BlockOre("DangerZone:Brown Block", "res/blocks/block_brown.png", 5, 1);
	
	public static Block block_darkgrey = new BlockOre("DangerZone:Dark Grey Block", "res/blocks/block_darkgrey.png", 5, 1);
	public static Block block_darkgreen = new BlockOre("DangerZone:Dark Green Block", "res/blocks/block_darkgreen.png", 5, 1);
	public static Block block_darkred = new BlockOre("DangerZone:Dark Red Block", "res/blocks/block_darkred.png", 5, 1);
	public static Block block_darkblue = new BlockOre("DangerZone:Dark Blue Block", "res/blocks/block_darkblue.png", 5, 1);
	public static Block block_darkyellow = new BlockOre("DangerZone:Dark Yellow Block", "res/blocks/block_darkyellow.png", 5, 1);
	public static Block block_darkorange = new BlockOre("DangerZone:Dark Orange Block", "res/blocks/block_darkorange.png", 5, 1);
	public static Block block_darkpurple = new BlockOre("DangerZone:Dark Purple Block", "res/blocks/block_darkpurple.png", 5, 1);
	public static Block block_darkviolet = new BlockOre("DangerZone:Dark Violet Block", "res/blocks/block_darkviolet.png", 5, 1);
	public static Block block_darkbrown = new BlockOre("DangerZone:Dark Brown Block", "res/blocks/block_darkbrown.png", 5, 1);
	
	
	public static Block reefgrass = new SeaPlant("DangerZone:Reef Grass", "res/blocks/reefgrass.png");
	public static Block redreefgrass = new SeaPlant("DangerZone:Red Reef Grass", "res/blocks/redreefgrass.png");	
	public static Block redcoral = new SeaPlant("DangerZone:Red Coral", "res/blocks/redcoral.png");
	public static Block yellowcoral = new SeaPlant("DangerZone:Yellow Coral", "res/blocks/yellowcoral.png");
	public static Block bluecoral = new SeaPlant("DangerZone:Blue Coral", "res/blocks/bluecoral.png");
	public static Block firecoral = new SeaPlant("DangerZone:Fire Coral", "res/blocks/firecoral.png");	
	public static Block redfancoral = new SeaPlant("DangerZone:Red Fan Coral", "res/blocks/redfan.png");
	public static Block blackfancoral = new SeaPlant("DangerZone:Black Fan Coral", "res/blocks/blackfan.png");	
	public static Block seaweed = new SeaPlant("DangerZone:Seaweed", "res/blocks/seaweed.png");
	public static Block tallseaweed = new TallSeaPlant("DangerZone:Tall Seaweed", "res/blocks/tallseaweed.png");
	public static Block brownseaweed = new SeaPlant("DangerZone:Brown Seaweed", "res/blocks/brownseaweed.png");
	public static Block browntallseaweed = new TallSeaPlant("DangerZone:Tall Brown Seaweed", "res/blocks/browntallseaweed.png");
	public static Block yellowseaweed = new SeaPlant("DangerZone:Yellow Seaweed", "res/blocks/yellowseaweed.png");
	public static Block yellowtallseaweed = new TallSeaPlant("DangerZone:Tall Yellow Seaweed", "res/blocks/tallyellowseaweed.png");
	public static Block kelp = new SeaPlant("DangerZone:Kelp", "res/blocks/kelp.png");
	public static Block tallkelp = new TallSeaPlant("DangerZone:Tall Kelp", "res/blocks/tallkelp.png");
	
	public static Block BlockArray[];
	public static final int blocksMAX = 2048;
	public static Properties prop = null;
		
	public Blocks(){
		int i;
		BlockArray = new Block[blocksMAX];
		for(i=0;i<blocksMAX;i++){
			BlockArray[i] = null;
		}
	}
	
	public static int registerBlock(Block b){
		int i = 0;
		
		for(i=1;i<blocksMAX;i++){
			if(BlockArray[i] != null){
				if(BlockArray[i].uniquename.equals(b.uniquename)){ //duplicate!!!
					return 0; //duplicate!!!
				}
			}
		}
		
		for(i=1;i<blocksMAX;i++){
			if(BlockArray[i] == null)break;
		}
		if(i >= blocksMAX-1)return 0;
		
		if(prop != null)i = Utils.getPropertyInt(prop, b.uniquename, 1, blocksMAX-1, i);

		//existing or new block
		if(BlockArray[i] == null){ //Slot is open, this is good. Give it the same ID it had last time!
			BlockArray[i] = b;
			b.blockID = i;
			if(prop != null)prop.setProperty(b.uniquename, String.format("%d", i)); //next time we will find it!
			return i;
		}else{ 
			//Oops. Slot already taken.
			//Give this slot to the existing one, and bump the intruder to a new slot.
			//Should only happen when adding new mods and they get loaded before old ones.
			Block intruder = BlockArray[i];
			int isave = i;
			BlockArray[i] = b;
			b.blockID = i;
			for(i=1;i<blocksMAX;i++){
				if(BlockArray[i] == null){
					BlockArray[i] = intruder;
					intruder.blockID = i;
					if(prop != null)prop.setProperty(intruder.uniquename, String.format("%d", i)); //next time we will find it!
					return isave;
				}
			}			
		}
		return 0;
	}
	
	public static int lookup(String name){
		int ret = Utils.getPropertyInt(prop, name, 1, blocksMAX-1, 0);
		return ret;
	}
	
	public static void addthis(String name, int i){
		prop.setProperty(name, String.format("%d", i));
		return;
	}
	
	public static void reRegisterBlockAt(String s, int loc){
		int i;
		
		if(loc <= 0 || loc >= blocksMAX)return;
		if(s == null || s.equals(""))return;
		
		if(BlockArray[loc] != null){
			if(s.equals(BlockArray[loc].uniquename)){
				return; //already where its supposed to be! :)
			}
			//maybe it is somewhere else?
			for(i=1;i<blocksMAX;i++){
				if(BlockArray[i] != null){
					if(s.equals(BlockArray[i].uniquename)){
						//Take it, and move the intruder where I was
						Block me = BlockArray[i];
						Block intruder = BlockArray[loc];
						BlockArray[loc] = me;
						BlockArray[loc].blockID = loc;
						BlockArray[i] = intruder;
						BlockArray[i].blockID = i;
						return;
					}
				}
			}
		}else{
			//maybe it is somewhere else?
			for(i=1;i<blocksMAX;i++){
				if(BlockArray[i] != null){
					if(s.equals(BlockArray[i].uniquename)){
						//Move it!
						BlockArray[loc] = BlockArray[i];
						BlockArray[loc].blockID = loc;
						BlockArray[i] = null;
						return;
					}
				}
			}
		}
	}
	
	public static void load(){
		InputStream input = null;
		prop = new Properties();
		String filepath = new String();		
		filepath = String.format("worlds/%s/blockIDs.dat", DangerZone.worldname);	
		 
		try {	 
			input = new FileInputStream(filepath);
	 
			// load a properties file
			prop.load(input);

		} catch (IOException ex) {
			//ex.printStackTrace();
			input = null;
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static void save(){

		OutputStream output = null;
		String filepath = new String();		
		filepath = String.format("worlds/%s/blockIDs.dat", DangerZone.worldname);	
		File f = new File(filepath);		
		f.getParentFile().mkdirs();	
		
		try {	 
			output = new FileOutputStream(filepath);
			// save properties
			prop.store(output, null);
	 
		} catch (IOException io) {
			io.printStackTrace();
		}
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	//used for when it matters and block can change solidity, like a door...
	public static boolean isSolid(int blockid, World w, int d, int x, int y, int z){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].getIsSolid(w, d, x, y, z);
	}
	
	//used only for attaching things
	public static boolean isSolidThisSide(int blockid, World w, int d, int x, int y, int z, int side){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].isSolidThisSide(w, d, x, y, z, side);
	}
	
	//used for when it matters and block can change solidity, like a door...
	public static void bumpedBlock(int blockid, Entity e, World w, int d, int x, int y, int z){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;
		BlockArray[blockid].bumpedBlock(e, w, d, x, y, z);
	}
	
	//when you really don't care too much...
	public static boolean isSolid(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].isSolid;
	}
	
	public static boolean isLiquid(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].isLiquid;
	}
	
	public static boolean isValid(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return true;
	}
	
	public static boolean isSquishy(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].isSquishy;
	}
	
	public static boolean showTop(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].showTop;
	}
	
	public static boolean isSolidForRender(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].isSolidForRendering;
	}
	
	public static boolean renderAllSides(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].renderAllSides;
	}
	
	public static boolean renderSmaller(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].renderSmaller;
	}
	
	public static boolean shouldShow(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].showInInventory;
	}
	
	public static boolean isTranslucentForRender(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].isTranslucent;
	}
	
	public static boolean hasOwnRenderer(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].hasOwnRenderer;
	}
	
	public static boolean shouldAlwaysRender(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].alwaysRender;
	}
	
	public static boolean alwaystick(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].alwaystick;
	}
	
	public static boolean randomtick(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].randomtick;
	}
	
	public static boolean isWood(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].isWood;
	}
	
	public static boolean isStone(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].isStone;
	}
	
	public static boolean isDirt(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].isDirt;
	}
	
	public static boolean isLeaves(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].isLeaves;
	}
	
	public static boolean isWaterPlant(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].isWaterPlant;
	}
	
	public static boolean canLeavesGrow(int blockid){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].canLeavesGrow;
	}
	
	public static void doblocktick(World w, int d, int x, int y, int z, int blockid){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;		
		BlockArray[blockid].tickMe(w, d, x, y, z);
	}
	
	public static void dofastblocktick(World w, int d, int x, int y, int z, int blockid){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;		
		BlockArray[blockid].tickMeFast(w, d, x, y, z);
	}
	
	public static void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int blockid, int meta, int sides, boolean focused){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;
		BlockArray[blockid].renderMe(wr, w, d, x, y, z, blockid, meta, sides, focused);
	}
	
	public static void renderMeToVBO(long chunkvbos[], WorldRenderer wr, World w, int d, int x, int y, int z, int blockid, int meta, int sides, boolean focused, int xo, int yo, int zo){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;
		BlockArray[blockid].renderMeToVBO(chunkvbos, wr, w, d, x, y, z, blockid, meta, sides, focused, xo, yo, zo);
	}
	
	public static String getBreakSound(int blockid){
		if(blockid <= 0)return null;
		if(blockid >= blocksMAX)return null;
		if(BlockArray[blockid]==null)return null;
		return BlockArray[blockid].getBreakSound();
	}
	
	public static String getPlaceSound(int blockid){
		if(blockid <= 0)return null;
		if(blockid >= blocksMAX)return null;
		if(BlockArray[blockid]==null)return null;
		return BlockArray[blockid].getPlaceSound();
	}
	
	public static String getHitSound(int blockid){
		if(blockid <= 0)return null;
		if(blockid >= blocksMAX)return null;
		if(BlockArray[blockid]==null)return null;
		return BlockArray[blockid].getHitSound();
	}
	
	public static String getParticleName(int blockid){
		if(blockid <= 0)return null;
		if(blockid >= blocksMAX)return null;
		if(BlockArray[blockid]==null)return null;
		return BlockArray[blockid].getParticleName();
	}
	
	public static int getMaxStack(int blockid){
		if(blockid <= 0)return 0;
		if(blockid >= blocksMAX)return 0;
		if(BlockArray[blockid]==null)return 0;
		return BlockArray[blockid].maxstack;
	}
	
	public static int getMinDamage(int blockid){
		if(blockid <= 0)return 0;
		if(blockid >= blocksMAX)return 0;
		if(BlockArray[blockid]==null)return 0;
		return BlockArray[blockid].mindamage;
	}

	public static Texture getTexture(int blockid){
		if(blockid <= 0)return null;
		if(blockid >= blocksMAX)return null;
		if(BlockArray[blockid]==null)return null;
		return BlockArray[blockid].getTexture(1 /*front*/);
	}
	
	public static int getActiveBlockid(int blockid){
		if(blockid <= 0)return 0;
		if(blockid >= blocksMAX)return 0;
		if(BlockArray[blockid]==null)return 0;
		if(BlockArray[blockid].active_partner==null)return 0;
		return BlockArray[blockid].active_partner.blockID;
	}
	
	public static int getStaticBlockid(int blockid){
		if(blockid <= 0)return 0;
		if(blockid >= blocksMAX)return 0;
		if(BlockArray[blockid]==null)return 0;
		if(BlockArray[blockid].static_partner==null)return 0;
		return BlockArray[blockid].static_partner.blockID;
	}
	
	public static Texture getTextureForSide(int blockid, int side){
		if(blockid <= 0)return null;
		if(blockid >= blocksMAX)return null;
		if(BlockArray[blockid]==null)return null;
		return BlockArray[blockid].getTexture(side);
	}
	
	public static void notifyNeighborChanged(int blockid, World w, int d, int x, int y, int z){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;
		BlockArray[blockid].notifyNeighborChanged(w, d, x, y, z);
	}
	
	public static String getUniqueName(int blockid){
		if(blockid <= 0)return null;
		if(blockid >= blocksMAX)return null;
		if(BlockArray[blockid]==null)return null;
		return BlockArray[blockid].uniquename;
	}
	
	public static int findByName(String name){
		int i;
		if(name == null)return 0;
		for(i=1;i<blocksMAX;i++){
			if(BlockArray[i] != null){
				if(BlockArray[i].uniquename != null){
					if(name.equals(BlockArray[i].uniquename)){
						return i;
					}
				}
			}
		}
		return 0;
	}
	
	//called with PLAYER on CLIENT
	//all other entities on SERVER
	public static void entityInLiquid(int blockid, Entity e){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;
		BlockArray[blockid].entityInLiquid(e);
	}
	
	public static boolean rightClickOnBlock(int blockid, Player p, int d, int x, int y, int z){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].rightClickOnBlock(p, d, x, y, z);
	}
	
	public static boolean leftClickOnBlock(int blockid, Player p, int d, int x, int y, int z){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return BlockArray[blockid].leftClickOnBlock(p, d, x, y, z);
	}
	
	public static void onBlockBroken(int blockid, Player p, int d, int x, int y, int z){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;
		BlockArray[blockid].onBlockBroken(p, d, x, y, z);
	}
	
	public static int getMaxDamage(int blockid){
		if(blockid <= 0)return 0;
		if(blockid >= blocksMAX)return 0;
		if(BlockArray[blockid]==null)return 0;
		return BlockArray[blockid].maxdamage;
	}
	
	public static int getBlockDrop(int blockid, Player p, World w, int d, int x, int y, int z){
		if(blockid <= 0)return 0;
		if(blockid >= blocksMAX)return 0;
		if(BlockArray[blockid]==null)return 0;
		return BlockArray[blockid].getBlockDrop(p, w, d, x, y, z);
	}
	
	public static int getItemDrop(int blockid, Player p, World w, int d, int x, int y, int z){
		if(blockid <= 0)return 0;
		if(blockid >= blocksMAX)return 0;
		if(BlockArray[blockid]==null)return 0;
		return BlockArray[blockid].getItemDrop(p, w, d, x, y, z);
	}
	
	public static int getDropCount(int blockid, Player p, World w, int d, int x, int y, int z){
		if(blockid <= 0)return 0;
		if(blockid >= blocksMAX)return 0;
		if(BlockArray[blockid]==null)return 0;
		return BlockArray[blockid].getDropCount(p, w, d, x, y, z);
	}
	
	public static float getFriction(int blockid){
		if(blockid <= 0)return 0;
		if(blockid >= blocksMAX)return 0;
		if(BlockArray[blockid]==null)return 0;
		return BlockArray[blockid].friction;
	}
	
	public static float getLightLevel(int blockid, World w, int d, int x, int y, int z){
		if(blockid <= 0)return 0.0f;
		if(blockid >= blocksMAX)return 0.0f;
		if(BlockArray[blockid]==null)return 0.0f;
		return BlockArray[blockid].getBrightness(w, d, x, y, z);
	}
	
	public static void doSteppedOn(int blockid, Entity e, World w, int d, int x, int y, int z){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;
		BlockArray[blockid].doSteppedOn(e, w, d, x, y, z);
	}
	
	//called constantly while in
	public static void doIsIn(int blockid, Entity e, World w, int d, int x, int y, int z){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;
		BlockArray[blockid].doIsIn(e, w, d, x, y, z);
	}
	
	//called just once on entry
	public static void doEntered(int blockid, Entity e, World w, int d, int x, int y, int z){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;
		BlockArray[blockid].doEntered(e, w, d, x, y, z);
	}
	
	public static boolean doPlaceBlock(int blockid, int focusbid, Player p, World w, int d, int x, int y, int z, int side){
		if(blockid <= 0)return false;
		if(blockid >= blocksMAX)return false;
		if(BlockArray[blockid]==null)return false;
		return Blocks.BlockArray[blockid].doPlaceBlock(focusbid, p, w, d, x, y, z, side);
	}
	
	public static void doBreakBlock(int blockid, World w, int d, int x, int y, int z){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;
		int bid = BlockArray[blockid].getBlockDrop(null, w, d, x, y, z);
		int iid = BlockArray[blockid].getItemDrop(null, w, d, x, y, z);
		if(bid != 0 || iid != 0){
			if(bid != 0){
				EntityBlockItem eb = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
				if(eb != null){
					eb.setBID(bid);
					eb.fill(bid, 0, BlockArray[blockid].getDropCount(null, w, d, x, y, z));
					w.spawnEntityInWorld(eb);
				}
			}
			if(iid != 0){
				EntityBlockItem eb = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, (double)x+0.5f, (double)y+0.5f, (double)z+0.5f);
				if(eb != null){
					eb.setIID(iid);
					eb.fill(0, iid, BlockArray[blockid].getDropCount(null, w, d, x, y, z));
					w.spawnEntityInWorld(eb);
				}
			}
		}
	}
	
	
	public static int getBurnTime(int blockid){
		if(blockid <= 0)return 0;
		if(blockid >= blocksMAX)return 0;
		if(BlockArray[blockid]==null)return 0;
		return BlockArray[blockid].burntime;
	}
	
	public static int getMenu(int blockid){
		if(blockid <= 0)return InventoryMenus.GENERIC;
		if(blockid >= blocksMAX)return InventoryMenus.GENERIC;
		if(BlockArray[blockid]==null)return InventoryMenus.GENERIC;
		return BlockArray[blockid].menu;
	}
	
	public static Block getBlock(int blockid){
		if(blockid <= 0)return null;
		if(blockid >= blocksMAX)return null;
		return BlockArray[blockid];
	}
	
	public static String getStepSound(int blockid){
		if(blockid <= 0)return null;
		if(blockid >= blocksMAX)return null;
		if(BlockArray[blockid]==null)return null;
		return BlockArray[blockid].getStepSound();
	}
	
	public static void renderMeHeld(WorldRenderer wr, Entity e, int blockid, boolean isdisplay){
		if(blockid <= 0)return;
		if(blockid >= blocksMAX)return;
		if(BlockArray[blockid]==null)return;
		BlockArray[blockid].renderMeHeld(wr, e, blockid, isdisplay);
	}
	

}
