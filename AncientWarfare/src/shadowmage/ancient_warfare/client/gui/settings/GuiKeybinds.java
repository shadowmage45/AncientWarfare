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
package shadowmage.ancient_warfare.client.gui.settings;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.input.Keybind;
import shadowmage.ancient_warfare.client.input.KeybindManager;
import shadowmage.meim.common.config.Config;

public class GuiKeybinds extends GuiContainerAdvanced
{

GuiContainerAdvanced parentGui;
GuiScrollableArea area;

private List<Keybind> keybinds;
private Keybind kb = null;//current editing keybind
private GuiButtonSimple kbButton = null;

/**
 * @param container
 */
public GuiKeybinds(Container container, GuiContainerAdvanced parent)
  {
  super(container);
  this.parentGui = parent;
  this.keybinds = KeybindManager.getKeybinds();
  this.shouldCloseOnVanillaKeys = false;
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
  if(kb!=null)
    {
    this.drawStringGui("Setting key for:"+kb.getKeyName(), 15, 8, 0xffffffff);
    this.drawStringGui("Press Esc to Cancel", 15, 18, 0xffffffff);
    }
  }

@Override
public void updateScreenContents()
  {
  area.updateGuiPos(guiLeft, guiTop);
  }


@Override
public void onElementActivated(IGuiElement element)
  {
  switch(element.getElementNumber())
    {
    case 0:
    mc.displayGuiScreen(parentGui);
    break;
    
    case 1:
    break;
    
    default:
    break;
    }  
  if(element.getElementNumber()>=2 && element.getElementNumber() < keybinds.size()+2)
    {
    this.kb = keybinds.get(element.getElementNumber()-2);
    this.kbButton = (GuiButtonSimple) element;
    }
  }

@Override
public void setupControls()
  {
  int buffer = 2;
  int buttonSize = 16;
  int keyBindCount = keybinds.size();
  
  int totalHeight = keyBindCount * (buffer+buttonSize);
  

  this.addGuiButton(0, getXSize()-55-10, 10, 55, 16, "Done");
  
  area = new GuiScrollableArea(1, this, 10, 30, this.getXSize()-20, this.getYSize()-40, totalHeight);
  this.guiElements.put(1, area);
  
  int kX = 5;
  int kY = 0;
  for(int i = 0; i < keyBindCount; i++)
    {
    kY = i * (buffer+buttonSize);  
    Keybind kb = keybinds.get(i);
    area.addGuiElement(new GuiButtonSimple(i+2, area, this.getXSize()-20-16-10, buttonSize, kb.getKeyName() + " :: "+kb.getKeyChar()).updateRenderPos(kX, kY));
    }
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub
  }


@Override
protected void keyTyped(char par1, int par2)
  {
  if(par2 == Keyboard.KEY_ESCAPE)
    {
    kb=null;
    kbButton=null;
    }
  if(kb!=null)
    {
    Config.logDebug("setting keybind "+kb.getKeyName()+" to: "+Keyboard.getKeyName(par2));
    kb.setKeyCode(par2);
    kbButton.setButtonText(kb.getKeyName() + " :: "+kb.getKeyChar());
    kb=null;
    kbButton=null;        
    }  
  super.keyTyped(par1, par2);    
  }



}
