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
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class TEFarmCarrot extends TEWorkSiteFarm
{

ItemStack carrotFilter = new ItemStack(Item.carrot,1);
/**
 * 
 */
public TEFarmCarrot()
  {
  this.mainBlockID = Block.carrot.blockID;
  this.mainBlockMatureMeta = 7;
  }

@Override
public void onWorkFinished(NpcBase npc, WorkPoint point)
  {
  if(point.hasWork(worldObj))
    {
    if(point.getTargetType()==TargetType.FARM_HARVEST)
      {
      Config.logDebug("harvesting carrot!!");
      List<ItemStack> blockDrops = Block.carrot.getBlockDropped(npc.worldObj, point.floorX(), point.floorY(), point.floorZ(), 7, 0);
      worldObj.setBlockToAir(point.floorX(), point.floorY(), point.floorZ());
      for(ItemStack item : blockDrops)
        {
        if(item==null){continue;}
        if(InventoryTools.doItemsMatch(item, carrotFilter) && inventory.canHoldItem(carrotFilter, item.stackSize))
          {
          item = inventory.tryMergeItem(item);
          if(item!=null)
            {
            InventoryTools.dropItemInWorld(worldObj, item, xCoord+0.5d, yCoord, zCoord+0.5d);
            }
          }
        else
          {
          item = npc.inventory.tryMergeItem(item);
          if(item!=null)
            {
            InventoryTools.dropItemInWorld(worldObj, item, xCoord+0.5d, yCoord, zCoord+0.5d);
            }
          }
        }
      }
    else if(point.getTargetType()==TargetType.FARM_PLANT)
      {
      if(inventory.containsAtLeast(carrotFilter, 1))
        {
        Config.logDebug("planting carrot!!");
        inventory.tryRemoveItems(carrotFilter, 1);
        worldObj.setBlock(point.floorX(), point.floorY()+1, point.floorZ(), mainBlockID, 0,3);
        }
      else
        {
        Config.logDebug("had plant job but no carrots!!");
        }
      }
    }
  super.onWorkFinished(npc, point);
  Config.logDebug("wheat farm work finished POST SUPER.  wkred: "+ this.workPoints.size());
  }

@Override
public boolean canAssignWorkPoint(NpcBase npc, WorkPoint p)
  {
  if(p.getTargetType()== TargetType.FARM_HARVEST || (p.getTargetType()== TargetType.FARM_PLANT && this.inventory.containsAtLeast(carrotFilter, 1)))
    {
    return true;
    }
  Config.logDebug("farm did not contain carrots, could not assign plant job");
  return false;
  }


}
