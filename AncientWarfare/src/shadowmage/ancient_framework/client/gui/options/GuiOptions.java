/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_framework.client.gui.options;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiCheckBoxSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiElement;
import shadowmage.ancient_framework.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.GuiString;
import shadowmage.ancient_framework.client.gui.elements.GuiTab;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.client.input.Keybind;
import shadowmage.ancient_framework.client.input.KeybindManager;
import shadowmage.ancient_framework.client.render.RenderTools;
import shadowmage.ancient_framework.common.config.AWClientConfig;
import shadowmage.ancient_framework.common.config.AWClientConfig.ClientConfigOption;
import shadowmage.ancient_framework.common.container.ContainerBase;

public class GuiOptions extends GuiContainerAdvanced
{

GuiButtonSimple done;

GuiTab options;
int optionsTabHeight;
GuiTab keybinds;
int keybindTabHeight;
GuiScrollableArea area;

GuiTab activeTab;
Keybind changingKeybind;

private List<GuiElement> optionsTabElements = new ArrayList<GuiElement>();
private List<GuiElement> keybindTabElements = new ArrayList<GuiElement>();

public GuiOptions(ContainerBase container)
  {
  super(container);
  this.shouldCloseOnVanillaKeys = true;
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
protected void renderBackgroundImage(String tex)
  {
  if(tex!=null)
    {
    RenderTools.drawQuadedTexture(guiLeft, guiTop+13, this.xSize, this.ySize-13, 256, 240, tex, 0, 0);
    }
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {

  }

@Override
public void updateScreenContents()
  {

  }

@Override
public void setupControls()
  {  
  this.options = addGuiTab(1, 8, 0, 120, 16, "Options");
  this.keybinds = addGuiTab(2, 128, 0, 120, 16, "Keybinds");
  keybinds.enabled = false;
 
  this.area = new GuiScrollableArea(3, this, 8, 8+16, 256-16, 240-16-16, 240-16-16);
  this.guiElements.put(3, area); 
  
  activeTab = options;
  
  Collection<ClientConfigOption> clientOptions = AWClientConfig.getClientOptions();  
  for(ClientConfigOption option : clientOptions)
    {
    addClientOption(option, optionsTabHeight);
    optionsTabHeight+=18;
    }  
  
  
  Collection<Keybind> keybinds = KeybindManager.getKeybinds();
  for(Keybind k : keybinds)
    {
    addKeybind(k, keybindTabHeight);
    keybindTabHeight += 16;
    }
  
  }

@Override
public void updateControls()
  {
  area.elements.clear();
  if(this.activeTab==options)
    {
    area.elements.addAll(optionsTabElements);
    area.updateTotalHeight(optionsTabHeight);
    }
  else
    {
    area.elements.addAll(keybindTabElements);
    area.updateTotalHeight(keybindTabHeight);
    }
  }

private HashMap<GuiElement, ClientConfigOption> optionsElementBooleanMap = new HashMap<GuiElement, ClientConfigOption>();
private HashMap<GuiElement, ClientConfigOption> optionsElementIntegerMap = new HashMap<GuiElement, ClientConfigOption>();
private HashMap<GuiElement, Keybind> keybindElementMap = new HashMap<GuiElement, Keybind>();

private void addClientOption(ClientConfigOption option, int targetY)
  {
  optionsTabElements.add(new GuiString(optionsTabElements.size(), area, 120, 12, option.displayName).updateRenderPos(0, targetY+3));
  if(option.dataClass==boolean.class)
    {
    GuiCheckBoxSimple box = new GuiCheckBoxSimple(optionsTabElements.size()-1, area, 16, 16);
    box.updateRenderPos(200, targetY);
    box.setChecked((Boolean)option.dataValue);
    optionsTabElements.add(box);
    optionsElementBooleanMap.put(box, option);
    }
  else
    {
    GuiNumberInputLine line = new GuiNumberInputLine(optionsTabElements.size()-1, area, 40, 12, 20, "0");
    line.updateRenderPos(200, targetY);
    line.setAsIntegerValue();
    line.setIntegerValue((Integer)option.dataValue);
    optionsTabElements.add(line);
    optionsElementIntegerMap.put(line, option);
    }
  }

private void addKeybind(Keybind k, int targetY)
  {
  keybindTabElements.add(new GuiString(keybindTabElements.size(), area, 120, 12, k.getKeyName()).updateRenderPos(0, targetY));
  GuiButtonSimple button = new GuiButtonSimple(keybindTabElements.size()-1, area, 35, 14, k.getKeyChar());
  button.updateRenderPos(200, targetY);
  keybindTabElements.add(button);
  keybindElementMap.put(button, k);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==options || element==keybinds)
    {
    options.enabled = element==options;
    keybinds.enabled = element==keybinds;
    this.activeTab = (GuiTab) element;
    this.forceUpdate = true;
    this.changingKeybind = null;
    }
  else if(this.activeTab==options) 
    {
    if(this.optionsElementBooleanMap.containsKey(element))
      {
      ClientConfigOption option = this.optionsElementBooleanMap.get(element);
      option.dataValue = ((GuiCheckBoxSimple)element).checked();
      }
    else if(this.optionsElementIntegerMap.containsKey(element))
      {
      ClientConfigOption option = this.optionsElementBooleanMap.get(element);
      option.dataValue = ((GuiNumberInputLine)element).getIntVal();
      }
    }
  else
    {
    if(this.keybindElementMap.containsKey(element))
      {
      Keybind k = this.keybindElementMap.get(element);
      this.changingKeybind = k;
      }
    }
  }

@Override
protected void keyTyped(char par1, int par2)
  {
  if(this.changingKeybind==null)
    {
    super.keyTyped(par1, par2);    
    }
  else
    {
    this.changingKeybind.setKeyCode(par2);
    this.changingKeybind = null;
    }
  }


}
