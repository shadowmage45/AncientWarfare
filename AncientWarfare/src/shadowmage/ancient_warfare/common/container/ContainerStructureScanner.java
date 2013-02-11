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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.AWStructureModule;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemStructureScanner;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.file.StructureExporter;
import shadowmage.ancient_warfare.common.structures.file.StructureExporterRuins;
import shadowmage.ancient_warfare.common.world_gen.WorldGenStructureManager;

public class ContainerStructureScanner extends ContainerBase
{

/**
 * client-side values, used in GUI
 */
public int xSize;
public int ySize;
public int zSize;

/**
 * server side values, used during exporting
 */
String name = "default";
boolean world = false;
boolean survival = false;

/**
 * what formats to export to
 */
boolean formatAW = true;
boolean formatRuins = false;

/**
 * should export to include directory, and add to in-game lists?
 */
boolean includeOnExport = false;


/**
 * @param openingPlayer
 * @param synch
 */
public ContainerStructureScanner(EntityPlayer openingPlayer)
  {
  super(openingPlayer, null);
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  /**
   * server side only
   */
  if(tag.hasKey("clearItem"))
    {
    this.clearItem();
    return;
    }  
  if(tag.hasKey("export"))
    {
    this.handleExportSettings(tag.getCompoundTag("export"));
    this.export();
    this.clearItem();
    }
  if(tag.hasKey("edit"))
    {
    this.handleEditServer(tag.getCompoundTag("edit"));
    }
  }

/**
 * server side only
 * @param tag
 */
public void handleExportSettings(NBTTagCompound tag)
  {  
  if(tag.hasKey("name"))
    {
    this.name = tag.getString("name");
    }
  if(tag.hasKey("world"))
    {
    this.world = tag.getBoolean("world");
    }  
  if(tag.hasKey("surv"))
    {
    this.survival = tag.getBoolean("surv");
    }
  if(tag.hasKey("fR"))//formatRuins
    {
    this.formatRuins = tag.getBoolean("fR");
    }
  if(tag.hasKey("fAW"))//formatAW
    {
    this.formatAW = tag.getBoolean("fAW");
    }
  if(tag.hasKey("inc"))//includeOnExport
    {
    this.includeOnExport = tag.getBoolean("inc");
    }
  }

public void sendSettingsAndExport(String name, boolean world, boolean surv, boolean fR, boolean fAW, boolean inc)
  {
  NBTTagCompound baseTag = new NBTTagCompound();
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", name);
  tag.setBoolean("world", world);
  tag.setBoolean("surv", surv);
  tag.setBoolean("fR", fR);
  tag.setBoolean("fAW", fAW);
  tag.setBoolean("inc", inc);
  
  baseTag.setTag("export", tag);
  this.sendDataToServer(baseTag);
  }

public void handleEditServer(NBTTagCompound tag)
  {
  this.handleExportSettings(tag);
  this.export();
  ContainerEditor edit = new ContainerEditor(player);
  edit.setStructureServer(tag);
  //TODO manually set structure of edit to the exported name of this structure....
  player.openContainer = edit;
  }

public void handleEditClient(String name, boolean world, boolean surv, boolean fR, boolean fAW, boolean inc)
  {
  NBTTagCompound baseTag = new NBTTagCompound();
  NBTTagCompound tag = new NBTTagCompound();
  
  tag.setString("name", name);
  tag.setBoolean("world", world);
  tag.setBoolean("surv", surv);
  tag.setBoolean("fR", fR);
  tag.setBoolean("fAW", fAW);
  tag.setBoolean("inc", inc);
  tag.setString("setStructure", name);
  
  baseTag.setTag("edit", tag);
  this.sendDataToServer(baseTag);
  }

/**
 * server side only
 */
public void export()
  {
  ProcessedStructure struct = ItemStructureScanner.scannedStructures.get(this.player);
  ItemStructureScanner.scannedStructures.remove(this.player);
  if(struct==null)
    {
    Config.logError("Could not locate structure for: "+this.player.getEntityName());
    return;
    }
  if(this.name==null || this.name.equals(""))
    {
    Config.logError("Improperly named structure for export");
    return;
    }
  struct.name = this.name;
  if(this.world)
    {
//    if(struct.worldGen)
//      {
//      WorldGenStructureManager.instance().addStructure(struct, false, 1, 1);//TODO add values to export config, or remove the entire thing...
//      }
    //TODO export to world-gen settings, update world-gen file....(dirty hack)
    }
  struct.survival = this.survival;
  String path;
  if(this.formatAW)
    {
    player.addChatMessage("Exporting structure to AW Format: "+name);
    if(!this.includeOnExport)
      {
      path = String.valueOf(AWStructureModule.outputDirectory+name+".aws");
      }
    else
      {
      path = String.valueOf(AWStructureModule.includeDirectory+name+".aws");
      }    
    boolean success = StructureExporter.writeStructureToFile(struct, path,false); 
    if(success && includeOnExport)
      {
      player.addChatMessage("Including structure in live structure lists");
      StructureManager.instance().addStructure(struct, true);
      }    
    else if(!success)
      {
      player.addChatMessage("Error exporting to AW Format, check naming");
      }
    }
  if(this.formatRuins)
    {
    player.addChatMessage("Exporting structure to Ruins Format");
    path = String.valueOf(AWStructureModule.outputDirectory+name+".tml");
    StructureExporterRuins.writeStructureToFile(struct, path);
    }  
  }

public void clearItem()
  {
  ItemStack stack = player.getCurrentEquippedItem();
  if(stack==null || !(stack.getItem() instanceof ItemStructureScanner))
    {
    Config.logError("Could not clear structure data from item, improper item detected");
    return;
    }
  ItemStructureScanner.clearStructureData(stack);
  ItemStructureScanner.scannedStructures.remove(player);
  player.addChatMessage("Clearing Structure Scanner Item, it is now ready to scan the next area");
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  this.xSize = tag.getInteger("x");
  this.ySize = tag.getInteger("y");
  this.zSize = tag.getInteger("z");
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  ArrayList<NBTTagCompound> packetTags = new ArrayList<NBTTagCompound>();
  NBTTagCompound tag = new NBTTagCompound();
  ProcessedStructure struct = ItemStructureScanner.scannedStructures.get(this.player);
  if(struct==null)
    {
    return null;
    }
  tag.setInteger("x", struct.xSize);
  tag.setInteger("y", struct.ySize);
  tag.setInteger("z", struct.zSize);  
  packetTags.add(tag);
  return packetTags;
  }

}
