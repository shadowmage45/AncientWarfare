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
package shadowmage.ancient_warfare.client.gui.npc;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiCheckBoxSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiFakeSlot;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.civics.BlockCivic;
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
  return Statics.TEXTURE_PATH+"gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  // TODO Auto-generated method stub
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(blockSlots.contains(element))
    {
    int num = element.getElementNumber();
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("swap", num);
    this.sendDataToServer(tag);
    this.closeGUI();
    return;
    }
  if(slots.contains(element))
    {
    if(element==this.currentMouseElement)//handle slot click
      {
      GuiFakeSlot fakeSlot = (GuiFakeSlot)element;
      int index = element.getElementNumber();
      int point = index/8;
      int slot = index%8;
      NBTTagCompound tag = new NBTTagCompound();
      tag.setBoolean("filter", true);
      tag.setByte("f", (byte)point);
      tag.setByte("s", (byte)slot);
      if(fakeSlot.getStack()!=null)
        {
        tag.setCompoundTag("fs", InventoryTools.writeItemStackToTag(fakeSlot.getStack(), new NBTTagCompound()));
        }
      this.sendDataToServer(tag);
      this.container.info.getPoint(point).setFilterStack(slot, fakeSlot.getStack());
      }
    }
  if(deliverButtons.contains(element))
    {
    GuiButtonSimple button = (GuiButtonSimple)element;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setByte("f", (byte)button.getElementNumber());    
    tag.setBoolean("set", true);
    WayPointItemRouting p = this.container.info.getPoint(element.getElementNumber());
    boolean newVal = !p.getDeliver();
    p.setDeliver(newVal);
    tag.setBoolean("d", newVal);
    button.setButtonText( newVal ? "Deposit" : "Pickup" );        
    this.sendDataToServer(tag);
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
  if(typeButtons.contains(element))
    {
    GuiButtonSimple button = (GuiButtonSimple)element;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setByte("f", (byte)button.getElementNumber());
    tag.setBoolean("set", true);
    WayPointItemRouting p = this.container.info.getPoint(element.getElementNumber());
    p.nextRoutingType();
    tag.setString("rt", p.getRoutingType().name());
    button.setButtonText(p.getRoutingType().name());
    this.sendDataToServer(tag);
    }
  if(sideButtons.contains(element))
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setByte("f", (byte)element.getElementNumber());
    tag.setBoolean("side", true);
    this.container.info.getPoint(element.getElementNumber()).incrementSide();
    this.sendDataToServer(tag);
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
HashSet<GuiButtonSimple> upButtons = new HashSet<GuiButtonSimple>();
HashSet<GuiButtonSimple> downButtons = new HashSet<GuiButtonSimple>();
HashSet<GuiButtonSimple> sideButtons = new HashSet<GuiButtonSimple>();
HashSet<GuiButtonSimple> removeButtons = new HashSet<GuiButtonSimple>();
HashSet<GuiButtonSimple> typeButtons = new HashSet<GuiButtonSimple>();
HashSet<GuiButtonSimple> deliverButtons = new HashSet<GuiButtonSimple>();
HashSet<GuiFakeSlot> blockSlots = new HashSet<GuiFakeSlot>();

@Override
public void setupControls()
  {
  int size = container.info.getRouteSize();
  area = new GuiScrollableArea(0, this, 7, 7, 256-14, 240-85-7, size*(37));
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
  slot = new GuiFakeSlot(index, area, 0, index*entryHeight);
  slot.enabled = true;
  slot.isClickable = true;
  slot.autoUpdateOnClick = false;
  blockSlots.add(slot);
  int id = player.worldObj.getBlockId(point.floorX(), point.floorY(), point.floorZ());
  int meta = player.worldObj.getBlockMetadata(point.floorX(), point.floorY(), point.floorZ());
  if(id!=0 && Block.blocksList[id]!=null)
    {
    Block block = Block.blocksList[id];
    if(block instanceof BlockCivic)
      {
      BlockCivic blockC = (BlockCivic)block;
      slot.setItemStack(CivicRegistry.instance().getItemFor(blockC.blockNum, meta));
      slot.addToToolitp("Block: "+point.floorX()+", "+point.floorY()+", "+point.floorZ() + "  Side: "+ForgeDirection.getOrientation(point.getSide())); 
      slot.addToToolitp(slot.getStack().getDisplayName());
      slot.addToToolitp("Click to change block position");
      }
    else
      {
      slot.setItemStack(new ItemStack(block,1,meta));
      slot.addToToolitp("Block: "+point.floorX()+", "+point.floorY()+", "+point.floorZ() + "  Side: "+ForgeDirection.getOrientation(point.getSide())); 
      slot.addToToolitp(slot.getStack().getDisplayName());
      slot.addToToolitp("Click to change block position");
      }
    }
  area.elements.add(slot);
      
  button = new GuiButtonSimple(index, area, 100, 14, point.getRoutingType().toString());
  button.updateRenderPos(62, index*entryHeight + 20);
  area.elements.add(button);
  typeButtons.add(button);
  
  button = new GuiButtonSimple (index, area, 60, 14, point.getDeliver() ? "Deposit" : "Pickup");
  button.updateRenderPos(0, index*entryHeight+20);
  area.elements.add(button);
  deliverButtons.add(button);
  
  button = new GuiButtonSimple(index, area, 20, 18, "+");
  button.updateRenderPos(20*9+1, index*entryHeight);
  area.elements.add(button);
  upButtons.add(button);
  
  button = new GuiButtonSimple(index, area, 20, 18, "-");
  button.updateRenderPos(20*10+3, index*entryHeight);
  area.elements.add(button);
  downButtons.add(button);
  
  button = new GuiButtonSimple(index, area, 36, 14, ForgeDirection.VALID_DIRECTIONS[point.getSide()].toString());
  button.updateRenderPos(17*10-6, index*entryHeight+20);
  area.elements.add(button);
  sideButtons.add(button);
  
  button = new GuiButtonSimple(index, area, 21, 14, "Del");
  button.updateRenderPos(20*10+2, index*entryHeight+20);
  area.elements.add(button);
  removeButtons.add(button);
  
  for(int k = 0; k < 8; k++)
    {
    slot = new GuiFakeSlot(index*8+k, area, (k+1)*20, entryHeight*index);
    area.elements.add(slot);
    slots.add(slot);
    slot.setItemStack(point.getFilterStack(k));      
    }
  }

int entryHeight = 37;
@Override
public void updateControls()
  {
  int size = container.info.getRouteSize();
  slots.clear();
  deliverBoxes.clear();
  area.elements.clear();
  upButtons.clear();
  downButtons.clear();
  removeButtons.clear();
  typeButtons.clear();
  WayPointItemRouting point;
  for(int i = 0; i < size; i++)
    {
    point = container.info.getPoint(i);
    this.addButtonsFor(i, point);   
    }
  area.updateTotalHeight(container.info.getRouteSize()*entryHeight);
  area.updateGuiPos(guiLeft, guiTop);  
  }

}
