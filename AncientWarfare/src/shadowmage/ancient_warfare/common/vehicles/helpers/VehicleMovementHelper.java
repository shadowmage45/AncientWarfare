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

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.VehicleMovementType;

public class VehicleMovementHelper implements INBTTaggable
{
/**
 * vehicle input flow (onInput)
 * 
 * if client
 *  if client move
 *    set local info to input
 *    send FULL input packet to server
 *  else
 *    send partial input to server 
 * else
 *    set local info to input
 */

/**
 * vehicle movement update flow
 * 
 * if client
 *  if client move
 *    every X ticks, send FULL input packet to server
 * else
 *  every X ticks send FULL motion packet to clients
 *    if client move
 *      do not send to riding player
 * update local motion params (client AND server)
 */

private VehicleBase vehicle;

private float forwardAccel = 0;
private float strafeAccel = 0;
public float forwardMotion = 0;
public float strafeMotion = 0;
protected float prevWidth = 0.8f;
public float localThrottle = 0.f;
public float airPitch = 0.f;
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
    if(Config.clientVehicleMovement && vehicle.riddenByEntity!=null && vehicle.riddenByEntity==AWCore.proxy.getClientPlayer())
      {
      vehicle.setForwardInput(forward);
      vehicle.setStrafeInput(strafe);      
      }
    sendInputToServer(forward, strafe, false);
    }
  else
    {
    vehicle.setForwardInput(forward);
    vehicle.setStrafeInput(strafe);
    }
  }

/**
 * client side - handle throttle input from inputHelper
 * @param input
 */
public void handleThrottleInput(byte input)
  {
  if(vehicle.worldObj.isRemote)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setByte("tr", input);
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(vehicle);
    pkt.setInputData(tag);
    pkt.sendPacketToServer();
    if(Config.clientVehicleMovement && vehicle.riddenByEntity!=null && vehicle.riddenByEntity==AWCore.proxy.getClientPlayer())//this is a client, with thePlayer riding
      {
      localThrottle += ((float)input) * 0.025f;
      if(localThrottle>1.f)
        {
        localThrottle = 1.f;
        }
      if(localThrottle<0.f)
        {
        localThrottle = 0.f;
        }
      } 
    } 
  }

/**
 * client/server handle throttle update packet
 * @param in
 */
protected void handleThrottlePacket(NBTTagCompound in)
  {
  byte input = in.getByte("tr");  
  if(vehicle.worldObj.isRemote)
    {        
    localThrottle = in.getFloat("trA");
    Config.logDebug("setting throttle client value to: " + localThrottle);
    }
  else
    {
    localThrottle += ((float)input) * 0.025f;
    if(localThrottle>1.f)
      {
      localThrottle = 1.f;
      }
    if(localThrottle<0.f)
      {
      localThrottle = 0.f;
      }
    Config.logDebug("setting throttle server value to: " + localThrottle);
    NBTTagCompound tag = new NBTTagCompound();
    tag.setFloat("trA", localThrottle);
    tag.setByte("tr", input);
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(vehicle);
    pkt.setInputData(tag);
    pkt.sendPacketToAllTrackingClients(vehicle);
    }
  }

/**
 * send input to server. at the very least, send forward/strafe input
 * if fullPacket -- send pos/motion/accel as well
 * @param forward
 * @param strafe
 * @param fullPacket
 */
public void sendInputToServer(byte forward, byte strafe, boolean fullPacket)
  {
  if(!vehicle.worldObj.isRemote)
    {
    return;
    }
  NBTTagCompound tag = new NBTTagCompound();  
  tag.setByte("f", forward);
  tag.setByte("s", strafe);
  if(fullPacket)
    {
    tag.setFloat("fMot", forwardMotion);
    tag.setFloat("sMot", strafeMotion);    
    tag.setFloat("px", (float)vehicle.posX);
    tag.setFloat("py", (float)vehicle.posY);
    tag.setFloat("pz", (float)vehicle.posZ);
    tag.setFloat("ry", (float)vehicle.rotationYaw);
    tag.setFloat("fAcc", forwardAccel);
    tag.setFloat("sAcc", strafeAccel);  
    tag.setFloat("rp", airPitch);
    }
  Packet02Vehicle pkt = new Packet02Vehicle();
  pkt.setParams(this.vehicle);
  pkt.setInputData(tag);
  pkt.sendPacketToServer();
  }

