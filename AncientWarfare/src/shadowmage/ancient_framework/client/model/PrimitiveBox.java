/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_framework.client.model;

import org.lwjgl.opengl.GL11;

public class PrimitiveBox extends Primitive
{
float ratio = 0.0625f;

public PrimitiveBox(ModelPiece parent)
  {
  super(parent);
  }

public PrimitiveBox(ModelPiece parent, float x1, float y1, float z1, float x2, float y2, float z2, float rx, float ry, float rz, float tx, float ty)
  {
  this(parent);
  this.x1 = x1;
  this.y1 = y1;
  this.z1 = z1;
  this.x2 = x2;
  this.y2 = y2;
  this.z2 = z2;
  this.rx = rx;
  this.ry = ry;
  this.rz = rz;
  this.tx = tx;
  this.ty = ty;
  }

public float tx, ty;//texture offsets, in texture space (0->1)

public void render()
  {
  
  
//render the cube. only called a single time when building the display list for a piece
  GL11.glPushMatrix();
  if(rx!=0){GL11.glRotatef(rx, 1, 0, 0);}
  if(ry!=0){GL11.glRotatef(ry, 0, 1, 0);}
  if(rz!=0){GL11.glRotatef(rz, 0, 0, 1);}  
  
  //front side
  GL11.glBegin(GL11.GL_QUADS);
  
  GL11.glTexCoord2f(tx, ty);//offset for the coords for the 'front' face
  GL11.glVertex3f(x2, y1, z1);
  GL11.glVertex3f(x1, y1, z1);
  GL11.glVertex3f(x1, y2, z1);
  GL11.glVertex3f(x2, y2, z1);
  
  //right side
  GL11.glTexCoord2f(tx, ty);//offset for the coords for the 'right' face
  GL11.glVertex3f(x1, y1, z1);
  GL11.glVertex3f(x1, y1, z2);
  GL11.glVertex3f(x1, y2, z2);
  GL11.glVertex3f(x1, y2, z1);
  
//  //left side
  GL11.glTexCoord2f(tx, ty);//offset for the coords for the 'left' face
  GL11.glVertex3f(x2, y1, z2);
  GL11.glVertex3f(x2, y1, z1);
  GL11.glVertex3f(x2, y2, z1);
  GL11.glVertex3f(x2, y2, z2);
  
//  //top side
  GL11.glTexCoord2f(tx, ty);//offset for the coords for the 'top' face
  GL11.glVertex3f(x2, y2, z1);
  GL11.glVertex3f(x1, y2, z1);
  GL11.glVertex3f(x1, y2, z2);
  GL11.glVertex3f(x2, y2, z2);
  
//  //bottom side
  GL11.glTexCoord2f(tx, ty);//offset for the coords for the 'bottom' face
  GL11.glVertex3f(x2, y1, z2);
  GL11.glVertex3f(x1, y1, z2);
  GL11.glVertex3f(x1, y1, z1);
  GL11.glVertex3f(x2, y1, z1);
//  
//  //rear side
  GL11.glTexCoord2f(tx, ty);//offset for the coords for the 'rear' face
  GL11.glVertex3f(x1, y1, z2);
  GL11.glVertex3f(x2, y1, z2);
  GL11.glVertex3f(x2, y2, z2);
  GL11.glVertex3f(x1, y2, z2);
  
  GL11.glEnd();
  GL11.glPopMatrix();  
  }

@Override
public Primitive copy()
  {
  // TODO Auto-generated method stub
  return null;
  }

}
