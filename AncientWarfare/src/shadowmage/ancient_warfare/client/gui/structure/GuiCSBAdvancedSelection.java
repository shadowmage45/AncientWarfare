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
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.input.Mouse;

import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.common.container.ContainerCSB;
import shadowmage.ancient_warfare.common.item.ItemLoader;

public class GuiCSBAdvancedSelection extends   GuiContainerAdvanced
{

private GuiScreen parent;
private ContainerCSB container;

String vehicleString = "Not Forced";
String npcString = "Not Forced";
String gateString = "Not Forced";
String teamString = "Not Forced";

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
  return "/shadowmage/ancient_warfare/resources/gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  this.drawString(fontRenderer, "These settings override default", guiLeft+10, guiTop+8, 0xffffffff);
  this.drawString(fontRenderer, "template settings", guiLeft+10, guiTop+18, 0xffffffff);
  
  this.drawString(fontRenderer, "Forced Vehicle:", guiLeft+10, guiTop + 30+3, 0xffffffff);
  this.drawString(fontRenderer, "Forced NPC    :", guiLeft+10, guiTop + 50+3, 0xffffffff);
  this.drawString(fontRenderer, "Forced Gate   :", guiLeft+10, guiTop + 70+3, 0xffffffff);
  this.drawString(fontRenderer, "Forced Team   :", guiLeft+10, guiTop + 90+3, 0xffffffff);
  }

@Override
public void setupGui()
  {
  this.controlList.clear();
  this.addGuiButton(0, 256-35-10, 10, 35, 18, "Done");
  int merchWidth = 12;
  int labelWidth = 80;
  int buffer = 2;
  int leftCol = 95;
  int midCol = leftCol + merchWidth + buffer;
  int rightCol = midCol + labelWidth + buffer;
  
  this.addMerchantButton(1, leftCol, 30-1, false);
  this.addGuiButton(2, midCol, 30, 80, 16, this.vehicleString);
  this.addMerchantButton(3, rightCol, 30-1, true);
  
  this.addMerchantButton(4, leftCol, 50-1, false);
  this.addGuiButton(5, midCol, 50, 80, 16, this.npcString);
  this.addMerchantButton(6, rightCol, 50-1, true);
    
  this.addMerchantButton(7, leftCol, 70-1, false);
  this.addGuiButton(8, midCol, 70, 80, 16, this.gateString);
  this.addMerchantButton(9, rightCol, 70-1, true);
  
  this.addMerchantButton(10, leftCol, 90-1, false);
  this.addGuiButton(11, midCol, 90, 80, 16, this.teamString);
  this.addMerchantButton(12, rightCol, 90-1, true);
  }

@Override
public void updateScreenContents()
  {  
  if(forceUpdate)
    {
    forceUpdate=false;
    if(this.container.clientSettings!=null)
      {
      if(container.clientSettings.team>=0)
        {
        this.teamString = String.valueOf(container.clientSettings.team);
        }
      else
        {
        this.teamString = "Not Forced";
        }  
      }
    //TODO
    /**
     * handle the other overrides, update local display string from container
     */
    
    
    
    this.initGui();
    }
  }

boolean updateOnClose = false;
boolean forceUpdate = false;

@Override
public void buttonClicked(GuiButton button)
  {  
  switch(button.id)
  {
  case 0:
  this.switchBackToParent();
  break;
  
  case 1:
  break;
  
  case 2:
  break;
  
  case 3:
  case 4:
  case 5:
  case 6:
  case 7:
  case 8:
  case 9:
  break;
  
  
  case 10://team --
  this.adjustTeam(-1);
  updateOnClose = true;
  this.forceUpdate = true;
  break;
  
  case 11://team button  
  int buttonNum = Mouse.getEventButton();  
  if(buttonNum==1)
    {
    this.adjustTeam(-1);
    updateOnClose = true;
    this.forceUpdate = true;
    }
  else if(buttonNum==0)
    {
    this.adjustTeam(1);
    updateOnClose = true;
    this.forceUpdate = true;
    }
  break;
  
  case 12://team ++
  this.adjustTeam(1);
  updateOnClose = true;
  this.forceUpdate = true;
  break;
  
  default:
  break;
  }  
  
  }

public void adjustTeam(int adj)
  {
  container.clientSettings.team += adj;
  if(container.clientSettings.team<-1)
    {
    container.clientSettings.team=-1;
    }
  if(container.clientSettings.team>15)
    {
    container.clientSettings.team=15;
    }
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



}
