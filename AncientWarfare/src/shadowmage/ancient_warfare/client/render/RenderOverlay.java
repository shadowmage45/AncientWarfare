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
package shadowmage.ancient_warfare.client.render;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import shadowmage.ancient_warfare.common.config.Settings;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.VehicleMovementType;
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

public void renderVehicleOverlay()
  {
  VehicleBase vehicle = (VehicleBase) mc.thePlayer.ridingEntity;
  int white = 0xffffffff;
  if(vehicle.vehicleType.getMovementType()==VehicleMovementType.AIR1 || vehicle.vehicleType.getMovementType()==VehicleMovementType.AIR2)
    {
    this.drawString(fontRenderer, "Throttle: "+vehicle.moveHelper.throttle, 10, 10, white);    
    this.drawString(fontRenderer, "Pitch: "+vehicle.rotationPitch, 10, 20, white);
    this.drawString(fontRenderer, "Climb Rate: "+vehicle.motionY*20, 10, 30, white);
    this.drawString(fontRenderer, "Elevation: "+vehicle.posY, 10, 40, white);
    }
  else
    {
    this.drawString(fontRenderer, "Range: "+vehicle.firingHelper.clientHitRange, 10, 10, white);
    this.drawString(fontRenderer, "Pitch: "+vehicle.firingHelper.clientTurretPitch, 10, 20, white);
    this.drawString(fontRenderer, "Yaw: "+vehicle.firingHelper.clientTurretYaw, 10, 30, white);
    this.drawString(fontRenderer, "Velocity: "+vehicle.firingHelper.clientLaunchSpeed, 10, 40, white);    
    }
  IAmmoType ammo = vehicle.ammoHelper.getCurrentAmmoType();
  if(ammo!=null)
    {
    int count = vehicle.ammoHelper.getCurrentAmmoCount();
    this.drawString(fontRenderer, "Ammo: "+StatCollector.translateToLocal(ammo.getDisplayName()), 10, 50, white);
    this.drawString(fontRenderer, "Count: "+count, 10, 60, white);
    }
  else
    {
    this.drawString(fontRenderer, "No Ammo Selected", 10, 50, white);
    }    
  if(Settings.getRenderAdvOverlay())
    {
    float velocity = Trig.getVelocity(vehicle.motionX, vehicle.motionZ);
    this.drawString(fontRenderer, "Velocity: "+velocity*20.f+"m/s  max: " + vehicle.currentForwardSpeedMax*20, 10, 70, white);
    this.drawString(fontRenderer, "Yaw Rate: "+vehicle.moveHelper.getRotationSpeed()*20.f, 10, 80, white);   
    } 
  }

@Override
public void tickEnd(EnumSet<TickType> type, Object... tickData)
  {
  if(Settings.getRenderOverlay() && mc.currentScreen==null && mc.thePlayer!=null)
    {
    if(mc.thePlayer.ridingEntity instanceof VehicleBase)
      {
      this.renderVehicleOverlay();      
      }
    else
      {
      ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
      if(stack!=null && stack.getItem()==ItemLoader.civicPlacer)
        {
        int xSize = 0;
        int ySize = 0;
        int zSize = 0;
        int cube = 0;
        BlockPosition p1;
        BlockPosition p2;
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("civicInfo"))
          {
          NBTTagCompound tag = stack.getTagCompound().getCompoundTag("civicInfo");
          if(tag.hasKey("pos1"))
            {
            p1 = new BlockPosition(tag.getCompoundTag("pos1"));
            }
          else
            {
            p1 = BlockTools.getBlockClickedOn(mc.thePlayer, mc.theWorld, mc.thePlayer.isSneaking());
            if(p1==null)
              {
              p1 = new BlockPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
              }
            }
          if(tag.hasKey("pos2"))
            {
            p2 = new BlockPosition(tag.getCompoundTag("pos2"));
            }
          else
            {
            p2 = BlockTools.getBlockClickedOn(mc.thePlayer, mc.theWorld, mc.thePlayer.isSneaking());
            if(p2==null)
              {
              p2 = new BlockPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
              }
            }
          xSize = Math.abs(p2.x-p1.x)+1;
          ySize = Math.abs(p2.y-p1.y)+1;
          zSize = Math.abs(p2.z-p1.z)+1;
          cube = xSize*ySize*zSize;
          }

        this.drawString(fontRenderer, "X-Size: "+xSize, 10, 10, 0xffffffff);
        this.drawString(fontRenderer, "Y-Size: "+ySize, 10, 20, 0xffffffff);
        this.drawString(fontRenderer, "Z-Size: "+zSize, 10, 30, 0xffffffff);
        this.drawString(fontRenderer, "Cube  : "+cube, 10, 40, 0xffffffff);
        }
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
