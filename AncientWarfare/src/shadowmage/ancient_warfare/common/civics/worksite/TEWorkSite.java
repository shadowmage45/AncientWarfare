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
package shadowmage.ancient_warfare.common.civics.worksite;

import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.TECivicWarehouse;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public abstract class TEWorkSite extends TECivic implements ISidedInventory
{

protected LinkedList<WorkPoint> workPoints = new LinkedList<WorkPoint>();

public TEWorkSite()
  {
  this.isWorkSite = true;    
  }

protected abstract void scan();

protected abstract void doWork(NpcBase npc, WorkPoint p);

protected abstract TargetType validateWorkPoint(WorkPoint p);

@Override
protected void onCivicUpdate()
  { 
  validateWorkPoints();  
  if(!hasWork() && worldObj.checkChunksExist(minX, minY, minZ, maxX, maxY, maxZ))
    {
    if(!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
      {
      scan();
      }
    } 
  super.onCivicUpdate();
  }

@Override
protected void updateHasWork()
  {  
  if(this.inventory.getEmptySlotCount()<=0)
    {
    this.setHasWork(false);
    }
  else
    {
    this.setHasWork(!this.workPoints.isEmpty());
    }
  }

protected ItemStack depositItem(ItemStack stack)
  {
  stack = inventory.tryMergeItem(stack);
  if(stack!=null)
    {
    TECivicWarehouse wareHouse = this.getWarehousePosition();
    if(wareHouse!=null)
      {
      stack = wareHouse.inventory.tryMergeItem(stack);
      }
    }  
  return stack;
  }

@Override
public void doWork(NpcBase npc)
  {
  Iterator<WorkPoint> it = this.workPoints.iterator();
  WorkPoint p;
  while(it.hasNext())
    {
    p = it.next();
    if(validateWorkPoint(p)!=p.work)
      {
      it.remove();
      }
    else
      {
      this.doWork(npc, p);
      break;
      }
    }  
  this.updateHasWork();
  }

protected void validateWorkPoints()
  {
  Iterator<WorkPoint> it = this.workPoints.iterator();
  WorkPoint p;
  while(it.hasNext())
    {
    p = it.next();
    if(validateWorkPoint(p)!=p.work)
      {
      it.remove();
      }
    }
  setHasWork(!workPoints.isEmpty());
  }

protected void addWorkPoint(int x, int y, int z, TargetType work)
  {
  this.workPoints.add(new WorkPoint(x, y, z, work));
  }

protected void addWorkPoint(Entity ent, TargetType work)
  {
  this.workPoints.add(new WorkPoint(ent, work));
  }

@Override
public boolean isStackValidForSlot(int i, ItemStack itemstack)
  {
  if(i>=0 && i <this.getCivic().getResourceSlotSize())
    {
    for(ItemStack stack : this.getCivic().getResourceItemFilters())
      {
      if(InventoryTools.doItemsMatch(itemstack, stack))
        {
        return true;
        }
      }
    return false;
    }
  return true;
  }


/**
 * get the inventory slot indices for the input side
 * @param input side (0--bottom, 1--top, 2-5 sides)
 * @return an array of ints containing the slot numbers for the input side
 */
@Override
public int[] getSizeInventorySide(int var1)
  {
  if(resourceSlotIndices.length==0)
    {
    return otherSlotIndices;
    }
  switch(var1)
  {
  case 0://accessed from bottom
  return null;
  case 1://accessed from top
  Config.logDebug("returning resource slot indices from te: size: "+resourceSlotIndices.length);
  return resourceSlotIndices;
  
  /**
   * 2-5 fallthrough
   */
  case 2:
  case 3:
  case 4:
  case 5:
  Config.logDebug("returning normal slot indices from te: size: "+otherSlotIndices.length);
  return otherSlotIndices;
  }
  return null;
  }

/**
 * can insert into slot
 * @param slot, stack, side
 */
@Override
public boolean func_102007_a(int i, ItemStack itemstack, int j)
  {
  // TODO Auto-generated method stub
  return false;
  }

/**
 * can item be withdrawn from slot
 * @param slot, stack, side
 */
@Override
public boolean func_102008_b(int i, ItemStack itemstack, int j)
  {
  // TODO Auto-generated method stub
  return false;
  }


}
