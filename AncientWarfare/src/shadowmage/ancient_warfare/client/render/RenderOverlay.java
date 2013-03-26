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
package shadowmage.ancient_warfare.client.render;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import shadowmage.ancient_warfare.common.config.Settings;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;




public class RenderOverlay extends Gui implements ITickHandler
{

private Minecraft mc = Minecraft.getMinecraft();
private FontRenderer fontRenderer = mc.fontRenderer;

public void renderOverlay()
  {
  
  }

@Override
public void tickStart(EnumSet<TickType> type, Object... tickData)
  {
  
  }

@Override
public void tickEnd(EnumSet<TickType> type, Object... tickData)
  {
  if(Settings.getRenderOverlay() && mc.currentScreen==null && mc.thePlayer!=null && mc.thePlayer.ridingEntity instanceof VehicleBase)
    {
    VehicleBase vehicle = (VehicleBase) mc.thePlayer.ridingEntity;
    this.drawString(fontRenderer, "Range: "+vehicle.firingHelper.clientHitRange, 10, 10, 0xffffffff);
    this.drawString(fontRenderer, "Pitch: "+vehicle.firingHelper.clientTurretPitch, 10, 20, 0xffffffff);
    this.drawString(fontRenderer, "Yaw: "+vehicle.firingHelper.clientTurretYaw, 10, 30, 0xffffffff);
    this.drawString(fontRenderer, "Velocity: "+vehicle.firingHelper.clientLaunchSpeed, 10, 40, 0xffffffff);
    IAmmoType ammo = vehicle.ammoHelper.getCurrentAmmoType();
    if(ammo!=null)
      {
      int count = vehicle.ammoHelper.getCurrentAmmoCount();
      this.drawString(fontRenderer, "Ammo: "+ammo.getDisplayName(), 10, 50, 0xffffffff);
      this.drawString(fontRenderer, "Count: "+count, 10, 60, 0xffffffff);
      }
    else
      {
      this.drawString(fontRenderer, "No Ammo Selected", 10, 50, 0xffffffff);
      }    
    if(Settings.getRenderAdvOverlay())
      {
      this.drawString(fontRenderer, "FSp: "+vehicle.moveHelper.forwardMotion*20.f, 10, 70, 0xffffffff);
      this.drawString(fontRenderer, "SSp: "+vehicle.moveHelper.strafeMotion*20.f, 10, 80, 0xffffffff);
      this.drawString(fontRenderer, "Wgt: "+vehicle.currentWeight+ " base: "+vehicle.baseWeight, 10, 90, 0xffffffff);
      this.drawString(fontRenderer, "Speed: "+vehicle.currentForwardSpeedMax*20.f+ " base: "+vehicle.baseForwardSpeed*20.f + " root: "+vehicle.vehicleType.getBaseForwardSpeed()*20.f, 10, 100, 0xffffffff);    
      float weightAdjust = 1.f;
      if(vehicle.currentWeight > vehicle.baseWeight)
        {
        weightAdjust = vehicle.baseWeight  / vehicle.currentWeight;
        }
      this.drawString(fontRenderer, "WeightAdjusted max Speed: "+vehicle.currentForwardSpeedMax*weightAdjust*20.f, 10, 110, 0xffffffff);
      }    
    }   
  }

@Override
public EnumSet<TickType> ticks()
  {
  return EnumSet.of(TickType.RENDER);
  }

@Override
public String getLabel()
  {
  return "AWOverlayRender";
  }

}
