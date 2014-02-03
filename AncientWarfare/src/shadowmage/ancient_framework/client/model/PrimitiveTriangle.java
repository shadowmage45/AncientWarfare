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

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_framework.common.config.AWLog;

public class PrimitiveTriangle extends Primitive
{

float x1, y1, x2, y2, x3, y3;
/**
 * @param parent
 */
public PrimitiveTriangle(ModelPiece parent)
  {
  super(parent);
  }

public float x1(){return x1;}
public float y1(){return y1;}
public float x2(){return x2;}
public float y2(){return y2;}
public float x3(){return x3;}
public float y3(){return y3;}

@Override
protected void renderForDisplayList()
  {

  float tw = parent.getModel().textureWidth;
  float th = parent.getModel().textureHeight;
  float px = 1.f/tw;
  float py = 1.f/th;
  float w = (x2-x1)*16.f;
  float h = (y2-y1)*16.f;
  float l = 1.f;//TODO fix this....
  float ty = this.ty;
  float tx = this.tx;
  
  float tx1, ty1, tx2, ty2;
  
//render the cube. only called a single time when building the display list for a piece
  if(rx!=0){GL11.glRotatef(rx, 1, 0, 0);}
  if(ry!=0){GL11.glRotatef(ry, 0, 1, 0);}
  if(rz!=0){GL11.glRotatef(rz, 0, 0, 1);}  
  
 
  GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

  AWLog.logDebug("tx, ty: "+tx+","+ty);
  AWLog.logDebug("w,l,h: "+w+","+l+","+h);
//  AWLog.logDebug(String.format("t: %.4f, %.4f, %.4f, %.4f", tx1, ty1, tx2, ty2));
  
  //front side  
  tx1 = (tx + l)*px;  
  ty1 = (th - (ty + l + h))*py;
  tx2 = (tx + l + w)*px;
  ty2 = (th - (ty + l))*py;  
  
  GL11.glNormal3f(0, 0, 1);
  GL11.glTexCoord2f(tx2, ty2);
  GL11.glVertex3f(x3, y3, 0.f);
  GL11.glTexCoord2f(tx2, ty1);
  GL11.glVertex3f(x2, y2, 0.f);
  GL11.glTexCoord2f(tx1, ty1);
  GL11.glVertex3f(x1, y1, 0.f);
  
  GL11.glEnd();
  }

@Override
public Primitive copy()
  {
  PrimitiveTriangle box = new PrimitiveTriangle(parent);
  box.setBounds(x1, y1, x2, y2, x3, y3);
  box.setOrigin(x, y, z);
  box.setRotation(rx, ry, rz);
  box.tx = tx;
  box.ty = ty;
  return box;
  }

@Override
public void addPrimitiveLines(ArrayList<String> lines)
  {

  }

@Override
public void readFromLine(String[] lineBits)
  {
  // TODO Auto-generated method stub
  
  }

public void setBounds(float x1, float y1, float x2, float y2, float x3, float y3)
  {
  this.x1 = x1;
  this.x2 = x2;
  this.x3 = x3;
  this.y1 = y1;
  this.y2 = y2;
  this.y3 = y3;
  this.isCompiled = false;
  }


}
