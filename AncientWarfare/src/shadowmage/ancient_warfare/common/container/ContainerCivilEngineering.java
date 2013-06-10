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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.AWStructureModule;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.crafting.TEAWStructureCraft;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

public class ContainerCivilEngineering extends ContainerBase
{

TEAWStructureCraft te;
public boolean isWorking;
public ResourceListRecipe currentRecipe = null;
public ResourceListRecipe clientRecipe = null;
public int displayProgress;
public int displayProgressMax;

int resultSlotNum;
/**
 * @param openingPlayer
 * @param synch
 */
public ContainerCivilEngineering(EntityPlayer openingPlayer, TEAWStructureCraft te)
  {
  super(openingPlayer, null);
  this.te = te;
  this.addPlayerSlots(player, 8, 156, 4);

  int posX = 8;
  int posY = 8+24;
  int slotNum = 0;
  Slot slot;
  for(int y = 0; y < 3; y++)
    {
    for(int x = 0; x <6; x++)
      {
      posX = 8 + x * 18;
      posY = 8 + 18 + 4 + y * 18 + 18;
      slotNum = y * 6 + x;
      slot = new Slot(te, slotNum, posX, posY);
      this.addSlotToContainer(slot);
      }
    }
  slot = new SlotPullOnly(te, 18, 8 + 3* 18 + 27 , 8 + 18 + 4 + 3 * 18 + 27);
  this.resultSlotNum = this.inventorySlots.size();
  this.addSlotToContainer(slot);
  Slot s;
  for(Object o : this.inventorySlots)
    {
    s = (Slot)o;
    s.yDisplayPosition-=1000;
    }  
  }


@Override
public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotClickedIndex)
  {
  ItemStack slotStackCopy = null;
  Slot theSlot = (Slot)this.inventorySlots.get(slotClickedIndex);
  if (theSlot != null && theSlot.getHasStack())
    {
    ItemStack slotStack = theSlot.getStack();
    slotStackCopy = slotStack.copy();
    int invIndex = theSlot.getSlotIndex();
    
    int storageSlots = te.craftMatrix.length;    
    if (slotClickedIndex < 36)//player slots...
      {      
      if (!this.mergeItemStack(slotStack, 36, 36+storageSlots, false))//merge into storage inventory
        {
        return null;
        }
      }
    else if(slotClickedIndex >=36 &&slotClickedIndex < 36+storageSlots)//storage slots, merge to player inventory
      {
      if (!this.mergeItemStack(slotStack, 0, 36, true))//merge into player inventory
        {
        return null;
        }
      }
    else if(slotClickedIndex>0 && slotClickedIndex==resultSlotNum)
      {
      if (!this.mergeItemStack(slotStack, 0, 36, true))//merge into player inventory
        {
        return null;
        }
      }      
    if (slotStack.stackSize == 0)
      {
      theSlot.putStack((ItemStack)null);
      }
    else
      {
      theSlot.onSlotChanged();
      }
    if (slotStack.stackSize == slotStackCopy.stackSize)
      {
      return null;
      }
    theSlot.onPickupFromSlot(par1EntityPlayer, slotStack);
    }
  return slotStackCopy;
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
    this.displayProgress = tag.getInteger("prog");
    }
  if(tag.hasKey("progMax"))
    {
    this.displayProgressMax = tag.getInteger("progMax");
    }
  if(tag.hasKey("rec"))
    {
    this.currentRecipe = new ResourceListRecipe(tag.getCompoundTag("rec")); 
    this.clientRecipe = this.currentRecipe;
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }  
  if(tag.hasKey("recUp"))
    {
    this.handleRecipeUpdate(tag.getCompoundTag("recUp"));   
    }
  if(tag.hasKey("rem"))
    {
    this.currentRecipe = null;
    this.clientRecipe = null;
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }
  if(tag.hasKey("stop") && !player.worldObj.isRemote)
    {
    te.stopWorkAndClearRecipe();
    this.currentRecipe = null;
    this.clientRecipe = null;
    }
  if(tag.hasKey("set") && !player.worldObj.isRemote)
    {
    String name = tag.getString("set");   
    ResourceListRecipe recipe = AWCraftingManager.instance().getStructureRecipeFor(name);
    te.validateAndSetRecipe(recipe);
    }
  if(tag.hasKey("work"))
    {
    this.isWorking = tag.getBoolean("work");
    }
  }

protected void handleRecipeUpdate(NBTTagCompound tag)
  {
  ItemStackWrapperCrafting item = new ItemStackWrapperCrafting(tag);
  for(ItemStackWrapperCrafting craft : this.currentRecipe.getResourceList())
    {
    if(craft.matches(item))
      {
      craft.setRemainingNeeded(item.getRemainingNeeded());
      break;
      }
    }
  if(this.gui!=null)
    {
    this.gui.refreshGui();      
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
  if(this.displayProgress!=te.getWorkProgress())
    {
    this.displayProgress = te.getWorkProgress();
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("prog", displayProgress);
    this.sendDataToPlayer(tag);
    }
  if(this.displayProgressMax!=te.getWorkProgressMax())
    {
    this.displayProgressMax = te.getWorkProgressMax();
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("progMax", displayProgressMax);
    this.sendDataToPlayer(tag);
    }


  /**
   * check if current recipe is the same as TE recipe
   * if not, set current recipe to TE recipe and send complete packet
   * else check resource lists for differences, send diff
   *  
   */
  ResourceListRecipe ter = te.getRecipe();
  if(ter==null)
    {
    if(this.currentRecipe!=null)
      {
      this.currentRecipe = null;
      NBTTagCompound tag = new NBTTagCompound();   
      tag.setBoolean("rem", true);      
      this.sendDataToPlayer(tag);
      }   
    }
  else
    {
    if(this.currentRecipe==null || !this.currentRecipe.getDisplayName().equals(ter.getDisplayName()))
      {
      this.currentRecipe = te.getRecipe().copy();
      NBTTagCompound tag = new NBTTagCompound();
      tag.setCompoundTag("rec", this.currentRecipe.getNBTTag());      
      this.sendDataToPlayer(tag);
      }
    else
      {
      //      Config.logDebug("examining te recipe to match up to this.recipe resource list");
      List<ItemStackWrapperCrafting> curList = this.currentRecipe.getResourceList();
      List<ItemStackWrapperCrafting> teList = this.te.getRecipe().getResourceList();
      ItemStackWrapperCrafting item = null;
      for(int i = 0; i < curList.size(); i++)
        {
        int needed = curList.get(i).getRemainingNeeded();
        if(needed!=teList.get(i).getRemainingNeeded())
          {
          item = teList.get(i);
          curList.get(i).setRemainingNeeded(item.getRemainingNeeded());
          break;
          }
        }
      if(item!=null)
        {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setCompoundTag("recUp", item.writeToNBT(new NBTTagCompound()));
        this.sendDataToPlayer(tag);
        }
      }
    }

  if(this.isWorking!= te.isStarted)
    {
    this.isWorking = te.isStarted;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("work", this.isWorking);
    this.sendDataToPlayer(tag);
    }
  }



}
