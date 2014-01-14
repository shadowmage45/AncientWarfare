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
package shadowmage.ancient_warfare.client.gui.gate;

import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerGateControl;

public class GuiGateControl extends GuiContainerAdvanced
{

public GuiGateControl(Container container)
  {
  super(container);
  }

@Override
public int getXSize()
  {
  return 126;
  }

@Override
public int getYSize()
  {
  return 32;
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
  this.addGuiButton(0, 8, 8, 55, 16, "Repack");
  this.addGuiButton(1, getXSize()-8-55, 8, 55, 16, "Done");
  }

@Override
public void updateControls()
  {

  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element.getElementNumber()==0)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("repack", true);
    this.sendDataToServer(tag);
    this.closeGUI();
    }
  else if(element.getElementNumber()==1)
    {
    this.closeGUI();
    }
  }

}
