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

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiCheckBoxSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.ancient_structures.common.container.ContainerStructureScanner;

public class GuiStructureScanner extends GuiContainerAdvanced
{


String name = "";

GuiCheckBoxSimple includeBox;
GuiTextInputLine nameBox;

GuiScrollableArea area;

private ContainerStructureScanner container;

/**
 * @param container
 */
public GuiStructureScanner(ContainerBase container)
  {
  super(container);
  this.container = (ContainerStructureScanner)container;
  this.shouldCloseOnVanillaKeys = false;
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
  this.drawStringGui("Structure Name: ", 8, 8, 0xffffffff);  
  this.drawString(fontRenderer, "Add to game immediately: ", guiLeft+8, guiTop+38, 0xffffffff);  
  this.drawStringGui("Validation Settings: ", 8, 55, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  this.name = nameBox.getText();
  area.updateGuiPos(guiLeft, guiTop);
  }

@Override
public void setupControls()
  {
  this.guiElements.clear();
  this.addGuiButton(0, 35, 18, "Done").updateRenderPos(256-35-10, 10); 
  this.addGuiButton(1, 45, 18, "Export").updateRenderPos(256-45-10, 30);
  this.area = new GuiScrollableArea(2, this, 8, 70, getXSize()-16, getYSize() - 78, 8);
  
  
  this.guiElements.put(2, area);
  this.addGuiButton(3, 45, 18, "Reset").updateRenderPos(256-45-10, 50);
    
  nameBox = this.addTextField(4, 8, 20, 120, 10, 30, name);
  
  includeBox = (GuiCheckBoxSimple) this.addCheckBox(5, 16, 16).setChecked(true).updateRenderPos(145, 35);
 
  }

@Override
public void updateControls()
  {

  }

@Override
public void onElementActivated(IGuiElement element)
  {
  ContainerStructureScanner container =null;
  switch(element.getElementNumber())
  {
  case 0://done
  closeGUI();
  break;
  
  case 1://export
    {
    if(!name.equals(""))
      {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setBoolean("export", includeBox.checked);
      tag.setString("name", name);
      this.sendDataToServer(tag);
      closeGUI();      
      }    
    }
  break;
    
  case 3://clearData
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("reset", true);
    this.sendDataToServer(tag);
    closeGUI();  
    }
  break;
    
  case 4://text field, validate text
    {
    this.nameBox.setText(this.validateString(this.nameBox.getText()));
    }  
  break;    
  }  
  this.name = nameBox.getText(); 
  }

protected String validateString(String input)
  {
  String scrubbed = "";
  for(int i = 0; i < input.length(); i++)
    {
    char ch = input.charAt(i);
    if(isValidChar(ch))
      {
      scrubbed = scrubbed + ch;
      }
    }  
  return scrubbed;
  }

protected boolean isValidChar(char ch)
  {
  /**
   *  public static final char[] allowedCharactersArray = new char[] {'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};
   */
  switch(ch)
  {
  case '/':
  case '\\':
  case '\n':
  case '\r':
  case '\"':
  case '\'':
  case '`':
  case '\t':
  case '\f':
  case '?':
  case '*':
  case '<':
  case '>':
  case '(':
  case ')':
  case '|':
  case ':':
  case '{':
  case '}':
  case '[':
  case ']':
  return false;
  default:
  return true;
  }
  }

}
