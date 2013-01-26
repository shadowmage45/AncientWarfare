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
package shadowmage.ancient_warfare.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.block.TEBuilder;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.build.BuilderTicked;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.utils.BlockLoader;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBuilderTicked extends ItemBuilderInstant
{

/**
 * @param itemID
 * @param hasSubTypes
 */
public ItemBuilderTicked(int itemID)
  {
  super(itemID);
  }

protected void attemptConstruction(World world, ProcessedStructure struct, int face, BlockPosition hit)
  {
  BlockPosition offsetHit = hit.copy();
  offsetHit.moveForward(face, -struct.zOffset + 1);    
  world.setBlockAndMetadata(hit.x, hit.y, hit.z, BlockLoader.instance().builder.blockID, BlockTools.getBlockMetaFromPlayerFace(face));    
  TEBuilder te = (TEBuilder) world.getBlockTileEntity(hit.x, hit.y, hit.z);
  if(te!=null)
    {
    BuilderTicked builder = new BuilderTicked(world, struct, face, offsetHit);
    builder.startConstruction();
    te.setBuilder(builder);
    }  
  } 

@Override
@SideOnly(Side.CLIENT)
public List<AxisAlignedBB> getBBForStructure(EntityPlayer player, String name)
  {  
  StructureClientInfo struct = StructureManager.instance().getClientStructure(name);
  if(struct==null)
    {
    return null;
    }
  int face = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);  
  BlockPosition originalHit = BlockTools.getBlockClickedOn(player, player.worldObj, true);
  if(originalHit==null)
    {
    return null;
    }
  BlockPosition hit = originalHit.copy();    
  hit.moveForward(face, -struct.zOffset + 1);
  hit = this.offsetForWorldRender(hit, face);
  AxisAlignedBB b = struct.getBBForRender(hit, face);  
  ArrayList<AxisAlignedBB> bbs = new ArrayList<AxisAlignedBB>();
  bbs.add(b);
    
  /**
   * get single-block BB for the builder block
   */
  hit = originalHit;
  b = AxisAlignedBB.getBoundingBox(hit.x, hit.y, hit.z, hit.x+1, hit.y+1, hit.z+1);
  bbs.add(b);
    
  return bbs;
  }


}
