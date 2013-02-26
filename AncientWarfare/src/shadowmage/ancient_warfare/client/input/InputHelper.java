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
package shadowmage.ancient_warfare.client.input;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import shadowmage.ancient_warfare.common.config.Config;

import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;


public class InputHelper extends KeyHandler
{


static int FORWARD = Keyboard.KEY_W;
static int BACKWARD = Keyboard.KEY_S;
static int LEFT = Keyboard.KEY_A;
static int RIGHT = Keyboard.KEY_D;
static int FIRE = Keyboard.KEY_SPACE;

/**
 * options screen to set the config on the above keys;
 */
static KeyBinding options = new KeyBinding("AW-options", Keyboard.KEY_F7);

private static KeyBinding[] keys = new KeyBinding[]{options};
private static boolean[] keyRepeats = new boolean []{false};

private static InputHelper INSTANCE;

private InputHelper()
  {
  super(keys, keyRepeats);
  }

public static InputHelper instance()
  {
  if(INSTANCE == null){}
  return INSTANCE;
  }

public void loadKeysFromConfig()
  {
  //TODO load any keybindings from config file that need custom binding (Because they may conflict with vanilla keybinds)
  FORWARD = Config.getKeyBindID("forward", Keyboard.KEY_W, "Move Forwards/Accelerate");
  }

@Override
public String getLabel()
  {
  return null;
  }

@Override
public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
  {
  }

@Override
public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
  {
  }

@Override
public EnumSet<TickType> ticks()
  {
  return EnumSet.of(TickType.CLIENT);
  }

public static byte getForwardsInput()
  {
  boolean forward = Keyboard.isKeyDown(FORWARD);
  boolean reverse = Keyboard.isKeyDown(BACKWARD);
  return (byte) (forward && reverse ? 0 : reverse ? -1 : forward? 1 : 0); // 0 if none or both are pressed, -1 for reverse, 1 for forward
  }

public static byte getStrafeInput()
  {
  boolean left = Keyboard.isKeyDown(LEFT);
  boolean right = Keyboard.isKeyDown(RIGHT);
  return (byte) (left && right ? 0 : left ? -1 : right ? 1 : 0);//return 0 if left and right are pressed, else return -1 if only left is pressed or 1 if only right is pressed. finally, return 0 if no input
  }

public static boolean getFireInput()
  {
  return Keyboard.isKeyDown(FIRE);
  }


}
