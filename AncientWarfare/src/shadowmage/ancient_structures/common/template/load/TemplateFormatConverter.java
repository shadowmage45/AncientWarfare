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
package shadowmage.ancient_structures.common.template.load;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntitySkull;
import shadowmage.ancient_framework.AWFramework;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.NBTTools;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureValidationSettingsDefault;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.StructurePluginAutomation;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.StructurePluginNpcs;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.StructurePluginVehicles;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules.TemplateRuleBlockDoors;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules.TemplateRuleBlockInventory;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules.TemplateRuleBlockLogic;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules.TemplateRuleBlockSign;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules.TemplateRuleModBlocks;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.block_rules.TemplateRuleVanillaBlocks;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.entity_rules.TemplateRuleEntityHanging;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.entity_rules.TemplateRuleEntityLogic;
import shadowmage.ancient_structures.common.template.plugin.default_plugins.entity_rules.TemplateRuleVanillaEntity;
import shadowmage.ancient_structures.common.template.rule.TemplateRule;
import shadowmage.ancient_structures.common.template.rule.TemplateRuleEntity;
import shadowmage.ancient_structures.common.template.save.TemplateExporter;



public class TemplateFormatConverter
{

/**
 * cached TE instances to use for writing out of basic NBT data into tag to use for
 * converted rule
 */
private static TileEntityCommandBlock teCommand = new TileEntityCommandBlock();
private static TileEntitySkull teSkull = new TileEntitySkull();
private static TileEntityDropper teDropper = new TileEntityDropper();
private static TileEntityDispenser teDispenser = new TileEntityDispenser();
private static TileEntityFurnace teFurnace = new TileEntityFurnace();
private static TileEntityHopper teHopper = new TileEntityHopper();
private static TileEntityBrewingStand teBrewingStand = new TileEntityBrewingStand();
private static TileEntityChest teChest = new TileEntityChest();

private static HashSet<Block> specialHandledBlocks = new HashSet<Block>();//just a temp cache to keep track of what blocks to not register with blanket block rule
private static HashSet<Class <?extends Entity>> normalHandledEntities = new HashSet<Class <?extends Entity>>();
private static HashSet<Class <?extends Entity>> hangingEntities = new HashSet<Class <?extends Entity>>();
private static HashSet<Class <?extends Entity>> nbtEntities = new HashSet<Class <?extends Entity>>();
/**
 * pre-built nbt-tags for different entity types for use during conversion
 * map is keyed by mobID from EntityList
 */
private static HashMap<String, NBTTagCompound> entityDefaultTags = new HashMap<String, NBTTagCompound>();

static
{
specialHandledBlocks.add(Block.signPost);//
specialHandledBlocks.add(Block.signWall);//
specialHandledBlocks.add(Block.doorIron);//
specialHandledBlocks.add(Block.doorWood);//
specialHandledBlocks.add(Block.commandBlock);//
specialHandledBlocks.add(Block.mobSpawner);//noop
specialHandledBlocks.add(Block.furnaceBurning);//
specialHandledBlocks.add(Block.furnaceIdle);//
specialHandledBlocks.add(Block.skull);//
specialHandledBlocks.add(Block.brewingStand);//
specialHandledBlocks.add(Block.chest);//
specialHandledBlocks.add(Block.dropper);//
specialHandledBlocks.add(Block.dispenser);//
specialHandledBlocks.add(Block.hopperBlock);//

normalHandledEntities.add(EntityPig.class);//
normalHandledEntities.add(EntitySheep.class);//
normalHandledEntities.add(EntityCow.class);//
normalHandledEntities.add(EntityChicken.class);//
normalHandledEntities.add(EntityBoat.class);//
normalHandledEntities.add(EntityIronGolem.class);//
normalHandledEntities.add(EntityWolf.class);//
normalHandledEntities.add(EntityOcelot.class);//
normalHandledEntities.add(EntityWither.class);//
normalHandledEntities.add(EntitySnowman.class);//

hangingEntities.add(EntityPainting.class);/**TODO**/
hangingEntities.add(EntityItemFrame.class);/**TODO**/

nbtEntities.add(EntityHorse.class);//TECHNICALLY NOOP, BUT HANDLED ANYWAY
nbtEntities.add(EntityVillager.class);//
nbtEntities.add(EntityMinecartChest.class);/**TODO**/
nbtEntities.add(EntityMinecartHopper.class);/**TODO**/
nbtEntities.add(EntityMinecartEmpty.class);//
nbtEntities.add(EntityMinecartFurnace.class);//
nbtEntities.add(EntityMinecartTNT.class);//

/**
 * TODO add conversion tags to conversion-tag map
 */
NBTTagCompound tag;
List<String> lines = new ArrayList<String>();

lines.add("TAG=10={");
lines.add("TAG=9=Items{");
lines.add("}");
lines.add("TAG=9=Pos{");
lines.add("TAG=6={380.5}");
lines.add("TAG=6={4.5}");
lines.add("TAG=6={-1004.5099999904633}");
lines.add("}");
lines.add("TAG=3=PortalCooldown{0}");
lines.add("TAG=9=Motion{");
lines.add("TAG=6={0.0}");
lines.add("TAG=6={-0.0}");
lines.add("TAG=6={0.0}");
lines.add("}");
lines.add("TAG=1=OnGround{0}");
lines.add("TAG=2=Fire{-1}");
lines.add("TAG=3=Dimension{0}");
lines.add("TAG=5=FallDistance{0.0}");
lines.add("TAG=2=Air{300}");
lines.add("TAG=9=Rotation{");
lines.add("TAG=5={90.0}");
lines.add("TAG=5={0.0}");
lines.add("}");
lines.add("TAG=1=Invulnerable{0}");
lines.add("}");
tag= NBTTools.readNBTFrom(lines);
entityDefaultTags.put((String) EntityList.classToStringMapping.get(EntityMinecartChest.class), tag);
lines.clear();

lines.add("TAG=10={");
lines.add("TAG=9=Items{");
lines.add("}");
lines.add("TAG=3=TransferCooldown{0}");
lines.add("TAG=9=Motion{");
lines.add("TAG=6={0.0}");
lines.add("TAG=6={-0.0}");
lines.add("TAG=6={0.04088106621525384}");
lines.add("}");
lines.add("TAG=1=OnGround{0}");
lines.add("TAG=3=Dimension{0}");
lines.add("TAG=2=Air{300}");
lines.add("TAG=9=Pos{");
lines.add("TAG=6={380.5}");
lines.add("TAG=6={4.5}");
lines.add("TAG=6={-1002.7069389818918}");
lines.add("}");
lines.add("TAG=3=PortalCooldown{0}");
lines.add("TAG=2=Fire{-1}");
lines.add("TAG=5=FallDistance{0.0}");
lines.add("TAG=9=Rotation{");
lines.add("TAG=5={90.0}");
lines.add("TAG=5={0.0}");
lines.add("}");
lines.add("TAG=1=Invulnerable{0}");
lines.add("}");
tag= NBTTools.readNBTFrom(lines);
entityDefaultTags.put((String) EntityList.classToStringMapping.get(EntityMinecartHopper.class), tag);
lines.clear();

lines.add("TAG=10={");
lines.add("TAG=9=DropChances{");
lines.add("TAG=5=0{0.085}");
lines.add("TAG=5=1{0.085}");
lines.add("TAG=5=2{0.085}");
lines.add("TAG=5=3{0.085}");
lines.add("TAG=5=4{0.085}");
lines.add("}");
lines.add("TAG=3=Age{0}");
lines.add("TAG=9=Attributes{");
lines.add("TAG=10={");
lines.add("TAG=8=Name{generic.maxHealth}");
lines.add("TAG=6=Base{20.0}");
lines.add("}");
lines.add("TAG=10={");
lines.add("TAG=8=Name{generic.knockbackResistance}");
lines.add("TAG=6=Base{0.0}");
lines.add("}");
lines.add("TAG=10={");
lines.add("TAG=8=Name{generic.movementSpeed}");
lines.add("TAG=6=Base{0.5}");
lines.add("}");
lines.add("TAG=10={");
lines.add("TAG=8=Name{generic.followRange}");
lines.add("TAG=6=Base{16.0}");
lines.add("TAG=9=Modifiers{");
lines.add("TAG=10={");
lines.add("TAG=8=Name{Random spawn bonus}");
lines.add("TAG=4=UUIDLeast{-5775864776407555021}");
lines.add("TAG=3=Operation{1}");
lines.add("TAG=6=Amount{-0.0491561135329342}");
lines.add("TAG=4=UUIDMost{-3773800893355899517}");
lines.add("}");
lines.add("}");
lines.add("}");
lines.add("}");
lines.add("TAG=9=Motion{");
lines.add("TAG=6={0.0}");
lines.add("TAG=6={-0.0784000015258789}");
lines.add("TAG=6={0.0}");
lines.add("}");
lines.add("TAG=8=CustomName{}");
lines.add("TAG=2=Health{20}");
lines.add("TAG=5=HealF{20.0}");
lines.add("TAG=1=CustomNameVisible{0}");
lines.add("TAG=3=Riches{0}");
lines.add("TAG=2=AttackTime{0}");
lines.add("TAG=2=Fire{-1}");
lines.add("TAG=1=Invulnerable{0}");
lines.add("TAG=2=DeathTime{0}");
lines.add("TAG=5=AbsorptionAmount{0.0}");
lines.add("TAG=9=Equipment{");
lines.add("TAG=10={");
lines.add("}");
lines.add("TAG=10={");
lines.add("}");
lines.add("TAG=10={");
lines.add("}");
lines.add("TAG=10={");
lines.add("}");
lines.add("TAG=10={");
lines.add("}");
lines.add("}");
lines.add("TAG=1=OnGround{1}");
lines.add("TAG=2=HurtTime{0}");
lines.add("TAG=3=Profession{3}");
lines.add("TAG=3=Dimension{0}");
lines.add("TAG=2=Air{300}");
lines.add("TAG=9=Pos{");
lines.add("TAG=6={382.11279494825624}");
lines.add("TAG=6={4.0}");
lines.add("TAG=6={-999.4431747913983}");
lines.add("}");
lines.add("TAG=1=CanPickUpLoot{0}");
lines.add("TAG=3=PortalCooldown{0}");
lines.add("TAG=1=PersistenceRequired{0}");
lines.add("TAG=1=Leashed{0}");
lines.add("TAG=5=FallDistance{0.0}");
lines.add("TAG=9=Rotation{");
lines.add("TAG=5={-445.0739}");
lines.add("TAG=5={0.0}");
lines.add("}");
lines.add("}");
tag= NBTTools.readNBTFrom(lines);
entityDefaultTags.put((String) EntityList.classToStringMapping.get(EntityVillager.class), tag);
lines.clear();

lines.add("TAG=10={");
lines.add("TAG=3=Temper{0}");
lines.add("TAG=9=DropChances{");
lines.add("TAG=5=0{0.085}");
lines.add("TAG=5=1{0.085}");
lines.add("TAG=5=2{0.085}");
lines.add("TAG=5=3{0.085}");
lines.add("TAG=5=4{0.085}");
lines.add("}");
lines.add("TAG=3=Age{0}");
lines.add("TAG=9=Attributes{");
lines.add("TAG=10={");
lines.add("TAG=8=Name{generic.maxHealth}");
lines.add("TAG=6=Base{20.0}");
lines.add("}");
lines.add("TAG=10={");
lines.add("TAG=8=Name{generic.knockbackResistance}");
lines.add("TAG=6=Base{0.0}");
lines.add("}");
lines.add("TAG=10={");
lines.add("TAG=8=Name{generic.movementSpeed}");
lines.add("TAG=6=Base{0.2975187803390599}");
lines.add("}");
lines.add("TAG=10={");
lines.add("TAG=8=Name{generic.followRange}");
lines.add("TAG=6=Base{16.0}");
lines.add("TAG=9=Modifiers{");
lines.add("TAG=10={");
lines.add("TAG=8=Name{Random spawn bonus}");
lines.add("TAG=4=UUIDLeast{-8033270966726406722}");
lines.add("TAG=3=Operation{1}");
lines.add("TAG=6=Amount{0.049372350241752114}");
lines.add("TAG=4=UUIDMost{111513669306630993}");
lines.add("}");
lines.add("}");
lines.add("}");
lines.add("TAG=10={");
lines.add("TAG=8=Name{horse.jumpStrength}");
lines.add("TAG=6=Base{0.5816810733145679}");
lines.add("}");
lines.add("}");
lines.add("TAG=1=HasReproduced{0}");
lines.add("TAG=9=Motion{");
lines.add("TAG=6={-0.002262490133446357}");
lines.add("TAG=6={-0.0784000015258789}");
lines.add("TAG=6={0.012293541012387513}");
lines.add("}");
lines.add("TAG=8=CustomName{}");
lines.add("TAG=3=Type{0}");
lines.add("TAG=2=Health{20}");
lines.add("TAG=1=Bred{0}");
lines.add("TAG=5=HealF{20.0}");
lines.add("TAG=1=CustomNameVisible{0}");
lines.add("TAG=2=AttackTime{0}");
lines.add("TAG=2=Fire{-1}");
lines.add("TAG=1=ChestedHorse{0}");
lines.add("TAG=1=Invulnerable{0}");
lines.add("TAG=2=DeathTime{0}");
lines.add("TAG=8=OwnerName{}");
lines.add("TAG=1=Tame{0}");
lines.add("TAG=5=AbsorptionAmount{0.0}");
lines.add("TAG=9=Equipment{");
lines.add("TAG=10={");
lines.add("}");
lines.add("TAG=10={");
lines.add("}");
lines.add("TAG=10={");
lines.add("}");
lines.add("TAG=10={");
lines.add("}");
lines.add("TAG=10={");
lines.add("}");
lines.add("}");
lines.add("TAG=3=InLove{0}");
lines.add("TAG=1=OnGround{1}");
lines.add("TAG=2=HurtTime{0}");
lines.add("TAG=3=Dimension{0}");
lines.add("TAG=2=Air{300}");
lines.add("TAG=9=Pos{");
lines.add("TAG=6={380.349088078742}");
lines.add("TAG=6={4.0}");
lines.add("TAG=6={-999.6999999880791}");
lines.add("}");
lines.add("TAG=1=CanPickUpLoot{0}");
lines.add("TAG=3=PortalCooldown{0}");
lines.add("TAG=1=PersistenceRequired{0}");
lines.add("TAG=1=Leashed{0}");
lines.add("TAG=5=FallDistance{0.0}");
lines.add("TAG=3=Variant{259}");
lines.add("TAG=9=Rotation{");
lines.add("TAG=5={80.99795}");
lines.add("TAG=5={0.0}");
lines.add("}");
lines.add("TAG=1=EatingHaystack{0}");
lines.add("}");
tag= NBTTools.readNBTFrom(lines);
entityDefaultTags.put((String) EntityList.classToStringMapping.get(EntityHorse.class), tag);
lines.clear();

lines.add("TAG=10={");
lines.add("TAG=9=Pos{");
lines.add("TAG=6={380.5}");
lines.add("TAG=6={4.5}");
lines.add("TAG=6={-1003.5}");
lines.add("}");
lines.add("TAG=3=PortalCooldown{0}");
lines.add("TAG=9=Motion{");
lines.add("TAG=6={0.0}");
lines.add("TAG=6={-0.0}");
lines.add("TAG=6={0.0}");
lines.add("}");
lines.add("TAG=1=OnGround{0}");
lines.add("TAG=2=Fire{-1}");
lines.add("TAG=3=Dimension{0}");
lines.add("TAG=5=FallDistance{0.0}");
lines.add("TAG=2=Air{300}");
lines.add("TAG=9=Rotation{");
lines.add("TAG=5={0.0}");
lines.add("TAG=5={0.0}");
lines.add("}");
lines.add("TAG=1=Invulnerable{0}");
lines.add("}");
tag= NBTTools.readNBTFrom(lines);
entityDefaultTags.put((String) EntityList.classToStringMapping.get(EntityMinecartEmpty.class), tag);
lines.clear();

lines.add("TAG=10={");
lines.add("TAG=9=Motion{");
lines.add("TAG=6={0.0}");
lines.add("TAG=6={-0.0}");
lines.add("TAG=6={0.0}");
lines.add("}");
lines.add("TAG=1=OnGround{0}");
lines.add("TAG=6=PushZ{0.0}");
lines.add("TAG=3=Dimension{0}");
lines.add("TAG=2=Air{300}");
lines.add("TAG=9=Pos{");
lines.add("TAG=6={380.5}");
lines.add("TAG=6={4.5}");
lines.add("TAG=6={-1002.5}");
lines.add("}");
lines.add("TAG=3=PortalCooldown{0}");
lines.add("TAG=2=Fire{-1}");
lines.add("TAG=6=PushX{0.0}");
lines.add("TAG=5=FallDistance{0.0}");
lines.add("TAG=9=Rotation{");
lines.add("TAG=5={0.0}");
lines.add("TAG=5={0.0}");
lines.add("}");
lines.add("TAG=1=Invulnerable{0}");
lines.add("TAG=2=Fuel{0}");
lines.add("}");
tag= NBTTools.readNBTFrom(lines);
entityDefaultTags.put((String) EntityList.classToStringMapping.get(EntityMinecartFurnace.class), tag);
lines.clear();

lines.add("TAG=10={");
lines.add("TAG=9=Pos{");
lines.add("TAG=6={380.5}");
lines.add("TAG=6={4.5}");
lines.add("TAG=6={-1001.5}");
lines.add("}");
lines.add("TAG=3=PortalCooldown{0}");
lines.add("TAG=3=TNTFuse{-1}");
lines.add("TAG=9=Motion{");
lines.add("TAG=6={0.0}");
lines.add("TAG=6={-0.0}");
lines.add("TAG=6={0.0}");
lines.add("}");
lines.add("TAG=1=OnGround{0}");
lines.add("TAG=2=Fire{-1}");
lines.add("TAG=3=Dimension{0}");
lines.add("TAG=5=FallDistance{0.0}");
lines.add("TAG=2=Air{300}");
lines.add("TAG=9=Rotation{");
lines.add("TAG=5={0.0}");
lines.add("TAG=5={0.0}");
lines.add("}");
lines.add("TAG=1=Invulnerable{0}");
lines.add("}");

tag= NBTTools.readNBTFrom(lines);
entityDefaultTags.put((String) EntityList.classToStringMapping.get(EntityMinecartTNT.class), tag);
lines.clear();
}


public StructureTemplate convertOldTemplate(File file, List<String> templateLines)
  {
  AWLog.logDebug("should read old template format...");
  
  /**
   * parsed-out data, to be used to construct new template
   */
  List<TemplateRule> parsedRules = new ArrayList<TemplateRule>();
  List<TemplateRuleEntity> parsedEntityRules = new ArrayList<TemplateRuleEntity>();
  short[] templateData = null;
  int xSize = 0, ySize = 0, zSize = 0;
  int xOffset = 0, yOffset = 0, zOffset = 0;
  boolean preserveBlocks;
  int ruleNumber = 0;  
  
  String name = file.getName();
  if(name.length()>=4)
    {
    name = name.substring(0, name.length()-4);
    }
  Iterator<String> it = templateLines.iterator();
  List<String> groupedLines = new ArrayList<String>();
  String line;
  int parsedLayers = 0;
  int readSizeParams = 0;
  int highestRuleNumber = 0;
  int highestEntityRuleNumber =0;
  while(it.hasNext() && (line = it.next())!=null)
    {    
    if(line.toLowerCase().startsWith("xsize="))
      {
      xSize = StringTools.safeParseInt("=", line);
      readSizeParams++;
      if(readSizeParams==3)
        {
        templateData = new short[xSize*ySize*zSize];
        }
      }
    else if(line.toLowerCase().startsWith("ysize="))
      {
      ySize = StringTools.safeParseInt("=", line);
      readSizeParams++;
      if(readSizeParams==3)
        {
        templateData = new short[xSize*ySize*zSize];
        }
      }
    else if(line.toLowerCase().startsWith("zsize="))
      {
      zSize = StringTools.safeParseInt("=", line);
      readSizeParams++;
      if(readSizeParams==3)
        {
        templateData = new short[xSize*ySize*zSize];
        }
      }
    else if(line.toLowerCase().startsWith("verticaloffset="))
      {
      yOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("xoffset="))
      {
      xOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("zoffset"))
      {
      zOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("rule:"))
      {
      parseTag("rule", it, groupedLines);
      TemplateRule rule = parseOldBlockRule(groupedLines);
      if(rule!=null)
        {
    	AWLog.logDebug("parsed old rule: "+rule);
        if(rule.ruleNumber>highestRuleNumber)
          {
          highestRuleNumber = rule.ruleNumber;
          }        
        parsedRules.add(rule);
        }
      groupedLines.clear();
      }
    else if(line.toLowerCase().startsWith("entity:"))
      {
      parseTag("entity", it, groupedLines);
      TemplateRuleEntity rule = parseOldEntityRule(groupedLines);
      if(rule!=null)
        {
        if(rule.ruleNumber>highestEntityRuleNumber)
          {
          highestEntityRuleNumber = rule.ruleNumber;
          }        
        parsedEntityRules.add(rule);
        }
      groupedLines.clear();
      }
    else if(line.toLowerCase().startsWith("layer:"))
      {
      parseTag("layer", it, groupedLines);
      parseLayer(groupedLines, templateData, parsedLayers, xSize, ySize, zSize);
      parsedLayers++;
      groupedLines.clear();
      } 
    else if(line.toLowerCase().startsWith("npc:"))
      {
      parseTag("npc", it, groupedLines);
      TemplateRuleEntity rule = parseNpcRule(groupedLines);
      if(rule!=null)
        {
        if(rule.ruleNumber>highestEntityRuleNumber)
          {
          highestEntityRuleNumber = rule.ruleNumber;
          }        
        parsedEntityRules.add(rule);
        }
      groupedLines.clear();
      }
    else if(line.toLowerCase().startsWith("gate:"))
      {
      parseTag("gate", it, groupedLines);
      TemplateRuleEntity rule = parseGateRule(groupedLines);
      if(rule!=null)
        {
        if(rule.ruleNumber>highestEntityRuleNumber)
          {
          highestEntityRuleNumber = rule.ruleNumber;
          }        
        parsedEntityRules.add(rule);
        }
      groupedLines.clear();
      }
    else if(line.toLowerCase().startsWith("vehicle:"))
      {
      parseTag("vehicle", it, groupedLines);
      TemplateRuleEntity rule = parseVehicleRule(groupedLines);
      if(rule!=null)
        {
        if(rule.ruleNumber>highestEntityRuleNumber)
          {
          highestEntityRuleNumber = rule.ruleNumber;
          }        
        parsedEntityRules.add(rule);
        }
      groupedLines.clear();
      }
    }  
  
  TemplateRule[] rules = new TemplateRule[highestRuleNumber+1];
  TemplateRule rule;
  for(int i = 0; i < parsedRules.size(); i++)
    {
    rule = parsedRules.get(i);
    if(rule.ruleNumber>=1 && rules[rule.ruleNumber]==null)
      {
      rules[rule.ruleNumber] = rule;
      }
    else
      {
      AWLog.logError("error parsing template rules, duplicate rule number detected for: "+rule.ruleNumber);
      }
    }
  
  TemplateRuleEntity entityRule;
  TemplateRuleEntity[] entityRules = new TemplateRuleEntity[parsedEntityRules.size()];
  for(int i = 0; i < parsedEntityRules.size(); i++)
    {
	entityRule = parsedEntityRules.get(i);
	entityRule.ruleNumber = i;
	entityRules[i] = entityRule;    
    }
  
  zOffset = zSize - 1 - zOffset;//invert offset to normalize for the new top-left oriented template construction
  StructureTemplate template = new StructureTemplate(name, xSize, ySize, zSize, xOffset, yOffset, zOffset);
  template.setRuleArray(rules);
  template.setEntityRules(entityRules);
  template.setTemplateData(templateData);
  template.setValidationSettings(new StructureValidationSettingsDefault());
  TemplateExporter.exportTo(template, new File(TemplateLoader.outputDirectory));
  return template;
  }

private List<String> parseTag(String tag, Iterator<String> it, List<String> output)
  {
  String line;
  while(it.hasNext() && (line = it.next())!=null)
    {
    if(line.toLowerCase().startsWith(":end"+tag))
      {
      break;
      }
    output.add(line);
    }
  return output;
  }

private TemplateRuleEntity parseOldEntityRule(List<String> lines)
  {
  int x = 0, y = 0, z = 0, dir = 0;
  float ox = 0.f, oy = 0.f, oz = 0.f;
  float rotation = 0.f;
  String mobID = "";
  TemplateRuleVanillaEntity rule = null;
  for(String line : lines)
    {
    if(line.toLowerCase().startsWith("entityname=")){mobID = StringTools.safeParseString("=", line);}
    else if(line.toLowerCase().startsWith("bx=")){x = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("by=")){y = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("bz=")){z = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("ox=")){ox = StringTools.safeParseFloat("=", line);}
    else if(line.toLowerCase().startsWith("oy=")){oy = StringTools.safeParseFloat("=", line);}
    else if(line.toLowerCase().startsWith("oz=")){oz = StringTools.safeParseFloat("=", line);}
    else if(line.toLowerCase().startsWith("rotation=")){rotation = StringTools.safeParseFloat("=", line);}
    else if(line.toLowerCase().startsWith("hangdir=")){dir = StringTools.safeParseInt("=", line);}
    }
  Class clz = (Class) EntityList.stringToClassMapping.get(mobID);  
  if(nbtEntities.contains(clz))
    {    
    NBTTagCompound tag = entityDefaultTags.get(mobID);
    rule = new TemplateRuleEntityLogic();    
    ((TemplateRuleEntityLogic)rule).tag = tag;  
    }
  else if(hangingEntities.contains(clz))
    {
    NBTTagCompound tag = entityDefaultTags.get(mobID);
    rule = new TemplateRuleEntityHanging();   
    ((TemplateRuleEntityHanging)rule).tag = tag;
    ((TemplateRuleEntityHanging)rule).direction = dir;
    }
  else if(normalHandledEntities.contains(clz))
    {
    rule = new TemplateRuleVanillaEntity();
    } 
  if(rule!=null)
    {
    rule.x = x;
    rule.y = y;
    rule.z = z;
    rule.xOffset = ox;
    rule.zOffset = oz;
    rule.mobID = mobID;    
    }
  rule.ruleNumber = nextEntityID;
  nextEntityID++;
  return rule;
  }

int nextEntityID = 0;

private TemplateRule parseCivicRule(List<String> lines)
  {
  if(AWFramework.loadedAutomation)
    {
    return StructurePluginAutomation.parseAutomationRule(lines);
    }
  return null;
  }

private TemplateRuleEntity parseGateRule(List<String> lines)
  {
  TemplateRuleEntity rule = null;
  /**
   * TODO
   */
  return rule;
  }

private TemplateRuleEntity parseVehicleRule(List<String> lines)
  {
  if(AWFramework.loadedVehicles)
    {
    return StructurePluginVehicles.parseVehicleRule(lines);
    }
  return null;
  }

private TemplateRuleEntity parseNpcRule(List<String> lines)
  {
   if(AWFramework.loadedVehicles)
    {
    return StructurePluginNpcs.parseNpcRule(lines);
    }
  return null;
  }

private TemplateRule parseOldBlockRule(List<String> lines)
  {
  int number = 0;
  int id = 0;
  int meta = 0;
  int buildPass = 0;
  for(String line : lines)
    {
    if(line.toLowerCase().startsWith("number=")){number = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("blocks="))
      {
      String[] blockLines = StringTools.safeParseString("=", line).split(",");
      String[] blockData = blockLines[0].split("-");
      id = StringTools.safeParseInt(blockData[0]);
      meta = StringTools.safeParseInt(blockData[1]);
      }
    else if(line.toLowerCase().startsWith("order=")){buildPass = StringTools.safeParseInt("=", line);}    
    }
  
  Block block = Block.blocksList[id];
  AWLog.logDebug("parsing old block rule...rule: "+number+ " id: "+id +" meta: "+meta +" order: "+buildPass + " foundBlock: "+block);
  if(block==null)//skip air block rule (0/null), or non-present blocks
    {    
    return null;
    }  
  else if(id>256)
    {
    return parseModBlock(block, number, buildPass, meta);    
    }
  else if(specialHandledBlocks.contains(block))
    {
    return parseSpecialBlockRule(block, number, buildPass, meta, lines);
    }
  else
    {
    TemplateRuleVanillaBlocks rule = new TemplateRuleVanillaBlocks();
    rule.ruleNumber = number;
    rule.blockName = block.getUnlocalizedName();
    rule.meta = meta;
    rule.buildPass = buildPass;
    return rule;
    }
  }

private TemplateRule parseSpecialBlockRule(Block block, int number, int buildPass, int meta, List<String> lines)
  {
  TemplateRuleVanillaBlocks rule = null;
  if(block==Block.doorWood || block==Block.doorIron)
    {
    rule = new TemplateRuleBlockDoors();
    rule.ruleNumber = number;
    rule.blockName = block.getUnlocalizedName();
    rule.meta = meta;
    rule.buildPass = buildPass;
    }//vanilla door rule
  else if(block==Block.signPost || block==Block.signWall)
    {
    rule = new TemplateRuleBlockSign();
    rule.ruleNumber = number;
    rule.blockName = block.getUnlocalizedName();
    rule.meta = meta;
    rule.buildPass = buildPass;
    ((TemplateRuleBlockSign)rule).wall = block == Block.signWall;
    ((TemplateRuleBlockSign)rule).signContents = new String[]{"","","",""};        
    }//vanilla sign rule
  else if(block==Block.commandBlock)
    {
    NBTTagCompound tag = new NBTTagCompound();
    teCommand.writeToNBT(tag); 

    rule = new TemplateRuleBlockLogic();
    rule.ruleNumber = number;
    rule.blockName = block.getUnlocalizedName();
    rule.meta = meta;
    rule.buildPass = buildPass;
    ((TemplateRuleBlockLogic)rule).tag = tag;
    }
  else if(block==Block.mobSpawner)
    {
    //NOOP -- no previous spawner-block handling
    }//vanilla spawner rule
  else if(block==Block.furnaceBurning || block==Block.furnaceIdle)
    {
    NBTTagCompound tag = new NBTTagCompound();
    teFurnace.writeToNBT(tag); 

    rule = new TemplateRuleBlockLogic();
    rule.ruleNumber = number;
    rule.blockName = block.getUnlocalizedName();
    rule.meta = meta;
    rule.buildPass = buildPass;
    ((TemplateRuleBlockLogic)rule).tag = tag;
    }
  else if(block==Block.skull)
    {
    NBTTagCompound tag = new NBTTagCompound();
    teSkull.writeToNBT(tag); 

    rule = new TemplateRuleBlockLogic();
    rule.ruleNumber = number;
    rule.blockName = block.getUnlocalizedName();
    rule.meta = meta;
    rule.buildPass = buildPass;
    ((TemplateRuleBlockLogic)rule).tag = tag;
    }    
  else if(block==Block.brewingStand)
    {
    NBTTagCompound tag = new NBTTagCompound();
    teBrewingStand.writeToNBT(tag); 

    rule = new TemplateRuleBlockLogic();
    rule.ruleNumber = number;
    rule.blockName = block.getUnlocalizedName();
    rule.meta = meta;
    rule.buildPass = buildPass;
    ((TemplateRuleBlockLogic)rule).tag = tag;
    }
  else if(block==Block.chest)
    {
    NBTTagCompound tag = new NBTTagCompound();
    teChest.writeToNBT(tag); 

    rule = new TemplateRuleBlockInventory();
    rule.ruleNumber = number;
    rule.blockName = block.getUnlocalizedName();
    rule.meta = meta;
    rule.buildPass = buildPass;
    ((TemplateRuleBlockInventory)rule).tag = tag;
    ((TemplateRuleBlockInventory)rule).randomLootLevel = 1;
    }//vanilla chests
  else if(block==Block.dispenser)
    {
    NBTTagCompound tag = new NBTTagCompound();
    teDispenser.writeToNBT(tag); 

    rule = new TemplateRuleBlockInventory();
    rule.ruleNumber = number;
    rule.blockName = block.getUnlocalizedName();
    rule.meta = meta;
    rule.buildPass = buildPass;
    ((TemplateRuleBlockInventory)rule).tag = tag;
    ((TemplateRuleBlockInventory)rule).randomLootLevel = 0;
    }
  else if(block==Block.dropper)
    {
    NBTTagCompound tag = new NBTTagCompound();
    teDropper.writeToNBT(tag); 

    rule = new TemplateRuleBlockInventory();
    rule.ruleNumber = number;
    rule.blockName = block.getUnlocalizedName();
    rule.meta = meta;
    rule.buildPass = buildPass;
    ((TemplateRuleBlockInventory)rule).tag = tag;
    ((TemplateRuleBlockInventory)rule).randomLootLevel = 0;
    }
  else if(block==Block.hopperBlock)
    {
    NBTTagCompound tag = new NBTTagCompound();
    teHopper.writeToNBT(tag); 

    rule = new TemplateRuleBlockInventory();
    rule.ruleNumber = number;
    rule.blockName = block.getUnlocalizedName();
    rule.meta = meta;
    rule.buildPass = buildPass;
    ((TemplateRuleBlockInventory)rule).tag = tag;
    ((TemplateRuleBlockInventory)rule).randomLootLevel = 0;
    }
  return rule;
  }

private TemplateRule parseModBlock(Block block, int number, int buildPass, int meta)
  {
  TemplateRuleModBlocks rule = new TemplateRuleModBlocks();
  rule.ruleNumber = number;
  rule.blockName = block.getUnlocalizedName();
  rule.meta = meta;
  return rule;
  }

private void parseLayer(List<String> lines, short[] templateData, int yLayer, int xSize, int ySize, int zSize)
  {
  if(templateData==null){throw new IllegalArgumentException("cannot fill data into null template data array");}
  int z = 0;
  for(String st : lines)
    {
    if(st.startsWith("layer:") || st.startsWith(":endlayer"))
      {
      continue;
      }
    short[] data = StringTools.parseShortArray(st);
    for(int x = 0; x < xSize && x < data.length; x++)
      {
      templateData[StructureTemplate.getIndex(x, yLayer, z, xSize, ySize, zSize)] = data[x];
      }
    z++;
    }  
  }


}
