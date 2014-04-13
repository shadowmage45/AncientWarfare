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

import buildcraft.BuildCraftTransport;
import buildcraft.transport.TileGenericPipe;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_structures.api.TemplateRuleBlock;

public class TemplateRuleBuildCraftPipe extends TemplateRuleBlock
{

NBTTagCompound pipeTagData = new NBTTagCompound();

public TemplateRuleBuildCraftPipe(World world, int x, int y, int z, Block block, int meta, int turns)
  {
  super(world, x, y, z, block, meta, turns);
  TileGenericPipe pipe = (TileGenericPipe) world.getBlockTileEntity(x, y, z); 
  pipe.writeToNBT(pipeTagData);
  
  int[] facadeBlocks = new int[ForgeDirection.VALID_DIRECTIONS.length];
  int[] facadeMeta = new int[ForgeDirection.VALID_DIRECTIONS.length];
  boolean[] plugs = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
  int side;
  for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) 
    {
    side = i;
    if(i>=2)
      {
      side = i-2;
      side = (side + turns)%4;
      side = side + 2;
      }
    facadeBlocks[side] = pipeTagData.getInteger("facadeBlocks[" + i + "]");
    facadeMeta[side] = pipeTagData.getInteger("facadeMeta[" + i + "]");
    plugs[side] = pipeTagData.getBoolean("plug[" + i + "]");
    }
  for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) 
    {
    pipeTagData.setInteger("facadeBlocks[" + i + "]", facadeBlocks[i]);
    pipeTagData.setInteger("facadeMeta[" + i + "]", facadeMeta[i]);
    pipeTagData.setBoolean("plug[" + i + "]", plugs[i]);
    }
  }

public TemplateRuleBuildCraftPipe()
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
  NBTTagCompound pipeTagData = (NBTTagCompound) this.pipeTagData.copy();
  int[] facadeBlocks = new int[ForgeDirection.VALID_DIRECTIONS.length];
  int[] facadeMeta = new int[ForgeDirection.VALID_DIRECTIONS.length];
  boolean[] plugs = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
  int side;
  for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) 
    {
    side = i;
    if(i>=2)
      {
      side = i-2;
      side = (side + turns)%4;
      side = side + 2;
      }
    facadeBlocks[side] = pipeTagData.getInteger("facadeBlocks[" + i + "]");
    facadeMeta[side] = pipeTagData.getInteger("facadeMeta[" + i + "]");
    plugs[side] = pipeTagData.getBoolean("plug[" + i + "]");
    }
  for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) 
    {
    pipeTagData.setInteger("facadeBlocks[" + i + "]", facadeBlocks[i]);
    pipeTagData.setInteger("facadeMeta[" + i + "]", facadeMeta[i]);
    pipeTagData.setBoolean("plug[" + i + "]", plugs[i]);
    }
  world.setBlock(x, y, z, BuildCraftTransport.genericPipeBlock.blockID);
  TileGenericPipe tile = (TileGenericPipe) world.getBlockTileEntity(x, y, z);
  tile.readFromNBT(pipeTagData);
  world.markBlockForUpdate(x, y, z);
  }

@Override
public void parseRuleData(NBTTagCompound tag)
  {
  pipeTagData = tag.getCompoundTag("pipeData");
  }

@Override
public void writeRuleData(NBTTagCompound tag)
  {
  tag.setCompoundTag("pipeData", pipeTagData);
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
