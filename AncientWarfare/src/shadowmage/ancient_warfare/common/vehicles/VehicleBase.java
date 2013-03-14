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
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.interfaces.IEntityContainerSynch;
import shadowmage.ancient_warfare.common.interfaces.IMissileHitCallback;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.inventory.VehicleInventory;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.registry.VehicleRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.utils.ByteTools;
import shadowmage.ancient_warfare.common.utils.EntityPathfinder;
import shadowmage.ancient_warfare.common.utils.Pos3f;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleVarHelpers.DummyVehicleHelper;
import shadowmage.ancient_warfare.common.vehicles.armors.IVehicleArmorType;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleAmmoHelper;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleFiringHelper;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleFiringVarsHelper;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleMovementHelper;
import shadowmage.ancient_warfare.common.vehicles.helpers.VehicleUpgradeHelper;
import shadowmage.ancient_warfare.common.vehicles.materials.IVehicleMaterial;
import shadowmage.ancient_warfare.common.vehicles.types.VehicleType;
import shadowmage.ancient_warfare.common.vehicles.upgrades.IVehicleUpgradeType;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class VehicleBase extends Entity implements IEntityAdditionalSpawnData, IMissileHitCallback, IEntityContainerSynch
{
/**
 * these are the current max stats.  set from setVehicleType().  
 * these are local cached bases, after application of material factors
 * should not be altered at all after vehicle is first initialized
 */
public float baseForwardSpeed;
public float baseStrafeSpeed;
public float basePitchMin;
public float basePitchMax;
public float baseTurretRotationMax;
public float baseLaunchSpeedMax;
public float baseHealth = 100;
public float baseAccuracy = 1.f;
public float baseWeight = 1000;//kg
public int baseReloadTicks = 100;
public float baseGenericResist = 0.f;
public float baseFireResist = 0.f;
public float baseExplosionResist = 0.f;

/**
 * local current stats, fully updated and modified from upgrades/etc. should not be altered aside from
 * upgrades/armor
 */
public float currentForwardSpeedMax = 0.8f;
public float currentStrafeSpeedMax = 2.0f;
public int currentReloadTicks = 100;
public float currentTurretPitchMin = 0.f;
public float currentTurretPitchMax = 90.f;
public float currentLaunchSpeedPowerMax = 32.321f;
public float currentGenericResist = 0.f;
public float currentFireResist = 0.f;
public float currentExplosionResist = 0.f;
public float currentWeight = 1000.f;
public float currentTurretPitchSpeed = 0.f;
public float currentTurretYawSpeed = 0.f;
public float currentAccuracy = 1.f;

/**
 * local variables, may be altered by input/etc...
 */
public float localVehicleHealth = 100;
public float localTurretRotationHome = 0.f;
public float localTurretRotationMax = 45.f;
public float localTurretRotation = 0.f;
public float localTurretDestRot = 0.f;
public float localTurretRotInc = 1.f;
public float localTurretPitch = 45.f;
public float localTurretDestPitch = 45.f;
public float localTurretPitchInc = 1.f;
public float localLaunchPower = 31.321f;

/**
 * set by move helper on movement update. used during client rendering to update wheel rotation and other movement speed based animations
 */
public float wheelRotation = 0.f;
public float wheelRotationPrev = 0.f;

/**
 * the team number this vehicle was assigned to
 */
public int teamNum = 0;
/**
 * used to clear input if the vehicle WAS ridden but isn't any more
 */
private boolean isRidden = false;

/**
 * set client-side when incoming damage is taken
 */
public int hitAnimationTicks = 0;
public int moveUpdateTicks = 0;

/**
 * complex stat tracking helpers, move, ammo, upgrades, general stats
 */
public VehicleAmmoHelper ammoHelper;
public VehicleUpgradeHelper upgradeHelper;
public VehicleMovementHelper moveHelper;
public VehicleFiringHelper firingHelper;
public VehicleFiringVarsHelper firingVarsHelper;
public VehicleInventory inventory;
public EntityPathfinder navigator;

public IVehicleType vehicleType = VehicleRegistry.CATAPULT_STAND_FIXED;//set to dummy vehicle so it is never null...
public int vehicleMaterialLevel = 0;//the current material level of this vehicle. should be read/set prior to calling updateBaseStats

public VehicleBase(World par1World)
  {
  super(par1World);
  this.navigator = new EntityPathfinder(this, worldObj, 16);  
  this.upgradeHelper = new VehicleUpgradeHelper(this);
  this.moveHelper = new VehicleMovementHelper(this);
  this.ammoHelper = new VehicleAmmoHelper(this);
  this.firingHelper = new VehicleFiringHelper(this);
  this.firingVarsHelper = new DummyVehicleHelper(this);
  this.inventory = new VehicleInventory(this);
  this.stepHeight = 1.12f;
  }

public void setVehicleType(IVehicleType vehicle, int materialLevel)
  {
  this.vehicleType = vehicle;
  this.vehicleMaterialLevel = materialLevel;
  VehicleFiringVarsHelper help =vehicle.getFiringVarsHelper(this);
  if(help!=null)
    {
    this.firingVarsHelper = help;
    }
  float width = vehicleType.getWidth();
  float height = vehicleType.getHeight();
  this.setSize(width, height);
  for(IAmmoType ammo : vehicleType.getValidAmmoTypes())
    {
    this.ammoHelper.addUseableAmmo(ammo);
    }
  for(IVehicleUpgradeType up : this.vehicleType.getValidUpgrades())
    {
    this.upgradeHelper.addValidUpgrade(up);
    }
  for(IVehicleArmorType armor : this.vehicleType.getValidArmors())
    {
    this.upgradeHelper.addValidArmor(armor);
    }
  this.inventory.ammoInventory = new AWInventoryBasic(vehicle.getAmmoBaySize(), this.inventory);
  this.inventory.armorInventory = new AWInventoryBasic(vehicle.getArmorBaySize(), this.inventory);
  this.inventory.storageInventory = new AWInventoryBasic(vehicle.getStorageBaySize(), this.inventory);
  this.inventory.upgradeInventory = new AWInventoryBasic(vehicle.getUpgradeBaySize(), this.inventory);
  this.updateBaseStats();
  this.resetCurrentStats();

  if(this.localTurretPitch<this.currentTurretPitchMin)
    {
    this.localTurretPitch = this.currentTurretPitchMin;    
    }
  else if(this.localTurretPitch > this.currentTurretPitchMax)
    {
    this.localTurretPitch = this.currentTurretPitchMax;
    }
  this.localLaunchPower = this.firingHelper.getAdjustedMaxMissileVelocity();
  if(!this.canAimRotate())
    {
    this.localTurretRotation = this.rotationYaw;
    }
  }

public void updateBaseStats()
  {
  Config.logDebug("updating base stats. server"+!worldObj.isRemote);
  IVehicleMaterial material = vehicleType.getMaterialType();
  int level = this.vehicleMaterialLevel;
  baseForwardSpeed = vehicleType.getBaseForwardSpeed() * material.getSpeedForwardFactor(level);
  baseStrafeSpeed = vehicleType.getBaseStrafeSpeed() * material.getSpeedStrafeFactor(level);
  basePitchMin = vehicleType.getBasePitchMin();
  basePitchMax = vehicleType.getBasePitchMax();
  baseTurretRotationMax = vehicleType.getBaseTurretRotationAmount();
  baseLaunchSpeedMax = vehicleType.getBaseMissileVelocityMax();
  baseHealth = vehicleType.getBaseHealth() * material.getHPFactor(level);
  baseAccuracy = vehicleType.getBaseAccuracy() * material.getAccuracyFactor(level);
  baseWeight = vehicleType.getBaseWeight() * material.getWeightFactor(level); 
  baseExplosionResist = 0.f;
  baseFireResist = 0.f;
  baseGenericResist = 0.f;
  }

public float getHorizontalMissileOffset()
  {
  return this.vehicleType.getMissileHorizontalOffset();
  }

public float getVerticalMissileOffset()
  {
  return this.vehicleType.getMissileVerticalOffset();
  }

public float getForwardsMissileOffset()
  {
  return this.vehicleType.getMissileForwardsOffset();
  }

public boolean isAimable()
  {
  return vehicleType.isCombatEngine();
  }

public boolean canAimRotate()
  {
  return vehicleType.canAdjustYaw();
  }

public boolean canAimPitch()
  {
  return vehicleType.canAdjustPitch();
  }

public boolean canAimPower()
  {
  return vehicleType.canAdjustPower();
  }

public boolean isDrivable()
  {
  return vehicleType.isDrivable();
  }

public boolean isMountable()
  {
  return vehicleType.isMountable();
  }

public float getRiderForwardOffset()
  {
  return vehicleType.getRiderForwardsOffset();  
  }

public float getRiderVerticalOffset()
  {
  return vehicleType.getRiderVerticalOffest();
  }

public float getRiderHorizontalOffset()
  {
  return vehicleType.getRiderHorizontalOffset();
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
  if(canAimRotate())
    {
    angle+=this.localTurretRotation;
    }
  x = Trig.cosDegrees(angle)*len;
  z = -Trig.sinDegrees(angle)*len;
  off.x = x;
  off.y = y;
  off.z = z;
  return off;
  }

/**
 * called on every tick that the vehicle is 'firing' to update the firing animation and to call
 * launchMissile when animation has reached launch point
 */
public void onFiringUpdate()
  {
  this.firingVarsHelper.onFiringUpdate();
  }

/**
 * called every tick after the vehicle has fired, until reload timer is complete, to update animations
 */
public void onReloadUpdate()
  {
  this.firingVarsHelper.onReloadUpdate();
  }

/**
 * called every tick after startLaunching() is called, until setFinishedLaunching() is called...
 */
public void onLaunchingUpdate()
  {
  this.firingVarsHelper.onLaunchingUpdate();
  }

/**
 * reset all upgradeable stats back to the base for this vehicle
 */
public void resetCurrentStats()
  {
  Config.logDebug("resetting upgrade stats. server"+!worldObj.isRemote);
  this.firingHelper.resetUpgradeStats();
  this.moveHelper.resetUpgradeStats();
  this.currentForwardSpeedMax = this.baseForwardSpeed;
  this.currentStrafeSpeedMax = this.baseStrafeSpeed;
  this.currentTurretPitchMin = this.basePitchMin;
  this.currentTurretPitchMax = this.basePitchMax;
  this.localTurretRotationMax = this.baseTurretRotationMax;
  this.currentReloadTicks = this.baseReloadTicks;
  this.currentLaunchSpeedPowerMax = this.baseLaunchSpeedMax;
  this.currentExplosionResist = this.baseExplosionResist;
  this.currentFireResist = this.baseFireResist;
  this.currentGenericResist = this.baseGenericResist;
  this.currentWeight = this.baseWeight;
  this.currentAccuracy = this.baseAccuracy;
  Config.logDebug("lscm: "+this.currentLaunchSpeedPowerMax);
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
  float prevPitch = this.localTurretPitch;
  float prevYaw = this.localTurretRotation;
  if(this.worldObj.isRemote)
    {
    this.onUpdateClient();
    }
  else
    {    
    this.onUpdateServer();
    }
  if(localTurretPitch!=localTurretDestPitch)
    {
    this.updateTurretPitch();
    }
  if(!this.canAimRotate())
    {
    this.localTurretRotation = this.rotationYaw;    
    }
  else if(localTurretRotation!=localTurretDestRot)
    {
    this.updateTurretRotation();
    } 
  this.localTurretRotationHome = this.rotationYaw;
  this.moveHelper.onMovementTick();
  this.firingHelper.onTick();  
  if(this.hitAnimationTicks>0)
    {
    this.hitAnimationTicks--;
    }  
  this.currentTurretPitchSpeed = this.localTurretPitch - prevPitch;;
  this.currentTurretYawSpeed = this.localTurretRotation - prevYaw;
  }


/**
 * client-side updates
 */
public void onUpdateClient()
  {  
  if(Config.clientVehicleMovement && this.riddenByEntity !=null && this.riddenByEntity == AWCore.proxy.getClientPlayer())
    {
    moveUpdateTicks++;
    if(moveUpdateTicks>=Config.clientMoveUpdateTicks)
      {
      moveUpdateTicks=0;
      NBTTagCompound tag = new NBTTagCompound();
      tag.setFloat("px", (float)this.posX);
      tag.setFloat("py", (float)this.posY);
      tag.setFloat("pz", (float)this.posZ);
      tag.setFloat("ry", (float)this.rotationYaw);
      tag.setFloat("fm", this.moveHelper.forwardMotion);
      tag.setFloat("sm", this.moveHelper.strafeMotion);      
      Packet02Vehicle pkt = new Packet02Vehicle();
      pkt.setParams(this);
      pkt.setClientMoveData(tag);
      pkt.sendPacketToServer();
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
  this.isRidden = this.riddenByEntity != null;
  }

public void updateTurretPitch()
  {
  if(!canAimPitch())
    {
    localTurretDestPitch = localTurretPitch;
    return;
    }
  if(localTurretPitch>localTurretDestPitch)
    {
    localTurretPitch-=localTurretPitchInc;
    }
  else if(localTurretPitch<localTurretDestPitch)
    {
    localTurretPitch+=localTurretPitchInc;
    }
  if(Trig.getAbsDiff(localTurretDestPitch, localTurretPitch)<localTurretPitchInc)
    {
    localTurretPitch = localTurretDestPitch;
    }
  }

public void updateTurretRotation()
  {
  if(!canAimRotate())
    {
    localTurretDestRot = localTurretRotation;
    return;
    }
 
  if(localTurretRotation==localTurretDestRot)
    {
    return;
    }
  while(localTurretRotation<0)
    {
    localTurretRotation+=360;
    }
  while(localTurretRotation>=360)
    {
    localTurretRotation-=360;
    }
  while(localTurretDestRot<0)
    {
    localTurretDestRot+=360;
    }
  while(localTurretDestRot>=360)
    {
    localTurretDestRot-=360;
    }
  
  byte turnDirection = 0;
  
  float curMod = localTurretRotation%360;
  float destMod = localTurretDestRot%360;
  float diff = curMod>destMod ? curMod - destMod : destMod-curMod;
  
  byte turnDir = 0;
  if(curMod>destMod)
    {
    if(diff<180)
      {
      turnDir=-1;
      }
    else
      {
      turnDir = 1;
      }
    }
  else if (curMod<destMod)
    {
    if(diff<180)
      {
      turnDir=1;
      }
    else
      {
      turnDir = -1;
      }
    } 
  localTurretRotation += (float)localTurretRotInc * (float)turnDir;  
  if(Trig.getAbsDiff(localTurretDestRot, localTurretRotation) <= localTurretRotInc)
    {
    localTurretRotation = localTurretDestRot;
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
    this.handleHealthUpdateData(tag.getFloat("health"));
    }
  if(tag.hasKey("upgrade"))
    {
    this.upgradeHelper.handleUpgradePacketData(tag.getCompoundTag("upgrade"));
    }
  if(tag.hasKey("ammo"))
    {
    this.ammoHelper.handleAmmoUpdatePacket(tag.getCompoundTag("ammo"));
    }
  if(tag.hasKey("ammoSel"))
    {
    this.ammoHelper.handleAmmoSelectPacket(tag.getCompoundTag("ammoSel"));
    }
  if(tag.hasKey("ammoUpd"))
    {
    this.ammoHelper.handleAmmoCountUpdate(tag.getCompoundTag("ammoUpd"));
    }
  if(tag.hasKey("clientMove"))
    {
    this.handleClientMoveData(tag.getCompoundTag("clientMove"));
    }
  }

public void handleClientMoveData(NBTTagCompound tag)
  { 
  float x = tag.getFloat("px");
  float y = tag.getFloat("py");
  float z = tag.getFloat("pz");
  float ry = tag.getFloat("ry");
  float fm = tag.getFloat("fm");
  float sm = tag.getFloat("sm");
  this.setPositionAndRotationNormalized(x, y, z, ry, 0.f, 0);
  this.moveHelper.forwardMotion = fm;
  this.moveHelper.strafeMotion = sm;
  }

/**
 * called client-side to
 * @param health
 */
public void handleHealthUpdateData(float health)
  {
  this.localVehicleHealth = health;
  this.hitAnimationTicks = 20;
  }

public void handleInputData(NBTTagCompound tag)
  {
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
  float adjDmg = upgradeHelper.getScaledDamage(par1DamageSource, par2);
  this.localVehicleHealth -= adjDmg;  
  if(this.localVehicleHealth<=0)
    {
    this.setDead();
    return true;
    }
  if(!this.worldObj.isRemote)
    {
    Packet02Vehicle pkt = new Packet02Vehicle();
    pkt.setParams(this);
    pkt.setHealthUpdate(this.localVehicleHealth);
    pkt.sendPacketToAllTrackingClients(this);
    }
  return false;
  }

@Override
public String getTexture()
  {
  return vehicleType.getTextureForMaterialLevel(vehicleMaterialLevel);
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
  float yaw = this.vehicleType.moveRiderWithTurret() ? localTurretRotation : rotationYaw;
  posX += Trig.sinDegrees(yaw)*-this.getRiderForwardOffset();
  posX += Trig.cosDegrees(yaw)*this.getRiderHorizontalOffset();
  posZ += Trig.cosDegrees(yaw)*-this.getRiderForwardOffset();
  posZ += Trig.sinDegrees(yaw)*this.getRiderHorizontalOffset();
  this.riddenByEntity.setPosition(posX, posY  + this.riddenByEntity.getYOffset(), posZ);
  }

@Override
public boolean interact(EntityPlayer player)
  {  
  if(this.isMountable() && !player.worldObj.isRemote && !player.isSneaking() && (this.riddenByEntity==null || this.riddenByEntity==player))
    {
    player.mountEntity(this);
    return true;
    }
  else if(!player.worldObj.isRemote && player.isSneaking())
    {
    GUIHandler.instance().openGUI(GUIHandler.VEHICLE_DEBUG, player, worldObj, this.entityId, 0, 0);
    }
  return true;
  }

@Override
public void setPositionAndRotation2(double par1, double par3, double par5, float yaw, float par8, int par9)
  {      
  this.setPositionAndRotationNormalized(par1, par3, par5, yaw, par8, par9);
  }

public void setPositionAndRotationNormalized(double par1, double par3, double par5, float yaw, float par8, int par9)
  {
  if(this.riddenByEntity!=null && this.riddenByEntity == AWCore.proxy.getClientPlayer())//if this is a client instance, and thePlayer is riding...
    {
    if(Config.clientVehicleMovement)
      {
      return;
      }
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
  return par1Entity.getBoundingBox();
  }

@Override
public boolean canBeCollidedWith()
  {
  return true;
  }

@Override
protected void entityInit()
  {
  }

@Override
public void writeSpawnData(ByteArrayDataOutput data)
  {
  data.writeFloat(this.localVehicleHealth);  
  data.writeInt(this.vehicleType.getGlobalVehicleType());
  data.writeInt(this.vehicleMaterialLevel);
  ByteTools.writeNBTTagCompound(upgradeHelper.getNBTTag(), data);
  ByteTools.writeNBTTagCompound(ammoHelper.getNBTTag(), data);
  ByteTools.writeNBTTagCompound(moveHelper.getNBTTag(), data);
  ByteTools.writeNBTTagCompound(firingHelper.getNBTTag(), data);
  ByteTools.writeNBTTagCompound(firingVarsHelper.getNBTTag(), data);
  data.writeFloat(localLaunchPower);
  data.writeFloat(localTurretPitch);
  data.writeFloat(localTurretRotation);
  data.writeFloat(localTurretDestPitch);
  data.writeFloat(localTurretDestRot);
  data.writeInt(teamNum);
  data.writeFloat(localTurretRotationHome);
  }

@Override
public void readSpawnData(ByteArrayDataInput data)
  {
  this.localVehicleHealth = data.readFloat();
  IVehicleType type = VehicleType.getVehicleType(data.readInt());
  this.setVehicleType(type, data.readInt());
  this.upgradeHelper.readFromNBT(ByteTools.readNBTTagCompound(data));
  this.ammoHelper.readFromNBT(ByteTools.readNBTTagCompound(data));
  this.moveHelper.readFromNBT(ByteTools.readNBTTagCompound(data));
  this.firingHelper.readFromNBT(ByteTools.readNBTTagCompound(data));
  this.firingVarsHelper.readFromNBT(ByteTools.readNBTTagCompound(data));
  this.localLaunchPower = data.readFloat();
  this.localTurretPitch = data.readFloat();
  this.localTurretRotation = data.readFloat();
  this.localTurretDestPitch = data.readFloat();
  this.localTurretDestRot = data.readFloat();
  this.upgradeHelper.updateUpgradeStats();
  this.teamNum = data.readInt();
  this.localTurretRotationHome = data.readFloat();
  }

@Override
protected void readEntityFromNBT(NBTTagCompound tag)
  {
  IVehicleType vehType = VehicleType.getVehicleType(tag.getInteger("vehType"));
  int level = tag.getInteger("matLvl");
  this.setVehicleType(vehType, level);
  this.localVehicleHealth = tag.getFloat("health");
  this.localTurretRotationHome = tag.getFloat("turHome");
  this.inventory.readFromNBT(tag);
  this.upgradeHelper.readFromNBT(tag.getCompoundTag("upgrades"));
  this.ammoHelper.readFromNBT(tag.getCompoundTag("ammo"));
  this.moveHelper.readFromNBT(tag.getCompoundTag("move"));
  this.firingHelper.readFromNBT(tag.getCompoundTag("fire"));
  this.firingVarsHelper.readFromNBT(tag.getCompoundTag("vars"));
  this.localLaunchPower = tag.getFloat("lc");
  this.localTurretPitch = tag.getFloat("tp");
  this.localTurretDestPitch = tag.getFloat("tpd");
  this.localTurretRotation = tag.getFloat("tr");
  this.localTurretDestRot = tag.getFloat("trd");  
  this.upgradeHelper.updateUpgrades(); 
  this.ammoHelper.updateAmmoCounts();
  this.teamNum = tag.getInteger("team");
  }

@Override
protected void writeEntityToNBT(NBTTagCompound tag)
  {
  tag.setInteger("vehType", this.vehicleType.getGlobalVehicleType());
  tag.setInteger("matLvl", this.vehicleMaterialLevel);
  tag.setFloat("health", this.localVehicleHealth);
  tag.setFloat("turHome", this.localTurretRotationHome);
  this.inventory.writeToNBT(tag);
  tag.setCompoundTag("upgrades", this.upgradeHelper.getNBTTag());
  tag.setCompoundTag("ammo", this.ammoHelper.getNBTTag());
  tag.setCompoundTag("move", this.moveHelper.getNBTTag());
  tag.setCompoundTag("fire", this.firingHelper.getNBTTag());  
  tag.setCompoundTag("vars", this.firingVarsHelper.getNBTTag());
  tag.setFloat("lc", localLaunchPower);
  tag.setFloat("tp", localTurretPitch);
  tag.setFloat("tpd", localTurretDestPitch);
  tag.setFloat("tr", localTurretRotation);
  tag.setFloat("trd", localTurretDestRot);
  tag.setInteger("team", this.teamNum);
  }


/**
 * missile callback methods...
 */
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

/**
 * container sych methods
 */
@Override
public void handleClientInput(NBTTagCompound tag)
  {
  
  }

@Override
public void addPlayer(EntityPlayer player)
  {
  
  }

@Override
public void removePlayer(EntityPlayer player)
  {
  }

@Override
public boolean canInteract(EntityPlayer player)
  {
  return true;
  }

}
