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
package shadowmage.ancient_warfare.client.render.machine;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_warfare.client.model.ModelEngine;
import shadowmage.ancient_warfare.client.render.AWTextureManager;
import shadowmage.ancient_warfare.common.machine.EngineData;
import shadowmage.ancient_warfare.common.machine.TEEngine;

public class RenderTEMotor extends TileEntitySpecialRenderer implements IItemRenderer
{

ModelEngine teModel = new ModelEngine();

@Override
public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
  {
  GL11.glPushMatrix();
  TEEngine engine = (TEEngine)tileentity;
  String tex = Statics.texturePath+"models/"+engine.getTexture();
  AWTextureManager.bindTexture(tex);
  GL11.glTranslated(d0+0.5d, d1+0.5d, d2+0.5d);
  teModel.setDirection(engine.getFacing());
  teModel.setPistonPosition(engine.getPistonProgress(), f, engine.getPistonDirection());
  teModel.renderEngine();
  this.renderExtras(engine, f);
  GL11.glPopMatrix();
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
  String tex = Statics.texturePath+"models/"+EngineData.getEngineTexture(item.getItemDamage());
  AWTextureManager.bindTexture(tex);
  GL11.glTranslated(0.5d, 0.5d, 0.5d);
  teModel.setDirection(ForgeDirection.UP);
  teModel.setPistonPosition(4.f, 0, (byte) 0);
  teModel.renderEngine();
  GL11.glPopMatrix();
  }

public void renderExtras(TEEngine te, float f)
  {
  
  }

}
