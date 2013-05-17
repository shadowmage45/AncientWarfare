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
package shadowmage.ancient_warfare.client.gui.npc;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiCheckBoxSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiFakeSlot;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.civics.BlockCivic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerCourierRoutingSlip;
import shadowmage.ancient_warfare.common.npcs.waypoints.WayPointItemRouting;
import shadowmage.ancient_warfare.common.registry.CivicRegistry;

public class GuiCourierRoutingSlip extends GuiContainerAdvanced
{

ContainerCourierRoutingSlip container;

/**
 * @param container
 */
public GuiCourierRoutingSlip(Container container)
  {
  super(container);
  this.container = (ContainerCourierRoutingSlip)container;
  this.shouldCloseOnVanillaKeys = true;
  }

@Override
public void handleDataFromContainer(NBTTagCompound tag)
  {   
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
  // TODO Auto-generated method stub
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(slots.contains(element))
    {
    if(element==this.currentMouseElement)//handle slot click
      {
      GuiFakeSlot fakeSlot = (GuiFakeSlot)element;
      int index = element.getElementNumber();
      int point = index/4;
      int slot = index%4;
      NBTTagCompound tag = new NBTTagCompound();
      tag.setBoolean("filter", true);
      tag.setByte("f", (byte)point);
      tag.setByte("s", (byte)slot);
      if(fakeSlot.getStack()!=null)
        {
        tag.setCompoundTag("fs", fakeSlot.getStack().writeToNBT(new NBTTagCompound()));
        }
      this.sendDataToServer(tag);
      this.container.info.getPoint(point).setFilterStack(slot, fakeSlot.getStack());
      }
    }
  if(deliverBoxes.contains(element))
    {    
    GuiCheckBoxSimple box = (GuiCheckBoxSimple)element;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setByte("f", (byte)box.getElementNumber());
    tag.setBoolean("set", true);
    tag.setBoolean("d", box.checked());
    this.sendDataToServer(tag);
    this.container.info.getPoint(element.getElementNumber()).setDeliver(box.checked());
    }
  if(this.includeBoxes.contains(element))
    {    
    GuiCheckBoxSimple box = (GuiCheckBoxSimple)element;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setByte("f", (byte)box.getElementNumber());
    tag.setBoolean("set", true);
    tag.setBoolean("i", box.checked());
    this.sendDataToServer(tag);
    this.container.info.getPoint(element.getElementNumber()).setInclude(box.checked());
    }
  if(partialBoxes.contains(element))
    {    
    GuiCheckBoxSimple box = (GuiCheckBoxSimple)element;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setByte("f", (byte)box.getElementNumber());
    tag.setBoolean("set", true);
    tag.setBoolean("p", box.checked());
    this.sendDataToServer(tag);
    this.container.info.getPoint(element.getElementNumber()).setPartial(box.checked());
    }
  if(upButtons.contains(element))
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("move", true);
    tag.setByte("f", (byte)element.getElementNumber());
    tag.setBoolean("u", true);
    this.sendDataToServer(tag);
    this.container.info.movePointUp(element.getElementNumber());
    this.refreshGui();
    }
  if(downButtons.contains(element))
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("move", true);
    tag.setByte("f", (byte)element.getElementNumber());
    tag.setBoolean("d", true);
    this.sendDataToServer(tag);
    this.container.info.movePointDown(element.getElementNumber());
    this.refreshGui();
    }
  if(removeButtons.contains(element))
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("move", true);
    tag.setByte("f", (byte)element.getElementNumber());
    tag.setBoolean("rem", true);
    this.sendDataToServer(tag);
    this.container.info.removeRoutePoint(element.getElementNumber());
    this.refreshGui();
    }
  }

@Override
public void updateScreenContents()
  {
  area.updateGuiPos(guiLeft, guiTop);  
  }

GuiScrollableArea area;
HashSet<GuiFakeSlot> slots = new HashSet<GuiFakeSlot>();
HashSet<GuiCheckBoxSimple> deliverBoxes = new HashSet<GuiCheckBoxSimple>();
HashSet<GuiCheckBoxSimple> includeBoxes = new HashSet<GuiCheckBoxSimple>();
HashSet<GuiCheckBoxSimple> partialBoxes = new HashSet<GuiCheckBoxSimple>();
HashSet<GuiButtonSimple> upButtons = new HashSet<GuiButtonSimple>();
HashSet<GuiButtonSimple> downButtons = new HashSet<GuiButtonSimple>();
HashSet<GuiButtonSimple> removeButtons = new HashSet<GuiButtonSimple>();

