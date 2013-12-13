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
package shadowmage.ancient_warfare.client.input;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

public class KeybindManager
{

private static List<Keybind> keybinds = new ArrayList<Keybind>();
private static List<IHandleInput> inputHandlers = new ArrayList<IHandleInput>();

public static List<Keybind> getKeybinds()
  {
  return keybinds;
  }

public static void addKeybind(Keybind kb)
  {
  keybinds.add(kb);
  }

public static void addHandler(IHandleInput handler)
  {
  if(!inputHandlers.contains(handler))
    {
    inputHandlers.add(handler);
    }
  }

public static void onTick()
  {
  Iterator<Keybind> it = keybinds.iterator();
  Keybind kb;
  while(it.hasNext())
    {
    kb = it.next();
    boolean down = Keyboard.isKeyDown(kb.keyCode);
    if(kb.isPressed && !down)//KEY UP
      {
      kb.isPressed = false;
      for(IHandleInput ih : inputHandlers)
        {
        ih.onKeyUp(kb);
        }
      }
    else if(!kb.isPressed && down)//KEY DOWN
      {
      kb.isPressed = true;
      kb.checked = false;
      for(IHandleInput ih : inputHandlers)
        {
        ih.onKeyPressed(kb);
        }
      }    
    }
  }


}
