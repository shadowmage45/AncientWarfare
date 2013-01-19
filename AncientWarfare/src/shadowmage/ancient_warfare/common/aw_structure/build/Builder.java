/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public Licence.
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
package shadowmage.ancient_warfare.common.aw_structure.build;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_core.utils.INBTTaggable;
import shadowmage.ancient_warfare.common.aw_structure.data.BlockData;
import shadowmage.ancient_warfare.common.aw_structure.data.BlockDataManager;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.aw_structure.data.rules.BlockRule;

public abstract class Builder implements INBTTaggable
{
public final ProcessedStructure struct;
public final World world;

/**
 * build passes
 */
int currentPriority = 0;
int maxPriority;

public int facing = 0;
public int currentX = 0;
public int currentY = 0; 
public int currentZ = 0;
public BlockPosition buildPos;
protected boolean isFinished = false;
public int dimension = 0;

public Builder(World world, ProcessedStructure struct, int facing, BlockPosition hit)
  {
  this.struct = struct;
  this.world = world; 
  this.dimension = world.getWorldInfo().getDimension();
  this.buildPos = hit;  
  this.facing = facing;
  for(BlockRule rule : struct.blockRules)
    {
    if(rule.order>this.maxPriority)
      {
      this.maxPriority = rule.order;
      }
    }
  }

/**
 * for instantBuilder--construct
 * for tickedBuilder--add to tickQue in AWStructureModule
 */
public abstract void startConstruction();

/**
 * for instantBuilder--NOOP
 * for tickedBuilder--self-remove from buildQue
 */
public abstract void finishConstruction();

/**
 * for instantBuilder--NOOP
 * for tickedBuilder--place singleBlock or increment timer between blocks
 */
public abstract void onTick();


public boolean isFinished()
  {
  return isFinished;
  }

public void setFinished()
  {
  this.isFinished = true;
  }

protected void placeBlock(World world, BlockPosition pos, int id, int meta)
  {
  if(id==Block.doorSteel.blockID || id==Block.doorWood.blockID)
    {
    if(meta==8)
      {
      return;
      }
    else
      {
      world.setBlockAndMetadata(pos.x, pos.y+1, pos.z, id, 8);
      }
    }
  world.setBlockAndMetadata(pos.x, pos.y, pos.z, id, meta);
  }

protected void placeBlockNotify(World world, BlockPosition pos, int id, int meta)
  {
//  if(id==Block.doorSteel.blockID || id==Block.doorWood.blockID)
//    {
//    if(meta==8)
//      {
//      return;
//      }
//    else
//      {
//      ItemDoor.placeDoorBlock(world, pos.x, pos.y, pos.z, 0, Block.blocksList[id]);
//      world.setBlockAndMetadataWithNotify(pos.x, pos.y+1, pos.z, id, 8);
//      }
//    }
  world.setBlockAndMetadataWithNotify(pos.x, pos.y, pos.z, id, meta);
  }


/**
 * get rotation amount relative to default facing of 2
 * @param facing
 * @return
 */
protected int getRotationAmt(int facing)
  {
  if(facing==2)
    {
    return 0;
    }
  else if(facing==3)
    {
    return 1;
    }
  else if(facing==0)
    {
    return 2;
    }
  else if(facing==1)
    {
    return 3;
    }
  return 0;
  }


/**
 * return true if could increment coords OR build pass
 * @return
 */
protected boolean tryIncrementing()
  {  
  if(!this.incrementCoords())
    {
    if(!this.incrementBuildPass())
      {      
      return false;
      }    
    }
  return true;
  }

/**
 * return true if can continue (currentPriority++ is valid priority) 
 * @return
 */
protected boolean incrementBuildPass()
  {
  this.currentX = 0;
  this.currentY = 0;
  this.currentZ = 0;
  this.currentPriority++;
  if(this.currentPriority>this.maxPriority)
    {
    return false;
    }
  return true;
  }

/**
 * return true if can continue (increment was to a valid coord)
 * @return
 */
protected boolean incrementCoords()
  {
  if(this.currentX+1 < this.struct.xSize )
    {
    this.currentX++;
    return true;
    }
  if(this.currentZ+1 < this.struct.zSize)
    {
    this.currentZ++;
    this.currentX = 0;
    return true;
    }
  if(this.currentY+1 < this.struct.ySize)
    {
    this.currentY++;
    this.currentX = 0;
    this.currentZ = 0;
    return true;
    }
  return false;
  }

public void setProgress(int x, int y, int z, int pass)
  {
  this.currentX = x;
  this.currentY = y;
  this.currentZ = z;
  this.currentPriority = pass;
  }

public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setBoolean("finished", isFinished);
  tag.setByte("face", (byte)facing);
  tag.setInteger("x", currentX);
  tag.setInteger("y", currentY);
  tag.setInteger("z", currentZ);
  tag.setInteger("dim", this.world.getWorldInfo().getDimension());
  tag.setInteger("bX", this.buildPos.x);
  tag.setInteger("bY", this.buildPos.y);
  tag.setInteger("bZ", this.buildPos.z);
  return tag;
  }

public void readFromNBT(NBTTagCompound tag)
  {
  this.isFinished = tag.getBoolean("finished");
  this.facing = tag.getByte("face");
  this.currentX = tag.getInteger("x");
  this.currentY = tag.getInteger("y");
  this.currentZ = tag.getInteger("z");
  this.dimension = tag.getInteger("dim");
  this.buildPos = new BlockPosition(tag.getInteger("bX"), tag.getInteger("bY"), tag.getInteger("bZ"));  
  }

}
