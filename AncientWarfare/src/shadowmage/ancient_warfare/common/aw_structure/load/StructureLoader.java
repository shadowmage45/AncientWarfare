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
import shadowmage.ancient_warfare.common.aw_structure.AWStructureModule;
import shadowmage.ancient_warfare.common.aw_structure.data.LoadedStructureRaw;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;

public class StructureLoader
{

public StructureLoader()
  {
 
  }

/**
 * called probableStructureFiles because they haven't been opened/read/checked for validity
 */
private List<File> probableStructureFiles = new ArrayList<File>();

private List<File> probableTempStructureFiles = new ArrayList<File>();

public void scanForPrebuiltFiles()
  {
  probableStructureFiles.clear();  
  this.recursiveScan(new File(AWStructureModule.includeDirectory), probableStructureFiles);  
  }

public void scanForTempFiles()
  {
  probableTempStructureFiles.clear();
  this.recursiveScan(new File(AWStructureModule.playerTempDirectory), probableTempStructureFiles);
  }

private void recursiveScan(File directory, List<File> fileList)
  {
  if(directory==null)
    {
    Config.logError("Could not locate "+directory+" directory to load structures!");
    return;
    }
  File[] allFiles = directory.listFiles();
  if(allFiles==null)
    {
    Config.logError("Could not locate "+directory+" directory to load structures!--no files in directory file list!");
    return;
    }
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
    Config.logError("Structure parser returned invalid structure, there is a problem with the template file: "+file.getName());
    return null;
    }    
  return rawStruct;
  }

private List<ProcessedStructure> processFilesFor(List<File> fileList)
  {
  List<ProcessedStructure> structures = new ArrayList<ProcessedStructure>();
  ProcessedStructure struct;
  for(File file : fileList)
    {
    struct = this.parseFile(file);
    if(struct!=null)
      {
      structures.add(struct);
      }
    }  
  return structures;  
  }


public List<ProcessedStructure> processTempFiles()
  {
  return processFilesFor(probableTempStructureFiles);
  }

public List<ProcessedStructure> processStructureFiles()
  {
  return processFilesFor(probableStructureFiles);
  }

/**
 * debug method, used to set temp building for debug builder
 * @param file
 * @return
 */
@Deprecated
public static ProcessedStructure processSingleStructure(File file)
  {
  LoadedStructureRaw rawStruct = new LoadedStructureRaw(file);  
  if(!rawStruct.isValid)
    {
    Config.logError("Structure parser returned invalid structure, there is a problem with the template file: "+file.getName());
    return null;
    }
  return rawStruct;
  }

}
