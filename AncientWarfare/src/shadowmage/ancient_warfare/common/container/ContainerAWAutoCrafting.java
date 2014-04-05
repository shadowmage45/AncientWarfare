/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.TEAWAutoCrafting;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class ContainerAWAutoCrafting extends ContainerBase
{

TEAWAutoCrafting te;
public int displayProgress;
public int displayProgressMax;

/**
 * is TE currently processing a recipe?
 */
public boolean isWorking = false;

int resultSlotNum = -1;

public ItemStack result = null;
public ItemStack[] layoutMatrix = new ItemStack[9];
/**
 * @param openingPlayer
 * @param synch
 */
public ContainerAWAutoCrafting(EntityPlayer openingPlayer, TEAWAutoCrafting te)
  {
  super(openingPlayer, null);
  this.te = te;
  this.addPlayerSlots(player, 8, 156, 4);

  int posX = 8;
  int posY = 8+24;
  int slotNum = 0;
  Slot slot;
  int totalSlots = te.craftMatrix.length;
  int slotCount = 0;
  int columns = totalSlots / 3;
  if(totalSlots%3!=0){columns++;}  
  for(int y = 0; y < 3; y++)
    {
    for(int x = 0; x < columns; x++)
      {
      posX = 8 + x * 18;
      posY = 8 + 24 + 18 + 4 + 18 + 4;
      posY += y * 18;
      slotNum = y * columns + x;
      slot = new Slot(te, slotNum, posX, posY);
      this.addSlotToContainer(slot);
      slotCount++;
      }
    }
  if(te.resultSlot!=null && te.resultSlot.length>0)
    {
    slot = new SlotPullOnly(te, te.resultSlot[0], 8 + 3* 18 + 18 , 8 + 24 + 18 + 4 + 18 + 4 + 18);
    resultSlotNum = this.inventorySlots.size();
    this.addSlotToContainer(slot);
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
  if(tag.hasKey("time"))
    {
    this.displayProgress = tag.getInteger("time");
    }  
  if(tag.hasKey("timeMax"))
    {
    this.displayProgressMax = tag.getInteger("timeMax");
    }
  if(tag.hasKey("stop") && !player.worldObj.isRemote)
    {
    te.stopAndClear();
    }
  if(tag.hasKey("set") && !player.worldObj.isRemote)
    {
    int slot = tag.getInteger("setSlot");
    ItemStack item = null;
    if(tag.hasKey("setItem"))
      {
      item = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("setItem"));
      }    
    te.setLayoutMatrixSlot(slot, item);
    }
  if(tag.hasKey("layout") && player.worldObj.isRemote)//client side full-matrix init
    {
    /*
     * TODO clean this up to not xmit whole matrix, only slot(s) changed
     */
    this.layoutMatrix = new ItemStack[9];
    NBTTagList list = tag.getTagList("layout");
    for(int i = 0; i < list.tagCount(); i++)
      {
      NBTTagCompound slotTag = (NBTTagCompound) list.tagAt(i);
      ItemStack item = ItemStack.loadItemStackFromNBT(slotTag.getCompoundTag("setItem"));     
      int slot = slotTag.getInteger("setSlot");
      this.layoutMatrix[slot] = item;
      }
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }
  if(tag.hasKey("work"))
    {
    this.isWorking = tag.getBoolean("work");
    }
  if(tag.hasKey("result"))
    {    
    this.result = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("result"));
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }
  if(tag.hasKey("resultClear"))
    {
    this.result = null;
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }
  }

public void handleLayoutSlotClick(int slot, ItemStack stack)
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setBoolean("set", true);
  tag.setInteger("setSlot", slot);
  if(stack!=null)
    {
    tag.setCompoundTag("setItem", stack.writeToNBT(new NBTTagCompound()));
    }  
  this.sendDataToServer(tag);
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
  NBTTagCompound tag = null;  
  if(this.displayProgress!=te.getWorkProgress())
    {
    if(tag==null){tag = new NBTTagCompound();}
    this.displayProgress = te.getWorkProgress();
    tag.setInteger("time", displayProgress);
    }  
  if(this.displayProgressMax!=te.getWorkProgressMax())
    {
    if(tag==null){tag = new NBTTagCompound();}
    this.displayProgressMax = te.getWorkProgressMax();
    tag.setInteger("timeMax", displayProgressMax);
    }
  if(this.isWorking!= te.isWorking())
    {
    if(tag==null){tag = new NBTTagCompound();}
    this.isWorking = te.isWorking();
    tag.setBoolean("work", this.isWorking);
    } 
  ItemStack a;
  ItemStack b;
  boolean sendMatrix = false;
  for(int i = 0; i < 9 ; i++)
    {
    a = this.layoutMatrix[i];
    b = te.getLayoutMatrixSlot(i);
    if(a==null && b==null){continue;}
    else if(!InventoryTools.doItemsMatch(a, b))
      {
      sendMatrix = true;
      if(b!=null){this.layoutMatrix[i] = b.copy();}
      else{this.layoutMatrix[i] = b;}
      }
    }
  if(sendMatrix)
    {
    if(tag==null){tag = new NBTTagCompound();}
    NBTTagList list = new NBTTagList();
    NBTTagCompound itemTag;
    for(int i = 0; i < 9 ; i++)
      {
      a = this.layoutMatrix[i];    
      if(a==null){continue;}
      itemTag = new NBTTagCompound();
      itemTag.setInteger("setSlot", i);
      itemTag.setCompoundTag("setItem", a.writeToNBT(new NBTTagCompound()));
      list.appendTag(itemTag);      
      }
    tag.setTag("layout", list);
    }
  if(this.result != te.result)
    {
    if(tag==null){tag = new NBTTagCompound();}
    this.result = te.result;
    if(this.result!=null)
      {
      tag.setCompoundTag("result", this.result.writeToNBT(new NBTTagCompound()));      
      }
    else
      {
      tag.setBoolean("resultClear", true);
      }
    }
  te.setCanUpdate();
  if(tag!=null)
    {
    this.sendDataToPlayer(tag);
    }
  }


}
