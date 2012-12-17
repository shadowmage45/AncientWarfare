package shadowmage.ancient_warfare.common.aw_core.config;

import java.io.File;
import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;

public class Config 
{
//*******************************************************FIELDS**********************************************//

public static final String CORE_VERSION_MAJOR = "0.1.0";
public static final String CORE_VERSION_MINOR = "0";
public static final String CORE_VERSION_BUILD = "001";
public static final String CORE_BUILD_STATUS = "debug";
public static final String MC_VERSION = "1.4.5";

/**
 * should debug features be enabled? (debug keybinds, debug overlay rendering)
 */
public static final boolean DEBUG = false;

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
    logger.fine(info);
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
  Config.instance().setCoreInfo();
  Config.instance().setVehicleInfo();
  Config.instance().setKingdomInfo();
  Config.instance().setWorldGenInfo();
  config.save();
  }

public static void saveConfig()
  {
  if(config!=null)
    {
    config.save();
    }
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

public void setCoreInfo()
  {
  
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

}
