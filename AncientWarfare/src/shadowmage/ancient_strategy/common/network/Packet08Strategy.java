/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_strategy.common.network;

import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import shadowmage.ancient_framework.common.network.PacketBase;
import shadowmage.ancient_strategy.common.structure.StrategyStructureManager;

public class Packet08Strategy extends PacketBase
{

UUID sendingId;

public Packet08Strategy()
  {
  }

@Override
public String getChannel()
  {
  return "AW_mod";
  }

@Override
public int getPacketType()
  {
  return 8;
  }

public void setSendingID(UUID id)
  {
  this.sendingId = id;
  }

@Override
public void writeDataToStream(ByteArrayDataOutput data)
  {
  if(sendingId!=null)
    {
    data.writeBoolean(true);
    data.writeLong(sendingId.getMostSignificantBits());
    data.writeLong(sendingId.getLeastSignificantBits());
    }
  }

@Override
public void readDataStream(ByteArrayDataInput data)
  {
  if(data.readBoolean())
    {
    long i1 = data.readLong();
    long i2 = data.readLong();
    this.sendingId = new UUID(i1, i2);    
    }
  }

@Override
public void execute()
  {
  if(sendingId!=null)
    {
    StrategyStructureManager.instance().getStructureByID(world, sendingId).handlePacketData(packetData);
    }
  else
    {
    StrategyStructureManager.instance().handlePacketData(world, packetData);
    }  
  }

}
