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

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.chunkloading.ChunkLoader;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.event.EventHandler;
import shadowmage.ancient_warfare.common.gates.EntityGate;
import shadowmage.ancient_warfare.common.gates.types.Gate;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.lang.LanguageLoader;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.network.PacketHandler;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.pathfinding.threading.ClientTicker;
import shadowmage.ancient_warfare.common.pathfinding.threading.ServerTicker;
import shadowmage.ancient_warfare.common.proxy.CommonProxy;
import shadowmage.ancient_warfare.common.registry.AWEntityRegistry;
import shadowmage.ancient_warfare.common.registry.AmmoRegistry;
import shadowmage.ancient_warfare.common.registry.ArmorRegistry;
import shadowmage.ancient_warfare.common.registry.CivicRegistry;
import shadowmage.ancient_warfare.common.registry.NpcRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.GameDataTracker;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.utils.ServerTickTimer;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.world_gen.VillageGenerator;
import shadowmage.ancient_warfare.common.world_gen.WorldGenManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;


@Mod( modid = "AncientWarfare", name="Ancient Warfare", version="MC"+Config.MC_VERSION+"-"+Config.CORE_VERSION_MAJOR+"."+Config.CORE_VERSION_BUILD+"-"+Config.CORE_BUILD_STATUS)
@NetworkMod
(
clientSideRequired = true,
serverSideRequired = true,
packetHandler = PacketHandler.class,
channels = {"AW_vehicle", "AW_tile", "AW_gui", "AW_soldier", "AW_mod"},
versionBounds="MC"+Config.MC_VERSION+"-"+Config.CORE_VERSION_MAJOR+"."+Config.CORE_VERSION_BUILD+"-"+Config.CORE_BUILD_STATUS
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
  Config.log("Starting Loading.  Version: "+"MC"+Config.MC_VERSION+"--"+Config.CORE_VERSION_MAJOR+"."+Config.CORE_VERSION_BUILD+"-"+Config.CORE_BUILD_STATUS);

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
  AWEntityRegistry.registerEntity(EntityGate.class, "Gate.GateBase", 130, 100, false);
//  LanguageLoader.instance().loadDefaultLanguageFiles();
  Config.log("Ancient Warfare Pre-Init finished.");
  }

/**
 * load registry stuff....
 * @param evt
 */
@Init
public void init(FMLInitializationEvent evt)
  {
  Config.log("Ancient Warfare Init started.");
  NetworkRegistry.instance().registerGuiHandler(this, GUIHandler.instance());
  proxy.registerClientData();

  /**
   * process loaded structures
   */
  AWStructureModule.instance().process();
  Config.log("Ancient Warfare Init completed.");
  }

/**
 * finalize config settings, load NPCs (which rely on other crap from other mods..potentially)
 * @param evt
 */
@PostInit
public void load(FMLPostInitializationEvent evt)
  {  
  Config.log("Ancient Warfare Post-Init started");
  
  VillageGenerator.load();
  NpcRegistry.instance().registerNPCs(); 
  CivicRegistry.instance().registerCivics();
  Gate.registerGateTypes();
  ResearchGoal.load();
  AWCraftingManager.instance().loadRecipes();
  /**
   * and finally, save the config in case there were any changes made during init
   */
  Config.saveConfig();
  
  TickRegistry.registerTickHandler(new ServerTickTimer(), Side.SERVER);
  TickRegistry.registerTickHandler(new ServerTicker(), Side.SERVER);
  TickRegistry.registerTickHandler(new ClientTicker(), Side.CLIENT);
  Config.log("Ancient Warfare Post-Init completed.  Successfully completed all loading stages."); 
  }

@ServerStopping
public void serverStarting(FMLServerStoppingEvent evt)
  {
  GameDataTracker.instance().resetAllTrackedData();
  }

}
