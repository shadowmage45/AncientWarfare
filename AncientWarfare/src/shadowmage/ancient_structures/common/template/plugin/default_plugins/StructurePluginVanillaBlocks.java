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
package shadowmage.ancient_structures.common.template.plugin.default_plugins;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import shadowmage.ancient_structures.common.template.plugin.StructureContentPlugin;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;

public class StructurePluginVanillaBlocks extends StructureContentPlugin
{

public StructurePluginVanillaBlocks()
  {
  
  }

@Override
public void addHandledBlocks(List<Block> handledBlocks)
  {
  
  }

@Override
public void addHandledEntities(List<Class> handledEntities)
  {
  
  }

@Override
public TemplateRule getRuleForBlock(World world, Block block, int facing, int x, int y, int z, List<TemplateRule> priorRules)
  {
  return null;
  }

@Override
public TemplateRule getRuleForEntity(World world, Entity entity, int facing, int x, int y, int z, List<TemplateRule> priorRules)
  {
  return null;
  }

@Override
public TemplateRule parseRule(String[] ruleLines)
  {
  return null;
  }

}
