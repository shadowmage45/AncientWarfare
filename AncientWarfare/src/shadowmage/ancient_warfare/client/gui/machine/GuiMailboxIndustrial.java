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
package shadowmage.ancient_warfare.client.gui.machine;

import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiString;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.container.ContainerMailboxBase;

public class GuiMailboxIndustrial extends GuiMailbox
{

/**
 * @param container
 */
public GuiMailboxIndustrial(ContainerMailboxBase container)
  {
  super(container);
  }

@Override
public int getXSize()
  {
  return 176;
  }

@Override
public void updateScreenContents()
  {
  this.boxNameButton.setButtonText(container.getSideName(0) != null ? container.getSideName(0) : "No Name");
  this.topNameButton.setButtonText(container.getSideName(1) != null ? container.getSideName(1) : "No Dest.");  
  if(this.container.getSideName(0)==null)
    {
    this.container.removeSlots();
    }
  else
    {
    this.container.addSlots();
    }
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element.getElementNumber()<2)
    {
    if(element.getElementNumber()==0 || container.getSideName(0)!=null)
      {
      this.container.removeSlots();
      mc.displayGuiScreen(new GuiMailboxSelection(this, element.getElementNumber()));      
      }
    }
  }

@Override
public void setupControls()
  {
  GuiString label;
  this.boxNameButton = new GuiButtonSimple(0, this, 4*18, 12, "");
  this.boxNameButton.updateRenderPos(52, 96-1);
  this.guiElements.put(0, boxNameButton);
  
  label = new GuiString(0+2, this, 4*18, 8, "Box Name:");
  label.updateRenderPos(88 , 96-1-10);
  label.center = true;
  
  this.guiElements.put(0+6, label);
  this.topNameButton = new GuiButtonSimple(1, this, 4*18, 12, "");
  this.topNameButton.updateRenderPos(52, 18-1);
  this.guiElements.put(1, topNameButton);
  
  label = new GuiString(1+6, this, 4*18, 8, "Top/Sides");
  label.updateRenderPos(88, 18-1-10);
  label.center = true;
  this.guiElements.put(1+2, label); 
  }

}
