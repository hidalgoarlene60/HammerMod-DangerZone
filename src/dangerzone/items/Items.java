package dangerzone.items;
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
import dangerzone.Effects;
import dangerzone.ItemAttribute;
import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.entities.Entity;
import dangerzone.gui.InventoryMenus;


public class Items {
	
	public static Item stick = new ItemStick("DangerZone:Stick", "res/items/2by4.png");
	public static Item door = new ItemDoor("DangerZone:Door", "res/items/door.png");
	public static Item sign = new ItemSign("DangerZone:Sign", "res/items/sign.png");
	public static Item eggbutterfly = new ItemSpawnEgg("DangerZone:Spawn Butterfly", "res/items/eggbutterfly.png", "DangerZone:Butterfly");
	
	public static Item eggghost = new ItemSpawnEgg("DangerZone:Spawn Ghost", "res/items/eggghost.png", "DangerZone:Ghost");
	public static Item eggghostskelly = new ItemSpawnEgg("DangerZone:Spawn Ghost Skelly", "res/items/eggghostskelly.png", "DangerZone:GhostSkelly");
	public static Item eggrat = new ItemSpawnEgg("DangerZone:Spawn Rat", "res/items/eggrat.png", "DangerZone:Rat");
	public static Item eggcockroach = new ItemSpawnEgg("DangerZone:Spawn Cockroach", "res/items/eggcockroach.png", "DangerZone:Cockroach");
	public static Item eggmoose = new ItemSpawnEgg("DangerZone:Spawn Moose", "res/items/eggmoose.png", "DangerZone:Moose");
	public static Item egggoose = new ItemSpawnEgg("DangerZone:Spawn Goose", "res/items/egggoose.png", "DangerZone:Goose");
	public static Item egggosling = new ItemSpawnEgg("DangerZone:Spawn Gosling", "res/items/egggoose.png", "DangerZone:Gosling");
	public static Item eggostrich = new ItemSpawnEgg("DangerZone:Spawn Ostrich", "res/items/eggostrich.png", "DangerZone:Ostrich");
	public static Item eggsparklemuffin = new ItemSpawnEgg("DangerZone:Spawn Sparklemuffin", "res/items/eggsparklemuffin.png", "DangerZone:Sparklemuffin");
	public static Item eggskeletorus = new ItemSpawnEgg("DangerZone:Spawn Skeletorus", "res/items/eggskeletorus.png", "DangerZone:Skeletorus");
	public static Item egganteater = new ItemSpawnEgg("DangerZone:Spawn Anteater", "res/items/egganteater.png", "DangerZone:Anteater");
	public static Item eggwerewolf = new ItemSpawnEgg("DangerZone:Spawn Werewolf", "res/items/eggwerewolf.png", "DangerZone:Werewolf");
	public static Item eggvampire = new ItemSpawnEgg("DangerZone:Spawn Vampire", "res/items/eggvampire.png", "DangerZone:Vampire");
	public static Item eggthecount = new ItemSpawnEgg("DangerZone:Spawn The Count", "res/items/eggthecount.png", "DangerZone:The Count");
	public static Item eggvampiremoose = new ItemSpawnEgg("DangerZone:Spawn Vampire Moose", "res/items/eggvampiremoose.png", "DangerZone:Vampire Moose");
	public static Item eggfish = new ItemSpawnEgg("DangerZone:Spawn Fish", "res/items/eggfish.png", "DangerZone:Fish");
	public static Item egganotherfish = new ItemSpawnEgg("DangerZone:Spawn Another Fish", "res/items/eggfish.png", "DangerZone:Another Fish");
	public static Item eggpufferfish = new ItemSpawnEgg("DangerZone:Spawn Puffer Fish", "res/items/eggfish.png", "DangerZone:Puffer Fish");
	public static Item eggbutterflyfish = new ItemSpawnEgg("DangerZone:Spawn Butterfly Fish", "res/items/eggfish.png", "DangerZone:Butterfly Fish");
	public static Item eggstickfish = new ItemSpawnEgg("DangerZone:Spawn Stick Fish", "res/items/eggfish.png", "DangerZone:Stick Fish");
	public static Item eggminnow = new ItemSpawnEgg("DangerZone:Spawn Minnow", "res/items/eggfish.png", "DangerZone:Minnow");
	public static Item eggpiranah = new ItemSpawnEgg("DangerZone:Spawn Piranah", "res/items/eggfish.png", "DangerZone:Piranah");
	public static Item eggfrog = new ItemSpawnEgg("DangerZone:Spawn Magic Frog", "res/items/eggfrog.png", "DangerZone:Frog");
	public static Item eggvixen = new ItemSpawnEgg("DangerZone:Spawn Vixen", "res/items/eggvixen.png", "DangerZone:Vixen");
	public static Item maglev = new ItemSpawnEgg("DangerZone:Spawn MagLev Cart", "res/items/monorail.png", "DangerZone:MagLev");
	public static Item flag = new ItemSpawnEgg("DangerZone:Tower Defense Flag", "res/items/flag.png", "DangerZone:Flag");
	public static Item eggsnarler = new ItemSpawnEgg("DangerZone:Spawn Snarler", "res/items/eggsnarler.png", "DangerZone:Snarler");
	public static Item eggbulletbat = new ItemSpawnEgg("DangerZone:Spawn Bullet Bat", "res/items/eggbulletbat.png", "DangerZone:Bullet Bat");
	public static Item eggmartian = new ItemSpawnEgg("DangerZone:Spawn Martian", "res/items/eggmartian.png", "DangerZone:Martian");
	public static Item eggdesertrainfrog = new ItemSpawnEgg("DangerZone:Spawn Desert Rain Frog", "res/items/eggdesertrainfrog.png", "DangerZone:Desert Rain Frog");
	public static Item eggeel = new ItemSpawnEgg("DangerZone:Spawn Eel", "res/items/eggeel.png", "DangerZone:Eel");
	public static Item raft = new ItemSpawnEgg("DangerZone:Spawn Raft", "res/items/raft.png", "DangerZone:Raft");
	public static Item eggmermaid = new ItemSpawnEgg("DangerZone:Spawn Mermaid", "res/items/eggmermaid.png", "DangerZone:Mermaid");
	public static Item egghammerhead = new ItemSpawnEgg("DangerZone:Spawn Hammerhead Shark", "res/items/egghammerhead.png", "DangerZone:Hammerhead Shark");
	
