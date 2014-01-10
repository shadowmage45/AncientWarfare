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

import java.util.HashMap;

import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerWarzones;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.warzone.Warzone;

public class GuiWarzones extends GuiContainerAdvanced
{

GuiScrollableArea area;
GuiButtonSimple done;
ContainerWarzones container;

public GuiWarzones(Container container)
  {
  super(container);
  this.container = (ContainerWarzones)container;
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

  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub

  }

@Override
public void setupControls()
  {
  this.area = new GuiScrollableArea(0, this, 8, 8+16+4, getXSize()-16, getYSize()-16-16-4, getYSize()-16-16-4);
  this.guiElements.put(0, area);
  this.done = this.addGuiButton(1, this.getXSize()-55-8, 8, 55, 16, "Back");  
  }

@Override
public void updateControls()
  {
  this.area.elements.clear();
  int totalY = 0;
  for(Warzone z : this.container.zoneList)
    {
    addWarzone(z, totalY);
    totalY += 22;
    }  
  this.area.updateTotalHeight(totalY);
  }

private void addWarzone(Warzone z, int targetY)
  {
  GuiString label = new GuiString(area.elements.size(), area, 120, 12, "Min: "+z.min.toString());
  label.updateRenderPos(0, targetY);
  area.elements.add(label);
  
  label = new GuiString(area.elements.size(), area, 120, 12, "Max: "+z.max.toString());
  label.updateRenderPos(0, targetY+11);
  area.elements.add(label);
  
  
  GuiButtonSimple button = new GuiButtonSimple(area.elements.size(), area, 55, 16, "Remove");
  button.updateRenderPos(165, targetY+3);
  area.elements.add(button);  
  this.removalMap.put(button, z);  
  }

private HashMap<GuiButtonSimple, Warzone> removalMap = new HashMap<GuiButtonSimple, Warzone>();


@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==done)
    {
    GUIHandler.instance().openGUI(GUIHandler.SETTINGS, player, player.worldObj, 0, 0, 0);
    }
  else if(removalMap.containsKey(element))
    {
    Warzone z = removalMap.get(element);
    NBTTagCompound removeTag = new NBTTagCompound();
    removeTag.setBoolean("removal", true);
    z.writeToNBT(removeTag);
    this.sendDataToServer(removeTag);
    }
  }
}
