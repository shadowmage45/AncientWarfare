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
package shadowmage.ancient_vehicles.common.vehicle;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityVehicle extends Entity implements IEntityAdditionalSpawnData
{

private VehicleType vehicleType;

public EntityVehicle(World par1World)
  {
  super(par1World);
  }

public EntityVehicle(World world, VehicleType type)
  {
  this(world);
  this.vehicleType = type;
  }

public VehicleType getVehicleType()
  {
  return this.vehicleType;
  }

@Override
protected void entityInit()
  {

  }

@Override
protected void readEntityFromNBT(NBTTagCompound tag)
  {

  }

@Override
protected void writeEntityToNBT(NBTTagCompound tag)
  {

  }

@Override
public void writeSpawnData(ByteArrayDataOutput data)
  {
  
  }

@Override
public void readSpawnData(ByteArrayDataInput data)
  {
  
  }

}
