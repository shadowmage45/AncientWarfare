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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.player.EntityPlayer;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiElement;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiTab;
import shadowmage.ancient_warfare.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerDummy;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterAZ;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterTextFilter;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;

public class GuiResearchBook extends GuiContainerAdvanced
{

GuiTab activeTab = null;
HashSet<GuiTab> tabs = new HashSet<GuiTab>();
PlayerEntry entry;
RecipeSorterAZ sorterAZ = new RecipeSorterAZ();
RecipeSorterTextFilter sorterFilter = new RecipeSorterTextFilter();
GuiTextInputLine searchBox;
GuiScrollableArea area;
int buttonWidth = 256 - 16 - 24-10;
HashMap<GuiButtonSimple, ResourceListRecipe> recipes = new HashMap<GuiButtonSimple, ResourceListRecipe>();

GuiTab mainTab;
GuiTab vehicleTab;
GuiTab ammoTab;
GuiTab npcTab;
GuiTab civicTab;
GuiTab miscTab;
GuiTab researchTab;
GuiTab structuresTab;
GuiTab craftingTab;

EnumSet recipeTypes = null;
/**
 * tabs
 * INFO
 *    hints/tips/beginners guide/basic stats
 * VEHICLES
 *    searchable list of  (available?) vehicle detail pages
 * AMMO
 *    searchable list of (available?) ammo detail pages
 * NPC
 *    searchable list of (available?) npc detail pages
 * MISC
 *    gates/armor/upgrades (available?) detail pages
 * CIVICS -- MISSING
 *    searchable list of  (available?) civic detail pages
 * RESEARCH
 *    searchable list of all (completed?) research
 * STRUCTURES
 *    info sheet on structures -- how to scan, build, etc
 */
/**
 * @param container
 */
public GuiResearchBook(EntityPlayer player)
  {
  super(new ContainerDummy());
  this.entry = PlayerTracker.instance().getClientEntry();
  this.shouldCloseOnVanillaKeys = true;
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
protected void renderBackgroundImage(String tex)
  {
  if(tex!=null)
    {
    RenderTools.drawQuadedTexture(guiLeft, guiTop+21, this.xSize, this.ySize-42, 256, 240, tex, 0, 0);
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
 
  }

@Override
public void updateScreenContents()
  {
  area.updateGuiPos(guiLeft, guiTop);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(this.tabs.contains(element))
    {
    GuiTab selected = (GuiTab) element;
    for(GuiTab tab : this.tabs)
      {
      tab.enabled = false;
      }
    selected.enabled = true;
    this.activeTab = selected;
    this.forceUpdate = true;
    this.searchBox.selected = false;
    }
  
  if(this.activeTab==this.mainTab)
    {
   /**
    * need basic stats on front info page, ++ descriptions of what they other tabs are for
    */
    }
  else if(this.activeTab==this.vehicleTab)
    {
    if(this.recipes.containsKey(element))
      {
      /**
       * TODO swap over to vehicle details page
       */
      this.handleRecipeClick(element);
      }
//    this.recipeTypes = EnumSet.of(RecipeType.VEHICLE, RecipeType.VEHICLE_MISC);
//    this.handleSearchBoxUpdate(recipeTypes);
    }
  else if(this.activeTab==this.ammoTab){}
  else if(this.activeTab==this.npcTab){}
  else if(this.activeTab==this.civicTab){}
  else if(this.activeTab==this.researchTab){}
  else if(this.activeTab==this.craftingTab){}
  else if(this.activeTab==this.structuresTab){}
  else if(this.activeTab==this.miscTab){} 
  
  }

@Override
public void setupControls()
  {
  GuiTab tab = this.addGuiTab(1000, 3, 0, 50, 24, "Info");
  this.activeTab = tab;
  this.tabs.add(tab);
  this.mainTab = tab;
  tab = this.addGuiTab(1001, 3+50, 0, 50, 24, "Vehicles");
  tab.enabled = false;
  this.tabs.add(tab);
  this.vehicleTab = tab;
  tab = this.addGuiTab(1002, 3+50+50, 0, 50, 24, "Ammo");
  tab.enabled = false;
  this.tabs.add(tab);
  this.ammoTab = tab;
  tab = this.addGuiTab(1003, 3+50+50+50, 0, 50, 24, "Npcs");
  tab.enabled = false;
  this.tabs.add(tab);
  this.npcTab = tab;
  tab = this.addGuiTab(1004, 3+50+50+50+50, 0, 50, 24, "Civics");
  tab.enabled = false;
  this.tabs.add(tab);
  this.civicTab = tab;
    
  int y = this.getYSize()-24;
  tab = this.addGuiTab(1005, 3, y, 60, 24, "Research");
  tab.enabled = false;
  tab.inverted = true;
  this.tabs.add(tab);
  this.researchTab = tab;
  tab = this.addGuiTab(1006, 3+60, y, 60, 24, "Crafting");
  tab.enabled = false;
  tab.inverted = true;
  this.tabs.add(tab);
  this.craftingTab = tab;  
  tab = this.addGuiTab(1007, 3+60+60, y, 80, 24, "Structures");
  tab.enabled = false;
  tab.inverted = true;
  this.tabs.add(tab);
  this.structuresTab = tab;
  tab = this.addGuiTab(1008, 3+60+60+80, y, 50, 24, "Misc");
  tab.enabled = false;
  tab.inverted = true;
  this.tabs.add(tab);
  this.miscTab = tab;
  
  this.searchBox = (GuiTextInputLine) new GuiTextInputLine(2, this, 240, 12, 30, "").updateRenderPos(5, 24);
  searchBox.selected = false;
  int w = 256-10;
  int h = 240 - 42 - 10 - 20;
  int x = 5;
  y = 21+5+20;  
  this.area = new GuiScrollableArea(0, this, x, y, w, h, 0);
  this.forceUpdate = true;
  }

@Override
public void updateControls()
  {
  this.guiElements.clear();
  this.area.elements.clear();
  this.recipeTypes = null;
  for(GuiTab tab : this.tabs)
    {
    this.guiElements.put(tab.getElementNumber(), tab);
    }
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).updateGuiPos(guiLeft, guiTop);
    }
  if(this.activeTab==this.mainTab)
    {
   /**
    * need basic stats on front info page, ++ descriptions of what they other tabs are for
    */
    }
  else if(this.activeTab==this.vehicleTab)
    {
    this.recipeTypes = EnumSet.of(RecipeType.VEHICLE, RecipeType.VEHICLE_MISC);
    this.guiElements.put(1, searchBox);
    this.handleSearchBoxUpdate(recipeTypes);
    }
  else if(this.activeTab==this.ammoTab){}
  else if(this.activeTab==this.npcTab){}
  else if(this.activeTab==this.civicTab){}
  else if(this.activeTab==this.researchTab){}
  else if(this.activeTab==this.craftingTab){}
  else if(this.activeTab==this.structuresTab){}
  else if(this.activeTab==this.miscTab){} 
  
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).updateGuiPos(guiLeft, guiTop);
    }
  }

protected void handleSearchBoxUpdate(EnumSet<RecipeType> recipeTypes)
  {  
  this.guiElements.put(0, area);
  String text = this.searchBox.getText();
  this.recipes.clear();
  this.area.elements.clear();
  this.sorterFilter.setFilterText(text);
  PlayerEntry entry = PlayerTracker.instance().getClientEntry();
  if(recipeTypes!=null)
    {
    this.addRecipeButtons(AWCraftingManager.instance().getRecipesContaining(entry, text, recipeTypes, player.capabilities.isCreativeMode), sorterFilter);      
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
    this.handleSearchBoxUpdate(this.recipeTypes);
    }
  }

protected void handleRecipeClick(IGuiElement element)
  {  
  this.handleRecipeDetailsClick(recipes.get(element));     
  }

protected void handleRecipeDetailsClick(ResourceListRecipe recipe)
  {
  mc.displayGuiScreen(new GuiRecipeDetails(this, recipe));
  }
}
