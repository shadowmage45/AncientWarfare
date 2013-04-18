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
package shadowmage.ancient_warfare.client.gui.npc;

import java.util.EnumSet;
import java.util.Iterator;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.container.ContainerCommandBaton;
import shadowmage.ancient_warfare.common.item.ItemNpcCommandBaton;
import shadowmage.ancient_warfare.common.item.ItemNpcCommandBaton.Command;

public class GuiCommandBaton extends GuiContainerAdvanced
{

ContainerCommandBaton container;
Command[] batonCommands;

/**
 * @param container
 */
public GuiCommandBaton(Container container)
  {
  super(container);
  this.container = (ContainerCommandBaton)container;  
  this.shouldCloseOnVanillaKeys = true;
  this.batonCommands = ItemNpcCommandBaton.getApplicableCommands(player.getCurrentEquippedItem());
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
  this.drawStringGui("Has Entity Assigned "+container.settings.hasEntity(), 10, 10, 0xffffffff);
  this.drawStringGui("Current Command: "+container.settings.command.getCommandName(), 10, 20, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub

  }


@Override
public void onElementActivated(IGuiElement element)
  {
  int id = element.getElementNumber();
  if(id==0)
    {
    this.closeGUI();
    }
  else if(id >= 10 && id < this.batonCommands.length+10)
    {
    Command cmd = this.batonCommands[element.getElementNumber()-10];
    this.container.settings.command = cmd;
    this.container.saveSettings();
    }  
  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, 45, 12, "Done").updateRenderPos(getXSize()-45-5, 5);
  for(int i = 0; i < this.batonCommands.length; i ++)
    {
    this.addGuiButton(i+10, 10, 40 + i*14, 140, 12, this.batonCommands[i].getCommandName());
    }  
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub

  }

}
