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
package shadowmage.ancient_warfare.common.soldiers.ai.objectives;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.soldiers.ai.tasks.AIDismountVehicle;
import shadowmage.ancient_warfare.common.soldiers.helpers.NpcTargetHelper;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AIDismountVehicles extends NpcAIObjective
{

/**
 * @param npc
 * @param maxPriority
 */
public AIDismountVehicles(NpcBase npc, int maxPriority)
  {
  super(npc, maxPriority);
  }

@Override
public void updateObjectivePriority()
  {
  if(!npc.isRidingVehicle())
    {
//    Config.logDebug("updating dismount : not riding vehicle");
    this.objectiveTarget = null;
    this.currentPriority = 0;
    }
  else
    {
    AIAggroEntry target = npc.targetHelper.getHighestAggroTargetInRange(NpcTargetHelper.TARGET_ATTACK, ((VehicleBase)npc.ridingEntity).vehicleType.getMinAttackDistance());  
    if(target!=null)
      {
//      Config.logDebug("updating dismount targets "+target.toString());
      this.objectiveTarget = target;
      this.currentPriority = this.maxPriority;
      }
    else
      {
//      Config.logDebug("updating dismount targets : no target");
      this.objectiveTarget = null;
      this.currentPriority = 0;
      }
    }
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIDismountVehicle(npc));
  }

}
