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
package shadowmage.ancient_warfare.common.npcs.ai.tasks;

import net.minecraft.entity.Entity;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAITask;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AIDismountVehicle extends NpcAITask
{

/**
 * @param npc
 */
public AIDismountVehicle(NpcBase npc)
  {
  super(npc);
  this.exclusiveTasks = MOVE_TO + ATTACK + REPAIR + HARVEST + HEAL + MOUNT_VEHICLE + FOLLOW + WANDER;
  }

@Override
public byte getTaskType()
  {
  return task_dismount;
  }

@Override
public void onTick()
  {  
  Config.logDebug("executing dismount task");
  npc.dismountVehicle();
  }

@Override
public boolean shouldExecute()
  {
  return npc.isRidingVehicle();
  }


}
