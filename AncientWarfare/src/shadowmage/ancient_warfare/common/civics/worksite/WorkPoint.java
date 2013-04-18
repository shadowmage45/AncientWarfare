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
package shadowmage.ancient_warfare.common.civics.worksite;

import net.minecraft.entity.Entity;
import shadowmage.ancient_warfare.common.utils.TargetType;

public class WorkPoint
{

TargetType type;
protected Entity ent;
protected int x;
protected int y; 
protected int z;

public WorkPoint(int x, int y, int z, TargetType type)
  {
  this.x = x;
  this.y = y;
  this.z = z;
  this.type = type;
  }

public WorkPoint(Entity ent, TargetType type)
  {
  this.ent = ent;
  this.type = type;
  }

public float posX()
  {
  return (float) (this.ent!=null ? ent.posX : x);
  }

public float posY()
  {
  return (float) (this.ent!=null ? ent.posY : y);
  }

public float posZ()
  {
  return (float) (this.ent!=null ? ent.posZ : z);
  }

public boolean isEqual(WorkPoint b)
  {
  return type==b.type && ((ent!=null && ent==b.ent) || (x==b.x && y==b.y && z==b.z));
  }

}
