package shadowmage.ancient_warfare.common.aw_core.config;

import java.io.File;
import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;

public class ConfigManager 
{

private ConfigManager(){};

private static ConfigManager INSTANCE;
public static ConfigManager instance()
 {
 if(INSTANCE==null)
   {
   INSTANCE = new ConfigManager();
   }
 return INSTANCE;
 }

private static Configuration config;
private static Logger logger;

public static void setLogger(Logger log)
  {
  logger = log;
  }

public static void loadConfig(File inputFile)
  {
  config = new Configuration(inputFile);
  ConfigManager.instance().setCoreInfo();
  ConfigManager.instance().setVehicleInfo();
  ConfigManager.instance().setKingdomInfo();
  ConfigManager.instance().setWorldGenInfo();
  config.save();
  }

public static void saveConfig()
  {
  if(config!=null)
    {
    config.save();
    }
  }

public int getItemID(String name, int defaultID)
  {
  return config.getItem(name, defaultID).getInt(defaultID);
  }

public int getItemID(String name, int defaultID, String comment)
  {
  return config.getItem(name, defaultID, comment).getInt(defaultID);
  }

public int getBlockID(String name, int defaultID)
  {
  return config.getBlock(name, defaultID).getInt(defaultID);
  }

public int getBlockID(String name, int defaultID, String comment)
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
