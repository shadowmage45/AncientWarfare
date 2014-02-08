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
//  plotTest();
  }

private void plotTest()
  {
  BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
  for(int x= 0; x < 256; x++)
    {
    for(int y = 0; y <256; y++)
      {
      image.setRGB(x, y, 0xff222222);
      }
    }
  
  List<Point2i> points = new ArrayList<Point2i>();
  
  PrimitiveTriangle.plotLine3(0, 0, 10, 10, points);
  plotPoints(image, points);
  points.clear();
  
  PrimitiveTriangle.plotLine3(10, 10, 20, 0, points);
  plotPoints(image, points);
  points.clear();
  
  PrimitiveTriangle.plotLine3(0, 0, 20, 0, points);
  plotPoints(image, points);
  points.clear();
  
  try
    {
    ImageIO.write(image, "png", new File("test.png"));
    } 
  catch (IOException e)
    {
    e.printStackTrace();
    }
  }

private void plotPoints(BufferedImage image, List<Point2i> points)
  {
  for(Point2i point : points)
    {
    image.setRGB(point.x, point.y, 0xffff0000);
    }
  }



}
