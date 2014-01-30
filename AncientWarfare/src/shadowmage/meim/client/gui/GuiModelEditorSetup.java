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
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiElement;
import shadowmage.ancient_framework.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.GuiString;
import shadowmage.ancient_framework.client.model.Primitive;
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

private GuiButtonSimple primitiveWMinus;
private GuiButtonSimple primitiveWPlus;
private GuiButtonSimple primitiveHMinus;
private GuiButtonSimple primitiveHPlus;
private GuiButtonSimple primitiveLMinus;
private GuiButtonSimple primitiveLPlus;

private GuiNumberInputLine primitiveWInput;
private GuiNumberInputLine primitiveHInput;
private GuiNumberInputLine primitiveLInput;

private GuiScrollableArea leftControlPanel;
private GuiScrollableArea leftPiecesPanel;
private GuiScrollableArea rightControlPanel;
private GuiScrollableArea rightPrimitivesPanel;

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
        /**
         * TODO open new piece addition gui
         */
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
        /**
         * TODO copy piece
         */
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
        gui.selectedPiece = null;
        gui.selectedPrimitive = null;
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
        /**
         * TODO delete piece
         */
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
        /**
         * TODO open select gui to change piece parent
         */
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
        //TODO delete current selected box
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
        //open piece-selection gui for swap
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
        gui.selectedPiece.setPosition(gui.selectedPiece.x()-1, gui.selectedPiece.y(), gui.selectedPiece.z()); 
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
        gui.selectedPiece.setPosition(gui.selectedPiece.x()+1, gui.selectedPiece.y(), gui.selectedPiece.z()); 
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
        gui.selectedPiece.setPosition(getFloatVal(), gui.selectedPiece.y(), gui.selectedPiece.z()); 
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
        gui.selectedPiece.setPosition(gui.selectedPiece.x(), gui.selectedPiece.y()-1, gui.selectedPiece.z());
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
        gui.selectedPiece.setPosition(gui.selectedPiece.x(), gui.selectedPiece.y()+1, gui.selectedPiece.z()); 
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
        gui.selectedPiece.setPosition(gui.selectedPiece.x(), getFloatVal(), gui.selectedPiece.z());     
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
        gui.selectedPiece.setPosition(gui.selectedPiece.x(), gui.selectedPiece.y(), gui.selectedPiece.z()-1); 
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
        gui.selectedPiece.setPosition(gui.selectedPiece.x(), gui.selectedPiece.y(), gui.selectedPiece.z()+1); 
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
        gui.selectedPiece.setPosition(gui.selectedPiece.x(), gui.selectedPiece.y(), getFloatVal());     
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
        gui.selectedPiece.setRotation(gui.selectedPiece.rx(), gui.selectedPiece.ry(), gui.selectedPiece.rz()+1); 
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
        Primitive p = gui.selectedPrimitive.copy();
        p.setOrigin(gui.selectedPrimitive.x()-1, gui.selectedPrimitive.y(), gui.selectedPrimitive.z());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
        Primitive p = gui.selectedPrimitive.copy();
        p.setOrigin(gui.selectedPrimitive.x()+1, gui.selectedPrimitive.y(), gui.selectedPrimitive.z());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        Primitive p = gui.selectedPrimitive.copy();
        p.setOrigin(getFloatVal(), gui.selectedPrimitive.y(), gui.selectedPrimitive.z());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
        Primitive p = gui.selectedPrimitive.copy();
        p.setOrigin(gui.selectedPrimitive.x(), gui.selectedPrimitive.y()-1, gui.selectedPrimitive.z());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
        Primitive p = gui.selectedPrimitive.copy();
        p.setOrigin(gui.selectedPrimitive.x(), gui.selectedPrimitive.y()+1, gui.selectedPrimitive.z());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        Primitive p = gui.selectedPrimitive.copy();
        p.setOrigin(gui.selectedPrimitive.x(), getFloatVal(), gui.selectedPrimitive.z());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
        Primitive p = gui.selectedPrimitive.copy();
        p.setOrigin(gui.selectedPrimitive.x(), gui.selectedPrimitive.y(), gui.selectedPrimitive.z()-1);
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
        Primitive p = gui.selectedPrimitive.copy();
        p.setOrigin(gui.selectedPrimitive.x(), gui.selectedPrimitive.y(), gui.selectedPrimitive.z()+1);
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        Primitive p = gui.selectedPrimitive.copy();
        p.setOrigin(gui.selectedPrimitive.x(), gui.selectedPrimitive.y(), getFloatVal());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
        Primitive p = gui.selectedPrimitive.copy();
        p.setRotation(gui.selectedPrimitive.rx()-1, gui.selectedPrimitive.ry(), gui.selectedPrimitive.rz());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
        Primitive p = gui.selectedPrimitive.copy();
        p.setRotation(gui.selectedPrimitive.rx()+1, gui.selectedPrimitive.ry(), gui.selectedPrimitive.rz());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        Primitive p = gui.selectedPrimitive.copy();
        p.setRotation(getFloatVal(), gui.selectedPrimitive.ry(), gui.selectedPrimitive.rz());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
        Primitive p = gui.selectedPrimitive.copy();
        p.setRotation(gui.selectedPrimitive.rx(), gui.selectedPrimitive.ry()-1, gui.selectedPrimitive.rz());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
        Primitive p = gui.selectedPrimitive.copy();
        p.setRotation(gui.selectedPrimitive.rx(), gui.selectedPrimitive.ry()+1, gui.selectedPrimitive.rz());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        Primitive p = gui.selectedPrimitive.copy();
        p.setRotation(gui.selectedPrimitive.rx(), getFloatVal(), gui.selectedPrimitive.rz());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
        Primitive p = gui.selectedPrimitive.copy();
        p.setRotation(gui.selectedPrimitive.rx(), gui.selectedPrimitive.ry(), gui.selectedPrimitive.rz()-1);
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
        Primitive p = gui.selectedPrimitive.copy();
        p.setRotation(gui.selectedPrimitive.rx(), gui.selectedPrimitive.ry(), gui.selectedPrimitive.rz()+1);
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
      if(GuiModelEditor.model!=null && gui.selectedPiece!=null)
        {
        Primitive p = gui.selectedPrimitive.copy();
        p.setRotation(gui.selectedPrimitive.rx(), gui.selectedPrimitive.ry(), getFloatVal());
        gui.selectedPiece.removePrimitive(gui.selectedPrimitive);
        gui.selectedPrimitive = p;
        gui.selectedPiece.addPrimitive(p); 
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
  
  return totalHeight;
  }

