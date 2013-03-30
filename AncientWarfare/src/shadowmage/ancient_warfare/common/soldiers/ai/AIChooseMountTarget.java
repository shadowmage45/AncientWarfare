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

import shadowmage.ancient_warfare.common.soldiers.NpcAI;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.soldiers.helpers.targeting.AIAggroEntry;

public class AIChooseMountTarget extends NpcAI
{

/**
 * @param npc
 */
public AIChooseMountTarget(NpcBase npc)
  {
  super(npc);
  this.taskType = MOUNT_VEHICLE;
  this.exclusiveTasks = ATTACK + HEAL + REPAIR + MOVE_TO;
  this.successTicks = 200;
  this.failureTicks = 40;
  }

@Override
public void onAiStarted()
  {  
  // TODO Auto-generated method stub
  }

@Override
public void onTick()
  {
  if(npc.getTarget()!=null)
    {
    finished = true;
    return;
    }
  AIAggroEntry entry = npc.targetHelper.getHighestAggroTarget(NpcAI.TARGET_MOUNT);
  if(entry!=null)
    {
    npc.setTargetAW(entry);
    }
  //TODO find a mountable same/friendly team vehicle within X blocks (20?)
  //set npc current target to type "mount", with entity of the vehicle to mount
  }

@Override
public boolean shouldExecute(NpcBase npc)
  {
  return super.shouldExecute(npc) && npc.getTarget()==null && npc.ridingEntity==null;
  }

}
