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

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.interfaces.IEntityContainerSynch;
import shadowmage.ancient_warfare.common.machine.TEFoodProcessor;

public class ContainerFoodProcessor extends ContainerBase
{

TEFoodProcessor te;
/**
 * @param openingPlayer
 * @param synch
 */
public ContainerFoodProcessor(EntityPlayer openingPlayer, TEFoodProcessor te)
  {
  super(openingPlayer, null); 
  this.te = te;
  this.addPlayerSlots(openingPlayer, 8, 8+3*18+8, 4);
  
  
  int x = 0;
  int y = 0;
  for(int i = 1; i <te.getSizeInventory(); i++)
    {    
    this.addSlotToContainer(new Slot(te, i, x*18 + 8 + 3*18, y*18+8));    
    x++;
    if(x>2)
      {
      x=0;
      y++;
      }    
    }
  
  this.addSlotToContainer(new Slot(te, 0, x*18 + 8 + 4*18, y*18+8+3*18+4));
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {

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

}
