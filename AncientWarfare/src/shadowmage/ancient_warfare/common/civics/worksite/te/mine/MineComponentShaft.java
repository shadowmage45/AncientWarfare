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
package shadowmage.ancient_warfare.common.civics.worksite.te.mine;

import java.util.ArrayList;
import java.util.PriorityQueue;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class MineComponentShaft extends MineComponent
{ 

MineSubComponent shaft;

public MineComponentShaft()
  {
  this.maxWorkers = 1;
  this.shaft = new MineSubComponent(this);
  }

@Override
public MinePoint getWorkFor(NpcBase worker)
  {
  return shaft.getNextPoint();
  }

@Override
public boolean hasWork()
  {
  return shaft.hasWork();
  }

@Override
public int scanComponent(World world, int minX, int minY, int minZ, int xSize, int ySize, int zSize, int order, int shaftX, int shaftZ)
  {  
  MinePoint entry;
  for(int y = minY + ySize-1; y >= minY; y--, order++)//start at the top...
    {
    for(int x = shaftX; x<=shaftX+1; x++)
      {
      for(int z = shaftZ; z<=shaftZ+1; z++)
        {
        if(z==shaftZ)
          {
          order = this.addSouthShaft(world, x,y,z, order);
          //check shaftZ-1 for resources
          }
        else
          {
          order = this.addNorthShaft(world, x, y, z, order);
          //check shaftZ+2 for resources
          }        
        }
      }    
    }
  return order;
  }

protected int addNorthShaft(World world, int x, int y, int z, int order)
  {
  int id = world.getBlockId(x, y, z);
  int id2 = world.getBlockId(x, y, z+1);
  if(id!=Block.ladder.blockID)//not clear or ladder, set to clear
    {
    shaft.addNewPoint(x, y, z, order, TargetType.MINE_CLEAR, false);
    order++;
    }
  //scan N block, add clear/fill if needed
  if(isValidResource(id2))
    {
  //add north block clear
    shaft.addNewPoint(x, y, z+1, order, TargetType.MINE_CLEAR, false);    
    order++;
    //add north block fill
    shaft.addNewPoint(x, y, z+1, order, TargetType.MINE_FILL, true);
    order++;
    }  
  else if(needsFilled(id2))
    {
    shaft.addNewPoint(x, y, z+1, order, TargetType.MINE_FILL, true);
    order++;
    }
  if(id!=Block.ladder.blockID)
    {
    shaft.addNewPoint(x, y, z, order, (byte)2, TargetType.MINE_LADDER, true);
    order++;
    }
  return order;
  }

protected int addSouthShaft(World world, int x, int y, int z, int order)
  {
  int id = world.getBlockId(x, y, z);
  int id2 = world.getBlockId(x, y, z-1);
  if(id!=Block.ladder.blockID && id!=0)//not clear or ladder, set to clear
    {
    shaft.addNewPoint(x, y, z, order, TargetType.MINE_CLEAR, false);
    order++;
    }
  if(isValidResource(id2))
    {
  //add north block clear
    shaft.addNewPoint(x, y, z-1, order, TargetType.MINE_CLEAR, false);    
    order++;
    //add north block fill
    shaft.addNewPoint(x, y, z-1, order, TargetType.MINE_FILL, true);
    order++;
    }  
  else if(needsFilled(id2))
    {
    shaft.addNewPoint(x, y, z-1, order, TargetType.MINE_FILL, true);
    order++;
    }
  if(id!=Block.ladder.blockID)
    {
    shaft.addNewPoint(x, y, z, order, (byte)3, TargetType.MINE_LADDER, false);
    order++;
    }
  return order;
  }

@Override
public void onWorkFinished(NpcBase npc, MinePoint p)
  {
  this.shaft.onPointFinished(npc, p);
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setCompoundTag("shaft", shaft.getNBTTag());
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.shaft.readFromNBT(tag.getCompoundTag("shaft"));  
  }

@Override
public void onWorkFailed(NpcBase npc, MinePoint p)
  {
  shaft.onPointFinished(npc, p);
  }

@Override
public void verifyCompletedNodes(World world)
  {
  this.shaft.validatePoints(world);
  }

}
