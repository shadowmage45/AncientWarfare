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
package shadowmage.ancient_structures.common.template.build.validation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.world.World;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureBB;

public abstract class StructureValidator
{

public final StructureValidationType validationType;
private int selectionWeight;
private int clusterValue;
private int minDuplicateDistance;
private boolean worldGenEnabled;
private boolean isUnique;//should this structure generate only once?
private boolean preserveBlocks;

private boolean biomeWhiteList;//should treat biome list as white or blacklist?
private Set<String> biomeList;//list of biomes for white/black list.  treated as white/black list from whitelist toggle

private boolean dimensionWhiteList;//should treat dimension list as white or blacklist?
private int[] acceptedDimensions;//list of accepted dimensions treated as white/black list from whitelist toggle

protected StructureValidator(StructureValidationType validationType)
  {
  this.validationType = validationType;
  selectionWeight = 1;
  clusterValue = 1;  
  minDuplicateDistance = 4;
  biomeList = new HashSet<String>();
  }

protected abstract void readFromLines(List<String> lines);
protected abstract void write(BufferedWriter writer) throws IOException;
protected abstract void setDefaultSettings(StructureTemplate template);

/**
 * should this template be included for selection for generation? should only validate block placement, most other stuff has been checked (dimension/biome/cluster value/etc)
 */
public abstract boolean shouldIncludeForSelection(World world, int x, int y, int z, int face, StructureTemplate template);

/**
 * if template should be included for selection, get the adjusted spawn Y level from the input block position.  this adjustedY will be used for validation and generation if template is selected and validated
 */
public abstract int getAdjustedSpawnY(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb);

/**
 * if selected for placement, validate that placement. return false if placement is invalid 
 */
public abstract boolean validatePlacement(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb);

/**
 * after validation, do any necessary clearing or leveling/etc
 */
public abstract void preGeneration(World world, int x, int y, int z, int face, StructureTemplate template, StructureBB bb);



public static final StructureValidator parseValidator(List<String> lines)
  {
  String type = null;
  List<String> tagLines = new ArrayList<String>();
  Iterator<String> it = lines.iterator();
  String line;
  boolean unique = false, worldGen = false, biome = false, dimension = false, blocks = false;
  int selectionWeight=1, clusterValue=1, duplicate=1;
  int[] dimensions = null;
  Set<String> biomes = new HashSet<String>();
  
  while(it.hasNext() && (line=it.next())!=null)
    {    
    if(line.toLowerCase().startsWith("type=")){type = StringTools.safeParseString("=", line);}
    else if(line.toLowerCase().startsWith("unique=")){unique = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("worldgenenabled=")){worldGen = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("biomewhitelist=")){biome = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("dimensionwhitelise=")){dimension = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("preserveblocks=")){blocks = StringTools.safeParseBoolean("=", line);}
    else if(line.toLowerCase().startsWith("dimensionlist=")){dimensions = StringTools.safeParseIntArray("=", line);}
    else if(line.toLowerCase().startsWith("biomelist=")){addBiomesToSet(biomes, StringTools.safeParseStringArray("=", line));}
    else if(line.toLowerCase().startsWith("selectionweight=")){selectionWeight = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("clustervalue=")){clusterValue = StringTools.safeParseInt("=", line);}
    else if(line.toLowerCase().startsWith("minduplicatedistance=")){duplicate = StringTools.safeParseInt("=", line);}    
    else if(line.toLowerCase().startsWith("data:"))
      {
      tagLines.add(line);
      while(it.hasNext() && (line=it.next())!=null)
        {
        tagLines.add(line);
        if(line.toLowerCase().startsWith(":enddata"))
          {
          break;
          }
        }
      }
    }
  StructureValidationType validatorType = StructureValidationType.getTypeFromName(type);
  StructureValidator validator;
  if(validatorType==null)
    {
    validator = StructureValidationType.GROUND.getValidator();
    }
  else
    {
    validator = validatorType.getValidator();
    validator.readFromLines(tagLines);
    }
  validator.dimensionWhiteList = dimension;
  validator.acceptedDimensions = dimensions;
  validator.biomeWhiteList = biome;
  validator.biomeList = biomes;
  validator.worldGenEnabled = worldGen; 
  validator.isUnique = unique;
  validator.preserveBlocks = blocks;
  validator.clusterValue = clusterValue;
  validator.selectionWeight = selectionWeight;
  validator.minDuplicateDistance = duplicate;  
  return validator;
  }

private static final void addBiomesToSet(Set<String> biomes, String[] names)
  {
  for(String name : names)
    {
    biomes.add(name.toLowerCase());
    }
  }

public static final void writeValidator(BufferedWriter out, StructureValidator validator) throws IOException
  {
  out.write("type="+validator.validationType.getName());  
  out.newLine();
  out.write("worldGenEnabled="+validator.worldGenEnabled);  
  out.newLine();
  out.write("unique="+validator.isUnique);
  out.newLine();
  out.write("preserveBlocks="+validator.preserveBlocks);
  out.newLine();
  out.write("selectionWeight="+validator.selectionWeight);
  out.newLine();
  out.write("clusterValue="+validator.clusterValue);
  out.newLine();
  out.write("minDuplicateDistance="+validator.minDuplicateDistance);
  out.newLine();
  out.write("dimensionWhiteList="+validator.dimensionWhiteList);
  out.newLine();
  out.write("dimensionList="+StringTools.getCSVStringForArray(validator.acceptedDimensions));
  out.newLine();  
  out.write("biomeWhiteList="+validator.biomeWhiteList);
  out.newLine();
  out.write("biomeList="+StringTools.getCSVValueFor(validator.biomeList.toArray(new String[validator.biomeList.size()])));
  out.newLine();
  out.write("data:");
  out.newLine();
  validator.write(out);
  out.write(":enddata");
  out.newLine();
  }

public final StructureValidator setDefaults(StructureTemplate template)
  {
  setDefaultSettings(template);
  return this;
  }

public final int getSelectionWeight()
  {
  return selectionWeight;
  }

public final int getClusterValue()
  {
  return clusterValue;
  }

public final boolean isWorldGenEnabled()
  {
  return worldGenEnabled;
  }

public final Set<String> getBiomeList()
  {
  return biomeList;
  }

public final boolean isPreserveBlocks()
  {
  return preserveBlocks;
  }

public final boolean isBiomeWhiteList()
  {
  return biomeWhiteList;
  }

public final boolean isUnique()
  {
  return isUnique;
  }

public final boolean isDimensionWhiteList() 
  {
	return dimensionWhiteList;
	}

public final int[] getAcceptedDimensions()
  {
  return acceptedDimensions;
  }

public final int getMinDuplicateDistance()
  {
  return minDuplicateDistance;
  }

}
