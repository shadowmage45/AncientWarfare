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
package shadowmage.ancient_structures.common.template.build;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;
import shadowmage.ancient_structures.common.template.rule.TemplateRuleEntity;
import shadowmage.ancient_structures.common.world_gen.WorldStructureGenerator;

public class StructureBuilder
{

StructureTemplate template;
World world;
int turns;
int maxPriority = 4;
int currentPriority;//current build priority...may not be needed anymore?
int currentX, currentY, currentZ;//coords in template
int destXSize, destYSize, destZSize;
BlockPosition destination;

BlockPosition min;
BlockPosition max;

boolean isFinished = false;

public StructureBuilder(World world, StructureTemplate template, int face, int x, int y, int z)
  {
  this.world = world;
  this.template = template;
  destination = new BlockPosition();   
  currentX = currentY = currentZ = 0;
  destXSize = template.xSize;
  destYSize = template.ySize;
  destZSize = template.zSize;
  currentPriority = 0;
  
  turns = ((face+2)%4);
  int swap;
  for(int i = 0; i<turns; i++)
    {
    swap = destXSize;
    destXSize = destZSize;
    destZSize = swap;
    }
    
  /**
   * here we take the back-left corner in template space
   */
  BlockPosition destinationKey = new BlockPosition(0, 0, 0);
  
  /**
   * and we rotate that corner into local space
   */
  BlockTools.rotateInArea(destinationKey, template.xSize, template.zSize, turns);
  
  /**
   * we are placing destination1 to be the back-let corner of the structure. offset it by the rotated corner to get the correct corner
   */
  BlockPosition destination1 = new BlockPosition(x-destinationKey.x, y-destinationKey.y, z-destinationKey.z);
  
  /**
   * next, offset the back-left corner by the structures build-key offsets
   */
  destination1.moveLeft(face, template.xOffset);
  destination1.moveForward(face, template.zOffset);
  destination1.y-=template.yOffset;
  
  /**
   * copy position to make the front-right corner.
   */
  BlockPosition destination2 = new BlockPosition(destination1);
  
  /**
   * offset this position directly by the size of the structure to get the actual front-right corner
   */
  destination2.offset(destXSize-1, template.ySize-1, destZSize-1);            
  
  /**
   * calculate structure bounding box min/max from destination 1 and destination 2
   */
  min = BlockTools.getMin(destination1, destination2);
  max = BlockTools.getMax(destination1, destination2);     
  
  /**
   * initialize the first target destination so that the structure is ready to start building when called on to build
   */
  incrementDestination();
  }

public void instantConstruction()
  {
  while(!this.isFinished)
    {
    this.placeCurrentPosition();
    }
  this.placeEntities();
  }

protected void placeEntities()
  {   
  TemplateRuleEntity[] rules = template.getEntityRules();
  for(TemplateRuleEntity rule : rules)
    {
	if(rule==null){continue;}
	AWLog.logDebug("placing entity from rule..."+rule);
    destination.x = rule.x;
    destination.y = rule.y;
    destination.z = rule.z;
    BlockTools.rotateInArea(destination, destXSize, destZSize, turns);
    destination.offsetBy(min);
    rule.handlePlacement(world, turns, destination.x, destination.y, destination.z);
    }
  }

protected void placeCurrentPosition()
  {
  TemplateRule rule = template.getRuleAt(currentX, currentY, currentZ);
  if(rule!=null)
    {
    placeRule(rule);
    }
  else
    {
    if(!template.getValidationSettings().preserveBlocks)
      {
      world.setBlockToAir(destination.x, destination.y, destination.z);
      }
    }
  if(incrementPosition())
    {
    incrementDestination();
    }
  else
    {
    this.isFinished = true;
    }
  }

protected void placeRule(TemplateRule rule)
  {  
  if(rule.shouldPlaceOnBuildPass(world, turns, destination.x, destination.y, destination.z, currentPriority))
    {
    rule.handlePlacement(world, turns, destination.x, destination.y, destination.z);    
    }
  }

protected void incrementDestination()
  {
  destination.reassign(currentX, currentY, currentZ);
  BlockTools.rotateInArea(destination, template.xSize, template.zSize, turns);
  destination.offsetBy(min);
  }

/**
 * return true if could increment position
 * return false if template is finished
 * @return
 */
protected boolean incrementPosition()
  {
  currentX++;
  if(currentX>=template.xSize)
    {
    currentX = 0;
    currentZ++;
    if(currentZ>=template.zSize)
      {
      currentZ = 0;
      currentY++;
      if(currentY>=template.ySize)
        {
        currentY = 0;
        currentPriority++;
        if(currentPriority>maxPriority)
          {
          currentPriority = 0;
          return false;
          }
        }
      }
    }
  return true;
  }

protected void doLeveling()
  {
  int bx, by, bz;
  if(!template.getValidationSettings().isDoLeveling()){return;}
  for(bx = this.min.x; bx<=this.max.x; bx++)
    {
    for(bz = this.min.z; bz<=this.max.z; bz++)
      {
      for(by = this.min.y+template.yOffset; by<=this.min.y+template.yOffset+template.getValidationSettings().getMaxLeveling(); by++)
        {
        world.setBlockToAir(bx, by, bz);
        }
      }
    }
  }

protected void doBorderLeveling()
  {
  if(!template.getValidationSettings().doBorderLeveling || template.getValidationSettings().getBorderMaxLeveling()<=0){return;}  
  int bx, by, bz;
  int borderSize = template.getValidationSettings().getBorderSize();
  int maxLeveling = template.getValidationSettings().getBorderMaxLeveling();
  int stepNumber = 0;
  int stepHeight = borderSize / maxLeveling;
  int targetY = min.y + template.yOffset;
  int leveledY;
  int actualHeight;
  boolean gradient = template.getValidationSettings().isGradientBorder();
  if(borderSize>0)
    {
    //check min and max Z along X axis, and corners
    for(bx = min.x-borderSize; bx <= max.x+borderSize; bx++)
      {
      for(bz = min.z-1; bz >= min.z - borderSize; bz--)
        {
        stepNumber=getStepNumber(bx, bz, min.x, max.x, min.z, max.z);
        leveledY = gradient? targetY + stepNumber*stepHeight : targetY; 
        actualHeight = WorldStructureGenerator.getTargetY(world, bx, bz);
        if(actualHeight>leveledY)
          {
          for(by = actualHeight; by>= leveledY; by--)
            {
            world.setBlockToAir(bx, by, bz);
            }
          }
        }      
      for(bz = max.z+1; bz <= max.z + borderSize; bz++)
        {
        stepNumber=getStepNumber(bx, bz, min.x, max.x, min.z, max.z);
        leveledY = gradient? targetY + stepNumber*stepHeight : targetY; 
        actualHeight = WorldStructureGenerator.getTargetY(world, bx, bz);
        if(actualHeight>leveledY)
          {
          for(by = actualHeight; by>= leveledY; by--)
            {
            world.setBlockToAir(bx, by, bz);
            }
          }
        }
      }
    //check min+1 and max-1 X along Z axis (already checked corners in previous test)
    for(bz = min.z; bz <= max.z; bz++)
      {
      for(bx = min.x-1; bx >= min.x - borderSize; bx--)
        {
        stepNumber=getStepNumber(bx, bz, min.x, max.x, min.z, max.z);
        leveledY = gradient? targetY + stepNumber*stepHeight : targetY; 
        actualHeight = WorldStructureGenerator.getTargetY(world, bx, bz);
        if(actualHeight>leveledY)
          {
          for(by = actualHeight; by>= leveledY; by--)
            {
            world.setBlockToAir(bx, by, bz);
            }
          }
        }
      for(bx = max.x+1; bx <= max.x + borderSize; bx++)
        {
        stepNumber=getStepNumber(bx, bz, min.x, max.x, min.z, max.z);
        leveledY = gradient? targetY + stepNumber*stepHeight : targetY; 
        actualHeight = WorldStructureGenerator.getTargetY(world, bx, bz);
        if(actualHeight>leveledY)
          {
          for(by = actualHeight; by>= leveledY; by--)
            {
            world.setBlockToAir(bx, by, bz);
            }
          }
        }
      }  
    }
  }

protected void doBorderFill()
  {
  if(!template.getValidationSettings().doBorderFill || template.getValidationSettings().getBorderMaxFill()<=0){return;}  
  int bx, by, bz;
  int borderSize = template.getValidationSettings().getBorderSize();
  int maxFill = template.getValidationSettings().getBorderMaxFill();
  
  int stepNumber = 0;
  int stepHeight = borderSize / maxFill;
  
  int targetY = min.y + template.yOffset;
  
  int filledBottomY;
  
  int topEmptyBlockY;
  boolean gradient = template.getValidationSettings().isGradientBorder();
  if(borderSize>0)
    {
    //check min and max Z along X axis, and corners
    for(bx = min.x-borderSize; bx <= max.x+borderSize; bx++)
      {
      for(bz = min.z-1; bz >= min.z - borderSize; bz--)
        {        
        stepNumber = getStepNumber(bx, bz, min.x, max.x, min.z, max.z);
        filledBottomY = gradient? targetY - stepNumber*stepHeight : targetY;         
        topEmptyBlockY = WorldStructureGenerator.getTargetY(world, bx, bz)+1;
        if(topEmptyBlockY <= filledBottomY)
          {
          for(by = topEmptyBlockY; by<=filledBottomY; by++)
            {
            world.setBlock(bx, by, bz, Block.cobblestone.blockID);
            }
          }
        }      
      for(bz = max.z+1; bz <= max.z + borderSize; bz++)
        {
        stepNumber = getStepNumber(bx, bz, min.x, max.x, min.z, max.z);    
        filledBottomY = gradient? targetY - stepNumber*stepHeight : targetY;         
        topEmptyBlockY = WorldStructureGenerator.getTargetY(world, bx, bz)+1;
        if(topEmptyBlockY <= filledBottomY)
          {
          for(by = topEmptyBlockY; by<=filledBottomY; by++)
            {
            world.setBlock(bx, by, bz, Block.cobblestone.blockID);
            }
          }
        }
      }
    //check min+1 and max-1 X along Z axis (already checked corners in previous test)
    for(bz = min.z; bz <= max.z; bz++)
      {
      for(bx = min.x-1; bx >= min.x - borderSize; bx--)
        {
        stepNumber = getStepNumber(bx, bz, min.x, max.x, min.z, max.z);
        filledBottomY = gradient? targetY - stepNumber*stepHeight : targetY;         
        topEmptyBlockY = WorldStructureGenerator.getTargetY(world, bx, bz)+1;
        if(topEmptyBlockY <= filledBottomY)
          {
          for(by = topEmptyBlockY; by<=filledBottomY; by++)
            {
            world.setBlock(bx, by, bz, Block.cobblestone.blockID);
            }
          }
        }
      for(bx = max.x+1; bx <= max.x + borderSize; bx++)
        {
        stepNumber = getStepNumber(bx, bz, min.x, max.x, min.z, max.z);
        filledBottomY = gradient? targetY - stepNumber*stepHeight : targetY;         
        topEmptyBlockY = WorldStructureGenerator.getTargetY(world, bx, bz)+1;
        if(topEmptyBlockY <= filledBottomY)
          {
          for(by = topEmptyBlockY; by<=filledBottomY; by++)
            {
            world.setBlock(bx, by, bz, Block.cobblestone.blockID);
            }
          }
        }
      }  
    }
  }

private int getStepNumber(int x, int z, int minX, int maxX, int minZ, int maxZ)
  {
  int steps = 0;
  if(x<minX)
    {
    steps += minX - x;
    }
  else if(x > maxX)
    {
    steps += x - maxX;
    }  
  if(z<minZ)
    {
    steps += minZ - z;
    }
  else if(z > maxZ)
    {
    steps += z - maxZ;
    }  
  return steps;
  }

protected void doFill()
  {
  if(!template.getValidationSettings().isDoFillBelow()){return;}
  String biomeName;
  BiomeGenBase biome;
  int id;
  for(int bx = this.min.x; bx<=this.max.x; bx++)
    {
    for(int bz = this.min.z; bz<=this.max.z; bz++)
      {
      for(int by = this.min.y; by>=this.min.y-template.getValidationSettings().getMaxFill(); by--)
        {
        if(world.getBlockId(bx, by, bz)!=0){continue;}
        biome = world.getBiomeGenForCoords(bx, bz);
        if(by >= this.min.y+template.yOffset-4)
          {
          id = biome.topBlock;
          if(id<=0){id = Block.grass.blockID;}
          }
        else
          {
          id = biome.fillerBlock;
          if(id<=0){id = Block.stone.blockID;}
          }        
        world.setBlock(bx, by, bz, id);
        }
      }
    }
  }
}
