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

import net.minecraft.block.Block;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.targeting.TargetType;

public class WorkPointFarm extends WorkPoint
{

int plantID;
int plantFullGrownMeta;
int tilledEarthID = Block.tilledField.blockID;

/**
 * @param x
 * @param y
 * @param z
 * @param type
 */
public WorkPointFarm(int x, int y, int z, TargetType type, int plantID, int plantMeta)
  {
  super(x, y, z, type);
  this.plantID = plantID;
  this.plantFullGrownMeta = plantMeta;
  }

@Override
public boolean hasWork(World world)
  {
  if(this.type == TargetType.FARM_PLANT)
    {
    if(world.getBlockId(x, y, z)==tilledEarthID && world.getBlockId(x, y+1, z)==0)
      {
      return true;  
      }
    }
  else if(this.type==TargetType.FARM_HARVEST && world.getBlockId(x, y-1, z)==tilledEarthID && world.getBlockId(x, y, z)==plantID)
    {
    if(world.getBlockMetadata(x, y, z)==this.plantFullGrownMeta)
      {
      return true;
      }
    } 
  return false;
  }

public boolean isValidEntry(World world)
  {
  if(this.type == TargetType.FARM_PLANT)
    {
    if(world.getBlockId(x, y, z)==tilledEarthID)
      {
      return true;
      }
    }
  else if(this.type==TargetType.FARM_HARVEST && world.getBlockId(x, y-1, z)==tilledEarthID)
    {
    return true;
    } 
  return false;
  }

}
