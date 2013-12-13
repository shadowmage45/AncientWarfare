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

import net.minecraft.client.gui.GuiScreen;
import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiCheckBoxSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.container.ContainerCSB;

/**
 * advanced structure overrides (manually override 
 * @author Shadowmage
 *
 */
public class GuiCSBAdvancedSelection extends   GuiContainerAdvanced
{

private GuiScreen parent;
private ContainerCSB container;

/**
 * @param container
 */
public GuiCSBAdvancedSelection(ContainerCSB container, GuiScreen parent)
  {
  super(container);
  this.parent = parent;
  this.container = container;
  this.forceUpdate = true;
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
  this.drawString(fontRenderer, "These settings override default", guiLeft+10, guiTop+8, 0xffffffff);
  this.drawString(fontRenderer, "template settings", guiLeft+10, guiTop+18, 0xffffffff);  
  this.drawString(fontRenderer, "Spawn Vehicles", guiLeft+30, guiTop + 30+4, 0xffffffff);
  this.drawString(fontRenderer, "Spawn NPCs", guiLeft+30, guiTop + 50+4, 0xffffffff);
  this.drawString(fontRenderer, "Spawn Gates", guiLeft+30, guiTop + 70+4, 0xffffffff);
  this.drawString(fontRenderer, "Forced Team", guiLeft+10, guiTop + 90+3, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {    
  teamSelect.setValue((int)container.clientSettings.teamOverride);
  }

boolean updateOnClose = false;

public void setTeamNum(int num)
  {
  if(num>=-1 && num<=15)
    {
    container.clientSettings.teamOverride = num;    
    }
  }

private void setVehicle(boolean val)
  {
  this.container.clientSettings.spawnVehicle = val;
  }

private void setNpc(boolean val)
  {
  this.container.clientSettings.spawnNpc = val;
  }

private void setGate(boolean val)
  {
  this.container.clientSettings.spawnGate = val;
  }

public void switchBackToParent()
  {
  if(this.updateOnClose)
    {
    this.container.updateServerContainer();
    }
  mc.displayGuiScreen(parent);
  }

@Override
protected void keyTyped(char par1, int par2)
  {
  if (par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.keyCode)
    {
    this.switchBackToParent();
    }
  else
    {
    super.keyTyped(par1, par2);
    }
  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, 256-35-10, 10, 35, 18, "Done");
  int merchWidth = 12;
  int labelWidth = 80;
  int buffer = 2;
  int leftCol = 95;
  int midCol = leftCol + merchWidth + buffer;
  int rightCol = midCol + labelWidth + buffer;
    
  vehicles = this.addCheckBox(1, 10, 30, 16, 16).setChecked(this.container.clientSettings.spawnVehicle);
  npcs = this.addCheckBox(2, 10, 50, 16, 16).setChecked(this.container.clientSettings.spawnNpc);
  gates = this.addCheckBox(3, 10, 70, 16, 16).setChecked(this.container.clientSettings.spawnGate);
  
  this.addGuiButton(10, leftCol, 90, 12, 16, "<");
  this.teamSelect = this.addNumberField(11, 80, 16, 2, String.valueOf(this.container.clientSettings.teamOverride));
  this.addGuiButton(12, rightCol, 90, 12, 16, ">");
  this.teamSelect.setAsIntegerValue().setMinMax(-1, 15).updateRenderPos(midCol, 90);  
  }

GuiCheckBoxSimple vehicles;
GuiCheckBoxSimple npcs;
GuiCheckBoxSimple gates;
GuiNumberInputLine teamSelect;

@Override
public void updateControls()
  {  
    
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  switch(element.getElementNumber())
  {
  case 0:
  this.switchBackToParent();
  break;
  
  case 1:
  this.setVehicle(vehicles.checked());
  updateOnClose = true;
  break;
  
  case 2:
  this.setNpc(npcs.checked());
  updateOnClose = true;
  break;
  
  case 3:
  this.setGate(gates.checked());
  updateOnClose = true;
  break;
  
  case 4:
  case 5:
  case 6:
  case 7:
  case 8:
  case 9:
  break;
  
  
  case 10://team --
  this.setTeamNum(container.clientSettings.teamOverride-1);
  updateOnClose = true;
  this.forceUpdate = true;
  break;
  
  case 11://team button  
  this.setTeamNum(this.teamSelect.getIntVal());
  updateOnClose = true;
  this.forceUpdate = true;
  break;
  
  case 12://team ++
  this.setTeamNum(container.clientSettings.teamOverride+1);
  updateOnClose = true;
  this.forceUpdate = true;
  break;
  
  default:
  break;
  }  
  }



}
