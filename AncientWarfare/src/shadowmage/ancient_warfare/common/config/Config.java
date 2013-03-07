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

public static int structureGenMinDistance = 2;
public static int structureGenMaxCheckRange = 16;
public static int structureGeneratorRandomChance = 10;
public static int structureGeneratorRandomRange = 1000;
public static int structureGenMaxClusterValue = 50;
public static boolean invertShiftClickOnItems = false;
public static String templateExtension = "aws";

public static int trajectoryIterationsServer = 20;

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
  config.addCustomCategoryComment("structure-management", "Global World Generation options, effect every save/world");
  this.templateExtension = config.get("a-general-options", "template_extension", "aws", "The extension used by templates, must be a three-digit extension valid on your file system").value;
  }

public void setVehicleInfo()
  {
  
  }

public void setKingdomInfo()
  {
  
  }

public void setWorldGenInfo()
  {
  this.structureGenMinDistance = config.get("structure-management", "world_gen_min_dist", 1).getInt(1);
  this.structureGenMaxCheckRange = config.get("structure-management", "world_gen_max_search", 16).getInt(1);
  this.structureGeneratorRandomChance = config.get("structure-management", "world_gen_random_chance", 10).getInt(10);
  this.structureGeneratorRandomRange = config.get("structure-management", "world_gen_random_range", 1000).getInt(1000);
  this.structureGenMaxClusterValue = config.get("structure-management", "world_gen_max_cluster_value", 50).getInt(50);
  }

private void setStructureInfo()
  {
  boolean exportDefaults = config.get("structure-management", "exportdefaults", true, "Re-export default included structures, in case they have been changed in any way, or need files regenerated").getBoolean(true);
  if(exportDefaults)
    {
    config.get("structure-management", "exportdefaults", false).value = "false";
    AWStructureModule.instance().setExportDefaults();
    }
  
  }

}
