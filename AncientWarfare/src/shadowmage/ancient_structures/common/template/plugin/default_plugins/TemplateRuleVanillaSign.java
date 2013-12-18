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

import shadowmage.ancient_structures.common.block.BlockDataManager;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class TemplateRuleVanillaSign extends TemplateRuleVanillaBlocks
{

String signContents[];
boolean wall = true;
/**
 * @param world
 * @param x
 * @param y
 * @param z
 * @param block
 * @param meta
 */
public TemplateRuleVanillaSign(World world, int x, int y, int z, Block block, int meta, int turns)
  {
  super(world, x, y, z, block, meta, turns);
  TileEntitySign te = (TileEntitySign) world.getBlockTileEntity(x, y, z);
  signContents = new String[te.signText.length];
  for(int i = 0; i < signContents.length; i++)
    {
    signContents[i] = te.signText[i];
    }
  if(block==Block.signPost)
    {
    wall = false;
    /**
     * figure out meta-rotation
     */
    /**
     * else...already handled by super-call
     */
    } 
  }

public TemplateRuleVanillaSign(String[] ruleData)
  {
  super(ruleData);
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  Block block = wall? Block.signWall : Block.signPost;//BlockDataManager.getBlockByName(blockName);
  int meta = 0;
  if(block==Block.signPost)
    {
    /**
     * figure out meta-rotation
     */
    }
  else
    {
    meta = BlockDataManager.getRotatedMeta(block, this.meta, turns);
    }
  world.setBlock(x, y, z, block.blockID, meta, 2);
  TileEntitySign te = (TileEntitySign) world.getBlockTileEntity(x, y, z);
  te.signText = new String[this.signContents.length];
  for(int i = 0; i < this.signContents.length; i++)
    {
    te.signText[i] = this.signContents[i];
    }
  world.markBlockForUpdate(x, y, z);
  }


}