	public static Item eggcloud = new ItemSpawnEgg("DangerZone:Spawn Cloud", "res/items/egggoose.png", "DangerZone:Cloud");
	public static Item eggvolcano = new ItemSpawnEgg("DangerZone:Spawn VOLCANO!!!", "res/items/volcano.png", "DangerZone:Volcano");

	public static Item lumpcopper = new Item("DangerZone:Copper", "res/items/lumpcopper.png");
	public static Item lumptin = new Item("DangerZone:Tin", "res/items/lumptin.png");
	public static Item lumpsilver = new Item("DangerZone:Silver", "res/items/lumpsilver.png");
	public static Item lumpplatinum = new Item("DangerZone:Platinum", "res/items/lumpplatinum.png");
	
	public static Item coingold = new Item("DangerZone:Gold Coin", "res/items/coingold.png");
	public static Item coinsilver = new Item("DangerZone:Silver Coin", "res/items/coinsilver.png");
	public static Item coinplatinum = new Item("DangerZone:Platinum Coin", "res/items/coinplatinum.png");
	
	public static Item woodensword = new ItemSword("DangerZone:Wooden Sword", "res/items/woodensword.png", 200, 6);
	public static Item woodenpickaxe = new ItemPickAxe("DangerZone:Wooden Pickaxe", "res/items/woodenpickaxe.png", 400, 5, 8);
	public static Item woodenaxe = new ItemAxe("DangerZone:Wooden Axe", "res/items/woodenaxe.png", 200, 5, 2);
	public static Item woodenshovel = new ItemShovel("DangerZone:Wooden Shovel", "res/items/woodenshovel.png", 300, 3, 2);
	public static Item woodenhoe = new ItemHoe("DangerZone:Wooden Hoe", "res/items/woodenhoe.png", 200, 1, 3);
	
	public static Item stonesword = new ItemSword("DangerZone:Stone Sword", "res/items/stonesword.png", 400, 8);
	public static Item stonepickaxe = new ItemPickAxe("DangerZone:Stone Pickaxe", "res/items/stonepickaxe.png", 800, 8, 10);
	public static Item stoneaxe = new ItemAxe("DangerZone:Stone Axe", "res/items/stoneaxe.png", 400, 8, 3);
	public static Item stoneshovel = new ItemShovel("DangerZone:Stone Shovel", "res/items/stoneshovel.png", 60, 4, 3);
	public static Item stonehoe = new ItemHoe("DangerZone:Stone Hoe", "res/items/stonehoe.png", 400, 1, 6);
	
	public static Item coppersword = new ItemSword("DangerZone:Copper Sword", "res/items/coppersword.png", 500, 10);
	public static Item copperpickaxe = new ItemPickAxe("DangerZone:Copper Pickaxe", "res/items/copperpickaxe.png", 1200, 9, 12);
	public static Item copperaxe = new ItemAxe("DangerZone:Copper Axe", "res/items/copperaxe.png", 500, 9, 4);
	public static Item coppershovel = new ItemShovel("DangerZone:Copper Shovel", "res/items/coppershovel.png", 1000, 5, 4);
	public static Item copperhoe = new ItemHoe("DangerZone:Copper Hoe", "res/items/copperhoe.png", 500, 2, 7);
	
	public static Item tinsword = new ItemSword("DangerZone:Tin Sword", "res/items/tinsword.png", 600, 12);
	public static Item tinpickaxe = new ItemPickAxe("DangerZone:Tin Pickaxe", "res/items/tinpickaxe.png", 1400, 10, 14);
	public static Item tinaxe = new ItemAxe("DangerZone:Tin Axe", "res/items/tinaxe.png", 600, 10, 5);
	public static Item tinshovel = new ItemShovel("DangerZone:Tin Shovel", "res/items/tinshovel.png", 1200, 6, 5);
	public static Item tinhoe = new ItemHoe("DangerZone:Tin Hoe", "res/items/tinhoe.png", 600, 2, 8);
	
	public static Item silversword = new ItemSword("DangerZone:Silver Sword", "res/items/silversword.png", 800, 14);
	public static Item silverpickaxe = new ItemPickAxe("DangerZone:Silver Pickaxe", "res/items/silverpickaxe.png", 1600, 11, 16);
	public static Item silveraxe = new ItemAxe("DangerZone:Silver Axe", "res/items/silveraxe.png", 800, 11, 6);
	public static Item silvershovel = new ItemShovel("DangerZone:Silver Shovel", "res/items/silvershovel.png", 1400, 7, 6);
	public static Item silverhoe = new ItemHoe("DangerZone:Silver Hoe", "res/items/silverhoe.png", 800, 3, 10);
	
	public static Item platinumsword = new ItemSword("DangerZone:Platinum Sword", "res/items/platinumsword.png", 1600, 24);
	public static Item platinumpickaxe = new ItemPickAxe("DangerZone:Platinum Pickaxe", "res/items/platinumpickaxe.png", 2000, 16, 24);
	public static Item platinumaxe = new ItemAxe("DangerZone:Platinum Axe", "res/items/platinumaxe.png", 1600, 16, 8);
	public static Item platinumshovel = new ItemShovel("DangerZone:Platinum Shovel", "res/items/platinumshovel.png", 1600, 10, 7);
	public static Item platinumhoe = new ItemHoe("DangerZone:Platinum Hoe", "res/items/platinumhoe.png", 1000, 5, 20);
	
