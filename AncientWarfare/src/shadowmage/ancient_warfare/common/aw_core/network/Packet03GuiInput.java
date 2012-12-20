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
package shadowmage.ancient_warfare.common.aw_core.network;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.aw_core.AWCore;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.network.FMLNetworkHandler;

public class Packet03GuiInput extends PacketBase
{

@Override
public String getChannel()
  {  
  return "AW_gui";
  }

@Override
public int getPacketType()
  {
  return 3;
  }

@Override
public void writeDataToStream(ByteArrayDataOutput data)
  {
  
  }

@Override
public void readDataStream(ByteArrayDataInput data)
  {
  
  }

public void setGuiToOpen(byte id, int x, int y, int z)
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setByte("id", id);
  tag.setInteger("x", x);
  tag.setInteger("y", y);
  tag.setInteger("z", z);
  this.packetData.setTag("openGUI", tag);
  }

public void setInputTag(NBTTagCompound tag)
  {
  this.packetData.setTag("input", tag);
  }

@Override
public void execute()
  {
  NBTTagCompound tag = null;
  if(this.packetData.hasKey("input") && player.openContainer instanceof IHandlePacketData)//"input" may be client->server input OR server->client updates, as long as the GUI reads from the container
    {
    tag = packetData.getCompoundTag("input");
    ((IHandlePacketData)player.openContainer).handlePacketData(tag);
    return;
    }  
  else if(this.packetData.hasKey("openGUI") && !this.world.isRemote)//only accept open-gui command on server-side, allow client-side GUIs to be opened by FML
    {
    tag = packetData.getCompoundTag("openGUI");
    FMLNetworkHandler.openGui(player, AWCore.instance, tag.getByte("id"), world, tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
    return;
    }  
  }

}
