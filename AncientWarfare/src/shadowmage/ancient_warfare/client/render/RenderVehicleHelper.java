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

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import shadowmage.ancient_warfare.client.registry.RenderRegistry;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class RenderVehicleHelper extends Render
{

@Override
public void doRender(Entity var1, double x, double y, double z, float yaw, float tick)
  {
  VehicleBase vehicle = (VehicleBase) var1;
  GL11.glPushMatrix();
  GL11.glTranslated(x, y, z);
  GL11.glRotatef(yaw, 0, 1, 0);
  GL11.glScalef(-1, -1, 1);    
  Minecraft.getMinecraft().renderEngine.bindTexture(Minecraft.getMinecraft().renderEngine.getTexture(var1.getTexture()));
  RenderRegistry.instance().getRenderForVehicle(vehicle.vehicleType.getGlobalVehicleType()).renderVehicle(vehicle, x, y, z, yaw, tick);  
  GL11.glPopMatrix();
  }

}
