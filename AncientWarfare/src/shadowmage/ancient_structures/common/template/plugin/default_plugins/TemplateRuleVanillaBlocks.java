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
package shadowmage.ancient_structures.common.template.plugin.default_plugins;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_structures.common.block.BlockDataManager;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;

public class TemplateRuleVanillaBlocks extends TemplateRule
{

String blockName;
int meta;
NBTTagCompound dataTag;

public TemplateRuleVanillaBlocks(Block block, int meta)
  {
  super("vanillaBlock");
  this.blockName = block.getUnlocalizedName();
  this.meta = meta;  
  }


public TemplateRuleVanillaBlocks(Block block, int meta, NBTTagCompound specialData)
  {
  this(block, meta);
  this.dataTag = specialData;
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  Block block = BlockDataManager.getBlockByName(blockName);
  int localMeta = BlockDataManager.getRotatedMeta(block, this.meta, turns);  
  world.setBlock(x, y, z, block.blockID, localMeta, 2);//using flag=2 -- no block update, but send still send to clients (should help with issues of things popping off)
  //world.setBlockMetadataWithNotify(x, y, z, localMeta, 2);//may need to re-set metadata...freaking chests
  }

@Override
public String[] getRuleLines()
  {
  return null;
  }
  
@Override
public boolean shouldReuseRule(World world, Block block, int meta, int x, int y, int z)
  {
  return block!=null && dataTag==null && blockName.equals(block.getUnlocalizedName()) && meta == this.meta;
  }

@Override
public boolean shouldReuseRule(World world, Entity entity, int x, int y, int z)
  {
  return false;
  }
}
