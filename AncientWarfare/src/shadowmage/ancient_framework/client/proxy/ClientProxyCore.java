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
package shadowmage.ancient_framework.client.proxy;

import org.lwjgl.input.Keyboard;

import shadowmage.ancient_framework.client.gui.options.GuiKeybinds;
import shadowmage.ancient_framework.client.gui.options.GuiOptions;
import shadowmage.ancient_framework.client.gui.options.GuiPerformanceMonitor;
import shadowmage.ancient_framework.client.input.IHandleInput;
import shadowmage.ancient_framework.client.input.Keybind;
import shadowmage.ancient_framework.client.input.KeybindManager;
import shadowmage.ancient_framework.client.input.TickHandlerClientKeyboard;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.network.GUIHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxyCore extends ClientProxyBase implements IHandleInput
{

public ClientProxyCore()
  {
  // TODO Auto-generated constructor stub
  }

@Override
public void registerClientData()
  {
  
  AWLog.logDebug("registering client data for core proxy");
  TickRegistry.registerTickHandler(new TickHandlerClientKeyboard(), Side.CLIENT);
  KeyBindingRegistry.registerKeyBinding(new KeybindManager());
  GUIHandler.instance().registerGui(Statics.guiOptions, GuiOptions.class);
  GUIHandler.instance().registerGui(Statics.guiKeybinds, GuiKeybinds.class);
  GUIHandler.instance().registerGui(Statics.guiPerformance, GuiPerformanceMonitor.class);  
//  KeybindManager.addKeybind(new Keybind(this, Keyboard.KEY_A, "Test Keybind A"));
  }

@Override
public void onKeyUp(Keybind kb)
  {
  // TODO Auto-generated method stub  
  }

@Override
public void onKeyPressed(Keybind kb)
  {
  // TODO Auto-generated method stub  
  }

@Override
public void onTickEnd()
  {
  // TODO Auto-generated method stub  
  }

@Override
public void onMouseMoved(int x, int y)
  {
  // TODO Auto-generated method stub  
  }

@Override
public void onMouseButtonPressed(int num)
  {
  // TODO Auto-generated method stub  
  }

@Override
public void onMouseButtonUp(int num)
  {
  // TODO Auto-generated method stub  
  }

@Override
public void onMouseWheel(int delta)
  {
  // TODO Auto-generated method stub  
  }
}
