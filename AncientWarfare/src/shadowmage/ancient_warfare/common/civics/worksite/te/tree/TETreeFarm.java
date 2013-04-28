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
package shadowmage.ancient_warfare.common.civics.worksite.te.tree;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.worksite.WorkPoint;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class TETreeFarm extends TECivic
{

Block woodBlock = Block.wood;
int logMeta = 0;
int saplingID;
int saplingMeta;

/**
 * 
 */
public TETreeFarm()
  {
  
  }

@Override
public boolean onInteract(World world, EntityPlayer player)
  {
  if(!world.isRemote)
    {
    GUIHandler.instance().openGUI(GUIHandler.CIVIC_BASE, player, world, xCoord, yCoord, zCoord);
    }
  return true;
  }

@Override
public void updateWorkPoints()
  {
  super.updateWorkPoints();
  if(woodBlock==null)
    {
    return;
    }
  for(int y = this.minY; y<=this.maxY; y++)
    {
    for(int x = this.minX; x<=this.maxX; x++)
      {
      for(int z = this.minZ; z<=this.maxZ; z++)
        {        
        this.updateOrAddWorkPoint(x, y, z);
        }
      }
    }
  }

protected void updateOrAddWorkPoint(int x, int y, int z)
  {  
  WorkPoint p;
  TargetType t = null;
  int id = worldObj.getBlockId(x, y, z);
  int meta = worldObj.getBlockMetadata(x, y, z);
  if( id==woodBlock.blockID && (meta &3) == this.logMeta )
    {
    t = TargetType.TREE_CHOP;
    }
  else
    {
    return;
    }
  p = new WorkPoint(x,y,z, t, this);
  if(!this.workPoints.contains(p))
    {    
    Config.logDebug("adding new work point to tree farm: "+p);
    this.workPoints.add(p);
    }
  }

@Override
public void onWorkFinished(NpcBase npc, WorkPoint point)
  {
  super.onWorkFinished(npc, point);
  if(point.getTargetType()==TargetType.TREE_CHOP)
    {
    Config.logDebug("chopping tree!!");
    
    int id = worldObj.getBlockId(point.floorX(), point.floorY()-1, point.floorZ());
    if(id==Block.dirt.blockID || id==Block.grass.blockID)
      {
      this.workPoints.add(new WorkPoint(point.floorX(), point.floorY(), point.floorZ(), TargetType.TREE_PLANT, this));
      }
    ArrayList<ItemStack> drops = BlockTools.breakBlock(worldObj, point.floorX(), point.floorY(), point.floorZ(), 0);
    
    }  
  else if(point.getTargetType()==TargetType.TREE_PLANT)
    {
    Config.logDebug("planting sapling");
    int id = worldObj.getBlockId(point.floorX(), point.floorY()-1, point.floorZ());
    if(id==Block.dirt.blockID || id==Block.grass.blockID)
      {
      worldObj.setBlock(point.floorX(), point.floorY(), point.floorZ(), saplingID, saplingMeta, 3);
//      this.workPoints.add(new WorkPoint(point.floorX(), point.floorY(), point.floorZ(), TargetType.TREE_PLANT, this));
      }    
    }
  }



}
