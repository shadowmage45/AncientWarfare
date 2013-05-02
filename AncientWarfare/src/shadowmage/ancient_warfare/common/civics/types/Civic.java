/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public Licence.
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

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnChicken;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnCow;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnPig;
import shadowmage.ancient_warfare.common.civics.worksite.te.barn.TEBarnSheep;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmCactus;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmCarrot;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmMelon;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmMushroomBrown;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmMushroomRed;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmNetherStalk;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmPotato;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmPumpkin;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmReed;
import shadowmage.ancient_warfare.common.civics.worksite.te.farm.TEFarmWheat;
import shadowmage.ancient_warfare.common.civics.worksite.te.mine.TEWorkSiteMine;
import shadowmage.ancient_warfare.common.civics.worksite.te.tree.TETreeFarmOak;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemLoader;

public class Civic implements ICivicType
{

public static final Civic[] civicList = new Civic[64];

public static ICivicType wheatFarm = new CivicFarm(0, "Wheat Farm", "A place for workers to tend and harvest Wheat", TEFarmWheat.class, "civicFarmWheat1");
public static ICivicType carrotFarm = new CivicFarm(1, "Carrot Farm", "A place for workers to tend and harvest Carrot", TEFarmCarrot.class, "civicFarmCarrot1");
public static ICivicType potatoFarm = new CivicFarm(2, "Potato Farm", "A place for workers to tend and harvest Potato", TEFarmPotato.class, "civicFarmPotato1");
public static ICivicType melonFarm = new CivicFarm(3, "Melon Farm", "A place for workers to tend and harvest Melon", TEFarmMelon.class, "civicFarmWheat1");
public static ICivicType pumpkinFarm = new CivicFarm(4, "Pumpkin Farm", "A place for workers to tend and harvest Pumpkin", TEFarmPumpkin.class, "civicFarmWheat1");
public static ICivicType cactusFarm = new CivicFarm(5, "Cactus Farm", "A place for workers to tend and harvest Cactus", TEFarmCactus.class, "civicFarmWheat1");
public static ICivicType reedFarm = new CivicFarm(6, "Reed Farm", "A place for workers to tend and harvest Reed", TEFarmReed.class, "civicFarmWheat1");
public static ICivicType mushroomRedFarm = new CivicFarm(7, "Red Mushroom Farm", "A place for workers to tend and harvest Red Mushroom", TEFarmMushroomRed.class, "civicFarmWheat1");
public static ICivicType mushroomBrownFarm = new CivicFarm(8, "Brown Mushroom Farm", "A place for workers to tend and harvest Brown Mushroom", TEFarmMushroomBrown.class, "civicFarmWheat1");
public static ICivicType netherstalkFarm = new CivicFarm(9, "Netherstalk Farm", "A place for workers to tend and harvest Netherstalk", TEFarmNetherStalk.class, "civicFarmWheat1");
public static ICivicType mineBasic = new CivicMine(10, "Basic Layout Mine", "A mine with a basic but less than perfectly efficient layout", TEWorkSiteMine.class);
//mine r2
//mine r3
//mine altr1
//mine altr2
//mine altr3
public static ICivicType treeFarmOak = new CivicTreeFarm(16, "Oak Tree Farm", "Tend to and harvest oak trees", TETreeFarmOak.class);
//spruce
//birch
//jungle
public static ICivicType builder = new CivicBuilder(20);
//town-hall r1
//town-hall r2
//town-hall r3
public static ICivicType pigFarm = new CivicFarm(24, "Pig Farm", "A place for workers to breed and cull Pigs", TEBarnPig.class, "civicFarmWheat1");
public static ICivicType cowFarm = new CivicFarm(25, "Cow Farm", "A place for workers to breed and cull Cows", TEBarnCow.class, "civicFarmWheat1");
public static ICivicType chickenFarm = new CivicFarm(26, "Chicken Farm", "A place for workers to breed and cull Chickens", TEBarnChicken.class, "civicFarmWheat1");
public static ICivicType sheepFarm = new CivicFarm(27, "Sheep Farm", "A place for workers to breed and shear Sheep", TEBarnSheep.class, "civicFarmWheat1");


protected int globalID = 0;
protected String name = "";
protected String tooltip = "";
protected String itemIconTexture = "";
protected int maxWorkers = 1;
protected int workSizeMaxHorizontal = 10;
protected int workSizeMaxHeight = 2;
protected boolean isWorkSite = false;
protected boolean isDepository = false;
protected boolean isDwelling = false;
protected boolean addToCreative = true;
protected Class<? extends TECivic> teClass;
protected ItemStack displayStackCache = null;
protected int inventorySize = 0;
protected String[] blockIconNames = new String[]{"","",""};
protected CivicWorkType workType = CivicWorkType.NONE;

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

@Override
public int getGlobalID()
  {
  return globalID;
  }

@Override
public String getIconTexture()
  {
  return "ancientwarfare:civic/"+itemIconTexture;
  }

@Override
public String getDisplayName()
  {
  return name;
  }

@Override
public String getDisplayTooltip()
  {
  return tooltip;
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
  ItemStack item = new ItemStack(ItemLoader.civicPlacer,1);
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
public int getInventorySize()
  {
  return inventorySize;
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

}
