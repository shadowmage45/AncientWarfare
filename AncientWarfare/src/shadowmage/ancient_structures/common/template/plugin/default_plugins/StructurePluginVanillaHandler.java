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

import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_structures.common.block.BlockDataManager;
import shadowmage.ancient_structures.common.template.plugin.StructureContentPlugin;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;

public class StructurePluginVanillaHandler extends StructureContentPlugin
{

private HashSet<Block> specialHandledBlocks = new HashSet<Block>();

public StructurePluginVanillaHandler()
  {
  
  }

@Override
public void addHandledBlocks(List<Block> handledBlocks)
  {
  Block block;
  for(int i = 0; i < 256; i++)
    {
    block = Block.blocksList[i];
    if(block!=null)
      {
      handledBlocks.add(block);
      }
    }
  
  /**
   * tile-entity / nbt based blocks. 
   * some need proper rotation support in addition to specialized
   * nbt handling 
   */
  specialHandledBlocks.add(Block.chest);
  specialHandledBlocks.add(Block.dropper);
  specialHandledBlocks.add(Block.dispenser);
  specialHandledBlocks.add(Block.enderChest);
  specialHandledBlocks.add(Block.commandBlock);
  specialHandledBlocks.add(Block.mobSpawner);
  specialHandledBlocks.add(Block.signPost);
  specialHandledBlocks.add(Block.signWall);
  specialHandledBlocks.add(Block.furnaceBurning);
  specialHandledBlocks.add(Block.furnaceIdle);
  specialHandledBlocks.add(Block.hopperBlock);
  specialHandledBlocks.add(Block.skull);
  specialHandledBlocks.add(Block.brewingStand);
  }

@Override
public void addHandledEntities(List<Class> handledEntities)
  {
  handledEntities.add(EntityVillager.class);
  handledEntities.add(EntityIronGolem.class);
  handledEntities.add(EntityChicken.class);
  handledEntities.add(EntityCow.class);
  handledEntities.add(EntityPig.class);
  handledEntities.add(EntitySheep.class);
  
  handledEntities.add(EntityHorse.class);  
  handledEntities.add(EntityWolf.class);
  
  handledEntities.add(EntityMinecart.class);
  handledEntities.add(EntityBoat.class);
  
  handledEntities.add(EntityPainting.class);
  handledEntities.add(EntityItemFrame.class);  
  }

@Override
public TemplateRule getRuleForBlock(World world, Block block, int turns, int x, int y, int z, List<TemplateRule> priorRules)
  {
  TemplateRuleVanillaBlocks rule = null;  
  int meta = world.getBlockMetadata(x, y, z);
  if(block!=null)
    {
    meta = BlockDataManager.getRotatedMeta(block.blockID, meta, turns);
    for(TemplateRule r : priorRules)
      {
      if(r.shouldReuseRule(world, block, meta, x, y, z))
        {
        return r;
        }
      }
    rule = new TemplateRuleVanillaBlocks(block, meta);
    if(specialHandledBlocks.contains(block))
      {
      AWLog.logDebug("should add special info to block...");
      }
    }
  return rule;
  }

@Override
public TemplateRule getRuleForEntity(World world, Entity entity, int turns, int x, int y, int z, List<TemplateRule> priorRules)
  {
  return null;
  }

@Override
public TemplateRule parseRule(String[] ruleLines)
  {
  return null;
  }

}
