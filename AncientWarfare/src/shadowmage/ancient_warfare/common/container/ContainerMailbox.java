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
package shadowmage.ancient_warfare.common.container;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.machine.TEMailBox;
import shadowmage.ancient_warfare.common.tracker.MailboxData;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class ContainerMailbox extends ContainerBase
{

TEMailBox te;

public String boxName = null;
public String topDestination = null;
public String[] sideDestinations = new String[4];
public Set<String> boxNames = new HashSet<String>();


/**
 * @param openingPlayer
 * @param synch
 */
public ContainerMailbox(EntityPlayer openingPlayer, TEMailBox te)
  {
  super(openingPlayer, null);
  this.te = te;
  this.addPlayerSlots(openingPlayer, 46, 156, 4);
  
  int index = 0; 
  int x;
  int y;
  int xPos;
  int yPos;
  int i;
  
  //46,108
  for(i = 0, x = 0, y = 0, xPos=46, yPos=108; i < 18; i++, index++)
    {    
    this.addSlotToContainer(new Slot(te, index, x*18+xPos, y*18+yPos)); 
    x++;
    if(x>8)
      {
      x=0;
      y++;
      }
    } 
  
  //92,30
  for(i = 0, x = 0, y = 0, xPos=92, yPos=30; i < 4; i++, index++, x++)
    {
    this.addSlotToContainer(new Slot(te, index, x*18+xPos, y*18+yPos));    
    }  
  
  for(i = 0, x = 0, y = 0; i < 4; i++)
    {
    x = 0;    
    switch(i)
    {
    case 0:
    //12,30
    xPos =12;
    yPos =30;
    break;

    case 1:
    //12,78
    xPos = 12;
    yPos = 78;
    break;
    
    case 2:    
    //172,30
    xPos = 172;
    yPos = 30;
    break;
    
    case 3:
    //172,78
    xPos = 172;
    yPos = 78;
    break;    
    }
    for(int k = 0; k <4; k++, index++, x++)
      {
      this.addSlotToContainer(new Slot(te, index, x*18+xPos, y*18+yPos));      
      }
    }
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
      this.setBoxName(box, name);
      }
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }
  if(tag.hasKey("add"))
    {
    if(!MailboxData.instance().tryAddMailbox(tag.getString("add")))
      {
      NBTTagCompound reply = new NBTTagCompound();
      reply.setString("reject", "Could not add name, duplicate name detected.");
      this.sendDataToGUI(reply);      
      }
    }
  if(tag.hasKey("remove"))
    {
    if(!MailboxData.instance().tryRemoveMailbox(tag.getString("remove")))
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
      te.setBoxName(box, name);
      this.sendDataToGUI(reply);   
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
  
  NBTTagList boxList = null;
  NBTTagCompound tag = null;
  for(int i = 0; i < 6 ; i++)
    {
    if(!this.equals(this.getBoxName(i), te.getBoxName(i)))
      {
      if(updateTag==null){updateTag = new NBTTagCompound();}      
      if(boxList==null)
        {
        boxList = new NBTTagList();
        }
      tag = new NBTTagCompound();
      tag.setInteger("box", i);
      if(te.getBoxName(i)!=null)
        {
        tag.setString("name", te.getBoxName(i));
        }
      boxList.appendTag(tag);      
      this.setBoxName(i, te.getBoxName(i));
      }
    }
  if(boxList!=null)
    {
    updateTag.setTag("boxList", boxList);
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

public String getBoxName(int side)
  {
  switch(side)
  {
  case 0:
  return this.boxName;
  case 1:
  return this.topDestination;
  case 2:
  case 3:
  case 4:
  case 5:
  return this.sideDestinations[BlockTools.getCardinalFromSide(side)];
  default:
  return null;
  }
  }

public void setBoxName(int side, String name)
  {
  switch(side)
  {
  case 0:
  this.boxName = name;
  break;
  
  case 1:
  this.topDestination = name;
  break;
  
  case 2:
  case 3:
  case 4:
  case 5:
  this.sideDestinations[BlockTools.getCardinalFromSide(side)]=name;
  }
  }

}
