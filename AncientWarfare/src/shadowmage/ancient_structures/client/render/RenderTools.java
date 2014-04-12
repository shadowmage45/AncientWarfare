/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_structures.client.render;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

public class RenderTools
{

static float zLevel = 0;


/**
 * draw a player-position-normalized bounding box (can only be called during worldRender)
 * @param bb
 */
public static void drawOutlinedBoundingBox(AxisAlignedBB bb, float r, float g, float b)
  {
  GL11.glEnable(GL11.GL_BLEND);
  GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
  GL11.glColor4f(r, g, b, 0.4F);
  GL11.glLineWidth(8.0F);
  GL11.glDisable(GL11.GL_TEXTURE_2D);
  GL11.glDepthMask(false);
  Tessellator tess = Tessellator.instance;
  tess.startDrawing(3);
  tess.addVertex(bb.minX, bb.minY, bb.minZ);
  tess.addVertex(bb.maxX, bb.minY, bb.minZ);
  tess.addVertex(bb.maxX, bb.minY, bb.maxZ);
  tess.addVertex(bb.minX, bb.minY, bb.maxZ);
  tess.addVertex(bb.minX, bb.minY, bb.minZ);
  tess.draw();
  tess.startDrawing(3);
  tess.addVertex(bb.minX, bb.maxY, bb.minZ);
  tess.addVertex(bb.maxX, bb.maxY, bb.minZ);
  tess.addVertex(bb.maxX, bb.maxY, bb.maxZ);
  tess.addVertex(bb.minX, bb.maxY, bb.maxZ);
  tess.addVertex(bb.minX, bb.maxY, bb.minZ);
  tess.draw();
  tess.startDrawing(1);
  tess.addVertex(bb.minX, bb.minY, bb.minZ);
  tess.addVertex(bb.minX, bb.maxY, bb.minZ);
  tess.addVertex(bb.maxX, bb.minY, bb.minZ);
  tess.addVertex(bb.maxX, bb.maxY, bb.minZ);
  tess.addVertex(bb.maxX, bb.minY, bb.maxZ);
  tess.addVertex(bb.maxX, bb.maxY, bb.maxZ);
  tess.addVertex(bb.minX, bb.minY, bb.maxZ);
  tess.addVertex(bb.minX, bb.maxY, bb.maxZ);
  tess.draw();
  GL11.glDepthMask(true);
  GL11.glEnable(GL11.GL_TEXTURE_2D);
  GL11.glDisable(GL11.GL_BLEND);
  }

/**
 * @param bb
 * @param player
 * @param partialTick
 * @return
 */
public static AxisAlignedBB adjustBBForPlayerPos(AxisAlignedBB bb, EntityPlayer player, float partialTick)
  {
  double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTick;
  double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTick;
  double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTick;  
  return bb.offset(-x, -y, -z);
  }

public static void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
  {
  float f = 0.00390625F;
  float f1 = 0.00390625F;
  Tessellator tessellator = Tessellator.instance;
  tessellator.startDrawingQuads();
  tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
  tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
  tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
  tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
  tessellator.draw();
  }

public static void drawTexturedModelRectFromIcon(int par1, int par2, Icon par3Icon, int par4, int par5)
  {
  Tessellator tessellator = Tessellator.instance;
  tessellator.startDrawingQuads();
  tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par5), (double)zLevel, (double)par3Icon.getMinU(), (double)par3Icon.getMaxV());
  tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + par5), (double)zLevel, (double)par3Icon.getMaxU(), (double)par3Icon.getMaxV());
  tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + 0), (double)zLevel, (double)par3Icon.getMaxU(), (double)par3Icon.getMinV());
  tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)zLevel, (double)par3Icon.getMinU(), (double)par3Icon.getMinV());
  tessellator.draw();
  }

