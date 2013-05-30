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

import java.util.HashSet;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiTab;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;

public class GuiResearch extends GuiContainerAdvanced
{

GuiTab activeTab = null;

/**
 * @param container
 */
public GuiResearch(Container container)
  {
  super(container);
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
  return 210;
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
    case 100:
    this.drawKnownBackground();
    break;
    case 101:
    this.drawAvailableBackground();
    break;
    case 102:
    this.drawProgressBackground();
    break;
    }
    }
  }

public void drawKnownBackground()
  {
  PlayerEntry entry = PlayerTracker.instance().getClientEntry();
  int x = 5;
  int y = 5;
  for(IResearchGoal goal : entry.getKnownResearch())
    {
    this.drawStringGui(goal.getDisplayName(), x, y, 0xffffffff);
    y += 10;
    }  
  }

public void drawAvailableBackground()
  {
  PlayerEntry entry = PlayerTracker.instance().getClientEntry();
  int x = 5;
  int y = 5; 
  for(IResearchGoal goal : entry.getAvailableResearch())
    {
    this.drawStringGui(goal.getDisplayName(), x, y, 0xffffffff);
    y += 10;
    }
  }

public void drawProgressBackground()
  {
  
  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub
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
  }

HashSet<GuiTab> tabs = new HashSet<GuiTab>();

@Override
public void setupControls()
  {  
  GuiTab tab = this.addGuiTab(100, 5, -21, 90, 24, "Known");
  this.tabs.add(tab);
  this.activeTab = tab;
  tab = this.addGuiTab(101, 5+90, -21, 90, 24, "Available");
  tab.enabled = false;
  this.tabs.add(tab);
  tab = this.addGuiTab(102, 5+90+90, -21, 256-90-90-10, 24, "Progress");
  tab.enabled = false;
  this.tabs.add(tab);
  tab = this.addGuiTab(103, 5, this.getYSize()-3, 90, 24, "All Unknown");
  tab.enabled = false;
  tab.inverted = true;
  this.tabs.add(tab);
  }

@Override
public void updateControls()
  {
  this.guiElements.clear();
  for(GuiTab tab : this.tabs)
    {
    this.guiElements.put(tab.getElementNumber(), tab);
    }
  
  
  }

}
