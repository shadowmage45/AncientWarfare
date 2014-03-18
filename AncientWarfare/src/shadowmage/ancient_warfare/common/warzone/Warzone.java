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
package shadowmage.ancient_warfare.common.warzone;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class Warzone
{

public BlockPosition min;
public BlockPosition max;

public Warzone()
  {
  min = new BlockPosition();
  max = new BlockPosition();
  }

public Warzone(BlockPosition p1, BlockPosition p2)
  {
  min = BlockTools.getMin(p1, p2);
  max = BlockTools.getMax(p1, p2);
  }

public boolean isPositionInZone(int x, int y, int z)
  {
  if(x<min.x || y<min.y || z<min.z || x>max.x || z>max.z || y>max.y)
    {
    return false;
    }
  return true;
  }

public void readFromNBT(NBTTagCompound tag)
  {
  min.read(tag.getCompoundTag("min"));
  max.read(tag.getCompoundTag("max"));
  }

public void writeToNBT(NBTTagCompound tag)
  {
  NBTTagCompound posTag = new NBTTagCompound();
  min.writeToNBT(posTag);
  tag.setTag("min", posTag);
  posTag = new NBTTagCompound();
  max.writeToNBT(posTag);
  tag.setTag("max", posTag);
  }

public boolean matches(BlockPosition min, BlockPosition max)
  {
  return min.equals(this.min) && max.equals(this.max);
  }

@Override
public String toString()
  {
  return String.format("WZ: %s -> %s", min, max);
  }
}
