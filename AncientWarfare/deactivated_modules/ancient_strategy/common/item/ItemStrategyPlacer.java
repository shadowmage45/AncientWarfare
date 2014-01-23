/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_strategy.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.item.AWItemClickable;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_strategy.common.structure.StrategyStructure;
import shadowmage.ancient_strategy.common.structure.StrategyStructureManager;
import shadowmage.ancient_structures.common.manager.StructureTemplateManager;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureBuilder;

public class ItemStrategyPlacer extends AWItemClickable
{

public ItemStrategyPlacer(int itemID)
  {
  super(itemID);
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack,  BlockPosition hit, int side)
  {
  if(world.isRemote || hit==null){return true;}
  hit.offsetForMCSide(side);
  int face = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);  
  StructureTemplate template = StructureTemplateManager.instance().getTemplate("strategyTest");
  StructureBuilder builder = new StructureBuilder(world, template, face, hit.x, hit.y, hit.z);
  builder.instantConstruction();
  
  StrategyStructure structure = new StrategyStructure(hit.x, hit.y, hit.z, face, template, null);
  StrategyStructureManager.instance().addStructure(world, structure);  
  return true;
  }

@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player,  ItemStack stack, BlockPosition hit, int side)
  {
  return false;
  }

private class StrategyInfo
{
String structureName;
//structure type
//health, if they are 'repackable'
}


}
