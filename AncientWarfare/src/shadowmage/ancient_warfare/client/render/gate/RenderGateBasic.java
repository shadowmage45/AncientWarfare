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
package shadowmage.ancient_warfare.client.render.gate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.client.model.ModelGateBasic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.gates.EntityGate;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class RenderGateBasic extends Render
{

ModelGateBasic model = new ModelGateBasic();

public RenderGateBasic()
  {
  
  }

@Override
public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
  {
  GL11.glPushMatrix();  
  EntityGate g = (EntityGate) entity;
  BlockPosition min = BlockTools.getMin(g.pos1, g.pos2);
  BlockPosition max = BlockTools.getMax(g.pos1, g.pos2);
 
  boolean xAxis = min.x == max.x;
  
  float axisRotation = 0;
  
  if(!xAxis)
    {
    axisRotation = 90;
    }
  model.setModelRotation(axisRotation);
  
  float tx = xAxis ? 0 : 1;
  float ty = -1;
  float tz = xAxis ? 1 : 0;
  int renderedPiece = 0;
  float wid = xAxis ? max.z-min.z + 1 : max.x-min.x + 1;
  float hi = max.y - min.y + 1;
  
//  GL11.glTranslatef(tx*wid*0.5f, 0, tz*wid*0.5f);
  
  for(int y = 0; y<hi; y++)
    {    
    GL11.glPushMatrix();
    for(int x = 0; x <wid; x++)
      {
      if(y == hi-1 && x>0 && x<wid-1)
        {
        model.renderTop();
        }
      else if(y==hi-1 && x==0)
        {
        model.renderCorner();
        }
      else if(y==hi-1 && x==wid-1)
        {
        model.setModelRotation(axisRotation+180);
        model.renderCorner();
        }
      else if(x==0)
        {
        model.renderSide();
        }
      else if(x==wid-1)
        {
        model.setModelRotation(axisRotation+180);
        model.renderSide();
        }
      if(y + g.edgePosition <= hi)
        {
        GL11.glPushMatrix();
        GL11.glTranslatef(0, -g.edgePosition, 0);
        model.setModelRotation(axisRotation);
        model.renderSolidWall();
        GL11.glPopMatrix();
        }      
      GL11.glTranslatef(tx, 0, tz);
      }
    GL11.glPopMatrix();
    GL11.glTranslatef(0, ty, 0);
    }
  GL11.glPopMatrix();
  }

}
