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
package shadowmage.ancient_warfare.common.aw_structure.load;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_structure.data.LoadedStructureRaw;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;

public class StructureLoader
{

public StructureLoader(String directory)
  {
  this.dir = directory;
  }

/**
 * file path for reading structures
 */
public String dir;

/**
 * called probableStructureFiles because they haven't been opened/read/checked for validity
 */
private List<File> probableStructureFiles = new ArrayList<File>();

public void scanForPrebuiltFiles()
  {
  probableStructureFiles.clear();
  System.out.println("config base dir: " + dir);
  this.recursiveScan(new File(dir+"/AWConfig/structures/included/"), probableStructureFiles);
  System.out.println("found: "+this.probableStructureFiles.size()+" probable structure files");
  }

private void recursiveScan(File directory, List<File> fileList)
  {
  if(directory==null)
    {
    Config.logError("Could not locate config/AWConfig/structures/included/ directory to load structures!");
    return;
    }
  File[] allFiles = directory.listFiles();
  File currentFile;
  for(int i = 0; i < allFiles.length; i++)
    {
    currentFile = allFiles[i];
    if(currentFile.isDirectory())
      {
      recursiveScan(currentFile, fileList);
      }
    else if(isProbableStructureFile(currentFile))
      {
      System.out.println("adding: "+currentFile+" to probableStructuresList");
      fileList.add(currentFile);
      }
    }
  }

private boolean isProbableStructureFile(File file)
  {
  return file.getName().toLowerCase().endsWith(".aws");
  }

private ProcessedStructure parseFile(File file)
  {  
  LoadedStructureRaw rawStruct = new LoadedStructureRaw(file);  
  if(!rawStruct.isValid)
    {
    return null;
    }
  ProcessedStructure struct = new ProcessedStructure(rawStruct);  
  return struct;
  }

public List<ProcessedStructure> processStructureFiles()
  {
  List<ProcessedStructure> structures = new ArrayList<ProcessedStructure>();
  ProcessedStructure struct;
  for(File file : probableStructureFiles)
    {
    struct = this.parseFile(file);
    if(struct!=null)
      {
      structures.add(struct);
      }
    }  
  return structures;
  }
}
