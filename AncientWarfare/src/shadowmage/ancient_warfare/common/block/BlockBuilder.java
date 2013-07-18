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
package shadowmage.ancient_warfare.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public class BlockBuilder extends AWBlockContainer
{
/**
   * @param par1
   * @param par2Material
   */
public BlockBuilder(int par1)
  {
  super(par1, Material.rock, "AWBuilderBlock");
  this.setCreativeTab(null);
  }

@Override
public boolean onBlockClicked(World world, int posX, int posY, int posZ,  EntityPlayer player, int sideHit, float hitVecX, float hitVecY,   float hitVecZ)
  {
  //NOOP
  return false;
  }

@Override
public IInventory[] getInventoryToDropOnBreak(World world, int x, int y, int z,  int par5, int par6)
  {
  //NOOP
  return null;
  }

@Override
public void breakBlock(World world, int x, int y, int z, int par5, int par6)
  {
  TEBuilder builder = (TEBuilder) world.getBlockTileEntity(x, y, z);
  if(builder!=null)
    {
    builder.removeBuilder();
    }
  super.breakBlock(world, x, y, z, par5, par6);
  }

@Override
public TileEntity getNewTileEntity(World world, int meta)
  {
  return new TEBuilder();
  }

@Override
public void registerIcons(IconRegister reg, Description d)
  {
  d.setIcon(reg.registerIcon("ancientwarfare:builder/builderBottom"), 0);
  d.setIcon(reg.registerIcon("ancientwarfare:builder/builderTop"), 1);
  d.setIcon(reg.registerIcon("ancientwarfare:builder/builderFront"), 2);
  d.setIcon(reg.registerIcon("ancientwarfare:builder/builderSides"), 3);
  }

@Override
public Icon getIcon(int side, int meta)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(blockID);
  if(d!=null)
    {
    if(side==0)
      {
      return d.getIconFor(0);
      }
    else if (side==1)
      {
      return d.getIconFor(1);
      }
    else if(side==meta)
      {
      return d.getIconFor(2);
      }
    else
      {
      return d.getIconFor(3);
      }
    }
  return super.getIcon(side, meta);
  }


}
