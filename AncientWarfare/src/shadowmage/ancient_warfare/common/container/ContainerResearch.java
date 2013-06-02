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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.crafting.TEAWResearch;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

public class ContainerResearch extends ContainerBase
{

TEAWResearch te;

/**
 * local references for slots for easier access of placement
 */
SlotResourceOnly bookSlot;
public Slot[] researchSlots = new Slot[9];

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
 * server side value to detect changes made to player entry
 */
int researchLength = 0;
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
      posX = 8+27 + x * 18;
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

public void handleGoalSelectionClient(IResearchGoal goal)
  {
  if(goal==null || this.goal!=null){return;}
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("goal", goal.getGlobalResearchNum());
  this.sendDataToServer(tag);
  }

public void handleGoalStopClient()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("goal", -1);
  this.sendDataToServer(tag);
  }

protected void handleGoalSelectionServer(IResearchGoal goal)
  {    
  if(this.te.currentResearch!=null || goal==null){return;}
  boolean start = true;
  List<ItemStackWrapperCrafting> neededItems = goal.getResearchResources();
  ItemStack fromInv = null;
  ResourceListRecipe recipe = new ResourceListRecipe(new ItemStack(Item.appleRed), RecipeType.NONE);
  recipe.addResources(neededItems);
  if(!recipe.doesInventoryContainResources(te, te.getAccessibleSlotsFromSide(2)))
    {
    start = false;
    }
  if(start)
    {
    recipe.removeResourcesFrom(te, te.getAccessibleSlotsFromSide(2));   
    this.goal = goal;
    this.te.startResearch(goal); 
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("goal", goal.getGlobalResearchNum());
    this.sendDataToPlayer(tag);
    }  
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
    if(!player.worldObj.isRemote)
      {
      int num = tag.getInteger("goal");
      if(num==-1)
        {        
        te.currentResearch = null;
        te.resetProgress();
        /**
         * TODO refund items..throw on ground if cannot fit in inventory
         */
        }
      else
        {
        this.handleGoalSelectionServer(ResearchGoal.getGoalByID(tag.getInteger("goal")));
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
    if(this.gui!=null)
      {
      this.gui.refreshGui();      
      }
    } 
  if(tag.hasKey("entry"))
    {    
    if(this.gui!=null)
      {
      this.gui.refreshGui();      
      }
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
  super.detectAndSendChanges();
  if(this.player.worldObj.isRemote){return;}
  if(te.researchProgress!=this.displayProgress)
    {
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
      this.goal = null;
      }
    else
      {
      this.goal = te.currentResearch;
      tag.setInteger("goal", this.goal.getGlobalResearchNum());      
      }  
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
      this.researchLength = this.playerEntry.getKnownResearch().size();
      }
    else
      {
      if(this.playerEntry==null && te.researchingPlayer==null)
        {
        return;
        }
      this.playerEntry = null;
      NBTTagCompound tag = new NBTTagCompound();
      tag.setBoolean("entry", false);   
      this.sendDataToPlayer(tag);
      }
    }
  else if(this.playerEntry!=null && this.playerEntry.playerName.equals(te.researchingPlayer))
    {
    int len = this.playerEntry.getKnownResearch().size();
    if(len!=this.researchLength)
      {
      NBTTagCompound tag = this.playerEntry.getNBTTag();
      tag.setBoolean("entry", true);   
      this.sendDataToPlayer(tag);
      this.researchLength = len;
      }    
    }
  }

}