/**
 * send a motion update packet to clients tracking this entity
 * checks for clientVehicleMovement, and will ignore the riding player
 * if clientMovement is true (wont even send a packet)
 */
public void sendUpdateToClients()
  {  
  if(vehicle.worldObj.isRemote)
    {
    return;
    }
  NBTTagCompound tag = new NBTTagCompound();  
  tag.setFloat("fMot", forwardMotion);
  tag.setFloat("sMot", strafeMotion);    
  tag.setFloat("ry", (float)vehicle.rotationYaw);
  tag.setFloat("fAcc", forwardAccel);
  tag.setFloat("sAcc", strafeAccel);
  tag.setFloat("rp", airPitch);
  Packet02Vehicle pkt = new Packet02Vehicle();
  pkt.setParams(this.vehicle);
  pkt.setInputData(tag);  
  if(Config.clientVehicleMovementBase && vehicle.riddenByEntity instanceof EntityPlayer)
    {
    EntityPlayer player = (EntityPlayer) vehicle.riddenByEntity;
    List<EntityPlayer> allPlayers = vehicle.worldObj.playerEntities;
    for(EntityPlayer serverPlayer : allPlayers)
      {
      if(serverPlayer!=player)
        {
        pkt.sendPacketToPlayer(serverPlayer);
        }
      }
    }
  else
    {
    pkt.sendPacketToAllTrackingClients(vehicle);
    }
  }

