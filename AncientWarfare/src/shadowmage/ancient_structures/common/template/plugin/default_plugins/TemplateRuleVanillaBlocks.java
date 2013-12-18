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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.block.BlockDataManager;
import shadowmage.ancient_structures.common.template.rule.TemplateRuleBlock;

public class TemplateRuleVanillaBlocks extends TemplateRuleBlock
{

String blockName;
int meta;

/**
 * constructor for dynamic construction.  passed world and coords so that the rule can handle its own logic internally
 * @param world
 * @param x
 * @param y
 * @param z
 * @param block
 * @param meta
 */
public TemplateRuleVanillaBlocks(World world, int x, int y, int z, Block block, int meta, int turns)
  {
  super(world, x, y, z, block, meta, turns);
  this.blockName = block.getUnlocalizedName();
  this.meta = BlockDataManager.getRotatedMeta(block, meta, turns);
  }

public TemplateRuleVanillaBlocks()
  {  
  
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  Block block = BlockDataManager.getBlockByName(blockName);
  int localMeta = BlockDataManager.getRotatedMeta(block, this.meta, turns);  
  world.setBlock(x, y, z, block.blockID, localMeta, 2);//using flag=2 -- no block update, but send still send to clients (should help with issues of things popping off)
  }

@Override
public String[] getRuleLines()
  {
  return new String[]{"blockName="+this.blockName, "meta="+this.meta};
  }
  
@Override
public boolean shouldReuseRule(World world, Block block, int meta, int turns, TileEntity te, int x, int y, int z)
  {
  return block!=null && blockName.equals(block.getUnlocalizedName()) && BlockDataManager.getRotatedMeta(block, meta, turns) == this.meta;
  }

@Override
public void parseRuleData(String[] ruleData)
  {
  if(ruleData.length<2){throw new IllegalArgumentException("not enough data for block rule");}
  this.blockName = StringTools.safeParseString("=", ruleData[0]);
  this.meta = StringTools.safeParseInt("=", ruleData[1]);
  }

}
