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
package shadowmage.ancient_framework.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabAW extends CreativeTabs
{

public static CreativeTabAW normal = new CreativeTabAW("Ancient Warfare");

public static CreativeTabAW vehicleTab = new CreativeTabAW("Ancient Warfare Vehicles")
  {
  @Override
  public ItemStack getIconItemStack()
    {
    return new ItemStack(ItemLoaderCore.vehicleSpawner);
    }
  };
  
public static CreativeTabAW npcTab = new CreativeTabAW("Ancient Warfare Npcs")
  {
  @Override
  public ItemStack getIconItemStack()
    {
    return new ItemStack(ItemLoaderCore.npcSpawner,1,2);
    }
  };
  
public static CreativeTabAW ammoTab = new CreativeTabAW("Ancient Warfare Ammunitions")
  {
  @Override
  public ItemStack getIconItemStack()
    {
    return new ItemStack(ItemLoaderCore.ammoItem);
    }
  };

public static CreativeTabAW researchTab = new CreativeTabAW("Ancient Warfare Research")
  {
  @Override
  public ItemStack getIconItemStack()
    {
    return new ItemStack(ItemLoaderCore.researchNotes);
    }
  };
  
public static CreativeTabAW instance()
  {
  return normal;
  }

private CreativeTabAW(String label)
  {
  super(label); 
  }

@SideOnly(Side.CLIENT)

/**
 * Get the ItemStack that will be rendered to the tab.
 */
@Override
public ItemStack getIconItemStack()
  {
  return new ItemStack(ItemLoaderCore.civicPlacer);
  }

/**
 * Gets the translated Label.
 */
@Override
public String getTranslatedTabLabel()
  {
  return this.getTabLabel();
  }

}
