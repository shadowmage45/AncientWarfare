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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiElement;
import shadowmage.ancient_framework.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.GuiString;
import shadowmage.ancient_framework.client.model.ModelPiece;
import shadowmage.ancient_framework.client.model.Primitive;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.meim.common.config.MEIMConfig;

public class GuiModelEditorSetup
{

private GuiModelEditor gui;

private GuiButtonSimple load;
private GuiButtonSimple save;
private GuiButtonSimple importPiece;
private GuiButtonSimple uvMap;
private GuiButtonSimple clear;
private GuiButtonSimple loadTexture;

private GuiButtonSimple copyPiece;
private GuiButtonSimple newPiece;
private GuiButtonSimple clearSelection;
private GuiButtonSimple deletePiece;
private GuiButtonSimple changePieceParent;
private GuiButtonSimple clearPieceParent;
private GuiButtonSimple changeBoxOwnerPiece;

private GuiButtonSimple addBox;
private GuiButtonSimple deleteBox;

private GuiButtonSimple pieceXMinus;
private GuiButtonSimple pieceXPlus;
private GuiButtonSimple pieceYMinus;
private GuiButtonSimple pieceYPlus;
private GuiButtonSimple pieceZMinus;
private GuiButtonSimple pieceZPlus;

private GuiNumberInputLine pieceXInput;
private GuiNumberInputLine pieceYInput;
private GuiNumberInputLine pieceZInput;

private GuiButtonSimple pieceRXMinus;
private GuiButtonSimple pieceRXPlus;
private GuiButtonSimple pieceRYMinus;
private GuiButtonSimple pieceRYPlus;
private GuiButtonSimple pieceRZMinus;
private GuiButtonSimple pieceRZPlus;

private GuiNumberInputLine pieceRXInput;
private GuiNumberInputLine pieceRYInput;
private GuiNumberInputLine pieceRZInput;

private GuiButtonSimple primitiveXMinus;
private GuiButtonSimple primitiveXPlus;
private GuiButtonSimple primitiveYMinus;
private GuiButtonSimple primitiveYPlus;
private GuiButtonSimple primitiveZMinus;
private GuiButtonSimple primitiveZPlus;

private GuiNumberInputLine primitiveXInput;
private GuiNumberInputLine primitiveYInput;
private GuiNumberInputLine primitiveZInput;

private GuiButtonSimple primitiveRXMinus;
private GuiButtonSimple primitiveRXPlus;
private GuiButtonSimple primitiveRYMinus;
private GuiButtonSimple primitiveRYPlus;
private GuiButtonSimple primitiveRZMinus;
private GuiButtonSimple primitiveRZPlus;

private GuiNumberInputLine primitiveRXInput;
private GuiNumberInputLine primitiveRYInput;
private GuiNumberInputLine primitiveRZInput;

private GuiButtonSimple primitiveX1Minus;
private GuiButtonSimple primitiveX1Plus;
private GuiButtonSimple primitiveY1Minus;
private GuiButtonSimple primitiveY1Plus;
private GuiButtonSimple primitiveZ1Minus;
private GuiButtonSimple primitiveZ1Plus;

private GuiNumberInputLine primitiveX1Input;
private GuiNumberInputLine primitiveY1Input;
private GuiNumberInputLine primitiveZ1Input;

private GuiButtonSimple primitiveX2Minus;
private GuiButtonSimple primitiveX2Plus;
private GuiButtonSimple primitiveY2Minus;
private GuiButtonSimple primitiveY2Plus;
private GuiButtonSimple primitiveZ2Minus;
private GuiButtonSimple primitiveZ2Plus;

private GuiNumberInputLine primitiveX2Input;
private GuiNumberInputLine primitiveY2Input;
private GuiNumberInputLine primitiveZ2Input;

private GuiScrollableArea leftControlPanel;
private GuiScrollableArea leftPiecesPanel;
private GuiScrollableArea rightControlPanel;
private GuiScrollableArea rightPrimitivesPanel;


HashMap<GuiString, ModelPiece> pieceLabelMap = new HashMap<GuiString, ModelPiece>();
HashMap<GuiString, Primitive> primitiveLabelMap = new HashMap<GuiString, Primitive>();

private float scale = 0.0625f;

public GuiModelEditorSetup(GuiModelEditor gui)
  {
  this.gui = gui;
  }

public void setupControls()
  {
  leftControlPanel = new GuiScrollableArea(0, gui, 0, 0, 100, 120, 120);
  this.addElement(leftControlPanel);
  leftPiecesPanel = new GuiScrollableArea(1, gui, 0, 120, 100, 120, 120);
  this.addElement(leftPiecesPanel);
  rightControlPanel = new GuiScrollableArea(2, gui, 0, 0, 100, 120, 120);
  this.addElement(rightControlPanel);
  rightPrimitivesPanel = new GuiScrollableArea(3, gui, 0, 120, 100, 120, 120);
  this.addElement(rightPrimitivesPanel); 
    
  addLeftControls();
  addRightControls();
  addLeftLabels();
  addRightLabels();
  }

private void addLeftControls()
  {
  int totalHeight = 0;
   
  newPiece = new GuiButtonSimple(0, leftControlPanel, 84, 12, "New Piece")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num))
        {
        Minecraft.getMinecraft().displayGuiScreen(new GuiNewPiece((ContainerBase) gui.inventorySlots, gui));
        }
      return true;
      }
    };  
  newPiece.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  leftControlPanel.elements.add(newPiece);
  
  copyPiece = new GuiButtonSimple(0, leftControlPanel, 84, 12, "Copy Piece")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num))
        {
        gui.copyPiece();
        }
      return true;
      }
    };  
  copyPiece.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  leftControlPanel.elements.add(copyPiece);
  
  clearSelection = new GuiButtonSimple(0, leftControlPanel, 84, 12, "Clear Selection")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num))
        {
        gui.clearSelection();
        }
      return true;
      }
    };  
  clearSelection.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  leftControlPanel.elements.add(clearSelection);
  
  deletePiece = new GuiButtonSimple(0, leftControlPanel, 84, 12, "Delete Piece")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num))
        {
        gui.deletePiece();
        }
      return true;
      }
    };  
  deletePiece.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  leftControlPanel.elements.add(deletePiece);
    
  changePieceParent = new GuiButtonSimple(0, leftControlPanel, 84, 12, "Change Parent")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num))
        {
        gui.changeParent();
        }
      return true;
      }
    };  
  changePieceParent.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  leftControlPanel.elements.add(changePieceParent);
  
  clearPieceParent = new GuiButtonSimple(0, leftControlPanel, 84, 12, "Clear Parent")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num))
        {
        if(gui.selectedPiece!=null && gui.selectedPiece.getParent()!=null)
          {
          gui.selectedPiece.getParent().removeChild(gui.selectedPiece);
          }
        }
      return true;
      }
    };  
  clearPieceParent.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  leftControlPanel.elements.add(clearPieceParent);
  
  addBox = new GuiButtonSimple(0, leftControlPanel, 84, 12, "Add Box")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num))
        {
        Minecraft.getMinecraft().displayGuiScreen(new GuiNewPrimitive((ContainerBase) gui.inventorySlots, gui));
        }
      return true;
      }
    };  
  addBox.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  leftControlPanel.elements.add(addBox);
  
  deleteBox = new GuiButtonSimple(0, leftControlPanel, 84, 12, "Delete Box")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.deletePiece();
        }
      return true;
      }
    };  
  deleteBox.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  leftControlPanel.elements.add(deleteBox);
  
  changeBoxOwnerPiece = new GuiButtonSimple(0, leftControlPanel, 84, 12, "Change Box Owner")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.swapBox();
        }
      return true;
      }
    };  
  changeBoxOwnerPiece.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  leftControlPanel.elements.add(changeBoxOwnerPiece);
     
  totalHeight = addLeftPieceControls(totalHeight);
  totalHeight = addLeftPrimitiveControls(totalHeight);
    
  leftControlPanel.updateTotalHeight(totalHeight);
  }

