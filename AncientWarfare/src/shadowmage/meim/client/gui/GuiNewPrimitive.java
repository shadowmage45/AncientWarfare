/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.meim.client.gui;

import net.minecraft.client.Minecraft;
import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiString;
import shadowmage.ancient_framework.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.client.model.ModelPiece;
import shadowmage.ancient_framework.client.model.PrimitiveBox;
import shadowmage.ancient_framework.common.container.ContainerBase;

public class GuiNewPrimitive extends GuiContainerAdvanced
{

private GuiModelEditor parentGui;

public GuiNewPrimitive(ContainerBase container, GuiModelEditor parent)
  {
  super(container);
  this.parentGui = parent;
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  
  }

@Override
public int getXSize()
  {
  return 116;
  }

@Override
public int getYSize()
  {
  return 40;
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  // TODO Auto-generated method stub

  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub

  }

@Override
public void setupControls()
  {
  GuiString label = new GuiString(0, this, 100, 12, "Name:");
  label.updateRenderPos(8, 8);
  this.addElement(label);
  
  GuiButtonSimple button = new GuiButtonSimple(1, this, 35, 12, "Back")
    {
    @Override
    public void onElementActivated()
      {
      Minecraft.getMinecraft().displayGuiScreen(parentGui);
      parentGui.refreshGui();
      }
    };
  button.updateRenderPos(40, 8);
  this.addElement(button);
    
  button = new GuiButtonSimple(2, this, 32, 12, "Add")
    {
    @Override
    public void onElementActivated()
      {
      String name = inputLine.getText();
      if(parentGui.selectedPiece!=null)
        {
        PrimitiveBox b = new PrimitiveBox(parentGui.selectedPiece);
        b.setBounds(-0.5f, -0.5f, -0.5f, 1, 1, 1);
        b.setOrigin(0, 0, 0);
        b.setRotation(0, 0, 0);
        parentGui.selectedPiece.addPrimitive(b);        
        parentGui.selectedPrimitive = b;        
        Minecraft.getMinecraft().displayGuiScreen(parentGui);
        parentGui.refreshGui();
        }
      }
    };
  button.updateRenderPos(80, 8);
  this.addElement(button);
  
  
  inputLine = new GuiTextInputLine(3, this, 100, 10, 80, "");
  inputLine.updateRenderPos(8, 8+12+2);
  this.addElement(inputLine);
  }

GuiTextInputLine inputLine;

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub

  }

}
