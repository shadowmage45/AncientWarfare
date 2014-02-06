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

import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.StringTools;

public class PrimitiveTriangle extends Primitive
{

float x1, y1, z1, x2, y2, z2, x3, y3, z3;
float normalX, normalY, normalZ;//normal for lighting...should be calc'd when setBounds is called
/**
 * @param parent
 */
public PrimitiveTriangle(ModelPiece parent)
  {
  super(parent);
  }

public float x1(){return x1;}
public float y1(){return y1;}
public float z1(){return z1;}
public float x2(){return x2;}
public float y2(){return y2;}
public float z2(){return z2;}
public float x3(){return x3;}
public float y3(){return y3;}
public float z3(){return z3;}

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
  
  GL11.glNormal3f(normalX, normalY, normalZ);
  GL11.glTexCoord2f(tx2, ty2);
  GL11.glTexCoord2f(tx2, ty1);
  GL11.glTexCoord2f(tx1, ty1);
  GL11.glVertex3f(x1, y1, z1);
  GL11.glVertex3f(x2, y2, z2);
  GL11.glVertex3f(x3, y3, z3);
  
  GL11.glEnd();
  }

public void reverseVertexOrder()
  {
  float x = x1;
  float y = y1;
  float z = z1;
  x1 = x3;
  y1 = y3;
  z1 = z3;
  x3 = x;
  y3 = y;
  z3 = z;
  this.setBounds(x1, y1, z1, x2, y2, z1, x3, y3, z3);
  }

@Override
public Primitive copy()
  {
  PrimitiveTriangle box = new PrimitiveTriangle(parent);
  box.setBounds(x1, y1, z1, x2, y2, z2, x3, y3, z3);
  box.setOrigin(x, y, z);
  box.setRotation(rx, ry, rz);
  box.tx = tx;
  box.ty = ty;
  return box;
  }

@Override
public void addPrimitiveLines(ArrayList<String> lines)
  {
  StringBuilder b = new StringBuilder("triangle=").append(parent.getName()).append(",");
  b.append(x).append(",").append(y).append(",").append(z).append(",").append(rx).append(",").append(ry).append(",").append(rz).append(",").append(tx).append(",").append(ty).append(",");
  b.append(x1).append(",").append(y1).append(",").append(z1).append(",").append(x2).append(",").append(y2).append(",").append(z2).append(",").append(x3).append(",").append(y3).append(",").append(z3);
  lines.add(b.toString());
  }

@Override
public void readFromLine(String[] lineBits)
  {
  String parent = lineBits[0];
  x = StringTools.safeParseFloat(lineBits[1]);
  y = StringTools.safeParseFloat(lineBits[2]);
  z = StringTools.safeParseFloat(lineBits[3]);
  rx = StringTools.safeParseFloat(lineBits[4]);
  ry = StringTools.safeParseFloat(lineBits[5]);
  rz = StringTools.safeParseFloat(lineBits[6]);
  tx = StringTools.safeParseFloat(lineBits[7]);
  ty = StringTools.safeParseFloat(lineBits[8]);
  x1 = StringTools.safeParseFloat(lineBits[9]);
  y1 = StringTools.safeParseFloat(lineBits[10]);
  z1 = StringTools.safeParseFloat(lineBits[11]);
  x2 = StringTools.safeParseFloat(lineBits[12]);
  y2 = StringTools.safeParseFloat(lineBits[13]);
  z2 = StringTools.safeParseFloat(lineBits[14]);
  x3 = StringTools.safeParseFloat(lineBits[15]);
  y3 = StringTools.safeParseFloat(lineBits[16]);
  z3 = StringTools.safeParseFloat(lineBits[17]);
  setBounds(x1, y1, z1, x2, y2, z2, x3, y3, z3);
  }

public void setBounds(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3)
  {
  this.x1 = x1;
  this.x2 = x2;
  this.x3 = x3;
  this.y1 = y1;
  this.y2 = y2;
  this.y3 = y3;
  this.z1 = z1;
  this.z2 = z2;
  this.z3 = z3;
  
  float vx, vy, vz, wx, wy, wz;
  vx = x2-x1;
  vy = y2-y1;
  vz = z2-z1;
  wx = x3-x1;
  wy = y3-y1;
  wz = z3-z1;
  
  normalX = (vy*wz)-(vz*wy);
  normalY = (vz*wx)-(vx*wz);
  normalZ = (vx*wy)-(vy*wx);
  
  float norm = MathHelper.sqrt_float(normalX * normalX + normalY * normalY + normalZ * normalZ);
  normalX/=norm;
  normalY/=norm;
  normalZ/=norm;
  this.setCompiled(false);
  }


}
