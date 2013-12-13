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
package shadowmage.ancient_warfare.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import shadowmage.ancient_framework.common.item.CreativeTabAW;
import shadowmage.ancient_framework.common.registry.entry.Description;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRation extends ItemFood
{

Description description;
/**
 * @param id
 */
public ItemRation(int id)
  {
  super(id, 2, 0.2f, false);
  this.setCreativeTab(CreativeTabAW.normal);
  }

@SideOnly(Side.CLIENT)
public void registerIcons(IconRegister par1IconRegister)
  {
  this.itemIcon = par1IconRegister.registerIcon("ancientwarfare:npc/ration1");
  }

@Override
public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
  if(stack!=null)
    {
    Description d = description;
    if(d!=null)
      {
      List<String> tips = d.getDisplayTooltips(stack.getItemDamage());
      if(tips!=null && !tips.isEmpty())
        {
        for(String tip : tips)
          {
          list.add(StatCollector.translateToLocal(tip));
          }        
        }
      }     
    }  
  }

@Override
public String getItemStackDisplayName(ItemStack par1ItemStack)
  {
  return getItemDisplayName(par1ItemStack);
  }

@Override
public String getUnlocalizedName()
  {
  Description d = description;
  if(d!=null)
    {
    return d.getDisplayName(0);
    }
  return "Unregistered Item : "+itemID;
  }

@Override
public String getUnlocalizedName(ItemStack par1ItemStack)
  {
  Description d = description;
  if(d!=null)
    {
    return d.getDisplayName(par1ItemStack.getItemDamage());
    }
  return "Unregistered Item : "+itemID+":"+par1ItemStack.getItemDamage();
  }

@Override
public String getItemDisplayName(ItemStack par1ItemStack)
  {
  Description d = description;
  if(d!=null)
    {
    return StatCollector.translateToLocal(d.getDisplayName(par1ItemStack.getItemDamage()));
    }
  return "Unregistered Item : "+itemID+":"+par1ItemStack.getItemDamage();
  }

}