private int addLeftPieceControls(int totalHeight)
  {
  int col1 = 0;
  int col2 = 25;
  int col3 = 25+12+2;
  int col4 = 25+12+2+20+2;
  pieceXMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setPosition(gui.selectedPiece.x() - 1 * scale, gui.selectedPiece.y(), gui.selectedPiece.z());  
        updateButtonValues();
        }
      return true;
      }
    };  
  pieceXMinus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(pieceXMinus);
  
  pieceXPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setPosition(gui.selectedPiece.x()+1 * scale, gui.selectedPiece.y(), gui.selectedPiece.z());  
        updateButtonValues();
        }
      return true;
      }
    };  
  pieceXPlus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(pieceXPlus);
  
  pieceXInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()      
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setPosition(getFloatVal() * scale, gui.selectedPiece.y(), gui.selectedPiece.z());  
        updateButtonValues();
        }
      }     
    };
  pieceXInput.setValue(0.f);
  pieceXInput.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(pieceXInput);
  
  GuiString label = new GuiString(0, leftControlPanel, 25, 12, "P:X");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  pieceYMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setPosition(gui.selectedPiece.x(), gui.selectedPiece.y()-1 * scale, gui.selectedPiece.z()); 
        updateButtonValues();
        }
      return true;
      }
    };  
  pieceYMinus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(pieceYMinus);
  
  pieceYPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setPosition(gui.selectedPiece.x(), gui.selectedPiece.y()+1 * scale, gui.selectedPiece.z());  
        updateButtonValues();
        }
      return true;
      }
    };  
  pieceYPlus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(pieceYPlus);
  
  pieceYInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()      
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setPosition(gui.selectedPiece.x(), getFloatVal() * scale, gui.selectedPiece.z());      
        updateButtonValues();
        }
      }  
    };
  pieceYInput.setValue(0.f);
  pieceYInput.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(pieceYInput);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "P:Y");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
    
  
  pieceZMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setPosition(gui.selectedPiece.x(), gui.selectedPiece.y(), gui.selectedPiece.z()-1 * scale);  
        updateButtonValues();
        }
      return true;
      }
    };  
  pieceZMinus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(pieceZMinus);
  
  pieceZPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setPosition(gui.selectedPiece.x(), gui.selectedPiece.y(), gui.selectedPiece.z()+1 * scale);  
        updateButtonValues();
        }
      return true;
      }
    };  
  pieceZPlus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(pieceZPlus);
  
  pieceZInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()      
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setPosition(gui.selectedPiece.x(), gui.selectedPiece.y(), getFloatVal() * scale);   
        updateButtonValues();   
        }
      }       
    };
  pieceZInput.setValue(0.f);
  pieceZInput.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(pieceZInput);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "P:Z");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  pieceRXMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setRotation(gui.selectedPiece.rx()-1, gui.selectedPiece.ry(), gui.selectedPiece.rz());  
        updateButtonValues();
        }
      return true;
      }
    };  
  pieceRXMinus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(pieceRXMinus);
  
  pieceRXPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setRotation(gui.selectedPiece.rx()+1, gui.selectedPiece.ry(), gui.selectedPiece.rz());  
        updateButtonValues();
        }
      return true;
      }
    };  
  pieceRXPlus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(pieceRXPlus);
  
  pieceRXInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()      
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setRotation(getFloatVal(), gui.selectedPiece.ry(), gui.selectedPiece.rz());  
        updateButtonValues();
        }
      }
    };
  pieceRXInput.setValue(0.f);
  pieceRXInput.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(pieceRXInput);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "P:RX");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  pieceRYMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setRotation(gui.selectedPiece.rx(), gui.selectedPiece.ry()-1, gui.selectedPiece.rz());  
        updateButtonValues();
        }
      return true;
      }
    };  
  pieceRYMinus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(pieceRYMinus);
  
  pieceRYPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setRotation(gui.selectedPiece.rx(), gui.selectedPiece.ry()+1, gui.selectedPiece.rz());  
        updateButtonValues();
        }
      return true;
      }
    };  
  pieceRYPlus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(pieceRYPlus);
  
  pieceRYInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()      
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setRotation(gui.selectedPiece.rx(), getFloatVal(), gui.selectedPiece.rz());  
        updateButtonValues();
        }
      }   
    };
  pieceRYInput.setValue(0.f);
  pieceRYInput.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(pieceRYInput);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "P:RY");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  pieceRZMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setRotation(gui.selectedPiece.rx(), gui.selectedPiece.ry(), gui.selectedPiece.rz()-1);  
        updateButtonValues();
        }
      return true;
      }
    };  
  pieceRZMinus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(pieceRZMinus);
  
  pieceRZPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setRotation(gui.selectedPiece.rx(), gui.selectedPiece.ry(), gui.selectedPiece.rz()+1);  
        updateButtonValues();
        }
      return true;
      }
    };  
  pieceRZPlus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(pieceRZPlus);
  
  pieceRZInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()      
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        gui.selectedPiece.setRotation(gui.selectedPiece.rx(), gui.selectedPiece.ry(),  getFloatVal());  
        updateButtonValues();
        }
      }      
    };
  pieceRZInput.setValue(0.f);
  pieceRZInput.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(pieceRZInput);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "P:RZ");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
      
  return totalHeight;
  }

