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

public class PrimitiveTriangle extends Primitive
{

float tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3;
/**
 * @param parent
 */
public PrimitiveTriangle(ModelPiece parent)
  {
  super(parent);
  }

@Override
protected void renderForDisplayList()
  {
 
  }

@Override
public Primitive copy()
  {
  return null;
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


}