public void addLeftLabels()
  {
  
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
        Minecraft.getMinecraft().displayGuiScreen(new GuiFileSelect(gui, gui, MEIMConfig.getModelSaveDir(), false));
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
        /**
         * TODO open UVMap editor GUI
         */
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
        Minecraft.getMinecraft().displayGuiScreen(new GuiFileSelect(gui, gui, MEIMConfig.getModelSaveDir(), false));
        }
      return true;
      }
    };
  loadTexture.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  rightControlPanel.elements.add(loadTexture);
    
  rightControlPanel.updateTotalHeight(totalHeight);
  }

public void addRightLabels()
  {
  
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
  
  }

/**
 * 
 */
public void updateScreenContents()
  {
  pieceXInput.setValue(gui.selectedPiece==null ? 0.f : gui.selectedPiece.x());
  pieceYInput.setValue(gui.selectedPiece==null ? 0.f : gui.selectedPiece.y());
  pieceZInput.setValue(gui.selectedPiece==null ? 0.f : gui.selectedPiece.z());
  
  pieceRXInput.setValue(gui.selectedPiece==null ? 0.f : gui.selectedPiece.rx());
  pieceRYInput.setValue(gui.selectedPiece==null ? 0.f : gui.selectedPiece.ry());
  pieceRZInput.setValue(gui.selectedPiece==null ? 0.f : gui.selectedPiece.rz());  
  /**
   * update input boxes, bx, by, bz, brx, bry, brz, bx1, by1, bz1, bx2, by2, bz2
   */
  }

}
