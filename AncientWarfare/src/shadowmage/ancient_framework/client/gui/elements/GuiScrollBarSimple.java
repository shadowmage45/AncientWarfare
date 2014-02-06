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
package shadowmage.ancient_framework.client.gui.elements;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_framework.client.render.AWTextureManager;
import shadowmage.ancient_framework.common.config.Statics;



public class GuiScrollBarSimple extends GuiElement
{

/**
 * height of the handle-button/barthingie, in MCpixels...
 */
int handleHeight;

/**
 * current position of the scroll bar
 */
public int handleTop;


final int buffer = 4;

/**
 * @param elementNum
 * @param parent
 * @param o
 * @param oY
 * @param w
 * @param h
 */
public GuiScrollBarSimple(int elementNum, IGuiElementCallback parent, int w, int h, int intialSetSize, int displaySize)
  {
  super(elementNum, parent, w, h);
  this.updateHandleHeight(intialSetSize, displaySize);
  this.handleTop = 0;
  }

@Override
public void drawElement(int mouseX, int mouseY)
  {
  if(!this.hidden)
    {    
    String tex = Statics.TEXTURE_PATH+"gui/guiButtons.png";
    if(height<=128)//do simple render
      { 
      this.drawQuadedTexture(renderPosX+guiLeft, renderPosY+guiTop, width, height, 40, 128, tex, 80, 120);
      this.drawQuadedTexture(renderPosX+guiLeft+buffer, renderPosY+guiTop+buffer+handleTop, width-buffer*2, handleHeight, 32, 128, tex, 120, 120);
      return;
      }
    
    
    AWTextureManager.bindTexture(tex);
    float texPixPercent = 1.f / 256.f;
    float x, y, u, v;
    x = renderPosX+guiLeft;
    y = renderPosY+guiTop;
    u = 80;
    v = 120;
    float x1, y1, x2, y2, x3, y3, x4, y4;
    float u1, v1, u2, v2, u3, v3, u4, v4;
    /**
     * setup render bounds
     */
    

    /**
     * render the top-left bit
     */
    x1 = x;//top-left
    y1 = y;//top-left
    x2 = x1;//bottom-left
    y2 = y1 + 8;//bottom-left
    x3 = x2 + (width/2);//bottom-right
    y3 = y2;//bottom-right
    x4 = x3;//top-right
    y4 = y3-8;//top-right    
    u1 = 80 *texPixPercent;
    v1 = 120 * texPixPercent;    
    u2 = u1;
    v2 = v1 + 8 * texPixPercent;    
    u3 = u2 + (width/2) * texPixPercent;
    v3 = v2;    
    u4 = u3;
    v4 = v3 - 8 * texPixPercent;     
    renderQuad(x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, u3, v3, u4, v4);
    
    /**
     * render the top-right bit
     */
    x1 = x+8;//top-left
    y1 = y;//top-left
    x2 = x1;//bottom-left
    y2 = y1 + 8;//bottom-left
    x3 = x2 + (width/2);//bottom-right
    y3 = y2;//bottom-right
    x4 = x3;//top-right
    y4 = y3-8;//top-right
    u1 = 80 * texPixPercent + (40-(width/2))*texPixPercent;
    v1 = 120 * texPixPercent;    
    u2 = u1;
    v2 = v1 + 8 * texPixPercent;    
    u3 = u2 + (width/2) * texPixPercent;
    v3 = v2;    
    u4 = u3;
    v4 = v3 - 8 * texPixPercent;
    renderQuad(x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, u3, v3, u4, v4);
    

    /**
     * render bottom-left bit
     */
    x1 = x;
    y1 = y + height - 8;
    x2 = x1;
    y2 = y1 + 8;
    x3 = x2 + (width/2);
    y3 = y2;
    x4 = x3;
    y4 = y3-8;    
    u1 = 80 *texPixPercent;
    v1 = 120 * texPixPercent + 120 * texPixPercent;    
    u2 = u1;
    v2 = v1 + 8 * texPixPercent;    
    u3 = u2 + (width/2) * texPixPercent;
    v3 = v2;    
    u4 = u3;
    v4 = v3 - 8 * texPixPercent;
    renderQuad(x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, u3, v3, u4, v4);
    
    
    x1 = x+8;//top-left
    y1 = y + height - 8;//top-left
    x2 = x1;//bottom-left
    y2 = y1 + 8;//bottom-left
    x3 = x2 + (width/2);//bottom-right
    y3 = y2;//bottom-right
    x4 = x3;//top-right
    y4 = y3-8;//top-right
    u1 = 80 * texPixPercent + (40-(width/2))*texPixPercent;
    v1 = 120 * texPixPercent + 120 * texPixPercent;    
    u2 = u1;
    v2 = v1 + 8 * texPixPercent;    
    u3 = u2 + (width/2) * texPixPercent;
    v3 = v2;    
    u4 = u3;
    v4 = v3 - 8 * texPixPercent;
    renderQuad(x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, u3, v3, u4, v4);
    
    float h = height - 16;
    float ny = y+8;
    float uh;
    while(h>0)
      {
      uh = h > 40 ? 40 : h;
      h-=uh;
      
      /**
       * render left bit
       */
      x1 = x;//top-left
      y1 = ny;//top-left
      x2 = x1;//bottom-left
      y2 = y1 + uh;//bottom-left
      x3 = x2 + (width/2);//bottom-right
      y3 = y2;//bottom-right
      x4 = x3;//top-right
      y4 = y3 - uh;//top-right
      
      u1 = 80 * texPixPercent;
      v1 = 120 * texPixPercent + (8*texPixPercent);    
      u2 = u1;
      v2 = v1 + uh * texPixPercent;    
      u3 = u2 + (width/2) * texPixPercent;
      v3 = v2;    
      u4 = u3;
      v4 = v3 - uh * texPixPercent;
      renderQuad(x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, u3, v3, u4, v4);
      
      /**
       * render right bit
       */
      x1 = x + (width/2);//top-left
      y1 = ny;//top-left
      x2 = x1;//bottom-left
      y2 = y1 + uh;//bottom-left
      x3 = x2 + (width/2);//bottom-right
      y3 = y2;//bottom-right
      x4 = x3;//top-right
      y4 = y3 - uh;//top-right
      
      u1 = 80 * texPixPercent + (40-(width/2))*texPixPercent;
      v1 = 120 * texPixPercent + (8*texPixPercent);    
      u2 = u1;
      v2 = v1 + uh * texPixPercent;    
      u3 = u2 + (width/2) * texPixPercent;
      v3 = v2;    
      u4 = u3;
      v4 = v3 - uh * texPixPercent;
      renderQuad(x1, y1, x2, y2, x3, y3, x4, y4, u1, v1, u2, v2, u3, v3, u4, v4);
      
      
      ny +=uh;
      }
    }
  }

protected void renderQuad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float u1, float v1, float u2, float v2, float u3, float v3, float u4, float v4)
  {
  GL11.glBegin(GL11.GL_QUADS);
  GL11.glTexCoord2f(u1, v1);
  GL11.glVertex3f(x1, y1, 0);
  GL11.glTexCoord2f(u2, v2);
  GL11.glVertex3f(x2, y2, 0);
  GL11.glTexCoord2f(u3, v3);
  GL11.glVertex3f(x3, y3, 0);
  GL11.glTexCoord2f(u4, v4);
  GL11.glVertex3f(x4, y4, 0);    
  GL11.glEnd();
  }

