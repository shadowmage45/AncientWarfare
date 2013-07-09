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
package shadowmage.ancient_warfare.common.tracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.machine.TEMailBox;
import shadowmage.ancient_warfare.common.utils.InventoryTools;

public class MailboxData implements INBTTaggable
{

private MailboxData(){};
private static final MailboxData INSTANCE = new MailboxData();
public static MailboxData instance(){return INSTANCE;}

private HashMap<String, BoxData> mailboxes = new HashMap<String, BoxData>();

public Collection<String> getBoxNames()
  {
  return this.mailboxes.keySet();
  }

public boolean tryAddMailbox(String name)
  {
  if(this.mailboxes.containsKey(name))
    {
    return false;
    }
  this.mailboxes.put(name, new BoxData(name));
  return true;
  }

public boolean tryRemoveMailbox(String name)
  {
  BoxData data = mailboxes.get(name);
  if(data!=null && !data.incomingItems.isEmpty())
    {
    return false;
    }  
  this.mailboxes.remove(name);
  return true;
  }

public boolean tryAssignMailbox(TEMailBox te, String name)
  {
  if(this.mailboxes.containsKey(name))
    {    
    BoxData data = this.mailboxes.get(name);
    if(!data.isAssigned)
      {
      if(data.boxName!=null && this.mailboxes.containsKey(data.boxName))
        {
        Config.logDebug("unassigning old name: "+data.boxName);
        this.mailboxes.get(data.boxName).isAssigned = false;
        }
      Config.logDebug("assigning mailbox for name: "+name);
      this.mailboxes.get(name).handleAssignment(te);
      te.setBoxName(name);
      return true;
      }
    }
  return false;
  }

protected void markDirty()
  {
  GameDataTracker.instance().markGameDataDirty();
  }

public void resetTrackedData()
  {
  this.mailboxes.clear();
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  NBTTagList list = tag.getTagList("mailboxes");
  for(int i = 0; i < list.tagCount(); i++)
    {
    BoxData d = new BoxData((NBTTagCompound) list.tagAt(i));
    this.mailboxes.put(d.boxName, d);
    } 
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  
  NBTTagList list = new NBTTagList();
  for(BoxData d : mailboxes.values())
    {
    list.appendTag(d.getNBTTag());
    }  
  tag.setTag("mailboxes", list);  
  
  return tag;
  }

private class BoxData implements INBTTaggable
{
String boxName;
int dimID;
int x;
int y;
int z;
boolean isAssigned = false;
List<RoutedDelivery> incomingItems = new ArrayList<RoutedDelivery>();

private BoxData(NBTTagCompound tag)
  {
  this.readFromNBT(tag);
  }

private BoxData(String name)
  {
  this.boxName = name;
  }

private void handleAssignment(TEMailBox box)
  {
  this.dimID = box.worldObj.provider.dimensionId;
  this.x = box.xCoord;
  this.y = box.yCoord;
  this.z = box.zCoord;
  this.isAssigned = true;
  }

private void clearAssignment()
  {
  this.isAssigned = false;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {  
  this.boxName = tag.getString("boxName");  
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
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
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
  return tag;
  }

}

private class RoutedDelivery implements INBTTaggable
{
String destination;
ItemStack item;
int ticksLeft = 0;

private RoutedDelivery(){}

private RoutedDelivery(String destination, ItemStack item, int travelTicks)
  {
  this.destination = destination;
  this.item = item;
  this.ticksLeft = travelTicks;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.destination = tag.getString("destination");
  this.item = InventoryTools.loadStackFromTag(tag.getCompoundTag("item"));
  this.ticksLeft = tag.getInteger("ticks");
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("destination", destination);
  tag.setCompoundTag("item", InventoryTools.writeItemStackToTag(item, new NBTTagCompound()));
  tag.setInteger("ticks", this.ticksLeft);
  return tag;
  }

}

}
