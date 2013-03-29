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

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.soldiers.NpcAI;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;

public class AIChooseAttackTarget extends NpcAI
{

/**
 * @param npc
 */
public AIChooseAttackTarget(NpcBase npc)
  {
  super(npc);
  this.failureTicks = 20;
  this.successTicks = 100;
  this.taskName = "ChooseAttackTarget";
  this.taskType = ATTACK;
  }

@Override
public int exclusiveTasks()
  {
  return HEAL+REPAIR+HARVEST;//basically...all other target-oriented tasks...
  }

@Override
public void onAiStarted()
  {
  
  }

@Override
public void onTick()
  {  
  npc.setTargetAW(npc.targetHelper.getHighestAggroTarget("attack")); 
  if(npc.getTarget()!=null)
    {
    this.success = true;
    }
  Config.logDebug("choosing target. new target type: "+npc.getTargetType());
  if(npc.getTarget()!=null)
    {
    Config.logDebug("target: "+npc.getTarget().getEntity());
    }
  this.finished = true;
  }

}
