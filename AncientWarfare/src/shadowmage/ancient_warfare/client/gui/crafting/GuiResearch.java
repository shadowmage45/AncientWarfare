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
import java.util.HashSet;
import java.util.List;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiTab;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.gui.info.GuiResearchGoal;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerDummy;
import shadowmage.ancient_warfare.common.container.ContainerResearch;
import shadowmage.ancient_warfare.common.research.GoalSorterAZ;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;

public class GuiResearch extends GuiContainerAdvanced
{

GuiTab activeTab = null;
ContainerResearch container;
IResearchGoal selectedGoal = null;
GoalSorterAZ sorterAZ = new GoalSorterAZ();
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
  String goal = null;
  if(container.goal!=null)
    {
    goal = container.goal.getDisplayName();
    }
  else if(this.selectedGoal!=null)
    {
    goal = this.selectedGoal.getDisplayName();
    }
  else
    {
    goal = "No Research";
    }
  this.drawStringGui("Current Selection: ", 8+18+5, 5+21+3, 0xffffffff);
  this.drawStringGui(goal, 8+18+5, 5+21+10+3, 0xffffffff);
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

@Override
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
  /**
   * draw fake stacks into slots
   */
  if(this.selectedGoal!=null && container.goal==null)
    {
    int x = 0;
    int y = 0;
    for(ItemStack stack : this.selectedGoal.getResearchResources())
      {
      if(x>=3)
        {
        x=0;
        y++;
        }      
      if(!this.container.researchSlots[y*3+x].getHasStack())
        {
        this.renderItemStack(stack, guiLeft + 8 + x*18+27, guiTop + 8+18+4+24 + y*18, mouseX, mouseY, true, true);        
        }   
      x++;   
      }
    }
  }

public void drawUnknownBackground()
  {
  
  }

public void drawKnownBackground()
  {
  
  }

public void drawAvailableBackground()
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
  float max = container.goal!=null ? container.goal.getResearchTime() : selectedGoal!=null ? selectedGoal.getResearchTime() : 0;
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
  if(this.selectedGoal!=null && this.container.playerEntry!=null && this.container.playerEntry.getKnownResearch().contains(selectedGoal))
    {
    this.selectedGoal = null;
    this.forceUpdate = true;
    }
  else if(this.container.playerEntry==null)
    {
    this.selectedGoal = null;
    this.forceUpdate = true;
    }  
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
  case 100://known -- no action?
  if(goals.contains(element))
    {    
    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
      {
      mc.displayGuiScreen(new GuiResearchGoal(new ContainerDummy(), ResearchGoal.getGoalByID(element.getElementNumber()-1000), this));
      }
    }
  break;
  
  case 101://available
  if(goals.contains(element))
    {    
    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
      {
      mc.displayGuiScreen(new GuiResearchGoal(new ContainerDummy(), ResearchGoal.getGoalByID(element.getElementNumber()-1000), this));
      }
    else if(this.container.goal==null)
      {
      IResearchGoal g = ResearchGoal.getGoalByID(element.getElementNumber()-1000);
      this.selectedGoal = g;
      }
    }
  break;
  
  case 102://progress
  if(element.getElementNumber()==1)//start button
    {
    this.container.handleGoalSelectionClient(selectedGoal);
    }
  else if(element.getElementNumber()==2)//stop button
    {
    Config.logDebug("sending stop from gui");
    this.container.handleGoalStopClient();
    }
  break;
  
  case 103://unknown
  if(goals.contains(element))
    {    
    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
      {
      mc.displayGuiScreen(new GuiResearchGoal(new ContainerDummy(), ResearchGoal.getGoalByID(element.getElementNumber()-1000), this));
      }
    }
  break;
  }
  }



HashSet<GuiTab> tabs = new HashSet<GuiTab>();

GuiScrollableArea area;
int buttonWidth = 176-10-12-10;

@Override
public void setupControls()
  {  
  GuiTab tab = this.addGuiTab(100, 5+60+60, 0, 40, 24, "Known");
  this.tabs.add(tab);
  tab.enabled = false;
  tab = this.addGuiTab(101, 5+60, 0, 60, 24, "Available");
  tab.enabled = false;
  this.tabs.add(tab);
  tab = this.addGuiTab(102, 5, 0, 60, 24, "Progress");
  this.tabs.add(tab);
  this.activeTab = tab;
  tab = this.addGuiTab(103, 5, this.getYSize()-24, 90, 24, "All Unknown");
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
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).updateGuiPos(guiLeft, guiTop);
    }  
  }

HashSet<GuiButtonSimple> goals = new HashSet<GuiButtonSimple>();

protected void addKnownControls()
  {
  if(container.playerEntry!=null)
    {    
    this.guiElements.put(0, area);
    this.addGoalButtons(container.playerEntry.getKnownResearch()); 
    }
  }

protected void addGoalButtons(List<IResearchGoal> goals)
  {
  Collections.sort(goals, sorterAZ);
  area.updateTotalHeight(goals.size()*18);
  GuiButtonSimple button;
  int x = 0;
  int y = 0;
  ArrayList<String> tooltip = new ArrayList<String>();
  tooltip.add("Hold (shift) while clicking to");
  tooltip.add("view detailed information");
  for(IResearchGoal goal : goals)
    {      
    button = new GuiButtonSimple(1000+goal.getGlobalResearchNum(), area, buttonWidth, 16, goal.getDisplayName());
    button.updateRenderPos(x, y);
    button.setTooltip(tooltip);
    area.addGuiElement(button);
    this.goals.add(button);
    y+=18;
    }
  }

protected void addAvailableControls()
  {  
  if(container.playerEntry!=null)
    {
    this.guiElements.put(0, area);
    if(container.playerEntry!=null)
      {    
      this.guiElements.put(0, area);
      this.addGoalButtons(container.playerEntry.getAvailableResearch()); 
      }
    } 
  }

protected void addProgressControls()
  {
  this.container.addSlots();
  GuiButtonSimple button = this.addGuiButton(1, this.getXSize()-40-18, 8+18+4+24, 35, 16, "Start");
  button.updateGuiPos(guiLeft, guiTop);
  if(container.goal!=null || selectedGoal==null)
    {
    button.enabled = false;    
    }
  button = this.addGuiButton(2, this.getXSize()-40-18, 8+18+4+24+16+2+16+2, 35, 16 , "Stop");
  button.updateGuiPos(guiLeft, guiTop);
  if(container.goal==null)
    {
    button.enabled= false;
    }
  }

protected void addUnknownControls()
  {
  goals.clear();
  if(container.playerEntry!=null)
    {
    this.guiElements.put(0, area);
    if(container.playerEntry!=null)
      {    
      this.guiElements.put(0, area);
      this.addGoalButtons( container.playerEntry.getUnknwonResearch()); 
      }
    } 
  }

}
