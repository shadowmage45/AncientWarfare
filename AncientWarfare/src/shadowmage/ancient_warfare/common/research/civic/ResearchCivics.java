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
package shadowmage.ancient_warfare.common.research.civic;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.research.ResearchGoal;

public class ResearchCivics extends ResearchGoal
{

/**
 * @param num
 */
public ResearchCivics(int num, int level)
  {
  super(num);
  this.displayName = "Civilian Engineering " + (level + 1);
  this.researchTime = 900 * (level+1);
  this.detailedDescription.add("Researching Civilian Engineering advances knowledge regarding" +
  		" the design and construction of civic buildings and worksites.  Knowlege of civilian" +
  		" engineering is necessary for the construction of civic buildings and recruiting of" +
  		" civilian workers.");
  this.addResource(new ItemStack(Item.silk, level+1), false, false);  
  this.addResource(new ItemStack(Item.stick, level+1), false, false);
  this.addResource(new ItemStack(Item.paper, (level+1)*2), false, false);
  this.addResource(new ItemStack(Item.dyePowder, level+1, 0), false, false);
  this.addResource(new ItemStack(Block.torchWood, level+1), false, false);
  }

}
