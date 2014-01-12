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
package shadowmage.ancient_warfare.common.network;

import net.minecraft.entity.Entity;
import shadowmage.ancient_warfare.common.interfaces.IEntityPacketHandler;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class Packet06Entity extends PacketBase
{

int entityID;

public void setHealthUpdate(int health)
  {
  this.packetData.setInteger("health", health);
  }

public void setParams(Entity ent)
  {  
  this.entityID = ent.entityId;
  }
@Override
public String getChannel()
  {
  return "AW_mod";
  }

@Override
public int getPacketType()
  {
  return 6;
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
  Entity ent = world.getEntityByID(entityID);
  if(ent instanceof IEntityPacketHandler)
    {
    ((IEntityPacketHandler)ent).onPacketDataReceived(packetData);
    }  
  }


}
