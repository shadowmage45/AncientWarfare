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
package shadowmage.ancient_warfare.common.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public class TEAWCrafting extends TileEntity implements IInventory, ISidedInventory
{

int orientation;

/**
 * 
 */
public TEAWCrafting()
  {
  }

public Icon getIconForSide(BlockAWCrafting block, int side, int meta)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(block.blockID);
  return null;
  }

public void setOrientation(int face)
  {
  this.orientation = face;
  }

@Override
public int[] getAccessibleSlotsFromSide(int var1)
  {
  // TODO Auto-generated method stub
  return null;
  }

@Override
public boolean canInsertItem(int i, ItemStack itemstack, int j)
  {
  // TODO Auto-generated method stub
  return false;
  }

@Override
public boolean canExtractItem(int i, ItemStack itemstack, int j)
  {
  // TODO Auto-generated method stub
  return false;
  }

@Override
public int getSizeInventory()
  {
  // TODO Auto-generated method stub
  return 0;
  }

@Override
public ItemStack getStackInSlot(int i)
  {
  // TODO Auto-generated method stub
  return null;
  }

@Override
public ItemStack decrStackSize(int i, int j)
  {
  // TODO Auto-generated method stub
  return null;
  }

@Override
public ItemStack getStackInSlotOnClosing(int i)
  {
  // TODO Auto-generated method stub
  return null;
  }

@Override
public void setInventorySlotContents(int i, ItemStack itemstack)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public String getInvName()
  {
  // TODO Auto-generated method stub
  return null;
  }

@Override
public boolean isInvNameLocalized()
  {
  // TODO Auto-generated method stub
  return false;
  }

@Override
public int getInventoryStackLimit()
  {
  // TODO Auto-generated method stub
  return 0;
  }

@Override
public boolean isUseableByPlayer(EntityPlayer entityplayer)
  {
  // TODO Auto-generated method stub
  return false;
  }

@Override
public void openChest()
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void closeChest()
  {
  // TODO Auto-generated method stub
  
  }

@Override
public boolean isStackValidForSlot(int i, ItemStack itemstack)
  {
  // TODO Auto-generated method stub
  return false;
  }



}
