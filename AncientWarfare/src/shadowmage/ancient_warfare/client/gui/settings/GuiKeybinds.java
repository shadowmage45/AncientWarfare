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
package shadowmage.ancient_warfare.client.gui.settings;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;

public class GuiKeybinds extends GuiContainerAdvanced
{

GuiContainerAdvanced parentGui;
GuiScrollableArea area;


/**
 * @param container
 */
public GuiKeybinds(Container container, GuiContainerAdvanced parent)
  {
  super(container);
  this.parentGui = parent;
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
  // TODO Auto-generated method stub
  return null;
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  area.drawElement(mouseX, mouseY);
  }

@Override
public void updateScreenContents()
  {
  area.updateGuiPos(guiLeft, guiTop);
  }


@Override
public void onElementActivated(IGuiElement element)
  {
  switch(element.getElementNumber())
    {
    case 0:
    break;
    case 1:
    mc.displayGuiScreen(parentGui);
    break;
    default:
    break;
    }
  
  }

@Override
public void setupControls()
  {
  int buffer = 2;
  int buttonSize = 16;
  int keyBindCount = 20;
  int totalHeight = keyBindCount * (buffer+buttonSize);
  Config.logDebug("total area height: "+totalHeight);
  area = new GuiScrollableArea(0, this, 10, 20, this.getXSize()-20, this.getYSize()-30, totalHeight);
  this.addGuiButton(1, getXSize()-35-5, 5, 35, 12, "Done");
  int kX = 5;
  int kY = 0;
  for(int i = 0; i < keyBindCount; i++)
    {
    kY = i * (buffer+buttonSize);    
    area.addGuiElement(new GuiButtonSimple(i+2, area, 100, buttonSize, "Button"+i).updateRenderPos(kX, kY));
    }
  this.guiElements.put(0, area);
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub
  }

}
