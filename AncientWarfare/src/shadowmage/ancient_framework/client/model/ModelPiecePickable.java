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
import java.util.List;

import org.lwjgl.opengl.GL11;

public class ModelPiecePickable
{

ModelBaseAWPickable parent;

public ModelPiecePickable(ModelBaseAWPickable model)
  {
  this.parent = model;
  }

float ratio = 0.0625f;
String pieceName;
boolean visible = true;
float x, y, z;//manipulatable coordinates for this piece, relative to either model origin or parent-piece origin (if base piece or has parent)
float rx, ry, rz;//manipulatable rotation for this piece, relative to either model rotation or parent-piece rotation (if base piece or has parent)
int displayListNum = -1;//display list for the boxes that make up this piece
boolean isBasePiece;//if this is a base-piece or not, set during parsing by reading if this piece has a parent
List<ModelPiecePickable> children = new ArrayList<ModelPiecePickable>();//the children of this piece
List<Primitive> primitives = new ArrayList<Primitive>();//the list of boxes that make up this piece, really only used during first construction of display list

public void render()
  {
  if(!visible)
    {
    return;
    }
  GL11.glPushMatrix();
  if(x!=0 || y!=0 || z!=0)
    {
    GL11.glTranslatef(ratio*x, ratio*y, ratio*z);
    }  
  if(rx!=0){GL11.glRotatef(rx, 1, 0, 0);}
  if(ry!=0){GL11.glRotatef(ry, 0, 1, 0);}
  if(rz!=0){GL11.glRotatef(rz, 0, 0, 1);}  
  if(displayListNum>=0)
    {
    GL11.glPushMatrix();
    GL11.glCallList(displayListNum);
    GL11.glPopMatrix();
    }
  else
    {    
    displayListNum = GL11.glGenLists(1);
    GL11.glNewList(displayListNum, GL11.GL_COMPILE);
    for(Primitive primitive : this.primitives)
      {
      primitive.render();
      }
    GL11.glEndList();
    GL11.glPushMatrix();
    GL11.glCallList(displayListNum);
    GL11.glPopMatrix();
    }
  
  for(ModelPiecePickable child : this.children)
    {
    child.render();
    }
  GL11.glPopMatrix();
  }


public int renderForSelection(int startIndex)
  {
  GL11.glPushMatrix();
  if(x!=0 || y!=0 || z!=0)
    {
    GL11.glTranslatef(ratio*x, ratio*y, ratio*z);
    }  
  if(rx!=0){GL11.glRotatef(rx, 1, 0, 0);}
  if(ry!=0){GL11.glRotatef(ry, 0, 1, 0);}
  if(rz!=0){GL11.glRotatef(rz, 0, 0, 1);}  
  if(displayListNum>=0)
    {
    GL11.glPushMatrix();
    GL11.glCallList(displayListNum);
    GL11.glPopMatrix();
    }
  else
    {    
    displayListNum = GL11.glGenLists(1);
    GL11.glNewList(displayListNum, GL11.GL_COMPILE);
    for(Primitive primitive : this.primitives)
      {
      primitive.render();
      startIndex++;
      }
    GL11.glEndList();
    GL11.glPushMatrix();
    GL11.glCallList(displayListNum);
    GL11.glPopMatrix();
    }
  for(ModelPiecePickable child : this.children)
    {
    startIndex = child.renderForSelection(startIndex);
    }
  GL11.glPopMatrix();
  return startIndex;
  }

public int getSelectedPrimitive(int startNum, int selection)
  {
  for(Primitive primitive : this.primitives)
    {
    if(startNum==selection)
      {
      parent.selectedPrimitive = primitive;
      break;
      }
    startNum++;
    }
  if(parent.selectedPrimitive==null)
    {
    for(ModelPiecePickable child : this.children)
      {
      startNum = child.getSelectedPrimitive(startNum, selection);
      if(parent.selectedPrimitive==null){return startNum;}
      }    
    }
  return startNum;
  } 

}
