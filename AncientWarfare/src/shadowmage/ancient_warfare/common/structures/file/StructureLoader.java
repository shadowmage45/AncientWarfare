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
package shadowmage.ancient_warfare.common.structures.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import shadowmage.ancient_warfare.common.AWStructureModule;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.structures.data.LoadedStructureRaw;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;

public class StructureLoader
{

public StructureLoader()
  {
 
  }

/**
 * called probableStructureFiles because they haven't been opened/read/checked for validity
 */
private List<File> probableStructureFiles = new ArrayList<File>();
private List<File> probableRuinsFiles = new ArrayList<File>();


public void scanForPrebuiltFiles()
  {
  probableStructureFiles.clear();  
  this.recursiveScan(new File(AWStructureModule.includeDirectory), probableStructureFiles, ".aws");  
  this.recursiveScan(new File(AWStructureModule.convertDirectory), probableRuinsFiles, ".tml");
  }

private void recursiveScan(File directory, List<File> fileList, String extension)
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
      recursiveScan(currentFile, fileList, extension);
      }
    else if(isProbableFile(currentFile, extension))
      {
      fileList.add(currentFile);
      }
    }
  }

private boolean isProbableFile(File file, String extension)
  {
  return file.getName().toLowerCase().endsWith(extension);
  }

private List<ProcessedStructure> processFilesFor(List<File> fileList)
  {
  List<ProcessedStructure> structures = new ArrayList<ProcessedStructure>();
  ProcessedStructure struct;
  for(File file : fileList)
    {
    struct = this.processFile(file);
    if(struct!=null)
      {
      structures.add(struct);
      }
    }  
  return structures;  
  }

private LoadedStructureRaw processFile(File file)
  {
  Scanner reader = null;
  try
    {
    reader = new Scanner(new FileInputStream(file));
    } 
  catch (FileNotFoundException e)
    {    
    Config.logError("There was an error while parsing template file: "+file.getName()); 
    e.printStackTrace();    
    return null;
    }
  List<String> lines = new ArrayList<String>();
  String line;
  /**
   * throw everything into a list, close the file
   */
  while(reader.hasNextLine())
    {
    line = reader.nextLine();
    if(line.startsWith("#"))//skip comment lines entirely, no need to parse later
      {
      continue;
      }
    lines.add(line);
    }  
  reader.close();
  
  LoadedStructureRaw struct = null;
  /**
   * process from a nice in-memory list
   */
  try
    {
    if(file.getName().endsWith(".aws"))
      {
      struct = new LoadedStructureRaw(lines, false);
      }
    else
      {
      struct = new LoadedStructureRaw(lines, true);
      }
    }
  catch(Exception e)
    {    
    Config.logError("There was an error while parsing template file: "+file.getName()); 
    return null;
    }
  if(struct !=null && !struct.isValid)
    {
    Config.logError("There was an error while parsing template file: "+file.getName()); 
    return null;
    }
  return struct;
  }

public List<ProcessedStructure> processStructureFiles()
  {
  return processFilesFor(probableStructureFiles);
  }

public void convertRuinsTemplates()
  {
  for(File file : this.probableRuinsFiles)
    {
    String name = file.getName();
    name = name.split(".tml")[0];
    String newFile = String.valueOf(AWStructureModule.outputDirectory+name+".aws");
    
    LoadedStructureRaw raw = processFile(file);
    if(raw==null || !raw.isValid)
      {
      continue;
      }    
    if(!StructureExporterRuins.writeStructureToFile(raw, newFile))
      {
      continue;
      }       
    String renamedName = file.getName()+".cvt";
    File renamedFile = new File(AWStructureModule.convertDirectory+renamedName);
    file.renameTo(renamedFile);
    }
  }

}
