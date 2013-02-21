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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerEditor;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class GuiEditorSelect extends GuiContainerAdvanced
{

private final List<StructureClientInfo> clientStructures;
int currentLowestViewed = 0;
private static final int numberDisplayed = 8;
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
  clientStructures = StructureManager.instance().getClientStructures();
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
    Config.logDebug("received bad sel packet on editor select GUI");
    this.errorMessage = "Structure is not currently available for editing";
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
  return "/shadowmage/ancient_warfare/resources/gui/guiBackgroundLarge.png";
  }
@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  this.drawString(fontRenderer, "Structure to Edit: "+currentStructure, guiLeft + 10, guiTop + 14, 0xffffffff);
  this.fontRenderer.drawSplitString(errorMessage, guiLeft+10, guiTop+24, 190, 0xffff0000);
  //this.drawString(fontRenderer, errorMessage, guiLeft+10, guiTop+24, 0xffff0000);
  
  this.drawString(fontRenderer, "Wid", guiLeft + 190, guiTop + 46+8, 0xffffffff);
  this.drawString(fontRenderer, "Len", guiLeft + 210, guiTop + 46+8, 0xffffffff);
  this.drawString(fontRenderer, "Hig", guiLeft + 230, guiTop + 46+8, 0xffffffff);

  for(int i = 0; i+currentLowestViewed < clientStructures.size() && i < numberDisplayed; i++)
    {
    this.drawString(fontRenderer, String.valueOf(clientStructures.get(i+currentLowestViewed).xSize), guiLeft + 190, guiTop + 20 * i + 64+8, 0xffffffff);
    this.drawString(fontRenderer, String.valueOf(clientStructures.get(i+currentLowestViewed).zSize), guiLeft + 210, guiTop + 20 * i + 64+8, 0xffffffff);
    this.drawString(fontRenderer, String.valueOf(clientStructures.get(i+currentLowestViewed).ySize), guiLeft + 230, guiTop + 20 * i + 64+8, 0xffffffff);
    }  
  }

@Override
public void updateScreenContents()
  {
 
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

private List<GuiButtonSimple> structureButtons = new ArrayList<GuiButtonSimple>();

@Override
public void setupControls()
  {
  this.addGuiButton(0, 256-35-10, 10, 35, 18, "Done"); 
  this.addGuiButton(1, 10, 40+8, 35, 18, "Prev");
  this.addGuiButton(2, 50, 40+8, 35, 18, "Next");

  this.addGuiButton(20, 256-35-10, 30, 35, 18, "Edit");
  
  for(int i = 0, buttonNum = 3; i< numberDisplayed; i++, buttonNum++)
    {
    structureButtons.add(this.addGuiButton(buttonNum, 10, 60 + (20*i) +8, 120, 14, ""));
    }

//  for(int i = 0, buttonNum = 3; i+currentLowestViewed < clientStructures.size() && i < numberDisplayed; i++, buttonNum++)
//    {
//    this.addGuiButton(buttonNum, 10, 60 + (20*i) +8, 120, 14, StringTools.subStringBeginning(clientStructures.get(this.currentLowestViewed + i).name, 14));
//    } 
  }

@Override
public void updateControls()
  {
  for(int i = 0; i <numberDisplayed; i++)
    {
    GuiButtonSimple button = this.structureButtons.get(i);
    if(i+currentLowestViewed < clientStructures.size())
      {
      button.enabled = true;
      button.hidden = false;
      button.setButtonText(StringTools.subStringBeginning(clientStructures.get(this.currentLowestViewed + i).name, 14));
      }
    else
      {
      button.enabled = false;
      button.hidden = true;
      }
    } 
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  switch(element.getElementNumber())
  {
  case 0:
  closeGUI();
  return;

  case 1:
  if(this.currentLowestViewed-8 >=0)
    {
    this.currentLowestViewed-=8;
    forceUpdate = true;
    }
  return;

  case 2:
  if(this.currentLowestViewed+8 < this.clientStructures.size())
    {
    this.currentLowestViewed+=8;
    forceUpdate = true;   
    }
  return;

  case 20:
  if(StructureManager.instance().getClientStructure(currentStructure)!=null)
    {
    this.selectStructureToEdit(currentStructure);    
    }
  return;
  }
  this.errorMessage = "";
  if(element.getElementNumber()>=3 && element.getElementNumber() < 11)
    {
    int index = (this.currentLowestViewed + element.getElementNumber()) - 3;
    if(index>=this.clientStructures.size())
      {
      return;
      }
    forceUpdate = true;
    this.setStructureName(this.clientStructures.get(index).name);
    }
  }

}
