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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.civics.TECivicTownHall;
import shadowmage.ancient_warfare.common.civics.TECivicWarehouse;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.ai.tasks.AIDismountVehicle;
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
public byte getObjectiveNum()
  {
  return upkeep;
  }

@Override
public void addTasks()
  {
  this.aiTasks.add(new AIDismountVehicle(npc));
  this.aiTasks.add(new AIMoveToTarget(npc, 3, false));
  }

@Override
public void updatePriority()
  {
  if(npc.npcUpkeepTicks==0 && npc.teamNum<16)
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
  if(this.upkeepTarget==null || npc.getTargetType()!=TargetType.UPKEEP)
    {
    this.checkForUpkeepTarget();
    if(this.upkeepTarget==null || npc.getTargetType()!=TargetType.UPKEEP)
      {
      return;
      }
    }
  if(npc.getDistanceFromTarget(npc.getTarget()) < 4)
    {
    this.attemptUpkeepWithdrawal();
    }   
  }

protected void attemptWithdrawalTownHall(TECivicTownHall te)
  {
  Config.logDebug("doing town-hall upkeep");
  int neededValue = npc.npcType.getUpkeepCost(npc.rank);
  boolean doWithdrawal = false;
  boolean foundExtra = false;
  int foundValue = InventoryTools.getFoodValue(te, 0, te.getSizeInventory()-1);  
  if(foundValue>=neededValue)
    {
    doWithdrawal = true;
    }

  ItemStack extra = npc.npcType.getUpkeepAdditionalItem(npc.rank);
  if(doWithdrawal)    
    {
    if(extra!=null)
      {
      if(InventoryTools.containsAtLeast(te, extra, extra.stackSize, 0, te.getSizeInventory()-1))
        {
        doWithdrawal = true;
        InventoryTools.tryRemoveItems(te, extra, extra.stackSize, 0, te.getSizeInventory()-1);
        }
      else
        {
        doWithdrawal = false;
        }
      }
    }  
  if(doWithdrawal)
    {
    InventoryTools.tryRemoveFoodValue(te, 0, te.getSizeInventory()-1, neededValue);
    npc.npcUpkeepTicks = Config.npcUpkeepTicks;
    this.setFinished();
    }
  else
    {
    Config.logDebug("attempting town-hall adjacent warehouse upkeep");
    TECivicWarehouse warehouse = te.getWarehousePosition();
    if(warehouse==null){return;}
    foundValue = InventoryTools.getFoodValue(warehouse, 0, warehouse.getSizeInventory()-1);
    if(foundValue>=neededValue)
      {
      doWithdrawal = true;
      }
    if(doWithdrawal)    
      {
      if(extra!=null)
        {
        if(InventoryTools.containsAtLeast(warehouse, extra, extra.stackSize, 0, warehouse.getSizeInventory()-1))
          {
          doWithdrawal = true;
          InventoryTools.tryRemoveItems(warehouse, extra, extra.stackSize, 0, warehouse.getSizeInventory()-1);
          }
        else
          {
          doWithdrawal = false;
          }
        }
      }  
    if(doWithdrawal)
      {
      InventoryTools.tryRemoveFoodValue(warehouse, 0, warehouse.getSizeInventory()-1, neededValue);
      npc.npcUpkeepTicks = Config.npcUpkeepTicks;
      this.setFinished();
      }
    }
  }

protected void attemptUpkeepWithdrawal()
  {
  if(upkeepTarget instanceof TECivicTownHall)
    {
    this.attemptWithdrawalTownHall((TECivicTownHall)upkeepTarget);
    return;
    }
  int neededValue = npc.npcType.getUpkeepCost(npc.rank);
  boolean doWithdrawal = false;
  int foundValue = InventoryTools.getFoodValue(this.upkeepTarget, 0, this.upkeepTarget.getSizeInventory()-1);  
  if(foundValue>=neededValue)
    {
    doWithdrawal = true;
    }
  if(doWithdrawal)    
    {
    ItemStack extra = npc.npcType.getUpkeepAdditionalItem(npc.rank);
    if(extra!=null)
      {
      if(InventoryTools.containsAtLeast(upkeepTarget, extra, extra.stackSize, 0, upkeepTarget.getSizeInventory()-1))
        {
        InventoryTools.tryRemoveItems(upkeepTarget, extra, extra.stackSize, 0, upkeepTarget.getSizeInventory()-1);
        }
      else
        {
        doWithdrawal = false;
        }
      }
    }  
  if(doWithdrawal)
    {
    InventoryTools.tryRemoveFoodValue(upkeepTarget, 0, upkeepTarget.getSizeInventory()-1, neededValue);
    npc.npcUpkeepTicks = Config.npcUpkeepTicks;
    upkeepTarget.onInventoryChanged();
    this.setFinished();
    }
  }

protected void checkForUpkeepTarget()
  {
  WayPoint p = npc.wayNav.getUpkeepSite();
  if(p==null)
    {
    this.upkeepTarget = null;
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
