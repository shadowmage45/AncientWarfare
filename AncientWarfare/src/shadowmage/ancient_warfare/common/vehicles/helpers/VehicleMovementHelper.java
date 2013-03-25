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

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class VehicleMovementHelper implements INBTTaggable
{

private VehicleBase vehicle;
private byte forwardInput = 0;
private byte strafeInput = 0;



private float forwardAccel = 0;
private float strafeAccel = 0;
public float forwardMotion = 0;
public float strafeMotion = 0;

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
  if(!vehicle.isDrivable())
    {
    return;
    }
  NBTTagCompound tag = new NBTTagCompound();  
  tag.setByte("f", forward);
  tag.setByte("s", strafe);
  Packet02Vehicle pkt = new Packet02Vehicle();
  pkt.setParams(this.vehicle);
  pkt.setInputData(tag);
  pkt.sendPacketToServer();
  if(Config.clientVehicleMovement)
    {
    this.setForwardInput(forward);
    this.setStrafeInput(strafe);
    }
  }

/**
 * handles input data from packets, updates local input state, and relays changes to clients (server only)
 * only relays data if move input has been received and changed from current state 
 * @param tag
 */
public void handleInputData(NBTTagCompound tag)
  {
  if(Config.clientVehicleMovement && vehicle.riddenByEntity!=null && vehicle.riddenByEntity==AWCore.proxy.getClientPlayer())//this is a client, with thePlayer riding
    {
    return;
    }
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
  float weightAdjust = 1.f;
  if(vehicle.currentWeight > vehicle.baseWeight)
    {
    weightAdjust = vehicle.baseWeight  / vehicle.currentWeight;
    }
  if(forwardInput!=0)
    {
    forwardAccel = forwardInput * 0.03f * (vehicle.currentForwardSpeedMax*weightAdjust - MathHelper.abs(forwardMotion));
    if(forwardInput<0)
      {
      forwardAccel *= 0.6f;
      }
    forwardAccel *= weightAdjust;
    }
  else
    {
    forwardAccel = forwardMotion * -0.08f;
    }
  if(strafeInput!=0)
    {
    strafeAccel = -strafeInput * 0.06f * (vehicle.currentStrafeSpeedMax*weightAdjust -MathHelper.abs(strafeMotion));
    strafeAccel *= weightAdjust;
    if((strafeInput>0 && strafeMotion >0 ) || (strafeInput<0 && strafeMotion<0))
      {
      strafeAccel += strafeMotion * -0.13f;
      }
    }  
  else
    {
    strafeAccel = strafeMotion * -0.13f;
    }
  
  strafeMotion +=strafeAccel;
  forwardMotion +=forwardAccel;
  
  float absFor = MathHelper.abs(forwardMotion);
  float absStr = MathHelper.abs(strafeMotion);
    
  if(forwardInput ==1 && absFor > vehicle.currentForwardSpeedMax*weightAdjust)
    {
    forwardMotion = vehicle.currentForwardSpeedMax;
    }
  else if(forwardInput == -1 && absFor > vehicle.currentForwardSpeedMax * 0.6f)
    {
    forwardMotion = -vehicle.currentForwardSpeedMax * 0.6f;
    }
  else if(absFor <= 0.02f && forwardInput == 0)
    {
    forwardMotion = 0;
    }
  if(absStr > vehicle.currentStrafeSpeedMax * weightAdjust)
    {
    if(strafeMotion>0)
      {
      strafeMotion = vehicle.currentStrafeSpeedMax;
      }
    else
      {
      strafeMotion = -vehicle.currentStrafeSpeedMax;
      }     
    }
  else if(absStr <= 0.2f && strafeInput == 0)
    {
    strafeMotion = 0;
    }  
  
  if(!vehicle.onGround)
    {
    vehicle.motionY -= (9.81f*0.05f*0.05f);
//    Config.logDebug("vehicle not on ground, falling!! server:"+!vehicle.worldObj.isRemote+"  pos: "+vehicle.posX+","+vehicle.posY+","+vehicle.posZ);
    }
  else
    {
    vehicle.motionY = 0.f;
    }
  if(strafeMotion !=0 || forwardMotion !=0 || vehicle.motionY !=0)
    {    
    vehicle.moveEntity(vehicle.motionX, vehicle.motionY, vehicle.motionZ);
    float x = Trig.sinDegrees(vehicle.rotationYaw)*-forwardMotion;
    float z = Trig.cosDegrees(vehicle.rotationYaw)*-forwardMotion;  
    vehicle.motionX = x;
    vehicle.motionZ = z;   
    vehicle.rotationYaw += strafeMotion;  
    vehicle.wheelRotationPrev = vehicle.wheelRotation;
    vehicle.wheelRotation += forwardMotion*0.02f;
    }
  else if(forwardMotion==0)
    {
    vehicle.wheelRotationPrev = vehicle.wheelRotation;
    }
  if(!vehicle.worldObj.isRemote || (vehicle.worldObj.isRemote && Config.clientVehicleMovement))
    {
    
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

public void resetUpgradeStats()
  {
 
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setByte("s", strafeInput);
  tag.setByte("f", forwardInput);
  tag.setFloat("ms", strafeMotion);
  tag.setFloat("sa", strafeAccel);
  tag.setFloat("mf", forwardMotion);
  tag.setFloat("fa", forwardAccel);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  { 
  this.strafeInput = tag.getByte("s");
  this.forwardInput = tag.getByte("f");
  this.strafeMotion = tag.getFloat("ms");
  this.strafeAccel = tag.getFloat("sa");
  this.forwardMotion = tag.getFloat("mf");
  this.forwardAccel = tag.getFloat("fa");
  }

}
