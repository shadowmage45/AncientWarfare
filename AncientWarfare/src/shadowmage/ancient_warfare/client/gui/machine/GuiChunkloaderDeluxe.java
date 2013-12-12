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

import net.minecraft.inventory.Container;
import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiRectangle;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerChunkloaderDeluxe;
import shadowmage.ancient_warfare.common.container.ContainerChunkloaderDeluxe.ChunkMapEntry;

public class GuiChunkloaderDeluxe extends GuiContainerAdvanced
{

ContainerChunkloaderDeluxe container;
/**
 * @param container
 */
public GuiChunkloaderDeluxe(Container container)
  {
  super(container);
  this.shouldCloseOnVanillaKeys = true;
  this.container = (ContainerChunkloaderDeluxe) container;
  }

@Override
public int getXSize()
  {
  return 11*16 + 16;
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
  this.drawStringGui("Click on chunks to force/unforce", 8, 8, WHITE);
  this.drawStringGui("Red=Forced   Black=Unforced", 8, 18, WHITE);
  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub

  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element instanceof GuiRectangle)
    {
    int index = element.getElementNumber();
    int z = index / 11;
    int x = index % 11;
    if(x==5 && z==5){return;}
    this.container.handleChunkSelection(x, z);
    this.refreshGui();
    }
  }

@Override
public void setupControls()
  {
  this.guiElements.clear();
  int xPos = 8;
  int yPos = 240 - (11*16)-8;
  int x;
  int z;
  boolean flag;
  int color;
  GuiRectangle rect;
  int index = 0;  
  ChunkMapEntry chunk;
  for(z = 0; z < 11; z++, yPos+=16, xPos = 8)
    {    
    for(x = 0; x<11; x++, xPos+=16, index++)
      {     
      chunk = container.chunkMap[x][z];
      flag = !chunk.isForced();
      
      color = x==5 && z==5 ? 0xffff00ff : flag? 0xff000000 : 0xffff0000;
      rect = new GuiRectangle(index, this, 16, 16);
      rect.updateRenderPos(xPos, yPos);
      rect.setRenderColor(color);
      rect.addToToolitp("Chunk: "+chunk.getChunkX()+","+chunk.getChunkZ());
      if(x==5 && z==5)
        {
        rect.addToToolitp("Cannot Unforce TEs own chunk");
        }
      this.guiElements.put(index, rect);
      this.rects[x][z] = rect;
      }
    } 
  }

GuiRectangle[][] rects = new GuiRectangle[11][11];

@Override
public void updateControls()
  {
  int x;
  int z;  
  boolean flag;
  int color;
  for(z = 0; z < 11; z++)
    {    
    for(x = 0; x<11; x++)
      {     
      flag = !container.chunkMap[x][z].isForced();
      color = x==5 && z==5 ? 0xffff00ff : flag? 0xff000000 : 0xffff0000;
      this.rects[x][z].setRenderColor(color);
      }
    } 
  }

}
