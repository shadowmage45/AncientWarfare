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

import shadowmage.ancient_framework.common.config.AWLog;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class Packet07TestLargePacket extends PacketBase
{

byte[] testData;

public Packet07TestLargePacket()
  {
  testData = new byte[48000];
  for(int i = 0; i < 48000; i++)
    {
    testData[i] = (byte)(i%2);
    }
  }

@Override
public String getChannel()
  {
  return "AW_mod";
  }

@Override
public int getPacketType()
  {
  return 7;
  }

@Override
public void writeDataToStream(ByteArrayDataOutput data)
  {
  data.write(testData);
  }

@Override
public void readDataStream(ByteArrayDataInput data)
  {
  data.readFully(testData = new byte[48000]);
  }

@Override
public void execute()
  {  
  AWLog.logDebug("received test-data packet...length: "+testData.length);
  new Exception().printStackTrace();
  }

}
