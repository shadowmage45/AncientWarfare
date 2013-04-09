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

import net.minecraft.entity.passive.EntityVillager;
import shadowmage.ancient_warfare.common.registry.NpcRegistry;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.structures.data.ScannedEntityEntry;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class NpcRule
{
int bx;
int by;
int bz;
float ox;
float oy;
float oz;
float rotation;
float pitch;
public short npcType;
public short npcRank;

public static NpcRule populateRule(ScannedEntityEntry entry, EntityVillager villager)
  {  
  NpcRule rule = new NpcRule();
  populateBasics(rule, entry);
  rule.npcType = (short) NpcRegistry.npcVillager.getGlobalNpcType();
  rule.npcRank = (short) villager.getProfession();
  return rule;
  }

public static NpcRule populateRule(ScannedEntityEntry entry, NpcBase npc)
  {
  NpcRule rule = new NpcRule();
  populateBasics(rule, entry);
  rule.npcType = (short) npc.npcType.getGlobalNpcType();
  rule.npcRank = (short) npc.rank;
  return rule;
  }

public static NpcRule parseRule(List<String> ruleLines)
  {
  NpcRule rule = new NpcRule();
  String line;
  Iterator<String> it = ruleLines.iterator();
  while(it.hasNext())
    {
    line = it.next();   
    if(line.toLowerCase().startsWith("type"))
      {
      rule.npcType = StringTools.safeParseShort("=", line);     
      }
    else if(line.toLowerCase().startsWith("rank"))
      {
      rule.npcRank = StringTools.safeParseShort("=", line);  
      }
    else if(line.toLowerCase().startsWith("bx"))
      {
      rule.bx = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("by"))
      {
      rule.by = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("bz"))
      {
      rule.bz = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("ox"))
      {
      rule.ox = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("oy"))
      {
      rule.oy = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("oz"))
      {
      rule.oz = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("rotation"))
      {
      rule.rotation = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("pitch"))
      {
      rule.pitch = StringTools.safeParseFloat("=", line);
      }
    } 
  return rule;
  }

private static NpcRule populateBasics(NpcRule rule, ScannedEntityEntry entry)
  {
  rule.bx = entry.bx;
  rule.by = entry.by;
  rule.bz = entry.bz;
  rule.ox = entry.ox;
  rule.oy = entry.oy;
  rule.oz = entry.oz;
  rule.rotation = entry.r;
  rule.pitch = entry.p;
  return rule;
  }

public List<String> getRuleLines()
  {
  ArrayList<String> lines = new ArrayList<String>();
  lines.add("npc:");
  lines.add("type="+this.npcType);
  lines.add("rank="+this.npcRank);
  lines.add("bx="+bx);
  lines.add("by="+by);
  lines.add("bz="+bz);
  lines.add("ox="+ox);
  lines.add("oy="+oy);
  lines.add("oz="+oz);
  lines.add("rotation="+rotation);
  lines.add("pitch="+pitch);
  lines.add(":endnpc");
  return lines;
  }

}
