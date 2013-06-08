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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiElement;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiTab;
import shadowmage.ancient_warfare.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerAWCrafting;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterTextFilter;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;

public abstract class GuiCraftingTabbed extends GuiContainerAdvanced
{

GuiTab activeTab = null;
HashSet<GuiTab> tabs = new HashSet<GuiTab>();
HashMap<GuiButtonSimple, ResourceListRecipe> recipes = new HashMap<GuiButtonSimple, ResourceListRecipe>();
GuiScrollableArea area;
RecipeSorterTextFilter sorterFilter = new RecipeSorterTextFilter();
GuiTextInputLine searchBox;
int buttonWidth = 176-10-12-10;
ContainerAWCrafting container;

GuiTab craftingTab = null;
GuiTab tab1 = null;
GuiTab tab2 = null;
GuiButton startButton = null;
GuiButton stopButton = null;
EnumSet recipeTypes = null;

/**
 * @param container
 */
public GuiCraftingTabbed(Container container)
  {
  super(container);
  this.container = (ContainerAWCrafting)container;
  }

public abstract EnumSet<RecipeType> getTab1RecipeTypes();
public abstract EnumSet<RecipeType> getTab2RecipeTypes();

@Override
public int getXSize()
  {
  return 176;
  }

@Override
public int getYSize()
  {
  return 240;
  }

@Override
protected void renderBackgroundImage(String tex)
  {
  if(tex!=null)
    {
    RenderTools.drawQuadedTexture(guiLeft, guiTop+21, this.xSize, this.ySize-21, 256, 240, tex, 0, 0);
    }
  }

@Override
public String getGuiBackGroundTexture()
  {
  return Config.texturePath+"gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  // TODO Auto-generated method stub

  }

public void drawProgressBackground()
  { 
  int w = 100;
  int h = 10; 
  int w1 = 100;
  int x = guiLeft + 7;
  int y = guiTop + 112+18+3;
  String tex = Config.texturePath+"gui/guiButtons2.png";
  RenderTools.drawQuadedTexture(x, y, w+6, h+6, 256, 40, tex, 0, 0);
  float progress = container.displayProgress;
  float max = container.displayProgressMax;
  float percent = 0;
  if(max!=0)
    {
    percent = progress/max;
    }
  w1 = (int)(percent*100.f);
  tex = Config.texturePath+"gui/guiButtons.png"; 
  RenderTools.drawQuadedTexture(x+3, y+3, w1, h, 104, 10, tex, 152, 234);
  x += 112;
  y += 4;
  w = (int) ((max-progress)/20);
  h = w/60;
  w = w% 60;
  tex = String.format("%sm %ss", h,w);
  this.drawString(getFontRenderer(), tex, x, y, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  // TODO Auto-generated method stub
  }

@Override
public void setupControls()
  {
  // TODO Auto-generated method stub
  }

@Override
public void updateControls()
  {
  this.guiElements.clear();
  this.area.elements.clear();
  this.recipes.clear();
  for(GuiTab tab : this.tabs)
    {
    this.guiElements.put(tab.getElementNumber(), tab);
    }
  this.container.removeSlots();
  if(this.activeTab==this.craftingTab)
    {
    /**
     * ADD ITEM STACK SLOT FOR RESULT
     * GUI STRING ELEMENT FOR TEXT
     */
    }
  else if(this.activeTab==this.tab1)
    {
    this.guiElements.put(0, area);
    this.guiElements.put(1, searchBox);    
    this.handleSearchBoxUpdate();
    }
  else if(this.activeTab==this.tab2)
    {
    this.guiElements.put(0, area);
    this.guiElements.put(1, searchBox);
    this.handleSearchBoxUpdate();
    }
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).updateGuiPos(guiLeft, guiTop);
    }
  }

protected void handleSearchBoxUpdate()
  {  
  if(this.activeTab == this.tab1 || this.activeTab == this.tab2)
    {
    String text = this.searchBox.getText();
    this.recipes.clear();
    this.area.elements.clear();
    this.sorterFilter.setFilterText(text);
    PlayerEntry entry = container.entry;
    this.recipeTypes = activeTab==tab1 ? this.getTab1RecipeTypes() : this.getTab2RecipeTypes();
    if(recipeTypes!=null)
      {
      this.addRecipeButtons(AWCraftingManager.instance().getRecipesContaining(entry, text, recipeTypes, player.capabilities.isCreativeMode), sorterFilter);      
      }
    }
  }

protected void addRecipeButtons(List<ResourceListRecipe> recipes, Comparator sorter)
  {
  this.guiElements.put(0, area);
  Collections.sort(recipes, sorter);
  area.updateTotalHeight(recipes.size()*18);
  GuiButtonSimple button;
  int x = 0;
  int y = 0;
  ArrayList<String> tooltip = new ArrayList<String>();
  tooltip.add("Hold (shift) while clicking to");
  tooltip.add("view detailed recipe information");
  int num = 100;
  for(ResourceListRecipe recipe : recipes)
    {      
    button = new GuiButtonSimple(num, area, buttonWidth, 16, recipe.getDisplayName());
    button.updateRenderPos(x, y);
    button.setTooltip(tooltip);
    area.addGuiElement(button);
    this.recipes.put(button, recipe);
    y+=18;
    num++;
    }
  }

@Override
protected void keyTyped(char par1, int par2)
  {
  for(Integer i : this.guiElements.keySet())
    {
    GuiElement el = this.guiElements.get(i);
    el.onKeyTyped(par1, par2);
    }
  if(!this.searchBox.selected)
    {
    super.keyTyped(par1, par2);
    }
  else
    {
    this.handleSearchBoxUpdate();
    }
  }
}
