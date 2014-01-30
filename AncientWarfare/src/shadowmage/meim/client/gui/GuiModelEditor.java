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
import shadowmage.ancient_framework.client.gui.elements.GuiString;
import shadowmage.ancient_framework.client.gui.elements.IFileSelectCallback;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.client.model.ModelBaseAW;
import shadowmage.ancient_framework.client.model.ModelPiece;
import shadowmage.ancient_framework.client.model.Primitive;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.meim.common.config.MEIMConfig;

public class GuiModelEditor extends GuiContainerAdvanced implements IFileSelectCallback
{

static final int SELECT_LOAD = 0;
static final int SELECT_SAVE = 1;
static final int SELECT_IMPORT_PIECE = 2;
static final int SELECT_LOAD_TEXTURE = 3;
private static final int SELECT_SAVE_TEXTURE = 4;
private static final int SELECT_EXPORT_UV_MAP = 5;

static ModelBaseAW model;

int selectionMode = -1;

ModelPiece selectedPiece;
Primitive selectedPrimitive;

private GuiModelEditorSetup setup;

public GuiModelEditor(ContainerBase container)
  {
  super(container);
  if(model==null)
    {
    initModel();
    }
  this.shouldCloseOnVanillaKeys = true;
  this.setup = new GuiModelEditorSetup(this);
  }

public void initModel()
  {
  /**
   * TODO create new model, add default box
   */
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  //element actions handled by anonymous element classes implementation in GuiModelEditorSetup
  //this.refreshGui();
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
  this.setup.updateScreenContents();
  }

@Override
public void setupControls()
  {
  this.setup.setupControls();//all controls are maintained in the setup class
  }

@Override
public void updateControls()
  {
  setup.updateControls(guiLeft, guiTop, width, height);//all controls are maintained in the setup class    
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
