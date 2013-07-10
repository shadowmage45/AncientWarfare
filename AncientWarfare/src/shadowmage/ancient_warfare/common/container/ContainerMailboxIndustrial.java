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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import shadowmage.ancient_warfare.common.machine.TEMailBoxBase;

public class ContainerMailboxIndustrial extends ContainerMailboxBase
{

/**
 * @param openingPlayer
 * @param te
 */
public ContainerMailboxIndustrial(EntityPlayer openingPlayer, TEMailBoxBase te)
  {
  super(openingPlayer, te);
  this.addPlayerSlots(openingPlayer, 8, 156, 4);  
  int index = 0; 
  int x;
  int y;
  int xPos;
  int yPos;
  int i;  
  //46,108
  for(i = 0, x = 0, y = 0, xPos=8, yPos=108; i < 18; i++, index++)
    {    
    if(index>=te.getSizeInventory()){break;}
    this.addSlotToContainer(new Slot(te, index, x*18+xPos, y*18+yPos)); 
    x++;
    if(x>8)
      {
      x=0;
      y++;
      }
    } 
  
  //92,30
  for(i = 0, x = 0, y = 0, xPos=8, yPos=30; i < 27; i++, index++)
    {
    if(index>=te.getSizeInventory()){break;}
    this.addSlotToContainer(new Slot(te, index, x*18+xPos, y*18+yPos));
    x++;
    if(x>=9)
      {
      x=0;
      y++;
      }
    } 
  
  }

}
