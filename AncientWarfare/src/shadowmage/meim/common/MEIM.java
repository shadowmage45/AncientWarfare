package shadowmage.meim.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import shadowmage.ancient_framework.client.model.PrimitiveTriangle;
import shadowmage.ancient_framework.client.model.PrimitiveTriangle.Point2i;
import shadowmage.meim.common.config.MEIMConfig;
import shadowmage.meim.common.item.ItemLoader;
import shadowmage.meim.common.network.NetworkManager;
import shadowmage.meim.common.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


@Mod( modid = "MEIM", name="MEIM", version=MEIMConfig.VERSION, dependencies="required-after:AncientWarfareCore")
@NetworkMod
(
clientSideRequired = true,
serverSideRequired = false,
packetHandler = NetworkManager.class,
channels = {"MEIM"},
versionBounds="["+MEIMConfig.VERSION+",)"
)
public class MEIM 
{

@SidedProxy(clientSide = "shadowmage.meim.client.proxy.ClientProxy", serverSide = "shadowmage.meim.common.proxy.CommonProxy")
public static CommonProxy proxy;
@Instance("MEIM")
public static MEIM instance;		

@EventHandler
public void preInit(FMLPreInitializationEvent evt) 
  {
  MEIMConfig.instance();
  MEIMConfig.loadConfig(evt.getSuggestedConfigurationFile());
  MEIMConfig.setLogger(evt.getModLog());
  ItemLoader.load();
  MEIMConfig.saveConfig();
  }

}
