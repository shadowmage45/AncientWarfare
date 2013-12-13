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
package shadowmage.ancient_structures.common.world_gen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_framework.common.utils.Pair;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;

/**
 * per-world generated structure map....saved with world-data on world-save...
 * @author Shadowmage
 *
 */
public class WorldGenStructureMap implements INBTTaggable
{

private HashMap<Integer, HashMap<Integer, GeneratedStructureEntry>> generatedStructures = new HashMap<Integer, HashMap<Integer, GeneratedStructureEntry>>();
private List<String> generatedUniques = new ArrayList<String>();

/**
 * @param par1Str
 */
public WorldGenStructureMap()
  {
  
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  /**
   * struct:
   * tag-  -mainData
   *   list- xList "x"
   *     tag xTag "x"
   *      int x  pos in map "x"
   *      list zList "z"
   *        tag z pos in map "z"
   *        <also entry tag, z is appended to entrytag> 
   * 
   */
  NBTTagList xList = tag.getTagList("x");
  for(int x = 0; x <xList.tagCount(); x++)
    {
    NBTTagCompound xTag = (NBTTagCompound) xList.tagAt(x);
    int xPos = xTag.getInteger("x");
    int zPos;
    NBTTagList zList = xTag.getTagList("z");
    for(int z = 0; z< zList.tagCount(); z++)
      {      
      NBTTagCompound entTag = (NBTTagCompound) zList.tagAt(z);
      GeneratedStructureEntry ent = new GeneratedStructureEntry(entTag);
      zPos = entTag.getInteger("z");      
      if(!this.generatedStructures.containsKey(xPos))
        {
        this.generatedStructures.put(xPos, new HashMap<Integer, GeneratedStructureEntry>());
        }      
      this.generatedStructures.get(xPos).put(zPos, ent);
      }
    }
  NBTTagList uniqueList = tag.getTagList("uniques");
  for(int i = 0; i < uniqueList.tagCount(); i++)
    {
    this.generatedUniques.add(((NBTTagString)uniqueList.tagAt(i)).data);
    }
  }

/**
 * 
 * @param x CHUNKX
 * @param z CHUNKZ
 * @return null if none set
 */
public GeneratedStructureEntry getEntryFor(int x, int z)
  {
  if(this.generatedStructures.containsKey(x))
    {
    return this.generatedStructures.get(x).get(z);
    }
  return null;
  }

private void setEntry(int x, int z, GeneratedStructureEntry entry)
  {
  if(!this.generatedStructures.containsKey(x))
    {
    this.generatedStructures.put(x, new HashMap<Integer, GeneratedStructureEntry>());
    }
  this.generatedStructures.get(x).put(z, entry);
  }

/**
 * TODO change this to check closest chunks first, expand outward? <might be faster when structures are closer>
 * @param x
 * @param z
 * @param range
 * @return
 */
public boolean canGenerateAt(int x, int z, int range)
  {
  boolean valid = true;
  for(int i = x - range; i <= x+range; i++)
    {
    for(int j = z - range; j <= z+range; z++)
      {
      if(this.generatedStructures.containsKey(i))
        {
        if(this.generatedStructures.get(i).containsKey(j))
          {
          if(range< this.generatedStructures.get(i).get(j).structureValue)
            {
            return false;
            }          
          }
        }
      }
    }
  return true;
  }

private boolean isStructureAt(int x, int z)
  {
  if(!this.generatedStructures.containsKey(x))
    {
    return false;
    }
  return this.generatedStructures.get(x).containsKey(z);
  }

/**
 * world x and worldZ
 * @param x
 * @param z
 * @param value
 * @param name
 */
public void setGeneratedAt(int x, int y, int z, int face, int value, String name, boolean unique)
  {
  int cX = x/16;
  int cZ = z/16;  
  byte oX = (byte) (x %16);
  byte oZ = (byte) (z %16);
  GeneratedStructureEntry ent = new GeneratedStructureEntry(name, oX, (byte)y, oZ, (byte)face, (byte) value);
  ent.name = name;
  ent.structureValue = (byte)value;
  if(unique)
    {
    this.generatedUniques.add(name);
    }
  this.setEntry(cX, cZ, ent);
  }

//TODO move this to a proper mathtools class....
public static int getAbsDiff(int a, int b)
  {
  if(a<b)
    {
    return b-a;
    }
  return a-b;
  }

public float getDistance(int sourceX, int sourceZ, int x, int z)
  {
  int xdiff = getAbsDiff(x, sourceX);
  int zdiff = getAbsDiff(z, sourceZ);
  return MathHelper.sqrt_float(xdiff*xdiff+zdiff*zdiff);
  }

public int getStructureValue(int x, int z)
  {
  if(this.generatedStructures.containsKey(x) && this.generatedStructures.get(x).containsKey(z))
    {
    return this.generatedStructures.get(x).get(z).structureValue;
    }
  return 0;
  }

public float getDistanceAndSetDistCache(int sourceX, int sourceZ, int foundX, int foundZ)
  {
  this.structureDistanceBounds = getStructureValue(foundX, foundZ);
  return getDistance(sourceX,sourceZ,foundX,foundZ);
  }

public int structureDistanceBounds = 0;
/**
 * checks for the closest structure by essentially spiraling outward
 * @param sourceX
 * @param sourceZ
 * @param maxDistance
 * @return
 */
public Pair<Float, Integer> getClosestStructureDistance(int sourceX, int sourceZ, int maxDistance)
  {
  int foundValue = 0;
  float closestStructure = maxDistance;
  int currentDistance = 1;
  for(currentDistance = 1; currentDistance <= maxDistance; currentDistance++)
    {
    int x = sourceX - currentDistance;
    int z = sourceZ + currentDistance;     
    for(int i = 0; i < currentDistance*2 + 1; i++)
      {
      if(isStructureAt(x+i, z))
        {
        float dist = this.getDistance(sourceX, sourceZ, x+i, z);
        if(dist<closestStructure)
          {
          closestStructure = dist;
          }
        if(dist<maxDistance)//circular...
          {
          foundValue += this.generatedStructures.get(x+i).get(z).structureValue;
          }
        }
      if(isStructureAt(x, z-i))
        {
        float dist = this.getDistance(sourceX, sourceZ, x, z-i);
        if(dist<closestStructure)
          {
          closestStructure = dist;
          }
        if(dist<maxDistance)
          {
          foundValue += this.generatedStructures.get(x).get(z-i).structureValue;
          }
        }
      }
    x = sourceX + currentDistance;
    z = sourceZ - currentDistance;
    for(int i = 0; i < currentDistance*2 + 1; i++)
      {
      if(isStructureAt(x-i, z))
        {
        float dist = this.getDistance(sourceX, sourceZ, x-i, z);
        if(dist<closestStructure)
          {
          closestStructure = dist;
          }
        if(dist<maxDistance)
          {
          foundValue += this.generatedStructures.get(x-i).get(z).structureValue;
          }
        }
      if(isStructureAt(x, z+i))
        {
        float dist = this.getDistance(sourceX, sourceZ, x, z+i);
        if(dist<closestStructure)
          {
          closestStructure = dist;
          }
        if(dist<maxDistance)
          {
          foundValue += this.generatedStructures.get(x).get(z+i).structureValue;
          }
        }
      }    
    }  
  return new Pair<Float, Integer>(closestStructure, foundValue);
  }

@Override
public NBTTagCompound getNBTTag()
  {
  /**
   * struct:
   * tag-  -mainData
   *   list- xList
   *     tag xTag
   *      int x  pos in map
   *      list zList
   *        tag z pos in map
   *        <also entry tag, z is appended to entrytag> 
   * 
   */  
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList xList = new NBTTagList();
  for(Integer x : this.generatedStructures.keySet())
    {
    NBTTagCompound xTag = new NBTTagCompound();
    xTag.setInteger("x", x);
    NBTTagList zList = new NBTTagList();
    for(Integer z : this.generatedStructures.get(x).keySet())
      {
      NBTTagCompound entTag = this.generatedStructures.get(x).get(z).getNBTTag();
      if(entTag!=null)
        {
        entTag.setInteger("z", z);
        zList.appendTag(entTag);
        }
      }    
    xTag.setTag("z", zList);
    xList.appendTag(xTag);
    }
  tag.setTag("x", xList);
  
  NBTTagList uniqueList = new NBTTagList();
  NBTTagString string;
  for(String unique : this.generatedUniques)
    {
    string = new NBTTagString(unique, unique);
    uniqueList.appendTag(string);
    }
  tag.setTag("uniques", uniqueList);
  return tag;
  }

public Collection<String> getUniqueStructureList()
  {
  return this.generatedUniques;
  }
}