private int addLeftPrimitiveControls(int totalHeight)
  {
  int col1 = 0;
  int col2 = 25;
  int col3 = 25+12+2;
  int col4 = 25+12+2+20+2;
  
  primitiveXMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {   
        gui.selectedPrimitive.setOrigin(gui.selectedPrimitive.x()-1 * scale, gui.selectedPrimitive.y(), gui.selectedPrimitive.z());          
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveXMinus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(primitiveXMinus);
  
  primitiveXPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setOrigin(gui.selectedPrimitive.x()+1 * scale, gui.selectedPrimitive.y(), gui.selectedPrimitive.z());  
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveXPlus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(primitiveXPlus);
  
  primitiveXInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {        
        gui.selectedPrimitive.setOrigin(getFloatVal() * scale, gui.selectedPrimitive.y(), gui.selectedPrimitive.z());          
        updateButtonValues();
        }
      }
    };
  primitiveXInput.setValue(0.f);
  primitiveXInput.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(primitiveXInput);
  
  GuiString label = new GuiString(0, leftControlPanel, 25, 12, "B:OX");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  primitiveYMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {   
        gui.selectedPrimitive.setOrigin(gui.selectedPrimitive.x(), gui.selectedPrimitive.y()-1 * scale, gui.selectedPrimitive.z());  
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveYMinus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(primitiveYMinus);
  
  primitiveYPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setOrigin(gui.selectedPrimitive.x(), gui.selectedPrimitive.y()+1 * scale, gui.selectedPrimitive.z());
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveYPlus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(primitiveYPlus);
  
  primitiveYInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setOrigin(gui.selectedPrimitive.x(), getFloatVal() * scale, gui.selectedPrimitive.z());  
        updateButtonValues();
        }
      }
    };
  primitiveYInput.setValue(0.f);
  primitiveYInput.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(primitiveYInput);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "B:OY");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  primitiveZMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {   
        gui.selectedPrimitive.setOrigin(gui.selectedPrimitive.x(), gui.selectedPrimitive.y(), gui.selectedPrimitive.z()-1 * scale);  
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveZMinus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(primitiveZMinus);
  
  primitiveZPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setOrigin(gui.selectedPrimitive.x(), gui.selectedPrimitive.y(), gui.selectedPrimitive.z()+1 * scale);  
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveZPlus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(primitiveZPlus);
  
  primitiveZInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setOrigin(gui.selectedPrimitive.x(), gui.selectedPrimitive.y(), getFloatVal() * scale);  
        updateButtonValues();
        }
      }    
    };
  primitiveZInput.setValue(0.f);
  primitiveZInput.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(primitiveZInput);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "B:OZ");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  primitiveRXMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        { 
        gui.selectedPrimitive.setRotation(gui.selectedPrimitive.rx()-1, gui.selectedPrimitive.ry(), gui.selectedPrimitive.rz());  
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveRXMinus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(primitiveRXMinus);
  
  primitiveRXPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setRotation(gui.selectedPrimitive.rx()+1, gui.selectedPrimitive.ry(), gui.selectedPrimitive.rz());  
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveRXPlus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(primitiveRXPlus);
  
  primitiveRXInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setRotation(getFloatVal(), gui.selectedPrimitive.ry(), gui.selectedPrimitive.rz());  
        updateButtonValues();
        }
      }
    };
  primitiveRXInput.setValue(0.f);
  primitiveRXInput.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(primitiveRXInput);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "B:RX");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  primitiveRYMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {   
        gui.selectedPrimitive.setRotation(gui.selectedPrimitive.rx(), gui.selectedPrimitive.ry()-1, gui.selectedPrimitive.rz());  
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveRYMinus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(primitiveRYMinus);
  
  primitiveRYPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setRotation(gui.selectedPrimitive.rx(), gui.selectedPrimitive.ry()+1, gui.selectedPrimitive.rz());  
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveRYPlus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(primitiveRYPlus);
  
  primitiveRYInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setRotation(gui.selectedPrimitive.rx(), getFloatVal(), gui.selectedPrimitive.rz());
        updateButtonValues();
        }
      }
    };
  primitiveRYInput.setValue(0.f);
  primitiveRYInput.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(primitiveRYInput);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "B:RY");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  primitiveRZMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {   
        gui.selectedPrimitive.setRotation(gui.selectedPrimitive.rx(), gui.selectedPrimitive.ry(), gui.selectedPrimitive.rz()-1);  
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveRZMinus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(primitiveRZMinus);
  
  primitiveRZPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setRotation(gui.selectedPrimitive.rx(), gui.selectedPrimitive.ry(), gui.selectedPrimitive.rz()+1);
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveRZPlus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(primitiveRZPlus);
  
  primitiveRZInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setRotation(gui.selectedPrimitive.rx(), gui.selectedPrimitive.ry(), getFloatVal());  
        updateButtonValues();
        }
      }
    };
  primitiveRZInput.setValue(0.f);
  primitiveRZInput.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(primitiveRZInput);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "B:RZ");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  primitiveX1Minus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {   
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1()-1 * scale, gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1(), gui.selectedPrimitive.width(), gui.selectedPrimitive.height(), gui.selectedPrimitive.length());  
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveX1Minus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(primitiveX1Minus);
  
  primitiveX1Plus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1()+1 * scale, gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1(), gui.selectedPrimitive.width(), gui.selectedPrimitive.height(), gui.selectedPrimitive.length());
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveX1Plus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(primitiveX1Plus);
  
  primitiveX1Input = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setBounds(getFloatVal() * scale, gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1(), gui.selectedPrimitive.width(), gui.selectedPrimitive.height(), gui.selectedPrimitive.length()); 
        updateButtonValues();
        }
      }
    };
  primitiveX1Input.setValue(0.f);
  primitiveX1Input.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(primitiveX1Input);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "B:X1");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  primitiveY1Minus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {   
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1()-1 * scale, gui.selectedPrimitive.z1(), gui.selectedPrimitive.width(), gui.selectedPrimitive.height(), gui.selectedPrimitive.length());
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveY1Minus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(primitiveY1Minus);
  
  primitiveY1Plus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1()+1 * scale, gui.selectedPrimitive.z1(), gui.selectedPrimitive.width(), gui.selectedPrimitive.height(), gui.selectedPrimitive.length());
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveY1Plus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(primitiveY1Plus);
  
  primitiveY1Input = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), getFloatVal() * scale, gui.selectedPrimitive.z1(), gui.selectedPrimitive.width(), gui.selectedPrimitive.height(), gui.selectedPrimitive.length());  
        updateButtonValues();
        }
      }
    };
  primitiveY1Input.setValue(0.f);
  primitiveY1Input.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(primitiveY1Input);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "B:Y1");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  primitiveZ1Minus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {   
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1()-1 * scale, gui.selectedPrimitive.width(), gui.selectedPrimitive.height(), gui.selectedPrimitive.length());
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveZ1Minus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(primitiveZ1Minus);
  
  primitiveZ1Plus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1()+1 * scale, gui.selectedPrimitive.width(), gui.selectedPrimitive.height(), gui.selectedPrimitive.length());
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveZ1Plus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(primitiveZ1Plus);
  
  primitiveZ1Input = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1(), getFloatVal() * scale, gui.selectedPrimitive.width(), gui.selectedPrimitive.height(), gui.selectedPrimitive.length()); 
        updateButtonValues();
        }
      }
    };
  primitiveZ1Input.setValue(0.f);
  primitiveZ1Input.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(primitiveZ1Input);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "B:Z1");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  primitiveX2Minus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {   
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1(), gui.selectedPrimitive.width()-1 * scale, gui.selectedPrimitive.height(), gui.selectedPrimitive.length());
        updateButtonValues(); 
        }
      return true;
      }
    };  
  primitiveX2Minus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(primitiveX2Minus);
  
  primitiveX2Plus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1(), gui.selectedPrimitive.width()+1 * scale, gui.selectedPrimitive.height(), gui.selectedPrimitive.length());
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveX2Plus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(primitiveX2Plus);
  
  primitiveX2Input = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1(), getFloatVal() * scale, gui.selectedPrimitive.height(), gui.selectedPrimitive.length());   
        updateButtonValues();
        }
      }
    };
  primitiveX2Input.setValue(0.f);
  primitiveX2Input.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(primitiveX2Input);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "B:X2");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  primitiveY2Minus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {   
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1(), gui.selectedPrimitive.width(), gui.selectedPrimitive.height()-1 * scale, gui.selectedPrimitive.length());
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveY2Minus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(primitiveY2Minus);
  
  primitiveY2Plus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1(), gui.selectedPrimitive.width(), gui.selectedPrimitive.height()+1 * scale, gui.selectedPrimitive.length());
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveY2Plus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(primitiveY2Plus);
  
  primitiveY2Input = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1(), gui.selectedPrimitive.width(), getFloatVal() * scale, gui.selectedPrimitive.length()); 
        updateButtonValues();
        }
      }
    };
  primitiveY2Input.setValue(0.f);
  primitiveY2Input.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(primitiveY2Input);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "B:Y2");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  primitiveZ2Minus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {   
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1(), gui.selectedPrimitive.width(), gui.selectedPrimitive.height(), gui.selectedPrimitive.length()-1 * scale);
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveZ2Minus.updateRenderPos(col2, totalHeight);
  leftControlPanel.addGuiElement(primitiveZ2Minus);
  
  primitiveZ2Plus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1(), gui.selectedPrimitive.width(), gui.selectedPrimitive.height(), gui.selectedPrimitive.length()+1 * scale);
        updateButtonValues();
        }
      return true;
      }
    };  
  primitiveZ2Plus.updateRenderPos(col4, totalHeight);
  leftControlPanel.addGuiElement(primitiveZ2Plus);
  
  primitiveZ2Input = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public void onElementActivated()
      {
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null && gui.selectedPrimitive!=null)
        {
        gui.selectedPrimitive.setBounds(gui.selectedPrimitive.x1(), gui.selectedPrimitive.y1(), gui.selectedPrimitive.z1(), gui.selectedPrimitive.width(), gui.selectedPrimitive.height(), getFloatVal() * scale);   
        updateButtonValues();
        }
      }
    };
  primitiveZ2Input.setValue(0.f);
  primitiveZ2Input.updateRenderPos(col3, totalHeight);
  leftControlPanel.addGuiElement(primitiveZ2Input);
  
  label = new GuiString(0, leftControlPanel, 25, 12, "B:Z2");
  label.updateRenderPos(col1, totalHeight);
  leftControlPanel.addGuiElement(label);
  
  totalHeight+=12;
  
  
  return totalHeight;
  }

