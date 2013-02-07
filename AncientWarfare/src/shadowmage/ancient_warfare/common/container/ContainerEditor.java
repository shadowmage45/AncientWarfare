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

import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.utils.ByteTools;
import shadowmage.ancient_warfare.common.utils.StringTools;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class ContainerEditor extends ContainerBase
{

private String currentEditingStructure = "";
private String currentSelectedStructure = "";

private ProcessedStructure serverStructure;

private StructureClientInfo clientStructure;
public List<String> clientLines;

/**
 * temporary store for packetData sent to client
 */
private byte[][] packetData;
int recievedParts = 0;
int totalParts = 0;
boolean finishedReceiving = false;

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
    if(tag.hasKey("templateData"))
      {
      this.handlePartialTemplateClient(tag);
      }
    }
  else//server side handle data....
    {
    if(tag.hasKey("setStructure"))
      {
      this.setStructureServer(tag);      
      }
    if(tag.hasKey("name"))
      {
      this.currentSelectedStructure = tag.getString("name");
      }
    }  
  }

private void handlePartialTemplateClient(NBTTagCompound tag)
  {
  /**
   * composite recieved packetData into full byte array
   */
  NBTTagCompound templateTag = tag.getCompoundTag("templateData");
  int total = templateTag.getInteger("num");
  int current = templateTag.getInteger("of");
  int size = templateTag.getInteger("packetSize");
  byte[] bytes = templateTag.getByteArray("bytes");
  if(packetData==null)
    {
    totalParts = total;
    packetData = new byte[total][size];
    }
  packetData[current]=bytes;
  recievedParts++;
  
  if(recievedParts >= totalParts)//should really only ever be equal, greater is an error
    {
    finishedReceiving = true;
    List<byte[]> chunks = new ArrayList<byte[]>(totalParts);
    for(int i = 0; i < totalParts; i++)
      {
      chunks.add(packetData[i]);
      }
    byte[] fullFile = ByteTools.compositeByteChunks(chunks, size);
    List<String> lines = StringTools.getLines(fullFile);
    if(lines!=null)
      {
      this.clientLines = lines;
      }
    }  
  }

private void sendTemplateToClient(ProcessedStructure struct) throws UnsupportedEncodingException, IOException
  {
  int packetSize = 8192;
  List<byte[]> chunks = struct.getTemplate().getPacketBytes(packetSize);
  for(int i = 0; i < chunks.size(); i++)
    {
    NBTTagCompound outerTag = new NBTTagCompound();
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("num", i);
    tag.setInteger("of",chunks.size());
    tag.setInteger("packetSize", packetSize);
    tag.setByteArray("bytes", chunks.get(i));
    outerTag.setTag("templateData", tag);
    this.sendDataToPlayer(outerTag);
    }
  }

public void setStructureServer(NBTTagCompound tag)
  {
  this.currentEditingStructure = tag.getString("setStructure");
  this.currentSelectedStructure = this.currentEditingStructure;
  this.serverStructure = StructureManager.instance().getStructureServer(currentEditingStructure);
  if(this.serverStructure==null)
    {
    Config.logError("Severe error attempting to set structure for editing: "+this.currentEditingStructure);
    this.currentEditingStructure= "";
    return;    
    }
  this.serverStructure.lock();
  try
    {
    this.sendTemplateToClient(serverStructure);
    } 
  catch (UnsupportedEncodingException e)
    {
    Config.logError("Error sending template to client");
    e.printStackTrace();
    } 
  catch (IOException e)
    {
    Config.logError("Error sending template to client");
    e.printStackTrace();
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {  
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return null;
  }

}
