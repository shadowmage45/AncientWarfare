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
package shadowmage.ancient_warfare.common.civics.worksite.te.tree;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import shadowmage.ancient_warfare.common.civics.worksite.TEWorkSite;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.utils.Trig;

public abstract class TETreeFarm extends TEWorkSite
{

int logID = Block.wood.blockID;
int logMeta = 0;
int saplingID = Block.sapling.blockID;
int saplingMeta = 0;
int maxSearchHeight = 16;
ItemStack saplingFilter;
ItemStack logFilter;
ItemStack bonemealFilter = new ItemStack(Item.dyePowder,1,15);
List<BlockPosition> treeBasePositions = new ArrayList<BlockPosition>();

/**
 * 
 */
public TETreeFarm()
  {
  this.renderBounds = true;
  }

@Override
protected void onCivicUpdate()
  {
  super.onCivicUpdate();
  List<EntityItem> entities = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(minX-1, minY-1, minZ-1, maxX+2, maxY+2, maxZ+2));
  ItemStack stack;
  if(entities!=null && overFlow.isEmpty())
    {
    for(EntityItem ent : entities)
      {
      if(ent!=null && ent.getEntityItem()!=null)
        {
        stack = ent.getEntityItem();
        if((stack.itemID==saplingID && stack.getItemDamage()==saplingMeta))
          {
          stack = tryAddItemToInventory(stack, resourceSlotIndices, regularIndices);
          if(stack!=null)
            {
            ent.setEntityItemStack(stack);
            }
          else//stack is null/merged sucessfully
            {
            ent.setDead();
            }
          }
        else if( stack.itemID==Item.appleRed.itemID)
          {
          if(inventory.canHoldItem(stack, stack.stackSize, regularIndices))
            {
            stack = inventory.tryMergeItem(stack, regularIndices);
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
  return AxisAlignedBB.getAABBPool().getAABB(minX, maxY, minZ, maxX+1, maxY+1+maxSearchHeight, maxZ+1);
  }

@Override
protected void scan()
  {
  TargetType t;
  int id;
  int meta;
  this.treeBasePositions.clear();
  this.workPoints.clear();
  for(int y = this.minY; y <=this.maxY; y++)
    {
    for(int x = this.minX; x<=this.maxX; x++)
      {
      for(int z = this.minZ; z<=this.maxZ; z++)
        {
        id = worldObj.getBlockId(x, y, z);
        meta = worldObj.getBlockMetadata(x, y, z);
        if(id==0 && x%4==0 && z%4==0)
          {
          id=worldObj.getBlockId(x, y-1, z);
          if(id==Block.dirt.blockID || id==Block.grass.blockID)
            {
            this.addWorkPoint(x, y, z, TargetType.TREE_PLANT);
            }
          }
        else if(id==this.saplingID && meta==this.saplingMeta && inventory.containsAtLeast(bonemealFilter, 3))
          {
          this.addWorkPoint(x, y, z, TargetType.TREE_BONEMEAL);
          }
        else if(id==this.logID && (meta &3) == this.logMeta)
          {
          boolean skip = false;
          for(BlockPosition pos : this.treeBasePositions)
            {
            if(pos.x==x && pos.z==z)
              {
              skip = true;
              break;
              }
            }
          if(!skip)
            {
            List<Node> treeBlocks = TreeFinder.instance().findTreeNodes(worldObj, x, y, z, logID, logMeta);
            this.treeBasePositions.add(new BlockPosition(x,y,z));
            for(Node n : treeBlocks)
              {
              this.addWorkPoint(n.x, n.y, n.z, TargetType.TREE_CHOP);              
              }
            }
          }
        }
      }
    }
  }

@Override
protected void doWork(IWorker npc, WorkPoint p)
  {
  if(p.work==TargetType.TREE_CHOP)
    {
    List<ItemStack> drops = BlockTools.breakBlock(worldObj, p.x, p.y, p.z, 0);   
    for(ItemStack item : drops)
      {      
      if(this.resourceFilterContains(item))
        {
        InventoryTools.tryMergeStack(this, item, 1);
        }
      item = InventoryTools.tryMergeStack(this, item, 2);
      if(item!=null)
        {
        this.overFlow.add(item);
        }
      }
    }  
  else if(p.work==TargetType.TREE_PLANT && inventory.containsAtLeast(saplingFilter, 1))
    { 
    worldObj.setBlock(p.x, p.y, p.z, saplingID, saplingMeta, 3);      
    inventory.tryRemoveItems(saplingFilter, 1);    
    }
  else if(p.work==TargetType.TREE_BONEMEAL && inventory.containsAtLeast(bonemealFilter, 3))
    {
    if(worldObj.getBlockId(p.x, p.y, p.z)==this.saplingID && worldObj.getBlockMetadata(p.x, p.y, p.z)==this.saplingMeta)
      {
      BlockSapling block = (BlockSapling) Block.blocksList[this.saplingID];
      if(block!=null)
        {
        block.growTree(worldObj, p.x, p.y, p.z, worldObj.rand);
        this.inventory.tryRemoveItems(bonemealFilter, 3);
        }
      }
    }
  }

@Override
protected TargetType validateWorkPoint(WorkPoint p)
  {
  TargetType t = TargetType.NONE;
  if(p!=null)
    {
    t = validateWorkPoint(p.x, p.y, p.z);    
    }
  return t;
  }

protected TargetType validateWorkPoint(int x, int y, int z)
  {
  int id = worldObj.getBlockId(x, y, z);
  int meta = worldObj.getBlockMetadata(x, y, z);  
  boolean hasSapling = inventory.containsAtLeast(saplingFilter, 1);  
  if( id==logID && (meta &3) == this.logMeta)
    {    
    return TargetType.TREE_CHOP;
    }
  else if(id==0 && y<=this.maxY && Trig.isBetween(x, minX, maxX) && Trig.isBetween(z, minZ, maxZ))
    {
    if(x%4==0 && z%4==0)
      {
      id = worldObj.getBlockId(x, y-1, z);
      if((id==Block.dirt.blockID || id==Block.grass.blockID) && hasSapling)
        {
        return TargetType.TREE_PLANT;
        }
      } 
    }
  else if(id==saplingID && meta==saplingMeta && inventory.containsAtLeast(bonemealFilter, 3))
    {
    return TargetType.TREE_BONEMEAL;
    }
  return TargetType.NONE;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  if(tag.hasKey("workPointList"))
    {
    NBTTagList points = tag.getTagList("workPointList");
    NBTTagCompound pointTag;
    WorkPoint point;
    for(int i = 0; i < points.tagCount(); i++)
      {
      pointTag = (NBTTagCompound) points.tagAt(i);
      point = WorkPoint.constructFromNBT(pointTag);
      this.workPoints.add(point);
      }
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  if(!this.workPoints.isEmpty())
    {
    NBTTagList pointList = new NBTTagList();
    for(WorkPoint p : this.workPoints)
      {
      pointList.appendTag(p.writeToNBT(new NBTTagCompound()));
      }
    tag.setTag("workPointList", pointList);
    }
  
  }


}
