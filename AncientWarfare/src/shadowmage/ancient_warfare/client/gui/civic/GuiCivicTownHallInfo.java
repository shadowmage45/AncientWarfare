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
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.GuiString;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.container.ContainerCivicTownHallInfo;
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
  return Statics.TEXTURE_PATH+"gui/guiBackgroundLarge.png";
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
  else if(element.getElementNumber()==3)//clear living
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("clearLiving", true);
    this.sendDataToServer(tag);
    this.container.datas = null;
    this.refreshGui();
    this.area1.updateTotalHeight(0);
    }
  else if(element.getElementNumber()==4)//clear dead
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("clearDead", true);
    this.sendDataToServer(tag);
    this.container.deadDatas = null;
    this.refreshGui();
    this.area2.updateTotalHeight(0);
    }
  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, 256-5-40, 5, 40, 16, "Back");
  this.addGuiButton(3, 5, 5, 40, 16, "Clear");
  this.addGuiButton(4, 5, 5+97+20, 40, 16, "Clear");
  this.guiElements.put(5, new GuiString(5, this, 100, 8, "Living:").updateRenderPos(5+40+4, 5+4));
  this.guiElements.put(6, new GuiString(6, this, 100, 8, "Dead:").updateRenderPos(5+40+4, 5+4+97+20));
  area1 = new GuiScrollableArea(1, this, 5, 5+18, 256-10, 97, 0);
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
      area1.elements.add(new GuiString(6, area1, 256-20, 10, entry.getPrimaryDescription()).updateRenderPos(0, y));
      y += 10;
      area1.elements.add(new GuiString(6, area1, 256-20, 10, entry.getLocation()).updateRenderPos(0, y));
      y += 14;
      }    
    area1.updateTotalHeight(y);
    } 
  if(this.container.deadDatas!=null)
    {
    int y = 0;
    for(NpcDataEntry entry : this.container.deadDatas.getDataList())
      {
      area2.elements.add(new GuiString(6, area2, 256-20, 10, entry.getPrimaryDescription()).updateRenderPos(0, y));
      y += 10;
      area2.elements.add(new GuiString(6, area2, 256-20, 10, entry.getLocation()).updateRenderPos(0, y));
      y += 14;
      }    
    area2.updateTotalHeight(y);
    }
  }


}
