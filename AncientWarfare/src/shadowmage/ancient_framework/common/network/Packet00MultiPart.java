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
int chunkNumber;
int totalChunks;

int uniquePacketID;//to identify which multi-part packets belong to this packet
int startIndex;//the start index of this data chunk
int chunkLength;//the length of this data chunk
int totalLength;//the total length of the entire original data packet
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
  data.writeInt(chunkNumber);
  data.writeInt(totalChunks);
  data.writeInt(startIndex);
  data.writeInt(chunkLength);  
  data.writeInt(totalLength);
  data.write(datas);
  }

@Override
public void readDataStream(ByteArrayDataInput data)
  {
  sourcePacketChannel = data.readLine();
  sourcePacketType = data.readInt();
  chunkNumber = data.readInt();
  totalChunks = data.readInt();
  startIndex = data.readInt();
  chunkLength = data.readInt();  
  totalLength = data.readInt();
  datas = new byte[chunkLength];
  data.readFully(datas);
  }

@Override
public void execute()
  {
  PacketHandler.handleMultiPartPacketReceipt(this, this.player);
  }

}
