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
public float launchPowerCurrent = launchPowerBase;

public Pos3f targetPos = null;

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
  }

public void handleInputData(NBTTagCompound tag)
  {
  
  vehicle.moveHelper.handleInputData(tag);
  if(tag.hasKey("fm") && (!this.isFiring || vehicle.worldObj.isRemote))//if fire command and not already firing (or is client)...
    {  
    if(tag.hasKey("fmx"))//if it has detailed position info..
      {
      this.targetPos = new Pos3f(tag.getFloat("fmx"), tag.getFloat("fmy"), tag.getFloat("fmz"));
      Config.logDebug("set targetPos to: "+targetPos.toString());
      }
    else
      {
      this.targetPos = null;
      }
    Config.logDebug("Initiating launch missile sequence");
    this.startMissileLaunch();
    
    
    
    if(!vehicle.worldObj.isRemote)//relay info to tracking clients..
      {
      Packet02Vehicle pkt = new Packet02Vehicle();
      pkt.setParams(vehicle);
      NBTTagCompound reply = new NBTTagCompound();
      reply.setBoolean("fm", true);
      if(tag.hasKey("fmx"))//relay hit target info, if applicable, else use current angle settings
        {
        reply.setFloat("fmx", tag.getFloat("fmx"));
        reply.setFloat("fmy", tag.getFloat("fmy"));
        reply.setFloat("fmz", tag.getFloat("fmz"));
        }
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
      if(this.targetPos!=null)
        {        
        float x = (float) (targetPos.x - vehicle.posX - off.x);
        float y = (float) (targetPos.y - vehicle.posY - off.y);
        float z = (float) (targetPos.z - vehicle.posZ - off.z);
        
        
        Config.logDebug("grabbing angles to hit target: "+x+","+y+","+z);
        float len = MathHelper.sqrt_float(x*x+z*z);
        Config.logDebug("distance " + len);
        Pair<Float, Float> angles = Trig.getLaunchAngleToHit(x, y, z, this.launchPowerCurrent);
        
        //get target angle from target pos, offset for vehicle offsets..
        if(angles.value()>=this.turretPitchMin && angles.value()<=this.turretPitchMax)
          {
          angle = angles.value();
          }
        else if(angles.key()>=this.turretPitchMin && angles.key() <= this.turretPitchMax)
          {
          angle = angles.key();
          }
        else
          {
          angle = 45;
          }
        this.turretPitch = angle;
        }      
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

public void handleFireInput(Vec3 target)
  {
  
  }

public void handleAimInput(Vec3 target)
  {
  
  }

public void handleAimUpdate(NBTTagCompound tag)
  {
  
  }

}
