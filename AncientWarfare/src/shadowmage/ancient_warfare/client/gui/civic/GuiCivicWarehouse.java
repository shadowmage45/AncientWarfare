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
package shadowmage.ancient_warfare.client.gui.civic;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.civics.TECivicWarehouse;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerCivicWarehouse;

public class GuiCivicWarehouse extends GuiContainerAdvanced
{

int pageNum = 0;
int elementsPerPage = 9*4;

TECivicWarehouse te;
ContainerCivicWarehouse container;

/**
 * @param container
 */
public GuiCivicWarehouse(Container container, TECivicWarehouse te)
  {
  super(container);
  this.container = (ContainerCivicWarehouse) container;
  this.te = te;
  this.shouldCloseOnVanillaKeys = true;
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
  int index;
  ItemStack stack;
  
  
  int adjX = mouseX-guiLeft - 7;
  int adjY = mouseY-guiTop - 7;
  
  boolean mouseOver = false;
  int mouseIndex = -1;
  if(adjX >= 0 && adjX < 9 * 18 && adjY>=0 && adjY< 4*18)
    {
    int xIndex = adjX/18;
    int yIndex = adjY/18;
    int totalIndex = yIndex*9 + xIndex;
    mouseOver = true;
    mouseIndex = totalIndex;   
    }
  
  boolean renderMouseItem = false;
  int x;
  int y;
  int mx = 0;
  int my = 0;
  for(int i = 0; i < this.elementsPerPage; i++)
    {
    x = (i%9)*18;
    y = (i/9)*18;
    index = i + (this.pageNum*this.elementsPerPage);    
    if(index<this.container.warehouseItems.size())
      {
      if(index == mouseIndex)
        {
        renderMouseItem = true;
        mx = x;
        my = y;
        continue;
        }
      stack = this.container.warehouseItems.get(index).stack;
      this.renderItemStack(stack, guiLeft+x+8, guiTop+y+8, mouseX, mouseY, true);
      }
    }
  if(renderMouseItem)
    {
    stack = this.container.warehouseItems.get(mouseIndex).stack;
    this.renderItemStack(stack, guiLeft+mx+8, guiTop+my+8, mouseX, mouseY, true);
    }
  
  int centerX = guiLeft+this.getXSize()/2;
  int buttonY = guiTop + 8 + 4*18 + 2;
  int textTop = buttonY + 16;
  String pageString = "Page "+(this.pageNum+1) +"/"+ ((this.container.warehouseItems.size()/this.elementsPerPage)+1);
  this.drawCenteredString(getFontRenderer(), pageString, centerX, buttonY+2, 0xffffffff);
  this.drawCenteredString(this.getFontRenderer(), "Size:", centerX, textTop, 0xffffffff);
  this.drawCenteredString(this.getFontRenderer(), String.valueOf(this.te.getSizeInventory()), centerX, textTop+10, 0xffffffff);
  this.drawCenteredString(this.getFontRenderer(), "Filled:", centerX, textTop+20, 0xffffffff);
  this.drawCenteredString(this.getFontRenderer(), String.valueOf(this.container.filledSlotCount), centerX, textTop+30, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  switch(element.getElementNumber())
  {
  case 0://prev
  if(this.pageNum-1>=0)
    {
    this.pageNum--;
    }
  break;
  
  case 1://next
  if(this.pageNum+1 <= this.container.warehouseItems.size()/this.elementsPerPage)
    {
    this.pageNum++;
    }
  break;
  }
  }

@Override
public void setupControls()
  {
  int buttonRowY = 8 + 4*18 + 2;
  this.addGuiButton(0, 8, buttonRowY, 30, 12, "Prev");
  this.addGuiButton(1, this.getXSize()-8-30, buttonRowY, 30, 12, "Next");
  }



@Override
protected void mouseClicked(int par1, int par2, int par3)
  {
  int adjX = par1-guiLeft - 7;
  int adjY = par2-guiTop - 7;
  
  if(adjX >= 0 && adjX < 9 * 18 && adjY>=0 && adjY< 4*18)
    {
    int xIndex = adjX/18;
    int yIndex = adjY/18;
    int totalIndex = yIndex*9 + xIndex + (pageNum * elementsPerPage) ;
    Config.logDebug("clicked in warehouse inventory area");
    if(totalIndex<container.warehouseItems.size())
      {
      Config.logDebug("clicked on : "+container.warehouseItems.get(totalIndex).stack.getDisplayName());
      }
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("req", true);
    tag.setInteger("slot", totalIndex);
    this.sendDataToServer(tag);
    }
  super.mouseClicked(par1, par2, par3);
  }


@Override
public void updateControls()
  {
  
  }

}
