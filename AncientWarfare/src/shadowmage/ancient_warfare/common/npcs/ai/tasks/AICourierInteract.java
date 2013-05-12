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

import java.awt.Point;

import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAITask;
import shadowmage.ancient_warfare.common.npcs.ai.objectives.AICourier;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class AICourierInteract extends NpcAITask
{

AICourier parent;
/**
 * @param npc
 */
public AICourierInteract(NpcBase npc, AICourier parent)
  {
  super(npc);
  this.parent = parent;
  }

@Override
public void onTick()
  {
  npc.swingItem();
  if(npc.actionTick<=0)
    {
    //transact next item from list
    }
  }

@Override
public boolean shouldExecute()
  {
  if(npc.getTargetType()==TargetType.DELIVER && npc.getDistanceFromTarget(npc.getTarget())<3 && parent.routeFilter!=null && !parent.routeFilter.isFinished())
    {
    return true;
    }
  return false;
  }

@Override
public byte getTaskType()
  {
  return task_courier;
  }

}
