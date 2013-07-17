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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IInteractable;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public abstract class TEMachine extends TileEntity implements IInteractable
{

protected int guiNumber = -1;
protected boolean canUpdate = false;
protected boolean shouldWriteInventory = true;
public boolean canPointVertical = false;
public boolean facesOpposite = false;

protected ForgeDirection facingDirection = ForgeDirection.UNKNOWN;

public void onBlockBreak()
  {
  
  }

public void setDirection(ForgeDirection direction)
  {
  this.facingDirection = direction;
  Config.logDebug("setting direction to: "+direction);
  }

public ForgeDirection getFacing()
  {
  return this.facingDirection;
  }

public void onBlockPlaced()
  {
  
  }

@Override
public boolean canUpdate()
  {
  return canUpdate;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.facingDirection = ForgeDirection.getOrientation(tag.getByte("face"));
  if(this instanceof IInventory)
    {
    InventoryTools.readInventoryFromTag((IInventory)this, tag.getCompoundTag("inventory"));
    }
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setByte("face", (byte) this.facingDirection.ordinal());
  if(this instanceof IInventory && this.shouldWriteInventory)
    {
    tag.setCompoundTag("inventory",InventoryTools.getTagForInventory((IInventory)this));
    }
  }

@Override
public Packet getDescriptionPacket()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setByte("face", (byte) this.facingDirection.ordinal());
  Packet132TileEntityData pkt = new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
  return pkt;
  }

@Override
public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {
  if(pkt.customParam1.hasKey("face"))
    {
    this.facingDirection = ForgeDirection.getOrientation(pkt.customParam1.getByte("face"));
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
  if(this.guiNumber>=0)
    {
    GUIHandler.instance().openGUI(guiNumber, player, player.worldObj, xCoord, yCoord, zCoord);    
    }
  }
}
