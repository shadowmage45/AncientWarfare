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
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerCivicTownHallInfo;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.tracker.entry.NpcDataEntry;

public class GuiCivicTownHallInfo extends GuiContainerAdvanced
{

TECivic teBase;
GuiScrollableArea area1;
GuiScrollableArea area2;

ContainerCivicTownHallInfo container;

/**
 * @param container
 */
public GuiCivicTownHallInfo(Container container, TECivic te)
  {
  super(container);
  this.teBase = te;
  this.shouldCloseOnVanillaKeys = true;
  this.container = (ContainerCivicTownHallInfo)container;
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
  area1.updateGuiPos(guiLeft, guiTop);
  area2.updateGuiPos(guiLeft, guiTop);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element.getElementNumber()==0)
    {
    this.closeGUI();
    GUIHandler.instance().openGUI(GUIHandler.CIVIC_TOWNHALL, player, player.worldObj, teBase.xCoord, teBase.yCoord, teBase.zCoord);
    }
  else
    {
    /**
     * TODO...nothing? just info displays in areas?
     */
    }
  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, 256-5-40, 5, 40, 16, "Back");
  area1 = new GuiScrollableArea(1, this, 5, 5+18, 256-14, 97, 0);
  this.guiElements.put(1, area1);
  area2 = new GuiScrollableArea(2, this, 5, 5+18+97+20, 256-10, 97, 0);
  this.guiElements.put(2, area2);
  this.refreshGui();
  }

@Override
public void updateControls()
  {
  this.area1.elements.clear();
  this.area2.elements.clear();
  if(this.container.datas!=null)
    {
    int y = 0;
    for(NpcDataEntry entry : this.container.datas.getDataList())
      {
      area1.elements.add(new GuiString(y, area1, 256-20, 10, entry.toString()).updateRenderPos(0, y));
      y +=14;
      }    
    area1.updateTotalHeight(y);
    } 
  if(this.container.deadDatas!=null)
    {
    int y = 0;
    area2.updateTotalHeight(y);
    }
  }


}
