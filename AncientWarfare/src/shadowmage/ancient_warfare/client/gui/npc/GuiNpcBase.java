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
package shadowmage.ancient_warfare.client.gui.npc;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiFakeSlot;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;

public class GuiNpcBase extends GuiContainerAdvanced
{


NpcBase npc;
/**
 * @param container
 */
public GuiNpcBase(Container container, NpcBase npc)
  {
  super(container);
  this.npc = npc;
  this.shouldCloseOnVanillaKeys = true;
  }

@Override
public void onElementActivated(IGuiElement element)
  {

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
  if(npc.npcType.getSpecInventorySize(npc.rank)>0)
    {
    this.drawStringGui("Special Tools", 8, 5 + 10 + 3*18 + 5, 0xffffffff);
    }  
  this.drawStringGui("Class: "+npc.npcType.getDisplayName(npc.rank), 8, 112, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {

  }

@Override
public void setupControls()
  {
 
  }

@Override
public void updateControls()
  {

  }

}
