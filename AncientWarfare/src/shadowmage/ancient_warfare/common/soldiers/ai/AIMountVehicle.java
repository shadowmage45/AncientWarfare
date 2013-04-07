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
package shadowmage.ancient_warfare.common.soldiers.ai;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcAI;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AIMountVehicle extends NpcAI
{

/**
 * @param npc
 */
public AIMountVehicle(NpcBase npc)
  {
  super(npc);
  this.successTicks = 200;
  this.failureTicks = 10;
  this.taskName = "MountVehicle";
  this.taskType = MOUNT_VEHICLE;
  this.exclusiveTasks = NONE;
  }

@Override
public void onAiStarted()
  {
  // TODO Auto-generated method stub
  }

@Override
public void onTick()
  {  
  if(npc.ridingEntity!=null)
    {
    finished = true;
    return;
    }
  if(npc.getTarget()==null || !npc.getTargetType().equals(NpcAI.TARGET_MOUNT))
    {
    finished = true;
    return;
    }
  if(npc.getTarget().getDistanceFrom()>npc.targetHelper.getAttackDistance(npc.getTarget()))
    {
    finished = true;
    return;
    }
  if(npc.getTarget().isValidEntry())
    {
    Config.logDebug("ai mount tick");
    VehicleBase vehicle = (VehicleBase)npc.getTarget().getEntity();
    npc.mountEntity(vehicle);
    this.success = true;
    this.finished = true;
    }
  else
    {
    finished = true;   
    }
  }

@Override
public boolean shouldExecute(NpcBase npc)
  {
  return npc.getTargetType().equals(NpcAI.TARGET_MOUNT) && npc.ridingEntity==null;
  }

}
