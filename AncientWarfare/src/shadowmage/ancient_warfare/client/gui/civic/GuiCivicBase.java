/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.client.gui.civic;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiCheckBoxSimple;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerCivicTE;

public class GuiCivicBase extends GuiContainerAdvanced
{

TECivic teBase;
ContainerCivicTE container;
//int ySize;

/**
 * @param container
 */
public GuiCivicBase(Container container, TECivic te)
  {
  super(container);  
  this.container = (ContainerCivicTE) container;
  this.teBase = te;
  this.shouldCloseOnVanillaKeys = true;
  this.xSize = this.getXSize();
  this.ySize = this.getYSize();
  }

@Override
public int getXSize()
  {
  return 176;
  }

@Override
public int getYSize()
  {
  return this.container==null ? 240 : this.container.playerSlotsY + 4*18 +4 + 8;
  }

@Override
public String getGuiBackGroundTexture()
  {
  return Config.texturePath+"gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  this.drawStringGui("Civic Type: "+teBase.getCivic().getLocalizedName(), 8, 4, WHITE);
  if(container.regLabel)
    {
    this.drawStringGui("Inventory (Sides)", 8, container.regSlotY-10, WHITE);    
    }
  if(container.resLabel)
    {
    this.drawStringGui("Resources (Top)", 8, container.resSlotY-10, WHITE);
    }
  if(container.specLabel)
    {
    this.drawStringGui("Special Resources (Bottom)", 8, container.specSlotY-10, WHITE);
    }
  this.drawStringGui("Player Inventory", 8, container.playerSlotsY-10, WHITE);
  }

@Override
public void updateScreenContents()
  {
  }
@Override
public void onElementActivated(IGuiElement element)
  {
//  if(element.getElementNumber()==0)
//    {
//    teBase.sendBroadCastWork(((GuiCheckBoxSimple)element).checked());
//    }
  }

@Override
public void setupControls()
  {
//  this.addCheckBox(0, 176-8-18, 112+10+12, 18, 18).setChecked(teBase.broadcastWork);
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub

  }

}
