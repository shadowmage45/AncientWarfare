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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.civics.types.Civic;
import shadowmage.ancient_warfare.common.civics.worksite.te.builder.TECivicBuilder;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.registry.CivicRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCivicBuilder extends ItemBuilderInstant
{
private Icon icon;
/**
 * @param itemID
 */
public ItemCivicBuilder(int itemID)
  {
  super(itemID);
  this.setHasSubtypes(true);
  }

@Override
public Icon getIconFromDamage(int par1)
  {
  return icon;
  }

@Override
public void registerIcons(IconRegister par1IconRegister)
  {
  icon = par1IconRegister.registerIcon("ancientwarfare:castle");
  }

/**
 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
 */
@SideOnly(Side.CLIENT)
@Override
public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
  {
  List<StructureClientInfo> info = StructureManager.instance().getClientStructures();
  ItemStack stack;
  NBTTagCompound tag;
  for(StructureClientInfo c : info)
    {    
    if(!c.survival)
      {
      continue;
      }
    stack = new ItemStack(this,1);
    tag = new NBTTagCompound();
    tag.setString("name", c.name);
    stack.setTagInfo("structData", tag);
    par3List.add(stack);
    }
  }

public static ItemStack getCivicBuilderItem(String structure)
  {
  ItemStack stack = new ItemStack(ItemLoader.civicBuilder.itemID,1,0);
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", structure);
  stack.setTagInfo("structData", tag);
  return stack;
  }

@Override
public boolean attemptConstruction(World world, ProcessedStructure struct, BlockPosition hit, int face, StructureBuildSettings settings)
  {
  if(struct.isLocked())
    {
    return false;
    }
  BlockPosition offsetHit = hit.copy();
  offsetHit.moveForward(face, -struct.zOffset + 1 + struct.getClearingBuffer());
  CivicRegistry.instance().setCivicBlock(world, hit.x, hit.y, hit.z, Civic.builder.getGlobalID());

  TECivicBuilder te = (TECivicBuilder)world.getBlockTileEntity(hit.x, hit.y, hit.z);
  if(te!=null)
    { 
    BuilderTicked builder = new BuilderTicked(world, struct, face, offsetHit);
    builder.startConstruction();
    builder.setOverrides(-1, false, false, true);
    te.setBuilder(builder);
    struct.addBuilder(builder);    
    return true;
    }  
  return false;
  } 

@Override
public boolean renderBuilderBlockBB()
  {
  return true;
  }
}
