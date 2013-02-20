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
package shadowmage.ancient_warfare.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

@Deprecated
public class GuiCheckBox extends GuiButton
{

boolean checked = false;
boolean autoSet = true;

public GuiCheckBox(int buttonID, int posX, int posY, int width, int height)
  {
  super(buttonID, posX, posY, width, height, "");
 
  }

public GuiCheckBox setChecked(boolean checked)
  {
  this.checked = checked;
  return this;
  }

public boolean checked()
  {
  return checked;
  }


/**
 * Draws this button to the screen.
 */
public void drawButton(Minecraft mc, int mouseX, int mouseY)
  {
  if(this.drawButton)
    {   
    int halfWidth = this.width/2;
    int halfHeight = this.height/2;

    //topLeft corner to be drawn
    int tlX = 0;
    int tlY = 120;

    //topRight corner to be drawn
    int trX = 40 - halfWidth;
    int trY = 120;

    //bottomLeft corner to be drawn
    int blX = 0;
    int blY = 120+40 - halfHeight;

    //bottomRight corner to be drawn    
    int brX = 40 - halfWidth;
    int brY = 120+40 - halfHeight;     

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/shadowmage/ancient_warfare/resources/gui/guiButtons.png"));
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.field_82253_i = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    int isMouseOver = this.getHoverState(this.field_82253_i);
    int vOffset = isMouseOver * 40;//will return 0, 40, or 80..for inactive, active, hover, apply to Y offset in UV rendering
    int hOffset = checked ? 40 : 0;

    
    
    //this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, w, h);
    this.drawTexturedModalRect(this.xPosition, this.yPosition, tlX + hOffset, tlY + vOffset, halfWidth, halfHeight);
    this.drawTexturedModalRect(this.xPosition+halfWidth, this.yPosition, trX + hOffset, trY + vOffset, halfWidth, halfHeight);
    this.drawTexturedModalRect(this.xPosition, this.yPosition+halfHeight, blX + hOffset, blY + vOffset, halfWidth, halfHeight);
    this.drawTexturedModalRect(this.xPosition+halfWidth, this.yPosition+halfHeight, brX + hOffset, brY + vOffset, halfWidth, halfHeight);

    
    }
  }

/**
 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
 * e).
 */
@Override
public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
  {
  boolean isPressed = this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
  if(autoSet && enabled && isPressed)
    {
    this.checked = !this.checked;
    }
  return isPressed;
  }


}
