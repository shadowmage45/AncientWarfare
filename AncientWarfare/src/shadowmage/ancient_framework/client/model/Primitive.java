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

public abstract class Primitive
{

public float tx, ty;//texture offsets, in texture space (0->1)
float x, y, z;//origin of this primitive, relative to parent origin and orientation

float rx, ry, rz;//rotation of this primitive, relative to parent orientation

float x1, y1, z1, x2, y2, z2;//extends of bounding box in local space (post rotation) -- used for w/l/h for boxes

int primitiveNumber = 0;

protected boolean isCompiled = false;
int displayListNumber = -1;

public ModelPiece parent;

public Primitive(ModelPiece parent)
  {
  this.parent = parent;
  }

public void render()
  {
  if(!isCompiled)
    {   
    buildDisplayList();
    }
  GL11.glCallList(displayListNumber);
  }

public void buildDisplayList()
  {
  if(this.displayListNumber<0)
    {
    this.displayListNumber = GL11.glGenLists(1);
    }  
  GL11.glNewList(displayListNumber, GL11.GL_COMPILE);
  if(x!=0 || y!=0 || z!=0){GL11.glTranslatef(x, y, z);}
  if(rx!=0){GL11.glRotatef(rx, 1, 0, 0);}
  if(ry!=0){GL11.glRotatef(ry, 0, 1, 0);}
  if(rz!=0){GL11.glRotatef(rz, 0, 0, 1);}  
  renderForDisplayList();
  if(x!=0 || y!=0 || z!=0){GL11.glTranslatef(-x, -y, -z);}
  if(rx!=0){GL11.glRotatef(-rx, 1, 0, 0);}
  if(ry!=0){GL11.glRotatef(-ry, 0, 1, 0);}
  if(rz!=0){GL11.glRotatef(-rz, 0, 0, 1);}  
  GL11.glEndList();
  isCompiled = true;
  }

protected abstract void renderForDisplayList();

public abstract Primitive copy();

public float x(){return x;}
public float y(){return y;}
public float z(){return z;}
public float rx(){return rx;}
public float ry(){return ry;}
public float rz(){return rz;}
public float x1(){return x1;}
public float y1(){return y1;}
public float z1(){return z1;}
public float width(){return x2-x1;}
public float height(){return y2-y1;}
public float length(){return z2-z1;}

public void setOrigin(float x, float y, float z)
  {
  this.x = x;
  this.y = y;
  this.z = z;
  this.isCompiled = false;
  }

public void setRotation(float rx, float ry, float rz)
  {
  this.rx = rx;
  this.ry = ry;
  this.rz = rz;
  this.isCompiled = false;
  }

public void setBounds(float x1, float y1, float z1, float width, float height, float length)
  {
  this.x1 = x1;
  this.x2 = x1 + width;
  this.y1 = y1;
  this.y2 = y1 + height;
  this.z1 = z1;
  this.z2 = z1 + length;
  this.isCompiled = false;
  }

@Override
public String toString()
  {
  return String.format("Primitive:: origin: %.1f, %.1f, %.1f rotation: %.1f, %.1f, %.1f min: %.1f, %.1f, %.1f max: %.1f, %.1f, %.1f", x,y,z, rx,ry,rz, x1,y1,z1, x2,y2,z2);
  }

public abstract void addPrimitiveLines(ArrayList<String> lines);

public abstract void readFromLine(String[] lineBits);


}
