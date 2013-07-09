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

import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TEMailBox extends TEMachine implements IInventory, ISidedInventory
{

AWInventoryBasic inventory = new AWInventoryBasic(38);
int[][] sideSlotIndices = new int[4][4];
int[] topIndices = new int[4];
int[] bottomIndices = new int[18];

String boxName = null;
String topDestination = null;
String[] sideDestinations = new String[4];


/**
 * 
 */
public TEMailBox()
  {
  this.machineNumber = 1;
  this.canUpdate = true;
  this.guiNumber = GUIHandler.MAILBOX;
  
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

/************************************************BOX INTERACTION METHODS*************************************************/

public String getBoxName()
  {
  return this.boxName;
  }

public void setBoxName(String name)
  {
  this.boxName = name;
  }

public String getBoxName(int side)
  {
  switch(side)
  {
  case 0:
  return this.boxName;
  case 1:
  return this.topDestination;
  case 2:
  case 3:
  case 4:
  case 5:
  return this.sideDestinations[BlockTools.getCardinalFromSide(side)];
  default:
  return null;
  }
  }

public void setBoxName(int side, String name)
  {
  switch(side)
  {
  case 0:
  this.boxName = name;
  break;
  
  case 1:
  this.topDestination = name;
  break;
  
  case 2:
  case 3:
  case 4:
  case 5:
  this.sideDestinations[BlockTools.getCardinalFromSide(side)]=name;
  }
  }

/************************************************DATA SYNCH METHODS*************************************************/

@Override
public void readFromNBT(NBTTagCompound tag)
  {  
  super.readFromNBT(tag);
  if(tag.hasKey("boxName"))
    {
    this.boxName = tag.getString("boxName");    
    }
  if(tag.hasKey("topName"))
    {
    this.topDestination = tag.getString("topName");
    }
  for(int i = 0; i < 4; i++)
    {
    if(tag.hasKey(String.valueOf("sideName"+i)))
      {
      this.sideDestinations[i] = tag.getString(String.valueOf("sideName"+i));
      }
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  if(this.boxName!=null)
    {
    tag.setString("boxName", boxName);    
    }
  if(topDestination!=null)
    {
    tag.setString("topName", topDestination);
    }
  for(int i = 0; i <4; i++)
    {
    if(tag.hasKey(String.valueOf("sideName"+i)))
      {
      this.sideDestinations[i] = tag.getString(String.valueOf("sideName"+i));
      }
    }
  }

/************************************************SIDED INVENTORY METHODS*************************************************/

@Override
public int[] getAccessibleSlotsFromSide(int side)
  {
  if(side==0)
    {
    return bottomIndices;
    }
  else if(side==1)
    {
    return topIndices;
    }
  side = BlockTools.getCardinalFromSide(side);
  side = (side + this.rotation)%4;
  return sideSlotIndices[side];
  }


@Override
public boolean canInsertItem(int i, ItemStack itemstack, int j)
  {
  return true;
  }

@Override
public boolean canExtractItem(int i, ItemStack itemstack, int j)
  {
  if(j==1)
    {
    return true;
    }
  return false;
  }

/************************************************INVENTORY METHODS*************************************************/

@Override
public void onInventoryChanged()
  {
  super.onInventoryChanged();
  }

@Override
public int getSizeInventory()
  {
  return inventory.getSizeInventory();
  }

@Override
public ItemStack getStackInSlot(int i)
  {
  return inventory.getStackInSlot(i);
  }

@Override
public ItemStack decrStackSize(int i, int j)
  {
  return inventory.decrStackSize(i, j);
  }

@Override
public ItemStack getStackInSlotOnClosing(int i)
  {
  return inventory.getStackInSlotOnClosing(i);
  }

@Override
public void setInventorySlotContents(int i, ItemStack itemstack)
  {
  inventory.setInventorySlotContents(i, itemstack);
  }

@Override
public String getInvName()
  {
  return "AWInventory.mail";
  }

@Override
public boolean isInvNameLocalized()
  {
  return false;
  }

@Override
public int getInventoryStackLimit()
  {
  return inventory.getInventoryStackLimit();
  }

@Override
public boolean isUseableByPlayer(EntityPlayer entityplayer)
  {
  return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
  }

@Override
public void openChest()
  {
  }

@Override
public void closeChest()
  {
  }

@Override
public boolean isStackValidForSlot(int i, ItemStack itemstack)
  {
  return true;
  }
}
