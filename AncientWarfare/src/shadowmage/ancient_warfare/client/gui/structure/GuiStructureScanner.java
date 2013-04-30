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

import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiCheckBoxSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerEditor;
import shadowmage.ancient_warfare.common.container.ContainerStructureScanner;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class GuiStructureScanner extends GuiContainerAdvanced
{

boolean formatAW = true;
boolean include = false;
boolean formatRuins;
boolean worldGen;
boolean survival;
String name = "";
String weightString = "";
String valueString = "";
boolean unique;


GuiCheckBoxSimple formatAWBox;
GuiCheckBoxSimple includeBox;
GuiCheckBoxSimple formatRuinsBox;
GuiCheckBoxSimple worldGenBox;
GuiCheckBoxSimple survivalBox;
GuiCheckBoxSimple uniqueBox;

GuiTextInputLine weight;
GuiTextInputLine value;

GuiTextInputLine nameBox;

/**
 * @param container
 */
public GuiStructureScanner(Container container)
  {
  super(container);
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
  return Config.texturePath+"gui/guiBackgroundLarge.png";
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
  this.drawString(fontRenderer, "Export to AW Format    : ", guiLeft+10, guiTop+53, 0xffffffff);
  this.drawString(fontRenderer, "Add to game immediately: ", guiLeft+10, guiTop+73, 0xffffffff);  
  this.drawString(fontRenderer, "Export to Ruins Format : ", guiLeft+10, guiTop+93, 0xffffffff);
  this.drawString(fontRenderer, "Include for World-Gen  : ", guiLeft+10, guiTop+113, 0xffffffff);  
  this.drawString(fontRenderer, "Include for Survival   : ", guiLeft+10, guiTop+133, 0xffffffff);  
  if(worldGenBox.checked())
    {
    this.drawString(fontRenderer, "World-Gen Weight       : ", guiLeft+10, guiTop+153, 0xffffffff);
    this.drawString(fontRenderer, "World-Gen Value        : ", guiLeft+10, guiTop+173, 0xffffffff);
    this.drawString(fontRenderer, "World-Gen Unique?      : ", guiLeft+10, guiTop+193, 0xffffffff);
    }  
  }

@Override
public void updateScreenContents()
  {
  this.name = nameBox.getText();
  this.weightString = this.weight.getText();  
  this.valueString = this.value.getText();  
  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, 35, 18, "Done").updateRenderPos(256-35-10, 10); 
  this.addGuiButton(1, 45, 18, "Export").updateRenderPos(256-45-10, 30);
  this.addGuiButton(8, 45, 18, "Reset").updateRenderPos(256-45-10, 50);
  this.addGuiButton(9, 45, 18, "Edit").updateRenderPos(256-45-10, 70);
  formatAWBox = this.addCheckBox(2, 16, 16).setChecked(formatAW);
  formatAWBox.updateRenderPos(145, 50);
  includeBox = this.addCheckBox(3, 16, 16).setChecked(include);
  includeBox.updateRenderPos(145, 70);
  formatRuinsBox = this.addCheckBox(4, 16, 16).setChecked(formatRuins);
  formatRuinsBox.updateRenderPos(145, 90);
  worldGenBox = this.addCheckBox(5,16, 16).setChecked(worldGen);
  worldGenBox.updateRenderPos(145, 110);
  survivalBox = this.addCheckBox(7, 145, 130, 16, 16).setChecked(survival);
  uniqueBox = this.addCheckBox(10, 145, 190, 16, 16).setChecked(unique);  
  
  nameBox = this.addTextField(11, 10, 30, 120, 10, 30, name);  
  weight = this.addTextField(12, 145, 150, 40, 10, 3, weightString);
  value = this.addTextField(13, 145, 170, 40, 10, 3, valueString);
  
  if(worldGenBox.checked())
    {
    weight.hidden = false;
    value.hidden = false;;
    uniqueBox.hidden = false;
    }
  else
    {
    uniqueBox.hidden = true;
    weight.hidden = true;
    value.hidden = true;
    }
  }

@Override
public void updateControls()
  {
  if(worldGenBox.checked())
    {
    weight.hidden = false;
    value.hidden = false;;
    uniqueBox.hidden = false;
    }
  else
    {
    uniqueBox.hidden = true;
    weight.hidden = true;
    value.hidden = true;
    }
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  ContainerStructureScanner container =null;
  switch(element.getElementNumber())
  {
  case 0:
  closeGUI();
  break;
  
  case 1: 
  container = (ContainerStructureScanner)this.inventorySlots;
  if(container!=null && !name.equals(""))
    {
    int weight = StringTools.safeParseInt(this.weight.getText());
    int value = StringTools.safeParseInt(this.value.getText());
    container.sendSettingsAndExport(name, worldGen, survival, formatRuins, formatAW, include, weight, value, unique, false); 
    //container.sendSettingsAndExport(name, worldGen, survival, formatRuins, formatAW, include);
    } 
  closeGUI();
  break;
  
  case 2:
  break;
  
  case 8://clearData
  NBTTagCompound tag = new NBTTagCompound();
  tag.setBoolean("clearItem", true);
  this.sendDataToServer(tag);
  closeGUI();
  break;
  
  case 9://manual edit and then save...  
  NBTTagCompound editTag = new NBTTagCompound();
  container = (ContainerStructureScanner)this.inventorySlots;
  if(container!=null && !name.equals(""))
    {
    int weight = StringTools.safeParseInt(this.weight.getText());
    int value = StringTools.safeParseInt(this.value.getText());
    container.sendSettingsAndExport(name, worldGen, survival, formatRuins, formatAW, include, weight, value, unique, true);     
    player.openContainer = new ContainerEditor(player);
    mc.displayGuiScreen(new GuiEditor((ContainerEditor)player.openContainer, null));
    } 
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
  this.survival = survivalBox.checked();
  this.unique = uniqueBox.checked();
  this.name = nameBox.getText();
  this.valueString = value.getText();
  this.weightString = weight.getText();
  if(worldGenBox.checked())
    {
    weight.hidden = false;
    value.hidden = false;;
    uniqueBox.hidden = false;
    }
  else
    {
    uniqueBox.hidden = true;
    weight.hidden = true;
    value.hidden = true;
    }
  }


}
