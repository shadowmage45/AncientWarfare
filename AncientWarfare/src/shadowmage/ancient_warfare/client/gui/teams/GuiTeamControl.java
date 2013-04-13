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

import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiNumberInputLine;
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

TeamEntry entry = null;
GuiScrollableArea area;
int prevMemberCount = 0;
private ContainerTeamControl container;
private GuiNumberInputLine teamSelectNumber;

/**
 * @param container
 */
public GuiTeamControl(Container container)
  {
  super(container);
  this.container = (ContainerTeamControl)container;
  this.shouldCloseOnVanillaKeys = true;
  entry = TeamTracker.instance().getTeamEntryFor(player);
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
  return "/shadowmage/ancient_warfare/resources/gui/guiBackgroundLarge.png";
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
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  switch(element.getElementNumber())
  {
  case 0:
  this.closeGUI();
  break;
  
  case 5:
  mc.displayGuiScreen(new GuiTeamControlAdvanced(this, inventorySlots));
  break;
  
  case 10://apply
  byte num = (byte) teamSelectNumber.getIntVal();
  Config.logDebug("sending application packet, newteam: "+num);
  TeamTracker.instance().handleClientApplyToTeam(player.getEntityName(), num);
  break;
  
  
  default:
  break;
  }
  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, 45, 12, "Done").updateRenderPos(getXSize()-45-5, 5);
  this.addGuiButton(5, getXSize()-75-5, 20, 75, 12, "Adv Controls");
  this.addGuiButton(6, 5, 5, 65, 12, "Change Team");
  this.addGuiButton(7, 5, 20, 12, 12, "-");
  this.teamSelectNumber = (GuiNumberInputLine) this.addNumberField(8, 45, 12, 1, "0").setMinMax(0, 15).updateRenderPos(5+12+2, 20);
  this.addGuiButton(9, 5+12+45+4, 20, 12, 12, "+");
  this.addGuiButton(10, 5, 40, 85, 12, "Apply To Team");
    
  int buffer = 2;
  int buttonSize = 8;
  int entryCount = this.entry.memberNames.size();
  int totalHeight = entryCount * (buffer+buttonSize);  
  area = new GuiScrollableArea(1, this, 10, 70, this.getXSize()-20, this.getYSize()-80, totalHeight);
  this.guiElements.put(1, area);
  
  int kX = 5;
  int kY = 0;
  TeamMemberEntry entry;
  for(int i = 0; i < entryCount; i++)
    {
    kY = i * (buffer+buttonSize);
    entry = this.entry.memberNames.get(i);
    area.addGuiElement(new GuiString(i+20, area, this.getXSize()-30, buttonSize, entry.getMemberName() + "  Rank: "+entry.getMemberRank()).updateRenderPos(kX, kY));
    }
  }

@Override
public void updateControls()
  {
  area.elements.clear();  
  int buffer = 2;
  int buttonSize = 8;
  int entryCount = this.entry.memberNames.size();
  int totalHeight = entryCount * (buffer+buttonSize); 
  area.updateTotalHeight(totalHeight);
  int kX = 5;
  int kY = 0;
  TeamMemberEntry entry;
  for(int i = 0; i < entryCount; i++)
    {
    kY = i * (buffer+buttonSize);
    entry = this.entry.memberNames.get(i);
    area.addGuiElement(new GuiString(i+20, area, this.getXSize()-30, buttonSize, entry.getMemberName() + "  Rank: "+entry.getMemberRank()).updateRenderPos(kX, kY));
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
