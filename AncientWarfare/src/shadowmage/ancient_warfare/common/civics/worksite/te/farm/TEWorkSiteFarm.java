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
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.civics.worksite.TEWorkSite;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public abstract class TEWorkSiteFarm extends TEWorkSite
{

int mainBlockID = Block.crops.blockID;//the blockID that this civic looks for within its work bounds
int tilledEarthID = Block.tilledField.blockID;//the 'plantable' block. these are the 'plant' points, if y+1==0
int mainBlockMatureMeta;
ItemStack plantableFilter;
ItemStack bonemealFilter;
boolean canUseBonemeal = false;

//id / meta of plantable block (-1 meta for all)
//id / meta of harvest block


public TEWorkSiteFarm()
  {
  this.renderBounds = true;
  this.bonemealFilter = new ItemStack(Item.dyePowder,1,15);
  }

protected TargetType validateWorkPoint(int x, int y, int z)
  {
  int id = worldObj.getBlockId(x, y, z);
  if(id==0 && (worldObj.getBlockId(x, y-1, z) == Block.dirt.blockID || worldObj.getBlockId(x, y-1, z)==Block.grass.blockID))
    {
    return TargetType.FARM_TILL;
    }
  else if(id==0 && worldObj.getBlockId(x, y-1, z)==tilledEarthID && inventory.containsAtLeast(plantableFilter, 1))
    {    
    return TargetType.FARM_PLANT;
    }
  else if(id==this.mainBlockID  && this.overFlow.isEmpty())
    {
    int meta = worldObj.getBlockMetadata(x, y, z);    
    if(meta==this.mainBlockMatureMeta)
      {
      return TargetType.FARM_HARVEST;
      }
    else if(this.canUseBonemeal && this.inventory.containsAtLeast(bonemealFilter, 3))
      {
      return TargetType.FARM_BONEMEAL;
      }
    }
  return TargetType.NONE;
  }

@Override
protected void scan()
  {
  TargetType t = TargetType.NONE;
  int id;
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
protected void doWork(IWorker npc, WorkPoint p)
  {
  if(npc==null || p==null)
    {
    return;
    } 
  if(p.work==TargetType.FARM_HARVEST)
    {
    List<ItemStack> drops = BlockTools.breakBlock(worldObj, p.x, p.y, p.z, 0);   
    for(ItemStack item : drops)
      {
      if(this.resourceFilterContains(item))
        {
        this.tryAddItemToInventory(item, resourceSlotIndices, regularIndices);
        }
      else
        {
        this.tryAddItemToInventory(item, regularIndices);
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
    }
  else if(p.work==TargetType.FARM_BONEMEAL)
    {
    if(worldObj.getBlockMetadata(p.x, p.y, p.z)==this.mainBlockMatureMeta)
      {
      List<ItemStack> drops = BlockTools.breakBlock(worldObj, p.x, p.y, p.z, 0);   
      for(ItemStack item : drops)
        {
        if(this.resourceFilterContains(item))
          {
          this.tryAddItemToInventory(item, resourceSlotIndices, regularIndices);
          }
        else
          {
          this.tryAddItemToInventory(item, regularIndices);
          }      
        }
      }
    else
      { 
      Config.logDebug("using bonemeal on: "+p);
      if(inventory.containsAtLeast(bonemealFilter, 3))
        {
        inventory.tryRemoveItems(bonemealFilter, 3);
        worldObj.setBlock(p.x, p.y, p.z, mainBlockID, mainBlockMatureMeta, 3);
        }      
      }   
    }
  else if(p.work == TargetType.FARM_TILL)
    {
    worldObj.setBlock(p.x, p.y-1, p.z, Block.tilledField.blockID, 0, 3);
    }
  }

@Override
protected TargetType validateWorkPoint(WorkPoint p)
  {
  return p==null ? TargetType.NONE : validateWorkPoint(p.x, p.y, p.z);
  }


}
