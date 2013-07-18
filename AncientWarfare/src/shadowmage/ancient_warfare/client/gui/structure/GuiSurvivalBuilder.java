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
package shadowmage.ancient_warfare.client.gui.structure;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerSurvivalBuilder;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.utils.IDPairCount;

public class GuiSurvivalBuilder extends GuiContainerAdvanced
{

private final ContainerSurvivalBuilder cont;

/**
 * @param container
 */
public GuiSurvivalBuilder(Container container)
  {
  super(container);
  this.cont = (ContainerSurvivalBuilder)container;
  if(cont==null)
    {
    closeGUI();
    }
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
  this.drawString(fontRenderer, "Needed Blocks:", guiLeft+10, guiTop+10, 0xffffffff);
  int pos = 0;
  int row = 0;
  int col = 0;
  for(IDPairCount count : this.cont.idCounts)
    {
    ItemStack stack = new ItemStack(count.id, count.count, count.meta);
    if(stack.getItem()==null)
      {
      continue;      
      }
    this.renderItemStack(stack, guiLeft+10 +20*col , guiTop+30 + 20*row, mouseX, mouseY, true);    
    row++;
    if(row>=10)
      {
      row=0;
      col++;
      }
    pos++;
    }
  }

@Override
public void updateScreenContents()
  {
  
  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, 256-35-10, 10, 35, 18, "Done"); 
  this.addGuiButton(1, 256-35-10, 30, 35, 18, "Clear");  
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub  
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  switch(element.getElementNumber())
  {
  case 0:
  closeGUI();
  break;
  
  case 1:  
  NBTTagCompound tag = new NBTTagCompound();
  tag.setBoolean("clear", true);
  StructureManager.instance().clearClientTempInfo();
  this.sendDataToServer(tag);
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
 
  }

}