@Override
public void setupControls()
  {
  int size = container.info.getRouteSize();
  area = new GuiScrollableArea(0, this, 10, 10, 256-20, 240-120-10, size*(18+2));
  this.guiElements.put(0, area);
 
  WayPointItemRouting point;
  for(int i = 0; i < size; i++)
    {
    point = container.info.getPoint(i);
    this.addButtonsFor(i, point);   
    }
  }

protected void addButtonsFor(int index, WayPointItemRouting point)
  {
  GuiFakeSlot slot;// = new GuiFakeSlot(0, area, k*20, 20*i);
  GuiCheckBoxSimple box;
  GuiButtonSimple button;
  slot = new GuiFakeSlot(0, area, 0, index*20);
  slot.enabled = false;
  int id = player.worldObj.getBlockId(point.floorX(), point.floorY(), point.floorZ());
  int meta = player.worldObj.getBlockMetadata(point.floorX(), point.floorY(), point.floorZ());
  if(id!=0 && Block.blocksList[id]!=null)
    {
    Block block = Block.blocksList[id];
    if(block instanceof BlockCivic)
      {
      BlockCivic blockC = (BlockCivic)block;
      slot.setItemStack(CivicRegistry.instance().getItemFor(blockC.blockNum, meta));
      slot.addToToolitp("Routing Waypoint: "+point.floorX()+", "+point.floorY()+", "+point.floorZ()); 
      slot.addToToolitp(slot.getStack().getDisplayName());
      }
    else
      {
      slot.setItemStack(new ItemStack(block,1,meta));
      slot.addToToolitp("Routing Waypoint: "+point.floorX()+", "+point.floorY()+", "+point.floorZ()); 
      slot.addToToolitp(slot.getStack().getDisplayName());
      }
    }
  area.elements.add(slot);
  
  box = new GuiCheckBoxSimple(index, area, 18, 18);
  box.updateRenderPos(20*5, index*20);
  box.setChecked(point.getDeliver());
  area.elements.add(box);
  deliverBoxes.add(box);  
  
  box = new GuiCheckBoxSimple(index, area, 18, 18);
  box.updateRenderPos(20*6, index*20);
  box.setChecked(point.getInclude());
  area.elements.add(box);
  includeBoxes.add(box);
  
  box = new GuiCheckBoxSimple(index, area, 18, 18);
  box.updateRenderPos(20*7, index*20);
  box.setChecked(point.getPartial());
  area.elements.add(box);
  partialBoxes.add(box);
  
  button = new GuiButtonSimple(index, area, 18, 18, "+");
  button.updateRenderPos(20*8, index*20);
  area.elements.add(button);
  upButtons.add(button);
  
  button = new GuiButtonSimple(index, area, 18, 18, "-");
  button.updateRenderPos(20*9, index*20);
  area.elements.add(button);
  downButtons.add(button);
  
  button = new GuiButtonSimple(index, area, 18, 18, "Del");
  button.updateRenderPos(20*10, index*20);
  area.elements.add(button);
  removeButtons.add(button);
  
  for(int k = 0; k < 4; k++)
    {
    slot = new GuiFakeSlot(index*4+k, area, (k+1)*20, 20*index);
    area.elements.add(slot);
    slots.add(slot);
    slot.setItemStack(point.getFilterStack(k));      
    }
  }

@Override
public void updateControls()
  {
  int size = container.info.getRouteSize();
  slots.clear();
  deliverBoxes.clear();
  area.elements.clear();
  includeBoxes.clear();
  partialBoxes.clear();  
  upButtons.clear();
  downButtons.clear();
  removeButtons.clear();
  WayPointItemRouting point;
  for(int i = 0; i < size; i++)
    {
    point = container.info.getPoint(i);
    this.addButtonsFor(i, point);   
    }
  area.updateTotalHeight(container.info.getRouteSize()*(18+2));
  area.updateGuiPos(guiLeft, guiTop);  
  }

}
