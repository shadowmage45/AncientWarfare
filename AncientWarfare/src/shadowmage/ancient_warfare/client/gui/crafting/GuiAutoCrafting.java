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
package shadowmage.ancient_warfare.client.gui.crafting;

import java.util.HashSet;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiFakeSlot;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerAWAutoCrafting;

public class GuiAutoCrafting extends GuiContainerAdvanced
{

int buttonWidth = 176-10-12-10;
ContainerAWAutoCrafting container;

GuiFakeSlot[] fakeSlotArray = new GuiFakeSlot[9];
HashSet<GuiFakeSlot> fakeSlots = new HashSet<GuiFakeSlot>();

/**
 * @param container
 */
public GuiAutoCrafting(Container container)
  {
  super(container);
  this.container = (ContainerAWAutoCrafting)container;
  this.shouldCloseOnVanillaKeys = true;
  }

@Override
public int getXSize()
  {
  return 176;
  }

@Override
public int getYSize()
  {
  return 240;
  }

@Override
public String getGuiBackGroundTexture()
  {
  return Config.texturePath+"gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  this.drawProgressBackground();
  }

public void drawProgressBackground()
  { 
  int w = 100;
  int h = 10; 
  int w1 = 100;
  int x = guiLeft + 7;
  int y = guiTop + 112+18+3;
  String tex = Config.texturePath+"gui/guiButtons2.png";
  RenderTools.drawQuadedTexture(x, y, w+6, h+6, 256, 40, tex, 0, 0);
  float progress = container.displayProgress;
  float max = container.displayProgressMax;
  float percent = 0;
  if(max!=0)
    {
    percent = progress/max;
    }
  w1 = (int)(percent*100.f);
  tex = Config.texturePath+"gui/guiButtons.png"; 
  RenderTools.drawQuadedTexture(x+3, y+3, w1, h, 104, 10, tex, 152, 234);
  x += 112;
  y += 4;
  w = (int) ((max-progress)/20);
  h = w/60;
  w = w% 60;
  tex = String.format("%sm %ss", h,w);
  this.drawString(getFontRenderer(), tex, x, y, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(this.fakeSlots.contains(element))
    {
    int slotNum = element.getElementNumber();
    ItemStack stack = player.inventory.getItemStack();
    if(stack!=null)
      {
      stack = stack.copy();
      stack.stackSize = 1;
      }
    this.container.handleLayoutSlotClick(slotNum, stack);    
    }
  }

@Override
public void setupControls()
  {
  int slot = 0;
  int x = 0;
  int y = 0;
  
  GuiFakeSlot slotElement;
  for(int i = 0; i < 9 ;i++)
    {
    slotElement = new GuiFakeSlot(i, this);
    this.guiElements.put(i, slotElement);
    this.fakeSlots.add(slotElement);
    this.fakeSlotArray[i] = slotElement;
    slotElement.autoUpdateOnClick = false;
    slotElement.updateRenderPos(8 + 18* x, 8 + 18*y);
    x++;
    if(x>=3)
      {
      x = 0;
      y++;
      }
    }
  resultSlot = new GuiFakeSlot(9, this);
  resultSlot.updateRenderPos(8+4*18, 8+18);
  resultSlot.isClickable = false;
  this.guiElements.put(9, resultSlot);
  this.forceUpdate = true;
  }

GuiFakeSlot resultSlot;

@Override
public void updateControls()
  {
  for(int i = 0; i < 9; i++)
    {
    this.fakeSlotArray[i].setItemStack(this.container.layoutMatrix[i]);
    }
  this.resultSlot.setItemStack(this.container.result);
  }

}
