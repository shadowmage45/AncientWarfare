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

import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerDebugInfo;

public class GuiDebugInfo extends GuiContainerAdvanced
{

GuiScrollableArea area;
GuiButtonSimple backButton;
ContainerDebugInfo container;

/**
 * @param container
 */
public GuiDebugInfo(ContainerDebugInfo container)
  {
  super(container);
  this.shouldCloseOnVanillaKeys = true;
  this.container = container;
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
  int white = 0xffffffff;
  this.drawStringGui("tickTime  : "+container.tickTime, 5, 5, white);
  this.drawStringGui("tps       : "+container.tickPerSecond, 5, 15, white);
  this.drawStringGui("pathAvg   : "+container.pathTimeTickAverage, 5, 25, white);
  this.drawStringGui("pathOneSec: "+container.pathTimeOneSecond, 5, 35, white);
  this.drawStringGui("memUse    : "+container.memUse, 5, 45, white);
  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  // TODO Auto-generated method stub
  }

@Override
public void setupControls()
  {
  // TODO Auto-generated method stub

  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub

  }

}
