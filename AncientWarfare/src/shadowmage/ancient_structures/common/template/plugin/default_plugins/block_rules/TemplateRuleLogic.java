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
package shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.utils.NBTTools;

public class TemplateRuleLogic extends TemplateRuleVanillaBlocks
{

NBTTagCompound tag = new NBTTagCompound();

public TemplateRuleLogic(World world, int x, int y, int z, Block block, int meta, int turns)
  {
  super(world, x, y, z, block, meta, turns);
  TileEntity te = world.getBlockTileEntity(x, y, z);
  te.writeToNBT(tag);  
  }

public TemplateRuleLogic()
  {
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  super.handlePlacement(world, turns, x, y, z);
  tag.setInteger("x", x);
  tag.setInteger("y", y);
  tag.setInteger("z", z);
  TileEntity te = world.getBlockTileEntity(x, y, z);
  te.readFromNBT(tag);
  world.markBlockForUpdate(x, y, z);
  }

@Override
public void writeRuleData(BufferedWriter out) throws IOException
  {
  super.writeRuleData(out);
  out.write("tag:");
  out.newLine();
  List<String> tagData = new ArrayList<String>();
  NBTTools.writeNBTToLines(tag, tagData);
  for(String line : tagData)
    {
    out.write(line);
    out.newLine();
    }
  out.write(":endtag");
  out.newLine();
  }

@Override
public boolean shouldReuseRule(World world, Block block, int meta, int turns, TileEntity te, int x, int y, int z)
  {
  return false;
  }

@Override
public void parseRuleData(List<String> ruleData)
  {
  super.parseRuleData(ruleData);
  List<String> tagLines = new ArrayList<String>();
  Iterator<String> it = ruleData.iterator();
  String line;
  while(it.hasNext() && (line=it.next())!=null)
    {
    if(line.startsWith("tag:"))
      {
      while(it.hasNext() && (line=it.next())!=null)
        {
        tagLines.add(line);
        if(line.startsWith(":endtag"))
          {
          break;
          }
        }
      }
    }
  tag = NBTTools.readNBTFrom(tagLines);
  }


}