	public static Item dark = new ItemDark("DangerZone:Darkness", "res/items/dark.png");
	public static Item light = new ItemLight("DangerZone:Lightness", "res/items/light.png");
	public static Item diamond = new Item("DangerZone:Diamond", "res/items/diamond.png");
	public static Item emerald = new Item("DangerZone:Emerald", "res/items/emerald.png");
	public static Item bloodstone = new Item("DangerZone:Bloodstone", "res/items/bloodstone.png");
	public static Item sunstone = new Item("DangerZone:Sunstone", "res/items/sunstone.png");
	
	public static Item trophycockroach = new ItemTrophy("DangerZone:Cockroach Trophy", "DangerZone:Cockroach", 1.0f, 256);
	public static Item trophybutterfly = new ItemTrophy("DangerZone:Butterfly Trophy", "DangerZone:Butterfly", 0.45f, 256);
	public static Item trophyrat = new ItemTrophy("DangerZone:Rat Trophy", "DangerZone:Rat", 0.50f, 128);
	public static Item trophymoose = new ItemTrophy("DangerZone:Moose Trophy", "DangerZone:Moose", 0.25f, 16);
	public static Item trophyghost = new ItemTrophy("DangerZone:Ghost Trophy", "DangerZone:Ghost", 0.50f, 32);
	public static Item trophyghostskelly = new ItemTrophy("DangerZone:Ghost Skelly Trophy", "DangerZone:GhostSkelly", 0.45f, 32);
	public static Item trophygoose = new ItemTrophy("DangerZone:Goose Trophy", "DangerZone:Goose", 0.45f, 32);
	public static Item trophygosling = new ItemTrophy("DangerZone:Gosling Trophy", "DangerZone:Gosling", 0.95f, 64);
	public static Item trophyostrich = new ItemTrophy("DangerZone:Ostrich Trophy", "DangerZone:Ostrich", 0.40f, 16);
	public static Item trophysparklemuffin = new ItemTrophy("DangerZone:Sparklemuffin Trophy", "DangerZone:Sparklemuffin", 0.35f, 16);
	public static Item trophyskeletorus = new ItemTrophy("DangerZone:Skeletorus Trophy", "DangerZone:Skeletorus", 0.35f, 16);
	public static Item trophyanteater = new ItemTrophy("DangerZone:Anteater Trophy", "DangerZone:Anteater", 0.30f, 16);
	public static Item trophywerewolf = new ItemTrophy("DangerZone:Werewolf Trophy", "DangerZone:Werewolf", 0.45f, 16);
	public static Item trophyvampire = new ItemTrophy("DangerZone:Vampire Trophy", "DangerZone:Vampire", 0.45f, 16);
	public static Item trophyvixen = new ItemTrophy("DangerZone:Vixen Trophy", "DangerZone:Vixen", 0.45f, 16);
	public static Item trophythecount = new ItemTrophy("DangerZone:The Count Trophy", "DangerZone:The Count", 0.45f, 16);
	public static Item trophyvampiremoose = new ItemTrophy("DangerZone:Vampire Moose Trophy", "DangerZone:Vampire Moose", 0.25f, 16);
	public static Item trophyfish = new ItemTrophy("DangerZone:Fish Trophy", "DangerZone:Fish", 0.45f, 32);
	public static Item trophyanotherfish = new ItemTrophy("DangerZone:Another Fish Trophy", "DangerZone:Another Fish", 0.45f, 32);
	public static Item trophypufferfish = new ItemTrophy("DangerZone:Puffer Fish Trophy", "DangerZone:Puffer Fish", 0.45f, 32);
	public static Item trophybutterflyfish = new ItemTrophy("DangerZone:Butterfly Fish Trophy", "DangerZone:Butterfly Fish", 0.45f, 32);
	public static Item trophystickfish = new ItemTrophy("DangerZone:Stick Fish Trophy", "DangerZone:Stick Fish", 0.45f, 32);
	public static Item trophyminnow = new ItemTrophy("DangerZone:Minnow Trophy", "DangerZone:Minnow", 0.45f, 32);
	public static Item trophypiranah = new ItemTrophy("DangerZone:Piranah Trophy", "DangerZone:Piranah", 0.40f, 32);
	public static Item trophysnarler = new ItemTrophy("DangerZone:Snarler Trophy", "DangerZone:Snarler", 0.45f, 32);
	public static Item trophybulletbat = new ItemTrophy("DangerZone:Bullet Bat Trophy", "DangerZone:Bullet Bat", 0.45f, 64);
	public static Item trophymartian = new ItemTrophy("DangerZone:Martian Trophy", "DangerZone:Martian", 0.35f, 32);
	public static Item trophydesertrainfrog = new ItemTrophy("DangerZone:Desert Rain Frog Trophy", "DangerZone:Desert Rain Frog", 0.45f, 32);
	public static Item trophyeel = new ItemTrophy("DangerZone:Eel Trophy", "DangerZone:Eel", 0.40f, 32);
	public static Item trophymermaid = new ItemTrophy("DangerZone:Mermaid Trophy", "DangerZone:Mermaid", 0.45f, 16);
	public static Item trophyhammerhead = new ItemTrophy("DangerZone:Hammerhead Shark Trophy", "DangerZone:Hammerhead Shark", 0.15f, 4);
	
