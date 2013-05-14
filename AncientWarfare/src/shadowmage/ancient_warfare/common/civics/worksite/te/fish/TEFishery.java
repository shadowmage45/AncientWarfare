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
package shadowmage.ancient_warfare.common.civics.worksite.te.fish;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.civics.worksite.TEWorkSite;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class TEFishery extends TEWorkSite
{

ItemStack fishFilter;
int waterBlocks = 0;

public TEFishery()
  {
  ItemStack fishFilter = new ItemStack(Item.fishRaw);
  }

@Override
protected void scan()
  {
  waterBlocks = 0;
  int id;
  int canHold = inventory.canHoldMore(fishFilter);
  for(int y = this.minY; y<=this.maxY; y++)
    {
    for(int z = this.minZ; z<= this.maxZ; z++)
      {
      for(int x = this.minX; x<=this.maxX; x++)
        {
        id = worldObj.getBlockId(x, y, z);
        if(id==Block.waterMoving.blockID || id==Block.waterStill.blockID)
          {
          waterBlocks++;
          if(canHold>0)
            {
            canHold--;            
            this.addWorkPoint(x, y, z, TargetType.FISH_CATCH);
            }
          }
        }
      }
    }  
  }

@Override
protected void doWork(NpcBase npc, WorkPoint p)
  {
  if(p.work==TargetType.FISH_CATCH && inventory.canHoldItem(fishFilter, 1))
    {
    float waterPercent = (float)waterBlocks * 0.001953125f; // essentially /512
    waterPercent *= 0.5f;//cut percent in half
    Config.logDebug("fish catch percent: "+waterPercent);
    float check = rng.nextFloat();
    Config.logDebug("check roll: "+check);
    if(check<=waterPercent)      
      {      
      Config.logDebug("check was within bounds, harvesting fish");
      ItemStack stack = this.inventory.tryMergeItem(fishFilter.copy());
      stack = this.overflow.tryMergeItem(stack);
      InventoryTools.dropItemInWorld(worldObj, stack, xCoord+0.5d, yCoord+1.d, zCoord+0.5d);
      }
    }
  }

@Override
protected TargetType validateWorkPoint(WorkPoint p)
  {
  int id = worldObj.getBlockId(p.x, p.y, p.z); 
  if(id == Block.waterMoving.blockID || id==Block.waterStill.blockID)
    {
    return TargetType.FISH_CATCH;
    }
  return TargetType.NONE;
  }

}
