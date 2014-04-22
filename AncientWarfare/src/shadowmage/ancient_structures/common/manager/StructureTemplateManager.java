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
package shadowmage.ancient_structures.common.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import shadowmage.ancient_structures.common.config.AWLog;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.StructureTemplateClient;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.network.Packet07StructureData;

public class StructureTemplateManager
{
private StructureTemplateManager(){}
private static StructureTemplateManager instance = new StructureTemplateManager(){};
public static StructureTemplateManager instance(){return instance;}

private HashMap<String,StructureTemplate> loadedTemplates = new HashMap<String,StructureTemplate>();
private HashMap<String,StructureTemplateClient> serverCopyOfClientTemplates = new HashMap<String,StructureTemplateClient>();
private HashMap<String,StructureTemplateClient> clientTemplates = new HashMap<String,StructureTemplateClient>();

public void addTemplate(StructureTemplate template)
  {
  if(template.getValidationSettings()==null)
    {
    AWLog.logError("Could not load template for: " + template.name + " no validation settings present.");
    return;
    }
  if(template.getValidationSettings().isWorldGenEnabled())
    {
    WorldGenStructureManager.instance().registerWorldGenStructure(template);    
    }
  loadedTemplates.put(template.name, template);
  StructureTemplateClient cl = new StructureTemplateClient(template);
  serverCopyOfClientTemplates.put(cl.name, cl);
      
  MinecraftServer server = MinecraftServer.getServer();
  if(server!=null && server.isServerRunning() && server.getConfigurationManager()!=null)
    {
    NBTTagCompound tag = new NBTTagCompound();
    cl.writeToNBT(tag);    
    Packet07StructureData pkt = new Packet07StructureData();
    pkt.packetData.setTag("singleStructure", tag);
    pkt.sendPacketToAllPlayers();    
    }
  }

public void addTemplate(StructureTemplateClient template)
  {
  clientTemplates.put(template.name, template);
  AWCraftingManager.instance().addStructureRecipe(template);
  }

public void onPlayerConnect(EntityPlayer player)
  {
  NBTTagList list = new NBTTagList();
  for(StructureTemplateClient cl : serverCopyOfClientTemplates.values())
    {
    NBTTagCompound tag = new NBTTagCompound();
    cl.writeToNBT(tag);
    list.appendTag(tag);
    }
  Packet07StructureData pkt = new Packet07StructureData();
  pkt.packetData.setTag("structureList", list);
  pkt.sendPacketToPlayer(player);
  }

public void onTemplateData(NBTTagCompound tag)
  {
  if(tag.hasKey("singleStructure"))
    {
    NBTTagCompound structureTag = tag.getCompoundTag("singleStructure");
    readClientStructure(structureTag);
    }
  else
    {
    NBTTagList list = tag.getTagList("structureList");
    NBTTagCompound structureTag;
    for(int i = 0; i < list.tagCount(); i++)
      {
      structureTag = (NBTTagCompound) list.tagAt(i);
      readClientStructure(structureTag);
      }    
    }
  }

private void readClientStructure(NBTTagCompound tag)
  {
  StructureTemplateClient template = StructureTemplateClient.readFromNBT(tag);
  addTemplate(template);
  }

public void getSurvivalTemplates(List<StructureTemplate> templates)
  {
  for(StructureTemplate template : this.loadedTemplates.values())
    {
    if(template.getValidationSettings().isSurvival())
      {
      templates.add(template);
      }
    }
  }

public void getClientSurvivalTemplates(List<StructureTemplateClient> templates)
  {
  for(StructureTemplateClient template : this.clientTemplates.values())
    {
    if(template.survival)
      {
      templates.add(template);
      }
    }
  }

public Collection<StructureTemplateClient> getClientStructures()
  {
  return clientTemplates.values();
  }

public StructureTemplateClient getClientTemplate(String name)
  {
  return clientTemplates.get(name);
  }

public StructureTemplate getTemplate(String name)
  {
  return this.loadedTemplates.get(name);
  }

}
