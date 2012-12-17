package shadowmage.ancient_warfare.common.aw_core.network;

import java.util.HashMap;
import java.util.Map;

import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_core.utils.NBTWriter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
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
  try
    {
    ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
    int packetType = data.readInt();
    boolean hasTag = data.readBoolean();
    NBTTagCompound tag = null;
    if(hasTag)
      {
      tag = NBTWriter.readTagFromStream(data);
      }
    PacketBase realPacket = this.constructPacket(packetType);
    if(realPacket==null)
      {
      System.out.println("Extreme error during packet handling");
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
  return (PacketBase) this.packetTypes.get(type).getClass().newInstance();
  }

}
