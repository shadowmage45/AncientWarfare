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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.manager.BlockDataManager;

public class StructureValidationSettingsDefault extends StructureValidationSettings
{

public static HashSet<String> defaultTargetBlocks = new HashSet<String>();
public static HashSet<String> defaultClearBlocks = new HashSet<String>();
public static HashSet<String> defaultBorderTargetBlocks = new HashSet<String>();

static
{
defaultTargetBlocks.add(Block.dirt.getUnlocalizedName());
defaultTargetBlocks.add(Block.grass.getUnlocalizedName());
defaultTargetBlocks.add(Block.stone.getUnlocalizedName());
defaultTargetBlocks.add(Block.sand.getUnlocalizedName());
defaultTargetBlocks.add(Block.gravel.getUnlocalizedName());
defaultTargetBlocks.add(Block.sandStone.getUnlocalizedName());
defaultTargetBlocks.add(Block.blockClay.getUnlocalizedName());
defaultTargetBlocks.add(Block.snow.getUnlocalizedName());
defaultTargetBlocks.add(Block.oreIron.getUnlocalizedName());
defaultTargetBlocks.add(Block.oreCoal.getUnlocalizedName());

defaultClearBlocks.addAll(defaultTargetBlocks);
defaultClearBlocks.add(Block.waterStill.getUnlocalizedName());
defaultClearBlocks.add(Block.lavaStill.getUnlocalizedName());
defaultClearBlocks.add(Block.cactus.getUnlocalizedName());
defaultClearBlocks.add(Block.vine.getUnlocalizedName());
defaultClearBlocks.add(Block.tallGrass.getUnlocalizedName());
defaultClearBlocks.add(Block.wood.getUnlocalizedName());
defaultClearBlocks.add(Block.plantRed.getUnlocalizedName());
defaultClearBlocks.add(Block.plantYellow.getUnlocalizedName());
defaultClearBlocks.add(Block.deadBush.getUnlocalizedName());
defaultClearBlocks.add(Block.leaves.getUnlocalizedName());
defaultClearBlocks.add(Block.wood.getUnlocalizedName());

defaultBorderTargetBlocks.addAll(defaultTargetBlocks);
}
/**
 * given an area with a source point, how far above the source-point is the highest acceptable block located? 
 * e.g. a 'normal' setting would be the same height as the structures above-ground height which would allow
 * generation of a structure with no blocks left 'hanging' above it, and minimal cut-in into a cliffside/etc
 * e.g. a 'perfect' setting would be 0, meaning the ground must be flat and level prior to generation
 * 
 * negative values imply to skip leveling checking
 */
int maxLeveling;

/**
 * if true, will clear blocks in leveling bounds PRIOR to template construction.  Set to false if you wish to
 * preserve any existing blocks within the structure bounds.
 */
boolean doLeveling;

/**
 * given an area with a source point, how far below the source point may 'holes' extend into the ground along the
 * edges of the structure.
 * e.g. a 'normal' setting would be 1-2 blocks, which would ensure that the chosen site was flat enough that it would
 * generate with minimal under-fill.
 * e.g. an 'extreme' setting to force-placement might be 4-8 blocks or more.  Placement would often be ugly with only
 * part of the structure resting on the ground.
 * 
 * negative values imply to skip edge-depth checking
 */
int maxFill;

/**
 * if true, will fill _directly_ below the structure down to the specified number of blocks from maxMissingEdgeDepth
 * filling will occur PRIOR to template construction.
 */
boolean doFillBelow;

/**
 * the size of the border around the base structure BB to check for additional functions
 * 0 or negative values denote no border.
 */
int borderSize;

/**
 * same as with structure-leveling -- how much irregularity can there be above the chose ground level
 * negative values imply to skip border leveling tests
 */
int borderMaxLeveling;
boolean doBorderLeveling;

/**
 * how irregular can the border surrounding the structure be in the -Y direction?
 * negative values imply to skip border depth tests
 */
int borderMaxFill;
boolean doBorderFill;

boolean gradientBorder;

boolean preserveBlocks;
boolean preserveWater;
boolean preserveLava;

boolean biomeWhiteList;//should treat biome list as white or blacklist?
Set<String> biomeList;//list of biomes for white/black list.  treated as white/black list from whitelist toggle

boolean dimensionWhiteList;//should treat dimension list as white or blacklist?
int[] acceptedDimensions;//list of accepted dimensions treated as white/black list from whitelist toggle

Set<String> acceptedTargetBlocks;//list of accepted blocks which the structure may be built upon or filled over -- 100% of blocks directly below the structure must meet this list
Set<String> acceptedTargetBlocksBorder;
Set<String> acceptedTargetBlocksBorderRear;


Set<String> acceptedClearBlocks;//list of blocks which may be cleared/removed during leveling and buffer operations. 100% of blocks to be removed must meet this list

/**
 * world generation selection and clustering settings
 */
boolean worldGenEnabled;
boolean isUnique;//should this structure generate only once?
int selectionWeight;
int clusterValue;
int minDuplicateDistance;

/**
 * survival mode availability settings
 */
boolean survivalEnabled;
ItemStack[] resourceStacks;

public StructureValidationSettingsDefault()
  {
  biomeList = new HashSet<String>();
  acceptedClearBlocks = new HashSet<String>();
  acceptedTargetBlocks = new HashSet<String>();
  acceptedTargetBlocksBorder = new HashSet<String>();
  acceptedTargetBlocksBorderRear = new HashSet<String>();
  acceptedDimensions = new int[]{};
  
  acceptedClearBlocks.addAll(defaultClearBlocks);
  acceptedTargetBlocks.addAll(defaultTargetBlocks);
  acceptedTargetBlocksBorder.addAll(defaultBorderTargetBlocks);
  acceptedTargetBlocksBorderRear.addAll(defaultBorderTargetBlocks);
  }

@Override
public void parseSettings(List<String> lines)
  {
  Iterator<String> it = lines.iterator();
  String line;
  while(it.hasNext() && (line = it.next())!=null)
    {
    if(line.startsWith("validation:")){continue;}
    else if(line.toLowerCase().startsWith(":endvalidation")){break;}
    else if(line.toLowerCase().startsWith("survivalenabled=")){survivalEnabled = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("worldgenenabled=")){worldGenEnabled = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("unique=")){isUnique = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("preserveblocks=")){preserveBlocks = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("preservewater=")){preserveWater = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("preservelava=")){preserveLava = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("selectionweight=")){selectionWeight = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("clustervalue=")){clusterValue = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("minduplicatedistance=")){minDuplicateDistance = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("leveling=")){maxLeveling = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("fill=")){maxFill = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("border=")){borderSize = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("borderleveling=")){borderMaxLeveling = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("borderfill=")){borderMaxFill = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("doleveling=")){doLeveling = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("dofill=")){doFillBelow = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("doborderleveling=")){doBorderLeveling = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("doborderfill=")){doBorderFill = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("gradientborder=")){gradientBorder = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("accepteddimensions=")){acceptedDimensions = StringTools.safeParseIntArray("=", line);}
    else if(line.toLowerCase().startsWith("dimensionwhitelist=")){dimensionWhiteList = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("acceptedbiomes=")){parseStringsToSet(biomeList, StringTools.safeParseStringArray("=", line), true);}
    else if(line.toLowerCase().startsWith("validtargetblocks=")){parseStringsToSet(acceptedTargetBlocks, StringTools.safeParseStringArray("=", line), false);}
    else if(line.toLowerCase().startsWith("validclearingblocks=")){parseStringsToSet(acceptedClearBlocks, StringTools.safeParseStringArray("=", line), false);}
    else if(line.toLowerCase().startsWith("validborderblocks=")){parseStringsToSet(acceptedTargetBlocksBorder, StringTools.safeParseStringArray("=", line), false);}
    else if(line.toLowerCase().startsWith("validborderblocksrear=")){parseStringsToSet(acceptedTargetBlocksBorderRear, StringTools.safeParseStringArray("=", line), false);}
    
    /**
     * TODO survival resource-list
     */
    }
  }

private void parseStringsToSet(Set<String> toFill, String[] data, boolean lowerCase)
  {
  for(String name : data)
    {
    toFill.add(lowerCase? name.toLowerCase() : name);
    }
  }

@Override
public void writeSettings(BufferedWriter writer) throws IOException
  {
  writer.write("survivalEnabled="+survivalEnabled);
  writer.newLine();
  writer.write("worldGenEnabled="+worldGenEnabled);  
  writer.newLine();
  writer.write("unique="+isUnique);
  writer.newLine();
  writer.write("preserveBlocks="+preserveBlocks);
  writer.newLine();
  writer.write("preserveWater="+preserveWater);
  writer.newLine();
  writer.write("preserveLava="+preserveLava);
  writer.newLine();
  writer.write("selectionWeight="+selectionWeight);
  writer.newLine();
  writer.write("clusterValue="+clusterValue);
  writer.newLine();
  writer.write("minDuplicateDistance="+minDuplicateDistance);
  writer.newLine();
  writer.write("leveling="+maxLeveling);
  writer.newLine();
  writer.write("fill="+maxFill);
  writer.newLine();
  writer.write("border="+borderSize);
  writer.newLine();
  writer.write("borderLeveling="+borderMaxLeveling);
  writer.newLine();
  writer.write("borderFill="+borderMaxFill);
  writer.newLine();
  writer.write("doLeveling="+doLeveling);
  writer.newLine();
  writer.write("doFill="+doFillBelow);
  writer.newLine();
  writer.write("doBorderLeveling="+doBorderLeveling);
  writer.newLine();
  writer.write("doBorderFill="+doBorderFill);
  writer.newLine();  
  writer.write("gradientBorder="+gradientBorder);
  writer.newLine();
  writer.write("dimensionWhiteList="+dimensionWhiteList);
  writer.newLine();
  writer.write("acceptedDimensions="+StringTools.getCSVStringForArray(acceptedDimensions));
  writer.newLine();  
  writer.write("biomeWhiteList="+biomeWhiteList);
  writer.newLine();
  writer.write("acceptedBiomes="+StringTools.getCSVValueFor(biomeList.toArray(new String[biomeList.size()])));
  writer.newLine();
  writer.write("validTargetBlocks="+StringTools.getCSVValueFor(acceptedTargetBlocks.toArray(new String[acceptedTargetBlocks.size()])));
  writer.newLine();
  writer.write("validClearingBlocks="+StringTools.getCSVValueFor(acceptedClearBlocks.toArray(new String[acceptedClearBlocks.size()])));
  writer.newLine();  
  writer.write("validBorderBlocks="+StringTools.getCSVValueFor(acceptedTargetBlocksBorder.toArray(new String[acceptedTargetBlocksBorder.size()])));
  writer.newLine();
  writer.write("validBorderBlocksRear="+StringTools.getCSVValueFor(acceptedTargetBlocksBorderRear.toArray(new String[acceptedTargetBlocksBorderRear.size()])));
  writer.newLine();
  /**  
   * TODO survival resource-list
   */
  }



private String[] getStringArrayFrom(Set<Block> blocks, String[] in)
  {
  if(in==null || in.length< blocks.size())
    {
    in = new String[blocks.size()];
    }
  int index = 0;
  for(Block b : blocks)
    {
    if(b==null){continue;}
    in[index] = b.getUnlocalizedName();
    index++;    
    }
  return in;
  }

public void setDefaultValidationSettings(int xSize, int ySize, int zSize, int yOffset)
  {
  int thirdSize = (int)((float)ySize/3.f) + 1;
  this.maxFill = thirdSize;
  this.maxLeveling = -1;
  
  this.borderSize = thirdSize;
  this.borderMaxFill = thirdSize;
  this.borderMaxLeveling = thirdSize;
  this.gradientBorder = true;
  }

}
