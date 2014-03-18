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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.block.TEAWBlockReinforced;
import shadowmage.ancient_warfare.common.crafting.TEAWCrafting;
import shadowmage.ancient_warfare.common.machine.TEEngine;
import shadowmage.ancient_warfare.common.machine.TEMachine;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.NBTReader;
import shadowmage.ancient_warfare.common.utils.NBTWriter;
import shadowmage.ancient_warfare.common.utils.StringTools;

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
int blockMeta;

/**
 * the facing direction, if applicable, for the machine, relative to north facing
 */
ForgeDirection facing;

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
  this.blockMeta = meta;
  this.team = te.teamNum;
  this.machineType = 0;
  this.facing = BlockTools.getForgeDirectionFromCardinal(te.orientation);
  this.x = x;
  this.y = y;
  this.z = z;
  this.setTagFromTile(te);  
  }

public MachineRule(TEMachine te, int x, int y, int z, int meta)
  {
  this.blockMeta = meta;
  this.team = te.getTeamNum();
  this.machineType = 1;
  this.facing = te.getFacing();
  this.x = x;
  this.y = y;
  this.z = z;
  this.setTagFromTile(te);
  }

public MachineRule(TEEngine te, int x, int y, int z, int meta)
  {
  this.blockMeta = meta;
  this.team = te.getTeamNum();
  this.machineType = 2;
  this.facing = te.getFacing();
  this.x = x;
  this.y = y;
  this.z = z;
  this.setTagFromTile(te);
  }

public MachineRule(TEAWBlockReinforced te, int x, int y, int z, int meta)
  {
  this.blockMeta = meta;
  this.team = te.ownerTeam;
  this.machineType = 3;
  this.facing = ForgeDirection.NORTH;
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
  facing = BlockTools.rotateRight(facing, BlockTools.getRotationAmt(currentFacing));
  }

public void createBlock(World world, BlockPosition buildPos, ProcessedStructure struct, int facing)
  {
  BlockPosition target = BlockTools.getTranslatedPosition(buildPos, new BlockPosition(x-struct.xOffset,y-struct.verticalOffset, z-struct.zOffset), facing, new BlockPosition(struct.xSize, struct.ySize, struct.zSize));
  int rotation = BlockTools.getRotationAmount(2, facing);
  ForgeDirection face = BlockTools.rotateRight(this.facing, rotation);
  tileTag.setInteger("x", target.x);
  tileTag.setInteger("y", target.y);
  tileTag.setInteger("z", target.z);
  switch(machineType)
  {  
  case 0:
  createCraftingBlock(world, target, face);
  break;
  
  case 1:
  createMachine(world, target, face);
  break;
  
  case 2:
  createEngine(world, target, face);
  break;
  
  case 3:
  createReinforcedBlock(world, target, face);
  break;
  }
  world.markBlockForUpdate(target.x, target.y, target.z);
  }

protected void createCraftingBlock(World world, BlockPosition target, ForgeDirection facing)
  {
  world.setBlock(target.x, target.y, target.z, BlockLoader.crafting.blockID, blockMeta, 3);
  TEAWCrafting te = (TEAWCrafting) world.getBlockTileEntity(target.x, target.y, target.z);  
  te.readFromNBT(tileTag);
  te.orientation = BlockTools.getCardinalFromSide(facing);
  }

protected void createMachine(World world, BlockPosition target, ForgeDirection facing)
  {
  world.setBlock(target.x, target.y, target.z, BlockLoader.machineBlock.blockID, blockMeta, 3);
  TEMachine te = (TEMachine) world.getBlockTileEntity(target.x, target.y, target.z);
  te.readFromNBT(tileTag);
  te.setDirection(facing);
  }

protected void createEngine(World world, BlockPosition target, ForgeDirection facing)
  {
  world.setBlock(target.x, target.y, target.z, BlockLoader.engineBlock.blockID, blockMeta, 3);
  TEEngine te = (TEEngine) world.getBlockTileEntity(target.x, target.y, target.z);
  te.readFromNBT(tileTag);
  te.setDirection(facing);
  }

protected void createReinforcedBlock(World world, BlockPosition target, ForgeDirection facing)
  {
  world.setBlock(target.x, target.y, target.z, BlockLoader.reinforced.blockID, blockMeta, 3);
  TEAWBlockReinforced te = (TEAWBlockReinforced) world.getBlockTileEntity(target.x, target.y, target.z);
  te.readFromNBT(tileTag);
  //NOOP facing  
  }

public List<String> getRuleLines()
  {
  List<String> lines = new ArrayList<String>();  
  lines.add("machine:");
  lines.add("type="+machineType);
  lines.add("meta="+blockMeta);
  lines.add("facing="+facing.ordinal());
  lines.add("position="+StringTools.getCSVStringForArray(new int[]{x,y,z}));
  if(tileTag!=null)
    {
    lines.add("data:");
    lines.addAll(NBTWriter.writeNBTToStrings(tileTag));
    lines.add(":enddata");    
    }
  lines.add(":endmachine");  
  return lines;
  }

public static MachineRule parseLines(List<String> lines)
  {
  MachineRule rule = new MachineRule();
  String line;
  Iterator<String> it = lines.iterator();
  boolean valid = true;
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("machine:") || line.toLowerCase().startsWith(":endmachine"))
      {
      continue;
      }
    else if(line.toLowerCase().startsWith("type"))
      {
      rule.machineType = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("meta"))
      {
      rule.blockMeta = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("facing"))
      {
      rule.facing = ForgeDirection.getOrientation(StringTools.safeParseInt("=", line));
      }
    else if(line.toLowerCase().startsWith("position"))
      {
      int[] pos = StringTools.safeParseIntArray("=", line);
      if(pos==null || pos.length<3)
        {
        valid = false;
        }
      else
        {
        rule.x = pos[0];
        rule.y = pos[1];
        rule.z = pos[2];
        }
      }
    else if(line.toLowerCase().startsWith("data:"))
      {   
      ArrayList<String>dataLines = new ArrayList<String>();
      while(it.hasNext())
        {
        line = it.next();   
        if(line.toLowerCase().startsWith(":enddata"))
          {
          NBTTagCompound tileTag = NBTReader.readTagFromLines(dataLines);
          rule.tileTag = tileTag;
          break;
          }
        dataLines.add(line);
        }
      }
    }
  
  
  if(valid)
    {
    return rule;
    }
  return null;
  }

}
