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
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.common.config.Config;

public class GuiScrollableArea extends GuiElement implements IGuiElementCallback
{


int parentGuiWidth;
int parentGuiHeight;

GuiButtonSimple testButton;

/**
 * @param elementNum
 * @param parent
 * @param w
 * @param h
 */
public GuiScrollableArea(int elementNum, IGuiElementCallback parent, int w,  int h, int pw, int ph)
  {
  super(elementNum, parent, w, h);
  this.parentGuiWidth = pw;
  this.parentGuiHeight = ph;
  }

@Override
public void drawElement(int mouseX, int mouseY)
  {
  ScaledResolution scaledRes = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
  int guiScale = scaledRes.getScaleFactor();
  float vAspect = (float)this.mc.displayWidth/(float)this.mc.displayHeight;
 
  float w = this.width * guiScale;
  float h = height * guiScale;
     
  float x = mc.displayWidth / 2 - (this.parentGuiWidth*guiScale) / 2 + w / 2; 
  float y = mc.displayHeight - (mc.displayHeight/ 2 - (this.parentGuiHeight*guiScale)/ 2 + h / 2) - h;
  float scaleY = (float)mc.displayHeight / h;
  float scaleX = (float)mc.displayWidth / w;  
  GL11.glViewport((int)x, (int)y, (int)w, (int)h);  
  GL11.glScalef(scaleX, scaleY, 1);
  for(int i = 0; i < 50; i++)
    {
    this.drawString(mc.fontRenderer, "testString....aba;lkja;lhasdfasdfqwerqweradfadfaqwerqwetasdfawerqwerqasdfawerqwerqwerqasdfasdfasdfaqewrqwreghjkghuityuitftyertyfbgsrtwertsdfgasdfawreqwe", 0, i*10, 0xffffffff);
    }  
  GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
  if(testButton!=null)
    {
    testButton.drawElement(mouseX, mouseY);
    }
  }

@Override
public boolean handleMousePressed(int x, int y, int num)
  {
  // TODO Auto-generated method stub
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
  // TODO Auto-generated method stub
  
  }

@Override
public void onElementReleased(IGuiElement element)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void onElementDragged(IGuiElement element)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void onElementMouseWheel(IGuiElement element, int amt)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void onElementKeyTyped(char ch, int keyNum)
  {
  // TODO Auto-generated method stub
  
  }

}
