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
package shadowmage.ancient_warfare.client.gui.teams;

import java.util.HashSet;

import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry.TeamMemberEntry;

public class GuiTeamControlAdvanced extends GuiContainerAdvanced
{

TeamEntry entry;
GuiTeamControl parent;
int prevMemberCount = 0;

private GuiNumberInputLine teamSelectNumber;
private GuiScrollableArea area;
private GuiScrollableArea area2;

/**
 * @param container
 */
public GuiTeamControlAdvanced(GuiTeamControl parent, Container container)
  {
  super(container);
  this.parent = parent;
  this.entry = parent.entry;
  this.shouldCloseOnVanillaKeys = false;
  this.prevMemberCount = entry.memberNames.size();
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
  this.drawCenteredString(fontRenderer, "Current Team: "+entry.teamNum, guiLeft+(getXSize()/2), guiTop+5, 0xffffffff);  
  this.drawCenteredString(fontRenderer, "Current Rank: "+entry.getPlayerRank(player.getEntityName()), guiLeft+(getXSize()/2), guiTop+15, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  if(entry.memberNames.size()!=prevMemberCount)
    {
    this.prevMemberCount = entry.memberNames.size();
    this.forceUpdate = true;
    }
  area.updateGuiPos(guiLeft, guiTop);
  area2.updateGuiPos(guiLeft,  guiTop);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  switch(element.getElementNumber())
  {
  case 0:
  mc.displayGuiScreen(parent);
  return;
    
  default:
  break;
  }
  
  TeamMemberEntry entry;
  if(this.rankMinusButtons.contains(element))
    {
    entry = this.entry.memberNames.get(element.getElementNumber()-100);
    if(entry.getMemberRank()-1 >= 0 && !entry.getMemberName().equals(player.getEntityName()))
      {
      TeamTracker.instance().handleClientRankChange(entry.getMemberName(), (byte)this.entry.teamNum, (byte) (entry.getMemberRank()-1));
      }
    }
  else if(this.rankPlusButtons.contains(element))
    {
    entry = this.entry.memberNames.get(element.getElementNumber()-100);
    if(entry.getMemberRank()+1 < this.entry.getPlayerRank(player.getEntityName()) && !entry.getMemberName().equals(player.getEntityName()))
      {
      TeamTracker.instance().handleClientRankChange(entry.getMemberName(), (byte)this.entry.teamNum, (byte) (entry.getMemberRank()+1));
      }
    }
  else if(this.kickMemberButtons.contains(element))
    {    
    entry = this.entry.memberNames.get(element.getElementNumber()-100);
    TeamTracker.instance().handleClientApplyToTeam(entry.getMemberName(), (byte)0);
    }
  else if(this.acceptButtons.contains(element))
    {
    TeamTracker.instance().handleClientAppAction((byte)this.entry.teamNum, this.entry.applicants.get(element.getElementNumber()-100), true);
    }
  else if(this.denyButtons.contains(element))
    {
    TeamTracker.instance().handleClientAppAction((byte)this.entry.teamNum, this.entry.applicants.get(element.getElementNumber()-100), false);    
    }
  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, 45, 12, "Done").updateRenderPos(getXSize()-45-5, 5);
  this.addNumberField(2, 35, 12, 1, String.valueOf(entry.teamNum)).setMinMax(0, 15).updateRenderPos(5, 5+12+2);
  
  int buffer = 2;
  int buttonSize = 12;
  
  int entryCount = this.entry.memberNames.size();
  int totalHeight = entryCount * (buffer+buttonSize);
  
  int totalHeight2 = this.entry.applicants.size() * (buffer+buttonSize);
  area = new GuiScrollableArea(1, this, 10, 65, this.getXSize()-20, this.getYSize()-80-80, totalHeight);
  area2 = new GuiScrollableArea(2, this, 10, 70+80, this.getXSize()-20, this.getYSize()-80-80, totalHeight2);
  this.guiElements.put(1, area);
  this.guiElements.put(2, area2);
  
  TeamMemberEntry entry;
  for(int i = 0; i < entryCount; i++)
    {
    entry = this.entry.memberNames.get(i);
    this.addTeamControlButtons(i, entry);
    }  
  for(int i = 0; i < this.entry.applicants.size(); i++)
    {
    this.addTeamApplicantButtons(i);
    }
  }

private void addTeamControlButtons(int index, TeamMemberEntry entry)
  {
  int y = index * (2+12);
  GuiString name = (GuiString) new GuiString(-1, area,this.getXSize()-30, 12, entry.getMemberName() + "  Rank: "+entry.getMemberRank()).updateRenderPos(5, y);
  GuiButtonSimple button = (GuiButtonSimple) new GuiButtonSimple(100+index, area, 16, 12, "<").updateRenderPos(256-20-16-2-35-2-16-5-12, y);
  GuiButtonSimple button1 = (GuiButtonSimple) new GuiButtonSimple(100+index, area, 35, 12, "Kick").updateRenderPos(256-20-16-2-35-5-12, y);
  GuiButtonSimple button2 = (GuiButtonSimple) new GuiButtonSimple(100+index, area, 16, 12, ">").updateRenderPos(256-20-16-5-12-1, y);
  area.addGuiElement(name);
  area.addGuiElement(button);
  area.addGuiElement(button1);
  area.addGuiElement(button2);
  rankMinusButtons.add(button);
  rankPlusButtons.add(button2);
  kickMemberButtons.add(button1);
  }

private void addTeamApplicantButtons(int index)
  {
  int y = index * (2+12);
  GuiString name = (GuiString) new GuiString(-1, area,this.getXSize()-30, 12, entry.applicants.get(index)).updateRenderPos(5, y);
  GuiButtonSimple button = (GuiButtonSimple) new GuiButtonSimple(100+index, area, 45, 12, "Accept").updateRenderPos(256-20-5-12-45-2-45, y);  
  GuiButtonSimple button2 = (GuiButtonSimple) new GuiButtonSimple(100+index, area, 45, 12, "Deny").updateRenderPos(256-20-5-12-45, y);
  area2.addGuiElement(name);
  area2.addGuiElement(button);  
  area2.addGuiElement(button2);
  acceptButtons.add(button);
  denyButtons.add(button2);  
  }

HashSet<GuiButtonSimple> rankMinusButtons = new HashSet<GuiButtonSimple>();
HashSet<GuiButtonSimple> rankPlusButtons = new HashSet<GuiButtonSimple>();
HashSet<GuiButtonSimple> kickMemberButtons = new HashSet<GuiButtonSimple>();

HashSet<GuiButtonSimple> acceptButtons = new HashSet<GuiButtonSimple>();
HashSet<GuiButtonSimple> denyButtons = new HashSet<GuiButtonSimple>();

@Override
public void updateControls()
  {
  area.elements.clear();
  rankMinusButtons.clear();
  rankPlusButtons.clear();
  kickMemberButtons.clear(); 
  acceptButtons.clear();
  denyButtons.clear();
  int buffer = 2;
  int buttonSize = 12;
  int entryCount = this.entry.memberNames.size();
  int totalHeight = entryCount * (buffer+buttonSize); 
  area.updateTotalHeight(totalHeight);
  area2.updateTotalHeight((buffer+buttonSize)*this.entry.applicants.size());
  TeamMemberEntry entry;
  for(int i = 0; i < entryCount; i++)
    {
    entry = this.entry.memberNames.get(i);
    this.addTeamControlButtons(i, entry);
    }  
  for(int i = 0; i < this.entry.applicants.size(); i++)
    {
    this.addTeamApplicantButtons(i);
    }
  }

@Override
public void handleDataFromContainer(NBTTagCompound tag)
  {  
  if(tag.hasKey("rebuild"))
    {
    this.rebuildTeamInfo();
    this.forceUpdate = true;    
    }  
  }

private void rebuildTeamInfo()
  {
  this.entry = TeamTracker.instance().getTeamEntryFor(player);
  }
}
