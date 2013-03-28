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
import shadowmage.ancient_warfare.common.utils.Trig;

public class AIMoveToTarget extends NpcAI
{

float prevDistance;
float distance;

/**
 * @param npc
 */
public AIMoveToTarget(NpcBase npc)
  {
  super(npc);  
  this.successTicks = 20;
  this.failureTicks = 20;
  }

@Override
public int exclusiveTasks()
  {  
  return 0;
  }

@Override
public void onAiStarted()
  {
  
  }

@Override
public void onTick()
  {
  if(npc.getTarget()==null)
    {
    this.finished = true;
    this.success = true;
    return;
    }
  float bX = npc.getTarget().posX;
  float bY = npc.getTarget().posY;
  float bZ = npc.getTarget().posZ;
  this.prevDistance = this.distance;
  this.distance = (float) npc.getDistance(bX, bY, bZ);
  if(distance==prevDistance)
    {
    Config.logDebug("NPC could not move, or did not move between AIMoveToTarget ticks");
    }
  if(distance>12)
    {
    float angle = Trig.getYawTowardsTarget(npc.posX, npc.posZ, bX, bZ);
    bX = Trig.sinDegrees(angle)*12;
    bZ = Trig.cosDegrees(angle)*12;
    }
  npc.getNavigator().tryMoveToXYZ(bX, bY, bZ, npc.getAIMoveSpeed());
  }


}
