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

import java.util.ArrayList;

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
int minYLevel = 36;//the lowest
int levelHeight = 4;
int shaftX;//used to determine ladder-meta for shafts..
int shaftZ;
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
    if(!initialized)
      {
      Config.logDebug("initializing mine!");
      
      this.initialized = true;
      shaftX = minX -1 + (maxX-minX+1)/2;
      shaftZ = minZ -1 + (maxZ-minZ+1)/2;
      this.loadLevel(0);      
      }
    if(this.currentLevel!=null && !mineFinished && !this.currentLevel.hasWork())
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
      Config.logDebug("mine is finished");
      }
    } 
  super.updateEntity();
  }

@Override
public WorkPoint getWorkPoint(NpcBase npc)
  {
  Config.logDebug("npc requesting work: "+npc);
  if(this.currentLevel!=null && this.currentLevel.hasWork())
    {
    MinePoint p = this.currentLevel.getNextMinePoint();
    if(p!=null)
      {
      return new WorkPointMine(p);
      }
    }
  return null;
  }

@Override
public boolean canAssignWorkPoint(NpcBase npc, WorkPoint p)
  {
  Config.logDebug("can assign work:");
  // TODO NOOP
  return true;
  }

@Override
public void onWorkFinished(NpcBase npc, WorkPoint point)
  {
  if(this.currentLevel!=null && point instanceof WorkPointMine)
    {
    WorkPointMine p = (WorkPointMine)point;
    MinePoint m = p.minePoint;   
    this.currentLevel.onPointFinished(p.minePoint);
    }
  super.onWorkFinished(npc, point);
  }

@Override
public WorkPoint doWork(NpcBase npc, WorkPoint p)
  {
  p.incrementHarvestHits();
  if(p.shouldFinish())//hits were enough to trigger 'finish'
    {
    WorkPointMine m = (WorkPointMine)p;
    MinePoint mp = m.minePoint;
    switch(mp.currentAction)
      {
      case MINE_CLEAR_THEN_LADDER:
      return handleLadderBlock(npc, m, mp);
      case MINE_CLEAR_BRANCH:
      return handleTunnelOrBranch(npc, m, mp);
      case MINE_CLEAR_TUNNEL:
      return handleTunnelOrBranch(npc, m, mp);
      case MINE_CLEAR_THEN_FILL:
      return handleClearThenFill(npc, m, mp);
      default:
      return null;
      }
    }
  return p;
  }

protected WorkPointMine handleLadderBlock(NpcBase npc, WorkPointMine p, MinePoint m)
  {
  int id = npc.worldObj.getBlockId(m.x, m.y, m.z);
  if(id==0)
    {
    //TODO figure out ladder meta...
    //z+ = 2
    //z- = 3
//    npc.worldObj.setBlock(m.x, m.y, m.z, Block.ladder.blockID);
    if(m.z<=shaftZ)
      {
      npc.worldObj.setBlockAndMetadata(m.x, m.y, m.z, Block.ladder.blockID, 3);
      }
    else
      {
      npc.worldObj.setBlockAndMetadata(m.x, m.y, m.z, Block.ladder.blockID, 2);
      }
    inventory.tryRemoveItems(torchFilter, 1);
    }
  else if(id!=Block.ladder.blockID)
    {
    //clear block
    handleBlockBreak(npc, p, m);
    p.resetHarvestTicks();
    return p;
    }
  return null;
  }

protected WorkPointMine handleClearThenTorch(NpcBase npc, WorkPointMine p, MinePoint m)
  {
  int id = npc.worldObj.getBlockId(m.x, m.y, m.z);
  if(id==0)
    {
    //TODO figure out ladder meta...
    //z+ = 2
    //z- = 3
    npc.worldObj.setBlockAndMetadata(m.x, m.y, m.z, Block.ladder.blockID, 5);
    inventory.tryRemoveItems(ladderFilter, 1);
    }
  else if(id!=Block.torchWood.blockID)
    {
    //clear block
    handleBlockBreak(npc, p, m);
    p.resetHarvestTicks();
    return p;
    }
  return null;
  }

