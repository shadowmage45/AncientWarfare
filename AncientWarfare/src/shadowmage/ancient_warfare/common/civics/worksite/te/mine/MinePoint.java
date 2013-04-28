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

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class MinePoint implements Comparable<MinePoint>, INBTTaggable
{

int x;
int y;
int z;
int order = 0;
byte special;//used for meta...
boolean singleAction = true;
MineSubComponent owner;

TargetType action = TargetType.NONE;

protected MinePoint(int x, int y, int z, int order, TargetType type)
  {
  this.x = x;
  this.y = y;
  this.z = z;
  this.order = order;
  this.action = type;
  }

protected MinePoint(int x, int y, int z, int order, byte special, TargetType type)
  {
  this(x,y,z, order, type);
  this.special = special;
  }

protected MinePoint setOwner(MineSubComponent owner)
  {
  this.owner = owner;
  return this;
  }

protected MinePoint setSingleAction(boolean action)
  {
  this.singleAction = action;
  return this;
  }

/**
 * @param tag
 */
public MinePoint(NBTTagCompound tag)
  {
  this.readFromNBT(tag);
  }

public boolean hasWork(World world)
  {
  //TODO set this up to swtich by action type, determine if has work.
  //should only be called on initial setup and 'finished list validation'
  //replaces a big mess of logic in minelevel with a simple 'hasWork' call
  switch(action)
  {
  case MINE_LADDER:
  return world.getBlockId(x, y, z)!=Block.ladder.blockID;
  case MINE_TORCH:
  return world.getBlockId(x, y, z)!=Block.torchWood.blockID;
  case MINE_FILL:
  int id = world.getBlockId(x, y, z);
  return id==0 || id==Block.waterMoving.blockID || id==Block.waterStill.blockID || id == Block.lavaMoving.blockID || id==Block.lavaStill.blockID;
  case MINE_CLEAR:
  return world.getBlockId(x, y, z)!=0;
  }
  return false;
  }

@Override
public int compareTo(MinePoint o)
  {
  if(o==null)
    {
    return -1;
    }
  if(order<o.order)
    {
    return -1;
    }
  if(order>o.order)
    {
    return 1;
    }
  return 0;
  }

@Override
public int hashCode()
  {
  final int prime = 31;
  int result = 1;
  result = prime * result + order;
  result = prime * result + x;
  result = prime * result + y;
  result = prime * result + z;
  return result;
  }

@Override
public boolean equals(Object obj)
  {
  if (this == obj)
    return true;
  if (obj == null)
    return false;
  if (!(obj instanceof MinePoint))
    return false;
  MinePoint other = (MinePoint) obj;  
  if (order != other.order)
    return false;
  if (x != other.x)
    return false;
  if (y != other.y)
    return false;
  if (z != other.z)
    return false;
  return true;
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("x", x);
  tag.setInteger("y", y);
  tag.setInteger("z", z);
  tag.setInteger("act", this.action.ordinal());
  tag.setByte("sp", special);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.x = tag.getInteger("x");
  this.y = tag.getInteger("y");
  this.z = tag.getInteger("z");
  this.special = tag.getByte("sp");
  this.action = TargetType.values()[tag.getInteger("act")];
  }

@Override
public String toString()
  {
  return String.format("MinePoint: %d, %d, %d, : %s", x,y,z, action);
  }

}
