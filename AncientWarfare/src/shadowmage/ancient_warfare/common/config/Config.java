/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public Licence.
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
import java.util.logging.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.Configuration;
import shadowmage.ancient_warfare.common.AWStructureModule;
import shadowmage.ancient_warfare.common.npcs.INpcType;
import shadowmage.ancient_warfare.common.npcs.NpcTypeBase;

public class Config
{
//*******************************************************FIELDS**********************************************//

public static final String CORE_VERSION_MAJOR = "0.1.0";
public static final String CORE_VERSION_BUILD = "007";
public static final String CORE_BUILD_STATUS = "alpha";
public static final String MC_VERSION = "1.5.2";

/**
 * should debug features be enabled? (debug keybinds, debug overlay rendering, load and enable debug items)
 */
public static final boolean DEBUG = false;


//***************************************************LOADED CONFIGS******************************************//

public static String texturePath = "/mods/ancientwarfare/textures/";
public static boolean invertShiftClickOnItems = false;
public static String templateExtension = "aws";
public static boolean adjustMissilesForAccuracy = false;
public static boolean blockDestruction = true;
public static boolean blockFires = true;
public static boolean addOversizeAmmo = true;
public static boolean useVehicleSetupTime = true;
public static boolean soldiersUseAmmo = false;
public static boolean useNpcWorkForCrafting = true;

public static int trajectoryIterationsServer = 20;
public static int civicBroadcastRange = 80;
public static int npcUpkeepTicks = 20*60*5;//five minute base timer..  -1 to disable upkeep
public static int npcHealingTicks = 20*10;//ten second base timer  -1 to disable healing
public static int npcAITicks = 5;
public static int npcAISearchRange = 80;
public static int npcPathfinderType = 1;//0-inline, 1-scheduled, 2-threaded
public static int npcPathfinderThreads = 2;


//***************************************************SYNCHED CONFIGS************************************************//
/**
 * the base (Server side) and current (client side) values...
 */
public static boolean clientVehicleMovementBase = true;
public static int clientMoveUpdateTicksBase = 3;
public static boolean clientVehicleMovement = true;
public static int clientMoveUpdateTicks = 3;

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
  
  /**
   * general options
   */
  this.templateExtension = config.get("a-general-options", "template_extension", "aws", "The extension used by templates, must be a three-digit extension valid on your file system").getString();
  this.clientVehicleMovementBase = config.get("a-general-options", "client_movement", true, "If true, movement is calculated and authenticated client-side (smoother motion for clients)").getBoolean(true);
  this.adjustMissilesForAccuracy = config.get("a-general-options", "missile_accuracy", true, "If true, missiles will be adjusted for vehicle and rider accuracy when launched.").getBoolean(true);
  this.blockDestruction = config.get("a-general-options", "missile_destroy_blocks", true, "If true, missiles will be capable of destroying blocks.").getBoolean(true);
  this.blockFires = config.get("a-general-options", "missile_start_fires", true, "If true, missiles will be capable of lighting fires and placing lava blocks.").getBoolean(true);
  this.disableResearch = config.get("a-general-options", "disable_research", false, "If true, research system will be disabled and all recipes will be available.").getBoolean(false);
  
  /**
   * performance options
   */
  this.npcAITicks = config.get("b-performance", "npc_aiticks", 5, "How many ticks should pass between updating passive ai tasks for NPCs?").getInt(5);
  this.npcAISearchRange = config.get("b-performance", "npc_search_radius", 140, "How many blocks of radius should entities search for targets and work? (MAX range, some AI limits this further)").getInt(140);
  this.trajectoryIterationsServer = config.get("b-performance", "vehicle_trajectory_iterations", 20, "How many iterations should the brute-force trajectory algorith run? (used for soldiers server side)").getInt(20);
  this.clientMoveUpdateTicksBase = config.get("b-performance", "client_movement_ticks", 3, "How many ticks between client movement update packets if client movement is enabled? (setting is sent and synched to clients on login)").getInt(3);
  this.npcPathfinderType = config.get("b-performance", "pathfinder_type", 1, "0--Immediate. 1--Scheduled. 2--Threaded").getInt(1);
  this.npcPathfinderThreads = config.get("b-performance", "pathfinder_threaded_thread_count", 2, "How many threads to use? (spare cores is a good starting number)").getInt(2);
  }

public void setVehicleInfo()
  {
  this.addOversizeAmmo = config.get("c-vehicle-options", "add_oversize_ammo", true, "If true, ALL ammo types for a vehicle will be available regardless of weight/being useless.").getBoolean(true);
  this.soldiersUseAmmo = config.get("c-vehicle-options", "soldiers_use_ammo", false, "If true, Soldiers will consume ammo from vehicles but the type must be selected before hand, and kept stocked. If false, soldiers are limited to a default ammo type for the vehicle.").getBoolean(false);
  this.useVehicleSetupTime = config.get("c-vehicle-options", "use_setup_time", true, "If true, vehicles will be un-useable for the first 5 seconds after placement.").getBoolean(true);
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
        config.get("d-npc_target_settings", name, defaults, "Forced targets for npc type: "+t.getDisplayName());
        }
      }
    }
  defaults = new String[]{"Zombie", "Spider","Creeper", "CaveSpider", "Blaze", 
      "Enderman", "Ghast", "Giant", "LavaSlime", "PigZombie", "Silverfish", "Skeleton", "Slime"};
  config.get("d-npc_target_settings", "civilian", defaults, "Forced targets for civilians to run away from");
  }

public void setWorldGenInfo()
  {

  }

private void setStructureInfo()
  {
  boolean exportDefaults = config.get("structure-management", "exportdefaults", true, "Re-export default included structures, in case they have been changed in any way, or need files regenerated").getBoolean(true);
  if(exportDefaults)
    {
    config.get("structure-management", "exportdefaults", false).set(false);//.value = "false";
    AWStructureModule.instance().setExportDefaults();
    }  
  }

public void handleClientInit(NBTTagCompound tag)
  {
  if(tag.hasKey("cm"))
    {
    this.clientVehicleMovement = tag.getBoolean("cm");
    this.clientMoveUpdateTicks = tag.getInteger("cmt");
    this.disableResearch = tag.getBoolean("disableResearch");
    }
  }

public NBTTagCompound getClientInitData()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setBoolean("cm", this.clientVehicleMovementBase);
  tag.setInteger("cmt", this.clientMoveUpdateTicksBase);
  tag.setBoolean("disableResearch", this.disableResearch);
  return tag;
  }

}
