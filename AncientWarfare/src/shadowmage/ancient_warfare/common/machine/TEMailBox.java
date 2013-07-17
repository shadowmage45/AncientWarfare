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
package shadowmage.ancient_warfare.common.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.inventory.AWInventoryMailbox;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.tracker.MailboxData;
import shadowmage.ancient_warfare.common.tracker.entry.BoxData;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.Trig;

public class TEMailBox extends TEMailBoxBase
{

int[][] sideSlotIndices = new int[4][4];
int[] topIndices = new int[4];
int[] bottomIndices = new int[18];

/**
 * 
 */
public TEMailBox()
  {
  this.guiNumber = GUIHandler.MAILBOX;
  this.mailBoxSize = 38;
  this.inventory = new AWInventoryMailbox(mailBoxSize, null);
  int index = 0;  
  for(int i = 0; i < 18; i++, index++)
    {
    bottomIndices[i]=index;
    }
  for(int i = 0; i < 4; i++, index++)
    {
    topIndices[i] = index;
    }  
  for(int i = 0; i < 4; i++)
    {
    for(int k = 0; k <4; k++, index++)
      {
      sideSlotIndices[i][k]=index;
      }
    }
  }

/************************************************UPDATE METHODS*************************************************/

@Override
public void updateEntity()
  {
  if(this.worldObj==null || this.worldObj.isRemote || this.boxData==null){return;}
  this.mailTicks++;
  if(this.mailTicks>=Config.mailSendTicks)
    {
    this.mailTicks = 0;
    /**
     * check each side destination in data, if valid destination, send packages
     */
    String destination = null;
    int[] slots;
    int slot;
    ItemStack stack;
    BoxData data;
    for(int i = 1; i <6; i++)
      {
      destination = this.boxData.getSideName(i);      
      if(destination==null){continue;}      
      data=MailboxData.instance().getBoxDataFor(destination, mailBoxSize);
      if(data==null || !data.isAssigned()){continue;}
      slots = i==1? this.topIndices : this.sideSlotIndices[i-2];
      for(int k = 0; k < slots.length; k++)
        {
        slot = slots[k];
        stack = this.getStackInSlot(slot);
        if(stack==null){continue;}
        
        float dist = Trig.getDistance(xCoord, yCoord, zCoord, data.posX(), data.posY(), data.posZ());
        data.addIncomingItem(stack, (int)dist*5);
        this.setInventorySlotContents(slot, null);
        Config.logDebug("adding stack to mail route for: "+destination + " time: "+((int)dist*5));
        }
      } 
    }
  }

/************************************************SIDED INVENTORY METHODS*************************************************/

@Override
public int[] getAccessibleSlotsFromSide(int side)
  {
  if(this.boxData==null)
    {
    return new int[0];
    }
  if(side==0)
    {
    return bottomIndices;
    }
  else if(side==1)
    {
    return topIndices;
    }
  ForgeDirection d = ForgeDirection.getOrientation(side);
  if(d==facingDirection)
    {
    return sideSlotIndices[0];
    }
  if(d==BlockTools.getRight(facingDirection))
    {
    return sideSlotIndices[1];
    }
  if(d== facingDirection.getOpposite())
    {
    return sideSlotIndices[2];
    }
  if(d== BlockTools.getLeft(facingDirection))
    {
    return sideSlotIndices[3];
    }
  return this.bottomIndices;
  }

/************************************************INVENTORY METHODS*************************************************/

}
