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
package shadowmage.ancient_warfare.common.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.warzone.Warzone;
import shadowmage.ancient_warfare.common.warzone.WarzoneManager;

public class ContainerWarzones extends ContainerBase
{

int zonesLength;
public List<Warzone> zoneList = new ArrayList<Warzone>();

/**
 * @param openingPlayer
 */
public ContainerWarzones(EntityPlayer openingPlayer)
  {
  super(openingPlayer, null);
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("zoneList"))
    {
    Config.logDebug("handlePacketData handling zoneList packet");
    this.readWarzones(tag);
    this.refreshGui();
    }
  if(tag.hasKey("removal"))
    {
    Warzone z = new Warzone();
    z.readFromNBT(tag);
    WarzoneManager.instance().removeWarzone(player.worldObj, z.min, z.max);
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  Config.logDebug("receiving init data...passing to handlePacket..");
  this.handlePacketData(tag);
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  List<NBTTagCompound> tags = new ArrayList<NBTTagCompound>(); 
  tags.add(getZoneListTag());
  return tags;
  }

@Override
public void detectAndSendChanges()
  {
  super.detectAndSendChanges();
  Collection<Warzone> zones = WarzoneManager.instance().getCurrentWarzones(player.worldObj);
  if(zones.size() != this.zonesLength)
    {
    this.sendDataToPlayer(getZoneListTag());
    }
  }

private NBTTagCompound getZoneListTag()
  {  
  Collection<Warzone> zones = WarzoneManager.instance().getCurrentWarzones(player.worldObj);
  this.zonesLength = zones.size();
  NBTTagCompound zonestag = new NBTTagCompound();
  NBTTagList zoneList = new NBTTagList();
  NBTTagCompound zt;
  for(Warzone zone : zones)
    {
    zt = new NBTTagCompound();
    zone.writeToNBT(zt);
    zoneList.appendTag(zt);
    }
  zonestag.setTag("zoneList", zoneList);
  return zonestag;
  }

private void readWarzones(NBTTagCompound tag)
  {
  Config.logDebug("reading zones tag...");
  this.zoneList.clear();
  NBTTagList zoneList = tag.getTagList("zoneList");
  NBTTagCompound zoneTag;
  Warzone zone;
  for(int i = 0; i < zoneList.tagCount(); i++)
    {
    zoneTag = (NBTTagCompound) zoneList.tagAt(i);
    zone = new Warzone();
    zone.readFromNBT(zoneTag);
    this.zoneList.add(zone);
    }
  Config.logDebug("read: "+this.zoneList.size() + " zones from tag");
  }



}
