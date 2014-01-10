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
package shadowmage.ancient_warfare.client.gui.settings;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;

public class GuiWarzones extends GuiContainerAdvanced
{

GuiScrollableArea area;

public GuiWarzones(Container container)
  {
  super(container);
  this.shouldCloseOnVanillaKeys = true;
  }

@Override
public void onElementActivated(IGuiElement element)
  {
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
  // TODO Auto-generated method stub

  }

@Override
public void setupControls()
  {
  this.area = new GuiScrollableArea(0, this, 8, 8, getXSize()-16, getYSize()-16-16-4, getYSize()-16-16-4);
  this.addGuiButton(1, this.getXSize()-55-8, 8, "Back"); 
  
  }

@Override
public void updateControls()
  {
  this.area.elements.clear();
  int totalY = 0;
  
  
  this.area.updateTotalHeight(totalY);
  }

}
