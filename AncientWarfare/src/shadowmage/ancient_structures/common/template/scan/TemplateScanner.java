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
package shadowmage.ancient_structures.common.template.scan;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_structures.AWStructures;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;

public class TemplateScanner
{

TemplateRule[] rules;
    
public TemplateScanner()
  {
  
  }

/**
 * 
 * @param world
 * @param min
 * @param max
 * @param key 
 * @param turns # of turns for proper orientation
 * @return
 */
public StructureTemplate scan(World world, BlockPosition min, BlockPosition max, BlockPosition key, int turns)
  {
  int xSize = max.x - min.x+1;
  int ySize = max.y - min.y+1;
  int zSize = max.z - min.z+1;
  
  int xOutSize, zOutSize;
  xOutSize = xSize;
  zOutSize = zSize;
    
  int swap;
  for(int i = 0; i < turns; i++)
    {
    swap = xOutSize;
    xOutSize = zOutSize;
    zOutSize = swap;
    }    
    
  key.x = key.x - min.x;
  key.y = key.y - min.y;
  key.z = key.z - min.z; 
  
  short[] templateRuleData = new short[xSize*ySize*zSize];
  BlockTools.rotateInArea(key, xSize, zSize, turns);    
  
  List<TemplateRule> currentBlockRules = new ArrayList<TemplateRule>();
  List<TemplateRule> currentEntityRules = new ArrayList<TemplateRule>();
  List<TemplateRule> currentRulesAll = new ArrayList<TemplateRule>();
  Block scannedBlock;
  Entity scannedEntity = null;
  TemplateRule scannedRule;
  
  int index;
  int scanX, scanZ, scanY;
  BlockPosition destination = new BlockPosition();
  int nextRuleID = 1;
  for(scanY = min.y; scanY<=max.y; scanY++)  
    {
    for(scanZ = min.z; scanZ<=max.z; scanZ++)
      {
      for(scanX = min.x; scanX<=max.x; scanX++)
        {
        destination.x = scanX - min.x;
        destination.y = scanY - min.y; 
        destination.z = scanZ - min.z;
        AWLog.logDebug("input :"+destination);
        BlockTools.rotateInArea(destination, xSize, zSize, turns);
        AWLog.logDebug("output:"+destination);
        scannedBlock = Block.blocksList[world.getBlockId(scanX, scanY, scanZ)];
        if(scannedBlock!=null)
          {
          scannedRule = AWStructures.instance.pluginManager.getRuleForBlock(world, scannedBlock, turns, scanX, scanY, scanZ, currentBlockRules);   
          if(scannedRule!=null)
            {
            currentBlockRules.add(scannedRule);
            scannedRule.ruleNumber = nextRuleID;
            nextRuleID++;
            }
          }
        else if(scannedEntity!=null)
          {       
          scannedRule = AWStructures.instance.pluginManager.getRuleForEntity(world, scannedEntity, turns, scanX, scanY, scanZ, currentEntityRules);
          if(scannedRule!=null)
            {
            currentEntityRules.add(scannedRule);
            scannedRule.ruleNumber = nextRuleID;
            nextRuleID++;
            }
          }
        else
          {
          scannedRule = null;
          }
        if(scannedRule!=null)
          {
          currentRulesAll.add(scannedRule);
          index = StructureTemplate.getIndex(destination.x, destination.y, destination.z, xOutSize, ySize, zOutSize);
          templateRuleData[index] = (short) scannedRule.ruleNumber;
          }        
        }
      }
    }  
  TemplateRule[] templateRules = new TemplateRule[currentRulesAll.size()];
  for(int i = 0; i < templateRules.length; i++)
    {
    templateRules[i] = currentRulesAll.get(i);
    }
  StructureTemplate template = new StructureTemplate("testTemplate"+System.currentTimeMillis(), xOutSize, ySize, zOutSize, key.x, key.y, key.z);
  template.setTemplateData(templateRuleData);
  template.setRuleArray(templateRules);
  AWLog.logDebug("template :\n"+template);
  return template;
  }

}
