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

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class VehicleMoveHelper implements INBTTaggable
{

byte forwardInput;
byte turnInput;
byte powerInput;
byte rotationInput;

double destX;
double destY;
double destZ;
float destYaw;
float destPitch;

int moveTicks = 0;
int rotationTicks = 0;
int pitchTicks = 0;

protected float forwardMotion = 0.f;
protected float verticalMotion = 0.f;
protected float turnMotion = 0.f;
protected float pitchMotion = 0.f;
protected float strafeMotion = 0.f;

protected float groundDrag = 0.96f;
protected float groundStop = 0.02f;
protected float rotationDrag = 0.94f;
protected float rotationStop = 0.2f;

protected float rotationSpeed = 0.f;
protected float pitchSpeed = 0.f;
protected VehicleBase vehicle;

public VehicleMoveHelper(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

public float getRotationSpeed()
  {
  return this.rotationSpeed;
  }

public void setForwardInput(byte in)
  {
  this.forwardInput = in;
  }

public void setStrafeInput(byte in)  
  {
  this.turnInput = in;
  }

public void handleMoveData(NBTTagCompound tag)
  {
  if(tag.hasKey("rp"))
    {
    this.pitchTicks = Config.vehicleMoveUpdateFrequency;
    this.destPitch = tag.getFloat("rp");
    }
  if(tag.hasKey("ry"))
    {
    this.rotationTicks = Config.vehicleMoveUpdateFrequency;
    this.destYaw = tag.getFloat("ry");
    }
  if(tag.hasKey("px"))
    {
    this.moveTicks = Config.vehicleMoveUpdateFrequency;
    this.destX = tag.getFloat("px");
    }
  if(tag.hasKey("py"))
    {
    this.moveTicks = Config.vehicleMoveUpdateFrequency;
    this.destY = tag.getFloat("py");
    }
  if(tag.hasKey("pz"))
    {
    this.moveTicks = Config.vehicleMoveUpdateFrequency;
    this.destZ = tag.getFloat("pz");
    }
  }

public void handleInputData(NBTTagCompound tag)
  {  
  if(tag.hasKey("f"))
    {
    this.forwardInput=tag.getByte("f");    
    }
  if(tag.hasKey("s"))
    {
    this.turnInput = tag.getByte("s");
    }
  if(tag.hasKey("p"))
    {
    this.powerInput = tag.getByte("p");
    }
  if(tag.hasKey("r"))
    {
    this.rotationInput = tag.getByte("r");
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

protected void onUpdateClient()
  {
  vehicle.motionX = 0;
  vehicle.motionY = 0;
  vehicle.motionZ = 0;
  rotationSpeed = 0;
  if(this.rotationTicks>0)
    {
    float dr = (this.destYaw - vehicle.rotationYaw)/rotationTicks;
    rotationSpeed = dr;
    this.rotationTicks--;
    } 
  vehicle.rotationYaw += this.rotationSpeed;
  if(moveTicks>0)
    {
    double dx = (destX - vehicle.posX)/(float)moveTicks;
    double dy = (destY - vehicle.posY)/(float)moveTicks;
    double dz = (destZ - vehicle.posZ)/(float)moveTicks;
    vehicle.motionX = dx;
    vehicle.motionY = dy;
    vehicle.motionZ = dz;  
    moveTicks--;
    }
  this.vehicle.moveEntity(vehicle.motionX, vehicle.motionY, vehicle.motionZ);
  }

protected void onUpdateServer()
  {
  switch(vehicle.vehicleType.getMovementType())
  {
  case GROUND:
  this.applyGroundMotion();
  break;
  
  case WATER:
  this.applyWaterMotion();
  break;
  
  case AIR1:
  this.applyAir1Motion();
  break;
  
  case AIR2:
  this.applyAir2Motion();
  break;  
  }
  this.vehicle.rotationYaw -= this.turnMotion;
  this.vehicle.rotationPitch += this.pitchMotion;
  this.vehicle.moveEntity(vehicle.motionX, vehicle.motionY, vehicle.motionZ);
  this.tearUpGrass();
  boolean sendUpdate = (vehicle.motionX!=0 || vehicle.motionY!=0 || vehicle.motionZ!=0 || vehicle.rotationYaw!=vehicle.prevRotationYaw || vehicle.rotationPitch!=vehicle.prevRotationPitch);
  sendUpdate = sendUpdate || vehicle.riddenByEntity!=null;
  sendUpdate = sendUpdate && this.vehicle.ticksExisted % Config.vehicleMoveUpdateFrequency==0;
  sendUpdate = sendUpdate || this.vehicle.ticksExisted%60==0; 
  if(sendUpdate)
    {
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setMoveUpdate(this.vehicle, true, false, true);
    pkt.sendPacketToAllTrackingClients(vehicle);
    }
  }

protected void applyGroundMotion()
  {
  this.applyForwardInput(0.0125f, true);
  this.applyTurnInput(0.05f);
  }

protected void applyWaterMotion()
  {
  this.applyForwardInput(0.0125f, true);
  this.applyTurnInput(0.05f);
  this.handleWaterMovement();
  }

protected void applyAir1Motion()
  {

  this.applyTurnInput(0.05f);
  }

protected void applyAir2Motion()
  {

  this.applyTurnInput(0.05f);
  }

protected void applyForwardInput(float inputFactor, boolean slowReverse)
  {
  float weightAdjust = 1.f;
  if(vehicle.currentWeight > vehicle.baseWeight)
    {
    weightAdjust = vehicle.baseWeight  / vehicle.currentWeight;
    }  
  if(forwardInput!=0)
    {
    boolean reverse = (forwardInput ==-1 && forwardMotion>0) || (forwardInput ==1 && forwardMotion<0);
    float maxSpeed = vehicle.currentForwardSpeedMax * weightAdjust;
    float maxReverse = -maxSpeed * (slowReverse? 0.6f : 1.f);
    float percent = 1 - (forwardMotion >= 0 ? (forwardMotion / maxSpeed) : (forwardMotion / maxReverse));
    percent = percent > 0.25f ? 0.25f : percent;
    percent = reverse ? 0.25f : percent;
    float changeFactor = percent * forwardInput * inputFactor;
    if(reverse){changeFactor *=2;}
    forwardMotion += changeFactor;
    if(forwardMotion > maxSpeed){forwardMotion = maxSpeed;}
    if(forwardMotion < maxReverse){forwardMotion = maxReverse;}
    }
  else
    {
    forwardMotion*=groundDrag;
    }
  if(Math.abs(forwardMotion)<groundStop && forwardInput==0)
    {
    forwardMotion = 0.f;
    }
  vehicle.motionX = Trig.sinDegrees(vehicle.rotationYaw)*-forwardMotion;
  vehicle.motionY = 0;
  vehicle.motionZ = Trig.cosDegrees(vehicle.rotationYaw)*-forwardMotion;  
  }

protected void applyTurnInput(float inputFactor)
  {
  float weightAdjust = 1.f;
  if(vehicle.currentWeight > vehicle.baseWeight)
    {
    weightAdjust = vehicle.baseWeight  / vehicle.currentWeight;
    }  
  if(turnInput!=0)
    {
    boolean reverse = (turnInput ==-1 && turnMotion>0) || (turnInput ==1 && turnMotion<0);
    float maxSpeed = vehicle.currentStrafeSpeedMax * weightAdjust;
    float percent = 1 - (Math.abs(turnMotion));
    percent = reverse ? 1.f : percent;
    float changeFactor = percent * (turnInput*2) * inputFactor;
    if(reverse){changeFactor *=2;}
    turnMotion += changeFactor;
    if(turnMotion > maxSpeed){turnMotion = maxSpeed;}
    if(turnMotion < -maxSpeed){turnMotion = -maxSpeed;}
    }
  else
    {
    turnMotion *= rotationDrag;
    }
  if(Math.abs(turnMotion)<rotationStop && turnInput == 0)
    {
    turnMotion = 0.f;
    }  
  }

/**
 * handle boat style movement
 * @return
 */
protected boolean handleWaterMovement()
  {
  byte submersionDepthMax = 5;
  double submersionAmount = 0.0D;
  boolean inWater = false;
  for (int i = 0; i < submersionDepthMax; ++i)
    {
    double d1 = vehicle.boundingBox.minY + (vehicle.boundingBox.maxY - vehicle.boundingBox.minY) * (double)(i + 0) / (double)submersionDepthMax - 0.125D;
    double d2 = vehicle.boundingBox.minY + (vehicle.boundingBox.maxY - vehicle.boundingBox.minY) * (double)(i + 1) / (double)submersionDepthMax - 0.125D;
    AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(vehicle.boundingBox.minX, d1, vehicle.boundingBox.minZ, vehicle.boundingBox.maxX, d2, vehicle.boundingBox.maxZ);

    if (vehicle.worldObj.isAABBInMaterial(axisalignedbb, Material.water))
      {
      submersionAmount += 1.0D / (double)submersionDepthMax;
      inWater = true;
      }
    
    }

  double vehicleMotion = Math.sqrt(vehicle.motionX * vehicle.motionX + vehicle.motionZ * vehicle.motionZ);
  double vehicleForwardMotion;
  double strafeMotion;

  if (vehicleMotion > 0.26249999999999996D)
    {
    vehicleForwardMotion = Math.cos((double)vehicle.rotationYaw * Math.PI / 180.0D);
    strafeMotion = Math.sin((double)vehicle.rotationYaw * Math.PI / 180.0D);

    for (int j = 0; (double)j < 1.0D + vehicleMotion * 60.0D; ++j)
      {
      double particleFloatOffset = (double)(vehicle.getRNG().nextFloat() * 2.0F - 1.0F);
      double particleIntegerOffset = (double)(vehicle.getRNG().nextInt(2) * 2 - 1) * 0.7D;
      double particleXOffset;
      double particleZOffset;

      if (vehicle.getRNG().nextBoolean())
        {
        particleXOffset = vehicle.posX - vehicleForwardMotion * particleFloatOffset * 0.8D + strafeMotion * particleIntegerOffset;
        particleZOffset = vehicle.posZ - strafeMotion * particleFloatOffset * 0.8D - vehicleForwardMotion * particleIntegerOffset;
        vehicle.worldObj.spawnParticle("splash", particleXOffset, vehicle.posY - 0.125D, particleZOffset, vehicle.motionX, vehicle.motionY, vehicle.motionZ);
        }
      else
        {
        particleXOffset = vehicle.posX + vehicleForwardMotion + strafeMotion * particleFloatOffset * 0.7D;
        particleZOffset = vehicle.posZ + strafeMotion - vehicleForwardMotion * particleFloatOffset * 0.7D;
        vehicle.worldObj.spawnParticle("splash", particleXOffset, vehicle.posY - 0.125D, particleZOffset, vehicle.motionX, vehicle.motionY, vehicle.motionZ);
        }
      }
    }
  if (submersionAmount < 1.0D)
    {
    vehicleForwardMotion = submersionAmount * 2.0D - 1.0D;
    vehicle.motionY += 0.03999999910593033D * vehicleForwardMotion;
    }
  else
    {
    if (vehicle.motionY < 0.0D)
      {
      vehicle.motionY /= 2.0D;
      }
    vehicle.motionY += 0.007000000216066837D;
    }
  return inWater;
  }

protected void tearUpGrass()
  {
  if(vehicle.worldObj.isRemote || !vehicle.onGround)
    {
    return;
    }
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
  if(Math.abs(yawDiff)<10 && Trig.getVelocity(x-vehicle.posX, y-vehicle.posY, z-vehicle.posZ)>=0.25f)//further away than 1 block, move towards it
    {
    fMot = 1;
    }
  this.forwardInput = fMot;
  this.turnInput = sMot;
  }

public void stopMotion()
  {
  this.clearInputFromDismount();
  vehicle.motionX = 0;
  vehicle.motionY = 0;
  vehicle.motionZ = 0;
  }

public void clearInputFromDismount()  
  {
  this.forwardInput = 0;
  this.turnInput = 0;
  this.powerInput = 0;
  this.rotationInput = 0;
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setByte("fi", forwardInput);
  tag.setByte("si", turnInput);
  tag.setByte("pi", powerInput);
  tag.setByte("ri", rotationInput);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  { 
  this.forwardInput = tag.getByte("fi");
  this.turnInput = tag.getByte("si");
  this.powerInput = tag.getByte("pi");
  this.rotationInput = tag.getByte("ri");
  }

}
