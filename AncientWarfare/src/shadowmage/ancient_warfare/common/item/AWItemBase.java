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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AWItemBase extends Item
{

public AWItemBase(int itemID, boolean hasSubTypes)
  {
  super(itemID);  
  this.setHasSubtypes(hasSubTypes);
  this.setCreativeTab(CreativeTabAW.instance());
  }

/**
 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
 */
@SideOnly(Side.CLIENT)
@Override
public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(itemID);
  if(d!=null)
    {
    par3List.addAll(d.getDisplayStackCache());
    }
  else
    {
    super.getSubItems(par1, par2CreativeTabs, par3List);
    } 
  }

public boolean isShiftClick(EntityPlayer player)
  {
  boolean shift = player.isSneaking();
  if(Config.invertShiftClickOnItems)
    {
    return !shift;
    }
  else
    {
    return shift;
    }
  }

@Override
public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
  if(stack!=null)
    {
    Description d = DescriptionRegistry2.instance().getDescriptionFor(stack.itemID);
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
  Description d = DescriptionRegistry2.instance().getDescriptionFor(itemID);
  if(d!=null)
    {
    return d.getDisplayName(0);
    }
  return "Unregistered Item : "+itemID;
  }

@Override
public String getUnlocalizedName(ItemStack par1ItemStack)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(itemID);
  if(d!=null)
    {
    return d.getDisplayName(par1ItemStack.getItemDamage());
    }
  return "Unregistered Item : "+itemID+":"+par1ItemStack.getItemDamage();
  }

@Override
public String getItemDisplayName(ItemStack par1ItemStack)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(itemID);
  if(d!=null)
    {
    String name = d.getDisplayName(par1ItemStack.getItemDamage());
    return StatCollector.translateToLocal(name);
    }
  return "Unregistered Item : "+itemID+":"+par1ItemStack.getItemDamage();
  }

@Override
public void registerIcons(IconRegister par1IconRegister)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(itemID);
  if(d!=null)
    {
//    Config.logDebug("registering icons for : "+itemID +":: "+d.getDisplayName(0));
    d.registerIcons(par1IconRegister);
    }
  }

@Override
public Icon getIconFromDamage(int par1)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(itemID);
  if(d!=null)
    {
    return d.getIconFor(par1);
    }
  return super.getIconFromDamage(par1);
  }

@Override
public Icon getIconFromDamageForRenderPass(int par1, int par2)
  {
  return getIconFromDamage(par1);
  }

@Override
public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player,  ItemStack usingItem, int useRemaining)
  {
  return getIconFromDamage(stack.getItemDamage());
  }

@Override
public Icon getIcon(ItemStack stack, int pass)
  {
  return getIconFromDamage(stack.getItemDamage());
  }

}
