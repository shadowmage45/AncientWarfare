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
package shadowmage.ancient_structures.common.template.plugin;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;

public abstract class StructureContentPlugin
{

/**
 * add to the input list any blocks that this plugin handles
 * @param handledBlocks
 */
public abstract void addHandledBlocks(List<Block> handledBlocks);

/**
 * add to the input list any entities that this plugin handles
 * @param handledEntities
 */
public abstract void addHandledEntities(List<Class> handledEntities);

/**
 * called when a block this plugin is registered to handle is scanned.
 * should return a templateRule capable of handling the placement of this
 * block during structure building
 * @param world
 * @param block
 * @param facing --
 * @param x -- world x coord
 * @param y -- world y coord
 * @param z -- world z coord
 * @return
 */
public abstract TemplateRule getRuleForBlock(World world, Block block, int facing, int x, int y, int z, List<TemplateRule> priorRules);

/**
 * called when an entity this plugin is registered to handle is scanned
 * should return a templateRule capable of handling the placement of this
 * entity during structure building
 * @param world --
 * @param entity --
 * @param facing --
 * @param x -- world x coord
 * @param y -- world y coord
 * @param z -- world z coord
 * @return
 */
public abstract TemplateRule getRuleForEntity(World world, Entity entity, int facing, int x, int y, int z, List<TemplateRule> priorRules);

/**
 * @param ruleLines -- the template lines to parse into a templateRule
 * @return a fully populated rule ready to handle placement tasks
 */
public abstract TemplateRule parseRule(String[] ruleLines);

}