public void addLeftLabels()
  {
  int totalHeight = 0;
  pieceLabelMap.clear();
  if(gui.model!=null)
    {
    List<ModelPiece> pieces = new ArrayList<ModelPiece>();
    gui.model.getPieces(pieces);
    
    GuiString label = new GuiString(0, leftPiecesPanel, 80, 12, "Pieces:");
    label.updateRenderPos(0, totalHeight);
    leftPiecesPanel.addGuiElement(label);
    totalHeight+=12;
    
    for(ModelPiece piece : pieces)
      {
      label = new GuiString(0, leftPiecesPanel, 80, 12, piece.getName())
        {
        @Override
        public void onElementActivated()
          {
          ModelPiece p = pieceLabelMap.get(this);
          if(p!=null)
            {
            gui.selectedPiece = p;
            gui.selectedPrimitive = null;
            gui.refreshGui();
            updateButtonValues();
            AWLog.logDebug("selected piece: "+gui.selectedPiece + " prims: " + (gui.selectedPiece!=null ? gui.selectedPiece.getPrimitives().size() : "null"));
            }
          }
        };
      label.clickable = true;
      label.updateRenderPos(0, totalHeight);
      pieceLabelMap.put(label, piece);
      leftPiecesPanel.addGuiElement(label);
      totalHeight+=12;
      }    
    }  
  leftPiecesPanel.updateTotalHeight(totalHeight);
  }

