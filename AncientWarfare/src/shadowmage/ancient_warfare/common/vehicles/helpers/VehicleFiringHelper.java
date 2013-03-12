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

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.config.Settings;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.missiles.MissileBase;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
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
public float clientLaunchSpeed = 0.f;

/**
 * used on final launch, to calc final angle from 'approximate' firing arm/turret angle
 */
public Pos3f targetPos = null;

/**
 * is this vehicle in the process of launching a missile ? (animation, etc)
 */
public boolean isFiring = false;

/**
 * has started launching...
 */
public boolean isLaunching = false;
/**
 * if this vehicle isFiring, has it already finished launched, and is in the process of cooling down?
 */
public boolean isReloading = false;

/**
 * how many ticks until this vehicle is done reloading and can fire again
 */
public int reloadingTicks = 0;

private VehicleBase vehicle;

public VehicleFiringHelper(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

/**
 * spawn a missile of current missile type, with current firing paramaters, with additional raw x, y, z offsets
 * @param ox
 * @param oy
 * @param oz
 */
public void spawnMissile(float ox, float oy, float oz)
  {
  if(!vehicle.worldObj.isRemote)
    {      
    if(vehicle.ammoHelper.getCurrentAmmoCount()>0)
      {
      vehicle.ammoHelper.decreaseCurrentAmmo(1);
      
      Pos3f off = vehicle.getMissileOffset();
      float x = (float) vehicle.posX + off.x + ox;
      float y = (float) vehicle.posY + off.y + oy;
      float z = (float) vehicle.posZ + off.z + oz;
      
      float power = vehicle.launchPowerCurrent> getAdjustedMaxMissileVelocity() ? getAdjustedMaxMissileVelocity() : vehicle.launchPowerCurrent;      
      float yaw = vehicle.turretRotation;
      float pitch = vehicle.turretPitch; 
      if(Config.adjustMissilesForAccuracy)
        {
        //TODO fix all this crap up, make a dedicated random somewhere for this
        //TODO check the variance on random, and if I am inverting properly
        Config.logDebug("spawning missile");
        Config.logDebug("orig params");
        Config.logDebug("po: "+power+" y: "+yaw+" pi: "+pitch);
        Random rnd = new Random();
        float accuracy = getAccuracyAdjusted();
        Config.logDebug("adj acc: "+accuracy);
        yaw   += (float)rnd.nextGaussian() * (1.f - accuracy)*10.f;
        if(vehicle.canAimPower())
          {
          power += (float)rnd.nextGaussian() * (1.f - accuracy)*2.5f; 
          if(power<1.f)
            {
            power=1.f;
            }
          }
        else if(vehicle.canAimPitch())
          {
          pitch += (float)rnd.nextGaussian() * (1.f - accuracy)*10.f;
          }        
        
        
        Config.logDebug("new params");
        Config.logDebug("po: "+power+" y: "+yaw+" pi: "+pitch);
        
        }
      
      MissileBase missile = vehicle.ammoHelper.getMissile2(x, y, z, yaw, pitch, power);
      if(missile!=null)
        {
        vehicle.worldObj.spawnEntityInWorld(missile);
        }
      }
    }
  }

public void onTick()
  {
  if(this.isReloading)
    {    
    this.vehicle.onReloadUpdate();
    if(this.reloadingTicks<=0)
      {
      this.setFinishedReloading();            
      }
    this.reloadingTicks--;
    }
  if(this.isFiring)
    {
    this.vehicle.onFiringUpdate();
    }
  if(this.isLaunching)
    {
    vehicle.onLaunchingUpdate();
    }  
  if(vehicle.worldObj.isRemote)
    {
    if(!vehicle.canAimPitch())
      {
      this.clientTurretPitch = vehicle.turretPitch;
      }
    if(!vehicle.canAimPower())
      {
      this.clientLaunchSpeed = vehicle.launchPowerCurrent;
      }
    if(!vehicle.canAimRotate())
      {
      this.clientTurretYaw = vehicle.rotationYaw;
      }
    }
  if(vehicle.turretPitch<vehicle.turretPitchMin)
    {
    vehicle.turretPitch = vehicle.turretPitchMin;    
    }
  else if(vehicle.turretPitch > vehicle.turretPitchMax)
    {
    vehicle.turretPitch = vehicle.turretPitchMax;
    }  
  if(!vehicle.canAimPower())
    {
    vehicle.launchPowerCurrent = vehicle.launchSpeedCurrentMax;
    }
  }

public float getAdjustedMaxMissileVelocity()
  {  
  float velocity = vehicle.launchSpeedCurrentMax;
  IAmmoType ammo = vehicle.ammoHelper.getCurrentAmmoType();
  if(ammo!=null)
    {
    float missileWeight = ammo.getAmmoWeight();
    if(missileWeight>vehicle.vehicleType.getMaxMissileWeight())
      {
      velocity *= vehicle.vehicleType.getMaxMissileWeight()/missileWeight;
      }
    }
  return velocity;
  }

public float getAccuracyAdjusted()
  {
  float accuracy = this.vehicle.currentAccuracy;
  if(vehicle.riddenByEntity!=null && vehicle.riddenByEntity instanceof NpcBase)
    {
    //TODO adjust accuracy by soldier accuracy offset amount...
    }  
  return accuracy;
  }

/**
 * if not already firing, this will initiate the launch sequence
 */
public void startMissileLaunch()
  {
  if(!this.isFiring && this.reloadingTicks <=0)
    {
    this.isFiring = true;
    this.isLaunching = false;
    this.isReloading = false;
    }
  }

public void setFinishedReloading()
  {
  this.isFiring = false;
  this.isReloading = false;
  this.isLaunching = false;
  this.reloadingTicks = 0;
  }

public void startLaunching()
  {
  this.isFiring = false;
  this.isLaunching = true;
  this.isReloading = false;
  }

public void setFinishedLaunching()
  {
  this.isFiring = false;
  this.isReloading = true;
  this.isLaunching = false;
  this.reloadingTicks = vehicle.reloadTimeCurrent; 
  }

public float getBestAngle(float a, float b)
  {
  if(a>=vehicle.turretPitchMin && a <=vehicle.turretPitchMax)
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
    vehicle.turretDestPitch = tag.getFloat("aimPitch");
    reply.setFloat("aimPitch", vehicle.turretDestPitch);
    Config.logDebug("setting desired turret pitch to: "+vehicle.turretDestPitch);
    } 
  if(tag.hasKey("aimYaw"))
    {
    sendReply = true;
    vehicle.turretDestRot = tag.getFloat("aimYaw");
    reply.setFloat("aimYaw", vehicle.turretDestRot);
    }
  if(tag.hasKey("aimPow"))
    {
    sendReply = true;
    vehicle.launchPowerCurrent = tag.getFloat("aimPow");
    reply.setFloat("aimPow", vehicle.launchPowerCurrent);
    }
  if(!vehicle.worldObj.isRemote && sendReply) 
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
 * params are in change DELTAS
 * @param pitch
 * @param yaw
 */
public void handleAimKeyInput(float pitch, float yaw)
  {
  Config.logDebug("receiving key input. pitch: "+pitch+ " yaw: "+yaw);
  boolean pitchUpdated = false;
  boolean powerUpdated = false;
  boolean yawUpdated = false;
  if(vehicle.canAimPitch())
    {
    float pitchTest = this.clientTurretPitch + pitch;
    if(pitchTest<vehicle.turretPitchMin)
      {
      pitchTest = vehicle.turretPitchMin;
      }
    else if(pitchTest>vehicle.turretPitchMax)
      {
      pitchTest = vehicle.turretPitchMax;
      }
    if(pitchTest!=this.clientTurretPitch)
      {
      pitchUpdated = true;
      this.clientTurretPitch = pitchTest;
      }
    }
  else if(vehicle.canAimPower())
    {
    float powerTest = clientLaunchSpeed + pitch;
    if(powerTest<0)
      {
      powerTest = 0;
      }
    else if(powerTest>getAdjustedMaxMissileVelocity())
      {
      powerTest = getAdjustedMaxMissileVelocity();
      }
    if(this.clientLaunchSpeed!=powerTest)
      {
      powerUpdated =true;
      this.clientLaunchSpeed = powerTest;
      }
    }
  if(vehicle.canAimRotate())
    {
    yawUpdated = true;
    this.clientTurretYaw += yaw;
    }
  
  if(powerUpdated || pitchUpdated || yawUpdated)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("aim", true);
    if(pitchUpdated)
      {
      tag.setFloat("aimPitch", clientTurretPitch);      
      }
    if(powerUpdated)
      {
      tag.setFloat("aimPow", clientLaunchSpeed);
      }
    if(yawUpdated)
      {
      tag.setFloat("aimYaw", clientTurretYaw);
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
public void handleAimMouseInput(Vec3 target)
  {  
  boolean updated = false;
  boolean updatePitch = false;
  boolean updatePower = false;
  boolean updateYaw = false;
  Pos3f offset = vehicle.getMissileOffset();
  float x = (float) vehicle.posX + offset.x;
  float y = (float) vehicle.posY + offset.y;
  float z = (float) vehicle.posZ + offset.z;
  float tx = (float) (target.xCoord - x);
  float ty = (float) (target.yCoord - y);
  float tz = (float) (target.zCoord - z);
  float range = MathHelper.sqrt_float(tx*tx+tz*tz);
  if(vehicle.canAimPitch())
    {   
    Pair<Float, Float> angles = Trig.getLaunchAngleToHit(tx, ty, tz, vehicle.launchPowerCurrent);  
    if(angles.key().isNaN() || angles.value().isNaN())
      { 
      }
    else if(angles.value()>=vehicle.turretPitchMin && angles.value()<=vehicle.turretPitchMax)
      {
      if(this.clientTurretPitch!=angles.value())
        {
        this.clientTurretPitch = angles.value();
        updated = true;
        updatePitch = true;
        }
      }
    else if(angles.key()>=vehicle.turretPitchMin && angles.key() <= vehicle.turretPitchMax)
      {
      if(this.clientTurretPitch!=angles.key())
        {
        this.clientTurretPitch = angles.key();
        updated = true;
        updatePitch = true;
        }
      }
    }
  else if(vehicle.canAimPower())
    {     
    float power = Trig.iterativeSpeedFinder(tx, ty, tz, vehicle.turretPitch, Settings.getClientPowerIterations());
    if(this.clientLaunchSpeed!=power && power < getAdjustedMaxMissileVelocity())
      {
      this.clientLaunchSpeed = power;
      updated = true;
      updatePower = true;
      }
    }  
  if(vehicle.canAimRotate())
    {
    float xAO = (float) (vehicle.posX - target.xCoord);  
    float zAO = (float) (vehicle.posZ - target.zCoord);
    float yaw = Trig.toDegrees((float) Math.atan2(xAO, zAO));
    if(yaw!=this.clientTurretYaw && yaw >=vehicle.turretRotationHome - vehicle.turretRotationMax && yaw <= vehicle.turretRotationHome + vehicle.turretRotationMax)
      {    
      this.clientTurretYaw = yaw;
      updated = true;
      updateYaw = true;
      }  
    }
  if(!vehicle.canAimPitch())
    {
    this.clientTurretPitch = vehicle.turretPitch;
    }
  if(!vehicle.canAimPower())
    {
    this.clientLaunchSpeed = vehicle.launchPowerCurrent;
    }
  if(!vehicle.canAimRotate())
    {
    this.clientTurretYaw = vehicle.rotationYaw;
    }
  if(updated)
    {
    this.clientHitRange = range;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("aim", true);
    if(updatePitch)
      {
      tag.setFloat("aimPitch", this.clientTurretPitch);
      }
    if(updateYaw)
      {
      tag.setFloat("aimYaw", this.clientTurretYaw);
      }
    if(updatePower)
      {
      tag.setFloat("aimPow", this.clientLaunchSpeed);
      }
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
  tag.setInteger("rt", reloadingTicks);
  tag.setBoolean("f", this.isFiring);
  tag.setBoolean("r", this.isReloading);
  tag.setBoolean("l", this.isLaunching);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.reloadingTicks = tag.getInteger("rt");
  this.isFiring = tag.getBoolean("f");
  this.isReloading = tag.getBoolean("r");
  this.isLaunching = tag.getBoolean("l");
  }

public void resetUpgradeStats()
  {
  
  }

}