	public static Item goosemeat = new ItemFood("DangerZone:Raw Goose", "res/items/goose_raw.png", 3);
	public static Item goosemeat_cooked = new ItemFood("DangerZone:Cooked Goose", "res/items/goose_cooked.png", 15);
	public static Item ostrichmeat = new ItemFood("DangerZone:Raw Ostrich", "res/items/goose_raw.png", 5);
	public static Item ostrichmeat_cooked = new ItemFood("DangerZone:Cooked Ostrich", "res/items/goose_cooked.png", 20);
	public static Item fishmeat = new ItemFood("DangerZone:Raw Fish", "res/items/fishraw.png", 2);
	public static Item fishmeat_cooked = new ItemFood("DangerZone:Cooked Fish", "res/items/fishcooked.png", 15);
	public static Item moosemeat = new ItemFood("DangerZone:Raw Meat", "res/items/moosemeat.png", 5);
	public static Item moosemeat_cooked = new ItemFood("DangerZone:Cooked Meat", "res/items/moosemeat_cooked.png", 20);
	public static Item moosebone = new Item("DangerZone:Bone", "res/items/moosebone.png");
	public static Item bread = new ItemFood("DangerZone:Awful Gluten Free Bread", "res/items/bread.png", 18);
	public static Item butter = new ItemFood("DangerZone:Butter", "res/items/butter.png", 5);
	public static Item cheese = new ItemFood("DangerZone:Cheese", "res/items/cheese.png", 10);
	
	public static Item feather = new Item("DangerZone:Feather", "res/items/feather.png");
	public static Item radish = new ItemRadish("DangerZone:Radish", "res/items/radish.png");
	public static Item rice = new ItemRice("DangerZone:Rice", "res/items/rice.png");
	public static Item corn = new ItemCorn("DangerZone:Corn", "res/items/corn_seed.png");
	public static Item peach = new ItemFood("DangerZone:Peach", "res/items/peach.png", 8);
	public static Item cherries = new ItemFood("DangerZone:Cherries", "res/items/cherries.png", 6);
	public static Item apple = new ItemFood("DangerZone:Apple", "res/items/apple.png", 5);
	
	public static Item peachseed = new ItemPeachSeed("DangerZone:Peach Pit", "res/items/peachtree_seed.png");
	public static Item cherryseed = new ItemCherrySeed("DangerZone:Cherry Pit", "res/items/cherrytree_seed.png");
	public static Item appleseed = new ItemAppleSeed("DangerZone:Apple Seed", "res/items/appletree_seed.png");
	
	public static Item deadbug = new ItemFood("DangerZone:Dead Bug", "res/items/deadbug.png", 2);
	public static Item bottle = new Item("DangerZone:Bottle", "res/items/bottle.png");
	public static Item experiencebottle = new ItemExpBottle("DangerZone:Experience", "res/items/experiencebottle.png");
	
	public static Item copperhelmet = new ItemArmor("DangerZone:Copper Helmet", "res/items/copperhelmet.png", "res/skins/copperarmor.png", "res/skins/copperarmor2.png", 0.250f, 500, 0);
	public static Item copperchestplate = new ItemArmor("DangerZone:Copper Chestplate", "res/items/copperchestplate.png", "res/skins/copperarmor.png", "res/skins/copperarmor2.png", 0.35f, 500, 1);
	public static Item copperleggings = new ItemArmor("DangerZone:Copper Leggings", "res/items/copperleggings.png", "res/skins/copperarmor.png", "res/skins/copperarmor2.png", 0.250f, 500, 2);
	public static Item copperboots = new ItemArmor("DangerZone:Copper Boots", "res/items/copperboots.png", "res/skins/copperarmor.png", "res/skins/copperarmor2.png", 0.33f, 500, 3);

	public static Item tinhelmet = new ItemArmor("DangerZone:Tin Helmet", "res/items/tinhelmet.png", "res/skins/tinarmor.png", "res/skins/tinarmor2.png", 0.650f, 1000, 0);
	public static Item tinchestplate = new ItemArmor("DangerZone:Tin Chestplate", "res/items/tinchestplate.png", "res/skins/tinarmor.png", "res/skins/tinarmor2.png", 0.75f, 1000, 1);
	public static Item tinleggings = new ItemArmor("DangerZone:Tin Leggings", "res/items/tinleggings.png", "res/skins/tinarmor.png", "res/skins/tinarmor2.png", 0.50f, 1000, 2);
	public static Item tinboots = new ItemArmor("DangerZone:Tin Boots", "res/items/tinboots.png", "res/skins/tinarmor.png", "res/skins/tinarmor2.png", 0.53f, 1000, 3);
	
	public static Item silverhelmet = new ItemArmor("DangerZone:Silver Helmet", "res/items/silverhelmet.png", "res/skins/silverarmor.png", "res/skins/silverarmor2.png", 1.0f, 2000, 0);
	public static Item silverchestplate = new ItemArmor("DangerZone:Silver Chestplate", "res/items/silverchestplate.png", "res/skins/silverarmor.png", "res/skins/silverarmor2.png", 1.25f, 2000, 1);
	public static Item silverleggings = new ItemArmor("DangerZone:Silver Leggings", "res/items/silverleggings.png", "res/skins/silverarmor.png", "res/skins/silverarmor2.png", 1.250f, 2000, 2);
	public static Item silverboots = new ItemArmor("DangerZone:Silver Boots", "res/items/silverboots.png", "res/skins/silverarmor.png", "res/skins/silverarmor2.png", 0.73f, 2000, 3);

	public static Item platinumhelmet = new ItemArmor("DangerZone:Platinum Helmet", "res/items/platinumhelmet.png", "res/skins/platinumarmor.png", "res/skins/platinumarmor2.png", 1.50f, 3000, 0);
	public static Item platinumchestplate = new ItemArmor("DangerZone:Platinum Chestplate", "res/items/platinumchestplate.png", "res/skins/platinumarmor.png", "res/skins/platinumarmor2.png", 1.75f, 3000, 1);
	public static Item platinumleggings = new ItemArmor("DangerZone:Platinum Leggings", "res/items/platinumleggings.png", "res/skins/platinumarmor.png", "res/skins/platinumarmor2.png", 1.50f, 3000, 2);
	public static Item platinumboots = new ItemArmor("DangerZone:Platinum Boots", "res/items/platinumboots.png", "res/skins/platinumarmor.png", "res/skins/platinumarmor2.png", 1.33f, 3000, 3);

