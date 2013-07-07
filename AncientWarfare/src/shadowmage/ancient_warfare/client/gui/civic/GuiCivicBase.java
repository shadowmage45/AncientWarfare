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
package shadowmage.ancient_warfare.client.gui.civic;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiCheckBoxSimple;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.config.Config;

public class GuiCivicBase extends GuiContainerAdvanced
{

TECivic teBase;
/**
 * @param container
 */
public GuiCivicBase(Container container, TECivic te)
  {
  super(container);
  this.teBase = te;
  this.shouldCloseOnVanillaKeys = true;
  }

@Override
public int getXSize()
  {
  return 176;
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
  this.drawStringGui("Inventory", 8, 5, 0xffffffff);
  

  this.drawStringGui("Civic Type: "+teBase.getCivic().getDisplayName(), 8, 112+12+3, 0xffffffff);  
  this.drawStringGui("Broadcast Work: ", 8 , 112+10+5+12, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub

  }
@Override
public void onElementActivated(IGuiElement element)
  {
  if(element.getElementNumber()==0)
    {
    teBase.sendBroadCastWork(((GuiCheckBoxSimple)element).checked());
    }
  }

@Override
public void setupControls()
  {
  this.addCheckBox(0, 176-8-18, 112+10+12, 18, 18).setChecked(teBase.broadcastWork);
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub

  }

}
