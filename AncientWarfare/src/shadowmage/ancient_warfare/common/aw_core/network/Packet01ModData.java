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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

/**
 * client-server mod meta-data comms packet
 * used to send server-> client onLogin data
 * used to send server-> client team/playerEntry update data
 * used to send server-> client cooldown, research, and stats data
 * used to send client-> server input for non-GUI information (team changes,
 * @author Shadowmage
 *
 */
public class Packet01ModData extends PacketBase
{

@Override
public String getChannel()
  {
  // TODO Auto-generated method stub
  return null;
  }

@Override
public int getPacketType()
  {
  // TODO Auto-generated method stub
  return 0;
  }



@Override
public void writeDataToStream(ByteArrayDataOutput data)
  {
  // TODO Auto-generated method stub

  }

@Override
public void readDataStream(ByteArrayDataInput data)
  {
  // TODO Auto-generated method stub

  }

@Override
public void execute()
  {
  NBTTagCompound tag;
  if(this.packetData.hasKey("clientInput") && !world.isRemote)
    {
    tag = packetData.getCompoundTag("clientInput");
    //TODO handle various inputs...
    return;//stop processing after reading client input if it was a client->server packet
    }
  if(!world.isRemote)
    {
    //exit out if server recieves a packet intended for client
    }
  
  /**
   * cooldown update info, pass tag off to cooldownTracker...or have playerEntry have cooldownData directly?
   */
  if(this.packetData.hasKey("cdn"))
    {
    tag = packetData.getCompoundTag("cdn");    
    }
  
  /**
   * server->client team update data
   * should be passed onto client team tracker
   */
  if(this.packetData.hasKey("team"))
    {
    tag = packetData.getCompoundTag("team");
    }
  
  /**
   * server->client research update data
   * should be passed into client player entry, and from there passed
   * into client player research data.
   */
  if(this.packetData.hasKey("res"))
    {
    tag = packetData.getCompoundTag("res");
    }
  }
  
    
    


}
