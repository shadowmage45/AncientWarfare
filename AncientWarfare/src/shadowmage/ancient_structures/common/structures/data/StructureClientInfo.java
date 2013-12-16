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
package shadowmage.ancient_structures.common.structures.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.IDPairCount;
import shadowmage.ancient_structures.common.item.ItemCivicBuilder;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;

public class StructureClientInfo
{
public final String name;
public final int xSize;
public final int ySize;
public final int zSize;
public final int xOffset;
public final int yOffset;
public final int zOffset;
public final int maxLeveling;
public final int maxClearing;
public final int levelingBuffer;
public final int clearingBuffer;
public boolean creative = true;
public boolean worldGen = false;
public boolean survival = false;
List<IDPairCount> resourceList = new ArrayList<IDPairCount>();
List<ItemStack> cachedResources = new ArrayList<ItemStack>();

public StructureClientInfo(NBTTagCompound tag)
  {
  this.name = tag.getString("name");
  this.xSize = tag.getShort("x");
  this.ySize = tag.getShort("y");
  this.zSize = tag.getShort("z");
  this.xOffset = tag.getShort("xO");
  this.yOffset = tag.getShort("yO");
  this.zOffset = tag.getShort("zO");
  this.survival = tag.getBoolean("surv");
  this.maxLeveling = tag.getShort("mL");
  this.maxClearing = tag.getShort("mC");
  this.levelingBuffer = tag.getShort("lB");
  this.clearingBuffer = tag.getShort("cB");
  if(tag.hasKey("res"))
    {
    NBTTagList list = tag.getTagList("res");
    NBTTagCompound tag1;
    for(int i = 0; i < list.tagCount(); i++)
      {
      tag1 = (NBTTagCompound) list.tagAt(i);
      this.resourceList.add(new IDPairCount(tag1));
      }
    }
  }

public static NBTTagCompound getClientTag(AWStructure struct)
 {
 
 int leveling = struct.getLevelingMax();
 int levelingB = struct.getLevelingBuffer();
 int clearing = struct.getClearingMax();
 int clearingB = struct.getClearingBuffer(); 
 
 NBTTagCompound structTag = new NBTTagCompound();
 if(struct!=null)
   {
   structTag.setString("name", String.valueOf(struct.name));
   structTag.setShort("x", (short)struct.xSize);
   structTag.setShort("y", (short)struct.ySize);
   structTag.setShort("z", (short)struct.zSize);
   structTag.setShort("xO", (short)struct.xOffset);
   structTag.setShort("yO", (short)struct.verticalOffset);
   structTag.setShort("zO", (short)struct.zOffset);
   structTag.setBoolean("surv", struct.survival);
   structTag.setShort("mL", (short)leveling);
   structTag.setShort("mC",(short) clearing);
   structTag.setShort("lB", (short)levelingB);
   structTag.setShort("cB", (short)clearingB);
   if(struct.survival)
     {
     NBTTagList list = new NBTTagList();
     for(IDPairCount count : struct.getResourceList())
       {
       list.appendTag(count.getTag());
       }     
     structTag.setTag("res", list);
     }
   }
 return structTag;
 }

/**
 * returns a world-coordinate bounding box for a given hitPosition
 * and facing
 * @param hit
 * @param face
 * @return
 */
public AxisAlignedBB getBBForRender(BlockPosition hit, int face)
  {
  StructureBB bb = AWStructure.getBoundingBox(hit, face, xOffset, yOffset, zOffset, xSize+1, ySize+1, zSize+1);  
  return AxisAlignedBB.getBoundingBox(bb.pos1.x, bb.pos1.y+yOffset, bb.pos1.z, bb.pos2.x, bb.pos2.y+yOffset, bb.pos2.z);  
  }

public AxisAlignedBB getLevelingBBForRender(BlockPosition hit, int face)
  {
  StructureBB bb = AWStructure.getLevelingBoundingBox(hit, face, xOffset, yOffset, zOffset, xSize+1, ySize+1, zSize+1, maxLeveling, levelingBuffer);
  return AxisAlignedBB.getBoundingBox(bb.pos1.x, bb.pos1.y+yOffset, bb.pos1.z, bb.pos2.x, bb.pos2.y+yOffset+1, bb.pos2.z);
  }

public AxisAlignedBB getClearingBBForRender(BlockPosition hit, int face)
  {
  StructureBB bb = AWStructure.getClearingBoundinBox(hit, face, xOffset, yOffset, zOffset, xSize+1, ySize+1, zSize+1, maxClearing, clearingBuffer);
  return AxisAlignedBB.getBoundingBox(bb.pos1.x, bb.pos1.y+yOffset, bb.pos1.z, bb.pos2.x, bb.pos2.y+yOffset+1, bb.pos2.z);
  }

public Collection<ItemStack> getResourcesNeeded()
  {
  if(!this.cachedResources.isEmpty())
    {
    return this.cachedResources;
    }
  List<ItemStack> items = new ArrayList<ItemStack>();  
  List<IDPairCount> ids = this.resourceList;
  int left;
  ItemStack stack;
  for(IDPairCount count : ids)
    {
    stack = new ItemStack(count.id, count.count,count.meta);
    items.add(stack);
    }
  this.cachedResources.addAll(items);
  return items;
  }

public ResourceListRecipe constructRecipe()
  {
  if(!this.survival){return null;}
  Collection<ItemStack> stacks = this.getResourcesNeeded();
  ItemStack result = ItemCivicBuilder.getCivicBuilderItem(this.name);
  ResourceListRecipe recipe = new ResourceListRecipe(result, RecipeType.STRUCTURE);
  recipe.addResources(stacks, false, false);
  recipe.setDisplayName(name);
  return recipe;
  }


}
