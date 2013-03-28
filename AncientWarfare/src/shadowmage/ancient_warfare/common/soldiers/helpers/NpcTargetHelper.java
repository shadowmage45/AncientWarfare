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
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.utils.Pair;

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
HashMap<String, AITargetList> targetEntries = new HashMap<String, AITargetList>();

/**
 * future/potential targets based on aggro priority
 */
HashMap<String, ArrayList<AIAggroEntry>> aggroEntries = new HashMap<String, ArrayList<AIAggroEntry>>();

public NpcTargetHelper(NpcBase npc)
  {
  this.npc = npc;
  }

public void addTargetEntry(String type, Class <? extends Entity> clz, int priority, boolean same, boolean opp)
  {  
  if(!this.targetEntries.containsKey(type))
    {
    this.targetEntries.put(type, new AITargetList(type, same, opp));
    }
  this.targetEntries.get(type).addTarget(clz, priority, true);
  }

public void addTargetEntry(String type, Class <? extends Entity> clz, int priority)
  {  
  if(!this.targetEntries.containsKey(type))
    {
    this.targetEntries.put(type, new AITargetList(type, false, false));
    }
  this.targetEntries.get(type).addTarget(clz, priority, true);
  }

public void addTargetEntryTileEntity(String type, Class <? extends TileEntity> clz, int priority, boolean same, boolean opp)
  {
  if(!this.targetEntries.containsKey(type))
    {
    this.targetEntries.put(type, new AITargetList(type, same, opp));
    }
  this.targetEntries.get(type).addTarget(clz, priority, false);
  }

public int getPriorityFor(String type, Entity ent)
  {
  if(this.targetEntries.containsKey(type))
    {
    return this.targetEntries.get(type).getPriorityFor(ent);
    }
  return -1;
  }

public void addOrUpdateAggroEntry(String type, int x, int y, int z, int aggroAmt, int priority)
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
    entries.add(new AIAggroEntry(x, y, z, priority).setAggro(aggroAmt).setType(type));
    }
  }

public void addOrUpdateAggroEntry(String type, Entity entity, int aggroAmt, int priority)
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
    entries.add(new AIAggroEntry(entity, priority).setAggro(aggroAmt).setType(type));
    }
  }

public void checkForTargets()
  {    
  float mr = (float)Config.npcAISearchRange;
  float dist = 0;
  AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(npc.posX-mr, npc.posY-mr, npc.posZ-mr, npc.posX-mr, npc.posY-mr, npc.posZ-mr);
  List<Entity> entityList = npc.worldObj.getEntitiesWithinAABBExcludingEntity(npc, bb);
  if(entityList!=null && !entityList.isEmpty())
    {
    Iterator<Entity> it = entityList.iterator();
    Entity ent;
    while(it.hasNext())
      {
      ent = it.next();      
      dist = npc.getDistanceToEntity(ent);
      if(dist>mr)
        {
        continue;
        }
      for(String key : this.targetEntries.keySet())        
        {
        int pri = this.getPriorityFor(key, ent);
        if(pri>=0)
          {
          this.addOrUpdateAggroEntry(key, ent, Config.npcAITicks, pri);
          }
        }     
      }
    }
  }

public void updateAggroEntries()
  {  
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

public AIAggroEntry getHighestAggroTarget(String type)
  {
  if(this.aggroEntries.containsKey(type))
    {
    List<AIAggroEntry> entries = this.aggroEntries.get(type);
    int lowest = 9999;    
    AIAggroEntry bestEntry = null;
    for(AIAggroEntry ent : entries)
      {
      if(ent.targetPriority<lowest)
        {        
        bestEntry = ent;
        }
      else if(ent.targetPriority==lowest)
        {
        if(ent.getDistanceFrom(npc)<bestEntry.getDistanceFrom(npc))
          {
          bestEntry = ent;
          }
        }
      }
    return bestEntry;
    }
  return null;
  }

public class AITargetList
{
String type;
boolean includeSameTeam = false;
boolean includeOppositeTeam = false;
ArrayList<AITargetEntry> targetEntries = new ArrayList<AITargetEntry>();

public AITargetList(String name, boolean sameTeam, boolean oppositeTeam)
  {
  this.type = name;
  this.includeSameTeam = sameTeam;
  this.includeOppositeTeam = oppositeTeam;
  }

public void addTarget(Class clz, int priority, boolean isEntity)
  {
  this.targetEntries.add(new AITargetEntry(clz, priority, isEntity));
  }

public int getPriorityFor(Entity ent)
  {
  boolean agg;
  if(ent instanceof NpcBase)
    {
    agg = npc.isAggroTowards((NpcBase)ent);
    if(agg && !this.includeOppositeTeam)
      {
      return -1;
      }
    else if(!agg && !this.includeSameTeam)
      {
      return -1;
      }
    }
  else if(ent instanceof EntityPlayer)
    {
    agg = npc.isAggroTowards((NpcBase)ent);
    if(agg && !this.includeOppositeTeam)
      {
      return -1;
      }
    else if(!agg && !this.includeSameTeam)
      {
      return -1;
      }
    }
  for(AITargetEntry entry : targetEntries)
    {
    if(entry.isTarget(ent))
      {
      return entry.priority;
      }
    }
  return -1;
  }

}

public class AITargetEntry
{  
Class entityClass = null;
public int priority = 0;
public boolean isEntityTarget = false;
public boolean isTileTarget = false;

public AITargetEntry(Class clz, int priority, boolean isEntityTarget)
  {
  this.entityClass = clz;
  this.priority = priority;
  this.isEntityTarget = isEntityTarget;
  }  

public boolean isTarget(Entity ent)
  {
  return entityClass.isAssignableFrom(ent.getClass());//ent.getClass().isAssignableFrom(entityClass);
  }    
}

public class AIAggroEntry
{
private WeakReference<Entity> ent = new WeakReference<Entity>(null);
public String targetType = "";
public int targetPriority;
public float posX;
public float posY;
public float posZ;
public int aggroLevel;
public boolean isEntityEntry = false;

public AIAggroEntry(Entity ent, int pri)
  {
  this.ent = new WeakReference<Entity>(ent);
  this.isEntityEntry = true;
  this.targetPriority = pri;
  } 

public AIAggroEntry(int x, int y, int z, int pri)
  {
  this.posX = x;
  this.posY = y;
  this.posZ = z;
  this.targetPriority = pri;
  }

public float getDistanceFrom(NpcBase npc)
  {
  if(!this.isEntityEntry)
    {
    return (float) npc.getDistance(posX, posY, posZ);
    }
  if(this.ent.get()!=null)
    {
    return npc.getDistanceToEntity(this.ent.get());
    }
  return 0;
  }

public AIAggroEntry setType(String type)
  {
  this.targetType = type;
  return this;
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
    if(npc.worldObj.getChunkProvider().chunkExists(((int)posX/16), ((int)posZ/16)))
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
  return this.posX == x && this.posY == y && this.posZ == z;
  }
}
}
