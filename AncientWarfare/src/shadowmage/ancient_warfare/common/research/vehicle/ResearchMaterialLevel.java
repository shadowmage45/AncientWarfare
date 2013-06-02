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
package shadowmage.ancient_warfare.common.research.vehicle;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.vehicles.materials.VehicleMaterial;

public class ResearchMaterialLevel extends ResearchGoal
{

int level;
/**
 * @param num
 */
public ResearchMaterialLevel(int num, int level, VehicleMaterial material)
  {
  super(num);
  this.level = level;
  this.displayName = "Material: "+material.getDisplayName(level);
  this.displayTooltip = "Enables use of higher ranked materials";
  this.detailedDescription.add("Researching higher ranks of vehicle construction" +
  		" materials will enable the construction of those materials, and allow for" +
  		" the use of those materials in the construction of vehicles (vehicle and" +
  		" proper tier for vehicle must also be unlocked).");
  this.researchTime = 1200*(level+1);
  this.addResource(new ItemStack(Item.paper, (level+1)*2), false, false);
  this.addResource(new ItemStack(Item.dyePowder, level+1, 0), false, false);
  this.addResource(new ItemStack(Block.torchWood, level+1), false, false);
  }

}