public void stopMotion()
  {  
  if(!vehicle.worldObj.isRemote)
    {    
    vehicle.clearPath();
    this.setInput((byte)0, (byte)0);
    if(forwardAccel!=0 || strafeAccel!=0 || strafeMotion!=0 || forwardMotion!=0)
      {
      this.forwardAccel = 0;
      this.forwardMotion = 0;
      this.strafeAccel = 0;
      this.strafeMotion = 0;
      this.sendUpdateToClients();  
      }    
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
  if(tag.hasKey("f") || tag.hasKey("s"))
    {
    vehicle.setForwardInput(tag.getByte("f"));
    vehicle.setStrafeInput(tag.getByte("s"));
    } 
  if(tag.hasKey("fMot"))
    {
    this.forwardMotion = tag.getFloat("fMot");
    }
  if(tag.hasKey("sMot"))
    {
    this.strafeMotion = tag.getFloat("sMot");
    }
  if(tag.hasKey("fAcc"))
    {
    this.forwardAccel = tag.getFloat("fAcc");
    }
  if(tag.hasKey("sAcc"))
    {
    this.strafeAccel = tag.getFloat("sAcc");
    }
  if(tag.hasKey("px"))
    {
    vehicle.posX = (float)tag.getFloat("px");
    vehicle.lastTickPosX = vehicle.prevPosX = vehicle.posX;
    }
  if(tag.hasKey("py"))
    {
    vehicle.posY = (float)tag.getFloat("py");
    vehicle.lastTickPosY = vehicle.prevPosY = vehicle.posY;
    }
  if(tag.hasKey("pz"))
    {
    vehicle.posZ = (float)tag.getFloat("pz");
    vehicle.lastTickPosZ = vehicle.prevPosZ = vehicle.posZ;
    }
  if(tag.hasKey("ry"))
    {
    float vehRot = vehicle.rotationYaw;
    float newRot = tag.getFloat("ry");
    while(newRot + 360 <=vehRot) {newRot+=360.f;}
    while(newRot - 360 >=vehRot) {newRot-=360.f;}
    vehicle.rotationYaw = newRot;
    }
  if(tag.hasKey("rp"))
    {
    airPitch = tag.getFloat("rp");
    } 
  if(tag.hasKey("tr"))
    {
    this.handleThrottlePacket(tag); 
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
  if(Math.abs(yawDiff)<10 && Trig.getVelocity(x-vehicle.posX, y-vehicle.posY, z-vehicle.posZ)>=0.25f)//further away than 1 block, move towards it
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
  VehicleMovementType type = vehicle.vehicleType.getMovementType();
  switch(type)
  {
  case AIR1:
  case AIR2:
    {
    this.handleAirMovementUpdate();
    break;   
    } 
  
  case GROUND:  
  case WATER:    
  default:
    {
    this.handleGroundMovementUpdate();    
    }
  }  
  }

protected void handleGroundMovementUpdate()
  {
  byte forwardInput = vehicle.getForwardInput();
  byte strafeInput = vehicle.getStrafeInput();
  this.prevWidth = vehicle.width;
  vehicle.width = 0.8f;
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
  if(vehicle.vehicleType.getMovementType()==VehicleMovementType.WATER)
    {
    if(!this.handleWaterMovement())
      {
      strafeMotion *=.85f;
      strafeAccel *= 0.85f;
      forwardAccel *= 0.85f;
      forwardMotion *= .85f;
      }
    vehicle.motionY *= 0.949999988079071D;
    }
  else if(vehicle.vehicleType.getMovementType()==VehicleMovementType.GROUND)
    {
    vehicle.motionY -= (9.81f*0.05f*0.05f);
    }
   
  if(strafeMotion !=0 || forwardMotion !=0 || vehicle.motionY !=0)
    { 
    if(vehicle.riddenByEntity instanceof NpcBase)
      {
      vehicle.width = 0.8f;
      vehicle.setPosition(vehicle.posX, vehicle.posY, vehicle.posZ);
      }    
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
  vehicle.width = prevWidth;
  vehicle.setPosition(vehicle.posX, vehicle.posY, vehicle.posZ);
  }

protected void handleAirMovementUpdate()
  {
  if(vehicle.riddenByEntity==null)
    {
    this.localThrottle = 0;
    }
  byte pitchInput = vehicle.getForwardInput();
  byte strafeInput = vehicle.getStrafeInput();
  float weightAdjust = 1.f;
  if(vehicle.currentWeight > vehicle.baseWeight)
    {
    weightAdjust = vehicle.baseWeight  / vehicle.currentWeight;
    }  
  if(pitchInput!=0)
    {
    forwardAccel = pitchInput * 0.06f * (vehicle.currentForwardSpeedMax*weightAdjust - MathHelper.abs(forwardMotion));    
    forwardAccel *= weightAdjust;
    }
  else
    {
    forwardAccel = forwardMotion * -0.13f;
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
  if(pitchInput ==1 && absFor > vehicle.currentPitchSpeedMax*weightAdjust)
    {
    forwardMotion = vehicle.currentPitchSpeedMax;
    }
  else if(pitchInput == -1 && absFor > vehicle.currentPitchSpeedMax * 0.6f)
    {
    forwardMotion = -vehicle.currentPitchSpeedMax * 0.6f;
    }
  else if(absFor <= 0.02f && pitchInput == 0)
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
  
  boolean onGround = vehicle.worldObj.getBlockId(MathHelper.floor_double(vehicle.posX), MathHelper.floor_double(vehicle.posY)-1, MathHelper.floor_double(vehicle.posZ))!=0;
//  float velocity = Trig.getVelocity(vehicle.motionX, vehicle.motionY, vehicle.motionZ);
  float horizontalVelocity = Trig.getVelocity(vehicle.motionX, vehicle.motionZ);   
  float maxVelocity = vehicle.currentForwardSpeedMax;
  float velocityPercent = horizontalVelocity/maxVelocity;
  float doubleVelocityPercent = horizontalVelocity  / (maxVelocity*2);
  float drag = onGround ? 0.995f : 0.999f;   
  drag = localThrottle==0 && onGround ? drag * 0.985f : drag;
  
  
  float accel = 0.025f * localThrottle*localThrottle*localThrottle;
  accel *= (1-velocityPercent);
  
  
  float pitchAccel = -Trig.sinDegrees(airPitch) * horizontalVelocity * 0.075f * (1-doubleVelocityPercent);
  
  if(airPitch<0)
    {
    pitchAccel *= 0.5f;
    }
  
  horizontalVelocity = (horizontalVelocity + accel + pitchAccel)*(drag);
  
  
  /**
   * how to detect when 'crashed'
   *   check for previous velocity vs current velocity 
   * 
   * 
   * 
   */ 
  boolean crashSpeed = false;

  
  if(horizontalVelocity > vehicle.currentForwardSpeedMax * 0.5f)
    {
    crashSpeed = true;
    }
  else
    {
    }
  
  if(horizontalVelocity < vehicle.currentForwardSpeedMax * 0.65f)
    {
    vehicle.motionY -= (9.81f*0.05f*0.05f);
    if(!onGround)
      {
      airPitch--;      
      }
    }
  else
    {
    vehicle.motionY = Trig.sinDegrees(airPitch) * horizontalVelocity;    
    }  
  
  if(horizontalVelocity < 0.04f && localThrottle==0)//short-stop code
    {
    horizontalVelocity = 0.f;
    }  
  
  boolean vertCrashSpeed = false;
  if(vehicle.motionY < -0.25f || vehicle.motionY > 0.25f)
    {
    vertCrashSpeed = true;    
    }
  
  
  
  vehicle.moveEntity(vehicle.motionX, vehicle.motionY, vehicle.motionZ);
  
  if(vehicle.isCollidedHorizontally)
    {
    if(!onGround || crashSpeed)
      {
      Config.logDebug("CRASH");
      if(!vehicle.worldObj.isRemote && vehicle.riddenByEntity instanceof EntityPlayer)
        {
        EntityPlayer player = (EntityPlayer) vehicle.riddenByEntity;
        player.addChatMessage("you have crashed!!");
        }
      if(!vehicle.worldObj.isRemote)
        {
        vehicle.setDead();
        }
      }
    }
  
  if(vehicle.isCollidedVertically)
    {
    if(vertCrashSpeed)
      {
      Config.logDebug(" VERT CRASH");
      if(!vehicle.worldObj.isRemote && vehicle.riddenByEntity instanceof EntityPlayer)
        {
        EntityPlayer player = (EntityPlayer) vehicle.riddenByEntity;
        player.addChatMessage("you have crashed (vertical)!!");
        }
      if(!vehicle.worldObj.isRemote)
        {
        vehicle.setDead();
        }
      }
    }
  
  float x = Trig.sinDegrees(vehicle.rotationYaw)*-horizontalVelocity;
  float z = Trig.cosDegrees(vehicle.rotationYaw)*-horizontalVelocity;
  
  vehicle.motionX= x;  
  vehicle.motionZ= z;
  
  vehicle.rotationYaw += strafeMotion;  
  airPitch -= forwardMotion;
  float mp = vehicle.onGround ? 1 : 20;
  if(airPitch>mp){airPitch = mp;}
  if(airPitch<-mp){airPitch = -mp;}
  if(airPitch<.2f && airPitch>-.2f && forwardMotion==0)
    {
    airPitch = 0;
    }
  vehicle.wheelRotationPrev = vehicle.wheelRotation;
  vehicle.wheelRotation += localThrottle * 0.1f;
  this.tearUpGrass();  
  vehicle.setPosition(vehicle.posX, vehicle.posY, vehicle.posZ);
  
//  Config.logDebug(String.format("handling air movement update. client: %s throttle:%s  yaw: %.2f   pitch %.2f   horizVelocity %.2f  motion: %.2f, %.2f, %.2f", vehicle.worldObj.isRemote, localThrottle, vehicle.rotationYaw, airPitch, horizontalVelocity, vehicle.motionX, vehicle.motionY, vehicle.motionZ));
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

/**
 * tears up grass, flowers, snow, drops them as blocks in the world.
 */
public void tearUpGrass()
  {
  if(vehicle.worldObj.isRemote)
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
  tag.setFloat("tr", localThrottle);  
  tag.setFloat("ap", airPitch);
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
  this.localThrottle = tag.getFloat("tr");
  this.airPitch = tag.getFloat("ap");
  }

}
