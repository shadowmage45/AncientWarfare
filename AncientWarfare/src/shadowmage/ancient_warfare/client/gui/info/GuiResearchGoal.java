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

import org.lwjgl.input.Keyboard;

import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiItemStack;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

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
  
  int ticks = goal.getResearchTime();
  int seconds = ticks/20;
  int minutes = seconds/60;
  seconds = seconds % 60;
  ticks *= 5;//set to 100 scale
  ticks %= 100;//mod 100 to set 100==0
  ticks /=10;//div 10 to get tenths...
  String timeLabel = minutes + "m " + seconds + "."+ticks+"s";
  this.drawStringGui("Research Time: "+timeLabel, 5, 5+10, 0xffffffff);
  this.drawStringGui("Needed Resources:", 5, 5+20, 0xffffffff);
  int x = 0;
  int y = 0;
  for(ItemStackWrapperCrafting stack : goal.getResearchResources())
    {
    this.renderItemStack(stack.getFilter(), guiLeft + x*18 + 8, guiTop + y * 18 + 24+10, mouseX, mouseY, true);
    x++;
    if(x>=9)      
      {
      x = 0;
      y++;
      }
    }
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
  this.area = new GuiScrollableArea(0, this, 5, 5+16+5+30, 256-10, 240-10-16-5-30, 0);
  int elementNum = 100;
  int nextElementY = 0;
  List<String> descriptionLines = RenderTools.getFormattedLines(this.goal.getDetailedDescription(), 200);
  for(String st : descriptionLines)
    {
    area.addGuiElement(new GuiString(elementNum, area, 240-24, 10, st).updateRenderPos(0, nextElementY));
    elementNum++;
    nextElementY += 10;
    }
  nextElementY += 10;  
  area.addGuiElement(new GuiString(elementNum, area, 240-24, 10, "Used In Recipes: ").updateRenderPos(0, nextElementY));
  nextElementY += 10;
  List<ResourceListRecipe> recipes = AWCraftingManager.instance().getRecipesDependantOn(goal);
  for(ResourceListRecipe recipe : recipes)
    {
    area.addGuiElement(new GuiString(elementNum, area, 240-24, 10, recipe.getDisplayName()).updateRenderPos(22, nextElementY+5));
    elementNum++;
    area.addGuiElement(new GuiItemStack(elementNum, area).setItemStack(recipe.getResult()).updateRenderPos(0, nextElementY));
    elementNum++;
    nextElementY += 18;
    }
  nextElementY += 10;
  area.addGuiElement(new GuiString(elementNum, area, 240-24, 10, "Used In Research: ").updateRenderPos(0, nextElementY));
  nextElementY += 10;
  for(IResearchGoal g : ResearchGoal.getUnlocks(goal))
    {
    area.addGuiElement(new GuiString(elementNum, area, 240-24, 10, g.getDisplayName()).updateRenderPos(0, nextElementY));
    elementNum++;
    nextElementY += 10;
    }    
  area.updateTotalHeight(nextElementY);
  area.updateGuiPos(guiLeft, guiTop);
  this.guiElements.put(0, area);
  
  back = this.addGuiButton(1, this.getXSize()-40, 5, 35, 16, "Back");
  }

@Override
protected void keyTyped(char par1, int par2)
  {
  if(par2 == this.mc.gameSettings.keyBindInventory.keyCode || par2 == Keyboard.KEY_ESCAPE)
    {
    mc.displayGuiScreen(parent);
    return;
    }
  super.keyTyped(par1, par2);
  }


@Override
public void updateControls()
  {
  // TODO Auto-generated method stub
  }


}
