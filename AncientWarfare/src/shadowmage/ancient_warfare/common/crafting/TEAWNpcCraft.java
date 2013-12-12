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
package shadowmage.ancient_warfare.common.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_framework.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;

public class TEAWNpcCraft extends TEAWCrafting
{

/**
 * 
 */
public TEAWNpcCraft()
  {
  this.modelID = 5;
  this.craftMatrix = new int[]{0,1,2,3,4,5,6,7,8};
  this.resultSlot = new int[]{9};
  this.bookSlot = new int[]{10};
  this.inventory = new AWInventoryBasic(11);
  }

@Override
public void readDescriptionPacket(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub

  }

@Override
public void writeDescriptionData(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub

  }

@Override
public void writeExtraNBT(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub

  }

@Override
public void readExtraNBT(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub

  }

@Override
public void onBlockClicked(EntityPlayer player)
  {
  if(player.worldObj.isRemote){return;}
  GUIHandler.instance().openGUI(GUIHandler.NPC_CRAFT, player, player.worldObj, xCoord, yCoord, zCoord);
  }

}
