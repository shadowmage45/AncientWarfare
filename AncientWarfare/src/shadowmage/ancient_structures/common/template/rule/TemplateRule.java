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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * base template-rule class.  Plugins should define their own rule classes.
 * all data to place the block/entity/target of the rule must be contained in the rule.
 * ONLY one rule per block-position in the template.  So -- no entity/block combination in same space unless
 * handled specially via a plugin rule
 * @author Shadowmage
 */
public abstract class TemplateRule
{

public int ruleNumber = -1;

/**
 * all sub-classes must implement a no-param constructor for when loaded from file (at which point they should initialize from the parseRuleData method)
 */
public TemplateRule()
  {
 
  }

/**
 * input params are the target position for placement of this rule and destination orientation
 * @param world
 * @param turns
 * @param x
 * @param y
 * @param z 
 */
public abstract void handlePlacement(World world, int turns, int x, int y, int z);

public abstract void parseRuleData(List<String> ruleData);
public abstract void writeRuleData(BufferedWriter out) throws IOException;

public abstract void addResources(List<ItemStack> resources);

public abstract boolean shouldPlaceOnBuildPass(World world, int turns, int x, int y, int z, int buildPass);

}
