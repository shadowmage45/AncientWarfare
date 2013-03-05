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
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import org.lwjgl.input.Keyboard;

import shadowmage.ancient_warfare.common.config.Config;
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

private int aimInputUpdateTicks = 5;

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
    this.handleFireAction();    
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
  if(mc.thePlayer!=null && mc.thePlayer.ridingEntity instanceof VehicleBase && !mc.isGamePaused && mc.currentScreen==null)
    {
    this.handleAimUpdate();
    }
  }

int inputUpdateTicks = 0;

public void handleAimUpdate()
  {
  inputUpdateTicks--;
  if(inputUpdateTicks>0)
    {
    return;
    }
  inputUpdateTicks = 5;
  MovingObjectPosition pos = getPlayerLookTargetClient(mc.thePlayer, 140);
  if(pos!=null)
    {
    ((VehicleBase)mc.thePlayer.ridingEntity).firingHelper.handleAimInput(pos.hitVec);
    }
  }

public void handleFireAction()
  {
  if(mc.thePlayer!=null && mc.thePlayer.ridingEntity instanceof VehicleBase)
    {
    VehicleBase vehicle = (VehicleBase) mc.thePlayer.ridingEntity;
    MovingObjectPosition pos = getPlayerLookTargetClient(mc.thePlayer, 140);
    if(pos!=null)
      {
      vehicle.firingHelper.handleFireInput(pos.hitVec);
      }
    else
      {
      vehicle.firingHelper.handleFireInput(null);
      } 
    }
  }

public MovingObjectPosition getPlayerLookTargetClient(EntityPlayer player, float range)
  {
  Vec3 playerPos = player.getPosition(0);
  Vec3 lookVector = player.getLook(0);
  Vec3 endVector = playerPos.addVector(lookVector.xCoord * range, lookVector.yCoord * range, lookVector.zCoord * range);

  MovingObjectPosition blockHit = player.worldObj.rayTraceBlocks(playerPos, endVector);
  
  float var9 = 1.f;
  
  float closestFound = 0.f;
  if(blockHit!=null)
    {
    closestFound = (float) blockHit.hitVec.distanceTo(playerPos);
    }
  List possibleHitEntities = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.renderViewEntity, this.mc.renderViewEntity.boundingBox.addCoord(lookVector.xCoord * range, lookVector.yCoord * range, lookVector.zCoord * range).expand((double)var9, (double)var9, (double)var9));
  Iterator<Entity> it = possibleHitEntities.iterator();
  Entity hitEntity = null;
  Entity currentExaminingEntity = null;
  while(it.hasNext())
    {
    currentExaminingEntity = it.next();
    if(currentExaminingEntity.canBeCollidedWith())
      {
      float borderSize = currentExaminingEntity.getCollisionBorderSize();
      AxisAlignedBB entBB = currentExaminingEntity.boundingBox.expand((double)borderSize, (double)borderSize, (double)borderSize);
      MovingObjectPosition var17 = entBB.calculateIntercept(playerPos, endVector);

      if (entBB.isVecInside(playerPos))
        {
        if (0.0D < closestFound || closestFound == 0.0D)
          {
          hitEntity = currentExaminingEntity;
          closestFound = 0.0f;
          }
        }
      else if (var17 != null)
        {
        double var18 = playerPos.distanceTo(var17.hitVec);

        if (var18 < closestFound || closestFound == 0.0D)
          {
          hitEntity = currentExaminingEntity;
          closestFound = (float) var18;
          }
        }
      }
    }
  if(hitEntity!=null)
    {
    //Config.logDebug("entity hit!!");
    blockHit = new MovingObjectPosition(hitEntity);
    }
  //get bounding box of entities between var4 and either var6 or blockHit.hitVec..
  //    break into segments by length, check in chunks, plan for overlap?

  return blockHit;
  }


}
