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
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_framework.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_warfare.common.gates.EntityGate;
import shadowmage.ancient_warfare.common.research.ResearchGoalNumbers;

public class GateSingleIron extends Gate
{

/**
 * @param id
 */
public GateSingleIron(int id)
  {
  super(id);
  this.displayName = "item.gate.5";
  this.tooltip = "item.gate.5.tooltip";
  this.texture = "gateIron1.png";
  this.modelType = 1;
  this.iconTexture = "gateIronSingle";
  this.neededResearch.add(ResearchGoalNumbers.iron1);
  this.neededResearch.add(ResearchGoalNumbers.mechanics2);
  this.resourceStacks.add(new ItemStackWrapperCrafting(Block.stone, 10, false, false));
  this.resourceStacks.add(new ItemStackWrapperCrafting(Item.ingotIron, 12, false, false));
  }

@Override
public void setInitialBounds(EntityGate gate, BlockPosition pos1,   BlockPosition pos2)
  {
  BlockPosition min = BlockTools.getMin(pos1, pos2);
  BlockPosition max = BlockTools.getMax(pos1, pos2);
  boolean wideOnXAxis = min.x!=max.x;
  float width = wideOnXAxis ? max.x-min.x+1 : max.z-min.z + 1;
  float xOffset = wideOnXAxis ? width*0.5f: 0.5f;
  float zOffset = wideOnXAxis ? 0.5f : width*0.5f;
  gate.pos1 = pos1;
  gate.pos2 = pos2;
  gate.edgeMax = width;
  gate.setPosition(min.x+xOffset, min.y, min.z+zOffset);  
  }

}
