package shadowmage.ancient_warfare.common.aw_vehicles;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.utils.EntityPathfinder;
import shadowmage.ancient_warfare.common.aw_core.utils.IMissileHitCallback;
import shadowmage.ancient_warfare.common.aw_vehicles.stats.AmmoStats;
import shadowmage.ancient_warfare.common.aw_vehicles.stats.ArmorStats;
import shadowmage.ancient_warfare.common.aw_vehicles.stats.GeneralStats;
import shadowmage.ancient_warfare.common.aw_vehicles.stats.UpgradeStats;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class VehicleBase extends Entity implements IEntityAdditionalSpawnData, IMissileHitCallback
{

private float vehicleMaxHealthBase = 100;
private float vehicleMaxHealth = 100;
private float vehicleHealth = 100;

private float turretRotation = 0.f;
private float turretRotationMin = 0.f;
private float turretRotationMax = 360.f;

private float turretPitch = 0.f;
private float turretPitchMin = 0.f;
private float turretPitchMax = 90.f;

private int aimPower = 0;
private int aimPowerMin = 0;
private int aimPowreMax = 100;

/**
 * vehicle pathfinding, used by soldiers when they are riding the vehicle
 * also will be used for player set waypoints
 */
public EntityPathfinder navigator;

/**
 * complex stat-tracking, each will have a read/write NBT function
 * for both saving to NBT and relaying via packet to the client
 * also, they should intelligently relay only _changed_ bits to clients..somehow
 */
private AmmoStats ammoStats = new AmmoStats();
private ArmorStats armorStats = new ArmorStats();
private GeneralStats generalStats = new GeneralStats();
private UpgradeStats upgradeStats = new UpgradeStats();

public VehicleBase(World par1World)
  {
  super(par1World);
  this.navigator = new EntityPathfinder(this, worldObj, 16);
  }

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

/**
 * need to setup on-death item drops, clear any caching of vehicle
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
  }

/**
 * TODO
 * client-side updates, poll for input if ridden, send input to server
 */
public void onUpdateClient()
  {
  
  }

/**
 * TODO
 * apply motion from input if ridden and not pathfinding
 */
public void onUpdateServer()
  {
  
  }

@Override
public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
  {
  return super.attackEntityFrom(par1DamageSource, par2);
  }

@Override
public boolean canBePushed()
  {
  return super.canBePushed();
  }

@Override
public String getTexture()
  {
  return super.getTexture();
  }

@Override
public void updateRidden()
  {
  super.updateRidden();
  }

@Override
public void updateRiderPosition()
  {
  super.updateRiderPosition();
  }

@Override
public void mountEntity(Entity par1Entity)
  {
  super.mountEntity(par1Entity);
  }

@Override
public void unmountEntity(Entity par1Entity)
  {
  super.unmountEntity(par1Entity);
  }

@Override
public boolean shouldRiderSit()
  {
  return super.shouldRiderSit();
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
