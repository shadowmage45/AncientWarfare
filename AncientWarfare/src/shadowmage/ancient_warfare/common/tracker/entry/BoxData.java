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
package shadowmage.ancient_warfare.common.tracker.entry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.inventory.AWInventoryBasic;
import shadowmage.ancient_warfare.common.machine.TEMailBoxBase;
import shadowmage.ancient_warfare.common.tracker.MailboxData;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class BoxData implements INBTTaggable, IInventory
{

int ticks = 0;
public int dimID;
int x;
int y;
int z;
boolean isAssigned = false;
int[] bottomIndices = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
public List<RoutedDelivery> incomingItems = new ArrayList<RoutedDelivery>();
List<RoutedDelivery> deliverableItems = new ArrayList<RoutedDelivery>();
String [] sideNames = new String[5];
public String boxName = "";
AWInventoryBasic inventory;

public BoxData(NBTTagCompound tag)
  {
  this.readFromNBT(tag);  
  }

public int posX()
  {
  return x;
  }

public int posY()
  {
  return y;
  }

public int posZ()
  {
  return z;
  }

public BoxData(String name, int size)
  {
  this.boxName = name;
  this.inventory = new AWInventoryBasic(size);
  MailboxData.instance().markDirty();
  }

public void updateTick()
  {
  ticks++;
  if(this.ticks<20){return;}
  ticks = 0;
  this.validateSideNames();
  this.updateRoutedItems();
  this.tryDeliverMail(inventory, bottomIndices);  
  }

public boolean canBeRemoved()
  {
  return this.incomingItems.isEmpty() && this.deliverableItems.isEmpty() && this.inventory.getEmptySlotCount()==this.getSizeInventory();
  }

protected void validateSideNames()
  {
  String s;
  for(int i = 0; i < this.sideNames.length; i++)
    {
    s = this.sideNames[i];
    if(s==null){continue;}
    if(!MailboxData.instance().containsBox(s))
      {
      this.sideNames[i] = null;
      }
    }
  }

protected void updateRoutedItems()
  {
  if(this.incomingItems.isEmpty()){return;}  
  MailboxData.instance().markDirty();
  Iterator<RoutedDelivery> it = this.incomingItems.iterator();
  RoutedDelivery d;
  while(it.hasNext())
    {
    d = it.next();
    if(d.ticksLeft<=0)
      {
      this.deliverableItems.add(d);
      it.remove();
      continue;
      }
    d.ticksLeft -= 20;    
    }
  }

public void tryDeliverMail(IInventory inventory, int[] slots)
  {
  if(this.deliverableItems.isEmpty()){return;}
  Config.logDebug("Delivering items to mailbox: "+this.boxName);
  int delivered = 0;
  Iterator<RoutedDelivery> it = this.deliverableItems.iterator();
  RoutedDelivery d;
  ItemStack item;
  int prevQuantity;
  while(it.hasNext())
    {
    d = it.next();    
    item = d.item;
    prevQuantity = item.stackSize;
    item = InventoryTools.tryMergeStack(inventory, item, slots);
    delivered++;
    if(item==null)
      {
      it.remove();
      MailboxData.instance().markDirty();
      }
    else
      {
      if(item.stackSize!=prevQuantity)
        {
        MailboxData.instance().markDirty();
        }
      break;
      }    
    }
  Config.logDebug("Delivered: " + delivered + " items");
  }

public void addIncomingItem(ItemStack item, int ticks)
  {
  if(item==null || ticks<0){return;}
  if(ticks==0)
    {
    this.deliverableItems.add(new RoutedDelivery(boxName, item, ticks));
    }
  else
    {
    this.incomingItems.add(new RoutedDelivery(boxName, item, ticks));    
    }
  MailboxData.instance().markDirty();
  }

public String getBoxName()
  {
  return this.boxName;
  }

public boolean isAssigned()
  {
  return this.isAssigned;
  }

public String getSideName(int side)
  {
  if(side<0 || side >= this.sideNames.length + 1){return null;}
  if(side==0)
    {
    return boxName;
    }
  return this.sideNames[side-1];
  }

public void setSideName(int side, String name)
  {
  if(side<0 || side >= this.sideNames.length + 1){return;}
  if(side==0){return;}
  this.sideNames[side-1] = name;
  MailboxData.instance().markDirty();
  }

public void handleAssignment(TEMailBoxBase box)
  {
  if(box==null)
    {
    this.clearAssignment();
    return;
    }
  MailboxData.instance().markDirty();
  this.dimID = box.worldObj.provider.dimensionId;
  this.x = box.xCoord;
  this.y = box.yCoord;
  this.z = box.zCoord;
  this.isAssigned = true;
  }

public void clearAssignment()
  {
  Config.logDebug("clearing assigned status for mailbox: "+this.boxName);
  this.isAssigned = false;
  MailboxData.instance().markDirty();
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {  
  this.boxName = tag.getString("boxName");
  for(int i = 1; i <5; i++)
    {
    if(tag.hasKey("sideName"+i))
      {
      sideNames[i] = tag.getString("sideName"+i);
      }
    }
  this.dimID = tag.getInteger("dim");
  this.x = tag.getInteger("x");
  this.y = tag.getInteger("y");
  this.z = tag.getInteger("z");
  this.isAssigned = tag.getBoolean("assigned");
  if(tag.hasKey("items"))
    {
    NBTTagList list = tag.getTagList("items");
    for(int i = 0; i < list.tagCount(); i++)
      {
      RoutedDelivery d = new RoutedDelivery();
      d.readFromNBT((NBTTagCompound) list.tagAt(i));
      this.incomingItems.add(d);
      }
    }
  if(tag.hasKey("readyitems"))
    {
    NBTTagList list = tag.getTagList("readyitems");
    for(int i = 0; i < list.tagCount(); i++)
      {
      RoutedDelivery d = new RoutedDelivery();
      d.readFromNBT((NBTTagCompound) list.tagAt(i));
      this.deliverableItems.add(d);
      }
    }
  if(tag.hasKey("inventory"))
    {
    this.inventory = new AWInventoryBasic(tag.getInteger("inventorySize"));
    this.inventory.readFromNBT(tag.getCompoundTag("inventory"));
    }
  else
  {
	  this.inventory = new AWInventoryBasic(38);
  }
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  for(int i = 0; i < 5; i++)
    {
    if(this.sideNames[i]!=null)
      {
      tag.setString("sideName"+i, this.sideNames[i]);
      }
    }
  tag.setString("boxName", boxName);
  tag.setInteger("dim", dimID);
  tag.setInteger("x", x);
  tag.setInteger("y", y);
  tag.setInteger("z", z);
  tag.setBoolean("assigned", isAssigned);
  if(!this.incomingItems.isEmpty())
    {
    NBTTagList list = new NBTTagList();
    for(RoutedDelivery d : this.incomingItems)
      {
      list.appendTag(d.getNBTTag());
      }
    tag.setTag("items", list);
    }
  if(!this.deliverableItems.isEmpty())
    {
    NBTTagList list = new NBTTagList();
    for(RoutedDelivery d : this.deliverableItems)
      {
      list.appendTag(d.getNBTTag());
      }
    tag.setTag("readyitems", list);
    }
  if(this.inventory!=null)
    {
    tag.setInteger("inventorySize", this.inventory.getSizeInventory());
    tag.setCompoundTag("inventory", this.inventory.getNBTTag());
    }
  return tag;
  }

@Override
public void onInventoryChanged()
  {
  
  }

@Override
public int getSizeInventory()
  {
  return inventory.getSizeInventory();
  }

@Override
public ItemStack getStackInSlot(int i)
  {
  return inventory.getStackInSlot(i);
  }

@Override
public ItemStack decrStackSize(int i, int j)
  {
  return inventory.decrStackSize(i, j);
  }

@Override
public ItemStack getStackInSlotOnClosing(int i)
  {
  return inventory.getStackInSlotOnClosing(i);
  }

@Override
public void setInventorySlotContents(int i, ItemStack itemstack)
  {
  inventory.setInventorySlotContents(i, itemstack);
  }

@Override
public String getInvName()
  {
  return "AWInventory.mail";
  }

@Override
public boolean isInvNameLocalized()
  {
  return false;
  }

@Override
public int getInventoryStackLimit()
  {
  return inventory.getInventoryStackLimit();
  }

@Override
public boolean isUseableByPlayer(EntityPlayer entityplayer)
  {
  return entityplayer.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) <= 64D;
  }

@Override
public void openChest()
  {
  }

@Override
public void closeChest()
  {
  }

@Override
public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
  return true;
  }
}
