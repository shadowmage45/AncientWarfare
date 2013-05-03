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
package shadowmage.ancient_warfare.common.npcs.ai.objectives;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class AINpcUpkeepObjective extends NpcAIObjective
{

IInventory upkeepTarget;

/**
 * @param npc
 * @param maxPriority
 */
public AINpcUpkeepObjective(NpcBase npc, int maxPriority)
  {
  super(npc, maxPriority);
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 3, false));
  }

@Override
public void updatePriority()
  {
  if(npc.npcUpkeepTicks==0)
    {
    this.currentPriority = this.maxPriority;
    }
  else
    {
    this.currentPriority = 0;
    this.cooldownTicks = this.maxCooldownticks;
    }   
  }

@Override
public void onRunningTick()
  {
  if(npc.npcUpkeepTicks!=0)//only draw upkeep if ticks==0 (-1 == never draw upkeep, >0 == still has food)
    {
    this.currentPriority = 0;
    return;
    }
  if(npc.getTargetType()==TargetType.UPKEEP)
    {
    if(npc.getDistanceFromTarget(npc.getTarget())<3.5)
      {
      this.attemptUpkeepWithdrawal();
      }
    else
      {
      //too far away, keep moving closer
      }
    }
  else
    {
    this.checkForUpkeepTarget();
    }
  }

protected void attemptUpkeepWithdrawal()
  {
  int foundValue = 0;
  int neededValue = npc.npcType.getUpkeepCost(npc.rank);
  boolean doWithdrawal = false;
  ItemStack stack;
  for(int i = 0; i < this.upkeepTarget.getSizeInventory(); i++)
    {
    stack = this.upkeepTarget.getStackInSlot(i);
    if(stack!=null && stack.getItem() instanceof ItemFood)
      {
      foundValue += ((ItemFood)stack.getItem()).getHealAmount() * stack.stackSize;
      }
    if(foundValue>=neededValue)
      {
      ItemStack extra = npc.npcType.getUpkeepAdditionalItem(npc.rank);
      if(extra!=null)
        {
        if(InventoryTools.containsAtLeast(upkeepTarget, extra, extra.stackSize, 0, upkeepTarget.getSizeInventory()-1))
          {
          doWithdrawal = true;
          InventoryTools.tryRemoveItems(upkeepTarget, extra, extra.stackSize, 0, upkeepTarget.getSizeInventory()-1);
          }
        }
      break;
      }
    }
  if(doWithdrawal)
    {
    int withdrawnAmount = 0;
    for(int i = 0; i < this.upkeepTarget.getSizeInventory(); i++)
      {
      stack = this.upkeepTarget.getStackInSlot(i);
      if(stack!=null && stack.getItem() instanceof ItemFood)
        {
        int perItem = ((ItemFood)stack.getItem()).getHealAmount();
        while(withdrawnAmount<npc.npcType.getUpkeepCost(npc.rank) && stack.stackSize>0)
          {
          withdrawnAmount += perItem;
          stack.stackSize--;
          }
        if(stack.stackSize<=0)
          {
          upkeepTarget.setInventorySlotContents(i, null);
          }
        if(withdrawnAmount>=neededValue)
          {
          break;
          }
        }
      }    
    this.setFinished();
    }
  }

protected void checkForUpkeepTarget()
  {
  WayPoint p = npc.wayNav.getUpkeepSite();
  if(p==null)
    {
    this.upkeepTarget = null;    
    //check from aggro broadcast list?
    
    }
  else
    {
    npc.setTargetAW(p);
    if(p.isEntityEntry())
      {
      Entity ent = p.getEntity(npc.worldObj);
      if(ent instanceof IInventory)
        {
        upkeepTarget = (IInventory)ent;
        }
      }
    else
      {
      TileEntity te = p.getTileEntity(npc.worldObj);
      if(te instanceof IInventory)
        {
        upkeepTarget = (IInventory)te;
        }      
      }
    }
  }

@Override
public void onObjectiveStart()
  {
  this.checkForUpkeepTarget();
  }

@Override
public void stopObjective()
  {
  this.upkeepTarget = null;
  this.npc.clearPath();
  }

}
