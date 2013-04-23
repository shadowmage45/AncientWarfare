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

import shadowmage.ancient_warfare.common.civics.worksite.te.mine.MineLevel.MineActionType;

public class MinePointEntry implements Comparable<MinePointEntry>
{

int x;
int y;
int z;
int order = 0;
MineActionType action = MineActionType.NONE;//original action as designated when scanned
MineActionType currentAction = MineActionType.NONE;//current action needed, as designated by worked on/rescanned

protected MinePointEntry(int x, int y, int z, int order, MineActionType type)
  {
  this.order = order;
  this.action = type;
  this.currentAction = type;
  }

@Override
public int compareTo(MinePointEntry o)
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
  if (!(obj instanceof MinePointEntry))
    return false;
  MinePointEntry other = (MinePointEntry) obj;  
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

}
