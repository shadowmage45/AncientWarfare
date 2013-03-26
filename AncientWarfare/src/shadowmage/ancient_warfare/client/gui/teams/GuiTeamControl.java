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
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry;

public class GuiTeamControl extends GuiContainerAdvanced
{

TeamEntry entry = null;
GuiScrollableArea area;
int prevMemberCount = 0;

/**
 * @param container
 */
public GuiTeamControl(Container container)
  {
  super(container);
  this.shouldCloseOnVanillaKeys = true;
  entry = TeamTracker.instance().getTeamEntryFor(player);
  this.prevMemberCount = entry.memberNames.size();
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  switch(element.getElementNumber())
  {
  case 0:
  this.closeGUI();
  break;
  
  default:
  break;
  }
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
public void setupControls()
  {
  this.addGuiButton(0, 45, 12, "Done").updateRenderPos(getXSize()-45-5, 5);
  
  int buffer = 2;
  int buttonSize = 8;
  int keyBindCount = this.entry.memberNames.size();
  int totalHeight = keyBindCount * (buffer+buttonSize);  
  area = new GuiScrollableArea(1, this, 10, 30, this.getXSize()-20, this.getYSize()-40, totalHeight);
  this.guiElements.put(1, area);
  
  int kX = 5;
  int kY = 0;
  for(int i = 0; i < keyBindCount; i++)
    {
    kY = i * (buffer+buttonSize);
    area.addGuiElement(new GuiString(i+2, area, this.getXSize()-30, 8, this.entry.memberNames.get(i)).updateRenderPos(kX, kY));
    }
  }

@Override
public void updateControls()
  {
  area.elements.clear();  
  int buffer = 2;
  int buttonSize = 10;
  int keyBindCount = this.entry.memberNames.size();
  int totalHeight = keyBindCount * (buffer+buttonSize); 
  area.updateTotalHeight(totalHeight);
  int kX = 5;
  int kY = 0;
  for(int i = 0; i < keyBindCount; i++)
    {
    kY = i * (buffer+buttonSize);
    area.addGuiElement(new GuiString(i+2, area, this.getXSize()-30, buttonSize, this.entry.memberNames.get(i)).updateRenderPos(kX, kY));
    }
  }

}
