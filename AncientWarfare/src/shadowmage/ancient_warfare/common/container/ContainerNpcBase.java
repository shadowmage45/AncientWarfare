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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.waypoints.CourierRoutingInfo;

public class ContainerNpcBase extends ContainerBase
{
NpcBase npc;

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerNpcBase(EntityPlayer openingPlayer, NpcBase npc)
  {
  super(openingPlayer, null);
  this.npc = npc;
  int y;
  int x;
  int slotNum;
  int xPos; 
  int yPos;

  this.addPlayerSlots(openingPlayer, 8, 158, 4);   
  this.addArmorSlots();
//  IInventory te = npc.inventory;
//  Config.logDebug("setting npc inventory. size: "+te.getSizeInventory());
//  for(y = 0; y < te.getSizeInventory()/9; y++)
//    {
//    for(x = 0; x < 9; x++)
//      {
//      slotNum = y*9 + x;
//      if(slotNum<te.getSizeInventory())
//        {
//        xPos = 8 + x * 18;
//        yPos = y * 18 + 15;
//        if(slotNum>=27)
//          {
//          xPos = -1000;
//          yPos = -1000;
//          }
//        Slot slot = new Slot(te, slotNum, xPos, yPos);
//        this.addSlotToContainer(slot);        
//        }
//      }
//    }  
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
    int storageSlots = npc.npcType.getInventorySize(npc.rank);    
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
  if(tag.hasKey("repack") && !npc.worldObj.isRemote)
    {
    npc.repackIntoItem();
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
public boolean canInteractWith(EntityPlayer var1)
  {
  return super.canInteractWith(var1) && var1!=null && var1.getDistanceToEntity(npc) < 5.d;
  }

protected void addArmorSlots()
  {
  IInventory inv = npc.armorInventory;
  int x = 8, y = 40;
  for(int i = 0; i <4; i++)
    {    
    this.addSlotToContainer(new SlotArmor(player, inv, 3-i, x, y, i));
    x+=18;
    }
  }

}
