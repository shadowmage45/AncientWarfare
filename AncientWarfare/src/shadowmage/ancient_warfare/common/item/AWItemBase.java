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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AWItemBase extends Item
{

protected List<ItemStack> subTypes = new ArrayList<ItemStack>();

public AWItemBase(int itemID, boolean hasSubTypes)
  {
  super(itemID);  
  this.setHasSubtypes(hasSubTypes);
  this.setCreativeTab(CreativeTabAW.instance());
  this.setTextureFile("/shadowmage/ancient_warfare/resources/item/items.png");
  }

/**
 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
 */
@SideOnly(Side.CLIENT)
@Override
public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
  {
  if(this.hasSubtypes)
    {
    for(ItemStack stack : this.subTypes)
      {      
      if(stack!=null)
        {
        par3List.add(stack);
        }
      }
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

/**
 * add a subtype to this Item, to be displayed in creative inventories
 * @param stack
 */
public void addSubType(ItemStack stack)
  {
  if(stack==null)
    {
    return;
    }
  /**
   * if it does not obviously contain the stack
   */
  if(!this.subTypes.contains(stack))
    {
    /**
     * test to see if it contains a matching stack for itemID/DMG
     */
    for(Object obj : this.subTypes)
      {
      ItemStack test = (ItemStack)obj;
      if(test!=null)
        {
        if(ItemStack.areItemStacksEqual(stack, test))
          {
          return;
          }        
        }
      }
    this.subTypes.add(stack);
    }  
  }

@Override
public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
  {
  if(par1ItemStack!=null)
    {
    if(DescriptionRegistry.instance().contains(par1ItemStack))
      {
      String toolTip = DescriptionRegistry.instance().getEntryFor(par1ItemStack.itemID).getTooltip(par1ItemStack.getItemDamage());
      if(toolTip!=null)
        {
        par3List.add(toolTip);
        }
      }    
    }  
  }

/**
 * the first/top call for iconIndex
 */
@Override
public int getIconIndex(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
  {
  if(stack.getItem().getHasSubtypes())
    {
    return stack.getItemDamage();
    }
  return super.getIconIndex(stack, renderPass, player, usingItem, useRemaining);
  }

@Override
public int getIconFromDamage(int par1)
  {
  if(this.getHasSubtypes())
    {
    return par1;
    }
  return this.iconIndex;
  }

}
