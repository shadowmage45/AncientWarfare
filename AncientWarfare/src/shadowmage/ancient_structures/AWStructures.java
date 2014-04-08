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
import java.util.Random;
import java.util.logging.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import shadowmage.ancient_structures.client.proxy.CommonProxy;
import shadowmage.ancient_structures.common.config.AWLog;
import shadowmage.ancient_structures.common.config.AWStructureStatics;
import shadowmage.ancient_structures.common.item.AWStructuresItemLoader;
import shadowmage.ancient_structures.common.manager.BlockDataManager;
import shadowmage.ancient_structures.common.manager.StructureTemplateManager;
import shadowmage.ancient_structures.common.manager.WorldGenStructureManager;
import shadowmage.ancient_structures.common.template.load.TemplateLoader;
import shadowmage.ancient_structures.common.template.plugin.StructurePluginManager;
import shadowmage.ancient_structures.common.utils.AWGameData;
import shadowmage.ancient_structures.common.utils.AWMod;
import shadowmage.ancient_structures.common.utils.BlockTools;
import shadowmage.ancient_structures.common.world_gen.StructureMap;
import shadowmage.ancient_structures.common.world_gen.WorldStructureGenerator;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import cpw.mods.fml.common.IPlayerTracker;
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
import cpw.mods.fml.common.registry.GameRegistry;


@Mod( modid = "AncientStructures", name="Ancient Structures", version=Config.VERSION, dependencies="required-after:AncientWarfare")
@NetworkMod
(
clientSideRequired = true,
serverSideRequired = true,
versionBounds="["+Config.VERSION+",)"
)

public class AWStructures extends AWMod implements IPlayerTracker
{

@SidedProxy(clientSide = "shadowmage.ancient_structures.client.proxy.ClientProxyStructure", serverSide = "shadowmage.ancient_structures.client.proxy.CommonProxy")
public static CommonProxy proxy;
@Instance("AncientStructures")
public static AWStructures instance;  

public StructurePluginManager pluginManager;

@Override
public void loadConfiguration(File config, Logger log)
  {
  this.config = new AWStructureStatics(config, log, Config.VERSION);
  }

@EventHandler
public void preInit(FMLPreInitializationEvent evt) 
  {    
  AWLog.setLogger(evt.getModLog());
  AWLog.log("Ancient Warfare Structures Starting Loading.  Version: "+Config.VERSION);
  this.loadConfiguration(evt.getSuggestedConfigurationFile(), evt.getModLog());
  pluginManager = new StructurePluginManager();   
  String path = evt.getModConfigurationDirectory().getAbsolutePath();
  TemplateLoader.instance().initializeAndExportDefaults(path); 
  AWStructuresItemLoader.instance().registerItems();
  GameRegistry.registerPlayerTracker(instance);
  GameRegistry.registerWorldGenerator(WorldStructureGenerator.instance());
  AWGameData.addDataClass("AWStructureMap", StructureMap.class);
  MinecraftForge.EVENT_BUS.register( new AWGameData());
  proxy.registerClientData();
  AWLog.log("Ancient Warfare Structures Pre-Init finished.");
  }

@EventHandler
public void init(FMLInitializationEvent evt)
  {
  config.log("Ancient Warfare Structures Init started.");
  AWLog.log("Loading block names and mappings for structure system.");
  BlockDataManager.loadBlockList();//needs to be called during init, as not all mods would have registered blocks during AW's pre-init phase
  /**
   * listen for plugin registration
   * TODO 
   */
  config.log("Ancient Warfare Structures Init completed.");
  }

@Override
@EventHandler
public void postInit(FMLPostInitializationEvent evt)
  {
  config.log("Ancient Warfare Structures Post-Init started");  
  config.log("Loading plugins...");
  pluginManager.loadPlugins();
  config.log("Loading biome list...");
  WorldGenStructureManager.instance().loadBiomeList();
  config.log("Loading Structure templates...");
  TemplateLoader.instance().loadTemplates();
  config.log("Loading Creating recipes for survival-mode structures...");
  AWCraftingManager.instance().addStructureRecipes();
  config.log("Ancient Warfare Structures Post-Init completed.  Successfully completed all loading stages.");
  config.saveConfig();
  }

@Override
@EventHandler
public void serverPreStart(FMLServerAboutToStartEvent evt){}

@Override
@EventHandler
public void serverStarting(FMLServerStartingEvent evt){}

@Override
@EventHandler
public void serverStarted(FMLServerStartedEvent evt){}

@Override
@EventHandler
public void serverStopping(FMLServerStoppingEvent evt){}

@Override
@EventHandler
public void serverStopped(FMLServerStoppedEvent evt){}

@Override
public void onPlayerLogin(EntityPlayer player)
  {
  if(!player.worldObj.isRemote)
    {
    StructureTemplateManager.instance().onPlayerConnect(player);
    }  
  }

@Override
public void onPlayerLogout(EntityPlayer player){}

@Override
public void onPlayerChangedDimension(EntityPlayer player){}

@Override
public void onPlayerRespawn(EntityPlayer player){}

private void clusterValueTest()
  {
  int[][] chunkMap = new int[40][40];
  int[] structureValues = new int []{1,1,3,5,10,12,17,20,25,30,50,80,120};
  Random rng = new Random();
  
  int remainingValue = 0;
  int chosenValue = 0;
  for(int x = 0; x < 40; x++)
    {
    for(int z = 0; z< 40; z++)
      {
      if(rng.nextFloat()>0.075f){continue;}//default 7.5% chance to generate
      remainingValue = 200 - countValues(x, z, chunkMap);
      
      if(remainingValue<=0){continue;}//could not place any structure
      while(true)
        {
        chosenValue = structureValues[rng.nextInt(structureValues.length)];
        if(chosenValue<=remainingValue){break;}
        }      
      chunkMap[x][z]=chosenValue;      
      }
    }
  
  String output = "";
  for(int z = 0; z < 40; z++)
    {
    for(int x = 0; x< 40; x++)
      {      
      output = output + chunkMap[x][z];
      if(x<39){output = output+",";}
      }
    if(z<39){output = output+"\n";}
    }
  AWLog.logDebug("chunk map test output: "+output);
  }

private int countValues(int startX, int startZ, int[][] chunkMap)
  {
  startX-=8;
  startZ-=8;
  int endX = startX+17;
  int endZ = startZ+17;
  if(startX<0){startX=0;}
  if(startZ<0){startZ=0;}
  if(endX>40){endX=40;}
  if(endZ>40){endZ=40;}
  int value = 0;
  for(int x = startX; x< endX; x++)
    {
    for(int z = startZ; z<endZ; z++)
      {
      value += chunkMap[x][z];
      }
    }  
  return value;
  }

}
