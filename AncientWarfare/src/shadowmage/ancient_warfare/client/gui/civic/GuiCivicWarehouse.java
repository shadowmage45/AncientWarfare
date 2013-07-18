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
package shadowmage.ancient_warfare.client.gui.civic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiElement;
import shadowmage.ancient_warfare.client.gui.elements.GuiItemStack;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.civics.TECivicWarehouse;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerCivicWarehouse;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapper;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_warfare.common.utils.StackWrapperComparatorAlphaAZ;

public class GuiCivicWarehouse extends GuiContainerAdvanced
{

TECivicWarehouse te;
ContainerCivicWarehouse container;
GuiScrollableArea area;
Set<GuiItemStack> stacks = new HashSet<GuiItemStack>();
GuiTextInputLine searchBox;
StackWrapperComparatorAlphaAZ sorter = new StackWrapperComparatorAlphaAZ();

boolean receivedInfo = false;
int recTick =0;

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
  int centerX = guiLeft+176/2;
  int buttonY = guiTop + 8 + 4*18 + 2;
  int textTop = buttonY + 16;
  
  this.drawCenteredString(this.getFontRenderer(), "Size:", centerX, textTop, 0xffffffff);
  this.drawCenteredString(this.getFontRenderer(), String.valueOf(this.te.getSizeInventory()), centerX, textTop+10, 0xffffffff);
  this.drawCenteredString(this.getFontRenderer(), "Filled:", centerX, textTop+20, 0xffffffff);
  this.drawCenteredString(this.getFontRenderer(), String.valueOf(this.container.filledSlotCount), centerX, textTop+30, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  this.area.updateGuiPos(guiLeft, guiTop);
  if(!this.container.receivedDatas)
    {
    recTick++;
    if(recTick>=5)
      {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setBoolean("reqInit", true);
      this.sendDataToServer(tag);
      recTick = 0;
      }    
    }
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(this.stacks.contains(element))
    {
    GuiItemStack item = (GuiItemStack)element;
    this.handleStackClick(item);
    }
  this.forceUpdate = true;  
  }

@Override
public void setupControls()
  {    
  this.searchBox = (GuiTextInputLine) new GuiTextInputLine(1, this, 176-16, 12, 30, "").updateRenderPos(8, 5);
  searchBox.selected = false;
  int x = 7;
  int y = 4 + 12 + 4;
  int w = 176-14;
  int h = 4 * 18;
  this.area = new GuiScrollableArea(0, this, x, y, w, h, 0);
  this.forceUpdate = true;
  this.guiElements.put(0, area);
  this.guiElements.put(1, searchBox);
  }

@Override
public void updateControls()
  {
  this.addDisplayStacks();
  }

protected void addDisplayStacks()
  {
  this.stacks.clear();
  this.area.elements.clear();
  String text = this.searchBox.getText();
  int x = 0 ;
  int y = 0 ;
  int index = 0;
  GuiItemStack stack;
  ItemStack item;
  Collections.sort(this.container.warehouseItems, sorter);
  for(ItemStackWrapper wrap : this.container.warehouseItems)
    {
    item = wrap.getFilter();
    if(item==null || !item.getDisplayName().toLowerCase().contains(text.toLowerCase())){continue;}    
    stack = new GuiItemStack(index, area);
    stack.renderSlotBackground = true;
    stack.isClickable = true;
    stack.setItemStack(wrap.getFilter());
    stack.updateRenderPos(x*18, y*18);
    area.addGuiElement(stack);
    stacks.add(stack);
    x++;
    index++;
    if(x>=8)
      {
      x= 0;
      y++;
      }
    }
  if(x%8!=0)
    {
    y++;
    }
  area.updateTotalHeight(y*18);
  }

protected void handleStackClick(GuiItemStack stack)
  {
  NBTTagCompound tag = stack.getStack().writeToNBT(new NBTTagCompound());
  tag.setBoolean("req", true);
  this.sendDataToServer(tag);
  }

@Override
protected void keyTyped(char par1, int par2)
  { 
  if(!this.searchBox.selected)
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
    this.refreshGui();
    }
  }


}
