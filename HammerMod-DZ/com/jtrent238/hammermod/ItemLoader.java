package com.jtrent238.hammermod;

import com.jtrent238.hammermod.items.hammers.ItemDirtHammer;
import com.jtrent238.hammermod.items.hammers.ItemGlassHammer;
import com.jtrent238.hammermod.items.hammers.ItemStoneHammer;
import com.jtrent238.hammermod.items.hammers.ItemWoodHammer;

import dangerzone.gui.InventoryMenus;
import dangerzone.items.Item;
import dangerzone.items.Items;

public class ItemLoader {

	/*
	 * Basic Minecraft Hammers (Ex. Vannila Ores)
	 */
	public static Item ItemBaseHammer;
	public static Item ItemWoodHammer;
	public static Item ItemStoneHammer;
	public static Item ItemIronHammer;
	public static Item ItemGoldHammer;
	public static Item ItemDiamondHammer;
	public static Item ItemDirtHammer;
	public static Item ItemGlassHammer;
	public static Item ItemSandHammer;
	public static Item ItemCactusHammer;
	public static Item ItemGravelHammer;
	public static Item ItemWoolHammer_white;
	public static Item ItemEmeraldHammer;
	public static Item ItemGrassHammer;
	public static Item ItemObsidianHammer;
	public static Item ItemGlowstoneHammer;
	public static Item ItemRedstoneHammer;
	public static Item ItemLapizHammer;
	public static Item ItemNetherackHammer;
	public static Item ItemSoulSandHammer;
	public static Item ItemCoalHammer;
	public static Item ItemCharcoalHammer;
	public static Item ItemEndstoneHammer;
	public static Item ItemBoneHammer;
	public static Item ItemSpongeHammer;
	public static Item ItemBrickHammer;
	public static Item ItemSugarHammer;
	public static Item ItemSlimeHammer;
	public static Item ItemMelonHammer;
	public static Item ItemPumpkinHammer;
	public static Item ItemPotatoHammer;
	public static Item ItemCarrotHammer;
	public static Item ItemAppleHammer;
	public static Item ItemIceHammer;
	public static Item ItemPackedIceHammer;
	public static Item ItemSnowHammer;
	public static Item ItemCakeHammer;
	public static Item ItemDragonEggHammer;
	

	public static Item ItemTntHammer;
	public static Item ItemBedrockHammer;
	
	/*
	 * Mob Hammers
	 */
	public static Item ItemCreeperHammer;
	public static Item ItemPigHammer;
	public static Item ItemCowHammer;
	
	
	/*
	 * Hammers Using Ores from other mods
	 * **NOTE: REQUIRES Other mods to craft these hammers**
	 */
	public static Item ItemCopperHammer;
	public static Item ItemBronzeHammer;
	public static Item ItemTungstenHammer;
	public static Item ItemRubyHammer;
	public static Item ItemTinHammer;
	public static Item ItemSilverHammer;
	public static Item ItemJadeHammer;
	public static Item ItemAmethystHammer;
	public static Item ItemGraphiteHammer;
	public static Item ItemCitrineHammer;
	public static Item ItemPierreHammer;
	public static Item ItemSapphireHammer;
	public static Item ItemOnyxHammer;
	public static Item ItemNikoliteHammer;
	public static Item ItemSilicaHammer;
	public static Item ItemCinnabarHammer;
	public static Item ItemAmberBearingStoneHammer;
	public static Item ItemFerrousHammer;
	public static Item ItemAdaminiteHammer;
	public static Item ItemShinyHammer;
	public static Item ItemXychoriumHammer;
	public static Item ItemUraniumHammer;
	public static Item ItemTitaniumHammer;
	public static Item ItemBloodStoneHammer;
	public static Item ItemRustedHammer;
	public static Item ItemRositeHammer;
	public static Item ItemLimoniteHammer;
	public static Item ItemMithrilHammer;
	public static Item ItemPrometheumHammer;
	public static Item ItemHepatizonHammer;
	public static Item ItemPoopHammer;
	public static Item ItemAngmallenHammer;
	public static Item ItemManganeseHammer;
	public static Item ItemSearedBrickHammer;
	public static Item ItemElectrumHammer;
	public static Item ItemPigIronHammer;
	public static Item ItemArditeHammer;
	public static Item ItemAlumiteHammer;
	public static Item ItemCobaltHammer;
	public static Item ItemManyullynHammer;
	public static Item ItemOureclaseHammer;
	public static Item ItemHaderothHammer;
	public static Item ItemInfuscoliumHammer;
	public static Item ItemRubberHammer;
	public static Item ItemDesichalkosHammer;
	public static Item ItemMeutoiteHammer;
	public static Item ItemEximiteHammer;
	public static Item ItemMidasiumHammer;
	public static Item ItemSanguiniteHammer;
	public static Item ItemInolashiteHammer;
	public static Item ItemVulcaniteHammer;
	public static Item ItemLemuriteHammer;
	public static Item ItemAmordrineHammer;
	public static Item ItemCeruclaseHammer;
	public static Item ItemKalendriteHammer;
	public static Item ItemVyroxeresHammer;
	public static Item ItemCarmotHammer;
	public static Item ItemTartariteHammer;
	public static Item ItemAtlarusHammer;
	public static Item ItemAstralHammer;
	public static Item ItemCelenegilHammer;
	public static Item ItemAredriteHammer;
	public static Item ItemOrichalcumHammer;
	
