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
package shadowmage.ancient_warfare.common.vehicles.armors;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.research.ResearchGoalNumbers;

public class VehicleArmorStone extends VehicleArmorBase
{

/**
 * @param armorType
 */
public VehicleArmorStone(int armorType)
  {
  super(armorType);
  this.displayName = "Stone Armor Tier 1";
  this.tooltip = "Fire damage - 7%";
  this.general = 2.5f;
  this.explosive = 2.5f;
  this.fire = 7;
  this.iconTexture = "armorStone1";
  this.neededResearch.add(ResearchGoalNumbers.iron3);
  this.addNeededResource(new ItemStack(Block.stone, 3), false);
  this.addNeededResource(new ItemStack(Item.ingotIron, 2), false);
  }


}
