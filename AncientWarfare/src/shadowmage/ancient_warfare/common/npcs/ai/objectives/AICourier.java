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

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AICourierInteract;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPointItemRouting;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class AICourier extends NpcAIObjective
{

int ranPoints = 0;
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
    WayPointItemRouting point = null;
    for(int i = 0; i < npc.wayNav.getCourierSize(); i++)
      {
      point = npc.wayNav.getCourierPointAt(i);
      if(point!=null && point.isTargetLoaded(npc.worldObj))
        {
        this.currentPriority = this.maxPriority;
        break;
        }
      }
    }
  }

protected boolean validatePoint(WayPointItemRouting p)
  {
  if(p!=null)
    {
    if(!p.isTargetLoaded(npc.worldObj))
      {
      return false;
      }
    if(p.getTileEntity(npc.worldObj) instanceof IInventory)
      {
      return true;
      }    
    }
  return false;
  }

protected boolean findNextPoint()
  {
  WayPointItemRouting point = npc.wayNav.getActiveCourierPoint(); 
  npc.setTargetAW(null);
  for(int i = 0; i < npc.wayNav.getCourierSize(); i++)
    {
    point = npc.wayNav.getNextCourierPoint();
    if(point.isTargetLoaded(npc.worldObj))
      {
      npc.setTargetAW(point);
      this.ranPoints++;
      return true;
      }    
    }
  return false;
  }

@Override
public void onRunningTick()
  {  
  WayPointItemRouting point = npc.wayNav.getActiveCourierPoint();
  if(!validatePoint(point))
    {
    if(!findNextPoint())
      {
      this.setFinished();
      return;
      }
    }   
  if(this.ranPoints>=npc.wayNav.getCourierSize())
    {
    this.setFinished();
    }
  }

@Override
public void onObjectiveStart()
  {
  WayPointItemRouting point = npc.wayNav.getActiveCourierPoint();
  if(point==null || !point.isTargetLoaded(npc.worldObj))
    {
    findNextPoint();
    }
  else
    {
    npc.setTargetAW(npc.wayNav.getActiveCourierPoint());
    }
  this.ranPoints = 0;
  }

public void setPointFinished()
  {
  this.findNextPoint();
  }

@Override
public void stopObjective()
  {
  }

@Override
public byte getObjectiveNum()
  {
  return courier;
  }

}
