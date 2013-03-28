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
import net.minecraft.entity.player.EntityPlayer;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;

/**
 * tracks a list of priority target types by class, as well as maintaining a list
 * of targets on the 'aggro list' for this targetHelper.  An NPC may have multiple
 * targetHelpers for each type of task (attack, repair, heal, restock, w/e)
 * @author Shadowmage
 *
 */
public class NpcTargetHelper
{


Class entityClass = EntityPlayer.class;
Class npcClass = NpcBase.class;

NpcBase npc;

/**
 * list of all potential targets
 */
HashMap<String, ArrayList<AITargetEntry>> targetEntries = new HashMap<String, ArrayList<AITargetEntry>>();

/**
 * future/potential targets based on aggro priority
 */
HashMap<String, ArrayList<AIAggroEntry>> aggroEntries = new HashMap<String, ArrayList<AIAggroEntry>>();

int tickCount = 0;

public NpcTargetHelper(NpcBase npc)
  {
  this.npc = npc;
  }

public void addTargetEntry(String type, Class <? extends Entity> clz, int priority)
  {  
  if(!this.targetEntries.containsKey(type))
    {
    this.targetEntries.put(type, new ArrayList<AITargetEntry>());
    }
  this.targetEntries.get(type).add(new AITargetEntry(clz, priority));
  }

public int getPriorityFor(String type, Entity ent)
  {
  if(this.targetEntries.containsKey(type))
    {
    ArrayList<AITargetEntry> targets = this.targetEntries.get(type);
    for(AITargetEntry entry : targets)
      {
      if(entry.isTarget(ent))
        {
        return entry.priority;
        }
      }
    }
  return -1;
  }

public void addOrUpdateAggroEntry(String type, int x, int y, int z, int aggroAmt)
  {
  boolean found = false;
 
  if(!this.aggroEntries.containsKey(type))
    {
    this.aggroEntries.put(type, new ArrayList<AIAggroEntry>());
    }
  ArrayList<AIAggroEntry> entries = this.aggroEntries.get(type);
  for(AIAggroEntry ent : entries)
    {
    if(ent.matches(x, y, z))
      {
      found = true;
      ent.aggroLevel += aggroAmt;
      }
    }
  if(!found)
    {
    entries.add(new AIAggroEntry(x, y, z).setAggro(aggroAmt));
    }
  }

public void addOrUpdateAggroEntry(String type, Entity entity, int aggroAmt)
  {
  boolean found = false;
  
  if(!this.aggroEntries.containsKey(type))
    {
    this.aggroEntries.put(type, new ArrayList<AIAggroEntry>());
    }
  ArrayList<AIAggroEntry> entries = this.aggroEntries.get(type);
  for(AIAggroEntry ent : entries)
    {
    if(ent.matches(entity))
      {
      found = true;
      ent.aggroLevel += aggroAmt;
      }
    }
  if(!found)
    {
    entries.add(new AIAggroEntry(entity).setAggro(aggroAmt));
    }
  }

public void updateAggroEntries()
  {
  tickCount++;
  if(tickCount < Config.npcAITicks)
    {
    return;
    }
  tickCount = 0;
  Iterator<Entry<String, ArrayList<AIAggroEntry>>> it = this.aggroEntries.entrySet().iterator();
  Entry<String, ArrayList<AIAggroEntry>> mapEntry;
  Iterator<AIAggroEntry> listIt;
  ArrayList<AIAggroEntry> list;
  AIAggroEntry entry;
  while(it.hasNext())
    {
    mapEntry = it.next();
    list = mapEntry.getValue();
    listIt = list.iterator();
    while(listIt.hasNext())
      {
      entry = listIt.next();
      entry.aggroLevel -= Config.npcAITicks;
      if(!entry.isValidEntry() || entry.aggroLevel<=0)
        {
        it.remove();
        }
      } 
    }
  }

public AIAggroEntry getHighestAggroTarget()
  {

  return null;
  }

public class AITargetEntry
{  
Class entityClass = null;
public int priority = 0;
public boolean isEntityTarget = false;
public AITargetEntry(Class <? extends Entity> clz, int priority)
  {
  this.entityClass = clz;
  this.priority = priority;
  this.isEntityTarget = true;
  }  

public boolean isTarget(Entity ent)
  {
  return entityClass.isAssignableFrom(ent.getClass());//ent.getClass().isAssignableFrom(entityClass);
  }    
}

public class AIAggroEntry
{
private WeakReference<Entity> ent = new WeakReference<Entity>(null);
public int blockX;
public int blockY;
public int blockZ;
public int aggroLevel;
public boolean isEntityEntry = false;
public AIAggroEntry(Entity ent)
  {
  this.ent = new WeakReference<Entity>(ent);
  this.isEntityEntry = true;
  } 

public AIAggroEntry(int x, int y, int z)
  {
  this.blockX = x;
  this.blockY = y;
  this.blockZ = z;
  }

public AIAggroEntry setAggro(int aggro)
  {
  this.aggroLevel = aggro;
  return this;
  }

public Entity getEntity()
  {
  return ent.get();
  }

public boolean isValidEntry()
  {
  if(!this.isEntityEntry)
    {
    if(npc.worldObj.getChunkProvider().chunkExists(blockX/16, blockZ/16))
      {
      //TODO check and see if the block at x,y,z is still a valid target block (enemy priority block)
      }
    else
      {
      return false;
      }
    }
  else
    {
    if(this.ent.get()!=null)
      {
      //get distance, if out of range, return false...
      return true;
      }
    else
      {
      return false;        
      }
    }
  return false;
  }

public boolean matches(Entity ent)
  {
  return this.ent.get() != null && this.ent.get()==ent;
  }

public boolean matches(int x, int y, int z)
  {
  return this.blockX == x && this.blockY == y && this.blockZ == z;
  }
}
}
