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
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;

public class AIAggroList
{

protected NpcBase npc;
public final int targetType;
public ArrayList<AIAggroEntry> targetEntries = new ArrayList<AIAggroEntry>();

public AIAggroList(NpcBase owner, int targetType)
  {
  this.npc = owner;
  this.targetType = targetType;
  }

public void addOrUpdateEntry(Entity ent, int aggroAmt, AITargetEntry type)
  {  
  for(AIAggroEntry entry : this.targetEntries)
    {
    if(entry.matches(ent))
      {      
      entry.aggroLevel+=aggroAmt;
      return;
      }
    }
  this.targetEntries.add(new AIAggroEntry(npc, type, ent).setAggro(aggroAmt));
  }

public void addOrUpdateEntry(int x, int y, int z, int aggroAmt, AITargetEntry type)
  {
  for(AIAggroEntry entry : this.targetEntries)
    {
    if(entry.matches(x,y,z))
      {      
      entry.aggroLevel+=aggroAmt;
      return;
      }
    }
  this.targetEntries.add(new AIAggroEntry(npc, type, x, y, z).setAggro(aggroAmt));
  }

public AIAggroEntry getEntryFor(Entity ent)
  {
  for(AIAggroEntry entry : this.targetEntries)
    {
    if(entry.matches(ent))
      {
      return entry;
      }
    }
  return null;
  }

public boolean areTargetsInRange(float range)
  {
//  Config.logDebug("checking for targets in range: "+range+" type: "+this.targetType);
  for(AIAggroEntry entry : this.targetEntries)
    {
    
//    float dist = npc.getDistanceFromTarget(entry);
//    Config.logDebug("testing :"+entry+" at range: "+dist);
    if(npc.getDistanceFromTarget(entry) < range)
      {
      return true;
      }    
    }
  return false;
  }

public AIAggroEntry getHighestAggroTargetInRange(float range)
  {
  AIAggroEntry bestEntry = null;
  for(AIAggroEntry entry : this.targetEntries)
    {
    if(npc.getDistanceFromTarget(entry)>range)
      {
      continue;
      }
    if(entry.isValidEntry() && (bestEntry==null || entry.aggroLevel > bestEntry.aggroLevel))
      {
      bestEntry = entry;
      }
    }
  return bestEntry;
  }

public AIAggroEntry getHighestAggroTarget()
  {  
  AIAggroEntry bestEntry = null;
//  Config.logDebug("getting highest aggro target for: "+this.targetType);
  for(AIAggroEntry entry : this.targetEntries)
    {
//    Config.logDebug("examining entry: "+entry.toString());
    if(entry.isValidEntry() && (bestEntry==null || entry.aggroLevel > bestEntry.aggroLevel))
      {
//      Config.logDebug("new best found:");
      bestEntry = entry;
      }
    }
  return bestEntry;
  }

public void updateAggroEntries()
  {
  Iterator<AIAggroEntry> it = this.targetEntries.iterator();
//  Config.logDebug("updating target entries for: "+this.targetType);
  AIAggroEntry entry;
  float maxRange;
  while(it.hasNext())
    {
    entry = it.next();
//    Config.logDebug("examining entry: "+entry.toString());
    if(!entry.isValidEntry() || entry.getDistanceFrom()>entry.targetType.maxTargetRange || entry.aggroLevel <= 0)
      {
//      Config.logDebug("removing entry:");
      it.remove();
      return;
      }   
    entry.aggroLevel -= entry.targetType.getAggroAdjustment(entry);
    } 
  }

}
