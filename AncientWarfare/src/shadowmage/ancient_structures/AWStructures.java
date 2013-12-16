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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import shadowmage.ancient_framework.AWMod;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.interfaces.INBTTaggable;
import shadowmage.ancient_framework.common.network.PacketHandler;
import shadowmage.ancient_framework.common.proxy.CommonProxy;
import shadowmage.ancient_structures.common.block.BlockDataManager;
import shadowmage.ancient_structures.common.config.AWStructureStatics;
import shadowmage.ancient_structures.common.structures.StructureManager;
import shadowmage.ancient_structures.common.structures.build.StructureBuilder;
import shadowmage.ancient_structures.common.structures.file.StructureLoader;
import shadowmage.ancient_structures.common.world_gen.WorldGenManager;
import shadowmage.ancient_structures.common.world_gen.WorldGenStructureManager;

import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;


@Mod( modid = "AncientStructures", name="Ancient Structures", version=Statics.STRUCTURE_VERSION, dependencies="AncientWarfareFramework")
@NetworkMod
(
clientSideRequired = true,
serverSideRequired = true,
packetHandler = PacketHandler.class,
channels = {"AW_struct"},
versionBounds="["+Statics.STRUCTURE_VERSION+",)"
)

public class AWStructures extends AWMod implements ITickHandler, INBTTaggable
{

@SidedProxy(clientSide = "shadowmage.ancient_framework.client.proxy.ClientProxyBase", serverSide = "shadowmage.ancient_framework.common.proxy.CommonProxy")
public static CommonProxy proxy;
@Instance("AncientStructures")
public static AWStructures instance;  


/**
 * ticked builders (direct builders, those with no building block)
 */
private static List<StructureBuilder> builders = new ArrayList<StructureBuilder>();



private static StructureLoader loader;


public void setExportDefaults()
  {
  AWStructureStatics.shouldExportDefaults = true;
  }

public void load()
  {  
 
  this.setValidScannableEntities();
  
  TickRegistry.registerTickHandler(this, Side.SERVER);
  BlockDataManager.instance().loadBlockList();
  WorldGenStructureManager.instance().load();
  
  /**
   * create default dirs if they don't exist...
   */
  File existTest = new File(AWStructureStatics.outputDirectory);
  if(!existTest.exists())
    {
    config.log("Creating default Export Directory");
    existTest.mkdirs();
    }

  existTest = new File(AWStructureStatics.includeDirectory);
  if(!existTest.exists())
    {
    config.log("Creating default Include Directory");
    existTest.mkdirs();
    }
  
  existTest = new File(AWStructureStatics.convertDirectory);
  if(!existTest.exists())
    {
    config.log("Creating default Convert Directory");
    existTest.mkdirs();
    }
  
  existTest = new File(AWStructureStatics.configBaseDirectory);
  if(!existTest.exists())
    {
    config.log("Creating AWConfig directory in config/");
    existTest.mkdirs();
    }
     
  if(AWStructureStatics.shouldExportDefaults || (config.updatedVersion() && config.autoExportOnUpdate()))
    {
    this.copyDefaultStructures(AWStructureStatics.includeDirectory);
    AWStructureStatics.shouldExportDefaults = false;
    }

  loader = StructureLoader.instance();
  loader.scanForPrebuiltFiles(); 
  this.process();
  }

private void setDefaultStructureNames()
  {
  
  }

private void setValidScannableEntities()
  {
 
  }

/*************************************************************************** STRUCTURE LOADING ****************************************************************************/

private void copyDefaultStructures(String pathName)
  {
  if(AWStructureStatics.includeDirectory==null)
    {
    return;
    }  
  this.setDefaultStructureNames();
  InputStream is = null;
  FileOutputStream os = null;
  File file = null;
  config.log("Exporting default structures....");
  int exportCount = 0;
  byte[] byteBuffer;
  for(String fileName : AWStructureStatics.defaultExportStructures)
    {
    try
      {
      is = this.getClass().getResourceAsStream(AWStructureStatics.defaultTemplateDirectory+fileName);
      if(is==null)
        {
        continue;
        }
      
      String trimmedName = fileName.substring(0, fileName.length()-4);
      fileName = trimmedName +"."+AWStructureStatics.templateExtension;
      file = new File(AWStructureStatics.includeDirectory,fileName);
  
      if(!file.exists())
        {
        config.log("Exporting: "+fileName);
        file.createNewFile();
        }
      else
        {
        config.log("Overwriting: "+fileName);
        }
  
      byteBuffer = ByteStreams.toByteArray(is);
      is.close();
      if(byteBuffer.length>0)
        {
        os = new FileOutputStream(file);        
        os.write(byteBuffer);
        os.close();
        exportCount++;
        }
      }
    catch(Exception e)
      {
      config.logError("Error during export of: "+fileName);
      e.printStackTrace();
      }    
    }
  config.log("Exported "+exportCount+" structures");  
  }

private void createDirectory(File file)
  {
  if(!file.exists())
    {
    file.mkdirs();
    }
  }

protected void process()
  {
  if(loader==null)
    {
    return;
    }
  
  loader.convertRuinsTemplates();
  StructureManager.instance().addStructures(loader.processStructureFiles());  
  
  WorldGenManager.instance().loadConfig(AWStructureStatics.configBaseDirectory);
  }

public void addBuilder(StructureBuilder builder)
  {
  this.builders.add(builder);  
  }

public void removeBuilder(StructureBuilder builder)
  {
  this.builders.remove(builder);
  }

public boolean isBeingBuilt(String name)
  {
  for(StructureBuilder b : this.builders)
    {
    if(b.struct.name.equals(name))
      {
      return true;
      }
    }
  return false;
  }

public void clearAllData()
  {
  this.builders.clear();
  }

/*************************************************************************** TICK HANDLING ****************************************************************************/
@Override
public void tickStart(EnumSet<TickType> type, Object... tickData)
  {

  }

@Override
public void tickEnd(EnumSet<TickType> type, Object... tickData)
  {
  World world = (World)tickData[0];
  Iterator<StructureBuilder> it = builders.iterator();
  StructureBuilder builder;
  while(it.hasNext())
    {
    builder = it.next();
    if(builder.world==world)
      {
      builder.onTick();      
      }
    if(builder.isFinished())
      {
      it.remove();
      builder.clearBuilderFromStructure();
      }    
    } 
  }

@Override
public EnumSet<TickType> ticks()
  {
  return EnumSet.of(TickType.WORLD);
  }

@Override
public String getLabel()
  {
  return "AWStructTicker";
  }

/*************************************************************************** NBT LOAD/SAVE ****************************************************************************/
@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList builderList = new NBTTagList();
  for(StructureBuilder builder : builders)
    {
    builderList.appendTag(builder.getNBTTag());
    }  
  tag.setTag("builderList", builderList);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  MinecraftServer server = MinecraftServer.getServer();
  if(server==null)
    {
    config.logError("SEVERE ERROR LOADING BUILDERS, NULL SERVER");
    return;
    }
  NBTTagList builderList = tag.getTagList("builderList");
  for(int i = 0; i < builderList.tagCount(); i++)
    {
    NBTTagCompound builderTag = (NBTTagCompound) builderList.tagAt(i);
    int dimension = builderTag.getInteger("dim");
    World world = server.worldServerForDimension(dimension);
    if(world!=null)
      {
      StructureBuilder builder = StructureBuilder.readTickedBuilderFromNBT(builderTag);
      if(builder!=null)
        {
        builder.setWorld(world);
        builders.add(builder);
        }
      }
    else
      {
      config.logError("SEVERE ERROR LOADING BUILDERS, NULL WORLD FOR DIMENSION: "+dimension);
      }
    }
  }

/*************************************************************************** MOD INIT ****************************************************************************/

@Override
public void loadConfiguration(File config, Logger log)
  {
  this.config = new AWStructureStatics(config, log, Statics.STRUCTURE_VERSION);
  }

@EventHandler
public void preInit(FMLPreInitializationEvent evt) 
  {  
  this.loadConfiguration(evt.getSuggestedConfigurationFile(), evt.getModLog());
  config.log("Ancient Warfare Structures Starting Loading.  Version: "+Statics.STRUCTURE_VERSION);
  
  String path = evt.getModConfigurationDirectory().getAbsolutePath();
  AWStructureStatics.outputDirectory = path+"/AWConfig/structures/export/";
  AWStructureStatics.includeDirectory = path+"/AWConfig/structures/included/";
  AWStructureStatics.convertDirectory = path+"/AWConfig/structures/convert/";
  AWStructureStatics.configBaseDirectory = path+"/AWConfig/";  
  /**
   * TODO
   */
  config.log("Ancient Warfare Structures Pre-Init finished.");
  }

@EventHandler
public void init(FMLInitializationEvent evt)
  {
  config.log("Ancient Warfare Structures Init started.");
  /**
   * TODO
   */
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
