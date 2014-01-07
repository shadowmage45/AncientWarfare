/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_framework.client.gui.teams;

import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.GuiTab;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.client.render.RenderTools;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.ancient_framework.common.container.ContainerTeamControl;
import shadowmage.ancient_framework.common.teams.TeamEntry;
import shadowmage.ancient_framework.common.teams.TeamTracker;

public class GuiTeamControl extends GuiContainerAdvanced
{

GuiTab teamTab;
GuiTab changeTab;
GuiTab adminTab;

GuiScrollableArea area;


ContainerTeamControl container;

public GuiTeamControl(ContainerBase container)
  {
  super(container);
  this.container = (ContainerTeamControl) container;
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
    RenderTools.drawQuadedTexture(guiLeft, guiTop+13, this.xSize, this.ySize-13, 256, 240, tex, 0, 0);
    }
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
public void setupControls()
  {  
  area = new GuiScrollableArea(0, this, 8, 8+16, 256-16, 240-16-16, 240-16-16);
  this.guiElements.put(0, area);
  this.teamTab = addGuiTab(1, 3, 0, 78, 16, "Current Team");
  teamTab.enabled = true;
  this.changeTab = addGuiTab(2, 78+3, 0, 78, 16, "Change Teams");
  changeTab.enabled = false;  
  this.adminTab = addGuiTab(3, 3+78+78, 0, 78, 16, "Team Admin");
  adminTab.enabled = false;
//  if(this.container.currentTeamEntry.getRankOf(container.player.getEntityName())<7)
//    {
//    adminTab.hidden = true;
//    }
  }

@Override
public void updateControls()
  {

  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==this.teamTab || element==this.changeTab || element==this.adminTab)
    {
    teamTab.enabled = element==teamTab;
    changeTab.enabled = element==changeTab;
    adminTab.enabled = element==adminTab;
    }
  }
}
