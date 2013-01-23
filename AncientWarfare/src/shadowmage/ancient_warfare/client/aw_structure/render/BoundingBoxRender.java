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
package shadowmage.ancient_warfare.client.aw_structure.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_core.item.ItemLoader;
import shadowmage.ancient_warfare.common.aw_core.utils.Pos3f;
import shadowmage.ancient_warfare.common.aw_structure.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.aw_structure.store.StructureManager;


public class BoundingBoxRender
{

private static BoundingBoxRender INSTANCE;
private BoundingBoxRender(){}
public static BoundingBoxRender instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE= new BoundingBoxRender();
    }
  return INSTANCE;
  }

/**
 * draw a player-position-normalized bounding box (can only be called during worldRender)
 * @param bb
 */
public static void drawOutlinedBoundingBox(AxisAlignedBB bb)
  {
  GL11.glEnable(GL11.GL_BLEND);
  GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
  GL11.glColor4f(0.8F, 0.0F, 0.0F, 0.4F);
  GL11.glLineWidth(8.0F);
  GL11.glDisable(GL11.GL_TEXTURE_2D);
  GL11.glDepthMask(false);
  Tessellator tess = Tessellator.instance;
  tess.startDrawing(3);
  tess.addVertex(bb.minX, bb.minY, bb.minZ);
  tess.addVertex(bb.maxX, bb.minY, bb.minZ);
  tess.addVertex(bb.maxX, bb.minY, bb.maxZ);
  tess.addVertex(bb.minX, bb.minY, bb.maxZ);
  tess.addVertex(bb.minX, bb.minY, bb.minZ);
  tess.draw();
  tess.startDrawing(3);
  tess.addVertex(bb.minX, bb.maxY, bb.minZ);
  tess.addVertex(bb.maxX, bb.maxY, bb.minZ);
  tess.addVertex(bb.maxX, bb.maxY, bb.maxZ);
  tess.addVertex(bb.minX, bb.maxY, bb.maxZ);
  tess.addVertex(bb.minX, bb.maxY, bb.minZ);
  tess.draw();
  tess.startDrawing(1);
  tess.addVertex(bb.minX, bb.minY, bb.minZ);
  tess.addVertex(bb.minX, bb.maxY, bb.minZ);
  tess.addVertex(bb.maxX, bb.minY, bb.minZ);
  tess.addVertex(bb.maxX, bb.maxY, bb.minZ);
  tess.addVertex(bb.maxX, bb.minY, bb.maxZ);
  tess.addVertex(bb.maxX, bb.maxY, bb.maxZ);
  tess.addVertex(bb.minX, bb.minY, bb.maxZ);
  tess.addVertex(bb.minX, bb.maxY, bb.maxZ);
  tess.draw();
  GL11.glDepthMask(true);
  GL11.glEnable(GL11.GL_TEXTURE_2D);
  GL11.glDisable(GL11.GL_BLEND);
  }

}
