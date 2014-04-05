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
package shadowmage.ancient_structures.client.gui.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import shadowmage.ancient_structures.common.config.AWLog;
import shadowmage.ancient_structures.common.manager.BlockDataManager;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiCheckBoxSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiItemStack;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;

public class GuiBlockSelection extends GuiContainerAdvanced
{

GuiStructureScanner parent;
GuiScrollableArea area;
GuiButtonSimple doneButton;
GuiButtonSimple autoFillFromBiomesButton;
GuiButtonSimple autoFillVanillaBlocksButton;

private HashMap<GuiCheckBoxSimple, Block> blockBoxes = new HashMap<GuiCheckBoxSimple, Block>();
private HashMap<Block, GuiCheckBoxSimple> boxesByBlock = new HashMap<Block, GuiCheckBoxSimple>();

public GuiBlockSelection(GuiStructureScanner parent)
  {
  super(parent.container);
  this.parent = parent;
  }

@Override
public int getXSize()
  {
  return 256;
  }

@Override
public int getYSize()
  {
  return 240;
  }

@Override
public String getGuiBackGroundTexture()
  {
  return Config.texturePath+"gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {

  }

@Override
public void updateScreenContents()
  {
  area.updateGuiPos(guiLeft, guiTop);
  }

@Override
public void setupControls()
  {
  this.guiElements.clear();
  this.doneButton = (GuiButtonSimple) this.addGuiButton(0, 35, 18, "Done").updateRenderPos(256-35-10, 10);
  this.guiElements.put(1, new GuiString(1, this, 220, 10, "Select Target Blocks: ").updateRenderPos(8, 10));  
  this.guiElements.put(2, this.area = new GuiScrollableArea(2, this, 8, 18+10+4, getXSize()-16, getYSize() - 20-18-4, 8));
  area.elements.clear();
  
  this.autoFillFromBiomesButton = new GuiButtonSimple(0, area, 120, 16, "Auto Fill From Biomes");
  this.autoFillFromBiomesButton.updateRenderPos(20, 0);
  area.elements.add(autoFillFromBiomesButton);
  
  this.autoFillVanillaBlocksButton = new GuiButtonSimple(0, area, 120, 16, "Auto Fill Vanilla Blocks");
  this.autoFillVanillaBlocksButton.updateRenderPos(20, 16);
  area.elements.add(autoFillVanillaBlocksButton);
  
  int totalHeight = 32;
  int elementNum = 3;
  Block block;
  for(int i = 0; i <256; i++)
    {
    block = Block.blocksList[i];
    if(block==null){continue;}
    addBlock(elementNum, totalHeight, 0, block); 
    totalHeight += 18;
    }
  
  area.updateTotalHeight(totalHeight);
  }

private void addBlock(int elementNum, int y, int x, Block block)
  {
  GuiString label = new GuiString(elementNum, area, 100, 12, BlockDataManager.getBlockName(block));
  label.updateRenderPos(x, y+2);
  area.elements.add(label);
  
  GuiItemStack item = new GuiItemStack(elementNum, area);
  item.updateRenderPos(x+140, y);
  item.isClickable = false;
  item.setItemStack(new ItemStack(block));
  area.elements.add(item);
  
  GuiCheckBoxSimple box = new GuiCheckBoxSimple(elementNum, area, 16, 16);
  box.updateRenderPos(x+160, y);
  area.elements.add(box);
  
  blockBoxes.put(box, block);
  boxesByBlock.put(block, box);
  }


@Override
public void updateControls()
  {

  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==this.doneButton)
    {
    List<String> selectedBlocks = new ArrayList<String>();
    for(GuiCheckBoxSimple box : this.blockBoxes.keySet())
      {
      if(box.checked)
        {
        selectedBlocks.add(BlockDataManager.getBlockName(this.blockBoxes.get(box)));
        }
      }
    parent.onBlockSelectionCallback(selectedBlocks);
    Minecraft.getMinecraft().displayGuiScreen(parent);
    }
  else if(element==this.autoFillFromBiomesButton)
    {
    this.fillFromBiomes();
    }
  else if(element==this.autoFillVanillaBlocksButton)
    {
    this.addDefaults();
    }
  }

/**
 * add top and filler blocks to block list from biomes
 */
private void fillFromBiomes()
  {
  Set<String> selectedBiomes = new HashSet<String>();
  selectedBiomes.addAll(parent.biomeSelections);
  boolean whitelist = parent.lastKnownBooleanValues.get("biomeWhiteList")==null? false : parent.lastKnownBooleanValues.get("biomeWhiteList").booleanValue();
  
  Set<BiomeGenBase> biomesToSearch = new HashSet<BiomeGenBase>();
  
  for(BiomeGenBase biome : BiomeGenBase.biomeList)
    {
    if(biome==null){continue;}
    if(whitelist && selectedBiomes.contains(biome.biomeName) || !whitelist && !selectedBiomes.contains(biome.biomeName))
      {
      biomesToSearch.add(biome);
      }
    }
  
  Set<Block> targetBlocks = new HashSet<Block>();
  int topId;
  int fillId;
  Block topBlock;
  Block fillBlock;
  
  for(BiomeGenBase biome : biomesToSearch)
    {
    topId = biome.topBlock;
    fillId = biome.fillerBlock;
    if(topId<0)
      {
      topId = 255+topId;
      }
    if(fillId<0)
      {
      fillId = 255+fillId;
      }
    topBlock = Block.blocksList[topId];
    fillBlock = Block.blocksList[fillId];
    if(topBlock!=null)
      {
      targetBlocks.add(topBlock);
      }
    if(fillBlock!=null)
      {
      targetBlocks.add(fillBlock);
      }
    }  
  GuiCheckBoxSimple box;
  for(Block block : targetBlocks)
    {
    box = boxesByBlock.get(block);
    if(box==null){continue;}
    box.setChecked(true);
    }
  }

/**
 * add default blocks to target list, such as dirt, stone, grass, sand, gravel, and vanilla ores
 */
private void addDefaults()
  {
  Set<Block> targetBlocks = new HashSet<Block>(); 
  
  targetBlocks.add(Block.sand);
  targetBlocks.add(Block.gravel);
  targetBlocks.add(Block.stone);
  targetBlocks.add(Block.grass);
  targetBlocks.add(Block.dirt);
  targetBlocks.add(Block.blockClay);  
  targetBlocks.add(Block.stainedClay);
  targetBlocks.add(Block.oreGold);
  targetBlocks.add(Block.oreIron);
  targetBlocks.add(Block.oreDiamond);
  targetBlocks.add(Block.oreRedstone);
  targetBlocks.add(Block.oreRedstoneGlowing);
  targetBlocks.add(Block.oreLapis);
  targetBlocks.add(Block.oreCoal);
  
  GuiCheckBoxSimple box;
  for(Block block : targetBlocks)
    {
    box = boxesByBlock.get(block);
    if(box==null){continue;}
    box.setChecked(true);
    }
  }

}
