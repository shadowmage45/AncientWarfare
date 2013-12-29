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

import net.minecraft.block.Block;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureBB;
import shadowmage.ancient_structures.common.world_gen.WorldStructureGenerator;

public class StructureValidatorWater extends StructureValidator
{

int borderSize = 0;

/**
 * @param validationType
 */
public StructureValidatorWater()
  {
  super(StructureValidationType.WATER);
  }

@Override
protected void readFromLines(List<String> lines)
  {
  for(String line : lines)
    {
    if(line.toLowerCase().startsWith("border=")){borderSize = StringTools.safeParseInt("=", line);}
    }
  }

@Override
protected void write(BufferedWriter out) throws IOException
  {
  out.write("border="+borderSize);
  out.newLine();
  }

@Override
protected void setDefaultSettings(StructureTemplate template)
  {
  borderSize = (template.ySize - template.yOffset)/5;
  }

@Override
public boolean shouldIncludeForSelection(World world, int x, int y, int z, int face, StructureTemplate template)
  {
  int id = world.getBlockId(x, y-1, z);
  return id==Block.waterMoving.blockID || id==Block.waterStill.blockID;
  }

@Override
public boolean validatePlacement(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {
  int bx, bz, topY;
  int id;
  Block block;
  int minY = bb.min.y;
  for(bx = bb.min.x-borderSize; bx<=bb.max.x+borderSize; bx++)
    {
    bz = bb.min.z-borderSize;
    if(!validateBlock(world, bx, y-1, bz, minY)){ return false; }
              
    bz = bb.max.z+borderSize;
    if(!validateBlock(world, bx, y-1, bz, minY)){ return false; }
    }
  for(bz = bb.min.z-borderSize+1; bz<=bb.max.z+borderSize-1; bz++)
    {
    bx = bb.min.x-borderSize;
    if(!validateBlock(world, bx, y-1, bz, minY)){ return false; }
    
    bx = bb.max.x+borderSize;
    if(!validateBlock(world, bx, y-1, bz, minY)){ return false; }
    }
  return true;
  }

private boolean validateBlock(World world, int x, int y, int z, int minY)
  {
  int id = world.getBlockId(x, y, z);
  if(id!=Block.waterMoving.blockID && id!=Block.waterStill.blockID)
    {
    AWLog.logDebug("water structure rejected for no water block at: "+x+","+y+","+z);
    return false;
    }
  id = world.getBlockId(x, minY, z);
  if(id!=Block.waterMoving.blockID && id!=Block.waterStill.blockID)
    {
    AWLog.logDebug("water structure rejected for no water block at: "+x+","+minY+","+z);
    return false;
    }  
  return true;
  }

@Override
public void preGeneration(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {

  }

@Override
public int getAdjustedSpawnY(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb)
  {
  return y;
  }
}
