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
package shadowmage.ancient_warfare.client.gui.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiElement;
import shadowmage.ancient_framework.client.gui.elements.GuiItemStack;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.GuiString;
import shadowmage.ancient_framework.client.gui.elements.GuiTab;
import shadowmage.ancient_framework.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.container.ContainerDummy;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterAZ;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterTextFilter;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;
import shadowmage.ancient_warfare.common.vehicles.IVehicleType;
import shadowmage.ancient_warfare.common.vehicles.types.VehicleType;

public class GuiResearchBook extends GuiContainerAdvanced
{

GuiTab activeTab = null;
HashSet<GuiTab> tabs = new HashSet<GuiTab>();
PlayerEntry entry;
RecipeSorterAZ sorterAZ = new RecipeSorterAZ();
RecipeSorterTextFilter sorterFilter = new RecipeSorterTextFilter();
GuiTextInputLine searchBox;
GuiScrollableArea area;
int buttonWidth = 256 - 24 - 10;
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
      this.handleRecipeClick(element);
      }
    }
  else if(this.activeTab==this.ammoTab)
    {
    if(this.recipes.containsKey(element))
      {
      this.handleRecipeClick(element);
      }
    }
  else if(this.activeTab==this.npcTab)
    {
    if(this.recipes.containsKey(element))
      {
      this.handleRecipeClick(element);
      }
    }
  else if(this.activeTab==this.civicTab)
    {
    if(this.recipes.containsKey(element))
      {
      this.handleRecipeClick(element);
      }
    }
  else if(this.activeTab==this.researchTab)
    {
    if(this.recipes.containsKey(element))
      {
      this.handleRecipeClick(element);
      }
    }
  else if(this.activeTab==this.craftingTab)
    {
   
    }
  else if(this.activeTab==this.structuresTab)
    {
  
    }
  else if(this.activeTab==this.miscTab)
    {
   
    }   
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
  
  this.searchBox = (GuiTextInputLine) new GuiTextInputLine(2, this, 240, 12, 30, "").updateRenderPos(5, 28);
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
    this.addMainInfo();
    }
  else if(this.activeTab==this.vehicleTab)
    {
    this.recipeTypes = EnumSet.of(RecipeType.VEHICLE, RecipeType.VEHICLE_MISC);
    this.guiElements.put(1, searchBox);
    this.handleSearchBoxUpdate(recipeTypes);
    }
  else if(this.activeTab==this.ammoTab)
    {
    this.recipeTypes = EnumSet.of(RecipeType.AMMO, RecipeType.AMMO_MISC);
    this.guiElements.put(1, searchBox);
    this.handleSearchBoxUpdate(recipeTypes);
    }
  else if(this.activeTab==this.npcTab)
    {
    this.recipeTypes = EnumSet.of(RecipeType.NPC, RecipeType.NPC_MISC);
    this.guiElements.put(1, searchBox);
    this.handleSearchBoxUpdate(recipeTypes);
    }
  else if(this.activeTab==this.civicTab)
    {
    this.recipeTypes = EnumSet.of(RecipeType.CIVIC, RecipeType.CIVIC_MISC);
    this.guiElements.put(1, searchBox);
    this.handleSearchBoxUpdate(recipeTypes);
    }
  else if(this.activeTab==this.researchTab)
    {
    /**
     * hmm...might need sub-tabs/buttons for known/unknown
     */
    this.recipeTypes = EnumSet.of(RecipeType.RESEARCH);
    this.guiElements.put(1, searchBox);
    this.handleSearchBoxUpdate(recipeTypes);
    }
  else if(this.activeTab==this.craftingTab)
    {
    this.addCraftingInfo();//mostly done..or done..w/e
    }
  else if(this.activeTab==this.structuresTab)
    {
    this.addStructureInfo();
    }
  else if(this.activeTab==this.miscTab)
    {
    this.addMiscInfo();
    } 
  
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).updateGuiPos(guiLeft, guiTop);
    }
  }