private void addRightControls()
  {
  int totalHeight = 0;
  
  load = new GuiButtonSimple(0, rightControlPanel, 84, 12, "Load")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num))
        {
        gui.selectionMode = gui.SELECT_LOAD;
        Minecraft.getMinecraft().displayGuiScreen(new GuiFileSelect(gui, gui, MEIMConfig.getModelSaveDir(), false));
        }
      return true;
      }
    };
  load.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  rightControlPanel.elements.add(load);
  
  save = new GuiButtonSimple(0, rightControlPanel, 84, 12, "Save")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && gui.model!=null)
        {
        gui.selectionMode = gui.SELECT_SAVE;
        Minecraft.getMinecraft().displayGuiScreen(new GuiFileSelect(gui, gui, MEIMConfig.getModelSaveDir(), true));
        }
      return true;
      }
    };
  save.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  rightControlPanel.elements.add(save);
  
  importPiece = new GuiButtonSimple(0, rightControlPanel, 84, 12, "Import")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && gui.model!=null)
        {
        gui.selectionMode = gui.SELECT_IMPORT_PIECE;
        Minecraft.getMinecraft().displayGuiScreen(new GuiFileSelect(gui, gui, MEIMConfig.getModelSaveDir(), false));
        }
      return true;
      }
    };
  importPiece.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  rightControlPanel.elements.add(importPiece);
  
  clear = new GuiButtonSimple(0, rightControlPanel, 84, 12, "Clear")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num))
        {
        gui.model = null;
        gui.initModel();
        updateButtonValues();
        gui.refreshGui();
        }
      return true;
      }
    };
  clear.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  rightControlPanel.elements.add(clear);
  
  uvMap = new GuiButtonSimple(0, rightControlPanel, 84, 12, "UVMap")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num))
        {
        gui.openUVMap();
        }
      return true;
      }
    };
  uvMap.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  rightControlPanel.elements.add(uvMap);
    
  loadTexture = new GuiButtonSimple(0, rightControlPanel, 84, 12, "Load Texture")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num))
        {
        gui.selectionMode = GuiModelEditor.SELECT_LOAD_TEXTURE;
        Minecraft.getMinecraft().displayGuiScreen(new GuiFileSelect(gui, gui, MEIMConfig.getTexLoadDir(), false));
        }
      return true;
      }
    };
  loadTexture.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  rightControlPanel.elements.add(loadTexture);
  
  
  GuiString label = new GuiString(0, rightControlPanel, 40, 12, "Scale");
  label.updateRenderPos(0, totalHeight);
  rightControlPanel.addGuiElement(label);
  
  GuiNumberInputLine scaleInput = new GuiNumberInputLine(0, rightControlPanel, 40, 10, 10, "0.0625")
    {
    @Override
    public void onElementActivated()
      {
      scale = getFloatVal();
      updateButtonValues();
      }
    };
  scaleInput.setValue(scale);
  scaleInput.updateRenderPos(40, totalHeight+1);
  rightControlPanel.addGuiElement(scaleInput);
  totalHeight+=12;
    
  rightControlPanel.updateTotalHeight(totalHeight);
  }

