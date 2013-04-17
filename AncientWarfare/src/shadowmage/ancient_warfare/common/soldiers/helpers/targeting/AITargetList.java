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
package shadowmage.ancient_warfare.common.soldiers.helpers.targeting;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.utils.TargetType;

public class AITargetList
{	
TargetType type;
ArrayList<AITargetEntry> targetEntries = new ArrayList<AITargetEntry>();
protected NpcBase npc;

public AITargetList(NpcBase owner, TargetType name)
  {
  this.npc = owner;
  this.type = name;
  }

public void addTarget(AITargetEntry entry)
  {
  this.targetEntries.add(entry);
  }

public AITargetEntry getEntryFor(Entity ent)
  {  
  for(AITargetEntry entry : targetEntries)
    {
    if(entry.isTarget(ent))
      {
      return entry;
      }
    }
  return null;
  }

public int getPriorityFor(Entity ent)
  {
  AITargetEntry entry = getEntryFor(ent);
  if(entry!=null)
    {
    return entry.priority;
    }
  return -1;
  }
}
