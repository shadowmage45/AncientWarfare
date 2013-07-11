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

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.machine.TEMailBox;
import shadowmage.ancient_warfare.common.machine.TEMailBoxBase;
import shadowmage.ancient_warfare.common.tracker.entry.BoxData;
import shadowmage.ancient_warfare.common.tracker.entry.MailboxSaveData;

public class MailboxData implements INBTTaggable
{

private MailboxData(){};
private static final MailboxData INSTANCE = new MailboxData();
public static MailboxData instance(){return INSTANCE;}

private HashMap<String, BoxData> mailboxes = new HashMap<String, BoxData>();

private MailboxSaveData saveData = null;

public Collection<String> getBoxNames()
  {
  return this.mailboxes.keySet();
  }

public void handleWorldLoad(World world)
  {
  if(world.isRemote || this.saveData!=null)
    {
    return;
    }
  saveData = MailboxSaveData.get(world);
  }

public boolean tryAddMailbox(String name, int size)
  {
  if(this.mailboxes.containsKey(name))
    {
    return false;
    }
  this.mailboxes.put(name, new BoxData(name, size));
  return true;
  }

public void updateMailTicks()
  {
  for(BoxData d : this.mailboxes.values())
    {
    d.updateTick();
    }
  } 

public boolean tryRemoveMailbox(String name, TEMailBoxBase mailBox)
  {
  BoxData data = mailboxes.get(name);
  if(data==null)
    {
    return true;
    }
  if(!data.canBeRemoved()){return false;}
  else if(data.isAssigned())
    {
    World world = MinecraftServer.getServer().worldServerForDimension(data.dimID);
    if(world!=null && world.blockExists(data.posX(), data.posY(), data.posZ()))
      {
      TileEntity te = world.getBlockTileEntity(data.posX(), data.posY(), data.posZ());
      if(te instanceof TEMailBoxBase)
        {
        TEMailBoxBase tem = (TEMailBoxBase)te;
        if(tem.getBoxData() == data && tem !=mailBox)
          {
          return false;
          }
        }      
      }
    else
      {
      /**
       * chunk not loaded, return false
       */
      return false;
      }    
    }
  this.mailboxes.remove(name);
  return true;
  }

public boolean tryAssignMailbox(TEMailBoxBase te, String name)
  {
  if(this.mailboxes.containsKey(name))
    {    
    BoxData data = this.mailboxes.get(name);
    if(!data.isAssigned())
      {
      if(te.getBoxData()!=null)
        {
        te.getBoxData().clearAssignment();
        }
      te.setBoxData(data);
      Config.logDebug("assigning mailbox for name: "+name);
      this.mailboxes.get(name).handleAssignment(te);
      return true;
      }
    }
  return false;
  }

public BoxData getBoxDataFor(String name, int size)
  {
  if(!this.mailboxes.containsKey(name))
    {
    this.mailboxes.put(name, new BoxData(name, size));
    }
  return this.mailboxes.get(name);
  }

public void markDirty()
  {
  if(this.saveData!=null)
    {
    this.saveData.markDirty();
    }
  }

public void resetTrackedData()
  {
  this.mailboxes.clear();
  this.saveData = null;
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


}
