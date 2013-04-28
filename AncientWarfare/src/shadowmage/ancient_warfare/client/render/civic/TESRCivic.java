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
package shadowmage.ancient_warfare.client.render.civic;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import shadowmage.ancient_warfare.client.render.BoundingBoxRender;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.utils.Pos3f;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.missiles.AmmoHwachaRocket;

public class TESRCivic extends TileEntitySpecialRenderer
{

/**
 * 
 */
public TESRCivic()
  {
  // TODO Auto-generated constructor stub
  }

@Override
public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
  {
  TECivic te = (TECivic)tileentity;
  if(te.getCivic()==null || !te.getCivic().isWorkSite())
    {
    return;
    }
//  Config.logDebug("rendering at: "+d0+","+d1+","+d2);
  double minX = d0 + te.minX - te.xCoord;
  double minY = d1 + te.minY - te.yCoord;
  double minZ = d2 + te.minZ - te.zCoord;
  
  double maxX = d0 + te.maxX - te.xCoord + 1;
  double maxY = d1 + te.maxY - te.yCoord + 1;
  double maxZ = d2 + te.maxZ - te.zCoord + 1;
  
  double xSize = te.maxX - te.minX + 1;
  double ySize = te.maxY - te.minY + 1;
  double zSize = te.maxZ - te.minZ + 1;
  
  AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
  BoundingBoxRender.drawOutlinedBoundingBox(bb.expand(0.03d, 0.03d, 0.03d), 1.f, 0.f, 0.f);
  BoundingBoxRender.drawOutlinedBoundingBox(bb.expand(-0.03d, -0.03d, -0.03d), 1.f, 0.f, 0.f);
  
//  
//  GL11.glPushMatrix();
//  GL11.glEnable(GL11.GL_BLEND);
//  GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//  GL11.glDisable(GL11.GL_TEXTURE_2D);
//  GL11.glColor4d(1, 1, 1, 0.6d);
//  
//  /**
//   * draw a straight line on the ground
//   */
//  GL11.glLineWidth(3f);
//  GL11.glBegin(GL11.GL_LINES);    
//  GL11.glVertex3d(minX, minY, minZ);
//  GL11.glVertex3d(minX, minY, minZ+zSize);  
//  GL11.glVertex3d(minX, minY, minZ+zSize);
//  GL11.glVertex3d(maxX, maxY, maxZ);  
//  GL11.glEnd();
//    
//  GL11.glPopMatrix();
//  GL11.glDisable(GL11.GL_BLEND);
//  GL11.glEnable(GL11.GL_TEXTURE_2D);
  
  }


}
