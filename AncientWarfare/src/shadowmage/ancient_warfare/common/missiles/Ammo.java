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
package shadowmage.ancient_warfare.common.missiles;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
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

public static Ammo ammoStoneShot10 = new AmmoStoneShot(0,10);
public static Ammo ammoStoneShot25 = new AmmoStoneShot(1,25);
public static Ammo ammoStoneShot50 = new AmmoStoneShot(2,50);
public static Ammo ammoFireShot10 = new AmmoFlameShot(3,10);
public static Ammo ammoFireShot25 = new AmmoFlameShot(4,25);
public static Ammo ammoFireShot50 = new AmmoFlameShot(5,50);

public static Ammo ammoArrow = new AmmoArrow(30);

public static Ammo ammoRocket = new AmmoRocket(36);

private ItemStack ammoStack;
int ammoType;
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

public Ammo(int ammoType)
  {
  this.ammoStack = new ItemStack(ItemLoader.ammoItem.itemID, 1, ammoType);
  this.ammoType = ammoType;
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
