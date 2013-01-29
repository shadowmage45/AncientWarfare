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
package shadowmage.ancient_warfare.common.world_gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;

public class GeneratedStructureMap implements INBTTaggable
{
private HashMap<Integer, HashMap<Integer, GeneratedStructureEntry>> generatedStructures = new HashMap<Integer, HashMap<Integer, GeneratedStructureEntry>>();
private List<String> generatedUniques = new ArrayList<String>();

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList xList = new NBTTagList();
  for(Integer x : this.generatedStructures.keySet())
    {
    NBTTagList zList = new NBTTagList();
    for(Integer z : this.generatedStructures.get(x).keySet())
      {
      zList.appendTag(this.generatedStructures.get(x).get(z).getNBTTag());
      }
    xList.appendTag(zList);
    }
  tag.setTag("x", xList);  
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  NBTTagList xList = tag.getTagList("x");
  for(int x = 0; x <xList.tagCount(); x++)
    {
    NBTTagList zList = (NBTTagList) xList.tagAt(x);
    for(int z = 0; z< zList.tagCount(); z++)
      {
      GeneratedStructureEntry ent = new GeneratedStructureEntry();
      ent.readFromNBT((NBTTagCompound)zList.tagAt(z));
      if(!this.generatedStructures.containsKey(x))
        {
        this.generatedStructures.put(x, new HashMap<Integer, GeneratedStructureEntry>());
        }      
      this.generatedStructures.get(x).put(z, ent);
      }
    }
  }

/**
 * 
 * @param x CHUNKX
 * @param z CHUNKZ
 * @return null if none set
 */
private GeneratedStructureEntry getEntryFor(int x, int z)
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
          if(range< this.generatedStructures.get(i).get(j).structureMinDistance)
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

public void setGeneratedAt(int x, int z, int range, String name)
  {
  GeneratedStructureEntry ent = new GeneratedStructureEntry();
  ent.name = name;
  ent.structureMinDistance = range;
  this.setEntry(x, z, ent);
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

/**
 * checks for the closest structure by essentially spiraling outward
 * @param sourceX
 * @param sourceZ
 * @param maxDistance
 * @return
 */
public float getClosestStructureDistance(int sourceX, int sourceZ, int maxDistance)
  {
  int currentDistance = 1;
  for(currentDistance = 1; currentDistance <= maxDistance; currentDistance++)
    {
    int x = sourceX - currentDistance;
    int z = sourceZ - currentDistance; 
    
    for(int i = 0; i < currentDistance*2 + 1; i++)
      {
      if(isStructureAt(x, z))
        {
        int xdiff = getAbsDiff(x, sourceX);
        int zdiff = getAbsDiff(z, sourceZ);
        return MathHelper.sqrt_float(xdiff*xdiff+zdiff*zdiff);
        }
      x++;
      }
    for(int i = 0; i < currentDistance*2 + 1; i++)
      {
      if(isStructureAt(x, z))
        {
        
        }
      z++;
      }
    for(int i = 0; i < currentDistance*2 + 1; i++)
      {
      if(isStructureAt(x, z))
        {
        
        }
      x--;
      }
    for(int i = 0; i < currentDistance*2 + 1; i++)
      {
      if(isStructureAt(x, z))
        {
        
        }
      z--;
      }
    
    
    
    
    
    
    
    
    
    for(int i = 0; i < currentDistance; i++)
      {      
      if(isStructureAt(x-i, z) || isStructureAt(x+i,z))
        {
        return MathHelper.sqrt_float(i*i+currentDistance*currentDistance);
        }
      if(isStructureAt(x, z - i) || isStructureAt(x,z+ i))
        {
        return MathHelper.sqrt_float(i*i+currentDistance*currentDistance);
        }
      }
    x = sourceX + currentDistance;
    z = sourceZ + currentDistance; 
    for(int i = 0; i < currentDistance; i++)
      {      
      if(isStructureAt(x-i, z) || isStructureAt(x+i,z))
        {
        return MathHelper.sqrt_float(i*i+currentDistance*currentDistance);
        }
      if(isStructureAt(x, z - i) || isStructureAt(x,z+ i))
        {
        return MathHelper.sqrt_float(i*i+currentDistance*currentDistance);
        }
      }
    }  
  return -1;
  }

}
