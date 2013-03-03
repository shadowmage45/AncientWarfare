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

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.input.Keyboard;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;


public class InputHelper extends KeyHandler implements IHandleInput
{


/**
 * VANILLA KEYBINDS (TO SHOW UP IN VANILLA CONFIG MENU)
 */
static KeyBinding options = new KeyBinding("AW-options", Keyboard.KEY_F7);
private static KeyBinding[] keys = new KeyBinding[]{options};
private static boolean[] keyRepeats = new boolean []{false};
private static Minecraft mc = Minecraft.getMinecraft();

private boolean hasMoveInput = false;


private static InputHelper INSTANCE;
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

public void loadKeysFromConfig()
  {
  KeybindManager.addHandler(this);

  forward = new Keybind(Config.getKeyBindID("keybind.forward", Keyboard.KEY_W, "forwards/accelerate"), "Forward");
  KeybindManager.addKeybind(forward);
  reverse = new Keybind(Config.getKeyBindID("keybind.reverse", Keyboard.KEY_S, "reverse/deccelerate"), "Reverse");
  KeybindManager.addKeybind(reverse);
  left = new Keybind(Config.getKeyBindID("keybind.left", Keyboard.KEY_A, "turn/strafe left"), "Left Turn");
  KeybindManager.addKeybind(left);
  right = new Keybind(Config.getKeyBindID("keybind.right", Keyboard.KEY_D, "turn/strafe right"), "Right Turn");
  KeybindManager.addKeybind(right);
  fire = new Keybind(Config.getKeyBindID("keybind.fire", Keyboard.KEY_SPACE, "fire missile"), "Fire");
  KeybindManager.addKeybind(fire);
  }

@Override
public String getLabel()
  {
  return "AWKeybinds";
  }

/**
 * VANILLA KEYBINDS....
 */
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
  return (byte) 0;
  }

public static byte getStrafeInput()
  {
  return (byte) 0;
  }

public static boolean getFireInput()
  {
  return false;
  }

public boolean checkForInput()
  {
  return false;
  }

public boolean checkForFire()
  {
  return false;
  }


/**
 * AWKEYBINDS....
 */
@Override
public void onKeyUp(Keybind kb)
  {
  if(kb==forward || kb==left || kb==right || kb==reverse)
    {
    hasMoveInput = true;
    }
  }

@Override
public void onKeyPressed(Keybind kb)
  {
  if(kb==forward || kb==left || kb==right || kb==reverse)
    {
    hasMoveInput = true;
    }
  if(kb==fire)
    {
    if(mc.thePlayer!=null && mc.thePlayer.ridingEntity instanceof VehicleBase)
      {
      Packet02Vehicle pkt = new Packet02Vehicle();
      NBTTagCompound tag = new NBTTagCompound();
      tag.setBoolean("fm", true);
      pkt.setParams(mc.thePlayer.ridingEntity);
      pkt.setInputData(tag);
      pkt.sendPacketToServer();
      }
    }
  }

@Override
public void onTickEnd()
  {
  if(hasMoveInput)
    {
    hasMoveInput = false;
    if(mc.thePlayer!=null && mc.thePlayer.ridingEntity instanceof VehicleBase)
      {
      int strafe = right.isPressed && left.isPressed ? 0 : left.isPressed ? -1 : right.isPressed ? 1 : 0;
      int forwards = forward.isPressed && reverse.isPressed ? 0 : reverse.isPressed ? -1 : forward.isPressed ? 1 : 0;
      ((VehicleBase)mc.thePlayer.ridingEntity).handleKeyboardMovement((byte)forwards, (byte)strafe);
      }
    }  
  }



}
