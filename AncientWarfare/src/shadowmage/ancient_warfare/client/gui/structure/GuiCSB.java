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

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.container.ContainerCSB;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.utils.StringTools;

/**
 * creative structure builder
 * @author Shadowmage
 *
 */
public class GuiCSB extends GuiContainerAdvanced
{

private final ContainerCSB container;
private final List<StructureClientInfo> clientStructures;
int currentLowestViewed = 0;
private static final int numberDisplayed = 8;
//private boolean shouldForceUpdate = false;
String currentStructure = "";



/**
 * @param par1Container
 */
public GuiCSB(ContainerCSB container)
  {
  super(container);
  this.container = container;
  if(container instanceof ContainerCSB)
    {
    clientStructures = StructureManager.instance().getClientStructures();
    }  
  else
    {
    clientStructures = null;
    }    
  if(clientStructures==null)
    {
    closeGUI();
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
  this.drawString(fontRenderer, "Structure: "+currentStructure, guiLeft + 10, guiTop + 14, 0xffffffff);
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
  if(this.container.clientSettings!=null)
    {
    if(!this.currentStructure.equals(container.clientSettings.name))
      {
      this.currentStructure = container.clientSettings.name;
      }
    } 
  }


public void setStructureName(String name)
  {
  this.currentStructure = name;
  this.container.clientSettings.name = name;
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", name);
  this.sendDataToServer(tag);
  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, 35, 18, "Done").updateRenderPos(256-35-10, 10); 
  this.addGuiButton(1, 35, 18, "Prev").updateRenderPos(10, 40+8);
  this.addGuiButton(2, 35, 18, "Next").updateRenderPos(50, 40+8);  
  this.addGuiButton(20, 85, 16, "Advanced Setup").updateRenderPos(256-85-10, 30); 
  for(int i = 0, buttonNum = 3; i< numberDisplayed; i++, buttonNum++)
    {
    structureButtons.add(this.addGuiButton(buttonNum, 120, 14, ""));
    this.structureButtons.get(this.structureButtons.size()-1).updateRenderPos(10, 60 + (20*i) +8);
    }
  }

private List<GuiButtonSimple> structureButtons = new ArrayList<GuiButtonSimple>();

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
public void onElementActivated(IGuiElement button)
  {  
  switch(button.getElementNumber())
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
    
    case 11:
    case 12:
    case 13:
    case 14:
    case 15:
    case 16:
    return;
    case 20:
    mc.displayGuiScreen(new GuiCSBAdvancedSelection((ContainerCSB)inventorySlots, this));
    return;
    }  
 
  if(button.getElementNumber()>=3 && button.getElementNumber() < 11)
    {
    int index = (this.currentLowestViewed + button.getElementNumber()) - 3;
    if(index>=this.clientStructures.size())
      { 
      return;
      }
    this.forceUpdate = true;
    this.setStructureName(this.clientStructures.get(index).name);
    }  
  }


}
