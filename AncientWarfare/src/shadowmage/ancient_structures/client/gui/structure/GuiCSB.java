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
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.ancient_structures.client.utils.ClientStructureComparator;
import shadowmage.ancient_structures.common.container.ContainerCSB;
import shadowmage.ancient_structures.common.structures.StructureManager;
import shadowmage.ancient_structures.common.structures.data.StructureClientInfo;

/**
 * creative structure builder
 * @author Shadowmage
 *
 */
public class GuiCSB extends GuiContainerAdvanced
{

private ContainerCSB container;
private List<StructureClientInfo> clientStructures;

String currentStructure = "";



/**
 * @param par1Container
 */
public GuiCSB(ContainerBase container)
  {
  super(container);
  this.container = (ContainerCSB) container;
  clientStructures = new ArrayList<StructureClientInfo>();
  clientStructures.addAll(StructureManager.instance().getClientStructures());
  Collections.sort(clientStructures, ClientStructureComparator.COMPARATOR);
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
  this.drawString(fontRenderer, "Structure: "+currentStructure, guiLeft + 10, guiTop + 14, 0xffffffff); 
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
  area.updateGuiPos(guiLeft, guiTop);
  }


public void setStructureName(String name)
  {
  this.currentStructure = name;
  this.container.clientSettings.name = name;
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", name);
  this.sendDataToServer(tag);
  }

GuiScrollableArea area;

@Override
public void setupControls()
  {
  this.addGuiButton(0, 35, 18, "Done").updateRenderPos(256-35-10, 10); 
  GuiButtonSimple button = (GuiButtonSimple)this.addGuiButton(3, 85, 16, "Advanced Setup").updateRenderPos(256-85-10, 30);
  button.renderTooltip = true;
  button.addToToolitp("Access advanced controls for spawning");
  button.addToToolitp("vehicles/npcs/gates, and team overrides");
  
  int totalHeight = clientStructures.size()*14;
  area = new GuiScrollableArea(4, this, 10, 50, this.getXSize()-20, this.getYSize()-60, totalHeight);
  this.guiElements.put(4, area);
  
  int kX = 5;
  int kY = 0;
  int buttonHeight = 12;
  int buffer = 2;
  
  for(int i = 0; i < clientStructures.size(); i++)
    {
    String name = this.clientStructures.get(i).name;
    kY = i * (buffer+buttonHeight);
    area.addGuiElement(new GuiButtonSimple(i+20, area, this.getXSize()-20-16-10, buttonHeight, name).updateRenderPos(kX, kY));
    } 
  }



@Override
public void updateControls()
  {

  }

@Override
public void onElementActivated(IGuiElement button)
  {  
  switch(button.getElementNumber())
    {
    case 0:
    closeGUI();
    return;
  
    case 3:
    mc.displayGuiScreen(new GuiCSBAdvancedSelection((ContainerCSB)inventorySlots, this));
    return;   
    }  
 
  if(button.getElementNumber()>=20)
    {
    int index = button.getElementNumber()-20;
    if(index>=this.clientStructures.size() || index <0)
      { 
      return;
      }
    this.forceUpdate = true;
    this.setStructureName(this.clientStructures.get(index).name);
    }  
  }


}
