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

public class Config
{
//*******************************************************FIELDS**********************************************//

public static final String CORE_VERSION_MAJOR = "0.1.0";
public static final String CORE_VERSION_MINOR = "0";
public static final String CORE_VERSION_BUILD = "003";
public static final String CORE_BUILD_STATUS = "dev";
public static final String MC_VERSION = "1.4.7";

/**
 * should debug features be enabled? (debug keybinds, debug overlay rendering, load and enable debug items)
 */
public static final boolean DEBUG = true;


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

public static int trajectoryIterationsServer = 20;
public static int npcAITicks = 5;
public static int npcAISearchRange = 140;
public static int npcPathfinderType = 1;//0-inline, 1-scheduled, 2-threaded

/**
 * the base (Server side) and current (client side) values...
 */
public static boolean clientVehicleMovementBase = true;
public static int clientMoveUpdateTicksBase = 3;
public static boolean clientVehicleMovement = true;
public static int clientMoveUpdateTicks = 3;

private static Configuration config;
private static Logger logger;


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
  config.addCustomCategoryComment("structure-management", "Global World Generation options, effect every save/world");
  
  /**
   * general options
   */
  this.templateExtension = config.get("a-general-options", "template_extension", "aws", "The extension used by templates, must be a three-digit extension valid on your file system").getString();
  this.clientVehicleMovementBase = config.get("a-general-options", "client_movement", true, "If true, movement is calculated and authenticated client-side (smoother motion for clients)").getBoolean(true);
  this.adjustMissilesForAccuracy = config.get("a-general-options", "missile_accuracy", true, "If true, missiles will be adjusted for vehicle and rider accuracy when launched.").getBoolean(true);
  
  /**
   * performance options
   */
  this.npcAITicks = config.get("b-performance", "npc_aiticks", 5, "How many ticks should pass between updating passive ai tasks for NPCs?").getInt(5);
  this.trajectoryIterationsServer = config.get("b-performance", "vehicle_trajectory", 5, "How many iterations should the brute-force trajectory algorith run? (used for soldiers server side)").getInt(20);
  this.clientMoveUpdateTicksBase = config.get("b-performance", "client_movement_ticks", 3, "How many ticks between client movement update packets if client movement is enabled? (setting is sent and synched to clients on login)").getInt(3);
  }

public void setVehicleInfo()
  {
  
  }

public void setKingdomInfo()
  {
  
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
    }
  }

public NBTTagCompound getClientInitData()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setBoolean("cm", this.clientVehicleMovementBase);
  tag.setInteger("cmt", this.clientMoveUpdateTicksBase);
  return tag;
  }

}
