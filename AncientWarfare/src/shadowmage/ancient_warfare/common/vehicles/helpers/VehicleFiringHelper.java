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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.missiles.MissileBase;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.utils.Pair;
import shadowmage.ancient_warfare.common.utils.Pos3f;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;


/**
 * handles aiming, firing, updating turret, and client/server comms for input updates
 * @author Shadowmage
 */
public class VehicleFiringHelper implements INBTTaggable
{

/**
 * these values are updated when the client chooses an aim point, used by overlay rendering gui 
 */
public float clientHitRange = 0.f;
public float clientHitPosX = 0.f;
public float clientHitPosY = 0.f;
public float clientHitPosZ = 0.f;

/**
 * client-side values used by the riding player to check current input vs previous to see if new input packets should be sent...
 */
public float clientTurretYaw = 0.f;
public float clientTurretPitch = 0.f;

/**
 * used on final launch, to calc final angle from 'approximate' firing arm/turret angle
 */
public Pos3f targetPos = null;

public float turretRotation = 0.f;
public float turretDestRot = 0.f;
public float turretRotationMin = 0.f;
public float turretRotationMax = 360.f;
public float turretRotInc = 1.f;

public float turretPitch = 45.f;
public float turretDestPitch = 45.f;
public float turretPitchMin = 0.f;
public float turretPitchMax = 90.f;
public float turretPitchInc = 1.f;

/**
 * in meters/second
 */
public float launchPowerBase = 31.321f;//the necessary speed to go 100m at near 45' angle(s) at 0 launch height
public float launchPowerCurrent = launchPowerBase * 0.65f;


/**
 * is this vehicle in the process of launching a missile ? (animation, etc)
 */
public boolean isFiring = false;

/**
 * if this vehicle isFiring, has it already launched, and is in the process of cooling down?
 */
public boolean hasLaunched = false;

/**
 * how many ticks until this vehicle is done reloading and can fire again
 */
public int reloadingTicks = 0;

/**
 * current and base reload timers, used to set current reload timer upon completion of missile launch
 */
public int reloadTimeBase = 100;
public int reloadTimeCurrent = 100;

/**
 * accuracy stats...
 */
public float accuracyBase = 1.f;
public float accuracyCurrent = 1.f;

private VehicleBase vehicle;

public VehicleFiringHelper(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

public void onTick()
  {
  if(this.isFiring)
    {
    this.vehicle.onFiringUpdate();
    }
  if(this.reloadingTicks>0)
    {
    this.reloadingTicks--;
    this.vehicle.onReloadUpdate();
    if(this.reloadingTicks<=0)
      {
      this.hasLaunched = false;
      this.isFiring = false;
      this.reloadingTicks = 0;      
      }
    }
  if(this.turretPitch!=this.turretDestPitch)
    {
    this.updateTurretPitch();
    }
  if(this.turretRotation!=this.turretDestRot)
    {
    this.updateTurretRotation();
    }
  }

public void updateTurretPitch()
  {
  if(!vehicle.canAimPitch())
    {
    this.turretDestPitch = this.turretPitch;
    return;
    }
  if(this.turretPitch>this.turretDestPitch)
    {
    this.turretPitch-=this.turretPitchInc;
    }
  else if(this.turretPitch<this.turretDestPitch)
    {
    this.turretPitch+=this.turretPitchInc;
    }
  if(Trig.getAbsDiff(this.turretDestPitch, this.turretPitch)<this.turretPitchInc)
    {
    this.turretPitch = this.turretDestPitch;
    }
  }

public void updateTurretRotation()
  {
  if(!vehicle.canAimRotate())
    {
    this.turretDestRot = this.turretRotation;
    return;
    }
  //TODO
  }

/**
 * if not already firing, this will initiate the launch sequence
 */
public void startMissileLaunch()
  {
  if(!this.isFiring && this.reloadingTicks <=0)
    {
    this.isFiring = true;
    }
  }

/**
 * has to be called from vehicle.onFiringUpdate, triggers fireMissile()--
 * @return true if missile was launched
 */
public boolean launchMissile()
  {
  if(this.isFiring && !this.hasLaunched)
    {
    this.hasLaunched = true;
    this.reloadingTicks = this.reloadTimeCurrent;
    if(!vehicle.worldObj.isRemote)
      {      
      Pos3f off = vehicle.getMissileOffset();
      Config.logDebug("offset: "+off.toString());
      float angle = this.turretPitch;
      float x = (float) vehicle.posX + off.x;
      float y = (float) vehicle.posY + off.y;
      float z = (float) vehicle.posZ + off.z;
      if(this.targetPos!=null && this.turretPitch==this.turretDestPitch)//if has target, and is lined up..do good fire..
        {
        float tx = this.targetPos.x - x;
        float ty = this.targetPos.y - y;
        float tz = this.targetPos.z - z;
        Pair<Float, Float> angles = Trig.getLaunchAngleToHit(tx, ty, tz, launchPowerCurrent);
        angle = getBestAngle(angles.value(), angles.key());
        }      
      MissileBase missile = vehicle.ammoHelper.getMissile2(x, y, z, vehicle.rotationYaw, angle, launchPowerCurrent);
      if(missile!=null)
        {
        Config.logDebug("launching missile server side");
        vehicle.worldObj.spawnEntityInWorld(missile);
        }
      }
    return true;
    }  
  return false;
  }

public float getBestAngle(float a, float b)
  {
  if(a>=this.turretPitchMin && a <=this.turretPitchMax)
    {
    return a;
    }
  else
    {
    return b;
    }
  }

/**
 * called from the vehicle to handle input packet data for firing and/or aim updates
 * @param tag
 */
public void handleInputData(NBTTagCompound tag)
  {  
  if(tag.hasKey("fm") && (!this.isFiring || vehicle.worldObj.isRemote))//if fire command and not already firing (or is client)...
    { 
    this.handleFireUpdate(tag);    
    } 
  if(tag.hasKey("aim"))
    {
    this.handleAimUpdate(tag);
    }
  }

/**
 * handle fire updates
 * @param tag
 */
public void handleFireUpdate(NBTTagCompound tag)
  {  
  if(tag.hasKey("fmx"))
    {
    this.targetPos = new Pos3f(tag.getFloat("fmx"), tag.getFloat("fmy"), tag.getFloat("fmz"));
    }
  else
    {
    this.targetPos = null;
    }
  if(!vehicle.worldObj.isRemote)//relay info to tracking clients..
    {
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(vehicle);
    NBTTagCompound reply = new NBTTagCompound();
    reply.setBoolean("fm", true);  
    if(targetPos!=null)
      {
      reply.setFloat("fmx", targetPos.x);
      reply.setFloat("fmy", targetPos.y);
      reply.setFloat("fmz", targetPos.z);
      }
    pkt.setInputData(reply);
    pkt.sendPacketToAllTrackingClients(vehicle); 
    }
  this.startMissileLaunch();
  }

/**
 * handle aim-update information
 * @param tag
 */
public void handleAimUpdate(NBTTagCompound tag)
  {
  NBTTagCompound reply = new NBTTagCompound();
  boolean sendReply = false;
  if(tag.hasKey("aimPitch"))
    {
    sendReply = true;
    this.turretDestPitch = tag.getFloat("aimPitch");
    reply.setFloat("aimPitch", this.turretDestPitch);
    Config.logDebug("setting desired turret pitch to: "+this.turretDestPitch);
    } 
  if(tag.hasKey("aimYaw"))
    {
    sendReply = true;
    this.turretDestRot = tag.getFloat("aimYaw");
    reply.setFloat("aimYaw", this.turretDestRot);
    }
  if(!vehicle.worldObj.isRemote) 
    { 
    reply.setBoolean("aim", true);
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(vehicle);
    pkt.setInputData(reply);
    pkt.sendPacketToAllTrackingClients(vehicle);
    }
  }

/**
 * CLIENT SIDE--handle fire-button input from riding client.  Relay to server.  Include target vector if appropriate.
 * @param target
 */
public void handleFireInput(Vec3 target)
  {
  if(!this.isFiring)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("fm", true);
    if(target!=null)
      {
      tag.setFloat("fmx", (float) target.xCoord);
      tag.setFloat("fmy", (float) target.yCoord);
      tag.setFloat("fmz", (float) target.zCoord);
      }
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(vehicle);
    pkt.setInputData(tag);
    pkt.sendPacketToServer();
    }
  }

/**
 * CLIENT SIDE--used client side to update client desired pitch and yaw and send these to server/other clients...
 * @param target
 */
public void handleAimInput(Vec3 target)
  {  
  boolean updated = false;
  Pos3f offset = vehicle.getMissileOffsetForAim();
  float x = (float) vehicle.posX + offset.x;
  float y = (float) vehicle.posY + offset.y;
  float z = (float) vehicle.posZ + offset.z;
  float tx = (float) (target.xCoord - x);
  float ty = (float) (target.yCoord - y);
  float tz = (float) (target.zCoord - z);
  float range = MathHelper.sqrt_float(tx*tx+tz*tz);  
  
  if(vehicle.canAimPitch())
    {   
    Pair<Float, Float> angles = Trig.getLaunchAngleToHit(tx, ty, tz, launchPowerCurrent);    
    Config.logDebug("input::::angle pair for hit: "+angles.toString()+" range: "+range);
    if(angles.key().isNaN() || angles.value().isNaN())
      {
      Config.logDebug("exiting aim pitch input due to NaN params!!");      
      }
    else if(angles.value()>=this.turretPitchMin && angles.value()<=this.turretPitchMax)
      {
      if(this.clientTurretPitch!=angles.value())
        {
        this.clientTurretPitch = angles.value();
        updated = true;
        }
      }
    else if(angles.key()>=this.turretPitchMin && angles.key() <= this.turretPitchMax)
      {
      if(this.clientTurretPitch!=angles.key())
        {
        this.clientTurretPitch = angles.key();
        updated = true;
        }
      }
    }
  
  if(vehicle.canAimRotate())
    {
    float xAO = (float) (vehicle.posX - target.xCoord);  
    float zAO = (float) (vehicle.posZ - target.zCoord);
    float yaw = Trig.toDegrees((float) Math.atan2(xAO, zAO));
    Config.logDebug("calculated yaw target: "+yaw+" vehicle current yaw: "+vehicle.rotationYaw);
    if(yaw!=this.clientTurretYaw && yaw >=this.turretRotationMin && yaw <= this.turretRotationMax)
      {    
      this.clientTurretYaw = yaw;
      updated = true;
      }  
    }
   
  if(updated)
    {
    this.clientHitRange = range;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("aim", true);
    tag.setFloat("aimPitch", this.clientTurretPitch);    
    tag.setFloat("aimYaw", this.clientTurretYaw);    
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(vehicle);
    pkt.setInputData(tag);
    pkt.sendPacketToServer();
    }
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setFloat("lb", this.launchPowerBase);
  tag.setFloat("lc", launchPowerCurrent);
  tag.setFloat("ab", accuracyBase);
  tag.setFloat("ac", accuracyCurrent);
  tag.setInteger("rb", reloadTimeBase);
  tag.setInteger("rc", reloadTimeCurrent);
  tag.setInteger("rt", reloadingTicks);
  tag.setFloat("tp", turretPitch);
  tag.setFloat("tpd", turretDestPitch);
  tag.setFloat("tr", turretRotation);
  tag.setFloat("trd", turretDestRot);
  tag.setFloat("trmn", turretRotationMin);
  tag.setFloat("trmx", turretRotationMax);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.launchPowerBase = tag.getFloat("lb");
  this.launchPowerCurrent = tag.getFloat("lc");
  this.accuracyBase = tag.getFloat("ab");
  this.accuracyCurrent = tag.getFloat("ac");
  this.reloadTimeBase = tag.getInteger("rb");
  this.reloadTimeCurrent = tag.getInteger("rc");
  this.reloadingTicks = tag.getInteger("rt");
  this.turretPitch = tag.getFloat("tp");
  this.turretDestPitch = tag.getFloat("tpd");
  this.turretRotation = tag.getFloat("tr");
  this.turretDestRot = tag.getFloat("trd");
  this.turretRotationMin = tag.getFloat("trmn");
  this.turretRotationMax = tag.getFloat("trmx");
  }

public void resetUpgradeStats()
  {
  this.launchPowerCurrent = this.launchPowerBase;
  this.accuracyCurrent = this.accuracyBase;
  this.reloadTimeCurrent = this.reloadTimeBase;
  }

}
