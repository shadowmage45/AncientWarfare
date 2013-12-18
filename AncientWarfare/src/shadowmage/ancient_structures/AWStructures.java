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
package shadowmage.ancient_structures;

import java.io.File;
import java.util.logging.Logger;

import shadowmage.ancient_framework.AWFramework;
import shadowmage.ancient_framework.AWMod;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.network.PacketHandler;
import shadowmage.ancient_framework.common.proxy.CommonProxy;
import shadowmage.ancient_structures.common.block.BlockDataManager;
import shadowmage.ancient_structures.common.config.AWStructureStatics;
import shadowmage.ancient_structures.common.item.AWStructuresItemLoader;
import shadowmage.ancient_structures.common.template.load.TemplateLoader;
import shadowmage.ancient_structures.common.template.plugin.StructurePluginManager;
import shadowmage.ancient_structures.common.world_gen.WorldGenStructureManager;
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


@Mod( modid = "AncientStructures", name="Ancient Structures", version=Statics.STRUCTURE_VERSION, dependencies="required-after:AncientWarfareCore")
@NetworkMod
(
clientSideRequired = true,
serverSideRequired = true,
packetHandler = PacketHandler.class,
channels = {"AW_struct"},
versionBounds="["+Statics.STRUCTURE_VERSION+",)"
)

public class AWStructures extends AWMod
{

@SidedProxy(clientSide = "shadowmage.ancient_framework.client.proxy.ClientProxyBase", serverSide = "shadowmage.ancient_framework.common.proxy.CommonProxy")
public static CommonProxy proxy;
@Instance("AncientStructures")
public static AWStructures instance;  

public StructurePluginManager pluginManager;

@Override
public void loadConfiguration(File config, Logger log)
  {
  this.config = new AWStructureStatics(config, log, Statics.STRUCTURE_VERSION);
  }

@EventHandler
public void preInit(FMLPreInitializationEvent evt) 
  {  
  this.loadConfiguration(evt.getSuggestedConfigurationFile(), evt.getModLog());
  AWLog.log("Ancient Warfare Structures Starting Loading.  Version: "+Statics.STRUCTURE_VERSION);
  String path = evt.getModConfigurationDirectory().getAbsolutePath();
  pluginManager = new StructurePluginManager(); 
  
  BlockDataManager.instance().loadBlockList();
  AWStructuresItemLoader.instance().registerItems();
  TemplateLoader.instance().initializeAndExportDefaults(path);
  /**
   * TODO
   */
  config.log("Ancient Warfare Structures Pre-Init finished.");
  }

@EventHandler
public void init(FMLInitializationEvent evt)
  {
  config.log("Ancient Warfare Structures Init started.");
  pluginManager.loadPlugins();
  /**
   * TODO
   */
  TemplateLoader.instance().loadTemplates();
  config.log("Ancient Warfare Structures Init completed.");
  }

@Override
@EventHandler
public void postInit(FMLPostInitializationEvent evt)
  {
  config.log("Ancient Warfare Structures Post-Init started");
  /**
   * TODO
   */
  config.saveConfig();
  config.log("Ancient Warfare Structures Post-Init completed.  Successfully completed all loading stages."); 
  }

@Override
@EventHandler
public void serverPreStart(FMLServerAboutToStartEvent evt)
  {
  
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
