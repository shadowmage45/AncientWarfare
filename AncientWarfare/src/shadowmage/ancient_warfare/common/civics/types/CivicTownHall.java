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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.research.ResearchGoalNumbers;

public class CivicTownHall extends Civic
{

/**
 * @param id
 */
public CivicTownHall(int id, String name, String tooltip, int inventorySize, Class<? extends TECivic> teClass, int size)
  {
  super(id);
  this.name = name;
  this.tooltip = tooltip;
  this.isDepository = true;
  this.isWorkSite = false;
  this.isDwelling = false;
  this.maxWorkers = 0;
  this.workSizeMaxHeight = 0;
  this.workSizeMaxHorizontal = 0;
  this.workType = CivicWorkType.UPKEEP;
  this.inventorySize = inventorySize;
  this.teClass = teClass;
  this.itemIconTexture = "civicFarmWheat1";
  

  this.addRecipeResource(new ItemStack(Item.paper, 2+size), false);
  this.addRecipeResource(new ItemStack(Block.chest, 1+size), false);  
  this.addRecipeResource(new ItemStack(Item.ingotGold, 3), false);
  switch(size)
  {
  case 0:
  this.addRecipeResource(new ItemStack(Block.planks, 10), true);
  this.addNeededResearch(ResearchGoalNumbers.civics1);
  break;
  
  case 1:
  this.addRecipeResource(new ItemStack(Block.stoneSingleSlab,20,0), false);
  this.addNeededResearch(ResearchGoalNumbers.civics3);
  this.addNeededResearch(ResearchGoalNumbers.logistics2);
  break;
  
  case 2:
  this.addRecipeResource(new ItemStack(Block.stoneBrick, 10), false);
  this.addNeededResearch(ResearchGoalNumbers.civics5);
  this.addNeededResearch(ResearchGoalNumbers.logistics3);
  break;
  }
  }

}
