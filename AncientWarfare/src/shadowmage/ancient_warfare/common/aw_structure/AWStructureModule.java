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
import java.util.List;

/**
 * config/setup module for structures and structure related stuff....
 * @author Shadowmage
 *
 */
public class AWStructureModule
{

/**
 * file path for reading structures
 */
public static String dir;

/**
 * called probableStructureFiles because they haven't been opened/read/checked for validity
 */
private List<File> probableStructureFiles = new ArrayList<File>();


private AWStructureModule(){}
public AWStructureModule INSTANCE;
public AWStructureModule instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE= new AWStructureModule();
    }
  return INSTANCE;
  }

public static void setFileDirectory(File location)
  {
  try
    {
    dir = location.getCanonicalPath()+"/AWConfig/structures/";
    } 
  catch (IOException e)
    {
    e.printStackTrace();
    }
  }

public void scanForPrebuiltFiles()
  {
  probableStructureFiles.clear();
  this.recursiveScan(new File(dir), probableStructureFiles);
  }

private void recursiveScan(File directory, List<File> fileList)
  {
  File[] allFiles = directory.listFiles();
  File currentFile;
  for(int i = 0; i < allFiles.length; i++)
    {
    currentFile = allFiles[i];
    if(currentFile.isDirectory())
      {
      recursiveScan(directory, fileList);
      }
    else if(isProbableStructureFile(currentFile))
      {
      fileList.add(currentFile);
      }
    }
  }

public boolean isProbableStructureFile(File file)
  {
  return file.getName().endsWith(".dat") && file.getName().startsWith("AWStruct_");
  }

}
