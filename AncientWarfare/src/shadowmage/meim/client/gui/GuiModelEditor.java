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
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.meim.common.config.MEIMConfig;

public class GuiModelEditor extends GuiContainerAdvanced implements IFileSelectCallback
{

private static ModelBaseAW model;

private ModelPiece piece;
private Primitive primitive;

private GuiButtonSimple load;
private GuiButtonSimple save;
private GuiButtonSimple importPiece;
private GuiButtonSimple uvMap;

private GuiButtonSimple copyPiece;
private GuiButtonSimple newPiece;
private GuiButtonSimple deletePiece;
private GuiButtonSimple changeParents;

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
  
//  leftControlPanel.updateRenderPos(-guiLeft, -guiTop);
//  leftPiecesPanel.updateRenderPos(-guiLeft, -guiTop + height - 120);
//  rightControlPanel.updateRenderPos(-guiLeft + width - 100, 0);
//  rightPrimitivesPanel.updateRenderPos(-guiLeft + width - 100, 120);
  
  load = new GuiButtonSimple(0, this, 40, 12, "Load")
    {
    @Override
    public boolean handleMousePressed(int x, int y, int num)
      {
      if(super.handleMousePressed(x, y, num))
        {
        Minecraft.getMinecraft().displayGuiScreen(new GuiFileSelect(GuiModelEditor.this, GuiModelEditor.this, MEIMConfig.getModelSaveDir(), false));
        }
      return true;
      }
    };
  rightControlPanel.elements.add(load);
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
  
  }

}
