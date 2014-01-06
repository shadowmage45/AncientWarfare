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
package shadowmage.ancient_framework.common.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import shadowmage.ancient_framework.common.utils.NBTTools;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{

private static Map<Integer, Class<? extends PacketBase>> packetTypes = new HashMap<Integer, Class<? extends PacketBase>>();

static
{
packetTypes.put(0, Packet00MultiPart.class);
packetTypes.put(1, Packet01ModData.class);
packetTypes.put(2, Packet02Entity.class);
packetTypes.put(3, Packet03GuiComs.class);
packetTypes.put(4, Packet04TE.class);
packetTypes.put(5, Packet05Team.class);
}

public PacketHandler()  
  {

  }

public static void registerPacketType(int id, Class<? extends PacketBase> pktClass)
  {
  packetTypes.put(id, pktClass);
  }

@Override
public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
  {  
  ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
  int packetType = data.readInt();      
  NBTTagCompound tag =  NBTTools.readTagFromStream(data);      
  PacketBase realPacket = null;
  try 
    {
    realPacket = this.constructPacket(packetType);
    } 
  catch (InstantiationException e) 
    {
    e.printStackTrace();
    return;
    } 
  catch (IllegalAccessException e)
    {
    e.printStackTrace();
    return;
    }  
  if(realPacket==null)
    {
    return;
    }
  realPacket.packetData = tag;
  realPacket.player = (EntityPlayer)player;  
  realPacket.world = realPacket.player.worldObj;    
  realPacket.readDataStream(data);
  realPacket.execute();
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
public static PacketBase constructPacket(int type) throws InstantiationException, IllegalAccessException
  {
  return packetTypes.get(type).newInstance();
  }

private static HashMap<String, MPPacketList> serverMultiPartPacketHandlers = new HashMap<String, MPPacketList>();
private static HashMap<String, MPPacketList> clientMultiPartPacketHandlers = new HashMap<String, MPPacketList>();

public static void handleMultiPartPacketReceipt(Packet00MultiPart pkt, EntityPlayer player)
  {
  HashMap<String, MPPacketList> ph;
  if(player.worldObj.isRemote)
    {
    ph = clientMultiPartPacketHandlers;
    }
  else
    {
    ph = serverMultiPartPacketHandlers;
    }  
  if(!ph.containsKey(player.getEntityName()))
    {
    ph.put(player.getEntityName(), new MPPacketList());
    }
  ph.get(player.getEntityName()).handleMPPacket(pkt);
  }



private static class MPPacketEntry
{

int uniquePacketID;//used to determine which packets to combine
int packetType;//the destination packet type this MP packet represents
int totalLength;//the total length of byte-array data to be reconstructed for this destination packet
byte[] fullData;//the byte-array of datas for this destination packet
int receivedChunks;//how many chunks have been receieved from this packet?
int totalChunks;//total number of chunks that make up this destination packet

public MPPacketEntry(Packet00MultiPart pkt)
  {
  this.packetType = pkt.sourcePacketType;
  this.uniquePacketID = pkt.uniquePacketID;
  this.totalLength = pkt.totalLength;
  this.fullData = new byte[pkt.totalLength];  
  }

public boolean addPartialPacket(Packet00MultiPart pkt)
  {
  byte[] data = pkt.datas;
  for(int i = 0, k = pkt.startIndex; k < pkt.startIndex+pkt.chunkLength; k++, i++)
    {
    this.fullData[k]=data[i];
    }
  this.receivedChunks++;
  if(this.receivedChunks==this.totalChunks)
    {
    return true;
    }
  return false;
  }

}

private static class MPPacketList
{
HashMap<Integer, MPPacketEntry> partialPackets = new HashMap<Integer, MPPacketEntry>();

public void handleMPPacket(Packet00MultiPart pkt)
  {
  if(!partialPackets.containsKey(pkt.uniquePacketID))
    {
    partialPackets.put(pkt.uniquePacketID, new MPPacketEntry(pkt));
    }
  MPPacketEntry entry = partialPackets.get(pkt.uniquePacketID);
  if(entry.addPartialPacket(pkt))
    {
    partialPackets.remove(pkt.uniquePacketID);   
    PacketBase realPacket;
    try
      {
      ByteArrayDataInput data = ByteStreams.newDataInput(entry.fullData);
      int packetType = data.readInt();      
      realPacket = constructPacket(packetType);
      NBTTagCompound tag =  NBTTools.readTagFromStream(data);      
      realPacket.packetData = tag;
      realPacket.player = (EntityPlayer)pkt.player;  
      realPacket.world = pkt.player.worldObj;    
      realPacket.readDataStream(data);
      realPacket.execute();
      } 
    catch (InstantiationException e)
      {
      e.printStackTrace();
      } 
    catch (IllegalAccessException e)
      {
      e.printStackTrace();
      }
    }
  }

}

}
