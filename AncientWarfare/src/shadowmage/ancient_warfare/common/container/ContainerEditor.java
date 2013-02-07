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
package shadowmage.ancient_warfare.common.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ContainerEditor extends ContainerBase
{

List<String> serverEditorLines;

List<String> clientEditorLines;


/**
 * @param openingPlayer
 * @param synch
 */
public ContainerEditor(EntityPlayer openingPlayer)
  {
  super(openingPlayer, null); 
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(player.worldObj.isRemote)
    {
    
    }
  else
    {
    if(tag.hasKey("name"))
      {
      String name = tag.getString("name");
      ProcessedStructure struct = StructureManager.instance().getStructureServer(name);
      struct.lock();
      //TODO...hrrm...
      
      }
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub

  }

@Override
public List<NBTTagCompound> getInitData()
  {
  ArrayList<NBTTagCompound> packetTags = new ArrayList<NBTTagCompound>();
  if(serverEditorLines==null)
    {
    return packetTags;
    }
  
  ByteArrayDataOutput bado = ByteStreams.newDataOutput();    
  for(String line : serverEditorLines)      
    {
    bado.writeChars(line);
    }
  int packetSize = 8192;    
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
    }    
  for(int i = 0; i < numOfPackets; i++)
    {    
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("num", i);     
    tag.setInteger("of", numOfPackets);
    tag.setByteArray("bytes", packetBytes[i]);
    packetTags.add(tag);
    }  
  return packetTags;
  }

}
