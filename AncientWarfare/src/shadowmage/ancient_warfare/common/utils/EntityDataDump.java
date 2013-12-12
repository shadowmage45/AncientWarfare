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
package shadowmage.ancient_warfare.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityList;
import shadowmage.ancient_framework.common.config.Config;

public class EntityDataDump
{

/**
 * 
 */
public EntityDataDump()
  {
  // TODO Auto-generated constructor stub
  }

public static void dumpEntityData()
  {
  Config.logDebug("dumping entity data to file");
  List<String> entityNames = new ArrayList<String>();
  String name;
  for(Object obj : EntityList.classToStringMapping.values())
    {
    entityNames.add((String)obj);
    }
  String path = Config.configPath+"/AWConfig/entityDump.txt";
  File file = new File(path);
  try
    {
    if(file.exists())
      {
      file.delete();
      file.createNewFile();
      }
    FileWriter writer = new FileWriter(file);
    writer.write("---Entity Name Dump---\r\n");
    for(String st : entityNames)
      {
      writer.write(st+"\r\n");
      }
    writer.close();
    } 
  catch (IOException e)
    {  
    Config.logError("Error exporting entity name dump -- could not instantiate file");
    e.printStackTrace();
    }
  }

}
