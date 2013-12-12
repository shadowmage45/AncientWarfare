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
import java.util.HashSet;
import java.util.List;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiElement;
import shadowmage.ancient_framework.client.gui.elements.GuiItemStack;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.GuiTab;
import shadowmage.ancient_framework.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.gui.info.GuiRecipeDetails;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerAWCrafting;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterTextFilter;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

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
GuiButtonSimple startButton = null;
GuiButtonSimple stopButton = null;
EnumSet recipeTypes = null;

/**
 * @param container
 */
public GuiCraftingTabbed(Container container)
  {
  super(container);
  this.container = (ContainerAWCrafting)container;
  this.shouldCloseOnVanillaKeys = true;
  }

public abstract EnumSet<RecipeType> getTab1RecipeTypes();
public abstract EnumSet<RecipeType> getTab2RecipeTypes();
public abstract String getTab1Label();
public abstract String getTab2Label();

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
  if(this.activeTab==this.craftingTab)
    {
    this.drawProgressBackground();
    }
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
public void drawExtraForeground(int mouseX, int mouseY, float partialTick)
  {
  if(this.activeTab!=null)
    {
    if(this.activeTab==this.craftingTab)
      {
      this.drawProgressForeground();
      }
    else if(this.activeTab==this.tab1)
      {
      
      }
    else if(this.activeTab==this.tab2)
      {
      
      }
    }
  }

public void drawProgressForeground()
  {
  int lastx = 0;
  int lasty = 0;
  boolean last = false;
  int lastslot = -1;
  if(this.container.clientRecipe!=null && !this.container.isWorking)
    {
    int x = 0;
    int y = 0;
    int id = 0;
    for(ItemStackWrapperCrafting stack : this.container.clientRecipe.getResourceList())
      {
      if(x>=3)
        {
        x=0;
        y++;
        }      
      if(!this.container.getSlot(36 + y*3+x).getHasStack())
        {
        if(isMouseInRawArea(guiLeft+8+x*18, guiTop + 8 + 24 + 18 + 4 + 18 + 4 + y*18, 16, 16, mouseX, mouseY))
          {
//          this.renderItemStack(stack.getFilter(), guiLeft + 8 + x * 18, guiTop + 8 + 24 + 18 + 4 + 18 + 4 + y*18, mouseX, mouseY, true, false);
          last = true;
          lastx = guiLeft+8+x*18;
          lasty = guiTop + 8 + 24 + 18 + 4 + 18 + 4 + y*18;
          //lastslot = 36 + y*3 +x;
          lastslot = id;
          }
        else
          {
          this.renderItemStack(stack.getFilter(), guiLeft + 8 + x * 18, guiTop + 8 + 24 + 18 + 4 + 18 + 4 + y*18, mouseX, mouseY, true, true);          
          }        
        }   
      x++;   
      id++;
      }
    if(last)
      {
      ItemStackWrapperCrafting item = this.container.clientRecipe.getResourceList().get(lastslot);
      this.renderItemStack(item.getFilter(), lastx, lasty, mouseX, mouseY, true, true);
      }
    }
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
  else if(this.activeTab==this.tab1 || this.activeTab==this.tab2)
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
  }

protected void handleRecipeClick(IGuiElement element)
  {  
  if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
    {
    this.handleRecipeDetailsClick(recipes.get(element));          
    }
  else
    {
    if(container.isWorking || container.isLocked)
      {
      return;
      }    
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("set", true);
    tag.setCompoundTag("result", ((ResourceListRecipe)this.recipes.get(element)).getResult().writeToNBT(new NBTTagCompound()));
    this.sendDataToServer(tag);
    }
  }

protected void handleRecipeDetailsClick(ResourceListRecipe recipe)
  {
  mc.displayGuiScreen(new GuiRecipeDetails(this, recipe));
  }

@Override
public void setupControls()
  { 
  GuiTab tab = this.addGuiTab(1000, 8, 0, 40, 24, "Craft");
  this.tabs.add(tab);
  this.activeTab = tab;
  this.craftingTab = tab;
  String name = this.getTab1Label();
  if(name!=null)
    {
    tab = this.addGuiTab(1001, 8+40, 0, 60, 24, name);
    tab.enabled = false;
    this.tabs.add(tab);  
    this.tab1 = tab;
    }
  name = this.getTab2Label();
  if(name!=null)
    {
    tab = this.addGuiTab(1002, 8+40+60, 0, 60, 24, name);
    tab.enabled = false;
    this.tabs.add(tab);
    this.tab2 = tab;
    }
  this.searchBox = (GuiTextInputLine) new GuiTextInputLine(2, this, 176-16, 12, 30, "").updateRenderPos(8, 29);
  searchBox.selected = false;
  this.area = new GuiScrollableArea(0, this, 5, 21+18+10+5+18, 176-10, 240-21-10-18-5-8-18, 0);
  this.forceUpdate = true;
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
    this.handleSearchBoxUpdate();
    }  
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).updateGuiPos(guiLeft, guiTop);
    }
  }

protected void addCurrentRecipeElements()
  {
  ItemStack result = container.clientRecipe.getResult();
  String name = result.getDisplayName();
  if(name.length()>20)
    {
    name = name.substring(0, 20);
    }
  int x = 8;
  int y = 24 + 8 + 18 + 4;  
  GuiItemStack stack = new GuiItemStack(2, this).setItemStack(result).setRenderName(true);
  stack.updateRenderPos(x, y); 
  stack.renderTooltip = false;
  this.guiElements.put(2, stack);
  }

protected void addProgressButtons()
  {
  int x = 8 + 3* 18 + 18 + 27;
  int y = 8 + 24 +  4 + 18 + 4 + 18;
  this.startButton = new GuiButtonSimple(3, this, 36, 16, "Start");
  this.startButton.updateRenderPos(x, y);
  this.stopButton = new GuiButtonSimple(4, this, 36, 16, "Stop");
  this.stopButton.updateRenderPos(x, y+36);
  this.guiElements.put(3, startButton);
  this.guiElements.put(4, stopButton);
  if(container.isLocked || container.clientRecipe==null)
    {
    startButton.enabled = false;
    }
  else
    {
    stopButton.enabled = false;
    }
  if(container.clientRecipe==null)
    {
    stopButton.enabled = false;
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
    button = new GuiButtonSimple(num, area, buttonWidth, 16, StatCollector.translateToLocal(recipe.getLocalizedDisplayName()));
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
  if(!this.searchBox.selected)
    {
    super.keyTyped(par1, par2);
    }
  else
    {
    for(Integer i : this.guiElements.keySet())
      {
      GuiElement el = this.guiElements.get(i);
      el.onKeyTyped(par1, par2);
      }
    this.handleSearchBoxUpdate();
    }
  }
}
