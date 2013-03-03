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
package shadowmage.ancient_warfare.common.vehicles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IMissileHitCallback;
import shadowmage.ancient_warfare.common.inventory.VehicleInventory;
import shadowmage.ancient_warfare.common.missiles.MissileBase;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.utils.EntityPathfinder;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.helpers.AmmoHelper;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleMovementHelper;
import shadowmage.ancient_warfare.common.vehicles.stats.ArmorStats;
import shadowmage.ancient_warfare.common.vehicles.stats.GeneralStats;
import shadowmage.ancient_warfare.common.vehicles.stats.UpgradeStats;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class VehicleBase extends Entity implements IEntityAdditionalSpawnData, IMissileHitCallback
{

public static final int CATAPULT = 0;
public static final int BALLISTA = 1;
public static final int TREBUCHET = 2;
public static final int RAM = 3;
public static final int HWACHA = 4;
public static final int CHESTCART = 5;
public static final int BALLISTA_TURRET = 6;

public float vehicleMaxHealthBase = 100;
public float vehicleMaxHealth = 100;
public float vehicleHealth = 100;



public float turretRotation = 0.f;
public float turretRotationMin = 0.f;
public float turretRotationMax = 360.f;

public float turretPitch = 0.f;
public float turretPitchMin = 0.f;
public float turretPitchMax = 90.f;

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

/**
 * power is deprecated as a controllable stat, but will still be affected by upgrades and base vehicle stats
 */
public int aimPower = 0;
public int aimPowerMin = 0;
public int aimPowerMax = 100;

/**
 * set by move helper
 */
public float wheelRotation = 0.f;
public float wheelRotationPrev = 0.f;

private boolean isRidden = false;

public String texture = "";

/**
 * vehicle pathfinding, used by soldiers when they are riding the vehicle
 * also will be used for player set waypoints
 */
public EntityPathfinder navigator;

/**
 * complex stat tracking helpers, move, ammo, upgrades, general stats
 */
public AmmoHelper ammoHelper;
private ArmorStats armorStats = new ArmorStats();
private GeneralStats generalStats = new GeneralStats();
private UpgradeStats upgradeStats;
private VehicleInventory inventory;
private VehicleMovementHelper moveHelper;

public int vehicleType = -1;

public VehicleBase(World par1World)
  {
  super(par1World);
  this.navigator = new EntityPathfinder(this, worldObj, 16);  
  this.upgradeStats = new UpgradeStats(this);
  this.moveHelper = new VehicleMovementHelper(this);
  this.ammoHelper = new AmmoHelper(this);
  float width = this.getWidth();
  float height = this.getHeight();
  this.setSize(width, height);
  this.yOffset = height/2.f;  
  }

public abstract float getHeight();
public abstract float getWidth();

public boolean hasTurret()
  {
  return false;
  }

public boolean isDrivable()
  {
  return false;
  }

public boolean isMountable()
  {
  return false;
  }

public float getRiderForwardOffset()
  {
  return 1.0f;  
  }

public float getRiderVerticalOffset()
  {
  return 0.8f;
  }

public float getRiderHorizontalOffset()
  {
  return 0.f;
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
    if(!this.worldObj.isRemote)
      {
      MissileBase missile = this.ammoHelper.getMissile((float)posX, (float)posY+5, (float)posZ, 0*0.05f, 10*0.05f, 0);
      if(missile!=null)
        {
        this.worldObj.spawnEntityInWorld(missile);
        }
      }
    return true;
    }  
  return false;
  }

public abstract void onFiringUpdate();

public abstract void onReloadUpdate();

/**
 * need to setup on-death item drops
 */
@Override
public void setDead()
  {
  super.setDead();
  }

@Override
public void onUpdate()
  { 
  super.onUpdate();  
  if(this.worldObj.isRemote)
    {
    this.onUpdateClient();
    }
  else
    {    
    this.onUpdateServer();
    }
  this.moveHelper.onMovementTick();
  if(this.isFiring)
    {
    this.onFiringUpdate();
    }
  if(this.reloadingTicks>0)
    {
    this.reloadingTicks--;
    this.onReloadUpdate();
    if(this.reloadingTicks<=0)
      {
      this.hasLaunched = false;
      this.isFiring = false;
      this.reloadingTicks = 0;      
      }
    }
  }

/**
 * client-side updates, poll for input if ridden, send input to server
 */
public void onUpdateClient()
  {  
  if(this.riddenByEntity!=null && this.riddenByEntity == AWCore.proxy.getClientPlayer())
    {
    if(AWCore.proxy.inputHelper.hasInputChanged())
      {      
      this.handleKeyboardMovement((byte)AWCore.proxy.inputHelper.getForwardInput(), (byte)AWCore.proxy.inputHelper.getStrafeInput());
      }
    }
  }

/**
 * server-side updates...
 */
public void onUpdateServer()
  {
  if(this.isRidden && this.riddenByEntity==null)
    {
    this.isRidden = false;
    this.moveHelper.clearInputFromDismount();
    }
  }

/**
 * Called from Packet02Vehicle
 * Generic update method for client-server coms
 * @param tag
 */
