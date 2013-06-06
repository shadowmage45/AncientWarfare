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

import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiTab;
import shadowmage.ancient_warfare.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerDummy;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterAZ;
import shadowmage.ancient_warfare.common.crafting.RecipeSorterTextFilter;
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
 * RECIPES
 *    searchable list of all (available?) recipes
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
  }

@Override
public void setupControls()
  {
  GuiTab tab = this.addGuiTab(1000, 3, 0, 50, 24, "Info");
  this.activeTab = tab;
  this.tabs.add(tab);
  tab = this.addGuiTab(1001, 3+50, 0, 50, 24, "Vehicles");
  tab.enabled = false;
  this.tabs.add(tab);
  tab = this.addGuiTab(1002, 3+50+50, 0, 50, 24, "Ammo");
  tab.enabled = false;
  this.tabs.add(tab);
  tab = this.addGuiTab(1003, 3+50+50+50, 0, 50, 24, "Npcs");
  tab.enabled = false;
  this.tabs.add(tab);
  tab = this.addGuiTab(1004, 3+50+50+50+50, 0, 50, 24, "Civics");
  tab.enabled = false;
  this.tabs.add(tab);
    
  int y = this.getYSize()-24;
  tab = this.addGuiTab(1005, 3, y, 60, 24, "Research");
  tab.enabled = false;
  tab.inverted = true;
  this.tabs.add(tab);
  tab = this.addGuiTab(1006, 3+60, y, 60, 24, "Recipes");
  tab.enabled = false;
  tab.inverted = true;
  this.tabs.add(tab);
  tab = this.addGuiTab(1007, 3+60+60, y, 80, 24, "Structures");
  tab.enabled = false;
  tab.inverted = true;
  this.tabs.add(tab);
  tab = this.addGuiTab(1008, 3+60+60+80, y, 50, 24, "Misc");
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
  for(GuiTab tab : this.tabs)
    {
    this.guiElements.put(tab.getElementNumber(), tab);
    }
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).updateGuiPos(guiLeft, guiTop);
    }
  switch(this.activeTab.getElementNumber())
  {
  case 1000:
  break;
  case 1001:
  break;  
  case 1002:
  break;
  case 1003:
  break;
  case 1004:
  break;
  case 1005:
  break;
  case 1006:
  break;
  case 1007:
  break;
  case 1008:
  break;  
  }
  }

}
