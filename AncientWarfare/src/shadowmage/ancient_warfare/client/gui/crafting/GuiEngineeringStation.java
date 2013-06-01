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
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.gui.info.GuiRecipeDetails;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerEngineeringStation;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterAZ;
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
/**
 * @param container
 */
public GuiEngineeringStation(Container container)
  {
  super(container);
  this.shouldCloseOnVanillaKeys = true;
  this.container = (ContainerEngineeringStation)container;
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
//    this.drawKnownBackground();
    break;
    case 1001:
//    this.drawAvailableBackground();
    break;
    case 1002:
    this.drawProgressBackground();
    break;
    case 1003:
//    this.drawUnknownBackground();
    break;
    }
    }
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
    this.addCivicControls();
    break;
    
    case 1001://ammo
    this.addAmmoControls();
    break;
    
    case 1002://progress
    this.addProgressControls();
    break;
    
    case 1003://upgrades
    this.addUpgradeControls();
    break;
    }
    }  
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).updateGuiPos(guiLeft, guiTop);
    }  

  }

protected void addCivicControls()
  {
  this.addRecipeButtons(AWCraftingManager.instance().getCivicRecipesFor(player));
  }

protected void addAmmoControls()
  {
  this.addRecipeButtons(AWCraftingManager.instance().getAmmoRecipesFor(player));
  }

protected void addProgressControls()
  {
  container.addSlots();
  this.addGuiButton(1, 35, 16, "Clear").updateRenderPos(120, 40);  
  }

protected void addUpgradeControls()
  {
  this.addRecipeButtons(AWCraftingManager.instance().getUpgradeRecipesFor(player));
  }

protected void addRecipeButtons(List<ResourceListRecipe> recipes)
  {
  this.guiElements.put(0, area);
  Collections.sort(recipes, sorterAZ);
  area.updateTotalHeight(recipes.size()*18);
  GuiButtonSimple button;
  int x = 0;
  int y = 0;
  ArrayList<String> tooltip = new ArrayList<String>();
  tooltip.add("Hold (shift) while clicking to");
  tooltip.add("view detailed information");
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

}
