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
package shadowmage.ancient_warfare.common.world_gen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentVillage;
import net.minecraft.world.gen.structure.ComponentVillageStartPiece;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.structures.build.BuilderInstant;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public abstract class VillageGenComponent extends ComponentVillage
{

private int averageGroundLevel = -1;
ProcessedStructure structure;
BlockPosition hitPos = null;

/**
 * @param start
 * @param par2
 */
public VillageGenComponent(ComponentVillageStartPiece start, Integer par2, Integer face, ProcessedStructure struct, StructureBoundingBox box)
  {
  super(start, par2);
  this.coordBaseMode = face;
  this.structure = struct;
  this.boundingBox = box;
  Config.logDebug("constructing village gen component with bounding box of: "+boundingBox);
  }

@Override
public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox)
  {  
  if(this.structure==null){return false;}
  if (this.averageGroundLevel < 0)
    {
        this.averageGroundLevel = this.getAverageGroundLevel(world, structureboundingbox);

        if (this.averageGroundLevel < 0)
        {
            return true;
        }

        this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + structure.ySize - 1, 0);
    }
  int hostile = 0;
  int team = -1;
  if(VillageGenerator.instance().villageMap.containsKey(startPiece))
    {
    hostile = VillageGenerator.instance().villageMap.get(startPiece);
    if(hostile==0)
      {
      team=16;
      }
    else if(hostile==1)
      {
      team = 17;
      }
    }  
  BlockPosition hit = getPositionFromBoundingBox();
  hit.moveRight(coordBaseMode, structure.xOffset);
  hit.moveBack(coordBaseMode, structure.zOffset);
  hit.y = this.boundingBox.minY;
  
  BuilderInstant builder = new BuilderInstant(world, structure, coordBaseMode, hit);
  if(team>=0)
    {
    builder.setTeamOverride(team);    
    } 
  if(hitPos==null || !hitPos.equals(hit))
    {
    hitPos = hit;
    builder.startConstruction();    
    }
  else
    {
    }
  return true;
  }

public BlockPosition getPositionFromBoundingBox()
  {
  BlockPosition position = new BlockPosition(); 
  if(this.coordBaseMode==0)
    {
    position.x = this.boundingBox.maxX;
    position.z = this.boundingBox.minZ;
    }
  if(this.coordBaseMode==1)
    {
    position.x = this.boundingBox.maxX;
    position.z = this.boundingBox.maxZ;
    }  
  if(this.coordBaseMode==2)
    {
    position.x = this.boundingBox.minX;
    position.z = this.boundingBox.maxZ;
    }
  if(this.coordBaseMode==3)
    {
    position.x = this.boundingBox.minX;
    position.z = this.boundingBox.minZ;
    }
  position.y = this.boundingBox.minY;
  return position;
  }

}