protected void addMiscInfo()
  {
  this.guiElements.put(0, area);
  int y = 0;
  int elementNum = 0;
  List<String> displayText = new ArrayList<String>();
  
  displayText.add("Misc Tips and Info");  
  displayText.add("");
  
  displayText.add("1.) Always keep your NPCs fed, and protected.  Hungry NPCs won't work very hard!");  
  displayText.add("2.) More coming soon!");
 

  displayText = RenderTools.getFormattedLines(displayText, 220);
  GuiString string;  
  for(String line : displayText)
    {
    string = new GuiString(elementNum, area, 240, 10, line);
    string.updateRenderPos(0, y);
    y+=10;
    elementNum++;
    area.elements.add(string);
    }  
  area.updateTotalHeight(y);
  }

protected void addMainInfo()
  {
  this.guiElements.put(0, area);
  int y = 0;
  int elementNum = 0;
  List<String> displayText = new ArrayList<String>();
  
  displayText.add("Welcome to Ancient Warfare.");  
  displayText.add("");
  displayText.add("Current Team: " + TeamTracker.instance().getTeamForPlayer(player));
  displayText.add("Players on Team: " +TeamTracker.instance().getTeamEntryFor(player).memberNames.size());
  displayText.add("Known Research: "+entry.getKnownResearch().size());
  displayText.add("Unkown Research: "+entry.getUnknwonResearch().size());

  displayText.add("");
  displayText.add("To begin research, place a research book (that is bound to you) into a research table.");
  displayText.add("");
  displayText.add("To begin crafting, place a research book (that is bound to you) into the book slot of a crafting table.");
  displayText.add("");
  displayText.add("Recipes for Vehicles, NPCs, Civics, and Ammo may be viewed by opening the corresponding tab.  All research goals" +
  		" may be viewed by clicking on the Research Tab.");
  displayText.add("");
  displayText.add("Crafting instructions are available in the Crafing tab.");
  displayText.add("Structure processing instructions are available in the Structures tab.");
   
  displayText = RenderTools.getFormattedLines(displayText, 220);
  GuiString string;  
  for(String line : displayText)
    {
    string = new GuiString(elementNum, area, 240, 10, line);
    string.updateRenderPos(0, y);
    y+=10;
    elementNum++;
    area.elements.add(string);
    }  
  area.updateTotalHeight(y);
  }

protected void addStructureInfo()
  {
  this.guiElements.put(0, area);
  int y = 0;
  int elementNum = 0;
  List<String> displayText = new ArrayList<String>();
  
  String text = "Structure crafting in Ancient Warfare is accomplished at the Drafting Station.  " +
  		"This station allows for the production of structure creation items for any structures flagged" +
  		" as being available in survival-mode.";
  displayText.add(text);
  
  text = "";
  displayText.add(text);
  
  text = "In order to begin production of a structure, select if from the selection list, and click the" +
  		" start button on the crafting/progress page.  The structure will then be 'locked in' and may not be" +
  		" changed except through the use of the 'Clear' button (which will discard any used materials--so" +
  		" use with caution).";
  displayText.add(text);
  
  text = "";
  displayText.add(text);
  
  text = "Once a structure item has been produced, you must select an appropriate site for its construction.  In" +
  		" survival mode, the chosen site must be 100% clear of any existing blocks or obstructions in order to proceed" +
  		" with placement.  Once you have placed the building site, you will need workers in order to construct the building" +
  		" for you.  Currently Miner type NPCs are the only type that can construct buildings.";
  displayText.add(text);
 
  displayText = RenderTools.getFormattedLines(displayText, 220);
  GuiString string;  
  for(String line : displayText)
    {
    string = new GuiString(elementNum, area, 240, 10, line);
    string.updateRenderPos(0, y);
    y+=10;
    elementNum++;
    area.elements.add(string);
    }  
  area.updateTotalHeight(y);
  }

