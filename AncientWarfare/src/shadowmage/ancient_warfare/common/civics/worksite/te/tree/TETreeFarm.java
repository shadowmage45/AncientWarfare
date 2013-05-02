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
package shadowmage.ancient_warfare.common.civics.worksite.te.tree;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.worksite.TEWorkSite;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public abstract class TETreeFarm extends TEWorkSite
{

int logID = Block.wood.blockID;
int logMeta = 0;
int saplingID = Block.sapling.blockID;
int saplingMeta = 0;
int maxSearchHeight = 16;
ItemStack saplingFilter;

/**
 * 
 */
public TETreeFarm()
  {
  
  }

@Override
protected void onCivicUpdate()
  {
  super.onCivicUpdate();
  List<EntityItem> entities = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(minX-1, minY-1, minZ-1, maxX+2, maxY+2, maxZ+2));
  ItemStack stack;
  if(entities!=null)
    {
    for(EntityItem ent : entities)
      {
      if(ent!=null && ent.getEntityItem()!=null)
        {
        stack = ent.getEntityItem();
        if((stack.itemID==saplingID && stack.getItemDamage()==saplingMeta) || stack.itemID==Item.appleRed.itemID)
          {
          if(inventory.canHoldItem(stack, stack.stackSize))
            {
            stack = inventory.tryMergeItem(stack);
            if(stack!=null)
              {
              ent.setEntityItemStack(stack);
              }
            else//stack is null/merged sucessfully
              {
              ent.setDead();
              }
            }
          }
        }
      }
    }
  }

@Override
public AxisAlignedBB getSecondaryRenderBounds()
  {
  return AxisAlignedBB.getAABBPool().getAABB(minX, maxY+1, minZ, maxX+1, maxY+1+maxSearchHeight, maxZ+1);
  }

@Override
protected void scan()
  {
  TargetType t;
  for(int y = this.minY; y<=this.maxY+this.maxSearchHeight; y++)
    {
    for(int x = this.minX; x<=this.maxX; x++)
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
  if(p.work==TargetType.TREE_CHOP)
    {
    Config.logDebug("chopping tree!!"); 
    List<ItemStack> drops = BlockTools.breakBlock(worldObj, p.x, p.y, p.z, 0);   
    for(ItemStack item : drops)
      {
      item = npc.inventory.tryMergeItem(item);
      if(item!=null)
        {
        item = this.inventory.tryMergeItem(item);
        if(item!=null)
          {
          InventoryTools.dropItemInWorld(worldObj, item, xCoord+0.5d, yCoord, zCoord+0.5d);
          }
        }
      }
    }  
  else if(p.work==TargetType.TREE_PLANT && inventory.containsAtLeast(saplingFilter, 1))
    { 
    Config.logDebug("planting sapling ");
    worldObj.setBlock(p.x, p.y, p.z, saplingID, saplingMeta, 3);      
    inventory.tryRemoveItems(saplingFilter, 1);    
    }
  }

@Override
protected TargetType validateWorkPoint(WorkPoint p)
  {
  return p==null ? TargetType.NONE : validateWorkPoint(p.x, p.y, p.z);
  }

protected TargetType validateWorkPoint(int x, int y, int z)
  {
  int id = worldObj.getBlockId(x, y, z);
  int meta = worldObj.getBlockMetadata(x, y, z);
  if( id==logID && (meta &3) == this.logMeta )
    {
    return TargetType.TREE_CHOP;
    }
  else if(id==0)
    {
    if(x%4==0 && z%4==0)
      {
      id = worldObj.getBlockId(x, y-1, z);
      if((id==Block.dirt.blockID || id==Block.grass.blockID) && inventory.containsAtLeast(saplingFilter, 1))
        {
        return TargetType.TREE_PLANT;
        }
      } 
    }
  return TargetType.NONE;
  }
}