public static List<String> getFormattedLines(List<String> inLines, int maxWidth)
  {
  FontRenderer render = Minecraft.getMinecraft().fontRenderer;
  List<String> outLines = new ArrayList<String>();
  Iterator<String> inIt = inLines.iterator();
  
  String line;
  String newLine = "";
  int currentLineWidth = 0;
  int absLineWidth = 0;
  int charWidth = 0;
  while(inIt.hasNext())
    {
    line = inIt.next();
    absLineWidth = render.getStringWidth(line);
    currentLineWidth = 0;    
    if(absLineWidth<maxWidth)
      {
      outLines.add(line);
      continue;
      }
    newLine = "";
    for(int i = 0; i < line.length(); i++)
      {      
      char ch = line.charAt(i);
      charWidth = render.getCharWidth(ch);
      if(currentLineWidth + charWidth <=maxWidth)
        {
        newLine = newLine + ch;
        currentLineWidth += charWidth;
        }
      else
        {
        if(ch!=' ' && newLine.charAt(newLine.length()-1)!=' ')
          {
          newLine += "-";
          }
        outLines.add(newLine);
        currentLineWidth = charWidth;
        if(ch!=' ')
          {
          newLine = String.valueOf(ch);
          }
        else
          {
          newLine = "";
          }
        }
      }
    if(!newLine.equals(""))
      {
      outLines.add(newLine);
      }
    }
  return outLines;
  }

/**
 * @param i
 * @param j
 * @param k
 * @param l
 * @param m
 * @param n
 * @param o
 * @param p
 * @param xSize
 * @param ySize
 */
public static void renderQuarteredTexture(int textureWidth, int textureHeight, int texStartX, int texStartY, int texUsedWidth, int texUsedHeight, int renderStartX, int renderStartY, int renderWidth, int renderHeight)
  {
  //perspective percent x, y
  float perX = 1.f / ((float)textureWidth);
  float perY = 1.f / ((float)textureHeight);  
  float texMinX = ((float) texStartX) * perX;
  float texMinY = ((float) texStartY) * perY;
  float texMaxX = (float)(texStartX + texUsedWidth) * perX;
  float texMaxY = (float)(texStartY + texUsedHeight) * perY;
  float halfWidth = (((float) renderWidth) / 2.f) * perX;
  float halfHeight = (((float) renderHeight) / 2.f) * perY;    
  float halfRenderWidth = ((float)renderWidth) * 0.5f;
  float halfRenderHeight = ((float)renderHeight) * 0.5f;
    
  //draw top-left quadrant
  renderTexturedQuad(renderStartX, renderStartY, renderStartX+halfRenderWidth, renderStartY+halfRenderHeight, texMinX, texMinY, texMinX+halfWidth, texMinY+halfHeight);
  
  //draw top-right quadrant
  renderTexturedQuad(renderStartX+halfRenderWidth, renderStartY, renderStartX+halfRenderWidth*2, renderStartY+halfRenderHeight, texMaxX-halfWidth, texMinY, texMaxX, texMinY+halfHeight);
  
//  draw bottom-left quadrant
  renderTexturedQuad(renderStartX, renderStartY+halfRenderHeight, renderStartX+halfRenderWidth, renderStartY+halfRenderHeight*2, texMinX, texMaxY-halfHeight, texMinX+halfWidth, texMaxY);
 
  //draw bottom-right quadrant
  renderTexturedQuad(renderStartX+halfRenderWidth, renderStartY+halfRenderHeight, renderStartX+halfRenderWidth*2, renderStartY+halfRenderHeight*2, texMaxX-halfWidth, texMaxY-halfHeight, texMaxX, texMaxY);
  }

public static void renderTexturedQuad(float x1, float y1, float x2, float y2, float u1, float v1, float u2, float v2)
  {
  GL11.glBegin(GL11.GL_QUADS);
  GL11.glTexCoord2f(u1, v1);
  GL11.glVertex2f(x1, y1);
  GL11.glTexCoord2f(u1, v2);
  GL11.glVertex2f(x1, y2);
  GL11.glTexCoord2f(u2, v2);
  GL11.glVertex2f(x2, y2);
  GL11.glTexCoord2f(u2, v1);
  GL11.glVertex2f(x2, y1);
  GL11.glEnd();
  }
}
