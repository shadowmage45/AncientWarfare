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
package shadowmage.ancient_structures.common.template.rule;

import net.minecraft.world.World;
import shadowmage.ancient_structures.common.template.plugin.StructureContentPlugin;

/**
 * base template-rule class.  Plugins should define their own rule classes.
 * all data to place the block/entity/target of the rule must be contained in the rule.
 * ONLY one rule per block-position in the template.  So -- no entity/block combination in same space unless
 * handled specially via a plugin rule
 * @author Shadowmage
 */
public abstract class TemplateRule
{

private StructureContentPlugin parentPlugin;//the plugin responsible for this rule

/**
 * input params are the target position for placement of this rule and destination orientation
 * @param world
 * @param facing
 * @param x
 * @param y
 * @param z 
 */
public abstract void handlePlacement(World world, int facing, int x, int y, int z);

/**
 * sub-classes should return a name to be inserted into templates as to a reference to this rule
 * this reference should be unique among rule names to avoid conflict
 * end-users should be able to use this name to look up reference documentation about the rule
 * @return
 */
public abstract String getRuleTypeName();

}
