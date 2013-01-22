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
package shadowmage.ancient_warfare.common.aw_structure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_core.utils.INBTTaggable;
import shadowmage.ancient_warfare.common.aw_structure.build.Builder;
import shadowmage.ancient_warfare.common.aw_structure.data.BlockDataManager;
import shadowmage.ancient_warfare.common.aw_structure.load.StructureLoader;
import shadowmage.ancient_warfare.common.aw_structure.store.StructureManager;

import com.google.common.io.Files;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * config/setup module for structures and structure related stuff....
 * @author Shadowmage
 *
 */
public class AWStructureModule implements ITickHandler, INBTTaggable
{

/**
 * the base config directory
 */
private static String directory;
public static String outputDirectory = null;
public static String includeDirectory = null;
public static String playerTempDirectory = null;

private boolean shouldExportDefaults = false;

/**
 * ticked builders
 */
private static List<Builder> builders = new ArrayList<Builder>();


private static StructureLoader loader;

private AWStructureModule(){ }
private static AWStructureModule INSTANCE;
public static AWStructureModule instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE= new AWStructureModule();    
    }
  return INSTANCE;
  }

public void setExportDefaults()
  {
  this.shouldExportDefaults = true;
  }

public void load(String directory)
  {  
  TickRegistry.registerTickHandler(this, Side.SERVER);
  this.directory = directory;  
  outputDirectory = directory+"/AWConfig/structures/export/";
  includeDirectory = directory+"/AWConfig/structures/included/";
  playerTempDirectory = directory+"/AWConfig/structures/temp/";
  
  File existTest = new File(outputDirectory);
  if(!existTest.exists())
    {
    Config.log("Creating default Export Directory");
    existTest.mkdirs();
    }
 
  existTest = new File(includeDirectory);
  if(!existTest.exists())
    {
    Config.log("Creating default Include Directory");
    existTest.mkdirs();
    }
  
  existTest = new File(playerTempDirectory);
  if(!existTest.exists())
    {
    Config.log("Creating default Temp Directory");
    existTest.mkdirs();
    }  
  
  BlockDataManager.instance().loadBlockList();
  
  if(shouldExportDefaults || Config.DEBUG)
    {
    this.copyDefaultStructures(includeDirectory);
    this.shouldExportDefaults = false;
    }
  
  loader = new StructureLoader();
  loader.scanForPrebuiltFiles(); 
  }

private void copyDefaultStructures(String pathName)
  {
  //TODO figure this whole thing out....
  ArrayList<File> files = new ArrayList<File>();
  
  
  File destinationFile;
  for(File file : files)
    {
    destinationFile = new File(pathName,file.getName());
    try
      {
      Files.copy(file, destinationFile);
      } 
    catch (IOException e)
      {
      Config.logError("COULD NOT EXPORT DEFAULT STRUCTURES");
      e.printStackTrace();
      break;
      }
    }  
  }

private void createDirectory(File file)
  {
  if(!file.exists())
    {
    file.mkdirs();
    }
  }

public void process()
  {
  if(loader==null)
    {
    return;
    }
  StructureManager.instance().addStructures(loader.processStructureFiles());  
  }

public void addBuilder(Builder builder)
  {
  this.builders.add(builder);  
  }

public void removeBuilder(Builder builder)
  {
  this.builders.remove(builder);
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
  Iterator<Builder> it = builders.iterator();
  Builder builder;
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

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList builderList = new NBTTagList();
  for(Builder builder : builders)
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
    Config.logError("SEVERE ERROR LOADING BUILDERS, NULL SERVER");
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
      Builder builder = Builder.readTickedBuilderFromNBT(builderTag, world);
      if(builder!=null)
        {
        builders.add(builder);
        }
      }
    }
  }

public void clearAllData()
  {
  this.builders.clear();
  }

}
