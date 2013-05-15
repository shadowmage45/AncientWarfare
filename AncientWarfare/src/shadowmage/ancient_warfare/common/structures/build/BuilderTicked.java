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
package shadowmage.ancient_warfare.common.structures.build;

import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

/**
 * used by creative mode ticked builder, and survival mode combo scan/builder
 *  
 * @author Shadowmage
 *
 */
public class BuilderTicked extends Builder
{

public int tickTimer = 10;

public BuilderTicked(World world, ProcessedStructure struct, int facing, BlockPosition hit)
  {
  super(world, struct, facing, hit);
  }

protected BuilderTicked(ProcessedStructure struct, int facing, BlockPosition hit)
  {
  super(struct, facing, hit);
  }

@Override
public void startConstruction()
  {
  this.preConstruction();
  }

@Override
public void finishConstruction()
  { 
  
  }

@Override
public void onTick()
  {   
  if(world==null)
    {
    Config.logError("Builder being ticked with a null World!");
    return;
    }
  if(this.isFinished)
    {
    Config.logError("Ticking finished ticked-builder, was not removed from list when finished");
    return;
    }
  
  
  /**
   * timer/counter, only place a block every half-second
   */
  tickNum++;
  if(tickNum<tickTimer)
    {
    return;
    }
  tickNum=0;
  
  BlockRule rule = getCurrentRule();    
          
  BlockPosition target = getCurrentTarget();
  
  /**
   * if current block is of a higher order than current pass, skip until you find a lower/equal block or cannot increment build pass
   */
  while(shouldSkipBlock(world, rule, target.x, target.y, target.z, currentPriority))
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
  
  /**
   * once a valid target has been found, place block
   */
  handleBlockRulePlacement(world, target.x, target.y, target.z, rule, false);
  
  /**
   * and then once again try incrementing
   */
  if(!tryIncrementing())
    {
    this.setFinished();
    return;
    }
  }


private BlockPosition getCurrentTarget()
  {
  return BlockTools.getTranslatedPosition(buildPos, new BlockPosition(currentX-struct.xOffset,currentY-struct.verticalOffset,currentZ-struct.zOffset), facing, new BlockPosition(struct.xSize, struct.ySize, struct.zSize));
  }

private BlockRule getCurrentRule()
  {
  return struct.getRuleAt(currentX, currentY, currentZ);
  }




}
