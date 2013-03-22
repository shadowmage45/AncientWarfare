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
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.config.Config;
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
ArrayList<AIAggroEntry> aggroEntries = new ArrayList<AIAggroEntry>();

int tickCount = 0;

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

public void addOrUpdateAggroEntry(int x, int y, int z, int aggroAmt)
  {
  boolean found = false;
  for(AIAggroEntry ent : this.aggroEntries)
    {
    if(ent.matches(x, y, z))
      {
      found = true;
      ent.aggroLevel += aggroAmt;
      }
    }
  if(!found)
    {
    this.aggroEntries.add(new AIAggroEntry(x, y, z).setAggro(aggroAmt));
    }
  }

public void addOrUpdateAggroEntry(Entity entity, int aggroAmt)
  {
  boolean found = false;
  for(AIAggroEntry ent : this.aggroEntries)
    {
    if(ent.matches(entity))
      {
      found = true;
      ent.aggroLevel += aggroAmt;
      }
    }
  if(!found)
    {
    this.aggroEntries.add(new AIAggroEntry(entity).setAggro(aggroAmt));
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
  Iterator<AIAggroEntry> it = this.aggroEntries.iterator();
  AIAggroEntry entry;
  while(it.hasNext())
    {
    entry = it.next();
    entry.aggroLevel -= Config.npcAITicks;
    if(!entry.isValidEntry() || entry.aggroLevel<=0)
      {
      it.remove();
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
      if(this.ent.get()==null)
        {
        return true;
        }
      else
        {
        //get distance, if out of range, return false...
        }
      }
    return this.ent.get()!=null;
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
