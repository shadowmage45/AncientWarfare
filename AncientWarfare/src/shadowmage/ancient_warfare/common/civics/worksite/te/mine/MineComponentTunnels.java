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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.npcs.NpcBase;

public class MineComponentTunnels extends MineComponent
{

MineSubComponent leftTunnel = new MineSubComponent();
MineSubComponent rightTunnel = new MineSubComponent();

@Override
public int scanComponent(World world, int minX, int minY, int minZ, int xSize,  int ySize, int zSize, int order, int shaftX, int shaftZ)
  {
  return 0;
  }

@Override
public MinePoint getWorkFor(NpcBase worker)
  {
  return null;
  }

@Override
public boolean isComponentFinished()
  {
  return !leftTunnel.hasWork() && !rightTunnel.hasWork();
  }

@Override
public void onWorkFinished(NpcBase npc, MinePoint p)
  {
  
  }

@Override
public void onWorkFailed(NpcBase npc, MinePoint p)
  {
  
  }


@Override
public void verifyCompletedNodes(World world)
  {
  leftTunnel.validatePoints(world);
  rightTunnel.validatePoints(world);
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
