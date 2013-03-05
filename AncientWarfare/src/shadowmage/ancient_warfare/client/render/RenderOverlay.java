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
  if(Settings.renderOverlay && mc.currentScreen==null && mc.thePlayer!=null && mc.thePlayer.ridingEntity instanceof VehicleBase)
    {
    VehicleBase vehicle = (VehicleBase) mc.thePlayer.ridingEntity;
    this.drawString(fontRenderer, "Range: "+vehicle.firingHelper.clientHitRange, 10, 10, 0xffffffff);
    this.drawString(fontRenderer, "Pitch: "+vehicle.firingHelper.clientTurretPitch, 10, 20, 0xffffffff);
    this.drawString(fontRenderer, "Yaw: "+vehicle.firingHelper.clientTurretYaw, 10, 30, 0xffffffff);
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
