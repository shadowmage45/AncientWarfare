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
package shadowmage.ancient_strategy.common.structure;

import java.util.UUID;

import cpw.mods.fml.common.IPlayerTracker;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.gamedata.AWGameData;
import shadowmage.ancient_strategy.common.network.Packet08Strategy;

public class StrategyStructureManager implements IPlayerTracker
{

private StrategyStructureManager(){}
private static StrategyStructureManager instance = new StrategyStructureManager();
public static StrategyStructureManager instance(){return null;}

StrategyStructureData clientData;

public StrategyStructure getStructureByID(World world, UUID id)
  {
  StrategyStructureData data = world.isRemote? clientData : AWGameData.get(world, StrategyStructureData.dataName, StrategyStructureData.class);
  return data==null ? null : data.getStructureByID(world, id);
  }

public void addStructure(World world, StrategyStructure structure)
  {
  StrategyStructureData data = world.isRemote? clientData : AWGameData.get(world, StrategyStructureData.dataName, StrategyStructureData.class);
  data.addStructure(world, structure);
  if(!world.isRemote)
    {
    /**
     * send add packet
     */
    }
  }

public void removeStructure(World world, UUID id)
  {
  StrategyStructureData data = world.isRemote? clientData : AWGameData.get(world, StrategyStructureData.dataName, StrategyStructureData.class);
  data.removeStructure(world, id);
  if(!world.isRemote)
    {
    /**
     * send remove packet
     */
    }
  }

public void handlePacketData(World world, NBTTagCompound tag)
  {
  if(tag.hasKey("clientInit"))
    {
    this.clientData = new StrategyStructureData();
    this.clientData.readFromNBT(tag.getCompoundTag("clientInit"));
    }
  else if(tag.hasKey("add"))
    {
    //read the client structure from an inner tag
    }
  else if(tag.hasKey("remove"))
    {
    //read the uuid msb/lsb, construct an uuid, and remove by key
    }
  }

//********************************************************************PLAYER TRACKER METHODS******************************************************************************//
@Override
public void onPlayerLogin(EntityPlayer player)
  {  
  if(player.worldObj.isRemote){return;}
  NBTTagCompound tag = new NBTTagCompound();
  StrategyStructureData data = AWGameData.get(player.worldObj, StrategyStructureData.dataName, StrategyStructureData.class);
  data.writeToNBT(tag);
  
  Packet08Strategy pkt = new Packet08Strategy();
  pkt.packetData.setCompoundTag("clientInit", tag);
  pkt.sendPacketToPlayer(player);
  }

@Override
public void onPlayerLogout(EntityPlayer player){}

@Override
public void onPlayerChangedDimension(EntityPlayer player){}

@Override
public void onPlayerRespawn(EntityPlayer player){}

}
