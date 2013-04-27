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
package shadowmage.ancient_warfare.client.gui.vehicle;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerDummy;
import shadowmage.ancient_warfare.common.container.ContainerVehicle;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;

public class GuiVehicleDebug extends GuiContainerAdvanced
{

/**
 * @param container
 */
public GuiVehicleDebug(Container container)
  {
  super(container);
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
  return 196;
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
 
  }


@Override
public void onElementActivated(IGuiElement element)
  {  
  switch(element.getElementNumber())
  {
  case 0:
  this.closeGUI();
  break;
  case 1:
  mc.displayGuiScreen(new GuiVehicleStats(new ContainerDummy(), ((ContainerVehicle)this.inventorySlots).vehicle));
  break;
  case 2:
  Packet02Vehicle pkt = new Packet02Vehicle();
  pkt.setParams( ((ContainerVehicle)this.inventorySlots).vehicle );
  pkt.setPackCommand();
  pkt.sendPacketToServer();
  this.closeGUI();
  break;
  
  case 3:
  ((ContainerVehicle)inventorySlots).prevRow();
  break;
  
  case 4:
  ((ContainerVehicle)inventorySlots).nextRow();
  break; 
  
  default:
  break;
  }
  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, this.getXSize()-45-5, 5, 45, 16, "Done");
  this.addGuiButton(1, this.getXSize()-45-5, 5+16+2, 45, 16, "Stats");
  this.addGuiButton(2, this.getXSize()-45-5, 5+32+4,45,16, "Pack");
  
  this.addGuiButton(3, this.getXSize()-45-20-10-10, 5+6, 16, 16, "-");
  this.addGuiButton(4, this.getXSize()-45-20-10-10, 5+18*3-16+6, 16, 16, "+");
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub

  }

}
