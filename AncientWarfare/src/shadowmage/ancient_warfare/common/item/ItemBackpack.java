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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.network.GUIHandler;
import shadowmage.ancient_warfare.client.render.AWRenderHelper;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBackpack extends AWItemClickable
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemBackpack(int itemID)
  {
  super(itemID, true);
  this.maxStackSize = 1;
  this.hasLeftClick = false;
  }

@Override
public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
  if(stack!=null)
    {
    int dmg = stack.getItemDamage() - stack.getItemDamage()%16;
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
public Icon getIconFromDamage(int par1)
  {
  return super.getIconFromDamage(par1);
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(!world.isRemote)
    {
    GUIHandler.instance().openGUI(GUIHandler.BACKPACK, player, world, 0, 0, 0);
    }
  return false;
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  return false;
  }

public static AWInventoryBasic getInventoryFor(ItemStack stack)
  {
  AWInventoryBasic inventory = null;
  if(stack!=null && stack.hasTagCompound() && stack.getTagCompound().hasKey("AWBackPack"))
    {
    NBTTagCompound tag = stack.getTagCompound().getCompoundTag("AWBackPack");
    int size = tag.getInteger("size");
    inventory = new AWInventoryBasic(size);
    inventory.readFromNBT(tag);
    }
  if(inventory == null)
    {
    inventory = new AWInventoryBasic((stack.getItemDamage()/16)*9 + 9);
    }
  return inventory;
  }

public static void writeInventoryToItem(ItemStack stack, AWInventoryBasic inventory)
  {
  if(stack!=null && inventory!=null && stack.itemID == ItemLoader.backpack.itemID)
    {
    NBTTagCompound tag = inventory.getNBTTag();
    tag.setInteger("size", inventory.getSizeInventory());
    stack.setTagInfo("AWBackPack", tag);
    }
  }

@Override
@SideOnly(Side.CLIENT)
public int getColorFromItemStack(ItemStack stack, int par2)
  {  
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("color"))
    {
    int color = stack.getTagCompound().getInteger("color");
    return AWRenderHelper.getCompositeColor(color);    
    }
  return super.getColorFromItemStack(stack, par2);
  }

}
