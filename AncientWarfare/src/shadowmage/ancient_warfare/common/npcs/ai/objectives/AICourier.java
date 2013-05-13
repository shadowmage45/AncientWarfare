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

import net.minecraft.inventory.IInventory;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AICourierInteract;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.npcs.waypoints.RouteFilter;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPointItemRouting;

public class AICourier extends NpcAIObjective
{

public WayPointItemRouting point;
public boolean isPointFinished = false;
public boolean setupPoint = false;
public RouteFilter routeFilter = null;
/**
 * @param npc
 * @param maxPriority
 */
public AICourier(NpcBase npc, int maxPriority)
  {
  super(npc, maxPriority);
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 2, false));
  this.aiTasks.add(new AICourierInteract(npc, this));
  }

@Override
public void updatePriority()
  {
  this.currentPriority = 0;
  if(npc.wayNav.getCourierSize()>0)
    {
    this.currentPriority = this.maxPriority;
    Config.logDebug("setting courier priority to max");
    }
  }

protected boolean validatePoint(WayPointItemRouting p)
  {
  if(p!=null)
    {
    if(p.getTileEntity(npc.worldObj) instanceof IInventory)
      {
      return true;
      }    
    }
  return false;
  }

protected boolean findNextPoint()
  {
  point = npc.wayNav.getNextCourierPoint();
  npc.setTargetAW(point);
  if(point!=null)
    {
    if(validatePoint(point))
      {
      return true;
      }
    point = null;    
    }
  return false;
  }

@Override
public void onRunningTick()
  {  
  Config.logDebug("courier ai running");
  if(!validatePoint(point))
    {
    Config.logDebug("first validation failed");
    if(!findNextPoint())
      {
      Config.logDebug("could not find next point");
      this.setFinished();
      return;
      }
    Config.logDebug("found next point");
    }
  
  if(npc.getDistanceFromTarget(point) <3)
    {
    Config.logDebug("within distance to work");
    if(!setupPoint)
      {
      setupPoint = true;
      }
    else if(isPointFinished)
      {
      isPointFinished = false;
      if(!findNextPoint())
        {
        setFinished();
        return;
        }
      }
    else
      {
      //continue...let ai task do its thing
      }        
    }
  else
    {
    Config.logDebug("not in distance to work, setting action ticks to max, setup to false");
    setupPoint = false;
    npc.setActionTicksToMax();
    }
  }

public void setupPoint()
  {
  this.routeFilter = new RouteFilter(point, npc);
  }

@Override
public void onObjectiveStart()
  {
  findNextPoint();
  this.isPointFinished = false;
  this.routeFilter = null;
  }

@Override
public void stopObjective()
  {
  this.routeFilter = null;
  this.point = null;
  }

@Override
public byte getObjectiveNum()
  {
  return courier;
  }

}
