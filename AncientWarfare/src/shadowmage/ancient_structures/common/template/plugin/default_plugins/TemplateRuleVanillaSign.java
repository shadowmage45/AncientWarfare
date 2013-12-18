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

import java.io.BufferedWriter;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;
import shadowmage.ancient_structures.common.block.BlockDataManager;

public class TemplateRuleVanillaSign extends TemplateRuleVanillaBlocks
{

String signContents[];
boolean wall = true;

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
    this.meta = (meta+4*turns)%16;
    } 
  }

public TemplateRuleVanillaSign()
  {
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  Block block = wall? Block.signWall : Block.signPost;//BlockDataManager.getBlockByName(blockName);
  int meta = 0;
  if(block==Block.signPost)
    {
    meta = (this.meta+4*turns)%16; 
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

@Override
public void writeRuleData(BufferedWriter out) throws IOException
  {
  super.writeRuleData(out);
  for(int i = 0; i<4 ;i++)
    {
    out.write(signContents[i]);
    out.newLine();
    }
  }

@Override
public boolean shouldReuseRule(World world, Block block, int meta, int turns, TileEntity te, int x, int y, int z)
  {
  if(te instanceof TileEntitySign)
    {
    if(!signContents.equals(((TileEntitySign)te).signText))
      {
      return false;
      }
    }
  return ((block==Block.signPost && !this.wall && (meta+4*turns%16)==this.meta)||(block==Block.signWall && this.wall && BlockDataManager.getRotatedMeta(block, meta, turns)==this.meta));
  }

@Override
public void parseRuleData(String[] ruleData)
  {  
  super.parseRuleData(ruleData);
  this.signContents = new String[4];
  signContents[0] = ruleData[2];
  signContents[1] = ruleData[3];
  signContents[2] = ruleData[4];
  signContents[3] = ruleData[5];
  }


}
