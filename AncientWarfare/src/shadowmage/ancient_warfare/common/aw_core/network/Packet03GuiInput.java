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
import shadowmage.ancient_warfare.common.aw_core.config.Config;

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

public void setData(NBTTagCompound tag)
  {
  this.packetData.setCompoundTag("data", tag);
  }

@Override
public void execute()
  {
  if(packetData.hasKey("openGUI"))
    {
    NBTTagCompound tag = packetData.getCompoundTag("openGUI");
    FMLNetworkHandler.openGui(player, AWCore.instance, tag.getInteger("id"), world, tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
    if(world.isRemote)
      {
      Config.logError("Opening GUI on client-side only from openGUI packet.  This is not proper gui handling.");
      }
    return;
    }
  if(player.openContainer instanceof IHandlePacketData && packetData.hasKey("data"))
    {
    ((IHandlePacketData)player.openContainer).handlePacketData(packetData.getCompoundTag("data"));
    return;
    }
  Config.logError("Attempt to send container data packet to non-applicable container (no valid interface)");
  }

}
