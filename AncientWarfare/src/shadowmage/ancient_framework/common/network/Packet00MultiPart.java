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
package shadowmage.ancient_framework.common.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class Packet00MultiPart extends PacketBase
{

static int nextUniquePacketID = 0;

int sourcePacketType;
String sourcePacketChannel;

int uniquePacketID;//to identify which multi-part packets belong to this packet
int packetNumber;//the number of this packet in the total packet list
int totalPackets;//the total number of packets..used to determine when receipt is done

int datasLength;
byte[] datas;

public Packet00MultiPart()
  {
  uniquePacketID = nextUniquePacketID;
  nextUniquePacketID++;
  }

@Override
public String getChannel()
  {
  return "AW_mod";
  }

@Override
public int getPacketType()
  {
  return 0;
  }

@Override
public void writeDataToStream(ByteArrayDataOutput data)
  {
  data.writeChars(sourcePacketChannel);
  data.writeInt(sourcePacketType);
  data.writeInt(packetNumber);
  data.writeInt(totalPackets);  
  data.writeInt(datasLength);
  data.write(datas);
  }

@Override
public void readDataStream(ByteArrayDataInput data)
  {
  sourcePacketChannel = data.readLine();
  sourcePacketType = data.readInt();
  packetNumber = data.readInt();
  totalPackets = data.readInt();  
  datasLength = data.readInt();
  datas = new byte[datasLength];
  data.readFully(datas);
  }

@Override
public void execute()
  {
  /**
   * should be handled prior to execute()
   * ...packet handler should store to combine into the actual end-product packet
   */
  }

}
