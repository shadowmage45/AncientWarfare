/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.config;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.Configuration;
import shadowmage.ancient_warfare.common.AWStructureModule;
import shadowmage.ancient_warfare.common.npcs.INpcType;
import shadowmage.ancient_warfare.common.npcs.NpcTypeBase;

public class Config
{
//*******************************************************FIELDS**********************************************//
public static final String VERSION = "1.1.039-beta-MC164";//major version(mc version updates/major revisions), minor version(releases), build version(test releases total)
public static final String ANTVERSION = "@VERSION@";
public static String texturePath = "textures/";
public static String configPath = "";

/**
 * should debug features be enabled? (debug keybinds, debug overlay rendering, load and enable debug items)
 */
public static final boolean DEBUG = true;

//***************************************************LOADED CONFIGS******************************************//

public static boolean globalWarfare = true;
public static boolean warzonesArePeaceZones = false;
public static boolean enablePerformanceMonitor = false;
public static boolean invertShiftClickOnItems = false;
public static String templateExtension = "aws";
public static boolean adjustMissilesForAccuracy = false;
public static boolean blockDestruction = true;
public static boolean blockFires = true;
public static boolean addOversizeAmmo = true;
public static boolean useVehicleSetupTime = true;
public static boolean soldiersUseAmmo = false;
public static boolean useNpcWorkForCrafting = true;
public static boolean enableVillageGen = true;
public static boolean vehiclesTearUpGrass = true;
public static boolean autoExportOnUpdate = true;
public static boolean autoExportWorldGenOnUpdate = true;
public static boolean gatesOnlyDamageByRams = false;
public static boolean randomizeVillagers = false;
public static boolean renderHostileNames = false;
public static boolean renderNpcFlags = true;
public static boolean allowFriendlyFire = false;
public static boolean enableNpcTeleportHome = true;

public static boolean updatedVersion = false;
public static String configVersion = "";

public static int trajectoryIterationsServer = 20;
public static int civicBroadcastRange = 80;
public static int npcUpkeepTicks = 20*60*5;//five minute base timer..  -1 to disable upkeep
public static int npcHealingTicks = 20*10;//ten second base timer  -1 to disable healing
public static int npcAITicks = 5;
public static int npcAISearchRange = 80;
public static int mailSendTicks = 5*20;//five seconds between sending mail
public static int npcWorkMJ = 80;//how many BuildCraft MJ represent one NPC 'work' unit
public static int vehicleMoveUpdateFrequency = 3;
public static int mailDimensionalTime = 2500;


//***************************************************SYNCHED CONFIGS************************************************//
public static boolean disableResearch = false;



//***************************************************SINGLETON************************************************//

private Config(){};
private static Config INSTANCE;
public static Config instance()
 {
 if(INSTANCE==null)
   {
   INSTANCE = new Config();
   }
 return INSTANCE;
 }

private static Configuration config;
private static Logger logger;

//**************************************************STATIC VERSION LOADING*****************************************************//

private static final String loadVersion()
  {
  String version = "ERROR";
  InputStream is = Config.class.getResourceAsStream("shadowmage/ancient_warfare/resources/version.properties");
  Properties p = new Properties();
  try
    {
    p.load(is);
    version = p.getProperty("version");
    if(version ==null)
      {
      version = "ERROR";
      }
    is.close();
    }
  catch(Exception e)
    {  
    e.printStackTrace();
    version = "ERROR";
    }  
  return version;
  }

//**************************************************LOGGER*****************************************************//

public static void log(String info)
  {
  if(logger!=null)
    {
    logger.info(info);
    }  
  }

public static void logDebug(String info)
  {
  if(logger!=null && DEBUG)
    {    
    logger.info(String.valueOf("[DEBUG] "+info));        
    }
  }

public static void logError(String info)
  {
  if(logger!=null)
    {
    logger.severe(info);
    }
  }

public static void setLogger(Logger log)
  {
  logger = log;
  }

//**************************************************CONFIG*****************************************************//

public static void loadConfig(File inputFile)
  {  
  config = new Configuration(inputFile);
  instance().setCoreInfo();
  instance().setVehicleInfo();
  instance().setKingdomInfo();
  instance().setWorldGenInfo(); 
  instance().setStructureInfo();
  Settings.instance().loadSettings();
  config.save();
  }

public static void saveConfig()
  {
  if(config!=null)
    {
    config.save();
    }
  }

public static Configuration getConfig()
  {
  return config;
  }

public static int getItemID(String name, int defaultID)
  {
  return config.getItem(name, defaultID).getInt(defaultID);
  }

public static int getItemID(String name, int defaultID, String comment)
  {
  return config.getItem(name, defaultID, comment).getInt(defaultID);
  }

public static int getBlockID(String name, int defaultID)
  {
  return config.getBlock(name, defaultID).getInt(defaultID);
  }

public static int getBlockID(String name, int defaultID, String comment)
  {
  return config.getBlock(name, defaultID, comment).getInt(defaultID);
  }

public static int getKeyBindID(String name, int defaultID, String comment)
  {
  return config.get("keybinds", name, defaultID, comment).getInt(defaultID);
  }

public void setCoreInfo()
  {
  config.addCustomCategoryComment("a-general-options", "Global options that effect the entire mod");
  config.addCustomCategoryComment("b-performance", "Global options that may effect performance in some way");
  config.addCustomCategoryComment("c-vehicle-options", "Global options that effect vehicles in some fashion");
  config.addCustomCategoryComment("structure-management", "Global World Generation options, effect every save/world.  Check AWWorldGen.cfg for advanced options");
  config.addCustomCategoryComment("d-npc_target_settings", "Forced NPC Targets.  Place target names between the < > braces, each value on its own line. \nInvalid values or improperly spelled names will be silently ignored.\nNeeds the full name as registered in game (ask the mod author!)");
  config.addCustomCategoryComment("e_vehicle_config", "Enable/disable vehicle recipes and dungeon loot entries for specific vehicle types");
  config.addCustomCategoryComment("f_ammo_config", "Enable/disable ammo recipes and alter damage amounts");
  config.addCustomCategoryComment("g_npc_config", "Enable/disable npc recipes and alter damage/health/healing amounts");
  config.addCustomCategoryComment("h_additional_toggles", "Enable/disable various features/recipes that don't fit into other categories");
  String configVersion = config.get("version", "version", "NULL", "The mod version used to last save this config").getString();
  if(!configVersion.equals(VERSION))
    {
    this.updatedVersion = true;
    config.get("version", "version", VERSION, "The mod version used to last save this config").set(VERSION);
    }
  /**
   * general options
   */
  this.globalWarfare = config.get("a-general-options", "global_warfare", globalWarfare, "If true, Warzones will be disabled, and warfare is global and unchecked.").getBoolean(globalWarfare);
  this.warzonesArePeaceZones = config.get("a-general-options", "warzones_invert", warzonesArePeaceZones, "If true, warzones will be used as no-war zones instead...and warfare can occur anywhere outside those zones.").getBoolean(warzonesArePeaceZones);
  this.templateExtension = config.get("a-general-options", "template_extension", "aws", "The extension used by templates, must be a three-digit extension valid on your file system").getString();  
  this.adjustMissilesForAccuracy = config.get("a-general-options", "missile_accuracy", true, "If true, missiles will be adjusted for vehicle and rider accuracy when launched.").getBoolean(true);
  this.blockDestruction = config.get("a-general-options", "missile_destroy_blocks", true, "If true, missiles will be capable of destroying blocks.").getBoolean(true);
  this.blockFires = config.get("a-general-options", "missile_start_fires", true, "If true, missiles will be capable of lighting fires and placing lava blocks.").getBoolean(true);
  this.disableResearch = config.get("a-general-options", "disable_research", false, "If true, research system will be disabled and all recipes will be available.").getBoolean(false);
  this.useNpcWorkForCrafting = config.get("a-general-options", "npc_work", true, "If true, npcs (or interacting player) will be required to produce items at crafting stations.  Set to false to auto-produce.").getBoolean(true);
  this.enablePerformanceMonitor = config.get("a-general-options", "performance_monitor", true, "If true, enables a server-side performance monitor viewable by server OPs from the AW config menu (F7)").getBoolean(true);
  this.npcWorkMJ = config.get("a-general-options", "npc_work_mj", 70, "How many BuildCraft MJ represent one NPC 'work' unit.").getInt(70);
  this.autoExportOnUpdate = config.get("a-general-options", "auto_export_structure", true, "If true, will automatically re-export the default structure templates when the mod version is updated").getBoolean(true);
  this.autoExportWorldGenOnUpdate = config.get("a-general-options", "auto_export_worldgen", true, "If true, will automatically re-export the default world gen configuration when the mod version is updated").getBoolean(true);
  this.randomizeVillagers = config.get("a-general-options", "randomize_villagers", false, "If true, villager spawners will spawn a random villager type instead of set type").getBoolean(false);
  this.renderHostileNames = config.get("a-general-options", "render_hostile_nameplates", false, "If true, will render nameplates/health for hostile NPCs and vehicles -- client side config (for now)").getBoolean(false);
  this.renderNpcFlags = config.get("a-general-options", "render_npc_flags", true, "Enable/Disable rendering of NPC Team flags.").getBoolean(true);
  this.allowFriendlyFire = config.get("a-general-options", "allow_friendly_fire", false, "If true, soldiers can/will injure other friendly soldiers with arrows/ammunitions").getBoolean(false);
  this.mailDimensionalTime = config.get("a-general-options", "mailbox_dimensional_delay", mailDimensionalTime, "Delay introduced for cross-dimensional mail in ticks. Any mail sent across dimensions will take this number of ticks to arrive").getInt(mailDimensionalTime);
  /**
   * performance options
   */
  this.npcAITicks = config.get("b-performance", "npc_aiticks", 5, "How many ticks should pass between updating passive ai tasks for NPCs?").getInt(5);
  this.npcAISearchRange = config.get("b-performance", "npc_search_radius", 140, "How many blocks of radius should entities search for targets and work? (MAX range, some AI limits this further)").getInt(140);
  this.trajectoryIterationsServer = config.get("b-performance", "vehicle_trajectory_iterations", 20, "How many iterations should the brute-force trajectory algorith run? (used for soldiers server side)").getInt(20);  
  this.enableNpcTeleportHome = config.get("a-general-options", "npc_teleport_home", true, "If enabled, NPCs will teleport to home point (if assigned) if stuck and unable to path for too long.").getBoolean(true);
  
  config.get("g_npc_config", "enable_armor_swapping", true);
  }

public void setVehicleInfo()
  {
  this.addOversizeAmmo = config.get("c-vehicle-options", "add_oversize_ammo", true, "If true, ALL ammo types for a vehicle will be available regardless of weight/being useless.").getBoolean(true);
  this.soldiersUseAmmo = config.get("c-vehicle-options", "soldiers_use_ammo", false, "If true, Soldiers will consume ammo from vehicles but the type must be selected before hand, and kept stocked. If false, soldiers are limited to a default ammo type for the vehicle.").getBoolean(false);
  this.useVehicleSetupTime = config.get("c-vehicle-options", "use_setup_time", true, "If true, vehicles will be un-useable for the first 5 seconds after placement.").getBoolean(true);
  this.vehiclesTearUpGrass = config.get("c-vehicle-options", "vehicle_destroy_grass", true, "If true, vehicles will tear up grass/snow/flower blocks when driving over them.  Can be disabled for minor performance boost (less items in world)").getBoolean(true);
  this.gatesOnlyDamageByRams = config.get("c-vehicle-options", "gates_only_damaged_by_rams", false, "If true, gates will only take damage from battering rams (no other sources).  Default is false--to allow all damage sources").getBoolean(false);
  }

public void setKingdomInfo()
  {
  INpcType[] types = NpcTypeBase.getNpcTypes();
  String[] defaults;
  for(INpcType t : types)
    {    
    if(t==null){continue;}    
    String name = t.getConfigName();
    if(t.isCombatUnit() && !name.equals(""))
      {
      defaults = t.getDefaultTargets();
      if(defaults!=null && defaults.length>0)
        {
        config.get("d-npc_target_settings", name, defaults, "Forced targets for npc type: "+t.getConfigName());
        }
      }
    }
  config.get("d-npc_target_settings", "civilian", NpcTypeBase.defaultTargetList, "Forced targets that will aggro civilians and which they will run away from");
  }

public void setWorldGenInfo()
  {

  }

private void setStructureInfo()
  {
  boolean exportDefaults = config.get("structure-management", "exportdefaults", true, "Re-export default included structures, in case they have been changed in any way, or need files regenerated").getBoolean(true);
  if(exportDefaults)
    {
    config.get("structure-management", "exportdefaults", false).set(false);
    AWStructureModule.instance().setExportDefaults();
    }  
  this.enableVillageGen = config.get("structure-management", "enableVillageStructures", true, "If true, will generate additional Ancient Warfare structures in villages").getBoolean(true);
  }

public void handleClientInit(NBTTagCompound tag)
  {
  if(tag.hasKey("cm"))
    {
    this.disableResearch = tag.getBoolean("disableResearch");
    }
  }

public NBTTagCompound getClientInitData()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setBoolean("disableResearch", this.disableResearch);
  return tag;
  }

}
