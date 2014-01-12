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

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;

public class GuiTeamView extends GuiContainerAdvanced
{

/**
 * @param container
 */
public GuiTeamView(Container container)
  {
  super(container);
  // TODO Auto-generated constructor stub
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
  // TODO Auto-generated method stub

  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub

  }
@Override
public void onElementActivated(IGuiElement element)
  {
  // TODO Auto-generated method stub

  }
}
