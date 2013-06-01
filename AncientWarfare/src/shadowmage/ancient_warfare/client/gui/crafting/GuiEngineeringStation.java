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

import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.input.Keyboard;

import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiElement;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiTab;
import shadowmage.ancient_warfare.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.gui.info.GuiRecipeDetails;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerEngineeringStation;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterAZ;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterTextFilter;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;

public class GuiEngineeringStation extends GuiContainerAdvanced
{

GuiTab activeTab = null;
HashSet<GuiTab> tabs = new HashSet<GuiTab>();
HashMap<GuiButtonSimple, ResourceListRecipe> recipes = new HashMap<GuiButtonSimple, ResourceListRecipe>();
GuiScrollableArea area;
int buttonWidth = 176-10-12-10;
ContainerEngineeringStation container;
RecipeSorterAZ sorterAZ = new RecipeSorterAZ();
RecipeSorterTextFilter sorterFilter = new RecipeSorterTextFilter();
GuiTextInputLine searchBox;

/**
 * @param container
 */
public GuiEngineeringStation(Container container)
  {
  super(container);
  this.container = (ContainerEngineeringStation)container;
  this.shouldCloseOnVanillaKeys = true;
  }

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
  if(this.activeTab!=null)
    {
    switch(activeTab.getElementNumber())
    {
    case 1000:
    this.drawCivicBackgroud();    
    break;
    
    case 1001:
    this.drawAmmoBackground();
    break;
    
    case 1002:
    this.drawProgressBackground();
    break;
    
    case 1003://upgrades
    this.drawUpgradeBackground();
    break;
    
    case 1004://gates
    this.drawGateBackground();
    break;
    
    case 1005://search
    this.drawSearchBackground();
    break;
    }
    }
  }

public void drawCivicBackgroud()
  { 
  }

public void drawGateBackground()
  {
  }

public void drawAmmoBackground()
  {
  }

public void drawSearchBackground()
  {
  
  }

public void drawUpgradeBackground()
  {
  }

public void drawCurrentRecipeBackground()
  {
  if(this.container.currentRecipe!=null)
    {
    this.drawStringGui(this.container.currentRecipe.getDisplayName(), 8+18+2, 24+3+4, 0xffffffff);
    this.renderItemStack(this.container.currentRecipe.getResult(), guiLeft+8, guiTop+24+3, mouseX, mouseY, true);
    }
  }

@Override
public void drawExtraForeground(int mouseX, int mouseY, float partialTick)
  {
  if(this.activeTab!=null)
    {
    switch(activeTab.getElementNumber())
    {
    
    case 1000:
    this.drawCurrentRecipeBackground();
    break;
    
    case 1001:
    this.drawCurrentRecipeBackground();
    break;
    
    case 1002:
    this.drawProgressForeground();
    this.drawCurrentRecipeBackground();
    break;
    
    case 1003:
    this.drawCurrentRecipeBackground();
    break;
    
    case 1004:
    this.drawCurrentRecipeBackground();
    break;
    
    case 1005:
    
    break;
    }
    }
  }

public void drawProgressForeground()
  {
  
  }

public void drawProgressBackground()
  {
  /**
   * 152, 234   x 104,10
   */
  int w = 100;
  int h = 10; 
  int w1 = 100;
  int x = guiLeft + 7;
  int y = guiTop + 112;
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

  this.drawCurrentRecipeBackground();
  }

@Override
public void updateScreenContents()
  {
  this.area.updateGuiPos(guiLeft, guiTop);
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
  switch(activeTab.getElementNumber())
  {
  case 1000://civics
  if(recipes.containsKey(element))
    {
    this.handleRecipeClick(element);
    }
  break;
  
  case 1001://ammo
  if(recipes.containsKey(element))
    {
    this.handleRecipeClick(element);
    }
  break;
  
  case 1002://progress
  if(element.getElementNumber()==1)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("stop", true);
    this.sendDataToServer(tag);
    }
  break;
  
  case 1003://upgrades
  if(recipes.containsKey(element))
    {
    this.handleRecipeClick(element);
    }
  break;
  
  case 1004:
  if(recipes.containsKey(element))
    {
    this.handleRecipeClick(element);
    }
  break;
  
  case 1005:
  if(element==this.searchBox)
    {
    this.handleSearchBoxUpdate();
    }
  else if(recipes.containsKey(element))
    {
    this.handleRecipeClick(element);
    }
  break;
  }
  }

