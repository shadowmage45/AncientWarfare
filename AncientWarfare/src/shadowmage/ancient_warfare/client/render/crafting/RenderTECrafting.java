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
package shadowmage.ancient_warfare.client.render.crafting;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.client.model.ModelTEBase;
import shadowmage.ancient_warfare.client.registry.RenderRegistry;
import shadowmage.ancient_warfare.common.crafting.TEAWCrafting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderTECrafting implements ISimpleBlockRenderingHandler
{

/**
 * 
 */
public RenderTECrafting()
  {
  // TODO Auto-generated constructor stub
  }

@Override
public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
  {
  // TODO Auto-generated method stub
  }

@Override
public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
  {
  GL11.glPushMatrix();
  GL11.glScalef(-1, -1, 1);
//  Config.logDebug("rendering crafting TE model");
  Minecraft.getMinecraft().renderEngine.bindTexture("foo");
  TEAWCrafting craft = (TEAWCrafting)world.getBlockTileEntity(x, y, z);
  ModelTEBase model = RenderRegistry.instance().getTEModel(craft.getModelID());
  model.renderModel(craft);
  Minecraft.getMinecraft().renderEngine.resetBoundTexture();
  GL11.glPopMatrix();
  return false;
  }

@Override
public boolean shouldRender3DInInventory()
  {
  // TODO Auto-generated method stub
  return true;
  }

@Override
public int getRenderId()
  {
  // TODO Auto-generated method stub
  return 0;
  }

}
