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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroList;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AITargetEntry;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AITargetList;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

/**
 * tracks a list of priority target types by class, as well as maintaining a list
 * of targets on the 'aggro list' for this targetHelper.  An NPC may have multiple
 * targetHelpers for each type of task (attack, repair, heal, restock, w/e)
 * @author Shadowmage
 *
 */
public class NpcTargetHelper
{

NpcBase npc;

/**
 * list of all potential targets
 */
HashMap<String, AITargetList> targetEntries = new HashMap<String, AITargetList>();

/**
 * future/potential targets based on aggro priority
 */
HashMap<String, AIAggroList> aggroEntries = new HashMap<String, AIAggroList>();

public NpcTargetHelper(NpcBase npc)
  {
  this.npc = npc;
  }

public void addTargetEntry(AITargetEntry entry)
  {
  if(!this.targetEntries.containsKey(entry.getTypeName()))
    {
    this.targetEntries.put(entry.getTypeName(), new AITargetList(npc, entry.getTypeName()));
    }
  this.targetEntries.get(entry.getTypeName()).addTarget(entry);
  }
  
public int getPriorityFor(String type, Entity ent)
  {
  if(this.targetEntries.containsKey(type))
    {
    return this.targetEntries.get(type).getPriorityFor(ent);
    }
  return -1;
  }

public void addOrUpdateAggroEntry(AITargetEntry type, int x, int y, int z, int aggroAmt)
  { 
  if(!this.aggroEntries.containsKey(type.getTypeName()))
    {
    this.aggroEntries.put(type.getTypeName(), new AIAggroList(npc, type.getTypeName()));
    }
  this.aggroEntries.get(type.getTypeName()).addOrUpdateEntry(x,y,z, aggroAmt, type);    
  }

public void addOrUpdateAggroEntry(AITargetEntry type, Entity entity, int aggroAmt)
  {
  if(!this.aggroEntries.containsKey(type.getTypeName()))
    {
    this.aggroEntries.put(type.getTypeName(), new AIAggroList(npc, type.getTypeName()));
    }
  this.aggroEntries.get(type.getTypeName()).addOrUpdateEntry(entity, aggroAmt, type);  
  }

public void checkForTargets()
  {    
//    Config.logDebug("checking for targets");
  float mr = (float)Config.npcAISearchRange;
  float dist = 0;
  AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(npc.posX-mr, npc.posY-mr, npc.posZ-mr, npc.posX+mr, npc.posY+mr, npc.posZ+mr);
  List<Entity> entityList = npc.worldObj.getEntitiesWithinAABBExcludingEntity(npc, bb);
//    Config.logDebug("entityList size: "+entityList.size());
  if(entityList!=null && !entityList.isEmpty())
    {
    Iterator<Entity> it = entityList.iterator();
    AITargetEntry targetEntry;
    Entity ent;
    while(it.hasNext())
      {
      ent = it.next();      
      dist = npc.getDistanceToEntity(ent);
      if(dist>mr || !npc.getEntitySenses().canSee(ent))
        {
        continue;
        }
//      Config.logDebug("checking entity: "+ent);
      for(String key : this.targetEntries.keySet())        
        {
        AITargetList targetList = this.targetEntries.get(key);
        targetEntry = targetList.getEntryFor(ent);
        if(targetEntry==null)
          {
          continue;
          }
        if(dist>targetEntry.maxTargetRange)
          {
          continue;
          }        
//        Config.logDebug("checking targets of type: "+key);
        int pri = targetEntry.priority;
        if(pri>=0)
          {
//                    Config.logDebug("adding/updating entity aggro entry for target: "+ent);
          float distPercent = 1.f - (dist / targetEntry.maxTargetRange);
          int aggroAmt = (int)(Config.npcAITicks + (distPercent * (float)Config.npcAITicks)); 
          this.addOrUpdateAggroEntry(targetEntry, ent, aggroAmt);
          }
        }     
      }
    }
  }

public float getAttackDistance(AIAggroEntry target)
  {
  if(npc.isRidingVehicle())
    {
    return ((VehicleBase)npc.ridingEntity).getEffectiveRange((float)npc.ridingEntity.posY - target.posY());
    }
  if(!target.isEntityEntry)//TODO should get yaw towards target, offset len by adj len of actual BB edge pos at that yaw
    {
    //return 0.25f;
    return 1.f + npc.width*0.5f;
    }
  else//is entity entry
    {
    if(target.getEntity()!=null)
      {
      return 2.5f * (npc.width*0.5f + target.getEntity().width*0.5f);
      }
    }
  return 4f;//fallthrough for entity null
  }

public void updateAggroEntries()
  {  
  for(String key : this.aggroEntries.keySet())
    {
    this.aggroEntries.get(key).updateAggroEntries();
    }  
  }

public void handleBeingAttacked(EntityLiving damager)
  {
  
  }

public AIAggroEntry getHighestAggroTarget(String type)
  {
  if(this.aggroEntries.containsKey(type))
    {
    return this.aggroEntries.get(type).getHighestAggroTarget();
    }
  return null;
  }

}