	public static Item moosehead = new ItemMooseHead("DangerZone:Moose Head", "res/items/moosehead.png", 1.50f, 3000, 0);
	
	public static Item scubamask = new ItemMask("DangerZone:Scuba Mask", "res/items/scubamask.png", 1.00f, 3000, 0);
	public static Item scubatanks = new ItemTank("DangerZone:Scuba Tanks", "res/items/scubatanks.png", 1.00f, 3000, 1);
	
	public static Item tiara = new ItemTiara("DangerZone:Mermaid Tiara", "res/items/tiara.png", 1.750f, 3000, 0);


	public static Item furball = new ItemFurball("DangerZone:Furball", "res/items/furball.png");
	public static Item vampireteeth = new Item("DangerZone:Vamire Teeth", "res/items/vampireteeth.png");
	public static Item instability = new ItemInstability("DangerZone:Instability", "res/items/instability.png", 1f);
	public static Item instabilitylarge = new ItemInstability("DangerZone:Large Instability", "res/items/instability_large.png", 10f);
	public static Item instabilityhuge = new ItemInstability("DangerZone:Huge Instability", "res/items/instability_huge.png", 100f);	
	public static Item firestick = new ItemFireStick("DangerZone:Spawn Fire", "res/items/firestick.png");
	public static Item autofencekey = new Item("DangerZone:Auto-Fence Key", "res/items/barrierkey.png");
	public static Item dropstick = new ItemDropStick("DangerZone:Drop Stick", "res/items/dropstick.png");
	public static Item string = new ItemSpiderSilk("DangerZone:String", "res/items/spidersilk.png");
	public static Item bucket = new ItemBucket("DangerZone:Bucket", "res/items/bucket.png");
	public static Item bucketwater = new ItemBucketWater("DangerZone:Water Bucket", "res/items/bucketwater.png");
	public static Item bucketmilk = new ItemBucketMilk("DangerZone:Milk Bucket", "res/items/bucketmilk.png", 12);
	public static Item fireball = new ItemFireball("DangerZone:Fireball", "res/items/itemfireball.png", 10);
	public static Item bucketlava = new ItemBucketLava("DangerZone:Lava Bucket", "res/items/bucketlava.png", -50);
	public static Item charcoalstick = new Item("DangerZone:Charcoal Stick", "res/items/charcoal stick.png");
	public static Item woodchips = new Item("DangerZone:Wood Chips", "res/items/woodchips.png");
	public static Item woodpulp = new Item("DangerZone:Wood Pulp", "res/items/woodpulp.png");
	public static Item paper = new Item("DangerZone:Paper", "res/items/paper.png");
	public static Item arrow = new ItemArrow("DangerZone:Arrow", "res/items/arrow.png");
	public static Item bow = new ItemBow("DangerZone:Bow", "res/items/bow.png", 200);
	public static Item bow_empty = new ItemBowEmpty("DangerZone:Empty Bow", "res/items/bow_empty.png", 200);
	public static Item squeaktoy = new ItemSqueakToy("DangerZone:Squeak Toy", "res/items/squeaktoy.png");
	public static Item deadstickfish = new ItemDeadStickFish("DangerZone:Dead Stick Fish", "res/items/deadstickfish.png", 200, 6);
	public static Item pufferfishspikes = new ItemPufferFishSpikes("DangerZone:Puffer Fish Spikes", "res/items/pufferfishspikes.png", 400, 8);
	public static Item sharkfin = new Item("DangerZone:Shark Fin", "res/items/sharkfin.png");
	public static Item sharktooth = new Item("DangerZone:Shark Tooth", "res/items/sharktooth.png");

	
	
	public static Item frog_speed1 = new ItemFrog("DangerZone:Speed 1", "res/items/frog_speed1.png", Effects.SPEED, 1.5f, 200);
	public static Item frog_slowness1 = new ItemFrog("DangerZone:Slowness 1", "res/items/frog_slowness1.png", Effects.SLOWNESS, 2.0f, 200);	
	public static Item frog_speed2 = new ItemFrog("DangerZone:Speed 2", "res/items/frog_speed2.png", Effects.SPEED, 2.5f, 300);
	public static Item frog_slowness2 = new ItemFrog("DangerZone:Slowness 2", "res/items/frog_slowness2.png", Effects.SLOWNESS, 5.0f, 300);	
	public static Item frog_speed3 = new ItemFrog("DangerZone:Speed 3", "res/items/frog_speed3.png", Effects.SPEED, 3.5f, 400);
	public static Item frog_slowness3 = new ItemFrog("DangerZone:Slowness 3", "res/items/frog_slowness3.png", Effects.SLOWNESS, 10.0f, 400);
	
	public static Item frog_strength1 = new ItemFrog("DangerZone:Strength 1", "res/items/frog_strength1.png", Effects.STRENGTH, 1.5f, 200);
	public static Item frog_weakness1 = new ItemFrog("DangerZone:Weakness 1", "res/items/frog_weakness1.png", Effects.WEAKNESS, 1.5f, 200);	
	public static Item frog_strength2 = new ItemFrog("DangerZone:Strength 2", "res/items/frog_strength2.png", Effects.STRENGTH, 2.5f, 300);
	public static Item frog_weakness2 = new ItemFrog("DangerZone:Weakness 2", "res/items/frog_weakness2.png", Effects.WEAKNESS, 2.5f, 300);	
	public static Item frog_strength3 = new ItemFrog("DangerZone:Strength 3", "res/items/frog_strength3.png", Effects.STRENGTH, 4.5f, 400);
	public static Item frog_weakness3 = new ItemFrog("DangerZone:Weakness 3", "res/items/frog_weakness3.png", Effects.WEAKNESS, 4.5f, 400);
	
