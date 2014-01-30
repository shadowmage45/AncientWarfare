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

public abstract class Primitive
{

float x, y, z;//origin of this primitive, relative to parent origin and orientation

float rx, ry, rz;//rotation of this primitive, relative to parent orientation

float x1, y1, z1, x2, y2, z2;//extends of bounding box in local space (post rotation) -- used for w/l/h for boxes

int primitiveNumber = 0;

public ModelPiece parent;

public Primitive(ModelPiece parent)
  {
  this.parent = parent;
  }

public abstract void render();

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
public float x2(){return x2;}
public float y2(){return y2;}
public float z2(){return z2;}

public void setOrigin(float x, float y, float z)
  {
  this.x = x;
  this.y = y;
  this.z = z;
  }

public void setRotation(float rx, float ry, float rz)
  {
  this.rx = rx;
  this.ry = ry;
  this.rz = rz;
  }

public void setBounds(float x1, float y1, float z1, float x2, float y2, float z2)
  {
  this.x1 = x1;
  this.x2 = x2;
  this.y1 = y1;
  this.y2 = y2;
  this.z1 = z1;
  this.z2 = z2;
  }
}
