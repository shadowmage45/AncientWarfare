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
package shadowmage.ancient_warfare.common.network;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.npcs.NpcBase;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class Packet04Npc extends PacketBase
{

NpcBase npc;

int entityID;

public void setPathTarget(NBTTagCompound tag)
  {
  this.packetData.setCompoundTag("path", tag);
  }

public void setHealthUpdate(byte health)
  {
  this.packetData.setByte("health", health);
  }

public void setParams(Entity ent)
  {  
  this.entityID = ent.entityId;
  }
@Override
public String getChannel()
  {
  return "AW_soldier";
  }

@Override
public int getPacketType()
  {
  return 4;
  }

@Override
public void writeDataToStream(ByteArrayDataOutput data)
  {
  data.writeInt(entityID);
  }

@Override
public void readDataStream(ByteArrayDataInput data)
  {
  this.entityID = data.readInt();
  }

@Override
public void execute()
  {
  NpcBase npc = (NpcBase) world.getEntityByID(entityID);
  if(npc!=null)
    {
    npc.handlePacketUpdate(packetData);
    }  
  }

}
