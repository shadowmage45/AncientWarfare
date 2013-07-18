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

import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerMailbox;
import shadowmage.ancient_warfare.common.container.ContainerMailboxBase;

public class GuiMailbox extends GuiContainerAdvanced
{

ContainerMailboxBase container;

GuiButtonSimple[] sideNameButtons = new GuiButtonSimple[4];
GuiButtonSimple topNameButton = null;
GuiButtonSimple boxNameButton = null;

/**
 * @param container
 */
public GuiMailbox(ContainerMailboxBase container)
  {
  super(container);
  this.container = container;
  this.shouldCloseOnVanillaKeys = true;
  this.container.removeSlots();
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
  this.boxNameButton.setButtonText(container.getSideName(0) != null ? container.getSideName(0) : "No Name");
  this.topNameButton.setButtonText(container.getSideName(1) != null ? container.getSideName(1) : "No Dest.");
  for(int i = 0; i < this.sideNameButtons.length; i++)
    {
    this.sideNameButtons[i].setButtonText(container.getSideName(i+2)!=null? container.getSideName(i+2) : "No Dest.");
    }
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
  if(element.getElementNumber()<6)
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
  this.boxNameButton.updateRenderPos(46-1+2*18+9, 96-1);
  this.guiElements.put(0, boxNameButton);
  label = new GuiString(0+6, this, 4*18, 8, "Box Name:");
  label.updateRenderPos(46-1+2*18+9+2*18 , 96-1-10);
  label.center = true;
  this.guiElements.put(0+6, label);
  this.topNameButton = new GuiButtonSimple(1, this, 4*18, 12, "");
  this.topNameButton.updateRenderPos(92-1, 18-1);
  this.guiElements.put(1, topNameButton);
  label = new GuiString(1+6, this, 4*18, 8, "Top");
  label.updateRenderPos(92-1+2*18, 18-1-10);
  label.center = true;
  this.guiElements.put(1+6, label);
  
  String destName = "";
  int xPos = 0;
  int yPos = 0;
  for(int i = 0; i < 4; i++)
    {   
    switch(i)
    {
    case 0:
    //12,30
    xPos =12;
    yPos =30;
    destName = "Front";
    break;

    case 1:
    //12,78
    xPos = 12;
    yPos = 78;
    destName = "Left";
    break;
    
    case 2:    
    //172,30
    xPos = 172;
    yPos = 30;
    destName = "Rear";
    break;
    
    case 3:
    //172,78
    xPos = 172;
    yPos = 78;
    destName = "Right";
    break;    
    }
    this.sideNameButtons[i] = new GuiButtonSimple(2+i, this, 4*18, 12, "");
    this.sideNameButtons[i].updateRenderPos(xPos-1, yPos-12-1);
    this.guiElements.put(i+2, this.sideNameButtons[i]); 
    label = new GuiString(i+2+6, this, 4*18,10, destName);
    label.updateRenderPos(xPos-1+2*18, yPos-12-1-10);
    label.center = true;
    this.guiElements.put(label.getElementNumber(), label);
    }
  }

@Override
public void updateControls()
  {
  
  }

}
