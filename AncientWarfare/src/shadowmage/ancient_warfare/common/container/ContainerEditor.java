/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.container;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.structures.file.StructureExporter;
import shadowmage.ancient_warfare.common.structures.file.StructureLoader;
import shadowmage.ancient_warfare.common.utils.ByteTools;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class ContainerEditor extends ContainerBase
{

private String currentEditingStructure = "";
private String currentSelectedStructure = "";

private String structureFilePath = null;
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
    if(tag.hasKey("saveData"))
      {
      this.handlePartialTemplateServer(tag.getCompoundTag("saveData"));
      }
    if(tag.hasKey("remove"))
      {
      boolean success = StructureManager.instance().tryRemoveStructure(tag.getString("remove"));
      if(!success)
        {
        NBTTagCompound rejectTag = new NBTTagCompound();
        rejectTag.setBoolean("noRem", true);
        this.sendDataToGUI(rejectTag);
        }
      }
    if(tag.hasKey("delete"))
      {
      boolean success = StructureManager.instance().tryDeleteStructure(tag.getString("delete"));
      if(!success)
        {
        NBTTagCompound rejectTag = new NBTTagCompound();
        rejectTag.setBoolean("noDel", true);
        this.sendDataToGUI(rejectTag);
        }      
      }
    }  
  }

private void handlePartialTemplateClient(NBTTagCompound tag)
  {
//  Config.logDebug("receiving partial template packet");
  /**
   * composite recieved packetData into full byte array
   */
  NBTTagCompound templateTag = tag.getCompoundTag("templateData");
  int current = templateTag.getInteger("num");
  int total = templateTag.getInteger("of");
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
    packetData=null;
    }  
  }

private void sendTemplateToClient(ProcessedStructure struct) throws UnsupportedEncodingException, IOException
  {
//  Config.logDebug("sending template to client....");
  int packetSize = 8192;
  List<byte[]> chunks = struct.getTemplate().getPacketBytes(packetSize);
  for(int i = 0; i < chunks.size(); i++)
    {
//    Config.logDebug("sending template chunk to client");
    NBTTagCompound outerTag = new NBTTagCompound();
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("num", i);
    tag.setInteger("of",chunks.size());
    tag.setInteger("packetSize", packetSize);
    tag.setByteArray("bytes", chunks.get(i));
    outerTag.setTag("templateData", tag);
    this.sendDataToPlayer(outerTag);
    }
  NBTTagCompound tag = new NBTTagCompound();
  tag.setBoolean("openEdit", true);
  this.sendDataToGUI(tag);
  }

public void setStructureServer(NBTTagCompound tag)
  {
//  Config.logDebug("Setting structure to edit");
  this.currentEditingStructure = tag.getString("setStructure");
  this.currentSelectedStructure = this.currentEditingStructure;
  this.serverStructure = StructureManager.instance().getStructureServer(currentEditingStructure);
  if(this.serverStructure==null)
    {
    Config.logError("Severe error attempting to set structure for editing: "+this.currentEditingStructure);
    this.currentEditingStructure= "";
    return;    
    } 
  if(this.serverStructure.openBuilderCount()>0)
    {
    NBTTagCompound rejectTag = new NBTTagCompound();
    rejectTag.setBoolean("badSel", true);
    this.sendDataToGUI(rejectTag);
    this.currentEditingStructure = "";
    this.serverStructure = null;
    return;
    }
  this.serverStructure.addEditor(this.player.getEntityName());
  this.structureFilePath = serverStructure.filePath;
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

/**
 * client side saveTemplate--attempts to validate lines before saving (attempt to process into structure on client-side)
 */
public void saveTemplate()
  {
//  Config.logDebug("sending template to server....");
//  Config.logDebug("validating structure client side...");
  ProcessedStructure struct = StructureLoader.instance().loadStructureAW(clientLines, "0");
  if(struct==null)
    {
//    Config.logDebug("Invalid structure client-side, cannot send to server");
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("badSave", true);
    this.gui.handleDataFromContainer(tag);
    }  
  else
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("goodSave", true);
    this.gui.handleDataFromContainer(tag);
    }
//  Config.logDebug("structure is valid, sending to server...");
  byte[] allBytes;
  try
    {
    allBytes = StringTools.getByteArray(clientLines);
    } 
  catch (UnsupportedEncodingException e)
    {    
    e.printStackTrace();
    return;
    } 
  catch (IOException e)
    {    
    e.printStackTrace();
    return;
    }
  int packetSize = 8192;
  List<byte[]> chunks = ByteTools.getByteChunks(allBytes, packetSize);
  for(int i = 0; i < chunks.size(); i++)
    {
//    Config.logDebug("sending template chunk to server");
    NBTTagCompound outerTag = new NBTTagCompound();
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("num", i);
    tag.setInteger("of",chunks.size());
    tag.setInteger("size", packetSize);
    tag.setByteArray("bytes", chunks.get(i));
    outerTag.setTag("saveData", tag);
    this.sendDataToServer(outerTag);
    }
  }

private void handlePartialTemplateServer(NBTTagCompound tag)
  {
  int packetNum = tag.getInteger("num");
  int packetsTotal = tag.getInteger("of");
  int packetSize = tag.getInteger("size");
  byte[] packetBytes = tag.getByteArray("bytes");
  if(this.packetData==null)
    {
    this.recievedParts= 0;
    this.totalParts = packetsTotal;
    this.packetData = new byte[packetsTotal][packetSize];
    }
  this.packetData[packetNum]=packetBytes;
  this.recievedParts++;  
  if(this.recievedParts>=this.totalParts)
    {
    byte [] allBytes = ByteTools.compositeByteChunks(packetData);
    List<String> lines = StringTools.getLines(allBytes);
    String md5 = StructureLoader.instance().getMD5(allBytes);
    packetData = null;
    ProcessedStructure struct = StructureLoader.instance().loadStructureAW(lines, md5);
    if(struct!=null)
      {      
//      Config.logDebug("returned valid structure on server");
      this.saveTemplateServer(struct);
      }
    }
  }

private void saveTemplateServer(ProcessedStructure newStruct)
  {
  newStruct.filePath = serverStructure.filePath;
  newStruct.name = serverStructure.name;
  if(!StructureExporter.writeStructureToFile(newStruct, newStruct.filePath, true))
    {
    Config.logError("Error exporting structure: "+newStruct.name);
    }
  StructureManager.instance().addStructure(newStruct, true);
  serverStructure = newStruct;
  serverStructure.addEditor(this.player.getEntityName());
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {  
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return Collections.emptyList();
  }

@Override
public void onContainerClosed(EntityPlayer par1EntityPlayer)
  {
  if(this.serverStructure!=null)
    {
    this.serverStructure.removeEditor(this.player.getEntityName());
    }
  super.onContainerClosed(par1EntityPlayer);
  }



}
