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
package shadowmage.ancient_structures.common.structures.build;

import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_structures.common.AWStructures;
import shadowmage.ancient_structures.common.structures.data.BlockData;
import shadowmage.ancient_structures.common.structures.data.ProcessedStructure;
import shadowmage.ancient_structures.common.structures.data.rules.BlockRule;

public class BuilderInstant extends StructureBuilder
{

public BuilderInstant(World world, ProcessedStructure struct, int facing, BlockPosition hit)
  {
  super(world, struct, facing, hit);
  }

@Override
public void startConstruction()
  {  
  long t1;
  if(Config.DEBUG)
    {
    t1 = System.nanoTime();
    }
  this.preConstruction();
  long t2;
  if(Config.DEBUG)
    {
    t2 = System.nanoTime();
    }
  this.instantConstruction();
  if(this.deferredBlocks.isEmpty())
    {
    this.isFinished = true;
    }
  else
    {
    AWStructures.instance.addBuilder(this);
    }      
  this.placeNonBlocks(world); 
  switch(this.struct.getFillType())
  {
  case 1://straight
  this.doFillBeneathStraight(struct.ySize - struct.verticalOffset);
  break;
  
  case 2://inv pyramid
  this.doFillBeneathInvPyramid(struct.ySize - struct.verticalOffset);
  break;
  
  case 3://around
  this.doFillAround(struct.ySize);
  break;
  
  case 4://around+straight
  this.doFillBeneathStraight(struct.ySize - struct.verticalOffset);
  this.doFillAround(struct.ySize);
  break;  
  }
  long t3;
  if(Config.DEBUG)
    {
    t3 = System.nanoTime();
    Config.logDebug("preconst: "+(t2-t1));
    Config.logDebug("const: "+(t3-t2));
    Config.logDebug("Struct gen time: "+(t3-t1));
    }
  }

@Override
public void finishConstruction()
  {
  //NOOP on instant building
  }

@Override
public void onTick()
  {
  this.tickNum++;
  if(this.tickNum<10)
    {
    return;
    }
  for(BlockPosition pos : this.deferredBlocks.keySet())
    {
    BlockData data = this.deferredBlocks.get(pos);
    world.setBlock(pos.x, pos.y, pos.z, data.id, data.meta, 3);
    }
  this.isFinished = true;
  }

public void instantConstruction()
  {  
  for(int priority = 0; priority <=maxPriority; priority++)
    {
    this.buildPriority(priority);
    }
  }

private void buildPriority(int priority)
  {
  for(int x = 0; x< struct.xSize; x++)
    {
    for(int y = 0; y<struct.ySize; y++)
      {
      for(int z = 0; z<struct.zSize; z++)
        {
        BlockRule rule = struct.getRuleAt(x, y, z);
        if(rule==null || rule.order!=priority)
          {
          continue;
          } 
        BlockPosition target = BlockTools.getTranslatedPosition(buildPos, new BlockPosition(x-struct.xOffset,y-struct.verticalOffset, z-struct.zOffset), facing, new BlockPosition(struct.xSize, struct.ySize, struct.zSize));        
        if(shouldSkipBlock(world, rule, target.x, target.y, target.z, priority))
          {
          continue;
          } 
        if(target.y<=2)
        {
        	continue;
        }
        handleBlockRulePlacement(world, target.x, target.y, target.z, rule, false);      
        }
      }
    }
  }


}
