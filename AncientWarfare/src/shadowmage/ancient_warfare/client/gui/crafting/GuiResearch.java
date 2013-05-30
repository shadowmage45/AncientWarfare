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
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiTab;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerResearch;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.tracker.PlayerTracker;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;

public class GuiResearch extends GuiContainerAdvanced
{

GuiTab activeTab = null;
ContainerResearch container;
/**
 * @param container
 */
public GuiResearch(Container container)
  {
  super(container);
  this.shouldCloseOnVanillaKeys = true;
  this.container = (ContainerResearch)container;
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
  this.drawStringGui(container.goal==null? "No Research" : container.goal.getDisplayName(), -50, 0, 0xffffffff);
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
    case 103:
    this.drawUnknownBackground();
    break;
    }
    }
  }

public void drawExtraForeground(int mouseX, int mouseY, float partialTick)
  {
  if(this.activeTab!=null)
    {
    switch(activeTab.getElementNumber())
    {
    case 100:
    break;
    case 101:
    break;
    case 102:
    this.drawProgressForground();
    break;
    case 103:
    break;
    }
    }
  }

public void drawProgressForground()
  {
  
  }

public void drawUnknownBackground()
  {
  
  }

public void drawKnownBackground()
  {
//  PlayerEntry entry = PlayerTracker.instance().getClientEntry();
//  int x = 5;
//  int y = 25;
//  for(IResearchGoal goal : entry.getKnownResearch())
//    {
//    this.drawStringGui(goal.getDisplayName(), x, y, 0xffffffff);
//    y += 10;
//    }  
  }

public void drawAvailableBackground()
  {
  PlayerEntry entry = PlayerTracker.instance().getClientEntry();
  int x = 5;
  int y = 25; 
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
  this.area.updateGuiPos(guiLeft, guiTop);
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

GuiScrollableArea area;

@Override
public void setupControls()
  {  
  GuiTab tab = this.addGuiTab(100, 5, 0, 90, 24, "Known");
  this.tabs.add(tab);
  this.activeTab = tab;
  tab = this.addGuiTab(101, 5+90, 0, 90, 24, "Available");
  tab.enabled = false;
  this.tabs.add(tab);
  tab = this.addGuiTab(102, 5+90+90, 0, 256-90-90-10, 24, "Progress");
  tab.enabled = false;
  this.tabs.add(tab);
  tab = this.addGuiTab(103, 5, this.getYSize()-24, 90, 24, "All Unknown");
  tab.enabled = false;
  tab.inverted = true;
  this.tabs.add(tab);
  this.area = new GuiScrollableArea(0, this, 5, 21+18+10, 256-10, 240-42-10-18, 0);
  }

@Override
public void updateControls()
  {
  this.guiElements.clear();
  this.area.elements.clear();
  this.goals.clear();
  for(GuiTab tab : this.tabs)
    {
    this.guiElements.put(tab.getElementNumber(), tab);
    }
  this.container.removeSlots();
  if(this.activeTab!=null)
    {
    switch(activeTab.getElementNumber())
    {
    case 100://known
    this.addKnownControls();
    break;
    case 101://avail
    this.addAvailableControls();
    break;
    case 102://progress
    this.addProgressControls();
    break;
    case 103://unknown
    this.addUnknownControls();
    break;
    }
    }  
  }

HashSet<GuiButtonSimple> goals = new HashSet<GuiButtonSimple>();

protected void addKnownControls()
  {
  goals.clear();
  if(container.playerEntry!=null)
    {    
    this.guiElements.put(0, area);
    HashSet<IResearchGoal> goals = container.playerEntry.getKnownResearch();
    area.updateTotalHeight(goals.size()*18);
    int x = 0;
    int y = 0;
    GuiButtonSimple button;
    for(IResearchGoal goal : goals)
      {      
      button = new GuiButtonSimple(goal.getGlobalResearchNum(), area, 256-10, 16, goal.getDisplayName());
      button.updateRenderPos(x, y);
      area.addGuiElement(button);
      this.goals.add(button);
      y+=18;
      }
    }
  }

protected void addAvailableControls()
  {
  this.guiElements.put(0, area);
  }

protected void addProgressControls()
  {
  this.container.addSlots();
  }

protected void addUnknownControls()
  {
  this.guiElements.put(0, area);
  }

}
