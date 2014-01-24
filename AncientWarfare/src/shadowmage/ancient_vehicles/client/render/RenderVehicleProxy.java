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
package shadowmage.ancient_vehicles.client.render;

import java.util.HashMap;

import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import shadowmage.ancient_vehicles.client.model.ModelVehicleBase;
import shadowmage.ancient_vehicles.common.vehicle.EntityVehicle;

public class RenderVehicleProxy extends RenderEntity
{

private static HashMap<String, RenderEntity> vehicleRenders = new HashMap<String, RenderEntity>();
private static HashMap<String, ModelVehicleBase> vehicleModels = new HashMap<String, ModelVehicleBase>();
private static HashMap<String, ResourceLocation> vehicleTextures = new HashMap<String, ResourceLocation>();


@Override
public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTick)
  {
  if(entity.getClass()==EntityVehicle.class)
    {
    EntityVehicle vehicle = (EntityVehicle)entity;
    RenderEntity render = vehicleRenders.get(vehicle.getVehicleType().getName());
    if(render!=null)
      {
      render.doRender(vehicle, x, y, z, yaw, partialTick);
      return;
      }
    }
  super.doRender(entity, x, y, z, yaw, partialTick);
  }

@Override
protected ResourceLocation getEntityTexture(Entity entity)
  {
  return entity instanceof EntityVehicle ? vehicleTextures.get(((EntityVehicle)entity).getVehicleType().getName()) : null;
  }


}
