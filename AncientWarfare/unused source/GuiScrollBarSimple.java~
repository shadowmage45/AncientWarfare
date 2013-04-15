/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public Licence.
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
package shadowmage.ancient_warfare.client.gui.elements;

import org.lwjgl.opengl.GL11;

public class GuiScrollBarSimple extends GuiElement
{

/**
 * height of the handle-button/barthingie, in pixels...
 */
int handleHeight;

/**
 * current position of the scroll bar
 */
int handleTop;


final int buffer = 5;

/**
 * @param elementNum
 * @param parent
 * @param x
 * @param y
 * @param w
 * @param h
 */
public GuiScrollBarSimple(int elementNum, IGuiElementCallback parent, int x, int y, int w, int h, int intialSetSize, int displaySize)
  {
  super(elementNum, parent, x, y, w, h);
  this.updateHandleHeight(intialSetSize, displaySize);
  this.handleTop = 0;
  }

@Override
public void drawElement(int mouseX, int mouseY)
  {
  String tex = "/shadowmage/ancient_warfare/resources/gui/guiButtons.png";  
  this.drawQuadedTexture(renderPosX+guiLeft, renderPosY+guiTop, width, height, 40, 128, tex, 80, 120); 
  this.drawQuadedTexture(renderPosX+guiLeft+buffer, renderPosY+guiTop+buffer+handleTop, width-buffer*2, handleHeight, 30, 128, tex, 120, 120);  
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
  return false;
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
private void updateHandleHeight(int setSize, int displayElements)
  {
  int availBarHeight = this.height - this.buffer*2 - 20;//20 is the minimum handle height...
  float elementPercent = (float)displayElements/(float)setSize;
  if(elementPercent>1)
    {
    elementPercent = 1;
    }
  float bar = (float)availBarHeight * (float)elementPercent;
  int barHeight = (int) (bar + 20);
  this.handleHeight = barHeight;
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
