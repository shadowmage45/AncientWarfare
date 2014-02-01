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
import shadowmage.ancient_framework.common.container.ContainerBase;

public class GuiNewPiece extends GuiContainerAdvanced
{

private GuiModelEditor parentGui;

public GuiNewPiece(ContainerBase container, GuiModelEditor parent)
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
      if(!name.equals(""))
        {
        float x = parentGui.selectedPiece == null ? 0.f : parentGui.selectedPiece.x();
        float y = parentGui.selectedPiece == null ? 0.f : parentGui.selectedPiece.y();
        float z = parentGui.selectedPiece == null ? 0.f : parentGui.selectedPiece.z();
        float rx = parentGui.selectedPiece == null ? 0.f : parentGui.selectedPiece.rx();
        float ry = parentGui.selectedPiece == null ? 0.f : parentGui.selectedPiece.ry();
        float rz = parentGui.selectedPiece == null ? 0.f : parentGui.selectedPiece.rz();        
        ModelPiece p = new ModelPiece(parentGui.model, name, x, y, z, rx, ry, rz, parentGui.selectedPiece);
        parentGui.model.addPiece(p);
        parentGui.selectedPiece = p;
        parentGui.selectedPrimitive = null;        
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
