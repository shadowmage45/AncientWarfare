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

int textureWidth;
int textureHeight;

private HashMap<String, ModelPiece> pieces = new HashMap<String, ModelPiece>();
private List<ModelPiece> basePieces = new ArrayList<ModelPiece>();

public void renderModel()
  {
  for(ModelPiece piece : this.basePieces)
    {
    piece.render();
    }
  }

public void parseFromLines(List<String> lines)
  {
  
  }

protected void addPiece(ModelPiece piece)
  {
  pieces.put(piece.pieceName, piece);
  if(piece.isBasePiece)
    {
    basePieces.add(piece);
    }
  }

public void setPieceRotation(String name, float x, float y, float z)
  {
  ModelPiece piece = this.getPiece(name);
  if(piece==null){return;}
  piece.rx = x;
  piece.ry = y;
  piece.rz = z;
  }

protected ModelPiece getPiece(String name)
  {
  return this.pieces.get(name);
  }

/**
 * A single piece of a model.  A piece is a discrete static component of the model.  Pieces may be rotated and moved
 * relative to other pieces in the model (in contrast to boxes, which may not be altered).  All animation is done
 * by moving pieces relative to each-other and the model origin.
 * 
 * Each piece has a box list for the boxes of that piece, as well as a children list for sub-pieces
 *  (pieces which rotate/move relative to this piece, but may also need to rotate independently)
 * Each piece may have multiple boxes and multiple sub-pieces.
 * @author Shadowmage
 *
 */
private static class ModelPiece
{

String pieceName;
boolean visible = true;
float x, y, z;//manipulatable coordinates for this piece, relative to either model origin or parent-piece origin (if base piece or has parent)
float rx, ry, rz;//manipulatable rotation for this piece, relative to either model rotation or parent-piece rotation (if base piece or has parent)
int displayListNum = -1;//display list for the boxes that make up this piece
private boolean isBasePiece;//if this is a base-piece or not, set during parsing by reading if this piece has a parent
private List<ModelPiece> children = new ArrayList<ModelPiece>();//the children of this piece
private List<Box> boxes = new ArrayList<Box>();//the list of boxes that make up this piece, really only used during first construction of display list

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

/**
 * A single box from a model.  Each box is a discrete static component.
 * Boxes do not change position/rotation relative to other boxes in the
 * same piece.
 * @author Shadowmage
 *
 */
private static class Box
{

float x1, y1, z1, x2, y2, z2;//extents of the box, relative to piece origin
float rx, ry, rz;//rotation of this box, relative to the piece rotation
float tx, ty;//texture offsets, in texture space (0->1)

private void render()
  {
//render the cube. only called a single time when building the display list for a piece
  GL11.glPushMatrix();
  if(rx!=0){GL11.glRotatef(rx, 1, 0, 0);}
  if(ry!=0){GL11.glRotatef(ry, 0, 1, 0);}
  if(rz!=0){GL11.glRotatef(rz, 0, 0, 1);}  
  
  //front side
  GL11.glBegin(GL11.GL_QUADS);
  GL11.glTexCoord2f(tx, ty);//offset for the coords for the 'front' face
  GL11.glVertex3f(x1, y1, z1);
  GL11.glVertex3f(x2, y1, z1);
  GL11.glVertex3f(x2, y2, z1);
  GL11.glVertex3f(x1, y2, z1);
  
  //right side
  GL11.glTexCoord2f(tx, ty);//offset for the coords for the 'right' face
  GL11.glVertex3f(x2, y1, z1);
  GL11.glVertex3f(x2, y1, z2);
  GL11.glVertex3f(x2, y2, z2);
  GL11.glVertex3f(x2, y2, z1);
  
  //left side
  GL11.glTexCoord2f(tx, ty);//offset for the coords for the 'left' face
  GL11.glVertex3f(x2, y1, z2);
  GL11.glVertex3f(x2, y1, z1);
  GL11.glVertex3f(x2, y2, z1);
  GL11.glVertex3f(x2, y2, z2);

  //top side
  GL11.glTexCoord2f(tx, ty);//offset for the coords for the 'top' face
  GL11.glVertex3f(x1, y1, z2);
  GL11.glVertex3f(x2, y1, z2);
  GL11.glVertex3f(x2, y1, z1);
  GL11.glVertex3f(x1, y1, z1);
  
  //bottom side
  GL11.glTexCoord2f(tx, ty);//offset for the coords for the 'bottom' face
  GL11.glVertex3f(x1, y2, z1);
  GL11.glVertex3f(x2, y2, z1);
  GL11.glVertex3f(x2, y2, z2);
  GL11.glVertex3f(x1, y2, z2);
  
  //rear side
  GL11.glTexCoord2f(tx, ty);//offset for the coords for the 'rear' face
  GL11.glVertex3f(x2, y1, z2);
  GL11.glVertex3f(x1, y1, z2);
  GL11.glVertex3f(x1, y2, z2);
  GL11.glVertex3f(x2, y2, z2);
  
  GL11.glEnd();
  GL11.glPopMatrix();  
  }

}

}