	public static Item frog_regen1 = new ItemFrog("DangerZone:Regeneration 1", "res/items/frog_regen1.png", Effects.REGENERATION, 0.2f, 200);
	public static Item frog_poison1 = new ItemFrog("DangerZone:Poison 1", "res/items/frog_poison1.png", Effects.POISON, 0.05f, 200);	
	public static Item frog_regen2 = new ItemFrog("DangerZone:Regeneration 2", "res/items/frog_regen2.png", Effects.REGENERATION, 0.4f, 300);
	public static Item frog_poison2 = new ItemFrog("DangerZone:Poison 2", "res/items/frog_poison2.png", Effects.POISON, 0.1f, 300);	
	public static Item frog_regen3 = new ItemFrog("DangerZone:Regeneration 3", "res/items/frog_regen3.png", Effects.REGENERATION, 1.0f, 400);
	public static Item frog_poison3 = new ItemFrog("DangerZone:Poison 3", "res/items/frog_poison3.png", Effects.POISON, 0.2f, 400);
	
	public static Item frog_confusion1 = new ItemFrog("DangerZone:Confusion 1", "res/items/frog_confusion1.png", Effects.CONFUSION, 2f, 200);
	public static Item frog_confusion2 = new ItemFrog("DangerZone:Confusion 2", "res/items/frog_confusion2.png", Effects.CONFUSION, 4f, 300);
	public static Item frog_confusion3 = new ItemFrog("DangerZone:Confusion 3", "res/items/frog_confusion3.png", Effects.CONFUSION, 8f, 400);
	public static Item frog_morph1 = new ItemFrog("DangerZone:Morph 1", "res/items/frog_morph1.png", Effects.MORPH, 2f, 200);
	public static Item frog_morph2 = new ItemFrog("DangerZone:Morph 2", "res/items/frog_morph2.png", Effects.MORPH, 2f, 400);
	public static Item frog_morph3 = new ItemFrog("DangerZone:Morph 3", "res/items/frog_morph3.png", Effects.MORPH, 2f, 800);
	
	public static Item scrollaccuracy = new ItemScroll("DangerZone:Accuracy Scroll I", "res/items/scrollaccuracy.png", ItemAttribute.ACCURACY);
	public static Item scrolldamage = new ItemScroll("DangerZone:Damage Scroll I", "res/items/scrolldamage.png", ItemAttribute.DAMAGE);
	public static Item scrolldurability = new ItemScroll("DangerZone:Durability Scroll I", "res/items/scrolldurability.png", ItemAttribute.DURABILITY);
	public static Item scrollreach = new ItemScroll("DangerZone:Reach Scroll I", "res/items/scrollreach.png", ItemAttribute.REACH);
	public static Item scrollspam = new ItemScroll("DangerZone:Spam Scroll I", "res/items/scrollspam.png", ItemAttribute.SPAM);
	
	public static Item scrollaccuracyII = new ItemScroll("DangerZone:Accuracy Scroll II", "res/items/scrollaccuracy.png", ItemAttribute.ACCURACY, 2);
	public static Item scrolldamageII = new ItemScroll("DangerZone:Damage Scroll II", "res/items/scrolldamage.png", ItemAttribute.DAMAGE, 2);
	public static Item scrolldurabilityII = new ItemScroll("DangerZone:Durability Scroll II", "res/items/scrolldurability.png", ItemAttribute.DURABILITY, 2);
	public static Item scrollreachII = new ItemScroll("DangerZone:Reach Scroll II", "res/items/scrollreach.png", ItemAttribute.REACH, 2);
	public static Item scrollspamII = new ItemScroll("DangerZone:Spam Scroll II", "res/items/scrollspam.png", ItemAttribute.SPAM, 2);
	
	public static Item scrollaccuracyIII = new ItemScroll("DangerZone:Accuracy Scroll III", "res/items/scrollaccuracy.png", ItemAttribute.ACCURACY, 3);
	public static Item scrolldamageIII = new ItemScroll("DangerZone:Damage Scroll III", "res/items/scrolldamage.png", ItemAttribute.DAMAGE, 3);
	public static Item scrolldurabilityIII = new ItemScroll("DangerZone:Durability Scroll III", "res/items/scrolldurability.png", ItemAttribute.DURABILITY, 3);
	public static Item scrollreachIII = new ItemScroll("DangerZone:Reach Scroll III", "res/items/scrollreach.png", ItemAttribute.REACH, 3);
	public static Item scrollspamIII = new ItemScroll("DangerZone:Spam Scroll III", "res/items/scrollspam.png", ItemAttribute.SPAM, 3);
	
	public static Item scrollaccuracyIV = new ItemScroll("DangerZone:Accuracy Scroll IV", "res/items/scrollaccuracy.png", ItemAttribute.ACCURACY, 4);
	public static Item scrolldamageIV = new ItemScroll("DangerZone:Damage Scroll IV", "res/items/scrolldamage.png", ItemAttribute.DAMAGE, 4);
	public static Item scrolldurabilityIV = new ItemScroll("DangerZone:Durability Scroll IV", "res/items/scrolldurability.png", ItemAttribute.DURABILITY, 4);
	public static Item scrollreachIV = new ItemScroll("DangerZone:Reach Scroll IV", "res/items/scrollreach.png", ItemAttribute.REACH, 4);
	public static Item scrollspamIV = new ItemScroll("DangerZone:Spam Scroll IV", "res/items/scrollspam.png", ItemAttribute.SPAM, 4);
	
