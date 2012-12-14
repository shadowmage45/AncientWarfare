package shadowmage.ancient_warfare.common.aw_core;


import shadowmage.ancient_warfare.common.aw_core.config.ConfigManager;
import shadowmage.ancient_warfare.common.aw_core.network.PacketHandler;
import shadowmage.ancient_warfare.common.aw_core.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


@Mod( modid = "AncientWarfare", name="Ancient Warfare", version="0.1.0.001-alpha")
@NetworkMod
(
clientSideRequired = true,
serverSideRequired = true,
packetHandler = PacketHandler.class,
channels = {"AW_vehicle", "AW_tile", "AW_gui", "AW_soldier"},
//connectionHandler = ConnectionHandler.class,
versionBounds="[0.1.0,)"
)
public class AncientWarfareCore {
	

@SidedProxy(clientSide = "shadowmage.ancient_warfare.client.aw_core.proxy.ClientProxy", serverSide = "shadowmage.ancient_warfare.common.aw_core.proxy.CommonProxy")
public static CommonProxy proxy;
@Instance("AncientWarfare")
public static AncientWarfareCore instance;	
	
/**
 * load settings, config, items
 * @param evt
 */
@PreInit
public void preInit(FMLPreInitializationEvent evt) 
  {
  /**
   * load config file
   */
  ConfigManager.loadConfig(evt.getSuggestedConfigurationFile());
  }
	
/**
 * initialize modules
 * @param evt
 */
@PostInit
public void load(FMLPostInitializationEvent evt)
  {
  
  
  
  /**
   * and finally, save the config in case there were any changes made during init
   */
  ConfigManager.saveConfig();
  }
	
	

}
