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
package shadowmage.ancient_warfare.client.gui.machine;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;

public class GuiTrashcan extends GuiContainerAdvanced
{

/**
 * @param container
 */
public GuiTrashcan(Container container)
  {
  super(container);
  this.shouldCloseOnVanillaKeys = true;
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  // TODO Auto-generated method stub
  }

@Override
public int getXSize()
  {
  return 176;
  }

@Override
public int getYSize()
  {
  return 8 + 8 + 3*18 + 3*18 + 18 + 4 + 8;
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
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub

  }

}
