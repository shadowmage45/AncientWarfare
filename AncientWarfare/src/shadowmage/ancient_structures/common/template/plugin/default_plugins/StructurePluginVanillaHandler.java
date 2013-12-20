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

import net.minecraft.block.Block;
import shadowmage.ancient_structures.common.template.plugin.StructureContentPlugin;
import shadowmage.ancient_structures.common.template.plugin.StructurePluginManager;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules.TemplateRuleInventoried;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules.TemplateRuleLogic;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules.TemplateRuleVanillaBlocks;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules.TemplateRuleVanillaDoors;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules.TemplateRuleVanillaSign;

public class StructurePluginVanillaHandler extends StructureContentPlugin
{

HashSet<Block> specialHandledBlocks = new HashSet<Block>();//just a temp cache to keep track of what blocks to not register with blanket block rule

public StructurePluginVanillaHandler()
  {
  
  }

@Override
public void addHandledBlocks(StructurePluginManager manager)
  {  

  specialHandledBlocks.add(Block.doorIron);
  specialHandledBlocks.add(Block.doorWood);
  specialHandledBlocks.add(Block.signPost);
  specialHandledBlocks.add(Block.signWall);
  specialHandledBlocks.add(Block.mobSpawner);
  specialHandledBlocks.add(Block.commandBlock);  
  specialHandledBlocks.add(Block.skull);
  specialHandledBlocks.add(Block.furnaceBurning);
  specialHandledBlocks.add(Block.brewingStand);
  specialHandledBlocks.add(Block.beacon);
  specialHandledBlocks.add(Block.dispenser);
  specialHandledBlocks.add(Block.furnaceIdle);
  specialHandledBlocks.add(Block.chest);
  specialHandledBlocks.add(Block.dropper);
  specialHandledBlocks.add(Block.hopperBlock);
    
  Block block;
  for(int i = 0; i < 256; i++)
    {
    block = Block.blocksList[i];
    if(block!=null && ! specialHandledBlocks.contains(block))
      {
      manager.registerBlockHandler("vanillaBlocks", block, TemplateRuleVanillaBlocks.class);
      }
    } 
  specialHandledBlocks.clear();
  
  manager.registerBlockHandler("vanillaDoors", Block.doorIron, TemplateRuleVanillaDoors.class);
  manager.registerBlockHandler("vanillaDoors", Block.doorWood, TemplateRuleVanillaDoors.class);
  manager.registerBlockHandler("vanillaSign", Block.signPost, TemplateRuleVanillaSign.class);
  manager.registerBlockHandler("vanillaSign", Block.signWall, TemplateRuleVanillaSign.class);
  manager.registerBlockHandler("vanillaLogic", Block.mobSpawner, TemplateRuleLogic.class);
  manager.registerBlockHandler("vanillaLogic", Block.commandBlock, TemplateRuleLogic.class);
  manager.registerBlockHandler("vanillaLogic", Block.brewingStand, TemplateRuleLogic.class);
  manager.registerBlockHandler("vanillaLogic", Block.beacon, TemplateRuleLogic.class);
  manager.registerBlockHandler("vanillaLogic", Block.skull, TemplateRuleLogic.class);
  manager.registerBlockHandler("vanillaLogic", Block.furnaceBurning, TemplateRuleLogic.class);
  manager.registerBlockHandler("vanillaLogic", Block.furnaceIdle, TemplateRuleLogic.class);
  manager.registerBlockHandler("vanillaInventory", Block.dispenser, TemplateRuleInventoried.class);
  manager.registerBlockHandler("vanillaInventory", Block.chest, TemplateRuleInventoried.class);
  manager.registerBlockHandler("vanillaInventory", Block.dropper, TemplateRuleInventoried.class);
  manager.registerBlockHandler("vanillaInventory", Block.hopperBlock, TemplateRuleInventoried.class);
  }


@Override
public void addHandledEntities(StructurePluginManager manager)
  {
  //handledEntities.add(EntityVillager.class);
  //handledEntities.add(EntityIronGolem.class);
  //handledEntities.add(EntityChicken.class);
  //handledEntities.add(EntityCow.class);
  //handledEntities.add(EntityPig.class);
  //handledEntities.add(EntitySheep.class);
  //
  //handledEntities.add(EntityHorse.class);  
  //handledEntities.add(EntityWolf.class);
  //
  //handledEntities.add(EntityMinecart.class);
  //handledEntities.add(EntityBoat.class);
  //
  //handledEntities.add(EntityPainting.class);
  //handledEntities.add(EntityItemFrame.class); 
  }

}
