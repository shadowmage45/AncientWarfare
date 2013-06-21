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
package shadowmage.ancient_warfare.common.civics;

import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IWorker;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class TECivicTownHall extends TECivic
{

int foodValue = 0;
ItemStack rationFilter;

public TECivicTownHall()
  {
  this.hasWork = true;
  this.broadcastWork = true;
  rationFilter = new ItemStack(ItemLoader.rations);
  }

@Override
public void updateEntity()
  {  
  super.updateEntity();
  if(worldObj==null || worldObj.isRemote)
    {
    return;
    }
  if(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
    {
    int index;
    int foodValue = 0;
    ItemStack stack;
    for(int i = 0; i < this.otherSlotIndices.length; i++)
      {
      index = this.otherSlotIndices[i];
      stack = this.getStackInSlot(index);
      if(stack==null || stack.itemID == ItemLoader.rations.itemID || !(stack.getItem() instanceof ItemFood)){continue;}
      foodValue = ((ItemFood)stack.getItem()).getHealAmount();
      this.foodValue+=foodValue;
      stack.stackSize--;
      if(stack.stackSize<=0)
        {
        this.setInventorySlotContents(index, null);
        }
      break;
      }    
    }
  while(this.foodValue>=2 && this.inventory.canHoldItem(rationFilter, 1))
    {
    InventoryTools.tryMergeStack(inventory, rationFilter.copy(), otherSlotIndices);
    this.foodValue -=2;
    }
  }

@Override
public boolean hasWork()
  {
  return true;
  }

@Override
protected void updateHasWork()
  {  
  //noop
  }

@Override
protected void setHasWork(boolean newVal)
  {
  //noop
  }

@Override
public boolean canHaveMoreWorkers(IWorker npc)
  {  
  return true;
  }

@Override
protected void validateWorkers()
  {
  Iterator<IWorker> workIt = this.workers.iterator();
  IWorker npc = null;
  while(workIt.hasNext())
    {
    npc = workIt.next();
    if(npc==null || npc.isDead() || npc.getDistanceFrom(xCoord, yCoord, zCoord)>Config.npcAISearchRange)
      {      
      workIt.remove();
      continue;
      }
    WayPoint p = npc.getUpkeepPoint();
    if(p==null || p.floorX()!= xCoord || p.floorY()!=yCoord || p.floorZ()!=zCoord || worldObj.getBlockTileEntity(p.floorX(), p.floorY(), p.floorZ())!=this)
      {
      workIt.remove();
      continue;
      }    
    }
  }

@Override
public void broadcastWork(int maxRange)
  {
  if(this.worldObj==null || this.worldObj.isRemote)
    {
    return;
    }
  AxisAlignedBB bb = AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1).expand(maxRange, maxRange/2, maxRange);
  List<NpcBase> npcList = worldObj.getEntitiesWithinAABB(NpcBase.class, bb);
  for(NpcBase npc : npcList)
    {
    if(isHostile(npc.teamNum))      
      {
      if(npc.npcType.isCombatUnit())
        {
        npc.targetHelper.handleTileEntityTargetBroadcast(this, TargetType.ATTACK_TILE, Config.npcAITicks*11);
        }      
      }
    else
      {
      if(broadcastWork)
        {    
        if(npc.wayNav.getUpkeepSite() == null && npc.teamNum == this.teamNum)
          {
          npc.wayNav.setUpkeepSite(new WayPoint(this, 1, TargetType.UPKEEP));
          }
        }
      }
    }
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.foodValue = tag.getInteger("foodValue");
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setInteger("foodValue", this.foodValue);
  }


}
