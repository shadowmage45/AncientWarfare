/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
   Please see COPYING for precise license information.

   This file is part of Ancient Warfare.

   Ancient Warfare is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   Ancient Warfare is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Ancient Warfare.  If not, see <http://www.gnu.org/licenses/>.
 */
package shadowmage.ancient_structures.common.config;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import shadowmage.ancient_structures.common.manager.BlockDataManager;

public class AWStructureStatics extends ModConfiguration
{

public static String templateExtension = "aws";
public static boolean enableVillageGen = true;
public static boolean enableStructureGeneration = true;
public static int chunkSearchRadius = 16;
public static int maxClusterValue = 500;
public static int randomChance = 75;
public static int randomRange = 1000;
public static int spawnProtectionRange = 0;
public static Set<String> excludedSpawnerEntities = new HashSet<String>();
public static HashSet<String> skippableWorldGenBlocks = new HashSet<String>();

private static String worldGenCategory = "a_world-gen_settings";
private static String villageGenCategory = "b_village-gen_settings";
private static String excludedEntitiesCategory = "c_excluded_spawner_entities";
private static String worldGenBlocks = "d_world_gen_skippable_blocks";

/**
 * @param configFile
 * @param log
 * @param version
 */
public AWStructureStatics(File configFile, Logger log, String version)
  {
  super(configFile, log, version);
  }

@Override
public void initializeCategories()
  {
  this.config.addCustomCategoryComment(worldGenCategory, "Settings that effect all world-structure-generation.");
  }

@Override
public void initializeValues()
  {
  templateExtension = config.get(worldGenCategory, "template_extension", "aws").getString();
  enableVillageGen = config.get(worldGenCategory, "enable_village_generation", enableVillageGen).getBoolean(enableVillageGen);
  enableStructureGeneration = config.get(worldGenCategory, "enable_structure_generation", enableStructureGeneration).getBoolean(enableStructureGeneration);
  chunkSearchRadius = config.get(worldGenCategory, "validation_chunk_radius", chunkSearchRadius).getInt(chunkSearchRadius);
  maxClusterValue = config.get(worldGenCategory, "max_cluster_value", maxClusterValue).getInt(maxClusterValue);
  randomChance = config.get(worldGenCategory, "random_chance", randomChance).getInt(randomChance);
  randomRange = config.get(worldGenCategory, "random_range", randomRange).getInt(randomRange);
  spawnProtectionRange = config.get(worldGenCategory, "spawn_protection_chunk_radius", spawnProtectionRange).getInt(spawnProtectionRange);
  
  String[] defaultExcludedEntities = new String[]
        {
            "EnderCrystal",
            "EnderDragon",
            "EyeOfEnderSignal",
            "FallingSand",
            "Fireball",
            "FireworksRocketEntity",
            "Item",
            "ItemFrame",
            "LeashKnot",
            "Mob",
            "Monster",
            "Painting",
            "PrimedTnt",
            "SmallFireball",
            "Snowball",
            "ThrownEnderpearl",
            "ThrownExpBottle",
            "ThrownPotion",
            "WitherBoss",
            "WitherSkull",
            "XPOrb",
            "AncientWarfare.entity.npc",
            "AncientWarfare.entity.missile",
            "AncientWarfare.entity.vehicle",
            "AncientWarfare.entity.gate",
            "MinecraftFurnace",
            "MinecartSpawner",
            "MinecartHopper",
            "MinecartFurnace",
            "MinecartRideable",
            "MinecartChest",
            "MinecartTNT",
            "Boat",
            "LOTR.Marsh Wraith Ball",
            "LOTR.Orc Bomb",
            "LOTR.Spear",
            "LOTR.Barrel",
            "LOTR.Portal",
            "LOTR.Wargskin Rug",
            "LOTR.Crossbow Bolt",
            "LOTR.Throwing Axe",
            "LOTR.Thrown Rock",
            "LOTR.Plate",
            "LOTR.Mystery Web",
            "LOTR.Gandalf Fireball",
            "LOTR.Pebble",
            "LOTR.Smoke Ring",
            "LOTR.Gollum",
            "LOTR.Trader Respawn",
            "Metallurgy3Base.LargeTNTEntity",
            "Metallurgy3Base.MinersTNTEntity",
            "awger_Whitehall.EntityWhitehall",
            "awger_SmallBoat.EntityBoatChest",
            "awger_SmallBoat.EntityBoatPart",
            "awger_SmallBoat.EntitySmallBoat",
            "awger_Hoy.EntityHoy",
            "awger_Punt.EntityPunt",
            "BiomesOPlenty.PoisonDart",
            "BiomesOPlenty.MudBall",
            "BiomesOPlenty.Dart",
            "KoadPirates.Tether",
            "KoadPirates.Net",
            "KoadPirates.Shot",
            "KoadPirates.Cannon Ball",
            "weaponmod.flail",
            "weaponmod.boomerang",
            "weaponmod.dart",
            "weaponmod.bolt",
            "weaponmod.dynamite",
            "weaponmod.javelin",
            "weaponmod.knife",
            "weaponmod.spear",
            "weaponmod.dummy",
            "Arrow",
            "Cannon",
            "SonicBoom",
            "witchery.spellEffect",
            "witchery.corpse",
            "witchery.brew",
            "witchery.broom",
            "witchery.mandrake",
            "witchery.familiar",
            "witchery.owl",
            "witchery.eye",
            "JungleMobs.ConcapedeSegment",
            "DesertMobs.Mudshot",
            "DesertMobs.ThrowingScythe",
            "DemonMobs.Doomfireball",
            "DemonMobs.DemonicBlast",
            "DemonMobs.DemonicSpark",
            "DemonMobs.Hellfireball",
            "SwampMobs.VenomShot",
            "SwampMobs.PoisonRayEnd",
            "SwampMobs.PoisonRay",
            "RopesPlus.Frost Arrow303",
            "RopesPlus.Rope Arrow303",
            "RopesPlus.Penetrating Arrow303",
            "RopesPlus.Slime Arrow303",
            "RopesPlus.Arrow303",
            "RopesPlus.Redstonetorch Arrow303",
            "RopesPlus.Fire Arrow303",
            "RopesPlus.Exploding Arrow303",
            "RopesPlus.GrapplingHook",
            "RopesPlus.Confusing Arrow303",
            "RopesPlus.Warp Arrow303",
            "RopesPlus.Torch Arrow303",
            "RopesPlus.Seed Arrow303",
            "RopesPlus.Dirt Arrow303",
            "RopesPlus.FreeFormRope",
            "Goblins_mod.GArcaneball",
            "Goblins_mod.MTNTPrimed",
            "Goblins_mod.Bomb",
            "Goblins_mod.orbR",
            "Goblins_mod.Lightball",
            "Goblins_mod.ETNTPrimed",
            "Goblins_mod.GArcanebal",
            "Goblins_mod.Arcaneball",
            "Goblins_mod.orbY",
            "Goblins_mod.orbB",
            "Goblins_mod.orbG",
            "MoCreatures.Egg",
            "MoCreatures.MoCPlatform",
            "MoCreatures.LitterBox",
            "MoCreatures.FishBowl",
            "MoCreatures.KittyBed",
            "MoCreatures.PetScorpion",
            "BiblioCraft.SeatEntity",
            "minecolonies.pointer",
            "minecolonies.arrow",
            "minecolonies.citizen",
            "minecolonies.stonemason",
            "minecolonies.huntersdog",
            "minecolonies.stonemason",
            "minecolonies.farmer",
            "minecolonies.blacksmith",
            "minecolonies.builder",
            "minecolonies.miner",
            "minecolonies.baker",
            "minecolonies.deliveryman",
            "minecolonies.soldier",
            "extrabiomes.scarecrow",
            "Paleocraft.Bladeking68",
            "Test",
            "Petrified",
            "BladeTrap",
            "EyeRay",
            "MagicMissile",
            "RakshasaImage"
        };  
  defaultExcludedEntities = config.get(excludedEntitiesCategory, "excluded_entities", defaultExcludedEntities).getStringList();
  
  for(String st : defaultExcludedEntities)
    {
    excludedSpawnerEntities.add(st);
    }
  
  String[] defaultSkippableBlocks = new String[]    
    {
        BlockDataManager.getBlockName(Block.cactus),
        BlockDataManager.getBlockName(Block.vine),
        BlockDataManager.getBlockName(Block.tallGrass),
        BlockDataManager.getBlockName(Block.wood),
        BlockDataManager.getBlockName(Block.plantRed),
        BlockDataManager.getBlockName(Block.plantYellow),
        BlockDataManager.getBlockName(Block.deadBush),
        BlockDataManager.getBlockName(Block.leaves),
        BlockDataManager.getBlockName(Block.snow)
    };
  defaultSkippableBlocks = config.get(worldGenBlocks, "skippable_blocks", defaultSkippableBlocks).getStringList();
  for(String st : defaultSkippableBlocks)
    {
    skippableWorldGenBlocks.add(st);
    } 
  
  this.config.save();
  }

}
