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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.gates.EntityGate;
import shadowmage.ancient_warfare.common.gates.TEGateProxy;
import shadowmage.ancient_warfare.common.research.ResearchGoalNumbers;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

public class GateBasicWood extends Gate
{

/**
 * @param id
 */
public GateBasicWood(int id)
  {
  super(id);
  this.displayName = "item.gate.0";
  this.tooltip = "item.gate.0.tooltip";
  this.texture = "gateWood1.png";
  this.iconTexture = "gateWoodBasic";
  this.neededResearch.add(ResearchGoalNumbers.wood1);
  this.neededResearch.add(ResearchGoalNumbers.mechanics2);
  this.resourceStacks.add(new ItemStackWrapperCrafting(Block.stone, 10, false, false));
  this.resourceStacks.add(new ItemStackWrapperCrafting(Block.planks, 12));
  }


}
