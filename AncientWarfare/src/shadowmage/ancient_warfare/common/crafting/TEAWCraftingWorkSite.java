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
package shadowmage.ancient_warfare.common.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.worksite.TEWorkSite;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public abstract class TEAWCraftingWorkSite extends TEWorkSite implements ICraftingTE
{

byte orientation = 0;
byte modelID;

/**
 * 
 */
public TEAWCraftingWorkSite()
  {
  
  }

@Override
public boolean onInteract(World world, EntityPlayer player)
  {
  if(!world.isRemote)
    {
    this.onBlockClicked(player);
    }
  return true;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  super.readFromNBT(tag);
  this.readExtraNBT(tag);
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  this.writeExtraNBT(tag);
  }

@Override
public Packet getDescriptionPacket()
  {
  Packet132TileEntityData pkt = (Packet132TileEntityData) super.getDescriptionPacket();
  NBTTagCompound custData = new NBTTagCompound();
  this.writeDescriptionData(custData);
  pkt.customParam1.setTag("craftDesc", custData);
  return pkt;
  }

@Override
public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {  
  super.onDataPacket(net, pkt);
  this.readDescriptionPacket(pkt.customParam1.getCompoundTag("craftDesc"));
  }

@Override
public int getOrientation()
  {
  return orientation;
  }

@Override
public int getModelID()
  {
  return modelID;
  }

@Override
public void setOrientation(int face)
  {
  this.orientation = (byte) face;
  }

}
