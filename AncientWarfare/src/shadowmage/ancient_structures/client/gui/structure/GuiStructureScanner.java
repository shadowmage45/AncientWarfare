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
import shadowmage.ancient_framework.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.GuiString;
import shadowmage.ancient_framework.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.ancient_structures.common.container.ContainerStructureScanner;

public class GuiStructureScanner extends GuiContainerAdvanced
{


String name = "";


GuiTextInputLine nameBox;
GuiCheckBoxSimple includeBox;

GuiScrollableArea area;

GuiCheckBoxSimple worldGenBox;
GuiCheckBoxSimple uniqueBox;
GuiCheckBoxSimple survivalBox;
GuiNumberInputLine weightLine;
GuiNumberInputLine clusterLine;
GuiNumberInputLine minDuplicateLine;
GuiNumberInputLine levelingLine;
GuiNumberInputLine fillLine;
GuiNumberInputLine borderLine;
GuiNumberInputLine borderLevelingLine;
GuiNumberInputLine borderFillLine;
GuiCheckBoxSimple levelingBox;
GuiCheckBoxSimple fillBox;
GuiCheckBoxSimple borderLevelingBox;
GuiCheckBoxSimple borderFillBox;
GuiCheckBoxSimple preserveBlocksBox;



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
  
  int w = getXSize() - 16 - 20;
  int h = 16;
  int totalHeight = 0;
  area.addGuiElement(new GuiString(5, area, w, h, "World Gen Enabled: ").updateRenderPos(0, h*0));
  area.addGuiElement(worldGenBox = (GuiCheckBoxSimple) new GuiCheckBoxSimple(6, area, 16, h).setChecked(false).updateRenderPos(160, h*0));
  area.addGuiElement(new GuiString(7, area, w, h, "Unique:").updateRenderPos(0, h*1));
  area.addGuiElement(uniqueBox = (GuiCheckBoxSimple) new GuiCheckBoxSimple(8, area, 16, h).setChecked(false).updateRenderPos(160, h*1));
  area.addGuiElement(new GuiString(9, area, w, h, "Survival Enabled:").updateRenderPos(0, h*2));
  area.addGuiElement(survivalBox = (GuiCheckBoxSimple) new GuiCheckBoxSimple(10, area, 16, h).setChecked(false).updateRenderPos(160, h*2));
  area.addGuiElement(new GuiString(11,area, w, h, "Selection Weight:").updateRenderPos(0, h*3));
  area.addGuiElement(weightLine = (GuiNumberInputLine) new GuiNumberInputLine(12, area, 40, 12, 20, "").setAsIntegerValue().setIntegerValue(1).updateRenderPos(140, h*3+1));
  area.addGuiElement(new GuiString(13,area, w, h, "Cluster Value:").updateRenderPos(0, h*4));
  area.addGuiElement(clusterLine = (GuiNumberInputLine) new GuiNumberInputLine(14, area, 40, 12, 20, "").setAsIntegerValue().setIntegerValue(1).updateRenderPos(140, h*4+1));
  area.addGuiElement(new GuiString(15,area, w, h, "Duplicate min distance:").updateRenderPos(0, h*5));
  area.addGuiElement(minDuplicateLine = (GuiNumberInputLine) new GuiNumberInputLine(16, area, 40, 12, 20, "").setAsIntegerValue().setIntegerValue(1).updateRenderPos(140, h*5+1));
  area.addGuiElement(new GuiString(17,area, w, h, "Leveling:").updateRenderPos(0, h*6));
  area.addGuiElement(levelingLine = (GuiNumberInputLine) new GuiNumberInputLine(18, area, 40, 12, 20, "").setAsIntegerValue().setIntegerValue(0).updateRenderPos(140, h*6+1));
  area.addGuiElement(new GuiString(19,area, w, h, "Edge Missing Depth:").updateRenderPos(0, h*7));
  area.addGuiElement(fillLine = (GuiNumberInputLine) new GuiNumberInputLine(20, area, 40, 12, 20, "").setAsIntegerValue().setIntegerValue(0).updateRenderPos(140, h*7+1));
  area.addGuiElement(new GuiString(21,area, w, h, "Border Size:").updateRenderPos(0, h*8));
  area.addGuiElement(borderLine = (GuiNumberInputLine) new GuiNumberInputLine(22, area, 40, 12, 20, "").setAsIntegerValue().setIntegerValue(0).updateRenderPos(140, h*8+1));
  area.addGuiElement(new GuiString(23,area, w, h, "Border Leveling:").updateRenderPos(0, h*9));
  area.addGuiElement(borderLevelingLine = (GuiNumberInputLine) new GuiNumberInputLine(24, area, 40, 12, 20, "").setAsIntegerValue().setIntegerValue(0).updateRenderPos(140, h*9+1));
  area.addGuiElement(new GuiString(25,area, w, h, "Border Edge Depth:").updateRenderPos(0, h*10));
  area.addGuiElement(borderFillLine = (GuiNumberInputLine) new GuiNumberInputLine(26, area, 40, 12, 20, "").setAsIntegerValue().setIntegerValue(0).updateRenderPos(140, h*10+1));
  area.addGuiElement(new GuiString(27, area, w, h, "Do Leveling:").updateRenderPos(0, h*11));
  area.addGuiElement(levelingBox = (GuiCheckBoxSimple) new GuiCheckBoxSimple(28, area, 16, h).setChecked(false).updateRenderPos(160, h*11));
  area.addGuiElement(new GuiString(29, area, w, h, "Do Fill Below:").updateRenderPos(0, h*12));
  area.addGuiElement(fillBox = (GuiCheckBoxSimple) new GuiCheckBoxSimple(30, area, 16, h).setChecked(false).updateRenderPos(160, h*12));
  area.addGuiElement(new GuiString(31, area, w, h, "Do Border Leveling:").updateRenderPos(0, h*13));
  area.addGuiElement(borderLevelingBox = (GuiCheckBoxSimple) new GuiCheckBoxSimple(32, area, 16, h).setChecked(false).updateRenderPos(160, h*13));
  area.addGuiElement(new GuiString(33, area, w, h, "Do Border Fill:").updateRenderPos(0, h*14));
  area.addGuiElement(borderFillBox = (GuiCheckBoxSimple) new GuiCheckBoxSimple(34, area, 16, h).setChecked(false).updateRenderPos(160, h*14));
  area.addGuiElement(new GuiString(35, area, w, h, "Preserve Blocks:").updateRenderPos(0, h*15));
  area.addGuiElement(preserveBlocksBox = (GuiCheckBoxSimple) new GuiCheckBoxSimple(36, area, 16, h).setChecked(false).updateRenderPos(160, h*15));
  
  
  area.updateTotalHeight(h*area.elements.size()/2);
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
      tag.setBoolean("world", worldGenBox.checked);
      tag.setBoolean("survival", survivalBox.checked);
      tag.setBoolean("unique", uniqueBox.checked);
      tag.setBoolean("preserveBlocks", preserveBlocksBox.checked);
      tag.setBoolean("doLeveling", levelingBox.checked);
      tag.setBoolean("doFill", fillBox.checked);
      tag.setBoolean("doBorderLeveling", borderLevelingBox.checked);
      tag.setBoolean("doBorderFill", borderFillBox.checked);
      tag.setInteger("leveling", levelingLine.getIntVal());
      tag.setInteger("fill", fillLine.getIntVal());
      tag.setInteger("border", borderLine.getIntVal());
      tag.setInteger("borderLeveling", borderLevelingLine.getIntVal());
      tag.setInteger("borderFill", borderFillLine.getIntVal());
      tag.setInteger("weight", weightLine.getIntVal());
      tag.setInteger("value", clusterLine.getIntVal());
      tag.setInteger("dupe", minDuplicateLine.getIntVal());
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
