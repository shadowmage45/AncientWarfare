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
package shadowmage.ancient_warfare.client.gui.machine;

import net.minecraft.inventory.Container;
import net.minecraft.world.ChunkCoordIntPair;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiRectangle;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerChunkloaderDeluxe;

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
//  int xPos;
//  int yPos;
//  int x;
//  int z;
//  
//  int tx = container.te.xCoord/16;
//  int tz = container.te.zCoord/16;
//  int cx = tx;
//  int cz = tz;
//  int white = 0xffffffff;
//  ChunkCoordIntPair c;
//  String text;
//  boolean flag;
//  int color;
//  for(z = 0, yPos = this.guiTop; z < 11 && yPos < this.guiTop+this.getYSize(); z++, yPos+=16, cz++)
//    {    
//    for(x = 0, xPos = this.guiLeft; x<11 && xPos<this.guiLeft+this.getXSize(); x++, xPos+=16, tz++)
//      {     
//      flag = container.chunkMap[x][z]==null;
//      color = flag? 0xff000000 : 0xffff0000;
//      this.drawRect(xPos, yPos, xPos+16, yPos+16, color);
//      text = cx+","+cz;
//      this.drawString(getFontRenderer(), text, xPos, yPos+4, white);
//      }
//    } 
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
  int yPos = 8;
  int x;
  int z;  
  int tx = container.te.xCoord/16;
  int tz = container.te.zCoord/16;
  int cx = tx;
  int cz = tz;
  boolean flag;
  int color;
  GuiRectangle rect;
  int index = 0;  
  for(z = 0; z < 11; z++, yPos+=16, cz++, xPos = 8, cx=tx)
    {    
    for(x = 0; x<11; x++, xPos+=16, index++, cx++)
      {     
      flag = container.chunkMap[x][z]==null;
      color = x==5 && z==5 ? 0xffff00ff : flag? 0xff000000 : 0xffff0000;
      rect = new GuiRectangle(index, this, 16, 16);
      rect.updateRenderPos(xPos, yPos);
      rect.setRenderColor(color);
      rect.addToToolitp("Chunk: "+(cx-5)+","+(cz-5));
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
      flag = container.chunkMap[x][z]==null;
      color = x==5 && z==5 ? 0xffff00ff : flag? 0xff000000 : 0xffff0000;
      this.rects[x][z].setRenderColor(color);
      }
    } 
  }

}
