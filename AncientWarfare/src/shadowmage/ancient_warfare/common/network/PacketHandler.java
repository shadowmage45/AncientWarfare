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
package shadowmage.ancient_warfare.common.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_framework.common.network.PacketBase;
import shadowmage.ancient_framework.common.utils.NBTWriter;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{

private Map<Integer, Class<? extends PacketBase>> packetTypes = new HashMap<Integer, Class<? extends PacketBase>>();

public PacketHandler()  
  {
  this.packetTypes.put(1, Packet01ModData.class);
  this.packetTypes.put(2, Packet02Vehicle.class);
  this.packetTypes.put(3, Packet03GuiComs.class);
  this.packetTypes.put(4, Packet04Npc.class);
  this.packetTypes.put(5, Packet05TE.class);
  this.packetTypes.put(6, Packet06Entity.class);
  }

@Override
public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
  {
  try
    {
    ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
    int packetType = data.readInt();      
    NBTTagCompound tag =  NBTWriter.readTagFromStream(data);      
    PacketBase realPacket = this.constructPacket(packetType);
    if(realPacket==null)
      {
      //i think it will throw before this..but w/e
      return;
      }
    realPacket.packetData = tag;
    realPacket.player = (EntityPlayer)player;  
    realPacket.world = realPacket.player.worldObj;    
    realPacket.readDataStream(data);
    realPacket.execute();
    }
  catch(Exception e)
    {
    Config.logError("Extreme error during packet handling, could not instantiate packet instance, improper packetType info");
    Config.log("Exception During Packet Handling, problem reading packet data");
    e.printStackTrace();
    }
  
  }

/**
 * construct a new instance of a packet given only the packetType
 * used on receiving a packet, so that it may be populated by the data stream
 * in an intelligent manner
 * @param type
 * @return
 * @throws IllegalAccessException 
 * @throws InstantiationException 
 */
public PacketBase constructPacket(int type) throws InstantiationException, IllegalAccessException
  {
  return this.packetTypes.get(type).newInstance();
  }

}
