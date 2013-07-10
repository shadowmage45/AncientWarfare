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

import shadowmage.ancient_warfare.common.interfaces.IInteractable;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public abstract class TEMachine extends TileEntity implements IInteractable
{

protected int machineNumber = 0;
protected int rotation = 0;
protected int guiNumber = -1;
protected boolean canUpdate = false;

public int getMachineNumber()
  {
  return machineNumber;
  }

public int getRotation()
  {
  return rotation;
  }

public void onBlockBreak()
  {
  
  }

@Override
public boolean canUpdate()
  {
  return canUpdate;
  }

/**
 * 0-south
 * 1-east
 * 2-north
 * 3-west
 * 4-down
 * 5-up
 * @param rotation
 */
public void setRotation(int rotation)
  {
  this.rotation = rotation;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.rotation = tag.getByte("face");
  if(this instanceof IInventory)
    {
    InventoryTools.readInventoryFromTag((IInventory)this, tag.getCompoundTag("inventory"));
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setByte("face", (byte)this.rotation);
  if(this instanceof IInventory)
    {
    tag.setCompoundTag("inventory",InventoryTools.getTagForInventory((IInventory)this));
    }
  }

@Override
public Packet getDescriptionPacket()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setByte("face", (byte)rotation);
  Packet132TileEntityData pkt = new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
  return pkt;
  }

@Override
public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {
  if(pkt.customParam1.hasKey("face"))
    {
    this.rotation = pkt.customParam1.getByte("face");
    }
  super.onDataPacket(net, pkt);
  }

@Override
public void onPlayerInteract(EntityPlayer player)
  {
  if(!player.worldObj.isRemote && this.guiNumber>=0)
    {
    this.openGui(player);
    }
  }

public void openGui(EntityPlayer player)
  {
  GUIHandler.instance().openGUI(guiNumber, player, player.worldObj, xCoord, yCoord, zCoord);
  }
}
