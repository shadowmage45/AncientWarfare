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
package shadowmage.ancient_structures_buildcraft_plugin;

import java.util.List;

import buildcraft.BuildCraftEnergy;
import buildcraft.energy.TileEngine;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_structures.common.template.rule.TemplateRuleBlock;

public class TemplateRuleBuildCraftEngine extends TemplateRuleBlock
{

int engineType;
ForgeDirection orientation;
NBTTagCompound engineTag = new NBTTagCompound();

public TemplateRuleBuildCraftEngine(World world, int x, int y, int z, Block block, int meta, int turns)
  {
  super(world, x, y, z, block, meta, turns);
  TileEntity te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TileEngine)
    {
    TileEngine engine = (TileEngine)te;
    ForgeDirection d = ((TileEngine) te).orientation;
    for(int i = 0; i < turns; i++)
      {
      d = d.getRotation(ForgeDirection.UP);
      }
    orientation = d;
    engine.writeToNBT(engineTag);
    }
  this.engineType = meta;
  
  }

public TemplateRuleBuildCraftEngine()
  {
  
  }

@Override
public boolean shouldReuseRule(World world, Block block, int meta, int turns, TileEntity te, int x, int y, int z)
  {
  return false;
  }

@Override
public void handlePlacement(World world, int turns, int x, int y, int z)
  {
  ForgeDirection d = orientation;
  for(int i = 0; i < turns; i++)
    {
    d = d.getRotation(ForgeDirection.UP);
    }  
  world.setBlock(x, y, z, BuildCraftEnergy.engineBlock.blockID, engineType, 3);
  TileEntity te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TileEngine)
    {
    TileEngine engine = (TileEngine)te;
    engine.readFromNBT(engineTag);
    engine.orientation = d;
    world.markBlockForUpdate(x, y, z);
    }
  }

@Override
public void parseRuleData(NBTTagCompound tag)
  {
  orientation = ForgeDirection.values()[tag.getInteger("orientation")];
  engineType = tag.getInteger("engineType");
  engineTag = tag.getCompoundTag("engineTag");
  }

@Override
public void writeRuleData(NBTTagCompound tag)
  {
  tag.setInteger("oreintation", orientation.ordinal());
  tag.setInteger("engineType", engineType);
  tag.setTag("engineTag", engineTag);
  }

@Override
public void addResources(List<ItemStack> resources)
  {

  }

@Override
public boolean shouldPlaceOnBuildPass(World world, int turns, int x, int y, int z, int buildPass)
  {
  return buildPass==0;
  }

}
