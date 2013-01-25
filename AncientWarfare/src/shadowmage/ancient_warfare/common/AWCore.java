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
package shadowmage.ancient_warfare.common;


import java.io.IOException;

import net.minecraftforge.common.MinecraftForge;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.event.EventHandler;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.network.PacketHandler;
import shadowmage.ancient_warfare.common.proxy.CommonProxy;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.utils.BlockLoader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;


@Mod( modid = "AncientWarfare", name="Ancient Warfare", version="MC"+Config.MC_VERSION+"--"+Config.CORE_VERSION_MAJOR+"."+Config.CORE_VERSION_MINOR+"."+Config.CORE_VERSION_BUILD+"-"+Config.CORE_BUILD_STATUS)
@NetworkMod
(
clientSideRequired = true,
serverSideRequired = true,
packetHandler = PacketHandler.class,
channels = {"AW_vehicle", "AW_tile", "AW_gui", "AW_soldier", "AW_mod"},
versionBounds="["+"MC"+Config.MC_VERSION+"--"+Config.CORE_VERSION_MAJOR+"."+Config.CORE_VERSION_MINOR+"."+Config.CORE_VERSION_BUILD+",)"
)

public class AWCore 
{	
@SidedProxy(clientSide = "shadowmage.ancient_warfare.client.proxy.ClientProxy", serverSide = "shadowmage.ancient_warfare.common.proxy.CommonProxy")
public static CommonProxy proxy;
@Instance("AncientWarfare")
public static AWCore instance;	
	

/**
 * load settings, config, items
 * @param evt
 */
@PreInit
public void preInit(FMLPreInitializationEvent evt) 
  {
  /**
   * load config file and setup logger
   */
  Config.loadConfig(evt.getSuggestedConfigurationFile());
  Config.setLogger(evt.getModLog());
  
  /**
   * register player tracker
   */
  GameRegistry.registerPlayerTracker(PlayerTracker.instance());
  
  /**
   * register eventHandler
   */
  MinecraftForge.EVENT_BUS.register(EventHandler.instance());
  
  /**
   * load items
   */
  ItemLoader.instance().load();
  BlockLoader.instance().load();
  
  /**
   * load structure related stuff (needs config directory from this event, could save string and load later)
   */
  try
    {
    AWStructureModule.instance().load(evt.getModConfigurationDirectory().getCanonicalPath());
    } 
  catch (IOException e)
    {
    e.printStackTrace();
    }
 
  }

/**
 * load registry stuff....
 * @param evt
 */
@Init
public void init(FMLInitializationEvent evt)
  {
  NetworkRegistry.instance().registerGuiHandler(this, GUIHandler.instance());
  proxy.registerClientData();
  
  /**
   * process loaded structures
   */
  AWStructureModule.instance().process();
  }
	
/**
 * finalize config settings
 * @param evt
 */
@PostInit
public void load(FMLPostInitializationEvent evt)
  {  
  
  /**
   * and finally, save the config in case there were any changes made during init
   */
  Config.saveConfig();
  }
		

}
