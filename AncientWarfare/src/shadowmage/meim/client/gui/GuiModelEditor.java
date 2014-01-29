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

import java.io.File;

import net.minecraft.client.Minecraft;
import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.IFileSelectCallback;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.client.model.ModelBaseAW;
import shadowmage.ancient_framework.client.model.ModelPiece;
import shadowmage.ancient_framework.client.model.Primitive;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.meim.common.config.MEIMConfig;

public class GuiModelEditor extends GuiContainerAdvanced implements IFileSelectCallback
{

private static final int SELECT_LOAD = 0;
private static final int SELECT_SAVE = 1;
private static final int SELECT_IMPORT_PIECE = 2;
private static final int SELECT_LOAD_TEXTURE = 3;
private static final int SELECT_SAVE_TEXTURE = 4;
private static final int SELECT_EXPORT_UV_MAP = 5;

private static ModelBaseAW model;

private int selectionMode = -1;

private ModelPiece selectedPiece;
private Primitive selectedPrimitive;

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

public GuiModelEditor(ContainerBase container)
  {
  super(container);
  if(model==null)
    {
    initModel();
    }
  this.shouldCloseOnVanillaKeys = true;
  }

private void initModel()
  {
  /**
   * TODO create new model, add default box
   */
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  
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
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  
  }

@Override
public void updateScreenContents()
  {

  }

@Override
public void setupControls()
  {
  leftControlPanel = new GuiScrollableArea(0, this, 0, 0, 100, 120, 120);
  this.addElement(leftControlPanel);
  leftPiecesPanel = new GuiScrollableArea(1, this, 0, 120, 100, 120, 120);
  this.addElement(leftPiecesPanel);
  rightControlPanel = new GuiScrollableArea(2, this, 0, 0, 100, 120, 120);
  this.addElement(rightControlPanel);
  rightPrimitivesPanel = new GuiScrollableArea(3, this, 0, 120, 100, 120, 120);
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
        selectedPiece = null;
        selectedPrimitive = null;
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
        if(selectedPiece!=null && selectedPiece.getParent()!=null)
          {
          selectedPiece.getParent().removeChild(selectedPiece);
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
      if(super.handleMousePressed(x, y, num))
        {
       
        }
      return true;
      }
    };  
  deleteBox.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  leftControlPanel.elements.add(deleteBox);
  
  /**
   * add piece and box adjustment controls
   */  
  
  pieceXMinus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "-")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && model!=null && selectedPiece!=null)
        {
        selectedPiece.setPosition(selectedPiece.x()-1, selectedPiece.y(), selectedPiece.z());        
        pieceXInput.setValue(selectedPiece.x());        
        }
      return true;
      }
    };  
  pieceXMinus.updateRenderPos(40, totalHeight);
  leftControlPanel.addGuiElement(pieceXMinus);
  
  pieceXPlus = new GuiButtonSimple(0, leftControlPanel, 12, 12, "+")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && model!=null && selectedPiece!=null)
        {
        selectedPiece.setPosition(selectedPiece.x()+1, selectedPiece.y(), selectedPiece.z());        
        pieceXInput.setValue(selectedPiece.x()); 
        }
      return true;
      }
    };  
  pieceXPlus.updateRenderPos(40+12+20, totalHeight);
  leftControlPanel.addGuiElement(pieceXPlus);
  
  pieceXInput = new GuiNumberInputLine(0, leftControlPanel, 20, 12, 4, "0")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num) && model!=null && selectedPiece!=null)
        {
        selectedPiece.setPosition(getFloatVal(), selectedPiece.y(), selectedPiece.z()); 
        return true;
        }
      return false;
      }
    @Override
    public boolean handleMouseWheel(int x, int y, int wheel)
      {
      if(super.handleMouseWheel(x, y, wheel))
        {
        selectedPiece.setPosition(getFloatVal(), selectedPiece.y(), selectedPiece.z()); 
        return true;
        }
      return false;
      }
    @Override
    protected void handleCharAction(char ch)
      {
      super.handleCharAction(ch);
      selectedPiece.setPosition(getFloatVal(), selectedPiece.y(), selectedPiece.z());           
      }    
    };
  pieceXInput.setValue(0.f);
  
  
  leftControlPanel.updateTotalHeight(totalHeight);
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
        selectionMode = SELECT_LOAD;
        Minecraft.getMinecraft().displayGuiScreen(new GuiFileSelect(GuiModelEditor.this, GuiModelEditor.this, MEIMConfig.getModelSaveDir(), false));
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
      if(super.handleMousePressed(x, y, num) && model!=null)
        {
        selectionMode = SELECT_SAVE;
        Minecraft.getMinecraft().displayGuiScreen(new GuiFileSelect(GuiModelEditor.this, GuiModelEditor.this, MEIMConfig.getModelSaveDir(), false));
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
      if(super.handleMousePressed(x, y, num) && model!=null)
        {
        selectionMode = SELECT_IMPORT_PIECE;
        Minecraft.getMinecraft().displayGuiScreen(new GuiFileSelect(GuiModelEditor.this, GuiModelEditor.this, MEIMConfig.getModelSaveDir(), false));
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
        model = null;
        initModel();
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
        selectionMode = SELECT_LOAD_TEXTURE;
        Minecraft.getMinecraft().displayGuiScreen(new GuiFileSelect(GuiModelEditor.this, GuiModelEditor.this, MEIMConfig.getModelSaveDir(), false));
        }
      return true;
      }
    };
  loadTexture.updateRenderPos(0, totalHeight);
  totalHeight+=12;
  rightControlPanel.elements.add(loadTexture);
    
  rightControlPanel.updateTotalHeight(totalHeight);
  }

private void addLeftLabels()
  {
  
  }

private void addRightLabels()
  {
  
  }

@Override
public void updateControls()
  {
  leftControlPanel.updateRenderPos(-guiLeft, -guiTop);
  leftControlPanel.setHeight(height/2);
  leftPiecesPanel.updateRenderPos(-guiLeft, -guiTop + height/2);
  leftPiecesPanel.setHeight(height/2);
  rightControlPanel.updateRenderPos(-guiLeft + width - 100, -guiTop);
  rightControlPanel.setHeight(height/2);
  rightPrimitivesPanel.updateRenderPos(-guiLeft + width - 100, -guiTop + height/2);
  rightPrimitivesPanel.setHeight(height/2); 
  }

@Override
public void handleFileSelection(File file)
  {
  switch(selectionMode)
  {
  case SELECT_LOAD:
    {
    
    }
    break;
  case SELECT_SAVE:
    { 
    
    }
    break;
  case SELECT_IMPORT_PIECE:
    {
    
    }
    break;
  case SELECT_LOAD_TEXTURE:
    {
    
    }
    break;
  case SELECT_SAVE_TEXTURE:
    {
    
    }
    break;
  case SELECT_EXPORT_UV_MAP:
    {
    
    }
    break;
  }
  this.refreshGui();
  }

}
