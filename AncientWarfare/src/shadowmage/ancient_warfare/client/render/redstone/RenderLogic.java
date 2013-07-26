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
package shadowmage.ancient_warfare.client.render.redstone;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.machine.redstone.TERedstoneLogic;
import shadowmage.ancient_warfare.common.machine.redstone.logic.IRedstoneLogicTile;
import shadowmage.ancient_warfare.common.utils.Trig;

public abstract class RenderLogic
{



public RenderLogic()
  {
  }

public abstract void renderTile(IRedstoneLogicTile tile, TERedstoneLogic te, float partialTick);

public void setRenderForSide(ForgeDirection side)
  {
  float x = 0;
  float y = 0;
  float z = 0;
  switch(side)
  {
  case DOWN:
  //x = -90.f;
  break;
  
  case UP:
  x = 180.f;
  break;
  
  case NORTH:
  break;
  
  case SOUTH: 
  y = 180; 
  break;
  
  case EAST:
  y = -90;
  break;
  
  case WEST:
  y = 90;
  break;
  
  default:
  break;
  
  }
  if(x!=0)
    {
    GL11.glRotatef(x, 1, 0, 0);    
    }
  if(y!=0)
    {
    GL11.glRotatef(y, 0, 1, 0);
    }
  if(z!=0)
    {
    GL11.glRotatef(z, 0, 0, 1);
    }
  }

}
