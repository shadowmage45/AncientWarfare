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

import java.util.HashMap;

import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiElement;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerTeamControl;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry;

public class GuiTeamApplicant extends GuiContainerAdvanced
{

GuiButtonSimple done;
GuiScrollableArea area;
TeamEntry entry;

ContainerTeamControl container;

public GuiTeamApplicant(Container container)
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
  this.area = new GuiScrollableArea(3, this, 8, 8+16+4, getXSize()-16, getYSize()-16-16-4, 0);
  this.guiElements.put(3, area);
  }

@Override
public void updateControls()
  {
  this.area.elements.clear();
  
  this.entry = TeamTracker.instance().getTeamEntryFor(player);
  int rank = entry.getPlayerRank(player.getEntityName());
  int targetY = 0;
  
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 12, "Team: "+entry.teamNum + " Your Rank: "+rank).updateRenderPos(0, targetY));
  targetY+=14;
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 12, "Team Applicants:").updateRenderPos(0, targetY));
  targetY+=16;
  
  for(String name: entry.applicants)
    {
    addTeamMemberEntry(name, targetY);
    targetY+=14;
    }  
  }

private HashMap<GuiElement, String> acceptButtons = new HashMap<GuiElement, String>();
private HashMap<GuiElement, String> denyButtons = new HashMap<GuiElement, String>();

protected void addTeamMemberEntry(String name, int targetY)
  {
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 10, name).updateRenderPos(0, targetY));
  
  GuiButtonSimple button;
  
  button = new GuiButtonSimple(0, area, 40, 12, "Accept");
  button.updateRenderPos(120, targetY);
  acceptButtons.put(button, name);
  area.elements.add(button);
  
  button = new GuiButtonSimple(0, area, 40, 12, "Deny");
  button.updateRenderPos(120+40+4, targetY);
  denyButtons.put(button, name);
  area.elements.add(button);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==done)
    {
    mc.displayGuiScreen(new GuiTeamControl(container));
    }
  else if(acceptButtons.containsKey(element))
    {
    handleAcceptPress(acceptButtons.get(element));
    }
  else if(denyButtons.containsKey(element))
    {
    handleDenyPress(denyButtons.get(element));
    }
  }

private void handleAcceptPress(String name)
  {
  handleAcceptAction(name, true);
  player.addChatMessage("Accepted application for: "+name);
  }

private void handleDenyPress(String name)
  {
  handleAcceptAction(name, false);
  player.addChatMessage("Denied application for: "+name);
  }

private void handleAcceptAction(String name, boolean accept)
  {
  int team = entry.teamNum;
  NBTTagCompound tag = new NBTTagCompound();
  tag.setBoolean("accept", accept);
  tag.setInteger("team", team);
  tag.setString("name", name);
  this.sendDataToServer(tag);
  }

}
