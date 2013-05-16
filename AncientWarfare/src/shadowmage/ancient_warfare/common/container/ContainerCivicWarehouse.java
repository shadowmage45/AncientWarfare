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
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.civics.TECivicWarehouse;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.utils.ByteTools;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.utils.NBTWriter;
import shadowmage.ancient_warfare.common.utils.StackWrapper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class ContainerCivicWarehouse extends ContainerBase
{

TECivicWarehouse te;
public List<StackWrapper> warehouseItems = new ArrayList<StackWrapper>();

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerCivicWarehouse(EntityPlayer openingPlayer, TECivicWarehouse te)
  {
  super(openingPlayer, te);
  this.te = te;  
  if(!te.worldObj.isRemote)
    {
    this.warehouseItems = InventoryTools.getCompactedInventory(te.inventory);
    Config.logDebug("initializing server container. items length: "+warehouseItems.size());
    }
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  this.warehouseItems = InventoryTools.getCompactInventoryFromTag(tag);
  Config.logDebug("read warehouse items length: "+warehouseItems.size());
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  List<NBTTagCompound> tags = new ArrayList<NBTTagCompound>();
  tags.add(InventoryTools.getTagForCompactInventory(warehouseItems));
  return tags;
  }

}
