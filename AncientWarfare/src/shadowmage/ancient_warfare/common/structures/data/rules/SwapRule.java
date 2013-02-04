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
package shadowmage.ancient_warfare.common.structures.data.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.structures.data.BlockData;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class SwapRule
{


public class SwapEntry
{

public SwapEntry(String biome, int id, int meta, int rID, int rMeta)
  {
  this.biomeName = biome;
  this.sourceID = new BlockData(id, meta);
  this.resultID = new BlockData(rID, rMeta);
  }
String biomeName;
BlockData sourceID;
BlockData resultID;
}


public int ruleNumber;
ArrayList<SwapEntry> entries = new ArrayList<SwapEntry>();


private SwapRule()
  {
  
  }

public SwapRule(String name, int ruleNum, int id, int meta, int resultID, int resultMeta)
  {
  this.ruleNumber = ruleNum;
  this.entries.add(new SwapEntry(name, id, meta, resultID, resultMeta));
  }

public static SwapRule parseRule(List<String> lines)
  {
  try
    {
    Iterator<String> it = lines.iterator();
    String line;
    SwapRule rule = new SwapRule();
    while(it.hasNext())
      {
      line = it.next();
      if(line.toLowerCase().startsWith("number"))
        {
        rule.ruleNumber = StringTools.safeParseInt("=", line);      
        }
      if(line.toLowerCase().startsWith("biome"))
        {
        String split = StringTools.safeParseString("=", line);
        String[] splits = split.split(",");
        if(splits.length<3)
          {
          return null;
          }
        String name = splits[0];
        int id;
        int meta = 0;
        int rid;
        int rmt = 0;
        String[] sp1 = splits[1].split("-");
        String[] sp2 = splits[2].split("-");
        id = Integer.parseInt(sp1[0]);
        rid = Integer.parseInt(sp2[0]);
        if(sp1.length>1)
          {
          meta = Integer.parseInt(sp1[1]);
          }
        if(sp2.length>1)
          {
          rmt = Integer.parseInt(sp2[1]);
          }
        rule.addNewSwapEntry(name, id, meta, rid, rmt);      
        }
      }
    return rule;
    }
  catch(Exception e)
    {
    Config.logError("Error parsing biome rule");
    e.printStackTrace();
    return null;
    }  
  }

private void addNewSwapEntry(String name, int id, int meta, int rid, int rmt)
  {
  this.entries.add(new SwapEntry(name, id, meta, rid, rmt));
  }

public BlockData getSwappedData(String name, BlockData sourceID)
  {
  for(SwapEntry entry : this.entries)
    {
    if(entry.biomeName.equalsIgnoreCase(name) && entry.sourceID.id == sourceID.id && entry.sourceID.meta == sourceID.meta)
      {
      return entry.resultID;
      }
    }  
  return sourceID;
  }

public List<String> getExportData()
  {
  List<String> lines= new ArrayList<String>();
  lines.add("swap:\n");
  lines.add("number="+String.valueOf(this.ruleNumber)+"\n");
  for(SwapEntry entry : this.entries)
    {
    lines.add(String.valueOf("biome="+entry.biomeName+","+entry.sourceID.id+"-"+entry.sourceID.meta+","+entry.resultID.id+"-"+entry.resultID.meta+"\n"));
    }  
  lines.add(":endswap\n");
  return lines;
  }


}
