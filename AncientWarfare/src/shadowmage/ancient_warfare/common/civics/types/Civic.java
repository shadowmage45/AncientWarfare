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
package shadowmage.ancient_warfare.common.civics.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.TECivicTownHall;
import shadowmage.ancient_warfare.common.civics.TECivicWarehouse;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnChicken;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnCow;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnMooshroom;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnPig;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnSheep;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmCactus;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmCarrot;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmCocoa;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmMelon;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmMushroomBrown;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmMushroomRed;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmNetherStalk;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmPotato;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmPumpkin;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmReed;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmWheat;
import shadowmage.ancient_warfare.common.civics.worksite.te.fish.TEFishery;
import shadowmage.ancient_warfare.common.civics.worksite.te.fish.TESquidFarm;
import shadowmage.ancient_warfare.common.civics.worksite.te.mine.TEMine;
import shadowmage.ancient_warfare.common.civics.worksite.te.mine.TEMineQuarry;
import shadowmage.ancient_warfare.common.civics.worksite.te.tree.TETreeFarmBirch;
import shadowmage.ancient_warfare.common.civics.worksite.te.tree.TETreeFarmJungle;
import shadowmage.ancient_warfare.common.civics.worksite.te.tree.TETreeFarmOak;
import shadowmage.ancient_warfare.common.civics.worksite.te.tree.TETreeFarmSpruce;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.item.ItemLoader;

