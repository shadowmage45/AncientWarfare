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
package shadowmage.ancient_warfare.common.aw_structure.build;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_structure.AWStructureModule;
import shadowmage.ancient_warfare.common.aw_structure.data.BlockData;
import shadowmage.ancient_warfare.common.aw_structure.data.BlockDataManager;
import shadowmage.ancient_warfare.common.aw_structure.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.aw_structure.data.rules.BlockRule;


public class BuilderTicked extends Builder
{

int tickNum = 0; 
/**
   * @param world
   * @param struct
   * @param facing
   * @param hit
   */
public BuilderTicked(World world, ProcessedStructure struct, int facing, BlockPosition hit)
  {
  super(world, struct, facing, hit);
  }

@Override
public void startConstruction()
  {
  AWStructureModule.instance().addBuilder(this);
  }

@Override
public void finishConstruction()
  {  
  AWStructureModule.instance().removeBuilder(this);
  }

@Override
public void onTick()
  {   
  if(this.isFinished)
    {
    Config.logError("Ticking finished ticked-builder, was not removed from list when finished");
    return;
    }
  /**
   * timer/counter, only place a block every half-second
   */
  tickNum++;
  if(tickNum<10)
    {
    return;
    }
  tickNum=0;
  
  BlockRule rule = getCurrentRule();    
          
  BlockPosition target = getCurrentTarget();
  
  /**
   * if current block is of a higher order than current pass, skip until you find a lower/equal block or cannot increment build pass
   */
  while(rule.order>currentPriority || rule.ruleNumber==0 && isAirBlock(target))
    {
    if(!incrementCoords())
      {
      if(!incrementBuildPass())
        {
        this.setFinished();
        return;
        }
      }    
    rule = getCurrentRule();
    target = getCurrentTarget();
    }
  
  BlockData data = rule.getBlockChoice(new Random());
  int rotAmt = getRotationAmt(facing);
  int meta = BlockDataManager.instance().getRotatedMeta(data.id, data.meta, rotAmt);
  
  /**
   * place a block once we have found a block to place....
   */
  placeBlock(world, target, data.id, meta);
  
  /**
   * and then once again try incrementing
   */
  if(!tryIncrementing())
    {
    this.setFinished();
    return;
    }
  }

private boolean isAirBlock(BlockPosition target)
  {
  return world.getBlockId(target.x, target.y, target.z)==0;
  }

private BlockPosition getCurrentTarget()
  {
  return BlockTools.getTranslatedPosition(buildPos, new BlockPosition(currentX-struct.xOffset,currentY-struct.verticalOffset,currentZ-struct.zOffset), facing, new BlockPosition(struct.xSize, struct.ySize, struct.zSize));
  }

private BlockRule getCurrentRule()
  {
  return struct.getRuleAt(currentX, currentY, currentZ);
  }


private void placeBlock()
  {
  
  }
}