protected void addCraftingInfo()
  {
  this.guiElements.put(0, area);
  int y = 0;
  int elementNum = 0;
  List<String> displayText = new ArrayList<String>();
  String text = "Crafting in Ancient Warfare is accomplished at one of five crafting" +
  		" stations.  These stations each have specific types of recipes that may be crafted" +
  		" corresponding with the station type.  Crafting stations are available for Vehicles," +
  		" Npcs, Civics, Ammunitions, and Structures.";
  displayText.add(text);
  text = "";
  displayText.add(text);
  text = "All crafting stations aside from the Structure station require that a Research Book" +
  		" be present in the Upper-Left slot (The research book slot).  The recipes available at" +
  		" any particular station are dependant upon the research book that is slotted (and whether" +
  		" or not creative mode is enabled).";
  displayText.add(text);
  text = "";
  displayText.add(text);
  text = "Crafting stations need workers in order to make progress.  Any time a player is interacting" +
  		" with a crafting station this counts as a full-time worker.  NPC Craftsman may also be designated" +
  		" to work at crafting stations so that items may be crafted without a player present.  (Config option" +
  		" available to disable worker requirements)";
  displayText.add(text);
  text = "";
  displayText.add(text);
  text = "All Crafting Station recipes are available in the normal Crafting Bench (as well as the Civic" +
  		" crafting station).  The recipes are as follows: ";
  displayText.add(text);
  text = "";
  displayText.add(text);
  displayText = RenderTools.getFormattedLines(displayText, 220);
  GuiString string;
  
  for(String line : displayText)
    {
    string = new GuiString(elementNum, area, 240, 10, line);
    string.updateRenderPos(0, y);
    y+=10;
    elementNum++;
    area.elements.add(string);
    }
  

  GuiItemStack stack;
  ItemStack recipeStack;
    
  for(ShapedRecipes recipe : AWCraftingManager.vanillaRecipeList)
    {
    int sy = 0;
    int sx = 0;
    string = new GuiString(elementNum, area, 220, 10, recipe.getRecipeOutput().getDisplayName());
    string.updateRenderPos(0, y);
    area.elements.add(string);
    y+=10;
    elementNum++;
    for(int i = 0; i < 9; i++)
      {
      if(i<recipe.recipeItems.length)
        {
        recipeStack = recipe.recipeItems[i];        
        }
      else
        {
        recipeStack = null;
        }
      stack = new GuiItemStack(elementNum, area);
      stack.renderSlotBackground = true;
      stack.setItemStack(recipeStack);
      stack.updateRenderPos(sx * 18, y);      
      area.elements.add(stack);
      sx++;
      elementNum++;
      if(sx>=3)
        {
        sx= 0;
        sy++;
        y+=18;
        }
      }
    stack = new GuiItemStack(elementNum, area);
    stack.updateRenderPos(4*18, y-36);
    stack.setItemStack(recipe.getRecipeOutput());
    stack.renderSlotBackground = true;
    area.elements.add(stack);
    y+=10;
    elementNum++;
    }
  
  area.updateTotalHeight(y);
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
    this.addRecipeButtons(AWCraftingManager.instance().getRecipesContaining(entry, text, recipeTypes, true), sorterFilter);      
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
    button = new GuiButtonSimple(num, area, buttonWidth, 16, recipe.getLocalizedDisplayName());
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
  ResourceListRecipe recipe = recipes.get(element);
  if(recipe.type==RecipeType.RESEARCH)
    {
    this.handleResearchDetailsClick(recipe);
    }
  else if(recipe.type==RecipeType.VEHICLE)
    {
    this.handleVehicleDetailsClick(recipe);
    }
  else
    {
    this.handleRecipeDetailsClick(recipe);    
    }
  }

protected void handleRecipeDetailsClick(ResourceListRecipe recipe)
  {
  mc.displayGuiScreen(new GuiRecipeDetails(this, recipe));    
  }

protected void handleResearchDetailsClick(ResourceListRecipe recipe)
  {
  int id = recipe.getResult().getItemDamage();
  IResearchGoal goal = ResearchGoal.getGoalByID(id);
  mc.displayGuiScreen(new GuiResearchGoal(this, goal));
  }

protected void handleVehicleDetailsClick(ResourceListRecipe recipe)
  {
  int type = recipe.getResult().getItemDamage();
  int lev = recipe.getResult().getTagCompound().getCompoundTag("AWVehSpawner").getInteger("lev");
  IVehicleType t = VehicleType.getVehicleType(type);
  mc.displayGuiScreen(new GuiVehicleDetails(this, recipe, t, lev));
  }
}
