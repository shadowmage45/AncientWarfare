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
package shadowmage.ancient_warfare.client.gui.info;

import java.util.List;

import org.lwjgl.input.Keyboard;

import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerDummy;

public class GuiInfoList extends GuiContainerAdvanced
{

List<String> detailLines;
GuiScrollableArea area;
GuiContainerAdvanced parent;

/**
 * @param container
 */
public GuiInfoList(GuiContainerAdvanced parent, List<String> details)
  {
  super(new ContainerDummy());
  this.detailLines = RenderTools.getFormattedLines(details, 210);
  this.parent = parent;
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
  area.updateGuiPos(guiLeft, guiTop);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  // TODO Auto-generated method stub

  }

@Override
public void setupControls()
  {
  this.area = new GuiScrollableArea(0, this, 5, 5, 246, 230, this.detailLines.size()*10);
  int y = 0;
  for(String st : this.detailLines)
    {
    area.elements.add(new GuiString(y, area, 210, 10, st).updateRenderPos(0, y));
    y+=10;
    }  
  }

@Override
protected void keyTyped(char par1, int par2)
  {
  if(this.parent!=null && (par2 == this.mc.gameSettings.keyBindInventory.keyCode || par2 == Keyboard.KEY_ESCAPE))
    {
    mc.displayGuiScreen(parent);
    return;
    }
  super.keyTyped(par1, par2);
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub

  }

}
