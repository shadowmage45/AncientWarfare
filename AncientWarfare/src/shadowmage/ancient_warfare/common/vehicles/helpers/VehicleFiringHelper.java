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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.missiles.MissileBase;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.utils.Pair;
import shadowmage.ancient_warfare.common.utils.Pos3f;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;


/**
 * handles aiming, firing, updating turret, and client/server comms for input updates
 * @author Shadowmage
 *
 */
public class VehicleFiringHelper
{

/**
 * client-side values used by the riding player to check current input vs previous to see if new input packets should be sent...
 */
public float clientTurretYaw = 0.f;
public float clientTurretPitch = 0.f;

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
  }

public void updateTurretPitch()
  {
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

public void handleInputData(NBTTagCompound tag)
  {  
  this.handleAimUpdate(tag);
  //todo fire missile...
  if(tag.hasKey("fm") && (!this.isFiring || vehicle.worldObj.isRemote))//if fire command and not already firing (or is client)...
    { 
    Config.logDebug("Initiating launch missile sequence");
    this.startMissileLaunch();
    if(!vehicle.worldObj.isRemote)//relay info to tracking clients..
      {
      Packet02Vehicle pkt = new Packet02Vehicle();
      pkt.setParams(vehicle);
      NBTTagCompound reply = new NBTTagCompound();
      reply.setBoolean("fm", true);      
      pkt.setInputData(reply);
      pkt.sendPacketToAllTrackingClients(vehicle);      
      }
    }
 
  }

public boolean startMissileLaunch()
  {
  if(!this.isFiring && this.reloadingTicks <=0)
    {
    this.isFiring = true;
    return true;
    }
  return false;
  }



/**
 * has to be called from onFiringUpdate, triggers fireMissile()--
 * @return
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
      MissileBase missile = vehicle.ammoHelper.getMissile2(x, y, z, vehicle.rotationYaw, angle, launchPowerCurrent);
      if(missile!=null)
        {
        Config.logDebug("launching missile server side");
        vehicle.worldObj.spawnEntityInWorld(missile);
        }
//      this.debugLaunch();
      }
    return true;
    }  
  return false;
  }

public void handleFireInput()
  {
  if(!this.isFiring)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("fm", true);
    
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(vehicle);
    pkt.setInputData(tag);
    pkt.sendPacketToServer();
    }
  }

/**
 * used client side to update client desired pitch and yaw
 * @param target
 */
public void handleAimInput(Vec3 target)
  {
  if(target!=null)
    {    
    Pos3f offset = vehicle.getMissileOffsetForAim();
    float x = (float) vehicle.posX + offset.x;
    float y = (float) vehicle.posY + offset.y;
    float z = (float) vehicle.posZ + offset.z;
    float tx = (float) (target.xCoord - x);
    float ty = (float) (target.yCoord - y);
    float tz = (float) (target.zCoord - z);
    float range = MathHelper.sqrt_float(tx*tx+tz*tz);
    Pair<Float, Float> angles = Trig.getLaunchAngleToHit(tx, ty, tz, launchPowerCurrent);
    
    Config.logDebug("angle pair for hit: "+angles.toString()+" range: "+range);
    
    
    boolean updated = false;
    if(angles.value()>=this.turretPitchMin && angles.value()<=this.turretPitchMax)
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
    if(updated)
      {
      NBTTagCompound tag = new NBTTagCompound();
      //TODO add desired yaw...
      tag.setFloat("aimPitch", this.clientTurretPitch);
      
      
      Packet02Vehicle pkt = new Packet02Vehicle();
      pkt.setParams(vehicle);
      pkt.setInputData(tag);
      pkt.sendPacketToServer();
      }
    }
  /**
   * compare input to current, send new aim params to server
   */
  }

public void handleAimUpdate(NBTTagCompound tag)
  {
  boolean hadInput = false;
  if(tag.hasKey("aimPitch"))
    {
    hadInput = true;
    this.turretDestPitch = tag.getFloat("aimPitch");
    Config.logDebug("setting desired turret pitch to: "+this.turretDestPitch);
    }  
  //TODO handle other aim params...same as above...
  if(hadInput && !vehicle.worldObj.isRemote) 
    {
    NBTTagCompound reply = new NBTTagCompound();
    if(tag.hasKey("aimPitch"))
      {
      reply.setFloat("aimPitch", tag.getFloat("aimPitch"));
      }        
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(vehicle);
    pkt.setInputData(reply);
    pkt.sendPacketToAllTrackingClients(vehicle);
    }
  }

}