public class Civic implements ICivicType
{

public static final Civic[] civicList = new Civic[64];

public static ICivicType wheatFarm = new CivicFarm(0, "civic.farm.wheat", "civic.farm.wheat.tooltip", TEFarmWheat.class, "civicFarmWheat1", 10, 2).setBlockIcons("civicFarmWheatBottom", "civicFarmWheatTop", "civicFarmWheatSides").addResourceItem(new ItemStack(Item.seeds)).setSpecResourceSize(3).addSpecResourceItem(new ItemStack(Item.dyePowder,1,15));
public static ICivicType carrotFarm = new CivicFarm(1, "civic.farm.carrot", "civic.farm.carrot.tooltip", TEFarmCarrot.class, "civicFarmCarrot1", 10, 2).setBlockIcons("civicFarmCarrotBottom", "civicFarmCarrotTop", "civicFarmCarrotSides").addResourceItem(new ItemStack(Item.carrot)).setResourceSlotSize(1).setSpecResourceSize(3).addSpecResourceItem(new ItemStack(Item.dyePowder,1,15));
public static ICivicType potatoFarm = new CivicFarm(2, "civic.farm.potato", "civic.farm.potato.tooltip", TEFarmPotato.class, "civicFarmPotato1", 10, 2).setBlockIcons("civicFarmPotatoBottom", "civicFarmPotatoTop", "civicFarmPotatoSides").addResourceItem(new ItemStack(Item.potato)).setResourceSlotSize(1).setSpecResourceSize(3).addSpecResourceItem(new ItemStack(Item.dyePowder,1,15));
public static ICivicType melonFarm = new CivicFarm(3, "civic.farm.melon", "civic.farm.melon.tooltip", TEFarmMelon.class, "civicFarmWheat1", 10, 2).setBlockIcons("civicFarmMelonBottom", "civicFarmMelonTop", "civicFarmMelonSides").addResourceItem(new ItemStack(Item.melonSeeds)).setResourceSlotSize(3);
public static ICivicType pumpkinFarm = new CivicFarm(4, "civic.farm.pumpkin", "civic.farm.pumpkin.tooltip", TEFarmPumpkin.class, "civicFarmWheat1", 10, 2).setBlockIcons("civicFarmPumpkinBottom", "civicFarmPumpkinTop", "civicFarmPumpkinSides").addResourceItem(new ItemStack(Item.pumpkinSeeds)).setResourceSlotSize(3);
public static ICivicType cactusFarm = new CivicFarm(5, "civic.farm.cactus", "civic.farm.cactus.tooltip", TEFarmCactus.class, "civicFarmWheat1", 10, 2).setBlockIcons("civicFarmCactusBottom", "civicFarmCactusTop", "civicFarmCactusSides").addResourceItem(new ItemStack(Block.cactus)).setResourceSlotSize(1);
public static ICivicType reedFarm = new CivicFarm(6, "civic.farm.reed", "civic.farm.reed.tooltip", TEFarmReed.class, "civicFarmWheat1", 10, 2).setBlockIcons("civicFarmReedBottom", "civicFarmReedTop", "civicFarmReedSides").addResourceItem(new ItemStack(Item.reed)).setResourceSlotSize(1);
public static ICivicType mushroomRedFarm = new CivicFarm(7, "civic.farm.redmushroom", "civic.farm.redmushroom.tooltip", TEFarmMushroomRed.class, "civicFarmWheat1", 10, 2).setBlockIcons("civicFarmMushroomRedBottom", "civicFarmMushroomRedTop", "civicFarmMushroomRedSides").addResourceItem(new ItemStack(Block.mushroomRed)).setResourceSlotSize(1);
public static ICivicType mushroomBrownFarm = new CivicFarm(8, "civic.farm.brownmushroom", "civic.farm.brownmushroom.tooltip", TEFarmMushroomBrown.class, "civicFarmWheat1", 10, 2).setBlockIcons("civicFarmMushroomBrownBottom", "civicFarmMushroomBrownTop", "civicFarmMushroomBrownSides").addResourceItem(new ItemStack(Block.mushroomBrown)).setResourceSlotSize(1);
public static ICivicType netherstalkFarm = new CivicFarm(9, "civic.farm.netherstalk", "civic.farm.netherstalk.tooltip", TEFarmNetherStalk.class, "civicFarmWheat1", 10, 2).setBlockIcons("civicFarmNetherBottom", "civicFarmNetherTop", "civicFarmNetherSides").addResourceItem(new ItemStack(Item.netherStalkSeeds)).setResourceSlotSize(1);
public static ICivicType mineBasic = new CivicMine(10, "civic.mine.basic", "civic.mine.basic.tooltip", TEMine.class,16).setBlockIcons("civicMineBasicBottom", "civicMineBasicTop", "civicMineBasicSides").addResourceItem(new ItemStack(Block.ladder)).addResourceItem(new ItemStack(Block.torchWood));
//mine r2
//mine r3
public static ICivicType mineQuarry = new CivicMine(13, "civic.mine.quarry", "civic.mine.quarry.tooltip", TEMineQuarry.class,64).setBlockIcons("civicMineQuarryBottom", "civicMineQuarryTop", "civicMineQuarrySides").addResourceItem(new ItemStack(Block.ladder));
//mine altr2
//mine altr3
public static ICivicType treeFarmOak = new CivicTreeFarm(16, "civic.tree.oak", "civic.tree.oak.tooltip", TETreeFarmOak.class).setBlockIcons("civicFarmOakBottom", "civicFarmOakTop", "civicFarmOakSides").addResourceItem(new ItemStack(Block.sapling,1,0)).setSpecResourceSize(3).addSpecResourceItem(new ItemStack(Item.dyePowder,1,15));
public static ICivicType treeFarmSpruce = new CivicTreeFarm(17, "civic.tree.spruce", "civic.tree.spruce.tooltip", TETreeFarmSpruce.class).setBlockIcons("civicFarmSpruceBottom", "civicFarmSpruceTop", "civicFarmSpruceSides").addResourceItem(new ItemStack(Block.sapling,1,1)).setSpecResourceSize(3).addSpecResourceItem(new ItemStack(Item.dyePowder,1,15));
public static ICivicType treeFarmBirch = new CivicTreeFarm(18, "civic.tree.birch", "civic.tree.birch.tooltip", TETreeFarmBirch.class).setBlockIcons("civicFarmBirchBottom", "civicFarmBirchTop", "civicFarmBirchSides").addResourceItem(new ItemStack(Block.sapling,1,2)).setSpecResourceSize(3).addSpecResourceItem(new ItemStack(Item.dyePowder,1,15));
public static ICivicType treeFarmJungle = new CivicTreeFarm(19, "civic.tree.jungle", "civic.tree.jungle.tooltip", TETreeFarmJungle.class).setBlockIcons("civicFarmJungleBottom", "civicFarmJungleTop", "civicFarmJungleSides").addResourceItem(new ItemStack(Block.sapling,1,3)).setSpecResourceSize(3).addSpecResourceItem(new ItemStack(Item.dyePowder,1,15));
public static ICivicType builder = new CivicBuilder(20);//survival mode ticked builder....
public static ICivicType townHallSmall = new CivicTownHall(21, "civic.town.0", "civic.town.0.tooltip", 9, TECivicTownHall.class, 0).setBlockIcons("civicTownHallSmallBottom", "civicTownHallSmallTop", "civicTownHallSmallSides");
public static ICivicType townHallMedium = new CivicTownHall(22, "civic.town.1", "civic.town.1.tooltip", 18, TECivicTownHall.class, 1).setBlockIcons("civicTownHallMediumBottom", "civicTownHallMediumTop", "civicTownHallMediumSides");
public static ICivicType townHallLarge = new CivicTownHall(23, "civic.town.2", "civic.town.2.tooltip", 27, TECivicTownHall.class, 2).setBlockIcons("civicTownHallLargeBottom", "civicTownHallLargeTop", "civicTownHallLargeSides");
public static ICivicType pigFarm = new CivicFarm(24, "civic.animal.pig", "civic.animal.pig.tooltip", TEBarnPig.class, "civicFarmWheat1", 16, 2).setBlockIcons("civicFarmPigBottom", "civicFarmPigTop", "civicFarmPigSides").addResourceItem(new ItemStack(Item.carrot));
public static ICivicType cowFarm = new CivicFarm(25, "civic.animal.cow", "civic.animal.cow.tooltip", TEBarnCow.class, "civicFarmWheat1", 16, 2).setBlockIcons("civicFarmCowBottom", "civicFarmCowTop", "civicFarmCowSides").addResourceItem(new ItemStack(Item.bucketEmpty)).addResourceItem(new ItemStack(Item.wheat));
public static ICivicType chickenFarm = new CivicFarm(26, "civic.animal.chicken", "civic.animal.chicken.tooltip", TEBarnChicken.class, "civicFarmWheat1", 16, 2).setBlockIcons("civicFarmChickenBottom", "civicFarmChickenTop", "civicFarmChickenSides").addResourceItem(new ItemStack(Item.seeds));
public static ICivicType sheepFarm = new CivicFarm(27, "civic.animal.sheep", "civic.animal.sheep.tooltip", TEBarnSheep.class, "civicFarmWheat1", 16, 2).setBlockIcons("civicFarmSheepBottom", "civicFarmSheepTop", "civicFarmSheepSides").addResourceItem(new ItemStack(Item.wheat));
public static ICivicType mooshroomFarm = new CivicFarm(28, "civic.animal.mooshroom", "civic.animal.mooshroom.tooltip", TEBarnMooshroom.class, "civicFarmWheat1", 16 ,2).setBlockIcons("civicFarmMooshroomBottom", "civicFarmMooshroomTop", "civicFarmMooshroomSides").addResourceItem(new ItemStack(Item.wheat));
public static ICivicType fishFarm = new CivicFishFarm(29, "civic.fish.fish", "civic.fish.fish.tooltip", TEFishery.class,"civicFarmWheat1", 16, 2).setBlockIcons("civicFarmFishBottom", "civicFarmFishTop", "civicFarmFishSides");
public static ICivicType squidFarm = new CivicFishFarm(30, "civic.fish.squid", "civic.fish.squid.tooltip", TESquidFarm.class, "civicFarmWheat1",16,2).setBlockIcons("civicFarmSquidBottom", "civicFarmSquidTop", "civicFarmSquidSides");
public static ICivicType cocoaFarm = new CivicFarm(31, "civic.farm.cocoa", "civic.farm.cocoa.tooltip", TEFarmCocoa.class, "civicFarmWheat1", 10 ,2).setBlockIcons("civicFarmCocoaBottom", "civicFarmCocoaTop", "civicFarmCocoaSides").addResourceItem(new ItemStack(Item.dyePowder,1,3));
public static ICivicType warehouseSmall = new CivicWarehouse(32, "civic.warehouse", "civic.warehouse.tooltip", TECivicWarehouse.class, 9, 3);


protected int globalID = 0;
protected String name = "";
protected String tooltip = "";
protected String itemIconTexture = "";
protected int maxWorkers = 1;
protected int workSizeMaxHorizontal = 10;
protected int workSizeMaxHeight = 2;
protected int minSize1 = 1;
protected int minSize2 = 1;
protected int minHeight = 1;
//protected int inventorySize = 0;
protected int regularInventorySize = 0;
protected int resourceSlotSize = 0;
protected int specResourceSlotSize = 0;
protected boolean isWorkSite = false;
protected boolean isDepository = false;
protected boolean isDwelling = false;
protected boolean addToCreative = true;
protected Class<? extends TECivic> teClass;
protected ItemStack displayStackCache = null;
protected List<ItemStack> resourceStackList = new ArrayList<ItemStack>();
protected List<ItemStack> specResourceStackList = new ArrayList<ItemStack>();
protected String[] blockIconNames = new String[]{"","",""};
protected CivicWorkType workType = CivicWorkType.NONE;
protected List<ItemStackWrapperCrafting> recipeResources = new ArrayList<ItemStackWrapperCrafting>();
protected Set<Integer> neededResearch = new HashSet<Integer>();
protected Block blockType = null;
protected int blockMeta = -1;



public Civic(int id)
  {
  this.globalID = id;
  if(civicList[id]==null)
    {
    civicList[id]=this;
    }
  else
    {
    Config.logError("Duplicate Civic attempted to register: "+id);
    }
  }

public Civic setBlockIcons(String bottom, String top, String side)
  {  
  String first = "ancientwarfare:civic/";
  this.blockIconNames = new String[]{first+bottom, first+top, first+side};
  return this;
  }

public Civic addResourceItem(ItemStack filter)
  {
  this.resourceStackList.add(filter);
  return this;
  }

public Civic addSpecResourceItem(ItemStack filter)
  {
  this.specResourceStackList.add(filter);
  return this;
  }

public Civic addRecipeResource(ItemStack stack, boolean dmg)
  {
  this.recipeResources.add(new ItemStackWrapperCrafting(stack, dmg, false));
  return this;
  }

public Civic addNeededResearch(int num)
  {
  this.neededResearch.add(num);
  return this;
  }

public Civic addRecipeResources(ItemStack... stacks)
  {
  for(ItemStack stack : stacks)
    {
    if(stack!=null)
      {
      this.recipeResources.add(new ItemStackWrapperCrafting(stack, false, false));
      }
    }
  return this;
  }

public Civic setInventorySize(int size)
  {
  this.regularInventorySize = size;
  return this;
  }

public Civic setSpecResourceSize(int size)
  {
  this.specResourceSlotSize = size;
  return this;
  }

public Civic setResourceSlotSize(int size)
  {
  this.resourceSlotSize = size;
  return this;
  }

@Override
public int getGlobalID()
  {
  return globalID;
  }

@Override
public String getIconTexture()
  {
  return Statics.texturePath+"blocks/civic/"+itemIconTexture;
  }

@Override
public String getDisplayName()
  {
  return name;
  }

@Override
public String getLocalizedName()
  {
  return StatCollector.translateToLocal(name);
  }

@Override
public String getDisplayTooltip()
  {
  return tooltip;
  }

@Override
public String getLocalizedTooltip()
  {
  return StatCollector.translateToLocal(tooltip);
  }

@Override
public boolean isWorkSite()
  {
  return isWorkSite;
  }

@Override
public boolean isDepository()
  {
  return isDepository;
  }

@Override
public boolean isDwelling()
  {
  return isDwelling;
  }

@Override
public int getMaxWorkers()
  {
  return maxWorkers;
  }

@Override
public Class<? extends TECivic> getTileEntityClass()
  {
  return teClass;
  }

@Override
public ItemStack getItemToConstruct()
  {
  ItemStack item = new ItemStack(ItemLoader.civicPlacer,1, getGlobalID());
  NBTTagCompound tag = new NBTTagCompound();
  item.setTagInfo("civicInfo", tag);
  return item;
  }

@Override
public ItemStack getDisplayItem()
  {
  if(this.displayStackCache==null)
    {    
    this.displayStackCache = this.getItemToConstruct();
    }  
	return this.displayStackCache;  
  }

@Override
public int getTotalInventorySize()
  {
  return regularInventorySize+resourceSlotSize+specResourceSlotSize;
  }

@Override
public int getMaxWorkSizeWidth()
  {
  return this.workSizeMaxHorizontal;
  }

@Override
public int getMaxWorkSizeHeight()
  {
  return this.workSizeMaxHeight;
  }

@Override
public int getMaxWorkAreaCube()
  {
  return this.getMaxWorkSizeWidth()*this.getMaxWorkSizeWidth()*this.getMaxWorkSizeHeight();
  }

@Override
public String[] getIconNames()
  {
  return blockIconNames;
  }

@Override
public CivicWorkType getWorkType()
  {
  return workType;
  }

@Override
public boolean addToCreativeMenu()
  {
  return addToCreative;
  }

@Override
public int getResourceSlotSize()
  {
  return resourceSlotSize;
  }

@Override
public List<ItemStack> getResourceItemFilters()
  {
  return resourceStackList;
  }

@Override
public List<ItemStack> getSpecResourceItemFilters()
  {
  return specResourceStackList;
  }

@Override
public ResourceListRecipe constructRecipe()
  {
  if(!this.addToCreative)
    {
    return null;
    }
  ResourceListRecipe recipe = new ResourceListRecipe(this.getItemToConstruct(), RecipeType.CIVIC);  
  recipe.addNeededResearch(getNeededResearch());
  recipe.addResources(recipeResources);
  return recipe;
  }

@Override
public Collection<Integer> getNeededResearch()
  {
  return this.neededResearch;
  }

@Override
public int getMinWorkSizeWidth1()
  {
  return this.minSize1;
  }

@Override
public int getMinWorkSizeWidth2()
  {
  return this.minSize2;
  }

@Override
public int getMinWorkSizeHeight()
  {
  return this.minHeight;
  }

@Override
public int getSpecResourceSlotSize()
  {
  return this.specResourceSlotSize;
  }

@Override
public int getInventorySize()
  {
  return this.regularInventorySize;
  }

}
