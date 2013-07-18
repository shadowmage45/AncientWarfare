/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.civics.worksite.te.mine;

import java.util.ArrayList;
import java.util.PriorityQueue;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class MineComponentTunnels extends MineComponent
{

MineSubComponent leftTunnel;
MineSubComponent rightTunnel;

public MineComponentTunnels()
  {
  this.maxWorkers = 2;
  this.leftTunnel = new MineSubComponent(this);
  this.rightTunnel = new MineSubComponent(this);
  }

@Override
public int scanComponent(World world, int minX, int minY, int minZ, int xSize,  int ySize, int zSize, int order, int shaftX, int shaftZ)
  {
  int id = 0;
  for(int x = shaftX-1; x>= minX ; x--)//add west tunnel
    {
    for(int z = shaftZ; z<= shaftZ+1; z++)      
      {
      for(int y = minY; y<= minY+1; y++)
        {
        if(y==minY)
          {
          //add left/bottom
          order = this.addTunnelPiece(world, x, y, z, order, true, false);
          }
        else
          {
          order = this.addTunnelPiece(world, x, y, z, order, true, true);
          //add left/top
          }
        }
      }
    }
//  order = tunnelStartOrder;
  for(int x = shaftX+2; x <= minX + xSize-1; x++)//add west tunnel
    {
    for(int z = shaftZ; z<= shaftZ+1; z++)      
      {
      for(int y = minY; y<= minY+1; y++)
        {
        if(y==minY)
          {
          //add right/bottom
          order = this.addTunnelPiece(world, x, y, z, order, false, false);
          }
        else
          {
          order = this.addTunnelPiece(world, x, y, z, order, false, true);
          //add right/top
          }
        }
      }
    }
  return order;
  }

protected int addTunnelPiece(World world, int x, int y, int z, int order, boolean left, boolean top)
  {  
  int id1 = world.getBlockId(x, y, z);
  boolean addTorch = !top && x%4==0;
  if(id1!=0 && id1 != Block.torchWood.blockID)
    {
    if(left)
      {
      leftTunnel.addNewPoint(x, y, z, order, TargetType.MINE_CLEAR, !addTorch);      
      }
    else
      {
      rightTunnel.addNewPoint(x, y, z, order, TargetType.MINE_CLEAR, !addTorch);
      }
    order++;
    }
  int id = top? world.getBlockId(x, y+1, z) : world.getBlockId(x, y-1, z);
  int y1 = top? y+1 : y-1;
  if(isValidResource(id))
    {
    if(left)
      {
      leftTunnel.addNewPoint(x, y1, z, order, TargetType.MINE_CLEAR, false);      
      }
    else
      {
      rightTunnel.addNewPoint(x, y1, z, order, TargetType.MINE_CLEAR, false);
      }
    order++;
    if(left)
      {
      leftTunnel.addNewPoint(x, y1, z, order, TargetType.MINE_FILL, true);      
      }
    else
      {
      rightTunnel.addNewPoint(x, y1, z, order, TargetType.MINE_FILL, true);
      }
    order++;
    }
  else if(needsFilled(id))
    {
    if(left)
      {
      leftTunnel.addNewPoint(x, y1, z, order, TargetType.MINE_FILL, true);      
      }
    else
      {
      rightTunnel.addNewPoint(x, y1, z, order, TargetType.MINE_FILL, true);
      }
    order++;
    }  
  if(addTorch && id1!= Block.torchWood.blockID)
    {
    if(left)
      {
      leftTunnel.addNewPoint(x, y, z, order, TargetType.MINE_TORCH, true);      
      }
    else
      {
      rightTunnel.addNewPoint(x, y, z, order, TargetType.MINE_TORCH, true);
      }
    order++;
    }
  return order;
  }

@Override
public MinePoint getWorkFor(NpcBase worker)
  {
  if(leftTunnel.hasWork() && (leftTunnel.getWorker()==worker || leftTunnel.getWorker()==null))
    {
    leftTunnel.setWorker(worker);
    return leftTunnel.getNextPoint();
    }
  else if(rightTunnel.hasWork() && (rightTunnel.getWorker()==worker || rightTunnel.getWorker()==null))
    {
    rightTunnel.setWorker(worker);
    return rightTunnel.getNextPoint();
    }
  return null;
  }

@Override
public boolean hasWork()
  {
  return leftTunnel.hasWork() || rightTunnel.hasWork();
  }

@Override
public void onWorkFinished(NpcBase npc, MinePoint p)
  {
  if(p.owner==leftTunnel)//left tunnel
    {
    leftTunnel.onPointFinished(npc, p);
    if(!leftTunnel.hasWork())
      {
      leftTunnel.setWorker(null);
      }
    }
  else
    {
    rightTunnel.onPointFinished(npc, p);
    if(!rightTunnel.hasWork())
      {
      rightTunnel.setWorker(null);
      }
    }
  }

@Override
public void onWorkFailed(NpcBase npc, MinePoint p)
  {
  if(p.owner==leftTunnel)//left tunnel
    {
    leftTunnel.onPointFailed(npc, p);
    leftTunnel.setWorker(null);
    }
  else
    {
    rightTunnel.onPointFailed(npc, p);
    rightTunnel.setWorker(null);
    }
  }


@Override
public void verifyCompletedNodes(World world)
  {
  leftTunnel.validatePoints(world);
  if(leftTunnel.getWorker()!=null)
    {
    //verify that workers work point belongs to this tunnel
    }
  rightTunnel.validatePoints(world);
  if(rightTunnel.getWorker()!=null)
    {
  //verify that workers work point belongs to this tunnel
    }
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setCompoundTag("left", leftTunnel.getNBTTag());
  tag.setCompoundTag("right", rightTunnel.getNBTTag());
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.leftTunnel.readFromNBT(tag.getCompoundTag("left"));
  this.rightTunnel.readFromNBT(tag.getCompoundTag("right"));
  }


}
