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
package shadowmage.ancient_warfare.client.gui.structure;

import net.minecraft.client.gui.GuiButton;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.common.container.ContainerEditor;

public class GuiEditor extends GuiContainerAdvanced
{

private ContainerEditor cont;

private GuiTextBox editor;

/**
 * @param container
 */
public GuiEditor(ContainerEditor cont)
  {
  super(cont);
  this.cont = cont;
  }

@Override
public int getXSize()
  {
  return 256;
  }

@Override
public int getYSize()
  {
  return 240;
  }

@Override
public String getGuiBackGroundTexture()
  {
  return "/shadowmage/ancient_warfare/resources/gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  { 
  if(this.editor!=null)
    {
    this.editor.drawTextBox(fontRenderer, guiLeft+4, guiTop+4);
    }
  }

@Override
public void setupGui()
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void updateScreenContents()
  {
  if(this.editor==null && this.cont.clientLines!=null)
    {
    this.editor = new GuiTextBox(248, 232, 20, 32, 0xffffffff, 0x00000000, cont.clientLines);
    this.editor.activated = true;
    }
  }

@Override
public void buttonClicked(GuiButton button)
  {
  // TODO Auto-generated method stub
  
  }

@Override
protected void keyTyped(char par1, int par2)
  {
  if (par2 == 1)
    {
    super.keyTyped(par1, par2);
    }
  if(this.editor!=null)
    {
    this.editor.onKeyTyped(par1, par2);
    }
  
  }



}
