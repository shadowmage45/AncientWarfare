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
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.StringTools;

public class ModelBaseAW
{
//name needs changed to something...else

static float ratio = 0.0625f;//possibly not needed?

int textureWidth;
int textureHeight;
Primitive selectedPrimitive = null;


HashMap<String, ModelPiece> pieces = new HashMap<String, ModelPiece>();
List<ModelPiece> basePieces = new ArrayList<ModelPiece>();

public void renderModel()
  {
  for(ModelPiece piece : this.basePieces)
    {
    piece.render();
    }
  }


public void parseFromLines(List<String> lines)
  {
  String[] bits;
  for(String line : lines)
    {
    if(line.toLowerCase().startsWith("#"))
      {
      continue;
      }
    else if(line.toLowerCase().startsWith("texturesize="))
      {
      bits = line.split("=")[1].split(",");
      textureWidth = StringTools.safeParseInt(bits[0]);
      textureHeight = StringTools.safeParseInt(bits[1]);
      }
    else if(line.startsWith("part="))
      {
      bits = line.split("=")[1].split(",");
      ModelPiece piece = new ModelPiece(this);
      String pieceName = bits[0];
      String parentName = bits[1];
      if(parentName.equals("null"))
        {
        piece.isBasePiece = true;        
        }
      else
        {
        ModelPiece parent = getPiece(parentName);
        if(parent==null)
          {
          throw new IllegalArgumentException("could not create model, imporoper piece specification for: "+parentName);
          }
        parent.children.add(piece);
        }
      piece.pieceName = pieceName;
      piece.x = StringTools.safeParseFloat(bits[2]);
      piece.y = StringTools.safeParseFloat(bits[3]);
      piece.z = StringTools.safeParseFloat(bits[4]);
      piece.rx = StringTools.safeParseFloat(bits[5]);
      piece.ry = StringTools.safeParseFloat(bits[6]);
      piece.rz = StringTools.safeParseFloat(bits[7]);
      addPiece(piece);
      AWLog.logDebug("parsed a new piece: "+pieceName);      
      }
    else if(line.startsWith("box="))
      {
      bits = line.split("=")[1].split(",");
      //parse old-style x,y,z, w,h,l
      String parentName = bits[0];
      ModelPiece piece = getPiece(parentName);
      if(piece==null)
        {
        throw new IllegalArgumentException("could not construct model, improper piece reference for: "+parentName);
        }
      Box box = new Box(piece);
      box.x1 = StringTools.safeParseFloat(bits[1]);
      box.y1 = StringTools.safeParseFloat(bits[2]);
      box.z1 = StringTools.safeParseFloat(bits[3]);
      
      box.x2 = box.x1 + StringTools.safeParseFloat(bits[4]);
      box.y2 = box.y1 + StringTools.safeParseFloat(bits[5]);
      box.z2 = box.z1 + StringTools.safeParseFloat(bits[6]);
      
      if(bits.length>=10)
        {
        box.rx = StringTools.safeParseFloat(bits[7]);
        box.ry = StringTools.safeParseFloat(bits[8]);
        box.rz = StringTools.safeParseFloat(bits[9]);      
        }
      piece.primitives.add(box);
      AWLog.logDebug("parsed new box for piece: "+parentName);
      }    
    }
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
 * A single box from a model.  Each box is a discrete static component.
 * Boxes do not change position/rotation relative to other boxes in the
 * same piece.
 * @author Shadowmage
 *
 */
private class Box extends Primitive
{

public Box(ModelPiece parent)
  {
  super(parent);
  }

float x1, y1, z1, x2, y2, z2;//extents of the box, relative to piece origin
float rx, ry, rz;//rotation of this box, relative to the piece rotation
float tx, ty;//texture offsets, in texture space (0->1)

public void render()
  {
  x1*=ratio;
  y1*=ratio;
  z1*=ratio;
  x2*=ratio;
  y2*=ratio;
  z2*=ratio;
  
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

}

}
