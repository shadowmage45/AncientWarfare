package shadowmage.ancient_warfare.common.aw_core.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{

private Map packetTypes = new HashMap<Integer, Class<? extends PacketBase>>();

public PacketHandler()  
  {
  this.packetTypes.put(1, Packet01ModInit.class);
  }

@Override
public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
  {
  ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
  int packetType = data.readInt();
  PacketBase realPacket = this.constructPacket(packetType);
  if(realPacket==null)
    {
    System.out.println("Extreme error during packet handling");
    return;
    }
  realPacket.player = (EntityPlayer)player;
  if(realPacket.player!=null)
    {
    realPacket.world = realPacket.player.worldObj;
    }  
  realPacket.readDataStream(data);
  realPacket.execute();
  }

/**
 * construct a new instance of a packet given only the packetType
 * used on receiving a packet, so that it may be populated by the data stream
 * in an intelligent manner
 * @param type
 * @return
 */
public PacketBase constructPacket(int type)
  {
  try
    {
    return (PacketBase) this.packetTypes.get(type).getClass().newInstance();
    } 
  catch (InstantiationException e)
    { 
    System.out.println("Exception while constructing packet from dataStream");
    e.printStackTrace();
    } 
  catch (IllegalAccessException e)
    {   
    System.out.println("Exception while constructing packet from dataStream");
    e.printStackTrace();
    }
  return null;
  }

}
