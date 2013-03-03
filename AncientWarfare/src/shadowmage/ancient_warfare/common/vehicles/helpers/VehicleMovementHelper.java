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
package shadowmage.ancient_warfare.common.vehicles.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class VehicleMovementHelper
{

private VehicleBase vehicle;
private byte forwardInput = 0;
private byte strafeInput = 0;

public float maxSpeedBase = 0.8f;
public float maxSpeedCurrent = 0.8f;

public float maxStrafeBase = 2.0f;
public float maxStrafeCurrent = 2.0f;

private float forwardAccel = 0;
private float strafeAccel = 0;
private float forwardMotion = 0;
private float strafeMotion = 0;

public VehicleMovementHelper (VehicleBase veh)
  {
  this.vehicle = veh;
  }

public void setForwardInput(byte in)
  {
  this.forwardInput = in;
  }

public void setStrafeInput(byte in)
  {
  this.strafeInput = in;
  }

public void handleKeyboardInput(byte forward, byte strafe)
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setByte("f", forward);
  tag.setByte("s", strafe);
  Packet02Vehicle pkt = new Packet02Vehicle();
  pkt.setParams(this.vehicle);
  pkt.setInputData(tag);
  pkt.sendPacketToServer();
  }

/**
 * handles input data from packets, updates local input state, and relays changes to clients (server only)
 * only relays data if move input has been received and changed from current state 
 * @param tag
 */
public void handleInputData(NBTTagCompound tag)
  {
  boolean inputChanged = false;
  if(tag.hasKey("f"))
    {
    this.setForwardInput(tag.getByte("f"));
    inputChanged = true;
    }
  if(tag.hasKey("s"))
    {
    this.setStrafeInput(tag.getByte("s"));
    inputChanged = true;
    }
  if(tag.hasKey("fMot"))
    {
    this.forwardMotion = tag.getFloat("fMot");
    }
  if(tag.hasKey("sMot"))
    {
    this.strafeMotion = tag.getFloat("sMot");
    }
  if(inputChanged && !this.vehicle.worldObj.isRemote)
    {
    tag = new NBTTagCompound();
    tag.setByte("f", forwardInput);
    tag.setByte("s", this.strafeInput);
    tag.setFloat("fMot", forwardMotion);
    tag.setFloat("sMot", strafeMotion);
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(this.vehicle);
    pkt.setInputData(tag);
    AWCore.proxy.sendPacketToAllClientsTracking(this.vehicle, pkt);
    }
  }

/**
 * called every tick from vehicle onUpdate
 */
public void onMovementTick()
  {
  if(forwardInput!=0)
    {
    forwardAccel = forwardInput * 0.03f * (this.maxSpeedCurrent - MathHelper.abs(forwardMotion));
    if(forwardInput<0)
      {
      forwardAccel *= 0.6f;
      }
    }
  else
    {
    forwardAccel = forwardMotion * -0.08f;
    }
  if(strafeInput!=0)
    {
    strafeAccel = -strafeInput * 0.06f * (this.maxStrafeCurrent-MathHelper.abs(strafeMotion));
    }
  else
    {
    strafeAccel = strafeMotion * -0.1f;
    }
  
  strafeMotion +=strafeAccel;
  forwardMotion +=forwardAccel;
  
  float absFor = MathHelper.abs(forwardMotion);
  float absStr = MathHelper.abs(strafeMotion);
    
  if(forwardInput ==1 && absFor > maxSpeedCurrent)
    {
    forwardMotion = maxSpeedCurrent;
    }
  else if(forwardInput == -1 && absFor > maxSpeedCurrent * 0.6f)
    {
    forwardMotion = -maxSpeedCurrent * 0.6f;
    }
  else if(absFor <= 0.02f && forwardInput == 0)
    {
    forwardMotion = 0;
    }
  if(absStr > maxStrafeCurrent)
    {
    if(strafeMotion>0)
      {
      strafeMotion = maxStrafeCurrent;
      }
    else
      {
      strafeMotion = -maxStrafeCurrent;
      }     
    }
  else if(absStr <= 0.2f && strafeInput == 0)
    {
    strafeMotion = 0;
    }  
  vehicle.wheelRotationPrev = vehicle.wheelRotation;
  vehicle.wheelRotation += forwardMotion*0.02f;
  vehicle.rotationYaw += strafeMotion;    
  if(strafeMotion !=0 || forwardMotion !=0)
    {
    vehicle.moveEntity(vehicle.motionX, 0, vehicle.motionZ);
    float x = Trig.sinDegrees(vehicle.rotationYaw)*-forwardMotion;
    float z = Trig.cosDegrees(vehicle.rotationYaw)*-forwardMotion;  
    vehicle.motionX = x;
    vehicle.motionZ = z;
    }
  }

/**
 * TODO clean this up...
 */
public void clearInputFromDismount()
  {
  this.setForwardInput((byte) 0);
  this.setStrafeInput((byte) 0);  
  NBTTagCompound tag = new NBTTagCompound();
  tag.setByte("f", (byte) 0);
  tag.setByte("s", (byte) 0);
  Packet02Vehicle pkt = new Packet02Vehicle();
  pkt.setParams(this.vehicle);
  pkt.setInputData(tag);
  if(!this.vehicle.worldObj.isRemote)
    {
    AWCore.proxy.sendPacketToAllClientsTracking(this.vehicle, pkt);
    }
  }

}
