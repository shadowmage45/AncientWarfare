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
package shadowmage.ancient_warfare.common.civics.worksite.te.farm;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class TEFarmCocoa extends TEWorkSiteFarm
{


/**
 * 
 */
public TEFarmCocoa()
  {
  this.mainBlockID = Block.cocoaPlant.blockID;
  this.tilledEarthID = Block.wood.blockID;
  this.plantableFilter = new ItemStack(Item.dyePowder,1,3);
  }

protected boolean isJungleLog(int id, int meta)
  {
  return id==tilledEarthID && (meta & 3)==3;
  }

protected boolean isJungleLog(int x, int y, int z)
  {
  return isJungleLog(worldObj.getBlockId(x, y, z), worldObj.getBlockMetadata(x, y, z));
  }

@Override
protected TargetType validateWorkPoint(int x, int y, int z)
  {
  int id = worldObj.getBlockId(x, y, z);  
  if(id==0 && this.inventory.containsAtLeast(plantableFilter, 1))
    {    
    if(isJungleLog(x-1, y,z) || isJungleLog(x+1, y, z) || isJungleLog(x, y, z-1) || isJungleLog(x, y, z+1))
      {
      return TargetType.FARM_PLANT;
      }
    }
  else if(id==this.mainBlockID)
    {
    int meta = worldObj.getBlockMetadata(x, y, z);
    if(isMetaMature(meta) && this.inventory.getEmptySlotCount()>=1)
      {
      return TargetType.FARM_HARVEST;
      }    
    }
  return TargetType.NONE;
  }

protected boolean isMetaMature(int meta)
  {
  return meta >= 8 && meta <=11;
  }

/**
 * get the direction from x -> x1
 * @param x
 * @param y
 * @param z
 * @param x1
 * @param y1
 * @param z1
 * @return
 */
protected int getDirection(int x, int y, int z, int x1, int y1, int z1)
  {
  if(z1>z)
    {
    return 0;
    }
  else if(z1<z)
    {
    return 2;
    }
  else if(x1<x)
    {
    return 1;
    }
  else if(x1>x)
    {
    return 3;
    }
  return -1;
  }

@Override
protected void doWork(IWorker npc, WorkPoint p)
  {
  if(npc==null || p==null)
    {
    return;
    } 
  if(p.work==TargetType.FARM_HARVEST)
    {
    List<ItemStack> drops = BlockTools.breakBlock(worldObj, getOwnerName(), p.x, p.y, p.z, 0);   
    for(ItemStack item : drops)
      {
      this.tryAddItemToInventory(item, resourceSlotIndices, regularIndices);      
      }
    }
  else if(p.work==TargetType.FARM_PLANT)
    {
    if(inventory.containsAtLeast(plantableFilter, 1))
      {
      int meta = -1;
      if(isJungleLog(p.x-1, p.y,p.z))
        {
        meta = getDirection(p.x, p.y, p.z, p.x-1, p.y, p.z);
        }
      else if(isJungleLog(p.x+1, p.y, p.z))
        {
        meta = getDirection(p.x, p.y, p.z, p.x+1, p.y, p.z);
        }
      else if(isJungleLog(p.x, p.y, p.z-1))
        {
        meta = getDirection(p.x, p.y, p.z, p.x, p.y, p.z-1);
        }
      else if( isJungleLog(p.x, p.y, p.z+1))
        {
        meta = getDirection(p.x, p.y, p.z, p.x, p.y, p.z+1);
        }
      if(meta>=0)
        {
        inventory.tryRemoveItems(plantableFilter, 1);        
        worldObj.setBlock(p.x, p.y, p.z, mainBlockID, meta, 3);
        }    
      }  
    }    
  }

}
