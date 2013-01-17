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

import shadowmage.ancient_warfare.common.aw_structure.store.StructureManager;
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
  return "AW_mod";
  }

@Override
public int getPacketType()
  {  
  return 1;
  }



@Override
public void writeDataToStream(ByteArrayDataOutput data)
  {
  
  }

@Override
public void readDataStream(ByteArrayDataInput data)
  {
  
  }

@Override
public void execute()
  {
  NBTTagCompound tag;
  
  /***
   * init data, should break out to player entry, team entry, pass to client-trackers
   */
  if(this.packetData.hasKey("init"))
    {
    if(world.isRemote)
      {
      //TODO handle initialization data
      }
    }  
  
  /**
   * cooldown update
   */
  if(this.packetData.hasKey("cdn"))
    {
    if(world.isRemote)
      {
      
      }
    else
      {
      
      }    
    }
  
  /**
   * 
   */
  if(this.packetData.hasKey("struct"))
    {
    if(world.isRemote)
      {
      StructureManager.instance().handleUpdateClient(packetData.getCompoundTag("struct"));
      }
    else
      {
      StructureManager.instance().handleUpdateServer(packetData.getCompoundTag("struct"));
      }
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
