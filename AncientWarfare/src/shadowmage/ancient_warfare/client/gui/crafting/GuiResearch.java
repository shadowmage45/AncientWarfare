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
package shadowmage.ancient_warfare.client.gui.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiTab;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.gui.info.GuiResearchGoal;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerResearch;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;

public class GuiResearch extends GuiCraftingTabbed
{


HashMap<GuiButtonSimple, ResourceListRecipe> queueRecipes = new HashMap<GuiButtonSimple, ResourceListRecipe>();
HashMap<GuiButtonSimple, ResourceListRecipe> queuedRecipes = new HashMap<GuiButtonSimple, ResourceListRecipe>();

GuiScrollableArea area2;

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
  return "Queue";
  }

@Override
protected void handleRecipeDetailsClick(ResourceListRecipe recipe)
  {
  int id = recipe.getResult().getItemDamage();
  IResearchGoal goal = ResearchGoal.getGoalByID(id);
  mc.displayGuiScreen(new GuiResearchGoal(this, goal));
  }

@Override
public void updateScreenContents()
  {
  super.updateScreenContents();
  area2.updateGuiPos(guiLeft, guiTop);
  }

@Override
public void setupControls()
  { 
  super.setupControls();
  this.area2 = new GuiScrollableArea(3, this, 5, 21+18+10+5+18+80, 176-10, 240-21-10-18-5-8-18-80, 0);
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
    return;
    }
  if(this.activeTab==this.craftingTab)
    {
    if(element==stopButton)//clear
      {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setBoolean("stop", true);
      this.sendDataToServer(tag);
      }
    else if(element==startButton && !this.container.isLocked && this.container.clientRecipe!=null)
      {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setBoolean("start", true);
      this.sendDataToServer(tag);
      }
    }
  else if(this.activeTab==this.tab1)
    {
    if(element==this.searchBox)
      {
      this.handleSearchBoxUpdate();
      }
    else if(recipes.containsKey(element))
      {
      this.handleRecipeClick(element);
      }
    }
  else if(this.activeTab==this.tab2)//queue tab
    {
    if(element==this.searchBox)
      {
      this.handleSearchBoxUpdate();
      }
    else if(queueRecipes.containsKey(element))
      {
      if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
        this.handleRecipeDetailsClick(queueRecipes.get(element));
        }
      else
        {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("addQ", this.queueRecipes.get(element).getResult().getItemDamage());
        this.sendDataToServer(tag);        
        }
      }
    else if(queuedRecipes.containsKey(element))
      {
      if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
        this.handleRecipeDetailsClick(queuedRecipes.get(element));
        }
      else
        {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("remQ", this.queuedRecipes.get(element).getResult().getItemDamage());
        this.sendDataToServer(tag);        
        }
      }
    }
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
  if(this.container.clientRecipe!=null)
    {
    this.addCurrentRecipeElements();
    }
  if(this.activeTab==this.craftingTab)
    {
    this.container.addSlots();
    this.addProgressButtons();
    }
  else if(this.activeTab==this.tab1 || this.activeTab==this.tab2)
    {
    this.guiElements.put(0, area);
    this.guiElements.put(1, searchBox);
    if(this.activeTab==this.tab2)
      {
      this.guiElements.put(3, area2);
      }
    this.handleSearchBoxUpdate();    
    }  
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).updateGuiPos(guiLeft, guiTop);
    }
  }

@Override
public void drawExtraForeground(int mouseX, int mouseY, float partialTick)
  {
  if(this.activeTab!=null)
    {
    if(this.activeTab==this.craftingTab)
      {
      this.drawProgressForeground();
      }
    else if(this.activeTab==this.tab2)
      {
      this.drawStringGui("Queued: ", 5, 21+18+10+5+18+70+2, 0xffffffff);
      }
    }
  }

