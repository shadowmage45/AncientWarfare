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
package shadowmage.ancient_warfare.common.civics.worksite.te.mine;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.civics.worksite.TEWorkSite;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class TEMine extends TEWorkSite
{

int currentLevelNum = -1;
int minYLevel = 5;//the lowest
int levelHeight = 4;
int mineRescanTicks = 0;
int mineRescanMax = (20/Config.npcAITicks) * 10;
boolean initialized = false;
boolean mineFinished = false;
MineLevel currentLevel = null;

ItemStack fillerFilter = new ItemStack(Block.cobblestone,1);
ItemStack ladderFilter = new ItemStack(Block.ladder, 1);
ItemStack torchFilter = new ItemStack(Block.torchWood, 1);

public TEMine()
  {
  this.renderBounds = true;
  }

@Override
protected void onCivicUpdate()
  {
  mineRescanTicks++;
  if(!mineFinished && initialized && mineRescanTicks>=mineRescanMax)
    {
    mineRescanTicks = 0;
    this.workPoints.clear();
    this.loadLevel(currentLevelNum);
    }
  super.onCivicUpdate(); 
  }

public void handleLadderAction(IWorker npc,  WorkPoint m)
  {
  int id = worldObj.getBlockId(m.x, m.y, m.z);
  if(id!=0)
    {
    handleClearAction(npc, m);
    }
  if(inventory.containsAtLeast(ladderFilter, 1))
    {
    worldObj.setBlock(m.x, m.y, m.z, Block.ladder.blockID, (int)m.special, 3);
    inventory.tryRemoveItems(ladderFilter, 1);
    }
  }

public void handleTorchAction(IWorker npc, WorkPoint m)
  {
  int id = worldObj.getBlockId(m.x, m.y, m.z);
  boolean airBelow = worldObj.isAirBlock(m.x, m.y-1, m.z);
  if(id!=0)    
    {
    handleClearAction(npc, m);
    }
  if(!airBelow && inventory.containsAtLeast(torchFilter, 1))
    {
    worldObj.setBlock(m.x, m.y, m.z, Block.torchWood.blockID, 5, 3);  
    inventory.tryRemoveItems(torchFilter, 1);
    }
  }

public void handleClearAction(IWorker npc, WorkPoint m)
  {
  this.handleBlockBreak(npc, m.x, m.y, m.z);
  }

public void handleFillAction(IWorker npc, WorkPoint m)
  {  
  int id = worldObj.getBlockId(m.x, m.y, m.z);
  if(id!=0)
    {
    this.handleBlockBreak(npc, m.x, m.y, m.z);
    }
  if(inventory.containsAtLeast(fillerFilter, 1))
    {
    worldObj.setBlock(m.x, m.y, m.z, Block.cobblestone.blockID, 0,3);
    inventory.tryRemoveItems(fillerFilter, 1);
    }
  }

public boolean handleBlockBreak(IWorker npc, int x, int y, int z)
  {
  List<ItemStack> invDrops = new ArrayList<ItemStack>();
  TileEntity te = worldObj.getBlockTileEntity(x, y, z);
  if(te instanceof IInventory)
    {
    IInventory inv = (IInventory)te;
    ItemStack stack;
    for(int i = 0; i < inv.getSizeInventory(); i++)
      {
      stack = inv.getStackInSlot(i);
      if(stack!=null)
        {
        invDrops.add(stack);
        inv.setInventorySlotContents(i, null);
        }
      }
    }

  List<ItemStack> drops = BlockTools.breakBlock(worldObj, x, y, z, 0);
  if(drops!=null)
    {
    drops.addAll(invDrops);
    for(ItemStack drop : drops)
      {
      if(this.resourceFilterContains(drop))
        {
        this.tryAddItemToInventory(drop, resourceSlotIndices, regularIndices);
        }
      else
        {
        this.tryAddItemToInventory(drop, regularIndices);
        }
      }
    return true;
    }
  return false;
  }

protected void loadLevel(int level)
  { 
  if(level<0)
    {
    return;
    }
  this.currentLevelNum = level;
  int adjTopY = this.minY - (levelHeight * level);//the top of the level
  int adjMinY = adjTopY-(levelHeight-1);  
  this.currentLevel = getNewLevel(minX, adjMinY, minZ, maxX - minX + 1, levelHeight, maxZ - minZ + 1);
  this.currentLevel.initializeLevel(this, worldObj);
  this.workPoints.addAll(this.currentLevel.workList);
  }

protected MineLevel getNewLevel(int x, int y, int z, int xSize, int ySize, int zSize)
  {
  return new MineLevelClassic(x, y, z, xSize, ySize, zSize);
  }

public boolean hasTorch()
  {
  return inventory.containsAtLeast(torchFilter, 1);
  }

public boolean hasFiller()
  {
  return inventory.containsAtLeast(fillerFilter, 1);
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.mineFinished = tag.getBoolean("mineDone");
  if(!mineFinished)
    {
    this.currentLevelNum = -1;
    initialized = false;
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setBoolean("mineDone", this.mineFinished);
  }

@Override
protected void scan()
  {
  if(!initialized)
    {      
    this.initialized = true;
    this.loadLevel(0);      
    }  
  if(this.currentLevel!=null && !mineFinished && this.workPoints.isEmpty())//load next level
    {
    int adjTopY = this.minY - (currentLevel.levelSize * (currentLevelNum+1));//the top of the level
    if(adjTopY-currentLevel.levelSize-1 >= this.minYLevel)
      {
      this.loadLevel(currentLevelNum+1);
      }
    else
      {
      this.mineFinished = true;
      }
    }  
  }

@Override
protected void doWork(IWorker npc, WorkPoint p)
  {
  switch(p.work)
    {
    case MINE_CLEAR:
    handleClearAction(npc, p);
    break;
    case MINE_FILL:
    handleFillAction(npc, p);
    break;
    case MINE_LADDER:
    handleLadderAction(npc, p);
    break;
    case MINE_TORCH:   
    handleTorchAction(npc, p);
    break;
    }
  }

@Override
protected TargetType validateWorkPoint(WorkPoint p)
  {
  return p==null ? TargetType.NONE : validateWorkPoint(p.x, p.y, p.z, p.work);
  }

protected TargetType validateWorkPoint(int x, int y, int z, TargetType t)
  {
  switch(t)
  {
  case MINE_CLEAR:
  if(worldObj.getBlockId(x, y, z)!=0)
    {
    return t;
    }
  break;
  
  case MINE_LADDER:
  if(worldObj.getBlockId(x, y, z)!=Block.ladder.blockID)
    {
    if(inventory.containsAtLeast(ladderFilter, 1))
      {
      return t;
      }
    }
  break;
  
  case MINE_TORCH:
  if(worldObj.getBlockId(x, y, z)!=Block.torchWood.blockID)
    {
    if(inventory.containsAtLeast(torchFilter, 1))
      {
      return t;
      }    
    }
  break;
  
  case MINE_FILL:  
  if(worldObj.getBlockId(x, y, z)!=Block.cobblestone.blockID)
    {
    if(inventory.containsAtLeast(fillerFilter, 1))
      {
      return t;
      }
    }
  break;
  }  
  return TargetType.NONE;
  }

}
