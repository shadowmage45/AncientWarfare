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

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class MineComponentBranches extends MineComponent
{

ArrayList<MineSubComponent> branches = new ArrayList<MineSubComponent>();

@Override
public int scanComponent(World world, int minX, int minY, int minZ, int xSize, int ySize, int zSize, int order, int shaftX, int shaftZ)
  {
  this.branches.clear();
  int id = 0;
  MineSubComponent branch = null;
  for(int x = shaftX-1; x>=minX; x-=3)
    {
    branch = new MineSubComponent(this);
    for(int z = shaftZ+2; z <=minZ+zSize-1; z++)//do n/w side branches
      {
      for(int y = minY; y<= minY+1; y++)
        {
        order = this.addNodeToBranch(world, branch, x, y, z, order, y!=minY);
        }
      }
    branches.add(branch);
    branch = new MineSubComponent(this);
    for(int z = shaftZ-1; z >=minZ; z--)//do s/w side branches
      {
      for(int y = minY; y<= minY+1; y++)
        {
        order = this.addNodeToBranch(world, branch, x, y, z, order, y!=minY);
        }
      }
    branches.add(branch);
    }  
  for(int x = shaftX+2; x <= minX+xSize-1; x+=3)
    {
    branch = new MineSubComponent(this);
    for(int z = shaftZ+2; z <=minZ+zSize-1; z++)//do n/e side branches
      {
      for(int y = minY; y<= minY+1; y++)
        {
        order = this.addNodeToBranch(world, branch, x, y, z, order, y!=minY);
        }
      }
    branches.add(branch);
    branch = new MineSubComponent(this);
    for(int z = shaftZ-1; z >=minZ; z--)//do s/e side branches
      {
      for(int y = minY; y<= minY+1; y++)
        {
        order = this.addNodeToBranch(world, branch, x, y, z, order, y!=minY);
        }
      }
    branches.add(branch);
    }
  Config.logDebug("scanned branches..size: "+branches.size());
  return order;
  }

protected int addNodeToBranch(World world, MineSubComponent branch, int x, int y, int z, int order, boolean top)
  {
  int id1 = world.getBlockId(x, y, z);
  int id2 = top? world.getBlockId(x, y+1, z) : world.getBlockId(x, y-1, z);
  int id3 = world.getBlockId(x+1, y, z);
  int id4 = world.getBlockId(x-1, y, z);
  int y1 = top? y+1 : y-1; 
  boolean last = z%4!=0;
  if(id1!=0 && id1!=Block.torchWood.blockID)//current block needs cleared before can process sides/etc
    {
    branch.addNewPoint(x, y, z, order, TargetType.MINE_CLEAR, last);
    order++;
    }
  if(isValidResource(id2))
    {
    branch.addNewPoint(x, y1, z, order, TargetType.MINE_CLEAR, false);
    order++;
    branch.addNewPoint(x, y1, z, order, TargetType.MINE_FILL, true);
    order++;
    }
  else if(needsFilled(id2))
    {
    branch.addNewPoint(x, y1, z, order, TargetType.MINE_FILL, true);
    order++;
    }
  if(isValidResource(id3))
    {
    branch.addNewPoint(x+1, y, z, order, TargetType.MINE_CLEAR, false);
    order++;
    branch.addNewPoint(x+1, y, z, order, TargetType.MINE_FILL, true);
    order++;
    }
  else if(needsFilled(id3))
    {
    branch.addNewPoint(x+1, y, z, order, TargetType.MINE_FILL, true);
    order++;
    }
  if(isValidResource(id4))
    {
    branch.addNewPoint(x-1, y, z, order, TargetType.MINE_CLEAR, false);
    order++;
    branch.addNewPoint(x-1, y, z, order, TargetType.MINE_FILL, true);
    order++;
    }
  else if(needsFilled(id4))
    {
    branch.addNewPoint(x-1, y, z, order, TargetType.MINE_FILL, true);
    order++;
    }
  if(!top && z%4==0 && id1 != Block.torchWood.blockID)
    {
    branch.addNewPoint(x,y,z,order, TargetType.MINE_TORCH, true);
    order++;
    }
  return order;
  }

@Override
public MinePoint getWorkFor(NpcBase worker)
  {
  for(MineSubComponent b : this.branches)
    {
    if(b.hasWork() && (b.getWorker()==null || b.getWorker()==worker))
      {
      b.setWorker(worker);
      return b.getNextPoint();
      }
    }
  return null;
  }

@Override
public boolean hasWork()
  {
  boolean hasWork = false;
  for(MineSubComponent b : this.branches)
    {
    if(b.hasWork())
      {
      hasWork = true;
      break;
      }
    }
  return hasWork;
  }

@Override
public void onWorkFinished(NpcBase npc, MinePoint p)
  {
  for(MineSubComponent b : this.branches)
    {
    if(p.owner!=null && p.owner==b)
      {
      b.onPointFinished(npc, p);
      break;
      }
    }
  }

@Override
public void onWorkFailed(NpcBase npc, MinePoint p)
  {
  for(MineSubComponent b : this.branches)
    {
    if(p.owner !=null && p.owner==b)
      {
      b.onPointFailed(npc, p);
      }
    }
  }

@Override
public void verifyCompletedNodes(World world)
  {
  for(MineSubComponent comp : this.branches)
    {
    comp.validatePoints(world);
    }
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList list = new NBTTagList();
  for(MineSubComponent comp : this.branches)
    {
    list.appendTag(comp.getNBTTag());
    }
  tag.setTag("list", list);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  NBTTagList list = tag.getTagList("list");
  for(int i = 0; i < list.tagCount(); i++)
    {
    MineSubComponent sub = new MineSubComponent(this);
    sub.parent = this;
    sub.readFromNBT((NBTTagCompound) list.tagAt(i));
    branches.add(sub);
    }
  }



}
