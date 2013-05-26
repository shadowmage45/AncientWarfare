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

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIMoveToTarget;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

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
public byte getObjectiveNum()
  {
  return deposit_goods;
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIMoveToTarget(npc, 3, false));
  }

@Override
public void updatePriority()
  {
  targetInventory = null;
  WayPoint p = npc.wayNav.getDepositSite();
  boolean deposit = true;
  if(p==null || !p.isTargetLoaded(npc.worldObj))
    {
    deposit = false;
    } 
  else
    {    
    if(!p.isEntityEntry())
      {
      TileEntity te = p.getTileEntity(npc.worldObj);
      if(te==null || !(te instanceof IInventory))
        {
        deposit = false;
        npc.wayNav.setDepositSite(null);
        }
      else
        {
        if(targetInventory instanceof TileEntityChest)
          {
          targetInventory = Block.chest.getInventory(npc.worldObj, p.floorX(), p.floorY(), p.floorZ());
          }
        else
          {
          targetInventory = (IInventory)te;          
          }
        }
      }
    else if(p.isEntityEntry())
      {
      Entity ent = p.getEntity(npc.worldObj);
      if(ent==null)
        {
        deposit = false;
        npc.wayNav.setDepositSite(null);
        }
      else if(EntityMinecartChest.class.isAssignableFrom(ent.getClass()))
        {
        targetInventory = (IInventory)ent;
        }
      else if(ent instanceof VehicleBase)
        {
        VehicleBase veh = (VehicleBase)ent;
        if(veh.inventory.storageInventory.getSizeInventory()>0)
          {
          targetInventory = veh.inventory.storageInventory;
          }
        }
      }    
    }  
  if(deposit)
    {
    float percent = 1-npc.inventory.getPercentEmpty();
    int empty = npc.inventory.getEmptySlotCount();
    int full = npc.inventory.getSizeInventory() - empty;
    if(empty<=1)
      {
      deposit = true;
//      Config.logDebug(String.format("full inventory. E:%d, P:%.2f,  CP:%d", empty, percent, this.currentPriority));
      }
    else if(full<=2 && npc.inventory.getSizeInventory()>3)
      {
      deposit = false;
//      Config.logDebug(String.format("Mostly empty inventory. E:%d, P:%.2f,  CP:%d", empty, percent, this.currentPriority));
      }
    else
      {
      deposit = true;
      float pF = (float)this.maxPriority * percent;
      this.currentPriority = (int)pF;
//      Config.logDebug(String.format("Less than full inventory. E:%d, P:%.2f, PF:%.2f, CP:%d", empty, percent, pF, this.currentPriority));
      }    
    } 
  if(targetInventory==null)
    {
    deposit = false;
    }
  if(deposit)
    {
    if(this.currentPriority==0)
      {
      this.currentPriority = this.maxPriority;
      }
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
  WayPoint p = npc.wayNav.getDepositSite();
  if(targetInventory == null || p==null)
    {
    this.isFinished = true;
    this.currentPriority = 0;
    this.cooldownTicks = this.maxCooldownticks;
    return;
    }
  if(npc.getDistance(p.floorX(), p.floorY(), p.floorZ())>3.5)
    {
    //continue moving to target
    }
  else
    {
    //deposit as much stuff as possible
    ItemStack fromSlot = null;
    for(int i = 0; i < npc.inventory.getSizeInventory(); i++)
      {
      fromSlot = npc.inventory.getStackInSlotOnClosing(i);
      fromSlot = InventoryTools.tryMergeStack(targetInventory, fromSlot, -1);      
      if(fromSlot!=null)
        {
        npc.inventory.setInventorySlotContents(i, fromSlot);
        break;
        }
      }
    this.isFinished = true;
    this.cooldownTicks = this.maxCooldownticks;
    targetInventory.onInventoryChanged();
    npc.inventory.onInventoryChanged();
    }
  }

@Override
public void onObjectiveStart()
  {
  if(npc.wayNav.getDepositSite()!=null)
    {
    WayPoint p = npc.wayNav.getDepositSite();    
    npc.setTargetAW(p);   
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
  npc.clearPath();
  this.targetInventory = null;
  }

}
