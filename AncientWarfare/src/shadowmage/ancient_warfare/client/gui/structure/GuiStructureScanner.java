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

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.inventory.Container;
import shadowmage.ancient_warfare.client.gui.GuiCheckBox;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerStructureScanner;

public class GuiStructureScanner extends GuiContainerAdvanced
{

boolean formatAW = true;
boolean include = false;
boolean formatRuins;
boolean worldGen;
boolean survival;
boolean creative;
String name = "";


GuiCheckBox formatAWBox;
GuiCheckBox includeBox;
GuiCheckBox formatRuinsBox;
GuiCheckBox worldGenBox;
GuiCheckBox creativeBox;
GuiCheckBox survivalBox;

GuiTextField nameBox;

/**
 * @param container
 */
public GuiStructureScanner(Container container)
  {
  super(container);
  // TODO Auto-generated constructor stub
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
  ContainerStructureScanner container = (ContainerStructureScanner)this.inventorySlots;
  if(container!=null)
    {
    this.drawString(fontRenderer, "Scanned structure size: ", guiLeft+10, guiTop+10, 0xffffffff);
    this.drawString(fontRenderer, "Width: "+String.valueOf(container.xSize)+" Length: "+String.valueOf(container.zSize)+ " Height: " + String.valueOf(container.ySize), guiLeft+10, guiTop+20, 0xffffffff);
    }
  else
    {
    Config.logError("IMPROPER CONTAINER DETECTED IN SCANNING GUI");
    closeGUI();
    }
  this.nameBox.drawTextBox();
  this.drawString(fontRenderer, "Export to AW Format   : ", guiLeft+10, guiTop+53, 0xffffffff);
  this.drawString(fontRenderer, "Include after export  : ", guiLeft+10, guiTop+73, 0xffffffff);  
  this.drawString(fontRenderer, "Export to Ruins Format: ", guiLeft+10, guiTop+93, 0xffffffff);
  this.drawString(fontRenderer, "Flag for World Gen    : ", guiLeft+10, guiTop+113, 0xffffffff);
  this.drawString(fontRenderer, "Flag for Creative Mode: ", guiLeft+10, guiTop+133, 0xffffffff);
  this.drawString(fontRenderer, "Flag for Survival Mode: ", guiLeft+10, guiTop+153, 0xffffffff);  
  }

@Override
public void setupGui()
  {
  this.controlList.clear();
  this.addGuiButton(0, 256-35-10, 10, 35, 18, "Done"); 
  this.addGuiButton(1, 256-45-10, 30, 45, 18, "Export");
  formatAWBox = this.addCheckBox(2, 145, 50, 16, 16).setChecked(formatAW);
  includeBox = this.addCheckBox(3, 145, 70, 16, 16).setChecked(include);
  formatRuinsBox = this.addCheckBox(4, 145, 90, 16, 16).setChecked(formatRuins);
  worldGenBox = this.addCheckBox(5, 145, 110, 16, 16).setChecked(worldGen);
  creativeBox = this.addCheckBox(6, 145, 130, 16, 16).setChecked(creative);
  survivalBox = this.addCheckBox(7, 145, 150, 16, 16).setChecked(survival);
    
  nameBox = new GuiTextField(fontRenderer, guiLeft+10, guiTop+30, 120, 10);
  nameBox.setTextColor(-1);
  nameBox.func_82266_h(-1);
  nameBox.setEnableBackgroundDrawing(true);
  nameBox.setMaxStringLength(30);
  nameBox.setText(name);
  
  }

@Override
public void updateScreenContents()
  {
  if(nameBox!=null)
    {
    this.name = nameBox.getText();
    }
  }

@Override
public void buttonClicked(GuiButton button)
  {
  switch(button.id)
  {
  case 0:
  closeGUI();
  break;
  
  case 1: 
  ContainerStructureScanner container = (ContainerStructureScanner)this.inventorySlots;
  if(container!=null && !name.equals(""))
    {
    container.sendSettingsAndExport(name, worldGen, creative, survival, formatRuins, formatAW, include);
    } 
  closeGUI();
  break;
  
  case 2:
  break;
  
  case 11:
  case 12:
  case 13:
  case 14:
  case 15:
  case 16:
  
  default:
  break;
  }
  this.formatAW = formatAWBox.checked();
  this.include = includeBox.checked();
  this.formatRuins = formatRuinsBox.checked();
  this.worldGen = worldGenBox.checked();
  this.creative = creativeBox.checked();
  this.survival = survivalBox.checked();
  this.name = nameBox.getText();
  }



/**
 * Called when the mouse is clicked.
 */
@Override
protected void mouseClicked(int par1, int par2, int par3)
  {
  super.mouseClicked(par1, par2, par3);
  this.nameBox.mouseClicked(par1, par2, par3);
  }

/**
 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
 */
@Override
protected void keyTyped(char par1, int par2)
  {
  if (this.nameBox.textboxKeyTyped(par1, par2))
    {
    
    }
  else
    {
    super.keyTyped(par1, par2);
    }
  }


}
