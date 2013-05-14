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
package shadowmage.ancient_warfare.common.npcs.helpers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.helpers.targeting.AIAggroList;
import shadowmage.ancient_warfare.common.npcs.helpers.targeting.AIAggroTargetWrapper;
import shadowmage.ancient_warfare.common.npcs.helpers.targeting.AITargetEntry;
import shadowmage.ancient_warfare.common.npcs.helpers.targeting.AITargetList;
import shadowmage.ancient_warfare.common.targeting.TargetType;
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
HashMap<TargetType, AITargetList> targetEntries = new HashMap<TargetType, AITargetList>();

/**
 * future/potential targets based on aggro priority
 */
HashMap<TargetType, AIAggroList> aggroEntries = new HashMap<TargetType, AIAggroList>();

AITargetEntry revengeEntry;

public NpcTargetHelper(NpcBase npc)
  {
  this.npc = npc;
  revengeEntry = new AITargetEntry(npc, TargetType.ATTACK, Entity.class, 0, true, 140);
  }

public int getAggroAdjustmentFor(AIAggroTargetWrapper taget)
  {
  //TODO  
  return Config.npcAITicks;
  }

public float getMaxRangeFor(AIAggroTargetWrapper target)
  {
  //TODO //entry.targetEntry.maxTargetRange
  return Config.npcAISearchRange;
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

public void removeTarget(ITargetEntry target)
  {
  if(target!=null && this.aggroEntries.containsKey(target.getTargetType()))
    {
    this.aggroEntries.get(target.getTargetType()).removeEntry(target);
    }
  }

public boolean hasTargetsOfType(TargetType p)
  {
  if(this.aggroEntries.containsKey(p))
    {
    return this.aggroEntries.get(p).targetEntries.size()>0;
    }
  return false;
  }

public void handleTileEntityTargetBroadcast(TileEntity te, TargetType t, int aggroAmt)
  { 
  if(this.targetEntries.containsKey(t))
    {    
    AITargetList list = this.targetEntries.get(t);
    if(list!=null)
      {
      AITargetEntry entry = list.getEntryFor(te);
      if(entry!=null)
        {        
        this.addOrUpdateAggroEntry(entry, te, aggroAmt);
        }
      }
    }
  }

public void handleDimensionChange(int dim)
  {
  this.aggroEntries.clear();
  }

public void addOrUpdateAggroEntry(AITargetEntry type, TileEntity te, int aggroAmt)
  {
  if(!this.aggroEntries.containsKey(type.getTypeName()))
    {
    this.aggroEntries.put(type.getTypeName(), new AIAggroList(npc, type.getTypeName()));
    }
  this.aggroEntries.get(type.getTypeName()).addOrUpdateEntry(te, aggroAmt, type);  
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
//  long t = System.nanoTime();
  float mr = (float)Config.npcAISearchRange;
  float dist = 0;
  AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(npc.posX-mr, npc.posY-mr*0.5f, npc.posZ-mr, npc.posX+mr, npc.posY+mr*0.5f, npc.posZ+mr);
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
      if(dist>mr )//
        {
        continue;
        }
//      Config.logDebug("checking entity: "+ent);
      for(TargetType key : this.targetEntries.keySet())        
        {
//        Config.logDebug("checking targets of type: "+key);
        AITargetList targetList = this.targetEntries.get(key);
//        Config.logDebug("target list "+targetList );
        targetEntry = targetList.getEntryFor(ent);
//        Config.logDebug("target entry" + targetEntry);
        if(targetEntry==null)
          {
          continue;
          }
        if(dist>targetEntry.maxTargetRange || !npc.getEntitySenses().canSee(ent))
          {
//        	Config.logDebug("skipping due to vision or range "+targetEntry.maxTargetRange +" v: "+npc.getEntitySenses().canSee(ent));
          continue;
          }        
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
//  long t2 = System.nanoTime();
//  Config.logDebug("entity search took: "+(t2-t)+"ns");
  }

public float getAttackDistance(ITargetEntry target)
  {
	if(target==null)
    {
    return 4.f;
    }
  if(npc.isRidingVehicle())
    {
    return ((VehicleBase)npc.ridingEntity).getEffectiveRange((float)npc.ridingEntity.posY - target.posY());
    }  
  Entity ent = target.getEntity(npc.worldObj);
  if(ent==null)
    {
    return 1.f + npc.width*0.5f;
    }
  else//is entity entry
    {
    float rangedDistance = npc.npcType.getRangedAttackDistance(npc.rank);
    if(rangedDistance>0)
      {
      return rangedDistance;
      }
    return 2.8f * (npc.width*0.5f + target.getEntity(npc.worldObj).width*0.5f);
    }
  }

public void updateAggroEntries()
  {  
  for(TargetType key : this.aggroEntries.keySet())
    {
    this.aggroEntries.get(key).updateAggroEntries();
    }  
  }

public void handleBeingAttacked(EntityLiving damager)
  {
  if(this.targetEntries.containsKey(TargetType.ATTACK))
    {
    this.addOrUpdateAggroEntry(revengeEntry, damager, Config.npcAITicks * 4);
    }
  }

public void handleBroadcastTarget(Entity ent, TargetType type, int multi)
  {
  if(this.targetEntries.containsKey(type))
    {
    AITargetList list = this.targetEntries.get(type);
    AITargetEntry entry = list.getEntryFor(ent);
    if(entry!=null)
      {
      this.addOrUpdateAggroEntry(entry, ent, Config.npcAITicks * multi);
      }
    }
  }

public boolean isValidTarget(Class clz)
  {
  return false;
  }

public boolean areTargetsInRange(TargetType type, float range)
  {
  if(this.aggroEntries.containsKey(type))
    {
//    Config.logDebug("target helper has list of type: "+type+" checking if targets in range from list");
    return this.aggroEntries.get(type).areTargetsInRange(range);
    }
  return false;
  }

public ITargetEntry getHighestAggroTargetInRange(TargetType type, float range)
  {
  if(this.aggroEntries.containsKey(type))
    {
    return this.aggroEntries.get(type).getHighestAggroTargetInRange(range);
    }
  return null;
  }

public ITargetEntry getHighestAggroTarget(TargetType type)
  {
  if(this.aggroEntries.containsKey(type))
    {
    return this.aggroEntries.get(type).getHighestAggroTarget();
    }
  return null;
  }

public ITargetEntry getClosestTargetInRange(TargetType type, float range)
  {
  //TODO
  return null;
  }

}
