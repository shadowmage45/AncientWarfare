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
package shadowmage.ancient_warfare.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.VehicleRegistry;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;


public class ItemVehicleSpawner extends AWItemClickable
{

public ItemVehicleSpawner(int itemID)
  {
  super(itemID,true);
  this.setTextureFile("/shadowmage/ancient_warfare/resources/item/vehicles.png");
  this.setItemName("awVehicleItem");
  }

@Override
public String getItemNameIS(ItemStack par1ItemStack)
  {
  return "Vehicle" + String.valueOf(par1ItemStack.getItemDamage()); 
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("AWVehSpawner"))
    {
    int level = stack.getTagCompound().getCompoundTag("AWVehSpawner").getInteger("lev");
    VehicleBase vehicle = VehicleRegistry.instance().getVehicleForType(world, stack.getItemDamage(), level);
    vehicle.setPosition(hit.x+0.5d, hit.y, hit.z+0.5d);
    vehicle.prevRotationYaw = vehicle.rotationYaw = player.rotationYaw;
    world.spawnEntityInWorld(vehicle);
    return true;
    }
  Config.logError("Vehicle spawner item was missing NBT data, something may have corrupted this item");
  return false;
  }

@Override
public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
  {
  super.addInformation(stack, par2EntityPlayer, par3List, par4);  
  if(stack!=null)
    {
    if(stack.hasTagCompound() && stack.getTagCompound().hasKey("AWVehSpawner"))
      {
      par3List.add("Material Level: "+stack.getTagCompound().getCompoundTag("AWVehSpawner").getInteger("lev"));
      }
    }  
  }

@Override
public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
  {
  List displayStacks = VehicleRegistry.instance().getCreativeDisplayItems();
  par3List.addAll(displayStacks);
  }

}