protected void handleSearchBoxUpdate()
  {  
  if(this.activeTab!=null && this.activeTab.getElementNumber()==1005)
    {
    String text = this.searchBox.getText();
    this.recipes.clear();
    this.area.elements.clear();
    this.sorterFilter.setFilterText(text);
    this.addRecipeButtons(AWCraftingManager.instance().getRecipesContaining(text, EnumSet.of(RecipeType.AMMO, RecipeType.ARMOR, RecipeType.CIVIC, RecipeType.GATE, RecipeType.UPGRADE)), sorterFilter);
    }
  }

protected void handleRecipeClick(IGuiElement element)
  {  
  if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
    {
    mc.displayGuiScreen(new GuiRecipeDetails(this, recipes.get(element)));      
    }
  else
    {
    if(container.isWorking)
      {
      return;
      }
    NBTTagCompound tag = new NBTTagCompound();
    tag.setCompoundTag("set", recipes.get(element).getNBTTag());
    this.sendDataToServer(tag);
    }
  }

@Override
public void setupControls()
  {
  GuiTab tab = this.addGuiTab(1000, 5+60+60, 0, 40, 24, "Civics");
  this.tabs.add(tab);
  tab.enabled = false;
  tab = this.addGuiTab(1001, 5+60, 0, 60, 24, "Ammo");
  tab.enabled = false;
  this.tabs.add(tab);
  tab = this.addGuiTab(1002, 5, 0, 60, 24, "Progress");
  this.tabs.add(tab);
  this.activeTab = tab;
  tab = this.addGuiTab(1003, 5, this.getYSize()-24, 60, 24, "Upgrades");
  tab.enabled = false;
  tab.inverted = true;
  this.tabs.add(tab);
  tab = this.addGuiTab(1004, 5+60, this.getYSize()-24, 40, 24, "Gates");
  tab.enabled = false;
  tab.inverted = true;
  this.tabs.add(tab);
  tab = this.addGuiTab(1005, 5+60+40, this.getYSize()-24, 50, 24, "Search");
  tab.enabled = false;
  tab.inverted = true;
  this.tabs.add(tab);
  
  this.searchBox = (GuiTextInputLine) new GuiTextInputLine(2, this, 160, 12, 30, "").updateRenderPos(5, 24);
  searchBox.selected = false;
  this.area = new GuiScrollableArea(0, this, 5, 21+18+10+5, 176-10, 240-42-10-18-5-8, 0);
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
  if(this.activeTab!=null)
    {
    switch(activeTab.getElementNumber())
    {
    case 1000://civics
    this.addRecipeButtons(AWCraftingManager.instance().getRecipesOfTypesFor(player, RecipeType.CIVIC), sorterAZ);
    break;
    
    case 1001://ammo
    this.addRecipeButtons(AWCraftingManager.instance().getRecipesOfTypesFor(player, RecipeType.AMMO), sorterAZ);
    break;
    
    case 1002://progress
    container.addSlots();
    this.addGuiButton(1, 35, 16, "Clear").updateRenderPos(120, 40);  
    break;
    
    case 1003://upgrades/armor
    this.addRecipeButtons(AWCraftingManager.instance().getRecipesOfTypesFor(player, RecipeType.UPGRADE), sorterAZ);
    break;
    
    case 1004://gates
    this.addRecipeButtons(AWCraftingManager.instance().getRecipesOfTypesFor(player, RecipeType.GATE), sorterAZ);
    break;
    
    case 1005://search
    this.guiElements.put(2, searchBox);
    this.handleSearchBoxUpdate();
    break;
    }
    }  
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).updateGuiPos(guiLeft, guiTop);
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
