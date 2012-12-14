package shadowmage.ancient_warfare.common.aw_core.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public abstract class PacketBase 
{

/**
 * world and player are populated only after receiving, by the packetHandler
 */
public World world;
public EntityPlayer player;

public abstract String getChannel();

/**
 * the actual transformed packet that will be written and sent
 */
private Packet250CustomPayload packet250;

/**
 * should be treated as chunk update packet?
 */
private boolean chunkPacket = false;

/**
 * return the numerical packet type, used to create new packet instances from a packetTypeMap in PacketHandler
 * @return packetType
 */
public abstract int getPacketType();

/**
 * called to execute the contents of the packet, whether executed by the packet or passed
 * on to another entity/class
 */
public abstract void execute();

/**
 * write packet specific data to the stream
 * @param data
 */
public abstract void writeDataToStream(ByteArrayDataOutput data);

/**
 * read packet-specific data from the stream
 * @param data
 */
public abstract void readDataStream(ByteArrayDataInput data);

/**
 * create the custom250packet from the current data in this packet.
 */
protected void constructPacket()
  {  
  ByteArrayDataOutput data = ByteStreams.newDataOutput();

  /**
   * write the packet type number to the stream, decoded in packetHandler to create a new packet
   */
  data.writeInt(this.getPacketType());
  
  /**
   * write custom data to the output stream
   */
  this.writeDataToStream(data);
  packet250.channel = this.getChannel();
  packet250.data = data.toByteArray();
  packet250.length = packet250.data.length;
  }


}