public void addRightLabels()
  {
  int totalHeight = 0;
  primitiveLabelMap.clear();
  
  if(gui.selectedPiece!=null)
    {
    GuiString label = new GuiString(0, rightPrimitivesPanel, 80, 12, "Primitives:");
    label.updateRenderPos(0, totalHeight);
    rightPrimitivesPanel.addGuiElement(label);    
    totalHeight+=12;
    
    int num = 1;    
    for(Primitive p : gui.selectedPiece.getPrimitives())
      {
      label = new GuiString(0, rightPrimitivesPanel, 80, 12, "Box:"+num)
        {
        @Override
        public void onElementActivated()
          {
          gui.selectedPrimitive = primitiveLabelMap.get(this);
          gui.refreshGui();
          updateButtonValues();
          }
        };
      label.clickable = true;
      label.updateRenderPos(0, totalHeight);      
      totalHeight+=12;     
      num++;     
      
      primitiveLabelMap.put(label, p);
      rightPrimitivesPanel.addGuiElement(label);
      }
    }
  rightPrimitivesPanel.updateTotalHeight(totalHeight);
  }

private void addElement(GuiElement element)
  {
  gui.addElement(element);
  }

public void updateControls(int guiLeft, int guiTop, int width, int height)
  {
  leftControlPanel.updateRenderPos(-guiLeft, -guiTop);
  leftControlPanel.setHeight(height/2);
  leftControlPanel.updateTotalHeight(leftControlPanel.totalHeight);
  leftPiecesPanel.updateRenderPos(-guiLeft, -guiTop + height/2);
  leftPiecesPanel.setHeight(height/2);
  leftPiecesPanel.updateTotalHeight(leftPiecesPanel.totalHeight);
  rightControlPanel.updateRenderPos(-guiLeft + width - 100, -guiTop);
  rightControlPanel.setHeight(height/2);
  rightControlPanel.updateTotalHeight(rightControlPanel.totalHeight);
  rightPrimitivesPanel.updateRenderPos(-guiLeft + width - 100, -guiTop + height/2);
  rightPrimitivesPanel.setHeight(height/2); 
  rightPrimitivesPanel.updateTotalHeight(rightPrimitivesPanel.totalHeight);
  
  leftPiecesPanel.elements.clear();
  addLeftLabels();
  rightPrimitivesPanel.elements.clear();
  addRightLabels();
  
  this.updateButtonValues();
  }


