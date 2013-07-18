/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.gates.types;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import shadowmage.ancient_warfare.common.research.ResearchGoalNumbers;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

public class GateBasicIron extends Gate
{

/**
 * @param id
 */
public GateBasicIron(int id)
  {
  super(id);
  this.modelType = 1; 
  this.displayName = "item.gate.1";
  this.tooltip = "item.gate.1.tooltip";
  this.texture = "gateIron1.png";
  this.iconTexture = "gateIronBasic";
  this.neededResearch.add(ResearchGoalNumbers.iron1);
  this.neededResearch.add(ResearchGoalNumbers.mechanics2);
  this.resourceStacks.add(new ItemStackWrapperCrafting(Block.stone, 10, false, false));
  this.resourceStacks.add(new ItemStackWrapperCrafting(Item.ingotIron, 12, false, false));
  }

}
