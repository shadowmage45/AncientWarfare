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
package shadowmage.ancient_warfare.common.machine;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.inventory.AWInventoryMailbox;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.tracker.MailboxData;
import shadowmage.ancient_warfare.common.tracker.entry.BoxData;
import shadowmage.ancient_warfare.common.utils.Trig;

public class TEMailBoxIndustrial extends TEMailBoxBase
{

int[] bottomIndices = new int[18];
int[] otherIndices = new int[27];
/**
 * 
 */
public TEMailBoxIndustrial()
  {
  this.mailBoxSize = 45;
  this.guiNumber = GUIHandler.MAILBOX_INDUSTRIAL;
  this.inventory = new AWInventoryMailbox(mailBoxSize, null);
  int index = 0;  
  for(int i = 0; i < 18; i++, index++)
    {
    bottomIndices[i]=index;
    }
  for(int i = 0; i <27; i++, index++)
    {
    otherIndices[i] = index;
    }  
  }

@Override
public void updateEntity()
  {
  if(this.worldObj==null || this.worldObj.isRemote || this.boxData==null){return;}
  this.mailTicks++;
  if(this.mailTicks>=Config.mailSendTicks)
    {
    this.mailTicks = 0;
    /**
     * check each side destination in data, if valid destination, send packages
     */
    String destination = null;
    int[] slots;
    int slot;
    ItemStack stack;
    BoxData data;
    destination = this.boxData.getSideName(1);      
    if(destination!=null)
      {
      data=MailboxData.instance().getBoxDataFor(destination, mailBoxSize);
      if(data!=null)
        {
        slots = this.otherIndices;
        for(int k = 0; k < slots.length; k++)
          {
          slot = slots[k];
          stack = this.getStackInSlot(slot);
          if(stack==null){continue;}
          
          float dist = Trig.getDistance(xCoord, yCoord, zCoord, data.posX(), data.posY(), data.posZ());
          data.addIncomingItem(stack, (int)dist*5);
          this.setInventorySlotContents(slot, null);
          } 
        }
      }  
    }
  }

@Override
public int[] getAccessibleSlotsFromSide(int var1)
  {
  return var1==0? bottomIndices : otherIndices;
  }



}
