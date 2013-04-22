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

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.utils.TargetType;

public class AIDepositGoods extends NpcAIObjective
{

IInventory targetInventory = null;

/**
 * @param npc
 * @param maxPriority
 */
public AIDepositGoods(NpcBase npc, int maxPriority)
  {
  super(npc, maxPriority);
  this.maxCooldownticks = (20/Config.npcAITicks) * 10; // ten second cooldown on trying to deposit things
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 2, false));
  }

@Override
public void updatePriority()
  {
  if(npc.wayNav.getDepositPoint()==null)
    {
    this.currentPriority = 0;
    this.cooldownTicks = this.maxCooldownticks;
    return;
    } 
  else
    {
//    Config.logDebug("had deposit site, validating");
    WorkPoint p = npc.wayNav.getDepositPoint();
    TileEntity te = npc.worldObj.getBlockTileEntity(p.floorX(), p.floorY(), p.floorZ());
    if(te==null)
      {
//      Config.logDebug("no te at deposit site!");
      this.npc.wayNav.clearDepositPoint();
      this.currentPriority = 0;
      this.cooldownTicks = this.maxCooldownticks;
      return;
      }
    else if (te instanceof IInventory)
      {
//      Config.logDebug("te found at deposit site that impliments inventory");
//      int availSlots = InventoryTools.getEmptySlots(targetInventory, 0, targetInventory.getSizeInventory()-1);
//      if(availSlots <= 0)
//        {
//        Config.logDebug("te did not have any open slots");
//        this.currentPriority = 0;
//        this.cooldownTicks = 40;
//        this.isFinished = true;
//        return;
//        }
      }
    else
      {
//      Config.logDebug("was a TE but not inventory!");
      this.npc.wayNav.clearDepositPoint();
      this.currentPriority = 0;
      this.cooldownTicks = this.maxCooldownticks;
      return;
      }
//    Config.logDebug("site was valid inventory");
    }
  float percent = 1-npc.inventory.getPercentEmpty();
  int empty = npc.inventory.getEmptySlotCount();
  int full = npc.inventory.getSizeInventory() - empty;
  if(empty<=1)
    {
    this.currentPriority = this.maxPriority;
//    Config.logDebug(String.format("full inventory. E:%d, P:%.2f,  CP:%d", empty, percent, this.currentPriority));
    }
  else if(full<=2 && npc.inventory.getSizeInventory()>3)
    {
    this.currentPriority = 0;
//    Config.logDebug(String.format("Mostly empty inventory. E:%d, P:%.2f,  CP:%d", empty, percent, this.currentPriority));
    }
  else
    {
    float pF = (float)this.maxPriority * percent;
    this.currentPriority = (int)pF;
//    Config.logDebug(String.format("Less than full inventory. E:%d, P:%.2f, PF:%.2f, CP:%d", empty, percent, pF, this.currentPriority));
    }
  }

@Override
public void onRunningTick()
  {
  if(npc.wayNav.getDepositPoint()==null)
    {
    this.isFinished = true;
    this.currentPriority = 0;
    this.cooldownTicks = this.maxCooldownticks;
    return;
    }
  WorkPoint p = npc.wayNav.getDepositPoint();
  if(npc.getDistance(p.floorX(), p.floorY(), p.floorZ())>3)
    {
//    Config.logDebug("moving to deposit target");
    //continue moving to target
    }
  else
    {
    //deposit as much stuff as possible
    ItemStack fromSlot = null;
    for(int i = 0; i < npc.inventory.getSizeInventory(); i++)
      {
//      Config.logDebug("depositing into deposit target");
      fromSlot = npc.inventory.getStackInSlotOnClosing(i);
      fromSlot = InventoryTools.tryMergeStack(targetInventory, fromSlot, 0, targetInventory.getSizeInventory()-1);      
      if(fromSlot!=null)
        {
//        Config.logDebug("deposit target could not hold all items!!");
        npc.inventory.setInventorySlotContents(i, fromSlot);
        break;
        }
      }
    this.isFinished = true;
    this.cooldownTicks = this.maxCooldownticks;
    }
  }

@Override
public void onObjectiveStart()
  {
  if(npc.wayNav.getDepositPoint()!=null)
    {
    WorkPoint p = npc.wayNav.getDepositPoint();    
    TileEntity te = npc.worldObj.getBlockTileEntity(p.floorX(), p.floorY(), p.floorZ());
    if(te instanceof IInventory)
      {
      npc.setTargetAW(npc.targetHelper.getTargetFor(p.floorX(), p.floorY(), p.floorZ(), TargetType.WORK));
      this.targetInventory = (IInventory)te;
      }
    else
      {
      npc.wayNav.clearDepositPoint();
      this.isFinished = true;
      this.currentPriority = 0;
      this.cooldownTicks = this.maxCooldownticks;
      }    
    }
  else
    {
    this.isFinished = true;
    this.currentPriority = 0;   
    this.cooldownTicks = this.maxCooldownticks; 
    }
  }

@Override
public void stopObjective()
  {
  npc.setTargetAW(null);
  this.targetInventory = null;
  }

}
