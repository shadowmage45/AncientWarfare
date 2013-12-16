/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_structures.client.gui.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_structures.client.utils.ClientStructureComparator;
import shadowmage.ancient_structures.common.structures.StructureManager;
import shadowmage.ancient_structures.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.container.ContainerEditor;

public class GuiEditorSelect extends GuiContainerAdvanced
{

private final List<StructureClientInfo> clientStructures;

GuiScrollableArea area;
//int currentLowestViewed = 0;
//private static final int numberDisplayed = 8;
//private boolean shouldForceUpdate = false;

String currentStructure = "";
private ContainerEditor container;

String errorMessage = "";

/**
 * @param container
 */
public GuiEditorSelect(ContainerEditor container)
  {
  super(container);
  this.container = container;
  clientStructures = new ArrayList<StructureClientInfo>();
  clientStructures.addAll(StructureManager.instance().getClientStructures());
  Collections.sort(clientStructures, ClientStructureComparator.COMPARATOR);
  }

@Override
public void handleDataFromContainer(NBTTagCompound tag)
  {
  if(tag.hasKey("openEdit"))
    {
    mc.displayGuiScreen(new GuiEditor(this.container, this));
    }
  else if(tag.hasKey("badSel"))
    {
    AWLog.logError("received bad selection packet on editor select GUI(locked or invalid structure)");
    this.errorMessage = "Structure is not currently available for editing";
    }
  else if(tag.hasKey("noDel"))
    {
    AWLog.logError("received bad delete packet on editor select GUI(locked or invalid structure)");
    this.errorMessage = "Cannot delete structure, currently locked";
    }
  else if(tag.hasKey("noRem"))
    {
    AWLog.logError("received bad remove packet on editor select GUI(locked or invalid structure)");
    this.errorMessage = "Cannot remove structure, currently locked";
    }
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
  return Statics.TEXTURE_PATH+"gui/guiBackgroundLarge.png";
  }
@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  this.drawString(fontRenderer, "Structure to Edit: "+currentStructure, guiLeft + 10, guiTop + 14, 0xffffffff);
  this.fontRenderer.drawSplitString(errorMessage, guiLeft+10, guiTop+24, 190, 0xffff0000);     
  }

@Override
public void updateScreenContents()
  {
  area.updateGuiPos(guiLeft, guiTop);
  if(area.elements.size()!=StructureManager.instance().getClientStructures().size())
    {
    this.refreshGui();
    }
  }

/**
 * sets the structure to be edited, sends the name to server which will pull 
 * the actual structure and send its template to the underlying container
 * for this gui
 * @param name
 */
public void selectStructureToEdit(String name)
  {
  this.currentStructure = name;
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("setStructure", name);
  this.sendDataToServer(tag);
  }

/**
 * used when buttons are clicked to select what structure to display on top of the gui (also synchs server-side to the same)
 * @param name
 */
public void setStructureName(String name)
  {
  this.currentStructure = name;
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", name);
  this.sendDataToServer(tag);
  }


@Override
public void setupControls()
  {
  this.addGuiButton(0, 256-35-10, 10, 35, 18, "Done"); 
  this.addGuiButton(1, 10,30, 55, 18, "Delete");
  this.addGuiButton(2, 10+55+5, 30, 55, 18, "Remove");
  this.addGuiButton(3, 256-35-10, 30, 35, 18, "Edit");
  
  int totalHeight = clientStructures.size()*14;
  area = new GuiScrollableArea(4, this, 10, 50, this.getXSize()-20, this.getYSize()-60, totalHeight);
  this.guiElements.put(4, area);
  
  this.refreshGui();
  
  }

@Override
public void updateControls()
  {
  int kX = 5;
  int kY = 0;
  int buttonHeight = 12;
  int buffer = 2;
  
  this.area.elements.clear();
  for(int i = 0; i < clientStructures.size(); i++)
    {
    String name = this.clientStructures.get(i).name;
    kY = i * (buffer+buttonHeight);
    area.addGuiElement(new GuiButtonSimple(i+20, area, this.getXSize()-20-16-10, buttonHeight, name).updateRenderPos(kX, kY));
    } 
  }

protected void tryRemoveSelection()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("remove", currentStructure);
  this.sendDataToServer(tag);
  }

protected void tryDeleteSelection()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("delete", currentStructure);
  this.sendDataToServer(tag);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  this.errorMessage = "";
  switch(element.getElementNumber())
  {
  case 0:
  closeGUI();
  return;
 
  case 1://delete
  this.tryDeleteSelection();
  return;
  
  case 2://remove
  this.tryRemoveSelection();
  return;
  
  case 3:
  if(StructureManager.instance().getClientStructure(currentStructure)!=null)
    {
    this.selectStructureToEdit(currentStructure);    
    }
  return;
  }
  
  if(element.getElementNumber()>=20)
    {
    int index = element.getElementNumber()-20;
    if(index>=this.clientStructures.size() || index <0)
      { 
      return;
      }
    this.forceUpdate = true;
    this.setStructureName(this.clientStructures.get(index).name);
    }  
  }

}
