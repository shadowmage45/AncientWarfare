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
package shadowmage.ancient_warfare.common.aw_core.network;

import net.minecraft.entity.Entity;
import shadowmage.ancient_warfare.common.aw_vehicles.VehicleBase;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class Packet02Vehicle extends PacketBase
{


int entityID;


@Override
public String getChannel()
  {  
  return "AW_vehicle";
  }

public void setParams(Entity ent)
  {  
  this.entityID = ent.entityId;
  }

@Override
public int getPacketType()
  {  
  return 2;
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
  VehicleBase vehicle = (VehicleBase) world.getEntityByID(entityID);
  if(vehicle!=null)
    {
    vehicle.handlePacketUpdate(packetData);
    }  
  }

}
