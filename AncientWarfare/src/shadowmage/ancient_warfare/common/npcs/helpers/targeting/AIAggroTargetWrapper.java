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
package shadowmage.ancient_warfare.common.npcs.helpers.targeting;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.interfaces.ITargetEntry;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class AIAggroTargetWrapper
{

public int aggroLevel;
protected ITargetEntry target;

public AIAggroTargetWrapper(ITargetEntry target)
  {
  this.target = target;
  }

public ITargetEntry getTarget()
  {
  return this.target;
  }

public boolean matches(Entity ent)
  {
  return ent!=null ? this.target.getEntity(ent.worldObj)==ent : false;
  }

public boolean matches(TileEntity te)
  {
  return te!=null ? this.target.getTileEntity(te.worldObj)==te : false;
  }

public AIAggroTargetWrapper setAggro(int level)
  {
  this.aggroLevel = level;
  return this;      
  }

public boolean matches(int x, int y, int z)
  {
  return this.target.floorX()==x && this.target.floorY()==y && this.target.floorZ()==z;
  }

public boolean isValidEntry()
  {
  return true;
  }

@Override
public String toString()
  {
  return "TargetWrapper: "+this.target;
  }

}
