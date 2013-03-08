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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.common.config.Config;

public abstract class GuiElement extends Gui implements IGuiElement
{

protected final IGuiElementCallback parent;

final int elementNum;
int renderPosX;
int renderPosY;
int width;
int height;

/**
 * the most recently pressed button on this element, -1 for none
 */
int mouseButton = -1;

/**
 * the X,Y of where the mouse button was pressed or moved to
 */
int mouseDownX;
int mouseDownY;

/**
 * the X,Y of where the mouse button was pressed or moved from (used with mouseDown to calc move delta)
 */
int mouseLastX;
int mouseLastY;

int guiLeft;
int guiTop;

protected boolean isMouseOver = false;
public boolean enabled = true;
public boolean hidden = false;
public boolean renderWithGuiOffset = true;

protected Minecraft mc;
protected FontRenderer fr;

public GuiElement(int elementNum, IGuiElementCallback parent, int w, int h)
  {
  this.elementNum = elementNum;
  this.parent = parent;
  this.width = w;
  this.height = h;
  this.mc = Minecraft.getMinecraft();
  this.fr = mc.fontRenderer;
  }

@Override
public GuiElement updateRenderPos(int newX, int newY)
  {
  this.renderPosX = newX;
  this.renderPosY = newY;
  return this;
  }

@Override
public void updateGuiPos(int x, int y)
  {  
  this.guiLeft = x;
  this.guiTop = y; 
  }

@Override
public int getElementNumber()
  {
  return this.elementNum;
  }

@Override
public void onMousePressed(int x, int y, int num)
  {
  if(this.isMouseOver(x, y) && this.mouseButton == -1)
    {
    this.mouseButton = num;
    this.mouseDownX = x;
    this.mouseDownY = y;
    this.mouseLastX = x;
    this.mouseLastY = y;
    if(this.handleMousePressed(x, y, num) && this.parent!=null)
      {
      this.parent.onElementActivated(this);
      }
    }
  }

@Override
public void onMouseReleased(int x, int y, int num)
  {  
  if(this.mouseButton >=0 && num==this.mouseButton)
    {
    this.mouseLastX = x;
    this.mouseLastY = y;
    this.mouseButton = -1;
    if(this.handleMouseReleased(x, y, num) && this.parent!=null)
      {
      this.parent.onElementReleased(this);
      }
    }
  }

@Override
public void onMouseMoved(int x, int y, int num)
  {
  this.isMouseOver = false;
  if(this.isMouseOver(x, y))
    {
    this.isMouseOver = true;
    }
  if(this.mouseButton>=0)
    {
    this.mouseLastX = this.mouseDownX;
    this.mouseLastY = this.mouseDownY;
    this.mouseDownX = x;
    this.mouseDownY = y;
    if(this.handleMouseMoved(x, y, num) && this.parent!=null)
      {
      this.parent.onElementDragged(this);
      }
    }
  }

@Override
public void onMouseWheel(int x, int y, int wheel)
  {
  if(this.isMouseOver(x,y))
    {
    if(this.handleMouseWheel(x, y, wheel))
      {
      this.parent.onElementMouseWheel(this, wheel);
      }
    }
  }

public void onKeyTyped(char ch, int keyNum)
  {
  if(this.handleKeyInput(ch, keyNum) && this.parent!=null)
    {
    this.parent.onElementKeyTyped(ch, keyNum);
    }
  }

/**
 * renders the four corners of a texture, from the corner inward (e.g. for size-adaptable elements)
 * @param x renderPosX
 * @param y renderPosY
 * @param w renderWidth
 * @param h renderHeight
 * @param tw textureUsedWidth
 * @param th textureUsedHeight
 * @param tex theTexture
 * @param u textureStartX
 * @param v textureStartY
 */
protected void drawQuadedTexture(int x, int y, int w, int h, int tw, int th, String tex, int u, int v)
  {  
  int halfW = w/2;
  int halfH = h/2;  
  int u1 = u + tw - halfW;
  int v1 = v + th - halfH;
  GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(tex));
  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  this.drawTexturedModalRect(x, y, u, v, halfW, halfH);
  this.drawTexturedModalRect(x + halfW, y, u1, v, halfW, halfH);
  this.drawTexturedModalRect(x, y + halfH, u, v1, halfW, halfH);
  this.drawTexturedModalRect(x + halfW, y + halfH, u1, v1, halfW, halfH);
  }

@Override
public boolean isMouseOver(int x, int y)
  {
  if(!this.renderWithGuiOffset)
    {
    return x >=this.renderPosX && x<this.renderPosX+width && y>=this.renderPosY && y<this.renderPosY+height;
    }
//  int xMin = this.renderPosX + guiLeft;
//  int xMax = this.renderPosX + guiLeft + width;
//  int yMin = this.renderPosY + guiTop;
//  int yMax = this.renderPosY + guiTop + height;
  //Config.logDebug(this.getElementNumber() +"---"+ xMin+","+xMax+","+yMin+","+yMax);
  return x >=this.renderPosX+guiLeft && x<this.renderPosX+width+guiLeft && y>=this.renderPosY+guiTop && y<this.renderPosY+height+guiTop;
  }

public void clearMouseButton()
  {
  this.mouseButton =-1;  
  }

public void setMouseButton(int num)
  {
  this.mouseButton = num;
  }

}
