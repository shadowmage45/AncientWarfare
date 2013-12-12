/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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

import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class AIChooseCommander extends NpcAIObjective
{

/**
 * @param npc
 * @param maxPriority
 */
public AIChooseCommander(NpcBase npc, int maxPriority)
  {
  super(npc, maxPriority);
  this.currentPriority = 0;
  }

@Override
public void addTasks()
  {

  }

@Override
public void updatePriority()
  {
  this.cooldownTicks = this.maxCooldownticks;
  NpcBase com = npc.wayNav.getCommander();
  if(com==null || com.isDead || npc.getDistanceToEntity(com) > Config.npcAISearchRange)
    {
    com = null;
    ITargetEntry entry = npc.targetHelper.getHighestAggroTarget(TargetType.COMMANDER);
    if(entry!=null && entry.getEntity(npc.worldObj) instanceof NpcBase)
      {
      com = (NpcBase)entry.getEntity(npc.worldObj);
      }    
    npc.wayNav.setCommander(com);
    }
  }

@Override
public void onRunningTick()
  {

  }

@Override
public void onObjectiveStart()
  {

  }

@Override
public void stopObjective()
  {

  }

@Override
public byte getObjectiveNum()
  {
  return commander;
  }

}