/**
 * 
 */
public void updateButtonValues()
  {
  pieceXInput.setValue(gui.selectedPiece==null ? 0.f : gui.selectedPiece.x()/scale);
  pieceYInput.setValue(gui.selectedPiece==null ? 0.f : gui.selectedPiece.y()/scale);
  pieceZInput.setValue(gui.selectedPiece==null ? 0.f : gui.selectedPiece.z()/scale);
  
  pieceRXInput.setValue(gui.selectedPiece==null ? 0.f : gui.selectedPiece.rx());
  pieceRYInput.setValue(gui.selectedPiece==null ? 0.f : gui.selectedPiece.ry());
  pieceRZInput.setValue(gui.selectedPiece==null ? 0.f : gui.selectedPiece.rz()); 
  
  primitiveXInput.setValue(gui.selectedPrimitive==null ? 0.f : gui.selectedPrimitive.x()/scale);
  primitiveYInput.setValue(gui.selectedPrimitive==null ? 0.f : gui.selectedPrimitive.y()/scale);
  primitiveZInput.setValue(gui.selectedPrimitive==null ? 0.f : gui.selectedPrimitive.z()/scale);
  
  primitiveRXInput.setValue(gui.selectedPrimitive==null ? 0.f : gui.selectedPrimitive.rx());
  primitiveRYInput.setValue(gui.selectedPrimitive==null ? 0.f : gui.selectedPrimitive.ry());
  primitiveRZInput.setValue(gui.selectedPrimitive==null ? 0.f : gui.selectedPrimitive.rz()); 
  
  primitiveX1Input.setValue(gui.selectedPrimitive==null ? 0.f : gui.selectedPrimitive.x1()/scale);
  primitiveY1Input.setValue(gui.selectedPrimitive==null ? 0.f : gui.selectedPrimitive.y1()/scale);
  primitiveZ1Input.setValue(gui.selectedPrimitive==null ? 0.f : gui.selectedPrimitive.z1()/scale);
  
  primitiveX2Input.setValue(gui.selectedPrimitive==null ? 0.f : gui.selectedPrimitive.width()/scale);
  primitiveY2Input.setValue(gui.selectedPrimitive==null ? 0.f : gui.selectedPrimitive.height()/scale);
  primitiveZ2Input.setValue(gui.selectedPrimitive==null ? 0.f : gui.selectedPrimitive.length()/scale);  
  }

}
