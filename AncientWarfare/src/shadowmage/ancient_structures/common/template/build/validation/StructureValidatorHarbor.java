/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_structures.common.template.build.validation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureBB;
import shadowmage.ancient_structures.common.world_gen.WorldStructureGenerator;

public class StructureValidatorHarbor extends StructureValidator
{

BlockPosition testPosition = new BlockPosition();
Set<String> validTargetBlocks;
int maxLeveling;

public StructureValidatorHarbor()
  {
  super(StructureValidationType.HARBOR);
  }

@Override
protected void readFromLines(List<String> lines)
  {

  }

@Override
protected void write(BufferedWriter writer) throws IOException
  {

  }

@Override
protected void setDefaultSettings(StructureTemplate template)
  {

  }

@Override
public boolean shouldIncludeForSelection(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  Block block = Block.blocksList[world.getBlockId(x, y-1, z)];  
  if(block!=null && validTargetBlocks.contains(block.getUnlocalizedName()))
    {
    testPosition.reassign(x, y, z);
    testPosition.moveForward(face, template.zSize-template.zOffset-1);
    for(int by = y-1; by>= by-maxLeveling; by--)
      {
      block = Block.blocksList[world.getBlockId(x, y, z)];
      if(block==Block.waterStill || block==Block.waterMoving)
        {
        return true;
        }
      }
    }
  return false;
  }

@Override
public int getAdjustedSpawnY(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {
  testPosition.reassign(x, y, z);
  testPosition.moveForward(face, template.zSize-template.zOffset-1);
  return WorldStructureGenerator.getTargetY(world, testPosition.x, testPosition.z)+1;
  }

@Override
public boolean validatePlacement(World world, int x, int y, int z, int face,  StructureTemplate template, StructureBB bb)
  {
  int bx, bz;  
  int side;
  for(bx = bb.min.x; bx<=bb.max.x; bx++)
    {
    bz = bb.min.z;
    side = getSide(bx, bz, face, bb);   
    if(!validateBlock(world, bx, bz, side, template, bb)){return false;}
    
    bz = bb.max.z;
    side = getSide(bx, bz, face, bb);
    if(!validateBlock(world, bx, bz, side, template, bb)){return false;}    
    }
  for(bz = bb.min.z+1; bz<=bb.max.z-1; bz++)
    {
    bx = bb.min.x;
    side = getSide(bx, bz, face, bb);
    if(!validateBlock(world, bx, bz, side, template, bb)){return false;}
    
    bx = bb.max.x;
    side = getSide(bx, bz, face, bb);
    if(!validateBlock(world, bx, bz, side, template, bb)){return false;}    
    }
  return true;
  }

/**
 * 0 - front
 * 1 - rear
 * 2 - sides
 */
private int getSide(int x, int z, int face, StructureBB bb)
  {
  switch(face)
  {
  case 0:
    {
    if(z==bb.min.z){return 0;}
    else if(z==bb.max.z){return 1;}
    else if(x==bb.min.x || x==bb.max.x){return 2;}
    }
    break;
  case 1:
    {
    if(x==bb.max.x){return 0;}
    else if(x==bb.min.x){return 1;}
    else if(z==bb.min.z || z==bb.max.z){return 2;}
    }
    break;
  case 2:
    {
    if(z==bb.min.z){return 1;}
    else if(z==bb.max.z){return 0;}
    else if(x==bb.min.x || x==bb.max.x){return 2;}
    }
    break;
  case 3:
    {
    if(x==bb.max.x){return 1;}
    else if(x==bb.min.x){return 0;}
    else if(z==bb.min.z || z==bb.max.z){return 2;}
    }
    break;
  }
  return 0;
  }

private boolean validateBlock(World world, int x, int z, int side, StructureTemplate template, StructureBB bb)
  {  
  boolean ground = (side==0 || side==2);
  boolean water = side!=0;
  return true;
  }

@Override
public void preGeneration(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {
  
  }

}
