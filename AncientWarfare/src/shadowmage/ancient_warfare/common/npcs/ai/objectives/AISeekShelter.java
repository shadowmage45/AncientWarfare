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

import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.targeting.TargetPosition;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class AISeekShelter extends NpcAIObjective
{

Village theVillage;
VillageDoorInfo theDoor;

/**
 * @param npc
 * @param maxPriority
 */
public AISeekShelter(NpcBase npc, int maxPriority)
  {
  super(npc, maxPriority);
  }

@Override
public byte getObjectiveNum()
  {
  return shelter;
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 3, false));
  }

@Override
public void updatePriority()
  {
  this.currentPriority = 0;
  int x, y, z;
  x = MathHelper.floor_double(npc.posX);
  y = MathHelper.floor_double(npc.posY);
  z = MathHelper.floor_double(npc.posZ);
  if(((!npc.worldObj.isDaytime() || npc.worldObj.isRaining()) && !npc.worldObj.provider.hasNoSky) || npc.targetHelper.areTargetsInRange(TargetType.ATTACK, 16))
    {//if it is nighttime, or raining, and the world has a sky (no sky, no sun, no shelter at night!)
    WayPoint p = npc.wayNav.getHomePoint();
    if(p==null)
      {
      this.theVillage = npc.worldObj.villageCollectionObj.findNearestVillage(x, y, z, Config.npcAISearchRange);
      if(theVillage!=null)
        {
        this.currentPriority = this.maxPriority;
        }      
      }  
    else
      {
      this.currentPriority = this.maxPriority;
      }
    }
  }

@Override
public void onRunningTick()
  {
  
  }

@Override
public void onObjectiveStart()
  {
  int x, y, z;
  x = MathHelper.floor_double(npc.posX);
  y = MathHelper.floor_double(npc.posY);
  z = MathHelper.floor_double(npc.posZ);
  WayPoint p = npc.wayNav.getHomePoint();
  if(theVillage==null && p == null)
    {    
    this.isFinished = true;
    this.cooldownTicks = this.maxCooldownticks;
    }
  else
    {
    if(p!=null)
      {
      npc.setTargetAW(TargetPosition.getNewTarget(p.floorX(), p.floorY(), p.floorZ(), TargetType.MOVE));
      }
    else if(theVillage!=null)
      {
      theDoor = theVillage.findNearestDoorUnrestricted(x, y, z);
      if(theDoor==null)
        {
        this.isFinished = true;
        this.cooldownTicks = this.maxCooldownticks;    
        this.currentPriority = 0;
        }
      else
        {
        npc.setTargetAW(TargetPosition.getNewTarget(theDoor.getInsidePosX(), theDoor.getInsidePosY(), theDoor.getInsidePosZ(), TargetType.MOVE));
        }
      }
    }
  }

@Override
public void stopObjective()
  {
  this.theDoor = null;
  this.npc.clearPath();
  }

}
