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
package shadowmage.ancient_warfare.client.gui.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.research.ResearchGoal;

public class GuiResearch extends GuiCraftingTabbed
{

/**
 * @param container
 */
public GuiResearch(Container container)
  {
  super(container);
  }

@Override
public EnumSet<RecipeType> getTab1RecipeTypes()
  {
  return EnumSet.of(RecipeType.RESEARCH);
  }

@Override
public EnumSet<RecipeType> getTab2RecipeTypes()
  {
  return EnumSet.of(RecipeType.NONE);
  }

@Override
public String getTab1Label()
  {
  return "Research";
  }

@Override
public String getTab2Label()
  {
  return null;
  }

@Override
protected void addRecipeButtons(List<ResourceListRecipe> recipes,  Comparator sorter)
  {
  this.guiElements.put(0, area);
  Collections.sort(recipes, sorter);
  GuiButtonSimple button;
  int x = 0;
  int y = 0;
  ArrayList<String> tooltip = new ArrayList<String>();
  tooltip.add("Hold (shift) while clicking to");
  tooltip.add("view detailed recipe information");
  int num = 100;  
  for(ResourceListRecipe recipe : recipes)
    {      
    if(container.entry!=null && container.entry.hasDoneResearch(ResearchGoal.getGoalByID(recipe.getResult().getItemDamage())))
      {
      continue;
      }
    button = new GuiButtonSimple(num, area, buttonWidth, 16, recipe.getDisplayName());
    button.updateRenderPos(x, y);
    button.setTooltip(tooltip);
    area.addGuiElement(button);
    this.recipes.put(button, recipe);
    y+=18;
    num++;
    }
  area.updateTotalHeight(y);
  }




}
