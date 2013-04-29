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
package shadowmage.ancient_warfare.common.civics.worksite.te.tree;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class WorkPointTree extends WorkPoint
{

public TreePoint tp;
/**
 * @param x
 * @param y
 * @param z
 * @param type
 * @param owner
 */
public WorkPointTree(int x, int y, int z, TargetType type, TECivic owner, TreePoint tp)
  {
  super(x, y, z, type, owner);
  this.tp = tp;  
  }

/**
 * @param x
 * @param y
 * @param z
 * @param side
 * @param type
 * @param owner
 */
public WorkPointTree(int x, int y, int z, int side, TargetType type, TECivic owner, TreePoint tp)
  {
  super(x, y, z, side, type, owner);
  this.tp = tp;  
  }

/**
 * @param compoundTag
 */
public WorkPointTree(NBTTagCompound compoundTag)
  {
  super(compoundTag);
  }

}
