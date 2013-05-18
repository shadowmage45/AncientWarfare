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

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAITask;
import shadowmage.ancient_warfare.common.npcs.ai.objectives.AICourier;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPointItemRouting;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

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
  if(npc.aiManager.wasMoving)
    {
    npc.aiManager.wasMoving = false;
    npc.actionTick = 0;
    }
  npc.swingItem();
  if(npc.actionTick<=0)
    {
    WayPointItemRouting point = npc.wayNav.getActiveCourierPoint();
    if(point==null)
      {
      return;
      }
    TileEntity te = point.getTileEntity(npc.worldObj);
    IInventory target = null;
    if(te instanceof IInventory)
      {
      target = (IInventory)te;
      }
    if(target==null)
      {
      return;
      }
    if(!point.doWork(npc))
      {
      parent.setPointFinished();
      }   
    else
      {
      npc.setActionTicksToMax();
      }
    }
  }

@Override
public boolean shouldExecute()
  {
  if(npc.getTargetType()==TargetType.DELIVER && npc.getDistanceFromTarget(npc.getTarget())<3 && npc.wayNav.getActiveCourierPoint()!=null)
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
