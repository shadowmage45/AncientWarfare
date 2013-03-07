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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.common.config.Config;

public class GuiScrollableArea extends GuiElement implements IGuiElementCallback
{

int scrollPosX;//topLeft of the screen currently being drawn (the sub
int scrollPosY;//topLeft of the screen currently being drawn

int parentGuiWidth;
int parentGuiHeight;


private int totalHeight;

GuiScrollBarSimple scrollBar;

List<GuiElement> elements = new ArrayList<GuiElement>();

private GuiContainerAdvanced parentGui;



/**
 * @param elementNum
 * @param parent
 * @param w
 * @param h
 */
public GuiScrollableArea(int elementNum, GuiContainerAdvanced parent, int x, int y, int w,  int h, int totalHeight)
  {
  super(elementNum, parent, w, h);
  this.parentGui = parent;
  this.parentGuiWidth = parent.getXSize();
  this.parentGuiHeight = parent.getYSize();
  this.renderPosX = x;
  this.renderPosY = y;
  this.totalHeight = totalHeight;
  this.scrollBar = new GuiScrollBarSimple(elementNum, this, 16, h, totalHeight, h);
  this.scrollBar.updateRenderPos(w-16, 0);
  this.scrollPosY = this.scrollBar.getTopIndexForSet(totalHeight, height);
  }

public void addGuiElement(GuiElement el)
  {
  this.elements.add(el);
  }

@Override
public void drawElement(int mouseX, int mouseY)
  {
  ScaledResolution scaledRes = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
  int guiScale = scaledRes.getScaleFactor();
  float vAspect = (float)this.mc.displayWidth/(float)this.mc.displayHeight;

  float w = this.width * guiScale;
  float h = height * guiScale;

  float x = guiLeft*guiScale + renderPosX*guiScale;
  float y = guiTop*guiScale + parentGui.getYSize()*guiScale - h - renderPosY*guiScale;

  float scaleY = (float)mc.displayHeight / h;
  float scaleX = (float)mc.displayWidth / w;  
  GL11.glViewport((int)x, (int)y, (int)w, (int)h);  
  GL11.glScalef(scaleX, scaleY, 1);
//  for(int i = 0; i < 50; i++)
//    {
//    this.drawString(mc.fontRenderer, "testString....aba;lkja;lhasdfasdfqwerqweradfadfaqwerqwetasdfawerqwerqasdfawerqwerqwerqasdfasdfasdfaqewrqwreghjkghuityuitftyertyfbgsrtwertsdfgasdfawreqwe", 0, i*10, 0xffffffff);
//    } 


  mouseX = mouseX - this.scrollPosX - this.guiLeft - this.renderPosX;
  mouseY = mouseY + this.scrollPosY - this.guiTop - this.renderPosY;

  this.scrollPosY = this.scrollBar.getTopIndexForSet(2*width, width);
  this.scrollBar.updateGuiPos(0, 0);
  this.scrollBar.drawElement(mouseX, mouseY-scrollPosY);
  //Config.logDebug("adj mouse X: "+mouseX+","+mouseY);
  for(GuiElement el : this.elements)
    {
    el.drawElement(mouseX, mouseY);
    //Config.logDebug("drawing element. x: "+(el.guiLeft+el.renderPosX)+" y: "+(el.guiTop+el.renderPosY));
    }  
  GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
  }


@Override
public void updateGuiPos(int x, int y)
  {  
  this.guiLeft = x;
  this.guiTop = y; 
  for(GuiElement el : this.elements)
    {
    el.updateGuiPos(scrollPosX, -scrollPosY);
    }
  }

@Override
public void onMousePressed(int x, int y, int num)
  {
  int adjX = x - this.guiLeft - renderPosX;
  int adjY = y - this.guiTop - renderPosY;
  Config.logDebug("adjX: "+adjX+" adjY: "+adjY);

  if(this.isMouseOver(x,y))
    {
    this.scrollBar.onMousePressed(adjX, adjY, num);
    for(GuiElement el : this.elements)
      {
      el.onMousePressed(adjX, adjY, num);
      }
    }


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
  int adjX = x - this.guiLeft - renderPosX;
  int adjY = y - this.guiTop - renderPosY;
  for(GuiElement el : this.elements)
    {
    el.onMouseReleased(adjX, adjY, num);
    } 
  this.scrollBar.onMouseReleased(adjX, adjY, num);
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

  int adjX = x - this.guiLeft - renderPosX;
  int adjY = y - this.guiTop - renderPosY;
  for(GuiElement el : this.elements)
    {
    el.isMouseOver = false;
    if(this.isMouseOver(x,y))
      {
      el.onMouseMoved(adjX, adjY, num);
      }
    } 

  this.scrollBar.isMouseOver = false;
  if(isMouseOver(x, y))
    {
    this.scrollBar.onMouseMoved(adjX, adjY, num);
    }

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
    int adjX = x - this.guiLeft - renderPosX;
    int adjY = y - this.guiTop - renderPosY;
    this.scrollBar.onMouseWheel(adjX, adjY, wheel);
    for(GuiElement el : this.elements)
      {
      el.onMouseWheel(adjX, adjY, wheel);
      }
    if(this.handleMouseWheel(x, y, wheel))
      {
      this.parent.onElementMouseWheel(this, wheel);
      }
    }
  }

public void onKeyTyped(char ch, int keyNum)
  {  
  for(GuiElement el : this.elements)
    {
    el.onKeyTyped(ch, keyNum);
    } 
  this.scrollBar.onKeyTyped(ch, keyNum);
  if(this.handleKeyInput(ch, keyNum) && this.parent!=null)
    {
    this.parent.onElementKeyTyped(ch, keyNum);
    }
  }

@Override
public boolean handleMousePressed(int x, int y, int num)
  {  
  return false;
  }

@Override
public boolean handleMouseReleased(int x, int y, int num)
  {
  // TODO Auto-generated method stub
  return false;
  }

@Override
public boolean handleMouseMoved(int x, int y, int num)
  {
  // TODO Auto-generated method stub
  return false;
  }

@Override
public boolean handleMouseWheel(int x, int y, int wheel)
  {
  // TODO Auto-generated method stub
  return false;
  }

@Override
public boolean handleKeyInput(char ch, int keyNum)
  {
  // TODO Auto-generated method stub
  return false;
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  this.parent.onElementActivated(element);
  }

@Override
public void onElementReleased(IGuiElement element)
  {
  this.parent.onElementReleased(element);
  }

@Override
public void onElementDragged(IGuiElement element)
  {
  this.parent.onElementDragged(element);
  }

@Override
public void onElementMouseWheel(IGuiElement element, int amt)
  {
  this.parent.onElementMouseWheel(element, amt);
  }

@Override
public void onElementKeyTyped(char ch, int keyNum)
  {
  this.parent.onElementKeyTyped(ch, keyNum);
  }

}
