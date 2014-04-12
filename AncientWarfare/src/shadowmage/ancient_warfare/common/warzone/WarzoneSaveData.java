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
package shadowmage.ancient_warfare.common.warzone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class WarzoneSaveData extends WorldSavedData
{
public static final String dataName = "AW_WARZONE_DATA";
private HashMap<Integer, List<Warzone>> warzones = new HashMap<Integer, List<Warzone>>();

public WarzoneSaveData()
  {
  this(dataName);
  }

public WarzoneSaveData(String par1Str)
  {
  super(par1Str);
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  warzones.clear();
  NBTTagList dimList = tag.getTagList("dimList");
  NBTTagList warzoneList;
  NBTTagCompound dimTag;
  NBTTagCompound entryTag;
  int dim;
  Warzone zone;
  for(int i = 0; i < dimList.tagCount(); i++)
    {
    dimTag = (NBTTagCompound) dimList.tagAt(i);
    dim = dimTag.getInteger("dim");
    this.warzones.put(dim, new ArrayList<Warzone>());
    warzoneList = dimTag.getTagList("warzoneList");
    for(int k = 0; k < warzoneList.tagCount(); k++)
      {
      entryTag = (NBTTagCompound) warzoneList.tagAt(k);
      zone = new Warzone();
      zone.readFromNBT(entryTag);
      this.warzones.get(dim).add(zone);
      }    
    } 
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  NBTTagList dimList = new NBTTagList();
  NBTTagList warzoneList;
  NBTTagCompound dimTag;
  NBTTagCompound entryTag;
  for(Integer dim : warzones.keySet())
    {
    dimTag = new NBTTagCompound();
    dimTag.setInteger("dim", dim);
    warzoneList = new NBTTagList();
    for(Warzone zone : warzones.get(dim))
      {
      entryTag = new NBTTagCompound();
      zone.writeToNBT(entryTag);
      warzoneList.appendTag(entryTag);
      }    
    dimTag.setTag("warzoneList", warzoneList);
    dimList.appendTag(dimTag);
    }
  tag.setTag("dimList", dimList);
  }

public boolean isPositionInZone(World world, int x, int y, int z)
  {
  if(!this.warzones.containsKey(world.provider.dimensionId))
    {
    return false;
    }
  List<Warzone> warZones = this.warzones.get(world.provider.dimensionId);
  for(Warzone zone : warZones)
    {
    if(zone.isPositionInZone(x, y, z))
      {
      return true;
      }
    }  
  return false;
  }

public void addNewZone(World world, Warzone zone)
  {  
  if(!this.warzones.containsKey(world.provider.dimensionId))
    {
    this.warzones.put(world.provider.dimensionId, new ArrayList<Warzone>());    
    }
  this.warzones.get(world.provider.dimensionId).add(zone);
  }

public Collection<Warzone> getWarzones(World world)
  {
  return (Collection<Warzone>) (this.warzones.containsKey(world.provider.dimensionId) ? this.warzones.get(world.provider.dimensionId) : Collections.emptyList());
  }

public void removeWarzone(World world, BlockPosition min, BlockPosition max)
  {
  Collection<Warzone> worldZones = this.warzones.get(world.provider.dimensionId);
  if(worldZones==null){return;}
  Iterator<Warzone> it= worldZones.iterator();
  Warzone zone;
  while(it.hasNext())
    {
    zone = it.next();
    if(zone.matches(min, max))
      {
      it.remove();
      break;
      }    
    }
  
  }

}
