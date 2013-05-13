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
  npc.swingItem();
  if(npc.actionTick<=0)
    {
    TileEntity te = parent.point.getTileEntity(npc.worldObj);
    IInventory target = null;
    if(te instanceof IInventory)
      {
      target = (IInventory)te;
      }
    if(target==null)
      {
      return;
      }
    ItemStack fromSlot;    
    if(parent.point.getDeliver())
      {
      boolean foundWork = false;
      for(int k = 0; k < npc.inventory.getSizeInventory(); k++)
        {
        fromSlot = npc.inventory.getStackInSlot(k);
        if(parent.point.doesMatchFilter(fromSlot) && InventoryTools.canHoldItem(target, fromSlot, fromSlot.stackSize, 0, target.getSizeInventory()-1))
          {
          fromSlot = InventoryTools.tryMergeStack(target, fromSlot, 0, target.getSizeInventory()-1);
          npc.inventory.setInventorySlotContents(k, fromSlot);
          foundWork = true;
          break;
          }
        }
      if(!foundWork)
        {
        parent.isPointFinished = true;
        }
      }
    else
      {      
      boolean foundWork = false;
      for(int i = 0; i < target.getSizeInventory(); i++)
        {
        fromSlot = target.getStackInSlot(i);
        if(parent.point.doesMatchFilter(fromSlot) && InventoryTools.canHoldItem(npc.inventory, fromSlot, fromSlot.stackSize, 0, npc.inventory.getSizeInventory()-1))
          {
          fromSlot = InventoryTools.tryMergeStack(npc.inventory, fromSlot, 0, target.getSizeInventory()-1);
          target.setInventorySlotContents(i, fromSlot);
          foundWork = true;
          break;
          }
        }
      if(!foundWork)
        {
        parent.isPointFinished = true;
        }
      }
    }
  }

@Override
public boolean shouldExecute()
  {
  if(npc.getTargetType()==TargetType.DELIVER && npc.getDistanceFromTarget(npc.getTarget())<3 && parent.point!=null)
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
