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
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IMissileHitCallback;
import shadowmage.ancient_warfare.common.inventory.VehicleInventory;
import shadowmage.ancient_warfare.common.utils.EntityPathfinder;
import shadowmage.ancient_warfare.common.utils.Pos3f;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleAmmoHelper;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleFiringHelper;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleMovementHelper;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleUpgradeHelper;
import shadowmage.ancient_warfare.common.vehicles.stats.ArmorStats;
import shadowmage.ancient_warfare.common.vehicles.stats.GeneralStats;

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
public VehicleAmmoHelper ammoHelper;
public VehicleUpgradeHelper upgradeHelper;
public VehicleMovementHelper moveHelper;
public VehicleFiringHelper firingHelper;
public VehicleInventory inventory;

public int vehicleType = -1;

public VehicleBase(World par1World)
  {
  super(par1World);
  this.navigator = new EntityPathfinder(this, worldObj, 16);  
  this.upgradeHelper = new VehicleUpgradeHelper(this);
  this.moveHelper = new VehicleMovementHelper(this);
  this.ammoHelper = new VehicleAmmoHelper(this);
  this.firingHelper = new VehicleFiringHelper(this);
  float width = this.getWidth();
  float height = this.getHeight();
  this.setSize(width, height);
  this.yOffset = height/2.f;  
  }

public abstract float getHeight();
public abstract float getWidth();
public abstract float getHorizontalMissileOffset();//x axis
public abstract float getVerticalMissileOffset();
public abstract float getForwardsMissileOffset();//z axis

public abstract float getHorizontalMissileOffsetForAim();
public abstract float getVerticalMissileOffsetForAim();
public abstract float getForwardsMissileOffsetForAim();

public boolean isAimable()
  {
  return true;
  }

public boolean canAimRotate()
  {
  return false;
  }

public boolean canAimPitch()
  {
  return true;
  }

public boolean isDrivable()
  {
  return true;
  }

public boolean isMountable()
  {
  return true;
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

public Pos3f getMissileOffset()
  {
  Pos3f off = new Pos3f();
  
  float x = this.getHorizontalMissileOffset();
  float y = this.getVerticalMissileOffset();
  float z = this.getForwardsMissileOffset();
  float angle = Trig.toDegrees((float) Math.atan2(z, x));
  float len = MathHelper.sqrt_float(x*x+z*z);
  angle+= this.rotationYaw;
  
  x = Trig.cosDegrees(angle)*len;
  z = -Trig.sinDegrees(angle)*len;
  
  off.x = x;
  off.y = y;
  off.z = z;  
  return off;
  }

public Pos3f getMissileOffsetForAim()
  {
  Pos3f off = new Pos3f();
  
  float x = this.getHorizontalMissileOffsetForAim();
  float y = this.getVerticalMissileOffsetForAim();
  float z = this.getForwardsMissileOffsetForAim();
  float angle = Trig.toDegrees((float) Math.atan2(z, x));
  float len = MathHelper.sqrt_float(x*x+z*z);
  angle+= this.rotationYaw;
  
  x = Trig.cosDegrees(angle)*len;
  z = -Trig.sinDegrees(angle)*len;
  
  off.x = x;
  off.y = y;
  off.z = z;  
  
//  Config.logDebug("offset for aim: "+off.toString());
  return off;
  }

public void debugLaunch()
  {
//  if(this.riddenByEntity!=null)
//    {
//    EntityPlayer player = (EntityPlayer)this.riddenByEntity;
//
//    Vec3 hit = getPlayerLookHit(player, 170);
//    double x = hit.xCoord - this.posX;
//    double y = hit.yCoord - this.posY;
//    double z = hit.zCoord - this.posZ;
//    
//    float len = MathHelper.sqrt_float((float) (x*x+y*y+z*z));
//    
//    Pair<Float, Float> angles = Trig.getLaunchAngleToHit((float)x, (float)y, (float)z, 20, 9.81f);
//    Config.logDebug("player look hitPos: "+hit.xCoord +","+ hit.yCoord +","+ hit.zCoord);
//    Config.logDebug("toHit playerLook: " + angles.toString() + " for distance: "+len);
//    
//    
//    //Config.logDebug("trajectory: "+Trig.getLaunchAngleToHit(100, 0, 20, 9.81f));
//    MissileBase missile = this.ammoHelper.getMissile2((float)posX, (float)posY+5, (float)posZ, this.rotationYaw, angles.key(), 20.f);
//    if(missile!=null)
//      {
//      this.worldObj.spawnEntityInWorld(missile);
//      }
//    }
  
  }

/**
 * called on every tick that the vehicle is 'firing' to update the firing animation and to call
 * launchMissile when animation has reached launch point
 */
public abstract void onFiringUpdate();

/**
 * called every tick after the vehicle has fired, until reload timer is complete, to update animations
 */
public abstract void onReloadUpdate();

/**
 * reset all upgradeable stats back to the base for this vehicle
 */
public void resetUpgradeStats()
  {
  this.firingHelper.resetUpgradeStats();
  this.moveHelper.resetUpgradeStats();
  }

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
  this.firingHelper.onTick(); 
  }

/**
 * client-side updates, poll for input if ridden, send input to server
 */
public void onUpdateClient()
  {  
  
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
  if(tag.hasKey("upgrade"))
    {
    this.upgradeHelper.handleUpgradePacketData(tag.getCompoundTag("upgrade"));
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
  this.firingHelper.handleInputData(tag);
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
  data.writeFloat(this.vehicleHealth);
  }

@Override
public void readSpawnData(ByteArrayDataInput data)
  {
  this.vehicleHealth = data.readFloat();
  }

@Override
protected void entityInit()
  {

  }

@Override
protected void readEntityFromNBT(NBTTagCompound tag)
  {
  this.vehicleHealth = tag.getFloat("health");
  this.inventory.readFromNBT(tag);
  this.upgradeHelper.readFromNBT(tag.getCompoundTag("upgrades"));
  this.ammoHelper.readFromNBT(tag.getCompoundTag("ammo"));
  this.moveHelper.readFromNBT(tag.getCompoundTag("move"));
  this.firingHelper.readFromNBT(tag.getCompoundTag("fire"));
  }

@Override
protected void writeEntityToNBT(NBTTagCompound tag)
  {
  tag.setFloat("health", this.vehicleHealth);
  this.inventory.writeToNBT(tag);
  tag.setCompoundTag("upgrades", this.upgradeHelper.getNBTTag());
  tag.setCompoundTag("ammo", this.ammoHelper.getNBTTag());
  tag.setCompoundTag("move", this.moveHelper.getNBTTag());
  tag.setCompoundTag("fire", this.firingHelper.getNBTTag());
  }

@Override
public void onMissileImpact(World world, double x, double y, double z)
  {
  if(this.ridingEntity instanceof IMissileHitCallback)
    {
    ((IMissileHitCallback)this.ridingEntity).onMissileImpact(world, x, y, z);
    }
  }

@Override
public void onMissileImpactEntity(World world, Entity entity)
  {
  if(this.ridingEntity instanceof IMissileHitCallback)
    {
    ((IMissileHitCallback)this.ridingEntity).onMissileImpactEntity(world, entity);
    }
  }

}
