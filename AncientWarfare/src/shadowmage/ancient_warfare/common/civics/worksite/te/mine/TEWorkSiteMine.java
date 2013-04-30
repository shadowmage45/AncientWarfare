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
package shadowmage.ancient_warfare.common.civics.worksite.te.mine;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class TEWorkSiteMine extends TECivic
{

/**
 *  I'm thinking it should map by level, keep the current level map in memory
 *    as a basic 3D array of bytes/shorts denoting action to be taken on that block
 *    /whether an action needs to be taken
 *    --when scanning for work-points, instead of scanning the world, you scan the in-
 *    memory array, which has custom tailored information for the path to be taken and
 *    action taken (both smaller lookup space, and no need to go through 'world' to get there)
 * 
 *  Mines will need a certain amount of either:
 *    logs,
 *    planks,
 *    sticks, or
 *    ladders
 *    --for construction of their central shaft.  At least one slot in the inventory
 *        should be preserved for ladders
 *  
 *  Mines will maintain fill material in their inventory slots.  When a worker harvests a point,
 *    if it is fill material and the mine is low, it will go into mine inventory, otherwise it
 *    will go into worker inventory.
 *    
 *  Mine should layout itself such that there is a central 2*2 vertical shaft, with ladders
 *    on the N/S walls.
 *  From this shaft should be one main E/W 2*2 horizontal x-axis tunnel at every level
 *  From these E/W shafts will come basic 2h*1w branch mining shafts
 *  
 *    
 */

int currentLevelNum = -1;
int minYLevel = 5;//the lowest
int levelHeight = 4;
int mineRescanTicks = 0;
boolean initialized = false;
boolean mineFinished = false;
MineLevel currentLevel = null;

ItemStack fillerFilter = new ItemStack(Block.cobblestone,1);
ItemStack ladderFilter = new ItemStack(Block.ladder, 1);
ItemStack torchFilter = new ItemStack(Block.torchWood, 1);

public TEWorkSiteMine()
  {
  }

@Override
public void updateEntity()
  {
  if(worldObj!=null && !worldObj.isRemote)
    {    
    this.mineRescanTicks++;
    if(!initialized)
      {
      Config.logDebug("initializing mine");      
      this.initialized = true;
      this.loadLevel(0);      
      }
    if(!mineFinished && this.mineRescanTicks > Config.npcAITicks * 50)//250 ticks, 12 1/2 seconds...
      {
      Config.logDebug("INITIATING FULL MINE RESCAN");
      this.loadLevel(currentLevelNum);
      }
    if(this.currentLevel!=null && !mineFinished && !this.currentLevel.hasWork())//load next level
      {
      Config.logDebug("loading next level");
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
    else if(this.currentLevel==null)
      {
      Config.logDebug("current level was null!!");
      //uhh..maybe check if finished and re-init the level or something
      }
    else if(mineFinished)
      {
//      Config.logDebug("mine is finished");
      }
    } 
  super.updateEntity();
  }

@Override
public WorkPoint getWorkPoint(NpcBase npc)
  {
  if(this.currentLevel!=null && this.currentLevel.hasWork())
    {    
    return new WorkPoint(xCoord, yCoord, zCoord, TargetType.WORK, this);    
    }
  return null;
  }

@Override
public void onWorkFinished(NpcBase npc, WorkPoint p)
  {
  if(this.currentLevel!=null)
    {
    this.currentLevel.onWorkCompleted(this, npc);
    }  
  }

@Override
public WorkPoint doWork(NpcBase npc, WorkPoint p)
  {
  p.incrementHarvestHits();
  if(p.shouldFinish())//hits were enough to trigger 'finish'
    {
    this.onWorkFinished(npc, p);
    return null;
    }
  return p;
  }

public void handleLadderAction(NpcBase npc,  MinePoint m)
  {
  int id = npc.worldObj.getBlockId(m.x, m.y, m.z);
  if(id==0)
    {
    npc.worldObj.setBlock(m.x, m.y, m.z, Block.ladder.blockID, (int)m.special, 3);
    inventory.tryRemoveItems(ladderFilter, 1);
    }
  }

public void handleTorchAction(NpcBase npc,  MinePoint m)
  {
  int id = npc.worldObj.getBlockId(m.x, m.y, m.z);
  if(id==0)
    {
    npc.worldObj.setBlock(m.x, m.y, m.z, Block.torchWood.blockID, 5, 3);
    inventory.tryRemoveItems(torchFilter, 1);
    }
  }

public void handleClearAction(NpcBase npc, MinePoint m)
  {
  this.handleBlockBreak(npc, m.x, m.y, m.z);
  }

public void handleFillAction(NpcBase npc, MinePoint m)
  {
  int id = npc.worldObj.getBlockId(m.x, m.y, m.z);
  if(id==0)
    {
    npc.worldObj.setBlock(m.x, m.y, m.z, Block.cobblestone.blockID, 0,3);
    inventory.tryRemoveItems(fillerFilter, 1);
    }
  }

public boolean handleBlockBreak(NpcBase npc, int x, int y, int z)
  {
  List<ItemStack> drops = BlockTools.breakBlock(npc.worldObj, x, y, z, 0);
  if(drops!=null)
    {
    for(ItemStack drop : drops)
      {
      //  if filler block (cobble) && TE can hold more filler (keep room for ladders)
      //  else add to NPC inventory
      //check and see if te has space, else drop on ground
      if(InventoryTools.doItemsMatch(drop, fillerFilter) && inventory.getCountOf(fillerFilter)<128 && inventory.canHoldItem(drop, drop.stackSize))
        {
        drop = inventory.tryMergeItem(drop);
        if(drop!=null)
          {
          InventoryTools.dropItemInWorld(npc.worldObj, drop, xCoord, yCoord, zCoord);
          }
        }
      else
        {
        drop = npc.inventory.tryMergeItem(drop);
        if(drop!=null)
          {
          InventoryTools.dropItemInWorld(npc.worldObj, drop, xCoord, yCoord, zCoord);
          }
        }
      }
    return true;
    }
  return false;
  }

@Override
public void onWorkFailed(NpcBase npc, WorkPoint point)
  {
 
  }

@Override
public void updateWorkPoints()
  {
  
  }

@Override
protected void updateHasWork()
  {
  hasWork = false;
  if(currentLevel!=null && currentLevel.hasWork())
    {
    hasWork = true;
    }
  }

@Override
public boolean onInteract(World world, EntityPlayer player)
  {
  if(!world.isRemote)
    {
    GUIHandler.instance().openGUI(GUIHandler.CIVIC_BASE, player, world, xCoord, yCoord, zCoord);
    }
  return true;
  }

protected void loadLevel(int level)
  { 
  if(level<0)
    {
    return;
    }
  this.mineRescanTicks = 0;
  this.currentLevelNum = level;
  int adjTopY = this.minY - 4 * level;//the top of the level
  int adjMinY = adjTopY-3;  
  this.currentLevel = new MineLevelClassic(minX, adjMinY, minZ, maxX - minX + 1, 4, maxZ - minZ + 1);
  this.currentLevel.initializeLevel(worldObj);
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
//  this.initialized = tag.getBoolean("mineInit");
  this.mineFinished = tag.getBoolean("mineDone");
  if(!mineFinished)
    {
    this.currentLevelNum = -1;
    initialized = false;
    }
//  if(tag.hasKey("mineLevelData"))
//    {
//    this.currentLevel = new MineLevel(tag.getCompoundTag("mineLevelData"));
//    }
//  else
//    {
//    this.initialized = false;
//    this.mineFinished = false;
//    //force re-init...
//    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setBoolean("mineDone", this.mineFinished);
//  if(this.currentLevel!=null)
//    {
//    tag.setCompoundTag("mineLevelData", this.currentLevel.getNBTTag());
//    }  
  }

}
