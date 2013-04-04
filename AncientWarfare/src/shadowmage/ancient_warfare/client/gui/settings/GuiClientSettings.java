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
import shadowmage.ancient_warfare.client.gui.elements.GuiCheckBoxSimple;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Settings;

public class GuiClientSettings extends GuiContainerAdvanced
{

GuiCheckBoxSimple enableOverlayBox;
GuiCheckBoxSimple enableAdvancedOverlay;
GuiCheckBoxSimple enableMouseAim;
GuiCheckBoxSimple enableVehicleFPR;
GuiButtonSimple keyBinds;

/**
 * @param container
 */
public GuiClientSettings(Container container)
  {
  super(container);  
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
  this.drawString(fontRenderer, "Render Overlay", guiLeft+10+16+2, guiTop+10+4, 0xffffffff);
  this.drawString(fontRenderer, "Render Advanced Overlay", guiLeft+10+16+2, guiTop+30+4, 0xffffffff);
  this.drawString(fontRenderer, "Use Mouse Aim Input", guiLeft+10+16+2, guiTop+50+4, 0xffffffff);
  this.drawString(fontRenderer, "Render Ridden Vehicle in First-person", guiLeft+10+16+2, guiTop+70+4, 0xffffffff);
  this.drawString(fontRenderer, "TPS: "+AWCore.proxy.serverTPS+ " AVG TICK: "+AWCore.proxy.serverTickTime, guiLeft+5, guiTop+getYSize()-15, 0xffffffff);
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
    Settings.setRenderOverlay(this.enableOverlayBox.checked());
    break;
    
    case 1:
    Settings.setRenderAdvOverlay(this.enableAdvancedOverlay.checked());
    break;
    case 2:
    Settings.setMouseAim(this.enableMouseAim.checked());
    break;
    case 3:
    mc.displayGuiScreen(new GuiKeybinds(inventorySlots, this));
    break;
    case 4:
    mc.displayGuiScreen(null);
    break;
    case 5:
    Settings.setRenderVehiclesInFirstPerson(this.enableVehicleFPR.checked());
    break;
    default:
    break;
   
    }
  }

@Override
public void setupControls()
  {
  this.enableOverlayBox = this.addCheckBox(0, 10, 10, 16, 16).setChecked(Settings.getRenderOverlay());
  this.enableAdvancedOverlay = this.addCheckBox(1, 10, 30, 16, 16).setChecked(Settings.getRenderAdvOverlay());
  this.enableMouseAim = this.addCheckBox(2, 10, 50, 16, 16).setChecked(Settings.getMouseAim());
  this.keyBinds = this.addGuiButton(3, this.getXSize()-55-10, 30, 55, 16, "Keybinds");
  this.addGuiButton(4, getXSize()-55-10, 10, 55, 16, "Done");
  this.enableVehicleFPR = this.addCheckBox(5, 10, 70, 16, 16).setChecked(Settings.renderVehiclesInFirstPerson);
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub

  }

}
