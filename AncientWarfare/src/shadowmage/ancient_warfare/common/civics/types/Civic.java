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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemLoader;

public class Civic implements ICivicType
{

public static final Civic[] civicList = new Civic[64];

public static ICivicType wheatFarm = new CivicFarm(0, "Wheat Farm", "A place for workers to tend and harvest Wheat");
public static ICivicType melonFarm = new CivicFarm(1, "Melon Farm", "A place for workers to tend and harvest Melons");
public static ICivicType pumpkinFarm = new CivicFarm(2, "Pumpkin Farm", "A place for workers to tend and harvest Pumpkins");
public static ICivicType cactusFarm = new CivicFarm(3, "Cactus Farm", "A place for workers to tend and harvest Cacti");
public static ICivicType reedFarm = new CivicFarm(4, "Reed Farm", "A place for workers to tend and harvest Reeds");

protected int globalID = 0;
protected String name = "";
protected String tooltip = "";
protected int ranks = 1;
protected int maxWorkers = 1;
protected boolean isWorkSite = false;
protected boolean isDepository = false;
protected boolean isDwelling = false;
protected Class<? extends TECivic> teClass;
protected ItemStack[] displayStackCache = null;
protected int inventorySize = 0;


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
public String getDisplayName(int level)
  {
  return name;
  }

@Override
public String getDisplayTooltip(int level)
  {
  return tooltip;
  }

@Override
public int getNumOfRanks()
  {
  return ranks;
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
public int getMaxWorkers(int rank)
  {
  return maxWorkers;
  }

@Override
public Class<? extends TECivic> getTileEntityClass()
  {
  return teClass;
  }


@Override
public ItemStack getItemToConstruct(int rank)
  {
  ItemStack item = new ItemStack(ItemLoader.civicPlacer,1);
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("type", getGlobalID());
  tag.setInteger("rank", rank);
  item.setTagInfo("civicInfo", tag);
  return item;
  }

@Override
public ItemStack getDisplayItem(int rank)
  {
  if(this.displayStackCache==null)
    {
    this.displayStackCache = new ItemStack[this.getNumOfRanks()];
    for(int i = 0; i < this.getNumOfRanks();i++)
      {
      this.displayStackCache[i] = this.getItemToConstruct(i);
      }
    }  
	if(rank>=0 && rank< this.displayStackCache.length)
	  {
	  return this.displayStackCache[rank];
	  }
	else
	  {
	  return getItemToConstruct(rank);
	  }  
  }

@Override
public int getInventorySize(int level)
  {
  return inventorySize;
  }

}
