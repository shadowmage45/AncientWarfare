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
package shadowmage.ancient_vehicles.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class ModelBaseAW
{
//name needs changed to something...else

private static float ratio = 0.0625f;//possibly not needed?

private HashMap<String, ModelPiece> pieces = new HashMap<String, ModelPiece>();
private List<ModelPiece> basePieces = new ArrayList<ModelPiece>();

public void renderModel()
  {
  for(ModelPiece piece : this.basePieces)
    {
    piece.render();
    }
  }

public void parseFromLines(List<String> lines){}

protected void addPiece(ModelPiece piece)
  {
  pieces.put(piece.pieceName, piece);
  if(piece.parent==null)
    {
    basePieces.add(piece);
    }
  }

private static class ModelPiece
{

String pieceName;
float x, y, z;
float rx, ry, rz;
int displayListNum = -1;
private ModelPiece parent;
private List<ModelPiece> children = new ArrayList<ModelPiece>();
private List<Box> boxes = new ArrayList<Box>();

public void render()
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
    for(Box box : this.boxes)
      {
      box.render();
      }
    GL11.glEndList();
    GL11.glPushMatrix();
    GL11.glCallList(displayListNum);
    GL11.glPopMatrix();
    }
  
  for(ModelPiece child : this.children)
    {
    child.render();
    }
  GL11.glPopMatrix();
  }
}


private static class Box
{

float x1, y1, z1, x2, y2, z2;//extents of the box
float rx, ry, rz;
float tx, ty, tz;//texture offsets, in texture space (0->1)

private void render()
  {
//render the cube. only called a single time when building the display list for a piece
  GL11.glPushMatrix();
  if(rx!=0){GL11.glRotatef(rx, 1, 0, 0);}
  if(ry!=0){GL11.glRotatef(ry, 0, 1, 0);}
  if(rz!=0){GL11.glRotatef(rz, 0, 0, 1);}  
  
  //add vertices for cube
  //with texture vertices
  GL11.glPopMatrix();  
  }

}

}
