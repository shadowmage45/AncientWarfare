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
package shadowmage.ancient_structures.common.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_structures.common.config.AWLog;
import shadowmage.ancient_structures.common.item.AWStructuresItemLoader;
import shadowmage.ancient_warfare.common.container.ContainerBase;

public class ContainerSpawnerPlacer extends ContainerBase
{



public String mobID = "Pig";
public int minSpawnDelay = 200;
public int maxSpawnDelay = 800;
public int spawnCount = 4;
public int maxNearbyEntities = 6;
public int activatingRangeFromPlayer = 16;
public int spawnRange = 4;

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerSpawnerPlacer(EntityPlayer openingPlayer, int x, int y, int z)
  {
  super(openingPlayer, null);
  
  ItemStack builderItem = player.inventory.getCurrentItem(); 
  if(builderItem.hasTagCompound() && builderItem.getTagCompound().hasKey("spawnData"))
    {
    NBTTagCompound tag = builderItem.getTagCompound().getCompoundTag("spawnData");
    this.mobID = tag.getString("EntityId");
    this.minSpawnDelay = tag.getShort("MinSpawnDelay");
    this.maxSpawnDelay = tag.getShort("MaxSpawnDelay");
    this.spawnCount = tag.getShort("SpawnCount");
    this.maxNearbyEntities = tag.getShort("MaxNearbyEntities");
    this.activatingRangeFromPlayer = tag.getShort("RequiredPlayerRange");
    this.spawnRange = tag.getShort("SpawnRange");
    }
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("EntityId"))
    {
    this.mobID = tag.getString("EntityId");
    this.minSpawnDelay = tag.getShort("MinSpawnDelay");
    this.maxSpawnDelay = tag.getShort("MaxSpawnDelay");
    this.spawnCount = tag.getShort("SpawnCount");
    this.maxNearbyEntities = tag.getShort("MaxNearbyEntities");
    this.activatingRangeFromPlayer = tag.getShort("RequiredPlayerRange");
    this.spawnRange = tag.getShort("SpawnRange");
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  handlePacketData(tag);
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  NBTTagCompound tag = new NBTTagCompound(); 
  tag.setString("EntityId", mobID);
  tag.setShort("MinSpawnDelay", (short) minSpawnDelay);
  tag.setShort("MaxSpawnDelay", (short) maxSpawnDelay);
  tag.setShort("SpawnCount", (short) spawnCount);
  tag.setShort("MaxNearbyEntities", (short) maxNearbyEntities);
  tag.setShort("RequiredPlayerRange", (short) activatingRangeFromPlayer);
  tag.setShort("SpawnRange", (short) spawnRange);  
  ArrayList<NBTTagCompound> list = new ArrayList<NBTTagCompound>();  
  list.add(tag);  
  return list;
  }

@Override
public void onContainerClosed(EntityPlayer par1EntityPlayer)
  {
  super.onContainerClosed(par1EntityPlayer);
  if(par1EntityPlayer.worldObj.isRemote)
    {
    NBTTagCompound tag = new NBTTagCompound(); 
    tag.setString("EntityId", mobID);
    tag.setShort("MinSpawnDelay", (short) minSpawnDelay);
    tag.setShort("MaxSpawnDelay", (short) maxSpawnDelay);
    tag.setShort("SpawnCount", (short) spawnCount);
    tag.setShort("MaxNearbyEntities", (short) maxNearbyEntities);
    tag.setShort("RequiredPlayerRange", (short) activatingRangeFromPlayer);
    tag.setShort("SpawnRange", (short) spawnRange);  
    sendDataToServer(tag);    
    return;
    }
  ItemStack builderItem = player.inventory.getCurrentItem();  
  if(builderItem==null || builderItem.getItem()==null || builderItem.getItem()!=AWStructuresItemLoader.spawnerPlacer)
    {
    return;
    }
  if(!builderItem.hasTagCompound() || !builderItem.getTagCompound().hasKey("spawnData"))
    {
    builderItem.setTagInfo("spawnData", new NBTTagCompound());
    }
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("EntityId", mobID);
  tag.setShort("MinSpawnDelay", (short) minSpawnDelay);
  tag.setShort("MaxSpawnDelay", (short) maxSpawnDelay);
  tag.setShort("SpawnCount", (short) spawnCount);
  tag.setShort("MaxNearbyEntities", (short) maxNearbyEntities);
  tag.setShort("RequiredPlayerRange", (short) activatingRangeFromPlayer);
  tag.setShort("SpawnRange", (short) spawnRange);  
  builderItem.getTagCompound().setTag("spawnData", tag);

  }

}
