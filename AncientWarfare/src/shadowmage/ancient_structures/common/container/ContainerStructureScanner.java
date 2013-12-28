/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_structures.common.container;

import java.io.File;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_structures.common.item.AWStructuresItemLoader;
import shadowmage.ancient_structures.common.item.ItemStructureSettings;
import shadowmage.ancient_structures.common.manager.StructureTemplateManager;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureValidationType;
import shadowmage.ancient_structures.common.template.load.TemplateLoader;
import shadowmage.ancient_structures.common.template.save.TemplateExporter;
import shadowmage.ancient_structures.common.template.scan.TemplateScanner;

public class ContainerStructureScanner extends ContainerBase
{


ItemStructureSettings settings = new ItemStructureSettings();


/**
 * @param openingPlayer
 * @param synch
 */
public ContainerStructureScanner(EntityPlayer openingPlayer, int x, int y, int z)
  {
  super(openingPlayer, null);
  if(player.worldObj.isRemote)
    {
    return;
    }
  ItemStack builderItem = player.inventory.getCurrentItem();
  if(builderItem==null || builderItem.getItem()==null || builderItem.getItem()!=AWStructuresItemLoader.structureScanner)
    {
    return;
    } 
  settings.getSettingsFor(builderItem, settings);
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("export"))
    {
    boolean include = tag.getBoolean("export");
    String name = tag.getString("name");
    scanStructure(player.worldObj, settings.pos1(), settings.pos2(), settings.buildKey(), settings.face(), name, include, tag);
    
    
    settings.clearSettings();    
    }
  if(tag.hasKey("reset"))
    {
    settings.clearSettings();
    }
  }

public boolean scanStructure(World world, BlockPosition pos1, BlockPosition pos2, BlockPosition key, int face, String name, boolean include, NBTTagCompound tag)
  {
  BlockPosition min = BlockTools.getMin(pos1, pos2);
  BlockPosition max = BlockTools.getMax(pos1, pos2);
  TemplateScanner scanner = new TemplateScanner();
  int turns = face==0 ? 2 : face==1 ? 1 : face==2 ? 0 : face==3 ? 3 : 0; //because for some reason my mod math was off?  
  StructureTemplate template = scanner.scan(world, min, max, key, turns, name);
  
//  tag.setBoolean("world", worldGenBox.checked);
//  tag.setBoolean("unique", uniqueBox.checked);
//  tag.setBoolean("survival", survivalBox.checked);
//  tag.setBoolean("doLeveling", levelingBox.checked);
//  tag.setBoolean("doFill", fillBox.checked);
//  tag.setBoolean("doBorderLeveling", borderLevelingBox.checked);
//  tag.setBoolean("doBorderFill", borderFillBox.checked);
//  tag.setInteger("leveling", levelingLine.getIntVal());
//  tag.setInteger("fill", fillLine.getIntVal());
//  tag.setInteger("border", borderLine.getIntVal());
//  tag.setInteger("borderLeveling", borderLevelingLine.getIntVal());
//  tag.setInteger("borderFill", borderFillLine.getIntVal());
//  tag.setInteger("weight", weightLine.getIntVal());
//  tag.setInteger("value", clusterLine.getIntVal());
//  tag.setInteger("dupe", minDuplicateLine.getIntVal());

//  StructureValidationSettingsDefault settings = new StructureValidationSettingsDefault();
//  
//  settings.setWorldGenEnabled(tag.getBoolean("world"));
//  settings.setUnique(tag.getBoolean("unique"));
//  settings.setSurvivalEnabled(tag.getBoolean("survival"));
//  settings.setDoLeveling(tag.getBoolean("doLeveling"));
//  settings.setDoFillBelow(tag.getBoolean("doFill"));
//  settings.setDoBorderLeveling(tag.getBoolean("doBorderLeveling"));
//  settings.setDoBorderFill(tag.getBoolean("doBorderFill"));
//  settings.setPreserveBlocks(tag.getBoolean("preserveBlocks"));
//  settings.setPreserveWater(tag.getBoolean("preserveWater"));
//  settings.setPreserveWater(tag.getBoolean("preserveLava"));
//  settings.setGradientBorder(tag.getBoolean("gradient"));
//  settings.setBiomeWhiteList(tag.getBoolean("biomeWhiteList"));
//  settings.setDimensionWhiteList(tag.getBoolean("dimensionWhiteList"));  
//  
//  
//  settings.setMaxLeveling(tag.getInteger("leveling"));
//  settings.setMaxFill(tag.getInteger("fill"));
//  settings.setBorderSize(tag.getInteger("border"));
//  settings.setBorderMaxLeveling(tag.getInteger("borderLeveling"));
//  settings.setBorderMaxFill(tag.getInteger("borderFill"));
//  settings.setSelectionWeight(tag.getInteger("weight"));
//  settings.setClusterValue(tag.getInteger("value"));
//  settings.setMinDuplicateDistance(tag.getInteger("dupe"));  
//  settings.setAcceptedDimensions(tag.getIntArray("acceptedDimensions"));  
//  if(tag.hasKey("biomeList")){}//TODO
//  if(tag.hasKey("targetBlocksList")){}//TODO
//  if(tag.hasKey("clearBlocksList")){}//TODO
//  template.setValidationSettings(settings);
  
  template.setValidationSettings(StructureValidationType.GROUND.getValidator().setDefaults(template));
  if(include)
    {
    StructureTemplateManager.instance().addTemplate(template);    
    }
  TemplateExporter.exportTo(template, new File(include ? TemplateLoader.includeDirectory : TemplateLoader.outputDirectory));
  return true;
  }

@Override
public void onContainerClosed(EntityPlayer par1EntityPlayer)
  {
  super.onContainerClosed(par1EntityPlayer);
  if(par1EntityPlayer.worldObj.isRemote)
    {
    return;
    }
  ItemStack builderItem = player.inventory.getCurrentItem();  
  if(builderItem==null || builderItem.getItem()==null || builderItem.getItem()!=AWStructuresItemLoader.structureScanner)
    {
    return;
    }
  settings.setSettingsFor(builderItem, settings); 
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
 
  }

@Override
public List<NBTTagCompound> getInitData()
  {  
  return Collections.emptyList();
  }

}
