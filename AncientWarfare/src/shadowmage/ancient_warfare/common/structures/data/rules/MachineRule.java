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
package shadowmage.ancient_warfare.common.structures.data.rules;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.block.TEAWBlockReinforced;
import shadowmage.ancient_warfare.common.crafting.TEAWCrafting;
import shadowmage.ancient_warfare.common.machine.TEEngine;
import shadowmage.ancient_warfare.common.machine.TEMachine;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class MachineRule
{

int x;
int y;
int z;

/**
 * 0=crafting
 * 1=machine
 * 2=engine
 * 3=reinforced
 */
int machineType;

/**
 * the 'meta' of the block
 * as machines/crafting/engines/reinforced are all mapped by meta, this simplifies things greatly
 */
int type;

/**
 * the facing direction, if applicable
 */
int facing;

/**
 * the assigned team for the block, if any
 */
int team;

/**
 * the entire NBTTag as taken by writeToNBT from the tileEntity
 */
NBTTagCompound tileTag = new NBTTagCompound();

private MachineRule()
  {
  
  }

public MachineRule(TEAWCrafting te, int x, int y, int z, int meta)
  {
  this.type = meta;
  this.team = te.teamNum;
  this.machineType = 0;
  this.facing = te.orientation;
  this.x = x;
  this.y = y;
  this.z = z;
  this.setTagFromTile(te);  
  }

public MachineRule(TEMachine te, int x, int y, int z, int meta)
  {
  this.type = meta;
  this.team = te.getTeamNum();
  this.machineType = 1;
  this.facing = te.getFacing().ordinal();
  this.x = x;
  this.y = y;
  this.z = z;
  this.setTagFromTile(te);
  }

public MachineRule(TEEngine te, int x, int y, int z, int meta)
  {
  this.type = meta;
  this.team = te.getTeamNum();
  this.machineType = 2;
  this.facing = te.getFacing().ordinal();
  this.x = x;
  this.y = y;
  this.z = z;
  this.setTagFromTile(te);
  }

public MachineRule(TEAWBlockReinforced te, int x, int y, int z, int meta)
  {
  this.type = meta;
  this.team = te.ownerTeam;
  this.machineType = 3;
  this.facing = 0;
  this.x = x;
  this.y = y;
  this.z = z;
  this.setTagFromTile(te);
  }

protected void setPosFromTile(TileEntity te)
  {
  this.x = te.xCoord;
  this.y = te.yCoord;
  this.z = te.zCoord;
  }

protected void setTagFromTile(TileEntity te)
  {
  te.writeToNBT(this.tileTag);
  }

/**
 * normalize the x,y,z internal coords of this rule to represent a north-faced structure
 * @param currentFacing
 * @param xSize structure xSize as north-facing
 * @param ySize structure zSize as north-facing
 */
public void normalizeForNorthFacing(int currentFacing, int xSize, int zSize)
  {
  BlockPosition pos = BlockTools.getNorthRotatedPosition(x,y,z, currentFacing, xSize, zSize);  
  x = pos.x;
  z = pos.z;    
  }

public void createBlock(World world, BlockPosition buildPos, ProcessedStructure struct, int facing)
  {
  BlockPosition target = BlockTools.getTranslatedPosition(buildPos, new BlockPosition(x-struct.xOffset,y-struct.verticalOffset, z-struct.zOffset), facing, new BlockPosition(struct.xSize, struct.ySize, struct.zSize));
  
  switch(machineType)
  {  
  case 0:
  createCraftingBlock(world, target, facing);
  break;
  
  case 1:
  createMachine(world, target, facing);
  break;
  
  case 2:
  createEngine(world, target, facing);
  break;
  
  case 3:
  createReinforcedBlock(world, target, facing);
  break;
  }
  }

protected void createCraftingBlock(World world, BlockPosition target, int facing)
  {
  world.setBlock(target.x, target.y, target.z, BlockLoader.crafting.blockID, type, 3);
  TEAWCrafting te = (TEAWCrafting) world.getBlockTileEntity(target.x, target.y, target.z);  
  }

protected void createMachine(World world, BlockPosition target, int facing)
  {
  world.setBlock(target.x, target.y, target.z, BlockLoader.machineBlock.blockID, type, 3);
  TEMachine te = (TEMachine) world.getBlockTileEntity(target.x, target.y, target.z);  
  }

protected void createEngine(World world, BlockPosition target, int facing)
  {
  world.setBlock(target.x, target.y, target.z, BlockLoader.engineBlock.blockID, type, 3);
  TEEngine te = (TEEngine) world.getBlockTileEntity(target.x, target.y, target.z);
  }

protected void createReinforcedBlock(World world, BlockPosition target, int facing)
  {
  world.setBlock(target.x, target.y, target.z, BlockLoader.reinforced.blockID, type, 3);
  TEAWBlockReinforced te = (TEAWBlockReinforced) world.getBlockTileEntity(target.x, target.y, target.z);
  }

}
