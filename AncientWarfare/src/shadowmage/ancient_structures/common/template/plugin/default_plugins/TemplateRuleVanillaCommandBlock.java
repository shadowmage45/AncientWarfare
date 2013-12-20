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
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.utils.StringTools;

public class TemplateRuleVanillaCommandBlock extends TemplateRuleVanillaBlocks
{

String userName;
String command;
int commandCount;

public TemplateRuleVanillaCommandBlock(World world, int x, int y, int z, Block block, int meta, int turns)
  {
  super(world, x, y, z, block, meta, turns);
  TileEntityCommandBlock te = (TileEntityCommandBlock) world.getBlockTileEntity(x, y, z);
  
  this.userName = te.getCommandSenderName();
  this.command = te.getCommand();
  this.commandCount = te.getSignalStrength();  
  }

public TemplateRuleVanillaCommandBlock()
  {
  
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  super.handlePlacement(world, turns, x, y, z);
  TileEntityCommandBlock te = (TileEntityCommandBlock) world.getBlockTileEntity(x, y, z);
  te.setCommand(command);
  te.setCommandSenderName(userName);
  te.setSignalStrength(commandCount);
  }

@Override
public void parseRuleData(List<String> ruleData)
  {
  super.parseRuleData(ruleData);
  for(String line : ruleData)
    {
    if(line.toLowerCase().startsWith("command=")){command = StringTools.safeParseString("=", line);}
    else if(line.toLowerCase().startsWith("username=")){userName = StringTools.safeParseString("=", line);}
    else if(line.toLowerCase().startsWith("commandcount=")){commandCount = StringTools.safeParseInt("=", line);}    
    }
  }

@Override
public void writeRuleData(BufferedWriter out) throws IOException
  {
  super.writeRuleData(out);
  out.write("command="+command);
  out.newLine();
  out.write("userName="+userName);
  out.newLine();
  out.write("commandCount="+commandCount);
  out.newLine();
  }





}
