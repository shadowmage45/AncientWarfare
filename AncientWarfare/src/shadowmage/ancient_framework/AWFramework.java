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
package shadowmage.ancient_framework;

import java.io.File;
import java.util.logging.Logger;

import net.minecraftforge.common.MinecraftForge;
import shadowmage.ancient_framework.common.config.AWConfig;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.gamedata.AWGameData;
import shadowmage.ancient_framework.common.lang.LanguageLoader;
import shadowmage.ancient_framework.common.network.GUIHandler;
import shadowmage.ancient_framework.common.network.PacketHandler;
import shadowmage.ancient_framework.common.proxy.CommonProxy;
import shadowmage.ancient_framework.common.registry.ObjectRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;



@Mod( modid = "AncientWarfareCore", name="Ancient Warfare", version=Statics.FRAMEWORK_VERSION)
@NetworkMod
(
clientSideRequired = true,
serverSideRequired = true,
packetHandler = PacketHandler.class,
channels = {"AW_mod"},
versionBounds="["+Statics.FRAMEWORK_VERSION+",)"
)

public class AWFramework extends AWMod
{

@Instance("AncientWarfareCore")
public static AWFramework instance;
@SidedProxy(clientSide = "shadowmage.ancient_framework.client.proxy.ClientProxyBase", serverSide = "shadowmage.ancient_framework.common.proxy.CommonProxy")
public static CommonProxy proxy;

public ObjectRegistry objectRegistry;
public AWGameData gameData;
public shadowmage.ancient_framework.common.event.EventHandler eventHandler;

/**
 * flags for loading/loaded status of different modules, so that modules can register plugins/etc
 */
public static boolean loadedStructures = false;
public static boolean loadedVehicles = false;
public static boolean loadedNpcs = false;
public static boolean loadedAutomation = false;

@Override
public void loadConfiguration(File config, Logger log)
  {  
  this.config = new AWConfig(config, log, Statics.FRAMEWORK_VERSION);
  objectRegistry = new ObjectRegistry(this.config);
  AWLog.setLogger(log);
  }

@Override
@EventHandler
public void preInit(FMLPreInitializationEvent evt)
  {
  this.loadConfiguration(evt.getSuggestedConfigurationFile(), evt.getModLog());
  AWLog.log("Ancient Warfare Core Starting Loading.  Version: "+Statics.FRAMEWORK_VERSION);  
  gameData = new AWGameData();
  eventHandler = new shadowmage.ancient_framework.common.event.EventHandler();
  MinecraftForge.EVENT_BUS.register(eventHandler);
  NetworkRegistry.instance().registerGuiHandler(this, GUIHandler.instance());
  LanguageLoader.instance().loadLanguageFiles();
  }

@Override
@EventHandler
public void init(FMLInitializationEvent evt)
  {

  }

@Override
@EventHandler
public void postInit(FMLPostInitializationEvent evt)
  {
  config.saveConfig();
  }

@Override
@EventHandler
public void serverPreStart(FMLServerAboutToStartEvent evt)
  {
  AWGameData.resetTrackedData();
  }

@Override
@EventHandler
public void serverStarting(FMLServerStartingEvent evt)
  {

  }

@Override
@EventHandler
public void serverStarted(FMLServerStartedEvent evt)
  {

  }

@Override
@EventHandler
public void serverStopping(FMLServerStoppingEvent evt)
  {

  }

@Override
@EventHandler
public void serverStopped(FMLServerStoppedEvent evt)
  {

  }

}