@Override
protected void handleSearchBoxUpdate()
  {  
  if(this.activeTab == this.tab1)
    {
    this.area.setHeight(240-21-10-18-5-8-18);
    String text = this.searchBox.getText();
    this.recipes.clear();
    this.area.elements.clear();
    this.sorterFilter.setFilterText(text);
    PlayerEntry entry = container.entry;
    if(entry==null)
      {
      return;
      }
    List<IResearchGoal> goals = entry.getAvailableResearch();
    List<ResourceListRecipe> recipes = new ArrayList<ResourceListRecipe>();
    for(IResearchGoal g : goals)
      {
      recipes.add(AWCraftingManager.instance().getRecipeByResult(new ItemStack(ItemLoader.researchNotes,1,g.getGlobalResearchNum())));
      }
    this.addRecipeButtons(recipes, sorterFilter);     
    }
  if(this.activeTab==this.tab2)
    {
    String text = this.searchBox.getText();
    this.recipes.clear();
    this.area.setHeight(70);
    this.queueRecipes.clear();
    this.area.elements.clear();
    if(this.container.entry==null)
      {
      return;
      }
    this.sorterFilter.setFilterText(text);
    List<IResearchGoal> goals = this.container.entry.getUnknwonResearch();
    List<ResourceListRecipe> recipes = new ArrayList<ResourceListRecipe>();
    for(IResearchGoal b : goals)
      {
      recipes.add(AWCraftingManager.instance().getRecipeByResult(new ItemStack(ItemLoader.researchNotes,1,b.getGlobalResearchNum())));
      }
//    Iterator<ResourceListRecipe> it = recipes.iterator();
//    ResourceListRecipe recipe;
//    while(it.hasNext())
//      {
//      recipe = it.next();
//      for(ResourceListRecipe r : queuedRecipes.values())
//        {
//        if(recipe.matches(r))
//          {
//          it.remove();
//          }
//        }
//      }
    Collections.sort(recipes, sorterFilter);
    this.addQueueRecipeButtons(recipes, sorterFilter);    
    area2.elements.clear();    
    this.addQueuedRecipeButtons();
    }
  }

protected void addQueueRecipeButtons(List<ResourceListRecipe> recipes, Comparator sorter)
  {
  this.guiElements.put(0, area);
  Collections.sort(recipes, sorter);
  area.updateTotalHeight(recipes.size()*18);
  GuiButtonSimple button;
  int x = 0;
  int y = 0;
  ArrayList<String> tooltip = new ArrayList<String>();
  tooltip.add("Left click to add to queue, if possible");
  tooltip.add("Hold (shift) while clicking to");
  tooltip.add("view detailed recipe information");
  int num = 100;
  for(ResourceListRecipe recipe : recipes)
    {      
    button = new GuiButtonSimple(num, area, buttonWidth, 16, StatCollector.translateToLocal(recipe.getLocalizedDisplayName()));
    button.updateRenderPos(x, y);
    button.setTooltip(tooltip);
    area.addGuiElement(button);
    this.queueRecipes.put(button, recipe);
    y+=18;
    num++;
    }
  }

protected void addQueuedRecipeButtons()
  {
  this.guiElements.put(3, area2);
  sorterFilter.setFilterText("");
  List<Integer> queuedGoals = new ArrayList<Integer>();
  queuedGoals.addAll(((ContainerResearch)container).researchQueueCache);  
  List<ResourceListRecipe> queuedRecipes = new ArrayList<ResourceListRecipe>();
  for(Integer i : queuedGoals)
    {
    queuedRecipes.add(AWCraftingManager.instance().getRecipeByResult(new ItemStack(ItemLoader.researchNotes,1,i)));
    }    
  this.queuedRecipes.clear();
  area2.updateTotalHeight(queuedRecipes.size()*18);
  GuiButtonSimple button;
  int x = 0;
  int y = 0;
  ArrayList<String> tooltip = new ArrayList<String>();
  tooltip.add("Left click to remove from queue");
  tooltip.add("Hold (shift) while clicking to");
  tooltip.add("view detailed recipe information");
  int num = 100;
  for(ResourceListRecipe recipe : queuedRecipes)
    {      
    button = new GuiButtonSimple(num, area, buttonWidth, 16, StatCollector.translateToLocal(recipe.getLocalizedDisplayName()));
    button.updateRenderPos(x, y);
    button.setTooltip(tooltip);
    area2.addGuiElement(button);
    this.queuedRecipes.put(button, recipe);
    y+=18;
    num++;
    }
  }

}
