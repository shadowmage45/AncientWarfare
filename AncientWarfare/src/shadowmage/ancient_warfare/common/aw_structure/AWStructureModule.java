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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.World;
import shadowmage.ancient_warfare.client.aw_structure.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.aw_structure.build.Builder;
import shadowmage.ancient_warfare.common.aw_structure.data.BlockDataManager;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.aw_structure.load.StructureLoader;
import shadowmage.ancient_warfare.common.aw_structure.store.StructureManager;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * config/setup module for structures and structure related stuff....
 * @author Shadowmage
 *
 */
public class AWStructureModule implements ITickHandler
{

/**
 * the base config directory
 */
private static String directory;
public static String outputDirectory = null;
public static String includeDirectory = null;


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


public void load(String directory)
  {  
  TickRegistry.registerTickHandler(this, Side.SERVER);
  this.directory = directory;  
  outputDirectory = directory+"/AWConfig/structures/export/";
  includeDirectory = directory+"/AWConfig/structures/included/";
  
  File existTest = new File(outputDirectory);
  if(!existTest.exists())
    {
    System.out.println("creating directory");
    existTest.mkdirs();
    }
 
  existTest = new File(includeDirectory);
  if(!existTest.exists())
    {
    System.out.println("creating directory");
    existTest.mkdirs();
    }
  
  BlockDataManager.instance().loadBlockList();
  loader = new StructureLoader(directory);
  loader.scanForPrebuiltFiles();    
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
  for(Builder builder : builders)
    {
    if(builder.world==world)
      {
      builder.onTick();
      }
    }
  Iterator<Builder> it = builders.iterator();
  Builder builder;
  while(it.hasNext())
    {
    builder = it.next();
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

}
