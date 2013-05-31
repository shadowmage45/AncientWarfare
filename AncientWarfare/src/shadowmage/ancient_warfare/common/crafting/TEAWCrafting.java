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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public abstract class TEAWCrafting extends TileEntity
{

public int orientation;
int modelID;

/**
 * 
 */
public TEAWCrafting()
  {
  }

public int getOrientation()
  {
  return orientation;
  }

public int getModelID()
  {
  return this.modelID;
  }

public Icon getIconForSide(BlockAWCrafting block, int side, int meta)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(block.blockID);
  return null;
  }

public void onBlockClicked(EntityPlayer player)
  {
  }

public void setOrientation(int face)
  {
  this.orientation = face;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.orientation = tag.getByte("face");
  this.readExtraNBT(tag);
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setByte("face", (byte)this.orientation);
  this.writeExtraNBT(tag);
  }

@Override
public Packet getDescriptionPacket()
  {
  NBTTagCompound tag = new NBTTagCompound();
  this.writeDescriptionData(tag);
  tag.setByte("face", (byte) this.orientation);  
  Packet132TileEntityData pkt = new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);  
  return pkt;
  }

@Override
public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {
  super.onDataPacket(net, pkt);
  readDescriptionPacket(pkt.customParam1);
  this.orientation = pkt.customParam1.getByte("face");
  }

public abstract void readDescriptionPacket(NBTTagCompound tag);
public abstract void writeDescriptionData(NBTTagCompound tag);
public abstract void writeExtraNBT(NBTTagCompound tag);
public abstract void readExtraNBT(NBTTagCompound tag);

}