	public static Item scrollaccuracyV = new ItemScroll("DangerZone:Accuracy Scroll V", "res/items/scrollaccuracy.png", ItemAttribute.ACCURACY, 5);
	public static Item scrolldamageV = new ItemScroll("DangerZone:Damage Scroll V", "res/items/scrolldamage.png", ItemAttribute.DAMAGE, 5);
	public static Item scrolldurabilityV = new ItemScroll("DangerZone:Durability Scroll V", "res/items/scrolldurability.png", ItemAttribute.DURABILITY, 5);
	public static Item scrollreachV = new ItemScroll("DangerZone:Reach Scroll V", "res/items/scrollreach.png", ItemAttribute.REACH, 5);
	public static Item scrollspamV = new ItemScroll("DangerZone:Spam Scroll V", "res/items/scrollspam.png", ItemAttribute.SPAM, 5);
	
	public static Item scrollharm = new ItemScrollMagic("DangerZone:Harm Scroll", "res/items/scrollharm.png", 0);
	public static Item scrollheal = new ItemScrollMagic("DangerZone:Heal Scroll", "res/items/scrollheal.png", 1);
	
	public static Item drone = new ItemDrone("DangerZone:Drone", "res/items/drone.png");
	public static Item drone_motor = new Item("DangerZone:Drone Motor", "res/items/drone_motor.png");
	public static Item drone_body = new Item("DangerZone:Drone Frame", "res/items/drone_body.png");
	public static Item drone_camera = new Item("DangerZone:Drone Camera", "res/items/drone_camera.png");
	
	
	public static Item ItemArray[];
	public static final int itemsMAX = 2048;
	public static Properties prop = null;
		
	public Items(){
		int i;
		ItemArray = new Item[itemsMAX];
		for(i=0;i<itemsMAX;i++){
			ItemArray[i] = null;
		}
	}
	
	public static int registerItem(Item b){
		int i=0;
		
		for(i=1;i<itemsMAX;i++){
			if(ItemArray[i] != null){
				if(ItemArray[i].uniquename.equals(b.uniquename)){ //duplicate!!!
					return 0; //duplicate!!!
				}
			}
		}
		
		for(i=1;i<itemsMAX;i++){
			if(ItemArray[i] == null)break;
		}
		if(i >= itemsMAX-1)return 0;
		
		if(prop != null)i = Utils.getPropertyInt(prop, b.uniquename, 1, itemsMAX-1, i);
		
		//existing or new item
		if(ItemArray[i] == null){ //Slot is open, this is good. Give it the same ID it had last time!
			//System.out.printf("Empty spot at %d for item %s\n", i, b.uniquename);
			ItemArray[i] = b;
			b.itemID = i;
			if(prop != null)prop.setProperty(b.uniquename, String.format("%d", i)); //next time we will find it!
			return i;
		}else{ 
			//System.out.printf("NON-Empty spot at %d for item %s\n", i, b.uniquename );
			//Oops. Slot already taken.
			//Give this slot to the existing one, and bump the intruder to a new slot.
			//Should only happen when adding new mods and they get loaded before old ones.
			Item intruder = ItemArray[i];
			int isave = i;
			ItemArray[i] = b;
			b.itemID = i;
			for(i=1;i<itemsMAX;i++){
				if(ItemArray[i] == null){
					ItemArray[i] = intruder;
					intruder.itemID = i;
					//System.out.printf("Original moved to %d for item %s\n", i, intruder.uniquename );
					if(prop != null)prop.setProperty(intruder.uniquename, String.format("%d", i)); //next time we will find it!
					return isave;
				}
			}
			return 0;
		}
		
	}
	
