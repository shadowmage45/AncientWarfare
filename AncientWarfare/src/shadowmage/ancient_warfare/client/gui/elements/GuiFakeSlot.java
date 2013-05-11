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

import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GuiFakeSlot extends GuiElement
{

ItemStack fakeStack = null;

/**
 * @param elementNum
 * @param parent
 * @param w
 * @param h
 */
public GuiFakeSlot(int elementNum, IGuiElementCallback parent)
  {
  super(elementNum, parent, 18, 18);
  }

public GuiFakeSlot(int elementNum, IGuiElementCallback parent, int x, int y)
  {
  this(elementNum, parent);
  this.updateRenderPos(x, y);
  }

@Override
public void drawElement(int mouseX, int mouseY)
  {
  String tex = Config.texturePath+"gui/guiButtons.png";
  this.mc.renderEngine.bindTexture(tex);
  this.drawTexturedModalRect(guiLeft+renderPosX, guiTop+renderPosY, 152, 120, 18, 18);
  if(fakeStack!=null)
    {
    itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, fakeStack, guiLeft+renderPosX+1, guiTop+renderPosY+1);
    itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, fakeStack,  guiLeft+renderPosX+1, guiTop+renderPosY+1);
    if(isMouseOver)
      {
      GL11.glDisable(GL11.GL_LIGHTING);
      GL11.glDisable(GL11.GL_DEPTH_TEST);
      int k1 = guiLeft+renderPosX+1;
      int i1 = guiTop+renderPosY+1;
      this.drawGradientRect(k1, i1, k1 + 16, i1 + 16, -2130706433, -2130706433);
      GL11.glEnable(GL11.GL_LIGHTING);
      GL11.glEnable(GL11.GL_DEPTH_TEST); 
      int x = mouseX;
      int y = mouseY;
      drawItemStackTooltip(fakeStack, x+renderPosX+guiLeft, y+renderPosY+guiTop, true);
      }
    }  
  }

@Override
public boolean handleMousePressed(int x, int y, int num)
  {
  ItemStack p = mc.thePlayer.inventory.getItemStack();
  if(p!=null)
    {
    if(InventoryTools.doItemsMatch(fakeStack, p))
      {
      
      }
    else if(fakeStack!=null)
      {
      fakeStack=null;
      }
    else
      {
      fakeStack = p.copy();      
      }
    }
  else
    {
    fakeStack = null;
    }
  return true;
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

}
