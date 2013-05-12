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
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AICourierInteract;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.npcs.waypoints.RouteFilter;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPointItemRouting;

public class AICourier extends NpcAIObjective
{

WayPointItemRouting point;
boolean setupPoint = false;
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

protected void setupPoint(WayPointItemRouting p)
  {
  this.routeFilter = new RouteFilter(p, npc);  
  }

@Override
public void onRunningTick()
  {  
  if(!validatePoint(point))
    {
    if(!findNextPoint())
      {
      this.setFinished();
      return;
      }
    }
  if(npc.getDistanceFromTarget(point) <3)
    {
    if(!setupPoint)
      {      
      setupPoint = true;
      this.setupPoint(point);
      }
    if(routeFilter==null || routeFilter.isFinished())
      {
      setupPoint = false;
      if(!findNextPoint())
        {
        setFinished();
        return;
        }
      }    
    }
  else
    {
    npc.setActionTicksToMax();
    setupPoint = false;
    }
  }

@Override
public void onObjectiveStart()
  {
  findNextPoint();
  }

@Override
public void stopObjective()
  {
  this.point = null;
  this.setupPoint = false;
  }

@Override
public byte getObjectiveNum()
  {
  return courier;
  }

}
