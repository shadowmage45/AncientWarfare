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
package shadowmage.ancient_warfare.common.npcs.ai.objectives;

import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMountVehicle;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.targeting.TargetPositionEntity;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AIMountVehicles extends NpcAIObjective
{

int maxRange;
/**
 * @param npc
 * @param maxPriority
 */
public AIMountVehicles(NpcBase npc, int maxPriority, int maxRange)
  {
  super(npc, maxPriority);
  this.maxRange = maxRange;
  }

@Override
public byte getObjectiveNum()
  {
  return mount;
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 1.f, true));
  this.aiTasks.add(new AIMountVehicle(npc));
  }

@Override
public void updatePriority()
  {
  if(npc.ridingEntity!=null)
    {
    this.currentPriority = 0;
    }
  else if(npc.wayNav.getMountTarget()!=null && !npc.wayNav.getMountTarget().isDead && npc.wayNav.getMountTarget().riddenByEntity==null && npc.wayNav.getMountTarget().assignedRider==npc)
    {
    this.currentPriority = this.maxPriority;
    }
  else if(npc.targetHelper.areTargetsInRange(TargetType.MOUNT, maxRange))
    {
    this.currentPriority = this.maxPriority;
    }
  else
    {
    this.currentPriority = 0;
    }  
  }

@Override
public void onRunningTick()
  {
  if(npc.getTarget()==null)
    {    
    if(npc.wayNav.getMountTarget()!=null || npc.targetHelper.areTargetsInRange(TargetType.MOUNT, maxRange))
      {
      setMountTarget();      
      }
    else
      {
      this.setFinished();
      }
    }
  }

@Override
public void onObjectiveStart()
  {
  setMountTarget();
  }

@Override
public void stopObjective()
  {
  npc.clearPath();
  npc.setTargetAW(null);  
  }

protected void setMountTarget()
  {
  if(npc.wayNav.getMountTarget()!=null)
    {
    npc.setTargetAW(new TargetPositionEntity(npc.wayNav.getMountTarget(), TargetType.MOUNT));
    }
  else
    {
    ITargetEntry vehicleEntry  = npc.targetHelper.getHighestAggroTargetInRange(TargetType.MOUNT, maxRange);
    if(vehicleEntry.getEntity(npc.worldObj) instanceof VehicleBase)
      {
      VehicleBase vehicle = (VehicleBase)vehicleEntry.getEntity(npc.worldObj);
      npc.setTargetAW(vehicleEntry);
      npc.wayNav.setMountTarget(vehicle);
      vehicle.assignedRider = npc;
      }
    }  
  }

}
