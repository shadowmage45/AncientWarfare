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
package shadowmage.ancient_warfare.common.vehicles.helpers;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class VehicleMoveHelper
{

byte forwardInput;
byte strafeInput;
byte powerInput;
byte rotationInput;

protected VehicleBase vehicle;

public VehicleMoveHelper(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

public void handleInputData(NBTTagCompound tag)
  {  
  if(tag.hasKey("f"))
    {
    this.forwardInput=tag.getByte("f");    
    }
  if(tag.hasKey("s"))
    {
    this.strafeInput = tag.getByte("s");
    }
  if(tag.hasKey("p"))
    {
    this.powerInput = tag.getByte("p");
    }
  if(tag.hasKey("r"))
    {
    this.rotationInput = tag.getByte("r");
    }
  if(!vehicle.worldObj.isRemote)
    {
    tag = new NBTTagCompound();
    tag.setByte("f", forwardInput);
    tag.setByte("s", strafeInput);
    tag.setByte("p", powerInput);
    tag.setByte("r", rotationInput);    
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(vehicle);
    pkt.setInputData(tag);
    pkt.sendPacketToAllTrackingClients(vehicle);
    }
  }



public void onUpdate()
  {
  if(vehicle.worldObj.isRemote)
    {
    onUpdateClient();
    }
  else
    {
    onUpdateServer();
    }
  }

public void handleClientSynch(double x, double y, double z, float yaw, float pitch, int par9)
  {
  
  }

protected void onUpdateClient()
  {
  
  }

protected void onUpdateServer()
  {
  
  }


}
