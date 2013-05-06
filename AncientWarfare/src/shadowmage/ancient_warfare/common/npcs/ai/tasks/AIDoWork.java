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

import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAITask;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class AIDoWork extends NpcAITask
{
/**
 * @param npc
 */
public AIDoWork(NpcBase npc)
  {
  super(npc);
  }

@Override
public byte getTaskType()
  {
  return task_work;
  }

@Override
public void onTick()
  {
  TECivic te = npc.wayNav.getWorkSiteTile();   
  npc.swingItem();
  if(te!=null && npc.actionTick<=0)
    {  
    te.doWork(npc);
    npc.setActionTicksToMax();    
    }
  }

@Override
public boolean shouldExecute()
  {
  return npc.getTargetType()==TargetType.WORK && npc.wayNav.getWorkSiteTile()!=null && npc.getDistanceFromTarget(npc.getTarget())<2.4f;
  }

}
