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
package shadowmage.ancient_warfare.common.vehicles.missiles;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public abstract class Ammo implements IAmmoType
{

public static Ammo[] ammoTypes = new Ammo[64];//starting with 64 types...can/will expand as needed

/**
 * procedure to make new ammo type:
 * create ammo class
 * create static instance below (or anywhere really)
 * register the render in renderRegistry (or register it with renderregistry during startup)
 * add ammo to applicable vehicle type constructors 
 */

public static Ammo ammoBallShot = new AmmoBallShot(52);// has to be declared first, because others depend on it...
public static Ammo ammoBallIronShot = new AmmoIronBallShot(53);

public static Ammo ammoStoneShot10 = new AmmoStoneShot(0,10);
public static Ammo ammoStoneShot15 = new AmmoStoneShot(1,15);
public static Ammo ammoStoneShot30 = new AmmoStoneShot(2,30);
public static Ammo ammoStoneShot45 = new AmmoStoneShot(3, 45);
public static Ammo ammoFireShot10 = new AmmoFlameShot(4,10);
public static Ammo ammoFireShot15 = new AmmoFlameShot(5,15);
public static Ammo ammoFireShot30 = new AmmoFlameShot(6,30);
public static Ammo ammoFireShot45 = new AmmoFlameShot(7,45);
public static Ammo ammoExplosive10 = new AmmoExplosiveShot(8,10,false);
public static Ammo ammoExplosive15 = new AmmoExplosiveShot(9,15,false);
public static Ammo ammoExplosive30 = new AmmoExplosiveShot(10,30,false);
public static Ammo ammoExplosive45 = new AmmoExplosiveShot(11,45,false);
public static Ammo ammoHE10 = new AmmoExplosiveShot(12,10,true);
public static Ammo ammoHE15 = new AmmoExplosiveShot(13,15,true);
public static Ammo ammoHE30 = new AmmoExplosiveShot(14,30,true);
public static Ammo ammoHE45 = new AmmoExplosiveShot(15,45,true);
public static Ammo ammoNapalm10 = new AmmoNapalmShot(16,10);
public static Ammo ammoNapalm15 = new AmmoNapalmShot(16,15);
public static Ammo ammoNapalm30 = new AmmoNapalmShot(16,30);
public static Ammo ammoNapalm45 = new AmmoNapalmShot(16,45);
public static Ammo clusterShot10 = new AmmoClusterShot(20,10);
public static Ammo clusterShot15 = new AmmoClusterShot(21,15);
public static Ammo clusterShot30 = new AmmoClusterShot(22,30);
public static Ammo clusterShot45 = new AmmoClusterShot(23,45);
public static Ammo ammoPebbleShot10 = new AmmoPebbleShot(24, 10);
public static Ammo ammoPebbleShot15 = new AmmoPebbleShot(25, 15);
public static Ammo ammoPebbleShot30 = new AmmoPebbleShot(26, 30);
public static Ammo ammoPebbleShot45 = new AmmoPebbleShot(27, 45);
public static Ammo ammoIronShot5 = new AmmoIronShot(28,5,10);
public static Ammo ammoIronShot10 = new AmmoIronShot(29,10,15);
public static Ammo ammoIronShot15 = new AmmoIronShot(30,15,30);
public static Ammo ammoIronShot25 = new AmmoIronShot(31,25,45);
public static Ammo ammoCanisterShot5 = new AmmoCanisterShot(32,5);
public static Ammo ammoCanisterShot10 = new AmmoCanisterShot(33,10);
public static Ammo ammoCanisterShot15 = new AmmoCanisterShot(34,15);
public static Ammo ammoCanisterShot25 = new AmmoCanisterShot(35,25);
public static Ammo ammoGrapeShot5 = new AmmoGrapeShot(36,5);
public static Ammo ammoGrapeShot10 = new AmmoGrapeShot(37,10);
public static Ammo ammoGrapeShot15 = new AmmoGrapeShot(38,15);
public static Ammo ammoGrapeShot25 = new AmmoGrapeShot(39,25);
public static Ammo ammoArrow = new AmmoArrow(40);
public static Ammo ammoArrowFlame = new AmmoArrowFlame(41);
public static Ammo ammoArrowIron = new AmmoArrowIron(42);
public static Ammo ammoArrowIronFlame = new AmmoArrowIronFlame(43);
public static Ammo ammoBallistaBolt = new AmmoBallistaBolt(44);
public static Ammo ammoBallistaBoltFlame = new AmmoBallistaBoltFlame(45);
public static Ammo ammoBallistaBoltExplosive = new AmmoBallistaBoltExplosive(46);
public static Ammo ammoBallistaBoltIron = new AmmoBallistaBoltIron(47);
public static Ammo ammoRocket = new AmmoHwachaRocket(48);
public static Ammo ammoHwachaRocketFlame = new AmmoHwachaRocketFlame(49);
public static Ammo ammoHwachaRocketExplosive = new AmmoHwachaRocketExplosive(50);
public static Ammo ammoHwachaRocketAirburst = new AmmoHwachaRocketAirburst(51);
//52 stoneBallShot (decl. above)
//53 ironBallShot (decl. above)

//54-63 reserved for future ammo types




private final ItemStack ammoStack;
public final int ammoType;
int entityDamage;
int vehicleDamage;
static final float gravityFactor = 9.81f*0.05f*0.05f;
String displayName = "AW.Ammo";
String displayTooltip = "AW.AmmoTooltip";
String modelTexture = "foo.png";
boolean isRocket = false;
boolean isArrow = false;
boolean isPersistent = false;
boolean isFlaming = false;
boolean isPenetrating = false;
float ammoWeight = 10;
float renderScale = 1.f;
IAmmoType secondaryAmmoType = null;
int secondaryAmmoCount = 0;

public Ammo(int ammoType)
  {
  this.ammoType = ammoType;
  this.ammoStack = new ItemStack(ItemLoader.ammoItem.itemID, 1, ammoType);
  if(ammoType>=0 && ammoType<ammoTypes.length)
    {    
    ammoTypes[ammoType]=this;
    }  
  }

@Override
public int getAmmoType()
  {
  return this.ammoType;
  }

@Override
public ItemStack getDisplayStack()
  {
  return this.ammoStack;
  }

@Override
public ItemStack getAmmoStack(int qty)
  {
  return new ItemStack(ItemLoader.ammoItem.itemID, qty, this.getAmmoType());
  }

@Override
public String getDisplayName()
  {
  return displayName;
  }

@Override
public String getDisplayTooltip()
  {
  return displayTooltip;
  }

@Override
public String getModelTexture()
  {
  return modelTexture;
  }

@Override
public boolean isRocket()
  {
  return isRocket;
  }

@Override
public boolean isPersistent()
  {
  return isPersistent;
  }

@Override
public boolean updateAsArrow()
  {
  return isArrow;
  }

@Override
public float getAmmoWeight()
  {
  return ammoWeight;
  }

/**
 * override to implement upgrade-specific ammo use.  this method will be checked before a vehicle will accept ammo into its bay, or fire ammo from its bay
 */
@Override
public boolean isAmmoValidFor(VehicleBase vehicle)
  {
  return true;
  }

@Override
public float getRenderScale()
  {
  return renderScale;
  }

@Override
public float getGravityFactor()
  {
  return gravityFactor;
  }

@Override
public int getEntityDamage()
  {
  return entityDamage;
  }

@Override
public int getVehicleDamage()
  {
  return vehicleDamage;
  }

@Override
public boolean isFlaming()
  {
  return isFlaming;
  }

@Override
public boolean isPenetrating()
  {
  return isPenetrating;
  }

@Override
public IAmmoType getSecondaryAmmoType()
  {
  return secondaryAmmoType;
  }

@Override
public int getSecondaryAmmoTypeCount()
  {
  return secondaryAmmoCount;
  }

@Override
public boolean hasSecondaryAmmo()
  {
  return this.secondaryAmmoType!=null;
  }

protected void breakBlockAndDrop(World world, int x, int y, int z)
  {
  if(!Config.blockDestruction)
    {
    return;
    }
  int id = world.getBlockId(x, y , z);
  int meta = world.getBlockMetadata(x, y , z);
  if(id!=0 && id!=Block.bedrock.blockID && Block.blocksList[id]!=null)
    {      
    Block.blocksList[id].dropBlockAsItem(world, x, y , z, meta, 0);
    world.setBlock(x, y , z, 0);
    }
  }

/**
 * actually tries setting the block, block below, and block above
 * will grab the first success, top down.
 * @param world
 * @param x
 * @param y
 * @param z
 */
protected void igniteBlock(World world, int x, int y, int z)
  {
  //TODO...needs to uhh..check from bottom up...yah...
  if(!Config.blockFires)
    {
    return;
    }
  int id = world.getBlockId(x, y+1, z);  
  if(id==0)
    {      
    world.setBlockWithNotify(x, y+1, z, Block.fire.blockID);
    return;
    }
  id = world.getBlockId(x, y, z);
  if(id==0)
    {      
    world.setBlockWithNotify(x, y, z, Block.fire.blockID);
    return;
    }
  id = world.getBlockId(x, y-1, z);
  if(id==0)
    {      
    world.setBlockWithNotify(x, y-1, z, Block.fire.blockID);
    }
  }

}
