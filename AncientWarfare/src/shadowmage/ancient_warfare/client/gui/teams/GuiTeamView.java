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
package shadowmage.ancient_warfare.client.gui.teams;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerTeamControl;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry.TeamMemberEntry;

public class GuiTeamView extends GuiContainerAdvanced
{

GuiButtonSimple admin;
GuiButtonSimple change;
GuiButtonSimple done;
GuiScrollableArea area;
TeamEntry entry;

ContainerTeamControl container;

public GuiTeamView(Container container)
  {
  super(container);
  this.container = (ContainerTeamControl)container;
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
public void setupControls()
  {
  this.entry = TeamTracker.instance().getTeamEntryFor(player);
  this.done = this.addGuiButton(0, getXSize()-55-8, 8, 55, 16, "Back");
  this.change = this.addGuiButton(1, 8, 8, 75, 16, "Change Teams");
  this.admin = this.addGuiButton(2, 8+75+4, 8, 55, 16, "Admin");
  this.admin.hidden = true;
  this.area = new GuiScrollableArea(3, this, 8, 8+16+4, getXSize()-16, getYSize()-16-16-4, 0);
  this.guiElements.put(3, area);
  }

@Override
public void updateControls()
  {
  this.area.elements.clear();
  
  this.entry = TeamTracker.instance().getTeamEntryFor(player);
  int rank = entry.getPlayerRank(player.getEntityName());
  this.admin.hidden = rank < 7;
  
  int targetY = 0;
  
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 12, "Team: "+entry.teamNum + " Your Rank: "+rank).updateRenderPos(0, targetY));
  targetY+=14;
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 12, "Team Members:").updateRenderPos(0, targetY));
  targetY+=16;
  
  for(TeamMemberEntry member : entry.memberNames)
    {
    addTeamMemberEntry(member.getMemberName(), targetY);
    targetY+=12;
    }  
  }

protected void addTeamMemberEntry(String name, int targetY)
  {
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 10, name).updateRenderPos(0, targetY));
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==done)
    {
    this.closeGUI();
    }
  else if(element==change)
    {
    mc.displayGuiScreen(new GuiTeamChange(this.container));
    }
  else if(element==admin)
    {
    mc.displayGuiScreen(new GuiTeamControl(this.container));
    }
  }

}
