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
package shadowmage.ancient_warfare.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.machine.TEMachine;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class ItemMachine extends AWItemBlockBase
{

/**
 * @param par1
 */
public ItemMachine(int par1)
  {
  super(par1);
  this.bFull3D = true;
  this.hasSubtypes = true;  
  }

@Override
public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
  {
  if(super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
    {
    TileEntity ter = world.getBlockTileEntity(x, y, z);
    if(ter!=null)
      {
      TEMachine te = (TEMachine)world.getBlockTileEntity(x, y, z);
      int face = side;
      if(!te.canPointVertical)
        {
        face = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);
        face = (face+2)%4;
        face = BlockTools.getForgeDirectionFromCardinal(face).ordinal(); 
        }
      ForgeDirection direction = ForgeDirection.getOrientation(face);
      if(!te.facesOpposite)
        {
        direction = direction.getOpposite();
        }
      te.setTeamNum(TeamTracker.instance().getTeamForPlayer(player));
      te.setDirection(direction);           
      te.onBlockPlaced();
      }
    return true;
    }
  return false;
  }

}
