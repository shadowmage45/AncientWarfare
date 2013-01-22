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
package shadowmage.ancient_warfare.client.aw_structure.gui.survival_builder;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.aw_core.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.common.aw_structure.container.ContainerSurvivalBuilder;

public class GuiSurvivalBuilder extends GuiContainerAdvanced
{

private final ContainerSurvivalBuilder cont;

/**
 * @param container
 */
public GuiSurvivalBuilder(Container container)
  {
  super(container);
  this.cont = (ContainerSurvivalBuilder)container;
  if(cont==null)
    {
    closeGUI();
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
public void setupGui()
  {
  this.controlList.clear();
  this.addGuiButton(0, 256-35-10, 10, 35, 18, "Done"); 
  
  }

@Override
public void updateScreenContents()
  {
  
  }

@Override
public void buttonClicked(GuiButton button)
  {
  switch(button.id)
  {
  case 0:
  closeGUI();
  break;
  
  case 1:  
  closeGUI();
  break;
  
  case 2:
  break;
  
  case 11:
  case 12:
  case 13:
  case 14:
  case 15:
  case 16:
  
  default:
  break;
  }
 
  }

}
