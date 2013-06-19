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
package shadowmage.ancient_warfare.common.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TEAWBlockReinforced extends TileEntity
{

public int baseBlockID;

/**
 * 
 */
public TEAWBlockReinforced()
  {
  // TODO Auto-generated constructor stub
  }

@Override
public Packet getDescriptionPacket()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("block", baseBlockID);  
  Packet132TileEntityData pkt = new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);  
  return pkt;
  }

@Override
public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {
  super.onDataPacket(net, pkt);
  this.baseBlockID = pkt.customParam1.getInteger("block");
  }

}