protected WorkPointMine handleClearThenFill(NpcBase npc, WorkPointMine p, MinePoint m)
  {
  int id = npc.worldObj.getBlockId(m.x, m.y, m.z);
  if(id==0)
    {    
    //fill block
    npc.worldObj.setBlock(m.x, m.y, m.z, Block.cobblestone.blockID);
    if(npc.inventory.containsAtLeast(fillerFilter, 1))
      {
      npc.inventory.tryRemoveItems(fillerFilter, 1);
      }
    else if(inventory.containsAtLeast(fillerFilter, 1))
      {
      inventory.tryRemoveItems(fillerFilter, 1);
      }
    else
      {
      //NFC
      }    
    }
  else if(id != Block.cobblestone.blockID)
    {
    //clear block
    handleBlockBreak(npc, p, m);
    p.resetHarvestTicks();
    return p;
    }
  //if not cleared
  //  clear block
  //  reset hit counter
  //  add block to TE/NPC inventory
  //  return work point
  //else if block not filled
  //  fill block
  //  remove fill from NPC/TE inventory
  //  call onFinished
  //  return null
  return null;
  }

protected WorkPointMine handleTunnelOrBranch(NpcBase npc, WorkPointMine p, MinePoint m)
  {
  if(handleBlockBreak(npc, p, m))
    {
    return null;
    }
  return null;
  }

protected boolean handleBlockBreak(NpcBase npc, WorkPointMine p, MinePoint m)
  {
  int id = npc.worldObj.getBlockId(m.x, m.y, m.z);
  Block block = Block.blocksList[id];
  if(id!=0 && id!= Block.bedrock.blockID && block!=null)
    {
    ArrayList<ItemStack> drops = block.getBlockDropped(npc.worldObj, m.x, m.y, m.z, npc.worldObj.getBlockMetadata(m.x, m.y, m.z), 0);
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
    npc.worldObj.setBlock(m.x, m.y, m.z, 0);
    return true;
    }
  return false;
  }

@Override
public void onWorkFailed(NpcBase npc, WorkPoint point)
  {
  if(this.currentLevel!=null && point instanceof WorkPointMine)
    {
    this.currentLevel.addMinePointEntry(((WorkPointMine)point).minePoint);
    }
  super.onWorkFailed(npc, point);
  }

@Override
public void updateWorkPoints()
  {
  if(this.currentLevel!=null)
    {
    this.currentLevel.validatePoints(worldObj);
    } 
  }

@Override
public boolean hasWork(NpcBase npc)
  {
//  Config.logDebug("hasWork: " + (currentLevel!=null && currentLevel.hasWork()));
  return (currentLevel!=null && currentLevel.hasWork());
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
  Config.logDebug("loading level: "+level);
  if(level<0)
    {
    return;
    }
  this.currentLevelNum = level;
  int adjTopY = this.minY - 4 * level;//the top of the level
  int adjMinY = adjTopY-3;  
  this.currentLevel = new MineLevel(minX, adjMinY, minZ, maxX - minX + 1, 4, maxZ - minZ + 1);
  this.currentLevel.initializeLevel(worldObj);
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.currentLevelNum = tag.getInteger("mineLevel");
  if(tag.hasKey("mineLevelData"))
    {
    this.currentLevel = new MineLevel(tag.getCompoundTag("mineLevelData"));
    }
  else
    {
    this.loadLevel(currentLevelNum);
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setBoolean("mineInit", this.initialized);
  tag.setBoolean("mineDone", this.mineFinished);
  tag.setInteger("mineLevel", this.currentLevelNum);
  tag.setInteger("shaftX", shaftX);
  tag.setInteger("shaftZ", shaftZ);
  if(this.currentLevel!=null)
    {
    tag.setCompoundTag("mineLevelData", this.currentLevel.getNBTTag());
    }
  
  }


}
