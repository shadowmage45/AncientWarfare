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

import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;

public class StructureBuilder
{

StructureTemplate template;
World world;
int turns;
int maxPriority;
int currentPriority;//current build priority...may not be needed anymore?
int currentX, currentY, currentZ;//coords in template
int destXSize, destYSize, destZSize;
BlockPosition destination;
BlockPosition destinationKey;

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
  
  int swap;
  for(int i = 0; i<turns; i++)
    {
    swap = destXSize;
    destXSize = destZSize;
    destZSize = swap;
    }
  turns = ((face+2)%4);//sets rotation properly
   
  destinationKey = new BlockPosition(template.xOffset, template.yOffset, template.zOffset);
  AWLog.logDebug("destination key preRotate: "+destinationKey);
  BlockTools.rotateInArea(destinationKey, template.xSize, template.zSize, turns);
  AWLog.logDebug("destination key: "+destinationKey);    
  
  BlockPosition destination1 = new BlockPosition(x, y, z);
  destination1.moveLeft(face, template.xSize - 1 - template.xOffset);
  destination1.moveForward(face, template.zSize - 1 - template.zOffset);
  
  switch(face)
  {
  case 0:
  break;
  case 1:
  destination1.x--;
  break;
  case 2:
  destination1.x--;
  destination1.z--;
  break;
  case 3:
  destination1.z--;
  break;
  }
  
  BlockPosition destination2 = new BlockPosition(destination1);
  destination2.offset(destXSize-1, template.ySize-1, destZSize-1);
    
  
  AWLog.logDebug("back left corner should be: "+destination1);
  AWLog.logDebug("front left corner should be: "+destination2);
    
  
  min = BlockTools.getMin(destination1, destination2);
  max = BlockTools.getMax(destination1, destination2);  
  AWLog.logDebug("build area BB: "+min+", " +max + " hit: "+x+", "+y+", "+z + " turns: "+turns);
   
  incrementDestination();//set destination to start piece...
  }

public void instantConstruction()
  {
  while(!this.isFinished)
    {
    this.placeCurrentPosition();
    }
  }

protected void placeCurrentPosition()
  {
  TemplateRule rule = template.getRuleAt(currentX, currentY, currentZ);
  if(rule!=null)
    {
    placeRule(rule);
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
  AWLog.logDebug("handling block placement at: "+destination);
  rule.handlePlacement(world, turns, destination.x, destination.y, destination.z);
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

}
