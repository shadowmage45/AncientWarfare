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
package shadowmage.ancient_warfare.client.gui.info;

import java.util.List;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;

public class GuiResearchGoal extends GuiContainerAdvanced
{
GuiContainerAdvanced parent;
IResearchGoal goal;

/**
 * @param container
 */
public GuiResearchGoal(Container container, IResearchGoal goal, GuiContainerAdvanced parent)
  {
  super(container);
  this.goal = goal;
  this.parent = parent;
  }

@Override
public int getXSize()
  {
  return 256;
  }

@Override
public int getYSize()
  {
  return 240;
  }

@Override
public String getGuiBackGroundTexture()
  {
  return Config.texturePath+"gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  this.drawStringGui(goal.getDisplayName(), 5, 5, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  this.area.updateGuiPos(guiLeft, guiTop);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==back)
    {
    mc.displayGuiScreen(parent);
    }  
  }

GuiScrollableArea area;
GuiButtonSimple back;

@Override
public void setupControls()
  {
  this.area = new GuiScrollableArea(0, this, 5, 5+16+5, 256-10, 240-10-16-5, 0);
  int y = 0;
  int areaHeight = 0;
  List<String> descriptionLines = RenderTools.getFormattedLines(this.goal.getDetailedDescription(), 200);
  areaHeight = descriptionLines.size() * 10;
  for(String st : descriptionLines)
    {
    area.addGuiElement(new GuiString(y+100, area, 240-24, 10, st).updateRenderPos(0, y*10));
    y++;
    }
  y++;
  area.addGuiElement(new GuiString(y+100, area, 240-24, 10, "Used In Recipes: ").updateRenderPos(0, y*10));
  y++;
  List<ResourceListRecipe> recipes = AWCraftingManager.instance().getRecipesDependantOn(goal);
  areaHeight += 10 * recipes.size();
  for(ResourceListRecipe recipe : recipes)
    {
    area.addGuiElement(new GuiString(y+100, area, 240-24, 10, recipe.getDisplayName()).updateRenderPos(0, y*10));
    y++;
    }
  y++;
  area.addGuiElement(new GuiString(y+100, area, 240-24, 10, "Used In Research: ").updateRenderPos(0, y*10));
  y++;
  for(IResearchGoal g : ResearchGoal.getUnlocks(goal))
    {
    area.addGuiElement(new GuiString(y+100, area, 240-24, 10, g.getDisplayName()).updateRenderPos(0, y*10));
    y++;    
    }

  areaHeight = 10 * y;
  
  
  
  area.updateTotalHeight(areaHeight);
  area.updateGuiPos(guiLeft, guiTop);
  this.guiElements.put(0, area);
  
  back = this.addGuiButton(1, this.getXSize()-40, 5, 35, 16, "Back");
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub
  }


}
