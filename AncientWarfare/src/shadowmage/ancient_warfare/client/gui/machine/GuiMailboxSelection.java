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
package shadowmage.ancient_warfare.client.gui.machine;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiElement;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.GuiString;
import shadowmage.ancient_framework.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.container.ContainerMailboxBase;

public class GuiMailboxSelection extends GuiContainerAdvanced
{


ContainerMailboxBase container;
GuiMailbox parent;
GuiScrollableArea area;//8,40,160,93 (x,y,w,h)
GuiButtonSimple addButton;//8,24 (x,y)
GuiButtonSimple removeButton;//61,24 (x,y)
GuiButtonSimple selectButton;//123,8
GuiButtonSimple cancelButton;//123,24
GuiTextInputLine inputBox;//8,8

int sideSelection;

/**
 * @param container
 */
public GuiMailboxSelection(GuiMailbox parent, int side)
  {
  super(parent.inventorySlots);
  this.parent = parent;
  this.sideSelection = side;
  this.container = parent.container;
//  this.shouldCloseOnVanillaKeys = true;
  }

@Override
protected void keyTyped(char par1, int par2)
  {
  if(!this.inputBox.selected)
    {
    super.keyTyped(par1, par2);
    }
  else
    {
    for(Integer i : this.guiElements.keySet())
      {
      GuiElement el = this.guiElements.get(i);
      el.onKeyTyped(par1, par2);
      }
    }
  }

@Override
public int getXSize()
  {
  return 176;
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
  }

@Override
public void updateScreenContents()
  {
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==this.addButton)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("add", this.inputBox.getText());    
    this.sendDataToServer(tag);
    }
  else if(element == this.removeButton)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("remove", this.inputBox.getText());    
    this.sendDataToServer(tag);
    }
  else if(element == this.selectButton)
    {
    Config.logDebug("sending select packet!!");
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("select", this.inputBox.getText());
    tag.setInteger("box", this.sideSelection);
    this.sendDataToServer(tag);
    }
  else if(element == this.cancelButton)
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("addSlots", true);
    this.sendDataToServer(tag);
    this.container.addSlots();
    mc.displayGuiScreen(parent);     
    }    
  else if(this.boxNames.contains(element))
    {
    GuiString string = (GuiString)element;
    this.inputBox.setText(string.text);
    }
  }

@Override
protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4)
  {
  //NOOP -- no slot interaction for this GUI
//  if (par1Slot != null)
//    {
//    par2 = par1Slot.slotNumber;
//    }
//  this.mc.playerController.windowClick(this.inventorySlots.windowId, par2, par3, par4, this.mc.thePlayer);
  }

@Override
public void handleDataFromContainer(NBTTagCompound tag)
  {  
  super.handleDataFromContainer(tag);
  
  if(tag.hasKey("accept"))
    {
    this.container.addSlots();
    this.closeGUI();
    this.container.te.openGui(player);
    parent.refreshGui();
    }
  else if(tag.hasKey("reject"))
    {
    String error = tag.getString("reject");
    Config.logDebug("receiving reject message: "+error);
    parent.refreshGui();
    /**
     * TODO add error GUI
     */
    }
  }

@Override
public void setupControls()
  {
  this.area = new GuiScrollableArea(0, this, 8, 40, 160, 93*2, 0);
  this.guiElements.put(0, area);
  this.inputBox = this.addTextField(1, 8, 8, 98, 12, 20, "");
  this.addButton = this.addGuiButton(2, 8, 24, 45, 12, "Add");
  this.removeButton = this.addGuiButton(3, 61, 24, 45, 12, "Remove");
  this.selectButton = this.addGuiButton(4, 123, 8, 45, 12, "Select");
  this.cancelButton = this.addGuiButton(5, 123, 24, 45, 12, "Cancel");

  
  this.updateControls();
  }

@Override
public void updateControls()
  {
  area.elements.clear();
  int y = 0;
  int element = 6;
  for(String name : container.boxNames)
    {
    GuiString string = new GuiString(element, area, 90, 10, name);
    area.elements.add(string);
    boxNames.add(string);
    string.clickable = true;
    string.updateRenderPos(0, y);
    y+=10;
    element++;
    }
  area.updateTotalHeight(y);
  }

Set<GuiString> boxNames = new HashSet<GuiString>();

}
