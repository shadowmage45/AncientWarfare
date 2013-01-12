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
import java.util.List;

import com.google.common.io.Files;

import shadowmage.ancient_warfare.common.aw_structure.data.BlockDataManager;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.aw_structure.load.StructureLoader;

/**
 * config/setup module for structures and structure related stuff....
 * @author Shadowmage
 *
 */
public class AWStructureModule
{

/**
 * the base config directory
 */
private static String directory;
public static String outputDirectory = null;
public static String includeDirectory = null;

private static List<ProcessedStructure> structures = new ArrayList<ProcessedStructure>();
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
  structures.addAll(loader.processStructureFiles());
  }

}
