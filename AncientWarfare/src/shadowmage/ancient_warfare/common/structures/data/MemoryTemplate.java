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
package shadowmage.ancient_warfare.common.structures.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 * in-memory template w/ methods to handle breaking data up into packet-sized chunks of bytes
 * @author Shadowmage
 *
 */
public class MemoryTemplate
{
ArrayList<String> templateLines = new ArrayList<String>();

public void setLines(List<String> lines)
  {
  templateLines.clear();
  templateLines.addAll(lines);
  }

public List<byte[]> getPacketBytes(int packetSize)
  {
  ArrayList<byte []> packetChunks = new ArrayList<byte []>();
    
  ByteArrayDataOutput bado = ByteStreams.newDataOutput();    
  for(String line : templateLines)      
    {
    bado.writeChars(line);
    }
  byte[] allBytes = bado.toByteArray();
  ByteArrayDataInput badi = ByteStreams.newDataInput(allBytes);  
  int numOfPackets = (allBytes.length/packetSize)+1;
  byte[][] packetBytes = new byte[numOfPackets][packetSize];
  int currentByte = 0;    
  for(int x = 0; x < numOfPackets; x++)
    {
    for(int y = 0; y <packetSize; y++)
      {
      packetBytes[x][y] = allBytes[x*packetSize + y];
      }
    packetChunks.add(packetBytes[x]);
    }    
//  for(int i = 0; i < numOfPackets; i++)
//    {    
//    NBTTagCompound tag = new NBTTagCompound();
//    tag.setInteger("num", i);     
//    tag.setInteger("of", numOfPackets);
//    tag.setByteArray("bytes", packetBytes[i]);
//    packetTags.add(tag);
//    }  
  return packetChunks;
  }


}
