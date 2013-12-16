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
package shadowmage.ancient_warfare.client.input;

import org.lwjgl.input.Keyboard;

import shadowmage.ancient_framework.client.input.IHandleInput;
import shadowmage.ancient_framework.client.input.Keybind;
import shadowmage.ancient_framework.client.input.KeybindManager;
import shadowmage.ancient_warfare.AWCore;

public class AWCoreInputHandler implements IHandleInput
{

private AWCoreInputHandler(){}
private static AWCoreInputHandler instance = new AWCoreInputHandler(){};
public static AWCoreInputHandler instance(){return instance;}

public static Keybind forward;
public static Keybind reverse;
public static Keybind left;
public static Keybind right;
public static Keybind fire;
public static Keybind ammoPrev;
public static Keybind ammoNext;
public static Keybind pitchUp;
public static Keybind pitchDown;
public static Keybind turretLeft;
public static Keybind turretRight;
public static Keybind mouseAim;
public static Keybind ammoSelect;
public static Keybind control;

public void loadKeybinds()
  {
  forward = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.forward", Keyboard.KEY_W, "forwards/accelerate"), "Forward");
  KeybindManager.addKeybind(forward);
  reverse = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.reverse", Keyboard.KEY_S, "reverse/deccelerate"), "Reverse");
  KeybindManager.addKeybind(reverse);
  left = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.left", Keyboard.KEY_A, "turn/strafe left"), "Left Turn");
  KeybindManager.addKeybind(left);
  right = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.right", Keyboard.KEY_D, "turn/strafe right"), "Right Turn");
  KeybindManager.addKeybind(right);
  fire = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.fire", Keyboard.KEY_SPACE, "fire missile"), "Fire");
  KeybindManager.addKeybind(fire);  
  ammoPrev = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.ammoPrev", Keyboard.KEY_T, "previous ammo"), "Prev Ammo");
  KeybindManager.addKeybind(ammoPrev);
  ammoNext = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.ammoNext", Keyboard.KEY_G, "next ammo"), "Next Ammo");
  KeybindManager.addKeybind(ammoNext);
  pitchUp = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.aimUp", Keyboard.KEY_R, "Aim Up"), "Aim Up");
  KeybindManager.addKeybind(pitchUp);
  pitchDown = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.aimDown", Keyboard.KEY_F, "Aim Down"), "Aim Down");
  KeybindManager.addKeybind(pitchDown);
  turretLeft = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.turretLeft", Keyboard.KEY_Z, "turret left"),"Turret Left");
  KeybindManager.addKeybind(turretLeft);
  turretRight = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.turretRight", Keyboard.KEY_X, "turret right"),"Turret Right");  
  KeybindManager.addKeybind(turretRight);  
  mouseAim = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.mouseAim", Keyboard.KEY_C, "Enable/Disable Mouse Aim"), "Mouse Aim");
  KeybindManager.addKeybind(mouseAim);
  ammoSelect = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.ammoSelect", Keyboard.KEY_V, "Open ammo Select GUI"), "Ammo Select");
  KeybindManager.addKeybind(ammoSelect);
  control = new Keybind(this, AWCore.instance.config.getKeyBindID("keybind.control", Keyboard.KEY_LCONTROL, "Control/Alt function key"), "Control");
  KeybindManager.addKeybind(control);
  }

@Override
public void onKeyUp(Keybind kb)
  {
  
  }

@Override
public void onKeyPressed(Keybind kb)
  {
  
  }

@Override
public void onTickEnd()
  {
  
  }

@Override
public void onMouseMoved(int x, int y)
  {
  
  }

@Override
public void onMouseButtonPressed(int num)
  {
  
  }

@Override
public void onMouseButtonUp(int num)
  {
  
  }

@Override
public void onMouseWheel(int delta)
  {
  
  }

}