public void handlePacketUpdate(NBTTagCompound tag)
  {
  if(tag.hasKey("input"))
    {
    this.handleInputData(tag.getCompoundTag("input"));
    }
  if(tag.hasKey("health"))
    {    
    this.handleHealthUpdateData(tag);
    }
  if(tag.hasKey("upgrades"))
    {

    }
  if(tag.hasKey("ammo"))
    {

    }
  }

public void handleHealthUpdateData(NBTTagCompound tag)
  {
  this.vehicleHealth = tag.getFloat("health");
  }

public void handleInputData(NBTTagCompound tag)
  {
  Config.logDebug("receiving input packet data. server: "+!worldObj.isRemote);
  this.moveHelper.handleInputData(tag);
  if(tag.hasKey("fm"))
    {
    if(!this.worldObj.isRemote && !this.isFiring)
      {
      Packet02Vehicle pkt = new Packet02Vehicle();
      pkt.setParams(this);
      NBTTagCompound reply = new NBTTagCompound();
      reply.setBoolean("fm", true);
      pkt.setInputData(reply);
      pkt.sendPacketToAllTrackingClients(this);
      
      Config.logDebug("Initiating launch missile sequence");
      this.startMissileLaunch();
      }
    }
 
  }

/**
 * handle movement input, sent from inputHelper when vehicle is ridden
 * @param forward
 * @param strafe
 */
public void handleKeyboardMovement(byte forward, byte strafe)
  {
  this.moveHelper.handleKeyboardInput(forward, strafe);  
  }

@Override
public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
  {
  super.attackEntityFrom(par1DamageSource, par2);
  this.vehicleHealth-=par2;
  if(this.vehicleHealth<=0)
    {
    this.setDead();
    return true;
    }
  return false;
  }

@Override
public String getTexture()
  {
  return texture;
  }

@Override
public void updateRiderPosition()
  {
  if (!(this.riddenByEntity instanceof EntityPlayer) || !((EntityPlayer)this.riddenByEntity).func_71066_bF())
    {
    this.riddenByEntity.lastTickPosX = this.lastTickPosX;
    this.riddenByEntity.lastTickPosY = this.lastTickPosY + this.getRiderVerticalOffset() + this.riddenByEntity.getYOffset();
    this.riddenByEntity.lastTickPosZ = this.lastTickPosZ;
    }
  double posX = this.posX;
  double posY = this.posY + this.getRiderVerticalOffset();
  double posZ = this.posZ;
  posX += Trig.sinDegrees(rotationYaw)*-this.getRiderForwardOffset();
  posX += Trig.cosDegrees(rotationYaw)*this.getRiderHorizontalOffset();
  posZ += Trig.cosDegrees(rotationYaw)*-this.getRiderForwardOffset();
  posZ += Trig.sinDegrees(rotationYaw)*this.getRiderHorizontalOffset();
  this.riddenByEntity.setPosition(posX, posY  + this.riddenByEntity.getYOffset(), posZ);
  }

@Override
public boolean interact(EntityPlayer player)
  {  
  if(this.isMountable() && !player.worldObj.isRemote && !player.isSneaking())
    {
    player.mountEntity(this);
    return true;
    }
  return true;
  }

@Override
public void setPositionAndRotation2(double par1, double par3, double par5, float yaw, float par8, int par9)
  {      
  if(this.riddenByEntity!=null && this.riddenByEntity == AWCore.proxy.getClientPlayer())
    {
    double var10 = par1 - this.posX;
    double var12 = par3 - this.posY;
    double var14 = par5 - this.posZ;
    double var16 = var10 * var10 + var12 * var12 + var14 * var14;    
    if (var16 <= 1.0D)
      {
      float rot = this.rotationYaw;
      float rot2 = yaw;      
      if(Trig.getAbsDiff(rot, rot2)>2)
        { 
        //float diff = this.rotationYaw - this.prevRotationYaw;//pull diff of current rot and prev rot.  change rot. change prev rot to rot. apply diff to prev rot DONE
        this.setRotation(yaw, par8);
        this.prevRotationYaw = this.rotationYaw;//TODO hack to fix rendering...need to rebound prevRotataion..
//        this.prevRotationYaw = this.rotationYaw + diff;
        
        }      
      return;
      }
    Config.logDebug("crazy synch error!!");
    } 
  super.setPositionAndRotation(par1, par3, par5, yaw, par8);
  }

@Override
public AxisAlignedBB getBoundingBox()
  {
  return this.boundingBox;
  }

@Override
public AxisAlignedBB getCollisionBox(Entity par1Entity)
  {
  return par1Entity.boundingBox;
  }

@Override
public boolean canBeCollidedWith()
  {
  return true;
  }

@Override
public void writeSpawnData(ByteArrayDataOutput data)
  {

  }

@Override
public void readSpawnData(ByteArrayDataInput data)
  {

  }

@Override
protected void entityInit()
  {

  }

@Override
protected void readEntityFromNBT(NBTTagCompound var1)
  {

  }

@Override
protected void writeEntityToNBT(NBTTagCompound var1)
  {

  }

@Override
public void onMissileImpact(World world, double x, double y, double z)
  {
  }

@Override
public void onMissileImpactEntity(World world, Entity entity)
  {
  }

}
