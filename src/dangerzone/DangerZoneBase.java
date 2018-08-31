package dangerzone;

import dangerzone.biomes.BiomeManager;
import dangerzone.biomes.OverWorldBiome;
import dangerzone.biomes.BigTreeBiome;
import dangerzone.biomes.OverWorldBiomeManager;
import dangerzone.biomes.PathwayBiome;
import dangerzone.biomes.PleasantFieldsBiome;
import dangerzone.biomes.PleasantHillsBiome;
import dangerzone.biomes.PleasantPlainsBiome;
import dangerzone.biomes.RuggedBiome;
import dangerzone.biomes.RuggedHillsBiome;
import dangerzone.biomes.RuggedPlainsBiome;
import dangerzone.biomes.RuggedDesertBiome;
import dangerzone.biomes.SkyIslandBiome;
import dangerzone.biomes.WindsweptBiome;
import dangerzone.blocks.Blocks;
import dangerzone.blocks.ModelStand;
import dangerzone.entities.*;
import dangerzone.gui.InventoryMenus;
import dangerzone.items.Items;
import dangerzone.particles.ParticleBreak;
import dangerzone.particles.ParticleConfusion;
import dangerzone.particles.ParticleDeath;
import dangerzone.particles.ParticleDroplet;
import dangerzone.particles.ParticleDust;
import dangerzone.particles.ParticleFire;
import dangerzone.particles.ParticleHappy;
import dangerzone.particles.ParticleHurt;
import dangerzone.particles.ParticleLeaves;
import dangerzone.particles.ParticleMorph;
import dangerzone.particles.ParticlePoison;
import dangerzone.particles.ParticleRain;
import dangerzone.particles.ParticleRegeneration;
import dangerzone.particles.ParticleSlowness;
import dangerzone.particles.ParticleSmoke;
import dangerzone.particles.ParticleSparkle;
import dangerzone.particles.ParticleSpeed;
import dangerzone.particles.ParticleStrength;
import dangerzone.particles.ParticleWaterSpout;
import dangerzone.particles.ParticleWeakness;
import dangerzone.particles.Particles;
import dangerzone.thingstodo.ArmoredUp;
import dangerzone.thingstodo.AxeCrafter;
import dangerzone.thingstodo.BlockPlaced;
import dangerzone.thingstodo.CompareCrafter;
import dangerzone.thingstodo.DimensionChange;
import dangerzone.thingstodo.Eaten;
import dangerzone.thingstodo.FenceBuilder;
import dangerzone.thingstodo.FoundOnGround;
import dangerzone.thingstodo.HelmetCrafter;
import dangerzone.thingstodo.HoeCrafter;
import dangerzone.thingstodo.ItemRightClickedBlock;
import dangerzone.thingstodo.LeveledUp;
import dangerzone.thingstodo.MobKiller;
import dangerzone.thingstodo.MobSpawner;
import dangerzone.thingstodo.MobTamer;
import dangerzone.thingstodo.Murderer;
import dangerzone.thingstodo.PetFed;
import dangerzone.thingstodo.PickaxeCrafter;
import dangerzone.thingstodo.Psycho;
import dangerzone.thingstodo.Rider;
import dangerzone.thingstodo.ScrollCrafter;
import dangerzone.thingstodo.ShovelCrafter;
import dangerzone.thingstodo.SpellCast;
import dangerzone.thingstodo.SwordCrafter;

public class DangerZoneBase extends BaseMod {

	public static OverWorldBiomeManager overworld_biomemanager;
	public static OverWorldBiome overworldforest;
	public static OverWorldBiome overworldtallforest;
	public static OverWorldBiome overworldtallwillowforest;
	public static OverWorldBiome overworldscragglyforest;

	public static BiomeManager bigtree_biomemanager;
	public static BigTreeBiome my_biome2;
	public static BigTreeBiome my_biome2a;
	public static BigTreeBiome my_biome2b;
	
	public static BiomeManager pathway_biomemanager;
	public static PathwayBiome my_biome3;
	public static PathwayBiome my_biome3a;
	public static PathwayBiome my_biome3b;
	public static PathwayBiome my_biome3c;
	public static PathwayBiome my_biome3d;
	public static PathwayBiome my_biome3e;
	
	public static BiomeManager rugged_biomemanager;
	public static RuggedBiome ruggedmountains;
	public static RuggedBiome ruggedhills;
	public static RuggedBiome ruggedplains;
	public static RuggedBiome ruggedplains2;
	public static RuggedBiome ruggedplainsdesert;
	
	public static BiomeManager sky_biomemanager;
	public static SkyIslandBiome my_biome5;
	
	public static BiomeManager windsweptmanager;
	public static WindsweptBiome windswept;
	
	//public static BiomeManager my_biomemanager6;
	//public static TestBiome my_biome6;
	
	public static BiomeManager pleasant_biomemanager;
	public static RuggedBiome pleasanthills1;
	public static RuggedBiome pleasanthills2;
	public static RuggedBiome pleasantplains1;
	public static RuggedBiome pleasantplains2;
	public static RuggedBiome pleasantfields1;
	public static RuggedBiome pleasantfields2;

	
	
	
	public static DZWorldDecorator fengshui;
	
	public static FurnaceInventoryPacket furnacepacket;
	
	//
	//ENTITY MODELS ARE SHARED SO THAT GRAPHICS DOESN'T RUN OUT OF COMPILE LISTS AND CRASH!
	//Many entites, one model.
	//ModelTrophy is just a pass-through, so it's not here...
	//But each of the ModelTrophy entities all point back to these models!
	//
	public static ModelButterfly modelbutterfly = new ModelButterfly();
	public static ModelRat modelrat = new ModelRat();
	public static ModelMoose modelmoose = new ModelMoose();
	public static ModelCockroach modelcockroach = new ModelCockroach();
	public static ModelGhost modelghost = new ModelGhost();
	public static ModelGhostSkelly modelghostskelly = new ModelGhostSkelly();
	public static ModelGoose modelgoose = new ModelGoose();
	public static ModelFuzzButt modelfuzzbutt = new ModelFuzzButt();
	public static ModelAnteater modelanteater = new ModelAnteater();
	public static ModelHumanoid modelhumanoid = new ModelHumanoid();
	public static ModelOstrich modelostrich = new ModelOstrich();
	public static ModelLightning modellightning = new ModelLightning();
	public static ModelCloud modelcloud = new ModelCloud();
	public static ModelFish modelfish = new ModelFish();
	public static ModelBlock modelblock = new ModelBlock();
	public static ModelFrog modelfrog = new ModelFrog();
	public static ModelSign modelsign = new ModelSign();
	public static ModelMagic modelmagic = new ModelMagic();
	public static ModelSnarler modelsnarler = new ModelSnarler();
	public static ModelBulletBat modelbulletbat = new ModelBulletBat();
	public static ModelMartian modelmartian = new ModelMartian();
	public static ModelDesertRainFrog modeldesertrainfrog = new ModelDesertRainFrog();
	public static ModelPufferFish modelpufferfish = new ModelPufferFish();
	public static ModelButterflyFish modelbutterflyfish = new ModelButterflyFish();
	public static ModelStickFish modelstickfish = new ModelStickFish();
	public static ModelMinnow modelminnow = new ModelMinnow();
	public static ModelPiranah modelpiranah = new ModelPiranah();
	public static ModelEel modeleel = new ModelEel();
	public static ModelMermaid modelmermaid = new ModelMermaid();
	public static ModelHammerhead modelhammerhead = new ModelHammerhead();
	
	public DangerZoneBase(){
		super();		
	}
	
	/*
	 * Replace with the name of your mod.
	 */
	public String getModName()
	{
		return "DangerZone Base Version 1.6";
	}
	
	
	/*
	 * RETURN THE VERSION OF DANGERZONE THIS MOD IS BUILT WITH!!!!
	 * THIS IS SO WE CAN CHECK COMPATIBILITY AND NOT FRUSTRATE AND CRASH THE PLAYER!!!
	 * SEE DANGERZONE.VERSIONSTRING
	 * 
	 * Return your own copy of DangerZone.versionstring
	 */
	public String versionBuiltWith()
	{
		return "1.7";
	}
	
