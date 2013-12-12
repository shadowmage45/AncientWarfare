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
package shadowmage.ancient_warfare.common.container;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.ancient_warfare.common.machine.TEMailBoxBase;
import shadowmage.ancient_warfare.common.tracker.MailboxData;
import shadowmage.ancient_warfare.common.tracker.entry.BoxData;

public class ContainerMailboxBase extends ContainerBase
{

public TEMailBoxBase te;
BoxData data;

String[] sideNames = new String[6];
public Set<String> boxNames = new HashSet<String>();

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerMailboxBase(EntityPlayer openingPlayer, TEMailBoxBase te)
  {
  super(openingPlayer, null);
  this.te = te;
  this.data = te.getBoxData();
  }

public void addSlots()
  {
  Slot s;
  for(Object o : this.inventorySlots)
    {
    s = (Slot)o;
    if(s.yDisplayPosition<0)
      {
      s.yDisplayPosition+=1000;      
      }
    }
  }

public void removeSlots()
  {
  Slot s;
  for(Object o : this.inventorySlots)
    {
    s = (Slot)o;
    if(s.yDisplayPosition>=0)
      {
      s.yDisplayPosition-=1000;      
      }
    }
  }

@Override
public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotClickedIndex)
  {
  ItemStack slotStackCopy = null;
  Slot theSlot = (Slot)this.inventorySlots.get(slotClickedIndex);
  if (theSlot != null && theSlot.getHasStack())
    {
    ItemStack slotStack = theSlot.getStack();
    slotStackCopy = slotStack.copy();
    int storageSlots = 18;
    if (slotClickedIndex < 36)//player slots...
      {      
      if (!this.mergeItemStack(slotStack, 36+storageSlots, 36+te.getSizeInventory(), false))//merge into send inventory
        {
        return null;
        }
      }
    else if(slotClickedIndex >=36 &&slotClickedIndex < 36+storageSlots)//storage slots, merge to player inventory
      {
      if (!this.mergeItemStack(slotStack, 0, 36, true))//merge into player inventory
        {
        return null;
        }
      }
    else if(slotClickedIndex >=36+storageSlots &&slotClickedIndex < 36+te.getSizeInventory())//send slots, merge to player inventory
      {
      if (!this.mergeItemStack(slotStack, 0, 36, true))//merge into player inventory
        {
        return null;
        }
      }
    if (slotStack.stackSize == 0)
      {
      theSlot.putStack((ItemStack)null);
      }
    else
      {
      theSlot.onSlotChanged();
      }
    if (slotStack.stackSize == slotStackCopy.stackSize)
      {
      return null;
      }
    theSlot.onPickupFromSlot(par1EntityPlayer, slotStack);
    }
  return slotStackCopy;
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("nameList"))
    {
    Config.logDebug("receiving all names list");
    this.boxNames.clear();
    NBTTagList nameList = tag.getTagList("nameList");
    for(int i = 0; i < nameList.tagCount(); i++)
      {
      this.boxNames.add( ((NBTTagString) nameList.tagAt(i)).data);
      }
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }
  if(tag.hasKey("boxList"))
    {
    Config.logDebug("receiving box names list");
    NBTTagList boxList = tag.getTagList("boxList");
    NBTTagCompound boxTag = null;
    for(int i = 0; i < boxList.tagCount(); i++)
      {
      boxTag = (NBTTagCompound) boxList.tagAt(i);
      int box = boxTag.getInteger("box");
      String name = null;
      if(boxTag.hasKey("name"))
        {
        name = boxTag.getString("name");
        }
      Config.logDebug("setting box: "+box + " to: "+name);
      this.setSideName(box, name);
      }
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }
  if(tag.hasKey("add"))
    {
    if(!MailboxData.instance().tryAddMailbox(tag.getString("add"), te.mailBoxSize))
      {
      NBTTagCompound reply = new NBTTagCompound();
      reply.setString("reject", "Could not add name, duplicate name detected.");
      this.sendDataToGUI(reply);      
      }
    }
  if(tag.hasKey("remove"))
    {
    if(!MailboxData.instance().tryRemoveMailbox(tag.getString("remove"), te))
      {
      NBTTagCompound reply = new NBTTagCompound();
      reply.setString("reject", "Could not remove name: already assigned to a box, or has undelivered mail.");
      this.sendDataToGUI(reply);
      }
    }
  if(tag.hasKey("select"))
    {
    String name = tag.getString("select");
    int box = tag.getInteger("box");
    if(box==0)
      {
      NBTTagCompound reply = new NBTTagCompound();
      if(MailboxData.instance().tryAssignMailbox(te, name))
        {
        reply.setBoolean("accept", true);
        }
      else
        {
        reply.setString("reject", "could not assign box, name already assigned elsewhere");
        }
      this.sendDataToGUI(reply);      
      }
    else
      {
      NBTTagCompound reply = new NBTTagCompound();
      reply.setBoolean("accept", true);
      if(data!=null)
        {
        data.setSideName(box, name);
        this.sendDataToGUI(reply);        
        }   
      }    
    } 
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {

  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return Collections.emptyList();
  }

@Override
public void detectAndSendChanges()
  {
  super.detectAndSendChanges();
  if(player.worldObj.isRemote){return;}
  
  /**
   * update all mailbox names list
   */
  Collection<String> boxNames = MailboxData.instance().getBoxNames();
  NBTTagCompound updateTag = null;
  if(this.boxNames.size()!=boxNames.size() || !this.boxNames.containsAll(boxNames))
    {
    if(updateTag==null){updateTag = new NBTTagCompound();}
    NBTTagList nameList = new NBTTagList();
    this.boxNames.clear();
    this.boxNames.addAll(boxNames);
    for(String name : boxNames)
      {
      NBTTagString string = new NBTTagString("name", name);
      nameList.appendTag(string);
      }    
    updateTag.setTag("nameList", nameList);
    }
  
  /**
   * update box data
   */
  if(this.data!=this.te.getBoxData())
    {
    if(updateTag==null){updateTag = new NBTTagCompound();}
    this.data = te.getBoxData();
    if(this.data==null)
      {
      this.sideNames = new String[6];
      }
    } 

  /**
   * update box data sides
   */
  else
    {
    NBTTagList boxList = null;
    NBTTagCompound tag = null;
    for(int i = 0; i < 6 ; i++)
      {
      if(!this.equals(this.getSideName(i), (data!=null ? data.getSideName(i) : null)))
        {
        if(updateTag==null){updateTag = new NBTTagCompound();}      
        if(boxList==null)
          {
          boxList = new NBTTagList();
          }
        tag = new NBTTagCompound();
        tag.setInteger("box", i);
        this.setSideName(i, data.getSideName(i));
        if(this.getSideName(i)!=null)
          {
          tag.setString("name", this.getSideName(i));
          }
        boxList.appendTag(tag);      
        }
      }
    if(boxList!=null)
      {
      updateTag.setTag("boxList", boxList);
      }
    }
  if(updateTag!=null)
    {
    this.sendDataToPlayer(updateTag);
    }
  }

protected boolean equals(String a, String b)
  {
  if(a==null && b==null)
    {
    return true;
    }
  else if((a!=null && b==null) || (a==null && b!=null))
    {
    return false;
    }
  else
    {
    return a.equals(b);
    }
  }

public String getSideName(int side)
  {
  return this.sideNames[side];
  }

protected void setSideName(int side, String name)
  {
  this.sideNames[side]=name;
  }
}
