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
package shadowmage.ancient_warfare.common.soldiers.helpers;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;

public class NpcTargetHelper
{

NpcBase npc;

/**
 * list of all potential targets
 */
ArrayList<AITargetEntry> targetEntries = new ArrayList<AITargetEntry>();

/**
 * future/potential targets based on aggro priority -- mapped by entityID
 */
HashMap<Integer, AIAggroEntry> aggroEntries = new HashMap<Integer, AIAggroEntry>();

public NpcTargetHelper(NpcBase npc)
  {
  this.npc = npc;
  }

public void addTargetEntry(Class <? extends Entity> clz, int priority)
  {
  this.targetEntries.add(new AITargetEntry(clz, priority));
  }

public int getPriorityFor(Entity ent)
  {
  for(AITargetEntry entry : this.targetEntries)
    {
    if(entry.isTarget(ent))
      {
      return entry.priority;
      }
    }
  return -1;
  }

public void addOrUpdateAggroEntry(Entity ent, int aggroAmt)
  {
  if(!this.aggroEntries.containsKey(ent.entityId))
    {
    this.aggroEntries.put(ent.entityId, new AIAggroEntry(ent));
    }
  this.aggroEntries.get(ent.entityId).aggroLevel +=aggroAmt;
  }

public void updateAggroEntries()
  {
  Iterator<Entry<Integer, AIAggroEntry>> entryIt = aggroEntries.entrySet().iterator();  
  Entry<Integer, AIAggroEntry> entry;
  AIAggroEntry aiEntry;
  while(entryIt.hasNext())
    {
    entry = entryIt.next();
    aiEntry = entry.getValue();
    if(!aiEntry.isValidEntry())
      {
      entryIt.remove();
      }
    else
      {      
      aiEntry.aggroLevel--;
      if(aiEntry.aggroLevel<=0)
        {
        entryIt.remove();
        }
      }
    }
  }

public class AITargetEntry
  {  
  Class entityClass = null;
  public int priority = 0;
  public AITargetEntry(Class <? extends Entity> clz, int priority)
    {
    this.entityClass = clz;
    this.priority = priority;
    }  
  
  public boolean isTarget(Entity ent)
    {
    return entityClass.isAssignableFrom(ent.getClass());//ent.getClass().isAssignableFrom(entityClass);
    }
  }

public class AIAggroEntry
  {
  WeakReference<Entity> ent;
  public int aggroLevel;
  public AIAggroEntry(Entity ent)
    {
    this.ent = new WeakReference<Entity>(ent);
    } 
  
  public boolean isValidEntry()
    {
    return this.ent.get()!=null;
    }
  
  public boolean matches(Entity ent)
    {
    return this.ent.get() != null && this.ent.get()==ent;
    }
  }

}
