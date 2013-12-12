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
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
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
  if(element.getElementNumber()==0)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("repack", true);
    this.sendDataToServer(tag);
    this.closeGUI();
    }
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
  this.drawStringGui("Class: "+npc.npcType.getLocalizedName(npc.rank), 8, 8, 0xffffffff);  
  }

@Override
public void updateScreenContents()
  {

  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, 8, 8+10+2, 55, 16, "Repack").addToToolitp("Repack the NPC into a spawning item").addToToolitp("Retains present health when respawned");
  }

@Override
public void updateControls()
  {

  }

}
