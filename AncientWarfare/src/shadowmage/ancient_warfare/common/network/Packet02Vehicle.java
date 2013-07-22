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
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class Packet02Vehicle extends PacketBase
{
int entityID;
boolean isPosition = false;
boolean airData = false;
boolean isRotation = false;
VehicleBase vehicle;

@Override
public String getChannel()
  {  
  return "AW_vehicle";
  }

public void setParams(Entity ent)
  {  
  this.entityID = ent.entityId;
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

public void setPackCommand()
  {
  this.packetData.setBoolean("pack", true);
  }

public void setTurretParams(NBTTagCompound tag)
  {
  this.packetData.setCompoundTag("turret", tag);
  }

public void setMoveUpdate(VehicleBase vehicle, boolean pos, boolean airData, boolean rot)
  {
  this.vehicle = vehicle;
  this.entityID = vehicle.entityId;
  this.isPosition = pos;
  this.airData = airData;
  this.isRotation = rot;
  this.packetData.setBoolean("moveData", true);
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
  data.writeBoolean(isPosition);
  if(isPosition)
    {
    data.writeFloat((float) vehicle.posX);
    data.writeFloat((float) vehicle.posY);
    data.writeFloat((float) vehicle.posZ);
    }
  data.writeBoolean(airData);
  if(airData)
    {
    data.writeFloat((float) vehicle.moveHelper.throttle);
    }
  data.writeBoolean(isRotation);
  if(isRotation)
    {
    data.writeFloat(vehicle.rotationYaw);
    data.writeFloat(vehicle.rotationPitch);
    }
  }

@Override
public void readDataStream(ByteArrayDataInput data)
  {
  this.entityID = data.readInt();
  boolean flag = data.readBoolean();
  if(flag)//position data
    {
    packetData.setFloat("px", data.readFloat());
    packetData.setFloat("py", data.readFloat());
    packetData.setFloat("pz", data.readFloat());
    }
  flag = data.readBoolean();
  if(flag)//air data
    {
    packetData.setFloat("tr", data.readFloat());
    }
  flag = data.readBoolean();
  if(flag)//rotation data
    {
    packetData.setFloat("ry", data.readFloat());
    packetData.setFloat("rp", data.readFloat());
    }
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
