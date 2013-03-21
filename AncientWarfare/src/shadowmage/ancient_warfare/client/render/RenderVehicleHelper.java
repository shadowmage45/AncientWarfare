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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.client.model.ModelVehicleBase;
import shadowmage.ancient_warfare.client.registry.RenderRegistry;
import shadowmage.ancient_warfare.common.item.ItemVehicleSpawner;
import shadowmage.ancient_warfare.common.vehicles.IVehicleType;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.types.VehicleType;

public class RenderVehicleHelper extends Render implements IItemRenderer
{

private RenderVehicleHelper(){}
private static RenderVehicleHelper INSTANCE;
public static RenderVehicleHelper instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new RenderVehicleHelper();
    }
  return INSTANCE;
  }

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

public static void renderVehicleModel(int typeNum, int level)
  {
  IVehicleType type = VehicleType.getVehicleType(typeNum);
  ModelVehicleBase model = RenderRegistry.instance().getVehicleModel(typeNum);
  if(type!=null && model!= null)
    {
    GL11.glPushMatrix();
    GL11.glScalef(-1, -1, 1);    
    Minecraft.getMinecraft().renderEngine.bindTexture(Minecraft.getMinecraft().renderEngine.getTexture(type.getTextureForMaterialLevel(level)));    
    model.render(null, 0, 0, 0, 0, 0, 0.0625f);
    GL11.glPopMatrix();
    }
  }

@Override
public boolean handleRenderType(ItemStack item, ItemRenderType type)
  {  
  return true;
  }

@Override
public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
  {
  return true;
  }

@Override
public void renderItem(ItemRenderType type, ItemStack item, Object... data)
  {
  GL11.glPushMatrix();
  GL11.glScalef(0.35f, 0.35f, 0.35f);
  GL11.glTranslatef(0, -1.f, 0);
  renderVehicleModel(item.getItemDamage(), ItemVehicleSpawner.getVehicleLevelForStack(item));
  GL11.glPopMatrix();
  }

}
