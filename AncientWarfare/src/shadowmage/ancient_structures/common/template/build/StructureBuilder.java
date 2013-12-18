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
  turns = ((face+2)%4);
  
  /**
   * here we take the top-left corner, in template space
   */
  BlockPosition destinationKey = new BlockPosition(0, 0, 0);
  
  /**
   * and we rotate that corner into local space
   */
  BlockTools.rotateInArea(destinationKey, template.xSize, template.zSize, turns);
  
  /**
   * our first destination point should be the back-left corner
   * in order to get this point, we add the rotated destinationKey with the input cooridnates
   * we then offset by facing direction for template size and build-key to place the final 
   */
  BlockPosition destination1 = new BlockPosition(x-destinationKey.x, y-destinationKey.y, z-destinationKey.z);
  destination1.moveLeft(face, template.xSize - 1 - (template.xSize - 1 -template.xOffset));
  destination1.moveForward(face, template.zSize - 1 - (template.zSize-1 - template.zOffset));  
  BlockPosition destination2 = new BlockPosition(destination1);
  destination2.offset(destXSize-1, template.ySize-1, destZSize-1);            
  min = BlockTools.getMin(destination1, destination2);
  max = BlockTools.getMax(destination1, destination2);     
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
