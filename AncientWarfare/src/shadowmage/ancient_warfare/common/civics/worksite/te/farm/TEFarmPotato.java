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

public class TEFarmPotato extends TEWorkSiteFarm
{


ItemStack potatoFilter = new ItemStack(Item.potato,1);
/**
 * 
 */
public TEFarmPotato()
  {
  this.mainBlockID = Block.potato.blockID;
  this.mainBlockMatureMeta = 7;
  }

@Override
public void onWorkFinished(NpcBase npc, WorkPoint point)
  {
  if(point.hasWork(worldObj))
    {
    if(point.getTargetType()==TargetType.FARM_HARVEST)
      {
      Config.logDebug("harvesting potato!!");
      List<ItemStack> blockDrops = Block.carrot.getBlockDropped(npc.worldObj, point.floorX(), point.floorY(), point.floorZ(), 7, 0);
      worldObj.setBlockWithNotify(point.floorX(), point.floorY(), point.floorZ(), 0);
      for(ItemStack item : blockDrops)
        {
        if(item==null){continue;}
        if(InventoryTools.doItemsMatch(item, potatoFilter) && inventory.canHoldItem(potatoFilter, item.stackSize))
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
      if(inventory.containsAtLeast(potatoFilter, 1))
        {
        Config.logDebug("planting potato!!");
        inventory.tryRemoveItems(potatoFilter, 1);
        worldObj.setBlockAndMetadataWithNotify(point.floorX(), point.floorY()+1, point.floorZ(), mainBlockID, 0);
        }
      else
        {
        Config.logDebug("had plant job but no potato!!");
        }
      }
    }
  super.onWorkFinished(npc, point);
  Config.logDebug("wheat farm work finished POST SUPER.  wkred: "+ this.workPoints.size());
  }

@Override
public boolean canAssignWorkPoint(NpcBase npc, WorkPoint p)
  {
  if(p.getTargetType()== TargetType.FARM_HARVEST || (p.getTargetType()== TargetType.FARM_PLANT && this.inventory.containsAtLeast(potatoFilter, 1)))
    {
    return true;
    }
  Config.logDebug("farm did not contain potato, could not assign plant job");
  return false;
  }



}