	/*
	 * It is IMPORTANT TO NOTE that itemIDs and blockIDs (and most others) are INDETERMINATE during loading.
	 * In other words, they can change!!!! Do not use itemIDs or blockIDs in the registerThings() routine!!!!
	 * 
	 * This routine is for REGISTRATION and comes before anything else.
	 */
	public void registerThings(){
		
		//We need some content in the world!
		
		//Make a couple dimensions!
		Dimensions.registerDimension(Dimensions.overworlddimension);		
		//Dimensions.registerDimension(Dimensions.biometestdimension);		
		Dimensions.registerDimension(Dimensions.bigroundtreedimension);
		Dimensions.registerDimension(Dimensions.pathwaydimension);
		Dimensions.registerDimension(Dimensions.ruggedhillsdimension);
		Dimensions.registerDimension(Dimensions.windsweptdimension);
		Dimensions.registerDimension(Dimensions.skyislandsdimension);
		Dimensions.registerDimension(Dimensions.pleasantvilledimension);
		
				
		//make biomes and managers for the new dimensions
		overworld_biomemanager = new OverWorldBiomeManager();
		overworldforest = new OverWorldBiome("DangerZone:Overworld Forest");
		overworldtallforest = new OverWorldBiome("DangerZone:Overworld Tall Forest");
		overworldtallwillowforest = new OverWorldBiome("DangerZone:Overworld Tall Willow Forest");
		overworldtallwillowforest.mul_red = 1.15f;
		overworldtallwillowforest.mul_green = 1.15f;
		overworldtallwillowforest.mul_blue = 1.15f;
		overworldscragglyforest = new OverWorldBiome("DangerZone:Overworld Scraggly Forest");
		overworldscragglyforest.mul_red = 0.85f;
		overworldscragglyforest.mul_green = 0.85f;
		overworldscragglyforest.mul_blue = 0.85f;
		//register biomes with manager
		overworld_biomemanager.registerBiome(overworldforest);
		overworld_biomemanager.registerBiome(overworldtallforest);
		overworld_biomemanager.registerBiome(overworldtallwillowforest);
		overworld_biomemanager.registerBiome(overworldscragglyforest);
		//register manager with dimension
		Dimensions.overworlddimension.registerBiomeManager(overworld_biomemanager);
		
		bigtree_biomemanager = new BiomeManager();
		my_biome2 = new BigTreeBiome("DangerZone:Big Trees");
		my_biome2.rarityFactor = 1.20f;
		my_biome2a = new BigTreeBiome("DangerZone:Big Trees Flower");
		my_biome2b = new BigTreeBiome("DangerZone:Big Trees Flower Two");
		Dimensions.bigroundtreedimension.registerBiomeManager(bigtree_biomemanager);
		bigtree_biomemanager.registerBiome(my_biome2);
		bigtree_biomemanager.registerBiome(my_biome2a);
		bigtree_biomemanager.registerBiome(my_biome2b);
		
		pathway_biomemanager = new BiomeManager();
		my_biome3 = new PathwayBiome("DangerZone:Pathway");
		my_biome3a = new PathwayBiome("DangerZone:Pathway Forest");
		my_biome3b = new PathwayBiome("DangerZone:Pathway Umbrella Forest");
		my_biome3c = new PathwayBiome("DangerZone:Pathway Bulb Forest");
		my_biome3d = new PathwayBiome("DangerZone:Pathway Loop Forest");
		my_biome3d.mul_red = 0.95f;
		my_biome3d.mul_green = 0.95f;
		my_biome3d.mul_blue = 0.95f;
		my_biome3e = new PathwayBiome("DangerZone:Pathway Loop Forest II");
		my_biome3e.mul_red = 0.90f;
		my_biome3e.mul_green = 0.90f;
		my_biome3e.mul_blue = 0.90f;
		my_biome3c.rarityFactor = 0.55f;
		my_biome3d.rarityFactor = 0.65f;
		my_biome3e.rarityFactor = 0.75f;
		Dimensions.pathwaydimension.registerBiomeManager(pathway_biomemanager);
		pathway_biomemanager.registerBiome(my_biome3);
		pathway_biomemanager.registerBiome(my_biome3a);
		pathway_biomemanager.registerBiome(my_biome3b);
		pathway_biomemanager.registerBiome(my_biome3c);
		pathway_biomemanager.registerBiome(my_biome3d);
		pathway_biomemanager.registerBiome(my_biome3e);
		
		rugged_biomemanager = new BiomeManager();
		ruggedmountains = new RuggedBiome("DangerZone:Rugged");
		ruggedhills = new RuggedHillsBiome("DangerZone:Rugged Hills");
		ruggedplains = new RuggedPlainsBiome("DangerZone:Rugged Plains");
		ruggedplains.mul_green = 1.1f;
		ruggedplains2 = new RuggedPlainsBiome("DangerZone:Rugged Plains II");
		ruggedplainsdesert = new RuggedDesertBiome("DangerZone:Rugged Plains Desert");
		ruggedplainsdesert.mul_blue = 1.1f;
		ruggedplainsdesert.mul_red = 1.1f;
		ruggedplainsdesert.mul_green = 0.9f;
		Dimensions.ruggedhillsdimension.registerBiomeManager(rugged_biomemanager);
		rugged_biomemanager.registerBiome(ruggedmountains);
		rugged_biomemanager.registerBiome(ruggedhills);
		rugged_biomemanager.registerBiome(ruggedplains);
		rugged_biomemanager.registerBiome(ruggedplains2);
		rugged_biomemanager.registerBiome(ruggedplainsdesert);
		
		sky_biomemanager = new BiomeManager();
		my_biome5 = new SkyIslandBiome("DangerZone:Sky");
		Dimensions.skyislandsdimension.registerBiomeManager(sky_biomemanager);
		sky_biomemanager.registerBiome(my_biome5);
		
		windsweptmanager = new BiomeManager();
		windswept = new WindsweptBiome("DangerZone:Wind-Swept");
		Dimensions.windsweptdimension.registerBiomeManager(windsweptmanager);
		windsweptmanager.registerBiome(windswept);
		
		//my_biomemanager6 = new BiomeManager();
		//my_biome6 = new TestBiome("DangerZone:Test");
		//Dimensions.biometestdimension.registerBiomeManager(my_biomemanager6);
		//my_biomemanager6.registerBiome(my_biome6);
		
		pleasant_biomemanager = new BiomeManager();
		pleasanthills1 = new PleasantHillsBiome("DangerZone:Pleasant Hills");
		pleasanthills2 = new PleasantHillsBiome("DangerZone:Very Pleasant Hills");
		pleasanthills2.mul_green = 1.1f;
		pleasantplains1 = new PleasantPlainsBiome("DangerZone:Pleasant Plains");
		pleasantplains2 = new PleasantPlainsBiome("DangerZone:Very Pleasant Plains");
		pleasantplains2.mul_green = 1.1f;
		pleasantfields1 = new PleasantFieldsBiome("DangerZone:Pleasant Fields");
		pleasantfields2 = new PleasantFieldsBiome("DangerZone:Very Pleasant Fields");
		pleasantfields2.mul_green = 1.1f;
		
		Dimensions.pleasantvilledimension.registerBiomeManager(pleasant_biomemanager);
		pleasant_biomemanager.registerBiome(pleasanthills1);
		pleasant_biomemanager.registerBiome(pleasanthills2);
		pleasant_biomemanager.registerBiome(pleasantplains1);
		pleasant_biomemanager.registerBiome(pleasantplains2);
		pleasant_biomemanager.registerBiome(pleasantfields1);
		pleasant_biomemanager.registerBiome(pleasantfields2);



		
		Entities.registerEntity(EntityTrophy.class, "DangerZone:EntityTrophy", new ModelTrophy());
		
		Entities.registerEntity(Butterfly.class, "DangerZone:Butterfly", modelbutterfly);
		Entities.registerEntity(Ghost.class, "DangerZone:Ghost", modelghost);
		Entities.registerEntity(GhostSkelly.class, "DangerZone:GhostSkelly", modelghostskelly);
		Entities.registerEntity(Rat.class, "DangerZone:Rat", modelrat);
		Entities.registerEntity(Cockroach.class, "DangerZone:Cockroach", modelcockroach);
		Entities.registerEntity(Moose.class, "DangerZone:Moose", modelmoose);
		Entities.registerEntity(EntityShredder.class, "DangerZone:EntityShredder", null);
		Entities.registerEntity(EntityDesk.class, "DangerZone:EntityDesk", null);
		Entities.registerEntity(EntityChest.class, "DangerZone:EntityChest", null);
		Entities.registerEntity(EntityFurnace.class, "DangerZone:EntityFurnace", null);
		Entities.registerEntity(Sparklemuffin.class, "DangerZone:Sparklemuffin", modelfuzzbutt);
		Entities.registerEntity(Skeletorus.class, "DangerZone:Skeletorus", modelfuzzbutt);
		Entities.registerEntity(Goose.class, "DangerZone:Goose", modelgoose);
		Entities.registerEntity(Gosling.class, "DangerZone:Gosling", modelgoose);
		Entities.registerEntity(Ostrich.class, "DangerZone:Ostrich", modelostrich);
		Entities.registerEntity(Anteater.class, "DangerZone:Anteater", modelanteater);
		Entities.registerEntity(Werewolf.class, "DangerZone:Werewolf", modelhumanoid);
		Entities.registerEntity(Vampire.class, "DangerZone:Vampire", modelhumanoid);
		Entities.registerEntity(TheCount.class, "DangerZone:The Count", modelhumanoid);
		Entities.registerEntity(VampireMoose.class, "DangerZone:Vampire Moose", modelmoose);
		Entities.registerEntity(Fish.class, "DangerZone:Fish", modelfish);
		Entities.registerEntity(AnotherFish.class, "DangerZone:Another Fish", modelfish);
		Entities.registerEntity(PufferFish.class, "DangerZone:Puffer Fish", modelpufferfish);
		Entities.registerEntity(ButterflyFish.class, "DangerZone:Butterfly Fish", modelbutterflyfish);
		Entities.registerEntity(StickFish.class, "DangerZone:Stick Fish", modelstickfish);
		Entities.registerEntity(Piranah.class, "DangerZone:Piranah", modelpiranah);
		Entities.registerEntity(Minnow.class, "DangerZone:Minnow", modelminnow);
		Entities.registerEntity(Frog.class, "DangerZone:Frog", modelfrog);
		Entities.registerEntity(Vixen.class, "DangerZone:Vixen", modelhumanoid);
		Entities.registerEntity(Snarler.class, "DangerZone:Snarler", modelsnarler);
		Entities.registerEntity(BulletBat.class, "DangerZone:Bullet Bat", modelbulletbat);
		Entities.registerEntity(Martian.class, "DangerZone:Martian", modelmartian);
		Entities.registerEntity(DesertRainFrog.class, "DangerZone:Desert Rain Frog", modeldesertrainfrog);
		Entities.registerEntity(Eel.class, "DangerZone:Eel", modeleel);
		Entities.registerEntity(Mermaid.class, "DangerZone:Mermaid", modelmermaid);
		Entities.registerEntity(HammerheadShark.class, "DangerZone:Hammerhead Shark", modelhammerhead);
		
		
		Entities.registerEntity(ThrownBlockItem.class, "DangerZone:ThrownBlockItem", new ModelEntityBlockItem());
		Entities.registerEntity(ThrownInstability.class, "DangerZone:ThrownInstability", new ModelEntityBlockItem());
		Entities.registerEntity(ThrownExpBottle.class, "DangerZone:ThrownExpBottle", new ModelEntityBlockItem());
		Entities.registerEntity(ThrownFireball.class, "DangerZone:ThrownFireball", new ModelFireball());
		Entities.registerEntity(ThrownFrog.class, "DangerZone:ThrownFrog", new ModelEntityBlockItem());
		Entities.registerEntity(EntityFire.class, "DangerZone:Fire", new ModelFire());
		Entities.registerEntity(EntitySpawner.class, "DangerZone:EntitySpawner", new ModelSpawner());
		Entities.registerEntity(EntityLightning.class, "DangerZone:Lightning", modellightning);
		Entities.registerEntity(EntityCloud.class, "DangerZone:Cloud", modelcloud);
		Entities.registerEntity(EntityExplosion.class, "DangerZone:Explosion", null);
		Entities.registerEntity(EntityExtendedRangeDamage.class, "DangerZone:ExtendedRangeDamage", null);
		Entities.registerEntity(EntityBlock.class, "DangerZone:EntityBlock", modelblock);
		Entities.registerEntity(EntityExplosiveBlock.class, "DangerZone:EntityExplosiveBlock", modelblock);
		Entities.registerEntity(EntityVolcano.class, "DangerZone:Volcano", null);
		Entities.registerEntity(EntitySign.class, "DangerZone:EntitySign", modelsign);
		Entities.registerEntity(EntityMagic.class, "DangerZone:EntityMagic", modelmagic);
		Entities.registerEntity(EntityArrow.class, "DangerZone:EntityArrow", new ModelArrow());
		Entities.registerEntity(Drone.class, "DangerZone:Drone", new ModelDrone());
		Entities.registerEntity(DroneClaw.class, "DangerZone:DroneClaw", new ModelDroneClaw());
		Entities.registerEntity(EntityMagLev.class, "DangerZone:MagLev", new ModelMagLev());
		Entities.registerEntity(EntityRaft.class, "DangerZone:Raft", new ModelRaft());
		Entities.registerEntity(Flag.class, "DangerZone:Flag", new ModelFlag());
		Entities.registerEntity(EntityStand.class, "DangerZone:EntityStand", new ModelStand());
		
		Particles.registerParticle(ParticleBreak.class, "DangerZone:ParticleBreak");
		Particles.registerParticle(ParticleHurt.class, "DangerZone:ParticleHurt");
		Particles.registerParticle(ParticleDeath.class, "DangerZone:ParticleDeath");
		Particles.registerParticle(ParticleSmoke.class, "DangerZone:ParticleSmoke");
		Particles.registerParticle(ParticleFire.class, "DangerZone:ParticleFire");
		Particles.registerParticle(ParticleHappy.class, "DangerZone:ParticleHappy");
		Particles.registerParticle(ParticleSparkle.class, "DangerZone:ParticleSparkle");
		Particles.registerParticle(ParticleRain.class, "DangerZone:ParticleRain");
		Particles.registerParticle(ParticleDroplet.class, "DangerZone:ParticleDroplet");
		Particles.registerParticle(ParticleDust.class, "DangerZone:ParticleDust");
		Particles.registerParticle(ParticleSpeed.class, "DangerZone:ParticleSpeed");
		Particles.registerParticle(ParticleSlowness.class, "DangerZone:ParticleSlowness");
		Particles.registerParticle(ParticleStrength.class, "DangerZone:ParticleStrength");
		Particles.registerParticle(ParticleWeakness.class, "DangerZone:ParticleWeakness");
		Particles.registerParticle(ParticleRegeneration.class, "DangerZone:ParticleRegeneration");
		Particles.registerParticle(ParticlePoison.class, "DangerZone:ParticlePoison");
		Particles.registerParticle(ParticleConfusion.class, "DangerZone:ParticleConfusion");
		Particles.registerParticle(ParticleMorph.class, "DangerZone:ParticleMorph");
		Particles.registerParticle(ParticleWaterSpout.class, "DangerZone:ParticleWaterSpout");
		Particles.registerParticle(ParticleLeaves.class, "DangerZone:ParticleLeaves");
		
		Blocks.registerBlock(Blocks.stopblock);
		Blocks.registerBlock(Blocks.stone);
		Blocks.registerBlock(Blocks.stone2);
		Blocks.registerBlock(Blocks.stone3);
		Blocks.registerBlock(Blocks.stone4);
		Blocks.registerBlock(Blocks.greystone);
		Blocks.registerBlock(Blocks.greystone2);
		Blocks.registerBlock(Blocks.greystone3);
		Blocks.registerBlock(Blocks.greystone4);
		Blocks.registerBlock(Blocks.dirt);
		Blocks.registerBlock(Blocks.sand);
		Blocks.registerBlock(Blocks.grassblock);
		Blocks.registerBlock(Blocks.roachnest);
		Blocks.registerBlock(Blocks.oresilver);
		Blocks.registerBlock(Blocks.orecopper);
		Blocks.registerBlock(Blocks.oretin);
		Blocks.registerBlock(Blocks.oreplatinum);
		Blocks.registerBlock(Blocks.orelight);
		Blocks.registerBlock(Blocks.oredark);
		Blocks.registerBlock(Blocks.orediamond);
		Blocks.registerBlock(Blocks.oreemerald);
		Blocks.registerBlock(Blocks.orebloodstone);
		Blocks.registerBlock(Blocks.oresunstone);
		Blocks.registerBlock(Blocks.blockdark);
		Blocks.registerBlock(Blocks.blocklight);
		Blocks.registerBlock(Blocks.blockplatinum);
		Blocks.registerBlock(Blocks.blocksilver);
		Blocks.registerBlock(Blocks.blockcopper);
		Blocks.registerBlock(Blocks.blocktin);
		Blocks.registerBlock(Blocks.blockdiamond);
		Blocks.registerBlock(Blocks.blockemerald);
		Blocks.registerBlock(Blocks.blockbloodstone);
		Blocks.registerBlock(Blocks.blocksunstone);
		Blocks.registerBlock(Blocks.whitestone);
		Blocks.registerBlock(Blocks.hardrock);
		Blocks.registerBlock(Blocks.firestone);
		
				
		Blocks.registerBlock(Blocks.redwoodlog);
		Blocks.registerBlock(Blocks.darkplywood);
		Blocks.registerBlock(Blocks.redwoodleaves);
		Blocks.registerBlock(Blocks.redleaves);
		Blocks.registerBlock(Blocks.orangeleaves);
		Blocks.registerBlock(Blocks.yellowleaves);
		Blocks.registerBlock(Blocks.greenleaves);
		Blocks.registerBlock(Blocks.log);
		Blocks.registerBlock(Blocks.plywood);
		Blocks.registerBlock(Blocks.leaves);
		Blocks.registerBlock(Blocks.willowlog);
		Blocks.registerBlock(Blocks.lightplywood);
		Blocks.registerBlock(Blocks.willowleaves);
		Blocks.registerBlock(Blocks.pineleaves);

		Blocks.registerBlock(Blocks.grass);
		Blocks.registerBlock(Blocks.peachleaves);
		Blocks.registerBlock(Blocks.cherryleaves);
		Blocks.registerBlock(Blocks.appleleaves);
		
		Blocks.registerBlock(Blocks.workbench);
		Blocks.registerBlock(Blocks.chest);
		Blocks.registerBlock(Blocks.stand);
		Blocks.registerBlock(Blocks.claim_block);
		Blocks.registerBlock(Blocks.claim_marker);
		Blocks.registerBlock(Blocks.furnace);
		Blocks.registerBlock(Blocks.furnaceOn);
		
		Blocks.registerBlock(Blocks.lightstick);
		Blocks.registerBlock(Blocks.darkstick);
		
		Blocks.registerBlock(Blocks.water);
		Blocks.registerBlock(Blocks.waterstatic);
		Blocks.registerBlock(Blocks.milk);
		Blocks.registerBlock(Blocks.milkstatic);
		Blocks.registerBlock(Blocks.lava);
		Blocks.registerBlock(Blocks.lavastatic);
		Blocks.registerBlock(Blocks.lavafiller);
		Blocks.registerBlock(Blocks.autofence);
		Blocks.registerBlock(Blocks.post);
		Blocks.registerBlock(Blocks.ladder);
		
		Blocks.registerBlock(Blocks.corn_plant);
		Blocks.registerBlock(Blocks.corn_plant1);
		Blocks.registerBlock(Blocks.corn_plant2);
		Blocks.registerBlock(Blocks.corn_plant3);
		Blocks.registerBlock(Blocks.flower_red);
		Blocks.registerBlock(Blocks.flower_pink);
		Blocks.registerBlock(Blocks.flower_purple);
		Blocks.registerBlock(Blocks.flower_yellow);
		Blocks.registerBlock(Blocks.butterfly_plant);
		Blocks.registerBlock(Blocks.butterfly_plant1);
		Blocks.registerBlock(Blocks.butterfly_plant2);
		Blocks.registerBlock(Blocks.butterfly_plant3);		
		Blocks.registerBlock(Blocks.radish_plant);
		Blocks.registerBlock(Blocks.radish_plant1);
		Blocks.registerBlock(Blocks.radish_plant2);
		Blocks.registerBlock(Blocks.radish_plant3);
		Blocks.registerBlock(Blocks.rice_plant);
		Blocks.registerBlock(Blocks.rice_plant1);
		Blocks.registerBlock(Blocks.rice_plant2);
		Blocks.registerBlock(Blocks.rice_plant3);
		Blocks.registerBlock(Blocks.sapling_tallwood);
		Blocks.registerBlock(Blocks.sapling_cherry);
		Blocks.registerBlock(Blocks.sapling_peach);
		Blocks.registerBlock(Blocks.sapling_apple);
		Blocks.registerBlock(Blocks.sapling_scragglyredwood);
		Blocks.registerBlock(Blocks.sapling_scraggly);
		Blocks.registerBlock(Blocks.sapling_bigroundredwood);
		Blocks.registerBlock(Blocks.sapling_bigroundwillow);
		Blocks.registerBlock(Blocks.sapling_flower);
		Blocks.registerBlock(Blocks.sapling_flowertwo);
		Blocks.registerBlock(Blocks.sapling_scrub);
		Blocks.registerBlock(Blocks.sapling_flowernormal);
		Blocks.registerBlock(Blocks.sapling_umbrella);
		Blocks.registerBlock(Blocks.sapling_bulb);
		Blocks.registerBlock(Blocks.sapling_looplowspiral);
		Blocks.registerBlock(Blocks.sapling_loop);
		Blocks.registerBlock(Blocks.sapling_generic);
		Blocks.registerBlock(Blocks.sapling_tallwillow);
		Blocks.registerBlock(Blocks.sapling_vase);
		Blocks.registerBlock(Blocks.sapling_spiral);
		Blocks.registerBlock(Blocks.sapling_loopspiral);
		Blocks.registerBlock(Blocks.sapling_bowl);
		Blocks.registerBlock(Blocks.sapling_doublebowl);
		
		Blocks.registerBlock(Blocks.reefgrass);
		Blocks.registerBlock(Blocks.redreefgrass);		
		Blocks.registerBlock(Blocks.redcoral);
		Blocks.registerBlock(Blocks.yellowcoral);
		Blocks.registerBlock(Blocks.bluecoral);
		Blocks.registerBlock(Blocks.firecoral);		
		Blocks.registerBlock(Blocks.redfancoral);
		Blocks.registerBlock(Blocks.blackfancoral);		
		Blocks.registerBlock(Blocks.seaweed);
		Blocks.registerBlock(Blocks.tallseaweed);
		Blocks.registerBlock(Blocks.brownseaweed);
		Blocks.registerBlock(Blocks.browntallseaweed);
		Blocks.registerBlock(Blocks.yellowseaweed);
		Blocks.registerBlock(Blocks.yellowtallseaweed);
		Blocks.registerBlock(Blocks.kelp);
		Blocks.registerBlock(Blocks.tallkelp);
		Blocks.registerBlock(Blocks.fishnursery);

		Blocks.registerBlock(Blocks.glass);
		Blocks.registerBlock(Blocks.blueglass);
		Blocks.registerBlock(Blocks.greenglass);
		Blocks.registerBlock(Blocks.redglass);
		Blocks.registerBlock(Blocks.violetglass);
		Blocks.registerBlock(Blocks.yellowglass);
		
		Blocks.registerBlock(Blocks.stickyblock);
		Blocks.registerBlock(Blocks.coloringblock);
		Blocks.registerBlock(Blocks.doortop);
		Blocks.registerBlock(Blocks.doorbottom);
		Blocks.registerBlock(Blocks.blockinstability);
		Blocks.registerBlock(Blocks.blockinstability_large);
		Blocks.registerBlock(Blocks.blockinstability_huge);
		
		Blocks.registerBlock(Blocks.cloud_light);
		Blocks.registerBlock(Blocks.cloud_rain);
		Blocks.registerBlock(Blocks.cloud_thunder);
		
		Blocks.registerBlock(Blocks.butterflyspawner);
		Blocks.registerBlock(Blocks.ghostspawner);
		Blocks.registerBlock(Blocks.ghostskellyspawner);
		Blocks.registerBlock(Blocks.ratspawner);
		Blocks.registerBlock(Blocks.cockroachspawner);
		Blocks.registerBlock(Blocks.moosespawner);
		Blocks.registerBlock(Blocks.sparklemuffinspawner);
		Blocks.registerBlock(Blocks.skeletorusspawner);
		Blocks.registerBlock(Blocks.goosespawner);
		Blocks.registerBlock(Blocks.goslingspawner);
		Blocks.registerBlock(Blocks.ostrichspawner);
		Blocks.registerBlock(Blocks.anteaterspawner);
		Blocks.registerBlock(Blocks.werewolfspawner);
		Blocks.registerBlock(Blocks.vampirespawner);
		Blocks.registerBlock(Blocks.thecountspawner);
		Blocks.registerBlock(Blocks.vampiremoosespawner);
		Blocks.registerBlock(Blocks.fishspawner);
		Blocks.registerBlock(Blocks.anotherfishspawner);
		Blocks.registerBlock(Blocks.pufferfishspawner);
		Blocks.registerBlock(Blocks.butterflyfishspawner);
		Blocks.registerBlock(Blocks.stickfishspawner);
		Blocks.registerBlock(Blocks.minnowspawner);
		Blocks.registerBlock(Blocks.piranahspawner);
		Blocks.registerBlock(Blocks.vixenspawner);
		Blocks.registerBlock(Blocks.snarlerspawner);
		Blocks.registerBlock(Blocks.bulletbatspawner);
		Blocks.registerBlock(Blocks.martianspawner);
		Blocks.registerBlock(Blocks.desertrainfrogspawner);
		Blocks.registerBlock(Blocks.eelspawner);
		Blocks.registerBlock(Blocks.mermaidspawner);
		
		Blocks.registerBlock(Blocks.waterpump);
		Blocks.registerBlock(Blocks.waterspout);
		Blocks.registerBlock(Blocks.waterlight);
		Blocks.registerBlock(Blocks.waterdark);
		Blocks.registerBlock(Blocks.musicbox);
		Blocks.registerBlock(Blocks.puddlemaker);
		Blocks.registerBlock(Blocks.watercannon);
		Blocks.registerBlock(Blocks.waterstone);
		Blocks.registerBlock(Blocks.unwaterpump);
		Blocks.registerBlock(Blocks.waterswitch);
		Blocks.registerBlock(Blocks.shredder);
		Blocks.registerBlock(Blocks.desk);
		Blocks.registerBlock(Blocks.rail);
		Blocks.registerBlock(Blocks.railspeed);
		Blocks.registerBlock(Blocks.railslow);
		Blocks.registerBlock(Blocks.railstop);
		Blocks.registerBlock(Blocks.railfixedslow);
		Blocks.registerBlock(Blocks.railfixedmedium);
		Blocks.registerBlock(Blocks.railload);
		Blocks.registerBlock(Blocks.railunload);
		Blocks.registerBlock(Blocks.railup);
		Blocks.registerBlock(Blocks.raildown);
		Blocks.registerBlock(Blocks.raildplus);
		Blocks.registerBlock(Blocks.raildminus);
		
		Blocks.registerBlock(Blocks.block_lightgrey);
		Blocks.registerBlock(Blocks.block_lightgreen);
		Blocks.registerBlock(Blocks.block_lightred);
		Blocks.registerBlock(Blocks.block_lightblue);
		Blocks.registerBlock(Blocks.block_lightyellow);
		Blocks.registerBlock(Blocks.block_lightorange);
		Blocks.registerBlock(Blocks.block_lightpurple);
		Blocks.registerBlock(Blocks.block_lightviolet);
		Blocks.registerBlock(Blocks.block_lightbrown);
		
		Blocks.registerBlock(Blocks.block_grey);
		Blocks.registerBlock(Blocks.block_green);
		Blocks.registerBlock(Blocks.block_red);
		Blocks.registerBlock(Blocks.block_blue);
		Blocks.registerBlock(Blocks.block_yellow);
		Blocks.registerBlock(Blocks.block_orange);
		Blocks.registerBlock(Blocks.block_purple);
		Blocks.registerBlock(Blocks.block_violet);
		Blocks.registerBlock(Blocks.block_brown);
		
		Blocks.registerBlock(Blocks.block_darkgrey);
		Blocks.registerBlock(Blocks.block_darkgreen);
		Blocks.registerBlock(Blocks.block_darkred);
		Blocks.registerBlock(Blocks.block_darkblue);
		Blocks.registerBlock(Blocks.block_darkyellow);
		Blocks.registerBlock(Blocks.block_darkorange);
		Blocks.registerBlock(Blocks.block_darkpurple);
		Blocks.registerBlock(Blocks.block_darkviolet);
		Blocks.registerBlock(Blocks.block_darkbrown);
		
		//register some ores!
		Ores.registerOre(Blocks.orecopper, Blocks.stone, null, null, 10, 80, 35, 16, 0);
		Ores.registerOre(Blocks.oretin, Blocks.stone, null, null, 10, 70, 25, 20, 0);
		Ores.registerOre(Blocks.oresilver, Blocks.stone, null, null, 0, 50, 30, 8, 0);
		Ores.registerOre(Blocks.oreplatinum, Blocks.stone, null, null, 0, 30, 25, 4, 0);
		Ores.registerOre(Blocks.lavastatic, Blocks.stone, null, null, 0, 20, 1, 10, 2);
		Ores.registerOre(Blocks.firestone, Blocks.stone, null, null, 0, 20, 10, 4, 0);
		Ores.registerOre(Blocks.waterstatic, Blocks.stone, null, null, 0, 40, 1, 10, 2);
		Ores.registerOre(Blocks.waterstone, Blocks.stone, null, null, 0, 40, 10, 4, 0);
		Ores.registerOre(Blocks.coloringblock, Blocks.stone, null, null, 0, 10, 10, 4, 0);
		Ores.registerOre(Blocks.orelight, Blocks.stone, null, null, 35, 45, 40, 14, 0);
		Ores.registerOre(Blocks.oredark, Blocks.stone, null, null, 0, 20, 15, 6, 0); //random-ish
		Ores.registerOre(Blocks.sand, Blocks.stone, null, null, 0, 40, 10, 40, 1); //layer-ish
		Ores.registerOre(Blocks.dirt, Blocks.stone, null, null, 0, 40, 5, 40, 2);  //clump-ish
		Ores.registerOre(Blocks.orediamond, Blocks.stone, null, null, 0, 20, 20, 8, 0);
		Ores.registerOre(Blocks.oreemerald, Blocks.stone, null, null, 0, 20, 15, 4, 0);
		Ores.registerOre(Blocks.orebloodstone, Blocks.stone, null, null, 0, 20, 8, 4, 0);
		
		//Now register them a little higher up for the sky dimension
		Ores.registerOre(Blocks.orecopper, Blocks.stone, Dimensions.skyislandsdimension, null, 120, 200, 30, 16, 0);
		Ores.registerOre(Blocks.oretin, Blocks.stone, Dimensions.skyislandsdimension, null, 120, 200, 20, 12, 0);
		Ores.registerOre(Blocks.oresilver, Blocks.stone, Dimensions.skyislandsdimension, null, 120, 200, 15, 8, 0);
		Ores.registerOre(Blocks.oreplatinum, Blocks.stone, Dimensions.skyislandsdimension, null, 200, 250, 15, 4, 0);
		Ores.registerOre(Blocks.orelight, Blocks.stone, Dimensions.skyislandsdimension, null, 75, 115, 35, 12, 0);
		Ores.registerOre(Blocks.oredark, Blocks.stone, Dimensions.skyislandsdimension, null, 0, 65, 15, 6, 0); //nice random-ish
		Ores.registerOre(Blocks.sand, Blocks.stone, Dimensions.skyislandsdimension, null, 10, 200, 10, 40, 1); //layer-ish
		Ores.registerOre(Blocks.dirt, Blocks.stone, Dimensions.skyislandsdimension, null, 10, 175, 5, 40, 2);  //clump-ish
		Ores.registerOre(Blocks.orediamond, Blocks.stone, Dimensions.skyislandsdimension, null, 200, 250, 15, 6, 0);
		Ores.registerOre(Blocks.oreemerald, Blocks.stone, Dimensions.skyislandsdimension, null, 200, 250, 15, 6, 0);
		Ores.registerOre(Blocks.orebloodstone, Blocks.stone, Dimensions.skyislandsdimension, null, 200, 250, 15, 6, 0);
		Ores.registerOre(Blocks.oresunstone, Blocks.stone, Dimensions.skyislandsdimension, null, 200, 250, 15, 6, 0);
	
		
		Items.registerItem(Items.woodensword);
		Items.registerItem(Items.woodenpickaxe);
		Items.registerItem(Items.woodenaxe);
		Items.registerItem(Items.woodenshovel);
		Items.registerItem(Items.woodenhoe);
		Items.registerItem(Items.stonesword);
		Items.registerItem(Items.stonepickaxe);
		Items.registerItem(Items.stoneaxe);
		Items.registerItem(Items.stoneshovel);
		Items.registerItem(Items.stonehoe);
		Items.registerItem(Items.coppersword);
		Items.registerItem(Items.copperpickaxe);
		Items.registerItem(Items.copperaxe);
		Items.registerItem(Items.coppershovel);
		Items.registerItem(Items.copperhoe);	
		Items.registerItem(Items.tinsword);
		Items.registerItem(Items.tinpickaxe);
		Items.registerItem(Items.tinaxe);
		Items.registerItem(Items.tinshovel);
		Items.registerItem(Items.tinhoe);	
		Items.registerItem(Items.silversword);
		Items.registerItem(Items.silverpickaxe);
		Items.registerItem(Items.silveraxe);
		Items.registerItem(Items.silvershovel);
		Items.registerItem(Items.silverhoe);	
		Items.registerItem(Items.platinumsword);
		Items.registerItem(Items.platinumpickaxe);
		Items.registerItem(Items.platinumaxe);
		Items.registerItem(Items.platinumshovel);
		Items.registerItem(Items.platinumhoe);
		
		Items.registerItem(Items.lumpcopper);
		Items.registerItem(Items.lumptin);
		Items.registerItem(Items.lumpsilver);
		Items.registerItem(Items.lumpplatinum);
		Items.registerItem(Items.coingold);
		Items.registerItem(Items.coinsilver);
		Items.registerItem(Items.coinplatinum);
		Items.registerItem(Items.diamond);
		Items.registerItem(Items.emerald);
		Items.registerItem(Items.bloodstone);
		Items.registerItem(Items.sunstone);
		Items.registerItem(Items.light);
		Items.registerItem(Items.dark);
		Items.registerItem(Items.stick);
		Items.registerItem(Items.arrow);
		Items.registerItem(Items.bow);
		Items.registerItem(Items.bow_empty);
		Items.registerItem(Items.raft);
		Items.registerItem(Items.maglev);
		Items.registerItem(Items.flag);
		Items.registerItem(Items.squeaktoy);
		Items.registerItem(Items.deadstickfish);
		Items.registerItem(Items.pufferfishspikes);
		Items.registerItem(Items.sharktooth);
		Items.registerItem(Items.sharkfin);

		Items.registerItem(Items.corn);
		Items.registerItem(Items.moosemeat);
		Items.registerItem(Items.moosemeat_cooked);
		Items.registerItem(Items.goosemeat);
		Items.registerItem(Items.goosemeat_cooked);
		Items.registerItem(Items.ostrichmeat);
		Items.registerItem(Items.ostrichmeat_cooked);
		Items.registerItem(Items.fishmeat);
		Items.registerItem(Items.fishmeat_cooked);
		Items.registerItem(Items.radish);
		Items.registerItem(Items.rice);
		Items.registerItem(Items.peach);
		Items.registerItem(Items.apple);
		Items.registerItem(Items.cherries);
		Items.registerItem(Items.deadbug);
		Items.registerItem(Items.bread);
		Items.registerItem(Items.cheese);
		Items.registerItem(Items.butter);			
		
		Items.registerItem(Items.trophycockroach);
		Items.registerItem(Items.trophybutterfly);
		Items.registerItem(Items.trophyrat);
		Items.registerItem(Items.trophymoose);
		Items.registerItem(Items.trophyghost);
		Items.registerItem(Items.trophyghostskelly);
		Items.registerItem(Items.trophygoose);
		Items.registerItem(Items.trophygosling);
		Items.registerItem(Items.trophyostrich);
		Items.registerItem(Items.trophysparklemuffin);
		Items.registerItem(Items.trophyskeletorus);
		Items.registerItem(Items.trophyanteater);
		Items.registerItem(Items.trophywerewolf);
		Items.registerItem(Items.trophyvampire);
		Items.registerItem(Items.trophythecount);
		Items.registerItem(Items.trophyvampiremoose);
		Items.registerItem(Items.trophyfish);
		Items.registerItem(Items.trophyanotherfish);
		Items.registerItem(Items.trophybutterflyfish);
		Items.registerItem(Items.trophypufferfish);
		Items.registerItem(Items.trophystickfish);
		Items.registerItem(Items.trophyminnow);
		Items.registerItem(Items.trophypiranah);
		Items.registerItem(Items.trophyvixen);
		Items.registerItem(Items.trophysnarler);
		Items.registerItem(Items.trophybulletbat);
		Items.registerItem(Items.trophymartian);
		Items.registerItem(Items.trophydesertrainfrog);
		Items.registerItem(Items.trophyeel);
		Items.registerItem(Items.trophymermaid);
		Items.registerItem(Items.trophyhammerhead);
		
		Items.registerItem(Items.eggbutterfly);
		Items.registerItem(Items.eggghost);
		Items.registerItem(Items.eggghostskelly);
		Items.registerItem(Items.eggrat);
		Items.registerItem(Items.eggcockroach);
		Items.registerItem(Items.eggmoose);
		Items.registerItem(Items.egggoose);
		Items.registerItem(Items.egggosling);
		Items.registerItem(Items.eggostrich);
		Items.registerItem(Items.eggsparklemuffin);
		Items.registerItem(Items.eggskeletorus);
		Items.registerItem(Items.egganteater);
		Items.registerItem(Items.eggwerewolf);
		Items.registerItem(Items.eggvampire);
		Items.registerItem(Items.eggthecount);
		Items.registerItem(Items.eggvampiremoose);
		Items.registerItem(Items.eggfish);
		Items.registerItem(Items.egganotherfish);
		Items.registerItem(Items.eggpufferfish);
		Items.registerItem(Items.eggbutterflyfish);
		Items.registerItem(Items.eggstickfish);
		Items.registerItem(Items.eggminnow);
		Items.registerItem(Items.eggpiranah);
		Items.registerItem(Items.eggfrog);
		Items.registerItem(Items.eggvixen);
		Items.registerItem(Items.eggsnarler);
		Items.registerItem(Items.eggbulletbat);
		Items.registerItem(Items.eggmartian);
		Items.registerItem(Items.eggdesertrainfrog);
		Items.registerItem(Items.eggeel);
		Items.registerItem(Items.eggmermaid);
		Items.registerItem(Items.egghammerhead);
		
		Items.registerItem(Items.eggcloud);
		Items.registerItem(Items.eggvolcano);
		
		Items.registerItem(Items.copperhelmet);
		Items.registerItem(Items.copperchestplate);
		Items.registerItem(Items.copperleggings);
		Items.registerItem(Items.copperboots);
		Items.registerItem(Items.tinhelmet);
		Items.registerItem(Items.tinchestplate);
		Items.registerItem(Items.tinleggings);
		Items.registerItem(Items.tinboots);
		Items.registerItem(Items.silverhelmet);
		Items.registerItem(Items.silverchestplate);
		Items.registerItem(Items.silverleggings);
		Items.registerItem(Items.silverboots);
		Items.registerItem(Items.platinumhelmet);
		Items.registerItem(Items.platinumchestplate);
		Items.registerItem(Items.platinumleggings);
		Items.registerItem(Items.platinumboots);
		Items.registerItem(Items.moosehead);
		Items.registerItem(Items.scubamask);
		Items.registerItem(Items.scubatanks);
		Items.registerItem(Items.tiara);
		
		Items.registerItem(Items.furball);
		Items.registerItem(Items.vampireteeth);
		Items.registerItem(Items.instability);
		Items.registerItem(Items.instabilitylarge);
		Items.registerItem(Items.instabilityhuge);
		Items.registerItem(Items.bottle);
		Items.registerItem(Items.experiencebottle);
		Items.registerItem(Items.moosebone);
		Items.registerItem(Items.feather);
		Items.registerItem(Items.peachseed);
		Items.registerItem(Items.appleseed);
		Items.registerItem(Items.cherryseed);	
		
		Items.registerItem(Items.firestick);
		Items.registerItem(Items.autofencekey);
		Items.registerItem(Items.dropstick);
		Items.registerItem(Items.string);
		Items.registerItem(Items.bucket);
		Items.registerItem(Items.bucketwater);
		Items.registerItem(Items.bucketmilk);
		Items.registerItem(Items.bucketlava);
		Items.registerItem(Items.door);
		Items.registerItem(Items.sign);
		Items.registerItem(Items.fireball);
		Items.registerItem(Items.charcoalstick);
		Items.registerItem(Items.woodchips);
		Items.registerItem(Items.woodpulp);
		Items.registerItem(Items.paper);
		
		
		Items.registerItem(Items.frog_speed1);
		Items.registerItem(Items.frog_slowness1);
		Items.registerItem(Items.frog_speed2);
		Items.registerItem(Items.frog_slowness2);
		Items.registerItem(Items.frog_speed3);
		Items.registerItem(Items.frog_slowness3);
		Items.registerItem(Items.frog_strength1);
		Items.registerItem(Items.frog_weakness1);
		Items.registerItem(Items.frog_strength2);
		Items.registerItem(Items.frog_weakness2);
		Items.registerItem(Items.frog_strength3);
		Items.registerItem(Items.frog_weakness3);		
		Items.registerItem(Items.frog_regen1);
		Items.registerItem(Items.frog_poison1);
		Items.registerItem(Items.frog_regen2);
		Items.registerItem(Items.frog_poison2);
		Items.registerItem(Items.frog_regen3);
		Items.registerItem(Items.frog_poison3);
		Items.registerItem(Items.frog_confusion1);
		Items.registerItem(Items.frog_confusion2);
		Items.registerItem(Items.frog_confusion3);
		Items.registerItem(Items.frog_morph1);
		Items.registerItem(Items.frog_morph2);
		Items.registerItem(Items.frog_morph3);
		
		Items.registerItem(Items.scrollaccuracy);
		Items.registerItem(Items.scrolldamage);
		Items.registerItem(Items.scrolldurability);
		Items.registerItem(Items.scrollreach);
		Items.registerItem(Items.scrollspam);
		
		Items.registerItem(Items.scrollaccuracyII);
		Items.registerItem(Items.scrolldamageII);
		Items.registerItem(Items.scrolldurabilityII);
		Items.registerItem(Items.scrollreachII);
		Items.registerItem(Items.scrollspamII);
		
		Items.registerItem(Items.scrollaccuracyIII);
		Items.registerItem(Items.scrolldamageIII);
		Items.registerItem(Items.scrolldurabilityIII);
		Items.registerItem(Items.scrollreachIII);
		Items.registerItem(Items.scrollspamIII);
		
		Items.registerItem(Items.scrollaccuracyIV);
		Items.registerItem(Items.scrolldamageIV);
		Items.registerItem(Items.scrolldurabilityIV);
		Items.registerItem(Items.scrollreachIV);
		Items.registerItem(Items.scrollspamIV);
		
		Items.registerItem(Items.scrollaccuracyV);
		Items.registerItem(Items.scrolldamageV);
		Items.registerItem(Items.scrolldurabilityV);
		Items.registerItem(Items.scrollreachV);
		Items.registerItem(Items.scrollspamV);
		
		Items.registerItem(Items.scrollharm);
		Items.registerItem(Items.scrollheal);
		
		Items.registerItem(Items.drone);
		Items.registerItem(Items.drone_motor);
		Items.registerItem(Items.drone_body);
		Items.registerItem(Items.drone_camera);
		
		//register some cooking and smelting!
		Cooking.registerCookingRecipe(Blocks.orecopper, Items.lumpcopper, 20);
		Cooking.registerCookingRecipe(Blocks.oretin, Items.lumptin, 25);
		Cooking.registerCookingRecipe(Blocks.oresilver, Items.lumpsilver, 30);
		Cooking.registerCookingRecipe(Blocks.oreplatinum, Items.lumpplatinum, 45);
		Cooking.registerCookingRecipe(Blocks.sand, Blocks.glass, 60);
		Cooking.registerCookingRecipe(Items.moosemeat, Items.moosemeat_cooked, 20);
		Cooking.registerCookingRecipe(Items.goosemeat, Items.goosemeat_cooked, 35);
		Cooking.registerCookingRecipe(Items.ostrichmeat, Items.ostrichmeat_cooked, 40);
		Cooking.registerCookingRecipe(Items.fishmeat, Items.fishmeat_cooked, 25);
		Cooking.registerCookingRecipe(Items.stick, Items.charcoalstick, 10);
		
		//Make some noise!		
		registerSounds();
		


		//do some crafting
		Crafting.registerCraftingRecipe(Blocks.redwoodlog, null, null, null, null, null, null, null, null, Blocks.darkplywood, 4, false);
		Crafting.registerCraftingRecipe(Blocks.log, null, null, null, null, null, null, null, null, Blocks.plywood, 4, false);
		Crafting.registerCraftingRecipe(Blocks.willowlog, null, null, null, null, null, null, null, null, Blocks.lightplywood, 4, false);
		
		Crafting.registerCraftingRecipe(Blocks.plywood, null, null, null, null, null, null, null, null, Items.stick, 8, false);
		Crafting.registerCraftingRecipe(Blocks.darkplywood, null, null, null, null, null, null, null, null, Items.stick, 8, false);
		Crafting.registerCraftingRecipe(Blocks.lightplywood, null, null, null, null, null, null, null, null, Items.stick, 8, false);
		
		Crafting.registerCraftingRecipe(Blocks.plywood, Blocks.plywood, Blocks.plywood, Blocks.plywood, null, null, null, null, null, Blocks.workbench, 1, false);
		Crafting.registerCraftingRecipe(Blocks.lightplywood, Blocks.lightplywood, Blocks.lightplywood, Blocks.lightplywood, null, null, null, null, null, Blocks.workbench, 1, false);
		Crafting.registerCraftingRecipe(Blocks.darkplywood, Blocks.darkplywood, Blocks.darkplywood, Blocks.darkplywood, null, null, null, null, null, Blocks.workbench, 1, false);		
		
		Crafting.registerCraftingRecipe(Items.stick, Items.light, null, null, null, null, null, null, null, Blocks.lightstick, 8, false);
		Crafting.registerCraftingRecipe(Items.stick, Items.dark, null, null, null, null, null, null, null, Blocks.darkstick, 8, false);
		Crafting.registerCraftingRecipe(Items.stick, Items.light, Items.diamond, null, null, null, null, null, null, Blocks.autofence, 8, false);
		Crafting.registerCraftingRecipe(Items.stick, null, null, Items.stick, null, null, Items.stick, null, null, Blocks.post, 8, false);
		Crafting.registerCraftingRecipe(null, Items.stick, null, null, Items.stick, null, null, Items.stick, null, Blocks.post, 8, false);
		Crafting.registerCraftingRecipe(null, null, Items.stick, null, null, Items.stick, null, null, Items.stick, Blocks.post, 8, false);
		
		Crafting.registerCraftingRecipe(Blocks.plywood, Items.stick, Blocks.plywood, Blocks.plywood, Items.stick, Blocks.plywood, Blocks.plywood, Items.stick, Blocks.plywood, Blocks.ladder, 8, true);
		Crafting.registerCraftingRecipe(Blocks.lightplywood, Items.stick, Blocks.lightplywood, Blocks.lightplywood, Items.stick, Blocks.lightplywood, Blocks.lightplywood, Items.stick, Blocks.lightplywood, Blocks.ladder, 4, true);
		Crafting.registerCraftingRecipe(Blocks.darkplywood, Items.stick, Blocks.darkplywood, Blocks.darkplywood, Items.stick, Blocks.darkplywood, Blocks.darkplywood, Items.stick, Blocks.darkplywood, Blocks.ladder, 4, true);
		
		Crafting.registerCraftingRecipe(Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Blocks.blockcopper, 1, false);
		Crafting.registerCraftingRecipe(Blocks.blockcopper, null, null, null, null, null, null, null, null, Items.lumpcopper, 9, false);

		Crafting.registerCraftingRecipe(Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, Blocks.blocktin, 1, false);
		Crafting.registerCraftingRecipe(Blocks.blocktin, null, null, null, null, null, null, null, null, Items.lumptin, 9, false);

		Crafting.registerCraftingRecipe(Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Blocks.blocksilver, 1, false);
		Crafting.registerCraftingRecipe(Blocks.blocksilver, null, null, null, null, null, null, null, null, Items.lumpsilver, 9, false);

		Crafting.registerCraftingRecipe(Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Blocks.blockplatinum, 1, false);
		Crafting.registerCraftingRecipe(Blocks.blockplatinum, null, null, null, null, null, null, null, null, Items.lumpplatinum, 9, false);

		Crafting.registerCraftingRecipe(Items.light, Items.light, Items.light, Items.light, Items.light, Items.light, Items.light, Items.light, Items.light, Blocks.blocklight, 1, false);
		Crafting.registerCraftingRecipe(Blocks.blocklight, null, null, null, null, null, null, null, null, Items.light, 9, false);

		Crafting.registerCraftingRecipe(Items.dark, Items.dark, Items.dark, Items.dark, Items.dark, Items.dark, Items.dark, Items.dark, Items.dark, Blocks.blockdark, 1, false);
		Crafting.registerCraftingRecipe(Blocks.blockdark, null, null, null, null, null, null, null, null, Items.dark, 9, false);

		Crafting.registerCraftingRecipe(Items.string, Items.string, Items.string, Items.string, null, null, null, null, null, Blocks.stickyblock, 1, false);
		Crafting.registerCraftingRecipe(Blocks.stickyblock, null, null, null, null, null, null, null, null,Items.string, 4, false);
		Crafting.registerCraftingRecipe(Items.lumpcopper, null, Items.lumpcopper, Items.lumpcopper, null, Items.lumpcopper, null, Items.lumpcopper, null, Items.bucket, 1, true);

		Crafting.registerCraftingRecipe(Items.diamond, Items.diamond, Items.diamond, Items.diamond, Items.diamond, Items.diamond, Items.diamond, Items.diamond, Items.diamond, Blocks.blockdiamond, 1, false);
		Crafting.registerCraftingRecipe(Blocks.blockdiamond, null, null, null, null, null, null, null, null, Items.diamond, 9, false);
		
		Crafting.registerCraftingRecipe(Items.emerald, Items.emerald, Items.emerald, Items.emerald, Items.emerald, Items.emerald, Items.emerald, Items.emerald, Items.emerald, Blocks.blockemerald, 1, false);
		Crafting.registerCraftingRecipe(Blocks.blockemerald, null, null, null, null, null, null, null, null, Items.emerald, 9, false);
		
		Crafting.registerCraftingRecipe(Items.bloodstone, Items.bloodstone, Items.bloodstone, Items.bloodstone, Items.bloodstone, Items.bloodstone, Items.bloodstone, Items.bloodstone, Items.bloodstone, Blocks.blockbloodstone, 1, false);
		Crafting.registerCraftingRecipe(Blocks.blockbloodstone, null, null, null, null, null, null, null, null, Items.bloodstone, 9, false);
		
		Crafting.registerCraftingRecipe(Items.sunstone, Items.sunstone, Items.sunstone, Items.sunstone, Items.sunstone, Items.sunstone, Items.sunstone, Items.sunstone, Items.sunstone, Blocks.blocksunstone, 1, false);
		Crafting.registerCraftingRecipe(Blocks.blocksunstone, null, null, null, null, null, null, null, null, Items.sunstone, 9, false);
		
		Crafting.registerCraftingRecipe(Items.rice, Items.rice, Items.rice, null, null, null, null, null, null, Items.bread, 2, false);
		Crafting.registerCraftingRecipe(Items.bucketmilk, Items.bucketmilk, null, null, null, null, null, null, null, Items.cheese, 4, false);
		
		Crafting.registerCraftingRecipe(Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.light, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Blocks.waterpump, 1, true);
		Crafting.registerCraftingRecipe(Blocks.waterpump, Items.bucketwater, null, null, null, null, null, null, null, Blocks.waterspout, 1, false);
		Crafting.registerCraftingRecipe(Blocks.stone, Items.bucketwater, null, null, null, null, null, null, null, Blocks.waterstone, 1, false);
		Crafting.registerCraftingRecipe(Blocks.waterspout, Items.bucketwater, null, null, null, null, null, null, null, Blocks.puddlemaker, 1, false);
		Crafting.registerCraftingRecipe(Blocks.waterspout, Items.bucketwater, Items.lumpcopper, null, null, null, null, null, null, Blocks.watercannon, 1, false);
		Crafting.registerCraftingRecipe(Blocks.waterpump, Blocks.blocklight, null, null, null, null, null, null, null, Blocks.waterlight, 1, false);
		Crafting.registerCraftingRecipe(Blocks.waterpump, Blocks.blockdark, null, null, null, null, null, null, null, Blocks.waterdark, 1, false);
		Crafting.registerCraftingRecipe(Items.string, Items.string, Items.string, Items.string, Blocks.waterpump, Items.string, Items.string, Items.string, Items.string, Blocks.musicbox, 1, true);
		Crafting.registerCraftingRecipe(Items.bucketwater, Items.bucketwater, Items.bucketwater, Items.bucketwater, Blocks.waterpump, Items.bucketwater, Items.bucketwater, Items.bucketwater, Items.bucketwater, Blocks.unwaterpump, 1, true);
		Crafting.registerCraftingRecipe(Blocks.waterpump, Items.lumpcopper, null, null, null, null, null, null, null, Blocks.waterswitch, 1, false);
		
		Crafting.registerCraftingRecipe(Items.bucketwater, Items.woodpulp, Items.woodpulp, Items.woodpulp, null, null, null, null, null, Items.paper, 8, false);
		Crafting.registerCraftingRecipe(Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpplatinum, Items.dark, Items.lumpplatinum, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Blocks.shredder, 1, true);
		
		Crafting.registerCraftingRecipe(null, null, null, Blocks.plywood, Blocks.plywood, Blocks.plywood, Blocks.plywood, null, Blocks.plywood, Blocks.desk, 1, true);
		Crafting.registerCraftingRecipe(null, null, null, Blocks.lightplywood, Blocks.lightplywood, Blocks.lightplywood, Blocks.lightplywood, null, Blocks.lightplywood, Blocks.desk, 1, true);
		Crafting.registerCraftingRecipe(null, null, null, Blocks.darkplywood, Blocks.darkplywood, Blocks.darkplywood, Blocks.darkplywood, null, Blocks.darkplywood, Blocks.desk, 1, true);

		Crafting.registerCraftingRecipe(Blocks.plywood, Blocks.plywood, null, null, Blocks.plywood, null, Blocks.plywood, Blocks.plywood, null, Blocks.stand, 1, true);
		Crafting.registerCraftingRecipe(null, Blocks.plywood, Blocks.plywood, null, Blocks.plywood, null, null, Blocks.plywood, Blocks.plywood, Blocks.stand, 1, true);

		Crafting.registerCraftingRecipe(Blocks.lightplywood, Blocks.lightplywood, null, null, Blocks.lightplywood, null, Blocks.lightplywood, Blocks.lightplywood, null, Blocks.stand, 1, true);
		Crafting.registerCraftingRecipe(null, Blocks.lightplywood, Blocks.lightplywood, null, Blocks.lightplywood, null, null, Blocks.lightplywood, Blocks.lightplywood, Blocks.stand, 1, true);

		Crafting.registerCraftingRecipe(Blocks.darkplywood, Blocks.darkplywood, null, null, Blocks.darkplywood, null, Blocks.darkplywood, Blocks.darkplywood, null, Blocks.stand, 1, true);
		Crafting.registerCraftingRecipe(null, Blocks.darkplywood, Blocks.darkplywood, null, Blocks.darkplywood, null, null, Blocks.darkplywood, Blocks.darkplywood, Blocks.stand, 1, true);

		Crafting.registerCraftingRecipe(Items.stick, Items.feather, Items.lumptin, null, null, null, null, null, null, Items.arrow, 2, false);
		Crafting.registerCraftingRecipe(
				null, Items.stick, Items.string, 
				Items.stick, null, Items.string, 
				null, Items.stick, Items.string, Items.bow_empty, 1, true);
		Crafting.registerCraftingRecipe(
				Items.string, Items.stick, null, 
				Items.string, null, Items.stick, 
				Items.string, Items.stick, null, Items.bow_empty, 1, true);
		
		Crafting.registerCraftingRecipe(Items.drone_motor, null, Items.drone_motor, 
				null, Items.drone_body, null, 
				Items.drone_motor, Items.drone_camera, Items.drone_motor, 
				Items.drone, 1, true);
		
		Crafting.registerCraftingRecipe(Items.stick, Items.lumpplatinum, Items.light, Items.stick, null, null, null, null, null, Items.drone_motor, 1, false);
		Crafting.registerCraftingRecipe(Items.stick, null, Items.stick, null, Blocks.plywood, null, Items.stick, null, Items.stick, Items.drone_body, 1, true);
		Crafting.registerCraftingRecipe(Items.stick, null, Items.stick, null, Blocks.lightplywood, null, Items.stick, null, Items.stick, Items.drone_body, 1, true);
		Crafting.registerCraftingRecipe(Items.stick, null, Items.stick, null, Blocks.darkplywood, null, Items.stick, null, Items.stick, Items.drone_body, 1, true);
		Crafting.registerCraftingRecipe(Blocks.plywood, Items.lumpplatinum, Items.light, Items.stick, null, null, null, null, null, Items.drone_camera, 1, false);
		Crafting.registerCraftingRecipe(Blocks.lightplywood, Items.lumpplatinum, Items.light, Items.stick, null, null, null, null, null, Items.drone_camera, 1, false);
		Crafting.registerCraftingRecipe(Blocks.darkplywood, Items.lumpplatinum, Items.light, Items.stick, null, null, null, null, null, Items.drone_camera, 1, false);
		
		//wooden sword
		craftSword(Blocks.plywood, Items.woodensword);
		craftSword(Blocks.lightplywood, Items.woodensword);
		craftSword(Blocks.darkplywood, Items.woodensword);
						
		//wooden axe
		craftAxe(Blocks.plywood, Items.woodenaxe);
		craftAxe(Blocks.lightplywood, Items.woodenaxe);
		craftAxe(Blocks.darkplywood, Items.woodenaxe);
		
		//wooden pickaxe
		craftPickaxe(Blocks.plywood, Items.woodenpickaxe);
		craftPickaxe(Blocks.lightplywood, Items.woodenpickaxe);
		craftPickaxe(Blocks.darkplywood, Items.woodenpickaxe);
		
		//wooden hoe
		craftHoe(Blocks.plywood, Items.woodenhoe);
		craftHoe(Blocks.lightplywood, Items.woodenhoe);
		craftHoe(Blocks.darkplywood, Items.woodenhoe);
		
		//wooden shovel
		craftShovel(Blocks.plywood, Items.woodenshovel);
		craftShovel(Blocks.lightplywood, Items.woodenshovel);
		craftShovel(Blocks.darkplywood, Items.woodenshovel);
		
		//copper things
		craftSword(Items.lumpcopper, Items.coppersword);
		craftAxe(Items.lumpcopper, Items.copperaxe);
		craftPickaxe(Items.lumpcopper, Items.copperpickaxe);
		craftHoe(Items.lumpcopper, Items.copperhoe);
		craftShovel(Items.lumpcopper, Items.coppershovel);
		
		//tin things
		craftSword(Items.lumptin, Items.tinsword);
		craftAxe(Items.lumptin, Items.tinaxe);
		craftPickaxe(Items.lumptin, Items.tinpickaxe);
		craftHoe(Items.lumptin, Items.tinhoe);
		craftShovel(Items.lumptin, Items.tinshovel);
		
		//silver things
		craftSword(Items.lumpsilver, Items.silversword);
		craftAxe(Items.lumpsilver, Items.silveraxe);
		craftPickaxe(Items.lumpsilver, Items.silverpickaxe);
		craftHoe(Items.lumpsilver, Items.silverhoe);
		craftShovel(Items.lumpsilver, Items.silvershovel);
		
		//platinum things
		craftSword(Items.lumpplatinum, Items.platinumsword);
		craftAxe(Items.lumpplatinum, Items.platinumaxe);
		craftPickaxe(Items.lumpplatinum, Items.platinumpickaxe);
		craftHoe(Items.lumpplatinum, Items.platinumhoe);
		craftShovel(Items.lumpplatinum, Items.platinumshovel);
		
		//stone things
		craftSword(Blocks.stone, Items.stonesword);
		craftAxe(Blocks.stone, Items.stoneaxe);
		craftPickaxe(Blocks.stone, Items.stonepickaxe);
		craftHoe(Blocks.stone, Items.stonehoe);
		craftShovel(Blocks.stone, Items.stoneshovel);
		
		//chests
		craftChest(Blocks.plywood);
		craftChest(Blocks.lightplywood);
		craftChest(Blocks.darkplywood);
		
		//furnace
		Crafting.registerCraftingRecipe(Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, null, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.furnace, 1, true);
		//coloring block
		Crafting.registerCraftingRecipe(Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stickyblock, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.coloringblock, 1, true);
		
		
		
		Crafting.registerCraftingRecipe(Items.lumpcopper, Items.lumptin, Items.lumpcopper, null, null, null, null, null, null, Blocks.rail, 8, true);
		Crafting.registerCraftingRecipe(null, null, null, Items.lumpcopper, Items.lumptin, Items.lumpcopper, null, null, null, Blocks.rail, 8, true);
		Crafting.registerCraftingRecipe(null, null, null, null, null, null, Items.lumpcopper, Items.lumptin, Items.lumpcopper, Blocks.rail, 8, true);
		
		Crafting.registerCraftingRecipe(Blocks.rail, Items.light, Items.light, Items.light, null, null, null, null, null, Blocks.railspeed, 1, false);
		Crafting.registerCraftingRecipe(Blocks.rail, Items.string, null, null, null, null, null, null, null, Blocks.railslow, 1, false);
		Crafting.registerCraftingRecipe(Blocks.rail, Blocks.stickyblock, null, null, null, null, null, null, null, Blocks.railstop, 1, false);
		Crafting.registerCraftingRecipe(Blocks.rail, Blocks.blocklight, null, null, null, null, null, null, null, Blocks.railup, 1, false);
		Crafting.registerCraftingRecipe(Blocks.rail, Blocks.blockdark, null, null, null, null, null, null, null, Blocks.raildown, 1, false);
		Crafting.registerCraftingRecipe(Blocks.railup, Blocks.blocklight, null, null, null, null, null, null, null, Blocks.raildplus, 1, false);
		Crafting.registerCraftingRecipe(Blocks.raildown, Blocks.blockdark, null, null, null, null, null, null, null, Blocks.raildminus, 1, false);
		
		Crafting.registerCraftingRecipe(Blocks.rail, Items.light, null, null, null, null, null, null, null, Blocks.railfixedslow, 1, false);
		Crafting.registerCraftingRecipe(Blocks.rail, Items.light, Items.light, null, null, null, null, null, null, Blocks.railfixedmedium, 1, false);
		Crafting.registerCraftingRecipe(Blocks.rail, Items.stick, null, null, null, null, null, null, null, Blocks.railload, 1, false);
		Crafting.registerCraftingRecipe(Blocks.rail, Items.stick, Items.stick, null, null, null, null, null, null, Blocks.railunload, 1, false);
		
		
		Crafting.registerCraftingRecipe(Items.stick, Items.stick, Items.stick, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.light, Items.light, Items.light, Items.maglev, 1, true);
		Crafting.registerCraftingRecipe(Items.stick, Items.string, Items.stick, Items.stick, Items.string, Items.stick, Items.stick, Items.string, Items.stick, Items.raft, 1, true);
		
		//Desk		
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.paper, Items.stick, Items.lumpplatinum, Items.scrolldurability, 1, 200);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.paper, Items.stick, Items.diamond, Items.scrolldurabilityII, 1, 300);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.paper, Items.stick, Items.sunstone, Items.scrollaccuracy, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.paper, Items.stick, Items.bloodstone, Items.scrolldamage, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.paper, Items.stick, Items.light, Items.scrollreach, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.paper, Items.stick, Items.dark, Items.scrollspam, 1, 500);
		
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrolldurability, null, Items.lumpplatinum, Items.scrolldurabilityII, 1, 200);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrolldurability, null, Items.diamond, Items.scrolldurabilityIII, 1, 300);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrollaccuracy, null, Items.sunstone, Items.scrollaccuracyII, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrolldamage, null, Items.bloodstone, Items.scrolldamageII, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrollreach, null, Items.light, Items.scrollreachII, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrollspam, null, Items.dark, Items.scrollspamII, 1, 500);
		
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrolldurabilityII, null, Items.lumpplatinum, Items.scrolldurabilityIII, 1, 200);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrolldurabilityII, null, Items.diamond, Items.scrolldurabilityIV, 1, 300);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrollaccuracyII, null, Items.sunstone, Items.scrollaccuracyIII, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrolldamageII, null, Items.bloodstone, Items.scrolldamageIII, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrollreachII, null, Items.light, Items.scrollreachIII, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrollspamII, null, Items.dark, Items.scrollspamIII, 1, 500);
		
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrolldurabilityIII, null, Items.lumpplatinum, Items.scrolldurabilityIV, 1, 200);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrolldurabilityIII, null, Items.diamond, Items.scrolldurabilityV, 1, 300);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrollaccuracyIII, null, Items.sunstone, Items.scrollaccuracyIV, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrolldamageIII, null, Items.bloodstone, Items.scrolldamageIV, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrollreachIII, null, Items.light, Items.scrollreachIV, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrollspamIII, null, Items.dark, Items.scrollspamIV, 1, 500);
		
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrolldurabilityIV, null, Items.lumpplatinum, Items.scrolldurabilityV, 1, 200);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrolldurabilityIV, null, Items.diamond, Items.scrolldurabilityV, 1, 200);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrollaccuracyIV, null, Items.sunstone, Items.scrollaccuracyV, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrolldamageIV, null, Items.bloodstone, Items.scrolldamageV, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrollreachIV, null, Items.light, Items.scrollreachV, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.scrollspamIV, null, Items.dark, Items.scrollspamV, 1, 500);
		
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.paper, Items.stick, Items.frog_poison1, Items.scrollharm, 1, 1000);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.paper, Items.stick, Items.frog_poison2, Items.scrollharm, 1, 750);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.paper, Items.stick, Items.frog_poison3, Items.scrollharm, 1, 500);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.paper, Items.stick, Items.frog_regen1, Items.scrollheal, 1, 1000);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.paper, Items.stick, Items.frog_regen2, Items.scrollheal, 1, 750);
		DeskCrafting.registerCraftingRecipe(Items.charcoalstick, Items.paper, Items.stick, Items.frog_regen3, Items.scrollheal, 1, 500);
		
		//And now for some action!
		//spawn rates are chance in 1000, not 100.
		//Register an entity with NULL world. 			
		
		Spawnlist.registerSpawn(new Butterfly(null), Dimensions.overworlddimension, null, 50, 110, 20, 3, CreatureTypes.AIR, CreatureTypes.TRANSIENT); //2% (ish) chance of around 3 spawning in air
		Spawnlist.registerSpawn(new Cockroach(null), Dimensions.overworlddimension, null, 30, 100, 20, 3, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Skeletorus(null), Dimensions.overworlddimension, null, 0, 25, 500, 4, CreatureTypes.UNDERGROUND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Rat(null), Dimensions.overworlddimension, null, 0, 30, 800, 10, CreatureTypes.UNDERGROUND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Rat(null), Dimensions.overworlddimension, overworldscragglyforest, 50, 110, 250, 4, CreatureTypes.LAND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Ghost(null), Dimensions.overworlddimension, overworldtallforest, 30, 100, 225, 3, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new GhostSkelly(null), Dimensions.overworlddimension, overworldtallwillowforest, 30, 100, 175, 1, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Moose(null), Dimensions.overworlddimension, overworldforest, 60, 90, 5, 5, CreatureTypes.LAND, CreatureTypes.PERMANENT); 
		Spawnlist.registerSpawn(new Werewolf(null), Dimensions.overworlddimension, overworldforest, 60, 110, 50, 5, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Snarler(null), Dimensions.overworlddimension, null, 50, 110, 125, 4, CreatureTypes.LAND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Fish(null), Dimensions.overworlddimension, null, 40, 70, 100, 5, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Minnow(null), Dimensions.overworlddimension, null, 40, 70, 25, 25, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Eel(null), Dimensions.overworlddimension, null, 40, 70, 75, 5, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		
		Spawnlist.registerSpawn(new Butterfly(null), Dimensions.bigroundtreedimension, null, 50, 110, 20, 3, CreatureTypes.AIR, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Cockroach(null), Dimensions.bigroundtreedimension, null, 30, 100, 20, 3, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Skeletorus(null), Dimensions.bigroundtreedimension, my_biome2b, 0, 25, 500, 4, CreatureTypes.UNDERGROUND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Rat(null), Dimensions.bigroundtreedimension, null, 0, 25, 750, 10, CreatureTypes.UNDERGROUND, CreatureTypes.TRANSIENT);		
		Spawnlist.registerSpawn(new Rat(null), Dimensions.bigroundtreedimension, null, 20, 100, 160, 5, CreatureTypes.LAND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Goose(null), Dimensions.bigroundtreedimension, null, 40, 70, 80, 5, CreatureTypes.WATER, CreatureTypes.PERMANENT); 
		Spawnlist.registerSpawn(new Sparklemuffin(null), Dimensions.bigroundtreedimension, my_biome2a, 60, 90, 100, 3, CreatureTypes.LAND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Anteater(null), Dimensions.bigroundtreedimension, null, 60, 90, 5, 4, CreatureTypes.LAND, CreatureTypes.PERMANENT);
		Spawnlist.registerSpawn(new Fish(null), Dimensions.bigroundtreedimension, null, 40, 70, 100, 5, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Frog(null), Dimensions.bigroundtreedimension, null, 40, 70, 25, 10, CreatureTypes.WATER, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Vixen(null), Dimensions.bigroundtreedimension, null, 60, 100, 25, 3, CreatureTypes.LAND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Minnow(null), Dimensions.bigroundtreedimension, null, 40, 70, 25, 25, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Eel(null), Dimensions.bigroundtreedimension, null, 40, 70, 75, 5, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		
		Spawnlist.registerSpawn(new Butterfly(null), Dimensions.pathwaydimension, null, 50, 110, 20, 3, CreatureTypes.AIR, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Cockroach(null), Dimensions.pathwaydimension, null, 30, 100, 20, 3, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Skeletorus(null), Dimensions.pathwaydimension, null, 0, 25, 500, 4, CreatureTypes.UNDERGROUND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Rat(null), Dimensions.pathwaydimension, null, 0, 25, 800, 10, CreatureTypes.UNDERGROUND, CreatureTypes.TRANSIENT);		
		Spawnlist.registerSpawn(new Goose(null), Dimensions.pathwaydimension, null, 40, 70, 100, 5, CreatureTypes.WATER, CreatureTypes.PERMANENT); 
		Spawnlist.registerSpawn(new Skeletorus(null), Dimensions.pathwaydimension, my_biome3a, 70, 100, 150, 2, CreatureTypes.LAND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Anteater(null), Dimensions.pathwaydimension, null, 60, 90, 5, 4, CreatureTypes.LAND, CreatureTypes.PERMANENT);
		Spawnlist.registerSpawn(new Fish(null), Dimensions.pathwaydimension, null, 40, 70, 100, 5, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Frog(null), Dimensions.pathwaydimension, null, 40, 70, 25, 10, CreatureTypes.WATER, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Vixen(null), Dimensions.pathwaydimension, null, 60, 100, 75, 3, CreatureTypes.LAND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Minnow(null), Dimensions.pathwaydimension, null, 40, 70, 50, 25, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Eel(null), Dimensions.pathwaydimension, null, 40, 70, 75, 5, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Piranah(null), Dimensions.pathwaydimension, null, 40, 70, 15, 25, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		
		Spawnlist.registerSpawn(new Butterfly(null), Dimensions.ruggedhillsdimension, null, 50, 110, 20, 3, CreatureTypes.AIR, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Cockroach(null), Dimensions.ruggedhillsdimension, null, 30, 100, 20, 3, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Skeletorus(null), Dimensions.ruggedhillsdimension, null, 0, 25, 500, 4, CreatureTypes.UNDERGROUND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Rat(null), Dimensions.ruggedhillsdimension, null, 0, 25, 600, 10, CreatureTypes.UNDERGROUND, CreatureTypes.TRANSIENT);		
		Spawnlist.registerSpawn(new Ghost(null), Dimensions.ruggedhillsdimension, ruggedplains, 60, 110, 150, 4, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new GhostSkelly(null), Dimensions.ruggedhillsdimension, ruggedplains, 60, 110, 50, 2, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Werewolf(null), Dimensions.ruggedhillsdimension, ruggedplainsdesert, 60, 110, 250, 5, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new VampireMoose(null), Dimensions.ruggedhillsdimension, ruggedplains2, 60, 110, 100, 2, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Fish(null), Dimensions.ruggedhillsdimension, null, 40, 70, 100, 5, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new AnotherFish(null), Dimensions.ruggedhillsdimension, null, 40, 70, 125, 7, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new PufferFish(null), Dimensions.ruggedhillsdimension, null, 40, 70, 75, 4, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new ButterflyFish(null), Dimensions.ruggedhillsdimension, null, 40, 70, 125, 7, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new StickFish(null), Dimensions.ruggedhillsdimension, null, 40, 70, 75, 3, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Minnow(null), Dimensions.ruggedhillsdimension, null, 40, 70, 75, 25, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Piranah(null), Dimensions.ruggedhillsdimension, null, 40, 70, 15, 25, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new EntityVolcano(null), Dimensions.ruggedhillsdimension, ruggedmountains, 100, 150, 1, 1, CreatureTypes.LAND, CreatureTypes.PERMANENT); 
		Spawnlist.registerSpawn(new DesertRainFrog(null), Dimensions.ruggedhillsdimension, ruggedplainsdesert, 50, 110, 250, 10, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Eel(null), Dimensions.ruggedhillsdimension, null, 40, 70, 75, 5, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new HammerheadShark(null), Dimensions.ruggedhillsdimension, null, 40, 70, 25, 3, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		
		Spawnlist.registerSpawn(new Butterfly(null), Dimensions.skyislandsdimension, null, 100, 220, 20, 3, CreatureTypes.AIR, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Cockroach(null), Dimensions.skyislandsdimension, null, 60, 180, 20, 3, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 	
		Spawnlist.registerSpawn(new Butterfly(null), Dimensions.skyislandsdimension, null, 100, 220, 20, 3, CreatureTypes.AIR, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Ghost(null), Dimensions.skyislandsdimension, null, 100, 220, 200, 3, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new GhostSkelly(null), Dimensions.skyislandsdimension, null, 100, 220, 175, 1, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Rat(null), Dimensions.skyislandsdimension, null, 100, 180, 250, 5, CreatureTypes.LAND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Cockroach(null), Dimensions.skyislandsdimension, null, 100, 180, 25, 3, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Vampire(null), Dimensions.skyislandsdimension, null, 100, 220, 200, 3, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new TheCount(null), Dimensions.skyislandsdimension, null, 100, 220, 100, 1, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new VampireMoose(null), Dimensions.skyislandsdimension, null, 100, 220, 100, 1, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Ostrich(null), Dimensions.skyislandsdimension, null, 100, 180, 15, 4, CreatureTypes.LAND, CreatureTypes.PERMANENT); 
		Spawnlist.registerSpawn(new Frog(null), Dimensions.skyislandsdimension, null, 100, 220, 25, 10, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		
		Spawnlist.registerSpawn(new Butterfly(null), Dimensions.windsweptdimension, null, 50, 90, 120, 3, CreatureTypes.AIR, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Cockroach(null), Dimensions.windsweptdimension, null, 30, 70, 440, 7, CreatureTypes.LAND, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Skeletorus(null), Dimensions.windsweptdimension, null, 30, 70, 300, 2, CreatureTypes.LAND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Sparklemuffin(null), Dimensions.windsweptdimension, null, 30, 70, 300, 1, CreatureTypes.LAND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Rat(null), Dimensions.windsweptdimension, null, 30, 70, 400, 8, CreatureTypes.LAND, CreatureTypes.TRANSIENT);
		Spawnlist.registerSpawn(new Snarler(null), Dimensions.windsweptdimension, null, 30, 55, 200, 8, CreatureTypes.LAND, CreatureTypes.TRANSIENT);
		
		Spawnlist.registerSpawn(new Butterfly(null), Dimensions.pleasantvilledimension, null, 50, 110, 20, 3, CreatureTypes.AIR, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Mermaid(null), Dimensions.pleasantvilledimension, null, 40, 70, 15, 8, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new PufferFish(null), Dimensions.pleasantvilledimension, null, 40, 70, 75, 4, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new ButterflyFish(null), Dimensions.pleasantvilledimension, null, 40, 70, 125, 7, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new StickFish(null), Dimensions.pleasantvilledimension, null, 40, 70, 75, 3, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 
		Spawnlist.registerSpawn(new Minnow(null), Dimensions.pleasantvilledimension, null, 40, 70, 75, 25, CreatureTypes.WATER, CreatureTypes.TRANSIENT); 

		
		//NOT USED, but used to work. Leaving as an example. Taken out for server hardening.
		//furnacepacket = new FurnaceInventoryPacket();
		//CustomPackets.registerCustomPacket(furnacepacket); //register my receivers!!!
		
		//more crafting...
		Crafting.registerCraftingRecipe(Items.peach, null, null, null, null, null, null, null, null, Items.peachseed, 1, false);
		Crafting.registerCraftingRecipe(Items.cherries, null, null, null, null, null, null, null, null, Items.cherryseed, 3, false);
		Crafting.registerCraftingRecipe(Items.apple, null, null, null, null, null, null, null, null, Items.appleseed, 6, false);
		Crafting.registerCraftingRecipe(Blocks.glass, Blocks.flower_pink, null, null, null, null, null, null, null, Blocks.blueglass, 1, false);
		Crafting.registerCraftingRecipe(Blocks.glass, Blocks.flower_red, null, null, null, null, null, null, null, Blocks.redglass, 1, false);
		Crafting.registerCraftingRecipe(Blocks.glass, Blocks.grass, null, null, null, null, null, null, null, Blocks.greenglass, 1, false);
		Crafting.registerCraftingRecipe(Blocks.glass, Blocks.flower_purple, null, null, null, null, null, null, null, Blocks.violetglass, 1, false);
		Crafting.registerCraftingRecipe(Blocks.glass, Blocks.flower_yellow, null, null, null, null, null, null, null, Blocks.yellowglass, 1, false);
		
		Crafting.registerCraftingRecipe(Blocks.stone, Blocks.stone, null, null, null, null, null, null, null, Blocks.stone2, 2, false);
		Crafting.registerCraftingRecipe(Blocks.stone, Blocks.stone, Blocks.stone, null, null, null, null, null, null, Blocks.stone3, 3, false);
		Crafting.registerCraftingRecipe(Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, null, null, null, null, null, Blocks.stone4, 4, false);
		Crafting.registerCraftingRecipe(Blocks.greystone, Blocks.greystone, null, null, null, null, null, null, null, Blocks.greystone2, 2, false);
		Crafting.registerCraftingRecipe(Blocks.greystone, Blocks.greystone, Blocks.greystone, null, null, null, null, null, null, Blocks.greystone3, 3, false);
		Crafting.registerCraftingRecipe(Blocks.greystone, Blocks.greystone, Blocks.greystone, Blocks.greystone, null, null, null, null, null, Blocks.greystone4, 4, false);
		
		//armor
		Crafting.registerCraftingRecipe(null, null, null, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, null, Items.lumpcopper, Items.copperhelmet, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, null, Items.lumpcopper, null, null, null, Items.copperhelmet, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, null, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.copperchestplate, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, Items.lumpcopper, null, Items.lumpcopper, Items.lumpcopper, null, Items.lumpcopper, Items.copperleggings, 1, true);
		Crafting.registerCraftingRecipe(null, null, null, Items.lumpcopper, null, Items.lumpcopper, Items.lumpcopper, null, Items.lumpcopper, Items.copperboots, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpcopper, null, Items.lumpcopper, Items.lumpcopper, null, Items.lumpcopper, null, null, null, Items.copperboots, 1, true);

		Crafting.registerCraftingRecipe(null, null, null, Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, null, Items.lumptin, Items.tinhelmet, 1, true);
		Crafting.registerCraftingRecipe(Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, null, Items.lumptin, null, null, null, Items.tinhelmet, 1, true);
		Crafting.registerCraftingRecipe(Items.lumptin, null, Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, Items.tinchestplate, 1, true);
		Crafting.registerCraftingRecipe(Items.lumptin, Items.lumptin, Items.lumptin, Items.lumptin, null, Items.lumptin, Items.lumptin, null, Items.lumptin, Items.tinleggings, 1, true);
		Crafting.registerCraftingRecipe(null, null, null, Items.lumptin, null, Items.lumptin, Items.lumptin, null, Items.lumptin, Items.tinboots, 1, true);
		Crafting.registerCraftingRecipe(Items.lumptin, null, Items.lumptin, Items.lumptin, null, Items.lumptin, null, null, null, Items.tinboots, 1, true);

		Crafting.registerCraftingRecipe(null, null, null, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, null, Items.lumpsilver, Items.silverhelmet, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, null, Items.lumpsilver, null, null, null, Items.silverhelmet, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpsilver, null, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.silverchestplate, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, Items.lumpsilver, null, Items.lumpsilver, Items.lumpsilver, null, Items.lumpsilver, Items.silverleggings, 1, true);
		Crafting.registerCraftingRecipe(null, null, null, Items.lumpsilver, null, Items.lumpsilver, Items.lumpsilver, null, Items.lumpsilver, Items.silverboots, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpsilver, null, Items.lumpsilver, Items.lumpsilver, null, Items.lumpsilver, null, null, null, Items.silverboots, 1, true);

		Crafting.registerCraftingRecipe(null, null, null, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, null, Items.lumpplatinum, Items.platinumhelmet, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, null, Items.lumpplatinum, null, null, null, Items.platinumhelmet, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpplatinum, null, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.platinumchestplate, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, Items.lumpplatinum, null, Items.lumpplatinum, Items.lumpplatinum, null, Items.lumpplatinum, Items.platinumleggings, 1, true);
		Crafting.registerCraftingRecipe(null, null, null, Items.lumpplatinum, null, Items.lumpplatinum, Items.lumpplatinum, null, Items.lumpplatinum, Items.platinumboots, 1, true);
		Crafting.registerCraftingRecipe(Items.lumpplatinum, null, Items.lumpplatinum, Items.lumpplatinum, null, Items.lumpplatinum, null, null, null, Items.platinumboots, 1, true);

		Crafting.registerCraftingRecipe(Items.light, Items.dark, null, null, null, null, null, null, null, Items.instability, 2, false);
		Crafting.registerCraftingRecipe(Blocks.blocklight, Blocks.blockdark, null, null, null, null, null, null, null, Items.instabilitylarge, 2, false);
		Crafting.registerCraftingRecipe(Items.instability, Items.instability, Items.instability, Items.instability, Items.instability, Items.instability, Items.instability, Items.instability, Items.instability, Items.instabilitylarge, 1, false);
		Crafting.registerCraftingRecipe(Items.instabilitylarge, Items.instabilitylarge, Items.instabilitylarge, Items.instabilitylarge, Items.instabilitylarge, Items.instabilitylarge, Items.instabilitylarge, Items.instabilitylarge, Items.instabilitylarge, Items.instabilityhuge, 1, false);
		Crafting.registerCraftingRecipe(Items.instability, Items.stick, null, null, null, null, null, null, null, Items.firestick, 1, false);
		Crafting.registerCraftingRecipe(Items.light, Items.lumpcopper, null, null, null, null, null, null, null, Items.dropstick, 1, false);
		Crafting.registerCraftingRecipe(Blocks.autofence, Items.diamond, null, null, null, null, null, null, null, Items.autofencekey, 1, false);
		
		Crafting.registerCraftingRecipe(Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, Items.instability, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.blockinstability, 1, true);
		Crafting.registerCraftingRecipe(Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, Items.instabilitylarge, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.blockinstability_large, 1, true);
		Crafting.registerCraftingRecipe(Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, Items.instabilityhuge, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.blockinstability_huge, 1, true);
		
		
		
		//door
		Crafting.registerCraftingRecipe(Blocks.plywood, Blocks.plywood, null, Blocks.plywood, Blocks.plywood, null, Blocks.plywood, Blocks.plywood, null, Items.door, 1, true);
		Crafting.registerCraftingRecipe(Blocks.darkplywood, Blocks.darkplywood, null, Blocks.darkplywood, Blocks.darkplywood, null, Blocks.darkplywood, Blocks.darkplywood, null, Items.door, 1, true);
		Crafting.registerCraftingRecipe(Blocks.lightplywood, Blocks.lightplywood, null, Blocks.lightplywood, Blocks.lightplywood, null, Blocks.lightplywood, Blocks.lightplywood, null, Items.door, 1, true);

		Crafting.registerCraftingRecipe(null, Blocks.plywood, Blocks.plywood, null, Blocks.plywood, Blocks.plywood, null, Blocks.plywood, Blocks.plywood, Items.door, 1, true);
		Crafting.registerCraftingRecipe(null, Blocks.darkplywood, Blocks.darkplywood, null, Blocks.darkplywood, Blocks.darkplywood, null, Blocks.darkplywood, Blocks.darkplywood, Items.door, 1, true);
		Crafting.registerCraftingRecipe(null, Blocks.lightplywood, Blocks.lightplywood, null, Blocks.lightplywood, Blocks.lightplywood, null, Blocks.lightplywood, Blocks.lightplywood, Items.door, 1, true);
		
		Crafting.registerCraftingRecipe(Blocks.plywood, Items.moosebone, null, null, null, null, null, null, null, Items.sign, 1, false);
		Crafting.registerCraftingRecipe(Blocks.darkplywood, Items.moosebone, null, null, null, null, null, null, null, Items.sign, 1, false);
		Crafting.registerCraftingRecipe(Blocks.lightplywood, Items.moosebone, null, null, null, null, null, null, null, Items.sign, 1, false);
		
		Shredding.registerShredding(Items.woodchips, Items.woodpulp, 1);
		Shredding.registerShredding(Items.woodenaxe, Items.woodchips, 1);
		Shredding.registerShredding(Items.woodenshovel, Items.woodchips, 1);
		Shredding.registerShredding(Items.woodenpickaxe, Items.woodchips, 1);
		Shredding.registerShredding(Items.woodenhoe, Items.woodchips, 1);
		Shredding.registerShredding(Items.woodensword, Items.woodchips, 1);
		Shredding.registerShredding(Items.stick, Items.woodchips, 1);
		Shredding.registerShredding(Blocks.plywood, Items.woodchips, 4);
		Shredding.registerShredding(Blocks.darkplywood, Items.woodchips, 4);
		Shredding.registerShredding(Blocks.lightplywood, Items.woodchips, 4);
		Shredding.registerShredding(Blocks.redwoodlog, Items.woodchips, 16);
		Shredding.registerShredding(Blocks.log, Items.woodchips, 16);
		Shredding.registerShredding(Blocks.willowlog, Items.woodchips, 16);
		Shredding.registerShredding(Items.paper, Items.woodpulp, 1);
		Shredding.registerShredding(Items.scrollaccuracy, Items.woodpulp, 1);
		Shredding.registerShredding(Items.scrolldamage, Items.woodpulp, 1);
		Shredding.registerShredding(Items.scrolldurability, Items.woodpulp, 1);
		Shredding.registerShredding(Items.scrollreach, Items.woodpulp, 1);
		Shredding.registerShredding(Items.scrollspam, Items.woodpulp, 1);
		
		addAchievements();
		


		//add some color!
		fengshui = new DZWorldDecorator();
		WorldDecorators.registerWorldDecorator(fengshui);
		
		//and a command handler!
		CommandHandlers.registerCommandHandler(new DZCommandHandler());
		
	}
	
	private void registerSounds(){
		DangerZone.soundmangler.registerSound("DangerZone:pop", "res/sounds/pop.wav");
		DangerZone.soundmangler.registerSound("DangerZone:blockbreak", "res/sounds/blockbreak.wav");
		DangerZone.soundmangler.registerSound("DangerZone:blockplace", "res/sounds/blockplace.wav");
		DangerZone.soundmangler.registerSound("DangerZone:blockhit", "res/sounds/blockhit.wav");
		DangerZone.soundmangler.registerSound("DangerZone:leavesbreak", "res/sounds/leavesbreak.wav");
		DangerZone.soundmangler.registerSound("DangerZone:leavesplace", "res/sounds/leavesplace.wav");
		DangerZone.soundmangler.registerSound("DangerZone:leaves_hit", "res/sounds/leaves_hit.wav");	
		DangerZone.soundmangler.registerSound("DangerZone:ghost_sound", "res/sounds/ghost_sound.wav");
		DangerZone.soundmangler.registerSound("DangerZone:ratdead1", "res/sounds/ratdead1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:ratdead2", "res/sounds/ratdead2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:ratdead3", "res/sounds/ratdead3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:chain_rattles", "res/sounds/chain_rattles.wav");		
		DangerZone.soundmangler.registerSound("DangerZone:rathit", "res/sounds/rathit.wav");
		DangerZone.soundmangler.registerSound("DangerZone:ratlive", "res/sounds/ratlive.wav");
		DangerZone.soundmangler.registerSound("DangerZone:woodbreak", "res/sounds/woodbreak.wav");
		DangerZone.soundmangler.registerSound("DangerZone:woodplace", "res/sounds/woodplace.wav");
		DangerZone.soundmangler.registerSound("DangerZone:woodhit", "res/sounds/woodhit.wav");	
		DangerZone.soundmangler.registerSound("DangerZone:stonebreak", "res/sounds/stonebreak.wav");
		DangerZone.soundmangler.registerSound("DangerZone:stoneplace", "res/sounds/stoneplace.wav");
		DangerZone.soundmangler.registerSound("DangerZone:stonehit", "res/sounds/stonehit.wav");
		DangerZone.soundmangler.registerSound("DangerZone:big_splat", "res/sounds/big_splat.wav");
		DangerZone.soundmangler.registerSound("DangerZone:big_splash", "res/sounds/big_splash.wav");
		DangerZone.soundmangler.registerSound("DangerZone:ouch1", "res/sounds/ouch1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:ouch2", "res/sounds/ouch2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:ouch3", "res/sounds/ouch3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:eating", "res/sounds/eating.wav");
		DangerZone.soundmangler.registerSound("DangerZone:crystalblockbreak", "res/sounds/crystalblockbreak.wav");
		DangerZone.soundmangler.registerSound("DangerZone:crystalblockhit", "res/sounds/crystalblockhit.wav");
		DangerZone.soundmangler.registerSound("DangerZone:crystalblockplace", "res/sounds/crystalblockplace.wav");
		DangerZone.soundmangler.registerSound("DangerZone:furnace_ding", "res/sounds/furnace_ding.wav");
		DangerZone.soundmangler.registerSound("DangerZone:ding", "res/sounds/ding.wav");
		DangerZone.soundmangler.registerSound("DangerZone:moose_hit", "res/sounds/moose_hit.wav");
		DangerZone.soundmangler.registerSound("DangerZone:moose_death", "res/sounds/moose_death.wav");
		DangerZone.soundmangler.registerSound("DangerZone:burp", "res/sounds/burp.wav");
		DangerZone.soundmangler.registerSound("DangerZone:burp1", "res/sounds/burp1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:burp2", "res/sounds/burp2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:burp3", "res/sounds/burp3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:goose_hit", "res/sounds/goose_hit.wav");
		DangerZone.soundmangler.registerSound("DangerZone:goose_death", "res/sounds/goose_death.wav");
		DangerZone.soundmangler.registerSound("DangerZone:goose_living", "res/sounds/goose_living.wav");
		DangerZone.soundmangler.registerSound("DangerZone:dirt_hit", "res/sounds/dirt_hit.wav");
		DangerZone.soundmangler.registerSound("DangerZone:dirt_place", "res/sounds/dirt_place.wav");
		DangerZone.soundmangler.registerSound("DangerZone:fuzzbutt_hit", "res/sounds/fuzzbutt_hit.wav");
		DangerZone.soundmangler.registerSound("DangerZone:fuzzbutt_death", "res/sounds/fuzzbutt_death.wav");
		DangerZone.soundmangler.registerSound("DangerZone:fuzzbutt_living", "res/sounds/fuzzbutt_living.wav");
		DangerZone.soundmangler.registerSound("DangerZone:anteater_hit", "res/sounds/anteater_hit.wav");
		DangerZone.soundmangler.registerSound("DangerZone:little_splat", "res/sounds/little_splat.wav");
		DangerZone.soundmangler.registerSound("DangerZone:little_splash", "res/sounds/little_splash.wav");
		DangerZone.soundmangler.registerSound("DangerZone:bow", "res/sounds/bow.wav");
		DangerZone.soundmangler.registerSound("DangerZone:vampire_living", "res/sounds/vampire_living.wav");
		DangerZone.soundmangler.registerSound("DangerZone:werewolf_attack", "res/sounds/werewolf_attack.wav");
		DangerZone.soundmangler.registerSound("DangerZone:werewolf_death", "res/sounds/werewolf_death.wav");
		DangerZone.soundmangler.registerSound("DangerZone:werewolf_living", "res/sounds/werewolf_living.wav");
		DangerZone.soundmangler.registerSound("DangerZone:small_explosion1", "res/sounds/small_explosion1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:small_explosion2", "res/sounds/small_explosion2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:small_explosion3", "res/sounds/small_explosion3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:small_explosion4", "res/sounds/small_explosion4.wav");
		DangerZone.soundmangler.registerSound("DangerZone:small_explosion5", "res/sounds/small_explosion5.wav");
		DangerZone.soundmangler.registerSound("DangerZone:large_explosion1", "res/sounds/large_explosion1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:large_explosion2", "res/sounds/large_explosion2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:large_explosion3", "res/sounds/large_explosion3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:cryo_hurt", "res/sounds/cryo_hurt.wav");
		DangerZone.soundmangler.registerSound("DangerZone:cryo_death", "res/sounds/cryo_death.wav");
		DangerZone.soundmangler.registerSound("DangerZone:chest_open", "res/sounds/chest_open.wav");
		DangerZone.soundmangler.registerSound("DangerZone:chest_close", "res/sounds/chest_close.wav");
		DangerZone.soundmangler.registerSound("DangerZone:coin_deposit", "res/sounds/coin_deposit.wav");
		DangerZone.soundmangler.registerSound("DangerZone:coin_drop", "res/sounds/coin_drop.wav");
		DangerZone.soundmangler.registerSound("DangerZone:coin_jingle", "res/sounds/coin_jingle.wav");
		DangerZone.soundmangler.registerSound("DangerZone:coin_shake", "res/sounds/coin_shake.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:dirt1", "res/sounds/dirt1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:dirt2", "res/sounds/dirt2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:dirt3", "res/sounds/dirt3.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:grass1", "res/sounds/grass1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:grass2", "res/sounds/grass2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:grass3", "res/sounds/grass3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:grass4", "res/sounds/grass4.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:leaves1", "res/sounds/leaves1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:leaves2", "res/sounds/leaves2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:leaves3", "res/sounds/leaves3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:leaves4", "res/sounds/leaves4.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:sand1", "res/sounds/sand1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:sand2", "res/sounds/sand2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:sand3", "res/sounds/sand3.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:stone1", "res/sounds/stone1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:stone2", "res/sounds/stone2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:stone3", "res/sounds/stone3.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:wood1", "res/sounds/wood1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:wood2", "res/sounds/wood2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:wood3", "res/sounds/wood3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:wood4", "res/sounds/wood4.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:thunder1", "res/sounds/thunder1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:thunder2", "res/sounds/thunder2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:thunder3", "res/sounds/thunder3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:thunder4", "res/sounds/thunder4.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:rain1", "res/sounds/rain1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rain2", "res/sounds/rain2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rain3", "res/sounds/rain3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rain4", "res/sounds/rain4.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rain5", "res/sounds/rain5.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rain6", "res/sounds/rain6.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rain7", "res/sounds/rain7.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rain8", "res/sounds/rain8.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:teleport1", "res/sounds/teleport1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:teleport2", "res/sounds/teleport2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:teleport3", "res/sounds/teleport3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:frog1", "res/sounds/frog1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:frog2", "res/sounds/frog2.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:morph1", "res/sounds/morph1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:morph2", "res/sounds/morph2.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:toolbreak1", "res/sounds/toolbreak1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:toolbreak2", "res/sounds/toolbreak2.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:arc", "res/sounds/arc.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:magic1", "res/sounds/magic1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:magic2", "res/sounds/magic2.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:shredder1", "res/sounds/shredder1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:shredder2", "res/sounds/shredder2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:shredder3", "res/sounds/shredder3.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:drawer_open", "res/sounds/drawer_open.wav");
		DangerZone.soundmangler.registerSound("DangerZone:drawer_close", "res/sounds/drawer_close.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:door_open1", "res/sounds/door_open1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:door_close1", "res/sounds/door_close1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:door_open2", "res/sounds/door_open2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:door_close2", "res/sounds/door_close2.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:arrow_hit1", "res/sounds/arrow_hit1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:arrow_hit2", "res/sounds/arrow_hit2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:arrow_hit3", "res/sounds/arrow_hit3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:arrow_hit4", "res/sounds/arrow_hit4.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:vixen_hurt1", "res/sounds/vixen_hurt1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:vixen_hurt2", "res/sounds/vixen_hurt2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:vixen_hurt3", "res/sounds/vixen_hurt3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:vixen_hurt4", "res/sounds/vixen_hurt4.wav");
		DangerZone.soundmangler.registerSound("DangerZone:vixen_hurt5", "res/sounds/vixen_hurt5.wav");
		DangerZone.soundmangler.registerSound("DangerZone:vixen_hurt6", "res/sounds/vixen_hurt6.wav");
		DangerZone.soundmangler.registerSound("DangerZone:vixen_hurt7", "res/sounds/vixen_hurt7.wav");
		DangerZone.soundmangler.registerSound("DangerZone:vixen_hurt8", "res/sounds/vixen_hurt8.wav");
		DangerZone.soundmangler.registerSound("DangerZone:vixen_hurt9", "res/sounds/vixen_hurt9.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:drone", "res/sounds/drone.wav");
		DangerZone.soundmangler.registerSound("DangerZone:motor_updown", "res/sounds/electric_updown.wav");
		DangerZone.soundmangler.registerSound("DangerZone:motor_openclose", "res/sounds/electric_openclose.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:swish1", "res/sounds/swish1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:swish2", "res/sounds/swish2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:swish3", "res/sounds/swish3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:swish4", "res/sounds/swish4.wav");
		DangerZone.soundmangler.registerSound("DangerZone:swish5", "res/sounds/swish5.wav");
		DangerZone.soundmangler.registerSound("DangerZone:swish6", "res/sounds/swish6.wav");
		DangerZone.soundmangler.registerSound("DangerZone:swish7", "res/sounds/swish7.wav");
		DangerZone.soundmangler.registerSound("DangerZone:swish8", "res/sounds/swish8.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:humm1", "res/sounds/humm1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:humm2", "res/sounds/humm2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:stopscreech1", "res/sounds/stopscreech1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:stopscreech2", "res/sounds/stopscreech2.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:growl1", "res/sounds/growl1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:growl2", "res/sounds/growl2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:growl3", "res/sounds/growl3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:growl4", "res/sounds/growl4.wav");
		DangerZone.soundmangler.registerSound("DangerZone:growl5", "res/sounds/growl5.wav");
		DangerZone.soundmangler.registerSound("DangerZone:growl6", "res/sounds/growl6.wav");
		DangerZone.soundmangler.registerSound("DangerZone:growl7", "res/sounds/growl7.wav");
		DangerZone.soundmangler.registerSound("DangerZone:growl8", "res/sounds/growl8.wav");
		DangerZone.soundmangler.registerSound("DangerZone:growl9", "res/sounds/growl9.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:chirp1", "res/sounds/chirp1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:chirp2", "res/sounds/chirp2.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:batattack1", "res/sounds/batattack1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:batattack2", "res/sounds/batattack2.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:martian_death", "res/sounds/martian_death.wav");
		DangerZone.soundmangler.registerSound("DangerZone:martian_hit", "res/sounds/martian_hit.wav");
		DangerZone.soundmangler.registerSound("DangerZone:martian_living1", "res/sounds/martian_living1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:martian_living2", "res/sounds/martian_living2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:martian_living3", "res/sounds/martian_living3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:martian_living4", "res/sounds/martian_living4.wav");
		
		DangerZone.soundmangler.registerSound("DangerZone:rainfrog_death", "res/sounds/rainfrog_death.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rainfrog_hit", "res/sounds/rainfrog_hit.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rainfrog1", "res/sounds/rainfrog1.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rainfrog2", "res/sounds/rainfrog2.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rainfrog3", "res/sounds/rainfrog3.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rainfrog4", "res/sounds/rainfrog4.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rainfrog5", "res/sounds/rainfrog5.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rainfrog6", "res/sounds/rainfrog6.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rainfrog7", "res/sounds/rainfrog7.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rainfrog8", "res/sounds/rainfrog8.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rainfrog9", "res/sounds/rainfrog9.wav");
		DangerZone.soundmangler.registerSound("DangerZone:rainfrog10", "res/sounds/rainfrog10.wav");
		
	}
	
	public void addAchievements(){
		
		ToDoList.registerToDoItem(new SwordCrafter("DangerZone:task1", "Novice Swordsman", "Craft a sword!", ToDoList.CRAFTED));
		ToDoList.registerToDoItem(new PickaxeCrafter("DangerZone:task2", "Noob Miner", "Craft a pickaxe.", ToDoList.CRAFTED));
		ToDoList.registerToDoItem(new HoeCrafter("DangerZone:task3", "Real Gardener", "Craft a hoe.", ToDoList.CRAFTED));
		ToDoList.registerToDoItem(new ShovelCrafter("DangerZone:task4", "Ditch Digger", "Craft a shovel.", ToDoList.CRAFTED));
		ToDoList.registerToDoItem(new AxeCrafter("DangerZone:task5", "Lumberjack", "Craft an axe.", ToDoList.CRAFTED));
		ToDoList.registerToDoItem(new HelmetCrafter("DangerZone:task6", "Thick-Skulled Bonehead", "Craft a helmet.", ToDoList.CRAFTED));		
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task7", "Awful Archer", "Craft a bow!", ToDoList.CRAFTED, Items.bow_empty));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task8", "Dreadful Baker", "Craft some bread.", ToDoList.CRAFTED, Items.bread));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task9", "Scribe", "Craft a charcoal stick.", ToDoList.CRAFTED, Items.charcoalstick));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task10", "Cheesehead", "Craft some cheese,\nof course!", ToDoList.CRAFTED, Items.cheese));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task11", "Hermit", "Craft a door.", ToDoList.CRAFTED, Items.door));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task12", "Drone Operator", "Craft a drone.", ToDoList.CRAFTED, Items.drone));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task13", "Ruthless Wrecker", "Craft a dropstick.", ToDoList.CRAFTED, Items.dropstick));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task14", "Flaming Pyro", "Craft a firestick.", ToDoList.CRAFTED, Items.firestick));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task15", "Unibomber", "Craft an instability!", ToDoList.CRAFTED, Items.instability));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task16", "Railroad Engineer", "Craft a Maglev cart!", ToDoList.CRAFTED, Items.maglev));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task17", "Printer", "Craft some paper.", ToDoList.CRAFTED, Items.paper));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task18", "Castaway", "Craft a raft.", ToDoList.CRAFTED, Items.raft));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task19", "Despised Advertiser", "Craft a sign.", ToDoList.CRAFTED, Items.sign));		
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task20", "Hoarder", "Craft a chest.", ToDoList.CRAFTED, Blocks.chest));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task21", "Evil Wannabe", "Craft a darkstick.", ToDoList.CRAFTED, Blocks.darkstick));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task22", "Budding Writer", "Craft a desk.", ToDoList.CRAFTED, Blocks.desk));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task23", "Cook", "Craft a furnace.", ToDoList.CRAFTED, Blocks.furnace));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task24", "Window Washer", "Craft some glass.", ToDoList.CRAFTED, Blocks.glass));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task25", "Climber", "Craft a ladder.", ToDoList.CRAFTED, Blocks.ladder));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task26", "DJ", "Craft a music box.", ToDoList.CRAFTED, Blocks.musicbox));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task27", "RailRoad Builder", "Craft a rail.", ToDoList.CRAFTED, Blocks.rail));
		//missing 28
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task29", "FBI Employee", "Craft a shredder.", ToDoList.CRAFTED, Blocks.shredder));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task30", "Plumber", "Craft a water pump.", ToDoList.CRAFTED, Blocks.waterpump));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task31", "Naval Officer", "Craft a water cannon.", ToDoList.CRAFTED, Blocks.watercannon));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task32", "Plumber", "Craft a water pump.", ToDoList.CRAFTED, Blocks.waterpump));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task33", "Aqua Decorator", "Craft a water spout.", ToDoList.CRAFTED, Blocks.waterspout));		
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task34", "Normal Human", "Craft a lightstick.", ToDoList.CRAFTED, Blocks.lightstick));
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:task35", "Craftsman", "Craft a workbench.", ToDoList.CRAFTED, Blocks.workbench));
		ToDoList.registerToDoItem(new ScrollCrafter("DangerZone:task36", "Wizard Apprentice", "Craft a scroll!", ToDoList.CRAFTED));
		ToDoList.registerToDoItem(new FenceBuilder("DangerZone:autofencekey", "Fence Builder", "Craft an auto-fence key,\nand some auto-fencing.", ToDoList.CRAFTED, Items.autofencekey));
		//hidden flag (null title)! Used as part of Fence Builder. Called, but not displayed. You could also just keep track of both things in the one...
		ToDoList.registerToDoItem(new CompareCrafter("DangerZone:autofence", null, null, ToDoList.CRAFTED, Blocks.autofence));		
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task37", "Extreme Bravery", null, ToDoList.KILLED, Butterfly.class, "Butterflies", 20));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task38", "Moose Master", null, ToDoList.KILLED, VampireMoose.class, "Vampire Moose", 1));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task39", "Ghost Hunter", null, ToDoList.KILLED, Ghost.class, "Ghosts", 10));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task40", "Ghost Master", null, ToDoList.KILLED, GhostSkelly.class, "Ghost Skellys", 5));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task41", "Exterminator", null, ToDoList.KILLED, Rat.class, "Rats", 25));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task42", "Bug Buster", null, ToDoList.KILLED, Cockroach.class, "Cockroachs", 50));		
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task43", "Carnivore", null, ToDoList.KILLED, Moose.class, "Moose", 1));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task44", "Spider Hunter", null, ToDoList.KILLED, Skeletorus.class, "Skeletorus", 1));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task45", "Spider Master", null, ToDoList.KILLED, Sparklemuffin.class, "Sparklemuffin", 1));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task46", "Vampire Hunter", null, ToDoList.KILLED, Vampire.class, "Vampires", 10));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task47", "Vampire Nightmare", null, ToDoList.KILLED, TheCount.class, "The Count", 1));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task48", "Moose Master", null, ToDoList.KILLED, VampireMoose.class, "Vampire Moose", 1));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task49", "Fisherman", null, ToDoList.KILLED, Fish.class, "Fish", 20));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task50", "Misogynist", null, ToDoList.KILLED, Vixen.class, "Vixens", 5));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task51", "Insect Saviour", null, ToDoList.KILLED, BulletBat.class, "Bullet Bats", 5));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task52", "Land Lubber", null, ToDoList.KILLED, Mermaid.class, "Mermaid", 1));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task53", "Racist", null, ToDoList.KILLED, Martian.class, "Martians", 20));
		ToDoList.registerToDoItem(new MobKiller("DangerZone:task54", "Poseidon", null, ToDoList.KILLED, HammerheadShark.class, "Hammerhead Shark", 1));
		
		ToDoList.registerToDoItem(new MobSpawner("DangerZone:task55", "Down Pillow Maker", null, ToDoList.SPAWNED, Gosling.class, "Goslings", 10));
		ToDoList.registerToDoItem(new MobSpawner("DangerZone:task55b", "Tower of Power", null, ToDoList.SPAWNED, Flag.class, "Tower Defense Flag", 1));
		
		ToDoList.registerToDoItem(new MobTamer("DangerZone:task56", "Animal Friend", "Tame an Anteater.", ToDoList.TAMED, Anteater.class));
		ToDoList.registerToDoItem(new Murderer("DangerZone:task57", "Madman Murderer", "Kill one of your pets.", ToDoList.KILLED));
		ToDoList.registerToDoItem(new Psycho("DangerZone:task58", "Vicious Psychopath", "Kill someone else's pet.", ToDoList.KILLED));
		
		ToDoList.registerToDoItem(new Rider("DangerZone:task59", "Animal Control", "Ride an Ostrich!", ToDoList.RIDDEN, Ostrich.class));
		ToDoList.registerToDoItem(new Rider("DangerZone:task59a", "High-Speed Rail", "Ride a MagLev!", ToDoList.RIDDEN, EntityMagLev.class));
		
		ToDoList.registerToDoItem(new FoundOnGround("DangerZone:task60", "Twisted Dentist", "Find some Vampire Teeth!", ToDoList.PICKEDUP, Items.vampireteeth));
		ToDoList.registerToDoItem(new FoundOnGround("DangerZone:task61", "Greedy Bankster", "Find a Gold Coin.", ToDoList.PICKEDUP, Items.coingold));
		ToDoList.registerToDoItem(new FoundOnGround("DangerZone:task62", "Closet Water Princess", "Find a Tiara!", ToDoList.PICKEDUP, Items.tiara));
		ToDoList.registerToDoItem(new FoundOnGround("DangerZone:task63", "Nuisance", "Find a furball!", ToDoList.PICKEDUP, Items.furball));
		ToDoList.registerToDoItem(new FoundOnGround("DangerZone:task64", "Seriously Annoying", "Find a Squeek Toy!", ToDoList.PICKEDUP, Items.squeaktoy));
		ToDoList.registerToDoItem(new FoundOnGround("DangerZone:task65", "Miner", "Find some Copper Ore.", ToDoList.PICKEDUP, Blocks.orecopper));
		ToDoList.registerToDoItem(new FoundOnGround("DangerZone:task66", "Successful Miner", "Find some Platinum Ore.", ToDoList.PICKEDUP, Blocks.oreplatinum));
		ToDoList.registerToDoItem(new FoundOnGround("DangerZone:task67", "Wealthy Miner", "Find an Emerald!", ToDoList.PICKEDUP, Items.emerald));
		ToDoList.registerToDoItem(new FoundOnGround("DangerZone:task68", "Starving Artist", "Find a Coloring Block.", ToDoList.PICKEDUP, Blocks.coloringblock));
		ToDoList.registerToDoItem(new FoundOnGround("DangerZone:task69", "Oceanographer", "Find some seaweed.", ToDoList.PICKEDUP, Blocks.seaweed));
		ToDoList.registerToDoItem(new FoundOnGround("DangerZone:task69a", "Snorkeler", "Find a Scuba Mask.", ToDoList.PICKEDUP, Items.scubamask));
		ToDoList.registerToDoItem(new FoundOnGround("DangerZone:task69b", "Scuba Diver", "Find some Scuba Tanks.", ToDoList.PICKEDUP, Items.scubatanks));
		
		ToDoList.registerToDoItem(new DimensionChange("DangerZone:task70", "Time Lord Wannabe", "Change dimension 10 times.", ToDoList.DIMENSION));
		
		ToDoList.registerToDoItem(new BlockPlaced("DangerZone:task74", "Landscaper", "Plant a bunch of grass blocks.", ToDoList.PLACED, Blocks.grassblock, 20));
		ToDoList.registerToDoItem(new BlockPlaced("DangerZone:task75", "Stone Mason", "Place a lot of stone blocks.", ToDoList.PLACED, Blocks.stone, 50));
		ToDoList.registerToDoItem(new BlockPlaced("DangerZone:task76", "Earth Mover", "Place oodles of dirt blocks.", ToDoList.PLACED, Blocks.dirt, 100));
		ToDoList.registerToDoItem(new BlockPlaced("DangerZone:task77", "Home Builder", "Place tons of plywood blocks.", ToDoList.PLACED, Blocks.plywood, 100));
		ToDoList.registerToDoItem(new BlockPlaced("DangerZone:task77a", "Not a Source of Lumber", "Plant a Loop Tree Sapling.", ToDoList.PLACED, Blocks.sapling_loop, 1));
		ToDoList.registerToDoItem(new BlockPlaced("DangerZone:task77b", "Twisted Forest", "Plant a Bulb Tree Sapling.", ToDoList.PLACED, Blocks.sapling_bulb, 1));

		
		ToDoList.registerToDoItem(new SpellCast("DangerZone:task78", "Wizard", "Cast a Spell!", ToDoList.SPELLCAST));
		
		ToDoList.registerToDoItem(new LeveledUp("DangerZone:task79", "Not a Noob", "Over 5000 xp.", ToDoList.LEVELED, 5000));
		ToDoList.registerToDoItem(new LeveledUp("DangerZone:task80", "On a Roll", "Over 10000 xp.", ToDoList.LEVELED, 10000));
		ToDoList.registerToDoItem(new LeveledUp("DangerZone:task81", "Expert", "Over 15000 xp.", ToDoList.LEVELED, 15000));
		ToDoList.registerToDoItem(new LeveledUp("DangerZone:task82", "Game Master", "Over 20000 xp.", ToDoList.LEVELED, 20000));

		ToDoList.registerToDoItem(new Eaten("DangerZone:task83", "Doctor Avoidance", "Eat an Apple.", ToDoList.EATEN, Items.apple));
		ToDoList.registerToDoItem(new Eaten("DangerZone:task84", "OMG Delicious", "Eat a Peach.", ToDoList.EATEN, Items.peach));
		ToDoList.registerToDoItem(new Eaten("DangerZone:task85", "Celiac Sufferer", "Eat some gluten-free Bread.", ToDoList.EATEN, Items.bread));
		ToDoList.registerToDoItem(new Eaten("DangerZone:task86", "Desperately Hungry", "Eat a Dead Bug!", ToDoList.EATEN, Items.deadbug));
		ToDoList.registerToDoItem(new Eaten("DangerZone:task87", "Hungry Again Soon", "Eat some Rice.", ToDoList.EATEN, Items.rice));
		ToDoList.registerToDoItem(new Eaten("DangerZone:task88", "Happily Satiated", "Eat some cooked Moose!", ToDoList.EATEN, Items.moosemeat_cooked));
		
		ToDoList.registerToDoItem(new ArmoredUp("DangerZone:task89", "Fully Kitted Out", "Put on helmet, boots, leggings and chestplate!", ToDoList.ARMORPLACED));
		
		ToDoList.registerToDoItem(new BlockPlaced("DangerZone:task90", "Avid Miner", "Break stone blocks.", ToDoList.BROKEN, Blocks.stone, 100));
		ToDoList.registerToDoItem(new BlockPlaced("DangerZone:task91", "Stone Lover", "Break stone blocks.", ToDoList.BROKEN, Blocks.stone, 200));
		ToDoList.registerToDoItem(new BlockPlaced("DangerZone:task92", "Stone Hater", "Break stone blocks.", ToDoList.BROKEN, Blocks.stone, 500));
		
		ToDoList.registerToDoItem(new BlockPlaced("DangerZone:task93", "Hot Mama", "Break a firestone.", ToDoList.BROKEN, Blocks.firestone, 1));
		
		ToDoList.registerToDoItem(new PetFed("DangerZone:task94", "Loving Master", "Feed your pet.", ToDoList.PETFED));
		
		ToDoList.registerToDoItem(new PetFed("DangerZone:task95", "Get Wrecked", "Hit yourself with a thrown frog.", ToDoList.AFFECTED));
		
		ToDoList.registerToDoItem(new MobSpawner("DangerZone:task96", "Tower Defense Survivor", null, ToDoList.SPAWNED, Flag.class, "Flag", 1));
		
		ToDoList.registerToDoItem(new Eaten("DangerZone:task97", "Eat Healthy", "Try the fish!", ToDoList.EATEN, Items.fishmeat_cooked));
		ToDoList.registerToDoItem(new Eaten("DangerZone:task98", "Adventurous Palate", "Eat an Ostrich!", ToDoList.EATEN, Items.ostrichmeat_cooked));
		ToDoList.registerToDoItem(new Eaten("DangerZone:task99", "Love the Grease", "Eat some cooked Goose!", ToDoList.EATEN, Items.goosemeat_cooked));
		
		ToDoList.registerToDoItem(new ItemRightClickedBlock("DangerZone:task71", "Corn Husker", "Plant some corn!", ToDoList.ITEMRIGHTCLICKEDBLOCK, Items.corn, 20));
		ToDoList.registerToDoItem(new ItemRightClickedBlock("DangerZone:task72", "Salad Lover", "Plant a couple radish plants.", ToDoList.ITEMRIGHTCLICKEDBLOCK, Items.radish, 5));
		ToDoList.registerToDoItem(new ItemRightClickedBlock("DangerZone:task73", "Subsistence Farmer", "Plant a few rice plants.", ToDoList.ITEMRIGHTCLICKEDBLOCK, Items.rice, 20));
		ToDoList.registerToDoItem(new ItemRightClickedBlock("DangerZone:task100", "Peach Lover", "Plant a Peach Tree.", ToDoList.ITEMRIGHTCLICKEDBLOCK, Items.peachseed, 1));
		ToDoList.registerToDoItem(new ItemRightClickedBlock("DangerZone:task101", "Apple Admirer", "Plant an Apple Tree.", ToDoList.ITEMRIGHTCLICKEDBLOCK, Items.appleseed, 1));
		ToDoList.registerToDoItem(new ItemRightClickedBlock("DangerZone:task102", "Cherry Lover", "Plant a Cherry Tree.", ToDoList.ITEMRIGHTCLICKEDBLOCK, Items.cherryseed, 1));
	}
	
	
	/*
	 * This is the second stage of mod initialization.
	 * Everything from all active mods has been registered.
	 * ItemIDs, blockIDs and dimensionIDs are now stable. Maybe. Not necessarily for real server connections...
	 * Go ahead and finish whatever initialization, if any, you need to do here.
	 * 
	 * From world to world, your ItemIDs and blockIDs may change.
	 * But for any one world, your Item and Block IDs are fixed in stone.
	 * 
	 * 
	 */
	public void postLoadProcessing(){
		//String world_directory_where_i_should_save_files = DangerZone.getWorldDirectoryPath();
		Blocks.water.static_partner = Blocks.waterstatic; //because we can't cross-reference things before they are defined!
		Blocks.waterstatic.active_partner = Blocks.water; //because we can't cross-reference things before they are defined!
		Blocks.milk.static_partner = Blocks.milkstatic; //because we can't cross-reference things before they are defined!
		Blocks.milkstatic.active_partner = Blocks.milk; //because we can't cross-reference things before they are defined!
		Blocks.lava.static_partner = Blocks.lavastatic; //because we can't cross-reference things before they are defined!
		Blocks.lavastatic.active_partner = Blocks.lava; //because we can't cross-reference things before they are defined!
		
		Items.maglev.menu = InventoryMenus.GENERIC; //inherits from egg, but really isn't!
		Items.raft.menu = InventoryMenus.GENERIC; //inherits from egg, but really isn't!
	}
			

}
