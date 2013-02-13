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

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public abstract class ItemBuilderBase extends AWItemClickable 
{
/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemBuilderBase(int itemID)
  {
  super(itemID, false);
  }

/**
 * client side, return the client info associated with the structure to be built for this
 * item stack, null if none
 * @param stack
 * @return
 */
public abstract StructureClientInfo getStructureForStack(ItemStack stack);

/**
 * should this item also render the bounding box for the builder block? (ticked builder)
 * @return
 */
public abstract boolean renderBuilderBlockBB();

  
@Override
public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
  {
  return false;
  }

public static boolean isBuilderItem(int id)
  {
  return id== ItemLoader.structureBuilderDirect.itemID || id == ItemLoader.structureCreativeBuilder.itemID || id == ItemLoader.structureCreativeBuilderTicked.itemID;
  }

public static boolean hasScanBB(int id)
  {
  return id==ItemLoader.structureBuilderDirect.itemID;
  }

/**
 * did construction start sucessfully? (e.g. struct was not locked, location was valid)
 * @param world
 * @param struct
 * @param hit
 * @param facing
 * @return
 */
public abstract boolean attemptConstruction(World world, ProcessedStructure struct, BlockPosition hit, int facing);
  


}
