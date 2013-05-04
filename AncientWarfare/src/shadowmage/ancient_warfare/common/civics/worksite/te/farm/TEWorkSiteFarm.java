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

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.worksite.TEWorkSite;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public abstract class TEWorkSiteFarm extends TEWorkSite
{

int mainBlockID = Block.crops.blockID;//the blockID that this civic looks for within its work bounds
int tilledEarthID = Block.tilledField.blockID;//the 'plantable' block. these are the 'plant' points, if y+1==0
int mainBlockMatureMeta;
ItemStack plantableFilter;

//id / meta of plantable block (-1 meta for all)
//id / meta of harvest block


public TEWorkSiteFarm()
  {
  
  }

protected TargetType validateWorkPoint(int x, int y, int z)
  {
  int id = worldObj.getBlockId(x, y, z);  
  if(id==0 && worldObj.getBlockId(x, y-1, z)==tilledEarthID && inventory.containsAtLeast(plantableFilter, 1))
    {    
    return TargetType.FARM_PLANT;
    }
  else if(id==this.mainBlockID)
    {
    int meta = worldObj.getBlockMetadata(x, y, z);
    if(meta==this.mainBlockMatureMeta)
      {
      return TargetType.FARM_HARVEST;
      }    
    }
  return TargetType.NONE;
  }

@Override
protected void scan()
  {
  TargetType t = TargetType.NONE;
  for(int x = this.minX; x<=this.maxX; x++)
    {
    for(int y = this.minY; y<=this.maxY; y++)
      {
      for(int z = this.minZ; z<=this.maxZ; z++)
        {     
        t = this.validateWorkPoint(x, y, z);
        if(t!=TargetType.NONE)
          {
          this.addWorkPoint(x, y, z, t);
          }        
        }
      }
    }
  }

@Override
protected void doWork(NpcBase npc, WorkPoint p)
  {
  if(npc==null || p==null)
    {
    return;
    } 
  if(p.work==TargetType.FARM_HARVEST)
    {
    Config.logDebug("harvesting crops!!");
    Block cropsBlock = Block.blocksList[mainBlockID];
    List<ItemStack> blockDrops = cropsBlock.getBlockDropped(npc.worldObj, p.x, p.y, p.z, worldObj.getBlockMetadata(p.x, p.y, p.z), 0);
    worldObj.setBlockToAir(p.x, p.y, p.z);
    for(ItemStack item : blockDrops)
      {
      if(item==null){continue;}
      if(InventoryTools.doItemsMatch(item, plantableFilter) && inventory.canHoldItem(plantableFilter, item.stackSize))
        {
        int count = inventory.getCountOf(plantableFilter);
        if(count<128)
          {
          item = inventory.tryMergeItem(item);
          }
        if(item!=null)
          {
          item = npc.inventory.tryMergeItem(item);
          if(item!=null)
            {
            InventoryTools.dropItemInWorld(worldObj, item, xCoord+0.5d, yCoord, zCoord+0.5d);
            }            
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
  else if(p.work==TargetType.FARM_PLANT)
    {
    if(inventory.containsAtLeast(plantableFilter, 1))
      {
      inventory.tryRemoveItems(plantableFilter, 1);
      worldObj.setBlock(p.x, p.y, p.z, mainBlockID, 0, 3);
      }
    else
      {
      Config.logDebug("had plant job but no plantables!!");
      }
    }
    
  }

@Override
protected TargetType validateWorkPoint(WorkPoint p)
  {
  return p==null ? TargetType.NONE : validateWorkPoint(p.x, p.y, p.z);
  }


}