	/*
	 * Hammers For YouTubers
	 */
	public static Item ItemPatHammer;
	public static Item ItemJenHammer;
	public static Item ItemDanTDMHammer;
	public static Item ItemxJSQHammer;
	public static Item ItemSkyTheKidRSHammer;
	public static Item ItemThackAttack_MCHammer;
	public static Item Item_MrGregor_Hammer;
	
	/*
	 * Hammers For Twitch Streamers
	 */
	public static Item ItemDeeAxelJayHammer;
	public static Item ItemincapablegamerHammer;
	
	/*
	 * Community Hammers
	 */
	public static Item ItemCryingObsidainHammer;
	public static Item ItemMythicalHammer;
	public static Item ItemToasterHammer;
	
	/*
	 * Special Hammers
	 */
	public static Item ItemRainbowHammer;
	public static Item ItemMissingTextureHammer;
	
	/*
	 * Custom Hammers
	 */
	public static Item ItemCustomHammer_1;
	
	public static void LoadItems() {
		//maxstack = 1;
		//attackstrength = 5;
		//burntime = 15;
		//hold_straight = true;
		//flopped = false;
		//menu = InventoryMenus.HARDWARE;
		//this.showInInventory = true;
		ItemWoodHammer = new ItemWoodHammer("hammermod:ItemWoodHammer", "HammerMod-DZ_res/items/ItemWoodHammer.png");
		ItemStoneHammer = new ItemStoneHammer("hammermod:ItemStoneHammer", "HammerMod-DZ_res/items/ItemStoneHammer.png");
//		ItemIronHammer = new ItemIronHammer(ToolMaterial.IRON).setUnlocalizedName("ItemIronHammer").setTextureName("hammermod:ItemIronHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemGoldHammer = new ItemGoldHammer(ToolMaterial.GOLD).setUnlocalizedName("ItemGoldHammer").setTextureName("hammermod:ItemGoldHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemDiamondHammer = new ItemDiamondHammer(ToolMaterial.EMERALD).setUnlocalizedName("ItemDiamondHammer").setTextureName("hammermod:ItemDiamondHammer").setCreativeTab(HammerMod.HammerMod);
		ItemDirtHammer = new ItemDirtHammer("hammermod:ItemDirtHammer", "HammerMod-DZ_res/items/ItemDirtHammer.png");
		ItemGlassHammer = new ItemGlassHammer("hammermod:ItemGlassHammer", "HammerMod-DZ_res/items/ItemGlassHammer.png");
//		ItemSandHammer = new ItemSandHammer(SAND).setUnlocalizedName("ItemSandHammer").setTextureName("hammermod:ItemSandHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemCactusHammer = new ItemCactusHammer(CACTUS).setUnlocalizedName("ItemCactusHammer").setTextureName("hammermod:ItemCactusHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemGravelHammer = new ItemGravelHammer(GRAVEL).setUnlocalizedName("ItemGravelHammer").setTextureName("hammermod:ItemGravelHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemWoolHammer_white = new ItemWoolHammer(WOOL).setUnlocalizedName("ItemWoolHammer_white").setTextureName("hammermod:ItemWoolHammer_white").setCreativeTab(HammerMod.HammerMod);
//		ItemEmeraldHammer = new ItemEmeraldHammer(EMERALD).setUnlocalizedName("ItemEmeraldHammer").setTextureName("hammermod:ItemEmeraldHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemGrassHammer = new ItemGrassHammer(GRASS).setUnlocalizedName("ItemGrassHammer").setTextureName("hammermod:ItemGrassHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemObsidianHammer = new ItemObsidianHammer(OBSIDIAN).setUnlocalizedName("ItemObsidianHammer").setTextureName("hammermod:ItemObsidianHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemGlowstoneHammer = new ItemGlowstoneHammer(GLOWSTONE).setUnlocalizedName("ItemGlowstoneHammer").setTextureName("hammermod:ItemGlowstoneHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemRedstoneHammer = new ItemRedstoneHammer(REDSTONE).setUnlocalizedName("ItemRedstoneHammer").setTextureName("hammermod:ItemRedstoneHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemLapizHammer = new ItemLapizHammer(LAPIZ).setUnlocalizedName("ItemLapizHammer").setTextureName("hammermod:ItemLapisHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemNetherackHammer = new ItemNetherackHammer(NETHERACK).setUnlocalizedName("ItemNetherackHammer").setTextureName("hammermod:ItemNetherackHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemSoulSandHammer = new ItemSoulSandHammer(SOULSAND).setUnlocalizedName("ItemSoulSandHammer").setTextureName("hammermod:ItemSoulSandHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemCoalHammer = new ItemCoalHammer(COAL).setUnlocalizedName("ItemCoalHammer").setTextureName("hammermod:ItemCoalHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemCharcoalHammer = new ItemCharcoalHammer(CHARCOAL).setUnlocalizedName("ItemCharcoalHammer").setTextureName("hammermod:ItemCharcoalHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemEndstoneHammer = new ItemEndstoneHammer(ENDSTONE).setUnlocalizedName("ItemEndstoneHammer").setTextureName("hammermod:ItemEndstoneHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemBoneHammer = new ItemBoneHammer(BONE).setUnlocalizedName("ItemBoneHammer").setTextureName("hammermod:ItemBoneHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemSpongeHammer = new ItemSpongeHammer(SPONGE).setUnlocalizedName("ItemSpongeHammer").setTextureName("hammermod:ItemSpongeHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemBrickHammer = new ItemBrickHammer(BRICK).setUnlocalizedName("ItemBrickHammer").setTextureName("hammermod:ItemBrickHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemSugarHammer = new ItemSugarHammer(SUGAR).setUnlocalizedName("ItemSugarHammer").setTextureName("hammermod:ItemSugarHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemSlimeHammer = new ItemSlimeHammer(SLIME).setUnlocalizedName("ItemSlimeHammer").setTextureName("hammermod:ItemSlimeHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemMelonHammer = new ItemMelonHammer(MELON).setUnlocalizedName("ItemMelonHammer").setTextureName("hammermod:ItemMelonHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemPumpkinHammer = new ItemPumpkinHammer(PUMPKIN).setUnlocalizedName("ItemPumpkinHammer").setTextureName("hammermod:ItemPumpkinHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemPotatoHammer = new ItemPotatoHammer(POTATO).setUnlocalizedName("ItemPotatoHammer").setTextureName("hammermod:ItemPotatoHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemCarrotHammer = new ItemCarrotHammer(CARROT).setUnlocalizedName("ItemCarrotHammer").setTextureName("hammermod:ItemCarrotHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemAppleHammer = new ItemAppleHammer(APPLE).setUnlocalizedName("ItemAppleHammer").setTextureName("hammermod:ItemAppleHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemIceHammer = new ItemIceHammer(ICE).setUnlocalizedName("ItemIceHammer").setTextureName("hammermod:ItemIceHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemPackedIceHammer = new ItemPackedIceHammer(PACKED_ICE).setUnlocalizedName("ItemPackedIceHammer").setTextureName("hammermod:ItemPackedIceHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemSnowHammer = new ItemSnowHammer(SNOW).setUnlocalizedName("ItemSnowHammer").setTextureName("hammermod:ItemSnowHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemCakeHammer = new ItemCakeHammer(CAKE).setUnlocalizedName("ItemCakeHammer").setTextureName("hammermod:ItemCakeHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemDragonEggHammer = new ItemDragonEggHammer(DRAGEGG).setUnlocalizedName("ItemDragonEggHammer").setTextureName("hammermod:ItemDragonEggHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemTntHammer = new ItemTntHammer(TNT).setUnlocalizedName("ItemTntHammer").setTextureName("hammermod:ItemTntHammer").setCreativeTab(HammerMod.HammerMod);
//		ItemBedrockHammer = new ItemBedrockHammer(BEDROCK).setUnlocalizedName("ItemBedrockHammer").setTextureName("hammermod:ItemBedrockHammer").setCreativeTab(HammerMod.HammerMod);

		registerItems();
	}

	private static void registerItems() {
		
		Items.registerItem(ItemWoodHammer);
		Items.registerItem(ItemStoneHammer);
		Items.registerItem(ItemDirtHammer);
		Items.registerItem(ItemGlassHammer);
		
	}
	
}
