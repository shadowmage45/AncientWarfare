/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.manager.BlockDataManager;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.structures.build.Builder;
import shadowmage.ancient_warfare.common.structures.file.StructureLoader;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.world_gen.WorldGenManager;
import shadowmage.ancient_warfare.common.world_gen.WorldGenStructureManager;

import com.google.common.io.ByteStreams;

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
 * used to grab streams of resources from inside the jar/zip/whatever....
 */
private static final String defaultTemplateDirectory = "/shadowmage/ancient_warfare/resources/templates/";


/**
 * the base config directory
 */
private static String directory;

/**
 * specific directories.
 */
public static String outputDirectory = null;
public static String includeDirectory = null;
public static String convertDirectory = null;
public static String configBaseDirectory = null;

private static final List<String> defaultExportStructures = new ArrayList<String>();

private boolean shouldExportDefaults = false;

/**
 * ticked builders (direct builders, those with no building block)
 */
private static List<Builder> builders = new ArrayList<Builder>();

/**
 * list of valid entities for structure scanning
 */
private Set<Class> validEntitiesToScan = new HashSet<Class>();

private static StructureLoader loader;

private AWStructureModule(){}
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
  this.directory = directory;  
  outputDirectory = directory+"/AWConfig/structures/export/";
  includeDirectory = directory+"/AWConfig/structures/included/";
  convertDirectory = directory+"/AWConfig/structures/convert/";
  configBaseDirectory = directory+"/AWConfig/";
  this.setValidScannableEntities();
  
  TickRegistry.registerTickHandler(this, Side.SERVER);
  BlockDataManager.instance().loadBlockList();
  WorldGenStructureManager.instance().load();
  
  /**
   * create default dirs if they don't exist...
   */
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
  
  existTest = new File(convertDirectory);
  if(!existTest.exists())
    {
    Config.log("Creating default Convert Directory");
    existTest.mkdirs();
    }
     
  if(shouldExportDefaults || (Config.updatedVersion && Config.autoExportOnUpdate))
    {
    this.copyDefaultStructures(includeDirectory);
    this.shouldExportDefaults = false;
    }

  loader = StructureLoader.instance();
  loader.scanForPrebuiltFiles(); 
  }

private void setDefaultStructureNames()
  {
  this.defaultExportStructures.add("villageGardenLarge.aws");
  this.defaultExportStructures.add("villageGardenSmall.aws");
  this.defaultExportStructures.add("villageHouse1.aws");
  this.defaultExportStructures.add("villageHouse2.aws");
  this.defaultExportStructures.add("villageHouseGarden.aws");
  this.defaultExportStructures.add("villageHouseSmall.aws");
  this.defaultExportStructures.add("villageHouseSmall2.aws");
  this.defaultExportStructures.add("villageLibrary.aws");
  this.defaultExportStructures.add("villageSmith.aws");
  this.defaultExportStructures.add("villageTorch.aws");
  this.defaultExportStructures.add("villageWell.aws");
  this.defaultExportStructures.add("advancedVillageLibrary.aws");
  this.defaultExportStructures.add("obsidianVault.aws");
  this.defaultExportStructures.add("banditCamp.aws");
  this.defaultExportStructures.add("lavaFarm.aws");
  this.defaultExportStructures.add("fountain1.aws");
  this.defaultExportStructures.add("logCabin.aws");
  this.defaultExportStructures.add("fortress1.aws");
  this.defaultExportStructures.add("fortress2.aws");
  this.defaultExportStructures.add("tower1.aws");
  }

private void setValidScannableEntities()
  {
  this.validEntitiesToScan.add(VehicleBase.class);
  this.validEntitiesToScan.add(NpcBase.class);
  this.validEntitiesToScan.add(EntityVillager.class);
  this.validEntitiesToScan.add(EntityPig.class);
  this.validEntitiesToScan.add(EntitySheep.class);
  this.validEntitiesToScan.add(EntityCow.class);
  this.validEntitiesToScan.add(EntityWolf.class);
  this.validEntitiesToScan.add(EntityOcelot.class);
  this.validEntitiesToScan.add(EntityBoat.class);
  this.validEntitiesToScan.add(EntityItemFrame.class);
  this.validEntitiesToScan.add(EntityPainting.class);
  this.validEntitiesToScan.add(EntityMinecart.class);
  }

public boolean isScannableEntity(Class clz)
  {
  return this.validEntitiesToScan.contains(clz);
  }

private void copyDefaultStructures(String pathName)
  {
  if(includeDirectory==null)
    {
    return;
    }  
  this.setDefaultStructureNames();
  InputStream is = null;
  FileOutputStream os = null;
  File file = null;
  Config.log("Exporting default structures....");
  int exportCount = 0;
  byte[] byteBuffer;
  for(String fileName : defaultExportStructures)
    {
    try
      {
      is = this.getClass().getResourceAsStream(defaultTemplateDirectory+fileName);
      if(is==null)
        {
        continue;
        }
      
      String trimmedName = fileName.substring(0, fileName.length()-4);
      fileName = trimmedName +"."+Config.templateExtension;
      file = new File(includeDirectory,fileName);
  
      if(!file.exists())
        {
        Config.log("Exporting: "+fileName);
        file.createNewFile();
        }
      else
        {
        Config.log("Overwriting: "+fileName);
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
      Config.logError("Error during export of: "+fileName);
      e.printStackTrace();
      }    
    }
  Config.log("Exported "+exportCount+" structures");  
  }

private void createDirectory(File file)
  {
  if(!file.exists())
    {
    file.mkdirs();
    }
  }

/**
 * aka. post-load, post-init..w/e
 */
public void process()
  {
  if(loader==null)
    {
    return;
    }
  
  loader.convertRuinsTemplates();
  StructureManager.instance().addStructures(loader.processStructureFiles());  
  
  WorldGenManager.instance().loadConfig(configBaseDirectory);
  }

public void addBuilder(Builder builder)
  {
  this.builders.add(builder);  
  }

public void removeBuilder(Builder builder)
  {
  this.builders.remove(builder);
  }

public boolean isBeingBuilt(String name)
  {
  for(Builder b : this.builders)
    {
    if(b.struct.name.equals(name))
      {
      return true;
      }
    }
  return false;
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
      Builder builder = Builder.readTickedBuilderFromNBT(builderTag);
      if(builder!=null)
        {
        builder.setWorld(world);
        builders.add(builder);
        }
      }
    else
      {
      Config.logError("SEVERE ERROR LOADING BUILDERS, NULL WORLD FOR DIMENSION: "+dimension);
      }
    }
  }

public void clearAllData()
  {
  this.builders.clear();
  }

}
