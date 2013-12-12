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
package shadowmage.ancient_warfare.client.gui.settings;

import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerDebugInfo;

public class GuiDebugInfo extends GuiContainerAdvanced
{

GuiScrollableArea area;
GuiButtonSimple backButton;
ContainerDebugInfo container;

/**
 * @param container
 */
public GuiDebugInfo(ContainerDebugInfo container)
  {
  super(container);
  this.shouldCloseOnVanillaKeys = true;
  this.container = container;
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
  int white = 0xffffffff;
  String tickTime = String.format("%,.4f ms", (float)container.tickTime/1000000);
  String tps = String.format("%,d", container.tickPerSecond);
  String path = String.format("%,.4f ms", (float)container.pathTimeTickAverage/1000000);
  String memUse = String.format("%,d bytes", container.memUse);
  String civ = String.format("%,.4f ms", (float)container.civicTick/1000000);
  String npc = String.format("%,.4f ms", (float)container.npcTick/1000000);
  String veh = String.format("%,.4f ms", (float)container.vehicleTick/1000000);
  
  this.drawStringGui("tickTime", 5, 5, white);
  this.drawStringGui("tps", 5, 15, white);
  this.drawStringGui("memUse", 5, 25, white);
  this.drawStringGui("pathAvg", 5, 35, white);
  this.drawStringGui("civTick", 5, 45, white);
  this.drawStringGui("npcTick", 5, 55, white);
  this.drawStringGui("vehTick", 5, 65, white);
  
  this.drawStringGui(" : " + tickTime, 75, 5, white);
  this.drawStringGui(" : " + tps, 75, 15, white);
  this.drawStringGui(" : " + memUse, 75, 25, white);
  this.drawStringGui(" : " + path, 75, 35, white);
  this.drawStringGui(" : " + civ, 75, 45, white);
  this.drawStringGui(" : " + npc, 75, 55, white);
  this.drawStringGui(" : " + veh, 75, 65, white);
  
  container.tickTime = container.tickTime==0?  1: container.tickTime;
  float pathPercent = (float)container.pathTimeTickAverage / (float)container.tickTime;
  float civPercent = (float)container.civicTick / (float)container.tickTime;
  float npcPercent = (float)container.npcTick / (float)container.tickTime;
  float vehPercent = (float)container.vehicleTick / (float)container.tickTime;
    
  this.drawStringGui(" : " + String.format("%.2f", pathPercent*100) + "%", 135, 35, white);
  this.drawStringGui(" : " + String.format("%.2f", civPercent*100) + "%", 135, 45, white);
  this.drawStringGui(" : " + String.format("%.2f", npcPercent*100) + "%", 135, 55, white);
  this.drawStringGui(" : " + String.format("%.2f", vehPercent*100) + "%", 135, 65, white);
  
  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub
  }

@Override
public void onElementActivated(IGuiElement element)
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

}
