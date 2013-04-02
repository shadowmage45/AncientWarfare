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
import java.util.List;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import shadowmage.ancient_warfare.common.chunkloading.ChunkLoader;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.event.EventHandler;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.network.PacketHandler;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.pathfinding.PathFinder;
import shadowmage.ancient_warfare.common.pathfinding.PathUtils;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccess;
import shadowmage.ancient_warfare.common.pathfinding.PathWorldAccessTest;
import shadowmage.ancient_warfare.common.proxy.CommonProxy;
import shadowmage.ancient_warfare.common.registry.AWEntityRegistry;
import shadowmage.ancient_warfare.common.registry.AmmoRegistry;
import shadowmage.ancient_warfare.common.registry.ArmorRegistry;
import shadowmage.ancient_warfare.common.registry.NpcRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.utils.BlockLoader;
import shadowmage.ancient_warfare.common.utils.ServerTickTimer;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.world_gen.WorldGenManager;
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
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;


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
  Config.log("Starting Loading.  Version: "+"MC"+Config.MC_VERSION+"--"+Config.CORE_VERSION_MAJOR+"."+Config.CORE_VERSION_MINOR+"."+Config.CORE_VERSION_BUILD+"-"+Config.CORE_BUILD_STATUS);

  /**
   * register player tracker
   */
  GameRegistry.registerPlayerTracker(PlayerTracker.instance());

  /**
   * register eventHandler
   */
  MinecraftForge.EVENT_BUS.register(EventHandler.instance());

  /**
   * register worldGenHandler
   */
  GameRegistry.registerWorldGenerator(WorldGenManager.instance());

  /**
   * register chunk loader 
   */
  ForgeChunkManager.setForcedChunkLoadingCallback(this, ChunkLoader.instance());
  
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
  
  /**
   *load vehicles, ammo, upgrades 
   */
  AmmoRegistry.instance().registerAmmoTypes();
  VehicleUpgradeRegistry.instance().registerUpgrades();
  ArmorRegistry.instance().registerArmorTypes();
  /**
   * have to load vehicles after everything else i think...
   */
  AWEntityRegistry.registerEntity(VehicleBase.class, "Vehicle.VehicleBase", 130, 3, false);
  VehicleRegistry.instance().registerVehicles();
  AWEntityRegistry.registerEntity(NpcBase.class, "Npc.NpcBase", 130, 3, true);
  Config.log("Ancient Warfare Pre-Init finished");
  }

/**
 * load registry stuff....
 * @param evt
 */
@Init
public void init(FMLInitializationEvent evt)
  {
  Config.log("Ancient Warfare Init started");
  NetworkRegistry.instance().registerGuiHandler(this, GUIHandler.instance());
  proxy.registerClientData();

  /**
   * process loaded structures
   */
  AWStructureModule.instance().process();
  Config.log("Ancient Warfare Init completed");
  }

/**
 * finalize config settings, load NPCs (which rely on other crap from other mods..potentially)
 * @param evt
 */
@PostInit
public void load(FMLPostInitializationEvent evt)
  {  
  Config.log("Ancient Warfare Post-Init started");
  NpcRegistry.instance().registerNPCs(); 
  /**
   * and finally, save the config in case there were any changes made during init
   */
  Config.saveConfig();
  
  TickRegistry.registerTickHandler(new ServerTickTimer(), Side.SERVER);
  Config.log("Ancient Warfare Post-Init completed.  Successfully completed all loading stages."); 
//  this.pathTest();
//  PathUtils.getPositionsBetween(-10, 0, 0, 10, 0, 5);
//  PathUtils.getPositionsBetween2(-10, 0, 10, 5);
//  PathUtils.traceRay(0.5f, 0.25f, 10f, 12, 10, 15.5f);
  PathUtils.traceRay2(0, 0.5f, 0.f, 10, 0.0f, 15.0f);
  }

public void pathTest()
  {
  PathFinder pather = new PathFinder();
  PathWorldAccess world = new PathWorldAccessTest();
  List<Node> path;
  List<Node> pathPart;
  long tStart;
  long tCurrent;
  long tTotal = 0;
  Node n;
  int x;
  int y;
  int z;
  int searchRange = 10;
  for(int i = 0; i < 10; i++)
    {
    tTotal = 0;
    Config.logDebug("Doing path run:");
    tStart = System.nanoTime();
    path = pather.findPath(world, 1, 1, 1, 10, 1, 10, searchRange);
    tCurrent = System.nanoTime();
    tTotal += tCurrent - tStart;
    n = path.get(path.size()-1);
    for(Node node : path)
      {
      Config.logDebug(node.toString());    
      } 
    while(n!=null && (n.x != 10 || n.y!=1 || n.z!=10))//if not null, and not the goal
      {
      tStart = System.nanoTime();
      pathPart = pather.findPath(world, n.x, n.y, n.z, 10, 1, 10, searchRange);
      tCurrent = System.nanoTime();
      tTotal += tCurrent - tStart;
      n = pathPart.get(pathPart.size()-1);
      for(Node node : pathPart)
        {
        Config.logDebug(node.toString());    
        } 
      }
    
    Config.logDebug("Path run finished. path time : "+ tTotal/1000000L+"::"+tTotal);
    } 
  }


}
