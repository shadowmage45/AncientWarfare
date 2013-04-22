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
package shadowmage.ancient_warfare.common.civics.worksite.te.farm;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.civics.WorkType;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class TEWorkSiteFarmWheat extends TEWorkSiteFarm
{

ItemStack seedFilter = new ItemStack(Item.seeds,1);
ItemStack wheatFilter = new ItemStack(Item.wheat,1);

/**
 * 
 */
public TEWorkSiteFarmWheat()
  {
  this.mainBlockID = Block.crops.blockID;
  this.mainBlockMatureMeta = 7;
  }

@Override
public void onWorkFinished(NpcBase npc, WorkPoint point)
  {
  if(point.hasWork(worldObj))
    {
    if(point.getWorkType()==WorkType.FARM_HARVEST)
      {
      List<ItemStack> blockDrops = Block.crops.getBlockDropped(npc.worldObj, point.floorX(), point.floorY(), point.floorZ(), 7, 0);
      boolean gotSeedBack = false;
      for(ItemStack item : blockDrops)
        {
        if(!gotSeedBack && InventoryTools.doItemsMatch(item, seedFilter))
          {
          gotSeedBack = true;
          //place item into inventory
          }
        }
      Config.logDebug("harvesting wheat!!");
      worldObj.setBlockWithNotify(point.floorX(), point.floorY(), point.floorZ(), 0);
      ItemStack wheat = ItemStack.copyItemStack(wheatFilter);
      wheat.stackSize = 3;
//      Config.logDebug("adding wheat to inventory!");
      wheat = npc.inventory.tryMergeItem(wheat);
      if(wheat!=null)
        {
        Config.logDebug("should drop extra wheat!!");
        //TODO drop extra on the ground...
        }
      ItemStack seedStack = ItemStack.copyItemStack(seedFilter);
//      Config.logDebug("adding seeds to te inventory");
      seedStack = this.inventory.tryMergeItem(seedStack);
      if(seedStack!=null)
        {
        Config.logDebug("should drop extra seeds!!");
        //TODO drop extra on the ground...
        }
      }
    else if(point.getWorkType()==WorkType.FARM_PLANT)
      {
      if(npc.inventory.containsAtLeast(seedFilter, 1))
        {
        Config.logDebug("planting wheat!!");
        npc.inventory.tryRemoveItems(seedFilter, 1);
        worldObj.setBlockAndMetadataWithNotify(point.floorX(), point.floorY()+1, point.floorZ(), mainBlockID, 0);
        }
      else
        {
        Config.logDebug("had plant job but no seeds!!");
        }
      }
    }
  Config.logDebug("wheat farm work finished. wkred: "+ this.workPoints.size());
  super.onWorkFinished(npc, point);
  Config.logDebug("wheat farm work finished POST SUPER.  wkred: "+ this.workPoints.size());
  }

@Override
public boolean canAssignWorkPoint(NpcBase npc, WorkPoint p)
  {
  if(p.getWorkType()== WorkType.FARM_HARVEST || (p.getWorkType()== WorkType.FARM_PLANT && this.inventory.containsAtLeast(seedFilter, 1)))
    {
    return true;
    }
  Config.logDebug("farm did not contain seeds");
  return false;
  }

@Override
public WorkPoint getWorkPoint(NpcBase npc)
  {
  WorkPoint p = super.getWorkPoint(npc);
  if(p!=null && p.getWorkType()==WorkType.FARM_PLANT)
    {
    if(this.inventory.containsAtLeast(seedFilter, 1) && npc.inventory.canHoldItem(seedFilter, 1))
      {
      Config.logDebug("removing 1 seed");
      this.inventory.tryRemoveItems(seedFilter, 1);
      npc.inventory.tryMergeItem(ItemStack.copyItemStack(seedFilter));
      }
    else
      {
      return null;
      }
    }
  return p;
  }

}
