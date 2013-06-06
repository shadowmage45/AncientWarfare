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
package shadowmage.ancient_warfare.common.civics.types;

import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.crafting.TEAWVehicleCraft;

public class CivicSiegeWorkshop extends Civic
{

/**
 * @param id
 */
public CivicSiegeWorkshop(int id, int size)
  {
  super(id);
  this.blockType = BlockLoader.crafting;
  this.teClass = TEAWVehicleCraft.class;
  this.blockMeta = 3;
  switch(size)
  {
  case 0:
  this.name = "Small Siege Engine Workshop";
  this.minSize1 = 5;
  this.minSize2 = 5;
  this.minHeight = 5;
  this.workSizeMaxHeight =5;
  this.workSizeMaxHorizontal = 5;
  break;
  
  case 1:
  this.name = "Medium Siege Engine Workshop";
  this.minSize1 = 9;
  this.minSize2 = 9;
  this.minHeight = 9;
  this.workSizeMaxHeight =9;
  this.workSizeMaxHorizontal = 9;
  break;
  }
  }

}