	public static void reRegisterItemAt(String s, int loc){
		int i;
		
		if(loc <= 0 || loc >= itemsMAX)return;
		if(s == null || s.equals(""))return;
		
		if(ItemArray[loc] != null){
			if(s.equals(ItemArray[loc].uniquename)){
				//System.out.printf("Already in correct spot at %d for item %s\n", loc, s);
				return; //already where its supposed to be! :)
			}
			//maybe it is somewhere else?
			for(i=1;i<itemsMAX;i++){
				if(ItemArray[i] != null){
					if(s.equals(ItemArray[i].uniquename)){
						//Take it, and move the intruder where I was
						//System.out.printf("Taking spot at %d for item %s\n", loc, s);
						Item me = ItemArray[i];
						Item intruder = ItemArray[loc];
						ItemArray[loc] = me;
						ItemArray[loc].itemID = loc;
						ItemArray[i] = intruder;
						ItemArray[i].itemID = i;
						//System.out.printf("previous moved %d for item %s\n", i, intruder.uniquename);
						return;
					}
				}
			}
		}else{
			//maybe it is somewhere else?
			for(i=1;i<itemsMAX;i++){
				if(ItemArray[i] != null){
					if(s.equals(ItemArray[i].uniquename)){
						//System.out.printf("In wrong spot, fixing spot at %d for item %s\n", loc, s);
						//Move it!
						ItemArray[loc] = ItemArray[i];
						ItemArray[loc].itemID = loc;
						ItemArray[i] = null;
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
		filepath = String.format("worlds/%s/itemIDs.dat", DangerZone.worldname);	
		 
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
				//e.printStackTrace();
			}
		}		
	}
	
	public static void save(){

		OutputStream output = null;
		String filepath = new String();		
		filepath = String.format("worlds/%s/itemIDs.dat", DangerZone.worldname);	
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
	
	
	public static boolean shouldShow(int itemid){
		if(itemid <= 0)return false;
		if(itemid >= Items.itemsMAX)return false;
		if(Items.ItemArray[itemid]==null)return false;
		return Items.ItemArray[itemid].showInInventory;
	}
	
	public static Texture getTexture(int itemid){
		if(itemid <= 0)return null;
		if(itemid >= Items.itemsMAX)return null;
		if(Items.ItemArray[itemid]==null)return null;
		return Items.ItemArray[itemid].getTexture();
	}
	
	public static void renderMe(WorldRenderer wr, World w, int d, int x, int y, int z, int itemid){
		if(itemid <= 0)return;
		if(itemid >= Items.itemsMAX)return;
		if(Items.ItemArray[itemid]==null)return;
		Items.ItemArray[itemid].renderMe(wr, w, d, x, y, z, itemid);
	}
	
	public static void renderMeHeld(WorldRenderer wr, Entity e, int itemid, boolean isdisplay){
		if(itemid <= 0)return;
		if(itemid >= Items.itemsMAX)return;
		if(Items.ItemArray[itemid]==null)return;
		Items.ItemArray[itemid].renderMeHeld(wr, e, itemid, isdisplay);
	}
	
	public static int getMaxStack(int itemid){
		if(itemid <= 0)return 0;
		if(itemid >= Items.itemsMAX)return 0;
		if(Items.ItemArray[itemid]==null)return 0;
		return Items.ItemArray[itemid].maxstack;
	}
	
	public static int getAttackStrength(int itemid){
		if(itemid <= 0)return 0;
		if(itemid >= Items.itemsMAX)return 0;
		if(Items.ItemArray[itemid]==null)return 0;
		return Items.ItemArray[itemid].attackstrength;
	}
	
	public static int getWoodStrength(int itemid){
		if(itemid <= 0)return 0;
		if(itemid >= Items.itemsMAX)return 0;
		if(Items.ItemArray[itemid]==null)return 0;
		return Items.ItemArray[itemid].woodstrength;
	}
	
	public static int getStoneStrength(int itemid){
		if(itemid <= 0)return 0;
		if(itemid >= Items.itemsMAX)return 0;
		if(Items.ItemArray[itemid]==null)return 0;
		return Items.ItemArray[itemid].stonestrength;
	}
	
	public static int getDirtStrength(int itemid){
		if(itemid <= 0)return 0;
		if(itemid >= Items.itemsMAX)return 0;
		if(Items.ItemArray[itemid]==null)return 0;
		return Items.ItemArray[itemid].dirtstrength;
	}
	
	public static int getMaxUses(int itemid){
		if(itemid <= 0)return 0;
		if(itemid >= Items.itemsMAX)return 0;
		if(Items.ItemArray[itemid]==null)return 0;
		return Items.ItemArray[itemid].maxuses;
	}
	
	public static boolean rightClickOnBlock(int itemid, Player p, int d, int x, int y, int z, int side){
		if(itemid <= 0)return true;
		if(itemid >= Items.itemsMAX)return true;
		if(Items.ItemArray[itemid]==null)return true;
		return Items.ItemArray[itemid].rightClickOnBlock(p, d, x, y, z, side);
	}
	
	public static Item getItem(int itemid){
		if(itemid <= 0)return null;
		if(itemid >= Items.itemsMAX)return null;
		return Items.ItemArray[itemid];
	}
	
	public static String getUniqueName(int itemid){
		if(itemid <= 0)return null;
		if(itemid >= Items.itemsMAX)return null;
		if(Items.ItemArray[itemid]==null)return null;
		return Items.ItemArray[itemid].uniquename;
	}
	
	public static int findByName(String name){
		int i;
		if(name == null)return 0;
		for(i=1;i<itemsMAX;i++){
			if(Items.ItemArray[i] != null){
				if(Items.ItemArray[i].uniquename != null){
					if(name.equals(Items.ItemArray[i].uniquename)){
						return i;
					}
				}
			}
		}
		return 0;
	}
	
	public static float getLightLevel(int itemid){
		if(itemid <= 0)return 0;
		if(itemid >= Items.itemsMAX)return 0;
		if(Items.ItemArray[itemid]==null)return 0;
		return Items.ItemArray[itemid].brightness;
	}
	
	public static float getFoodValue(int itemid){
		if(itemid <= 0)return 0;
		if(itemid >= Items.itemsMAX)return 0;
		if(Items.ItemArray[itemid]==null)return 0;
		return Items.ItemArray[itemid].foodvalue;
	}
	
	public static boolean isFood(int itemid){
		if(itemid <= 0)return false;
		if(itemid >= Items.itemsMAX)return false;
		if(Items.ItemArray[itemid]==null)return false;
		return Items.ItemArray[itemid].isfood;
	}
	
	public static boolean isValid(int itemid){
		if(itemid <= 0)return false;
		if(itemid >= Items.itemsMAX)return false;
		if(Items.ItemArray[itemid]==null)return false;
		return true;
	}
	
	public static boolean eatAnyTime(int itemid){
		if(itemid <= 0)return false;
		if(itemid >= Items.itemsMAX)return false;
		if(Items.ItemArray[itemid]==null)return false;
		return Items.ItemArray[itemid].eatanytime;
	}
	
	public static int getBurnTime(int itemid){
		if(itemid <= 0)return 0;
		if(itemid >= Items.itemsMAX)return 0;
		if(Items.ItemArray[itemid]==null)return 0;
		return Items.ItemArray[itemid].burntime;
	}
	
	public static float getfullholdcount(int itemid){
		if(itemid <= 0)return 0;
		if(itemid >= Items.itemsMAX)return 0;
		if(Items.ItemArray[itemid]==null)return 0;
		return Items.ItemArray[itemid].getfullholdcount();
	}
	
	public static int getMenu(int itemid){
		if(itemid <= 0)return InventoryMenus.GENERIC;
		if(itemid >= Items.itemsMAX)return InventoryMenus.GENERIC;
		if(Items.ItemArray[itemid]==null)return InventoryMenus.GENERIC;
		return Items.ItemArray[itemid].menu;
	}
	
	public static boolean isFlipped(int itemid){
		if(itemid <= 0)return false;
		if(itemid >= Items.itemsMAX)return false;
		if(Items.ItemArray[itemid]==null)return false;
		return Items.ItemArray[itemid].flipped;
	}
	
	public static boolean isFlopped(int itemid){
		if(itemid <= 0)return false;
		if(itemid >= Items.itemsMAX)return false;
		if(Items.ItemArray[itemid]==null)return false;
		return Items.ItemArray[itemid].flopped;
	}

}