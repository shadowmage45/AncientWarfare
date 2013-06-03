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
package shadowmage.ancient_warfare.common.container;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.crafting.TEAWEngineering;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;

public class ContainerEngineeringStation extends ContainerBase
{

TEAWEngineering te;
public int displayProgress = 0;
public int displayProgressMax = 0;
public boolean isWorking = false;
public ResourceListRecipe currentRecipe;

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerEngineeringStation(EntityPlayer openingPlayer, TEAWEngineering te)
  {
  super(openingPlayer, null);
  this.te = te;
  this.addPlayerSlots(player, 8, 135, 4);
  
  int posX = 8;
  int posY = 8+24;
  int slotNum = 0;
  Slot slot;
  for(int y = 0; y < 3; y++)
    {
    for(int x = 0; x <3; x++)
      {
      posX = 8+27 + x * 18;
      posY = 8+18+4+24 + y * 18;
      slotNum = y * 3 + x;
      slot = new Slot(te, slotNum, posX, posY);
      this.addSlotToContainer(slot);
      }
    }
  slot = new SlotPullOnly(te, 9, 120, 8+18+4+24+18);
  this.addSlotToContainer(slot);
  Slot s;
  for(Object o : this.inventorySlots)
    {
    s = (Slot)o;
    s.yDisplayPosition-=1000;
    }  
  }

public void addSlots()
  {
  Slot s;
  for(Object o : this.inventorySlots)
    {
    s = (Slot)o;
    if(s.yDisplayPosition<0)
      {
      s.yDisplayPosition+=1000;      
      }
    }
  }

public void removeSlots()
  {
  Slot s;
  for(Object o : this.inventorySlots)
    {
    s = (Slot)o;
    if(s.yDisplayPosition>=0)
      {
      s.yDisplayPosition-=1000;      
      }
    }
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("prog"))
    {
    Config.logDebug("receiving display progress update");
    this.displayProgress = tag.getInteger("prog");
    }
  if(tag.hasKey("progMax"))
    {
    Config.logDebug("receiving display MAX update");
    this.displayProgressMax = tag.getInteger("progMax");
    }
  if(tag.hasKey("rec"))
    {
    Config.logDebug("receiving recipe update");
    this.currentRecipe = new ResourceListRecipe(tag.getCompoundTag("rec"));
    }
  if(tag.hasKey("rem"))
    {
    Config.logDebug("receiving clear recipe update");
    this.currentRecipe = null;
    }
  if(tag.hasKey("stop") && !player.worldObj.isRemote)
    {
    Config.logDebug("receiving server stop work command");
    te.stopWork();
    te.clearRecipe();
    }
  if(tag.hasKey("set") && !player.worldObj.isRemote)
    {
    Config.logDebug("receiving server set recipe command");
    te.validateAndSetRecipe(new ResourceListRecipe(tag.getCompoundTag("set")));
    }
  if(tag.hasKey("work"))
    {
    Config.logDebug("receiving work update");
    this.isWorking = tag.getBoolean("work");
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return Collections.emptyList();
  }

@Override
public void detectAndSendChanges()
  {
  super.detectAndSendChanges();
	if(this.player.worldObj.isRemote){return;}
  if(this.displayProgress!=te.displayProgress)
    {
    this.displayProgress = te.displayProgress;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("prog", displayProgress);
    this.sendDataToPlayer(tag);
    Config.logDebug("sending progress update");
    }
  if(this.displayProgressMax!=te.displayProgressMax)
    {
    this.displayProgressMax = te.displayProgressMax;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("progMax", displayProgressMax);
    this.sendDataToPlayer(tag);
    Config.logDebug("sending progress MAX update");
    }
  if(this.currentRecipe != te.getRecipe())
    {
    this.currentRecipe = te.getRecipe();
    NBTTagCompound tag = new NBTTagCompound();
    if(this.currentRecipe!=null)
      {
      tag.setCompoundTag("rec", this.currentRecipe.getNBTTag());
      }
    else
      {
      tag.setBoolean("rem", true);
      }
    this.sendDataToPlayer(tag);
    Config.logDebug("sending recipe update to player");
    }
  if(te.getRecipe()!=null && PlayerTracker.instance().getEntryFor(player).hasDoneResearch(te.getRecipe().getNeededResearch()))
    {
    te.shouldUpdate = true;
//    Config.logDebug("setting work table should update--true");
    }
  if(this.isWorking!= te.isWorking)
    {
    this.isWorking = te.isWorking;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("work", this.isWorking);
    this.sendDataToPlayer(tag);
    }
  }



}