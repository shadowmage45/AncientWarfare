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

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.research.IResearchGoal;

public class GuiResearchGoal extends GuiContainerAdvanced
{
GuiContainerAdvanced parent;
IResearchGoal goal;

/**
 * @param container
 */
public GuiResearchGoal(Container container, IResearchGoal goal, GuiContainerAdvanced parent)
  {
  super(container);
  this.goal = goal;
  this.parent = parent;
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
  this.area.updateGuiPos(guiLeft, guiTop);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==back)
    {
    mc.displayGuiScreen(parent);
    }  
  }

GuiScrollableArea area;
GuiButtonSimple back;

@Override
public void setupControls()
  {
  this.area = new GuiScrollableArea(0, this, 5, 5+16+5, 256-10, 240-10-16-5, this.goal.getDetailedDescription().size()*10);
  int y = 0;
  for(String st : this.goal.getDetailedDescription())
    {
    Config.logDebug("adding description line: "+st);
    area.addGuiElement(new GuiString(y+100, area, 240-24, 10, st).updateRenderPos(0, y*10));
    y++;
    }
  back = this.addGuiButton(1, this.getXSize()-40, 5, 35, 16, "Back");
  this.guiElements.put(0, area);
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub
  }


}
