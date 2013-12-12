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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.client.registry.RenderRegistry;
import shadowmage.ancient_warfare.common.gates.EntityGate;

public class RenderGateHelper extends Render
{

Minecraft mc = Minecraft.getMinecraft();
/**
 * 
 */
public RenderGateHelper()
  {
  
  }

@Override
public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
  {
  GL11.glPushMatrix();
  EntityGate gate = (EntityGate)entity;
  int typeNum = gate.getGateType().getGlobalID();
  
  if(gate.hurtAnimationTicks>0)
    {
    float percent = ((float)gate.hurtAnimationTicks / 20.f);
    GL11.glColor4f(1.f, 1.f-percent, 1.f-percent, 1.f);
    }
  AWTextureManager.bindTexture(((EntityGate)entity).getTexture());
  GL11.glTranslated(d0, d1, d2);
  GL11.glRotatef(f, 0, 1, 0);
  GL11.glScalef(-1, -1, 1);  
  RenderRegistry.instance().getGateRender(typeNum).doRender(entity, d0, d1, d2, f, f1);
  GL11.glColor4f(1.f, 1.f, 1.f, 1.f);
  GL11.glPopMatrix();
  }

@Override
protected ResourceLocation getEntityTexture(Entity entity)
  {
  return AWTextureManager.getResource(((EntityGate)entity).getTexture());
  }


}
