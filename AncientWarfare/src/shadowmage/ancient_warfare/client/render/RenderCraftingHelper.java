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

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.client.model.ModelTEBase;
import shadowmage.ancient_warfare.client.registry.RenderRegistry;
import shadowmage.ancient_warfare.common.crafting.TEAWCrafting;

public class RenderCraftingHelper extends TileEntitySpecialRenderer
{
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
  GL11.glScalef(-1, -1, 1);
  TEAWCrafting craft = (TEAWCrafting)tileentity;
  ModelTEBase model = RenderRegistry.instance().getTEModel(craft.getModelID());
  model.renderModel(craft);
  GL11.glPopMatrix();
  }

}
