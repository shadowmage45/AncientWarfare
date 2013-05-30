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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.TEAWResearch;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;

public class ContainerResearch extends ContainerBase
{

TEAWResearch te;

/**
 * local references for slots for easier access of placement
 */
SlotResourceOnly bookSlot;
Slot[] researchSlots = new Slot[9];

ItemStack book = null;

/**
 * server side cached...compare vs TE to see when to update client 
 */
public IResearchGoal goal = null;
/**
 * cached progress value, compared vs. TE to see when changes need sent to client
 */
public int displayProgress = 0;

public PlayerEntry playerEntry = null;

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerResearch(EntityPlayer openingPlayer, TEAWResearch te)
  {
  super(openingPlayer, null);
  this.te = te;
  
  this.addPlayerSlots(player, 8, 135, 4);
  
  int posX = 8;
  int posY = 8+24;
  int slotNum = 0;
  this.bookSlot = new SlotResourceOnly(te, 0, posX, posY, Arrays.asList(new ItemStack(ItemLoader.researchBook)));
  this.bookSlot.setIgnoreType(3);
  this.bookSlot.setMaxStackSize(1);
  Slot slot;
  this.addSlotToContainer(bookSlot);
  for(int y = 0; y < 3; y++)
    {
    for(int x = 0; x <3; x++)
      {
      posX = 8 + x * 18;
      posY = 8+18+4+24 + y * 18;
      slotNum = y * 3 + x + 1;
      slot = new Slot(te, slotNum, posX, posY);
      this.researchSlots[slotNum-1]=slot;
      this.addSlotToContainer(slot);
      }
    }
  Slot s;
  for(Object o : this.inventorySlots)
    {
    s = (Slot)o;
    s.yDisplayPosition-=1000;
    }
  bookSlot.yDisplayPosition = 8+24;
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
  bookSlot.yDisplayPosition = 8+24;
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
  bookSlot.yDisplayPosition = 8+24;
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("set"))
    {
    this.addSlots();
    }
  if(tag.hasKey("remove"))
    {
    this.removeSlots();
    }
  if(tag.hasKey("progress"))
    {
    this.displayProgress = tag.getInteger("progress");
    }
  if(tag.hasKey("goal"))
    { 
    this.gui.refreshGui();
    if(!player.worldObj.isRemote)
      {
      int num = tag.getInteger("goal");
      if(num==-1){te.currentResearch = null;}
      else
        {
        te.startResearch(ResearchGoal.getGoalByID(tag.getInteger("goal")));
        }
      }
    else
      {
      int num = tag.getInteger("goal");
      if(num==-1){this.goal=null;}
      else
        {
        this.goal = ResearchGoal.getGoalByID(tag.getInteger("goal"));
        }
      }
    } 
  if(tag.hasKey("entry"))
    {    
    this.gui.refreshGui();
    boolean name = tag.getBoolean("entry");
    if(name)
      {
      this.playerEntry = new PlayerEntry();
      this.playerEntry.readFromNBT(tag);
      }
    else
      {
      this.playerEntry = null;
      }
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub

  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return Collections.emptyList();
  }

@Override
public void detectAndSendChanges()
  {
  if(te.researchProgress!=this.displayProgress)
    {
    Config.logDebug("sending display progress update");
    this.displayProgress = te.researchProgress;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("progress", this.displayProgress);
    this.sendDataToPlayer(tag);
    }
  if(te.currentResearch!=this.goal)
    {
    NBTTagCompound tag = new NBTTagCompound();
    if(te.currentResearch==null)
      {
      tag.setInteger("goal", -1);
      }
    else
      {
      tag.setInteger("goal", this.goal.getGlobalResearchNum());      
      }
    Config.logDebug("sending goal update");    
    this.sendDataToPlayer(tag);
    }
  if(this.playerEntry==null || !this.playerEntry.playerName.equals(te.researchingPlayer))
    {
    if(te.researchingPlayer!=null)
      {
      this.playerEntry = PlayerTracker.instance().getEntryFor(te.researchingPlayer);
      NBTTagCompound tag = this.playerEntry.getNBTTag();
      tag.setBoolean("entry", true);   
      this.sendDataToPlayer(tag);
      Config.logDebug("sending entry");
      }
    else
      {
      this.playerEntry = null;
      NBTTagCompound tag = new NBTTagCompound();
      tag.setBoolean("entry", false);   
      this.sendDataToPlayer(tag);
      Config.logDebug("sending empty entry");
      }
    }
  super.detectAndSendChanges();
  }

}
