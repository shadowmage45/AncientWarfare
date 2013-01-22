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

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;


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

public static void renderBoundingBox(BlockPosition hit, int face, BlockPosition offset, BlockPosition size)
  {
  BlockPosition p1 = hit.copy();
  p1.moveLeft(face, offset.x);
  p1.moveForward(face, offset.z);

  BlockPosition p2 = p1.copy();
  p2.moveRight(face, size.x+1);
  p2.moveForward(face, size.z+1);
  p2.y += size.y+1;

  GL11.glEnable(GL11.GL_BLEND);
  GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
  GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
  GL11.glLineWidth(2.0F);
  GL11.glDisable(GL11.GL_TEXTURE_2D);
  GL11.glDepthMask(false);
  
  drawOutlinedBoundingBox(AxisAlignedBB.getBoundingBox(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z));
  
  GL11.glDepthMask(true);
  GL11.glEnable(GL11.GL_TEXTURE_2D);
  GL11.glDisable(GL11.GL_BLEND);
  }

private static void drawOutlinedBoundingBox(AxisAlignedBB bb)
  {
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
  }

}
