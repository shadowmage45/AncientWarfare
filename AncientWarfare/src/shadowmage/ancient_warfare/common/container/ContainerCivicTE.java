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
import shadowmage.ancient_warfare.common.civics.TECivic;

public class ContainerCivicTE extends ContainerBase
{


TECivic teBase;
public int regSlotY;
public boolean regLabel = false;
public int resSlotY;
public boolean resLabel = false;
public int specSlotY;
public boolean specLabel = false;
public int playerSlotsY;

/**
 * 
 * @param openingPlayer
 * @param synch
 */
public ContainerCivicTE(EntityPlayer openingPlayer, TECivic te)
  {
  super(openingPlayer, null);
  this.teBase = te;
  
  int invSize = teBase.getCivic().getInventorySize();
  regLabel = invSize>0 ? true : false;
  regSlotY = 4 + 10 + (regLabel ? 10: 0);//border, civic label, inventory label
  int regSlotHeight = (invSize/9 + (invSize%9==0 ? 0 : 1)) *18;  
  
//  Config.logDebug("regSLotY: "+regSlotY + " H: "+regSlotHeight);
  
  invSize = teBase.getCivic().getResourceSlotSize();
  resLabel = invSize > 0 ? true : false;
  resSlotY = regSlotY+regSlotHeight + (resLabel ? 14 : 0);
  int resSlotHeight = (invSize/9 + (invSize%9==0 ? 0 : 1)) *18;   
  
//  Config.logDebug("resSlotY: "+resSlotY + " H: "+resSlotHeight);
  
  invSize = teBase.getCivic().getSpecResourceSlotSize();
  specLabel = invSize > 0 ? true : false;
  specSlotY = resSlotY+resSlotHeight + (specLabel ? 14 : 0);
  int specSlotHeight = (invSize/9 + (invSize%9==0 ? 0 : 1)) *18; 
  
  
  
  playerSlotsY = specSlotY+specSlotHeight+4+10;//bottom of spec slot + buffer + label
  
//  Config.logDebug("specLabel: "+specLabel + " specSlotY: "+specSlotY + " specH: "+specSlotHeight +  " playerSlotY: "+playerSlotsY);
  
  this.addPlayerSlots(openingPlayer, 8, playerSlotsY, 4);//158   
  this.addResourceSlots(resSlotY);
  this.addRegularSlots(regSlotY);  
  this.addSpecSlots(specSlotY);
  }

protected void addRegularSlots(int yStart)
  {
  int y;
  int x;
  int slotNum;
  int xPos; 
  int yPos;  
  
  for(int i = teBase.getCivic().getResourceSlotSize(); i < teBase.getCivic().getInventorySize()+teBase.getCivic().getResourceSlotSize(); i++)
    {
    x = (i-teBase.getCivic().getResourceSlotSize()) %9;
    y = (i-teBase.getCivic().getResourceSlotSize()) /9;
    slotNum = i;
    xPos = 8 + x * 18;
    yPos = y*18+yStart;
    Slot slot = new Slot(teBase, slotNum, xPos, yPos);
    this.addSlotToContainer(slot);            
    }  
  }

protected void addResourceSlots(int yStart)
  {
  int y;
  int x;
  int slotNum;
  int xPos; 
  int yPos;  
  for(y = 0; y < teBase.getCivic().getResourceSlotSize(); y++)
    {
    for(x = 0; x < 9; x++)
      {
      slotNum = y*9 + x;
      xPos = 8 + x * 18;
      yPos = y * 18 + yStart;
      if(slotNum<teBase.getCivic().getResourceSlotSize())
        {
        Slot slot = new SlotResourceOnly(teBase, slotNum, xPos, yPos, teBase.getCivic().getResourceItemFilters());
        this.addSlotToContainer(slot);   
        }     
      }
    }
  }

protected void addSpecSlots(int yStart)
  {
  int y = 0;
  int x;
  int slotNum = teBase.getCivic().getResourceSlotSize()+teBase.getCivic().getInventorySize();
  int xPos; 
  int yPos = y*18+yStart; 
  for(x = 0; x < teBase.getCivic().getSpecResourceSlotSize(); x++)
    {
    xPos = 8 + x * 18;
    Slot slot = new SlotResourceOnly(teBase, slotNum, xPos, yPos, teBase.getCivic().getSpecResourceItemFilters());
    this.addSlotToContainer(slot);   
    slotNum++;
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
    int storageSlots = teBase.getCivic().getTotalInventorySize();    
    if (slotClickedIndex < 36)//player slots...
      {      
      if (!this.mergeItemStack(slotStack, 36, 36+storageSlots, false))//merge into storage inventory
        {
        return null;
        }
      }
    else if(slotClickedIndex >=36 &&slotClickedIndex < 36+storageSlots)//vehicle slots, merge to player inventory
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


@Override
public void handlePacketData(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub

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
public boolean canInteractWith(EntityPlayer var1)
  {
  return super.canInteractWith(var1) && var1!=null && var1.getDistance(teBase.xCoord+0.5d, teBase.yCoord, teBase.zCoord+0.5d) < 5.d;
  }


}