@Override
public boolean handleMousePressed(int x, int y, int num)
  {
  return false;
  }

@Override
public boolean handleMouseReleased(int x, int y, int num)
  {
  int delta = this.mouseDownY-this.mouseLastY;
  this.updateHandleDisplayPos(delta);
  return true;
  }

@Override
public boolean handleMouseMoved(int x, int y, int num)
  {
  int delta = this.mouseDownY-this.mouseLastY;
  this.updateHandleDisplayPos(delta);
  return true;
  }

@Override
public boolean handleMouseWheel(int x, int y, int wheel)
  {
  this.updateHandleDisplayPos(-wheel*3);
  return true;
  }

@Override
public boolean handleKeyInput(char ch, int keyNum)
  {
  return false;
  }

@Override
public boolean isMouseOver(int x, int y)
  {
  return x>= renderPosX+guiLeft+buffer && x< renderPosX+guiLeft+width+buffer && y>=renderPosY+guiTop+handleTop+buffer && y < renderPosY+guiTop+handleTop+handleHeight+buffer;
  }

/**
 * update the top (and bottom) displayPos of the the handle...
 */
private void updateHandleDisplayPos(int yDelta)
  {  
  this.handleTop += yDelta;
  if(this.handleTop<0)
    {
    this.handleTop = 0;
    }
  int lowestTopPosition = this.height-this.buffer*2-this.handleHeight;
  if(this.handleTop>lowestTopPosition)
    {
    this.handleTop = lowestTopPosition;
    }
  }

/**
 *  update the size of the handle, relative to the size of the underlying elementSet
 *  should be called BEFORE updateHandlePos, and before getTopIndex...
 */
public void updateHandleHeight(int setSize, int displayElements)
  {
  int availBarHeight = this.height - this.buffer*2 - 20;//20 is the minimum handle height...
  float elementPercent = (float )displayElements / (float) setSize;
  if(elementPercent>1)
    {
    elementPercent = 1;
    }
  float bar = (float)availBarHeight * (float)elementPercent;
  int barHeight = (int) (bar + 20);
  this.handleHeight = barHeight;
  this.updateHandleDisplayPos(0);
  }

/**
 * 
 * @param setSize total number of elements in the underlying set
 * @param displayElements how many elements are displayed on the screen at any one time
 * @param elementHeight the height in pixels of a single element
 * @return
 */
public int getTopIndexForSet(int setSize, int displayElements)
  {
  int validBarPixels = this.height - this.buffer*2 - this.handleHeight;
  int extraSetElements = setSize - displayElements;
  if(extraSetElements<=0)//the set is smaller than the display area
    {
    return 0;//element 0 is the first element viewed.
    }
  float pxPerElement = (float)validBarPixels / (float)extraSetElements;
  int element = (int) ((float)handleTop / (float)pxPerElement);
  return element;
  }


}
