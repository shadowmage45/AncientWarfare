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
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

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

public void setHealthUpdate(float health)
  {
  this.packetData.setFloat("health", health);
  }

public void setClientMoveData(NBTTagCompound tag)
  {
  this.packetData.setTag("clientMove", tag);
  }

public void setInputData(NBTTagCompound tag)
  {
  this.packetData.setTag("input", tag);
  }

public void setUpgradeData(NBTTagCompound tag)
  {
  this.packetData.setTag("upgrade", tag);
  }

public void setAmmoData(NBTTagCompound tag)
  {
  this.packetData.setTag("ammo", tag);
  }

public void setAmmoSelect(NBTTagCompound tag)
  {
  this.packetData.setTag("ammoSel", tag);
  }

public void setAmmoUpdate(NBTTagCompound tag)
  {
  this.packetData.setTag("ammoUpd", tag);
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
