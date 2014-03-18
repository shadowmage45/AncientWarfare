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
package shadowmage.ancient_structures.common.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import shadowmage.ancient_structures.common.config.AWLog;
import shadowmage.ancient_structures.common.manager.StructureTemplateManager;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.StructureTemplateClient;
import shadowmage.ancient_structures.common.template.build.StructureBuilder;
import shadowmage.ancient_warfare.common.item.AWItemClickable;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCivicBuilder extends AWItemClickable
{

private Icon icon;

private List<ItemStack> displayCache = new ArrayList<ItemStack>();

/**
 * @param itemID
 */
public ItemCivicBuilder(int itemID)
  {
  super(itemID, true);
  this.hasLeftClick = true;
  this.setMaxStackSize(1);  
  this.setCreativeTab(AWStructuresItemLoader.structureTab);
  }

@Override
public Icon getIconFromDamage(int par1)
  {
  return icon;
  }

@Override
public void registerIcons(IconRegister par1IconRegister)
  {
  icon = par1IconRegister.registerIcon("ancientwarfare:castle");
  }

/**
 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
 */

ItemStructureSettings displaySettings = new ItemStructureSettings();
@SideOnly(Side.CLIENT)
@Override
public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
  {
  ArrayList<StructureTemplateClient> templates = new ArrayList<StructureTemplateClient>();
  StructureTemplateManager.instance().getClientSurvivalTemplates(templates);
  AWLog.logDebug("attempting add of civic builder items.  templates size: "+templates.size());
  if(templates.size()!=displayCache.size())
    {
    displayCache.clear();
    ItemStack stack;
    for(StructureTemplateClient template : templates)
      {      
      stack = new ItemStack(this,1,0);
      displaySettings.clearSettings();
      displaySettings.setName(template.name);
      displaySettings.setSettingsFor(stack, displaySettings);  
      displayCache.add(stack);    
      }    
    }  
  par3List.addAll(displayCache);
//  super.getSubItems(par1, par2CreativeTabs, par3List);
  }

public static ItemStack getCivicBuilderItem(String structure)
  {
  ItemStructureSettings displaySettings = new ItemStructureSettings();
  displaySettings.setName(structure);

  ItemStack stack = new ItemStack(AWStructuresItemLoader.civicBuilder.itemID,1,0);
  displaySettings.setSettingsFor(stack, displaySettings);  
  return stack;
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {  
//  if(!world.isRemote && !player.isSneaking())
//    {
//    GUIHandler.instance().openGUI(GUIHandler.STRUCTURE_SELECT, player, player.worldObj, 0, 0, 0);    
//    return true;
//    }    
  return true;
  }

ItemStructureSettings buildSettings = new ItemStructureSettings();
@Override
public boolean onUsedFinalLeft(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {
  if(player==null || hit==null || world.isRemote)
    {
    return false;
    }
  ItemStructureSettings.getSettingsFor(stack, buildSettings);
  if(buildSettings.hasName())
    {
    StructureTemplate template = StructureTemplateManager.instance().getTemplate(buildSettings.name);
    if(template==null)
      {
      player.addChatMessage("no structure found to build...");
      return true;
      }
    hit.offsetForMCSide(side);
    /**
     * TODO place builder block on hit position
     * offset struct build-position by struct z-offset, so builder is outside of struct BB
     */
    AWLog.logDebug("constructing template: "+template);    
    StructureBuilder builder = new StructureBuilder(world, template, BlockTools.getPlayerFacingFromYaw(player.rotationYaw), hit.x, hit.y, hit.z);
    builder.instantConstruction();
    }  
  else
    {
    player.addChatMessage("no structure found to build...");
    return true;
    }
  return true;
  }

ItemStructureSettings viewSettings = new ItemStructureSettings();
@Override
public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
  String structure = "none";
  ItemStructureSettings.getSettingsFor(stack, viewSettings);
  if(viewSettings.hasName())
    {
    structure = viewSettings.name;
    }
  list.add("Current Structure: "+structure);
  }

@Override
public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
  {
  return false;
  }

}
