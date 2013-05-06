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

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class VehicleMovementHelper implements INBTTaggable
{

private VehicleBase vehicle;

private float forwardAccel = 0;
private float strafeAccel = 0;
public float forwardMotion = 0;
public float strafeMotion = 0;

public VehicleMovementHelper (VehicleBase veh)
  {
  this.vehicle = veh;
  }

/**
 * the one, the only, the setInput call....
 * @param forward
 * @param strafe
 */
public void setInput(byte forward, byte strafe)
  {
  if(!vehicle.isDrivable())
    {
    return;
    }
  if(vehicle.worldObj.isRemote)
    {
    if(Config.clientVehicleMovement)
      {
      vehicle.setForwardInput(forward);
      vehicle.setStrafeInput(strafe);      
      }    
    NBTTagCompound tag = new NBTTagCompound();  
    tag.setByte("f", forward);
    tag.setByte("s", strafe);
    tag.setFloat("fMot", forwardMotion);
    tag.setFloat("sMot", strafeMotion);
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(this.vehicle);
    pkt.setInputData(tag);
    pkt.sendPacketToServer();
    }
  else
    {
    vehicle.setForwardInput(forward);
    vehicle.setStrafeInput(strafe);
    }
  }

/**
 * recieve a input packet (input from client, or update data from server)
 * @param tag
 */
public void handleInputData(NBTTagCompound tag)
  {
  if(Config.clientVehicleMovement && vehicle.riddenByEntity!=null && vehicle.riddenByEntity==AWCore.proxy.getClientPlayer())//this is a client, with thePlayer riding
    {
    return;
    }
  boolean sendReply = false;
  if(tag.hasKey("f") && tag.hasKey("s"))
    {
    this.setInput(tag.getByte("f"), tag.getByte("s"));
    sendReply = true;
    } 
  if(tag.hasKey("fMot"))
    {
    this.forwardMotion = tag.getFloat("fMot");
    }
  if(tag.hasKey("sMot"))
    {
    this.strafeMotion = tag.getFloat("sMot");
    }
  if(sendReply && !this.vehicle.worldObj.isRemote)
    {
    tag = new NBTTagCompound();
    tag.setFloat("fMot", forwardMotion);
    tag.setFloat("sMot", strafeMotion);
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(this.vehicle);
    pkt.setInputData(tag);
    AWCore.proxy.sendPacketToAllClientsTracking(this.vehicle, pkt);
    }
  }

/**
 * called by navigator to move towards a current node, or current position
 * @param x
 * @param y
 * @param z
 */
public void setMoveTo(double x, double y, double z)
  {
  float yawDiff = Trig.getYawTowardsTarget(vehicle.posX, vehicle.posZ, x, z, vehicle.rotationYaw);  
  byte fMot = 0;
  byte sMot = 0;  
  if(Math.abs(yawDiff)>5)//more than 5 degrees off, correct yaw first, then move forwards
    {
    if(yawDiff<0)
      {
      sMot = 1;//left
      }
    else
      {
      sMot = -1;//right
      }
    }
  if(Math.abs(yawDiff)<10 && Trig.getVelocity(x-vehicle.posX, y-vehicle.posY, z-vehicle.posZ)>=1.f)//further away than 1 block, move towards it
    {
    fMot = 1;
    }
  setInput(fMot, sMot);
  }

/**
 * called every tick from vehicle onUpdate
 */
public void onMovementTick()
  {
  byte forwardInput = vehicle.getForwardInput();
  byte strafeInput = vehicle.getStrafeInput();
  this.vehicle.nav.onMovementUpdate();//vehicle navigator will provide input and other vehicle settings, if it has a node...
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
    vehicle.motionY -= (9.81f*0.05f*0.05f);//yes..vehicles will 'fall' whenever they are moving..it is what keeps them on the ground...
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
    this.tearUpGrass();
    }
  else if(forwardMotion==0)
    {
    vehicle.wheelRotationPrev = vehicle.wheelRotation;
    }  
  }

/**
 * tears up grass, flowers, snow, drops them as blocks in the world.
 */
public void tearUpGrass()
  {
  for(int var24 = 0; var24 < 4; ++var24)
    {      
    int x = MathHelper.floor_double(vehicle.posX + ((double)(var24 % 2) - 0.5D) * 0.8D);
    int y = MathHelper.floor_double(vehicle.posY);
    int z = MathHelper.floor_double(vehicle.posZ + ((double)(var24 / 2) - 0.5D) * 0.8D);
    //check top/upper blocks(riding through)
    int id = vehicle.worldObj.getBlockId(x, y, z);    
    if(isPlant(id))
      {
      BlockTools.breakBlockAndDrop(vehicle.worldObj, x, y, z);      
      }    
    //check lower blocks (riding on)
    if (vehicle.worldObj.getBlockId(x, y-1, z) == Block.grass.blockID)
      {
      vehicle.worldObj.setBlock(x, y-1, z, Block.dirt.blockID,0,3);
      }
    }
  }

protected static int[] plantBlockIDs = new int[]{Block.snow.blockID, Block.deadBush.blockID, Block.tallGrass.blockID, Block.plantRed.blockID, Block.plantYellow.blockID, Block.mushroomBrown.blockID, Block.mushroomRed.blockID};

protected boolean isPlant(int id)
  {
  for(int i =0; i < plantBlockIDs.length; i++)
    {
    if(id==plantBlockIDs[i])
      {
      return true;
      }
    }
  return false;
  }

public void clearInputFromDismount()
  {
  setInput((byte)0, (byte)0);
  }

public void resetUpgradeStats()
  {
 
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setByte("s", vehicle.getStrafeInput());
  tag.setByte("f", vehicle.getForwardInput());
  tag.setFloat("ms", strafeMotion);
  tag.setFloat("sa", strafeAccel);
  tag.setFloat("mf", forwardMotion);
  tag.setFloat("fa", forwardAccel);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  { 
  vehicle.setForwardInput(tag.getByte("f"));
  vehicle.setStrafeInput(tag.getByte("s"));  
  this.strafeMotion = tag.getFloat("ms");
  this.strafeAccel = tag.getFloat("sa");
  this.forwardMotion = tag.getFloat("mf");
  this.forwardAccel = tag.getFloat("fa");
  }

}
