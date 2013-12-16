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

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_framework.client.render.AWTextureManager;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_warfare.client.model.ModelTEBase;
import shadowmage.ancient_warfare.client.registry.RenderRegistry;
import shadowmage.ancient_warfare.common.crafting.TEAWCrafting;

public class RenderCraftingHelper extends TileEntitySpecialRenderer implements IItemRenderer
{

private static RenderCraftingHelper INSTANCE= null;
public static RenderCraftingHelper instance()
  {
  if (INSTANCE==null)
    {
    INSTANCE= new RenderCraftingHelper();
    }
  return INSTANCE;
  }

/**
 * 
 */
public RenderCraftingHelper()
  {
  // TODO Auto-generated constructor stub
  }

@Override
public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
  {
  GL11.glPushMatrix();
  GL11.glTranslated(d0+0.5d, d1, d2+0.5d);
  TEAWCrafting craft = (TEAWCrafting)tileentity;
  GL11.glRotatef(-90 * BlockTools.getTurnsForRender(craft.getOrientation()), 0, 1, 0);
  GL11.glScalef(-1, -1, 1);
  ModelTEBase model = RenderRegistry.instance().getTEModel(craft.getModelID());
  AWTextureManager.bindTexture(RenderRegistry.instance().getTEModelTexture(craft.getModelID()));
  model.renderModel(craft);
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
  GL11.glScalef(-1, -1, 1);
  GL11.glTranslatef(-0.5f, 0.0f, 0.5f);
  GL11.glRotatef(270, 0, 1, 0);
  ModelTEBase model = RenderRegistry.instance().getTEModel(item.getItemDamage());
  AWTextureManager.bindTexture(RenderRegistry.instance().getTEModelTexture(item.getItemDamage()));
  model.renderModel();
  GL11.glPopMatrix();
  }

}
