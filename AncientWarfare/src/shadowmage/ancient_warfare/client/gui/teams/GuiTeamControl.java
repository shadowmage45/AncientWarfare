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
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerTeamControl;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry.TeamMemberEntry;

public class GuiTeamControl extends GuiContainerAdvanced
{

GuiButtonSimple alliances;
GuiButtonSimple applicants;
GuiButtonSimple done;
GuiScrollableArea area;

TeamEntry entry;
int adminRank = 0;

ContainerTeamControl container;

public GuiTeamControl(Container container)
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
  // TODO Auto-generated method stub
  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub
  }

@Override
public void setupControls()
  {
  this.entry = TeamTracker.instance().getTeamEntryFor(player);
  this.done = this.addGuiButton(0, getXSize()-55-8, 8, 55, 16, "Back");
  this.applicants = this.addGuiButton(1, 8, 8, 75, 16, "Applicants");
  this.alliances = this.addGuiButton(2, 8+75+4, 8, 55, 16, "Alliances");
  this.area = new GuiScrollableArea(3, this, 8, 8+16+4, getXSize()-16, getYSize()-16-16-4, 0);
  this.guiElements.put(3, area);
  }

@Override
public void updateControls()
  {
  this.area.elements.clear();
  this.rankDownButtons.clear();
  this.rankUpButtons.clear();
  this.kickButtons.clear();
  
  this.entry = TeamTracker.instance().getTeamEntryFor(player);
  this.adminRank = entry.getPlayerRank(player.getEntityName());
  
  int targetY = 0;  
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 12, "Team: "+entry.teamNum + " Your Rank: "+adminRank).updateRenderPos(0, targetY));
  targetY+=14;
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 12, "Team Members:").updateRenderPos(0, targetY));
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 12, "Rank:").updateRenderPos(130, targetY));
  targetY+=16;
  
  for(TeamMemberEntry member : entry.members)
    {
    addTeamMemberEntry(member.getMemberName(), targetY);
    targetY+=14;
    }  
  }

private HashMap<IGuiElement, String> rankDownButtons = new HashMap<IGuiElement, String>();
private HashMap<IGuiElement, String> kickButtons = new HashMap<IGuiElement, String>();
private HashMap<IGuiElement, String> rankUpButtons = new HashMap<IGuiElement, String>();

protected void addTeamMemberEntry(String name, int targetY)
  {
  int memberRank = this.entry.getPlayerRank(name);
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 10, name).updateRenderPos(0, targetY));
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 10, String.valueOf(memberRank)).updateRenderPos(140, targetY));
  
  GuiButtonSimple button;
  if(memberRank<adminRank)
    {
    if(memberRank>0)
      {
      button = new GuiButtonSimple(0, area, 12, 12, "-");
      button.updateRenderPos(160, targetY);
      area.elements.add(button);
      rankDownButtons.put(button, name);      
      }
    
    button = new GuiButtonSimple(0, area, 35, 12, "Kick");
    button.updateRenderPos(160+12+2, targetY);
    area.elements.add(button);
    kickButtons.put(button, name);
    
    if(memberRank < adminRank+1)
      {
      button = new GuiButtonSimple(0, area, 12, 12, "+");
      button.updateRenderPos(160+12+2+35+2, targetY);
      area.elements.add(button);
      rankUpButtons.put(button, name);
      }
    }
   
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==done)
    {
    mc.displayGuiScreen(new GuiTeamView(this.container));
    }
  else if(element==applicants)
    {
    mc.displayGuiScreen(new GuiTeamApplicant(this.container));
    }
  else if(element==alliances)
    {
    mc.displayGuiScreen(new GuiTeamAlliance(this.container));
    }
  else if(rankDownButtons.containsKey(element))
    {
    handleRankDownPress(rankDownButtons.get(element));
    }
  else if(rankUpButtons.containsKey(element))
    {
    handleRankUpPress(rankUpButtons.get(element));
    }
  else if(kickButtons.containsKey(element))
    {
    handleKickPress(kickButtons.get(element));
    }
  }

private void handleRankDownPress(String name)
  {
  int memberRank = entry.getPlayerRank(name);
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", name);
  tag.setInteger("rank", memberRank-1);
  this.sendDataToServer(tag);
  }

private void handleRankUpPress(String name)
  {
  int memberRank = entry.getPlayerRank(name);
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", name);
  tag.setInteger("rank", memberRank+1);
  this.sendDataToServer(tag);
  }

private void handleKickPress(String name)
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", name);
  tag.setBoolean("kick", true);
  this.sendDataToServer(tag);
  }


}
