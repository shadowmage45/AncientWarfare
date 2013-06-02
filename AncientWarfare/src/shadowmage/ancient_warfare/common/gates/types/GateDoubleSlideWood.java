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
package shadowmage.ancient_warfare.common.gates.types;

import net.minecraft.block.Block;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.gates.EntityGate;
import shadowmage.ancient_warfare.common.research.ResearchGoalNumbers;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

public class GateDoubleSlideWood extends Gate
{

/**
 * @param id
 */
public GateDoubleSlideWood(int id)
  {
  super(id); 
  this.displayName = "Gate Double Wood";
  this.tooltip = "Opens towards both sides";
  this.texture = "gateWood1.png";
  this.iconTexture = "gateWoodDouble";
  this.neededResearch.add(ResearchGoalNumbers.wood1);
  this.neededResearch.add(ResearchGoalNumbers.mechanics3);
  this.resourceStacks.add(new ItemStackWrapperCrafting(Block.stone, 10, false, false));
  this.resourceStacks.add(new ItemStackWrapperCrafting(Block.planks, 12));
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
  gate.edgeMax = width * 0.5f;
  gate.setPosition(min.x+xOffset, min.y, min.z+zOffset);  
  }


}
