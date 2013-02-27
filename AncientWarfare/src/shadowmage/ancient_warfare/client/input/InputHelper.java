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

boolean prevFireInput;
boolean fireInput;
private int prevForwardsInput = 0;
private int prevStrafeInput = 0;
private int forwardsInput = 0;
private int strafeInput = 0;

private InputHelper()
  {
  super(keys, keyRepeats);
  }

public static InputHelper instance()
  {
  if(INSTANCE == null)
    {
    INSTANCE = new InputHelper();
    }
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
  return (byte) InputHelper.instance().forwardsInput;
  }

public static byte getStrafeInput()
  {
  return (byte) InputHelper.instance().strafeInput;
  }

public static boolean getFireInput()
  {
  return InputHelper.instance().checkForFire();
  }

private void updateMovementInputFlags()
  {
  this.prevForwardsInput = this.forwardsInput;
  this.prevStrafeInput = this.strafeInput;  
  
  boolean forward = Keyboard.isKeyDown(FORWARD);
  boolean reverse = Keyboard.isKeyDown(BACKWARD);
  this.forwardsInput = forward && reverse ? 0 : reverse ? -1 : forward? 1 : 0;
  
  boolean left = Keyboard.isKeyDown(LEFT);
  boolean right = Keyboard.isKeyDown(RIGHT);
  this.strafeInput = left && right ? 0 : left ? -1 : right ? 1 : 0; 
  }

public boolean checkForInput()
  {
  this.updateMovementInputFlags();
  return this.forwardsInput != this.prevForwardsInput || this.strafeInput !=this.prevStrafeInput;
  }

public boolean checkForFire()
  {
  this.prevFireInput = this.fireInput;
  this.fireInput = Keyboard.isKeyDown(FIRE);
  return this.fireInput!=this.prevFireInput && this.fireInput;
  }

/**
 * used when a vehicle is dismounted by local client, to clear inputHelper input caching
 */
public void clearInput()
  {
  this.prevForwardsInput = 0;
  this.prevStrafeInput = 0;
  this.prevFireInput = false;
  this.forwardsInput = 0;
  this.fireInput = false;
  this.strafeInput = 0;  
  }

}
